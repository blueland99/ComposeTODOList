package com.blueland.todo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.enums.DialogType
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.viewmodel.DialogViewModel

@Composable
fun CustomDialog(
    viewModel: DialogViewModel = hiltViewModel()
) {
    val isDialogVisible by viewModel.isDialogVisible
    val currentDialogType by viewModel.currentDialogType
    val title by viewModel.dialogTitle
    val content by viewModel.dialogContent
    val confirm by viewModel.dialogConfirm
    val cancel by viewModel.dialogCancel

    if (isDialogVisible) {
        val shape = LocalShapes.medium

        when (currentDialogType) {
            DialogType.ALERT,
            DialogType.CONFIRM -> {
                Dialog(
                    onDismissRequest = {
                        if (currentDialogType != DialogType.ALERT) {
                            viewModel.onDismiss()
                        }
                    },
                    properties = DialogProperties(
                        dismissOnClickOutside = currentDialogType != DialogType.ALERT, // 바깥 클릭으로 닫히지 않도록
                        dismissOnBackPress = currentDialogType != DialogType.ALERT // 뒤로가기 버튼으로 닫히지 않도록
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .background(LocalColors.current.background, shape = shape)
                            .border(width = 1.dp, color = LocalColors.current.line, shape = shape)
                            .clip(shape = shape)
                    ) {
                        Column {
                            Column(
                                modifier = Modifier.padding(vertical = 30.dp, horizontal = 20.dp)
                            ) {
                                // Title 영역
                                title?.let {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = it,
                                        style = LocalTextStyles.current.boldBodySm,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                // Content 영역
                                content?.let {
                                    Text(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        text = it,
                                        style = LocalTextStyles.current.mediumBodySm,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            HorizontalDivider(color = LocalColors.current.line)

                            // 버튼 영역
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                if (currentDialogType == DialogType.CONFIRM) {
                                    TextButton(
                                        modifier = Modifier.weight(1f),
                                        onClick = { viewModel.onDismiss() }
                                    ) {
                                        Text(
                                            cancel ?: stringResource(R.string.cancel),
                                            style = LocalTextStyles.current.mediumBodyXs
                                        )
                                    }
                                    VerticalDivider(color = LocalColors.current.line)
                                }
                                TextButton(
                                    modifier = Modifier.weight(1f),
                                    onClick = { viewModel.onConfirm() }
                                ) {
                                    Text(
                                        confirm ?: stringResource(R.string.confirm),
                                        style = LocalTextStyles.current.mediumBodyXs
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
