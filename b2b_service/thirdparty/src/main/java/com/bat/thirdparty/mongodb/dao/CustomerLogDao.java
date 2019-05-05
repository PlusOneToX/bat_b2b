package com.bat.thirdparty.mongodb.dao;

import com.bat.thirdparty.mongodb.dao.dataobject.CustomerLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 20:42
 */
public interface CustomerLogDao extends MongoRepository<CustomerLogDO, String> {

}
