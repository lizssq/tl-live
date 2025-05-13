package org.tl.live.Controller;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.tl.live.config.AlipayConfig;
import org.tl.live.enlity.WebResDTO;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.user.DTO.PaymentMethodDTO;
import org.tl.user.DTO.PaymentRecordDTO;
import org.tl.user.DTO.PaymentStatus;
import org.tl.user.DTO.RechargePlanDTO;
import org.tl.user.inter.IPaymentRPCService;
import org.tl.user.inter.IUserRPCService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final AlipayConfig alipayConfig;

    @Autowired
    public PaymentController(AlipayConfig alipayConfig) {
        this.alipayConfig = alipayConfig;
        initializeAlipayConfig();
    }

    private void initializeAlipayConfig() {
        Config config = new Config();
        config.protocol = alipayConfig.getProtocol();
        config.gatewayHost = alipayConfig.getGatewayHost();
        config.signType = alipayConfig.getSignType();
        config.appId = alipayConfig.getAppId();
        config.notifyUrl = alipayConfig.getNotifyUrl();
        config.merchantPrivateKey = alipayConfig.getMerchantPrivateKey();
        config.alipayPublicKey = alipayConfig.getAlipayPublicKey();

        Factory.setOptions(config);
    }
    @DubboReference(check = false)
    private IPaymentRPCService paymentRPCService;

    @DubboReference(check = false)
    private IUserRPCService userRPCService;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/alipay")
    public void alipay(Long orderId, HttpServletResponse response) throws Exception {
        PaymentRecordDTO paymentRecord = paymentRPCService.getPaymentRecordById(orderId);
        AlipayTradePrecreateResponse res =
                Factory.Payment.FaceToFace().preCreate(String.valueOf(paymentRecord.getPlanDesc()), String.valueOf(paymentRecord.getId()), String.valueOf(paymentRecord.getAmountPaid()));
        String httpBody = res.getHttpBody();
        System.out.println("响应结果：");
        System.out.println(httpBody);
        JSONObject jsonObject = JSONUtil.parseObj(httpBody);
        String qrUrl =
                jsonObject.getJSONObject("alipay_trade_precreate_response").get("qr_code").toString();
        // 生成二维码
        ServletOutputStream out = response.getOutputStream();
        QrCodeUtil.generate(qrUrl, 300, 300,"",out);
        out.close();
    }
    //扫码支付完成，支付宝回调我们的接口
    @PostMapping("/alipay/notify")
    public String notifyUrl(HttpServletRequest request) {
        System.out.println("回调。。。。。。");
        // 获取支付结果
        // 根据结果进行业务处理：修改订单的状态
        String outTradeNo = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        System.out.println(outTradeNo);
        System.out.println(trade_status);
        if ("TRADE_SUCCESS".equals(trade_status)) {
            boolean result = paymentRPCService.updatePaymentRecord(Long.valueOf(outTradeNo), "SUCCESS");
            System.out.println("支付成功");
        }
        if ("TRADE_CLOSED".equals(trade_status)) {
            boolean result = paymentRPCService.updatePaymentRecord(Long.valueOf(outTradeNo), "FAILED");
            System.out.println("支付失败");
        }
        return outTradeNo;
    }

    // 获取充值记录状态
    @GetMapping("/paymentStatus")
    public WebResDTO getPaymentStatus(Long id) {
        PaymentRecordDTO paymentRecord = paymentRPCService.getPaymentRecordById(id);
        return new WebResDTO(WebResDTO.SUCCESS_CODE, paymentRecord);
    }


    // 其他服务和方法
    @GetMapping("/paymentMethods")
    public WebResDTO getPaymentMethods() {
        List<PaymentMethodDTO> paymentMethods = paymentRPCService.getPaymentMethods();
        if (paymentMethods == null || paymentMethods.isEmpty()) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "No payment methods found");
        }
        return new WebResDTO(WebResDTO.SUCCESS_CODE,paymentMethods);
    }

    // 获取充值套餐
    @GetMapping("/RechargePlan")
    public WebResDTO getRechargePlan() {
        List<RechargePlanDTO> rechargePlanDTOS = paymentRPCService.getRechargePlans();
        if (rechargePlanDTOS == null || rechargePlanDTOS.isEmpty()) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "没有充值套餐");
        }
        return new WebResDTO(WebResDTO.SUCCESS_CODE,rechargePlanDTOS);
    }

    @PostMapping("/RechargePlan")
    public WebResDTO addRechargePlan(@RequestBody RechargePlanDTO rechargePlanDTO) {
        boolean result = paymentRPCService.addRechargePlan(rechargePlanDTO);
        if (result) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "冲值套餐添加成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "冲值套餐添加失败");
        }
    }

    @PutMapping("/RechargePlan")
    public WebResDTO updateRechargePlan(@RequestBody RechargePlanDTO rechargePlanDTO) {
        boolean result = paymentRPCService.updateRechargePlan(rechargePlanDTO);
        if (result) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "冲值套餐更新成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "冲值套餐更新失败");
        }
    }

    @DeleteMapping("/RechargePlan/{id}")
    public WebResDTO deleteRechargePlan(@PathVariable Long id) {
        boolean result = paymentRPCService.deleteRechargePlan(id);
        if (result) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "冲值套餐删除成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "冲值套餐删除失败");
        }
    }
    // 获取充值记录
    @GetMapping("/paymentRecord/{userId}")
    public WebResDTO getUserPaymentRecords(@PathVariable Long userId) {
        List<PaymentRecordDTO> paymentRecordDTOS = paymentRPCService.getUserPaymentRecords(userId);
        if (paymentRecordDTOS == null || paymentRecordDTOS.isEmpty()) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "没有充值记录");
        }
        return new WebResDTO(WebResDTO.SUCCESS_CODE,paymentRecordDTOS);
    }

    @GetMapping("/paymentRecord")
    public WebResDTO getAllPaymentRecords() {
        List<PaymentRecordDTO> paymentRecordDTOS = paymentRPCService.getAllPaymentRecords();
        if (paymentRecordDTOS == null || paymentRecordDTOS.isEmpty()) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "没有充值记录");
        }
        return new WebResDTO(WebResDTO.SUCCESS_CODE,paymentRecordDTOS);
    }

    @PostMapping("/paymentRecord")
    public WebResDTO addPaymentRecord(Long userId, Long planId, Long methodId , BigDecimal amountPaid, Integer coinsReceived) {
        // 这里可以添加一些参数校验逻辑
        if (userId == null || planId == null || methodId == null || amountPaid == null || coinsReceived == null) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "参数不能为空");
        }
        PaymentRecordDTO paymentRecordDTO = new PaymentRecordDTO();
        paymentRecordDTO.setUserId(Math.toIntExact(userId));
        paymentRecordDTO.setPlanId(Math.toIntExact(planId));
        paymentRecordDTO.setMethodId(Math.toIntExact(methodId));
        paymentRecordDTO.setAmountPaid(amountPaid);
        paymentRecordDTO.setCoinsReceived(coinsReceived);
        // 生成唯一的交易ID
        PaymentRecordDTO result = paymentRPCService.addPaymentRecord(paymentRecordDTO);
        if (result!= null) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, result);
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "充值记录添加失败");
        }
    }

    @PutMapping("/paymentRecord")
    public WebResDTO updateRechargeRecord(Long id, String paymentStatus) {
        // 这里可以添加一些参数校验逻辑
        if (id == null || paymentStatus == null) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "参数不能为空");
        }
        if(!paymentStatus.equals("SUCCESS") && !paymentStatus.equals("FAILED")&& !paymentStatus.equals("PENDING")) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "支付状态不合法");
        }
        boolean result = paymentRPCService.updatePaymentRecord(id, paymentStatus);

        if (result) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, "充值记录更新成功");
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "充值记录更新失败");
        }
    }

}
