package com.kira.learning.di

import android.content.Context
import androidx.room.Room
import com.kira.learning.LoanApplication
import com.kira.learning.app.AppDatabase
import com.kira.learning.app.ChatConversationDao
import com.kira.learning.bean.dao.AIResponseDao
import com.kira.learning.bean.dao.ChatMessageDao
import com.kira.learning.module.chat.database.ChatDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dao - 数据库的提供者模块
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // 提供数据库实例
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ai_response_db.db"
        ).build()
    }

    // 提供 DAO 实例（通过数据库实例获取）
    @Provides
    fun provideChatMessageDao(database: AppDatabase): ChatMessageDao {
        return database.chatMessageDao()
    }

    @Provides
    fun provideAIResponseDao(database: AppDatabase): AIResponseDao {
        return database.aiResponseDao()
    }

    @Provides
    fun provideChatDao(database: AppDatabase): ChatDao {
        return database.chatDao()
    }

    @Provides
    fun provideChatConversationDao(database: AppDatabase): ChatConversationDao = database.chatConversationDao()
}

internal fun getAppContext() = LoanApplication.getAppContext()