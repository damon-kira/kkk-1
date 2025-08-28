package com.kira.learning.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kira.learning.module.ai.ChoiceConverters
import com.kira.learning.bean.dao.AIResponseInfo
import com.kira.learning.bean.dao.ChatMessage
import com.kira.learning.bean.dao.AIResponseDao
import com.kira.learning.bean.dao.ChatMessageDao
import com.kira.learning.module.chat.bean.ChatEntity
import com.kira.learning.module.chat.database.ChatDao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "chat_conversation")
data class ChatConversation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastPreview: String = ""
)

@Dao
interface ChatConversationDao {
    @Insert suspend fun insert(conv: ChatConversation): Long
    @Update suspend fun update(conv: ChatConversation)
    @Query("SELECT * FROM chat_conversation ORDER BY updatedAt DESC") fun listFlow(): Flow<List<ChatConversation>>
    @Query("DELETE FROM chat_conversation WHERE id=:id") suspend fun delete(id: Long)
    @Query("SELECT * FROM chat_conversation WHERE id=:id") suspend fun find(id: Long): ChatConversation?
    @Query("UPDATE chat_conversation SET updatedAt=:ts, lastPreview=:preview WHERE id=:id") suspend fun touch(id: Long, ts: Long, preview: String)
}

@Database(
    entities = [AIResponseInfo::class, ChatMessage::class, ChatEntity::class, ChatConversation::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(ChoiceConverters::class) // 全局注册类型转换器
abstract class AppDatabase : RoomDatabase() {
    abstract fun aiResponseDao(): AIResponseDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatDao(): ChatDao
    abstract fun chatConversationDao(): ChatConversationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_response_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}