package cn.studease.util;

/**
 * Author: liushaoping
 * Date: 2015/8/25.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        String content = "*-,./;'[]\\=-`!<>?:{}|+_&^%$#@~，。、；‘“”’【】、=-·~！@#%……&*《》？：“{}|";
        System.out.println(String.format("before replace punctuation: %s", content));
        System.out.println(String.format("after replace punctuation: %s", StringUtil.clearPunctuation(content)));

        content = " asdkjfs             sjdf    kd\n\r\tsss ";
        System.out.println(String.format("before replace white space: %s", content));
        System.out.println(String.format("after replace white space: %s", StringUtil.clearWhiteSpace(content)));

    }

}
