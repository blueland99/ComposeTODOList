package com.blueland.todo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.blueland.todo.data.local.LocalDataSource
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

        NotificationUtil.createNotificationChannel(this)

        if (localDataSource.getNotification()) {
            // Worker
            scheduleIncompleteTodo(this)
        }
    }
}