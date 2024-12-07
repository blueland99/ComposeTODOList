package com.blueland.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Query("SELECT * FROM group_table ORDER BY createdAt DESC")
    suspend fun getAllGroups(): List<GroupEntity>

    @Delete
    suspend fun deleteGroup(group: GroupEntity)
}
