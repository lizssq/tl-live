package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.tl.user.DTO.BillDTO;
import org.tl.user.DTO.PageResult;
import org.tl.user.provider.entity.Bill;
import org.tl.user.provider.mapper.BillMapper;

import java.util.List;

@Service
public class BillService {

    // 这里可以添加一些业务逻辑，比如调用数据库获取账单列表等
    // 例如：
     @Resource
     private BillMapper billMapper;

     public PageResult<BillDTO> getBillList(Long userId, Integer pageNum, Integer pageSize, String startTime, String endTime) {
         Integer offset=null;
         if(pageNum != null || pageSize != null) {
             offset = (pageNum - 1) * pageSize;
         }
         List<Bill> bills = billMapper.selectBillPage(userId, offset, pageSize, startTime, endTime);
         // 这里可以将Bill转换为BillDTO,stream流
            List<BillDTO> billDTOs = bills.stream().map(bill -> {
                BillDTO billDTO = new BillDTO();
                billDTO.setDate(bill.getDate());
                billDTO.setTime(bill.getTime());
                billDTO.setType(bill.getType());
                billDTO.setAmount(bill.getAmount());
                billDTO.setDescription(bill.getDescription());
                billDTO.setDate(bill.getDate());
                // 其他属性的转换
                return billDTO;
            }).toList();
            // 这里可以计算总页数和总记录数
            int totalRecords = billMapper.selectBillCount(userId,startTime,endTime);

         return new PageResult<>(pageNum,pageSize, (long) totalRecords,totalRecords/pageSize,billDTOs);
     }

}
