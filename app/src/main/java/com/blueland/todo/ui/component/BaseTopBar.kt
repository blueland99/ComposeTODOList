package com.blueland.todo.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blueland.todo.R
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    titleResId: Int,
    onBackPressed: (() -> Unit)? = null,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalColors.current.background,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                onBackPressed?.let {
                    ImageButton(
                        imageResId = R.drawable.ic_back,
                        color = LocalColors.current.text1,
                        width = 20.dp,
                        height = 20.dp
                    ) {
                        it()
                    }
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(titleResId),
                    style = LocalTextStyles.current.boldBodyMd
                )
            }
        },
    )
}