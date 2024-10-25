package com.blueland.todo.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.ui.theme.LocalTextStyles

@Composable
fun BottomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    backgroundColor: Color = LocalColors.current.main,
    disabledBackgroundColor: Color = LocalColors.current.disable1,
    textColor: Color = LocalColors.current.button1,
    pressedTextColor: Color = LocalColors.current.button1,
    disabledTextColor: Color = LocalColors.current.button1,
    textStyle: TextStyle = LocalTextStyles.current.boldBodyXs,
    paddingValues: PaddingValues = PaddingValues(11.dp),
    shape: CornerBasedShape = LocalShapes.medium,
    contentAlignment: Alignment = Alignment.Center
) {
    DefaultButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        backgroundColor = backgroundColor,
        disabledBackgroundColor = disabledBackgroundColor,
        textColor = textColor,
        pressedTextColor = pressedTextColor,
        disabledTextColor = disabledTextColor,
        textStyle = textStyle,
        paddingValues = paddingValues,
        shape = shape,
        contentAlignment = contentAlignment
    )
}