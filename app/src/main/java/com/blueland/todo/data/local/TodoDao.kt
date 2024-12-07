package com.blueland.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    // 전체 할일 조회
    @Query("SELECT * FROM todo_table ORDER BY COALESCE(updatedAt, createdAt) DESC")
    suspend fun getAllTodos(): List<TodoEntity>

    // 특정 그룹에 속하는 할일 목록 조회
    @Query("SELECT * FROM todo_table WHERE groupId = :groupId ORDER BY COALESCE(updatedAt, createdAt) DESC")
    suspend fun getTodosByGroup(groupId: Int): List<TodoEntity>

    // 완료된 할 일의 개수 조회
    @Query("SELECT COUNT(*) FROM todo_table WHERE isCompleted = 1")
    fun getCompletedTodoCount(): Flow<Int>

    // 미완료된 할 일의 개수 조회
    @Query("SELECT COUNT(*) FROM todo_table WHERE isCompleted = 0")
    fun getIncompleteTodoCount(): Flow<Int>

    // 오늘 미완료된 할 일의 개수 조회
    @Query("SELECT COUNT(*) FROM todo_table WHERE isCompleted = 0 AND date(datetime(createdAt, 'unixepoch')) = date('now')")
    fun getTodayIncompleteTodos(): Flow<Int>

    // 오늘 등록된 할 일의 개수 조회
    @Query("SELECT COUNT(*) FROM todo_table WHERE date(datetime(createdAt, 'unixepoch')) = date('now')")
    fun getTodayRegisteredTodoCount(): Flow<Int>
}
