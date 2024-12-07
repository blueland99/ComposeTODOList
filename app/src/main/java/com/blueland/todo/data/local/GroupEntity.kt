package com.blueland.todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_table")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId: Int = 0,
    val groupName: String,
    val isSystemGroup: Boolean = false, // 시스템 그룹 여부
    val createdAt: Long = System.currentTimeMillis()
)
