package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.DTO.UserDTO;
import org.tl.user.provider.entity.LiveRoom;
import org.tl.user.provider.mapper.LiveRoomMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiveService {
    @Resource
    private LiveRoomMapper liveRoomMapper;

    @Resource
    private UserService userService;

    @Value("${tllive.live.room.push_url}")
    public String push_url;
    @Value("${tllive.live.room.pull_url}")
    public String pull_url;

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
    public int initializeLiveRoom(LiveRoomDTO liveRoomDTO){
        //redis中查找
        //todo
        UserDTO user = userService.getUserById(liveRoomDTO.getUserId());
        if(user!=null){
            LiveRoom liveRoom = liveRoomMapper.selectByUserId(liveRoomDTO.getUserId());
            if(liveRoom!=null){
                return 0;
            }else{
                String stream_code= user.getNickName()+"-"+user.getUserId();
                String pull_url_rtmp=pull_url+user.getNickName()+user.getUserId()+".m3u8";
                liveRoomDTO.setPushUrl(push_url);
                liveRoomDTO.setStreamCode(stream_code);
                liveRoomDTO.setPullUrlRtmp(pull_url_rtmp);
                LiveRoom newLiveRoom =new LiveRoom();
                BeanUtils.copyProperties(liveRoomDTO,newLiveRoom);
                return liveRoomMapper.insertSelective(newLiveRoom);
            }
        }
        return 0;
    }
    public LiveRoomDTO getLiveRoomByUserId(Long userId){
        LiveRoom liveRoom = liveRoomMapper.selectByUserId(userId);
        if(liveRoom==null){
            return null;
        }
        LiveRoomDTO liveRoomDTO=new LiveRoomDTO();
        BeanUtils.copyProperties(liveRoom,liveRoomDTO);
        return liveRoomDTO;
    }
}
