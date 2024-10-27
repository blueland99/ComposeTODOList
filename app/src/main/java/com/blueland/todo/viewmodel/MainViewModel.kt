package com.blueland.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueland.todo.data.local.TodoEntity
import com.blueland.todo.data.local.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    val allTodos = repository.allTodos

    // 항목 추가
    fun addTodo(title: String) {
        val newTodo = TodoEntity(title = title)
        viewModelScope.launch {
            repository.insert(newTodo)
        }
    }

    // 항목 업데이트
    fun updateTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    // 항목 삭제
    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}
