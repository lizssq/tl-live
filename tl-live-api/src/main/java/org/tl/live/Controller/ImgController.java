package org.tl.live.Controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
@RestController
public class ImgController {
    @GetMapping("/coverUrl")
    public void getCoverUrl(HttpServletResponse response, String coverUrl){
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
}
