package com.blueland.todo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blueland.todo.R
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles

@Composable
fun TodoEmptyView() {
    val emptyTextList = listOf(
        stringResource(R.string.empty_todo_value1),
        stringResource(R.string.empty_todo_value2),
        stringResource(R.string.empty_todo_value2),
        stringResource(R.string.empty_todo_value3),
        stringResource(R.string.empty_todo_value4),
        stringResource(R.string.empty_todo_value5),
        stringResource(R.string.empty_todo_value6),
        stringResource(R.string.empty_todo_value7),
        stringResource(R.string.empty_todo_value8),
        stringResource(R.string.empty_todo_value9),
        stringResource(R.string.empty_todo_value10),
        stringResource(R.string.empty_todo_value11),
        stringResource(R.string.empty_todo_value12),
        stringResource(R.string.empty_todo_value13),
        stringResource(R.string.empty_todo_value14),
        stringResource(R.string.empty_todo_value15),
    )

    val emptyEmojiList = listOf(
        stringResource(R.string.empty_todo_emoji1),
        stringResource(R.string.empty_todo_emoji2),
        stringResource(R.string.empty_todo_emoji2),
        stringResource(R.string.empty_todo_emoji3),
        stringResource(R.string.empty_todo_emoji4),
        stringResource(R.string.empty_todo_emoji5),
        stringResource(R.string.empty_todo_emoji6),
        stringResource(R.string.empty_todo_emoji7),
        stringResource(R.string.empty_todo_emoji8),
        stringResource(R.string.empty_todo_emoji9),
        stringResource(R.string.empty_todo_emoji10),
        stringResource(R.string.empty_todo_emoji11),
        stringResource(R.string.empty_todo_emoji12),
        stringResource(R.string.empty_todo_emoji13),
        stringResource(R.string.empty_todo_emoji14),
        stringResource(R.string.empty_todo_emoji15),
    )
    // 리스트에서 랜덤 인덱스를 가져옴
    val randomIndex = remember { emptyTextList.indices.random() }

    // 랜덤 텍스트와 이모지 선택
    val emptyText = emptyTextList[randomIndex]
    val emptyEmoji = emptyEmojiList[randomIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.empty_todo_value, emptyText, emptyEmoji),
            style = LocalTextStyles.current.mediumBodySm,
            color = LocalColors.current.text1,
            textAlign = TextAlign.Center
        )
    }
}