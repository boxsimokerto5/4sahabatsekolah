package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClassroomRoom
import com.example.data.model.ParentStudentAccount
import com.example.data.model.SchoolProfile
import com.example.data.model.TeacherAccount
import com.example.ui.UserRole
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelMintLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelSkyLight
import com.example.ui.theme.PastelYellowLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolAdminScreen(
  profile: SchoolProfile?,
  classrooms: List<ClassroomRoom>,
  teachers: List<TeacherAccount>,
  parentAccounts: List<ParentStudentAccount>,
  onUpdateProfile: (SchoolProfile) -> Unit,
  onAddClassroom: (name: String, gradeLevel: String, academicYear: String, teacherName: String, maxCap: Int) -> Unit,
  onDeleteClassroom: (Long) -> Unit,
  onAddTeacher: (fullName: String, nip: String, phone: String, assignedClass: String, username: String, pass: String) -> Unit,
  onUpdateTeacherAssignedClass: (id: Long, assignedClass: String) -> Unit,
  onDeleteTeacher: (Long) -> Unit,
  onAddParentStudent: (studentName: String, nisn: String, studentClass: String, parentName: String, parentPhone: String, username: String, pass: String) -> Unit,
  onDeleteParentStudent: (Long) -> Unit,
  onSwitchToRole: (UserRole) -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onLogout() }
  val context = LocalContext.current

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("🏢 Profil Sekolah", "🚪 Room Kelas", "👩‍🏫 Akun Guru", "👨‍👩‍👧 Akun Orang Tua")

  // Dialog States
  var showEditProfileDialog by remember { mutableStateOf(false) }
  var showAddClassroomDialog by remember { mutableStateOf(false) }
  var showAddTeacherDialog by remember { mutableStateOf(false) }
  var showAddParentStudentDialog by remember { mutableStateOf(false) }

  val activeSchoolName = profile?.schoolName ?: "SD Ceria Bangsa"
  val activePrincipal = profile?.principalName ?: "Drs. H. Mulyono, M.Pd"

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Panel Admin Lembaga 🏢", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1F2937))
            Text(activeSchoolName, fontSize = 11.sp, color = PastelPeach, fontWeight = FontWeight.Medium)
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = PastelPeachLight,
            modifier = Modifier
              .clickable { onLogout() }
              .padding(end = 12.dp)
              .testTag("admin_logout_action")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Keluar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PastelPeach)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    },
    floatingActionButton = {
      when (selectedTab) {
        1 -> FloatingActionButton(
          onClick = { showAddClassroomDialog = true },
          containerColor = PastelSky,
          contentColor = Color.White,
          shape = CircleShape,
          modifier = Modifier.testTag("add_classroom_fab")
        ) {
          Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Buat Room Kelas", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
        2 -> FloatingActionButton(
          onClick = { showAddTeacherDialog = true },
          containerColor = PastelLilac,
          contentColor = Color.White,
          shape = CircleShape,
          modifier = Modifier.testTag("add_teacher_fab")
        ) {
          Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Buat Akun Guru", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
        3 -> FloatingActionButton(
          onClick = { showAddParentStudentDialog = true },
          containerColor = PastelPeach,
          contentColor = Color.White,
          shape = CircleShape,
          modifier = Modifier.testTag("add_parent_fab")
        ) {
          Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Buat Akun Ortu", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF9FAFB))
    ) {

      // Summary KPI Stat Cards
      Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Admin Profile & Legal Status Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = CircleShape,
                color = PastelPeachLight,
                modifier = Modifier.size(38.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.Business, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(20.dp))
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(activeSchoolName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
                Text("Admin: $activePrincipal", fontSize = 11.sp, color = Color.Gray)
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = PastelMintLight
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("SK Terverifikasi", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 3 Metric Stat Boxes
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            AdminMetricCard(
              title = "Room Kelas",
              value = "${classrooms.size}",
              subtitle = "Rombel Aktif",
              bgColor = PastelSkyLight,
              tintColor = PastelSky,
              modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
              title = "Dewan Guru",
              value = "${teachers.size}",
              subtitle = "Wali Kelas",
              bgColor = PastelLilacLight,
              tintColor = PastelLilac,
              modifier = Modifier.weight(1f)
            )
            AdminMetricCard(
              title = "Siswa & Ortu",
              value = "${parentAccounts.size}",
              subtitle = "Akun Terhubung",
              bgColor = PastelPeachLight,
              tintColor = PastelPeach,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Fast Switch to Parent or Teacher View
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = { onSwitchToRole(UserRole.TEACHER) },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("👀 Lihat Portal Guru", fontSize = 11.sp, color = PastelLilac, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = { onSwitchToRole(UserRole.PARENT) },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("👀 Lihat Portal Ortu", fontSize = 11.sp, color = PastelPeach, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Scrollable Tab Row
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = PastelPeach,
            height = 3.dp
          )
        },
        divider = { HorizontalDivider(color = Color(0xFFE5E7EB)) }
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                color = if (selectedTab == index) PastelPeach else Color(0xFF4B5563)
              )
            }
          )
        }
      }

      // Content for Selected Tab
      Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (selectedTab) {
          0 -> SchoolProfileTabContent(
            profile = profile,
            onOpenEditDialog = { showEditProfileDialog = true }
          )
          1 -> ClassroomsTabContent(
            classrooms = classrooms,
            onDeleteClassroom = onDeleteClassroom
          )
          2 -> TeachersTabContent(
            teachers = teachers,
            classrooms = classrooms,
            onUpdateAssignedClass = onUpdateTeacherAssignedClass,
            onDeleteTeacher = onDeleteTeacher
          )
          3 -> ParentsTabContent(
            parentAccounts = parentAccounts,
            onDeleteAccount = onDeleteParentStudent
          )
        }
      }
    }
  }

  // DIALOGS
  if (showEditProfileDialog && profile != null) {
    EditSchoolProfileDialog(
      currentProfile = profile,
      onDismiss = { showEditProfileDialog = false },
      onSave = { updated ->
        onUpdateProfile(updated)
        showEditProfileDialog = false
      }
    )
  }

  if (showAddClassroomDialog) {
    AddClassroomDialog(
      teachers = teachers,
      onDismiss = { showAddClassroomDialog = false },
      onConfirm = { name, gradeLevel, academicYear, teacherName, maxCap ->
        onAddClassroom(name, gradeLevel, academicYear, teacherName, maxCap)
        showAddClassroomDialog = false
      }
    )
  }

  if (showAddTeacherDialog) {
    AddTeacherDialog(
      classrooms = classrooms,
      onDismiss = { showAddTeacherDialog = false },
      onConfirm = { fullName, nip, phone, assignedClass, username, pass ->
        onAddTeacher(fullName, nip, phone, assignedClass, username, pass)
        showAddTeacherDialog = false
      }
    )
  }

  if (showAddParentStudentDialog) {
    AddParentStudentDialog(
      classrooms = classrooms,
      onDismiss = { showAddParentStudentDialog = false },
      onConfirm = { studentName, nisn, studentClass, parentName, parentPhone, username, pass ->
        onAddParentStudent(studentName, nisn, studentClass, parentName, parentPhone, username, pass)
        showAddParentStudentDialog = false
      }
    )
  }
}

// ----------------------------------------------------
// TAB 1: SCHOOL PROFILE VIEW
// ----------------------------------------------------
@Composable
private fun SchoolProfileTabContent(
  profile: SchoolProfile?,
  onOpenEditDialog: () -> Unit
) {
  val p = profile ?: SchoolProfile(
    schoolName = "SD Ceria Bangsa",
    address = "Jl. Cikini Raya No. 45"
  )

  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(p.schoolName, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Color(0xFF1F2937))
              Text("NPSN: ${p.npsn} • Jenjang: ${p.level}", fontSize = 11.sp, color = Color.Gray)
            }
            IconButton(
              onClick = onOpenEditDialog,
              modifier = Modifier.testTag("edit_school_profile_button")
            ) {
              Icon(Icons.Default.Edit, contentDescription = "Edit Data Sekolah", tint = PastelPeach)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = Color(0xFFF3F4F6))
          Spacer(modifier = Modifier.height(14.dp))

          ProfileInfoRow(icon = Icons.Default.Place, label = "Alamat Gedung", value = "${p.address}, ${p.city}")
          ProfileInfoRow(icon = Icons.Default.Person, label = "Kepala Sekolah", value = p.principalName)
          ProfileInfoRow(icon = Icons.Default.Phone, label = "Telepon Kantor", value = p.phone)
          ProfileInfoRow(icon = Icons.Default.School, label = "Akreditasi", value = p.accreditation)

          Spacer(modifier = Modifier.height(12.dp))

          // Legal Document Box
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF0FDF4),
            border = BorderStroke(1.dp, Color(0xFF86EFAC)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("Dokumen Surat Penugasan / SK Resmi", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF14532D))
                Text(p.assignmentLetterFileName, fontSize = 10.sp, color = Color(0xFF166534))
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = onOpenEditDialog,
            colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Data Sekolah & Kontak", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// TAB 2: CLASSROOMS TAB CONTENT
// ----------------------------------------------------
@Composable
private fun ClassroomsTabContent(
  classrooms: List<ClassroomRoom>,
  onDeleteClassroom: (Long) -> Unit
) {
  if (classrooms.isEmpty()) {
    EmptyStateCard(
      title = "Belum Ada Room Kelas",
      description = "Buat ruangan kelas baru (misal Kelas 1-A, Kelas 2-B) untuk menghubungkan guru dan siswa."
    )
  } else {
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 70.dp)
    ) {
      items(classrooms, key = { it.id }) { room ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = CircleShape,
                color = PastelSkyLight,
                modifier = Modifier.size(42.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = PastelSky, modifier = Modifier.size(20.dp))
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(room.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
                Text("Wali: ${room.homeroomTeacherName}", fontSize = 11.sp, color = PastelLilac, fontWeight = FontWeight.SemiBold)
                Text("Tingkat: ${room.gradeLevel} • Tahun: ${room.academicYear}", fontSize = 10.sp, color = Color.Gray)
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF3F4F6)
              ) {
                Text(
                  text = "${room.studentCount}/${room.maxCapacity} siswa",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF4B5563),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }

              Spacer(modifier = Modifier.width(6.dp))

              IconButton(onClick = { onDeleteClassroom(room.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus Kelas", tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// TAB 3: TEACHERS TAB CONTENT
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeachersTabContent(
  teachers: List<TeacherAccount>,
  classrooms: List<ClassroomRoom>,
  onUpdateAssignedClass: (id: Long, assignedClass: String) -> Unit,
  onDeleteTeacher: (Long) -> Unit
) {
  if (teachers.isEmpty()) {
    EmptyStateCard(
      title = "Belum Ada Akun Guru",
      description = "Tambahkan guru baru untuk mengelola absensi, pembagian tugas kelas, dan bel pulang."
    )
  } else {
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 70.dp)
    ) {
      items(teachers, key = { it.id }) { teacher ->
        var showClassMenu by remember { mutableStateOf(false) }

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = CircleShape,
                  color = PastelLilacLight,
                  modifier = Modifier.size(40.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = PastelLilac, modifier = Modifier.size(20.dp))
                  }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(teacher.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
                  Text("NIP: ${teacher.nip}", fontSize = 10.sp, color = Color.Gray)
                  Text("Username: ${teacher.username} • HP: ${teacher.phone}", fontSize = 10.sp, color = Color(0xFF6B7280))
                }
              }

              IconButton(onClick = { onDeleteTeacher(teacher.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus Guru", tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp))
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Class Assignment Selector
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Penugasan Rombel:", fontSize = 11.sp, color = Color(0xFF4B5563))

              ExposedDropdownMenuBox(
                expanded = showClassMenu,
                onExpandedChange = { showClassMenu = !showClassMenu }
              ) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = PastelLilacLight,
                  modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .clickable { showClassMenu = true }
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(teacher.assignedClass, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PastelLilac)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Edit, contentDescription = null, tint = PastelLilac, modifier = Modifier.size(12.dp))
                  }
                }

                ExposedDropdownMenu(
                  expanded = showClassMenu,
                  onDismissRequest = { showClassMenu = false }
                ) {
                  classrooms.forEach { c ->
                    DropdownMenuItem(
                      text = { Text(c.name, fontSize = 12.sp) },
                      onClick = {
                        onUpdateAssignedClass(teacher.id, c.name)
                        showClassMenu = false
                      }
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// TAB 4: PARENTS & STUDENTS TAB CONTENT
// ----------------------------------------------------
@Composable
private fun ParentsTabContent(
  parentAccounts: List<ParentStudentAccount>,
  onDeleteAccount: (Long) -> Unit
) {
  if (parentAccounts.isEmpty()) {
    EmptyStateCard(
      title = "Belum Ada Akun Orang Tua & Siswa",
      description = "Buat akun login orang tua siswa agar mereka dapat memantau penjemputan, tabungan, dan rapor ananda."
    )
  } else {
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(10.dp),
      contentPadding = PaddingValues(bottom = 70.dp)
    ) {
      items(parentAccounts, key = { it.id }) { acc ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = CircleShape,
                color = PastelPeachLight,
                modifier = Modifier.size(40.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(20.dp))
                }
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text("Siswa: ${acc.studentName}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1F2937))
                Text("Orang Tua: ${acc.parentName} (${acc.parentPhone})", fontSize = 11.sp, color = Color(0xFF4B5563))
                Text("Kelas: ${acc.studentClass} • User: ${acc.username}", fontSize = 10.sp, color = PastelPeach, fontWeight = FontWeight.SemiBold)
              }
            }

            IconButton(onClick = { onDeleteAccount(acc.id) }) {
              Icon(Icons.Default.Delete, contentDescription = "Hapus Akun", tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp))
            }
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// EDIT SCHOOL PROFILE DIALOG
// ----------------------------------------------------
@Composable
private fun EditSchoolProfileDialog(
  currentProfile: SchoolProfile,
  onDismiss: () -> Unit,
  onSave: (SchoolProfile) -> Unit
) {
  var schoolName by remember { mutableStateOf(currentProfile.schoolName) }
  var address by remember { mutableStateOf(currentProfile.address) }
  var city by remember { mutableStateOf(currentProfile.city) }
  var phone by remember { mutableStateOf(currentProfile.phone) }
  var email by remember { mutableStateOf(currentProfile.email) }
  var principalName by remember { mutableStateOf(currentProfile.principalName) }
  var npsn by remember { mutableStateOf(currentProfile.npsn) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Edit Profil & Data Sekolah", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = schoolName,
          onValueChange = { schoolName = it },
          label = { Text("Nama Sekolah") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = address,
          onValueChange = { address = it },
          label = { Text("Alamat Gedung Sekolah") },
          modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Kota/Kabupaten") },
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = npsn,
            onValueChange = { npsn = it },
            label = { Text("NPSN") },
            modifier = Modifier.weight(1f)
          )
        }
        OutlinedTextField(
          value = principalName,
          onValueChange = { principalName = it },
          label = { Text("Kepala Sekolah") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("No. Telepon Sekolah") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("Email Sekolah") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSave(
            currentProfile.copy(
              schoolName = schoolName.trim(),
              address = address.trim(),
              city = city.trim(),
              npsn = npsn.trim(),
              principalName = principalName.trim(),
              phone = phone.trim(),
              email = email.trim()
            )
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
      ) {
        Text("Simpan Perubahan", color = Color.White)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Batal") }
    }
  )
}

// ----------------------------------------------------
// ADD CLASSROOM DIALOG
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddClassroomDialog(
  teachers: List<TeacherAccount>,
  onDismiss: () -> Unit,
  onConfirm: (name: String, gradeLevel: String, academicYear: String, teacherName: String, maxCap: Int) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var gradeLevel by remember { mutableStateOf("Kelas 1") }
  var academicYear by remember { mutableStateOf("2026/2027") }
  var teacherName by remember { mutableStateOf(teachers.firstOrNull()?.fullName ?: "Bu Sarah, S.Pd") }
  var expandedTeacher by remember { mutableStateOf(false) }

  val gradeLevels = listOf("Kelas 1", "Kelas 2", "Kelas 3", "Kelas 4", "Kelas 5", "Kelas 6")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Buat Room Kelas Baru", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
    text = {
      Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Nama Kelas *") },
          placeholder = { Text("Contoh: Kelas 2-A / Kelas 4-B") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = gradeLevel,
          onValueChange = { gradeLevel = it },
          label = { Text("Tingkat Kelas") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = academicYear,
          onValueChange = { academicYear = it },
          label = { Text("Tahun Ajaran") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
          expanded = expandedTeacher,
          onExpandedChange = { expandedTeacher = !expandedTeacher }
        ) {
          OutlinedTextField(
            value = teacherName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Wali Kelas") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTeacher) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
          )
          ExposedDropdownMenu(expanded = expandedTeacher, onDismissRequest = { expandedTeacher = false }) {
            teachers.forEach { t ->
              DropdownMenuItem(
                text = { Text(t.fullName, fontSize = 12.sp) },
                onClick = {
                  teacherName = t.fullName
                  expandedTeacher = false
                }
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.isNotBlank()) {
            onConfirm(name.trim(), gradeLevel.trim(), academicYear.trim(), teacherName.trim(), 30)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelSky)
      ) {
        Text("Buat Kelas", color = Color.White)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Batal") }
    }
  )
}

// ----------------------------------------------------
// ADD TEACHER DIALOG
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTeacherDialog(
  classrooms: List<ClassroomRoom>,
  onDismiss: () -> Unit,
  onConfirm: (fullName: String, nip: String, phone: String, assignedClass: String, username: String, pass: String) -> Unit
) {
  var fullName by remember { mutableStateOf("") }
  var nip by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var assignedClass by remember { mutableStateOf(classrooms.firstOrNull()?.name ?: "Kelas 1-A") }
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("123456") }

  var expandedClass by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Buat Akun Guru Baru", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
    text = {
      Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = fullName,
          onValueChange = { fullName = it },
          label = { Text("Nama Lengkap Guru *") },
          placeholder = { Text("Contoh: Bu Anita, S.Pd") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = nip,
          onValueChange = { nip = it },
          label = { Text("NIP / NUPTK") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Nomor WhatsApp / HP") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
          expanded = expandedClass,
          onExpandedChange = { expandedClass = !expandedClass }
        ) {
          OutlinedTextField(
            value = assignedClass,
            onValueChange = {},
            readOnly = true,
            label = { Text("Penugasan Kelas") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedClass) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
          )
          ExposedDropdownMenu(expanded = expandedClass, onDismissRequest = { expandedClass = false }) {
            classrooms.forEach { c ->
              DropdownMenuItem(
                text = { Text(c.name, fontSize = 12.sp) },
                onClick = {
                  assignedClass = c.name
                  expandedClass = false
                }
              )
            }
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username Login") },
            placeholder = { Text("anita") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (fullName.isNotBlank() && username.isNotBlank()) {
            onConfirm(fullName.trim(), nip.trim(), phone.trim(), assignedClass, username.trim().lowercase(), password.trim())
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelLilac)
      ) {
        Text("Simpan Akun Guru", color = Color.White)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Batal") }
    }
  )
}

// ----------------------------------------------------
// ADD PARENT STUDENT DIALOG
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddParentStudentDialog(
  classrooms: List<ClassroomRoom>,
  onDismiss: () -> Unit,
  onConfirm: (studentName: String, nisn: String, studentClass: String, parentName: String, parentPhone: String, username: String, pass: String) -> Unit
) {
  var studentName by remember { mutableStateOf("") }
  var nisn by remember { mutableStateOf("") }
  var studentClass by remember { mutableStateOf(classrooms.firstOrNull()?.name ?: "Kelas 2-B") }
  var parentName by remember { mutableStateOf("") }
  var parentPhone by remember { mutableStateOf("") }
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("123456") }

  var expandedClass by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Buat Akun Siswa & Orang Tua", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
    text = {
      Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = studentName,
          onValueChange = { studentName = it },
          label = { Text("Nama Siswa *") },
          placeholder = { Text("Contoh: Muhammad Farhan") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = nisn,
          onValueChange = { nisn = it },
          label = { Text("NISN Siswa") },
          placeholder = { Text("00928374") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
          expanded = expandedClass,
          onExpandedChange = { expandedClass = !expandedClass }
        ) {
          OutlinedTextField(
            value = studentClass,
            onValueChange = {},
            readOnly = true,
            label = { Text("Kelas Siswa") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedClass) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
          )
          ExposedDropdownMenu(expanded = expandedClass, onDismissRequest = { expandedClass = false }) {
            classrooms.forEach { c ->
              DropdownMenuItem(
                text = { Text(c.name, fontSize = 12.sp) },
                onClick = {
                  studentClass = c.name
                  expandedClass = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = parentName,
          onValueChange = { parentName = it },
          label = { Text("Nama Orang Tua (Ayah / Ibu) *") },
          placeholder = { Text("Contoh: Ibu Rina") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = parentPhone,
          onValueChange = { parentPhone = it },
          label = { Text("No. WhatsApp Orang Tua") },
          placeholder = { Text("081298...") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username Login") },
            placeholder = { Text("rina") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (studentName.isNotBlank() && parentName.isNotBlank() && username.isNotBlank()) {
            onConfirm(studentName.trim(), nisn.trim(), studentClass, parentName.trim(), parentPhone.trim(), username.trim().lowercase(), password.trim())
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
      ) {
        Text("Simpan Akun Ortu", color = Color.White)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Batal") }
    }
  )
}

// ----------------------------------------------------
// REUSABLE HELPER COMPONENTS
// ----------------------------------------------------
@Composable
private fun AdminMetricCard(
  title: String,
  value: String,
  subtitle: String,
  bgColor: Color,
  tintColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = bgColor,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = tintColor)
      Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
      Text(subtitle, fontSize = 9.sp, color = Color.Gray)
    }
  }
}

@Composable
private fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(icon, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(16.dp))
    Spacer(modifier = Modifier.width(8.dp))
    Text("$label: ", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))
    Text(value, fontSize = 12.sp, color = Color(0xFF4B5563))
  }
}

@Composable
private fun EmptyStateCard(title: String, description: String) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(28.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1F2937))
      Spacer(modifier = Modifier.height(6.dp))
      Text(description, fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
    }
  }
}
