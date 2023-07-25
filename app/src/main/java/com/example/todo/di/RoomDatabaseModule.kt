package com.example.todo.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.database.TaskDatabase
import com.example.todo.util.TASK_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule
{
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context:Context) =
        Room.databaseBuilder(context, TaskDatabase::class.java, TASK_DATABASE).build()


    @Singleton
    @Provides
    fun provideYourDao(db: TaskDatabase) = db.getTaskDao()
}