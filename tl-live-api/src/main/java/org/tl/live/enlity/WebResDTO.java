package org.tl.live.enlity;

import java.io.Serializable;

public class WebResDTO implements Serializable {
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;
    private int code;
    private Object data;

    public WebResDTO() {
    }

    public WebResDTO(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        return "WebResDTO{code=" + this.code  + ", data=" + this.data + '}';
    }
}
