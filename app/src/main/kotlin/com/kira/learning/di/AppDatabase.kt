package com.kira.learning.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kira.learning.xml.modules.ai.ChoiceConverters
import com.kira.learning.models.dao.AIResponseInfo
import com.kira.learning.models.dao.ChatMessage
import com.kira.learning.models.dao.AIResponseDao
import com.kira.learning.models.dao.ChatMessageDao
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
    entities = [AIResponseInfo::class, ChatMessage::class, ChatConversation::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ChoiceConverters::class) // 全局注册类型转换器
abstract class AppDatabase : RoomDatabase() {
    abstract fun aiResponseDao(): AIResponseDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun chatConversationDao(): ChatConversationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // 新增 chat_conversation 表（若之前 schema 未包含）
                database.execSQL(
                    """CREATE TABLE IF NOT EXISTS `chat_conversation` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        `lastPreview` TEXT NOT NULL
                    )""".trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_response_db"
                )
                    .addMigrations(MIGRATION_2_3)
                    // .fallbackToDestructiveMigration() // 如需保留老数据请保持迁移路径，不再使用破坏式升级
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}