package com.blueland.todo.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.data.local.GroupEntity
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.utils.getLocalizedGroupName
import com.blueland.todo.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun GroupView(
    viewModel: MainViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val groupItems by viewModel.groupItems.collectAsState()
    val selectedGroupId by viewModel.selectedGroupId.collectAsState()

    Log.e("TAG", "GroupView: ${groupItems.size}")

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // 전체 그룹 추가
        item {
            GroupItem(
                item = GroupEntity(groupName = stringResource(R.string.all)),
                isSelected = selectedGroupId == null,
                onClick = {
                    coroutineScope.launch {
                        viewModel.setGroupId(null)
                    }
                }
            )
        }

        items(groupItems) { item ->
            val isSelected = item.groupId == selectedGroupId
            GroupItem(
                item = item,
                isSelected = isSelected,
                onClick = {
                    coroutineScope.launch {
                        viewModel.setGroupId(item.groupId)
                    }
                }
            )
        }
    }
}

@Composable
fun GroupItem(
    item: GroupEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) LocalColors.current.main else LocalColors.current.button1,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = if (item.isSystemGroup) getLocalizedGroupName(LocalContext.current, item.groupName) else item.groupName,
            color = if (isSelected) LocalColors.current.button1 else LocalColors.current.text3
        )
    }
}
