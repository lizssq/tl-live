package org.tl.live.Controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.inter.ILiveRPCService;

import java.io.File;
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

    @PostMapping("/liveRoom")
    public WebResDTO addLiveRoom(Long userId, String title, String description,
                                 Integer categoryId, MultipartFile avatar) throws IOException {
        LiveRoomDTO liveRoomDTO=new LiveRoomDTO();
        if(userId!=null&&title!=null&&description!=null&& categoryId!=null&&!avatar.isEmpty()){
            if(liveRPCService.getLiveRoomByUserId(userId)!=null){
                return new WebResDTO(WebResDTO.ERROR_CODE,liveRPCService.getLiveRoomByUserId(userId));
            }
            liveRoomDTO.setTitle(title);
            liveRoomDTO.setDescription(description);
            liveRoomDTO.setUserId(userId);
            liveRoomDTO.setCategoryId(categoryId);
            String avatarType=avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf("."));
            String newAvatar= UUID.randomUUID()+avatarType;
            String cover_url="D:\\AAA\\" + newAvatar;
            liveRoomDTO.setCoverUrl(cover_url);
            liveRPCService.addLiveRoom(liveRoomDTO);

            avatar.transferTo(new File("D:\\AAA\\" + newAvatar));

            return new WebResDTO(WebResDTO.SUCCESS_CODE,liveRPCService.getLiveRoomByUserId(userId));
        }
        return new WebResDTO(WebResDTO.ERROR_CODE,"数据不完整");
    }
}
