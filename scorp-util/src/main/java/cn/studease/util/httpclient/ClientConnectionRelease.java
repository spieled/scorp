package cn.studease.util.httpclient;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Author: liushaoping
 * Date: 2015/8/28.
 */
public class ClientConnectionRelease {

    public final static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
//            HttpGet httpget = new HttpGet("http://localhost/");
            HttpGet httpget = new HttpGet("http://117.40.196.157:8080/agent/performance");

            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                // If the response does not enclose an entity, there is no need
                // to bother about connection release
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    try {
                        instream.read();
                        // do something useful with the response
                    } catch (IOException ex) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw ex;
                    } finally {
                        // Closing the input stream will trigger connection release
                        instream.close();
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }


}
