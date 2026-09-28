package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.UserRole
import com.example.ui.theme.PastelLilac
import com.example.ui.theme.PastelLilacLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
  messages: List<ChatMessage>,
  currentRole: UserRole,
  onSendMessage: (String) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  val quickReplies = listOf(
    "Baik Bu Guru, terima kasih infonya! ❤️",
    "Ananda Rafa hari ini sehat & ceria.",
    "Apakah ada buku yang harus dibawa besok?",
    "Terima kasih atas bimbingannya Bu Sarah."
  )

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = if (currentRole == UserRole.PARENT) "Bu Sarah (Wali Kelas 2B)" else "Bunda Dina (Wali Murid Rafa)",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Text(
              text = "Online • Respon Cepat Sekolah",
              fontSize = 11.sp,
              color = Color(0xFF10B981)
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = PastelLilacLight,
          titleContentColor = Color(0xFF1F2937)
        )
      )
    },
    bottomBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .padding(8.dp)
      ) {
        // Quick Replies
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
          items(quickReplies) { reply ->
            Surface(
              color = PastelLilacLight,
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.clickable {
                inputText = reply
              }
            ) {
              Text(
                text = reply,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = PastelLilac,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Input Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text("Ketik pesan untuk ${if (currentRole == UserRole.PARENT) "Bu Guru" else "Bunda"}...") },
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input_field"),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
              focusedContainerColor = PastelLilacLight.copy(alpha = 0.5f),
              unfocusedContainerColor = PastelLilacLight.copy(alpha = 0.3f),
              focusedIndicatorColor = PastelLilac,
              unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = false,
            maxLines = 3
          )

          Spacer(modifier = Modifier.width(8.dp))

          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(PastelLilac)
              .clickable {
                if (inputText.isNotBlank()) {
                  onSendMessage(inputText)
                  inputText = ""
                }
              }
              .testTag("send_chat_button"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.Send,
              contentDescription = "Kirim",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("chat_messages_list"),
      state = listState,
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(messages) { msg ->
        val isMine = if (currentRole == UserRole.PARENT) {
          msg.senderRole == "ORANG_TUA"
        } else {
          msg.senderRole == "GURU"
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
        ) {
          Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.82f)
          ) {
            Text(
              text = "${msg.senderName} • ${msg.time}",
              fontSize = 11.sp,
              color = Color(0xFF6B7280),
              modifier = Modifier.padding(bottom = 2.dp)
            )

            Surface(
              shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMine) 16.dp else 4.dp,
                bottomEnd = if (isMine) 4.dp else 16.dp
              ),
              color = if (isMine) PastelPeach else PastelLilacLight,
              shadowElevation = 1.dp
            ) {
              Text(
                text = msg.text,
                fontSize = 14.sp,
                color = if (isMine) Color.White else Color(0xFF1F2937),
                lineHeight = 19.sp,
                modifier = Modifier.padding(12.dp)
              )
            }
          }
        }
      }
    }
  }
}
