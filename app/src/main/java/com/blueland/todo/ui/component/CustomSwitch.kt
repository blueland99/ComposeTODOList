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
import com.blueland.todo.R
import com.blueland.todo.ui.theme.LocalColors

@Composable
fun CustomSwitch(
    modifier: Modifier = Modifier,
    checkedImageResId: Int = R.drawable.ic_switch_on,
    uncheckedImageResId: Int = R.drawable.ic_switch_off,
    checkedColor: Color = LocalColors.current.main,
    uncheckedColor: Color = LocalColors.current.icon1,
    disableColor: Color = LocalColors.current.disable1,
    checked: Boolean,
    enabled: Boolean = true,
    width: Dp = 44.dp,
    height: Dp = 23.dp,
    onChange: (checked: Boolean) -> Unit
) {
    Image(
        modifier = modifier
            .size(width, height)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (enabled) onChange(!checked)
                }
            ),
        painter = painterResource(if (checked) checkedImageResId else uncheckedImageResId),
        colorFilter = ColorFilter.tint(
            if (enabled)
                if (checked) checkedColor else uncheckedColor
            else disableColor
        ),
        contentDescription = null
    )
}