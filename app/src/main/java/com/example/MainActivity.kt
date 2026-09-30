package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.SchoolViewModel
import com.example.ui.UserRole
import com.example.ui.components.AppTopHeader
import com.example.ui.components.SupabaseConfigDialog
import com.example.ui.screens.AcademicScreen
import com.example.ui.screens.AnnouncementScreen
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ExamScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SavingsScreen
import com.example.ui.screens.SchoolAdminScreen
import com.example.ui.screens.SchoolRegistrationScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SuperadminScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.SoftBackground
import com.example.util.LocalNotificationService

class MainActivity : ComponentActivity() {
  private val currentIntentState = mutableStateOf<Intent?>(null)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    currentIntentState.value = intent
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: SchoolViewModel = viewModel()
        val context = LocalContext.current

        // Handle navigation when launched from a Notification
        val currentIntent = currentIntentState.value
        LaunchedEffect(currentIntent) {
          currentIntent?.getStringExtra(LocalNotificationService.EXTRA_TARGET_SCREEN)?.let { targetScreen ->
            when (targetScreen) {
              LocalNotificationService.SCREEN_ATTENDANCE -> viewModel.navigateTo(AppScreen.ATTENDANCE)
              LocalNotificationService.SCREEN_ANNOUNCEMENT -> viewModel.navigateTo(AppScreen.ANNOUNCEMENT)
            }
          }
        }

        // Request POST_NOTIFICATIONS permission on Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
          ) { isGranted ->
            Log.d("MainActivity", "Notification permission granted: $isGranted")
          }

          LaunchedEffect(Unit) {
            val permissionStatus = ContextCompat.checkSelfPermission(
              context,
              Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
              permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
          }
        }

        SchoolParentApp(viewModel)
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    currentIntentState.value = intent
  }
}

@Composable
fun SchoolParentApp(viewModel: SchoolViewModel = viewModel()) {
  val student by viewModel.student.collectAsStateWithLifecycle()
  val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val dismissalAlert by viewModel.dismissalAlert.collectAsStateWithLifecycle()
  val pickupQueues by viewModel.pickupQueues.collectAsStateWithLifecycle()
  val academicReports by viewModel.academicReports.collectAsStateWithLifecycle()
  val savingTransactions by viewModel.savingTransactions.collectAsStateWithLifecycle()
  val totalSavings by viewModel.totalSavings.collectAsStateWithLifecycle()
  val attendanceRecords by viewModel.attendanceRecords.collectAsStateWithLifecycle()
  val activities by viewModel.activities.collectAsStateWithLifecycle()
  val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
  val examSchedules by viewModel.examSchedules.collectAsStateWithLifecycle()
  val academicCalendarEvents by viewModel.academicCalendarEvents.collectAsStateWithLifecycle()
  val announcements by viewModel.announcements.collectAsStateWithLifecycle()
  val schoolProfile by viewModel.schoolProfile.collectAsStateWithLifecycle()
  val allSchoolProfiles by viewModel.allSchoolProfiles.collectAsStateWithLifecycle()
  val classrooms by viewModel.classrooms.collectAsStateWithLifecycle()
  val teachers by viewModel.teachers.collectAsStateWithLifecycle()
  val parentAccounts by viewModel.parentStudentAccounts.collectAsStateWithLifecycle()
  val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
  val loggedInAccount by viewModel.loggedInAccount.collectAsStateWithLifecycle()
  val snackbarEvent by viewModel.snackbarEvent.collectAsStateWithLifecycle()
  val isSupabaseConfigured by viewModel.isSupabaseConfigured.collectAsStateWithLifecycle()
  val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

  var showSupabaseDialog by remember { mutableStateOf(false) }
  var showLogoutDialog by remember { mutableStateOf(false) }

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(snackbarEvent) {
    snackbarEvent?.let { message ->
      snackbarHostState.showSnackbar(message)
      viewModel.clearSnackbar()
    }
  }

  val showBottomNav = isLoggedIn && currentRole != UserRole.ADMIN && currentRole != UserRole.SUPERADMIN && currentScreen in listOf(
    AppScreen.HOME,
    AppScreen.ACADEMIC,
    AppScreen.SAVINGS,
    AppScreen.ATTENDANCE,
    AppScreen.GALLERY,
    AppScreen.CHAT
  )

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .windowInsetsPadding(WindowInsets.statusBars),
    containerColor = SoftBackground,
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      if (showBottomNav) {
        NavigationBar(
          containerColor = Color.White,
          tonalElevation = 6.dp,
          modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("main_bottom_nav")
        ) {
          NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { viewModel.navigateTo(AppScreen.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
            label = { Text("Beranda", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = PastelPeachLight,
              selectedIconColor = PastelPeach,
              selectedTextColor = PastelPeach
            )
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.ACADEMIC,
            onClick = { viewModel.navigateTo(AppScreen.ACADEMIC) },
            icon = { Icon(Icons.Default.Grade, contentDescription = "Rapor") },
            label = { Text("Rapor", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = PastelPeachLight,
              selectedIconColor = PastelPeach,
              selectedTextColor = PastelPeach
            )
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.SAVINGS,
            onClick = { viewModel.navigateTo(AppScreen.SAVINGS) },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Tabungan") },
            label = { Text("Tabungan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = PastelPeachLight,
              selectedIconColor = PastelPeach,
              selectedTextColor = PastelPeach
            )
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.ATTENDANCE,
            onClick = { viewModel.navigateTo(AppScreen.ATTENDANCE) },
            icon = { Icon(Icons.Default.HowToReg, contentDescription = "Absensi") },
            label = { Text("Absensi", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = PastelPeachLight,
              selectedIconColor = PastelPeach,
              selectedTextColor = PastelPeach
            )
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.GALLERY,
            onClick = { viewModel.navigateTo(AppScreen.GALLERY) },
            icon = { Icon(Icons.Default.Collections, contentDescription = "Galeri") },
            label = { Text("Galeri", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = PastelPeachLight,
              selectedIconColor = PastelPeach,
              selectedTextColor = PastelPeach
            )
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.CHAT,
            onClick = { viewModel.navigateTo(AppScreen.CHAT) },
            icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Pesan") },
            label = { Text("Pesan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              indicatorColor = PastelPeachLight,
              selectedIconColor = PastelPeach,
              selectedTextColor = PastelPeach
            )
          )
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Top header with role switcher (only shown on Home screen to keep sub-screens clean with TopAppBar)
      if (isLoggedIn && currentScreen == AppScreen.HOME) {
        AppTopHeader(
          student = student,
          currentRole = currentRole,
          isSupabaseConfigured = isSupabaseConfigured,
          isSyncing = isSyncing,
          onSwitchRole = { viewModel.switchRole(it) },
          onOpenSupabaseConfig = { showSupabaseDialog = true },
          onLogout = { showLogoutDialog = true }
        )
      }

      if (showLogoutDialog) {
        AlertDialog(
          onDismissRequest = { showLogoutDialog = false },
          title = { Text("Konfirmasi Keluar") },
          text = { Text("Apakah Anda yakin ingin keluar dari akun ini dan kembali ke halaman login?") },
          confirmButton = {
            Button(
              onClick = {
                showLogoutDialog = false
                viewModel.logout()
              },
              colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
            ) {
              Text("Ya, Keluar", color = Color.White)
            }
          },
          dismissButton = {
            TextButton(onClick = { showLogoutDialog = false }) {
              Text("Batal")
            }
          }
        )
      }

      if (showSupabaseDialog) {
        SupabaseConfigDialog(
          initialUrl = viewModel.getSupabaseUrl(),
          initialKey = viewModel.getSupabaseAnonKey(),
          isConfigured = isSupabaseConfigured,
          sqlSchema = viewModel.getSqlSchema(),
          onSave = { url, key -> viewModel.saveSupabaseConfig(url, key) },
          onTestConnection = { url, key, callback ->
            viewModel.testSupabaseConnection(url, key, callback)
          },
          onDismiss = { showSupabaseDialog = false }
        )
      }

      Box(modifier = Modifier.weight(1f)) {
        AnimatedContent(
          targetState = currentScreen,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "ScreenTransition"
        ) { screen ->
          when (screen) {
            AppScreen.SPLASH -> SplashScreen(
              onNavigateToLogin = { viewModel.navigateTo(AppScreen.LOGIN) }
            )

            AppScreen.LOGIN -> LoginScreen(
              onLogin = { username, password -> viewModel.login(username, password) },
              onQuickLoginTeacher = { viewModel.quickLoginAsTeacher() },
              onQuickLoginParent = { viewModel.quickLoginAsParent() },
              onQuickLoginAdmin = { viewModel.quickLoginAsAdmin() },
              onNavigateToRegistration = { viewModel.navigateTo(AppScreen.SCHOOL_REGISTRATION) }
            )

            AppScreen.SCHOOL_REGISTRATION -> SchoolRegistrationScreen(
              onRegisterSuccess = { schoolName, npsn, level, address, city, phone, email, principalName, applicantName, applicantNik, applicantPhone, applicantRole, applicantAddress, letterFileName, adminUsername, adminPassword ->
                viewModel.registerSchool(
                  schoolName, npsn, level, address, city, phone, email, principalName, applicantName, applicantNik, applicantPhone, applicantRole, applicantAddress, letterFileName, adminUsername, adminPassword
                )
              },
              onNavigateToLogin = { viewModel.navigateTo(AppScreen.LOGIN) }
            )

            AppScreen.ADMIN_DASHBOARD -> SchoolAdminScreen(
              profile = schoolProfile,
              classrooms = classrooms,
              teachers = teachers,
              parentAccounts = parentAccounts,
              onUpdateProfile = { viewModel.updateSchoolProfile(it) },
              onAddClassroom = { name, gradeLevel, academicYear, teacherName, maxCap ->
                viewModel.addClassroom(name, gradeLevel, academicYear, teacherName, maxCap)
              },
              onDeleteClassroom = { viewModel.deleteClassroom(it) },
              onAddTeacher = { fullName, nip, phone, assignedClass, username, pass ->
                viewModel.addTeacher(fullName, nip, phone, assignedClass, username, pass)
              },
              onUpdateTeacherAssignedClass = { id, assignedClass ->
                viewModel.updateTeacherAssignedClass(id, assignedClass)
              },
              onDeleteTeacher = { viewModel.deleteTeacher(it) },
              onAddParentStudent = { studentName, nisn, studentClass, parentName, parentPhone, username, pass ->
                viewModel.addParentStudentAccount(studentName, nisn, studentClass, parentName, parentPhone, username, pass)
              },
              onDeleteParentStudent = { viewModel.deleteParentStudentAccount(it) },
              onSwitchToRole = { viewModel.switchRole(it) },
              onLogout = { viewModel.logout() }
            )

            AppScreen.SUPERADMIN_DASHBOARD -> SuperadminScreen(
              schools = allSchoolProfiles,
              onApproveSchool = { viewModel.approveSchoolRegistration(it) },
              onRejectSchool = { id, reason -> viewModel.rejectSchoolRegistration(id, reason) },
              onToggleSuspension = { id, currentStatus -> viewModel.toggleSchoolSuspension(id, currentStatus) },
              onDeleteSchool = { viewModel.deleteSchoolProfile(it) },
              onImpersonateSchool = { viewModel.impersonateSchool(it) },
              onLogout = { viewModel.logout() }
            )

            AppScreen.HOME -> HomeScreen(
              student = student,
              currentRole = currentRole,
              dismissalAlert = dismissalAlert,
              pickupQueues = pickupQueues,
              latestReport = academicReports.firstOrNull(),
              totalSavings = totalSavings,
              latestAttendance = attendanceRecords.firstOrNull(),
              recentActivities = activities,
              calendarEvents = academicCalendarEvents,
              announcements = announcements,
              attendanceRecords = attendanceRecords,
              savingTransactions = savingTransactions,
              onNavigateTo = { viewModel.navigateTo(it) },
              onBroadcastDismissal = { dismissed, time, msg ->
                viewModel.broadcastDismissal(dismissed, time, msg)
              },
              onNotifyParentArrival = { gate ->
                viewModel.notifyParentArrival(gate)
              },
              onUpdatePickupStatus = { id, st ->
                viewModel.updatePickupStatus(id, st)
              },
              onLikeActivity = { id ->
                viewModel.likeActivity(id)
              }
            )

            AppScreen.ACADEMIC -> AcademicScreen(
              reports = academicReports,
              currentRole = currentRole,
              onAddReport = { sub, sc, cat, fb, bd ->
                viewModel.addAcademicReport(sub, sc, cat, fb, bd)
              },
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) }
            )

            AppScreen.SAVINGS -> SavingsScreen(
              transactions = savingTransactions,
              totalBalance = totalSavings,
              currentRole = currentRole,
              onAddSavings = { amt, tp, nt ->
                viewModel.addSavings(amt, tp, nt)
              },
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) }
            )

            AppScreen.ATTENDANCE -> AttendanceScreen(
              records = attendanceRecords,
              currentRole = currentRole,
              onSubmitPermission = { st, nt ->
                viewModel.submitAttendancePermission(st, nt)
              },
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) },
              onTestNotification = { viewModel.triggerTestAttendanceNotification() }
            )

            AppScreen.GALLERY -> GalleryScreen(
              activities = activities,
              currentRole = currentRole,
              onLikeActivity = { viewModel.likeActivity(it) },
              onAddActivity = { tit, cat, desc, pht ->
                viewModel.addActivity(tit, cat, desc, pht)
              },
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) }
            )

            AppScreen.CHAT -> ChatScreen(
              messages = chatMessages,
              currentRole = currentRole,
              onSendMessage = { viewModel.sendMessage(it) },
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) }
            )

            AppScreen.EXAM -> ExamScreen(
              exams = examSchedules,
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) }
            )

            AppScreen.CALENDAR -> CalendarScreen(
              events = academicCalendarEvents,
              currentRole = currentRole,
              onAddEvent = { title, date, timeRange, targetClass, category, location, description, requiredItems ->
                viewModel.addCalendarEvent(title, date, timeRange, targetClass, category, location, description, requiredItems)
              },
              onUpdateRsvp = { id, status ->
                viewModel.updateEventRsvp(id, status)
              },
              onToggleCheckedItem = { event, item ->
                viewModel.toggleEventCheckedItem(event, item)
              },
              onDeleteEvent = { id ->
                viewModel.deleteCalendarEvent(id)
              },
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) }
            )

            AppScreen.ANNOUNCEMENT -> AnnouncementScreen(
              announcements = announcements,
              currentRole = currentRole,
              onAddAnnouncement = { title, letterNumber, category, targetAudience, content, isPinned, attachmentTitle ->
                viewModel.addAnnouncement(title, letterNumber, category, targetAudience, content, isPinned, attachmentTitle)
              },
              onMarkAsRead = { id, isRead ->
                viewModel.markAnnouncementAsRead(id, isRead)
              },
              onDeleteAnnouncement = { id ->
                viewModel.deleteAnnouncement(id)
              },
              onNavigateBack = { viewModel.navigateTo(AppScreen.HOME) },
              onTestNotification = { viewModel.triggerTestAnnouncementNotification() }
            )
          }
        }
      }
    }
  }
}
