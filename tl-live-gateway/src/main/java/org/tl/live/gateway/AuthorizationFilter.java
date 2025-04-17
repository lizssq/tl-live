package org.tl.live.gateway;

import com.alibaba.nacos.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.cookie.Cookie;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.tl.live.commonStatusEunm.GatewayHeaderEnum;
import org.tl.user.inter.IUserRPCService;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.logging.Logger;

@Component
public class AuthorizationFilter implements Ordered, GlobalFilter {
    Logger logger = Logger.getLogger(AuthorizationFilter.class.getName());

    @DubboReference
    private IUserRPCService userRPCService;

    @Resource
    private GatewayAppProperties gatewayAppProperties;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest serverHttpRequest =exchange.getRequest();
        //获得请求url
        String url = exchange.getRequest().getURI().getPath();
        logger.info("请求url："+url);
        //判断是否需要拦截
        for(String whilteurl:gatewayAppProperties.getWhitelist()){
            if(url.startsWith(whilteurl)){
                logger.info("不需要拦截");
                return chain.filter(exchange);
            }
        }
        List<HttpCookie> cookies= serverHttpRequest.getCookies().get("tltk");
        if(cookies == null || cookies.isEmpty()){
            logger.info("tltk为空");
            return Mono.empty();
        }else{
            logger.info("!cookies == null || !cookies.size() == 0");
        }
        /*//湖区第一噶cookie
        String tltk = cookies.get(0).getValue();
        //是否为空*/

        // 从请求头中获取 tltk
        HttpHeaders headers = serverHttpRequest.getHeaders();
        List<String> tltkList = headers.get("tltk");
        if (tltkList == null || tltkList.isEmpty()) {
            logger.info("tltk 为空");
            return Mono.empty();
        }

        // 获取第一个 tltk 值
        String tltk = tltkList.get(0);
        if (!StringUtils.hasText(tltk)) {
            logger.info("tltk 为空");
            return Mono.empty();
        }

        String userId= userRPCService.checkToken(tltk);
        if(StringUtils.isEmpty(userId)){
            logger.info("tltk无效");
            return Mono.empty();
        }
        //将userId放入请求头
        ServerHttpRequest serverHttpRequest1 = exchange.getRequest().mutate().header(GatewayHeaderEnum.GATEWAY_UESR_NAME.getName(),userId).build();
        logger.info("tltk有效，userId："+userId);
        return chain.filter(exchange.mutate().request(serverHttpRequest1).build());
    }
    @Override
    public int getOrder() {
        return 0;
    }
}
