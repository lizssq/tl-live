package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tl.user.DTO.GiftLogDTO;
import org.tl.user.DTO.GiftTypeDTO;
import org.tl.user.DTO.UserDTO;
import org.tl.user.provider.entity.GiftLog;
import org.tl.user.provider.entity.GiftType;
import org.tl.user.provider.mapper.GiftLogMapper;
import org.tl.user.provider.mapper.GiftTypeMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiftService {
    @Resource
    private GiftTypeMapper giftTypeMapper;
    @Resource
    private GiftLogMapper giftLogMapper;
    @Resource
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    // 其他服务和方法
    // 获取礼物列表
    public List<GiftTypeDTO> getGiftList() {
        List<GiftTypeDTO> dtoList = new ArrayList<>();

        // 调用数据库或其他服务获取礼物列表
        List<GiftType> giftTypes = giftTypeMapper.selectList(null);
        for(GiftType giftType : giftTypes) {
            GiftTypeDTO dto = new GiftTypeDTO();
            BeanUtils.copyProperties(giftType, dto);  // 自动拷贝属性
            dtoList.add(dto);
        }
        return dtoList;
    }

    // 发送礼物记录
    @Transactional
    public int  setGiftInfo (Long senderId, Long receiverId, Long roomId, Integer giftId, Integer amount ) {
        // 处理发送礼物的逻辑
        // 例如，更新数据库、发送消息等
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        GiftLog giftLog = new GiftLog();
        giftLog.setSenderId(senderId);
        giftLog.setReceiverId(receiverId);
        giftLog.setRoomId(roomId);
        giftLog.setGiftId(giftId);
        giftLog.setAmount(amount);
        GiftTypeDTO giftTypeDetail = getGiftTypeDetail(Long.valueOf(giftId));// 获取礼物类型详情
        BigDecimal totalMoney = BigDecimal.valueOf(giftTypeDetail.getPrice() *amount); // 获取礼物价格
        giftLog.setTotalCost(totalMoney);
        int i = giftLogMapper.insertSelective(giftLog);// 插入打赏记录
        if(i == 0) {
            return 0; // 插入失败
        }
        // 更新用户的金币数量
        UserDTO sender = userService.getUserById(senderId);
        UserDTO receiver = userService.getUserById(receiverId);
        if(sender != null && receiver != null) {
            boolean transfer = userService.transfer(senderId, receiverId, totalMoney);
            logger.info("转账成功: " + transfer);
        }
        return i;
    }

    public List<GiftLogDTO> getGiftLogListBySenderId(Long senderId) {
        // 调用数据库或其他服务获取打赏记录
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        List<GiftLog> giftLog = giftLogMapper.selectBySenderId(senderId);
        //拷贝
        List<GiftLogDTO> giftLogDTOList = new ArrayList<>();
        for (GiftLog giftLog1 : giftLog) {
            GiftLogDTO giftLogDTO = new GiftLogDTO();
            BeanUtils.copyProperties(giftLog1, giftLogDTO);  // 自动拷贝属性
            giftLogDTOList.add(giftLogDTO);
        }
        return giftLogDTOList;
    }

    public List<GiftLogDTO> getGiftLogListByReceiverId(Long receiverId) {
        // 调用数据库或其他服务获取打赏记录
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        List<GiftLog> giftLog = giftLogMapper.selectByReceiverId(receiverId);
        //拷贝
        List<GiftLogDTO> giftLogDTOList = new ArrayList<>();
        for (GiftLog giftLog1 : giftLog) {
            GiftLogDTO giftLogDTO = new GiftLogDTO();
            BeanUtils.copyProperties(giftLog1, giftLogDTO);  // 自动拷贝属性
            giftLogDTOList.add(giftLogDTO);
        }
        return giftLogDTOList;
    }
    // 获取用户打赏排行榜



    // 获取礼物日志列表
    public List<GiftLogDTO> getGiftLogList() {
        // 调用数据库或其他服务获取打赏记录
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        List<GiftLog> giftLog = giftLogMapper.selectAll(null);
        //拷贝
        List<GiftLogDTO> giftLogDTOList = new ArrayList<>();
        for (GiftLog giftLog1 : giftLog) {
            GiftLogDTO giftLogDTO = new GiftLogDTO();
            BeanUtils.copyProperties(giftLog1, giftLogDTO);  // 自动拷贝属性
            giftLogDTOList.add(giftLogDTO);
        }
        return giftLogDTOList;
    }

    // 添加礼物类型
    @Transactional
    public int addGiftType(GiftTypeDTO giftType) {
        // 处理添加礼物类型的逻辑
        // 例如，更新数据库、发送消息等
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        // 检查礼物类型是否已存在
        GiftType existingGiftType = giftTypeMapper.selectByGiftName(giftType.getGiftName());
        if (existingGiftType != null) {
            // 礼物类型已存在，返回错误或抛出异常
            return -1; // 或者抛出自定义异常
        }
        // 拷贝
        GiftType newGiftType = new GiftType();
        BeanUtils.copyProperties(giftType, newGiftType);  // 自动拷贝属性
        return giftTypeMapper.insertSelective(newGiftType);
    }

    // 更新礼物类型
    @Transactional
    public int updateGiftType(GiftTypeDTO giftType) {
        // 处理更新礼物类型的逻辑
        // 例如，更新数据库、发送消息等
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        // 检查礼物类型是否存在
        GiftType existingGiftType = giftTypeMapper.selectByName(giftType.getGiftName());
        if (existingGiftType != null && !existingGiftType.getId().equals(giftType.getId())) {
            // 礼物类型已存在，返回错误或抛出异常
            return -1; // 或者抛出自定义异常
        }
        // 拷贝
        GiftType newGiftType = new GiftType();
        BeanUtils.copyProperties(giftType, newGiftType);
        return giftTypeMapper.updateByPrimaryKeySelective(newGiftType);
    }

    // 删除礼物类型
    @Transactional
    public int deleteGiftType(Long giftId) {
        // 处理删除礼物类型的逻辑
        // 例如，更新数据库、发送消息等
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        return giftTypeMapper.deleteById(giftId);
    }

    // 获取礼物类型详情
    public GiftTypeDTO getGiftTypeDetail(Long giftId) {
        // 处理获取礼物类型详情的逻辑
        // 例如，更新数据库、发送消息等
        // 这里可以调用其他服务或方法来实现具体的业务逻辑
        GiftType giftType = giftTypeMapper.selectByPrimaryKey(giftId);
        if (giftType == null) {
            return null; // 或者抛出自定义异常
        }
        GiftTypeDTO dto = new GiftTypeDTO();
        BeanUtils.copyProperties(giftType, dto);  // 自动拷贝属性
        return dto;
    }
}
