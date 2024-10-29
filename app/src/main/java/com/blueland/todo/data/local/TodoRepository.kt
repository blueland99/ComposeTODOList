package com.blueland.todo.data.local

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    fun getAllTodos(): Flow<List<TodoEntity>> {
        return todoDao.getAllTodos()
    }

    // 완료된 할 일 개수
    fun getCompletedTodoCount(): Flow<Int> {
        return todoDao.getCompletedTodoCount()
    }

    // 미완료된 할 일 개수
    fun getIncompleteTodoCount(): Flow<Int> {
        return todoDao.getIncompleteTodoCount()
    }

    // 미완료된 할 일 개수
    fun getTodayIncompleteTodos(): Flow<Int> {
        return todoDao.getTodayIncompleteTodos()
    }

    // 오늘 등록된 할 일의 개수 조회
    fun getTodayRegisteredTodoCount(): Flow<Int> {
        return todoDao.getTodayRegisteredTodoCount()
    }

    suspend fun insert(todo: TodoEntity) {
        todoDao.insertTodo(todo)
    }

    suspend fun update(todo: TodoEntity) {
        todoDao.updateTodo(todo)
    }

    suspend fun delete(todo: TodoEntity) {
        todoDao.deleteTodo(todo)
    }
}
