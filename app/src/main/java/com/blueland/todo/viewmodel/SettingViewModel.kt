package com.blueland.todo.viewmodel

import androidx.lifecycle.ViewModel
import com.blueland.todo.data.local.LocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _notification = MutableStateFlow(true)
    val notification: StateFlow<Boolean> = _notification
    fun setNotification(value: Boolean) {
        _notification.value = value
        localDataSource.setNotification(value)
    }

    private val _createNotification = MutableStateFlow(true)
    val createNotification: StateFlow<Boolean> = _createNotification
    fun setCreateNotification(value: Boolean) {
        _createNotification.value = value
        localDataSource.setCreateNotification(value)
    }

    private val _checkNotification = MutableStateFlow(true)
    val checkNotification: StateFlow<Boolean> = _checkNotification
    fun setCheckNotification(value: Boolean) {
        _checkNotification.value = value
        localDataSource.setCheckNotification(value)
    }

    init {
        loadSettingData()
    }

    private fun loadSettingData() {
        _notification.value = localDataSource.getNotification()
        _createNotification.value = localDataSource.getCreateNotification()
        _checkNotification.value = localDataSource.getCheckNotification()
    }
}
