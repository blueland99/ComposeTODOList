package com.blueland.todo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.blueland.todo.data.local.LocalDataSource
import com.blueland.todo.data.worker.scheduleCreateTodo
import com.blueland.todo.data.worker.scheduleIncompleteTodo
import com.blueland.todo.utils.NotificationUtil
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var localDataSource: LocalDataSource

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Firebase 초기화
        FirebaseApp.initializeApp(this)

        // 앱 알림 표시를 위한 알림 채널 생성
        NotificationUtil.createNotificationChannel(this)

        val notificationEnabled = localDataSource.getNotification()
        val createNotificationEnabled = localDataSource.getCreateNotification()
        val checkNotificationEnabled = localDataSource.getCheckNotification()

        if (notificationEnabled) { // 알림 활성화
            // Worker
            if (createNotificationEnabled) { // 등록 알림 활성화
                scheduleCreateTodo(this)
            }
            if (checkNotificationEnabled) { // 확인 알림 활성화
                scheduleIncompleteTodo(this)
            }
        }
    }
}