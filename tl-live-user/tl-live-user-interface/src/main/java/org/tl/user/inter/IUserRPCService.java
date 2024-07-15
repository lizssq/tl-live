package org.tl.user.inter;

import org.tl.user.DTO.UserDTO;

public interface IUserRPCService {
    UserDTO getUserById(Long userId);

    String createToken(Long userId);

    String checkToken(String titk);
}
