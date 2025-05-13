package org.tl.live.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {

    /**
     * 上传文件到static目录下的指定子路径
     * @param subPath static目录下的子路径，如 "images"、"uploads" 等
     * @param file 上传的文件
     * @return 返回文件的访问路径（相对static的路径）
     * @throws IOException
     */
    public static String uploadFile(String subPath, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 获取项目根目录

        // 构建static目录路径
        String staticPath = "D:/AAA/";

        // 如果传入了子路径，则添加到路径中
        if (StringUtils.hasText(subPath)) {
            staticPath += subPath + "/";
        }

        // 确保目录存在
        File uploadDir = new File(staticPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 获取原始文件名并处理
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("文件名包含非法路径序列: " + originalFilename);
        }

        // 生成新的文件名（避免重名）
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        // 构建完整的目标路径
        Path targetLocation = Paths.get(staticPath + newFilename);

        // 保存文件
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 返回相对static的访问路径
        return (StringUtils.hasText(subPath) ? subPath + "/" : "") + newFilename;
    }
}