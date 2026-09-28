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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.SchoolProfile
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
fun SuperadminScreen(
  schools: List<SchoolProfile>,
  onApproveSchool: (Long) -> Unit,
  onRejectSchool: (Long, String) -> Unit,
  onToggleSuspension: (Long, String) -> Unit,
  onDeleteSchool: (Long) -> Unit,
  onImpersonateSchool: (SchoolProfile) -> Unit,
  onLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onLogout() }
  val context = LocalContext.current

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("⏳ Antrean Persetujuan", "🏛️ Direktori Kartu Sekolah", "📊 Statistik & Audit")

  var searchQuery by remember { mutableStateOf("") }
  var statusFilter by remember { mutableStateOf("SEMUA") } // SEMUA, PENDING, VERIFIED, REJECTED/SUSPENDED

  // Dialogs
  var rejectingSchoolId by remember { mutableStateOf<Long?>(null) }
  var rejectionReasonInput by remember { mutableStateOf("Dokumen penugasan tidak memenuhi verifikasi resmi dinas/yayasan.") }
  var previewDocumentSchool by remember { mutableStateOf<SchoolProfile?>(null) }

  // Metrics
  val pendingCount = schools.count { it.status == "PENDING" }
  val verifiedCount = schools.count { it.status == "VERIFIED" }
  val suspendedOrRejectedCount = schools.count { it.status == "REJECTED" || it.status == "SUSPENDED" }
  val totalSchools = schools.size

  val totalStudentsEstimated = schools.sumOf { it.totalStudents }
  val totalTeachersEstimated = schools.sumOf { it.totalTeachers }

  // Filtered Schools
  val filteredSchools = schools.filter { school ->
    val matchesSearch = school.schoolName.contains(searchQuery, ignoreCase = true) ||
      school.npsn.contains(searchQuery, ignoreCase = true) ||
      school.city.contains(searchQuery, ignoreCase = true) ||
      school.principalName.contains(searchQuery, ignoreCase = true) ||
      school.applicantName.contains(searchQuery, ignoreCase = true)

    val matchesFilter = when (statusFilter) {
      "PENDING" -> school.status == "PENDING"
      "VERIFIED" -> school.status == "VERIFIED"
      "OTHER" -> school.status == "REJECTED" || school.status == "SUSPENDED"
      else -> true
    }
    matchesSearch && matchesFilter
  }

  val pendingSchools = schools.filter { it.status == "PENDING" }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = Color(0xFF1E1B4B),
              modifier = Modifier.size(34.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  Icons.Default.Security,
                  contentDescription = null,
                  tint = Color(0xFFA5B4FC),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  "Superadmin Control Center 🛡️",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFF1F2937)
                )
              }
              Text(
                "gecckocreator • National Master Console",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6366F1)
              )
            }
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFFEE2E2),
            modifier = Modifier
              .clickable { onLogout() }
              .padding(end = 12.dp)
              .testTag("superadmin_logout_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                "Keluar",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626)
              )
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF8FAFC))
    ) {

      // TOP HERO KPI STATS
      Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          // Banner notification if pending exists
          if (pendingCount > 0) {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFFEF3C7),
              border = BorderStroke(1.dp, Color(0xFFF59E0B)),
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.HourglassTop,
                  contentDescription = null,
                  tint = Color(0xFFD97706),
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Perhatian: Terdapat $pendingCount pendaftaran sekolah baru yang menunggu verifikasi SK & persetujuan Anda!",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF92400E)
                )
              }
            }
          }

          // 4 Metric Badges Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            SuperMetricCard(
              title = "Total Sekolah",
              value = "$totalSchools",
              subtitle = "Terdaftar",
              tintColor = Color(0xFF4F46E5),
              bgColor = Color(0xFFEEF2FF),
              modifier = Modifier.weight(1f)
            )
            SuperMetricCard(
              title = "Menunggu",
              value = "$pendingCount",
              subtitle = "Perlu Review",
              tintColor = Color(0xFFD97706),
              bgColor = Color(0xFFFEF3C7),
              modifier = Modifier.weight(1f)
            )
            SuperMetricCard(
              title = "Terverifikasi",
              value = "$verifiedCount",
              subtitle = "Aktif",
              tintColor = Color(0xFF16A34A),
              bgColor = Color(0xFFDCFCE7),
              modifier = Modifier.weight(1f)
            )
            SuperMetricCard(
              title = "Total Siswa",
              value = "$totalStudentsEstimated",
              subtitle = "Siswa Terhubung",
              tintColor = Color(0xFF0284C7),
              bgColor = Color(0xFFE0F2FE),
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      // TAB NAVIGATION
      ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = Color(0xFF4F46E5),
            height = 3.dp
          )
        },
        divider = { HorizontalDivider(color = Color(0xFFE2E8F0)) }
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = title,
                  fontSize = 12.sp,
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                  color = if (selectedTab == index) Color(0xFF4F46E5) else Color(0xFF64748B)
                )
                if (index == 0 && pendingCount > 0) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    shape = CircleShape,
                    color = Color(0xFFEF4444),
                    modifier = Modifier.size(18.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Text(
                        "$pendingCount",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                      )
                    }
                  }
                }
              }
            }
          )
        }
      }

      // TAB CONTENT
      Box(modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 12.dp)) {
        when (selectedTab) {
          0 -> PendingApprovalsTab(
            pendingSchools = pendingSchools,
            onApprove = onApproveSchool,
            onOpenRejectDialog = { rejectingSchoolId = it },
            onPreviewDocument = { previewDocumentSchool = it }
          )
          1 -> DirectorySchoolsTab(
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            statusFilter = statusFilter,
            onStatusFilterChange = { statusFilter = it },
            schools = filteredSchools,
            onToggleSuspension = onToggleSuspension,
            onDeleteSchool = onDeleteSchool,
            onImpersonateSchool = onImpersonateSchool,
            onPreviewDocument = { previewDocumentSchool = it }
          )
          2 -> AuditAndNetworkTab(
            totalSchools = totalSchools,
            verifiedCount = verifiedCount,
            pendingCount = pendingCount,
            suspendedCount = suspendedOrRejectedCount,
            totalTeachers = totalTeachersEstimated,
            totalStudents = totalStudentsEstimated
          )
        }
      }
    }
  }

  // DIALOG: REJECT SCHOOL REASON
  if (rejectingSchoolId != null) {
    AlertDialog(
      onDismissRequest = { rejectingSchoolId = null },
      title = {
        Text("Tolak Pendaftaran Sekolah", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFDC2626))
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            "Tuliskan catatan alasan penolakan berkas agar pihak pemohon sekolah dapat memperbaiki surat penugasan / SK mereka:",
            fontSize = 12.sp,
            color = Color(0xFF4B5563)
          )
          OutlinedTextField(
            value = rejectionReasonInput,
            onValueChange = { rejectionReasonInput = it },
            label = { Text("Alasan Penolakan") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            rejectingSchoolId?.let { onRejectSchool(it, rejectionReasonInput.trim()) }
            rejectingSchoolId = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
        ) {
          Text("Tolak Pendaftaran", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { rejectingSchoolId = null }) {
          Text("Batal")
        }
      }
    )
  }

  // DIALOG: PREVIEW OFFICIAL LETTER (SK)
  if (previewDocumentSchool != null) {
    val s = previewDocumentSchool!!
    AlertDialog(
      onDismissRequest = { previewDocumentSchool = null },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Pratinjau Berkas Penugasan Resmi", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
      },
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF1F5F9),
            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                "LEMBAR VERIFIKASI DOKUMEN PENUGASAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF334155),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text("Nama Berkas: ${s.assignmentLetterFileName}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
              Text("Lembaga: ${s.schoolName}", fontSize = 11.sp, color = Color(0xFF475569))
              Text("NPSN: ${s.npsn}", fontSize = 11.sp, color = Color(0xFF475569))
              Text("Pemohon (PIC): ${s.applicantName} (${s.applicantRole})", fontSize = 11.sp, color = Color(0xFF475569))
              Text("NIK: ${s.applicantNik}", fontSize = 11.sp, color = Color(0xFF475569))
              Text("WhatsApp: ${s.applicantPhone}", fontSize = 11.sp, color = Color(0xFF475569))

              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFDCFCE7),
                border = BorderStroke(1.dp, Color(0xFF86EFAC))
              ) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    "Stempel Basah Digital & Tanda Tangan Terdaftar SahabatSekolah",
                    fontSize = 10.sp,
                    color = Color(0xFF166534),
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { previewDocumentSchool = null },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
        ) {
          Text("Tutup Berkas", color = Color.White)
        }
      }
    )
  }
}

// ----------------------------------------------------
// TAB 1: PENDING APPROVALS
// ----------------------------------------------------
@Composable
private fun PendingApprovalsTab(
  pendingSchools: List<SchoolProfile>,
  onApprove: (Long) -> Unit,
  onOpenRejectDialog: (Long) -> Unit,
  onPreviewDocument: (SchoolProfile) -> Unit
) {
  if (pendingSchools.isEmpty()) {
    EmptyPendingStateCard()
  } else {
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(14.dp),
      contentPadding = PaddingValues(bottom = 30.dp)
    ) {
      items(pendingSchools, key = { it.id }) { s ->
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = BorderStroke(1.5.dp, Color(0xFFF59E0B)),
          modifier = Modifier.fillMaxWidth().testTag("pending_school_card_${s.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                  shape = CircleShape,
                  color = Color(0xFFFEF3C7),
                  modifier = Modifier.size(36.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                  }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(s.schoolName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
                  Text("NPSN: ${s.npsn} • ${s.level}", fontSize = 11.sp, color = Color(0xFF64748B))
                }
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEF3C7)
              ) {
                Text(
                  "Menunggu Review",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF92400E),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            // Applicant Details
            Text("Informasi Pemohon (PIC):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
            DetailRow(icon = Icons.Default.Person, label = "Nama & Jabatan", value = "${s.applicantName} (${s.applicantRole})")
            DetailRow(icon = Icons.Default.Phone, label = "No. WhatsApp", value = s.applicantPhone)
            DetailRow(icon = Icons.Default.Place, label = "Gedung Sekolah", value = "${s.address}, ${s.city}")
            DetailRow(icon = Icons.Default.Security, label = "User Admin Baru", value = "${s.adminUsername} (Pass: ${s.adminPassword})")

            Spacer(modifier = Modifier.height(10.dp))

            // Document Box & Preview Button
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFF8FAFC),
              border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onPreviewDocument(s) }
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(s.assignmentLetterFileName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                    Text("Klik untuk melihat berkas & stempel SK", fontSize = 9.sp, color = Color.Gray)
                  }
                }
                Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(16.dp))
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Approve vs Reject
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = { onOpenRejectDialog(s.id) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFEF4444)),
                modifier = Modifier.weight(1f)
              ) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tolak Berkas", fontSize = 12.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
              }

              Button(
                onClick = { onApprove(s.id) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                modifier = Modifier.weight(1.3f).testTag("approve_school_${s.id}")
              ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Setujui & Terbitkan Akses", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// TAB 2: DIRECTORY & SCHOOL CARDS
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DirectorySchoolsTab(
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  statusFilter: String,
  onStatusFilterChange: (String) -> Unit,
  schools: List<SchoolProfile>,
  onToggleSuspension: (Long, String) -> Unit,
  onDeleteSchool: (Long) -> Unit,
  onImpersonateSchool: (SchoolProfile) -> Unit,
  onPreviewDocument: (SchoolProfile) -> Unit
) {
  Column {
    // Search Box
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchChange,
      label = { Text("Cari Sekolah / NPSN / Kota / Kepala Sekolah") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B)) },
      trailingIcon = {
        if (searchQuery.isNotBlank()) {
          IconButton(onClick = { onSearchChange("") }) {
            Icon(Icons.Default.Clear, contentDescription = "Hapus")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )

    // Filter Chips Row
    Row(
      modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      FilterChip(
        selected = statusFilter == "SEMUA",
        onClick = { onStatusFilterChange("SEMUA") },
        label = { Text("Semua", fontSize = 11.sp) }
      )
      FilterChip(
        selected = statusFilter == "VERIFIED",
        onClick = { onStatusFilterChange("VERIFIED") },
        label = { Text("Aktif", fontSize = 11.sp) }
      )
      FilterChip(
        selected = statusFilter == "PENDING",
        onClick = { onStatusFilterChange("PENDING") },
        label = { Text("Pending", fontSize = 11.sp) }
      )
      FilterChip(
        selected = statusFilter == "OTHER",
        onClick = { onStatusFilterChange("OTHER") },
        label = { Text("Ditangguhkan/Ditolak", fontSize = 11.sp) }
      )
    }

    if (schools.isEmpty()) {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("Tidak ada sekolah yang cocok dengan pencarian / filter ini.", fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 30.dp)
      ) {
        items(schools, key = { it.id }) { s ->
          SchoolDirectoryCard(
            school = s,
            onToggleSuspension = { onToggleSuspension(s.id, s.status) },
            onDeleteSchool = { onDeleteSchool(s.id) },
            onImpersonateSchool = { onImpersonateSchool(s) },
            onPreviewDocument = { onPreviewDocument(s) }
          )
        }
      }
    }
  }
}

// ----------------------------------------------------
// INDIVIDUAL SCHOOL DIRECTORY CARD
// ----------------------------------------------------
@Composable
private fun SchoolDirectoryCard(
  school: SchoolProfile,
  onToggleSuspension: () -> Unit,
  onDeleteSchool: () -> Unit,
  onImpersonateSchool: () -> Unit,
  onPreviewDocument: () -> Unit
) {
  val statusColor = when (school.status) {
    "VERIFIED" -> Color(0xFF16A34A)
    "PENDING" -> Color(0xFFD97706)
    "SUSPENDED" -> Color(0xFF6B7280)
    else -> Color(0xFFDC2626)
  }

  val statusBg = when (school.status) {
    "VERIFIED" -> Color(0xFFDCFCE7)
    "PENDING" -> Color(0xFFFEF3C7)
    "SUSPENDED" -> Color(0xFFF3F4F6)
    else -> Color(0xFFFEE2E2)
  }

  val statusLabel = when (school.status) {
    "VERIFIED" -> "Aktif (Verified) ✅"
    "PENDING" -> "Menunggu Review ⏳"
    "SUSPENDED" -> "Ditangguhkan ⚪"
    else -> "Ditolak 🔴"
  }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top Row: Name and Status Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
          Surface(
            shape = CircleShape,
            color = Color(0xFFEEF2FF),
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(18.dp))
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(school.schoolName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
            Text("NPSN: ${school.npsn} • ${school.level}", fontSize = 11.sp, color = Color(0xFF64748B))
          }
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = statusBg
        ) {
          Text(
            statusLabel,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = statusColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      HorizontalDivider(color = Color(0xFFF1F5F9))
      Spacer(modifier = Modifier.height(8.dp))

      // Info rows
      DetailRow(icon = Icons.Default.Place, label = "Kota / Alamat", value = "${school.city} (${school.address})")
      DetailRow(icon = Icons.Default.Person, label = "Kepala Sekolah", value = school.principalName)
      DetailRow(icon = Icons.Default.Phone, label = "Kontak PIC", value = "${school.applicantName} - ${school.applicantPhone}")

      // 3 Metrics
      Spacer(modifier = Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MiniStatBadge(label = "Rombel Kelas", value = "${school.totalClassrooms} Ruang", modifier = Modifier.weight(1f))
        MiniStatBadge(label = "Dewan Guru", value = "${school.totalTeachers} Guru", modifier = Modifier.weight(1f))
        MiniStatBadge(label = "Siswa Terdata", value = "${school.totalStudents} Siswa", modifier = Modifier.weight(1f))
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Bottom Action Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          // Impersonate / Open Admin Dashboard
          OutlinedButton(
            onClick = onImpersonateSchool,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Buka Portal", fontSize = 11.sp, color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold)
          }

          // Check SK Document
          OutlinedButton(
            onClick = onPreviewDocument,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Cek SK", fontSize = 11.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          // Suspend / Unsuspend
          if (school.status == "VERIFIED" || school.status == "SUSPENDED") {
            IconButton(onClick = onToggleSuspension) {
              Icon(
                if (school.status == "SUSPENDED") Icons.Default.LockOpen else Icons.Default.Block,
                contentDescription = "Tangguhkan",
                tint = if (school.status == "SUSPENDED") Color(0xFF16A34A) else Color(0xFFD97706),
                modifier = Modifier.size(18.dp)
              )
            }
          }

          // Delete
          IconButton(onClick = onDeleteSchool) {
            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// TAB 3: AUDIT & NETWORK METRICS
// ----------------------------------------------------
@Composable
private fun AuditAndNetworkTab(
  totalSchools: Int,
  verifiedCount: Int,
  pendingCount: Int,
  suspendedCount: Int,
  totalTeachers: Int,
  totalStudents: Int
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 30.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text("Ringkasan Ekosistem SahabatSekolah", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
          Spacer(modifier = Modifier.height(12.dp))

          AuditRow("Total Entitas Sekolah", "$totalSchools Lembaga Terdaftar")
          AuditRow("Sekolah Beroperasi (Aktif)", "$verifiedCount Sekolah Terverifikasi")
          AuditRow("Menunggu Verifikasi Legalitas", "$pendingCount Berkas Menunggu Review")
          AuditRow("Sekolah Ditangguhkan / Ditolak", "$suspendedCount Lembaga")
          AuditRow("Perkiraan Pendidik (Guru)", "$totalTeachers Guru Aktif")
          AuditRow("Perkiraan Peserta Didik (Siswa)", "$totalStudents Siswa Terkoneksi")
          AuditRow("Standar Keamanan Platform", "OAuth2 & Enkripsi Level Bank")
          AuditRow("Audit Terakhir Sistem", "28 September 2026, 17:05 WIB")
        }
      }
    }

    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFEEF2FF),
        border = BorderStroke(1.dp, Color(0xFFC7D2FE)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hak Akses Superadmin Master", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF312E81))
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            "Anda masuk dengan akun root 'gecckocreator'. Akun ini memiliki kewenangan penuh menyetujui, menolak, memantau, dan menghapus entitas sekolah manapun di seluruh Indonesia.",
            fontSize = 11.sp,
            color = Color(0xFF4338CA),
            lineHeight = 16.sp
          )
        }
      }
    }
  }
}

// ----------------------------------------------------
// REUSABLE HELPER COMPONENTS
// ----------------------------------------------------
@Composable
private fun SuperMetricCard(
  title: String,
  value: String,
  subtitle: String,
  tintColor: Color,
  bgColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = tintColor)
      Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
      Text(subtitle, fontSize = 8.sp, color = Color(0xFF64748B))
    }
  }
}

@Composable
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(icon, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
    Spacer(modifier = Modifier.width(6.dp))
    Text("$label: ", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
    Text(value, fontSize = 11.sp, color = Color(0xFF1E293B), maxLines = 1)
  }
}

@Composable
private fun MiniStatBadge(label: String, value: String, modifier: Modifier = Modifier) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = Color(0xFFF8FAFC),
    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
      Text(label, fontSize = 8.sp, color = Color.Gray)
    }
  }
}

@Composable
private fun AuditRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label, fontSize = 12.sp, color = Color(0xFF64748B))
    Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
  }
}

@Composable
private fun EmptyPendingStateCard() {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(48.dp))
      Spacer(modifier = Modifier.height(12.dp))
      Text("Semua Beres! Tidak Ada Antrean", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E293B))
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        "Seluruh berkas pendaftaran sekolah baru telah selesai ditinjau. Sekolah yang baru mendaftar akan otomatis muncul di sini.",
        fontSize = 12.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center
      )
    }
  }
}
