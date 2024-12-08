package com.blueland.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Query("SELECT * FROM group_table ORDER BY createdAt DESC")
    fun getAllGroupsFlow(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM group_table ORDER BY createdAt DESC")
    suspend fun getAllGroups(): List<GroupEntity>

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Transaction
    suspend fun deleteGroupAndReassignTodos(group: GroupEntity) {
        // 삭제되는 그룹의 할 일을 groupId 0으로 업데이트
        reassignTodosToUnknownGroup(group.groupId)
        // 그룹 삭제
        deleteGroup(group)
    }

    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    @Query("UPDATE todo_table SET groupId = 0 WHERE groupId = :groupId")
    suspend fun reassignTodosToUnknownGroup(groupId: Int)
}
