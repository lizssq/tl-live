package org.tl.user.provider.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
@TableName("t_sms")
public class SmsDO {

    @TableId(type = IdType.INPUT)
    private Long id;
    private Integer code;
    private String phone;
    private Date sendTime;
    private Date updateTime;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public SmsDO() {
    }

    public SmsDO(Long id, Integer code, String phone, Date sendTime, Date updateTime) {
        this.id = id;
        this.code = code;
        this.phone = phone;
        this.sendTime = sendTime;
        this.updateTime = updateTime;
    }
}
