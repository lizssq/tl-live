package org.tl.live.config;

import io.netty.util.internal.StringUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.tl.common.redis.builder.IMCacheKeyBuilder;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Logger;

@Configuration
public class WebsocketFilter implements Filter {
    Logger logger = Logger.getLogger(WebsocketFilter.class.getName());
    @Resource
    RedisTemplate<String,Object> redisTemplate;
    @Resource
    IMCacheKeyBuilder imCacheKeyBuilder;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String token= httpServletRequest.getHeader("Sec-WebSocket-Protocol");
        logger.info("Sec-WebSocket-Protocol: "+token);
        System.out.println("Sec-WebSocket-Protocol: "+token);
        Map<String,String[]> map = httpServletRequest.getParameterMap();

        String imKey= imCacheKeyBuilder.getIMTokenKey(token);
        Object tokenCache= redisTemplate.opsForValue().get(imKey);

        map.forEach((k,v)->{
            System.out.println(k+" : "+v[0]);
        });
        if(tokenCache==null){
            httpServletResponse.sendError(401,"无效连接");
            return;
        }
        if(StringUtils.hasText(token)){
            System.out.println("Sec-WebSocket-Protocol: "+token);
            httpServletResponse.setHeader("Sec-WebSocket-Protocol", token);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
