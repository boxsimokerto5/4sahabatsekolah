package com.example.ui.screens

import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicCalendarEvent
import com.example.ui.UserRole
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
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
  events: List<AcademicCalendarEvent>,
  currentRole: UserRole,
  onAddEvent: (String, String, String, String, String, String, String, String) -> Unit,
  onUpdateRsvp: (Long, String) -> Unit,
  onToggleCheckedItem: (AcademicCalendarEvent, String) -> Unit,
  onDeleteEvent: (Long) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current

  // State for Year and Month viewing (Default: Oktober 2026 to match active school term)
  var currentYearMonth by remember { mutableStateOf(YearMonth.of(2026, 10)) }
  var selectedDate by remember { mutableStateOf("2026-10-08") } // Selected day default: Field trip planetarium
  var selectedFilterCategory by remember { mutableStateOf<String?>(null) }
  var filterOnlyMyClass by remember { mutableStateOf(false) }

  var showAddDialog by remember { mutableStateOf(false) }
  var eventToDelete by remember { mutableStateOf<AcademicCalendarEvent?>(null) }

  // Month navigation helpers
  val monthNameFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("id", "ID"))
  val formattedCurrentMonth = currentYearMonth.format(monthNameFormatter)

  // Filtered events
  val filteredEvents = events.filter { event ->
    val matchCategory = selectedFilterCategory == null || event.category == selectedFilterCategory
    val matchClass = !filterOnlyMyClass || event.targetClass.contains("2-B", ignoreCase = true) || event.targetClass.contains("Semua", ignoreCase = true)
    matchCategory && matchClass
  }

  // Events on currently selected date
  val eventsOnSelectedDate = filteredEvents.filter { it.date == selectedDate }

  // Other upcoming events in this month
  val eventsInCurrentMonth = filteredEvents.filter {
    it.date.startsWith(currentYearMonth.toString())
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              "Kalender & Agenda Siswa 📅",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = Color(0xFF1F2937)
            )
            Text(
              if (currentRole == UserRole.TEACHER) "Mode Guru: Rencana & Program Kegiatan" else "Mode Orang Tua: Pemantauan & Persiapan Ananda",
              fontSize = 11.sp,
              color = if (currentRole == UserRole.TEACHER) PastelPeach else PastelSky
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("calendar_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (currentRole == UserRole.TEACHER) PastelPeachLight else PastelSkyLight,
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Text(
              text = if (currentRole == UserRole.TEACHER) "Guru (Penyusun)" else "Orang Tua",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (currentRole == UserRole.TEACHER) PastelPeach else PastelSky,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    },
    floatingActionButton = {
      if (currentRole == UserRole.TEACHER) {
        FloatingActionButton(
          onClick = { showAddDialog = true },
          containerColor = PastelPeach,
          contentColor = Color.White,
          shape = CircleShape,
          modifier = Modifier.testTag("add_calendar_event_fab")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah Rencana Kegiatan")
            Spacer(modifier = Modifier.width(6.dp))
            Text("Tambah Agenda", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      // 1. Month Navigator & Calendar Grid Card
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {

            // Month Header with Prev/Next
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              IconButton(
                onClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Bulan Sebelumnya")
              }

              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = formattedCurrentMonth.replaceFirstChar { it.uppercase() },
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937)
                )
                Text(
                  text = "${eventsInCurrentMonth.size} agenda kegiatan sekolah",
                  fontSize = 11.sp,
                  color = Color.Gray
                )
              }

              IconButton(
                onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Bulan Berikutnya")
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Calendar Days Grid (Sen - Min)
            CalendarMonthGrid(
              yearMonth = currentYearMonth,
              selectedDate = selectedDate,
              events = events,
              onSelectDate = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(10.dp))

            // Category Legend Dots
            CategoryLegendBar()
          }
        }
      }

      // 2. Filter Bar
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Filter Agenda 🔍",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF374151)
            )

            // Quick toggle for "Hanya Kelas 2-B"
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (filterOnlyMyClass) PastelMint else Color(0xFFF3F4F6),
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable { filterOnlyMyClass = !filterOnlyMyClass }
            ) {
              Text(
                text = if (filterOnlyMyClass) "✓ Khusus Kelas 2-B" else "Filter: Kelas 2-B",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (filterOnlyMyClass) Color.White else Color(0xFF4B5563),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }

          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
              FilterChip(
                selected = selectedFilterCategory == null,
                onClick = { selectedFilterCategory = null },
                label = { Text("Semua (${filteredEvents.size})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelPeachLight,
                  selectedLabelColor = PastelPeach
                )
              )
            }
            item {
              FilterChip(
                selected = selectedFilterCategory == "FIELD_TRIP",
                onClick = {
                  selectedFilterCategory = if (selectedFilterCategory == "FIELD_TRIP") null else "FIELD_TRIP"
                },
                label = { Text("🚌 Kegiatan Luar / Outing", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelMintLight,
                  selectedLabelColor = Color(0xFF1B5E20)
                )
              )
            }
            item {
              FilterChip(
                selected = selectedFilterCategory == "UJIAN",
                onClick = {
                  selectedFilterCategory = if (selectedFilterCategory == "UJIAN") null else "UJIAN"
                },
                label = { Text("📝 Ujian & Asesmen", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelYellowLight,
                  selectedLabelColor = Color(0xFFB78103)
                )
              )
            }
            item {
              FilterChip(
                selected = selectedFilterCategory == "PERTEMUAN",
                onClick = {
                  selectedFilterCategory = if (selectedFilterCategory == "PERTEMUAN") null else "PERTEMUAN"
                },
                label = { Text("👥 Pertemuan Wali", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelSkyLight,
                  selectedLabelColor = Color(0xFF0D47A1)
                )
              )
            }
            item {
              FilterChip(
                selected = selectedFilterCategory == "PENTAS_SENI",
                onClick = {
                  selectedFilterCategory = if (selectedFilterCategory == "PENTAS_SENI") null else "PENTAS_SENI"
                },
                label = { Text("🎨 Seni & Budaya", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelLilacLight,
                  selectedLabelColor = Color(0xFF4A148C)
                )
              )
            }
          }
        }
      }

      // 3. Header Agenda on Selected Date
      item {
        val parsedDate = try {
          LocalDate.parse(selectedDate)
        } catch (e: Exception) {
          LocalDate.of(2026, 10, 8)
        }
        val dateHumanReadable = parsedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID")))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Rencana Kegiatan: $dateHumanReadable",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color(0xFF1F2937)
            )
            Text(
              text = if (eventsOnSelectedDate.isEmpty()) "Tidak ada kegiatan khusus pada tanggal ini" else "${eventsOnSelectedDate.size} kegiatan terjadwal",
              fontSize = 12.sp,
              color = if (eventsOnSelectedDate.isEmpty()) Color.Gray else PastelPeach
            )
          }
        }
      }

      // 4. List of Events for the Selected Day
      if (eventsOnSelectedDate.isNotEmpty()) {
        items(eventsOnSelectedDate, key = { it.id }) { event ->
          EventPlanCard(
            event = event,
            currentRole = currentRole,
            onUpdateRsvp = { onUpdateRsvp(event.id, it) },
            onToggleCheckedItem = { item -> onToggleCheckedItem(event, item) },
            onDelete = { eventToDelete = event },
            onAddToCalendar = {
              val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, "[SahabatSekolah] ${event.title}")
                putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
                putExtra(CalendarContract.Events.DESCRIPTION, "${event.description}\n\nTarget: ${event.targetClass}\nPerlengkapan: ${event.requiredItems}")
                putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
              }
              try {
                context.startActivity(intent)
              } catch (e: Exception) {
                Toast.makeText(context, "Aplikasi kalender tidak ditemukan di perangkat", Toast.LENGTH_SHORT).show()
              }
            }
          )
        }
      } else {
        // Empty state on selected date + prompt to look at upcoming in the month
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                Icons.Default.EventAvailable,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                "Tidak ada agenda di tanggal ini",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF4B5563)
              )
              Text(
                "Ketuk tanggal lain yang memiliki titik warna pada kalender di atas untuk melihat detail kegiatan.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
              )
            }
          }
        }
      }

      // 5. Section: Seluruh Agenda Bulan Ini
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Daftar Semua Agenda $formattedCurrentMonth 📋",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF1F2937)
          )
        }
      }

      val otherEventsThisMonth = eventsInCurrentMonth.filter { it.date != selectedDate }
      if (otherEventsThisMonth.isNotEmpty()) {
        items(otherEventsThisMonth, key = { "other_${it.id}" }) { event ->
          EventPlanCard(
            event = event,
            currentRole = currentRole,
            onUpdateRsvp = { onUpdateRsvp(event.id, it) },
            onToggleCheckedItem = { item -> onToggleCheckedItem(event, item) },
            onDelete = { eventToDelete = event },
            onAddToCalendar = {
              val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, "[SahabatSekolah] ${event.title}")
                putExtra(CalendarContract.Events.EVENT_LOCATION, event.location)
                putExtra(CalendarContract.Events.DESCRIPTION, "${event.description}\n\nTarget: ${event.targetClass}\nPerlengkapan: ${event.requiredItems}")
              }
              try {
                context.startActivity(intent)
              } catch (e: Exception) {
                Toast.makeText(context, "Aplikasi kalender tidak ditemukan", Toast.LENGTH_SHORT).show()
              }
            }
          )
        }
      } else if (eventsInCurrentMonth.isEmpty()) {
        item {
          Text(
            "Belum ada agenda kegiatan yang dijadwalkan pada bulan ini.",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 8.dp)
          )
        }
      }
    }
  }

  // Teacher Add Event Dialog
  if (showAddDialog) {
    AddCalendarEventDialog(
      initialDate = selectedDate,
      onDismiss = { showAddDialog = false },
      onConfirm = { title, date, timeRange, targetClass, category, location, description, requiredItems ->
        onAddEvent(title, date, timeRange, targetClass, category, location, description, requiredItems)
        selectedDate = date
        showAddDialog = false
      }
    )
  }

  // Delete Confirmation Dialog
  eventToDelete?.let { ev ->
    AlertDialog(
      onDismissRequest = { eventToDelete = null },
      title = { Text("Hapus Agenda Kegiatan?") },
      text = { Text("Apakah Ibu Guru yakin ingin menghapus agenda '${ev.title}' dari kalender sekolah?") },
      confirmButton = {
        Button(
          onClick = {
            onDeleteEvent(ev.id)
            eventToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
        ) {
          Text("Hapus", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { eventToDelete = null }) {
          Text("Batal")
        }
      }
    )
  }
}

// ----------------------------------------------------
// CALENDAR MONTH GRID COMPONENT
// ----------------------------------------------------
@Composable
private fun CalendarMonthGrid(
  yearMonth: YearMonth,
  selectedDate: String,
  events: List<AcademicCalendarEvent>,
  onSelectDate: (String) -> Unit
) {
  val daysOfWeek = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
  val firstDayOfMonth = yearMonth.atDay(1)
  val firstDayOfWeekIndex = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday
  val daysInMonth = yearMonth.lengthOfMonth()

  // Date formatter
  val todayString = LocalDate.now().toString()

  Column(modifier = Modifier.fillMaxWidth()) {
    // Header row of week days
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceAround
    ) {
      daysOfWeek.forEachIndexed { idx, day ->
        Text(
          text = day,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (idx == 0) Color(0xFFEF4444) else Color(0xFF6B7280),
          textAlign = TextAlign.Center,
          modifier = Modifier.weight(1f)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Days grid calculation
    val totalSlots = firstDayOfWeekIndex + daysInMonth
    val rows = (totalSlots + 6) / 7

    for (row in 0 until rows) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        for (col in 0..6) {
          val slotIndex = row * 7 + col
          val dayNumber = slotIndex - firstDayOfWeekIndex + 1

          if (dayNumber in 1..daysInMonth) {
            val dateStr = String.format(Locale.US, "%04d-%02d-%02d", yearMonth.year, yearMonth.monthValue, dayNumber)
            val isSelected = dateStr == selectedDate
            val isToday = dateStr == todayString

            // Events on this day
            val dayEvents = events.filter { it.date == dateStr }

            Box(
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                  when {
                    isSelected -> PastelPeach
                    isToday -> PastelSkyLight
                    else -> Color.Transparent
                  }
                )
                .border(
                  width = if (isToday && !isSelected) 1.dp else 0.dp,
                  color = if (isToday && !isSelected) PastelSky else Color.Transparent,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { onSelectDate(dateStr) },
              contentAlignment = Alignment.Center
            ) {
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Text(
                  text = dayNumber.toString(),
                  fontSize = 13.sp,
                  fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                  color = when {
                    isSelected -> Color.White
                    col == 0 -> Color(0xFFEF4444)
                    else -> Color(0xFF1F2937)
                  }
                )

                // Category dots
                if (dayEvents.isNotEmpty()) {
                  Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(top = 2.dp)
                  ) {
                    dayEvents.take(3).forEach { ev ->
                      val dotColor = getCategoryColor(ev.category)
                      Box(
                        modifier = Modifier
                          .size(5.dp)
                          .clip(CircleShape)
                          .background(if (isSelected) Color.White else dotColor)
                      )
                    }
                  }
                }
              }
            }
          } else {
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// CATEGORY LEGEND BAR
// ----------------------------------------------------
@Composable
private fun CategoryLegendBar() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    LegendItem(color = Color(0xFF2E7D32), label = "Outing")
    LegendItem(color = Color(0xFFE65100), label = "Ujian")
    LegendItem(color = Color(0xFF1565C0), label = "Pertemuan")
    LegendItem(color = Color(0xFF7B1FA2), label = "Seni/Pentas")
    LegendItem(color = Color(0xFFC2185B), label = "Libur")
  }
}

@Composable
private fun LegendItem(color: Color, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(color)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = label, fontSize = 10.sp, color = Color(0xFF4B5563))
  }
}

// ----------------------------------------------------
// EVENT PLAN CARD (DETAILED & INTERACTIVE)
// ----------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EventPlanCard(
  event: AcademicCalendarEvent,
  currentRole: UserRole,
  onUpdateRsvp: (String) -> Unit,
  onToggleCheckedItem: (String) -> Unit,
  onDelete: () -> Unit,
  onAddToCalendar: () -> Unit
) {
  val categoryMeta = getCategoryMeta(event.category)
  val parsedDate = try {
    LocalDate.parse(event.date)
  } catch (e: Exception) {
    LocalDate.of(2026, 10, 8)
  }
  val dateFormatted = parsedDate.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale("id", "ID")))

  // Checklist items
  val requiredItemList = event.requiredItems.split(",")
    .map { it.trim() }
    .filter { it.isNotBlank() }

  val checkedItemList = event.checkedItems.split(",")
    .map { it.trim() }
    .filter { it.isNotBlank() }

  val progress = if (requiredItemList.isNotEmpty()) {
    checkedItemList.size.toFloat() / requiredItemList.size.toFloat()
  } else 0f

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {

      // Header row: Category Pill + Class Badge + Delete (Guru only)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = categoryMeta.bgColor
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              categoryMeta.icon,
              contentDescription = null,
              tint = categoryMeta.tintColor,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
              text = categoryMeta.label,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = categoryMeta.tintColor
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF3F4F6)
          ) {
            Text(
              text = event.targetClass,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF4B5563),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          if (currentRole == UserRole.TEACHER) {
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
              onClick = onDelete,
              modifier = Modifier.size(28.dp)
            ) {
              Icon(
                Icons.Default.Delete,
                contentDescription = "Hapus Agenda",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Event Title
      Text(
        text = event.title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1F2937)
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Time & Location Info
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Schedule, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "$dateFormatted • ${event.timeRange}", fontSize = 11.sp, color = Color(0xFF4B5563))
        }
      }

      if (event.location.isNotBlank()) {
        Spacer(modifier = Modifier.height(3.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.LocationOn, contentDescription = null, tint = PastelSky, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = event.location, fontSize = 11.sp, color = Color(0xFF4B5563))
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Description
      Text(
        text = event.description,
        fontSize = 12.sp,
        color = Color(0xFF4B5563),
        lineHeight = 17.sp
      )

      // Checklist Perlengkapan Siswa (Packing Checklist)
      if (requiredItemList.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.Backpack,
                  contentDescription = null,
                  tint = PastelMint,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Perlengkapan yang Wajib Disiapkan 🎒",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937)
                )
              }

              Text(
                text = "${checkedItemList.size}/${requiredItemList.size} siap",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (checkedItemList.size == requiredItemList.size) Color(0xFF15803D) else PastelPeach
              )
            }

            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = if (checkedItemList.size == requiredItemList.size) PastelMint else PastelPeach,
              trackColor = Color(0xFFE5E7EB)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Checkbox item list
            requiredItemList.forEach { item ->
              val isChecked = checkedItemList.contains(item)
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .clickable { onToggleCheckedItem(item) }
                  .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Checkbox(
                  checked = isChecked,
                  onCheckedChange = { onToggleCheckedItem(item) },
                  colors = CheckboxDefaults.colors(
                    checkedColor = PastelMint,
                    uncheckedColor = Color(0xFF9CA3AF)
                  ),
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = item,
                  fontSize = 12.sp,
                  color = if (isChecked) Color(0xFF15803D) else Color(0xFF374151),
                  fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider(color = Color(0xFFF3F4F6))
      Spacer(modifier = Modifier.height(10.dp))

      // Bottom Action Row: RSVP Status & Add to Calendar Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // RSVP for Parent
        Column {
          Text(
            text = "Konfirmasi Kehadiran Rafa:",
            fontSize = 10.sp,
            color = Color.Gray
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val isHadir = event.rsvpStatus == "HADIR"
            val isIzin = event.rsvpStatus == "IZIN"

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isHadir) Color(0xFFDCFCE7) else Color(0xFFF3F4F6),
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onUpdateRsvp("HADIR") }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.Check,
                  contentDescription = null,
                  tint = if (isHadir) Color(0xFF15803D) else Color(0xFF6B7280),
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "Siap Ikut",
                  fontSize = 11.sp,
                  fontWeight = if (isHadir) FontWeight.Bold else FontWeight.Normal,
                  color = if (isHadir) Color(0xFF15803D) else Color(0xFF6B7280)
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isIzin) Color(0xFFFEE2E2) else Color(0xFFF3F4F6),
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onUpdateRsvp("IZIN") }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.Close,
                  contentDescription = null,
                  tint = if (isIzin) Color(0xFFB91C1C) else Color(0xFF6B7280),
                  modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "Izin",
                  fontSize = 11.sp,
                  fontWeight = if (isIzin) FontWeight.Bold else FontWeight.Normal,
                  color = if (isIzin) Color(0xFFB91C1C) else Color(0xFF6B7280)
                )
              }
            }
          }
        }

        // Add to Device Google Calendar
        OutlinedButton(
          onClick = onAddToCalendar,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = PastelSky),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("+ Kalender HP", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    }
  }
}

// ----------------------------------------------------
// TEACHER ADD EVENT DIALOG
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCalendarEventDialog(
  initialDate: String,
  onDismiss: () -> Unit,
  onConfirm: (String, String, String, String, String, String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var date by remember { mutableStateOf(initialDate) }
  var timeRange by remember { mutableStateOf("08:00 - 11:30 WIB") }
  var targetClass by remember { mutableStateOf("Kelas 2-B") }
  var category by remember { mutableStateOf("FIELD_TRIP") }
  var location by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var requiredItems by remember { mutableStateOf("Botol minum, Buku catatan kecil, Topi sekolah") }

  var expandedCategory by remember { mutableStateOf(false) }
  var expandedClass by remember { mutableStateOf(false) }

  val categories = listOf(
    "FIELD_TRIP" to "🚌 Kegiatan Luar / Outing / Kunjungan",
    "UJIAN" to "📝 Ujian / Ulangan / Asesmen",
    "PERTEMUAN" to "👥 Pertemuan Orang Tua / POMG",
    "PENTAS_SENI" to "🎨 Seni, Olahraga & Peringatan",
    "LIBUR" to "🏖️ Libur Sekolah / Cuti Bersama"
  )

  val classes = listOf("Kelas 2-B", "Semua Kelas", "Kelas 1-A", "Kelas 2-A", "Kelas 3-B")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.EditCalendar, contentDescription = null, tint = PastelPeach)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Tambah Agenda Kegiatan Siswa", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Judul Kegiatan / Agenda *") },
          placeholder = { Text("Contoh: Kunjungan Edukasi ke Kebun Binatang") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Tanggal (YYYY-MM-DD)") },
            modifier = Modifier.weight(1.2f),
            singleLine = true
          )

          OutlinedTextField(
            value = timeRange,
            onValueChange = { timeRange = it },
            label = { Text("Waktu") },
            placeholder = { Text("08:00 - 12:00") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        // Category dropdown
        ExposedDropdownMenuBox(
          expanded = expandedCategory,
          onExpandedChange = { expandedCategory = !expandedCategory }
        ) {
          OutlinedTextField(
            value = categories.find { it.first == category }?.second ?: category,
            onValueChange = {},
            readOnly = true,
            label = { Text("Kategori Kegiatan") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
            modifier = Modifier
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .fillMaxWidth()
          )
          ExposedDropdownMenu(
            expanded = expandedCategory,
            onDismissRequest = { expandedCategory = false }
          ) {
            categories.forEach { (catKey, catLabel) ->
              DropdownMenuItem(
                text = { Text(catLabel, fontSize = 12.sp) },
                onClick = {
                  category = catKey
                  expandedCategory = false
                }
              )
            }
          }
        }

        // Target Class dropdown
        ExposedDropdownMenuBox(
          expanded = expandedClass,
          onExpandedChange = { expandedClass = !expandedClass }
        ) {
          OutlinedTextField(
            value = targetClass,
            onValueChange = {},
            readOnly = true,
            label = { Text("Target Kelas") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedClass) },
            modifier = Modifier
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .fillMaxWidth()
          )
          ExposedDropdownMenu(
            expanded = expandedClass,
            onDismissRequest = { expandedClass = false }
          ) {
            classes.forEach { cls ->
              DropdownMenuItem(
                text = { Text(cls, fontSize = 12.sp) },
                onClick = {
                  targetClass = cls
                  expandedClass = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = location,
          onValueChange = { location = it },
          label = { Text("Lokasi Kegiatan") },
          placeholder = { Text("Contoh: Museum Nasional / Lapangan Sekolah") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Deskripsi & Petunjuk Kegiatan") },
          placeholder = { Text("Tuliskan tujuan dan catatan untuk siswa/orang tua") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 3
        )

        OutlinedTextField(
          value = requiredItems,
          onValueChange = { requiredItems = it },
          label = { Text("Perlengkapan yang Wajib Dibawa Siswa") },
          placeholder = { Text("Pisahkan dengan koma: Topi, Botol minum, Buku") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title.trim(), date.trim(), timeRange.trim(), targetClass, category, location.trim(), description.trim(), requiredItems.trim())
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
      ) {
        Text("Jadwalkan Agenda", color = Color.White, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Batal")
      }
    }
  )
}

// ----------------------------------------------------
// HELPER MODELS & UTILS
// ----------------------------------------------------
private data class CategoryMeta(
  val label: String,
  val icon: ImageVector,
  val tintColor: Color,
  val bgColor: Color
)

private fun getCategoryColor(category: String): Color {
  return when (category) {
    "FIELD_TRIP" -> Color(0xFF2E7D32)
    "UJIAN" -> Color(0xFFE65100)
    "PERTEMUAN" -> Color(0xFF1565C0)
    "PENTAS_SENI" -> Color(0xFF7B1FA2)
    "LIBUR" -> Color(0xFFC2185B)
    else -> Color(0xFF4B5563)
  }
}

private fun getCategoryMeta(category: String): CategoryMeta {
  return when (category) {
    "FIELD_TRIP" -> CategoryMeta(
      label = "Kegiatan Luar / Outing",
      icon = Icons.Default.DirectionsBus,
      tintColor = Color(0xFF1B5E20),
      bgColor = PastelMintLight
    )
    "UJIAN" -> CategoryMeta(
      label = "Ujian & Asesmen",
      icon = Icons.Default.EditCalendar,
      tintColor = Color(0xFFB78103),
      bgColor = PastelYellowLight
    )
    "PERTEMUAN" -> CategoryMeta(
      label = "Pertemuan Orang Tua",
      icon = Icons.Default.Group,
      tintColor = Color(0xFF0D47A1),
      bgColor = PastelSkyLight
    )
    "PENTAS_SENI" -> CategoryMeta(
      label = "Seni & Peringatan",
      icon = Icons.Default.Celebration,
      tintColor = Color(0xFF4A148C),
      bgColor = PastelLilacLight
    )
    "LIBUR" -> CategoryMeta(
      label = "Libur Sekolah",
      icon = Icons.Default.Weekend,
      tintColor = Color(0xFF880E4F),
      bgColor = PastelPinkLight
    )
    else -> CategoryMeta(
      label = "Agenda Sekolah",
      icon = Icons.Default.School,
      tintColor = Color(0xFF374151),
      bgColor = Color(0xFFF3F4F6)
    )
  }
}
