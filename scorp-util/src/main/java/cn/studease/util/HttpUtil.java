package cn.studease.util;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;

/**
 * HTTP相关工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class HttpUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static String openURL(String url) {
        return openURL(url, new HashMap(), "utf-8", true);
    }


    public static <V> String openURL(String url, Map<String, V> params) {
        return openURL(url, params, "utf-8", true);
    }


    public static <V> String openURL(String url, Map<String, V> params, boolean encode) {
        return openURL(url, params, "utf-8", encode);
    }


    public static <V> String openURL(String url, Map<String, V> params, String charset) {
        return openURL(url, params, charset, true);
    }


    public static <V> String openURL(String url, Map<String, V> params, String charset, boolean encode) {
        if (!StringUtil.hasText(charset)) {
            charset = "utf-8";
        }
        try {
            String query = WebUtil.generateUrlQueryString(params, charset, encode);

            log.info("打开URL：" + url + "，参数：" + query);

            URLConnection conn = new URL(url).openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "text/xml;text/html");
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setRequestProperty("Content-Length", Integer.toString(query.length()));
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
            conn.setRequestProperty("HOST", conn.getURL().getHost());
            PrintWriter writer = new PrintWriter(new java.io.OutputStreamWriter(conn.getOutputStream(), charset));
            writer.write(query);
            writer.flush();
            writer.close();
            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), charset));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
            return StringUtil.deleteLastChar(result.toString());
        } catch (Exception e) {
            log.trace("无法正确获取URL内容", e);
            String msg = e.getMessage();
            return "出现异常：" + msg;
        }
    }

}
