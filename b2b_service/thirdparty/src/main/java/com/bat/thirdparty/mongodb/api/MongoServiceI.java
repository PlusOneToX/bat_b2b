package com.bat.thirdparty.mongodb.api;

import org.springframework.data.mongodb.core.MongoTemplate;

public interface MongoServiceI {

    void initIndex(MongoTemplate mongoTemplate);
}
