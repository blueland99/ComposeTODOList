package com.blueland.todo.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.blueland.todo.R
import com.blueland.todo.ui.component.BaseTopBar
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.viewmodel.DialogViewModel
import com.blueland.todo.viewmodel.GroupSettingViewModel
import com.blueland.todo.viewmodel.InputDialogViewModel

@Composable
fun GroupSettingScreen(
    navController: NavHostController,
    viewModel: GroupSettingViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
    inputDialogViewModel: InputDialogViewModel = hiltViewModel()
) {
    val TAG = LocalContext.current.javaClass.simpleName
    val context = LocalContext.current

    Scaffold(
        containerColor = LocalColors.current.background,
        topBar = {
            BaseTopBar(titleResId = R.string.group_setting) {
                navController.popBackStack()
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                GroupListView()
            }
        },
        floatingActionButton = {
            // 추가 버튼
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                containerColor = LocalColors.current.main,
                onClick = {
                    // 그룹 입력 팝업 띄움
                    inputDialogViewModel.showDialog(
                        title = context.getString(R.string.input_new_title),
                        hint = context.getString(R.string.group_hint),
                        onConfirm = {
                            Log.d(TAG, "click InputDialog onConfirm. value=$it")
                            if (it.trim().isEmpty()) {
                                Toast.makeText(context, context.getString(R.string.group_hint_toast), Toast.LENGTH_SHORT).show()
                                return@showDialog
                            }
                            viewModel.addGroup(it)
                            inputDialogViewModel.hideDialog()
                        },
                        onDismiss = {}
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