package com.util.lib

/**
 * author: weishl
 * data: 2019/12/6
 **/
object StrMatchUtil {

    /**
     * 匹配字母
     */
    fun isLetter(content: CharSequence?): Boolean {
        return Regex("""[a-zA-ZÁáÉéÍíÓóÚúÜüÏïñÑ]+""").matches(content.toString())
    }


    /**
     * 匹配一个或多个空格
     */
    fun isSpace(content: CharSequence?):Boolean{
        return Regex("""[ ]+""").matches(content.toString())
    }
}