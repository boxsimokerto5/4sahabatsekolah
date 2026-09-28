package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AcademicCalendarEvent
import com.example.data.model.AcademicReport
import com.example.data.model.AttendanceRecord
import com.example.data.model.DismissalAlert
import com.example.data.model.PickupQueue
import com.example.data.model.SchoolActivity
import com.example.data.model.SchoolAnnouncement
import com.example.data.model.Student
import com.example.ui.AppScreen
import com.example.ui.UserRole
import com.example.ui.components.DismissalSection
import com.example.ui.components.SummaryStatCard
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelMintLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelPink
import com.example.ui.theme.PastelPinkLight
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelSkyLight
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.PastelYellowLight
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
  student: Student?,
  currentRole: UserRole,
  dismissalAlert: DismissalAlert?,
  pickupQueues: List<PickupQueue>,
  latestReport: AcademicReport?,
  totalSavings: Long,
  latestAttendance: AttendanceRecord?,
  recentActivities: List<SchoolActivity>,
  calendarEvents: List<AcademicCalendarEvent> = emptyList(),
  announcements: List<SchoolAnnouncement> = emptyList(),
  onNavigateTo: (AppScreen) -> Unit,
  onBroadcastDismissal: (Boolean, String, String) -> Unit,
  onNotifyParentArrival: (String) -> Unit,
  onUpdatePickupStatus: (Long, String) -> Unit,
  onLikeActivity: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
  val formattedSavings = currencyFormatter.format(totalSavings).replace(",00", "")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen_content"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Banner with illustration
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .testTag("hero_banner_card"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
        ) {
          // Hero illustration
          Image(
            painter = painterResource(id = R.drawable.hero_school_1790519776497),
            contentDescription = "Suasana Belajar Ceria di Sekolah",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Soft gradient overlay for text readability
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Transparent,
                    Color(0xCC302A4A)
                  )
                )
              )
          )

          // Content overlay
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(16.dp)
          ) {
            Surface(
              color = PastelPeach,
              shape = RoundedCornerShape(10.dp)
            ) {
              Text(
                text = if (currentRole == UserRole.PARENT) "Selamat Datang Bunda Dina! ✨" else "Ruang Kerja Guru ✨",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (currentRole == UserRole.PARENT) {
                "Pantau tumbuh kembang ceria Ananda Rafa hari ini"
              } else {
                "Kelas 2-B: 28 Siswa • Pembelajaran Hari Ini Berjalan Ceria"
              },
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }

    // Dismissal Bell & Live Pickup Section
    item {
      DismissalSection(
        currentRole = currentRole,
        dismissalAlert = dismissalAlert,
        pickupQueues = pickupQueues,
        onBroadcastDismissal = onBroadcastDismissal,
        onNotifyParentArrival = onNotifyParentArrival,
        onUpdatePickupStatus = onUpdatePickupStatus
      )
    }

    // Official School Announcement Banner (Papan Pengumuman & Mading Sekolah)
    item {
      val topAnnouncement = announcements.firstOrNull { it.isPinned } ?: announcements.firstOrNull()
      val unreadCount = announcements.count { !it.isReadByParent }

      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateTo(AppScreen.ANNOUNCEMENT) }
          .testTag("home_announcement_banner_card")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = CircleShape,
                color = PastelPeachLight,
                modifier = Modifier.size(34.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    Icons.Default.Campaign,
                    contentDescription = null,
                    tint = PastelPeach,
                    modifier = Modifier.size(19.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Pengumuman Resmi Sekolah 📢",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937)
                )
                Text(
                  text = "Surat edaran & informasi penting sekolah",
                  fontSize = 11.sp,
                  color = Color.Gray
                )
              }
            }

            if (currentRole == UserRole.PARENT && unreadCount > 0) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = PastelPinkLight
              ) {
                Text(
                  text = "$unreadCount Baru",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFBE185D),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            } else {
              Text(
                text = "Mading →",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PastelPeach
              )
            }
          }

          if (topAnnouncement != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = Color(0xFFF9FAFB),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
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

                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = when (topAnnouncement.category) {
                        "URGENT" -> PastelPinkLight
                        "EDARAN" -> PastelSkyLight
                        "KEGIATAN" -> PastelMintLight
                        else -> PastelLilacLight
                      }
                    ) {
                      Text(
                        text = when (topAnnouncement.category) {
                          "URGENT" -> "🔴 Urgent"
                          "EDARAN" -> "📜 Surat Edaran"
                          "KEGIATAN" -> "🎉 Kegiatan"
                          else -> "📚 Akademik"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (topAnnouncement.category) {
                          "URGENT" -> Color(0xFFBE185D)
                          "EDARAN" -> Color(0xFF0369A1)
                          "KEGIATAN" -> Color(0xFF15803D)
                          else -> Color(0xFF6D28D9)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }

                  Text(
                    text = topAnnouncement.date,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = topAnnouncement.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = Color(0xFF1F2937),
                  maxLines = 2,
                  lineHeight = 18.sp
                )

                if (topAnnouncement.letterNumber.isNotBlank()) {
                  Spacer(modifier = Modifier.height(3.dp))
                  Text(
                    text = "No: ${topAnnouncement.letterNumber} • Oleh ${topAnnouncement.author}",
                    fontSize = 10.sp,
                    color = Color(0xFF6B7280)
                  )
                }
              }
            }
          }
        }
      }
    }

    // Quick Stats Overview (2x2 Grid)
    item {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = "Ringkasan Ananda Hari Ini 🌸",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1F2937)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          SummaryStatCard(
            title = "Nilai Terbaru",
            value = latestReport?.let { "${it.score}/100" } ?: "95/100",
            subtitle = latestReport?.let { "${it.subject} (${it.badge})" } ?: "Matematika",
            icon = Icons.Default.Grade,
            accentColor = PastelPeach,
            backgroundColor = PastelPeachLight,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppScreen.ACADEMIC) }
          )

          SummaryStatCard(
            title = "Tabungan Siswa",
            value = formattedSavings,
            subtitle = "Target: Rp 250rb (66%)",
            icon = Icons.Default.AccountBalanceWallet,
            accentColor = PastelMint,
            backgroundColor = PastelMintLight,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppScreen.SAVINGS) }
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          SummaryStatCard(
            title = "Absensi Harian",
            value = latestAttendance?.status ?: "HADIR",
            subtitle = latestAttendance?.time ?: "06:55 WIB",
            icon = Icons.Default.HowToReg,
            accentColor = PastelSky,
            backgroundColor = PastelSkyLight,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppScreen.ATTENDANCE) }
          )

          SummaryStatCard(
            title = "Kalender Kegiatan",
            value = if (calendarEvents.isNotEmpty()) "8 Okt" else "Kalender",
            subtitle = if (calendarEvents.isNotEmpty()) calendarEvents.first().title.take(18) + "..." else "Lihat Semua",
            icon = Icons.Default.CalendarMonth,
            accentColor = PastelLilac,
            backgroundColor = PastelLilacLight,
            modifier = Modifier.weight(1f),
            onClick = { onNavigateTo(AppScreen.CALENDAR) }
          )
        }
      }
    }

    // Upcoming Activity Banner Card (Usulan Kalender Terpadu Guru & Orang Tua)
    item {
      val nextEvent = calendarEvents.firstOrNull()
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateTo(AppScreen.CALENDAR) }
          .testTag("upcoming_calendar_event_card")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                shape = CircleShape,
                color = PastelLilacLight,
                modifier = Modifier.size(32.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = PastelLilac,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Rencana Kegiatan Terdekat 📅",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937)
                )
                Text(
                  text = "Agenda terpadu guru & wali murid",
                  fontSize = 11.sp,
                  color = Color.Gray
                )
              }
            }

            Text(
              text = "Buka Kalender →",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = PastelPeach
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          if (nextEvent != null) {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = Color(0xFFF9FAFB),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PastelMintLight
                  ) {
                    Text(
                      text = "🚌 " + nextEvent.targetClass,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF1B5E20),
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                  }
                  Text(
                    text = "${nextEvent.date} • ${nextEvent.timeRange}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4B5563)
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = nextEvent.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFF1F2937)
                )

                if (nextEvent.location.isNotBlank()) {
                  Text(
                    text = "📍 " + nextEvent.location,
                    fontSize = 11.sp,
                    color = Color.Gray
                  )
                }

                if (nextEvent.requiredItems.isNotBlank()) {
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "🎒 Wajib Disiapkan: ${nextEvent.requiredItems}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    maxLines = 1
                  )
                }
              }
            }
          }
        }
      }
    }

    // Navigation Feature Menu Shortcuts
    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Menu Fitur Utama 🎒",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            QuickMenuItem(
              icon = Icons.Default.Campaign,
              label = "Mading",
              bgColor = PastelPinkLight,
              tintColor = Color(0xFFBE185D),
              onClick = { onNavigateTo(AppScreen.ANNOUNCEMENT) }
            )
            QuickMenuItem(
              icon = Icons.Default.CalendarMonth,
              label = "Kalender",
              bgColor = PastelYellowLight,
              tintColor = Color(0xFFB78103),
              onClick = { onNavigateTo(AppScreen.CALENDAR) }
            )
            QuickMenuItem(
              icon = Icons.Default.Grade,
              label = "Rapor",
              bgColor = PastelPeachLight,
              tintColor = PastelPeach,
              onClick = { onNavigateTo(AppScreen.ACADEMIC) }
            )
            QuickMenuItem(
              icon = Icons.Default.AccountBalanceWallet,
              label = "Tabungan",
              bgColor = PastelMintLight,
              tintColor = PastelMint,
              onClick = { onNavigateTo(AppScreen.SAVINGS) }
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            QuickMenuItem(
              icon = Icons.Default.HowToReg,
              label = "Absensi",
              bgColor = PastelSkyLight,
              tintColor = PastelSky,
              onClick = { onNavigateTo(AppScreen.ATTENDANCE) }
            )
            QuickMenuItem(
              icon = Icons.Default.Collections,
              label = "Galeri",
              bgColor = PastelPinkLight,
              tintColor = PastelPink,
              onClick = { onNavigateTo(AppScreen.GALLERY) }
            )
            QuickMenuItem(
              icon = Icons.AutoMirrored.Filled.Chat,
              label = "Pesan",
              bgColor = PastelLilacLight,
              tintColor = PastelLilac,
              onClick = { onNavigateTo(AppScreen.CHAT) }
            )
            QuickMenuItem(
              icon = Icons.Default.EventNote,
              label = "Ujian",
              bgColor = PastelYellowLight,
              tintColor = Color(0xFFD97706),
              onClick = { onNavigateTo(AppScreen.EXAM) }
            )
          }
        }
      }
    }

    // Recent School Gallery Highlight
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Dokumentasi Kegiatan Siswa 📸",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
          )

          Text(
            text = "Lihat Semua",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PastelPeach,
            modifier = Modifier
              .clickable { onNavigateTo(AppScreen.GALLERY) }
              .padding(4.dp)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        recentActivities.take(2).forEach { act ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clickable { onNavigateTo(AppScreen.GALLERY) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(54.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(PastelPeachLight),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Collections,
                  contentDescription = null,
                  tint = PastelPeach,
                  modifier = Modifier.size(28.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Surface(
                  color = PastelLilacLight,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = act.category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PastelLilac,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = act.title,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937),
                  maxLines = 1
                )
                Text(
                  text = act.description,
                  fontSize = 11.sp,
                  color = Color(0xFF6B7280),
                  maxLines = 1
                )
              }

              Spacer(modifier = Modifier.width(8.dp))

              Surface(
                color = PastelPinkLight,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.clickable { onLikeActivity(act.id) }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Love",
                    tint = PastelPink,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(3.dp))
                  Text(
                    text = "${act.likes}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PastelPink
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

@Composable
fun QuickMenuItem(
  icon: ImageVector,
  label: String,
  bgColor: Color,
  tintColor: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
        .background(bgColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = tintColor,
        modifier = Modifier.size(24.dp)
      )
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = Color(0xFF374151)
    )
  }
}
