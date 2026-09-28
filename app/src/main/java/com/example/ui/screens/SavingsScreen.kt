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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.model.SavingTransaction
import com.example.ui.UserRole
import com.example.ui.theme.PastelMint
import com.example.ui.theme.PastelMintContainer
import com.example.ui.theme.PastelMintLight
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelYellow
import com.example.ui.theme.PastelYellowLight
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsScreen(
  transactions: List<SavingTransaction>,
  totalBalance: Long,
  currentRole: UserRole,
  onAddSavings: (amount: Long, type: String, note: String) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onNavigateBack() }

  var showAddDialog by remember { mutableStateOf(false) }

  val targetSavings = 250000L
  val progress = (totalBalance.toFloat() / targetSavings.toFloat()).coerceIn(0f, 1f)
  val percent = (progress * 100).toInt()

  val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
  val formattedBalance = currencyFormatter.format(totalBalance).replace(",00", "")
  val formattedTarget = currencyFormatter.format(targetSavings).replace(",00", "")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "Tabungan Siswa (Celengan Digital)",
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
          containerColor = PastelMintLight,
          titleContentColor = Color(0xFF1F2937)
        )
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = PastelMint,
        contentColor = Color.White,
        modifier = Modifier.testTag("add_savings_fab")
      ) {
        Icon(Icons.Default.Add, contentDescription = "Tambah Tabungan")
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .testTag("savings_screen_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Main Balance Card
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = PastelMintLight),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "TOTAL SALDO TABUNGAN RAFA",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = PastelMint
                )
                Text(
                  text = formattedBalance,
                  fontSize = 30.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF1F2937)
                )
              }

              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(CircleShape)
                  .background(PastelMint),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Savings,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(32.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Savings Target Tracker
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = Color.White,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "🎯 Target: Study Tour Museum Cilik",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151)
                  )
                  Text(
                    text = "$percent%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PastelMint
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                  progress = { progress },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                  color = PastelMint,
                  trackColor = PastelMintContainer
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = "$formattedBalance dari target $formattedTarget tercapai",
                  fontSize = 11.sp,
                  color = Color(0xFF6B7280)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action button
            Button(
              onClick = { showAddDialog = true },
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = PastelMint)
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (currentRole == UserRole.TEACHER) "Catat Tabungan Siswa" else "Titip Setor Tabungan",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }
        }
      }

      // Mutation List Title
      item {
        Text(
          text = "Riwayat Transaksi Tabungan",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1F2937)
        )
      }

      // Transactions
      items(transactions) { tx ->
        val isDeposit = tx.type == "MASUK"
        val itemFormatted = currencyFormatter.format(tx.amount).replace(",00", "")

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(if (isDeposit) PastelMintLight else PastelPeachLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isDeposit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = if (isDeposit) PastelMint else PastelPeach,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = tx.note,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
              )
              Text(
                text = tx.formattedDate,
                fontSize = 11.sp,
                color = Color(0xFF6B7280)
              )
            }

            Text(
              text = if (isDeposit) "+ $itemFormatted" else "- $itemFormatted",
              fontSize = 14.sp,
              fontWeight = FontWeight.ExtraBold,
              color = if (isDeposit) PastelMint else PastelPeach
            )
          }
        }
      }
    }
  }

  // Dialog Add Savings
  if (showAddDialog) {
    var amountStr by remember { mutableStateOf("20000") }
    var note by remember { mutableStateOf("Setoran Tabungan Harian") }
    var txType by remember { mutableStateOf("MASUK") }

    AlertDialog(
      onDismissRequest = { showAddDialog = false },
      title = { Text("Catat Tabungan Siswa", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
              onClick = { txType = "MASUK" },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (txType == "MASUK") PastelMint else PastelMintLight,
                contentColor = if (txType == "MASUK") Color.White else PastelMint
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Setor (Masuk)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = { txType = "KELUAR" },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (txType == "KELUAR") PastelPeach else PastelPeachLight,
                contentColor = if (txType == "KELUAR") Color.White else PastelPeach
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Tarik (Keluar)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }

          OutlinedTextField(
            value = amountStr,
            onValueChange = { amountStr = it },
            label = { Text("Jumlah (Rp)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )

          OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Catatan / Keterangan") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val amount = amountStr.toLongOrNull() ?: 10000L
            onAddSavings(amount, txType, note)
            showAddDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PastelMint)
        ) {
          Text("Simpan")
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
