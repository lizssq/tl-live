package org.tl.live.Controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.PhoneLoginParam;
import org.tl.live.enlity.WebResDTO;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.user.DTO.LoginDTO;
import org.tl.user.inter.ISmsRPCService;
import org.tl.user.inter.IUserPhoneLoginRPCService;
import org.tl.user.inter.IUserRPCService;

@Controller
@RequestMapping("/sms")
public class SmsController {
    @DubboReference(check = false)
    private ISmsRPCService smsRPCService;
    @DubboReference(check = false)
    private IUserRPCService userRPCService;
    @DubboReference(check = false)
    private IUserPhoneLoginRPCService userPhoneLoginRPCService;
    @DubboReference(check = false)
    private IGenerateIDRPCService generateIDRPCService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/sendLoginSms")
    public WebResDTO sendLoginSms(String mobile){
        //检验数据
        if(mobile == null || mobile.length() != 11){
            logger.info("手机号格式错误,phone:{}", mobile);
            return new WebResDTO(WebResDTO.ERROR_CODE, "手机号格式不正确");
        }
        //发送短信
        if(!smsRPCService.sendLoginSms(mobile)){
            logger.info("发送失败,phone:{}", mobile);
            return new WebResDTO(WebResDTO.ERROR_CODE, "发送失败");
        }
        logger.info("发送成功,phone:{}", mobile);
        return new WebResDTO(WebResDTO.SUCCESS_CODE, "发送成功");
    }
    @PostMapping("/loginPhone")
    public WebResDTO loginPhone(@RequestBody PhoneLoginParam phoneLoginParam, HttpServletResponse response){
        //检验数据
        if(phoneLoginParam.getPhone() == null || phoneLoginParam.getPhone().length() != 11){
            return new WebResDTO(WebResDTO.ERROR_CODE, "手机号格式不正确");
        }
        //校验四位int类型验证码
        if(phoneLoginParam.getCode() < 1000 || phoneLoginParam.getCode() > 9999){
            return new WebResDTO(WebResDTO.ERROR_CODE, "验证码格式不正确");
        }
        //验证验证码
        if(!smsRPCService.checkCode(phoneLoginParam.getPhone(), phoneLoginParam.getCode()).isSuccess()){
            logger.info("验证码错误,phone:{}", phoneLoginParam.getPhone());
            return new WebResDTO(WebResDTO.ERROR_CODE, "验证码错误");
        }
        //手机号登陆，如果第一次登陆则注册
        LoginDTO loginDTO = userPhoneLoginRPCService.loginByPhone(phoneLoginParam.getPhone());
        if(loginDTO == null){
            logger.info("登陆失败,phone:{}", phoneLoginParam.getPhone());
            return new WebResDTO(WebResDTO.ERROR_CODE, "登陆失败");
        }

        //返回Cookie
        String token = userRPCService.createToken(loginDTO.getUserId());
        logger.info("登陆成功,phone:{}", phoneLoginParam.getPhone());

        Cookie cookie = new Cookie("tltk", token);

        cookie.setMaxAge(30*24*60*60);

        response.addCookie(cookie);

        return new WebResDTO(WebResDTO.SUCCESS_CODE, "登录成功");
    }
}
