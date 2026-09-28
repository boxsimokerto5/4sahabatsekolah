package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Business
import com.example.R
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSky
import com.example.ui.theme.PastelSkyLight
import com.example.ui.theme.PastelYellowLight

@Composable
fun LoginScreen(
  onLogin: (String, String) -> Boolean,
  onQuickLoginTeacher: () -> Unit,
  onQuickLoginParent: () -> Unit,
  onQuickLoginAdmin: () -> Unit,
  onNavigateToRegistration: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Wali Murid, 1: Guru, 2: Admin Sekolah
  var username by remember { mutableStateOf("ortu") }
  var password by remember { mutableStateOf("ortu") }
  var passwordVisible by remember { mutableStateOf(false) }
  var rememberMe by remember { mutableStateOf(true) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  // Update default credentials when user switches tabs
  fun handleTabChange(index: Int) {
    selectedTab = index
    errorMessage = null
    when (index) {
      0 -> {
        username = "ortu"
        password = "ortu"
      }
      1 -> {
        username = "guru"
        password = "guru"
      }
      2 -> {
        username = "admin"
        password = "admin"
      }
    }
  }

  fun performLogin() {
    errorMessage = null
    val success = onLogin(username, password)
    if (!success) {
      errorMessage = "Kredensial tidak valid. Coba 'admin'/'admin', 'guru'/'guru', atau 'ortu'/'ortu'."
    }
  }

  val activeColor = when (selectedTab) {
    0 -> PastelPeach
    1 -> PastelLilac
    else -> PastelSky
  }
  val activeLightColor = when (selectedTab) {
    0 -> PastelPeachLight
    1 -> PastelLilacLight
    else -> PastelSkyLight
  }

  Column(
    modifier = modifier
      .fillMaxSize()
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
      .padding(horizontal = 22.dp, vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    Spacer(modifier = Modifier.height(16.dp))

    // Top Header / Mascot
    Surface(
      shape = CircleShape,
      color = Color.White,
      shadowElevation = 6.dp,
      modifier = Modifier.size(76.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Image(
          painter = painterResource(id = R.drawable.app_icon_1790519753543),
          contentDescription = "Logo",
          modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape),
          contentScale = ContentScale.Crop
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "SahabatSekolah",
      fontSize = 24.sp,
      fontWeight = FontWeight.ExtraBold,
      color = Color(0xFF1F2937)
    )

    Text(
      text = "SD Ceria Bangsa • Portal Masuk Terpadu",
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      color = Color.Gray
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Main Login Card
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("login_card")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {

        // Role Segmented Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color(0xFFF3F4F6),
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth(),
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = activeColor,
              height = 3.dp
            )
          },
          divider = {}
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { handleTabChange(0) },
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.FamilyRestroom,
                  contentDescription = null,
                  tint = if (selectedTab == 0) PastelPeach else Color.Gray,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  "Wali Murid",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (selectedTab == 0) PastelPeach else Color.Gray
                )
              }
            }
          )

          Tab(
            selected = selectedTab == 1,
            onClick = { handleTabChange(1) },
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.School,
                  contentDescription = null,
                  tint = if (selectedTab == 1) PastelLilac else Color.Gray,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  "Guru",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (selectedTab == 1) PastelLilac else Color.Gray
                )
              }
            }
          )

          Tab(
            selected = selectedTab == 2,
            onClick = { handleTabChange(2) },
            text = {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  Icons.Default.Business,
                  contentDescription = null,
                  tint = if (selectedTab == 2) PastelSky else Color.Gray,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  "Admin",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (selectedTab == 2) PastelSky else Color.Gray
                )
              }
            }
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Role Hint Banner
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = activeLightColor,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              Icons.Default.Info,
              contentDescription = null,
              tint = activeColor,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = when (selectedTab) {
                0 -> "Masuk untuk memantau kehadiran, rapor, tabungan, dan penjemputan Ananda Rafa."
                1 -> "Portal guru untuk mengelola bel pulang, absensi kelas, nilai rapor, dan mading."
                else -> "Portal Administrator Sekolah untuk kelola profil lembaga, room kelas, akun guru & murid."
              },
              fontSize = 11.sp,
              color = Color(0xFF374151),
              lineHeight = 15.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username Field
        OutlinedTextField(
          value = username,
          onValueChange = {
            username = it
            errorMessage = null
          },
          label = {
            Text(
              when (selectedTab) {
                0 -> "Username / NISN Siswa"
                1 -> "Username / NIP Guru"
                else -> "Username Admin Sekolah"
              }
            )
          },
          placeholder = {
            Text(
              when (selectedTab) {
                0 -> "Ketik: ortu"
                1 -> "Ketik: guru"
                else -> "Ketik: admin"
              }
            )
          },
          leadingIcon = {
            Icon(Icons.Default.Person, contentDescription = null, tint = activeColor)
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_username_field"),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = activeColor,
            focusedLabelColor = activeColor
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Field
        OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            errorMessage = null
          },
          label = { Text("Kata Sandi") },
          placeholder = { Text(if (selectedTab == 0) "Ketik: ortu" else "Ketik: guru") },
          leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = activeColor)
          },
          trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
              Icon(
                if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = if (passwordVisible) "Sembunyikan sandi" else "Tampilkan sandi"
              )
            }
          },
          visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
          keyboardActions = KeyboardActions(onDone = { performLogin() }),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("login_password_field"),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = activeColor,
            focusedLabelColor = activeColor
          )
        )

        // Remember Me & Forgot Password
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
              checked = rememberMe,
              onCheckedChange = { rememberMe = it },
              colors = CheckboxDefaults.colors(checkedColor = activeColor)
            )
            Text("Ingat Saya", fontSize = 11.sp, color = Color(0xFF4B5563))
          }

          Text(
            text = "Lupa Sandi?",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = activeColor,
            modifier = Modifier.clickable {
              errorMessage = "Untuk demo: Gunakan 'guru'/'guru' atau 'ortu'/'ortu'."
            }
          )
        }

        // Error message box
        AnimatedVisibility(visible = errorMessage != null) {
          errorMessage?.let { error ->
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFFEE2E2),
              border = BorderStroke(1.dp, Color(0xFFEF4444)),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.ErrorOutline,
                  contentDescription = null,
                  tint = Color(0xFFDC2626),
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = error,
                  fontSize = 11.sp,
                  color = Color(0xFFB91C1C)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Submit Button
        Button(
          onClick = { performLogin() },
          colors = ButtonDefaults.buttonColors(containerColor = activeColor),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("login_submit_button")
        ) {
          Icon(Icons.Default.Login, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = when (selectedTab) {
              0 -> "Masuk sebagai Wali Murid"
              1 -> "Masuk sebagai Guru"
              else -> "Masuk sebagai Admin Sekolah"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Prominent School Registration CTA Banner
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = Color.White,
      border = BorderStroke(1.5.dp, PastelPeach.copy(alpha = 0.5f)),
      shadowElevation = 2.dp,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp))
        .clickable { onNavigateToRegistration() }
        .testTag("register_school_banner")
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
              Icon(Icons.Default.School, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(20.dp))
            }
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              "Sekolah Anda Belum Terdaftar?",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1F2937)
            )
            Text(
              "Daftar sekolah baru & upload SK tugas",
              fontSize = 10.sp,
              color = Color.Gray
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = PastelPeach
        ) {
          Text(
            text = "Daftar 📝",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Quick 1-Click Demo Logins Section
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
        Text(
          text = "  Akses Cepat Penguji (1-Klik)  ",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF6B7280)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Admin 1-click
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color.White,
          border = BorderStroke(1.dp, PastelSky.copy(alpha = 0.4f)),
          shadowElevation = 1.dp,
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onQuickLoginAdmin() }
            .testTag("quick_login_admin")
        ) {
          Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Surface(
              shape = CircleShape,
              color = PastelSkyLight,
              modifier = Modifier.size(32.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Business, contentDescription = null, tint = PastelSky, modifier = Modifier.size(16.dp))
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Admin Sekolah", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Text("admin/admin", fontSize = 9.sp, color = PastelSky, fontWeight = FontWeight.SemiBold)
            Text("Kepala Sekolah", fontSize = 9.sp, color = Color.Gray)
          }
        }

        // Teacher 1-click
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color.White,
          border = BorderStroke(1.dp, PastelLilac.copy(alpha = 0.4f)),
          shadowElevation = 1.dp,
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onQuickLoginTeacher() }
            .testTag("quick_login_teacher")
        ) {
          Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Surface(
              shape = CircleShape,
              color = PastelLilacLight,
              modifier = Modifier.size(32.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.School, contentDescription = null, tint = PastelLilac, modifier = Modifier.size(16.dp))
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Bu Guru Sarah", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Text("guru/guru", fontSize = 9.sp, color = PastelLilac, fontWeight = FontWeight.SemiBold)
            Text("Wali Kelas 2-B", fontSize = 9.sp, color = Color.Gray)
          }
        }

        // Parent 1-click
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color.White,
          border = BorderStroke(1.dp, PastelPeach.copy(alpha = 0.4f)),
          shadowElevation = 1.dp,
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onQuickLoginParent() }
            .testTag("quick_login_parent")
        ) {
          Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Surface(
              shape = CircleShape,
              color = PastelPeachLight,
              modifier = Modifier.size(32.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(16.dp))
              }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Bunda Dina", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
            Text("ortu/ortu", fontSize = 9.sp, color = PastelPeach, fontWeight = FontWeight.SemiBold)
            Text("Ortu Rafa (2-B)", fontSize = 9.sp, color = Color.Gray)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "Keamanan Data Siswa & Keluarga Terjamin • SD Ceria Bangsa",
      fontSize = 11.sp,
      color = Color(0xFF9CA3AF),
      textAlign = TextAlign.Center
    )
  }
}
