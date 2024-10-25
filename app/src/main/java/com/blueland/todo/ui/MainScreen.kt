package com.blueland.todo.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.data.local.TodoEntity
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.viewmodel.InputDialogViewModel
import com.blueland.todo.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    inputDialogViewModel: InputDialogViewModel = hiltViewModel()
) {
    val TAG = LocalContext.current.javaClass.simpleName

    val items by viewModel.allTodos.collectAsState(initial = emptyList())

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

    Scaffold(
        containerColor = LocalColors.current.background,
        content = { paddingValues ->

            // 순환할 색상 리스트
            val colors = listOf(
                LocalColors.current.point1,
                LocalColors.current.point2,
                LocalColors.current.point3,
                LocalColors.current.point4
            )

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
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
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(top = 32.dp, bottom = 90.dp, start = 16.dp, end = 16.dp)
            ) {
                itemsIndexed(items) { index, item ->
                    val color = colors[index % colors.size]  // 색상을 순환하도록 설정
                    TodoItem(item = item, color = color)
                }
            }
        },
        floatingActionButton = {
            // 추가 버튼
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                containerColor = LocalColors.current.main,
                onClick = {
                    // 할 일 입력 팝업 띄움
                    inputDialogViewModel.showDialog(
                        onConfirm = {
                            Log.d(TAG, "click InputDialog onConfirm. value=$it")
                            viewModel.addTodo(it)
                        },
                        onDismiss = {
                            Log.d(TAG, "click InputDialog onDismiss")
                        }
                    )
                }
            ) {
                Image(
                    painterResource(R.drawable.ic_add),
                    modifier = Modifier.padding(10.dp),
                    colorFilter = ColorFilter.tint(LocalColors.current.button1),
                    contentDescription = null
                )
            }
        }
    )

    InputDialog()
}


@Composable
fun TodoItem(
    viewModel: MainViewModel = hiltViewModel(),
    item: TodoEntity,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = LocalShapes.large,
        elevation = CardDefaults.elevatedCardElevation(2.dp),

        colors = CardDefaults.cardColors(containerColor = LocalColors.current.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(if (item.isCompleted) R.drawable.ic_check_on else R.drawable.ic_check_off),
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            viewModel.updateTodo(item.copy(isCompleted = !item.isCompleted))
                        }
                    ),
                colorFilter = ColorFilter.tint(if (item.isCompleted) color else LocalColors.current.disable1),
                contentDescription = null
            )

            Text(
                item.title,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                style = LocalTextStyles.current.mediumBodySm
            )

            // 삭제 버튼
            Image(
                painterResource(R.drawable.ic_delete),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            viewModel.deleteTodo(item)
                        }
                    ),
                colorFilter = ColorFilter.tint(LocalColors.current.text1),
                contentDescription = null
            )
        }
    }
}