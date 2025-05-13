package org.tl.user.inter;

import org.tl.user.DTO.UserDTO;
import org.tl.user.DTO.UserProfileDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IUserRPCService {
    UserDTO getUserById(Long userId);

    String createToken(Long userId);

    String checkToken(String titk);

    boolean recharge(Long userId, BigDecimal addMoney);

    UserProfileDTO getUserProfile(Long userId);

    boolean follow(Long userId, Long followUserId);

    boolean unfollow(Long userId, Long followUserId);

    List<UserDTO> getFollowList(Long userId);

    List<UserDTO> getFollowerList(Long userId);

    List<UserDTO> getMutualFollowerList(Long userId);
}
