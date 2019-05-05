package com.bat.thirdparty.mongodb.executor;

import javax.annotation.Resource;

import com.bat.thirdparty.mongodb.MongoDbContext;
import com.bat.thirdparty.mongodb.dao.dataobject.OrderLogDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.thirdparty.mongodb.dto.OrderLogCmd;
import com.bat.thirdparty.mongodb.dao.OrderLogDao;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/9 9:02
 */
@Component
public class LogCmdExe {

    @Resource
    private OrderLogDao orderLogDao;

    @Resource
    private MongoDbContext mongoDbContext;

    /**
     * 创建订单操作日志
     * 
     * @param cmd
     */
    public void createOrderLog(OrderLogCmd cmd) {
        OrderLogDO orderLogDO = new OrderLogDO();
        BeanUtils.copyProperties(cmd, orderLogDO);
        mongoDbContext.getMongoDbFactory();
        orderLogDao.save(orderLogDO);
    }

}
