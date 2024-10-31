package com.blueland.todo.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.blueland.todo.ui.theme.LocalColors

@Composable
fun ImageButton(
    imageResId: Int,
    color: Color = LocalColors.current.icon1,
    disableColor: Color = LocalColors.current.disable1,
    enabled: Boolean = true,
    width: Dp = 18.dp,
    height: Dp = 18.dp,
    onClick: () -> Unit
) {
    Image(
        modifier = Modifier
            .size(width, height)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (enabled) onClick()
                }
            ),
        painter = painterResource(imageResId),
        colorFilter = ColorFilter.tint(if (enabled) color else disableColor),
        contentDescription = null
    )
}