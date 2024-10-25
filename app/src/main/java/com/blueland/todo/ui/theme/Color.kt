package com.blueland.todo.ui.theme

import androidx.compose.ui.graphics.Color

data class Colors(
    val background: Color,
    val main: Color,
    val text1: Color,
    val text2: Color,
    val text3: Color,
    val button1: Color,
    val disable1: Color,
    val line: Color,
)

val LightColors = Colors(
    background = Color(0xFFFFFFFF),
    main = Color(0xFF6F8BBE),
    text1 = Color(0xFF4A4A4A),
    text2 = Color(0xFF5D5D5D),
    text3 = Color(0xFF737373),
    button1 = Color(0xFFF9F9F9),
    disable1 = Color(0xFFCFCFCF),
    line = Color(0x33FFFFFF)
)

val DarkColors = Colors(
    background = Color(0xFF1E1E1E),
    main = Color(0xFF3B4B71),
    text1 = Color(0xFFE0E0E0),
    text2 = Color(0xFFCCCCCC),
    text3 = Color(0xFFB3B3B3),
    button1 = Color(0xFFF9F9F9),
    disable1 = Color(0xFF4D4D4D),
    line = Color(0x33FFFFFF)
)