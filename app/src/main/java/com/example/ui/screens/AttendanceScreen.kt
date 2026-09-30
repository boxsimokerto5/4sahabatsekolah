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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Sick
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.AttendanceRecord
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
fun AttendanceScreen(
  records: List<AttendanceRecord>,
  currentRole: UserRole,
  onSubmitPermission: (status: String, note: String) -> Unit,
  onNavigateBack: () -> Unit,
  onTestNotification: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  var showPermissionDialog by remember { mutableStateOf(false) }

  val hadirCount = records.count { it.status == "HADIR" }
  val totalDays = records.size.coerceAtLeast(1)
  val hadirPercent = (hadirCount * 100) / totalDays

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "Absensi & Kehadiran Siswa",
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
          containerColor = PastelSkyLight,
          titleContentColor = Color(0xFF1F2937)
        )
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("attendance_screen_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Monthly Attendance Overview
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = PastelSkyLight),
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
                  text = "PERSENTASE KEHADIRAN BULAN INI",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = PastelSky
                )
                Text(
                  text = "$hadirPercent%",
                  fontSize = 32.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF1F2937)
                )
                Text(
                  text = "Rafa sangat rajin & disiplin hadir pagi ☀️",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF4B5563)
                )
              }

              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(PastelSky),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.EventAvailable,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(32.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action: Pengajuan Izin Sakit / Izin Acara & Tes Notifikasi
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { showPermissionDialog = true },
                modifier = Modifier
                  .weight(1f)
                  .height(44.dp)
                  .testTag("submit_permission_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PastelSky)
              ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (currentRole == UserRole.PARENT) "Izin / Sakit" else "Input Absensi",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }

              onTestNotification?.let { testAction ->
                OutlinedButton(
                  onClick = testAction,
                  modifier = Modifier
                    .height(44.dp)
                    .testTag("test_attendance_notif_button"),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = PastelSky
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Tes Notif",
                    fontSize = 12.sp,
                    color = PastelSky,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }

      // Title
      item {
        Text(
          text = "Riwayat Kehadiran Harian",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1F2937)
        )
      }

      // List
      items(records) { rec ->
        val statusBg = when (rec.status) {
          "HADIR" -> PastelMintLight
          "SAKIT" -> PastelYellowLight
          "IZIN" -> PastelSkyLight
          else -> PastelPeachLight
        }

        val statusColor = when (rec.status) {
          "HADIR" -> PastelMint
          "SAKIT" -> Color(0xFFB45309)
          "IZIN" -> PastelSky
          else -> PastelPeach
        }

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(statusBg),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (rec.status == "HADIR") Icons.Default.CheckCircle else Icons.Default.Sick,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = rec.date,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937)
                )

                Surface(
                  color = statusBg,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = rec.status,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(2.dp))

              Text(
                text = "${rec.time} • ${rec.note}",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
              )
            }
          }
        }
      }
    }
  }

  // Permission Dialog
  if (showPermissionDialog) {
    var selectedStatus by remember { mutableStateOf("IZIN") }
    var reasonNote by remember { mutableStateOf("Ada keperluan keluarga / kontrol dokter") }

    AlertDialog(
      onDismissRequest = { showPermissionDialog = false },
      title = { Text("Pengajuan Izin / Absensi", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text("Pilih Keterangan:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("HADIR", "IZIN", "SAKIT").forEach { st ->
              Surface(
                color = if (selectedStatus == st) PastelSky else PastelSkyLight,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                  .weight(1f)
                  .clickable { selectedStatus = st }
              ) {
                Text(
                  text = st,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (selectedStatus == st) Color.White else PastelSky,
                  modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
              }
            }
          }

          OutlinedTextField(
            value = reasonNote,
            onValueChange = { reasonNote = it },
            label = { Text("Alasan / Catatan Izin") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onSubmitPermission(selectedStatus, reasonNote)
            showPermissionDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PastelSky)
        ) {
          Text("Kirimkan")
        }
      },
      dismissButton = {
        TextButton(onClick = { showPermissionDialog = false }) {
          Text("Batal")
        }
      }
    )
  }
}
