package org.tl.user.provider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tl.user.provider.config.IdCertConfig;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class RealNameAuthService {

    @Autowired
    private IdCertConfig idCertConfig;

    /**
     * 实名认证方法
     * @param idCard 身份证号
     * @param name 姓名
     * @return 认证结果代码
     *         200: 认证成功
     *         -1: URL格式错误
     *         -2: 主机地址错误
     *         -3: 其他网络错误
     *         -400: 参数错误
     *         -403: 授权或配额错误
     *         其他负HTTP状态码: 对应API错误
     */
    public int realNameAuthentication(String idCard, String name) {
        try {
            // 构建请求URL
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
            String urlStr = idCertConfig.getHost() + idCertConfig.getPath() 
                    + "?idCard=" + idCard + "&name=" + encodedName;
            
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "APPCODE " + idCertConfig.getAppcode());
            
            int httpCode = connection.getResponseCode();
            if (httpCode == HttpURLConnection.HTTP_OK) {
                // 认证成功，读取返回的JSON
                String json = readResponse(connection.getInputStream());
                // 这里假设返回的JSON中有code字段，实际应根据API文档解析
                return 200; // 认证成功
            } else {
                // 处理错误情况
                Map<String, List<String>> headers = connection.getHeaderFields();
                String errorMsg = headers.getOrDefault("X-Ca-Error-Message", List.of("Unknown error")).get(0);
                
                // 根据不同的错误类型返回不同的代码
                if (httpCode == 400) {
                    if (errorMsg.contains("Invalid AppCode")) {
                        return -403; // AppCode错误
                    } else if (errorMsg.contains("Invalid Param")) {
                        return -400; // 参数错误
                    }
                } else if (httpCode == 403) {
                    if (errorMsg.contains("Quota Exhausted")) {
                        return -403; // 配额用完
                    }
                }
                
                return -httpCode; // 返回负的HTTP状态码
            }
        } catch (MalformedURLException e) {
            return -1; // URL格式错误
        } catch (UnknownHostException e) {
            return -2; // 主机地址错误
        } catch (Exception e) {
            return -3; // 其他错误
        }
    }

    /**
     * 读取HTTP响应内容
     */
    private String readResponse(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
}