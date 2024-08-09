package org.tl.live.Controller;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tl.live.enlity.WebResDTO;
import org.tl.live.inter.IIMRPCService;
import org.tl.live.service.IMTokenService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/im")
public class IMController {

    @Value("${tllive.im.imInstance}")
    private String imServerName;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private IMTokenService imTokenService;
    @DubboReference
    private IIMRPCService imRPCService;

    /**
     * 获取IM服务器
     * @return
     */
    @PostMapping("/getIMServer")
    public WebResDTO getIMServer(String userId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(imServerName);
        if (instances == null || instances.size() == 0) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "IM服务器不存在");
        }else {
            int random = ThreadLocalRandom.current().nextInt(0, instances.size());
            ServiceInstance serviceInstance = instances.get(random);
            var instanceUrl="ws://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/chat/"+userId;
            var imToken = imTokenService.generateIMToken(userId);
            Map<String ,Object > res = new HashMap<>();
            res.put("imToken",imToken);
            res.put("url",instanceUrl);
            return new WebResDTO(WebResDTO.SUCCESS_CODE,res);
        }
    }
    /**
     * 发送房间公告
     */
    @PostMapping("/sendRoomNotice")
    public WebResDTO sendRoomNotice(String roomId, String message) {
        if(imRPCService.publishNotice(roomId, message)){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,"发送成功");
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"发送失败");
    }
}
