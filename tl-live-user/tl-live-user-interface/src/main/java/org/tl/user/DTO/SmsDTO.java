package org.tl.user.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class SmsDTO implements java.io.Serializable {
    private Long id;
    private Integer code;
    private String phone;
    private Date sendTime;
    private Date updateTime;
}