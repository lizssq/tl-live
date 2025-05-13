package org.tl.live;

import jakarta.annotation.Resource;
import me.ahoo.cosid.IdGeneratorDecorator;
import me.ahoo.cosid.provider.IdGeneratorProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tl.live.id.inter.IGenerateIDRPCService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class idGenerateTest {
    @DubboReference
    private IGenerateIDRPCService generateIDRPCService;

    @Resource
    private IdGeneratorProvider idGeneratorProvider;
    @Test
    public void testGetSequentialID() {
        System.out.println(generateIDRPCService.getUnorderedID());
    }
}
