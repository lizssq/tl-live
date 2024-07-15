package org.tl.live.Controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.PhoneLoginParam;
import org.tl.live.enlity.WebResDTO;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.user.DTO.LoginDTO;
import org.tl.user.inter.ISmsRPCService;
import org.tl.user.inter.IUserPhoneLoginRPCService;
import org.tl.user.inter.IUserRPCService;

@RestController
@RequestMapping("/sms")
@CrossOrigin
public class SmsController {
    @DubboReference
    private ISmsRPCService smsRPCService;
    @DubboReference
    private IUserRPCService userRPCService;
    @DubboReference
    private IUserPhoneLoginRPCService userPhoneLoginRPCService;
    @DubboReference
    private IGenerateIDRPCService generateIDRPCService;

    @PostMapping("/sendLoginSms")
    public WebResDTO sendLoginSms(){
        String phone = "15639777163";
        //检验数据
        if(phone == null || phone.length() != 11){
            return new WebResDTO(WebResDTO.ERROR_CODE, "手机号格式不正确");
        }
        //发送短信
        if(!smsRPCService.sendLoginSms(phone)){
            return new WebResDTO(WebResDTO.ERROR_CODE, "发送失败");
        }
        return new WebResDTO(WebResDTO.SUCCESS_CODE, "发送成功");
    }
    @PostMapping("/loginPhone")
    public WebResDTO loginPhone( HttpServletResponse response){
        //测试数据
        String Phone = "15639777163";
        int Code = 5764;
        //检验数据
        if(Phone == null || Phone.length() != 11){
            return new WebResDTO(WebResDTO.ERROR_CODE, "手机号格式不正确");
        }
        //校验六位int类型验证码
        if(Code < 0 || Code > 999999){
            return new WebResDTO(WebResDTO.ERROR_CODE, "验证码格式不正确");
        }
        //验证验证码
        if(!smsRPCService.checkCode(Phone, Code).isSuccess()){
            return new WebResDTO(WebResDTO.ERROR_CODE, "验证码错误");
        }
        //手机号登陆，如果第一次登陆则注册
        LoginDTO loginDTO = userPhoneLoginRPCService.loginByPhone(Phone);

        //返回Cookie
        String token = userRPCService.createToken(loginDTO.getUserId());

        Cookie cookie = new Cookie("tltk", token);

        cookie.setMaxAge(30*24*60*60);

        response.addCookie(cookie);

        return new WebResDTO(WebResDTO.SUCCESS_CODE, "登录成功");
    }
}
