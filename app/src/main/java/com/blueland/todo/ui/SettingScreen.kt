package com.blueland.todo.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blueland.todo.R
import com.blueland.todo.data.worker.cancelAllWorkers
import com.blueland.todo.data.worker.scheduleIncompleteTodo
import com.blueland.todo.ui.component.CustomSwitch
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavHostController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val notification by viewModel.notification.collectAsState()

    Scaffold(
        containerColor = LocalColors.current.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LocalColors.current.background,
                ),
                title = {
                    Text(
                        text = stringResource(R.string.setting),
                        style = LocalTextStyles.current.boldBodyMd
                    )
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
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
                                scheduleIncompleteTodo(context)
                            }
                        }
                    }
                }
            }
        }
    )
}