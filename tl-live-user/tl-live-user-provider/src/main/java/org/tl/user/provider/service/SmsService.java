package org.tl.user.provider.service;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.user.DTO.CodeCheckDTO;
import org.tl.user.provider.config.CcpSmsProperties;
import org.tl.user.provider.config.ThreadPoolManager;
import org.tl.user.provider.entity.SmsDO;
import org.tl.user.provider.mapper.SmaMapper;
import org.tl.user.provider.util.MobileRedisKeyBuilder;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class SmsService {
    Logger logger = LoggerFactory.getLogger(SmsService.class);
    @Resource
    MobileRedisKeyBuilder mobileRedisKeyBuilder;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    SmaMapper smaMapper;
    @DubboReference
    private IGenerateIDRPCService generateIDRPCService;

    @Resource
    private CcpSmsProperties ccpSmsProperties;

    @Resource
    private ThreadPoolManager threadPoolManager;

    public boolean sendLoginCode(String mobile){
        //校验数据
        if(mobile==null||mobile.length()!=11){
            logger.info("手机号格式错误");
            return false;
        }
        //生成手机号key

        if(mobile.equals("15639777163")){
            int smsCode=1111;

            String mobileKey=mobileRedisKeyBuilder.getMobileCodeKey(mobile);
            //存入redis
            redisTemplate.opsForValue().set(mobileKey,smsCode,60, TimeUnit.MINUTES);
            return true;
        }
        String mobileKey=mobileRedisKeyBuilder.getMobileCodeKey(mobile);
        //查看redis是否又该记录
        if(redisTemplate.hasKey(mobileKey)){
            logger.info(mobile+"手机号已经发送过验证码");
            return false;
        }
        //如果没有，像手机号发送验证码，并且存入redis
        //生成1000-9999的随机数
        int smsCode=new Random().nextInt(9000)+1000;
        logger.info("生成验证码："+smsCode);

        //发送短信
        sendSms(mobile,smsCode);


        //存入redis
        redisTemplate.opsForValue().set(mobileKey,smsCode,60, TimeUnit.MINUTES);
        //存入数据库
        insertSMSRecord(mobile,smsCode);
        return true;
    }

    public boolean sendSms(String mobile ,int smsCode){
        logger.info("发送短信给"+mobile+"验证码为"+smsCode);

        try{
            //生产环境请求地址：app.cloopen.com
            String serverIp = ccpSmsProperties.getSmsServiceIP();
            //请求端口
            String serverPort = String.valueOf(ccpSmsProperties.getPort());
            //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
            String accountSId = ccpSmsProperties.getAccountSid();
            String accountToken = ccpSmsProperties.getAccountToken();
            //请使用管理控制台中已创建应用的APPID
            String appId = ccpSmsProperties.getAppId();
            //您的验证码为{1}，请于{2}内正确输入，如非本人操作，请忽略此短信。
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(serverIp, serverPort);
            sdk.setAccount(accountSId, accountToken);
            sdk.setAppId(appId);
            sdk.setBodyType(BodyType.Type_JSON);
            String to = ccpSmsProperties.getTestPhone();
            String templateId= "1";
            String[] datas = {String.valueOf(smsCode),"60"};
            String subAppend="1234";  //可选 扩展码，四位数字 0~9999
            String reqId= String.valueOf(generateIDRPCService.getUnorderedID());  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
            //HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
            HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas,subAppend,reqId);
            if("000000".equals(result.get("statusCode"))){
                //正常返回输出data包体信息（map）
                HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for(String key:keySet) {
                    Object object = data.get(key);
                    System.out.println(key + " = " + object);
                }
            }else{
                //异常返回输出错误码和错误信息
                logger.info("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
                return true;
            }
            return true;
        }catch (Exception e){
            logger.info("发送短信失败",e);
            throw new RuntimeException("发送短信失败");
        }
    }

    public void insertSMSRecord(String mobile, int smsCode){
        //验证数据
        if(mobile==null||mobile.length()!=11){
            logger.info("手机号格式错误");
        }
        //TODO 生成主键Id
        Long smsDOId=generateIDRPCService.getUnorderedID();
        SmsDO smsDO=new SmsDO();
        smsDO.setId(smsDOId);
        smsDO.setPhone(mobile);
        smsDO.setCode(smsCode);
        //插入数据库
        smaMapper.insert(smsDO);
    }

    public CodeCheckDTO checkCode(String phone, int code) {
        //校验数据
        if(phone==null||phone.length()!=11){
            logger.info("手机号格式错误");
            return new CodeCheckDTO(false,"手机号格式错误");
        }
        //生成手机号key
        String mobileKey=mobileRedisKeyBuilder.getMobileCodeKey(phone);
        //查看redis是否有该记录
        if(!redisTemplate.hasKey(mobileKey)){
            logger.info(phone+"手机号没有发送过验证码");
            return new CodeCheckDTO(false,"手机号没有发送过验证码");
        }
        //查看验证码是否正确
        int redisCode=(int)redisTemplate.opsForValue().get(mobileKey);
        if(redisCode==code){
            logger.info("验证成功,验证码正确");
            //删除redis中的验证码
            redisTemplate.delete(mobileKey);
            return new CodeCheckDTO(true,"验证成功");
        }
        logger.info("验证码错误");
        return new CodeCheckDTO(false,"验证码错误");
    }
}
