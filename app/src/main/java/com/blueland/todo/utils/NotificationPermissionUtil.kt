package com.blueland.todo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermissionRequest() {
    val context = LocalContext.current

    // 권한 요청 런처 설정
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // TODO: 권한 허용
        } else {
            // TODO: 권한 거부
        }
    }

    // Android 13 (API 33) 이상인 경우에만 실행
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission(context, permissionLauncher)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun checkNotificationPermission(context: Context, permissionLauncher: ManagedActivityResultLauncher<String, Boolean>) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // 권한이 없는 경우 요청
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
