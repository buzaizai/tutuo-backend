package com.wjk.tutuo1.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "openai")
public class OpenaiProperties {
    private String apikey;
    private String chatEndpoint;
}
