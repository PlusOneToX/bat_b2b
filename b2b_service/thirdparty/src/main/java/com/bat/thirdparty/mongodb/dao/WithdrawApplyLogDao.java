package com.bat.thirdparty.mongodb.dao;

import com.bat.thirdparty.mongodb.dao.dataobject.WithdrawApplyLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 沙漠
 */
public interface WithdrawApplyLogDao extends MongoRepository<WithdrawApplyLogDO, String> {

}
