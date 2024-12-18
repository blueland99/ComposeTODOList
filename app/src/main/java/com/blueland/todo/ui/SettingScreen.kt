package com.blueland.todo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blueland.todo.R
import com.blueland.todo.data.worker.TodoCheckWorker
import com.blueland.todo.data.worker.cancelAllWorkers
import com.blueland.todo.data.worker.cancelWorker
import com.blueland.todo.data.worker.scheduleCreateTodo
import com.blueland.todo.data.worker.scheduleIncompleteTodo
import com.blueland.todo.navigation.Route
import com.blueland.todo.ui.component.BaseTopBar
import com.blueland.todo.ui.component.CustomSwitch
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.viewmodel.SettingViewModel

@Composable
fun SettingScreen(
    navController: NavHostController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val notification by viewModel.notification.collectAsState()
    val createNotification by viewModel.createNotification.collectAsState()
    val checkNotification by viewModel.checkNotification.collectAsState()

    Scaffold(
        containerColor = LocalColors.current.background,
        topBar = {
            BaseTopBar(titleResId = R.string.setting) {
                navController.popBackStack()
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 알림
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.notification),
                            modifier = Modifier.weight(1f),
                            style = LocalTextStyles.current.mediumBodySm
                        )

                        CustomSwitch(checked = notification) { checked ->
                            viewModel.setNotification(checked)
                            cancelAllWorkers(context)
                            if (checked) {
                                if (createNotification) {
                                    scheduleCreateTodo(context)
                                }
                                if (checkNotification) {
                                    scheduleIncompleteTodo(context)
                                }
                            }
                        }
                    }
                }

                // 등록 알림
                item {
                    Row(
                        modifier = Modifier.padding(start = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                stringResource(R.string.create_notification),
                                style = LocalTextStyles.current.regularBodySm
                            )
                            Text(
                                stringResource(R.string.create_notification_content),
                                style = LocalTextStyles.current.regularCaptionMd
                            )
                        }

                        CustomSwitch(
                            checked = createNotification,
                            enabled = notification
                        ) { checked ->
                            viewModel.setCreateNotification(checked)
                            cancelWorker(context, TodoCheckWorker.WorkConstants.WORK_NAME_TODAY_CREATE_TODO)
                            if (checked) {
                                scheduleCreateTodo(context)
                            }
                        }
                    }
                }

                // 확인 알림
                item {
                    Row(
                        modifier = Modifier.padding(start = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                stringResource(R.string.check_notification),
                                style = LocalTextStyles.current.regularBodySm
                            )
                            Text(
                                stringResource(R.string.check_notification_content),
                                style = LocalTextStyles.current.regularCaptionMd
                            )
                        }

                        CustomSwitch(
                            checked = checkNotification,
                            enabled = notification
                        ) { checked ->
                            viewModel.setCheckNotification(checked)
                            cancelWorker(context, TodoCheckWorker.WorkConstants.WORK_NAME_TODAY_CHECK_TODO)
                            if (checked) {
                                scheduleIncompleteTodo(context)
                            }
                        }
                    }
                }

                // 그룹 설정
                item {
                    Row(
                        modifier = Modifier
                            .clickable {
                                // 그룹 설정 화면으로 이동
                                navController.navigate(Route.GroupSetting.route)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.group_setting),
                            modifier = Modifier.weight(1f),
                            style = LocalTextStyles.current.mediumBodySm
                        )

                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_arrow_right),
                            tint = LocalColors.current.text1,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    )
}