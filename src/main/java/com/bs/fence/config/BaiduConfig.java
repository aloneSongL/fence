package com.bs.fence.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:baidu.properties")
@ConfigurationProperties(prefix = "baidu")
@Data
public class BaiduConfig {

    private String url;
    private String ak;
    private String serverId;

}