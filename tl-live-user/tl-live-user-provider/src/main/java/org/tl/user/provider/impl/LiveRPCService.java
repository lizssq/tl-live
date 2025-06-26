package org.tl.user.provider.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.tl.user.DTO.LiveCategoryDTO;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.DTO.PageResult;
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

    @Override
    public int updateLiveRoom(LiveRoomDTO liveRoomDTO) {
        return liveService.updateLiveRoom(liveRoomDTO);
    }

    @Override
    public String getLiveRoomAvatar(Long roomId) {
        return liveService.getLiveRoomAvatar(roomId);
    }

    @Override
    public List<LiveCategoryDTO> getLiveCategoryDTO() {
        return liveService.getLiveCategoryDTO();
    }

    @Override
    public List<LiveRoomDTO> getLiveRoomByCategoryId(Integer categoryId) {
        return liveService.getLiveRoomByCategoryId(categoryId);
    }

    @Override
    public List<LiveCategoryDTO> getCategoryRoomCount() {
        return liveService.getCategoryRoomCount();
    }

    @Override
    public PageResult<LiveRoomDTO> search(String keyword, Integer pageNum, Integer pageSize) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        PageResult<LiveRoomDTO> searchResults = liveService.search(keyword, pageNum, pageSize);
        if (searchResults != null) {
            return searchResults;
        } else {
            return null;
        }
    }

    @Override
    public LiveRoomDTO getLiveRoomByRoomId(Long roomId) {
        LiveRoomDTO liveRoom = liveService.getLiveRoomByRoomId(roomId);
        if (liveRoom != null) {
            return liveRoom;
        } else {
            return null;
        }
    }

    @Override
    public List<LiveRoomDTO> getRecommendLiveRoom() {
        return liveService.getRecommendLiveRoom();
    }

    @Override
    public List<LiveCategoryDTO> getCategoryDetail() {
        return liveService.getCategoryDetail();
    }

    @Override
    public int closeLiveRoom(Long roomId) {
        if (roomId == null) {
            return 0; // 返回0表示关闭失败
        }
        return liveService.closeLiveRoom(roomId);
    }

    @Override
    public LiveRoomDTO openLiveRoom(Long roomId) {
        if (roomId == null) {
            return null; // 返回null表示开启失败
        }
        LiveRoomDTO liveRoom = liveService.openLiveRoom(roomId);
        if (liveRoom != null) {
            return liveRoom;
        } else {
            return null; // 返回null表示开启失败
        }
    }
}
