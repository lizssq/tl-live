package org.tl.user.provider.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tl.common.redis.builder.UserCacheKeyBuilder;
import org.tl.live.commonStatusEunm.commonStatusEnum;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.live.util.ConvertBeanUtil;
import org.tl.user.DTO.*;
import org.tl.user.provider.entity.*;
import org.tl.user.provider.mapper.*;
import org.tl.user.provider.util.UserRedisKeyBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    UserRedisKeyBuilder userRedisKeyBuilder;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    UserPhoneMapper userPhoneMapper;
    @Resource
    UserCacheKeyBuilder userCacheKeyBuilder;
    @Resource
    private UserFollowMapper userFollowMapper;
    @Resource
    private ConversationsMapper conversationsMapper;
    @Resource
    private ConversationStatusMapper conversationStatusMapper;
    @Resource
    private MessagesMapper messagesMapper;
    @Resource
    private ConversationStatusMapper statusMapper;
    @Resource
    private LiveRoomMapper liveRoomMapper;
    @DubboReference
    private IGenerateIDRPCService generateIDRPCService;


    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private RealNameAuthService realNameAuthService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    ConvertBeanUtil convertBeanUtil = new ConvertBeanUtil();

    public UserDTO getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        String userRedisKey = userRedisKeyBuilder.getUserInfoKey(userId);
        Object userObj = redisTemplate.opsForValue().get(userRedisKey);

        if (userObj == null) {
            // 处理缓存未命中的情况
            // 可能需要从数据库加载数据并存储到Redis
            UserDO userDO = userMapper.selectById(userId);
            if (userDO != null) {
                UserDTO userDTO = convertBeanUtil.convert(userDO, UserDTO.class);
                redisTemplate.opsForValue().set(userRedisKeyBuilder.getUserInfoKey(userId), userDTO, 30, TimeUnit.MINUTES);
                return userDTO;
            } else {
                UserDTO noUserDTO = new UserDTO();
                noUserDTO.setUserId(-1L);
                redisTemplate.opsForValue().set(userRedisKeyBuilder.getUserInfoKey(userId), noUserDTO, 30, TimeUnit.MINUTES);
                return null;
            }
        } else {
            UserDTO userDTO = (UserDTO) userObj;
            // 使用userDTO进行后续操作
            //userDTO= (UserDTO) redisTemplate.opsForValue().get(userRedisKeyBuilder.getUserInfoKey(userId));
            if (userDTO != null && userDTO.getUserId() > 0) {
                return userDTO;
            } else if (userDTO != null && userDTO.getUserId() < 0) {
                return null;
            }
        }
        return null;
    }

    public LoginDTO registerByPhone(String phone) {
        UserDO userDO = new UserDO();
        //TODO 分配主键

        Long userId = generateIDRPCService.getSequentialID();
        userDO.setUserId(userId);
        userDO.setNickName("用户_" + userId);
        userDO.setAvatar("/images/user/9d3d4c70-de0b-46fa-8f11-70067fd72d72.png");
        userMapper.insert(userDO);

        UserPhoneDO userPhoneDO = new UserPhoneDO();
        userPhoneDO.setPhone(phone);
        userPhoneDO.setUserId(userId);
        userPhoneDO.setStatus(commonStatusEnum.VALID_USER.getCode());
        userPhoneMapper.insert(userPhoneDO);
        //用户个人信息
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(userId);
        userProfile.setGender(3);
        userProfile.setAvatar("/images/user/9d3d4c70-de0b-46fa-8f11-70067fd72d72.png");
        userProfileMapper.insertSelective(userProfile);
        logger.info("UserService:用户注册成功,phone:{}", phone);
        //系统默认消息
        Conversations conversations = new Conversations();
        conversations.setConversationType(1);
        conversations.setUser1Id(Math.toIntExact(userId));
        conversations.setPreview("欢迎使用本直播平台！");
        conversations.setReceiverId(userId.intValue());
        conversationsMapper.insertSelective(conversations);
        Conversations conversations1 = conversationsMapper.selectByUserIdAndType(conversations.getUser1Id(), conversations.getConversationType());
        logger.info("UserService:用户注册成功,创建会话,会话ID:{}", conversations1.getId());
        //创建会话状态
        ConversationStatus conversationStatus = new ConversationStatus();
        conversationStatus.setConversationId(conversations1.getId());
        conversationStatus.setUserId(Math.toIntExact(userId));
        conversationStatus.setUnreadCount(1);
        conversationStatusMapper.insertSelective(conversationStatus);
        logger.info("UserService:用户注册成功,创建会话状态,会话ID:{}", conversations1.getId());
        //添加聊天信息
        Messages messages = new Messages();
        messages.setConversationId(conversations1.getId());
        messages.setSenderId(-1);
        messages.setContent("欢迎使用本直播平台！");
        messagesMapper.insertSelective(messages);
        logger.info("UserService:用户注册成功,添加欢迎消息,欢迎消息:{}", messages);
        return LoginDTO.success(userId);
    }

    public String createToken(Long userId) {

        //生成token，随机数
        String token = UUID.randomUUID().toString();

        String userCacheKey = userCacheKeyBuilder.getUserPhoneKey(token);
        //存入redis
        redisTemplate.opsForValue().set(userCacheKey, userId, 30, TimeUnit.MINUTES);

        return token;
    }

    public String checkToken(String titk) {
        String userCacheKey = userCacheKeyBuilder.getUserPhoneKey(titk);
        Object userId = redisTemplate.opsForValue().get(userCacheKey);
        if (userId != null) {
            return userId.toString();
        }
        return null;
    }

    //转账
    @Transactional
    public boolean transfer(Long fromId, Long toId, BigDecimal totalCost) {
        // 1. 获取用户信息
        UserProfile fromUserProfile = userProfileMapper.selectByUserId(fromId);
        UserProfile toUserProfile = userProfileMapper.selectByUserId(toId);
        if (fromUserProfile == null || toUserProfile == null) {
            logger.info("UserService:用户不存在:" + fromId + "或" + toId);
            return false; // 用户不存在
        }

        // 2. 检查余额是否足够
        if (fromUserProfile.getBalance().compareTo(totalCost) < 0) {
            logger.info("UserService:用户余额不足:" + fromId + "余额:" + fromUserProfile.getBalance() + "转账金额:" + totalCost);
            return false; // 余额不足
        }

        // 3. 扣除余额
        fromUserProfile.setBalance(fromUserProfile.getBalance().subtract(totalCost));
        userProfileMapper.updateByPrimaryKeySelective(fromUserProfile);
        // 4. 增加余额
        toUserProfile.setBalance(toUserProfile.getBalance().add(totalCost));
        userProfileMapper.updateByPrimaryKeySelective(toUserProfile);

        return true;
    }

    //充值
    @Transactional
    public boolean recharge(Long userId, BigDecimal addMoney) {
        // 1. 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            logger.info("用户不存在");
            return false; // 用户不存在
        }

        // 2. 增加余额
        userProfile.setBalance(userProfile.getBalance().add(addMoney));
        userProfileMapper.updateByPrimaryKeySelective(userProfile);

        return true;
    }

    //获取用户个人信息
    public UserProfileDTO getUserProfile(Long userId) {
        // 1. 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return null; // 用户不存在
        }

        // 2. 转换为 DTO
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(userProfile, userProfileDTO);  // 自动拷贝属性

        return userProfileDTO;
    }

    //扣除余额
    @Transactional
    public boolean deductBalance(Long userId, BigDecimal amount) {
        // 1. 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return false; // 用户不存在
        }

        // 2. 检查余额是否足够
        if (userProfile.getBalance().compareTo(amount) < 0) {
            return false; // 余额不足
        }

        // 3. 扣除余额
        userProfile.setBalance(userProfile.getBalance().subtract(amount));
        userProfileMapper.updateByPrimaryKeySelective(userProfile);

        return true;
    }

    public boolean follow(Long userId, Long followUserId) {
        // 1. 检查用户是否已关注
        boolean existingFollow = isFollow(userId, followUserId);
        if (existingFollow) {
            logger.info("UserService:用户已关注:" + userId + "已关注" + followUserId);
            return false; // 已关注，无需重复关注
        }
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(Math.toIntExact(userId));
        userFollow.setTargetId(Math.toIntExact(followUserId));
        logger.info("UserService:用户关注:" + userId + "关注" + followUserId);
        userFollowMapper.insertSelective(userFollow);

        // 2. 是否互相关注
        boolean mutualFollow = isMutualFollower(userId, Math.toIntExact(followUserId));
        if (mutualFollow) {
            // 互相关注，判断是否有会话
            Conversations conversations = conversationsMapper.findPrivateConversation(Math.toIntExact(userId), Math.toIntExact(followUserId));
            if (conversations == null) {
                logger.info("UserService:用户互相关注,创建会话:" + userId + "和" + followUserId);
                // 创建会话
                conversations = new Conversations();
                conversations.setConversationType(0);
                conversations.setUser1Id(Math.toIntExact(userId));
                conversations.setUser2Id(Math.toIntExact(followUserId));
                conversations.setPreview("泥嚎");
                conversationsMapper.insertSelective(conversations);
                // 创建会话状态
                ConversationStatus conversationStatus = new ConversationStatus();
                conversationStatus.setConversationId(conversations.getId());
                conversationStatus.setUnreadCount(0);
                conversationStatus.setUserId(Math.toIntExact(userId));
                conversationStatusMapper.insertSelective(conversationStatus);
            }
            logger.info("UserService:用户互相关注,会话已存在:" + userId + "和" + followUserId);
        }

        return true;
    }

    public boolean unfollow(Long userId, Long followUserId) {
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(Math.toIntExact(userId));
        userFollow.setTargetId(Math.toIntExact(followUserId));
        // 这里假设 deleteByUserIdAndTargetId 是删除关注关系的方法

        return userFollowMapper.deleteByUserIdAndTargetId(userFollow) > 0;
    }

    public PageResult<UserProfileDTO> getFollowList(Long userId, int pageNum, int pageSize) {
        // 1. 获取用户的关注列表
        PageHelper.startPage(pageNum, pageSize);
        List<UserProfile> userProfileList = userFollowMapper.selectByUserId(Math.toIntExact(userId));
        if (userProfileList == null || userProfileList.isEmpty()) {
            return null; // 没有关注列表
        }
        Page<UserProfile> pageInfo = (Page<UserProfile>) userProfileList;
        // 转换DTO列表
        List<UserProfileDTO> dtos = userProfileList.stream()
                .map(profile -> {
                    UserProfileDTO dto = new UserProfileDTO();
                    BeanUtils.copyProperties(profile, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 构建分页结果
        PageResult<UserProfileDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
    }

    public PageResult<UserProfileDTO> getFollowerList(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 1. 获取用户的粉丝列表
        List<UserProfile> userProfileList = userFollowMapper.selectByTargetId(Math.toIntExact(userId));
        if (userProfileList == null || userProfileList.isEmpty()) {
            return null; // 没有粉丝列表
        }
        Page<UserProfile> pageInfo = (Page<UserProfile>) userProfileList;
        // 转换DTO列表
        List<UserProfileDTO> dtos = userProfileList.stream()
                .map(profile -> {
                    UserProfileDTO dto = new UserProfileDTO();
                    BeanUtils.copyProperties(profile, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 构建分页结果
        PageResult<UserProfileDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
    }

    public PageResult<UserProfileDTO> getMutualFollowerList(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        // 1. 获取用户的关注列表
        List<UserProfile> userProfileList = userFollowMapper.selectMutualFollowerByUserId(Math.toIntExact(userId));
        if (userProfileList == null || userProfileList.isEmpty()) {
            return null; // 没有粉丝列表
        }
        Page<UserProfile> pageInfo = (Page<UserProfile>) userProfileList;
        // 转换DTO列表
        List<UserProfileDTO> dtos = userProfileList.stream()
                .map(profile -> {
                    UserProfileDTO dto = new UserProfileDTO();
                    BeanUtils.copyProperties(profile, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 构建分页结果
        PageResult<UserProfileDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
    }

    @Transactional
    public int saveUserChatMessage(Long senderId, Long conversationsId, String content) {
        // 获取或创建会话
        //Conversations conv = conversationsMapper.findPrivateConversation(Math.toIntExact(senderId), Math.toIntExact(receiverId));
        Conversations conv = conversationsMapper.selectByPrimaryKey(conversationsId);
        Long toUserId = Long.valueOf(conv.getUser1Id()==senderId.intValue() ? conv.getUser2Id() : conv.getUser1Id());
        // 保存消息
        Messages message = new Messages();
        message.setConversationId(Math.toIntExact(conv.getId()));
        message.setSenderId(Math.toIntExact(senderId));
        message.setContent(content);

        logger.info("插入消息内容：" + message);

        int i = messagesMapper.insertSelective(message);
        logger.info("插入消息条数：" + i);

        // 更新会话预览
        conv.setPreview(content.length() > 50 ?
                content.substring(0, 47) + "..." : content);
        conv.setLastMessageTime(new Date());
        conversationsMapper.updateById(conv);
        // 更新会话状态未读数量

        incrementUnread(Long.valueOf(conv.getId()), toUserId);


        // 更新未读状态（使用MP的UpdateWrapper）
        UpdateWrapper<ConversationStatus> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("conversation_id", conv.getId())
                .eq("user_id", toUserId)
                .set("unread_count", true);
        statusMapper.update(null, updateWrapper);

        return 1;
    }

    public List<ConversationsDTO> getConversations(Long userId) {
        // 获取会话列表
        List<Conversations> conversationsList = conversationsMapper.selectByUserId(Math.toIntExact(userId));
        if (conversationsList == null || conversationsList.isEmpty()) {
            return new ArrayList<>(); // 没有会话记录
        }

        // 转换为DTO列表
        List<ConversationsDTO> dtos = conversationsList.stream()
                .map(conversation -> {
                    ConversationsDTO dto = new ConversationsDTO();
                    BeanUtils.copyProperties(conversation, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return dtos;
    }


    @Transactional
    public List<MessagesDTO> getHistoryMessages(Long userId, Long conversationId) throws AccessDeniedException {
        logger.info("UserService:用户id：{}，会话id：{}" , userId, conversationId);
        // 验证会话权限
        validateConversationAccess(userId, conversationId);

        // 获取完整消息记录
        List<Messages> messages = messagesMapper.selectFullHistory(conversationId);
        logger.info("UserService:获取会话历史消息,会话ID:{},消息数量:{}", conversationId, messages.size());

        // 标记会话已读
        markConversationRead(userId, conversationId);

        // 转换为DTO列表
        List<MessagesDTO> messagesDTOList = messages.stream()
                .map(message -> {
                    MessagesDTO dto = new MessagesDTO();
                    BeanUtils.copyProperties(message, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        logger.info("UserService:转换消息DTO列表,消息数量:{}", messagesDTOList.size());

        // 补充用户信息
        return messagesDTOList;
    }

    private void validateConversationAccess(Long userId, Long conversationId) throws AccessDeniedException {
        Integer count = statusMapper.countByUserAndConversation(userId, conversationId);
        /*if (count == 0) {
            throw new AccessDeniedException("无权访问该会话");
        }*/
        logger.info("userService: validateConversationAccess:count:{}",count);
    }

    private void markConversationRead(Long userId, Long conversationId) {
        statusMapper.updateUnreadStatus(userId, conversationId, 0, new Date());
        logger.info("UserService:标记会话已读,用户ID:{},会话ID:{}", userId, conversationId);
    }

    public UnreadCountDTO getUnreadCounts(Long userId) {
        List<ConversationStatus> statusList = statusMapper.selectByUserId(userId);

        int privateCount = 0;
        int systemCount = 0;
        for (ConversationStatus status : statusList) {
            if (status.getConversationType() == 0) { // 私信
                privateCount += status.getUnreadCount();
            } else { // 系统通知
                systemCount += status.getUnreadCount();
            }
        }

        return new UnreadCountDTO(privateCount, systemCount);
    }

    @Transactional
    public void incrementUnread(Long conversationId, Long userId) {
        // 原子操作更新未读数
        statusMapper.incrementUnreadCount(conversationId, userId, 1);
    }

    public ConversationsDTO getConversationById(Long conversationId) {
        Conversations conversation = conversationsMapper.selectByPrimaryKey(conversationId);
        if (conversation == null) {
            return null; // 会话不存在
        }

        ConversationsDTO dto = new ConversationsDTO();
        BeanUtils.copyProperties(conversation, dto);
        return dto;
    }

    public boolean updateUserProfile(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = new UserProfile();
        BeanUtils.copyProperties(userProfileDTO, userProfile);
        int result = userProfileMapper.updateByPrimaryKeySelective(userProfile);
        if (result == 0) {
            // 更新成功，清除缓存
            return false;
        }
        // 修改昵称
        UserDO userDO = new UserDO();
        userDO.setUserId(userProfile.getUserId());
        userDO.setNickName(userProfile.getNickname());
        userDO.setAvatar(userProfile.getAvatar());
        int i = userMapper.updateById(userDO);
        if (i == 0) {
            // 更新成功，清除缓存
            return false;
        }
        return true;
    }

    public int realNameAuthentication(String idCard, String name, Long userId) {
        // 1. 获取用户信息
        UserDO userDO = userMapper.selectById(userId);
        if (userDO == null) {
            return -1; // 用户不存在
        }
        // 2. 实名认证
        int code = realNameAuthService.realNameAuthentication(idCard, name);
        if (code == 200) {
            // 3. 更新用户信息
            userDO.setTrueName(name);
            userMapper.updateById(userDO);
            return 200; // 认证成功
        } else if (code == -400) {
            return -400; // 参数错误
        } else if (code == -403) {
            return -403; // 授权或配额错误
        } else {
            return code; // 其他错误
        }

    }


    public PageResult<UserProfileDTO> search(String keyword, Long userId, Integer pageNum, Integer pageSize) {
        // 1. 获取用户列表
        PageHelper.startPage(pageNum, pageSize);
        List<UserProfile> userProfileList = userProfileMapper.search(keyword, userId);
        if (userProfileList == null || userProfileList.isEmpty()) {
            return null; // 没有搜索结果
        }
        Page<UserProfile> pageInfo = (Page<UserProfile>) userProfileList;
        // 转换DTO列表
        List<UserProfileDTO> dtos = userProfileList.stream()
                .map(profile -> {
                    UserProfileDTO dto = new UserProfileDTO();
                    BeanUtils.copyProperties(profile, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        // 构建分页结果
        PageResult<UserProfileDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
    }

    public UserProfileDTO getUserByRoomId(Long roomId) {
        // 1. 获取房间信息
        LiveRoom liveRoom = liveRoomMapper.selectByPrimaryKey(roomId);
        if (liveRoom == null) {
            return null; // 房间不存在
        }
        Long userId = liveRoom.getUserId();
        // 2. 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile == null) {
            return null; // 用户不存在
        }
        // 3. 转换为 DTO
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        BeanUtils.copyProperties(userProfile, userProfileDTO);  // 自动拷贝属性

        return userProfileDTO;
    }

    public boolean isFollow(Long userId, Long followUserId) {
        // 1. 获取用户的关注列表
        UserFollow userFollow = userFollowMapper.isFollow(userId, followUserId);
        if (userFollow == null) {
            return false; // 没有关注
        }
        return true; // 已关注
    }

    public boolean isMutualFollower(Long userId, int followUserId) {
        // 1. 获取用户的关注列表
        List<UserProfile> userProfileList = userFollowMapper.selectMutualFollowerByUserId(Math.toIntExact(userId));
        if (userProfileList == null || userProfileList.isEmpty()) {
            return false; // 没有粉丝列表
        }
        // 2. 检查是否互相关注
        for (UserProfile userProfile : userProfileList) {
            if (userProfile.getUserId() == followUserId) {
                return true; // 是互相关注
            }
        }
        return false; // 不是互相关注
    }
}

