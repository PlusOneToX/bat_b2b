package com.bat.thirdparty.mongodb.executor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.thirdparty.log.api.dto.*;
import com.bat.thirdparty.log.utils.MongoUtil;
import com.bat.thirdparty.log.utils.PageHelper;
import com.bat.thirdparty.mongodb.MongoDbContext;
import com.bat.thirdparty.mongodb.convertor.LogConvertor;
import com.bat.thirdparty.mongodb.dao.dataobject.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.thirdparty.mongodb.dto.data.OrderLogDTO;
import com.bat.thirdparty.log.api.dto.*;
import com.bat.thirdparty.mongodb.dao.OrderLogDao;
import com.bat.thirdparty.mongodb.dao.dataobject.*;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/7/9 9:02
 */
@Component
public class LogQryExe {

    @Resource
    private OrderLogDao orderLogDao;

    @Resource
    private MongoDbContext mongoTemplateService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private MongoUtil mongoUtil;

    @Resource
    private MongoDbContext mongoDbContext;

    /**
     * 根据订单id获取操作日志(不分页)
     * 
     * @param orderId
     * @return
     */
    public List<OrderLogDTO> findOrderLogByOrderId(Integer orderId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "operateTime");
        OrderLogDO logDO = new OrderLogDO();
        logDO.setOrderId(orderId);
        mongoDbContext.getMongoDbFactory();
        Example<OrderLogDO> example = Example.of(logDO);
        List<OrderLogDO> orderLogDOS = orderLogDao.findAll(example, sort);
        return LogConvertor.toOrderLogDTOList(orderLogDOS);
    }

    public PageHelper findPageCommonLogByParams(LogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getBusinessModule())) {
            criteria.and("businessModule").regex(".*?" + qry.getBusinessModule() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getBusinessFunction())) {
            criteria.and("businessFunction").regex(".*?" + qry.getBusinessFunction() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getOperateId() != null) {
            criteria.and("operateId").is(qry.getOperateId());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        long count = mongoTemplate.count(query, CommonLogDO.class);
        mongoUtil.start(qry.getPage(), qry.getSize(), query);
        List<CommonLogDO> commonLogDOS = mongoTemplate.find(query, CommonLogDO.class);
        PageHelper pageHelper = mongoUtil.pageHelper(count, commonLogDOS);
        return pageHelper;
    }

    public PageHelper findPageDistributorLogByParams(DistributorLogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getDistributorId() != null) {
            criteria.and("distributorId").is(qry.getDistributorId());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        long count = mongoTemplate.count(query, DistributorLogDO.class);
        mongoUtil.start(qry.getPage(), qry.getSize(), query);
        List<DistributorLogDO> distributorLogDOS = mongoTemplate.find(query, DistributorLogDO.class);
        PageHelper pageHelper = mongoUtil.pageHelper(count, distributorLogDOS);
        return pageHelper;
    }

    public PageHelper findPageOrderLogByParams(OrderLogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getOrderId() != null) {
            criteria.and("orderId").is(qry.getOrderId());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        long count = mongoTemplate.count(query, OrderLogDO.class);
        mongoUtil.start(qry.getPage(), qry.getSize(), query);
        List<OrderLogDO> orderLogDOS = mongoTemplate.find(query, OrderLogDO.class);
        PageHelper pageHelper = mongoUtil.pageHelper(count, orderLogDOS);
        return pageHelper;
    }

    public PageHelper findPageVoucherLogByParams(VoucherLogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getVoucherId() != null) {
            criteria.and("voucherId").is(qry.getVoucherId());
        }
        if (qry.getOrderId() != null) {
            criteria.and("orderId").is(qry.getOrderId());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        long count = mongoTemplate.count(query, VoucherLogDO.class);
        mongoUtil.start(qry.getPage(), qry.getSize(), query);
        List<VoucherLogDO> voucherLogDOS = mongoTemplate.find(query, VoucherLogDO.class);
        PageHelper pageHelper = mongoUtil.pageHelper(count, voucherLogDOS);
        return pageHelper;
    }

    public PageHelper findPageRefundLogByParams(RefundLogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getRefundId() != null) {
            criteria.and("refundId").is(qry.getRefundId());
        }
        if (qry.getOrderId() != null) {
            criteria.and("orderId").is(qry.getOrderId());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        long count = mongoTemplate.count(query, RefundLogDO.class);
        mongoUtil.start(qry.getPage(), qry.getSize(), query);
        List<RefundLogDO> refundLogDOS = mongoTemplate.find(query, RefundLogDO.class);
        PageHelper pageHelper = mongoUtil.pageHelper(count, refundLogDOS);
        return pageHelper;
    }

    public PageHelper findPageOrderDeliverBillLogByParams(OrderDeliverBillLogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getOrderId() != null) {
            criteria.and("orderId").is(qry.getOrderId());
        }
        if (qry.getOrderDeliverBillId() != null) {
            criteria.and("orderDeliverBillId").is(qry.getOrderDeliverBillId());
        }
        if (qry.getOrderDeliverBillIds() != null) {
            criteria.and("orderDeliverBillId").in(qry.getOrderDeliverBillIds());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        long count = mongoTemplate.count(query, OrderDeliverBillLogDO.class);
        mongoUtil.start(qry.getPage(), qry.getSize(), query);
        List<OrderDeliverBillLogDO> orderDeliverBillLogDOS = mongoTemplate.find(query, OrderDeliverBillLogDO.class);
        PageHelper pageHelper = mongoUtil.pageHelper(count, orderDeliverBillLogDOS);
        return pageHelper;
    }

    public List<OrderDeliverBillLogDO> findOrderDeliverBillLogByParams(OrderDeliverBillLogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getOrderId() != null) {
            criteria.and("orderId").is(qry.getOrderId());
        }
        if (qry.getOrderDeliverBillId() != null) {
            criteria.and("orderDeliverBillId").is(qry.getOrderDeliverBillId());
        }
        if (qry.getOrderDeliverBillIds() != null) {
            criteria.and("orderDeliverBillId").in(qry.getOrderDeliverBillIds());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        List<OrderDeliverBillLogDO> orderDeliverBillLogDOS = mongoTemplate.find(query, OrderDeliverBillLogDO.class);
        return orderDeliverBillLogDOS;
    }

    public PageHelper findPageWithdrawApplyLogByParams(WithdrawApplyLogQry qry) {
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(qry.getOperateSource())) {
            criteria.and("operateSource").regex(".*?" + qry.getOperateSource() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperator())) {
            criteria.and("operator").regex(".*?" + qry.getOperator() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateType())) {
            criteria.and("operateType").regex(".*?" + qry.getOperateType() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateDes())) {
            criteria.and("operateDes").regex(".*?" + qry.getOperateDes() + ".*?");
        }
        if (StringUtils.isNotBlank(qry.getOperateData())) {
            criteria.and("operateData").regex(".*?" + qry.getOperateData() + ".*?");
        }
        if (qry.getWithdrawApplyId() != null) {
            criteria.and("withdrawApplyId").is(qry.getWithdrawApplyId());
        }
        if (qry.getDistributorId() != null) {
            criteria.and("distributorId").is(qry.getDistributorId());
        }
        if (qry.getStartTime() != null && qry.getEndTime() != null) {
            Date endTime = new Date(qry.getEndTime().getTime() + 1000);
            criteria.andOperator(Criteria.where("operateTime").gte(qry.getStartTime()),
                Criteria.where("operateTime").lt(endTime));
        }
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Order.desc("operateTime")));
        mongoDbContext.getMongoDbFactory();
        long count = mongoTemplate.count(query, WithdrawApplyLogDO.class);
        mongoUtil.start(qry.getPage(), qry.getSize(), query);
        List<WithdrawApplyLogDO> withdrawApplyLogDOS = mongoTemplate.find(query, WithdrawApplyLogDO.class);
        PageHelper pageHelper = mongoUtil.pageHelper(count, withdrawApplyLogDOS);
        return pageHelper;
    }
}
