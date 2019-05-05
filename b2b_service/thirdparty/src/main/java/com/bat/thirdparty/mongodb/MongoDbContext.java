package com.bat.thirdparty.mongodb;

import static com.bat.thirdparty.common.CommonErrorCode.TENANT_NO_NULL;

import javax.annotation.Resource;

import com.bat.thirdparty.message.dto.TenantDBDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.db.service.executor.TenantDBQry;
import com.bat.thirdparty.mongodb.api.MongoServiceI;
import com.bat.thirdparty.tenant.Tenant;
import com.bat.thirdparty.tenant.TenantContext;

import lombok.extern.slf4j.Slf4j;

/**
 * 模块名称: 【MongoDB】动态MongoTemplate维护类 模块描述: 维护一份全局静态Map<String, MongoTemplate>,用于存储不同租户对应的MongoTemplate.
 * 每个请求进来,在AOP切面中,会根据tenantId,从该templateMap中取出该租户对应的MongoTemplate,绑定到该线程上下文中,供dao层调用
 * 
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/10/26 18:09
 */
@Slf4j
@Component
public class MongoDbContext {

    @Resource
    private TenantDBQry rpcQry;
    @Resource
    private MongoServiceI mongoService;

    // public void setMongoTemplate(MongoTemplate mongoTemplate) {
    // TenantContext.currentMongoTemplate.set(mongoTemplate);
    // }

    public void setMongoDbFactory(MongoDatabaseFactory mongoDbFactory) {
        TenantContext.currentMongoDFactory.set(mongoDbFactory);
    }

    // /**
    // * 获取指定租户id的mongoTemplate,如果租户id为空串或null（包括字符串null），则返回默认主库的mongoTemplate
    // *
    // * @param tenantNo
    // * 租户id
    // * @return
    // */
    // public MongoTemplate getMongoTemplate(String tenantNo) {
    // switchMongoTemplate(tenantNo);
    // return TenantContext.currentMongoTemplate.get();
    // }
    // /**
    // * 根据线程上下文TenantContext类中存储的tenantId，获取mongoTemplate的方法
    // *
    // * @return
    // */
    // public MongoTemplate getMongoTemplate() {
    // // 先给本线程切换mongoTemplate,切换后再返回
    // String tenantNo = TenantContext.getTenantNo();
    // switchMongoTemplate(tenantNo);
    // return TenantContext.currentMongoTemplate.get();
    // }

    /**
     * 根据线程上下文TenantContext类中存储的tenantId，获取MongoDbFactory的方法
     *
     * @return
     */
    public MongoDatabaseFactory getMongoDbFactory() {
        // 先给本线程切换mongoTemplate,切换后再返回
        String tenantNo = TenantContext.getTenantNo();
        switchMongoDbFactory(tenantNo);
        return TenantContext.currentMongoDFactory.get();
    }

    // public void removeMongoTemplate(TenantDBDTO dbdto) {
    // // 删除mogondb信息
    // Tenant tenant = TenantContext.tenantInfoMap.get(dbdto.getTenantNo());
    // if (StringUtils.isNotBlank(dbdto.getMongodbHost())) {
    // String mongodbUri = "mongodb://" + dbdto.getUserName() + ":" + dbdto.getPassword() + "@"
    // + dbdto.getMongodbHost() + ":" + dbdto.getMongodbPort() + "/" + dbdto.getMongodbDatabase();
    // TenantContext.mongoTemplateMap.remove(mongodbUri);
    // }
    // if (tenant.getConfigMap().size() == 1 && StringUtils.isNotBlank(tenant.getConfigMap().get("mongodbUri"))) {
    // TenantContext.tenantInfoMap.remove(tenant);
    // } else if (StringUtils.isNotBlank(tenant.getConfigMap().get("mongodbUri"))) {
    // tenant.getConfigMap().remove("mongodbUri");
    // }
    // }

    public void removeMongoDbFactory(TenantDBDTO dbdto) {
        // 删除mogondb信息
        Tenant tenant = TenantContext.tenantInfoMap.get(dbdto.getTenantNo());
        if (StringUtils.isNotBlank(dbdto.getMongodbHost())) {
            String mongodbUri = "mongodb://" + dbdto.getUserName() + ":" + dbdto.getPassword() + "@"
                + dbdto.getMongodbHost() + ":" + dbdto.getMongodbPort() + "/" + dbdto.getMongodbDatabase();
            TenantContext.mongoDFactoryMap.remove(mongodbUri);
        }
        if (tenant.getConfigMap().size() == 1 && StringUtils.isNotBlank(tenant.getConfigMap().get("mongodbUri"))) {
            TenantContext.tenantInfoMap.remove(tenant);
        } else if (StringUtils.isNotBlank(tenant.getConfigMap().get("mongodbUri"))) {
            tenant.getConfigMap().remove("mongodbUri");
        }
    }

    /**
     * 切换线程上下文中绑定的MongoTemplate为对应租户的方法
     *
     * @param tenantNo
     *            租户标识
     */
    // private void switchMongoTemplate(String tenantNo) {
    // if (StringUtils.isBlank(tenantNo) || "null".equals(tenantNo)) {
    // throw ThirdPartyException.buildException(TENANT_NO_NULL);
    // }
    // // 动态数据源上下文中备份了租户数据源信息,我们可以从中获取该租户mongodb_uri信息来创建出MongoTemplate
    // // 【防御机制】如果动态数据源中找不到该租户信息,则有可能是最近新添加的租户,重新初始化一下内存中的动态数据源后再试一次----------防御开始
    // Tenant tenant = TenantContext.tenantInfoMap.get(tenantNo);
    // if (tenant == null) {
    // tenant = new Tenant(tenantNo);
    // TenantContext.tenantInfoMap.put(tenantNo, tenant);
    // }
    // String mongodbUri = tenant.getConfigMap().get("mongodbUri");
    // if (StringUtils.isBlank(mongodbUri)) {
    // PlatformTenantDBRpcDTO tenantDB = rpcQry.getTenantMongoDb(TenantContext.getTenantNo());
    // mongodbUri = "mongodb://" + tenantDB.getUserName() + ":" + tenantDB.getPassword() + "@" + tenantDB.getHost()
    // + ":" + tenantDB.getPort() + "/" + tenantDB.getNosqlDatabase();
    // tenant.getConfigMap().put("mongodbUri", mongodbUri);
    // }
    // MongoTemplate mongoTemplate = TenantContext.mongoTemplateMap.get(mongodbUri);
    // // 如果为空,则创建该租户对应的MongoTemplate对象
    // if (mongoTemplate == null) {
    // mongoTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongodbUri));
    // // 备份到全局map中
    // mongoService.initIndex(mongoTemplate);
    // TenantContext.mongoTemplateMap.put(mongodbUri, mongoTemplate);
    // }
    // // 【防御机制】----------------------------------------------------------------------------------------------------防御结束
    // // 绑定到本线程上下文变量中,完成切换
    // setMongoTemplate(mongoTemplate);
    // }

    /**
     * 切换线程上下文中绑定的MongoTemplate为对应租户的方法
     *
     * 租户标识
     */
    private void switchMongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        MultiMongoTemplate mongoTemplate = new MultiMongoTemplate(mongoDatabaseFactory);
        // 备份到全局map中
        mongoService.initIndex(mongoTemplate);
    }

    /**
     * 切换线程上下文中绑定的MongoTemplate为对应租户的方法
     *
     * @param tenantNo
     *            租户标识
     */
    private void switchMongoDbFactory(String tenantNo) {
        if (StringUtils.isBlank(tenantNo) || "null".equals(tenantNo)) {
            throw ThirdPartyException.buildException(TENANT_NO_NULL);
        }
        // 动态数据源上下文中备份了租户数据源信息,我们可以从中获取该租户mongodb_uri信息来创建出MongoDbFactory
        // 【防御机制】如果动态数据源中找不到该租户信息,则有可能是最近新添加的租户,重新初始化一下内存中的动态数据源后再试一次----------防御开始
        Tenant tenant = TenantContext.tenantInfoMap.get(tenantNo);
        if (tenant == null) {
            tenant = new Tenant(tenantNo);
            TenantContext.tenantInfoMap.put(tenantNo, tenant);
        }
        String mongodbUri = tenant.getConfigMap().get("mongodbUri");
        if (StringUtils.isBlank(mongodbUri)) {
            PlatformTenantDBRpcDTO tenantDB = rpcQry.getTenantMongoDb(TenantContext.getTenantNo());
            mongodbUri = "mongodb://" + tenantDB.getUserName() + ":" + tenantDB.getPassword() + "@" + tenantDB.getHost()
                + ":" + tenantDB.getPort() + "/" + tenantDB.getNosqlDatabase();
            tenant.getConfigMap().put("mongodbUri", mongodbUri);
        }
        MongoDatabaseFactory mongoDbFactory = TenantContext.mongoDFactoryMap.get(mongodbUri);
        // 如果为空,则创建该租户对应的MongoTemplate对象
        if (mongoDbFactory == null) {
            mongoDbFactory = new SimpleMongoClientDatabaseFactory(mongodbUri);
            // 备份到全局map中
            switchMongoTemplate(mongoDbFactory);
            TenantContext.mongoDFactoryMap.put(mongodbUri, mongoDbFactory);
        }
        // 【防御机制】----------------------------------------------------------------------------------------------------防御结束
        // 绑定到本线程上下文变量中,完成切换
        setMongoDbFactory(mongoDbFactory);
    }
}
