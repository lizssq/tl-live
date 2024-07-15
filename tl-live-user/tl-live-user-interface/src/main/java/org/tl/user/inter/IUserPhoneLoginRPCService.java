package org.tl.user.inter;

import org.tl.user.DTO.LoginDTO;

public interface IUserPhoneLoginRPCService {
    LoginDTO loginByPhone(String phone);
}
