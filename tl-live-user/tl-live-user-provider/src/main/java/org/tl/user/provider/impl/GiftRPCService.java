package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.GiftLogDTO;
import org.tl.user.DTO.GiftTypeDTO;
import org.tl.user.inter.IGiftRPCService;
import org.tl.user.provider.service.GiftService;

import java.util.List;

@DubboService
public class GiftRPCService implements IGiftRPCService {
    @Resource
    private GiftService giftService;


    @Override
    public List<GiftTypeDTO> getGiftList() {
        return giftService.getGiftList();
    }

    @Override
    public GiftTypeDTO getGiftInfo(Long giftId) {
        return giftService.getGiftTypeDetail(giftId);
    }

    @Override
    public int setGiftInfo(Long senderId, Long receiverId, Long roomId, Integer giftId, Integer amount) {
        return giftService.setGiftInfo(senderId, receiverId, roomId, giftId, amount);
    }

    @Override
    public List<GiftLogDTO> getGiftLogListBySenderId(Long senderId) {
        return giftService.getGiftLogListBySenderId(senderId);
    }

    @Override
    public List<GiftLogDTO> getGiftLogListByReceiverId(Long receiverId) {
        return giftService.getGiftLogListByReceiverId(receiverId);
    }

    @Override
    public List<GiftTypeDTO> getGiftRankList(Long roomId) {
        return null;
    }

    @Override
    public List<GiftLogDTO> getAllGiftLogList() {
        return giftService.getGiftLogList();
    }

    @Override
    public int addGift(GiftTypeDTO giftTypeDTO) {
        return giftService.addGiftType(giftTypeDTO);
    }

    @Override
    public int deleteGift(Long giftId) {
        return giftService.deleteGiftType(giftId);
    }

    @Override
    public int updateGift(GiftTypeDTO giftTypeDTO) {
        return giftService.updateGiftType(giftTypeDTO);
    }
}
