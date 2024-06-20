package com.example.particleapp.ui.archEaseScreen.homescreen

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.particleapp.ui.archEaseScreen.ArchEaseViewModel
import com.example.particleapp.ui.archEaseScreen.Screen
import com.example.particleapp.utils.Network
import com.example.particleapp.utils.QrCodeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.text.DecimalFormat

@Composable
fun MyAccountScreen(
    navController: NavHostController,
    viewModel: ArchEaseViewModel,
    showToast: (String) -> Unit,
    current: Context
) {
    Column(
        modifier = Modifier
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderScreen(onBackPressed = { navController.popBackStack() })
        UserInfoCard(viewModel, current)
        CryptoInfoCard(viewModel, current)
        OptionsMyAccountScreen(viewModel, navController, showToast)
    }
}

@Composable
fun OptionsMyAccountScreen(
    viewModel: ArchEaseViewModel,
    navController: NavHostController,
    showToast: (String) -> Unit
) {
    Column {
//        HomeScreenRowButton(icon = Icons.Default.Edit, buttonText = "Switch Chain", onClick = { navController.navigate(Screen.SwitchChainScreen) })
        HomeScreenRowButton(icon = Icons.Default.AccountCircle, buttonText = "Log Out", onClick = { viewModel.logout(
            onLogoutSuccessful = {
                navController.navigate(Screen.LoginScreen)
        },
            onLogoutFailed = { showToast("Log out failed! $it") }
        ) })
    }
}

@Composable
fun CryptoInfoCard(viewModel: ArchEaseViewModel, current: Context) {
    var address by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = current.getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
            address = Network.getAddr(sharedPref.getString("mnemonic", "").toString())!!.address
            val balanceResponse = Network.getBalance(address)
            val decfor = DecimalFormat("#.00").format(balanceResponse!!.balances!!.get(0)!!.amount!!.toFloat() / BigInteger.valueOf(1000000000000000000L).toFloat())
            balance = decfor
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Raehat Singh Nanda")
                Spacer(modifier = Modifier.width(15.dp))
//                Image(
//                    painter = rememberAsyncImagePainter(model = viewModel.particleNetwork().chainInfo.icon),
//                    contentDescription = null
//                )
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "Account Icon",
//                    modifier = Modifier.size(80.dp),
//                    tint = MaterialTheme.colorScheme.onSurface
//                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = QrCodeUtils.getReceivingQrCode(
                            address = address
                        ),
                        contentDescription = null
                    )
                    Text(modifier = Modifier.padding(top = 10.dp), text = "*scan this QR code to receive crypto", fontSize = 10.sp)
                }
                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(text = "Balance (arch)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$balance arch")
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
fun UserInfoCard(viewModel: ArchEaseViewModel, current: Context) {
    var address by remember { mutableStateOf("") }
    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = current.getSharedPreferences("MySharedPrefs", MODE_PRIVATE)
            address = Network.getAddr(sharedPref.getString("mnemonic", "").toString())!!.address
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "gouika2034")
            Text(text = "gouika2034@gmail.com")
            Text(text = address, fontSize = 10.sp, fontStyle = FontStyle.Italic)
        }
        if (viewModel.avatar() != null) {
            Image(
                painter = rememberAsyncImagePainter(viewModel.avatar()),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Account Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun HeaderScreen(
    onBackPressed: () -> Unit = {},
    onMenuClicked: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left arrow icon
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = null, modifier = Modifier
                .size(24.dp)
                .clickable { onBackPressed() },
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Menu",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(24.dp)
                .clickable { onMenuClicked() }
        )
    }
}