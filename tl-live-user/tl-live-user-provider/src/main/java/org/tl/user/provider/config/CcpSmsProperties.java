package org.tl.user.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "tllive.sms.ccp")
public class CcpSmsProperties {

    private String smsServiceIP;
    private int port;
    private String accountSid;
    private String accountToken;
    private String appId;
    private int templateId;
    private String testPhone;

    // Getters and Setters

    public String getSmsServiceIP() {
        return smsServiceIP;
    }

    public void setSmsServiceIP(String smsServiceIP) {
        this.smsServiceIP = smsServiceIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTestPhone() {
        return testPhone;
    }

    public void setTestPhone(String testPhone) {
        this.testPhone = testPhone;
    }
}
