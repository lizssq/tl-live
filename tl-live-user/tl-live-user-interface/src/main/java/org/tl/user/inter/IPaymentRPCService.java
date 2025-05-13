package org.tl.user.inter;

import org.tl.user.DTO.PaymentMethodDTO;
import org.tl.user.DTO.PaymentRecordDTO;
import org.tl.user.DTO.RechargePlanDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IPaymentRPCService {
    // 1. 获取支付方式列表
    public List<PaymentMethodDTO> getPaymentMethods();

    // 4. 处理支付回调
    public void handlePaymentCallback(String transactionId, String status);

    // 5. 获取用户充值记录
    public List<PaymentRecordDTO> getUserPaymentRecords(Long userId);

    public List<PaymentRecordDTO> getUserPaymentRecords(Long userId, String paymentStatus);

    public List<PaymentRecordDTO> getUserPaymentRecords(Long userId, String paymentStatus, Long startTime, Long endTime);

    public List<PaymentRecordDTO> getAllPaymentRecords();

    // 充值记录增删改查
    public PaymentRecordDTO addPaymentRecord(PaymentRecordDTO paymentRecordDTO);

    public boolean updatePaymentRecord(Long id, String paymentStatus);
    //通过id获取
    public PaymentRecordDTO getPaymentRecordById(Long id);

    // 6. 充值套餐增删改查
    public boolean addRechargePlan(RechargePlanDTO rechargePlanDTO);
    public boolean updateRechargePlan(RechargePlanDTO rechargePlanDTO);
    public boolean deleteRechargePlan(Long id);
    public RechargePlanDTO getRechargePlanById(Long id);
    public List<RechargePlanDTO> getRechargePlans();
}
