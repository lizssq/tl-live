package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.provider.entity.LiveRoom;
import org.tl.user.provider.mapper.LiveRoomMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiveService {
    @Resource
    private LiveRoomMapper liveRoomMapper;

    Logger logger = LoggerFactory.getLogger(LiveService.class);

    public List<LiveRoomDTO> LiveRoom(){
        List<LiveRoom> liveRooms = liveRoomMapper.selectList(null);
        List<LiveRoomDTO> dtoList=new ArrayList<>();
        for (LiveRoom liveRoom : liveRooms){
            LiveRoomDTO dto=new LiveRoomDTO();
            BeanUtils.copyProperties(liveRoom,dto);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
