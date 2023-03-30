package com.util.lib

import android.text.method.ReplacementTransformationMethod


/*
 *
 * Author: jinguang
 * Create: 2020/5/8 17:35
 * Description:
 */
//原本输入的小写字母
class TransInformation : ReplacementTransformationMethod() {
    /**
     * 原本输入的小写字母
     */
    override fun getOriginal(): CharArray {
        return charArrayOf(
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            'á',
            'é',
            'í',
            'ó',
            'ú',
            'ñ',
            'ü',
            'ï'

        )
    }
//    Á É Í Ó Ú Ñ ü ï
    /**
     * 替代为大写字母
     */
    override fun getReplacement(): CharArray {
        return charArrayOf(
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z',
            'Á',
            'É',
            'Í',
            'Ó',
            'Ú',
            'Ñ',
            'Ü',
            'Ï'
        )
    }
}

