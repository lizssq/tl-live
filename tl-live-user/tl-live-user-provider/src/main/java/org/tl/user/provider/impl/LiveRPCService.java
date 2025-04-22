package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.inter.ILiveRPCService;
import org.tl.user.provider.service.LiveService;

import java.util.List;
@DubboService
public class LiveRPCService implements ILiveRPCService {
    @Resource
    private LiveService liveService;
    @Override
    public List<LiveRoomDTO> getAllLiveRoom() {
        return liveService.LiveRoom();
    }

    @Override
    public int addLiveRoom(LiveRoomDTO liveRoomDTO) {
        return liveService.initializeLiveRoom(liveRoomDTO);
    }

    @Override
    public LiveRoomDTO getLiveRoomByUserId(Long userId) {
        return liveService.getLiveRoomByUserId(userId);
    }
}
