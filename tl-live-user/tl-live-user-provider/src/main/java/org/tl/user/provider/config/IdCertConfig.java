package org.tl.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.idcert")
public class IdCertConfig {
    private String host;
    private String path;
    private String appcode;
}