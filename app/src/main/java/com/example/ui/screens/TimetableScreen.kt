package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyUniform
import com.example.data.model.TimetableLesson
import com.example.ui.UserRole
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelMintLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelSkyLight
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.PastelYellowLight
import com.example.ui.theme.SoftBackground
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
  lessons: List<TimetableLesson>,
  uniforms: List<DailyUniform>,
  currentRole: UserRole,
  onTogglePackStatus: (Long, Boolean) -> Unit,
  onAddLesson: (String, Int, String, String, String, String, String, String) -> Unit,
  onDeleteLesson: (Long) -> Unit,
  onUpdateUniform: (Long, String, String, String, String) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  val days = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat")

  // Determine current day in Indonesian
  val calendar = Calendar.getInstance()
  val todayDayName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
    Calendar.MONDAY -> "Senin"
    Calendar.TUESDAY -> "Selasa"
    Calendar.WEDNESDAY -> "Rabu"
    Calendar.THURSDAY -> "Kamis"
    Calendar.FRIDAY -> "Jumat"
    else -> "Senin" // Default to Senin on weekend
  }

  var selectedDay by remember { mutableStateOf(todayDayName) }
  var showAddLessonDialog by remember { mutableStateOf(false) }
  var showEditUniformDialog by remember { mutableStateOf(false) }

  // Lessons for selected day
  val dayLessons = lessons.filter { it.dayOfWeek.equals(selectedDay, ignoreCase = true) }
    .sortedBy { it.periodNumber }

  // Uniform for selected day
  val currentUniform = uniforms.firstOrNull { it.dayOfWeek.equals(selectedDay, ignoreCase = true) }

  // Checklist packing statistics
  val lessonsWithItems = dayLessons.filter { it.requiredItems.isNotBlank() }
  val packedCount = lessonsWithItems.count { it.isCompletedByParent }
  val totalPackItems = lessonsWithItems.size

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = SoftBackground,
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Jadwal & Seragam Sekolah 🎒",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = Color(0xFF1F2937)
            )
            Text(
              text = "Kelas 2-B SD Ceria Bangsa • TP 2026/2027",
              fontSize = 11.sp,
              color = PastelSky
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("timetable_back_button")
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
              text = if (currentRole == UserRole.TEACHER) "Mode Guru" else "Bunda Mode",
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
      if (currentRole == UserRole.TEACHER || currentRole == UserRole.ADMIN) {
        FloatingActionButton(
          onClick = { showAddLessonDialog = true },
          containerColor = PastelPeach,
          contentColor = Color.White,
          shape = CircleShape,
          modifier = Modifier.testTag("add_lesson_fab")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Tambah Mapel", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Day Selector Bar (Senin - Jumat)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Pilih Hari Pembelajaran",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280)
              )
              if (selectedDay == todayDayName) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = PastelMintLight
                ) {
                  Text(
                    text = "Hari Ini ✨",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PastelMint,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              items(days) { day ->
                val isSelected = selectedDay == day
                val isToday = day == todayDayName

                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = if (isSelected) PastelSky else if (isToday) PastelSkyLight else Color(0xFFF3F4F6),
                  modifier = Modifier
                    .clickable { selectedDay = day }
                    .testTag("day_tab_$day")
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = day,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      fontSize = 13.sp,
                      color = if (isSelected) Color.White else if (isToday) PastelSky else Color(0xFF4B5563)
                    )
                    if (isToday) {
                      Spacer(modifier = Modifier.width(4.dp))
                      Box(
                        modifier = Modifier
                          .size(6.dp)
                          .clip(CircleShape)
                          .background(if (isSelected) Color.White else PastelSky)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      // 2. Uniform Card for Selected Day (Kode Seragam Harian)
      item {
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
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PastelPeachLight),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Checkroom,
                    contentDescription = "Seragam",
                    tint = PastelPeach,
                    modifier = Modifier.size(20.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Seragam Hari $selectedDay",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B7280)
                  )
                  Text(
                    text = currentUniform?.uniformTitle ?: "Seragam Bebas Rapi",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                  )
                }
              }

              if (currentRole == UserRole.TEACHER || currentRole == UserRole.ADMIN) {
                IconButton(
                  onClick = { showEditUniformDialog = true },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Aturan Seragam",
                    tint = PastelPeach,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Uniform details
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFF9FAFB),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = currentUniform?.description ?: "Gunakan pakaian rapi bersepatu sesuai ketentuan.",
                  fontSize = 12.sp,
                  color = Color(0xFF374151),
                  lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Accessories & Attributes badge
                if (!currentUniform?.accessories.isNullOrBlank()) {
                  Row(verticalAlignment = Alignment.Top) {
                    Text(
                      text = "Atribut:",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF4B5563)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = currentUniform.accessories,
                      fontSize = 11.sp,
                      color = Color(0xFF6B7280),
                      lineHeight = 16.sp
                    )
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                }

                // Shoes requirement
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "Alas Kaki:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B5563)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = currentUniform?.shoesColor ?: "Sepatu Hitam Polos",
                    fontSize = 11.sp,
                    color = Color(0xFF059669),
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            }
          }
        }
      }

      // 3. Backpack Checklist & Preparation Progress (Bunda Mode)
      if (totalPackItems > 0) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PastelSkyLight.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = PastelSky,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Persiapan Tas Sekolah $selectedDay",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                  )
                }

                Text(
                  text = "$packedCount / $totalPackItems Siap",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (packedCount == totalPackItems) Color(0xFF15803D) else PastelSky
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              LinearProgressIndicator(
                progress = { if (totalPackItems > 0) packedCount.toFloat() / totalPackItems else 0f },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp)),
                color = if (packedCount == totalPackItems) Color(0xFF10B981) else PastelSky,
                trackColor = Color.White
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = if (packedCount == totalPackItems) {
                  "Alhamdulillah, semua buku & perlengkapan sudah masuk ke tas ananda! ✨"
                } else {
                  "Centang kotak pada mata pelajaran di bawah setelah buku dimasukkan ke tas."
                },
                fontSize = 11.sp,
                color = if (packedCount == totalPackItems) Color(0xFF15803D) else Color(0xFF475569)
              )
            }
          }
        }
      }

      // 4. Section Header: Lessons
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Jadwal Pelajaran ($selectedDay)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
          )
          Text(
            text = "${dayLessons.size} Mata Pelajaran",
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
          )
        }
      }

      // 5. Lessons List
      if (dayLessons.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
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
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = PastelPeach,
                modifier = Modifier.size(40.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Belum Ada Jadwal untuk Hari $selectedDay",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
              )
              Text(
                text = "Wali kelas dapat menambahkan susunan jadwal KBM melalui tombol di bawah.",
                fontSize = 11.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      } else {
        items(dayLessons) { lesson ->
          LessonItemCard(
            lesson = lesson,
            currentRole = currentRole,
            onTogglePack = { isChecked ->
              onTogglePackStatus(lesson.id, isChecked)
            },
            onDelete = {
              onDeleteLesson(lesson.id)
            }
          )
        }
      }

      // Bottom Spacer
      item {
        Spacer(modifier = Modifier.height(48.dp))
      }
    }
  }

  // Dialog: Add Timetable Lesson (Teacher / Admin)
  if (showAddLessonDialog) {
    AddLessonDialog(
      dayOfWeek = selectedDay,
      periodNumberDefault = dayLessons.size + 1,
      onDismiss = { showAddLessonDialog = false },
      onConfirm = { period, timeRange, subject, teacher, room, items, colorHex ->
        onAddLesson(selectedDay, period, timeRange, subject, teacher, room, items, colorHex)
        showAddLessonDialog = false
      }
    )
  }

  // Dialog: Edit Uniform (Teacher / Admin)
  if (showEditUniformDialog && currentUniform != null) {
    EditUniformDialog(
      uniform = currentUniform,
      onDismiss = { showEditUniformDialog = false },
      onConfirm = { title, desc, acc, shoes ->
        onUpdateUniform(currentUniform.id, title, desc, acc, shoes)
        showEditUniformDialog = false
      }
    )
  }
}

@Composable
fun LessonItemCard(
  lesson: TimetableLesson,
  currentRole: UserRole,
  onTogglePack: (Boolean) -> Unit,
  onDelete: () -> Unit
) {
  val accentColor = try {
    Color(android.graphics.Color.parseColor(lesson.colorHex))
  } catch (e: Exception) {
    PastelSky
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      // Period indicator pill
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(accentColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "${lesson.periodNumber}",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = accentColor
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        // Time & Room
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AccessTime,
              contentDescription = null,
              tint = Color(0xFF6B7280),
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = lesson.timeRange,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF4B5563)
            )
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFF3F4F6)
          ) {
            Text(
              text = lesson.roomName,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF6B7280),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Subject Title
        Text(
          text = lesson.subject,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1F2937)
        )

        // Teacher Name
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(top = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = lesson.teacherName,
            fontSize = 11.sp,
            color = Color(0xFF6B7280)
          )
        }

        // Required items & Packing checklist
        if (lesson.requiredItems.isNotBlank()) {
          Spacer(modifier = Modifier.height(8.dp))
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (lesson.isCompletedByParent) PastelMintLight else Color(0xFFF9FAFB),
            border = BorderStroke(
              1.dp,
              if (lesson.isCompletedByParent) PastelMint.copy(alpha = 0.5f) else Color(0xFFE5E7EB)
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .clickable { onTogglePack(!lesson.isCompletedByParent) }
                .padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  imageVector = if (lesson.isCompletedByParent) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.MenuBook,
                  contentDescription = null,
                  tint = if (lesson.isCompletedByParent) PastelMint else Color(0xFF6B7280),
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                  Text(
                    text = "Bawa ke Sekolah:",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (lesson.isCompletedByParent) Color(0xFF15803D) else Color(0xFF4B5563)
                  )
                  Text(
                    text = lesson.requiredItems,
                    fontSize = 11.sp,
                    color = if (lesson.isCompletedByParent) Color(0xFF15803D) else Color(0xFF374151),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }

              Checkbox(
                checked = lesson.isCompletedByParent,
                onCheckedChange = { onTogglePack(it) },
                colors = CheckboxDefaults.colors(
                  checkedColor = PastelMint,
                  uncheckedColor = Color(0xFF9CA3AF)
                ),
                modifier = Modifier.size(28.dp)
              )
            }
          }
        }
      }

      // Teacher delete action
      if (currentRole == UserRole.TEACHER || currentRole == UserRole.ADMIN) {
        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Hapus Pelajaran",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}

@Composable
fun AddLessonDialog(
  dayOfWeek: String,
  periodNumberDefault: Int,
  onDismiss: () -> Unit,
  onConfirm: (Int, String, String, String, String, String, String) -> Unit
) {
  var subject by remember { mutableStateOf("") }
  var timeRange by remember { mutableStateOf("07.15 - 08.30 WIB") }
  var teacherName by remember { mutableStateOf("Bu Sarah, S.Pd") }
  var roomName by remember { mutableStateOf("Ruang Kelas 2-B") }
  var requiredItems by remember { mutableStateOf("") }
  var periodNumber by remember { mutableIntStateOf(periodNumberDefault) }
  var selectedColorHex by remember { mutableStateOf("#38BDF8") }

  val colorOptions = listOf("#38BDF8", "#F97316", "#10B981", "#8B5CF6", "#EC4899", "#F59E0B")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Tambah Pelajaran ($dayOfWeek)",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = subject,
          onValueChange = { subject = it },
          label = { Text("Nama Mata Pelajaran") },
          placeholder = { Text("contoh: Bahasa Indonesia") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = periodNumber.toString(),
            onValueChange = { periodNumber = it.toIntOrNull() ?: 1 },
            label = { Text("Jam Ke-") },
            modifier = Modifier.weight(0.7f),
            singleLine = true
          )
          OutlinedTextField(
            value = timeRange,
            onValueChange = { timeRange = it },
            label = { Text("Waktu KBM") },
            placeholder = { Text("07.15 - 08.30 WIB") },
            modifier = Modifier.weight(1.3f),
            singleLine = true
          )
        }

        OutlinedTextField(
          value = teacherName,
          onValueChange = { teacherName = it },
          label = { Text("Guru Pengajar") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = roomName,
          onValueChange = { roomName = it },
          label = { Text("Ruangan / Lokasi") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = requiredItems,
          onValueChange = { requiredItems = it },
          label = { Text("Buku & Alat yang Wajib Dibawa") },
          placeholder = { Text("contoh: Buku Tematik 2A, Krayon") },
          modifier = Modifier.fillMaxWidth()
        )

        Text(
          text = "Pilih Warna Kartu:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF6B7280)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          colorOptions.forEach { hex ->
            val color = Color(android.graphics.Color.parseColor(hex))
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color)
                .clickable { selectedColorHex = hex }
                .border(
                  2.dp,
                  if (selectedColorHex == hex) Color.Black else Color.Transparent,
                  CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              if (selectedColorHex == hex) {
                Icon(
                  Icons.Default.Check,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (subject.isNotBlank()) {
            onConfirm(
              periodNumber,
              timeRange,
              subject.trim(),
              teacherName.trim(),
              roomName.trim(),
              requiredItems.trim(),
              selectedColorHex
            )
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
      ) {
        Text("Simpan Jadwal")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Batal")
      }
    }
  )
}

@Composable
fun EditUniformDialog(
  uniform: DailyUniform,
  onDismiss: () -> Unit,
  onConfirm: (String, String, String, String) -> Unit
) {
  var title by remember { mutableStateOf(uniform.uniformTitle) }
  var description by remember { mutableStateOf(uniform.description) }
  var accessories by remember { mutableStateOf(uniform.accessories) }
  var shoesColor by remember { mutableStateOf(uniform.shoesColor) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Ubah Aturan Seragam (${uniform.dayOfWeek})",
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Nama Seragam") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Deskripsi Seragam") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 2
        )

        OutlinedTextField(
          value = accessories,
          onValueChange = { accessories = it },
          label = { Text("Atribut Tambahan (Topi/Dasi/Hasduk)") },
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = shoesColor,
          onValueChange = { shoesColor = it },
          label = { Text("Ketentuan Sepatu & Kaos Kaki") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title.trim(), description.trim(), accessories.trim(), shoesColor.trim())
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
      ) {
        Text("Simpan Aturan")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Batal")
      }
    }
  )
}
