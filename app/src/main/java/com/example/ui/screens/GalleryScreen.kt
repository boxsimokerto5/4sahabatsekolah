package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SportsScore
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SchoolActivity
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
  activities: List<SchoolActivity>,
  currentRole: UserRole,
  onLikeActivity: (Long) -> Unit,
  onAddActivity: (title: String, category: String, description: String, photoType: String) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  var selectedCategory by remember { mutableStateOf("Semua") }
  var showAddDialog by remember { mutableStateOf(false) }

  val categories = listOf("Semua", "Prakarya", "Olahraga", "Pentas", "Kelas")

  val filteredList = if (selectedCategory == "Semua") {
    activities
  } else {
    activities.filter { it.category.equals(selectedCategory, ignoreCase = true) }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "Galeri Kegiatan & Momen Siswa",
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
          containerColor = PastelPinkLight,
          titleContentColor = Color(0xFF1F2937)
        )
      )
    },
    floatingActionButton = {
      if (currentRole == UserRole.TEACHER) {
        FloatingActionButton(
          onClick = { showAddDialog = true },
          containerColor = PastelPink,
          contentColor = Color.White,
          modifier = Modifier.testTag("add_activity_fab")
        ) {
          Icon(Icons.Default.Add, contentDescription = "Unggah Momen Baru")
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("gallery_screen_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Category filter tabs
      item {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(categories) { cat ->
            val isSelected = selectedCategory == cat
            Surface(
              color = if (isSelected) PastelPink else PastelPinkLight,
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.clickable { selectedCategory = cat }
            ) {
              Text(
                text = cat,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else PastelPink,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
              )
            }
          }
        }
      }

      // Activity Cards
      items(filteredList) { act ->
        Card(
          shape = RoundedCornerShape(22.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column {
            // Visual Image Header with soft pastel fallback art
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
            ) {
              Image(
                painter = painterResource(id = R.drawable.hero_school_1790519776497),
                contentDescription = act.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )

              // Soft Gradient
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(
                    Brush.verticalGradient(
                      listOf(Color.Transparent, Color(0x992B1B3C))
                    )
                  )
              )

              // Category Badge top start
              Surface(
                color = PastelPeach,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .align(Alignment.TopStart)
                  .padding(12.dp)
              ) {
                Text(
                  text = act.category,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }

              // Date bottom end
              Text(
                text = act.date,
                fontSize = 11.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(12.dp)
              )
            }

            // Info & Description
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = act.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = act.description,
                fontSize = 13.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp
              )

              Spacer(modifier = Modifier.height(14.dp))

              // Love / Like button for Bunda
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PastelPinkLight)
                    .clickable { onLikeActivity(act.id) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Love Momen",
                    tint = PastelPink,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "${act.likes} Bunda Menyukai",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PastelPink
                  )
                }

                Surface(
                  color = PastelLilacLight,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = "Dokumentasi Kelas 2B",
                    fontSize = 11.sp,
                    color = PastelLilac,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // Teacher Add Activity Dialog
  if (showAddDialog) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Kelas") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Unggah Dokumentasi Kegiatan", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul Kegiatan") },
            modifier = Modifier.fillMaxWidth()
          )

          Text("Kategori:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(listOf("Prakarya", "Olahraga", "Pentas", "Kelas")) { c ->
              Surface(
                color = if (category == c) PastelPink else PastelPinkLight,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable { category = c }
              ) {
                Text(
                  text = c,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (category == c) Color.White else PastelPink,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }

          OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Keterangan Aktivitas Siswa") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) {
              onAddActivity(title, category, desc, "hero_banner")
              showAddDialog = false
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = PastelPink)
        ) {
          Text("Unggah Foto")
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
