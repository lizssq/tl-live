package org.tl.live.id.inter;

public interface IGenerateIDRPCService {
    //有序id
    Long getSequentialID();

    //无序id
    Long getUnorderedID();
}
