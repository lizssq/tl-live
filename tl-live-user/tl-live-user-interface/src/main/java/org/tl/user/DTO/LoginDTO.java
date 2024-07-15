package org.tl.user.DTO;

import java.io.Serializable;

public class LoginDTO implements Serializable {
    private boolean success;
    private String decs;

    private Long userId;

    private String nickName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public LoginDTO() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public LoginDTO(boolean success, String decs) {
        this.success = success;
        this.decs = decs;
    }

    public LoginDTO(boolean success, String decs, Long userId, String nickName) {
        this.success = success;
        this.decs = decs;
        this.userId = userId;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "success=" + success +
                ", decs='" + decs + '\'' +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                '}';
    }
    public static LoginDTO success(Long userId) {
        return new LoginDTO(true, "登录成功", userId, null);
    }
}
