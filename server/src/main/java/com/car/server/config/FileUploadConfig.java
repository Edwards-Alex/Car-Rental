package com.car.server.config;

import jakarta.servlet.MultipartConfigElement;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class FileUploadConfig {
    // 1. 调整Tomcat底层参数（关键解决文件数量限制）
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                // 设置最大参数数量（文件+普通参数总和）
                connector.setProperty("maxParameterCount", "1000");

                // 设置请求体最大大小（必须 >= multipart.max-request-size）
                connector.setProperty("maxPostSize", "52428800"); // 50MB

                // 设置最大吞食大小（处理失败时Tomcat能吞食的最大数据量）
                connector.setProperty("maxSwallowSize", "52428800"); // 50MB
            }
        });
    }

    // 2. 必须配置：提供multipart配置
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));  // 单个文件最大10MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(50)); // 总请求最大50MB
        factory.setLocation("/tmp"); // 临时目录（确保有写入权限）
        return factory.createMultipartConfig();
    }

    // 3. 必须配置：提供multipart解析器
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
