package com.blueland.todo.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.data.local.TodoEntity
import com.blueland.todo.enums.DialogType
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.utils.formatDate
import com.blueland.todo.viewmodel.DialogViewModel
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
                        title = context.getString(R.string.input_modify_title),
                        content = item.title,
                        hint = context.getString(R.string.todo_hint),
                        onConfirm = {
                            Log.d(TAG, "click InputDialog onConfirm. value=$it")
                            if (it.trim().isEmpty()) {
                                Toast.makeText(context, context.getString(R.string.todo_hint_toast), Toast.LENGTH_SHORT).show()
                                return@showDialog
                            }
                            viewModel.updateTodo(
                                item.copy(
                                    title = it,
                                    updatedAt = System.currentTimeMillis()
                                )
                            )
                            inputDialogViewModel.hideDialog()
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

@Composable
fun TodoItem(
    viewModel: MainViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
    item: TodoEntity,
    color: Color,
    onClick: (id: Int) -> Unit,
    onComplete: () -> Unit
) {
    val TAG = LocalContext.current.javaClass.simpleName

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onClick(item.id)
                    }
                )
            },
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
                            onComplete()
                        }
                    ),
                colorFilter = ColorFilter.tint(if (item.isCompleted) color else LocalColors.current.disable1),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 할 일
                Text(
                    item.title,
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalTextStyles.current.mediumBodySm
                )
                // 마지막 수정 일시 (수정 일시가 없으면 생성 일시 표시)
                Text(
                    formatDate(item.updatedAt ?: item.createdAt),
                    modifier = Modifier.fillMaxWidth(),
                    style = LocalTextStyles.current.mediumCaptionMd,
                    color = LocalColors.current.text3
                )
            }

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
                            dialogViewModel.showDialog(
                                dialogType = DialogType.CONFIRM,
                                title = context.getString(R.string.delete_title),
                                content = context.getString(R.string.delete_todo_content),
                                onConfirm = {
                                    Log.d(TAG, "MainScreen: click ConfirmDialog onConfirm")
                                    viewModel.deleteTodo(item)
                                },
                                onDismiss = {
                                    Log.d(TAG, "MainScreen: click ConfirmDialog onDismiss")
                                }
                            )
                        }
                    ),
                colorFilter = ColorFilter.tint(LocalColors.current.text1),
                contentDescription = null
            )
        }
    }
}