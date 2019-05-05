package com.bat.thirdparty.mongodb;

import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoDatabase;
import com.bat.thirdparty.tenant.TenantContext;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/11/15 16:41
 */
public class MultiMongoTemplate extends MongoTemplate {

    public MultiMongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    protected MongoDatabase doGetDatabase() {
        MongoDatabaseFactory mongoDbFactory = TenantContext.currentMongoDFactory.get();
        return mongoDbFactory == null ? super.doGetDatabase() : mongoDbFactory.getMongoDatabase();
    }
}
