package com.blueland.todo.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.data.local.GroupEntity
import com.blueland.todo.enums.DialogType
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.utils.getLocalizedGroupName
import com.blueland.todo.viewmodel.DialogViewModel
import com.blueland.todo.viewmodel.GroupSettingViewModel
import com.blueland.todo.viewmodel.InputDialogViewModel

@Composable
fun GroupListView(
    viewModel: GroupSettingViewModel = hiltViewModel(),
    inputDialogViewModel: InputDialogViewModel = hiltViewModel()
) {
    val TAG = LocalContext.current.javaClass.simpleName
    val context = LocalContext.current

    val groupItems by viewModel.groupItems.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 16.dp, bottom = 90.dp, start = 16.dp, end = 16.dp
        )
    ) {
        itemsIndexed(
            groupItems,
            key = { _, item -> item.groupId } // 컴포저블 재구성(recomposition)과 상태 유지의 불일치로 인해 추가
        ) { _, item ->
            GroupSettingItem(
                item = item,
                onClick = {
                    // 그룹 수정 팝업 띄움
                    inputDialogViewModel.showDialog(
                        title = context.getString(R.string.input_modify_title),
                        content = item.groupName,
                        hint = context.getString(R.string.group_hint),
                        onConfirm = {
                            Log.d(TAG, "click InputDialog onConfirm. value=$it")
                            if (it.trim().isEmpty()) {
                                Toast.makeText(context, context.getString(R.string.group_hint_toast), Toast.LENGTH_SHORT).show()
                                return@showDialog
                            }
                            viewModel.updateGroup(
                                item.copy(
                                    groupName = it
                                )
                            )
                            inputDialogViewModel.hideDialog()
                        },
                        onDismiss = {
                            Log.d(TAG, "click InputDialog onDismiss")
                        }
                    )
                },
            )
        }
    }
}

@Composable
fun GroupSettingItem(
    viewModel: GroupSettingViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
    item: GroupEntity,
    onClick: (groupId: Int) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (item.isSystemGroup) {
                            Toast
                                .makeText(context, context.getString(R.string.modify_system_group_toast), Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            onClick(item.groupId)
                        }
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
            val text =
                if (item.isSystemGroup)
                    getLocalizedGroupName(LocalContext.current, item.groupName)
                else
                    item.groupName
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                color = LocalColors.current.text1,
                style = LocalTextStyles.current.mediumBodySm
            )

            // 삭제 버튼
            if (!item.isSystemGroup)
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
                                    content = context.getString(R.string.delete_group_content),
                                    onConfirm = {
                                        viewModel.deleteGroup(item)
                                    },
                                    onDismiss = {}
                                )
                            }
                        ),
                    colorFilter = ColorFilter.tint(LocalColors.current.text1),
                    contentDescription = null
                )
        }
    }
}