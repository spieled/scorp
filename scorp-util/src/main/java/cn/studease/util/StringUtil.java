package cn.studease.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import org.slf4j.LoggerFactory;

/**
 * 字符串相关工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class StringUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StringUtil.class);
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static boolean isPhoneNumber(String number) {
        try {
            return (number != null) && (number.length() == 11) && (number.startsWith("1")) && (Pattern.compile("^[0123456789]+$").matcher(number).matches());
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean isEmail(String email) {
        return (email != null) && (Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*").matcher(email.toLowerCase()).matches());
    }

    public static String toRMB(String fee) {
        String num = "零壹贰叁肆伍陆柒捌玖";
        String dw = "圆拾佰仟万亿";
        String[] mm = fee.split("/.");
        String money = mm[0];
        String result = num.charAt(Integer.parseInt(new StringBuilder().append("").append(mm[1].charAt(0)).toString())) + "角" + num.charAt(Integer.parseInt(new StringBuilder().append("").append(mm[1].charAt(1)).toString())) + "分";
        for (int i = 0; i < money.length(); i++) {
            String str = "";
            int n = Integer.parseInt(money.substring(money.length() - i - 1, money.length() - i));
            str = str + num.charAt(n);
            if (i == 0) {
                str = str + dw.charAt(i);
            } else if ((i + 4) % 8 == 0) {
                str = str + dw.charAt(4);
            } else if (i % 8 == 0) {
                str = str + dw.charAt(5);
            } else {
                str = str + dw.charAt(i % 4);
            }
            result = str + result;
        }
        result = result.replaceAll("零([^亿万圆角分])", "零");
        result = result.replaceAll("亿零+万", "亿零");
        result = result.replaceAll("零+", "零");
        result = result.replaceAll("零([亿万圆])", "$1");
        result = result.replaceAll("壹拾", "拾");
        return result;
    }

    public static String[] split(String str, String regex) {
        if (str == null) {
            return new String[0];
        }
        return str.split(regex);
    }

    public static boolean match(String pattern, String str) {
        if ((!hasText(pattern)) || (!hasText(str))) {
            return false;
        }
        boolean startWith = pattern.startsWith("*");
        boolean endWith = pattern.endsWith("*");
        String[] array = split(pattern, "*");

        int lastIndex = -1;
        int currentIndex;
        switch (array.length) {
            case 0:
                return true;
            case 1:
                currentIndex = str.indexOf(array[0]);
                if ((startWith) && (endWith)) {
                    return currentIndex >= 0;
                }
                if (startWith) {
                    return currentIndex + array[0].length() == str.length();
                }
                if (endWith) {
                    return currentIndex == 0;
                }
                return str.equals(pattern);
        }
        for (String part : array) {
            if (hasText(part)) {
                currentIndex = str.indexOf(part);
                if (currentIndex > lastIndex) {
                    lastIndex = currentIndex;
                } else
                    return false;
            }
        }
        return true;
    }

    public static boolean hasText(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static boolean hasText(String[] array) {
        return (array != null) && (array.length > 0) && (array[0] != null) && (array[0].length() > 0);
    }

    public static String uuid() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String uuidNoSplit() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String randomString(int length) {
        if (length < 1) {
            return "";
        }
        java.util.Random rand = new java.util.Random();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = Constants.AZ09[rand.nextInt(Constants.AZ09_LENGTH)];
        }
        return new String(randBuffer);
    }

    public static <T> int compare(T v1, T v2) {
        if (v1 == null) {
            if (v2 == null) {
                return 0;
            }
            return -1;
        }

        if (v2 == null) {
            return 1;
        }
        int compared = 0;
        if (v1.getClass().getName().equals("java.lang.String")) {
            String s1 = (String) v1;
            String s2 = (String) v2;
            compared = compareString(s1, s2);
        } else {
            try {
                Comparable c1 = (Comparable) v1;
                Comparable c2 = (Comparable) v2;
                compared = c1.compareTo(c2);
            } catch (Exception ignored) {
                log.trace("无法正确排序", ignored);
            }
        }
        return compared;
    }

    public static int compareString(String s1, String s2) {
        if (s1 == null) {
            s1 = "";
        }
        if (s2 == null) {
            s2 = "";
        }


        int l1 = s1.length();
        int l2 = s2.length();
        int lx = l1 - l2;


        for (int i = 0; i < Math.min(l1, l2); i++) {
            if ((Character.isSupplementaryCodePoint(s1.charAt(i))) || (Character.isSupplementaryCodePoint(s2.charAt(i)))) {
                return comparePinYin(s1, s2);
            }
        }

        for (int i = Math.min(l1, l2); i < Math.max(l1, l2); i++) {
            if ((lx > 0) && (Character.isSupplementaryCodePoint(s1.charAt(i))))
                return 1;
            if ((lx < 0) && (Character.isSupplementaryCodePoint(s2.charAt(i)))) {
                return -1;
            }
        }


        if (lx != 0) {
            return lx;
        }


        for (int i = 0; i < s1.length(); i++) {
            int x = s1.charAt(i) - s2.charAt(i);
            if (x != 0) {
                return x;
            }
        }


        return 0;
    }

    public static int comparePinYin(String s1, String s2) {
        for (int i = 0; (i < s1.length()) && (i < s2.length()); i++) {
            int codePoint1 = s1.charAt(i);
            int codePoint2 = s2.charAt(i);

            if ((Character.isSupplementaryCodePoint(codePoint1)) || (Character.isSupplementaryCodePoint(codePoint2))) {
                i++;
            }
            if (codePoint1 != codePoint2) {
                if ((Character.isSupplementaryCodePoint(codePoint1)) || (Character.isSupplementaryCodePoint(codePoint2))) {
                    return codePoint1 - codePoint2;
                }
                String pinyin1 = getPinYin((char) codePoint1);
                String pinyin2 = getPinYin((char) codePoint2);
                if ((pinyin1 != null) && (pinyin2 != null)) {
                    if (!pinyin1.equals(pinyin2)) {
                        return pinyin1.compareTo(pinyin2);
                    }
                } else {
                    return codePoint1 - codePoint2;
                }
            }
        }
        return s1.length() - s2.length();
    }

    public static String getPinYin(char c) {
        String[] pinyin = net.sourceforge.pinyin4j.PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyin == null) {
            return "";
        }
        return pinyin[0];
    }

    public static String getPinYin(String str) {
        return getPinYin(str, false);
    }

    public static String getPinYin(String str, boolean full) {
        if (!hasText(str)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        char[] cs = str.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(net.sourceforge.pinyin4j.format.HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(net.sourceforge.pinyin4j.format.HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : cs) {
            if (c > '') {
                try {
                    String[] pinyins = net.sourceforge.pinyin4j.PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyins != null) {
                        if (full) {
                            sb.append(pinyins[0]);
                        } else {
                            sb.append(pinyins[0].charAt(0));
                        }
                    }
                } catch (net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination e) {
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString().replaceAll("\\W", "").trim();
    }

    public static String deleteLastChar(String str) {
        if (hasText(str)) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String deleteFirstChar(String str) {
        if (hasText(str)) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    public static String upperCaseFirstChar(String str) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String lowerCaseFirstChar(String str) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toUpperCase(String str) {
        return hasText(str) ? str.toUpperCase() : str;
    }

    public static String toLowerCase(String str) {
        return hasText(str) ? str.toLowerCase() : str;
    }

    public static String aesEncrypt(String plainText, String key) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = plainText.getBytes("utf-8");
            cipher.init(1, keySpec);
            return byte2hex(cipher.doFinal(byteContent));
        } catch (Exception e) {
            throw new RuntimeException("采用AES算法加密失败", e);
        }
    }

    public static String aesDecrypt(String cipherText, String key) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher aes = Cipher.getInstance("AES");
            aes.init(2, keySpec);
            return new String(aes.doFinal(hex2byte(cipherText)));
        } catch (Exception e) {
            throw new RuntimeException("采用AES算法解密失败", e);
        }
    }

    public static String md5(String plainText) {
        if (!hasText(plainText)) {
            return "";
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(plainText.getBytes());
            byte[] special = {2, 3, 7, 11, 14};
            for (byte i : special) {
                int b = md5Bytes[i];
                b += i;
                if (b > 127) {
                    b -= 127;
                }
                md5Bytes[i] = ((byte) b);
            }
            return byte2hex(md5.digest(md5Bytes));
        } catch (Exception e) {
            throw new RuntimeException("采用MD5算法加密失败", e);
        }
    }

    public static String md5Raw(String plainText) {
        if (!hasText(plainText)) {
            return "";
        }
        try {
            return byte2hex(MessageDigest.getInstance("MD5").digest(plainText.getBytes("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException("采用MD5算法加密失败", e);
        }
    }

    public static String sha1(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes("utf-8"));
            byte[] bytes = messageDigest.digest();
            int len = bytes.length;
            StringBuilder buf = new StringBuilder(len * 2);
            for (int j = 0; j < len; j++) {
                buf.append(HEX_DIGITS[(bytes[j] >> 4 & 0xF)]);
                buf.append(HEX_DIGITS[(bytes[j] & 0xF)]);
            }
            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException("采用SHA1算法加密失败", e);
        }
    }

    public static String readStreamString(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        StringBuffer out = new StringBuffer();
        try {
            byte[] b = new byte['က'];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                out.append(new String(b, 0, n, "utf-8"));
            }
            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException("读取流失败", e);
        }
    }


    public static byte[] readStreamBytes(InputStream inputStream) {
        try {
            BufferedInputStream bufin = new BufferedInputStream(inputStream);
            int buffSize = 4096;
            ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);
            byte[] temp = new byte[buffSize];
            int size;
            while ((size = bufin.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            inputStream.close();
            return out.toByteArray();
        } catch (Exception e) {
        }
        return new byte[0];
    }


    public static String byte2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    public static byte[] hex2byte(String hex) {
        if (hex.length() < 1) {
            return null;
        }
        byte[] result = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length() / 2; i++) {
            int high = Integer.parseInt(hex.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hex.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = ((byte) (high * 16 + low));
        }
        return result;
    }

    public static String trim(String parallel) {
        return parallel == null ? Constants.EMPTY : parallel.trim();
    }
}
