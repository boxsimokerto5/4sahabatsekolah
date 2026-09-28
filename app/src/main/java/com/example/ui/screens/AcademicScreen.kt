package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicReport
import com.example.ui.UserRole
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelMintLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelSkyLight
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.PastelYellowLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicScreen(
  reports: List<AcademicReport>,
  currentRole: UserRole,
  onAddReport: (String, Int, String, String, String) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  var showAddDialog by remember { mutableStateOf(false) }

  val averageScore = if (reports.isNotEmpty()) {
    reports.map { it.score }.average().toInt()
  } else 0

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "Laporan Akademik & Perkembangan",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = PastelPeachLight,
          titleContentColor = Color(0xFF1F2937)
        )
      )
    },
    floatingActionButton = {
      if (currentRole == UserRole.TEACHER) {
        FloatingActionButton(
          onClick = { showAddDialog = true },
          containerColor = PastelPeach,
          contentColor = Color.White,
          modifier = Modifier.testTag("add_academic_report_fab")
        ) {
          Icon(Icons.Default.Add, contentDescription = "Tambah Nilai")
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("academic_screen_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Summary Card Rata-Rata & Prestasi Bintang
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = PastelPeachLight),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "RATA-RATA NILAI ANANDA",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = PastelPeach
                )
                Text(
                  text = "$averageScore / 100",
                  fontSize = 28.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF1F2937)
                )
                Text(
                  text = "Predikat: Sangat Baik (A) 🌟",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFF4B5563)
                )
              }

              Box(
                modifier = Modifier
                  .size(64.dp)
                  .clip(CircleShape)
                  .background(Color.White),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.EmojiEvents,
                  contentDescription = null,
                  tint = PastelYellow,
                  modifier = Modifier.size(36.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Kudos / Badges Bar
            Text(
              text = "Koleksi Bintang Apresiasi Guru:",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              val badges = reports.map { it.badge }.distinct()
              items(badges) { badge ->
                Surface(
                  color = Color.White,
                  shape = RoundedCornerShape(12.dp),
                  shadowElevation = 1.dp
                ) {
                  Text(
                    text = badge,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Title Section
      item {
        Text(
          text = "Riwayat Penilaian & Catatan Perkembangan",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1F2937)
        )
      }

      // Reports List
      items(reports) { item ->
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Surface(
                  color = PastelLilacLight,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "${item.category} • ${item.date}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PastelLilac,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = item.subject,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937)
                )
              }

              // Score pill
              Surface(
                color = if (item.score >= 90) PastelMintLight else PastelSkyLight,
                shape = RoundedCornerShape(12.dp)
              ) {
                Text(
                  text = "${item.score}",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = if (item.score >= 90) PastelMint else PastelSky,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              color = PastelYellowLight,
              shape = RoundedCornerShape(10.dp)
            ) {
              Text(
                text = item.badge,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Catatan Guru: \"${item.teacherFeedback}\"",
              fontSize = 13.sp,
              color = Color(0xFF4B5563),
              lineHeight = 18.sp
            )
          }
        }
      }
    }
  }

  // Teacher Add Report Dialog
  if (showAddDialog) {
    var subject by remember { mutableStateOf("Matematika") }
    var scoreStr by remember { mutableStateOf("90") }
    var category by remember { mutableStateOf("Ulangan Harian") }
    var feedback by remember { mutableStateOf("Ananda sangat tekun dan fokus!") }
    var badge by remember { mutableStateOf("⭐ Bintang Rajin") }

    val subjectOptions = listOf("Matematika", "Bahasa Indonesia", "IPAS", "SBdP", "Pendidikan Agama", "PJOK")
    val badgeOptions = listOf("⭐ Bintang Rajin", "📚 Bintang Membaca", "🎨 Juara Kreatif", "💡 Pemecah Masalah", "🌟 Sahabat Teladan")

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Input Nilai Siswa (Guru)", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Pilih Mata Pelajaran:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(subjectOptions) { sub ->
              Surface(
                color = if (subject == sub) PastelPeach else PastelPeachLight,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable { subject = sub }
              ) {
                Text(
                  text = sub,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (subject == sub) Color.White else PastelPeach,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          OutlinedTextField(
            value = scoreStr,
            onValueChange = { scoreStr = it },
            label = { Text("Nilai Angka (0-100)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Kategori (Tugas, Ulangan, PTS)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = feedback,
            onValueChange = { feedback = it },
            label = { Text("Catatan Perkembangan Anak") },
            modifier = Modifier.fillMaxWidth()
          )

          Text("Pilih Badge Apresiasi:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(badgeOptions) { b ->
              Surface(
                color = if (badge == b) PastelYellow else PastelYellowLight,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable { badge = b }
              ) {
                Text(
                  text = b,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (badge == b) Color.White else Color(0xFFB45309),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val scoreVal = scoreStr.toIntOrNull() ?: 85
            onAddReport(subject, scoreVal, category, feedback, badge)
            showAddDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
        ) {
          Text("Simpan Nilai")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialog = false }) {
          Text("Batal")
        }
      }
    )
  }
}
