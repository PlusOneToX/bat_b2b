package com.bat.thirdparty.mongodb.dao;

import com.bat.thirdparty.mongodb.dao.dataobject.OrderDeliverBillLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 沙漠
 */
public interface OrderDeliverBillLogDao extends MongoRepository<OrderDeliverBillLogDO, String> {

}
