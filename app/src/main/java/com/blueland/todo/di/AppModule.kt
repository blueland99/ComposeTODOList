package com.blueland.todo.di

import android.content.Context
import android.content.SharedPreferences
import com.blueland.todo.data.local.LocalDataSource
import com.blueland.todo.data.local.TodoDao
import com.blueland.todo.data.local.TodoDatabase
import com.blueland.todo.data.local.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return TodoDatabase.getDatabase(context)
    }

    @Provides
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepository(todoDao)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("blueland_todo", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(sharedPreferences: SharedPreferences): LocalDataSource {
        return LocalDataSource(sharedPreferences)
    }
}