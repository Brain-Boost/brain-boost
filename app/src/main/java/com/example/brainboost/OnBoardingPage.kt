package com.example.brainboost

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.brainboost.ui.theme.OnBoarding
import com.google.accompanist.pager.ExperimentalPagerApi

class OnBoardingPage : ComponentActivity() {

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (getSystemService(AlarmManager::class.java).canScheduleExactAlarms()) {
                startActivity(Intent(this, HomePage::class.java))
                return
            }
        }*/

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    PreviewFunction()
                }
            }
        }

    }


    @ExperimentalPagerApi
    @Preview(showBackground = true)
    @Composable
    fun PreviewFunction() {
        Surface(modifier = Modifier.fillMaxSize()) {
            OnBoarding()
        }
    }
}
