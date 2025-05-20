package com.kira.learning.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kira.learning.module.ai.ChoiceConverters
import com.kira.learning.bean.AIResponseInfo
import com.kira.learning.bean.ChatMessage
import com.kira.learning.db.dao.AIResponseDao
import com.kira.learning.db.dao.ChatMessageDao
import com.kira.learning.module.chat.bean.ChatEntity
import com.kira.learning.module.chat.database.ChatDao

@Database(
    entities = [AIResponseInfo::class, ChatMessage::class, ChatEntity::class], // 包含的实体类
    version = 1,                           // 数据库版本
    exportSchema = false                   // 是否导出 Schema 文件
)
@TypeConverters(ChoiceConverters::class) // 全局注册类型转换器
abstract class AppDatabase : RoomDatabase() {
    abstract fun aiResponseDao(): AIResponseDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_response_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}