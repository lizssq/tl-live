package org.tl.user.inter;

import org.tl.user.DTO.BillDTO;
import org.tl.user.DTO.PageResult;

import java.util.List;

public interface IBillRPCService {
    /**
     * 获取账单列表
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 账单列表
     */
    PageResult<BillDTO> getBillList(Long userId, Integer pageNum, Integer pageSize, String startTime, String endTime);
}
