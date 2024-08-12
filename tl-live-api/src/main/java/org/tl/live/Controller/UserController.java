package org.tl.live.Controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tl.live.enlity.WebResDTO;
import org.tl.user.DTO.UserDTO;
import org.slf4j.Logger;
import org.tl.user.inter.IUserRPCService;

import static org.tl.live.enlity.WebResDTO.ERROR_CODE;
import static org.tl.live.enlity.WebResDTO.SUCCESS_CODE;


@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @DubboReference(check = false)
    private IUserRPCService userRPCService;

    @RequestMapping("/getUser")
    public WebResDTO getUserById(String userId) {
            if (StringUtils.isEmpty(userId)) {
                return new WebResDTO(ERROR_CODE, "userId不能为空");
            }
        logger.info("userId:{}，nikeName:{}", userId, userRPCService.getUserById(Long.valueOf(userId)).getNickName());
            return new WebResDTO(SUCCESS_CODE, userRPCService.getUserById(Long.valueOf(userId)));
    }
}
