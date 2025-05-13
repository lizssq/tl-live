package org.tl.live.Controller;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.GiftTypeDTO;
import org.tl.user.inter.IGiftRPCService;

@RestController
@RequestMapping("/gift")
public class GiftController {
    @DubboReference(check = false)
    private IGiftRPCService giftRPCService;

    @PostMapping("/sendGift")
    public WebResDTO sendGift(Long senderId, Long receiverId, Long roomId, Integer giftId, Integer amount) {
        // 调用礼物服务发送礼物
        int result = giftRPCService.setGiftInfo(senderId, receiverId, roomId, giftId, amount);
        if (result > 0) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "礼物发送成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "礼物发送失败");
        }
    }
    @GetMapping("/getGiftLogListBySenderId")
    public WebResDTO getGiftLogListBySenderId(Long senderId) {
        // 调用礼物服务获取打赏记录
        var giftLogList = giftRPCService.getGiftLogListBySenderId(senderId);
        if (giftLogList != null) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, giftLogList);
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "获取打赏记录失败");
        }
    }
    @GetMapping("/getGiftLogListByReceiverId")
    public WebResDTO getGiftLogListByReceiverId(Long receiverId) {
        // 调用礼物服务获取打赏记录
        var giftLogList = giftRPCService.getGiftLogListByReceiverId(receiverId);
        if (giftLogList != null) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, giftLogList);
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "获取打赏记录失败");
        }
    }
    @GetMapping("/getGiftLogList")
    public WebResDTO getGiftLogList() {
        // 调用礼物服务获取全部打赏记录
        var giftLogList = giftRPCService.getAllGiftLogList();
        if (giftLogList != null) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, giftLogList);
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "获取打赏记录失败");
        }
    }
    @GetMapping("/getGiftList")
    public WebResDTO getGiftList() {
        // 调用礼物服务获取礼物列表
        var giftList = giftRPCService.getGiftList();
        if (giftList != null) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, giftList);
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "获取礼物列表失败");
        }
    }
    @PostMapping("/addGiftType")
    public WebResDTO addGiftType(@RequestBody GiftTypeDTO giftTypeDTO) {
        // 调用礼物服务添加礼物类型
        int result = giftRPCService.addGift(giftTypeDTO);
        if (result > 0) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "礼物类型添加成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "礼物类型添加失败");
        }
    }

    @PostMapping("/updateGiftType")
    public WebResDTO updateGiftType(@RequestBody GiftTypeDTO giftTypeDTO) {
        // 调用礼物服务更新礼物类型
        int result = giftRPCService.updateGift(giftTypeDTO);
        if (result > 0) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "礼物类型更新成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "礼物类型更新失败");
        }
    }

    @PostMapping("/deleteGiftType")
    public WebResDTO deleteGiftType(@RequestParam Long giftId) {
        // 调用礼物服务删除礼物类型
        int result = giftRPCService.deleteGift(giftId);
        if (result > 0) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "礼物类型删除成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "礼物类型删除失败");
        }
    }
    @GetMapping("/getGiftById")
    public WebResDTO getGiftById(@RequestParam Long giftId) {
        // 调用礼物服务获取礼物详情
        var giftInfo = giftRPCService.getGiftInfo(giftId);
        if (giftInfo != null) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, giftInfo);
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "获取礼物详情失败");
        }
    }

}
