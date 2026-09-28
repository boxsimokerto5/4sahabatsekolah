package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SchoolDatabase
import com.example.data.model.AcademicCalendarEvent
import com.example.data.model.AcademicReport
import com.example.data.model.AttendanceRecord
import com.example.data.model.ChatMessage
import com.example.data.model.DismissalAlert
import com.example.data.model.ExamSchedule
import com.example.data.model.PickupQueue
import com.example.data.model.SavingTransaction
import com.example.data.model.SchoolActivity
import com.example.data.model.SchoolAnnouncement
import com.example.data.model.Student
import com.example.data.remote.SupabaseClientManager
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class UserRole {
  PARENT, // Bunda / Wali Murid
  TEACHER // Bu Guru / Wali Kelas
}

enum class AppScreen {
  HOME,
  ACADEMIC,
  SAVINGS,
  ATTENDANCE,
  GALLERY,
  CHAT,
  EXAM,
  CALENDAR,
  ANNOUNCEMENT
}

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

  val supabaseManager = SupabaseClientManager(application)
  private val repository: SchoolRepository

  private val _currentRole = MutableStateFlow(UserRole.PARENT)
  val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

  private val _currentScreen = MutableStateFlow(AppScreen.HOME)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _snackbarEvent = MutableStateFlow<String?>(null)
  val snackbarEvent: StateFlow<String?> = _snackbarEvent.asStateFlow()

  private val _isSupabaseConfigured = MutableStateFlow(supabaseManager.isConfigured)
  val isSupabaseConfigured: StateFlow<Boolean> = _isSupabaseConfigured.asStateFlow()

  private val _isSyncing = MutableStateFlow(false)
  val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

  init {
    val db = SchoolDatabase.getDatabase(application)
    repository = SchoolRepository(db.schoolDao(), supabaseManager)
    viewModelScope.launch {
      repository.initializeDefaultDataIfEmpty()
    }
  }

  val student: StateFlow<Student?> = repository.mainStudent
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val dismissalAlert: StateFlow<DismissalAlert?> = repository.latestDismissal
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val pickupQueues: StateFlow<List<PickupQueue>> = repository.pickupQueues
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val academicReports: StateFlow<List<AcademicReport>> = repository.academicReports
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val savingTransactions: StateFlow<List<SavingTransaction>> = repository.savingTransactions
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val totalSavings: StateFlow<Long> = repository.savingTransactions
    .map { list ->
      list.fold(0L) { acc, item ->
        if (item.type == "MASUK") acc + item.amount else acc - item.amount
      }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

  val attendanceRecords: StateFlow<List<AttendanceRecord>> = repository.attendanceRecords
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val activities: StateFlow<List<SchoolActivity>> = repository.schoolActivities
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val examSchedules: StateFlow<List<ExamSchedule>> = repository.examSchedules
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val academicCalendarEvents: StateFlow<List<AcademicCalendarEvent>> = repository.academicCalendarEvents
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val announcements: StateFlow<List<SchoolAnnouncement>> = repository.announcements
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun switchRole(role: UserRole) {
    _currentRole.value = role
    val roleName = if (role == UserRole.PARENT) "Bunda Dina (Wali Murid)" else "Bu Guru Sarah (Wali Kelas)"
    _snackbarEvent.value = "Beralih ke mode: $roleName"
  }

  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun clearSnackbar() {
    _snackbarEvent.value = null
  }

  // Supabase Configuration & Sync
  fun getSupabaseUrl(): String = supabaseManager.supabaseUrl
  fun getSupabaseAnonKey(): String = supabaseManager.supabaseAnonKey
  fun getSqlSchema(): String = SupabaseClientManager.SQL_SCHEMA_SCRIPT

  fun saveSupabaseConfig(url: String, key: String) {
    supabaseManager.supabaseUrl = url
    supabaseManager.supabaseAnonKey = key
    _isSupabaseConfigured.value = supabaseManager.isConfigured
    _snackbarEvent.value = if (supabaseManager.isConfigured) "Konfigurasi Supabase disimpan!" else "Konfigurasi Supabase dikosongkan (Mode Offline)."
  }

  fun testSupabaseConnection(url: String, key: String, onResult: (Boolean, String) -> Unit) {
    viewModelScope.launch {
      supabaseManager.supabaseUrl = url
      supabaseManager.supabaseAnonKey = key
      _isSupabaseConfigured.value = supabaseManager.isConfigured
      val result = supabaseManager.testConnection()
      result.onSuccess { msg ->
        onResult(true, msg)
        _snackbarEvent.value = "⚡ $msg"
        // Try initial sync
        syncSupabase()
      }.onFailure { err ->
        onResult(false, err.message ?: "Koneksi gagal")
        _snackbarEvent.value = "❌ ${err.message}"
      }
    }
  }

  fun syncSupabase() {
    viewModelScope.launch {
      if (!supabaseManager.isConfigured) {
        _snackbarEvent.value = "Supabase belum dikonfigurasi. Masukkan URL & Anon Key di pengaturan."
        return@launch
      }
      _isSyncing.value = true
      val res = repository.syncWithSupabase()
      _isSyncing.value = false
      res.onSuccess {
        _snackbarEvent.value = "☁️ Sinkronisasi Supabase Sukses!"
      }.onFailure {
        _snackbarEvent.value = "⚠️ Sinkronisasi Cloud: ${it.message}"
      }
    }
  }

  // Teacher broadcasts dismissal bell
  fun broadcastDismissal(isDismissed: Boolean, dismissalTime: String, message: String) {
    viewModelScope.launch {
      repository.broadcastDismissal(isDismissed, dismissalTime, message)
      _snackbarEvent.value = if (isDismissed) "🔔 Bel Pulang Sekolah berhasil dibunyikan!" else "Status pemulangan direset."
    }
  }

  // Parent clicks "Saya Sudah Sampai di Depan Gerbang!"
  fun notifyParentArrival(gate: String) {
    viewModelScope.launch {
      repository.notifyParentArrival(gate)
      _snackbarEvent.value = "🚗 Notifikasi terkirim: Anda sudah tiba di $gate! Guru & satpam memanggil Rafa."
    }
  }

  fun updatePickupStatus(id: Long, status: String) {
    viewModelScope.launch {
      repository.updatePickupStatus(id, status)
      _snackbarEvent.value = "Status antrean diperbarui: $status"
    }
  }

  // Add savings
  fun addSavings(amount: Long, type: String, note: String) {
    viewModelScope.launch {
      val now = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
      repository.addSavings(amount, type, note, now)
      _snackbarEvent.value = "Tabungan berhasil dicatat: ${if (type == "MASUK") "+ Rp " else "- Rp "}$amount"
    }
  }

  // Add Academic Report
  fun addAcademicReport(subject: String, score: Int, category: String, feedback: String, badge: String) {
    viewModelScope.launch {
      val now = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
      repository.addAcademicReport(subject, score, category, feedback, badge, now)
      _snackbarEvent.value = "Nilai $subject berhasil ditambahkan untuk Rafa!"
    }
  }

  // Submit Attendance Permission
  fun submitAttendancePermission(status: String, note: String) {
    viewModelScope.launch {
      val now = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
      repository.submitAttendancePermission(status, note, now)
      _snackbarEvent.value = "Pengajuan izin berhasil dikirim ke Bu Guru."
    }
  }

  // Like activity
  fun likeActivity(id: Long) {
    viewModelScope.launch {
      repository.likeActivity(id)
    }
  }

  // Add activity photo/documentation
  fun addActivity(title: String, category: String, description: String, photoType: String) {
    viewModelScope.launch {
      val now = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
      repository.addActivity(title, category, description, now, photoType)
      _snackbarEvent.value = "Momen kegiatan sekolah baru berhasil diunggah!"
    }
  }

  // Send message
  fun sendMessage(text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      val timeFormat = SimpleDateFormat("HH:mm 'WIB'", Locale("id", "ID")).format(Date())
      val role = _currentRole.value
      val senderRoleStr = if (role == UserRole.PARENT) "ORANG_TUA" else "GURU"
      val senderNameStr = if (role == UserRole.PARENT) "Bunda Dina" else "Bu Guru Sarah"
      repository.sendMessage(senderRoleStr, senderNameStr, text.trim(), timeFormat)
    }
  }

  // Academic Calendar & Activity Plan Operations
  fun addCalendarEvent(
    title: String,
    date: String,
    timeRange: String,
    targetClass: String,
    category: String,
    location: String,
    description: String,
    requiredItems: String
  ) {
    viewModelScope.launch {
      val creator = if (_currentRole.value == UserRole.TEACHER) "Bu Sarah, S.Pd (Wali Kelas)" else "Panitia Sekolah"
      repository.addAcademicCalendarEvent(
        title = title,
        date = date,
        timeRange = timeRange,
        targetClass = targetClass,
        category = category,
        location = location,
        description = description,
        requiredItems = requiredItems,
        createdBy = creator
      )
      _snackbarEvent.value = "Agenda kegiatan '$title' berhasil dijadwalkan!"
    }
  }

  fun updateEventRsvp(id: Long, rsvpStatus: String) {
    viewModelScope.launch {
      repository.updateEventRsvp(id, rsvpStatus)
      val msg = if (rsvpStatus == "HADIR") "Konfirmasi tersimpan: Rafa SIAP HADIR/IKUT! ✨" else "Konfirmasi tersimpan: IZIN TIDAK IKUT."
      _snackbarEvent.value = msg
    }
  }

  fun toggleEventCheckedItem(event: AcademicCalendarEvent, item: String) {
    viewModelScope.launch {
      repository.toggleEventCheckedItem(event, item)
    }
  }

  fun deleteCalendarEvent(id: Long) {
    viewModelScope.launch {
      repository.deleteAcademicCalendarEvent(id)
      _snackbarEvent.value = "Agenda kegiatan berhasil dihapus."
    }
  }

  // School Announcement Operations
  fun addAnnouncement(
    title: String,
    letterNumber: String,
    category: String,
    targetAudience: String,
    content: String,
    isPinned: Boolean,
    attachmentTitle: String
  ) {
    viewModelScope.launch {
      val now = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
      val author = if (_currentRole.value == UserRole.TEACHER) "Bu Sarah, S.Pd (Wali Kelas)" else "Kepala Sekolah / Tata Usaha"
      repository.addAnnouncement(
        title = title,
        letterNumber = letterNumber,
        category = category,
        targetAudience = targetAudience,
        content = content,
        author = author,
        date = now,
        isPinned = isPinned,
        attachmentTitle = attachmentTitle
      )
      _snackbarEvent.value = "Pengumuman resmi berhasil diterbitkan ke mading sekolah!"
    }
  }

  fun markAnnouncementAsRead(id: Long, isRead: Boolean) {
    viewModelScope.launch {
      repository.markAnnouncementAsRead(id, isRead)
      _snackbarEvent.value = if (isRead) "Tanda terima tersimpan: Anda telah membaca & memahami pengumuman ini." else "Tanda terima dibatalkan."
    }
  }

  fun deleteAnnouncement(id: Long) {
    viewModelScope.launch {
      repository.deleteAnnouncement(id)
      _snackbarEvent.value = "Pengumuman berhasil dihapus dari mading."
    }
  }
}
