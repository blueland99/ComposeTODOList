package com.blueland.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.blueland.todo.navigation.AppNavGraph
import com.blueland.todo.ui.theme.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MyAppTheme {
        content()
    }
}