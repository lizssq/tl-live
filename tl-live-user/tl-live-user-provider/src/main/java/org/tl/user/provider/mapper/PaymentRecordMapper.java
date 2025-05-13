package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.tl.user.provider.entity.PaymentRecord;

import java.util.List;

/**
* @author k1341
* @description 针对表【payment_record(支付流水记录表)】的数据库操作Mapper
* @createDate 2025-05-04 00:33:52
* @Entity org.tl.user.provider.entity.PaymentRecord
*/
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    int deleteByPrimaryKey(Long id);

    int insert(PaymentRecord record);

    int insertSelective(PaymentRecord record);

    PaymentRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentRecord record);

    int updateByPrimaryKey(PaymentRecord record);

    List<PaymentRecord> selectByUserId(Long userId);

    int deleteById(Long id);

    List<PaymentRecord> selectList();

    PaymentRecord selectById(Long id);
}
