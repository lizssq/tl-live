package org.tl.live.Controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.LiveRoomDTO;
import org.tl.user.inter.ILiveRPCService;

import java.util.List;

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
}
