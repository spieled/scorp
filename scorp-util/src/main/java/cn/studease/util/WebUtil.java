package cn.studease.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static void json(HttpServletResponse response, boolean success, String msg) {
        Map<String, Object> model = new HashMap();
        model.put("success", Boolean.valueOf(success));
        if (StringUtil.hasText(msg)) {
            model.put("msg", msg);
        }
        try {
            addCacheControlHeaders(response, true);
            response.getWriter().print(JSON.toJSONString(model));
        } catch (Exception e) {
            log.trace("输出JSON对象失败", e);
        }
    }

    public static void json(HttpServletResponse response, boolean success) {
        json(response, success, null);
    }


    public static void json(HttpServletResponse response, String msg) {
        json(response, !StringUtil.hasText(msg), msg);
    }


    public static void json(HttpServletResponse response, Object object) {
        try {
            addCacheControlHeaders(response, true);
            response.getWriter().print(JSON.toJSONString(object));
        } catch (IOException ignored) {
            log.trace("输出JSON对象失败", ignored);
        }
    }


    public static <T extends Object> void json(HttpServletResponse response, T pojo, String[] fields) {
        try {
            addCacheControlHeaders(response, true);
            response.getWriter().print(JSON.toJSONString(pojo, new com.alibaba.fastjson.serializer.SimplePropertyPreFilter(pojo.getClass(), fields), new com.alibaba.fastjson.serializer.SerializerFeature[0]));
        } catch (IOException ignored) {
            log.trace("输出JSON对象失败", ignored);
        }
    }


    public static void json(HttpServletResponse response, long totalCount, Collection<?> contents) {
        Map<String, Object> model = new HashMap();
        model.put("success", Boolean.valueOf(true));
        model.put("total", Long.valueOf(totalCount));
        model.put("list", contents);
        try {
            addCacheControlHeaders(response, true);
            response.getWriter().print(JSON.toJSONString(model));
        } catch (Exception ignored) {
            log.trace("输出JSON对象失败", ignored);
        }
    }

    public static HttpServletResponse addCacheControlHeaders(HttpServletResponse response, boolean responseJson) {
        response.setContentType(responseJson ? "application/json" : "text/html");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragrma", "no-cache");
        response.setDateHeader("Expires", 0L);
        return response;
    }

    public static Cookie addCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        if (!StringUtil.hasText(name)) {
            return null;
        }
        try {
            String path = getHttpServletRequest().getContextPath();
            if (!StringUtil.hasText(path)) {
                path = "/";
            }
            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAgeSeconds);
            cookie.setPath(path);
            response.addCookie(cookie);
            return cookie;
        } catch (Exception e) {
            log.trace("添加COOKIE失败", e);
        }
        return null;
    }


    public static Cookie getCookie(String name) {
        Cookie[] cookies = getHttpServletRequest().getCookies();
        if ((cookies != null) && (cookies.length > 0)) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }


    public static void deleteCookie(HttpServletResponse response, String name) {
        if (!StringUtil.hasText(name)) {
            return;
        }
        Cookie[] cookies = getHttpServletRequest().getCookies();
        if ((cookies != null) && (cookies.length > 0)) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return (T) getWebApplicationContext().getBean(clazz);
        } catch (Exception e) {
            log.trace("获取BEAN失败", e);
        }
        return null;
    }


    public static Object getBean(String name) {
        try {
            return getWebApplicationContext().getBean(name);
        } catch (Exception e) {
            log.trace("获取BEAN失败", e);
        }
        return null;
    }


    public static <T> T getBean(Class<T> clazz, String name) {
        try {
            return (T) getWebApplicationContext().getBean(name, clazz);
        } catch (Exception e) {
            log.trace("获取BEAN失败", e);
        }
        return null;
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return getWebApplicationContext().getBeansWithAnnotation(annotationType);
    }

    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("x-real-forwarded-for");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("x-forwarded-for");
            if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
                ip = request.getRemoteAddr();
            }
        }
        if ((ip != null) && (ip.length() > 15) && (ip.indexOf(",") > 0)) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }
}
