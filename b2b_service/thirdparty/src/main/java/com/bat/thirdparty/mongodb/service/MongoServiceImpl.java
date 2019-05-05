package com.bat.thirdparty.mongodb.service;

import java.util.List;

import com.bat.thirdparty.mongodb.dao.dataobject.*;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.stereotype.Service;

import com.bat.thirdparty.mongodb.api.MongoServiceI;
import com.bat.thirdparty.mongodb.dao.dataobject.*;

@Service
public class MongoServiceImpl implements MongoServiceI {

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(MongoServiceImpl.class);

    @Override
    public void initIndex(MongoTemplate mongoTemplate) {
        Logger.info("判断mongoDB是否需要添加索引....");
        List<IndexInfo> indexInfos = null;
        try {
            indexInfos = mongoTemplate.indexOps(CommonLogDO.class).getIndexInfo();
            Logger.info("正在判断操作日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(CommonLogDO.class)
                    .ensureIndex(new Index().on("businessFunction", Sort.Direction.ASC));
                mongoTemplate.indexOps(CommonLogDO.class).ensureIndex(new Index().on("operator", Sort.Direction.ASC));
                mongoTemplate.indexOps(CommonLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(CommonLogDO.class)
                    .ensureIndex(new Index().on("operateType", Sort.Direction.ASC));
                mongoTemplate.indexOps(CommonLogDO.class).ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
                mongoTemplate.indexOps(CommonLogDO.class)
                    .ensureIndex(new Index().on("operateData", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加操作日志索引出现异常{}", e);
        }
        try {
            indexInfos = mongoTemplate.indexOps(CustomerLogDO.class).getIndexInfo();
            Logger.info("正在判断C端客户日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(CustomerLogDO.class)
                    .ensureIndex(new Index().on("customerId", Sort.Direction.ASC));
                mongoTemplate.indexOps(CustomerLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(CustomerLogDO.class)
                    .ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加C端客户日志索引出现异常{}", e);
        }
        try {
            indexInfos = mongoTemplate.indexOps(DistributorLogDO.class).getIndexInfo();
            Logger.info("正在判断分销商日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(DistributorLogDO.class)
                    .ensureIndex(new Index().on("distributorId", Sort.Direction.ASC));
                mongoTemplate.indexOps(DistributorLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(DistributorLogDO.class)
                    .ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加分销商日志索引出现异常{}", e);
        }
        try {
            indexInfos = mongoTemplate.indexOps(OrderDeliverBillLogDO.class).getIndexInfo();
            Logger.info("正在判断发货单日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(OrderDeliverBillLogDO.class)
                    .ensureIndex(new Index().on("orderDeliverBillId", Sort.Direction.ASC));
                mongoTemplate.indexOps(OrderDeliverBillLogDO.class)
                    .ensureIndex(new Index().on("orderId", Sort.Direction.ASC));
                mongoTemplate.indexOps(OrderDeliverBillLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(OrderDeliverBillLogDO.class)
                    .ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加发货单日志索引出现异常{}", e);
        }
        try {
            indexInfos = mongoTemplate.indexOps(OrderLogDO.class).getIndexInfo();
            Logger.info("正在判断订单日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(OrderLogDO.class).ensureIndex(new Index().on("orderId", Sort.Direction.ASC));
                mongoTemplate.indexOps(OrderLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(OrderLogDO.class).ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加订单日志索引出现异常{}", e);
        }
        try {
            indexInfos = mongoTemplate.indexOps(VoucherLogDO.class).getIndexInfo();
            Logger.info("正在判断收款单日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(VoucherLogDO.class).ensureIndex(new Index().on("voucherId", Sort.Direction.ASC));
                mongoTemplate.indexOps(VoucherLogDO.class).ensureIndex(new Index().on("orderId", Sort.Direction.ASC));
                mongoTemplate.indexOps(VoucherLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(VoucherLogDO.class)
                    .ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加收款单日志索引出现异常{}", e);
        }
        try {
            indexInfos = mongoTemplate.indexOps(RefundLogDO.class).getIndexInfo();
            Logger.info("正在判断退款单日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(RefundLogDO.class).ensureIndex(new Index().on("refundId", Sort.Direction.ASC));
                mongoTemplate.indexOps(RefundLogDO.class).ensureIndex(new Index().on("orderId", Sort.Direction.ASC));
                mongoTemplate.indexOps(RefundLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(RefundLogDO.class).ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加退款单日志索引出现异常{}", e);
        }
        try {
            indexInfos = mongoTemplate.indexOps(WithdrawApplyLogDO.class).getIndexInfo();
            Logger.info("正在判断分销客户提现申请日志是否需要添加索引....");
            if (indexInfos == null || indexInfos.size() <= 1) {
                Logger.info("索引添加中....");
                mongoTemplate.indexOps(WithdrawApplyLogDO.class)
                    .ensureIndex(new Index().on("withdrawApplyId", Sort.Direction.ASC));
                mongoTemplate.indexOps(WithdrawApplyLogDO.class)
                    .ensureIndex(new Index().on("distributorId", Sort.Direction.ASC));
                mongoTemplate.indexOps(WithdrawApplyLogDO.class)
                    .ensureIndex(new Index().on("operateTime", Sort.Direction.DESC));
                mongoTemplate.indexOps(WithdrawApplyLogDO.class)
                    .ensureIndex(new Index().on("operateDes", Sort.Direction.ASC));
            }
        } catch (Exception e) {
            Logger.error("添加分销客户提现申请日志索引出现异常{}", e);
        }
    }
}
