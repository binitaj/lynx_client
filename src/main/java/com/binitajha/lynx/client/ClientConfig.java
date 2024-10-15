package com.binitajha.lynx.client;


import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate sslRestTemplate(RestTemplateBuilder builder, SslBundles sslBundles) {
        SslBundle sslBundle = sslBundles.getBundle("client");
        return builder.rootUri("LynxClient").setSslBundle(sslBundle).build();
    }

}
