package org.tl.live.commonStatusEunm;

public enum commonStatusEnum {
    INVALID_PARAM(0, "无效"),
    VALID_USER(1, "有效");
    private int code;
    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    commonStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
