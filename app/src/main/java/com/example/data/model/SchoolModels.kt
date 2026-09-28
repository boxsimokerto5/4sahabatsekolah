package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val gradeClass: String,
  val studentNumber: String,
  val parentName: String,
  val avatarColorHex: String,
  val targetSavings: Long = 250000L
)

@Entity(tableName = "dismissal_alerts")
data class DismissalAlert(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val gradeClass: String,
  val isDismissed: Boolean,
  val dismissalTime: String,
  val title: String,
  val message: String,
  val announcedBy: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "pickup_queues")
data class PickupQueue(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val studentName: String,
  val parentName: String,
  val gateLocation: String,
  val status: String, // "MENUNGGU", "DIPANGGIL", "SELESAI"
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "academic_reports")
data class AcademicReport(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val studentId: Long,
  val subject: String,
  val score: Int,
  val category: String, // "Tugas", "Ulangan Harian", "PTS", "Proyek"
  val teacherFeedback: String,
  val badge: String, // "⭐ Bintang Teliti", "🏆 Juara Kreatif", dll.
  val date: String
)

@Entity(tableName = "saving_transactions")
data class SavingTransaction(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val studentId: Long,
  val amount: Long,
  val type: String, // "MASUK", "KELUAR"
  val note: String,
  val timestamp: Long = System.currentTimeMillis(),
  val formattedDate: String
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val studentId: Long,
  val studentName: String,
  val date: String,
  val status: String, // "HADIR", "SAKIT", "IZIN", "TERLAMBAT"
  val time: String,
  val note: String
)

@Entity(tableName = "school_activities")
data class SchoolActivity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String, // "Kelas", "Prakarya", "Olahraga", "Pentas"
  val date: String,
  val description: String,
  val likes: Int = 12,
  val photoType: String = "hero_banner" // "hero_banner", "art", "reading", "sports"
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val senderRole: String, // "ORANG_TUA", "GURU"
  val senderName: String,
  val text: String,
  val time: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "exam_schedules")
data class ExamSchedule(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val subject: String,
  val examDate: String,
  val timeRange: String,
  val room: String,
  val syllabusSummary: String
)

@Entity(tableName = "academic_calendar_events")
data class AcademicCalendarEvent(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val date: String, // Format: "YYYY-MM-DD" e.g. "2026-10-08"
  val timeRange: String, // e.g. "08:00 - 12:00 WIB"
  val targetClass: String, // e.g. "Semua Kelas" or "Kelas 2-B"
  val category: String, // "FIELD_TRIP", "UJIAN", "PERTEMUAN", "PENTAS_SENI", "LIBUR"
  val location: String, // e.g. "Planetarium TIM Jakarta"
  val description: String,
  val requiredItems: String = "", // Comma-separated list e.g. "Topi Sekolah, Botol Minum, Buku Catatan"
  val checkedItems: String = "", // Comma-separated checked items
  val rsvpStatus: String = "BELUM_KONFIRMASI", // "HADIR", "IZIN", "BELUM_KONFIRMASI"
  val createdBy: String = "Bu Sarah, S.Pd (Wali Kelas 2B)"
)

@Entity(tableName = "school_announcements")
data class SchoolAnnouncement(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val letterNumber: String = "", // e.g. "048/SD-SS/SE/X/2026"
  val category: String, // "URGENT", "EDARAN", "KEGIATAN", "AKADEMIK"
  val targetAudience: String, // "Semua Wali Murid", "Kelas 2-B"
  val content: String,
  val author: String, // "Kepala Sekolah", "Tata Usaha", "Bu Sarah, S.Pd"
  val date: String, // e.g. "28 Sep 2026"
  val isPinned: Boolean = false,
  val isReadByParent: Boolean = false,
  val readCount: Int = 24,
  val totalRecipients: Int = 28,
  val attachmentTitle: String = ""
)

@Entity(tableName = "school_profiles")
data class SchoolProfile(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val schoolName: String,
  val npsn: String = "20104829",
  val level: String = "Sekolah Dasar (SD)",
  val accreditation: String = "A (Unggul)",
  val address: String,
  val city: String = "Jakarta Pusat",
  val phone: String = "021-3908271",
  val email: String = "admin@ceriabangsa.sch.id",
  val principalName: String = "Drs. H. Mulyono, M.Pd",
  val applicantName: String = "Drs. H. Mulyono, M.Pd",
  val applicantNik: String = "3171051203750001",
  val applicantPhone: String = "081289001234",
  val applicantRole: String = "Kepala Sekolah",
  val applicantAddress: String = "Jl. Cikini Raya No. 45, Menteng",
  val assignmentLetterFileName: String = "SK_Penugasan_Kepala_Sekolah_2026.pdf",
  val adminUsername: String = "admin",
  val adminPassword: String = "admin",
  val isVerified: Boolean = true,
  val status: String = "VERIFIED", // "PENDING", "VERIFIED", "REJECTED", "SUSPENDED"
  val rejectionReason: String = "",
  val registeredAt: String = "28 Sep 2026",
  val totalClassrooms: Int = 3,
  val totalTeachers: Int = 3,
  val totalStudents: Int = 86
)

@Entity(tableName = "classroom_rooms")
data class ClassroomRoom(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String, // e.g. "Kelas 2-B"
  val gradeLevel: String = "Kelas 2",
  val academicYear: String = "2026/2027",
  val homeroomTeacherName: String = "Bu Sarah, S.Pd",
  val studentCount: Int = 28,
  val maxCapacity: Int = 30
)

@Entity(tableName = "teacher_accounts")
data class TeacherAccount(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val fullName: String,
  val nip: String,
  val phone: String,
  val assignedClass: String, // e.g. "Kelas 2-B"
  val username: String,
  val password: String = "guru"
)

@Entity(tableName = "parent_student_accounts")
data class ParentStudentAccount(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val studentName: String,
  val nisn: String,
  val studentClass: String, // e.g. "Kelas 2-B"
  val parentName: String,
  val parentPhone: String,
  val username: String,
  val password: String = "ortu"
)
