package com.blueland.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TodoEntity::class], version = 2, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        // 마이그레이션 추가
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 할 일 수정 일시 updatedAt 열 추가
                db.execSQL("ALTER TABLE todo_table ADD COLUMN updatedAt INTEGER")

                // 할 일 완료 일시 completedAt 열 추가
                db.execSQL("ALTER TABLE todo_table ADD COLUMN completedAt INTEGER")

                // 완료 상태이지만 completedAt 값이 없는 항목에 현재 일시 설정
                val currentTime = System.currentTimeMillis()
                db.execSQL("UPDATE todo_table SET completedAt = $currentTime WHERE isCompleted = 1 AND completedAt IS NULL")
            }
        }

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                    .addMigrations(MIGRATION_1_2) // 마이그레이션 적용
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
