package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.SavingTransaction
import com.example.data.model.SchoolAnnouncement
import com.example.data.model.Student
import com.example.ui.AppScreen
import com.example.ui.UserRole
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelMintContainer
import com.example.ui.theme.PastelMintLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachDark
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelPink
import com.example.ui.theme.PastelPinkLight
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelSkyLight
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.PastelYellowLight
import java.text.NumberFormat
import java.util.Locale

/**
 * Composite Section that groups the 3 featured quick-access cards
 * for school announcements, attendance status, and student savings progress.
 */
@Composable
fun DashboardQuickAccessSection(
  student: Student?,
  currentRole: UserRole,
  announcements: List<SchoolAnnouncement>,
  latestAttendance: AttendanceRecord?,
  attendanceRecords: List<AttendanceRecord>,
  totalSavings: Long,
  savingTransactions: List<SavingTransaction>,
  onNavigateTo: (AppScreen) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag("dashboard_quick_access_section"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Section Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Akses Cepat & Layanan Utama 🎒",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1F2937)
        )
        Text(
          text = "Pantau pengumuman, presensi, & tabungan ananda",
          fontSize = 12.sp,
          color = Color(0xFF6B7280)
        )
      }
    }

    // 1. Quick Access Card: School Announcements (Papan Pengumuman & Edaran)
    QuickAccessAnnouncementsCard(
      announcements = announcements,
      currentRole = currentRole,
      onOpenAnnouncements = { onNavigateTo(AppScreen.ANNOUNCEMENT) }
    )

    // 2. Quick Access Card: Attendance Status (Status Presensi & Kehadiran Siswa)
    QuickAccessAttendanceCard(
      latestAttendance = latestAttendance,
      attendanceRecords = attendanceRecords,
      currentRole = currentRole,
      onOpenAttendance = { onNavigateTo(AppScreen.ATTENDANCE) }
    )

    // 3. Quick Access Card: Student Savings Progress (Progres Tabungan Siswa)
    QuickAccessSavingsCard(
      totalSavings = totalSavings,
      savingTransactions = savingTransactions,
      currentRole = currentRole,
      onOpenSavings = { onNavigateTo(AppScreen.SAVINGS) }
    )
  }
}

/**
 * 1. Card for Quick Access to School Announcements
 */
@Composable
fun QuickAccessAnnouncementsCard(
  announcements: List<SchoolAnnouncement>,
  currentRole: UserRole,
  onOpenAnnouncements: () -> Unit,
  modifier: Modifier = Modifier
) {
  val topAnnouncement = announcements.firstOrNull { it.isPinned } ?: announcements.firstOrNull()
  val unreadCount = announcements.count { !it.isReadByParent }

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, PastelPeach.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
      .clickable(onClick = onOpenAnnouncements)
      .testTag("quick_access_announcements_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .animateContentSize()
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Surface(
            shape = CircleShape,
            color = PastelPeachLight,
            modifier = Modifier.size(38.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                Icons.Default.Campaign,
                contentDescription = "Pengumuman Sekolah",
                tint = PastelPeach,
                modifier = Modifier.size(22.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Pengumuman Sekolah 📢",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1F2937)
            )
            Text(
              text = "Surat edaran & info resmi sekolah",
              fontSize = 11.sp,
              color = Color(0xFF6B7280)
            )
          }
        }

        // Unread Badge or Total Badge
        if (currentRole == UserRole.PARENT && unreadCount > 0) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = PastelPinkLight
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFBE185D))
              )
              Text(
                text = "$unreadCount Baru",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBE185D)
              )
            }
          }
        } else {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = PastelPeachLight
          ) {
            Text(
              text = "${announcements.size} Postingan",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = PastelPeachDark,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Announcement Preview Item
      if (topAnnouncement != null) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFFFAFAFA),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Badges row: Category + Pinned + Date
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                if (topAnnouncement.isPinned) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PastelYellowLight
                  ) {
                    Text(
                      text = "📌 Penting",
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFB78103),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                val (catBg, catText, catLabel) = when (topAnnouncement.category) {
                  "URGENT" -> Triple(PastelPinkLight, Color(0xFFBE185D), "🔴 Mendesak")
                  "EDARAN" -> Triple(PastelSkyLight, Color(0xFF0369A1), "📜 Surat Edaran")
                  "KEGIATAN" -> Triple(PastelMintLight, Color(0xFF15803D), "🎉 Kegiatan")
                  else -> Triple(PastelLilacLight, Color(0xFF6D28D9), "📚 Kurikulum")
                }

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = catBg
                ) {
                  Text(
                    text = catLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = catText,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Text(
                text = topAnnouncement.date,
                fontSize = 11.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
              text = topAnnouncement.title,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF1F2937),
              maxLines = 2,
              lineHeight = 20.sp,
              overflow = TextOverflow.Ellipsis
            )

            // Content excerpt preview
            if (topAnnouncement.content.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = topAnnouncement.content,
                fontSize = 12.sp,
                color = Color(0xFF4B5563),
                maxLines = 2,
                lineHeight = 17.sp,
                overflow = TextOverflow.Ellipsis
              )
            }

            // Letter number & Author
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (topAnnouncement.letterNumber.isNotBlank()) {
                  "No: ${topAnnouncement.letterNumber}"
                } else {
                  "Penerbit: ${topAnnouncement.author}"
                },
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
              )

              Text(
                text = "Target: ${topAnnouncement.targetAudience}",
                fontSize = 10.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      } else {
        // Empty placeholder
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "Belum ada pengumuman terbaru hari ini.",
            fontSize = 12.sp,
            color = Color.Gray
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Footer: Quick button to open Mading
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Ketuk untuk membaca surat edaran lengkap",
          fontSize = 11.sp,
          color = Color(0xFF9CA3AF)
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable(onClick = onOpenAnnouncements)
        ) {
          Text(
            text = "Buka Mading",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PastelPeachDark
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = PastelPeachDark,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

/**
 * 2. Card for Quick Access to Attendance Status
 */
@Composable
fun QuickAccessAttendanceCard(
  latestAttendance: AttendanceRecord?,
  attendanceRecords: List<AttendanceRecord>,
  currentRole: UserRole,
  onOpenAttendance: () -> Unit,
  modifier: Modifier = Modifier
) {
  val totalDays = attendanceRecords.size.coerceAtLeast(1)
  val hadirCount = attendanceRecords.count { it.status == "HADIR" }
  val sakitCount = attendanceRecords.count { it.status == "SAKIT" }
  val izinCount = attendanceRecords.count { it.status == "IZIN" }
  val alpaCount = attendanceRecords.count { it.status == "TERLAMBAT" || it.status == "ALPA" }
  val hadirPercent = ((hadirCount.toFloat() / totalDays.toFloat()) * 100).toInt().coerceIn(0, 100)

  val todayStatus = latestAttendance?.status ?: "HADIR"
  val todayTime = latestAttendance?.time ?: "06:55 WIB"
  val todayDate = latestAttendance?.date ?: "Hari ini"

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, PastelSky.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
      .clickable(onClick = onOpenAttendance)
      .testTag("quick_access_attendance_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .animateContentSize()
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Surface(
            shape = CircleShape,
            color = PastelSkyLight,
            modifier = Modifier.size(38.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                Icons.Default.HowToReg,
                contentDescription = "Status Kehadiran",
                tint = PastelSky,
                modifier = Modifier.size(22.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Status Kehadiran Siswa ☀️",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1F2937)
            )
            Text(
              text = "Presensi harian & kedisiplinan kelas",
              fontSize = 11.sp,
              color = Color(0xFF6B7280)
            )
          }
        }

        // Live Date Badge
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = PastelSkyLight
        ) {
          Text(
            text = todayDate,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = PastelSky,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Today's Status Hero Box
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = when (todayStatus) {
          "HADIR" -> Color(0xFFF0FDF4)
          "SAKIT" -> Color(0xFFFFFBEB)
          "IZIN" -> Color(0xFFF5F3FF)
          else -> Color(0xFFFEF2F2)
        },
        modifier = Modifier
          .fillMaxWidth()
          .border(
            1.dp,
            when (todayStatus) {
              "HADIR" -> Color(0xFFBBF7D0)
              "SAKIT" -> Color(0xFFFDE68A)
              "IZIN" -> Color(0xFFDDD6FE)
              else -> Color(0xFFFECACA)
            },
            RoundedCornerShape(16.dp)
          )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(
                when (todayStatus) {
                  "HADIR" -> PastelMint
                  "SAKIT" -> PastelYellow
                  "IZIN" -> PastelLilac
                  else -> Color(0xFFEF4444)
                }
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = when (todayStatus) {
                "HADIR" -> Icons.Default.CheckCircle
                "SAKIT" -> Icons.Default.Healing
                "IZIN" -> Icons.AutoMirrored.Filled.EventNote
                else -> Icons.Default.Schedule
              },
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = when (todayStatus) {
                  "HADIR" -> "Hadir Tepat Waktu"
                  "SAKIT" -> "Izin Sakit"
                  "IZIN" -> "Izin Tidak Masuk"
                  else -> "Terlambat Hadir"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
              )

              Surface(
                shape = RoundedCornerShape(6.dp),
                color = when (todayStatus) {
                  "HADIR" -> Color(0xFFDCFCE7)
                  "SAKIT" -> Color(0xFFFEF3C7)
                  "IZIN" -> Color(0xFFEDE9FE)
                  else -> Color(0xFFFEE2E2)
                }
              ) {
                Text(
                  text = todayTime,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = when (todayStatus) {
                    "HADIR" -> Color(0xFF15803D)
                    "SAKIT" -> Color(0xFFB45309)
                    "IZIN" -> Color(0xFF6D28D9)
                    else -> Color(0xFFB91C1C)
                  },
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = latestAttendance?.note?.takeIf { it.isNotBlank() }
                ?: if (todayStatus == "HADIR") "Suhu tubuh 36.4°C • Siswa dalam kondisi sehat & ceria"
                else "Keterangan telah dicatat oleh wali kelas 2-B",
              fontSize = 11.sp,
              color = Color(0xFF4B5563),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Monthly Attendance Statistics Bar
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF9FAFB), RoundedCornerShape(14.dp))
          .padding(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Persentase Kehadiran Bulan Ini",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151)
          )
          Text(
            text = "$hadirPercent% ($hadirCount/$totalDays Hari)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PastelSky
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
          progress = { (hadirPercent / 100f).coerceIn(0f, 1f) },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = PastelSky,
          trackColor = Color(0xFFE2E8F0)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Stat mini breakdown chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          AttendanceMiniPill(label = "Hadir", count = hadirCount, color = Color(0xFF15803D), bg = Color(0xFFDCFCE7))
          AttendanceMiniPill(label = "Sakit", count = sakitCount, color = Color(0xFFB45309), bg = Color(0xFFFEF3C7))
          AttendanceMiniPill(label = "Izin", count = izinCount, color = Color(0xFF6D28D9), bg = Color(0xFFEDE9FE))
          AttendanceMiniPill(label = "Alpa", count = alpaCount, color = Color(0xFFB91C1C), bg = Color(0xFFFEE2E2))
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Footer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (currentRole == UserRole.PARENT) "Ajukan izin atau lihat riwayat presensi" else "Kelola buku absensi kelas",
          fontSize = 11.sp,
          color = Color(0xFF9CA3AF)
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable(onClick = onOpenAttendance)
        ) {
          Text(
            text = "Riwayat Lengkap",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PastelSky
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = PastelSky,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun AttendanceMiniPill(
  label: String,
  count: Int,
  color: Color,
  bg: Color
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = bg
  ) {
    Text(
      text = "$label: $count",
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      color = color,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
    )
  }
}

/**
 * 3. Card for Quick Access to Student Savings Progress (Celengan Digital)
 */
@Composable
fun QuickAccessSavingsCard(
  totalSavings: Long,
  savingTransactions: List<SavingTransaction>,
  currentRole: UserRole,
  onOpenSavings: () -> Unit,
  modifier: Modifier = Modifier
) {
  val targetSavings = 250000L
  val progress = (totalSavings.toFloat() / targetSavings.toFloat()).coerceIn(0f, 1f)
  val percent = (progress * 100).toInt()

  val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
  val formattedBalance = currencyFormatter.format(totalSavings).replace(",00", "")
  val formattedTarget = currencyFormatter.format(targetSavings).replace(",00", "")
  val remainingAmount = (targetSavings - totalSavings).coerceAtLeast(0L)
  val formattedRemaining = currencyFormatter.format(remainingAmount).replace(",00", "")

  val lastTx = savingTransactions.firstOrNull()

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, PastelMint.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
      .clickable(onClick = onOpenSavings)
      .testTag("quick_access_savings_card")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .animateContentSize()
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Surface(
            shape = CircleShape,
            color = PastelMintLight,
            modifier = Modifier.size(38.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                Icons.Default.Savings,
                contentDescription = "Tabungan Siswa",
                tint = PastelMint,
                modifier = Modifier.size(22.dp)
              )
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Progres Tabungan Siswa 🪙",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1F2937)
            )
            Text(
              text = "Celengan digital & edukasi finansial",
              fontSize = 11.sp,
              color = Color(0xFF6B7280)
            )
          }
        }

        // Percentage Pill
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = PastelMintLight
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              Icons.Default.Stars,
              contentDescription = null,
              tint = PastelMint,
              modifier = Modifier.size(13.dp)
            )
            Text(
              text = "$percent% Terkumpul",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = PastelMint
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Balance & Target Showcase Box
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF7FBF8),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, Color(0xFFDCFCE7), RoundedCornerShape(16.dp))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Column {
              Text(
                text = "TOTAL SALDO SAAT INI",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF15803D),
                letterSpacing = 0.5.sp
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = formattedBalance,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F2937)
              )
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Target Impian",
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
              )
              Text(
                text = formattedTarget,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Progress Bar
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = PastelMint,
            trackColor = Color(0xFFE2E8F0)
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Remaining status caption
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (remainingAmount > 0) {
                "Kurang $formattedRemaining lagi untuk karyawisata 🚀"
              } else {
                "🎉 Target karyawisata tercapai dengan gemilang!"
              },
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = if (remainingAmount > 0) Color(0xFF4B5563) else Color(0xFF15803D)
            )

            Text(
              text = "Target: Karyawisata",
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              color = PastelMint
            )
          }
        }
      }

      // Last Transaction snippet if available
      if (lastTx != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFF9FAFB),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            val isDeposit = lastTx.type == "MASUK"
            Box(
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isDeposit) PastelMintLight else PastelPeachLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isDeposit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = if (isDeposit) PastelMint else PastelPeachDark,
                modifier = Modifier.size(14.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Transaksi Terakhir: ${if (isDeposit) "+" else "-"}${currencyFormatter.format(lastTx.amount).replace(",00", "")} (${lastTx.note})",
              fontSize = 11.sp,
              color = Color(0xFF4B5563),
              fontWeight = FontWeight.Medium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Footer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Program gemar menabung sejak dini",
          fontSize = 11.sp,
          color = Color(0xFF9CA3AF)
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable(onClick = onOpenSavings)
        ) {
          Text(
            text = "Buku Tabungan",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PastelMint
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = PastelMint,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}
