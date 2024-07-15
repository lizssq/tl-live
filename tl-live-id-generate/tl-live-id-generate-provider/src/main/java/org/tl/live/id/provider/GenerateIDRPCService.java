package org.tl.live.id.provider;

import jakarta.annotation.Resource;
import me.ahoo.cosid.provider.IdGeneratorProvider;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.tl.live.id.inter.IGenerateIDRPCService;

@DubboService
public class GenerateIDRPCService implements IGenerateIDRPCService {
    @Resource
    private IdGeneratorProvider idGeneratorProvider;
    @Override
    public Long getSequentialID() {
        return idGeneratorProvider.get("segmentID").get().generate();
    }

    @Override
    public Long getUnorderedID() {
        return idGeneratorProvider.get("snowflakeID").get().generate();
    }
}
