package cn.studease.util;

import java.io.*;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.CharSet;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultManagedHttpClientConnection;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装org.apache.httpcomponents.httpclient的网络请求工具类。提供高可用、可靠、好用的网络工具方法。
 * Author: liushaoping
 * Date: 2015/8/28.
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String HEADER_DATE = "Date";
    private static final String HEADER_CHARSET = "Accept-Charset";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String JSON_UTF8 = "application/json; charset=utf-8";

    private static final String UTF_8 = "UTF-8";
    private static PoolingHttpClientConnectionManager cm = null;
    private static HttpClient client = null;

    static {
        cm = new PoolingHttpClientConnectionManager();

        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .build();

        cm.setDefaultConnectionConfig(connectionConfig);
        cm.setDefaultMaxPerRoute(20);
        cm.setMaxTotal(200);
        client = HttpClients.custom().setConnectionManager(cm).build();

    }

    public static ByteArrayEntity byteArrayEntity(String content) {
        try {
            return new ByteArrayEntity(content.getBytes(UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url, Map<String, String> headers, HttpEntity httpEntity, int socketTimeout, int connectTimeout, int connectRequestTimeout, boolean compression) {
        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectRequestTimeout)
                .setContentCompressionEnabled(compression)
                .build();

        final HttpEntity entity;
        if (compression && httpEntity != null) {
            entity = new GzipCompressingEntity(httpEntity);
        } else {
            entity = httpEntity;
        }

        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);
        post.setHeader(HEADER_CHARSET, UTF_8);
        post.setHeader(HEADER_CONTENT_TYPE, JSON_UTF8);
        post.setHeader(HEADER_DATE, String.valueOf(System.currentTimeMillis()));
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        post.setEntity(entity);

        ResponseHandler<String> defaultStringResponseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException(String.format("Unexpected response status: %s ; content: %s", status, EntityUtils.toString(entity)));
                }
            }
        };

        String responseText = null;
        try {
            responseText = client.execute(post, defaultStringResponseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return responseText;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/receive";

        String resultContent = post(url, null, byteArrayEntity("毛主席万岁"), 5000, 5000, 5000, true);
        logger.info("result content is : " + resultContent);
    }

    public HttpEntity formEntity(Map<String, String> params) {
        List<NameValuePair> nvps = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            return new UrlEncodedFormEntity(nvps, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
