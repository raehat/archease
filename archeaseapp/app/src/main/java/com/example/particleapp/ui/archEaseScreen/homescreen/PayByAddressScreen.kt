package com.example.particleapp.ui.archEaseScreen.homescreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.particleapp.R
import com.example.particleapp.ui.archEaseScreen.ArchEaseViewModel
import com.example.particleapp.ui.archEaseScreen.Screen
import com.example.particleapp.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.particle.chains.ChainInfo

@Composable
fun PayByAddressScreen(
    navController: NavHostController,
    viewModel: ArchEaseViewModel,
    showToast: (String) -> Unit,
    current: Context
) {
    var receivingAddress by remember { mutableStateOf(viewModel.paymentData.address) }
    var amountToBeSent by remember { mutableStateOf(viewModel.paymentData.amount) }
    var resp by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
        .padding(top = 40.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderScreen(onBackPressed = { navController.popBackStack() })
        ReceivingAddressCard(viewModel.paymentData.address, onTextChanged = {
            receivingAddress = it
        })
        AmountToBeSentCard(navController, viewModel, amountToBeSent, onTextChanged = {
            amountToBeSent = it
        })
        Spacer(modifier = Modifier.height(10.dp))
//        SourceChainCard(viewModel, navController)
//        DestinationChainCard(viewModel, navController)
        Spacer(modifier = Modifier.height(20.dp))

        var isLoading by remember { mutableStateOf(false) }
        SendCryptoButton(onClick = {
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
                val sharedPref = current.getSharedPreferences("MySharedPrefs", android.content.Context.MODE_PRIVATE)
                val mnemonic = sharedPref.getString("mnemonic", "").toString()
                resp = Network.sendPaymentR(mnemonic, receivingAddress, amountToBeSent)!!.transactionHash
//                Blockchain.sendCrossChainPayment(
//                    amountToBeSent,
//                    ParticleNetwork.chainName.lowercase(),
//                    viewModel.paymentData.chainName.lowercase(),
//                    receivingAddress,
//                    showToast,
//                    onSuccess = {
//                        isLoading = false
//                        showToast("Payment successful")
//                        viewModel.paymentSuccess = true
//                        navController.navigate(Screen.PaymentCompletedScreen)
//                    },
//                    onFailed = {
//                        isLoading = false
//                        showToast("Payment Failed")
//                        viewModel.paymentSuccess = false
//                        navController.navigate(Screen.PaymentCompletedScreen)
//                    }
//                )
            }
            if (resp.isNotEmpty()) {
                showToast("Transaction hash: $resp")
                navController.navigate(Screen.PaymentCompletedScreen)
            }
        })
        Spacer(modifier = Modifier.height(30.dp))
        LoadingCircle(isVisible = isLoading)
    }
}

@Composable
fun LoadingCircle(isVisible: Boolean) {
    if (isVisible) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() 
        }
    }
}


@Composable
fun SendCryptoButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = {
            onClick()
        }) {
        Text(text = "Send", fontSize = 16.sp)
    }
}

@Composable
fun DestinationChainCard(viewModel: ArchEaseViewModel, navController: NavHostController) {
//    val destionationChainInfo = ChainInfo.getChain(viewModel.qrCodeData.chainId.toLong(), viewModel.qrCodeData.chainName)
//    if (destionationChainInfo != null)
//        viewModel.destinationChainInfo = destionationChainInfo
    Column {
        Text(modifier = Modifier
            .padding(horizontal = 16.dp),
            text = "Destination Chain")
        Spacer(modifier = Modifier.height(3.dp))
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.navigate(Screen.SelectDestinationChainScreen) }
            ,
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                AsyncImage(model = ChainInfo.getChain(
                    viewModel.paymentData.chainId.toLong(),
                    viewModel.paymentData.chainName
                )?.icon, contentDescription = null)
                Spacer(modifier = Modifier.width(15.dp))
                Text(text = ChainInfo.getChain(
                    viewModel.paymentData.chainId.toLong(),
                    viewModel.paymentData.chainName
                )?.fullname.toString())
            }
        }
    }
}

@Composable
fun SourceChainCard(viewModel: ArchEaseViewModel, navController: NavHostController) {
    Column {
        Text(modifier = Modifier
            .padding(horizontal = 16.dp),
            text = "Source Chain")
        Spacer(modifier = Modifier.height(3.dp))
        Card(
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.navigate(Screen.SwitchChainScreen) }
            ,
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                AsyncImage(model = viewModel.particleNetwork().chainInfo.icon, contentDescription = null)
                Spacer(modifier = Modifier.width(15.dp))
                Text(text = viewModel.particleNetwork().chainInfo.fullname)
            }
        }
    }
}

@Composable
fun AmountToBeSentCard(navController: NavHostController, viewModel: ArchEaseViewModel, amount: String, onTextChanged: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = { onTextChanged(it) },
            placeholder = { Text("Amount") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .padding(16.dp)
                .weight(0.75f)
        )

        var nativeTokenSelected by remember { mutableStateOf(true) }
//        Column(
//            modifier = Modifier
//                .weight(0.25f)
//                .background(MaterialTheme.colorScheme.background),
//            verticalArrangement = Arrangement.Center
//        ) {
//            Row(
//                modifier = Modifier
//                    .clickable { nativeTokenSelected = !nativeTokenSelected },
//                horizontalArrangement = Arrangement.Center
//            ) {
//                if (nativeTokenSelected) {
//                    AsyncImage(model = viewModel.particleNetwork().chainInfo.icon, contentDescription = null)
//                    Spacer(modifier = Modifier.width(15.dp))
//                    Text(text = viewModel.particleNetwork().chainInfo.nativeCurrency.symbol)
//                }
//                else {
                    Image(
                        painter = painterResource(id = R.drawable.archway),
                        contentDescription = null,
                        modifier = Modifier
                            .size(26.dp)
                    )
//                    Spacer(modifier = Modifier.width(3.dp))
//                    Text(text = "USDC")
//                }
//            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "arch", fontSize = 16.sp)
//        }
    }
}

@Composable
private fun ReceivingAddressCard(address: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = address,
        onValueChange = { onTextChanged(it) },
        placeholder = { Text("Receiving Address") },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}