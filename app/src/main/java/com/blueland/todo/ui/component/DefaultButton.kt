package com.blueland.todo.ui.component

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun DefaultButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    backgroundColor: Color,
    disabledBackgroundColor: Color,
    textColor: Color,
    pressedTextColor: Color,
    disabledTextColor: Color,
    textStyle: TextStyle,
    paddingValues: PaddingValues,
    shape: CornerBasedShape,
    contentAlignment: Alignment
) {
    var isPressed by remember { mutableStateOf(false) }

    val currentBackgroundColor by rememberUpdatedState(
        newValue = when {
            !enabled -> disabledBackgroundColor
            isPressed -> backgroundColor.copy(alpha = 0.9f)
            else -> backgroundColor
        }
    )

    val currentTextColor by rememberUpdatedState(
        newValue = when {
            !enabled -> disabledTextColor
            isPressed -> pressedTextColor
            else -> textColor
        }
    )

    Surface(
        modifier = modifier
            .sizeIn(minHeight = 4.dp)
            .pointerInput(enabled) {
                detectTapGestures(
                    onPress = {
                        if (enabled) {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                        }
                    },
                    onTap = {
                        Log.e("Default", "DefaultButton: enabled=$enabled")
                        if (enabled) {
                            onClick()
                        }
                    }
                )
            },
        color = currentBackgroundColor,
        shape = shape
    ) {
        Box(
            contentAlignment = contentAlignment,
            modifier = Modifier.padding(paddingValues)
        ) {
            Text(text = text, style = textStyle, color = currentTextColor)
        }
    }
}
