package org.tl.live.Controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.UserDTO;
import org.slf4j.Logger;
import org.tl.user.DTO.UserProfileDTO;
import org.tl.user.inter.IUserRPCService;

import java.math.BigDecimal;
import java.util.List;

import static org.tl.live.enlity.WebResDTO.ERROR_CODE;
import static org.tl.live.enlity.WebResDTO.SUCCESS_CODE;


@RestController
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @DubboReference(check = false)
    private IUserRPCService userRPCService;

    @RequestMapping("/getUser")
    public WebResDTO getUserById(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        logger.info("userId:{}，nikeName:{}", userId, userRPCService.getUserById(Long.valueOf(userId)).getNickName());
        return new WebResDTO(SUCCESS_CODE, userRPCService.getUserById(Long.valueOf(userId)));
    }
    @PutMapping("/recharge")
    public WebResDTO recharge(@RequestParam("userId") Long userId, @RequestParam("addMoney") BigDecimal addMoney) {
        if (userId == null || addMoney == null) {
            return new WebResDTO(ERROR_CODE, "userId或addMoney不能为空");
        }
        boolean result = userRPCService.recharge(userId, addMoney);
        if (result) {
            return new WebResDTO(SUCCESS_CODE, "充值成功");
        } else {
            return new WebResDTO(ERROR_CODE, "充值失败");
        }
    }
    @GetMapping("/getUserProfile")
    public WebResDTO getUserProfile(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        UserProfileDTO userProfile = userRPCService.getUserProfile(userId);
        if (userProfile != null) {
            return new WebResDTO(SUCCESS_CODE, userProfile);
        } else {
            return new WebResDTO(ERROR_CODE, "用户不存在");
        }
    }
    //用户关注
    @PostMapping("/follow")
    public WebResDTO follow(@RequestParam("userId") Long userId, @RequestParam("followUserId") Long followUserId) {
        if (userId == null || followUserId == null) {
            return new WebResDTO(ERROR_CODE, "userId或followUserId不能为空");
        }
        boolean result = userRPCService.follow(userId, followUserId);
        if (result) {
            return new WebResDTO(SUCCESS_CODE, "关注成功");
        } else {
            return new WebResDTO(ERROR_CODE, "关注失败");
        }
    }

    //用户取消关注
    @PostMapping("/unfollow")
    public WebResDTO unfollow(@RequestParam("userId") Long userId, @RequestParam("followUserId") Long followUserId) {
        if (userId == null || followUserId == null) {
            return new WebResDTO(ERROR_CODE, "userId或followUserId不能为空");
        }
        boolean result = userRPCService.unfollow(userId, followUserId);
        if (result) {
            return new WebResDTO(SUCCESS_CODE, "取消关注成功");
        } else {
            return new WebResDTO(ERROR_CODE, "取消关注失败");
        }
    }
    //获取用户关注列表
    @GetMapping("/getFollowList")
    public WebResDTO getFollowList(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        List<UserDTO> followList = userRPCService.getFollowList(userId);
        if (followList != null && !followList.isEmpty()) {
            return new WebResDTO(SUCCESS_CODE, followList);
        } else {
            return new WebResDTO(ERROR_CODE, "没有关注的用户");
        }
    }
    //获取用户粉丝列表
    @GetMapping("/getFollowerList")
    public WebResDTO getFollowerList(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        List<UserDTO> followerList = userRPCService.getFollowerList(userId);
        if (followerList != null && !followerList.isEmpty()) {
            return new WebResDTO(SUCCESS_CODE, followerList);
        } else {
            return new WebResDTO(ERROR_CODE, "没有粉丝");
        }
    }

    //互相关注列表
    @GetMapping("/mutualFollow")
    public WebResDTO mutualFollow(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        List<UserDTO> isFollowList = userRPCService.getMutualFollowerList(userId);
        if (isFollowList != null && !isFollowList.isEmpty()) {
            return new WebResDTO(SUCCESS_CODE, isFollowList);
        } else {
            return new WebResDTO(ERROR_CODE, "没有互相关注的用户");
        }
    }
}
