package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.LoginDTO;
import org.tl.user.inter.IUserPhoneLoginRPCService;
import org.tl.user.provider.service.LoginService;

@DubboService
public class UserPhoneLoginRPCService implements IUserPhoneLoginRPCService {
    @Resource
    private LoginService loginService;
    @Override
    public LoginDTO loginByPhone(String phone) {
        return loginService.loginByPhone(phone);
    }
}
