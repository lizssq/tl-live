package org.tl.live.busi;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class test {
    @Resource
    RocketMQTemplate rocketMQTemplate;
    @Test
    public void test() {
        rocketMQTemplate.convertAndSend("chatMessage", "test");
        rocketMQTemplate.syncSendDelayTimeSeconds("chatMessage", "test", 10);
        System.out.println("test");
    }
}
