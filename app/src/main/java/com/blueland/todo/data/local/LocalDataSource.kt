package com.blueland.todo.data.local

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * LocalDataSource는 로컬 저장소(예: SQLite 데이터베이스, SharedPreferences 등)와 상호작용하여 데이터를 관리합니다. 주요 역할은 다음과 같습니다:
 *
 * 데이터 읽기: 로컬 저장소에서 데이터를 읽어와 애플리케이션에 제공.
 * 데이터 쓰기: 애플리케이션에서 생성된 데이터를 로컬 저장소에 저장.
 * 캐싱: 네트워크 요청의 결과를 캐싱하여 오프라인 상태에서도 데이터를 사용할 수 있게 함.
 * 기타 로컬 작업: 특정 데이터 조작이나 변환 작업을 로컬에서 수행.
 */
class LocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val NOTIFICATION = "notification"
        private const val CREATE_NOTIFICATION = "createNotification"
        private const val CHECK_NOTIFICATION = "checkNotification"
    }

    fun getNotification(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION, true)
    }

    fun setNotification(value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(NOTIFICATION, value)
            apply()
        }
    }

    fun deleteNotification() {
        sharedPreferences.edit().apply {
            remove(NOTIFICATION)
            apply()
        }
    }

    fun getCreateNotification(): Boolean {
        return sharedPreferences.getBoolean(CREATE_NOTIFICATION, true)
    }

    fun setCreateNotification(value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(CREATE_NOTIFICATION, value)
            apply()
        }
    }

    fun deleteCreateNotification() {
        sharedPreferences.edit().apply {
            remove(CREATE_NOTIFICATION)
            apply()
        }
    }

    fun getCheckNotification(): Boolean {
        return sharedPreferences.getBoolean(CHECK_NOTIFICATION, true)
    }

    fun setCheckNotification(value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(CHECK_NOTIFICATION, value)
            apply()
        }
    }

    fun deleteCheckNotification() {
        sharedPreferences.edit().apply {
            remove(CHECK_NOTIFICATION)
            apply()
        }
    }
}
