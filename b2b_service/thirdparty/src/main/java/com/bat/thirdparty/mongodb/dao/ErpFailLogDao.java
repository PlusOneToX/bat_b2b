package com.bat.thirdparty.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bat.thirdparty.mongodb.dao.dataobject.ErpFailLogDO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/8 20:42
 */
public interface ErpFailLogDao extends MongoRepository<ErpFailLogDO, String> {

}
