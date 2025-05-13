package org.tl.live.Controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tl.live.enlity.WebResDTO;
import org.tl.live.utils.FileUploadUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class ImgController {

    private static final String BASE_PATH = "D:/AAA/";
    @GetMapping("/img")
    public void getCoverUrl(HttpServletResponse response, String coverUrl){
        File file=new File(BASE_PATH+coverUrl);
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
    @PostMapping("/img")//post请求，发送了文件以及文件子地址？->文件上传并且返回新地址Result
    public WebResDTO uploadFile(String subPath,MultipartFile file) {
        try {
            String filePath = FileUploadUtil.uploadFile(subPath,file);
            return new WebResDTO(WebResDTO.SUCCESS_CODE, filePath);
        } catch (IOException | IllegalArgumentException e) {
            return new WebResDTO(WebResDTO.ERROR_CODE, e.getMessage());
        }
    }
}
