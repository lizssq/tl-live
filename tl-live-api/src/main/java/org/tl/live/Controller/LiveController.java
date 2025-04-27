package org.tl.live.Controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.inter.ILiveRPCService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/live")
public class LiveController {
    @DubboReference
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

    @PutMapping("/liveRoom")
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

}
