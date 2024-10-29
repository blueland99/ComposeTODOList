package com.blueland.todo.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

// 일정한 시간 간격으로 Worker 예약
fun scheduleIncompleteTodo(context: Context) {
    // 미완료 할 일 Worker 예약
    createAndEnqueueWorker<TodoCheckWorker>(
        context,
        LocalTime.of(16, 0), // 오후 4시
        TodoCheckWorker.WorkConstants.WORK_NAME_TODAY_CHECK_TODO
    )

    // 할 일 생성 알림 Worker 예약
    createAndEnqueueWorker<TodoCheckWorker>(
        context,
        LocalTime.of(8, 0), // 오전 8시
        TodoCheckWorker.WorkConstants.WORK_NAME_TODAY_CREATE_TODO
    )
}


/**
 * 주어진 목표 시간에 따라 Worker를 생성 및 예약
 *
 * @param context 애플리케이션의 Context
 * @param targetTime 목표 시간
 * @param uniqueWorkName 고유 작업 이름
 */
private inline fun <reified T : CoroutineWorker> createAndEnqueueWorker(
    context: Context,
    targetTime: LocalTime,
    uniqueWorkName: String
) {
    val initialDelay = calculateInitialDelay(targetTime)

    // 작업에 전달할 데이터 생성
    val inputData = Data.Builder()
        .putString(TodoCheckWorker.WorkConstants.INPUT_DATA_WORK_NAME, uniqueWorkName) // 고유 작업 이름 추가
        .build()

    val workRequest = PeriodicWorkRequestBuilder<T>(1, TimeUnit.DAYS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setInputData(inputData)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        uniqueWorkName,
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}

/**
 * 주어진 목표 시간에 따라 초기 지연 시간 계산
 *
 * @param targetTime 목표 시간
 * @return 초기 지연 시간(밀리초 단위)
 */
private fun calculateInitialDelay(targetTime: LocalTime): Long {
    val now = LocalTime.now()
    val currentDate = LocalDate.now()
    val targetDateTime = if (now.isBefore(targetTime)) {
        currentDate.atTime(targetTime) // 오늘의 목표 시간
    } else {
        currentDate.plusDays(1).atTime(targetTime) // 내일의 목표 시간
    }

    return Duration.between(now.atDate(currentDate), targetDateTime).toMillis()
}