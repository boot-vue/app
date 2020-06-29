package com.bootvue.utils.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private static final PoolingHttpClientConnectionManager clientConnectionManager;
    private static final RequestConfig requestConfig;

    static {
        //pool
        clientConnectionManager = new PoolingHttpClientConnectionManager();
        // 最大1000
        clientConnectionManager.setMaxTotal(1000);
        // 单个路由最大100
        clientConnectionManager.setDefaultMaxPerRoute(100);

        //config
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
    }

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(clientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * @param url     url
     * @param params  请求参数(可以为空)
     * @param headers 请求头(可以为空)
     * @return CloseableHttpResponse
     * @throws URISyntaxException
     * @throws IOException
     */
    public static CloseableHttpResponse get(String url, Map<String, String> params, Map<String, String> headers) throws URISyntaxException, IOException {
        URIBuilder uri = new URIBuilder(url);
        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> pairs = new ArrayList<>();
            params.forEach((key, value) -> pairs.add(new BasicHeader(key, value)));
            uri.setParameters(pairs);  //请求参数
        }

        //请求头
        HttpGet httpGet = new HttpGet(uri.build());
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(httpGet::addHeader);
        }

        //请求
        return getHttpClient().execute(httpGet);
    }

    /**
     * @param url
     * @return CloseableHttpResponse
     * @throws IOException
     * @throws URISyntaxException
     */
    public static CloseableHttpResponse get(String url) throws IOException, URISyntaxException {
        return get(url, null, null);
    }

    /**
     * @param url
     * @param params  参数 ,可以为空
     * @param headers 请求头, 可以为空
     * @param isJson  是否发送application/json请求
     * @return CloseableHttpResponse
     */
    public static CloseableHttpResponse post(String url, Map<String, String> params, Map<String, String> headers, boolean isJson) throws IOException {
        HttpPost httpPost = new HttpPost();
        //参数
        if (!CollectionUtils.isEmpty(params)) {
            if (isJson) {
                //application/json
                httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));
            } else {
                //application/x-www-form-urlencoded
                List<NameValuePair> pairs = new ArrayList<>();
                params.forEach((key, value) -> pairs.add(new BasicHeader(key, value)));
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));
            }
        }
        //headers
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(httpPost::addHeader);
        }

        //请求
        return getHttpClient().execute(httpPost);
    }

    /**
     * 无参数post请求
     *
     * @param url url
     * @return CloseableHttpResponse
     * @throws IOException
     */
    public static CloseableHttpResponse post(String url) throws IOException {
        return post(url, null, null, false);
    }
}
