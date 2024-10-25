package com.blueland.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blueland.todo.R
import com.blueland.todo.ui.component.BottomButton
import com.blueland.todo.ui.theme.LocalColors

@Composable
fun MainScreen() {
    Scaffold(
        containerColor = LocalColors.current.background
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BottomButton(
                text = stringResource(R.string.next),
                onClick = { /* TODO */ }
            )
        }
    }
}