package org.tl.user.provider.mysql;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tl.live.id.inter.IGenerateIDRPCService;
import org.tl.user.provider.mapper.UserMapper;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MySqlTest {

    @Resource
    IGenerateIDRPCService generateIDRPCService;
    @Test
    public void testSms() {
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "2c94811c9035ff9f0190abe1af541b99";
        String accountToken = "1aec12a0e50c47f3ab6e8a877867cfe8";
        //请使用管理控制台中已创建应用的APPID
        String appId = "2c94811c9035ff9f0190abe1b0f21ba0";
        //您的验证码为{1}，请于{2}内正确输入，如非本人操作，请忽略此短信。
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String to = "15639777163";
        String templateId= "1";
        int code=new Random().nextInt(1000,9999);
        String[] datas = {String.valueOf(code),"60"};
        String subAppend="1234";  //可选 扩展码，四位数字 0~9999
        String reqId= String.valueOf(generateIDRPCService.getUnorderedID());  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
        //HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas,subAppend,reqId);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }

    }
}
