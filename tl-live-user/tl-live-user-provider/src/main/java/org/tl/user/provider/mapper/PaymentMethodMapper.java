package org.tl.user.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.tl.user.provider.entity.PaymentMethod;

/**
* @author k1341
* @description 针对表【payment_method(支付渠道配置表)】的数据库操作Mapper
* @createDate 2025-05-04 00:33:52
* @Entity org.tl.user.provider.entity.PaymentMethod
*/
public interface PaymentMethodMapper extends BaseMapper<PaymentMethod> {

    int deleteByPrimaryKey(Long id);

    int insert(PaymentMethod record);

    int insertSelective(PaymentMethod record);

    PaymentMethod selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentMethod record);

    int updateByPrimaryKey(PaymentMethod record);

    int deleteById(Long id);

}
