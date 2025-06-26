package org.tl.live.Controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.*;
import org.slf4j.Logger;
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
    //修改用户信息
    @PostMapping("/updateUserProfile")
    public WebResDTO updateUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        if (userProfileDTO == null || userProfileDTO.getUserId() == null) {
            return new WebResDTO(ERROR_CODE, "用户信息不能为空");
        }
        boolean result = userRPCService.updateUserProfile(userProfileDTO);
        if (result) {
            return new WebResDTO(SUCCESS_CODE, "修改成功");
        } else {
            return new WebResDTO(ERROR_CODE, "修改失败");
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
        //判断是否有该用户
        // todo
        boolean result = userRPCService.unfollow(userId, followUserId);
        if (result) {
            return new WebResDTO(SUCCESS_CODE, "取消关注成功");
        } else {
            return new WebResDTO(ERROR_CODE, "取消关注失败");
        }
    }
    @GetMapping("/getFollowList")
    public WebResDTO getFollowList(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize) {

        // 参数校验
        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        if (pageNum <= 0 || pageSize <= 0) {
            return new WebResDTO(ERROR_CODE, "分页参数必须大于0");
        }


        PageResult<UserProfileDTO> followList = userRPCService.getFollowList(userId, pageNum, pageSize);

        if (followList != null ) {
            return new WebResDTO(SUCCESS_CODE, followList);
        } else {
            return new WebResDTO(ERROR_CODE, "没有关注的用户");
        }
    }

    @GetMapping("/getFollowerList")
    public WebResDTO getFollowerList(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize) {

        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        if (pageNum <= 0 || pageSize <= 0) {
            return new WebResDTO(ERROR_CODE, "分页参数必须大于0");
        }

        PageResult<UserProfileDTO> followerList = userRPCService.getFollowerList(userId, pageNum, pageSize);

        if (followerList != null ) {
            return new WebResDTO(SUCCESS_CODE, followerList);
        } else {
            return new WebResDTO(ERROR_CODE, "没有粉丝");
        }
    }

    @GetMapping("/mutualFollow")
    public WebResDTO mutualFollow(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize) {

        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        if (pageNum <= 0 || pageSize <= 0) {
            return new WebResDTO(ERROR_CODE, "分页参数必须大于0");
        }

        PageResult<UserProfileDTO> isFollowList = userRPCService.getMutualFollowerList(userId, pageNum, pageSize);

        if (isFollowList != null) {
            return new WebResDTO(SUCCESS_CODE, isFollowList);
        } else {
            return new WebResDTO(ERROR_CODE, "没有互相关注的用户");
        }
    }
    @GetMapping("/conversations")
    public WebResDTO getConversations(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        List<ConversationsDTO> conversations = userRPCService.getConversations(userId);
        if (conversations != null && !conversations.isEmpty()) {
            return new WebResDTO(SUCCESS_CODE, conversations);
        } else {
            return new WebResDTO(ERROR_CODE, "没有会话记录");
        }
    }
    @GetMapping("/historyMessages")
    public WebResDTO getHistoryMessages(@RequestParam("userId") Long userId, @RequestParam("conversationId") Long conversationId) {
        if (userId == null || conversationId == null) {
            return new WebResDTO(ERROR_CODE, "userId或conversationId不能为空");
        }
        List<MessagesDTO> historyMessages = userRPCService.getHistoryMessages(userId, conversationId);
        if (historyMessages != null && !historyMessages.isEmpty()) {
            return new WebResDTO(SUCCESS_CODE, historyMessages);
        } else {
            return new WebResDTO(ERROR_CODE, "没有历史消息");
        }
    }

    @GetMapping("/unread")
    public WebResDTO getUnreadCounts(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return new WebResDTO(ERROR_CODE, "userId不能为空");
        }
        UnreadCountDTO unreadCounts = userRPCService.getUnreadCounts(userId);
        if (unreadCounts != null ) {
            return new WebResDTO(SUCCESS_CODE, unreadCounts);
        } else {
            return new WebResDTO(ERROR_CODE, "没有未读消息");
        }
    }

    //用户实名认证
    @PostMapping("/realNameAuthentication")
    public WebResDTO realNameAuthentication(String idCard, String name, Long userId) {
        if (StringUtils.isEmpty(idCard) || StringUtils.isEmpty(name) || userId == -1) {
            return new WebResDTO(ERROR_CODE, "身份证号、姓名或userId不能为空");
        }
        int result = userRPCService.realNameAuthentication(idCard, name, userId);
        if (result==200) {
            return new WebResDTO(SUCCESS_CODE, "实名认证成功");
        } else {
            return new WebResDTO(ERROR_CODE, "实名认证失败");
        }
    }

    //动态模糊查询，可根据手机号、昵称、ID查询
    @GetMapping("/search")
    public WebResDTO search(@RequestParam("keyword") String keyword,
                            @RequestParam(value = "userId", defaultValue = "-1") Long userId,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            return new WebResDTO(ERROR_CODE, "搜索关键字不能为空");
        }
        PageResult<UserProfileDTO> searchResults = userRPCService.search(keyword, userId, pageNum, pageSize);
        if (searchResults != null) {
            return new WebResDTO(SUCCESS_CODE, searchResults);
        } else {
            return new WebResDTO(ERROR_CODE, "没有搜索结果");
        }
    }

    //通过房间id查找主播信息
    @GetMapping("/roomId/{roomId}")
    public WebResDTO getUserByRoomId(@PathVariable Long roomId){
        UserProfileDTO liveRoom = userRPCService.getUserByRoomId(roomId);
        if(liveRoom!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRoom);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"没有直播间");
    }

    @PostMapping("/isFollow")
    public WebResDTO isFollow(@RequestParam("userId") Long userId, @RequestParam("followUserId") Long followUserId) {
        if (userId == null || followUserId == null) {
            return new WebResDTO(ERROR_CODE, "userId或followUserId不能为空");
        }
        boolean result = userRPCService.isFollow(userId, followUserId);
        if (result) {
            return new WebResDTO(SUCCESS_CODE, "已关注");
        } else {
            return new WebResDTO(ERROR_CODE, "未关注");
        }
    }
}
