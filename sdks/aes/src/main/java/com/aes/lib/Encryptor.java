package com.aes.lib;


import androidx.annotation.Keep;

@Keep
public class Encryptor {

    private static final String LIB_NAME = "es";
    static {
        System.loadLibrary(LIB_NAME);
    }

    /**
     * @param data
     * @param key1 秘钥
     * @param key2 秘钥偏移量
     * @return
     */
    public static native byte[] aesDecrypt(byte[] data, byte[] key1, byte[] key2);

    /**
     * @param data
     * @param key1 秘钥
     * @param key2 秘钥偏移量
     * @return
     */
    public static native byte[] aesEncrypt(byte[] data, byte[] key1, byte[] key2);

//    public static  byte[] aesDecrypt(byte[] data,int type,int isRelease){
//        return data;
//    };
//
//    public static  byte[] aesEncrypt(byte[] data,int type,int isRelease){
//        return data;
//    };


//    public static String getKeyBytes(String s) {
//        byte[]  raw = ByteConvertor.hexStringToBytes(s);
//        StringBuffer stringBuffer=new StringBuffer();
//         int len=raw.length;
//        for(int i=0;i<raw.length;i++){
//            String item=String.format("%02X", raw[i]);
//            if(i==len-1){
//                stringBuffer.append("0x"+item);
//            }else{
//                stringBuffer.append("0x"+item+" ,");
//            }
//
//        }
//        return  stringBuffer.toString();
//    }

//
//    public static int hexCharToInt(char c) {
//        if(c >= 48 && c <= 57) {
//            return c - 48;
//        } else if(c >= 65 && c <= 70) {
//            return c - 65 + 10;
//        } else if(c >= 97 && c <= 102) {
//            return c - 97 + 10;
//        } else {
//            throw new RuntimeException("invalid hex char '" + c + "'");
//        }
//    }
}
