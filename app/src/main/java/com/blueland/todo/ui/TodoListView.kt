package com.blueland.todo.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.viewmodel.InputDialogViewModel
import com.blueland.todo.viewmodel.MainViewModel

@Composable
fun TodoListView(
    viewModel: MainViewModel = hiltViewModel(),
    inputDialogViewModel: InputDialogViewModel = hiltViewModel()
) {
    val TAG = LocalContext.current.javaClass.simpleName
    val context = LocalContext.current

    val items by viewModel.items.collectAsState(initial = emptyList())

    // 순환할 색상 리스트
    val colors = listOf(
        LocalColors.current.point1,
        LocalColors.current.point2,
        LocalColors.current.point3,
        LocalColors.current.point4
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 16.dp, bottom = 90.dp, start = 16.dp, end = 16.dp
        )
    ) {
        itemsIndexed(
            items,
            key = { _, item -> item.id } // 컴포저블 재구성(recomposition)과 상태 유지의 불일치로 인해 추가
        ) { index, item ->
            val color = colors[index % colors.size]  // 색상을 순환하도록 설정
            TodoItem(
                item = item, color = color,
                onClick = {
                    // 할 일 수정 팝업 띄움
                    inputDialogViewModel.showDialog(
                        title = context.getString(R.string.input_modify_todo_title),
                        content = item.title,
                        hint = context.getString(R.string.input_hint),
                        onConfirm = {
                            Log.d(TAG, "click InputDialog onConfirm. value=$it")
                            viewModel.updateTodo(
                                item.copy(
                                    title = it,
                                    updatedAt = System.currentTimeMillis()
                                )
                            )
                        },
                        onDismiss = {
                            Log.d(TAG, "click InputDialog onDismiss")
                        }
                    )
                },
                onComplete = {
                    viewModel.updateTodo(
                        item.copy(
                            isCompleted = !item.isCompleted,
                            completedAt = System.currentTimeMillis()
                        )
                    )
                }
            )
        }
    }
}