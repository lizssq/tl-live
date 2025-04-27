package org.tl.user.provider.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tl.user.DTO.LiveCategoryDTO;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.DTO.UserDTO;
import org.tl.user.provider.entity.LiveCategory;
import org.tl.user.provider.entity.LiveRoom;
import org.tl.user.provider.mapper.LiveCategoryMapper;
import org.tl.user.provider.mapper.LiveRoomMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class LiveService {
    @Resource
    private LiveRoomMapper liveRoomMapper;

    @Resource
    private UserService userService;

    @Resource
    private LiveCategoryMapper liveCategoryMapper;

    @Value("${tllive.live.room.push_url}")
    public String push_url;
    @Value("${tllive.live.room.pull_url}")
    public String pull_url;

    Logger logger = LoggerFactory.getLogger(LiveService.class);

    //直播间
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
    public int updateLiveRoom(LiveRoomDTO liveRoomDTO){
        LiveRoom liveRoom=new LiveRoom();
        BeanUtils.copyProperties(liveRoomDTO,liveRoom);
        return liveRoomMapper.updateByPrimaryKeySelective(liveRoom);
    }
    public String getLiveRoomAvatar(Long roomId){
        return liveRoomMapper.selectAvatarByRoomId(roomId);
    }
    public LiveRoomDTO getLiveRoomByRoomId(Long roomId){
        LiveRoom liveRoom = liveRoomMapper.selectByPrimaryKey(roomId);
        if(liveRoom==null){
            return null;
        }
        LiveRoomDTO liveRoomDTO=new LiveRoomDTO();
        BeanUtils.copyProperties(liveRoom,liveRoomDTO);
        return liveRoomDTO;
    }
    public List<LiveRoomDTO> getLiveRoomByCategoryId(Integer categoryId){
        List<LiveRoom> liveRoomByCategoryId = liveRoomMapper.getLiveRoomByCategoryId(categoryId);
        List<LiveRoomDTO> dtoList=new ArrayList<>();
        for (LiveRoom liveRoom : liveRoomByCategoryId){
            LiveRoomDTO dto=new LiveRoomDTO();
            BeanUtils.copyProperties(liveRoom,dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    //直播分类
    public List<LiveCategoryDTO> getLiveCategoryDTO(){
        List<LiveCategoryDTO> list=new ArrayList<>();
        List<LiveCategory> liveCategories=liveCategoryMapper.selectList(null);
        for(LiveCategory l: liveCategories){
            LiveCategoryDTO liveCategoryDTO=new LiveCategoryDTO();
            BeanUtils.copyProperties(l,liveCategoryDTO);
            list.add(liveCategoryDTO);
        }
        return list;
    }

    public List<LiveCategoryDTO> getCategoryRoomCount(){
        List<LiveCategory> list = liveCategoryMapper.getCategoryRoomCount();
        List<LiveCategoryDTO> dtoList=new ArrayList<>();
        for (LiveCategory liveRoom : list){
            LiveCategoryDTO dto=new LiveCategoryDTO();
            BeanUtils.copyProperties(liveRoom,dto);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
