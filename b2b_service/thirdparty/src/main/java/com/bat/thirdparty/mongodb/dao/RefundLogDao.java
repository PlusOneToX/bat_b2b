package com.bat.thirdparty.mongodb.dao;

import com.bat.thirdparty.mongodb.dao.dataobject.RefundLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 沙漠
 */
public interface RefundLogDao extends MongoRepository<RefundLogDO, String> {

}
