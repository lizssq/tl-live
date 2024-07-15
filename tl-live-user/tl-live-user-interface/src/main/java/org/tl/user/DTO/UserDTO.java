package org.tl.user.DTO;

import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable {
    private Long userId;
    private String nickName;
    private String avatar;
    private String trueName;
    private Integer sex;
    private Date createTime;
    private Date updateTime;

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

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "UserId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", trueName='" + trueName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +'\'' +
                ", updateTime=" + updateTime;
    }

    public UserDTO() {
    }
}
