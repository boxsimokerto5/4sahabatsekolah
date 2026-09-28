package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.PastelYellowLight

@Composable
fun SupabaseConfigDialog(
  initialUrl: String,
  initialKey: String,
  isConfigured: Boolean,
  sqlSchema: String,
  onSave: (url: String, key: String) -> Unit,
  onTestConnection: (url: String, key: String, (Boolean, String) -> Unit) -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  var url by remember { mutableStateOf(initialUrl) }
  var key by remember { mutableStateOf(initialKey) }
  var isTesting by remember { mutableStateOf(false) }
  var testResult by remember { mutableStateOf<Pair<Boolean, String>?>(null) }
  var showSqlViewer by remember { mutableStateOf(false) }
  var copiedToClipboard by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(if (isConfigured) PastelMintLight else PastelLilacLight),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isConfigured) Icons.Default.CloudDone else Icons.Default.CloudQueue,
            contentDescription = null,
            tint = if (isConfigured) PastelMint else PastelLilac,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Integrasi Supabase Cloud",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
          )
          Text(
            text = if (isConfigured) "Status: Terhubung ke Cloud 🟢" else "Status: Mode Lokal (Offline Room)",
            fontSize = 11.sp,
            color = if (isConfigured) PastelMint else Color(0xFF6B7280),
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "Aplikasi ini mendukung sinkronisasi real-time ke database Supabase Anda. Jika belum diisi, aplikasi tetap berjalan lancar menggunakan Room Database lokal.",
          fontSize = 12.sp,
          color = Color(0xFF4B5563),
          lineHeight = 16.sp
        )

        OutlinedTextField(
          value = url,
          onValueChange = {
            url = it
            testResult = null
          },
          label = { Text("Supabase Project URL") },
          placeholder = { Text("https://xxx.supabase.co") },
          leadingIcon = { Icon(Icons.Default.Link, contentDescription = null, tint = PastelSky) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("supabase_url_input")
        )

        OutlinedTextField(
          value = key,
          onValueChange = {
            key = it
            testResult = null
          },
          label = { Text("Supabase Anon Public Key") },
          placeholder = { Text("eyJhbGciOiJIUzI1NiIsInR5cCI6...") },
          leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = PastelYellow) },
          maxLines = 3,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("supabase_key_input")
        )

        // Test status banner
        testResult?.let { res ->
          Surface(
            color = if (res.first) PastelMintLight else PastelPeachLight,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = res.second,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = if (res.first) Color(0xFF166534) else Color(0xFF991B1B),
              modifier = Modifier.padding(10.dp)
            )
          }
        }

        // Test Connection Button
        Button(
          onClick = {
            isTesting = true
            onTestConnection(url, key) { success, msg ->
              isTesting = false
              testResult = Pair(success, msg)
            }
          },
          enabled = !isTesting && url.isNotBlank() && key.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("test_supabase_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = PastelSky)
        ) {
          if (isTesting) {
            CircularProgressIndicator(
              color = Color.White,
              modifier = Modifier.size(20.dp),
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Menghubungkan...", fontSize = 12.sp)
          } else {
            Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Uji Koneksi & Sync Cloud", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }

        // Button to show SQL schema
        OutlinedButton(
          onClick = { showSqlViewer = true },
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .testTag("view_sql_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Lihat Kode SQL Tabel Supabase", fontSize = 12.sp)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSave(url, key)
          onDismiss()
        },
        colors = ButtonDefaults.buttonColors(containerColor = PastelMint),
        modifier = Modifier.testTag("save_supabase_button")
      ) {
        Text("Simpan")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Tutup")
      }
    }
  )

  // SQL Schema Modal Viewer
  if (showSqlViewer) {
    AlertDialog(
      onDismissRequest = { showSqlViewer = false },
      title = {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Script SQL Supabase", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Surface(
            color = if (copiedToClipboard) PastelMintLight else PastelSkyLight,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.clickable {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("SahabatSekolah Supabase SQL", sqlSchema)
              clipboard.setPrimaryClip(clip)
              copiedToClipboard = true
            }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (copiedToClipboard) Icons.Default.Check else Icons.Default.ContentCopy,
                contentDescription = "Salin SQL",
                tint = if (copiedToClipboard) PastelMint else PastelSky,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (copiedToClipboard) "Tersalin!" else "Salin SQL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (copiedToClipboard) PastelMint else PastelSky
              )
            }
          }
        }
      },
      text = {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF1E1E2E), RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Text(
            text = sqlSchema,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = Color(0xFFCDD6F4)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { showSqlViewer = false },
          colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
        ) {
          Text("Selesai")
        }
      }
    )
  }
}
