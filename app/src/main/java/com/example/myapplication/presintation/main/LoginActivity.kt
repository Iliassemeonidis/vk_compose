package com.example.myapplication.presintation.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.domain.entity.LoginAppState
import com.example.myapplication.presintation.NewsFeedApplication
import com.example.myapplication.presintation.ViewModelFactory
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class LoginActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val component by lazy {
        (application as NewsFeedApplication).component
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {

                val vm: LoginViewModel = viewModel(factory = viewModelFactory)

                val result = vm.authState.collectAsState(LoginAppState.InProgress)

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    vm.checkUserLogin()
                }

                when (result.value) {
                    LoginAppState.Failure -> {
                        Log.i("MainActivity", "Error")
                    }
                    LoginAppState.InProgress -> {
                        LoginScreen {
                        launcher.launch(arrayListOf(VKScope.WALL, VKScope.FRIENDS))
                    }}
                    LoginAppState.Success -> {
                        StatApp(viewModelFactory)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    private fun StatApp(viewModelFactory: ViewModelFactory) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NewsScreen(viewModelFactory)
        }
    }
}


