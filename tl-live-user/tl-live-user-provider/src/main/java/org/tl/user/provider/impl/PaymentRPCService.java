package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.PaymentMethodDTO;
import org.tl.user.DTO.PaymentRecordDTO;
import org.tl.user.DTO.RechargePlanDTO;
import org.tl.user.inter.IPaymentRPCService;
import org.tl.user.provider.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

@DubboService
public class PaymentRPCService implements IPaymentRPCService {
    @Resource
    private PaymentService paymentService;

    @Override
    public List<PaymentMethodDTO> getPaymentMethods() {
        return paymentService.getPaymentMethods();
    }

    @Override
    public void handlePaymentCallback(String transactionId, String status) {
        // 处理支付回调逻辑
        // 例如，更新支付记录状态、发送通知等
        // 这里可以调用 PaymentService 中的方法来处理支付回调

    }

    @Override
    public List<PaymentRecordDTO> getUserPaymentRecords(Long userId) {
        return paymentService.getUserPaymentRecords(userId);
    }

    @Override
    public List<PaymentRecordDTO> getUserPaymentRecords(Long userId, String paymentStatus) {
        return null;
    }

    @Override
    public List<PaymentRecordDTO> getUserPaymentRecords(Long userId, String paymentStatus, Long startTime, Long endTime) {
        return null;
    }

    @Override
    public List<PaymentRecordDTO> getAllPaymentRecords() {
        return paymentService.getAllPaymentRecords();
    }

    @Override
    public PaymentRecordDTO addPaymentRecord(PaymentRecordDTO paymentRecordDTO) {
        return paymentService.addPaymentRecord(paymentRecordDTO);
    }

    @Override
    public boolean updatePaymentRecord(Long id, String paymentStatus) {
        return paymentService.updateRechargeRecord(id, paymentStatus);
    }

    @Override
    public PaymentRecordDTO getPaymentRecordById(Long id) {
        return paymentService.getPaymentRecordById(id);
    }

    @Override
    public boolean addRechargePlan(RechargePlanDTO rechargePlanDTO) {
        return paymentService.addRechargePlan(rechargePlanDTO);
    }

    @Override
    public boolean updateRechargePlan(RechargePlanDTO rechargePlanDTO) {
        return paymentService.updateRechargePlan(rechargePlanDTO);
    }

    @Override
    public boolean deleteRechargePlan(Long id) {
        return paymentService.deleteRechargePlan(id);
    }

    @Override
    public RechargePlanDTO getRechargePlanById(Long id) {
        return null;
    }

    @Override
    public List<RechargePlanDTO> getRechargePlans() {
        return paymentService.getRechargePlans();
    }
}
