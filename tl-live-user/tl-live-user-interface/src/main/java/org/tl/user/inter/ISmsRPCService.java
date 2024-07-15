package org.tl.user.inter;

import org.tl.user.DTO.CodeCheckDTO;

public interface ISmsRPCService {
    boolean sendLoginSms(String phone);

    CodeCheckDTO checkCode(String phone, int code);


}
