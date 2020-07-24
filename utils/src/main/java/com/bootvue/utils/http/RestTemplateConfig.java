package com.bootvue.utils.http;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        //需要其它额外的功能可以再这里配置

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(10L))
                .connectTimeout(Duration.ofSeconds(10L)).readTimeout(Duration.ofSeconds(10L))
                .writeTimeout(Duration.ofSeconds(10L));

        OkHttpClient okHttpClient = new OkHttpClient(builder);
        ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return new RestTemplate(factory);
    }
}
