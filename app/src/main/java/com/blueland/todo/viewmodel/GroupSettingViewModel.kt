package com.blueland.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueland.todo.data.local.GroupEntity
import com.blueland.todo.data.local.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupSettingViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    val groupItems = repository.getAllGroupsFlow()

    // 항목 추가
    fun addGroup(groupName: String) {
        viewModelScope.launch {
            val newGroup = GroupEntity(groupName = groupName)
            repository.insert(newGroup)
        }
    }

    // 항목 업데이트
    fun updateGroup(todo: GroupEntity) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    // 항목 삭제
    fun deleteGroup(todo: GroupEntity) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}
