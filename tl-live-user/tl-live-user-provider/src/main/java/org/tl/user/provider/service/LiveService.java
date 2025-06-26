package org.tl.user.provider.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tl.user.DTO.*;
import org.tl.user.provider.entity.LiveCategory;
import org.tl.user.provider.entity.LiveRoom;
import org.tl.user.provider.entity.UserProfile;
import org.tl.user.provider.mapper.LiveCategoryMapper;
import org.tl.user.provider.mapper.LiveRoomMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<LiveRoom> liveRooms = liveRoomMapper.selectLiveRoomList();
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
                String stream_code= user.getNickName()+user.getUserId();
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

    public PageResult<LiveRoomDTO> search(String keyword, Integer pageNum, Integer pageSize) {
        // 1. 获取用户列表
        PageHelper.startPage(pageNum, pageSize);
        List<LiveRoom> liveRooms = liveRoomMapper.search(keyword);
        if (liveRooms == null || liveRooms.isEmpty()) {
            return null; // 没有搜索结果
        }
        Page<LiveRoom> pageInfo = (Page<LiveRoom>) liveRooms;
        // 转换DTO列表
        List<LiveRoomDTO> dtos = liveRooms.stream().map(liveRoom -> {
            LiveRoomDTO dto = new LiveRoomDTO();
            BeanUtils.copyProperties(liveRoom, dto);
            return dto;
        }).collect(Collectors.toList());

        // 构建分页结果
        PageResult<LiveRoomDTO> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(dtos);
        return result;
    }

    public List<LiveRoomDTO> getRecommendLiveRoom() {
        List<LiveRoom> liveRooms = liveRoomMapper.getRecommendLiveRoom();
        if (liveRooms == null || liveRooms.isEmpty()) {
            return null; // 没有推荐直播间
        }
        List<LiveRoomDTO> dtos = liveRooms.stream().map(liveRoom -> {
            LiveRoomDTO dto = new LiveRoomDTO();
            BeanUtils.copyProperties(liveRoom, dto);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    public List<LiveCategoryDTO> getCategoryDetail() {
        List<LiveCategory> liveRooms = liveCategoryMapper.getCategoryDetail();
        if (liveRooms == null || liveRooms.isEmpty()) {
            return null; // 没有推荐直播间
        }
        List<LiveCategoryDTO> dtos = liveRooms.stream().map(liveRoom -> {
            LiveCategoryDTO dto = new LiveCategoryDTO();
            BeanUtils.copyProperties(liveRoom, dto);
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    public int closeLiveRoom(Long roomId) {
        if (roomId == null) {
            return 0; // 返回0表示关闭失败
        }
        LiveRoom liveRoom = liveRoomMapper.selectByPrimaryKey(roomId);
        if (liveRoom == null) {
            return 0; // 房间不存在
        }
        liveRoom.setStatus(1); // 假设1表示关闭状态
        return liveRoomMapper.updateByPrimaryKeySelective(liveRoom);
    }

    public LiveRoomDTO openLiveRoom(Long roomId) {
        if (roomId == null) {
            return null; // 返回null表示开启失败
        }
        LiveRoom liveRoom = liveRoomMapper.selectByPrimaryKey(roomId);
        if (liveRoom == null) {
            return null; // 房间不存在
        }
        liveRoom.setStatus(0); // 假设0表示开启状态
        int result = liveRoomMapper.updateByPrimaryKeySelective(liveRoom);
        if (result > 0) {
            LiveRoomDTO liveRoomDTO = new LiveRoomDTO();
            BeanUtils.copyProperties(liveRoom, liveRoomDTO);
            return liveRoomDTO;
        }
        return null; // 返回null表示开启失败
    }
}
