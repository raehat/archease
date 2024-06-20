package com.example.particleapp.ui.archEaseScreen.loginscreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.particleapp.R
import com.example.particleapp.ui.buttons.ParticleAppButton
import com.example.particleapp.ui.archEaseScreen.ArchEaseViewModel
import com.example.particleapp.ui.archEaseScreen.Screen

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: ArchEaseViewModel,
    showToast: (String) -> Unit,
    context: Context
) {
    Image(painter = painterResource(id = R.drawable.splash_screen_logo), contentDescription = null)
    Text(
        text = "To get started, please click on login. You will be redirected to the login page.",
        textAlign = TextAlign.Center
    )
    ParticleAppButton(buttonText = "Login", onClick = { viewModel.login(
        onLoginSuccessful = { navController.navigate(Screen.HomeScreen) },
        onLoginFailed = { showToast("Login Failed: $it") },
        context = context
    ) })
}