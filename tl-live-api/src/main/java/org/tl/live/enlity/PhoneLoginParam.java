package org.tl.live.enlity;

import java.io.Serializable;

public class PhoneLoginParam implements Serializable {
    private String phone;
    private int code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PhoneLoginParam() {
    }

    public PhoneLoginParam(String phone, int code) {
        this.phone = phone;
        this.code = code;
    }
}
