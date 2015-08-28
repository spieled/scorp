package cn.studease;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Author: liushaoping
 * Date: 2015/8/25.
 */
public class RegexSensitiveWordFilter {

    private static Pattern pattern = null;

    static {
        pattern = Pattern.compile(getBadWordsStrings());
    }

    private static String getBadWordsStrings() {
        StringBuilder sb = new StringBuilder();
        Set<String> badWords = new SensitiveWordInit().readSensitiveWordFile();
        for (String word : badWords) {
            sb.append(word).append("|");
        }

        if (badWords.size() > 0) {
            final int index = sb.length() - 1;
            if (sb.charAt(index) == '|')
                sb.deleteCharAt(index);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        RegexSensitiveWordFilter filter = new RegexSensitiveWordFilter();
        String txt = "通辽科尔沁区->呼市有玉米35--65吨马上装价高急走 江贼民";
        int times = 100000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            filter.isContainSensitiveWord(txt);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Regex execute %s times, it cost %s ms", times, endTime - startTime));

        // 1000 badWord, Regex execute 100000 times, it cost 27 ms
        // 5700 badWord, Regex execute 100000 times, it cost 45 ms
        // 8000 badWord, Regex execute 100000 times, it cost 32 ms

    }

    public boolean isContainSensitiveWord(String txt) {
        return pattern.matcher(txt).find();
    }

}
