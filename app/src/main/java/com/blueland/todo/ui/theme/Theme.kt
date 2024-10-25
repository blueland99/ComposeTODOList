package com.blueland.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// CompositionLocal for colors and text styles
val LocalColors = staticCompositionLocalOf { LightColors }
val LocalTextStyles = staticCompositionLocalOf { createTextStyles(LightColors) }

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val textStyles = createTextStyles(colors)

    // 시스템 UI 컨트롤러 사용
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme

    // Status Bar 색상 설정
    systemUiController.setStatusBarColor(
        color = colors.background,
        darkIcons = useDarkIcons
    )

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTextStyles provides textStyles
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colors.background // 공통 배경색 적용
        ) {
            content()
        }
    }
}