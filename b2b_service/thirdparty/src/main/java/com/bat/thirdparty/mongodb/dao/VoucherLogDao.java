package com.bat.thirdparty.mongodb.dao;

import com.bat.thirdparty.mongodb.dao.dataobject.VoucherLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 沙漠
 */
public interface VoucherLogDao extends MongoRepository<VoucherLogDO, String> {

}
