package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.CodeCheckDTO;
import org.tl.user.inter.ISmsRPCService;
import org.tl.user.provider.service.SmsService;

@DubboService
public class SmsRPCService implements ISmsRPCService {
    @Resource
    SmsService smsService;
    @Override
    public boolean sendLoginSms(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public CodeCheckDTO checkCode(String phone, int code) {
        return smsService.checkCode(phone, code);
    }
}
