package org.tl.live.Controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.LiveCategoryDTO;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.DTO.PageResult;
import org.tl.user.inter.ILiveRPCService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/live")
public class LiveController {
    @DubboReference(check = false)
    private ILiveRPCService liveRPCService;

    @GetMapping("/liveRoom")
    public WebResDTO allLiveRoom(){
        List<LiveRoomDTO> allLiveRoom = liveRPCService.getAllLiveRoom();
        return new WebResDTO(WebResDTO.SUCCESS_CODE,allLiveRoom);
    }

    @PostMapping("/coverUrl")
    public WebResDTO getCoverUrl(MultipartFile coverUrl) throws IOException {
        if(!coverUrl.isEmpty()){
            String avatarType=coverUrl.getOriginalFilename().substring(coverUrl.getOriginalFilename().lastIndexOf("."));
            String newAvatar= UUID.randomUUID()+avatarType;
            String cover_url="D:\\AAA\\" + newAvatar;
            coverUrl.transferTo(new File("D:\\AAA\\" + newAvatar));
            return new WebResDTO(WebResDTO.SUCCESS_CODE,cover_url);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"coverUrl为空");
    }

    @GetMapping("/coverUrl")
    public void getCoverUrl(HttpServletResponse response,String coverUrl){
        File file=new File(coverUrl);
        try(ServletOutputStream outputStream = response.getOutputStream();
            FileInputStream in=new FileInputStream(file)) {
            if(file.exists()){
                byte [] bytes=new byte[1024];
                int read = in.read(bytes);
                while (read!=-1){
                    outputStream.write(bytes);
                    read=in.read(bytes);
                }
            }
        }catch (Exception e){
            // 打印异常消息
            System.err.println("异常信息: " + e.getMessage());
            // 打印完整的堆栈信息
            e.printStackTrace();
        }
    }

    @PostMapping("/addLiveRoom")
    public WebResDTO addLiveRoom(@RequestBody LiveRoomDTO liveRoomDTO) throws IOException {
        if(liveRoomDTO.getUserId()!=null&&liveRoomDTO.getTitle()!=null&&liveRoomDTO.getDescription()!=null&& liveRoomDTO.getCategoryId()!=null&&!liveRoomDTO.getCoverUrl().isEmpty()){
            if(liveRPCService.getLiveRoomByUserId(liveRoomDTO.getUserId())!=null){
                return new WebResDTO(WebResDTO.ERROR_CODE,liveRPCService.getLiveRoomByUserId(liveRoomDTO.getUserId()));
            }
            liveRPCService.addLiveRoom(liveRoomDTO);
            return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRPCService.getLiveRoomByUserId(liveRoomDTO.getUserId()));
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"数据不完整");
    }

    @PostMapping("/liveRoom")
    public WebResDTO updateLiveRoom(@RequestBody LiveRoomDTO liveRoomDTO) throws IOException {
        if(liveRoomDTO.getRoomId()!=null&&liveRoomDTO.getUserId()!=null){
            LiveRoomDTO liveRoom = liveRPCService.getLiveRoomByUserId(liveRoomDTO.getUserId());
            if(liveRoom.getRoomId().equals(liveRoomDTO.getRoomId())){
                liveRPCService.updateLiveRoom(liveRoomDTO);
                return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRPCService.getLiveRoomByUserId(liveRoomDTO.getUserId()));
            }
            return new WebResDTO(WebResDTO.ERROR_CODE,"用户id与房间id不匹配");
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"信息不完整");
    }

    @GetMapping("/liveRoom/categoryId")
    public WebResDTO getLiveRoomByCategory( Integer categoryId){
        return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRPCService.getLiveRoomByCategoryId(categoryId));
    }
    @GetMapping("/category")
    public WebResDTO getCategory(){
        return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRPCService.getLiveCategoryDTO());
    }

    @GetMapping("/liveCategory")
    public WebResDTO getLiveCategory(){
        return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRPCService.getCategoryRoomCount());
    }

    @GetMapping("/liveRoom/{userId}")
    public WebResDTO getLiveRoomByUserId(@PathVariable Long userId){
        LiveRoomDTO liveRoom = liveRPCService.getLiveRoomByUserId(userId);
        if(liveRoom!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRoom);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"没有直播间");
    }

    //模糊查询
    @GetMapping("/search")
    public WebResDTO search(@RequestParam("keyword") String keyword,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "12") Integer pageSize) {
        if (keyword == null || keyword.isEmpty()) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "搜索关键字不能为空");
        }
        PageResult<LiveRoomDTO> searchResults = liveRPCService.search(keyword, pageNum, pageSize);
        if (searchResults != null) {
            return new WebResDTO(WebResDTO.SUCCESS_CODE, searchResults);
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "没有搜索结果");
        }
    }

    //通过房间id查找房间信息、
    @GetMapping("/roomId/{roomId}")
    public WebResDTO getLiveRoomByRoomId(@PathVariable Long roomId){
        LiveRoomDTO liveRoom = liveRPCService.getLiveRoomByRoomId(roomId);
        if(liveRoom!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRoom);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"没有直播间");
    }

    //获取推荐直播间
    @GetMapping("/recommend")
    public WebResDTO getRecommendLiveRoom(){
        List<LiveRoomDTO> recommendLiveRoom = liveRPCService.getRecommendLiveRoom();
        if(recommendLiveRoom!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,recommendLiveRoom);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"没有推荐直播间");
    }
    //获取直播间分类
    @GetMapping("/categoryDetail")
    public WebResDTO getCategoryDetail(){
        List<LiveCategoryDTO> liveRoomByCategoryId = liveRPCService.getCategoryDetail();
        if(liveRoomByCategoryId!=null){
            return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRoomByCategoryId);
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"没有直播间");
    }
    //关闭直播间
    @PostMapping("/closeLiveRoom")
    public WebResDTO closeLiveRoom(Long roomId, Long userId){
        if(roomId!=null&&userId!=null){
            LiveRoomDTO liveRoom = liveRPCService.getLiveRoomByUserId(userId);
            if(liveRoom!=null&&liveRoom.getRoomId().equals(roomId)){
                int i =liveRPCService.closeLiveRoom(roomId);
                if(i>0){
                    return new WebResDTO(WebResDTO.SUCCESS_CODE,"直播间已关闭");
                }
                return new WebResDTO(WebResDTO.ERROR_CODE,"关闭失败");
            }
            return new WebResDTO(WebResDTO.ERROR_CODE,"用户id与房间id不匹配");
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"信息不完整");
    }

    //开始直播
    @PostMapping("/startLive")
    public WebResDTO startLive(Long roomId, Long userId) {
        if (roomId != null && userId != null) {
            LiveRoomDTO liveRoom = liveRPCService.getLiveRoomByUserId(userId);
            if (liveRoom != null && liveRoom.getRoomId().equals(roomId)) {
                LiveRoomDTO liveRoomDTO =liveRPCService.openLiveRoom(roomId);
                if(liveRoomDTO == null){
                    return new WebResDTO(WebResDTO.SUCCESS_CODE,"直播间已关闭");
                }
                return new WebResDTO(WebResDTO.SUCCESS_CODE, liveRoomDTO);
            }
            return new WebResDTO(WebResDTO.ERROR_CODE, "用户id与房间id不匹配");
        }
        return new WebResDTO(WebResDTO.ERROR_CODE, "信息不完整");
    }
    //是否正在直播
    @GetMapping("/isLive")
    public WebResDTO isLive(Long userId) {
        if (userId == null) {
            return new WebResDTO(WebResDTO.ERROR_CODE, "用户ID不能为空");
        }
        LiveRoomDTO liveRoom = liveRPCService.getLiveRoomByUserId(userId);
        if (liveRoom != null) {
            if (liveRoom.getStatus() == 0) { // 假设0表示直播中
                return new WebResDTO(WebResDTO.SUCCESS_CODE, "正在直播");
            } else {
                return new WebResDTO(WebResDTO.ERROR_CODE, "未在直播中");
            }
        } else {
            return new WebResDTO(WebResDTO.ERROR_CODE, "没有找到直播间");
        }
    }

}
