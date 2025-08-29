package com.kira.learning.module.chat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kira.learning.module.chat.bean.ChatEntity
import kotlinx.coroutines.flow.Flow

// app/src/main/kotlin/com/example/ai/chatdemo/database/ChatDao.kt
@Dao
interface ChatDao {
    // 获取所有聊天记录（按时间倒序）
    @Query("SELECT * FROM chat_table ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<ChatEntity>>

    // 插入新消息
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(chat: ChatEntity)
}

// app/src/main/kotlin/com/example/ai/chatdemo/database/AppDatabase.kt
//@Database(
//    entities = [ChatEntity::class], version = 1, exportSchema = false
//)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun chatDao(): ChatDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext, AppDatabase::class.kotlin, "chat_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}