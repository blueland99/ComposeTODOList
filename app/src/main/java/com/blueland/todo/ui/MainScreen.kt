package com.blueland.todo.ui

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.data.local.TodoEntity
import com.blueland.todo.enums.DialogType
import com.blueland.todo.enums.UpdateStatus
import com.blueland.todo.managers.AppUpdateManager
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.utils.NotificationPermissionRequest
import com.blueland.todo.utils.formatDate
import com.blueland.todo.viewmodel.DialogViewModel
import com.blueland.todo.viewmodel.InputDialogViewModel
import com.blueland.todo.viewmodel.MainViewModel
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
    inputDialogViewModel: InputDialogViewModel = hiltViewModel(),
) {
    val TAG = LocalContext.current.javaClass.simpleName

    NotificationPermissionRequest() // 알림 권한 요청

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var updateStatus by remember { mutableStateOf(UpdateStatus.NONE) }

    // 앱 실행 시 업데이트 상태 확인
    LaunchedEffect(Unit) {
        AppUpdateManager.checkForAppUpdate(context) { status ->
            updateStatus = status
        }
    }

    // UpdateStatus에 따른 다이얼로그 표시
    when (updateStatus) {
        UpdateStatus.REQUIRED -> {
            dialogViewModel.showDialog(
                dialogType = DialogType.ALERT,
                title = stringResource(R.string.update_title),
                content = stringResource(R.string.required_update_content),
                onConfirm = {
                    Log.d(TAG, "click AlertmDialog onConfirm")
                    // 업데이트를 위해 스토어로 이동
                    AppUpdateManager.openPlayStore(context)
                }
            )
        }

        UpdateStatus.RECOMMENDED -> {
            dialogViewModel.showDialog(
                dialogType = DialogType.CONFIRM,
                title = stringResource(R.string.update_title),
                content = stringResource(R.string.recommended_update_content),
                onConfirm = {
                    Log.d(TAG, "click ConfirmDialog onConfirm")
                    // 업데이트를 위해 스토어로 이동
                    AppUpdateManager.openPlayStore(context)
                },
                onDismiss = {
                    Log.d(TAG, "click ConfirmDialog onDismiss")
                }
            )
        }

        UpdateStatus.NONE -> {
            /* 업데이트 필요 없음 */
        }
    }

    // 뒤로가기 버튼
    BackHandler {
        (context as? ComponentActivity)?.finishAffinity()
        exitProcess(0) // 프로세스 종료
    }

    val items by viewModel.items.collectAsState(initial = emptyList())
    val completeCount by viewModel.completeCount.collectAsState(0)
    val incompleteCount by viewModel.incompleteCount.collectAsState(0)

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

    Scaffold(
        containerColor = LocalColors.current.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LocalColors.current.background,
                ),
                title = {},
                actions = {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.complete_value, completeCount),
                            style = LocalTextStyles.current.boldBodyMd,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.incomplete_value, incompleteCount),
                            style = LocalTextStyles.current.boldBodyMd,
                            textAlign = TextAlign.End
                        )
                    }
                }
            )
        },
        content = { paddingValues ->

            // 순환할 색상 리스트
            val colors = listOf(
                LocalColors.current.point1,
                LocalColors.current.point2,
                LocalColors.current.point3,
                LocalColors.current.point4
            )

            if (items.isEmpty()) {
                // 리스트에서 랜덤 인덱스를 가져옴
                val randomIndex = remember { emptyTextList.indices.random() }

                // 랜덤 텍스트와 이모지 선택
                val emptyText = emptyTextList[randomIndex]
                val emptyEmoji = emptyEmojiList[randomIndex]

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
                contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp, start = 16.dp, end = 16.dp)
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
                        title = context.getString(R.string.input_new_todo_title),
                        hint = context.getString(R.string.input_hint),
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
    CustomDialog()
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
                                content = context.getString(R.string.delete_content),
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