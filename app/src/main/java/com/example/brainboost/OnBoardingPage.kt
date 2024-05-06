package com.example.brainboost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.brainboost.ui.theme.OnBoarding
import com.google.accompanist.pager.ExperimentalPagerApi

class OnBoardingPage : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Green
                ) {
                    PreviewFunction()
                }

            }
        }
    }


    @ExperimentalPagerApi
    @Preview(showBackground = true)
    @Composable
    fun PreviewFunction(){
        Surface(modifier = Modifier.fillMaxSize()) {
            OnBoarding()
        }
    }
}
