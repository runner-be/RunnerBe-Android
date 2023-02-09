package com.applemango.runnerbe.presentation.screen.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.applemango.runnerbe.presentation.screen.compose.navigation.SignNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("key hash", com.kakao.util.helper.Utility.getKeyHash(this))
        setContent {
            SignNavHost()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SignNavHost()
}