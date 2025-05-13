//package com.kira.learning.module.ai
//
//import android.annotation.SuppressLint
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//
//class ChatDatabaseHelper(context: Context?) :
//    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL(CREATE_TABLE_CHAT_MESSAGES)
//        db.execSQL(CREATE_TABLE_CONVERSATION)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        // 在这里添加数据迁移逻辑，例如当数据库版本升级时，更新表结构或数据
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES)
//        onCreate(db)
//    }
//
//    // 添加查询、更新、删除等其他方法...
//    // 获取所有聊天消息
//    //    public Cursor getAllChatMessages() {
//    //        SQLiteDatabase db = getReadableDatabase();
//    //        String query = "SELECT * FROM " + TABLE_CHAT_MESSAGES;
//    //        return db.rawQuery(query, null);
//    //    }
//    // 将谈话记录Conversation对象保存到数据库
//    fun saveConversation(conversation: Conversation) {
//        val db = getWritableDatabase()
//        for (chatMessage in conversation.chatMessages) {
//            val values = ContentValues()
//            values.put(COLUMN_MESSAGE_TEXT, chatMessage.message)
//            //            chatMessage.setTimestamp(); // 设置消息时间戳
////            values.put(COLUMN_TIMESTAMP, chatMessage.getTimestamp());
//            values.put(COLUMN_IS_USER, if (chatMessage.isUser == true) 1 else 0)
//            values.put(COLUMN_CONVERSATION_ID, conversation.conversationId)
//            db.insert(TABLE_CHAT_MESSAGES, null, values)
//        }
//        val values = ContentValues()
//        values.put(COLUMN_CONVERSATION_ID, conversation.conversationId)
//        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis())
//        values.put(COLUMN_CONPAYERNAME, conversation.conPayerName)
//        db.insert(TABLE_CONVERSATION, null, values)
//        db.close()
//    }
//
//    // 只保存最后一条聊天记录
//    fun saveLastChatMessage(chatMessage: ChatMessage, conversationId: Long) {
//        val db = getWritableDatabase()
//        val values = ContentValues()
//        values.put(COLUMN_MESSAGE_TEXT, chatMessage.message)
//        values.put(COLUMN_IS_USER, if (chatMessage.isUser) 1 else 0)
//        values.put(COLUMN_CONVERSATION_ID, conversationId)
//        db.insert(TABLE_CHAT_MESSAGES, null, values)
//        db.close()
//    }
//
//    @SuppressLint("Range")
//    fun getChatMessages_cid(conversationId: Long): ArrayList<ChatMessage> {
//        val db = getReadableDatabase()
//        val query =
//            "SELECT * FROM " + TABLE_CHAT_MESSAGES + " WHERE " + COLUMN_CONVERSATION_ID + " = " + conversationId
//        val cursor = db.rawQuery(query, null)
//        val chatMessages = ArrayList<ChatMessage>()
//        while (cursor.moveToNext()) {
//            val messageText = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_TEXT))
//            val isUser = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_USER)) == 1
//            val chatMessage = ChatMessage(messageText, isUser, conversationId)
//            chatMessages.add(chatMessage)
//        }
//        cursor.close()
//        return chatMessages
//    }
//
//    @get:SuppressLint("Range")
//    val allConversations: MutableList<Conversation?>
//        get() {
//            // 查找所有的conversationID并在聊天记录表中查找对应的聊天记录
//            val db = getReadableDatabase()
//            val query =
//                "SELECT * FROM " + TABLE_CONVERSATION
//            val cursor = db.rawQuery(query, null)
//            // 使用一个Conversation来存储ID相同的聊天记录
//            val conversationList: MutableList<Conversation?> =
//                ArrayList<Conversation?>()
//            while (cursor.moveToNext()) {
//                val conversationId =
//                    cursor.getLong(cursor.getColumnIndex(COLUMN_CONVERSATION_ID))
//                val timestamp =
//                    cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP))
//                val chatMessages =
//                    getChatMessages_cid(conversationId)
//                val conPayerName =
//                    cursor.getString(cursor.getColumnIndex(COLUMN_CONPAYERNAME))
//                conversationList.add(
//                    Conversation(
//                        conversationId,
//                        timestamp,
//                        chatMessages,
//                        conPayerName
//                    )
//                )
//            }
//            cursor.close()
//            return conversationList
//        }
//
//    fun deleteConversation(conversationId: Long) {
//        val db = getWritableDatabase()
//        db.delete(
//            TABLE_CONVERSATION,
//            COLUMN_CONVERSATION_ID + " = ?",
//            arrayOf<String>(conversationId.toString())
//        )
//    } // 保存并更新的对话
//
//    companion object {
//        private const val DATABASE_NAME = "KunKunChat.db"
//        private const val DATABASE_VERSION = 1
//
//        private const val TABLE_CHAT_MESSAGES = "chat_messages"
//        private const val COLUMN_ID = "_id"
//        private const val COLUMN_MESSAGE_TEXT = "message_text"
//
//        private const val COLUMN_IS_USER = "is_user"
//
//        private const val COLUMN_CONVERSATION_ID = "conversation_id"
//
//        private val CREATE_TABLE_CHAT_MESSAGES = "CREATE TABLE " + TABLE_CHAT_MESSAGES + "(" +
//                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                COLUMN_MESSAGE_TEXT + " TEXT NOT NULL," +
//                COLUMN_IS_USER + " INTEGER NOT NULL," +
//                COLUMN_CONVERSATION_ID + " INTEGER NOT NULL" +
//                ")"
//
//        // 创建一张表用于保存所有的conversationID
//        private const val TABLE_CONVERSATION = "conversation"
//        private const val COLUMN_CONVERSATION = "_id"
//        private const val COLUMN_TIMESTAMP = "timestamp"
//        private const val COLUMN_CONPAYERNAME = "conPayerName"
//
//        //private static final String COLUMN_CONVERSATION_ID_VALUE = "conversation_id_value";
//        private val CREATE_TABLE_CONVERSATION = "CREATE TABLE " + TABLE_CONVERSATION + "(" +
//                COLUMN_CONVERSATION + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // COLUMN_CONVERSATION_ID 是外键
//                COLUMN_CONVERSATION_ID + " INTEGER NOT NULL," +
//                COLUMN_TIMESTAMP + " INTEGER NOT NULL," +
//                COLUMN_CONPAYERNAME + " TEXT NOT NULL" +
//                ")"
//    }
//}