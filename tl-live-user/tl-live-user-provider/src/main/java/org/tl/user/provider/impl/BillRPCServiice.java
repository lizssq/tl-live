package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.BillDTO;
import org.tl.user.DTO.PageResult;
import org.tl.user.inter.IBillRPCService;
import org.tl.user.provider.service.BillService;

import java.util.List;
@DubboService
public class BillRPCServiice implements IBillRPCService {

    @Resource
    private BillService billService;
    @Override
    public PageResult<BillDTO> getBillList(Long userId, Integer pageNum, Integer pageSize, String startTime, String endTime) {
        return billService.getBillList(userId, pageNum, pageSize, startTime, endTime);
    }
}
