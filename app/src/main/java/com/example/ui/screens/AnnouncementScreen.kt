package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SchoolAnnouncement
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
fun AnnouncementScreen(
  announcements: List<SchoolAnnouncement>,
  currentRole: UserRole,
  onAddAnnouncement: (String, String, String, String, String, Boolean, String) -> Unit,
  onMarkAsRead: (Long, Boolean) -> Unit,
  onDeleteAnnouncement: (Long) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }
  val context = LocalContext.current

  var searchQuery by remember { mutableStateOf("") }
  var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
  var showOnlyPinned by remember { mutableStateOf(false) }

  var showAddDialog by remember { mutableStateOf(false) }
  var announcementToDelete by remember { mutableStateOf<SchoolAnnouncement?>(null) }

  // Filtered list
  val filteredList = announcements.filter { item ->
    val matchQuery = searchQuery.isBlank() ||
      item.title.contains(searchQuery, ignoreCase = true) ||
      item.content.contains(searchQuery, ignoreCase = true) ||
      item.letterNumber.contains(searchQuery, ignoreCase = true)

    val matchCategory = selectedCategoryFilter == null || item.category == selectedCategoryFilter
    val matchPinned = !showOnlyPinned || item.isPinned

    matchQuery && matchCategory && matchPinned
  }

  val unreadCount = announcements.count { !it.isReadByParent }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              "Papan Pengumuman & Edaran 📢",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = Color(0xFF1F2937)
            )
            Text(
              text = if (currentRole == UserRole.TEACHER) "Mode Guru / Tata Usaha: Terbitkan & Pantau Baca" else "Mading Resmi Sekolah SahabatSekolah",
              fontSize = 11.sp,
              color = if (currentRole == UserRole.TEACHER) PastelPeach else PastelSky
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("announcement_back_button")
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
              text = if (currentRole == UserRole.TEACHER) "Guru (Penerbit)" else "Wali Murid",
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
          modifier = Modifier.testTag("add_announcement_fab")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Add, contentDescription = "Terbitkan Pengumuman")
            Spacer(modifier = Modifier.width(6.dp))
            Text("Buat Pengumuman", fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

      // 1. Search Bar Card
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari no. surat, judul, atau kata kunci...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            trailingIcon = {
              if (searchQuery.isNotBlank()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = "Hapus pencarian")
                }
              }
            },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
            shape = RoundedCornerShape(14.dp)
          )
        }
      }

      // 2. Summary Status & Category Filters
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          // Status Bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Kategori Pengumuman 🏷️",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF374151)
            )

            if (currentRole == UserRole.PARENT && unreadCount > 0) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = PastelPinkLight
              ) {
                Text(
                  text = "🔔 $unreadCount Belum Dibaca",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFBE185D),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            } else {
              Text(
                text = "${filteredList.size} surat edaran",
                fontSize = 12.sp,
                color = Color.Gray
              )
            }
          }

          // Filter Chips
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
              FilterChip(
                selected = selectedCategoryFilter == null && !showOnlyPinned,
                onClick = {
                  selectedCategoryFilter = null
                  showOnlyPinned = false
                },
                label = { Text("Semua (${announcements.size})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelPeachLight,
                  selectedLabelColor = PastelPeach
                )
              )
            }
            item {
              FilterChip(
                selected = showOnlyPinned,
                onClick = {
                  showOnlyPinned = !showOnlyPinned
                  selectedCategoryFilter = null
                },
                label = { Text("📌 Disematkan", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelYellowLight,
                  selectedLabelColor = Color(0xFFB78103)
                )
              )
            }
            item {
              FilterChip(
                selected = selectedCategoryFilter == "URGENT",
                onClick = {
                  selectedCategoryFilter = if (selectedCategoryFilter == "URGENT") null else "URGENT"
                  showOnlyPinned = false
                },
                label = { Text("🔴 Penting / Urgent", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelPinkLight,
                  selectedLabelColor = Color(0xFFBE185D)
                )
              )
            }
            item {
              FilterChip(
                selected = selectedCategoryFilter == "EDARAN",
                onClick = {
                  selectedCategoryFilter = if (selectedCategoryFilter == "EDARAN") null else "EDARAN"
                  showOnlyPinned = false
                },
                label = { Text("📜 Surat Edaran", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelSkyLight,
                  selectedLabelColor = Color(0xFF0369A1)
                )
              )
            }
            item {
              FilterChip(
                selected = selectedCategoryFilter == "KEGIATAN",
                onClick = {
                  selectedCategoryFilter = if (selectedCategoryFilter == "KEGIATAN") null else "KEGIATAN"
                  showOnlyPinned = false
                },
                label = { Text("🎉 Kegiatan", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelMintLight,
                  selectedLabelColor = Color(0xFF15803D)
                )
              )
            }
            item {
              FilterChip(
                selected = selectedCategoryFilter == "AKADEMIK",
                onClick = {
                  selectedCategoryFilter = if (selectedCategoryFilter == "AKADEMIK") null else "AKADEMIK"
                  showOnlyPinned = false
                },
                label = { Text("📚 Akademik", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = PastelLilacLight,
                  selectedLabelColor = Color(0xFF6D28D9)
                )
              )
            }
          }
        }
      }

      // 3. Announcements List
      if (filteredList.isNotEmpty()) {
        items(filteredList, key = { it.id }) { ann ->
          AnnouncementCard(
            announcement = ann,
            currentRole = currentRole,
            onToggleRead = { onMarkAsRead(ann.id, !ann.isReadByParent) },
            onDelete = { announcementToDelete = ann },
            onDownloadAttachment = {
              Toast.makeText(
                context,
                "📄 Mengunduh '${ann.attachmentTitle}' ke memori perangkat...",
                Toast.LENGTH_SHORT
              ).show()
            }
          )
        }
      } else {
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                Icons.Default.Campaign,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(54.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                "Tidak ada pengumuman yang sesuai",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF374151)
              )
              Text(
                "Coba ubah kata kunci pencarian atau pilih kategori lain.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      }
    }
  }

  // Teacher Add Dialog
  if (showAddDialog) {
    AddAnnouncementDialog(
      onDismiss = { showAddDialog = false },
      onConfirm = { title, letterNumber, category, targetAudience, content, isPinned, attachmentTitle ->
        onAddAnnouncement(title, letterNumber, category, targetAudience, content, isPinned, attachmentTitle)
        showAddDialog = false
      }
    )
  }

  // Delete Confirmation Dialog
  announcementToDelete?.let { item ->
    AlertDialog(
      onDismissRequest = { announcementToDelete = null },
      title = { Text("Hapus Pengumuman?") },
      text = { Text("Apakah Ibu Guru yakin ingin menghapus surat pengumuman '${item.title}' dari mading sekolah?") },
      confirmButton = {
        Button(
          onClick = {
            onDeleteAnnouncement(item.id)
            announcementToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
        ) {
          Text("Hapus", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { announcementToDelete = null }) {
          Text("Batal")
        }
      }
    )
  }
}

// ----------------------------------------------------
// ANNOUNCEMENT CARD COMPONENT
// ----------------------------------------------------
@Composable
private fun AnnouncementCard(
  announcement: SchoolAnnouncement,
  currentRole: UserRole,
  onToggleRead: () -> Unit,
  onDelete: () -> Unit,
  onDownloadAttachment: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }
  val categoryMeta = getAnnouncementCategoryMeta(announcement.category)

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize()
      .testTag("announcement_card_${announcement.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {

      // Pinned bar & Category row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          if (announcement.isPinned) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = PastelYellowLight
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.PushPin,
                  contentDescription = "Disematkan",
                  tint = Color(0xFFB78103),
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  "Penting & Disematkan",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFB78103)
                )
              }
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = categoryMeta.bgColor
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                categoryMeta.icon,
                contentDescription = null,
                tint = categoryMeta.tintColor,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                categoryMeta.label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = categoryMeta.tintColor
              )
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = announcement.date,
            fontSize = 11.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
          )

          if (currentRole == UserRole.TEACHER) {
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
              onClick = onDelete,
              modifier = Modifier.size(28.dp)
            ) {
              Icon(
                Icons.Default.Delete,
                contentDescription = "Hapus",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title
      Text(
        text = announcement.title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1F2937),
        lineHeight = 22.sp
      )

      // Official Letter Number & Target Audience
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (announcement.letterNumber.isNotBlank()) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFF3F4F6)
          ) {
            Text(
              text = "No: ${announcement.letterNumber}",
              fontSize = 10.sp,
              color = Color(0xFF4B5563),
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = PastelSkyLight
        ) {
          Text(
            text = "Target: ${announcement.targetAudience}",
            fontSize = 10.sp,
            color = Color(0xFF0369A1),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Content text (with expand/collapse)
      Text(
        text = if (isExpanded || announcement.content.length <= 150) {
          announcement.content
        } else {
          announcement.content.take(150) + "..."
        },
        fontSize = 13.sp,
        color = Color(0xFF374151),
        lineHeight = 19.sp
      )

      if (announcement.content.length > 150) {
        TextButton(
          onClick = { isExpanded = !isExpanded },
          contentPadding = PaddingValues(0.dp),
          modifier = Modifier.height(30.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = if (isExpanded) "Tampilkan Lebih Sedikit" else "Baca Selengkapnya",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = PastelPeach
            )
            Icon(
              if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = null,
              tint = PastelPeach,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Attachment Document Chip (if exists)
      if (announcement.attachmentTitle.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFF8FAFC),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onDownloadAttachment() }
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEE2E2),
                modifier = Modifier.size(32.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = announcement.attachmentTitle,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1F2937),
                  maxLines = 1
                )
                Text(
                  text = "Dokumen PDF Resmi • Ketuk untuk unduh",
                  fontSize = 10.sp,
                  color = Color.Gray
                )
              }
            }

            Icon(
              Icons.Default.Download,
              contentDescription = "Unduh",
              tint = PastelSky,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      HorizontalDivider(color = Color(0xFFF3F4F6))
      Spacer(modifier = Modifier.height(10.dp))

      // Bottom Row: Author + Read Receipts & Action
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Author info
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            Icons.Default.Person,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = announcement.author,
            fontSize = 11.sp,
            color = Color(0xFF4B5563),
            fontWeight = FontWeight.Medium
          )
        }

        // Parent or Teacher Action
        if (currentRole == UserRole.PARENT) {
          // Read Receipt Button for Parent
          val isRead = announcement.isReadByParent
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isRead) Color(0xFFDCFCE7) else PastelPeachLight,
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { onToggleRead() }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                if (isRead) Icons.Default.CheckCircle else Icons.Default.MarkEmailRead,
                contentDescription = null,
                tint = if (isRead) Color(0xFF15803D) else PastelPeach,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = if (isRead) "Sudah Dibaca ✓" else "Konfirmasi Baca",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isRead) Color(0xFF15803D) else PastelPeach
              )
            }
          }
        } else {
          // Teacher View: Reader Count
          val progress = announcement.readCount.toFloat() / announcement.totalRecipients.toFloat()
          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "Dibaca: ${announcement.readCount}/${announcement.totalRecipients} wali murid",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF059669)
            )
            Spacer(modifier = Modifier.height(3.dp))
            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .width(100.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
              color = Color(0xFF059669),
              trackColor = Color(0xFFE5E7EB)
            )
          }
        }
      }
    }
  }
}

// ----------------------------------------------------
// TEACHER ADD ANNOUNCEMENT DIALOG
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAnnouncementDialog(
  onDismiss: () -> Unit,
  onConfirm: (String, String, String, String, String, Boolean, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var letterNumber by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("EDARAN") }
  var targetAudience by remember { mutableStateOf("Semua Wali Murid") }
  var content by remember { mutableStateOf("") }
  var isPinned by remember { mutableStateOf(false) }
  var attachmentTitle by remember { mutableStateOf("") }

  var expandedCategory by remember { mutableStateOf(false) }
  var expandedAudience by remember { mutableStateOf(false) }

  val categories = listOf(
    "URGENT" to "🔴 PENTING / URGENT (Darurat / Cuaca / Vaksin)",
    "EDARAN" to "📜 SURAT EDARAN RESMI (Kebijakan Sekolah)",
    "KEGIATAN" to "🎉 KEGIATAN SISWA (Bazar / Lomba / Pentas)",
    "AKADEMIK" to "📚 INFORMASI AKADEMIK (Ujian / Rapor)"
  )

  val audiences = listOf("Semua Wali Murid", "Kelas 2-B", "Komite Sekolah", "Wali Murid Kelas 1-3")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Campaign, contentDescription = null, tint = PastelPeach)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Terbitkan Pengumuman Resmi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
          label = { Text("Judul Pengumuman *") },
          placeholder = { Text("Contoh: Edaran Kegiatan Pentas Seni & Bazar") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = letterNumber,
            onValueChange = { letterNumber = it },
            label = { Text("No. Surat (Opsional)") },
            placeholder = { Text("050/SD-SS/SE/X/2026") },
            modifier = Modifier.weight(1.2f),
            singleLine = true
          )

          // Pin switch
          Column(
            modifier = Modifier.weight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text("Sematkan 📌", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Switch(
              checked = isPinned,
              onCheckedChange = { isPinned = it },
              colors = SwitchDefaults.colors(checkedThumbColor = PastelPeach)
            )
          }
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
            label = { Text("Kategori") },
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

        // Audience dropdown
        ExposedDropdownMenuBox(
          expanded = expandedAudience,
          onExpandedChange = { expandedAudience = !expandedAudience }
        ) {
          OutlinedTextField(
            value = targetAudience,
            onValueChange = {},
            readOnly = true,
            label = { Text("Target Penerima") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAudience) },
            modifier = Modifier
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .fillMaxWidth()
          )
          ExposedDropdownMenu(
            expanded = expandedAudience,
            onDismissRequest = { expandedAudience = false }
          ) {
            audiences.forEach { aud ->
              DropdownMenuItem(
                text = { Text(aud, fontSize = 12.sp) },
                onClick = {
                  targetAudience = aud
                  expandedAudience = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Isi Pengumuman Lengkap *") },
          placeholder = { Text("Tuliskan detail informasi, tanggal penting, atau instruksi bagi wali murid...") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 4
        )

        OutlinedTextField(
          value = attachmentTitle,
          onValueChange = { attachmentTitle = it },
          label = { Text("Nama Lampiran PDF (Opsional)") },
          placeholder = { Text("Contoh: Surat_Edaran_Pentas_Seni.pdf") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && content.isNotBlank()) {
            onConfirm(title.trim(), letterNumber.trim(), category, targetAudience, content.trim(), isPinned, attachmentTitle.trim())
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
      ) {
        Text("Terbitkan", color = Color.White, fontWeight = FontWeight.Bold)
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
// CATEGORY META HELPER
// ----------------------------------------------------
private data class AnnouncementCategoryMeta(
  val label: String,
  val icon: ImageVector,
  val tintColor: Color,
  val bgColor: Color
)

private fun getAnnouncementCategoryMeta(category: String): AnnouncementCategoryMeta {
  return when (category) {
    "URGENT" -> AnnouncementCategoryMeta(
      label = "Penting / Urgent",
      icon = Icons.Default.PriorityHigh,
      tintColor = Color(0xFFBE185D),
      bgColor = PastelPinkLight
    )
    "EDARAN" -> AnnouncementCategoryMeta(
      label = "Surat Edaran",
      icon = Icons.Default.Description,
      tintColor = Color(0xFF0369A1),
      bgColor = PastelSkyLight
    )
    "KEGIATAN" -> AnnouncementCategoryMeta(
      label = "Kegiatan Siswa",
      icon = Icons.Default.Celebration,
      tintColor = Color(0xFF15803D),
      bgColor = PastelMintLight
    )
    "AKADEMIK" -> AnnouncementCategoryMeta(
      label = "Info Akademik",
      icon = Icons.Default.School,
      tintColor = Color(0xFF6D28D9),
      bgColor = PastelLilacLight
    )
    else -> AnnouncementCategoryMeta(
      label = "Pengumuman",
      icon = Icons.Default.Campaign,
      tintColor = Color(0xFF374151),
      bgColor = Color(0xFFF3F4F6)
    )
  }
}
