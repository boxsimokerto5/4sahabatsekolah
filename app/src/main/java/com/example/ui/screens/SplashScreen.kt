package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelSky
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onNavigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scale = remember { Animatable(0.85f) }

  LaunchedEffect(Unit) {
    scale.animateTo(
      targetValue = 1.05f,
      animationSpec = infiniteRepeatable(
        animation = tween(1200, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
      )
    )
  }

  // Automatic delay transition
  LaunchedEffect(Unit) {
    delay(2200)
    onNavigateToLogin()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFFFFF7F2),
            Color(0xFFFFECE5),
            Color(0xFFF3E8FF)
          )
        )
      )
      .clickable { onNavigateToLogin() }
      .testTag("splash_screen_root"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 28.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {

      // Elevated School Mascot / Logo Card
      Surface(
        shape = CircleShape,
        shadowElevation = 12.dp,
        color = Color.White,
        modifier = Modifier
          .size(130.dp)
          .scale(scale.value)
          .border(4.dp, Color.White, CircleShape)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Image(
            painter = painterResource(id = R.drawable.app_icon_1790519753543),
            contentDescription = "Logo SahabatSekolah",
            modifier = Modifier
              .fillMaxSize()
              .clip(CircleShape),
            contentScale = ContentScale.Crop
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // School & Platform Branding
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = PastelPeach.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, PastelPeach.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Default.School,
            contentDescription = null,
            tint = PastelPeach,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "SD CERIA BANGSA",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PastelPeach,
            letterSpacing = 1.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "SahabatSekolah",
        fontSize = 32.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF1F2937),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Jembatan Kasih & Komunikasi\nSekolah dan Keluarga Terpadu",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF4B5563),
        textAlign = TextAlign.Center,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Feature Badges
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.White.copy(alpha = 0.85f),
          shadowElevation = 2.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = PastelPeach, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("Aman & Nyaman", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
          }
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.White.copy(alpha = 0.85f),
          shadowElevation = 2.dp
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = PastelSky, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("Resmi Sekolah", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
          }
        }
      }

      Spacer(modifier = Modifier.height(40.dp))

      // Loading Progress & Manual Enter Button
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        LinearProgressIndicator(
          modifier = Modifier
            .width(160.dp)
            .height(5.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = PastelPeach,
          trackColor = Color.White.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = onNavigateToLogin,
          colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("splash_continue_button")
        ) {
          Text("Mulai Masuk", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White)
        }
      }
    }

    // Bottom Footer
    Text(
      text = "Versi 2.4 • SahabatSekolah Mobile",
      fontSize = 11.sp,
      color = Color(0xFF9CA3AF),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 20.dp)
    )
  }
}
