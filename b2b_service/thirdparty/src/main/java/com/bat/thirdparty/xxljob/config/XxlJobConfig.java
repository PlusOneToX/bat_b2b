package com.bat.thirdparty.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Data
@Component
@Configuration
@RefreshScope
public class XxlJobConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;
    @Value("${xxl.job.accessToken}")
    private String accessToken;
    @Value("${xxl.job.executor.appname}")
    private String appname;
    @Value("${xxl.job.executor.address}")
    private String address;
    @Value("${xxl.job.executor.ip}")
    private String ip;
    @Value("${xxl.job.executor.port}")
    private int port;
    @Value("${xxl.job.executor.logpath}")
    private String logPath;
    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;
    @Value("${xxl.job.scheduleType}")
    private String scheduleType;
    @Value("${xxl.job.glueType}")
    private String glueType;
    @Value("${xxl.job.executorRouteStrategy}")
    private String executorRouteStrategy;
    @Value("${xxl.job.misfireStrategy}")
    private String misfireStrategy;
    @Value("${xxl.job.executorBlockStrategy}")
    private String executorBlockStrategy;
    @Value("${xxl.job.executorTimeout}")
    private Integer executorTimeout;
    @Value("${xxl.job.executorFailRetryCount}")
    private Integer executorFailRetryCount;
    @Value("${xxl.job.userName}")
    private String userName;
    @Value("${xxl.job.password}")
    private String password;
    @Value("${xxl.job.ifRemember}")
    private String ifRemember;


    @Value("${xxl.job.baseUri}")
    private String baseUri;
    @Value("${xxl.job.JOB_INFO_ADD_URI}")
    private String JOB_INFO_ADD_URI;
    @Value("${xxl.job.JOB_INFO_LIST_URI}")
    private String JOB_INFO_LIST_URI;
    @Value("${xxl.job.JOB_INFO_LOGIN_URI}")
    private String JOB_INFO_LOGIN_URI;
    @Value("${xxl.job.JOB_INFO_START_URI}")
    private String JOB_INFO_START_URI;

    @Value("${xxl.job.JOB_INFO_UPDATE_URI}")
    private String JOB_INFO_UPDATE_URI;

    @Value("${xxl.job.JOB_INFO_DELETE_URI}")
    private String JOB_INFO_DELETE_URI;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init." + logRetentionDays);
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     * 1、引入依赖： <dependency> <groupId>org.springframework.cloud</groupId> <artifactId>spring-cloud-commons</artifactId>
     * <version>${version}</version> </dependency>
     *
     * 2、配置文件，或者容器启动变量 spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     * 3、获取IP String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */

}