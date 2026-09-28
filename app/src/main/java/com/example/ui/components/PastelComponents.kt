package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DismissalAlert
import com.example.data.model.PickupQueue
import com.example.data.model.Student
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

import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Sync

@Composable
fun AppTopHeader(
  student: Student?,
  currentRole: UserRole,
  isSupabaseConfigured: Boolean,
  isSyncing: Boolean,
  onSwitchRole: (UserRole) -> Unit,
  onOpenSupabaseConfig: () -> Unit,
  onLogout: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val roleGradient = when (currentRole) {
    UserRole.PARENT -> Brush.horizontalGradient(
      listOf(Color(0xFFFF9A8B), Color(0xFFFF6A88), Color(0xFFFF99AC))
    )
    UserRole.TEACHER -> Brush.horizontalGradient(
      listOf(Color(0xFF8E8CD8), Color(0xFF6B63B6), Color(0xFF9B8DF2))
    )
    UserRole.ADMIN -> Brush.horizontalGradient(
      listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF0369A1))
    )
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(roleGradient)
      .padding(horizontal = 16.dp, vertical = 14.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (currentRole == UserRole.PARENT) Icons.Default.School else Icons.Default.Person,
              contentDescription = "Role Icon",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = when (currentRole) {
                UserRole.PARENT -> "SD Ceria Bangsa"
                UserRole.TEACHER -> "Portal Wali Kelas 2-B"
                UserRole.ADMIN -> "Portal Administrator"
              },
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = Color.White.copy(alpha = 0.9f)
            )
            Text(
              text = when (currentRole) {
                UserRole.PARENT -> student?.name ?: "Rafa Al-Ghifari"
                UserRole.TEACHER -> "Bu Sarah, S.Pd"
                UserRole.ADMIN -> "Drs. H. Mulyono (Admin)"
              },
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          // Supabase Cloud button
          Surface(
            shape = RoundedCornerShape(18.dp),
            color = if (isSupabaseConfigured) Color(0xFF10B981).copy(alpha = 0.35f) else Color.White.copy(alpha = 0.22f),
            modifier = Modifier
              .clickable(onClick = onOpenSupabaseConfig)
              .border(1.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
              .testTag("open_supabase_config_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isSyncing) Icons.Default.Sync else if (isSupabaseConfigured) Icons.Default.CloudDone else Icons.Default.CloudQueue,
                contentDescription = "Supabase Status",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (isSyncing) "Syncing" else if (isSupabaseConfigured) "Cloud 🟢" else "Supabase",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          // Switch role pill button
          Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White.copy(alpha = 0.22f),
            modifier = Modifier
              .clickable {
                val nextRole = when (currentRole) {
                  UserRole.PARENT -> UserRole.TEACHER
                  UserRole.TEACHER -> UserRole.ADMIN
                  UserRole.ADMIN -> UserRole.PARENT
                }
                onSwitchRole(nextRole)
              }
              .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
              .testTag("switch_role_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "Ganti Peran",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = when (currentRole) {
                  UserRole.PARENT -> "Bunda"
                  UserRole.TEACHER -> "Guru"
                  UserRole.ADMIN -> "Admin"
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          // Logout button
          if (onLogout != null) {
            Surface(
              shape = RoundedCornerShape(18.dp),
              color = Color.White.copy(alpha = 0.22f),
              modifier = Modifier
                .clickable(onClick = onLogout)
                .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                .testTag("header_logout_button")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.Logout,
                  contentDescription = "Keluar / Logout",
                  tint = Color.White,
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "Keluar",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = if (currentRole == UserRole.PARENT) {
          "${student?.gradeClass ?: "Kelas 2-B"} • ${student?.studentNumber ?: "NISN 00928371"} • Bunda Dina"
        } else {
          "Kelola KBM, Pengumuman Pulang, Tabungan, & Absensi Kelas"
        },
        fontSize = 12.sp,
        color = Color.White.copy(alpha = 0.88f)
      )
    }
  }
}

@Composable
fun DismissalSection(
  currentRole: UserRole,
  dismissalAlert: DismissalAlert?,
  pickupQueues: List<PickupQueue>,
  onBroadcastDismissal: (isDismissed: Boolean, time: String, message: String) -> Unit,
  onNotifyParentArrival: (gate: String) -> Unit,
  onUpdatePickupStatus: (id: Long, status: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showTeacherDialog by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("dismissal_card"),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (dismissalAlert?.isDismissed == true) PastelYellowLight else PastelPeachLight
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(
                if (dismissalAlert?.isDismissed == true) PastelYellow else PastelPeach
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (dismissalAlert?.isDismissed == true) Icons.Default.NotificationsActive else Icons.Default.Campaign,
              contentDescription = "Jam Pulang",
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = if (dismissalAlert?.isDismissed == true) "NOTIFIKASI JAM PULANG" else "STATUS SEKOLAH",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (dismissalAlert?.isDismissed == true) Color(0xFFB45309) else Color(0xFFC53030)
            )
            Text(
              text = dismissalAlert?.title ?: "KBM Masih Berlangsung",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1F2937)
            )
          }
        }

        Surface(
          color = Color.White,
          shape = RoundedCornerShape(12.dp),
          shadowElevation = 1.dp
        ) {
          Text(
            text = dismissalAlert?.dismissalTime ?: "12:30 WIB",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (dismissalAlert?.isDismissed == true) Color(0xFFD97706) else Color(0xFFE53E3E),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = dismissalAlert?.message ?: "Belum ada pengumuman jam pulang untuk hari ini.",
        fontSize = 13.sp,
        color = Color(0xFF374151),
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Action depending on role
      if (currentRole == UserRole.PARENT) {
        // Parent action: "Saya Sudah Sampai di Depan Gerbang!"
        Button(
          onClick = {
            onNotifyParentArrival("Gerbang Utama (Pintu A)")
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("parent_arrival_button"),
          shape = RoundedCornerShape(16.dp),
          colors = ButtonDefaults.buttonColors(containerColor = PastelMint)
        ) {
          Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = "Mobil Penjemputan",
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Saya Sudah di Depan Gerbang!",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }
      } else {
        // Teacher actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              onBroadcastDismissal(
                true,
                "12:30 WIB",
                "Pelajaran hari ini telah selesai! Anak-anak bersiap di lobi dengan wali kelas. Silakan bunda menjemput di gerbang."
              )
            },
            modifier = Modifier
              .weight(1f)
              .height(46.dp)
              .testTag("broadcast_dismissal_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PastelYellow)
          ) {
            Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Bunyikan Bel Pulang", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }

          OutlinedButton(
            onClick = {
              onBroadcastDismissal(
                false,
                "Belum Pulang",
                "KBM sedang berjalan tertib. Jam kepulangan normal pukul 12:30 WIB."
              )
            },
            modifier = Modifier.height(46.dp),
            shape = RoundedCornerShape(14.dp)
          ) {
            Text("Reset", fontSize = 12.sp)
          }
        }
      }

      // Live pickup queue status
      if (pickupQueues.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          color = Color.White.copy(alpha = 0.85f),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = "🚗 Antrean Penjemputan Langsung:",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF4B5563)
            )
            Spacer(modifier = Modifier.height(4.dp))
            pickupQueues.take(2).forEach { queue ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "${queue.parentName} (${queue.gateLocation})",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF1F2937)
                )

                if (currentRole == UserRole.TEACHER) {
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                      color = if (queue.status == "DIPANGGIL") PastelMintLight else PastelSkyLight,
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.clickable {
                        val nextStatus = if (queue.status == "MENUNGGU") "DIPANGGIL" else "SELESAI"
                        onUpdatePickupStatus(queue.id, nextStatus)
                      }
                    ) {
                      Text(
                        text = if (queue.status == "MENUNGGU") "Panggil Siswa" else "Selesai",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (queue.status == "DIPANGGIL") PastelMint else PastelSky,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                      )
                    }
                  }
                } else {
                  Surface(
                    color = if (queue.status == "DIPANGGIL") PastelMintLight else PastelPeachLight,
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text(
                      text = queue.status,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (queue.status == "DIPANGGIL") PastelMint else PastelPeach,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
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

@Composable
fun SummaryStatCard(
  title: String,
  value: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  backgroundColor: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier
      .clickable(onClick = onClick)
      .testTag("summary_card_${title.lowercase()}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = backgroundColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(accentColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = null,
          tint = accentColor.copy(alpha = 0.7f),
          modifier = Modifier.size(16.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF6B7280)
      )

      Text(
        text = value,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1F2937)
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = accentColor,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}
