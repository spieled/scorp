package cn.studease.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * WEB相关工具类
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class WebUtil {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(WebUtil.class);
    private static final ThreadLocal<WebApplicationContext> wacThreadLocal = new ThreadLocal();

    public static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (Exception e) {
            log.trace("无法正确获取HttpServletRequest对象", e);
        }
        return null;
    }

    public static void setSessionAttribute(String key, Object value) {
        getHttpServletRequest().getSession().setAttribute(key, value);
    }


    public static Object getSessionAttribute(String key) {
        return getHttpServletRequest().getSession().getAttribute(key);
    }


    public static ServletContext getServletContext() {
        return getWebApplicationContext().getServletContext();
    }


    public static void setApplicationAttribute(String key, Object value) {
        getServletContext().setAttribute(key, value);
    }


    public static Object getApplicationAttribute(String key) {
        return getServletContext().getAttribute(key);
    }


    public static WebApplicationContext getWebApplicationContext() {
        WebApplicationContext wac = (WebApplicationContext) wacThreadLocal.get();
        if (wac == null) {
            wac = org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
        }

        WebApplicationContext child = (WebApplicationContext) wac.getServletContext().getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatch");
        return child == null ? wac : child;
    }


    public static void setWebApplicationContext(WebApplicationContext wac) {
        wacThreadLocal.set(wac);
    }


    public static void publishEvent(org.springframework.context.ApplicationEvent event) {
        getWebApplicationContext().publishEvent(event);
    }

    public static String getRequestValue(String name, String defaultValue) {
        String value = getHttpServletRequest().getParameter(name);
        return value == null ? defaultValue : value;
    }

    public static boolean isAjax() {
        return isAjax(getHttpServletRequest());
    }


    public static boolean isAjax(HttpServletRequest request) {
        try {
            return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        } catch (Exception e) {
        }
        return false;
    }

    public static String getRequestURI() {
        return getHttpServletRequest().getRequestURI();
    }

    private static void appendParamToUrlQuery(StringBuffer query, String key, String value, String charset, boolean encode) {
        if (key != null && key.trim().length() > 0) {
            query.append(key);
            query.append("=");
        }

        if (charset == null || charset.trim().length() == 0) {
            charset = "utf-8";
        }

        value = value.replaceAll("\n", "").replaceAll(" ", "");

        if (encode) {
            try {
                value = java.net.URLEncoder.encode(value, charset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("无法对参数进行URL编码，遇到了不支持的编码：" + e.getMessage(), e);
            }
        }

        query.append(value);
        query.append("&");
    }


    public static <V> String generateUrlQueryString(Map<String, V> params) {
        return generateUrlQueryString(params, "utf-8", true);
    }


    public static <V> String generateUrlQueryString(Map<String, V> params, String charset) {
        return generateUrlQueryString(params, charset, true);
    }


    public static <V> String generateUrlQueryString(Map<String, V> params, String charset, boolean encode) {
        if ((params == null) || (params.size() == 0)) {
            return "";
        }
        StringBuffer query = new StringBuffer();
        for (Map.Entry<String, V> param : params.entrySet()) {
            String key = (String) param.getKey();
            Object value = param.getValue();
            if (value == null) {
                value = "";
            }
            if (value.getClass().equals(HashSet.class)) {
                for (Object object : ((HashSet) value).toArray()) {
                    appendParamToUrlQuery(query, key, object.toString(), charset, encode);
                }
            } else {
                appendParamToUrlQuery(query, key, value.toString(), charset, encode);
            }
        }
        if (query.length() > 0) {
            query = query.deleteCharAt(query.length() - 1);
        }
        return query.toString();
    }

    public static List<JSONObject> parseJson(HttpServletRequest request, String name) {
        List<JSONObject> items = new ArrayList();
        JSONArray array = JSONArray.parseArray(request.getParameter(name));
        int i = 0;
        for (int j = array.size(); i < j; i++) {
            items.add(array.getJSONObject(i));
        }
        return items;
    }


    public static JSONObject parseJSONObject(HttpServletRequest request, String name) {
        return JSONObject.parseObject(request.getParameter(name));
    }


    public static <T> List<T> parseJson(HttpServletRequest request, String name, Class<T> clazz) {
        List<T> items = new ArrayList();
        JSONArray array = JSONArray.parseArray(request.getParameter(name));
        int i = 0;
        for (int j = array.size(); i < j; i++) {
            items.add(array.getObject(i, clazz));
        }
        return items;
    }

}
