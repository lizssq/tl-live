package org.tl.user.inter;

import org.tl.user.DTO.LiveCategoryDTO;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.DTO.PageResult;

import java.util.List;

public interface ILiveRPCService {
    List<LiveRoomDTO> getAllLiveRoom();

    int addLiveRoom(LiveRoomDTO liveRoomDTO);

    LiveRoomDTO getLiveRoomByUserId(Long userId);

    int updateLiveRoom (LiveRoomDTO liveRoomDTO);

    String getLiveRoomAvatar (Long roomId);

    public List<LiveCategoryDTO> getLiveCategoryDTO();

    public List<LiveRoomDTO> getLiveRoomByCategoryId(Integer categoryId);

    public List<LiveCategoryDTO> getCategoryRoomCount();


    PageResult<LiveRoomDTO> search(String keyword, Integer pageNum, Integer pageSize);

    LiveRoomDTO getLiveRoomByRoomId(Long roomId);

    List<LiveRoomDTO> getRecommendLiveRoom();

    List<LiveCategoryDTO> getCategoryDetail();

    int closeLiveRoom(Long roomId);

    LiveRoomDTO openLiveRoom(Long roomId);
}
