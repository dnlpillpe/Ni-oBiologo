package com.educalab.ninobiologo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.educalab.ninobiologo.ui.navigation.NinoBiologoNavGraph
import com.educalab.ninobiologo.ui.theme.NinoBiologoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as NinoBiologoApp).container

        setContent {
            NinoBiologoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NinoBiologoNavGraph(container = container)
                }
                DisposableEffect(Unit) {
                    onDispose { container.soundManager.release() }
                }
            }
        }
    }
}
