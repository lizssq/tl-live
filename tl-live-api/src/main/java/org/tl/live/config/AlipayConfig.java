package org.tl.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {
    private String protocol;
    private String gatewayHost;
    private String signType;
    private String appId;
    private String notifyUrl;
    private String merchantPrivateKey;
    private String alipayPublicKey;
}