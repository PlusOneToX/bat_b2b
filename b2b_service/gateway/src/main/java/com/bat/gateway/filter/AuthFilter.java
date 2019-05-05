package com.bat.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.gateway.common.JwtUtil;
import com.bat.gateway.nacos.GatewayConfig;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
public class AuthFilter implements GlobalFilter, Ordered {

    /**
     * 租户url缓存name
     */
    public static final String TENANT_URL_CACHE_6 = "TENANT_URL_CACHE_6:";

    private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Resource
    private GatewayConfig config;

    @CreateCache(name = TENANT_URL_CACHE_6)
    private Cache<String, List<String>> tenantUrlCache6;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse originalResponse = exchange.getResponse();
        URI uri = request.getURI();
        String path = uri.getPath();
        String tenantNo = null;

        // 非平台服务，初始化租户信息
        if (!path.contains(config.getPlatformURI()) && !path.contains(config.getSwaggerURI())) {
            String host = getForwardedHost(request);
            if (StringUtils.isBlank(host)) {
                host = uri.getHost();
            }
            System.out.print(host);
            List<String> tenantNos = tenantUrlCache6.get(host);
            if (CollectionUtils.isEmpty(tenantNos)) {
                return errorMessage(originalResponse, HttpStatus.OK, "B_TENANT_ERROR", "主机未配置租户信息");
            }
            tenantNo = request.getHeaders().getFirst("tenantNo");
            // 支付回调接口在路径上获取租户信息
            if (path.contains(config.getPayNotify()) || path.contains(config.getRefundNotify())) {
                String[] split = path.split("/");
                tenantNo = split[split.length - 1];
            }
            if (tenantNos.size() == 1) {
                // 如果参数中传了租户编码且在配置中也存在租户编码情况，需对比配置中的租户编码和参数中的租户编码是否一致
                if (StringUtils.isNotBlank(tenantNo) && !tenantNo.equals(tenantNos.get(0))) {
                    return errorMessage(originalResponse, HttpStatus.OK, "B_TENANT_ERROR", "租户编码不一致");
                } else if (StringUtils.isBlank(tenantNo)) {
                    tenantNo = tenantNos.get(0);
                }
            }
            if (StringUtils.isNotBlank(tenantNo)) {
                request = request.mutate().header("tenantNo", tenantNo).build();
            }
        }

        String token = request.getHeaders().getFirst("token");
        // 统一鉴权(排除无需鉴权URI) 前后台登录需组装新token
        if (config.getRegisterLoginURI().contains(path)) {
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            String finalTenantNo = tenantNo;
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // probably should reuse buffers
                            StringBuilder builder = new StringBuilder();
                            dataBuffers.forEach(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                // 释放掉内存
                                DataBufferUtils.release(dataBuffer);
                                builder.append(new String(content, Charset.forName("UTF-8")));
                            });
                            String responseData = builder.toString();
                            //将json字符串转换成jsonObject,解析用户信息，生存JWT token
                            JSONObject response = JSONObject.parseObject(responseData);
                            if (response.getBoolean("success")) {
                                JSONObject data = JSONObject.parseObject(responseData).getJSONObject("data");
                                Integer userId = data.getInteger("id");
                                String userName = data.getString("userName");
                                String password = data.getString("password");
                                JSONObject contact = data.getJSONObject("contact");
                                String contactId = null;
                                String contactName = null;
                                if (contact != null) {
                                    contactId = contact.getString("id");
                                    contactName = contact.getString("name");
                                    password = data.getString("password");
                                }
                                String token = JwtUtil.createToken(userId, userName, password, contactId, contactName, finalTenantNo);
                                this.getHeaders().set("token", token);
                                this.getHeaders().set("Access-Control-Expose-Headers", "token");
                            }
                            byte[] uppedContent = new String(responseData.getBytes(), Charset.forName("UTF-8")).getBytes();
                            return bufferFactory.wrap(uppedContent);
                        }));
                    }
                    // if body is not a flux. never got there.
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        } else if ((config.getOnLimitsURI().contains(path) || path.contains(config.getSwaggerURI())
                || path.contains(config.getPayNotify()) || path.contains(config.getRefundNotify())
                || path.contains(config.getThirdpartyOrderCancel()))
                && StringUtils.isBlank(token)) {
            // 无需鉴权验证
        } else {
            List<String> results = JwtUtil.verityAndUpdate(token, tenantNo);
            if (CollectionUtils.isEmpty(results)) {
                return errorMessage(originalResponse, HttpStatus.UNAUTHORIZED, "B_AUTH_FAIL", "用户信息已过期，请重新登录");
            } else {
                String userTenantNo = results.get(4);
                // 租户编码不为空情况需对比租户编码是否一致
                if (StringUtils.isNotBlank(userTenantNo) && StringUtils.isNotBlank(tenantNo) && !userTenantNo.equals(tenantNo)) {
                    System.out.print(tenantNo);
                    System.out.print(userTenantNo);
                    return errorMessage(originalResponse, HttpStatus.UNAUTHORIZED, "B_AUTH_FAIL", "用户校验失败，租户编码不一致，请重新登录");
                }
                //向headers中放文件，记得build
                try {
                    String userName = results.get(1);
                    if (!StringUtils.isBlank(userName)) {
                        userName = URLEncoder.encode(userName, "utf-8");
                    }
                    String contactName = results.get(3);
                    if (!StringUtils.isBlank(contactName)) {
                        contactName = URLEncoder.encode(contactName, "utf-8");
                    }
                    request = exchange.getRequest().mutate()
                            .header("userId", results.get(0))
                            .header("userName", userName)
                            .header("contactId", results.get(2))
                            .header("contactName", contactName).build();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                originalResponse.getHeaders().set("token", results.get(5));
                originalResponse.getHeaders().set("Access-Control-Expose-Headers", "token");
            }
        }
        return chain.filter(exchange.mutate().request(request).build());
    }

    /**
     * 错误信息
     *
     * @param originalResponse
     * @param statusCode
     * @param errCode
     * @param errMessage
     */
    private Mono<Void> errorMessage(ServerHttpResponse originalResponse, HttpStatus statusCode, String errCode, String errMessage) {
        originalResponse.setStatusCode(statusCode);
        JSONObject message = new JSONObject();
        message.put("success", false);
        message.put("errCode", errCode);
        message.put("errMessage", errMessage);
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = originalResponse.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        originalResponse.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return originalResponse.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -2;
    }

    /**
     * 获取真实地址(代理情况)
     *
     * @param request
     * @return
     */
    public static String getForwardedHost(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String host = headers.getFirst("X-Forwarded-Host");
        return host;
    }
}
