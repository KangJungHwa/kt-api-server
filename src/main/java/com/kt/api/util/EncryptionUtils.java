package com.kt.api.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtils {
    //암호화
    public static String getEncodingStr(String str){
        byte[] pass = str.getBytes(StandardCharsets.UTF_8);
        String encodedStr = Base64.getEncoder().encodeToString(pass);
        return encodedStr;
    }
    //복호화
    public static String getDecodingStr(String str){
        byte[] decoded = Base64.getDecoder().decode(str);
        String decodedStr = new String(decoded,StandardCharsets.UTF_8);
        return decodedStr;
    }
}
