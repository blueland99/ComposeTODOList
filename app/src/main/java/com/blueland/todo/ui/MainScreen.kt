package com.blueland.todo.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blueland.todo.R
import com.blueland.todo.enums.DialogType
import com.blueland.todo.enums.UpdateStatus
import com.blueland.todo.managers.AppUpdateManager
import com.blueland.todo.navigation.Route
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.utils.NotificationPermissionRequest
import com.blueland.todo.viewmodel.DialogViewModel
import com.blueland.todo.viewmodel.InputDialogViewModel
import com.blueland.todo.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
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
        coroutineScope.launch {
            viewModel.loadGroups()
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

    Scaffold(
        containerColor = LocalColors.current.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LocalColors.current.background,
                ),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.complete_value, completeCount),
                            style = LocalTextStyles.current.boldBodyMd
                        )

                        // 미완료
                        Text(
                            text = stringResource(R.string.incomplete_value, incompleteCount),
                            style = LocalTextStyles.current.boldBodyMd
                        )
                    }
                },
                actions = {
                    // 설정 버튼
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = LocalColors.current.icon1,
                        ),
                        onClick = {
                            // 설정 화면으로 이동
                            navController.navigate(Route.Setting.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 그룹
                GroupView()

                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                if (items.isEmpty()) {
                    TodoEmptyView()
                } else {
                    TodoListView()
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
                        title = context.getString(R.string.input_new_title),
                        hint = context.getString(R.string.todo_hint),
                        onConfirm = {
                            Log.d(TAG, "click InputDialog onConfirm. value=$it")
                            if (it.trim().isEmpty()) {
                                Toast.makeText(context, context.getString(R.string.todo_hint_toast), Toast.LENGTH_SHORT).show()
                                return@showDialog
                            }
                            viewModel.addTodo(it)
                            inputDialogViewModel.hideDialog()
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