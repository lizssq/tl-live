package org.tl.live.gateway;

import com.alibaba.nacos.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.cookie.Cookie;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
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
public class AuthorizationFilter implements Order, GlobalFilter {
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
        List<HttpCookie> cookies= serverHttpRequest.getCookies().get("titk");
        if(cookies == null || cookies.size() == 0){
            logger.info("titk为空");
            return Mono.empty();
        }
        //湖区第一噶cookie
        String titk = cookies.get(0).getValue();
        //是否为空
        if(!StringUtils.hasText(titk)){
            logger.info("titk为空");
            return Mono.empty();
        }
        String userId= userRPCService.checkToken(titk);
        if(StringUtils.isEmpty(userId)){
            logger.info("titk无效");
            return Mono.empty();
        }
        //将userId放入请求头
        ServerHttpRequest serverHttpRequest1 = exchange.getRequest().mutate().header(GatewayHeaderEnum.GATEWAY_UESR_NAME.getName(),userId).build();
        logger.info("titk有效，userId："+userId);
        return chain.filter(exchange.mutate().request(serverHttpRequest1).build());

    }

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
