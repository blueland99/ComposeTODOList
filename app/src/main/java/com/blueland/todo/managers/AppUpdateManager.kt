package com.blueland.todo.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.blueland.todo.enums.UpdateStatus
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlin.system.exitProcess

object AppUpdateManager {
    val TAG = this.javaClass.simpleName

    fun checkForAppUpdate(context: Context, callback: (UpdateStatus) -> Unit) {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(
            mapOf(
                "latest_app_version" to "1.0.0",
                "required_app_version" to "1.0.0"
            )
        ) // 기본값 설정

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val latestVersion = remoteConfig.getString("latest_app_version") // 최신 버전
                    val requiredVersion = remoteConfig.getString("required_app_version") // 최소 버전
                    val currentVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName

                    // 업데이트 상태 결정 및 콜백 호출
                    val status = determineUpdateStatus(currentVersion, latestVersion, requiredVersion)
                    callback(status)
                } else {
                    callback(UpdateStatus.NONE) // 실패 시 업데이트 필요 없음으로 처리
                }
            }
    }

    private fun determineUpdateStatus(currentVersion: String, latestVersion: String, requiredVersion: String): UpdateStatus {
        Log.d(TAG, "determineUpdateStatus: currentVersion=$currentVersion, latestVersion=$latestVersion, requiredVersion=$requiredVersion")
        return when {
            isVersionOutdated(currentVersion, requiredVersion) -> UpdateStatus.REQUIRED    // 필수 업데이트
            isVersionOutdated(currentVersion, latestVersion) -> UpdateStatus.RECOMMENDED   // 권장 업데이트
            else -> UpdateStatus.NONE                                                      // 업데이트 필요 없음
        }
    }

    private fun isVersionOutdated(currentVersion: String, targetVersion: String): Boolean {
        val currentParts = currentVersion.split(".")
        val targetParts = targetVersion.split(".")

        for (i in 0 until minOf(currentParts.size, targetParts.size)) {
            val currentPart = currentParts[i].toIntOrNull() ?: 0
            val targetPart = targetParts[i].toIntOrNull() ?: 0
            if (currentPart < targetPart) return true
            if (currentPart > targetPart) return false
        }
        return targetParts.size > currentParts.size
    }

    fun openPlayStore(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
        context.startActivity(intent)
        exitProcess(0) // 프로세스 종료
    }
}
