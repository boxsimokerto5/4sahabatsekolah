package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelMintLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelSkyLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolRegistrationScreen(
  onRegisterSuccess: (
    schoolName: String,
    npsn: String,
    level: String,
    address: String,
    city: String,
    phone: String,
    email: String,
    principalName: String,
    applicantName: String,
    applicantNik: String,
    applicantPhone: String,
    applicantRole: String,
    applicantAddress: String,
    assignmentLetterFileName: String,
    adminUsername: String,
    adminPassword: String
  ) -> Unit,
  onNavigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateToLogin() }
  val context = LocalContext.current

  // Form Fields
  var applicantName by remember { mutableStateOf("") }
  var applicantNik by remember { mutableStateOf("") }
  var applicantPhone by remember { mutableStateOf("") }
  var applicantRole by remember { mutableStateOf("Kepala Sekolah") }
  var applicantAddress by remember { mutableStateOf("") }

  var schoolName by remember { mutableStateOf("") }
  var npsn by remember { mutableStateOf("") }
  var level by remember { mutableStateOf("Sekolah Dasar (SD)") }
  var schoolAddress by remember { mutableStateOf("") }
  var schoolCity by remember { mutableStateOf("Jakarta Pusat") }
  var schoolPhone by remember { mutableStateOf("") }
  var schoolEmail by remember { mutableStateOf("") }
  var principalName by remember { mutableStateOf("") }

  var adminUsername by remember { mutableStateOf("") }
  var adminPassword by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  var passwordVisible by remember { mutableStateOf(false) }

  var assignmentLetterFileName by remember { mutableStateOf("Surat_Tugas_Penunjukan_Admin_2026.pdf") }
  var hasUploadedFile by remember { mutableStateOf(false) }

  var errorMessage by remember { mutableStateOf<String?>(null) }

  // Media Picker Launcher (Zero-permission photo/document picker)
  val filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      assignmentLetterFileName = "SK_Penugasan_Sekolah_${System.currentTimeMillis().toString().takeLast(4)}.pdf"
      hasUploadedFile = true
      Toast.makeText(context, "Dokumen surat tugas berhasil dipilih!", Toast.LENGTH_SHORT).show()
    }
  }

  // Dropdown States
  var expandedRole by remember { mutableStateOf(false) }
  val rolesList = listOf("Kepala Sekolah", "Wakil Kepala Sekolah", "Kepala Tata Usaha", "Operator IT / Dapodik")

  var expandedLevel by remember { mutableStateOf(false) }
  val levelsList = listOf(
    "Pendidikan Anak Usia Dini (PAUD/TK)",
    "Sekolah Dasar (SD / MI)",
    "Sekolah Menengah Pertama (SMP / MTs)",
    "Sekolah Menengah Atas/Kejuruan (SMA / SMK)"
  )

  fun submitRegistration() {
    errorMessage = null

    if (applicantName.isBlank()) {
      errorMessage = "Nama pemohon wajib diisi."
      return
    }
    if (applicantNik.length < 10) {
      errorMessage = "NIK harus valid (minimal 10-16 digit angka)."
      return
    }
    if (applicantPhone.isBlank()) {
      errorMessage = "Nomor WhatsApp / HP wajib diisi."
      return
    }
    if (schoolName.isBlank()) {
      errorMessage = "Nama sekolah resmi wajib diisi."
      return
    }
    if (schoolAddress.isBlank()) {
      errorMessage = "Alamat sekolah wajib diisi."
      return
    }
    if (adminUsername.isBlank() || adminPassword.isBlank()) {
      errorMessage = "Username dan kata sandi admin wajib ditentukan."
      return
    }
    if (adminPassword != confirmPassword) {
      errorMessage = "Kata sandi dan ulangi kata sandi tidak cocok!"
      return
    }

    val finalPrincipal = if (principalName.isBlank()) applicantName else principalName
    val finalLetterName = if (hasUploadedFile) assignmentLetterFileName else "SK_Penugasan_${schoolName.replace(" ", "_")}.pdf"

    onRegisterSuccess(
      schoolName.trim(),
      npsn.trim(),
      level,
      schoolAddress.trim(),
      schoolCity.trim(),
      if (schoolPhone.isBlank()) applicantPhone else schoolPhone.trim(),
      if (schoolEmail.isBlank()) "admin@${schoolName.lowercase().replace(" ", "")}.sch.id" else schoolEmail.trim(),
      finalPrincipal.trim(),
      applicantName.trim(),
      applicantNik.trim(),
      applicantPhone.trim(),
      applicantRole,
      applicantAddress.trim(),
      finalLetterName,
      adminUsername.trim(),
      adminPassword.trim()
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text("Pendaftaran Sekolah Baru 🏫", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1F2937))
            Text("Registrasi Lembaga & Admin Portal", fontSize = 11.sp, color = PastelPeach)
          }
        },
        navigationIcon = {
          IconButton(onClick = onNavigateToLogin, modifier = Modifier.testTag("register_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali ke Login")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
      )
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0xFFFFF9F5),
              Color(0xFFFBF4FF),
              Color(0xFFF3F4F6)
            )
          )
        )
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 18.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

      // Header Banner
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = PastelPeachLight,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = CircleShape,
            color = PastelPeach,
            modifier = Modifier.size(38.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.School, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              "Bergabung Bersama SahabatSekolah",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1F2937)
            )
            Text(
              "Daftarkan lembaga sekolah Anda untuk membuka portal guru, mading digital, rapor, dan sistem jemputan cerdas.",
              fontSize = 11.sp,
              color = Color(0xFF4B5563),
              lineHeight = 16.sp
            )
          }
        }
      }

      // SECTION 1: DATA PEMOHON (PIC)
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("1. Identitas Penanggung Jawab (Pemohon)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
          }

          HorizontalDivider(color = Color(0xFFF3F4F6))

          OutlinedTextField(
            value = applicantName,
            onValueChange = { applicantName = it },
            label = { Text("Nama Lengkap Pemohon & Gelar *") },
            placeholder = { Text("Contoh: Drs. H. Mulyono, M.Pd") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = applicantNik,
              onValueChange = { if (it.length <= 16) applicantNik = it },
              label = { Text("NIK (16 Digit) *") },
              placeholder = { Text("3171051203...") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
              value = applicantPhone,
              onValueChange = { applicantPhone = it },
              label = { Text("No. WhatsApp / HP *") },
              placeholder = { Text("08128900...") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
              singleLine = true,
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp)
            )
          }

          // Jabatan Dropdown
          ExposedDropdownMenuBox(
            expanded = expandedRole,
            onExpandedChange = { expandedRole = !expandedRole }
          ) {
            OutlinedTextField(
              value = applicantRole,
              onValueChange = {},
              readOnly = true,
              label = { Text("Jabatan di Sekolah *") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
              modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
              expanded = expandedRole,
              onDismissRequest = { expandedRole = false }
            ) {
              rolesList.forEach { r ->
                DropdownMenuItem(
                  text = { Text(r, fontSize = 13.sp) },
                  onClick = {
                    applicantRole = r
                    expandedRole = false
                  }
                )
              }
            }
          }

          OutlinedTextField(
            value = applicantAddress,
            onValueChange = { applicantAddress = it },
            label = { Text("Alamat Domisili Pemohon") },
            placeholder = { Text("Contoh: Jl. Diponegoro No. 12, Menteng") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
        }
      }

      // SECTION 2: DATA SEKOLAH / LEMBAGA
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Business, contentDescription = null, tint = PastelSky, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("2. Profil Lembaga Pendidikan / Sekolah", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
          }

          HorizontalDivider(color = Color(0xFFF3F4F6))

          OutlinedTextField(
            value = schoolName,
            onValueChange = { schoolName = it },
            label = { Text("Nama Resmi Sekolah *") },
            placeholder = { Text("Contoh: SD Ceria Bangsa / SDIT Insan Mulia") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = npsn,
              onValueChange = { npsn = it },
              label = { Text("NPSN Sekolah") },
              placeholder = { Text("20104829") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
              value = schoolCity,
              onValueChange = { schoolCity = it },
              label = { Text("Kota / Kabupaten") },
              placeholder = { Text("Jakarta Pusat") },
              singleLine = true,
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp)
            )
          }

          // Jenjang Dropdown
          ExposedDropdownMenuBox(
            expanded = expandedLevel,
            onExpandedChange = { expandedLevel = !expandedLevel }
          ) {
            OutlinedTextField(
              value = level,
              onValueChange = {},
              readOnly = true,
              label = { Text("Jenjang Pendidikan *") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLevel) },
              modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
              expanded = expandedLevel,
              onDismissRequest = { expandedLevel = false }
            ) {
              levelsList.forEach { lvl ->
                DropdownMenuItem(
                  text = { Text(lvl, fontSize = 13.sp) },
                  onClick = {
                    level = lvl
                    expandedLevel = false
                  }
                )
              }
            }
          }

          OutlinedTextField(
            value = schoolAddress,
            onValueChange = { schoolAddress = it },
            label = { Text("Alamat Lengkap Gedung Sekolah *") },
            placeholder = { Text("Contoh: Jl. Cikini Raya No. 45, RT 02 / RW 05") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = schoolPhone,
              onValueChange = { schoolPhone = it },
              label = { Text("Telepon Kantor") },
              placeholder = { Text("021-3908271") },
              singleLine = true,
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
              value = principalName,
              onValueChange = { principalName = it },
              label = { Text("Nama Kepala Sekolah") },
              placeholder = { Text("Drs. H. Mulyono") },
              singleLine = true,
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp)
            )
          }
        }
      }

      // SECTION 3: AKUN LOGIN ADMIN SEKOLAH
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Security, contentDescription = null, tint = PastelLilac, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("3. Akun Administrator Sekolah", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
          }

          HorizontalDivider(color = Color(0xFFF3F4F6))

          OutlinedTextField(
            value = adminUsername,
            onValueChange = { adminUsername = it },
            label = { Text("Username Admin Baru *") },
            placeholder = { Text("Contoh: admin_ceriabangsa") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          OutlinedTextField(
            value = adminPassword,
            onValueChange = { adminPassword = it },
            label = { Text("Kata Sandi *") },
            placeholder = { Text("Minimal 6 karakter") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PastelLilac) },
            trailingIcon = {
              IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
              }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Ulangi Kata Sandi *") },
            placeholder = { Text("Ketik ulang kata sandi di atas") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PastelLilac) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )
        }
      }

      // SECTION 4: UPLOAD SURAT PENUGASAN SEKOLAH
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("4. Dokumen Legalitas & Surat Penugasan", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1F2937))
          }

          HorizontalDivider(color = Color(0xFFF3F4F6))

          Text(
            text = "Lampirkan Surat Tugas dari Kepala Sekolah / SK Yayasan resmi yang menugaskan Anda sebagai Administrator SahabatSekolah.",
            fontSize = 11.sp,
            color = Color(0xFF4B5563),
            lineHeight = 16.sp
          )

          // Upload Container
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFF9FAFB),
            border = BorderStroke(1.dp, if (hasUploadedFile) Color(0xFF10B981) else Color(0xFFD1D5DB)),
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                filePickerLauncher.launch(
                  androidx.activity.result.PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                  )
                )
              }
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                if (hasUploadedFile) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                contentDescription = null,
                tint = if (hasUploadedFile) Color(0xFF10B981) else PastelSky,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = if (hasUploadedFile) "Dokumen Surat Tugas Terpilih" else "Unggah Surat Penugasan / SK Sekolah",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (hasUploadedFile) Color(0xFF065F46) else Color(0xFF1F2937)
              )
              Text(
                text = assignmentLetterFileName,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedButton(
                onClick = {
                  filePickerLauncher.launch(
                    androidx.activity.result.PickVisualMediaRequest(
                      ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                  )
                },
                shape = RoundedCornerShape(10.dp)
              ) {
                Text(if (hasUploadedFile) "Ganti Dokumen" else "Pilih Berkas PDF / Foto SK", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // Error Message Box
      AnimatedVisibility(visible = errorMessage != null) {
        errorMessage?.let { error ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFEE2E2),
            border = BorderStroke(1.dp, Color(0xFFEF4444)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFDC2626))
              Spacer(modifier = Modifier.width(10.dp))
              Text(text = error, fontSize = 12.sp, color = Color(0xFFB91C1C), fontWeight = FontWeight.Medium)
            }
          }
        }
      }

      // Submit Button
      Button(
        onClick = { submitRegistration() },
        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("submit_registration_button")
      ) {
        Text("Daftarkan Sekolah & Buka Panel Admin", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
      }

      // Back to Login text button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Sudah memiliki akun sekolah? ", fontSize = 12.sp, color = Color.Gray)
        TextButton(onClick = onNavigateToLogin) {
          Text("Masuk di Sini", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PastelPeach)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}
