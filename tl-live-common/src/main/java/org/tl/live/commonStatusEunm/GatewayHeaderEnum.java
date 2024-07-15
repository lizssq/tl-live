package org.tl.live.commonStatusEunm;

public enum GatewayHeaderEnum {
    GATEWAY_UESR_NAME("userLogin","用户登录ID");
    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    GatewayHeaderEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
