package org.tl.user.provider.mapper;

import org.apache.ibatis.annotations.Param;
import org.tl.user.provider.entity.Bill;

import java.util.List;

public interface BillMapper {
    /**
     * 获取账单列表
     * @param userId 用户ID
     * @param offset 页码
     * @param limit 每页大小
     * @return 账单列表
     */
    List<Bill> selectBillPage(@Param("userId") Long userId, @Param("offset")Integer offset,@Param("limit") Integer limit,@Param("start") String startTime,@Param("end") String endTime);

    int selectBillCount(@Param("userId")Long userId,@Param("start") String startTime, @Param("end")String endTime);
}
