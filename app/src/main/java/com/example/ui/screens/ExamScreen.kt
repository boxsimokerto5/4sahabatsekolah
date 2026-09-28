package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamSchedule
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.PastelYellowLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
  exams: List<ExamSchedule>,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "Jadwal Ujian & Kisi-Kisi",
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
          containerColor = PastelYellowLight,
          titleContentColor = Color(0xFF1F2937)
        )
      )
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("exam_screen_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Countdown & Notice Card
      item {
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = PastelYellowLight),
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
                  text = "PENILAIAN TENGAH SEMESTER (PTS)",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFB45309)
                )
                Text(
                  text = "Semester Ganjil 2026/2027",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF1F2937)
                )
              }

              Box(
                modifier = Modifier
                  .size(50.dp)
                  .clip(CircleShape)
                  .background(PastelYellow),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CalendarMonth,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(28.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              color = Color.White,
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Lightbulb,
                  contentDescription = null,
                  tint = PastelYellow,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Tips Bunda: Pastikan anak istirahat cukup, sarapan bergizi, dan belajar santai tanpa beban ya Bunda! 🌸",
                  fontSize = 12.sp,
                  color = Color(0xFF4B5563),
                  lineHeight = 16.sp
                )
              }
            }
          }
        }
      }

      // Title
      item {
        Text(
          text = "Mata Pelajaran & Kisi-Kisi Ujian",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1F2937)
        )
      }

      // Exams List
      items(exams) { item ->
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
              Text(
                text = item.subject,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
              )

              Surface(
                color = PastelPeachLight,
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = item.room,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = PastelPeach,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = PastelLilac,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = item.examDate,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151)
              )

              Spacer(modifier = Modifier.width(12.dp))

              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = PastelLilac,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = item.timeRange,
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              color = PastelLilacLight,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = PastelLilac,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Kisi-Kisi Materi:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PastelLilac
                  )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = item.syllabusSummary,
                  fontSize = 12.sp,
                  color = Color(0xFF374151),
                  lineHeight = 17.sp
                )
              }
            }
          }
        }
      }
    }
  }
}
