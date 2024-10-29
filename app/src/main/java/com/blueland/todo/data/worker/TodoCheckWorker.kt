package com.blueland.todo.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.blueland.todo.R
import com.blueland.todo.data.local.TodoRepository
import com.blueland.todo.utils.NotificationUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class TodoCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val todoRepository: TodoRepository
) : CoroutineWorker(appContext, params) {

    object WorkConstants {
        const val INPUT_DATA_WORK_NAME = "workName"
        const val WORK_NAME_TODAY_CHECK_TODO = "todayCheckTodo"
        const val WORK_NAME_TODAY_CREATE_TODO = "todayCreateTodo"
    }

    override suspend fun doWork(): Result {
        val workName = inputData.getString(WorkConstants.INPUT_DATA_WORK_NAME) ?: "" // 예약된 작업 타입 가져오기
        showNotification(workName)
        return Result.success()
    }

    private suspend fun showNotification(workName: String) {
        val title: String
        val message: String
        when (workName) {
            WorkConstants.WORK_NAME_TODAY_CREATE_TODO -> {
                // 미완료 할 일 작업 처리
                title = applicationContext.getString(R.string.create_title)
                message = applicationContext.getString(R.string.create_message)
            }

            WorkConstants.WORK_NAME_TODAY_CHECK_TODO -> {
                //
                val createdCount = todoRepository.getTodayRegisteredTodoCount().first()
                if (createdCount > 0) {
                    // 오늘 등록된 할 일 있음
                    val incompleteCount = todoRepository.getTodayIncompleteTodos().first()
                    if (incompleteCount > 0) {
                        // 미완료
                        title = applicationContext.getString(R.string.incomplete_title)
                        message = applicationContext.getString(R.string.incomplete_message_value, incompleteCount)
                    } else {
                        // 완료
                        title = applicationContext.getString(R.string.complete_title)
                        message = applicationContext.getString(R.string.complete_message)
                    }
                } else {
                    // 오늘 등록된 할 일 없음
                    title = applicationContext.getString(R.string.create_title)
                    message = applicationContext.getString(R.string.create_message_none)
                }
            }

            else -> {
                // 알 수 없는 작업
                return
            }
        }

        // 알림 띄움
        NotificationUtil.sendNotification(applicationContext, title, message)
    }
}

