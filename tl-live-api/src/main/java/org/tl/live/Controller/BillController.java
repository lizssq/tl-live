package org.tl.live.Controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.dubbo.config.annotation.DubboReference;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.BillDTO;
import org.tl.user.DTO.PageResult;
import org.tl.user.inter.IBillRPCService;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {
    @DubboReference(check = false)
    private IBillRPCService billService;

    @RequestMapping("/getBillList")
    public WebResDTO getBillList(@RequestParam("userId") Long userId,
                                 @RequestParam("pageNum") Integer pageNum,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("startTime") String startTime,
                                 @RequestParam("endTime") String endTime) {
        if (userId == null || pageNum == null || pageSize == null || startTime==null || endTime==null) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "参数错误");
        }
        PageResult<BillDTO> billList = billService.getBillList(userId, pageNum, pageSize, startTime, endTime);
        return new WebResDTO(WebResDTO.SUCCESS_CODE, billList);
    }

    @RequestMapping("/getBillListByTime")
    public WebResDTO getBillListByTime(@RequestParam("userId") Long userId,
                                       @RequestParam("startTime") String startTime,
                                       @RequestParam("endTime") String endTime) {
        if (userId == null || startTime==null || endTime==null) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "参数错误");
        }
        PageResult<BillDTO> billList = billService.getBillList(userId, 1, 1000, startTime, endTime);
        return new WebResDTO(WebResDTO.SUCCESS_CODE, billList);
    }
    @RequestMapping("/getBillListByPage")
    public WebResDTO getBillListByPage(@RequestParam("userId") Long userId,
                                       @RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        if (userId == null || pageNum == null || pageSize == null) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "参数错误");
        }
        PageResult<BillDTO> billList = billService.getBillList(userId, pageNum, pageSize, null, null);
        return new WebResDTO(WebResDTO.SUCCESS_CODE, billList);
    }

}
