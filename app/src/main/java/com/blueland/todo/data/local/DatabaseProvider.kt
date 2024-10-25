package com.blueland.todo.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: TodoDatabase? = null

    fun getDatabase(context: Context): TodoDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todo_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
