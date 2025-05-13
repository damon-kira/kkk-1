//package com.kira.learning.module.ai
//
//
//class ChatMessage //this.timestamp = System.currentTimeMillis();
//    (
//    val message: String,
//    val isUser: Boolean,
//    val timestamp: Long,
//    var conversationId: Long
//) {
//    val title: String?
//        get() {
//            // 返回第一句的内容作为标题
//            val lines: Array<String?> =
//                message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            if (lines.size > 0) {
//                return lines[0]
//            } else {
//                return message
//            }
//        }
//}
