package com.blueland.todo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueland.todo.data.local.GroupEntity
import com.blueland.todo.data.local.TodoEntity
import com.blueland.todo.data.local.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    private val _groupItems = MutableStateFlow<List<GroupEntity>>(listOf())
    val groupItems: StateFlow<List<GroupEntity>> = _groupItems

    private val _selectedGroupId = MutableStateFlow<Int?>(null)
    val selectedGroupId: StateFlow<Int?> = _selectedGroupId
    suspend fun setGroupId(groupId: Int?) {
        _selectedGroupId.value = groupId
        loadTodos()
    }

    private val _items = MutableStateFlow<List<TodoEntity>>(listOf())
    val items: StateFlow<List<TodoEntity>> = _items

    val completeCount = repository.getCompletedTodoCount()
    val incompleteCount = repository.getIncompleteTodoCount()

    init {
        viewModelScope.launch {
            _groupItems.value = repository.getAllGroups()
            Log.e("TAG", "size = ${groupItems.value.size}")
            loadTodos()
        }
    }

    // 할 일 조회
    private suspend fun loadTodos() {
        selectedGroupId.value?.let {
            _items.value = repository.getTodosByGroup(it)
        } ?: run {
            _items.value = repository.getAllTodos()
        }
    }

    // 항목 추가
    fun addTodo(title: String) {
        val newTodo = TodoEntity(title = title)
        viewModelScope.launch {
            repository.insert(newTodo)
            loadTodos()
        }
    }

    // 항목 업데이트
    fun updateTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.update(todo)
            loadTodos()
        }
    }

    // 항목 삭제
    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.delete(todo)
            loadTodos()
        }
    }
}
