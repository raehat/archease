package com.example.particleapp.ui.archEaseScreen.homescreen

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.particleapp.ui.archEaseScreen.ArchEaseViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.particleapp.R
import com.example.particleapp.data.Payment
import com.example.particleapp.ui.archEaseScreen.Screen
import com.example.particleapp.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ViewSentTransactionsScreen(
    navController: NavHostController,
    viewModel: ArchEaseViewModel,
    showToast: (String) -> Unit,
    current: Context
) {
    var isReverted by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderScreen(onBackPressed = { navController.popBackStack() })
        var payments by remember { mutableStateOf<List<Payment>>(emptyList()) }
        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                val sharedPrefs = current.getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
                val mnemonic = sharedPrefs.getString("mnemonic", "").toString()
                val address = Network.getAddr(mnemonic)!!.address
                payments = Network.getSentPaymentsR(address).Ok
            }
            isLoading = false
        }
        if (isLoading) Spacer(modifier = Modifier.height(30.dp))
        LoadingCircle(isVisible = isLoading)
        LazyColumn {
            items(payments.size) { index ->
                SentPayment(
                    sentTo = payments[index].receiver,
                    amount = payments[index].amount.toDouble(),
                    reverted = payments[index].reverted,
                    claimed = payments[index].claimed,
                    onRevertClicked = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val sharedPrefs = current.getSharedPreferences("MySharedPrefs",
                                Context.MODE_PRIVATE
                            )
                            val mnemonic = sharedPrefs.getString("mnemonic", "").toString()
                            Network.revertPaymentR(mnemonic, payments[index].payment_id.toString())
                            navController.navigate(Screen.PaymentCompletedScreen)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun SentPayment(
    sentTo: String,
    amount: Double,
    reverted: Boolean,
    claimed: Boolean,
    onRevertClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Sent to: $sentTo"
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount: $amount arch",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.archway),
                    contentDescription = "arch Logo",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Claimed: ${if (claimed) "True" else "False"}",
                textAlign = TextAlign.End,
                color = if (claimed) Color.Red else Color.Green
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onRevertClicked()
                },
                modifier = Modifier.align(Alignment.End),
//                colors = ButtonDefaults.buttonColors(contentColor = if (reverted) Color.LightGray else MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(text = if (reverted) "Already revered" else "Revert")
            }
        }
    }
}