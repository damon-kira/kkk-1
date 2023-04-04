package com.colombia.credit.expand

import com.colombia.credit.LoanApplication.Companion.getAppContext
import com.colombia.credit.R
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import java.lang.StringBuilder
import java.util.regex.Pattern


/**
 * 修改手机号校验格式，以1开头的11位数字，或者是非1开头的10位数字
 */
const val mobileRegex = "^[1]\\d{10}$|^[234567890]\\d{9}$"
fun checkMobile(mobile: String?): Boolean {
    if (!Pattern.matches(mobileRegex, mobile)) {
        return false
    }
    return true
}

fun checkMobileWithoutToast(mobile: String?): Boolean {

    if (mobile.isNullOrEmpty()) {
        return false
    }
    return Pattern.matches(mobileRegex, mobile)
}

/**
 * 是否是合法的字符包括
 * @param str String
 */
fun isValidChar(str: String): Boolean {
    return Regex("[-0-9a-zA-ZÁáÉéÍíÓóÚúÜüÏïñÑ*]+").matches(str)
}

/**
 * 是否是合法的字符包括
 * @param str String
 */
fun isValidChar2(str: String): Boolean {
    return Regex("[0-9a-zA-ZÁáÉéÍíÓóÚúÜüÏïñÑ]+").matches(str)
}

fun isVaildCheckNumber(str: String):Boolean{
    return Regex("[0-9a-fA-F]+").matches(str)
}

/**
 * 是否是合法的字符包括
 * @param str String
 */
fun isValidCharNumber(str: String): Boolean {
    return Regex("""[0-9]+""").matches(str)
}

/** 是否全是数字 */
fun String.isValidNumber() = Regex("[0-9]+").matches(this)

fun isSameNumber(str1: String ,str2: String): Boolean{
    var s1 = str1
    var s2 = str2
    if (s1.isNotEmpty() && s2.isNotEmpty()) {
        if (s1.startsWith("0")) {
            s1 = s1.substring(1)
        }
        if (s2.startsWith("0")) {
            s2 = s2.substring(1)
        }
        return s1 == s2
    }
    return false
}

fun checkMobileWithOutToast(mobile: String?): Boolean {
    if (mobile.isNullOrEmpty()) {
        return false
    }
    if (mobile.startsWith("1")) {
        if (mobile.length != 11) {
            return false
        }
    } else {
        if (mobile.length != 10) {
            return false
        }
    }
    return true
}

fun checkEmailFormat(email: String?): Boolean{
    val regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"
    if(email.isNullOrEmpty()){
        return false
    }
    if(email.matches(regex.toRegex())){
        return true
    }
    return false
}

/**
 * 校验邮政编码
 *
 * 长度为5位的纯数字，开头只能是0-9
 */
fun checkPostalCode(code: String?):Boolean{
    if (code.isNullOrEmpty()){
        return false
    }
    val regex = "^\\d{5}\$"

    if (!Pattern.matches(regex,code)){
        return false
    }
    return true
}

/**
 * 校验rfc 纳税识别号编码  后三位不填写则不校验
 *
 * 共13位，前四位字母，5-10位为数字，后三位不做限制；eg:ABC800520XXX
 */
fun checkRfcCode(code: String?):Boolean{
    if (code.isNullOrEmpty()){
        return true
    }
    val regex = "^[a-zA-Z]{4}[0-9]{6}[a-zA-Z0-9]{3}\$"
    if (!Pattern.matches(regex,code)){
        return false
    }
    return true
}
/**
 * 校验家庭手机号
 * 可以不填，填了的话长度必须是8-15
 */
fun isIllegalFamilyPhone(phone: String): Boolean {
    return isEmpty(phone) || (phone.length in 8..15)
}

/**
* 正则判断emoji表情
* @param input
* @return
*/
 fun isEmoji(input: String): Boolean {
    val p =
        Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\ud83e\udc00-\ud83e\udfff]" + "|[\u2100-\u32ff]|[\u0030-\u007f][\u20d0-\u20ff]|[\u0080-\u00ff]")
    val m = p.matcher(input)
    return m.find()
}

/**
 * 正则查找元音字母
 */
fun checkVowelChar(str: String):Boolean{
    val p = "[aeiou]"
    if (str.isEmpty())return false

    val compile = Pattern.compile(p)
    val matcher = compile.matcher(str.toLowerCase())

    return matcher.find()
}

/**
 * 获取第一个元音字母
 */
fun getFirstVowel(str: String): String {
    var firstVowel = ""
    for (char in str.toLowerCase()) {
        if ("aeiou".contains(char)) {
            firstVowel = char.toString()
            break
        }
    }
    return firstVowel
}

fun checkCurpStr(curp: String?):String{

//    if (curp == null || curp.length<18){
//        return getAppContext().getString(R.string.fill_in_correct_curp)
//    }
//    if (!checkCharacter(convertSpanish(curp.substring(0,4)))){
//        return getAppContext().getString(R.string.fill_in_correct_four_char)
//    }
//    if (!checkCurpBirth(curp)){
//        return getAppContext().getString(R.string.fill_in_correct_birch)
//    }
//    val sex = curp.substring(10,11).toLowerCase()
//    if (sex != "m" && sex != "h"){
//        return getAppContext().getString(R.string.fill_in_correct_sex)
//    }

 return ""
}

/**
 * 转换西班牙语重音符号
 */
fun convertSpanish(str: String):String{
    if (str.isNullOrEmpty())return ""
    val stringBuilder = StringBuilder()
    str.forEach {
//     Áá Éé Íí Óó Úú Ññ üï
        var char = it.toString().toUpperCase()
        char = when (char) {
            "Á" -> "A"
            "É" -> "E"
            "Í" -> "I"
            "Ó" -> "O"
            "Ú" -> "U"
            "Ñ" -> "N"
            "Ü" -> "U"
            "Ï" -> "I"
            else -> char
        }
        stringBuilder.append(char)
    }
    return stringBuilder.toString()
}

/**
 * 校验字符
 */
fun checkCharacter(str:String):Boolean{
    val rx = "^[A-Za-z]{4}"
    val matches = Pattern.matches(rx, str)
    logger_d("curp>>>","字符》》$str》》》匹配》》$matches")
    return matches
}

/**
 * 校验curp  前四位为字母--第5位到第10位为数字--第11位为H或M--填写总位数为18位
 */
fun checkCurp(curp:String?):Boolean{

    if (curp == null || curp.length < 18)return false
    val rx = "^[A-Za-z]{4}[0-9]{6}(h|m)[A-Za-z0-9]{7}"
    return Pattern.matches(rx, curp.toLowerCase())
}


/**
 * 校验curp 中的生日 后四位
 */
fun checkCurpBirth(curp: String?):Boolean{
    if (curp == null || curp.length < 18) return false
    val birth = curp.substring(6, 10)

    val rx = "(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])"

    val matches = Pattern.matches(rx, birth)

    logger_d("curp>>>","生日》》$birth》》》匹配》》$matches")
    return matches

}


fun checkClabeNum(clabe: String?): Boolean {
    clabe ?: return false
    val regex = Regex("^\\d{18}") // 匹配是否纯数字
    if (!clabe.matches(regex)) return false
    val finalNum = 10 // 被取余数
    val baseNum = arrayListOf(3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7)
    val size = clabe.length
    val lastChar: Int = clabe[size - 1] - '0'
    var result = 0
    for (index in 0 until size - 1) {
        result += (((clabe[index] - '0') * baseNum[index]) % finalNum)
    }
    result = (finalNum - result % finalNum) % finalNum
    logger_e("debug_Pattern", "check clabe Num result = $result")
    return result == lastChar
}

/**
 * 校验家庭手机号
 * 可以不填，填了的话长度必须是8-15
 */
fun checkHomePhone(phone: String): Boolean {
    return phone.length in 8..15
}