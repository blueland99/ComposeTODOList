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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.viewmodel.InputDialogViewModel

@Composable
fun InputDialog(
    viewModel: InputDialogViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isDialogVisible by viewModel.isDialogVisible
    val title by viewModel.dialogTitle
    val content by viewModel.dialogContent
    val hint by viewModel.dialogHint

    // 암호 입력 TextField
    var input by remember { mutableStateOf(content ?: "") }

    LaunchedEffect(content) {
        input = content ?: ""
    }

    if (isDialogVisible) {
        val shape = LocalShapes.medium

        Dialog(
            onDismissRequest = {
                viewModel.onDismiss()
            }
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

                        // 암호 입력
                        BasicTextField(
                            value = input,
                            onValueChange = { input = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                                .border(1.dp, LocalColors.current.line, shape = LocalShapes.small) // 1dp의 둥근 테두리 추가
                                .clip(LocalShapes.small)
                                .background(LocalColors.current.background),
                            textStyle = LocalTextStyles.current.mediumBodySm,
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 8.dp) // 내부 패딩 추가
                                ) {
                                    if (input.isEmpty()) {
                                        Text(
                                            text = hint ?: "",
                                            style = LocalTextStyles.current.mediumBodySm,
                                            color = LocalColors.current.disable1
                                        )
                                    }
                                    innerTextField() // 실제 입력 필드 표시
                                }
                            },
                            cursorBrush = SolidColor(LocalColors.current.main)
                        )
                    }

                    HorizontalDivider(color = LocalColors.current.line)

                    // 버튼 영역
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onDismiss() }
                        ) {
                            Text(
                                stringResource(R.string.cancel),
                                style = LocalTextStyles.current.mediumBodyXs
                            )
                        }
                        VerticalDivider(color = LocalColors.current.line)
                        TextButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.onConfirm(input.trim())
                            }
                        ) {
                            Text(
                                stringResource(R.string.confirm),
                                style = LocalTextStyles.current.mediumBodyXs
                            )
                        }
                    }
                }
            }
        }
    }
}
