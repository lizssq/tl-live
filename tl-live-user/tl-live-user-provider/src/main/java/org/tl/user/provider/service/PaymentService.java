package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.user.DTO.PaymentMethodDTO;
import org.tl.user.DTO.PaymentRecordDTO;
import org.tl.user.DTO.RechargePlanDTO;
import org.tl.user.provider.entity.PaymentMethod;
import org.tl.user.provider.entity.PaymentRecord;
import org.tl.user.provider.entity.PaymentStatus;
import org.tl.user.provider.entity.RechargePlan;
import org.tl.user.provider.mapper.PaymentMethodMapper;
import org.tl.user.provider.mapper.PaymentRecordMapper;
import org.tl.user.provider.mapper.RechargePlanMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {
    @Resource
    private PaymentMethodMapper paymentMethodMapper;

    @Resource
    private PaymentRecordMapper paymentRecordMapper;

    @Resource
    private RechargePlanMapper rechargePlanMapper;

    @DubboReference
    private IGenerateIDRPCService generateIDRPCService;
    @Resource
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    // 其他服务和方法
    //支付方式管理
    public List<PaymentMethodDTO> getPaymentMethods() {
        List<PaymentMethodDTO> paymentMethods =new ArrayList<>();
        List<PaymentMethod> paymentMethodList = paymentMethodMapper.selectList(null);
        //将PaymentMethod转换为PaymentMethodDTO
        for (PaymentMethod paymentMethod : paymentMethodList) {
            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
            BeanUtils.copyProperties(paymentMethod, paymentMethodDTO);
            paymentMethods.add(paymentMethodDTO);
        }
        return paymentMethods;
    }
    //查找
    public PaymentMethodDTO getPaymentMethodById(Long id) {
        PaymentMethod paymentMethod = paymentMethodMapper.selectById(id);
        if (paymentMethod != null) {
            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
            BeanUtils.copyProperties(paymentMethod, paymentMethodDTO);
            return paymentMethodDTO;
        }
        return null;
    }
    //添加
    public boolean addPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = new PaymentMethod();
        BeanUtils.copyProperties(paymentMethodDTO, paymentMethod);
        return paymentMethodMapper.insert(paymentMethod) > 0;
    }
    //更新
    public boolean updatePaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = new PaymentMethod();
        BeanUtils.copyProperties(paymentMethodDTO, paymentMethod);
        return paymentMethodMapper.updateById(paymentMethod) > 0;
    }
    //逻辑删除
    public boolean deletePaymentMethod(Long id) {
        return paymentMethodMapper.deleteById(id) > 0;
    }

    //添加充值记录
    @Transactional
    public PaymentRecordDTO addPaymentRecord( PaymentRecordDTO paymentRecordDTO) {
        Long id = generateIDRPCService.getUnorderedID();
        Long transactionId = generateIDRPCService.getUnorderedID();
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setId(id);
        paymentRecord.setUserId(paymentRecordDTO.getUserId());
        paymentRecord.setPlanId(paymentRecordDTO.getPlanId());
        paymentRecord.setMethodId(paymentRecordDTO.getMethodId());
        paymentRecord.setTransactionId(String.valueOf(transactionId));
        paymentRecord.setAmountPaid(paymentRecordDTO.getAmountPaid());
        paymentRecord.setCoinsReceived(paymentRecordDTO.getCoinsReceived());
        paymentRecord.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));
        paymentRecordDTO.setTransactionId(String.valueOf(transactionId));
        paymentRecordDTO.setId(id);
        paymentRecordDTO.setPaymentStatus(String.valueOf(org.tl.user.DTO.PaymentStatus.PENDING));
        logger.info("paymentRecordDTO：充值记录ID"+paymentRecordDTO.getId());
        logger.info("paymentRecord：生成充值记录"+paymentRecord.getId());
        int i = paymentRecordMapper.insertSelective(paymentRecord);
        if(i == 0) {
            return null; // 插入失败
        }
        return paymentRecordDTO;
    }
    //更新充值记录
    //todo
    //充值和修改充值记录并没有实现出现问题回退，待修改
    @Transactional
    public boolean updateRechargeRecord(Long id, String paymentStatus) {
        //将PaymentStatus转换为PaymentStatus
        logger.info("更新充值记录"+id);
        PaymentStatus paymentStatusEnum = PaymentStatus.fromValue(paymentStatus);
        if(paymentStatusEnum == PaymentStatus.SUCCESS) {
            //更新用户金币数量
            PaymentRecord paymentRecord = paymentRecordMapper.selectById(id);
            if (paymentRecord == null || paymentRecord.getPaymentStatus() == PaymentStatus.SUCCESS || paymentRecord.getPaymentStatus() == PaymentStatus.FAILED) {
                logger.info("充值记录不存在或已处理");
                return false; // 充值记录不存在或已处理
            }
            Long userId = Long.valueOf(paymentRecord.getUserId());
            Integer coinsReceived = paymentRecord.getCoinsReceived();

            Date completedAt = new Date();
            paymentRecord.setPaymentStatus(String.valueOf(paymentStatusEnum));
            paymentRecord.setCompletedAt(completedAt);
            logger.info("修改后的充值记录"+paymentRecord);
            int i = paymentRecordMapper.updateById(paymentRecord);
            logger.info("更新充值记录"+i);
            return userService.recharge(userId, BigDecimal.valueOf(coinsReceived)) && i != 0;
        }
        PaymentRecord paymentRecord = new PaymentRecord();
        Date completedAt = new Date();
        paymentRecord.setId(id);
        paymentRecord.setPaymentStatus(String.valueOf(paymentStatusEnum));
        paymentRecord.setCompletedAt(completedAt);
        int i = paymentRecordMapper.updateById(paymentRecord);
        if (i == 0) {
            return false; // 更新失败
        }
        return true;
    }

    //获取充值记录
    public List<PaymentRecordDTO> getUserPaymentRecords(Long userId) {
        List<PaymentRecord> paymentRecords = paymentRecordMapper.selectByUserId(userId);
        List<PaymentRecordDTO> paymentRecordDTOs = new ArrayList<>();
        logger.info("获取充值记录"+paymentRecords);
        //将PaymentRecord转换为PaymentRecordDTO
        for(PaymentRecord paymentRecord : paymentRecords) {
            PaymentRecordDTO paymentRecordDTO = new PaymentRecordDTO();
            BeanUtils.copyProperties(paymentRecord, paymentRecordDTO);
            paymentRecordDTO.setPaymentStatus(String.valueOf(paymentRecord.getPaymentStatus()));
            paymentRecordDTOs.add(paymentRecordDTO);
        }
        return paymentRecordDTOs;
    }

    //获取充值记录
    public List<PaymentRecordDTO> getAllPaymentRecords() {
        List<PaymentRecord> paymentRecords = paymentRecordMapper.selectList(null);
        List<PaymentRecordDTO> paymentRecordDTOs = new ArrayList<>();
        //将PaymentRecord转换为PaymentRecordDTO
        for(PaymentRecord paymentRecord : paymentRecords) {
            PaymentRecordDTO paymentRecordDTO = new PaymentRecordDTO();
            BeanUtils.copyProperties(paymentRecord, paymentRecordDTO);
            paymentRecordDTO.setPaymentStatus(String.valueOf(paymentRecord.getPaymentStatus()));
            paymentRecordDTOs.add(paymentRecordDTO);
        }
        return paymentRecordDTOs;
    }
    //获取充值记录通过Id
    public PaymentRecordDTO getPaymentRecordById(Long id) {
        PaymentRecord paymentRecord = paymentRecordMapper.selectById(id);
        if (paymentRecord != null) {
            PaymentRecordDTO paymentRecordDTO = new PaymentRecordDTO();
            BeanUtils.copyProperties(paymentRecord, paymentRecordDTO);
            paymentRecordDTO.setPaymentStatus(String.valueOf(paymentRecord.getPaymentStatus()));
            return paymentRecordDTO;
        }
        return null;
    }

    //充值方案
    public List<RechargePlanDTO> getRechargePlans() {
        List<RechargePlan> rechargePlans = rechargePlanMapper.selectList(null);
        List<RechargePlanDTO> rechargePlanDTOs = new ArrayList<>();
        //将RechargePlan转换为RechargePlanDTO
        for (RechargePlan rechargePlan : rechargePlans) {
            RechargePlanDTO rechargePlanDTO = new RechargePlanDTO();
            BeanUtils.copyProperties(rechargePlan, rechargePlanDTO);
            rechargePlanDTOs.add(rechargePlanDTO);
        }
        return rechargePlanDTOs;
    }

    //添加充值计划
    public boolean addRechargePlan(RechargePlanDTO rechargePlanDTO) {
        //将RechargePlanDTO转换为RechargePlan
        RechargePlan rechargePlan = new RechargePlan();
        BeanUtils.copyProperties(rechargePlanDTO, rechargePlan);
        return rechargePlanMapper.insertSelective(rechargePlan) > 0;
    }
    //更新充值计划
    public boolean updateRechargePlan(RechargePlanDTO rechargePlanDTO) {
        //将RechargePlanDTO转换为RechargePlan
        RechargePlan rechargePlan = new RechargePlan();
        BeanUtils.copyProperties(rechargePlanDTO, rechargePlan);
        return rechargePlanMapper.updateByPrimaryKeySelective(rechargePlan) > 0;
    }
    //删除充值计划
    public boolean deleteRechargePlan(Long id) {
        return rechargePlanMapper.deleteById(id) > 0;
    }

}
