package com.blueland.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TodoEntity::class, GroupEntity::class], version = 2, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun groupDao(): GroupDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                    .addMigrations(MIGRATION_1_2) // 마이그레이션 추가
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // 기본 "unknown" 그룹 추가
                            db.execSQL(
                                """
                                    INSERT INTO group_table (groupId, groupName, isSystemGroup, createdAt)
                                    VALUES (0, 'unknown', 1, ${System.currentTimeMillis()})
                                    """
                            )
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // group_table 생성
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS group_table (
                        groupId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        groupName TEXT NOT NULL,
                        isSystemGroup INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL
                        )"""
                )

                // 기본 "unknown" 그룹 추가
                db.execSQL(
                    """
                        INSERT INTO group_table (groupId, groupName, isSystemGroup, createdAt)
                        VALUES (0, 'unknown', 1, ${System.currentTimeMillis()})
                        """
                )

                // todo_table에 groupId 열 추가
                db.execSQL("ALTER TABLE todo_table ADD COLUMN groupId INTEGER NOT NULL DEFAULT 0")

                // 새로운 todo_table 생성
                db.execSQL(
                    """
                        CREATE TABLE todo_table_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        groupId INTEGER NOT NULL DEFAULT 0,
                        title TEXT NOT NULL,
                        isCompleted INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER,
                        completedAt INTEGER,
                        FOREIGN KEY(groupId) REFERENCES group_table(groupId) ON DELETE CASCADE
                        )
                        """
                )

                // 기존 데이터 복사
                db.execSQL(
                    """
                        INSERT INTO todo_table_new (id, groupId, title, isCompleted, createdAt, updatedAt, completedAt)
                        SELECT id, groupId, title, isCompleted, createdAt, updatedAt, completedAt
                        FROM todo_table
                        """
                )

                // 기존 테이블 삭제
                db.execSQL("DROP TABLE todo_table")

                // 새 테이블 이름 변경
                db.execSQL("ALTER TABLE todo_table_new RENAME TO todo_table")

                // groupId에 대한 인덱스 추가
                db.execSQL("CREATE INDEX IF NOT EXISTS index_todo_table_groupId ON todo_table(groupId)")
            }
        }
    }
}