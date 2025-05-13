package org.tl.user.inter;

import org.tl.user.DTO.GiftLogDTO;
import org.tl.user.DTO.GiftTypeDTO;

import java.util.List;

public interface IGiftRPCService {
    // 1. 获取礼物列表
    public List<GiftTypeDTO> getGiftList();
    // 2. 获取礼物详情
    public GiftTypeDTO getGiftInfo(Long giftId);
    // 3. 发送礼物
    public int setGiftInfo(Long senderId, Long receiverId, Long roomId, Integer giftId, Integer amount);
    // 5. 获取用户打赏记录
    public List<GiftLogDTO> getGiftLogListBySenderId(Long senderId);
    // 7. 获取主播打赏记录
    public List<GiftLogDTO> getGiftLogListByReceiverId(Long receiverId);
    // 8. 获取礼物排行榜
    public List<GiftTypeDTO> getGiftRankList(Long roomId);
    // 4. 获取全部打赏记录
    public List<GiftLogDTO> getAllGiftLogList();
    // 6. 添加礼物
    public int addGift(GiftTypeDTO giftTypeDTO);
    // 9. 删除礼物
    public int deleteGift(Long giftId);
    // 10. 更新礼物信息
    public int updateGift(GiftTypeDTO giftTypeDTO);

}
