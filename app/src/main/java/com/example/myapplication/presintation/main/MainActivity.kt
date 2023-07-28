package com.example.myapplication.presintation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.NewsScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {

                val vm: LoginViewModel = viewModel()

                val result = vm.authState.observeAsState()

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    vm.checkUserLogin(it)
                }

                if (result.value == LoginAppState.Success) {
                    StatApp()
                } else {
                    LoginScreen {
                        launcher.launch(arrayListOf(VKScope.WALL))
                    }
                }
            }
        }
    }

    @Composable
    private fun StatApp() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NewsScreen()
        }
    }
}


