package com.ycjd.payslip.util;



import org.apache.commons.lang3.StringUtils;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SaltUtil {
    /**
     *      * 字符串Base64位加密
     *      * @param str
     *      * @return
     *     
     */
    public static Map<String, Object> base64Encode(String string) {
        Map<String, Object> data = new HashMap<String, Object>();
// 生成1位随机数
        String salt = getSalt();
// 重新封装
        String str = string + salt;
// base64加密
        BASE64Encoder encoder = new BASE64Encoder();
        String result = encoder.encode(str.getBytes());
        data.put("result", result);
        data.put("salt", salt);
        return data;
    }

    /**
     *      * 字符串Base64位解密
     *      * @param str
     *      * @return
     *      * @throws IOException
     *     
     */
    public static String base64Decode(String str, String salt) {
            if (StringUtils.isNotBlank(str)) {
                BASE64Decoder decoder = new BASE64Decoder();
                String string = null;
                try {
                    string = decoder.decodeStr(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String result=string.substring(0,string.length()-salt.length());
                /*String result = string.replaceAll(salt, "");*/
                return result;
            } else {
                return null;
            }
    }

    private static String getSalt() {
        String[] ValidStr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        int number = ValidStr.length;
        String salt = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            String rand = String.valueOf(ValidStr[random.nextInt(number)]);
            salt += rand;
        }
        return salt;
    }
    public static int randomNumber(){
        return (int)(Math.random()*10+1);
    }

    public static void main(String[] args) {
        Map<String, Object> m1 = base64Encode("111");
        System.out.println(m1);
        String m2 = base64Decode("MjIy", "3");
        System.out.println(m2);
        String string="222";
        String result = string.replaceAll("2", "");
        System.out.println(result);
    }
}
