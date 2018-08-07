package com.bat.distributor.service.distributor.executor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.subaccount.DistributorSubAccountAdminConfigServiceI;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.mq.dto.SalesmanDTO;
import com.bat.distributor.service.common.*;
import com.bat.distributor.tenant.TenantContext;
import com.bat.distributor.service.Message.MessageSendService;
import com.bat.distributor.service.distributor.convertor.DistributorCheckConvertor;
import com.bat.distributor.service.distributor.convertor.DistributorConvertor;
import com.bat.distributor.service.distributor.validator.DistributorValidator;
import com.bat.distributor.service.subaccount.validator.SubAccountValidator;
import com.bat.dubboapi.promotion.dto.data.PromotionDistributorRpcDTO;
import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.dubboapi.system.check.dto.data.CheckConfigRpcDTO;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.bat.distributor.service.common.Constant.*;

@Slf4j
@Component
public class DistributorCmdExe {

    @Resource
    private DistributorMapper mapper;
    @Resource
    private DistributorSalesAreaMapper distributorSalesAreaMapper;
    @Resource
    private DistributorAddressMapper distributorAddressMapper;
    @Resource
    private DistributorBusinessMapper distributorBusinessMapper;
    @Resource
    private DistributorContactsMapper distributorContactsMapper;
    @Resource
    private DistributorExtendDataMapper distributorExtendDataMapper;
    @Resource
    private DistributorFinancialMapper distributorFinancialMapper;
    @Resource
    private DistributorSpecialGoodsMapper distributorSpecialGoodsMapper;
    @Resource
    private DistributorOneScalePriceMapper oneScalePriceMapper;
    @Resource
    private DistributorNextScalePriceMapper distributorNextScalePriceMapper;
    @Resource
    private DistributorRpcCmdExe rpcCmdExe;
    @Resource
    private MessageSendService sendService;
    @Resource
    private DistributorValidator distributorValidator;

    @Resource
    private CommonRpcExe commonRpcExe;
    @Resource
    private DistributorGoodsRelevanceMapper distributorGoodsRelevanceMapper;
    @Resource
    private DistributorPromotionRelevanceMapper distributorPromotionRelevanceMapper;
    @Resource
    private DistributorGroupSeckillRelevanceMapper distributorGroupSeckillRelevanceMapper;
    @Resource
    private DistributorBrandRelevanceMapper distributorBrandRelevanceMapper;
    @Resource
    private DistributorCheckMapper distributorCheckMapper;
    @Resource
    private DistributorCheckFlowMapper distributorCheckFlowMapper;
    @Resource
    private DistributorCheckConvertor checkConvertor;

    @Autowired
    private DistributorSubAccountAdminConfigServiceI distributorSubAccountAdminConfigServiceI;

    /**
     * 后台创建分销商
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createDistributor(DistributorCmd cmd, String userId, String userName) {
        if (StringUtils.isNotBlank(cmd.getBusiness().getDistributorGroupIds())) {
            cmd.getBusiness().setDistributorGroupIds("," + cmd.getBusiness().getDistributorGroupIds() + ",");
        }
        Date date = new Date(System.currentTimeMillis());
        distributorValidator.checkDistributor(cmd);
        // 分销商资料是否需要审批
        CheckConfigDetailRpcDTO detailRpcDTO = rpcCmdExe.getCheckFlows(DISTRIBUTOR_CHECK);
        DistributorDO distributorDO = DistributorConvertor.toDistributorDo(cmd, detailRpcDTO.getOpenFlag(), date);
        // 校验处理分账参数
        SubAccountValidator.validateAdminConfigParam(cmd.getExtendData(), cmd.getSubAccountAdminConfigCmd());
        String operateDes = "新增成功";
        boolean isFail = false;
        try {
            mapper.insert(distributorDO);
        } catch (DuplicateKeyException e) {
            operateDes = "创建失败！用户名已存在，请使用其他名称";
            isFail = true;
        }
        if (isFail) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_NAME_ERROR);
        }
        saveDistributorData(cmd, distributorDO, userId, userName);
        // 判断分销商是否开启资料审批
        if (distributorDO.getProfileStatus().equals(PROFILE_STATUS_1)) {
            distributorCheck(distributorDO.getId(), detailRpcDTO, date, userId, userName);
            operateDes = "提交成功";
            sendService.distributorLogPackage(distributorDO.getId(), "提交新增分销商审批", operateDes,
                    JSONObject.toJSONString(distributorDO));
        } else {
            // 更新可视范围
            if (distributorDO.getTreeNode() == 1) {
                updateDistributorVisible(distributorDO.getId(), distributorDO.getScalePriceDOS(),
                        distributorDO.getBusinessDO(), cmd.getBusiness().getScalePriceId());
            }
            // 发送同步erp消息
            if (cmd.getExtendData().getErpFlag().equals(ERP_FLAG_1)) {
                sendService.distributorSyncErp(distributorDO.getId());
            }
            sendService.distributorLogPackage(distributorDO.getId(), "新增分销商", operateDes,
                    JSONObject.toJSONString(distributorDO));
        }
        //发送分销商审核信息
        sendService.distributorExamineMsg(distributorDO.getId());
    }

    /**
     * 分销商新增，创建分销商审批
     *
     * @param distributorId
     */
    private void distributorCheck(Integer distributorId, CheckConfigDetailRpcDTO detailRpcDTO, Date date, String userId,
                                  String userName) {
        List<CheckConfigRpcDTO> checkFlows = detailRpcDTO.getCheckConfigs();
        if (CollectionUtils.isEmpty(checkFlows)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_FLOW_NULL);
        }
        DistributorCheckDO checkDO =
                DistributorConvertor.toDistributorCheckDO(checkFlows, distributorId, date, userId, userName);
        distributorCheckMapper.insert(checkDO);
        List<DistributorCheckFlowDO> checkFlowDOS =
                DistributorConvertor.toDistributorCheckFlowDOList(checkFlows, checkDO.getId(), date);
        distributorCheckFlowMapper.insertList(checkFlowDOS);
    }

    /**
     * 保存分销商相关数据
     *
     * @param cmd
     * @param distributorDO
     */
    private void saveDistributorData(DistributorCmd cmd, DistributorDO distributorDO, String userId, String userName) {
        Integer distributorId = distributorDO.getId();
        // 销售区域
        List<Integer> salesAreaIds = cmd.getSalesAreaIds();
        if (!CollectionUtils.isEmpty(salesAreaIds)) {
            List<DistributorSalesAreaDO> doList =
                    DistributorConvertor.toDistributorSalesAreaDOList(salesAreaIds, distributorId);
            distributorSalesAreaMapper.insertList(doList);
        }
        // 分销商地址
        DistributorAddressCmd address = cmd.getAddress();
        if (address != null) {
            DistributorAddressDO addressDO = DistributorConvertor.toDistributorAddressDo(address, distributorId);
            distributorAddressMapper.insert(addressDO);
        }
        // 分销商业务信息
        DistributorBusinessCmd business = cmd.getBusiness();
        if (business != null) {
            DistributorBusinessDO businessDO = DistributorConvertor.toDistributorBusinessDo(business, distributorId);
            distributorBusinessMapper.insert(businessDO);
            distributorDO.setBusinessDO(businessDO);
        }
        // 分销商价格等级信息
        if (cmd.getBusiness() != null && cmd.getBusiness().getScalePriceId() != null) {
            DistributorOneScalePriceCmd defaultScalePrice = new DistributorOneScalePriceCmd();
            defaultScalePrice.setOperationType(OPERATION_TYPE_1);
            defaultScalePrice.setBrandId(0);
            defaultScalePrice.setCategoryId(0);
            defaultScalePrice.setScalePriceId(cmd.getBusiness().getScalePriceId());
            defaultScalePrice.setDistributionScalePriceId(cmd.getBusiness().getDistributionScalePriceId());
            List<DistributorOneScalePriceCmd> scalePrices = cmd.getScalePrices();
            if (scalePrices == null) {
                scalePrices = new ArrayList<>();
                cmd.setScalePrices(scalePrices);
            }
            cmd.getScalePrices().add(defaultScalePrice);
        }
        List<DistributorOneScalePriceCmd> scalePrices = cmd.getScalePrices();
        List<DistributorOneScalePriceDO> scalePriceDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scalePrices)) {
            scalePriceDOS = DistributorConvertor.toDistributorOneScalePriceDOList(scalePrices, distributorId);
            oneScalePriceMapper.insertList(scalePriceDOS);
            distributorDO.setScalePriceDOS(scalePriceDOS);
        }
        // 分销商联系人
        List<DistributorContactsCmd> contacts = cmd.getContacts();
        if (!CollectionUtils.isEmpty(contacts)) {
            List<DistributorContactsDO> distributorContactsDOS =
                    DistributorConvertor.toDistributorContactsDOList(contacts, distributorId);
            try {
                distributorContactsMapper.insertList(distributorContactsDOS);
            } catch (DuplicateKeyException e) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR);
            }
        }
        // 分销商扩展信息
        DistributorExtendDataCmd extendData = cmd.getExtendData();
        if (extendData != null) {
            DistributorExtendDataDO extendDataDO =
                    DistributorConvertor.toDistributorExtendDataDO(extendData, distributorId);
            distributorExtendDataMapper.insert(extendDataDO);
        }
        // 分销商财务信息
        DistributorFinancialCmd financial = cmd.getFinancial();
        if (financial != null) {
            DistributorFinancialDO financialDO =
                    DistributorConvertor.toDistributorFinancialDO(financial, distributorId);
            distributorFinancialMapper.insert(financialDO);
        }
        // 分销商特价商品信息
        List<DistributorSpecialGoodsCmd> specialGoods = cmd.getSpecialGoods();
        List<DistributorSpecialGoodsDO> specialGoodsDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(specialGoods)) {
            specialGoodsDOS = DistributorConvertor.toDistributorSpecialGoodsDOList(specialGoods, distributorId);
            distributorSpecialGoodsMapper.insertList(specialGoodsDOS);
        }
        // 处理分账
        if (DistributorConstant.DISTRIBUTOR_CUSTOMER_FLAG_YES.equals(cmd.getExtendData().getCustomerFlag())
                && DistributorConstant.DISTRIBUTOR_CUSTOMER_MODE_SELF.equals(cmd.getExtendData().getCustomerMode())
                && DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_YES.equals(cmd.getExtendData().getSubAccountFlag())) {
            cmd.getSubAccountAdminConfigCmd().setDistributorId(distributorDO.getId());
            distributorSubAccountAdminConfigServiceI.create(cmd.getSubAccountAdminConfigCmd(), userId, userName);
        }
    }

    /**
     * 更新分销商可视范围
     *
     * @param distributorId
     * @param scalePriceDOS
     * @param defaultScalePriceId
     */
    private void updateDistributorVisible(Integer distributorId, List<DistributorOneScalePriceDO> scalePriceDOS,
                                          DistributorBusinessDO businessDO, Integer defaultScalePriceId) {
        // 更新品牌可视范围(指定分销商情况)
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            rpcCmdExe.brandDistributorRelevance(scalePriceDOS, distributorId);
        }
        if (businessDO != null) {
            Integer salesId = businessDO.getSalesId();
            // 更新商品可视范围
            UserRpcDTO userRpcDTO = commonRpcExe.getUserById(salesId);
            updateVisibleGoodsIds(distributorId, scalePriceDOS, salesId, userRpcDTO.getDepartmentId(),
                    businessDO.getDistributorGroupIds());
            // 更新活动可视范围
            updateVisiblePromotion(distributorId, defaultScalePriceId, salesId, userRpcDTO.getDepartmentId());
        }
    }

    /**
     * 更新活动可视范围
     *
     * @param distributorId
     * @param scalePriceId
     * @param salesId
     * @param departmentId
     */
    public void updateVisiblePromotion(Integer distributorId, Integer scalePriceId, Integer salesId,
                                       Integer departmentId) {
        PromotionDistributorRpcDTO rpcDTO =
                rpcCmdExe.updateVisiblePromotionIdsByDistributor(distributorId, scalePriceId, salesId, departmentId);
        // 促销活动可视范围
        List<Integer> promotionIds = rpcDTO.getPromotionIds();
        DistributorPromotionRelevanceDO promotionRelevanceDO =
                distributorPromotionRelevanceMapper.selectByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(promotionIds)) {
            String join = Joiner.on(",").join(promotionIds);
            join = "," + join + ",";
            if (promotionRelevanceDO == null) {
                promotionRelevanceDO = new DistributorPromotionRelevanceDO();
                promotionRelevanceDO.setPromotionIds(join);
                promotionRelevanceDO.setDistributorId(distributorId);
                distributorPromotionRelevanceMapper.insert(promotionRelevanceDO);
            } else {
                promotionRelevanceDO.setPromotionIds(join);
                distributorPromotionRelevanceMapper.updateByPrimaryKey(promotionRelevanceDO);
            }
        } else if (promotionRelevanceDO != null) {
            distributorPromotionRelevanceMapper.deleteByDistributorId(distributorId);
        }
        // 拼团秒杀可视范围
        List<Integer> groupSeckillIds = rpcDTO.getGroupSeckillIds();
        DistributorGroupSeckillRelevanceDO groupSeckillRelevanceDO =
                distributorGroupSeckillRelevanceMapper.selectByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(groupSeckillIds)) {
            String join = Joiner.on(",").join(groupSeckillIds);
            join = "," + join + ",";
            if (groupSeckillRelevanceDO == null) {
                groupSeckillRelevanceDO = new DistributorGroupSeckillRelevanceDO();
                groupSeckillRelevanceDO.setGroupSeckillIds(join);
                groupSeckillRelevanceDO.setDistributorId(distributorId);
                distributorGroupSeckillRelevanceMapper.insert(groupSeckillRelevanceDO);
            } else {
                groupSeckillRelevanceDO.setGroupSeckillIds(join);
                distributorGroupSeckillRelevanceMapper.updateByPrimaryKey(groupSeckillRelevanceDO);
            }
        } else if (groupSeckillRelevanceDO != null) {
            distributorGroupSeckillRelevanceMapper.deleteByDistributorId(distributorId);
        }
    }

    /**
     * 更新商品可视范围
     *
     * @param distributorId
     * @param scalePriceDOS
     * @param salesId
     */
    public void updateVisibleGoodsIds(Integer distributorId, List<DistributorOneScalePriceDO> scalePriceDOS,
                                      Integer salesId, Integer departmentId, String distributorGroupIds) {
        List<DistributorOneScalePriceDO> oneScalePriceDOS = scalePriceDOS.stream()
                .filter(scalePriceDO -> scalePriceDO.getBrandId() != null && scalePriceDO.getBrandId() != 0
                        && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0))
                .collect(Collectors.toList());
        List<Integer> goodsIds = rpcCmdExe.updateVisibleGoodsIdsByDistributor(distributorId, oneScalePriceDOS, salesId,
                departmentId, distributorGroupIds);
        DistributorGoodsRelevanceDO goodsRelevanceDO =
                distributorGoodsRelevanceMapper.selectByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(goodsIds)) {
            String join = Joiner.on(",").join(goodsIds);
            join = "," + join + ",";
            if (goodsRelevanceDO == null) {
                goodsRelevanceDO = new DistributorGoodsRelevanceDO();
                goodsRelevanceDO.setGoodsIds(join);
                goodsRelevanceDO.setDistributorId(distributorId);
                distributorGoodsRelevanceMapper.insert(goodsRelevanceDO);
            } else {
                goodsRelevanceDO.setGoodsIds(join);
                distributorGoodsRelevanceMapper.updateByPrimaryKey(goodsRelevanceDO);
            }
        } else if (goodsRelevanceDO != null) {
            distributorGoodsRelevanceMapper.deleteByDistributorId(distributorId);
        }
    }

    /**
     * 更新分销商
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDistributor(DistributorUpdateCmd cmd, String userId, String userName) throws Exception {
        if (StringUtils.isNotBlank(cmd.getBusiness().getDistributorGroupIds())) {
            cmd.getBusiness().setDistributorGroupIds("," + cmd.getBusiness().getDistributorGroupIds() + ",");
        }
        DistributorDO beforeDistributorDO = mapper.selectByPrimaryKey(cmd.getId());
        if (beforeDistributorDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_NULL);
        }
        // 检查数据
        CommonValidator.checkDistributorValidator(beforeDistributorDO, cmd);
        Map<String, List<List<Object>>> changeMap = new HashMap<>();
        Date date = new Date(System.currentTimeMillis());
        DistributorDO afterDistributorDO = checkDistributorDO(beforeDistributorDO, cmd, date, changeMap);
        // 更新分销商销售区域
        updateDistributorSalesArea(afterDistributorDO, cmd, changeMap);
        // 更新分销商地址
        updateDistributorAddress(afterDistributorDO, cmd, date, changeMap);
        // 更新分销商业务数据
        updateDistributorBusiness(afterDistributorDO, cmd, date, changeMap);
        // 更新分销商业务数据价格等级
        updateDistributorOneScalePrice(afterDistributorDO, cmd, changeMap);
        // 更新分销商联系人
        updateDistributorContact(afterDistributorDO, cmd, date, changeMap);
        // 更新分销商扩展数据
        updateDistributorExtendData(afterDistributorDO, cmd, date, changeMap);
        // 更新分销财务数据
        updateDistributorFinancial(afterDistributorDO, cmd, date, changeMap);
        // 更新分销商商品特价
        updateDistributorSpecialGoods(afterDistributorDO, cmd, date, changeMap);
        // 处理分账
        if (DistributorConstant.DISTRIBUTOR_CUSTOMER_FLAG_YES.equals(cmd.getExtendData().getCustomerFlag())
                && DistributorConstant.DISTRIBUTOR_CUSTOMER_MODE_SELF.equals(cmd.getExtendData().getCustomerMode())
                && DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_YES.equals(cmd.getExtendData().getSubAccountFlag())) {
            cmd.getSubAccountAdminConfigCmd().setDistributorId(beforeDistributorDO.getId());
            distributorSubAccountAdminConfigServiceI.update(cmd.getSubAccountAdminConfigCmd(), userId, userName);
        }
        // 变更内容不为空时，说明分销商资料有变更
        if (!CollectionUtils.isEmpty(changeMap)) {
            // 判断是否有一个审批单存在
            DistributorCheckDO checkDO =
                    distributorCheckMapper.selectCheckIngByDistributorId(afterDistributorDO.getId());
            if (checkDO != null) {
                throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_EXIT_ERROR,
                        MessageUtils.get(ErrorCode.B_DISTRIBUTOR_CHECK_EXIT_ERROR) + checkDO.getCheckUserName());
            }
            CheckConfigDetailRpcDTO detailRpcDTO = rpcCmdExe.getCheckFlows(DISTRIBUTOR_CHECK);
            Short openFlag = detailRpcDTO.getOpenFlag();
            // 分销商变更是否需要审批
            if (openFlag.equals(OPEN_FLAG_1)) {
                // 前台申请的分销商第一次同意时需特殊处理（资质申请状态为同意，资料审核状态为审核中）
                if ((beforeDistributorDO.getApplyStatus() != null
                        && beforeDistributorDO.getApplyStatus().equals(APPLY_STATUS_1))
                        || beforeDistributorDO.getProfileStatus().equals(PROFILE_STATUS_3)) {
                    // 前台申请的分销商，资质通过后先保存资料，资料审核状态改为审核中
                    afterDistributorDO.setProfileStatus(PROFILE_STATUS_1);
                    // 前台申请的分销商，重置申请审批时间为空
                    if (beforeDistributorDO.getApplyType().equals(APPLY_TYPE_2)) {
                        afterDistributorDO.setCheckTime(null);
                    }
                    updateDistributor(afterDistributorDO, changeMap);
                    distributorCheck(afterDistributorDO.getId(), detailRpcDTO, date, userId, userName);
                } else {
                    distributorCheck(afterDistributorDO.getId(), detailRpcDTO, changeMap, date, userId, userName);
                }
                sendService.distributorLogPackage(afterDistributorDO.getId(), "提交更新分销商审批", "提交成功",
                        JSONObject.toJSONString(cmd));
            } else {
                // 分销商不需要审批，直接更新
                updateDistributor(afterDistributorDO, changeMap);
                // 一级分销商更新可视范围
                if (afterDistributorDO.getTreeNode() == 1) {
                    updateDistributorVisible(afterDistributorDO.getId(), afterDistributorDO.getScalePriceDOS(),
                            afterDistributorDO.getBusinessDO(), cmd.getBusiness().getScalePriceId());
                }

                //更新一级分销商的所有子分销商的销售区域
                updatingSalesTerritoriesOfSubDistributors(cmd.getId(),cmd.getSalesAreaIds());

                // 发送同步erp消息
                DistributorExtendDataDO extendDataDO = afterDistributorDO.getAfterExtendDataDO();
                if (extendDataDO == null) {
                    extendDataDO = afterDistributorDO.getBeforeExtendDataDO();
                }
                if (extendDataDO.getErpFlag() != null && extendDataDO.getErpFlag().equals(ERP_FLAG_1)) {
                    sendService.distributorSyncErp(cmd.getId());
                }
                sendService.distributorLogPackage(afterDistributorDO.getId(), "更新分销商", "更新成功",
                        JSONObject.toJSONString(cmd));
            }
        }
    }

    /**
     * <h2>修改指定分销商的所有下级分销商的销售区域</h2>
     * @param distributorId
     * @param salesAreaIds
     */
    public void updatingSalesTerritoriesOfSubDistributors(Integer distributorId, List<Integer> salesAreaIds) {

        //根据一级分销商查询所有的下级分销商
        List<Integer> subDistributorIdList=mapper.queryAllSubDistributors(distributorId);
        if(!CollectionUtils.isEmpty(subDistributorIdList)){
            //将该分销商的所有下级分销商的销售区全部删了
            mapper.deleteSalesTerritoryByDistributorIdList(subDistributorIdList);
            //将新的销售区域插入进去
            for (Integer areaId:salesAreaIds) {
                mapper.inserDistributorSalesArea(subDistributorIdList,areaId);
            }
        }
    }

    /**
     * 分销商新增，编辑分销商审批
     *
     * @param distributorId
     */
    private void distributorCheck(Integer distributorId, CheckConfigDetailRpcDTO detailRpcDTO,
                                  Map<String, List<List<Object>>> changeMap, Date date, String userId, String userName) {
        List<CheckConfigRpcDTO> checkFlows = detailRpcDTO.getCheckConfigs();
        if (CollectionUtils.isEmpty(checkFlows)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_FLOW_NULL);
        }
        DistributorCheckDO checkDO =
                DistributorConvertor.toDistributorCheckDO(checkFlows, distributorId, changeMap, date, userId, userName);
        distributorCheckMapper.insert(checkDO);
        List<DistributorCheckFlowDO> checkFlowDOS =
                DistributorConvertor.toDistributorCheckFlowDOList(checkFlows, checkDO.getId(), date);
        distributorCheckFlowMapper.insertList(checkFlowDOS);
    }

    /**
     * 更新分销商信息(包含分销商所有信息)
     *
     * @param distributorDO
     */
    public void updateDistributor(DistributorDO distributorDO, Map<String, List<List<Object>>> changeMap) {
        Integer distributorId = distributorDO.getId();
        boolean isFail = false;
        try {
            mapper.updateByPrimaryKey(distributorDO);
        } catch (DuplicateKeyException e) {
            String operateDes = "更新失败！用户名已存在，请使用其他名称";
            sendService.distributorLogPackage(distributorId, "更新分销商", operateDes, JSONObject.toJSONString(changeMap));
            isFail = true;
        }
        if (isFail) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_NAME_ERROR);
        }
        // 销售区域更新
        List<List<Object>> salesAreas = changeMap.get("salesAreaIds");
        if (salesAreas != null) {
            distributorSalesAreaMapper.deleteByDistributorId(distributorId);
            List<DistributorSalesAreaDO> salesAreaDOS = distributorDO.getSalesAreaDOS();
            if (!CollectionUtils.isEmpty(salesAreaDOS)) {
                distributorSalesAreaMapper.insertList(salesAreaDOS);
            }
        }
        // 地址更新
        DistributorAddressDO afterAddressDO = distributorDO.getAfterAddressDO();
        if (afterAddressDO != null) {
            if (afterAddressDO.getId() == null) {
                distributorAddressMapper.insert(afterAddressDO);
            } else {
                distributorAddressMapper.updateByPrimaryKey(afterAddressDO);
            }
        }
        // 更新分销商业务数据
        DistributorBusinessDO afterBusinessDO = distributorDO.getBusinessDO();
        if (afterBusinessDO != null) {
            if (afterBusinessDO.getId() == null) {
                distributorBusinessMapper.insert(afterBusinessDO);
            } else {
                distributorBusinessMapper.updateByPrimaryKey(afterBusinessDO);
            }
        }
        // 更新分销商价格等级
        List<DistributorOneScalePriceDO> addScalePriceDOS = distributorDO.getAddScalePriceDOS();
        if (!CollectionUtils.isEmpty(addScalePriceDOS)) {
            oneScalePriceMapper.insertList(addScalePriceDOS);
        }
        List<DistributorOneScalePriceDO> updateScalePriceDOS = distributorDO.getUpdateScalePriceDOS();
        if (!CollectionUtils.isEmpty(updateScalePriceDOS)) {
            oneScalePriceMapper.updateList(updateScalePriceDOS);
        }
        List<DistributorOneScalePriceDO> deleteScalePriceDOS = distributorDO.getDeleteScalePriceDOS();
        if (!CollectionUtils.isEmpty(deleteScalePriceDOS)) {
            List<Integer> deleteIds =
                    deleteScalePriceDOS.stream().map(DistributorOneScalePriceDO::getId).collect(Collectors.toList());
            oneScalePriceMapper.deleteByIds(deleteIds);
        }
        // 更新分销商联系人
        List<DistributorContactsDO> addContacts = distributorDO.getAddContacts();
        if (!CollectionUtils.isEmpty(addContacts)) {
            try {
                distributorContactsMapper.insertList(addContacts);
            } catch (DuplicateKeyException e) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR);
            }
        }
        List<DistributorContactsDO> updateContacts = distributorDO.getUpdateContacts();
        if (!CollectionUtils.isEmpty(updateContacts)) {
            try {
                distributorContactsMapper.updateList(updateContacts);
            } catch (DuplicateKeyException e) {
                throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_PHONE_EMAIL_ERROR);
            }
        }
        List<DistributorContactsDO> deleteContacts = distributorDO.getDeleteContacts();
        if (!CollectionUtils.isEmpty(deleteContacts)) {
            List<Integer> deleteIds =
                    deleteContacts.stream().map(DistributorContactsDO::getId).collect(Collectors.toList());
            distributorContactsMapper.deleteByIds(deleteIds);
        }
        // 更新分销商扩展数据
        DistributorExtendDataDO afterExtendDataDO = distributorDO.getAfterExtendDataDO();
        if (afterExtendDataDO != null) {
            if (afterExtendDataDO.getId() == null) {
                distributorExtendDataMapper.insert(afterExtendDataDO);
            } else {
                distributorExtendDataMapper.updateByPrimaryKey(afterExtendDataDO);
            }
        }
        // 更新分销财务数据
        DistributorFinancialDO afterFinancialDO = distributorDO.getAfterFinancialDO();
        if (afterFinancialDO != null) {
            if (afterFinancialDO.getId() == null) {
                distributorFinancialMapper.insert(afterFinancialDO);
            } else {
                distributorFinancialMapper.updateByPrimaryKey(afterFinancialDO);
            }
        }
        // 更新分销商商品特价
        List<DistributorSpecialGoodsDO> addSpecialGoodsDOS = distributorDO.getAddSpecialGoodsDOS();
        if (!CollectionUtils.isEmpty(addSpecialGoodsDOS)) {
            distributorSpecialGoodsMapper.insertList(addSpecialGoodsDOS);
        }
        List<DistributorSpecialGoodsDO> updateSpecialGoodsDOS = distributorDO.getUpdateSpecialGoodsDOS();
        if (!CollectionUtils.isEmpty(updateSpecialGoodsDOS)) {
            distributorSpecialGoodsMapper.updateList(updateSpecialGoodsDOS);
        }
        List<DistributorSpecialGoodsDO> deleteSpecialGoodsDOS = distributorDO.getDeleteSpecialGoodsDOS();
        if (!CollectionUtils.isEmpty(deleteSpecialGoodsDOS)) {
            List<Integer> deleteIds =
                    deleteSpecialGoodsDOS.stream().map(DistributorSpecialGoodsDO::getId).collect(Collectors.toList());
            distributorSpecialGoodsMapper.deleteByIds(deleteIds);
        }
    }

    /**
     * 根据分销商id拒绝申请中的分销商
     *
     * @param cmd
     */
    public void refuse(DistributorId cmd) {
        Date time = new Date(System.currentTimeMillis());
        mapper.updateApplyStatusById(cmd.getId(), APPLY_STATUS_3, time);
        sendService.distributorLogPackage(cmd.getId(), "拒绝申请中的分销商", "拒绝成功", JSONObject.toJSONString(cmd));
    }

    /**
     * 更新分销商价格等级
     *
     * @param cmd
     */
    private void updateDistributorOneScalePrice(DistributorDO distributorDO, DistributorUpdateCmd cmd,
                                                Map<String, List<List<Object>>> changeMap) {
        // 多级分销不支持价格等级设置
        if (distributorDO.getTreeNode() > 1) {
            return;
        }
        List<DistributorOneScalePriceDO> scalePriceDOS = new ArrayList<>();
        if (cmd.getBusiness() != null && cmd.getBusiness().getScalePriceId() != null) {
            List<DistributorOneScalePriceDO> oneScalePriceDOS = oneScalePriceMapper.listByDistributorId(cmd.getId());
            if (!CollectionUtils.isEmpty(oneScalePriceDOS)) {
                scalePriceDOS.addAll(oneScalePriceDOS);
            }
            List<DistributorOneScalePriceCmd> scalePrices = cmd.getScalePrices();
            if (scalePrices == null) {
                scalePrices = new ArrayList<>();
                cmd.setScalePrices(scalePrices);
            }
            Optional<DistributorOneScalePriceDO> optional = scalePriceDOS.stream()
                    .filter(scalePriceDO -> (scalePriceDO.getBrandId() == null || scalePriceDO.getBrandId() == 0)
                            && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0))
                    .findFirst();
            DistributorOneScalePriceCmd defaultScalePriceCmd = new DistributorOneScalePriceCmd();
            defaultScalePriceCmd.setBrandId(0);
            defaultScalePriceCmd.setCategoryId(0);
            defaultScalePriceCmd.setScalePriceId(cmd.getBusiness().getScalePriceId());
            defaultScalePriceCmd.setDistributionScalePriceId(cmd.getBusiness().getDistributionScalePriceId());
            if (optional != null && optional.isPresent()) {
                DistributorOneScalePriceDO defaultScalePriceDO = optional.get();
                if (!defaultScalePriceDO.getScalePriceId().equals(cmd.getBusiness().getScalePriceId())
                        || (defaultScalePriceDO.getDistributionScalePriceId() == null
                        && cmd.getBusiness().getDistributionScalePriceId() != null)
                        || (defaultScalePriceDO.getDistributionScalePriceId() != null
                        && cmd.getBusiness().getDistributionScalePriceId() == null)
                        || ((defaultScalePriceDO.getDistributionScalePriceId() != null
                        && cmd.getBusiness().getDistributionScalePriceId() != null)
                        && !defaultScalePriceDO.getDistributionScalePriceId()
                        .equals(cmd.getBusiness().getDistributionScalePriceId()))) {
                    defaultScalePriceCmd.setOperationType(OPERATION_TYPE_2);
                    defaultScalePriceCmd.setId(defaultScalePriceDO.getId());
                    scalePrices.add(defaultScalePriceCmd);
                }
            } else {
                defaultScalePriceCmd.setOperationType(OPERATION_TYPE_1);
                scalePrices.add(defaultScalePriceCmd);
            }
        }
        Integer distributorId = cmd.getId();
        List<DistributorOneScalePriceCmd> scalePrices = cmd.getScalePrices();
        List<DistributorOneScalePriceDO> addOneScalePriceDOS = new ArrayList<>();
        List<DistributorOneScalePriceDO> updateOneScalePriceDOS = new ArrayList<>();
        List<DistributorOneScalePriceDO> deleteOneScalePriceDOS = new ArrayList<>();
        distributorDO.setAddScalePriceDOS(addOneScalePriceDOS);
        distributorDO.setUpdateScalePriceDOS(updateOneScalePriceDOS);
        distributorDO.setDeleteScalePriceDOS(deleteOneScalePriceDOS);
        List<Integer> deleteIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scalePrices)) {
            scalePrices.forEach(scalePrice -> {
                if (scalePrice.getOperationType().equals(OPERATION_TYPE_1)) {
                    DistributorOneScalePriceDO scalePriceDO =
                            DistributorConvertor.toDistributorOneScalePriceDO(scalePrice, distributorId);
                    scalePriceDO.setOperationType(OPERATION_TYPE_1);
                    scalePriceDOS.add(scalePriceDO);
                    addOneScalePriceDOS.add(scalePriceDO);
                    changeMap(changeMap, "scalePriceAdd", null, scalePriceDO);
                }
                if (scalePrice.getOperationType().equals(OPERATION_TYPE_2)) {
                    Optional<DistributorOneScalePriceDO> first = scalePriceDOS.stream()
                            .filter(scalePriceDO -> scalePriceDO.getId().equals(scalePrice.getId())).findFirst();
                    if (first == null || !first.isPresent()) {
                        throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_SCALE_PRICE_NULL);
                    }
                    DistributorOneScalePriceDO beforeScalePriceDO = first.get();
                    DistributorOneScalePriceDO afterScalePriceDO =
                            DistributorConvertor.toDistributorOneScalePriceDO(scalePrice, distributorId);
                    afterScalePriceDO.setId(beforeScalePriceDO.getId());
                    afterScalePriceDO.setOperationType(OPERATION_TYPE_2);
                    updateOneScalePriceDOS.add(afterScalePriceDO);
                    deleteIds.add(beforeScalePriceDO.getId());
                    changeMap(changeMap, "scalePriceUpdate", beforeScalePriceDO, afterScalePriceDO);
                }
                if (scalePrice.getOperationType().equals(OPERATION_TYPE_3)) {
                    deleteIds.add(scalePrice.getId());
                    DistributorOneScalePriceDO deleteOneScalePriceDO = scalePriceDOS.stream()
                            .filter(scalePriceDO -> scalePriceDO.getId().equals(scalePrice.getId())).findFirst().get();
                    deleteOneScalePriceDO.setOperationType(OPERATION_TYPE_3);
                    deleteOneScalePriceDOS.add(deleteOneScalePriceDO);
                    changeMap(changeMap, "scalePriceDelete", deleteOneScalePriceDO, null);
                }
            });
        }
        // 更新后的品牌等级列表
        List<DistributorOneScalePriceDO> updateScalePriceDOS = new ArrayList<>();
        distributorDO.setScalePriceDOS(updateScalePriceDOS);
        if (!CollectionUtils.isEmpty(deleteIds)) {
            List<DistributorOneScalePriceDO> oneScalePriceDOS = scalePriceDOS.stream()
                    .filter(scalePriceDO -> scalePriceDO.getId() == null
                            || (scalePriceDO.getId() != null && !deleteIds.contains(scalePriceDO.getId())))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(oneScalePriceDOS)) {
                updateScalePriceDOS.addAll(oneScalePriceDOS);
            }
        } else {
            updateScalePriceDOS.addAll(scalePriceDOS);
        }
        if (!CollectionUtils.isEmpty(updateOneScalePriceDOS)) {
            updateScalePriceDOS.addAll(updateOneScalePriceDOS);
        }
    }

    /**
     * 变更数据
     *
     * @param changeMap
     * @param key
     * @param before
     * @param after
     */
    private void changeMap(Map<String, List<List<Object>>> changeMap, String key, Object before, Object after) {
        List<List<Object>> vss = changeMap.get(key);
        if (vss == null) {
            vss = new ArrayList<>();
            changeMap.put(key, vss);
        }
        List<Object> changeList = new ArrayList<>();
        changeList.add(before);
        changeList.add(after);
        vss.add(changeList);
    }

    /**
     * 更新特价商品
     *
     * @param cmd
     * @param date
     */
    private void updateDistributorSpecialGoods(DistributorDO distributorDO, DistributorUpdateCmd cmd, Date date,
                                               Map<String, List<List<Object>>> changeMap) {
        if (distributorDO.getTreeNode() > 1) {
            return;
        }
        Integer distributorId = cmd.getId();
        List<DistributorSpecialGoodsCmd> specialGoods = cmd.getSpecialGoods();
        List<DistributorSpecialGoodsDO> addSpecialGoodsDOs = new ArrayList<>();
        List<DistributorSpecialGoodsDO> updateSpecialGoodsDOs = new ArrayList<>();
        List<DistributorSpecialGoodsDO> deleteSpecialGoodsDOs = new ArrayList<>();
        distributorDO.setAddSpecialGoodsDOS(addSpecialGoodsDOs);
        distributorDO.setUpdateSpecialGoodsDOS(updateSpecialGoodsDOs);
        distributorDO.setDeleteSpecialGoodsDOS(deleteSpecialGoodsDOs);
        if (!CollectionUtils.isEmpty(specialGoods)) {
            specialGoods.forEach(goodsCmd -> {
                if (goodsCmd.getOperationType().equals(OPERATION_TYPE_1)) {
                    DistributorSpecialGoodsDO specialGoodsDO =
                            DistributorConvertor.toDistributorSpecialGoodsDO(goodsCmd, distributorId);
                    addSpecialGoodsDOs.add(specialGoodsDO);
                    changeMap(changeMap, "specialGoodsAdd", null, specialGoodsDO);
                }
                if (goodsCmd.getOperationType().equals(OPERATION_TYPE_2)) {
                    DistributorSpecialGoodsDO beforeSpecialGoodsDO =
                            distributorSpecialGoodsMapper.selectByPrimaryKey(goodsCmd.getId());
                    if (beforeSpecialGoodsDO == null) {
                        throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_SPECIAL_GOODS_NULL);
                    }
                    DistributorSpecialGoodsDO afterSpecialGoodsDO =
                            DistributorConvertor.toDistributorSpecialGoodsDO(goodsCmd, distributorId);
                    afterSpecialGoodsDO.setId(beforeSpecialGoodsDO.getId());
                    afterSpecialGoodsDO.setCreateTime(beforeSpecialGoodsDO.getCreateTime());
                    afterSpecialGoodsDO.setUpdateTime(date);
                    updateSpecialGoodsDOs.add(afterSpecialGoodsDO);
                    changeMap(changeMap, "specialGoodsUpdate", beforeSpecialGoodsDO, afterSpecialGoodsDO);
                }
                if (goodsCmd.getOperationType().equals(OPERATION_TYPE_3)) {
                    DistributorSpecialGoodsDO specialGoodsDO =
                            distributorSpecialGoodsMapper.selectByPrimaryKey(goodsCmd.getId());
                    deleteSpecialGoodsDOs.add(specialGoodsDO);
                    changeMap(changeMap, "specialGoodsDelete", specialGoodsDO, null);
                }
            });
        }
    }

    /**
     * 更新分销商财务信息
     *
     * @param cmd
     * @param date
     */
    private void updateDistributorFinancial(DistributorDO distributorDO, DistributorUpdateCmd cmd, Date date,
                                            Map<String, List<List<Object>>> changeMap) throws Exception {
        Integer distributorId = cmd.getId();
        DistributorFinancialDO beforeFinancialDO = distributorFinancialMapper.getByDistributorId(distributorId);
        if (beforeFinancialDO == null) {
            beforeFinancialDO = new DistributorFinancialDO();
            beforeFinancialDO.setDistributorId(distributorDO.getId());
            beforeFinancialDO.setCreateTime(date);
            beforeFinancialDO.setUpdateTime(date);
        } else {
            beforeFinancialDO.setUpdateTime(date);
        }
        DistributorFinancialDO afterFinancialDO = new DistributorFinancialDO();
        DistributorFinancialCmd financial = cmd.getFinancial();
        if (financial == null) {
            return;
        }
        BeanUtils.copyProperties(financial, afterFinancialDO);
        afterFinancialDO.setId(beforeFinancialDO.getId());
        afterFinancialDO.setDistributorId(beforeFinancialDO.getDistributorId());
        afterFinancialDO.setCreateTime(beforeFinancialDO.getCreateTime());
        afterFinancialDO.setUpdateTime(beforeFinancialDO.getUpdateTime());
        Map<String, List<Object>> changeMapTemp = CommonUtils.compareFields(beforeFinancialDO, afterFinancialDO);
        if (changeMapTemp.size() > 0) {
            changeMapTemp.forEach((k, vs) -> {
                List<List<Object>> vss = new ArrayList<>();
                vss.add(vs);
                changeMap.put(k, vss);
            });
            distributorDO.setAfterFinancialDO(afterFinancialDO);
        }
    }

    /**
     * 更新分销商扩展信息
     *
     * @param cmd
     * @param date
     */
    private void updateDistributorExtendData(DistributorDO distributorDO, DistributorUpdateCmd cmd, Date date,
                                             Map<String, List<List<Object>>> changeMap) throws Exception {
        Integer distributorId = cmd.getId();
        DistributorExtendDataDO beforeExtendDataDO = distributorExtendDataMapper.getByDistributorId(distributorId);
        if (beforeExtendDataDO == null) {
            beforeExtendDataDO = new DistributorExtendDataDO();
            beforeExtendDataDO.setDistributorId(distributorDO.getId());
            beforeExtendDataDO.setCreateTime(date);
            beforeExtendDataDO.setUpdateTime(date);
        } else {
            beforeExtendDataDO.setUpdateTime(date);
        }
        DistributorExtendDataDO afterExtendDataDO = new DistributorExtendDataDO();
        DistributorExtendDataCmd extendData = cmd.getExtendData();
        if (extendData == null) {
            return;
        }
        BeanUtils.copyProperties(extendData, afterExtendDataDO);
        afterExtendDataDO.setId(beforeExtendDataDO.getId());
        afterExtendDataDO.setDistributorId(beforeExtendDataDO.getDistributorId());
        afterExtendDataDO.setCreateTime(beforeExtendDataDO.getCreateTime());
        afterExtendDataDO.setUpdateTime(beforeExtendDataDO.getUpdateTime());
        afterExtendDataDO.setDistributionQrUrl(beforeExtendDataDO.getDistributionQrUrl());
        Map<String, List<Object>> changeMapTemp = CommonUtils.compareFields(beforeExtendDataDO, afterExtendDataDO);
        if (changeMapTemp.size() > 0) {
            changeMapTemp.forEach((k, vs) -> {
                List<List<Object>> vss = new ArrayList<>();
                vss.add(vs);
                changeMap.put(k, vss);
            });
            afterExtendDataDO.setDistributorId(distributorId);
            distributorDO.setAfterExtendDataDO(afterExtendDataDO);
        }
        distributorDO.setBeforeExtendDataDO(beforeExtendDataDO);
    }

    /**
     * 更新分销商联系人
     *
     * @param cmd
     * @param date
     */
    private void updateDistributorContact(DistributorDO distributorDO, DistributorUpdateCmd cmd, Date date,
                                          Map<String, List<List<Object>>> changeMap) {
        Integer distributorId = cmd.getId();
        List<DistributorContactsCmd> contacts = cmd.getContacts();
        List<DistributorContactsDO> addContacts = new ArrayList<>();
        List<DistributorContactsDO> updateContacts = new ArrayList<>();
        List<DistributorContactsDO> deleteContacts = new ArrayList<>();
        distributorDO.setAddContacts(addContacts);
        distributorDO.setUpdateContacts(updateContacts);
        distributorDO.setDeleteContacts(deleteContacts);
        if (!CollectionUtils.isEmpty(contacts)) {
            contacts.forEach(contact -> {
                if (contact.getOperationType().equals(OPERATION_TYPE_1)) {
                    DistributorContactsDO contactsDO =
                            DistributorConvertor.toDistributorContactsDO(contact, distributorId);
                    addContacts.add(contactsDO);
                    changeMap(changeMap, "contactsAdd", null, contactsDO);
                }
                if (contact.getOperationType().equals(OPERATION_TYPE_2)) {
                    DistributorContactsDO beforeContactsDO =
                            distributorContactsMapper.selectByPrimaryKey(contact.getId());
                    if (beforeContactsDO == null) {
                        throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_CONTACT_NULL);
                    }
                    DistributorContactsDO afterContactsDO =
                            DistributorConvertor.toDistributorContactsDO(contact, distributorId);
                    afterContactsDO.setId(beforeContactsDO.getId());
                    afterContactsDO.setCreateTime(beforeContactsDO.getCreateTime());
                    afterContactsDO.setUpdateTime(date);
                    updateContacts.add(afterContactsDO);
                    changeMap(changeMap, "contactsUpdate", beforeContactsDO, afterContactsDO);
                }
                if (contact.getOperationType().equals(OPERATION_TYPE_3)) {
                    DistributorContactsDO deleteContactsDO =
                            distributorContactsMapper.selectByPrimaryKey(contact.getId());
                    deleteContacts.add(deleteContactsDO);
                    changeMap(changeMap, "contactsDelete", deleteContactsDO, null);
                }
            });
        }
    }

    /**
     * 更新分销商业务信息
     *
     * @param cmd
     * @param date
     */
    private void updateDistributorBusiness(DistributorDO distributorDO, DistributorUpdateCmd cmd, Date date,
                                           Map<String, List<List<Object>>> changeMap) throws Exception {
        Integer distributorId = cmd.getId();
        DistributorBusinessDO beforeBusinessDO = distributorBusinessMapper.getByDistributorId(distributorId);
        if (beforeBusinessDO == null) {
            beforeBusinessDO = new DistributorBusinessDO();
            beforeBusinessDO.setDistributorId(distributorDO.getId());
            beforeBusinessDO.setCreateTime(date);
            beforeBusinessDO.setUpdateTime(date);
        } else {
            distributorDO.setBusinessDO(beforeBusinessDO);
            beforeBusinessDO.setUpdateTime(date);
        }
        DistributorBusinessDO afterBusinessDO = new DistributorBusinessDO();
        DistributorBusinessUpdateCmd business = cmd.getBusiness();
        if (business == null) {
            return;
        }
        BeanUtils.copyProperties(business, afterBusinessDO);
        afterBusinessDO.setId(beforeBusinessDO.getId());
        afterBusinessDO.setDistributorId(beforeBusinessDO.getDistributorId());
        afterBusinessDO.setCreateTime(beforeBusinessDO.getCreateTime());
        afterBusinessDO.setUpdateTime(beforeBusinessDO.getUpdateTime());
        Map<String, List<Object>> changeMapTemp = CommonUtils.compareFields(beforeBusinessDO, afterBusinessDO);
        if (changeMapTemp.size() > 0) {
            changeMapTemp.forEach((k, vs) -> {
                List<List<Object>> vss = new ArrayList<>();
                vss.add(vs);
                changeMap.put(k, vss);
            });
            afterBusinessDO.setDistributorId(distributorId);
            distributorDO.setBusinessDO(afterBusinessDO);
        }
    }

    /**
     * 更新分销商地址信息
     *
     * @param cmd
     * @param date
     */
    private void updateDistributorAddress(DistributorDO distributorDO, DistributorUpdateCmd cmd, Date date,
                                          Map<String, List<List<Object>>> changeMap) throws Exception {
        Integer distributorId = cmd.getId();
        List<DistributorAddressDO> addressDOS =
                distributorAddressMapper.listByDistributorIdAndAddressType(distributorId, ADDRESS_TYPE_1);
        DistributorAddressDO beforeAddressDO = new DistributorAddressDO();
        if (!CollectionUtils.isEmpty(addressDOS)) {
            beforeAddressDO = addressDOS.get(0);
            beforeAddressDO.setUpdateTime(date);
        } else {
            beforeAddressDO.setDistributorId(distributorDO.getId());
            beforeAddressDO.setCreateTime(date);
            beforeAddressDO.setUpdateTime(date);
        }
        DistributorAddressDO afterAddressDo = new DistributorAddressDO();
        DistributorAddressCmd address = cmd.getAddress();
        if (address == null) {
            return;
        }
        BeanUtils.copyProperties(address, afterAddressDo);
        afterAddressDo.setId(beforeAddressDO.getId());
        afterAddressDo.setDefaultFlag(beforeAddressDO.getDefaultFlag());
        afterAddressDo.setDistributorId(beforeAddressDO.getDistributorId());
        afterAddressDo.setCreateTime(beforeAddressDO.getCreateTime());
        afterAddressDo.setUpdateTime(beforeAddressDO.getUpdateTime());
        if ((afterAddressDo.getProvinceId() == null || afterAddressDo.getProvinceId() == 0)
                && (beforeAddressDO.getProvinceId() == null || beforeAddressDO.getProvinceId() == 0)) {
            afterAddressDo.setProvinceId(beforeAddressDO.getProvinceId());
        }
        if ((afterAddressDo.getCityId() == null || afterAddressDo.getCityId() == 0)
                && (beforeAddressDO.getCityId() == null || beforeAddressDO.getCityId() == 0)) {
            afterAddressDo.setCityId(beforeAddressDO.getCityId());
        }
        if ((afterAddressDo.getDistrictId() == null || afterAddressDo.getDistrictId() == 0)
                && (beforeAddressDO.getDistrictId() == null || beforeAddressDO.getDistrictId() == 0)) {
            afterAddressDo.setDistrictId(beforeAddressDO.getDistrictId());
        }
        Map<String, List<Object>> changeMapTemp = CommonUtils.compareFields(beforeAddressDO, afterAddressDo);
        if (changeMapTemp.size() > 0) {
            changeMapTemp.forEach((k, vs) -> {
                List<List<Object>> vss = new ArrayList<>();
                vss.add(vs);
                changeMap.put(k, vss);
            });
            afterAddressDo.setDistributorId(distributorId);
            distributorDO.setAfterAddressDO(afterAddressDo);
        }
    }

    /**
     * 更新分销销售渠道其他信息
     *
     * @param cmd
     */
    private void updateDistributorSalesArea(DistributorDO distributorDO, DistributorUpdateCmd cmd,
                                            Map<String, List<List<Object>>> changeMap) {
        Integer distributorId = cmd.getId();
        List<DistributorSalesAreaDO> salesAreaDOS = distributorSalesAreaMapper.listByDistributorId(distributorId);
        List<Integer> beforeSalesAreaIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesAreaDOS)) {
            salesAreaDOS.forEach(salesAreaDO -> {
                beforeSalesAreaIds.add(salesAreaDO.getSalesAreaId());
            });
        }
        List<Integer> afterSalesAreaIds = cmd.getSalesAreaIds();
        if (afterSalesAreaIds == null) {
            afterSalesAreaIds = new ArrayList<>();
        }
        Collections.sort(beforeSalesAreaIds);
        Collections.sort(afterSalesAreaIds);
        if (!StringUtils.join(beforeSalesAreaIds, ',').equals(StringUtils.join(afterSalesAreaIds, ','))) {
            List<Integer> bSalesAreaIds = new ArrayList<>();
            List<Integer> aSalesAreaIds = new ArrayList<>();
            if (CollectionUtils.isEmpty(beforeSalesAreaIds)) {
                bSalesAreaIds = null;
            } else {
                bSalesAreaIds = beforeSalesAreaIds;

            }
            if (CollectionUtils.isEmpty(afterSalesAreaIds)) {
                aSalesAreaIds = null;
            } else {
                aSalesAreaIds = afterSalesAreaIds;
            }
            changeMap(changeMap, "salesAreaIds", bSalesAreaIds, aSalesAreaIds);
            List<DistributorSalesAreaDO> doList =
                    DistributorConvertor.toDistributorSalesAreaDOList(afterSalesAreaIds, distributorId);
            distributorDO.setSalesAreaDOS(doList);
        }
    }

    /**
     * 判断分销商基本信息更新
     *
     * @param cmd
     */
    private DistributorDO checkDistributorDO(DistributorDO beforeDO, DistributorUpdateCmd cmd, Date date,
                                             Map<String, List<List<Object>>> changeMap) throws Exception {
        DistributorDO afterDO = new DistributorDO();
        BeanUtils.copyProperties(cmd, afterDO);
        afterDO.setTreeNode(beforeDO.getTreeNode());
        afterDO.setFreezeStatus(beforeDO.getFreezeStatus());
        afterDO.setProfileStatus(beforeDO.getProfileStatus());
        if (beforeDO.getApplyStatus() != null && beforeDO.getApplyStatus().equals(APPLY_STATUS_1)
                && afterDO.getApplyStatus() != null
                && (afterDO.getApplyStatus().equals(APPLY_STATUS_2) || afterDO.getApplyStatus().equals(APPLY_STATUS_3))) {
            afterDO.setCheckTime(date);
            afterDO.setProfileStatus(PROFILE_STATUS_2);
        } else {
            afterDO.setCheckTime(beforeDO.getCheckTime());
        }
        afterDO.setApplyType(beforeDO.getApplyType());
        afterDO.setCreateTime(beforeDO.getCreateTime());
        afterDO.setUpdateTime(beforeDO.getUpdateTime());
        afterDO.setFreezeTime(beforeDO.getFreezeTime());
        afterDO.setApplyTime(beforeDO.getApplyTime());
        Map<String, List<Object>> changeMapTemp = CommonUtils.compareFields(beforeDO, afterDO);
        if (changeMapTemp.size() > 0) {
            changeMapTemp.forEach((k, vs) -> {
                List<List<Object>> vss = new ArrayList<>();
                vss.add(vs);
                changeMap.put(k, vss);
            });
            afterDO.setUpdateTime(date);
        }
        return afterDO;
    }

    /**
     * 冻结分销商列表
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void freezeDistributor(DistributorFreezeStatusCmd cmd) {
        // TODO 分销商资料是否需要审批
        Date freezeTime = new Date(System.currentTimeMillis());
        //冻结指定的一级分销商
        mapper.freezeDistributor(cmd.getId(), cmd.getFreezeStatus(), freezeTime);
        //生成一级分销商的冻结日志
        sendService.distributorLogPackage(cmd.getId(), "冻结分销商", "冻结成功", JSONObject.toJSONString(cmd));

        //根据一级分销商id查询多级分销商id
        List<Integer> multiLevelDistributorId = mapper.multiLevelDistributorById(cmd.getId());

        if (ObjectUtils.isNotEmpty(multiLevelDistributorId) && cmd.getFreezeStatus() == 2) {
            //冻结指定的一级分销商下面的多级分销商
            mapper.freezeMultiLevelDistributors(cmd.getId(), cmd.getFreezeStatus(), freezeTime);

            //生成多级分销商冻结日志
            multiLevelDistributorId.forEach(x -> {
                sendService.distributorLogPackage(x, "跟随一级分销商被冻结", "冻结成功", JSONObject.toJSONString(cmd));
            });
        }
    }

    /**
     * 根据Id删除分销商
     *
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDistributor(DistributorId cmd) {
        DistributorDO distributorDO = mapper.selectByPrimaryKey(cmd.getId());
        if (distributorDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_NULL);
        }
        // 冻结的分销商才允许删除
        if (!distributorDO.getFreezeStatus().equals(FREEZE_STATUS_2)
                && (distributorDO.getApplyType() != null && !distributorDO.getApplyType().equals(APPLY_STATUS_3))) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_TRADE_DELETE_FREEZE_APPLY_ERROR);
        }
        // TODO 分销商业务是否发生（订单）
        // 删除分销商销售区域
        distributorSalesAreaMapper.deleteByDistributorId(cmd.getId());
        // 删除分销商地址
        distributorAddressMapper.deleteByDistributorId(cmd.getId());
        // 删除分销商业务数据
        distributorBusinessMapper.deleteByDistributorId(cmd.getId());
        // 删除分销商业务数据价格等级
        oneScalePriceMapper.deleteByDistributorId(cmd.getId());
        distributorNextScalePriceMapper.deleteByDistributorId(cmd.getId());
        // 删除分销商联系人
        distributorContactsMapper.deleteByDistributorId(cmd.getId());
        // 删除分销商扩展数据
        distributorExtendDataMapper.deleteByDistributorId(cmd.getId());
        // 删除分销财务数据
        distributorFinancialMapper.deleteByDistributorId(cmd.getId());
        // 删除分销商商品特价
        distributorSpecialGoodsMapper.deleteByDistributorId(cmd.getId());
        mapper.deleteByPrimaryKey(cmd.getId());
        sendService.distributorLogPackage(cmd.getId(), "删除分销商", "删除成功", JSONObject.toJSONString(cmd));
    }

    /**
     * 下级分销商审核
     *
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkNextDistributor(DistributorApplyStatusCmd cmd) {
        DistributorDO distributorDO = mapper.selectByPrimaryKey(cmd.getId());
        if (distributorDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_NULL);
        }
        Date date = new Date(System.currentTimeMillis());
        distributorDO.setApplyStatus(cmd.getApplyStatus());
        distributorDO.setApplyTime(date);
        mapper.applyDistributor(distributorDO);
        sendService.distributorLogPackage(cmd.getId(), "下级分销商审核", "审核完毕", JSONObject.toJSONString(cmd));
    }

    /**
     * 根据分销商销售员更新分销商可视范围
     *
     * @param saleDTO
     */
    @Async
    public void updateDistributorVisibleBySalesId(String tenantNo, SalesmanDTO saleDTO) {
        TenantContext.setTenantNo(tenantNo);
        updateDistributorVisibleBySalesId(saleDTO);
        TenantContext.removeTenantNo();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDistributorVisibleBySalesId(SalesmanDTO saleDTO) {
        //查询业务员对应的分销商
        List<Integer> distributorIds = mapper.listDistributorIdBySalesId(saleDTO.getSalesId());

        if (!CollectionUtils.isEmpty(distributorIds)) {
            distributorIds.forEach(distributorId -> {
                // 更新品牌可视范围(指定分销商情况,暂不支持其他情况)
                // rpcCmdExe.brandDistributorRelevance(null, distributorId);

                //查询一级分销商价格等级关系
                List<DistributorOneScalePriceDO> scalePriceDOS = oneScalePriceMapper.listByDistributorId(distributorId);

                // 分销商默认价格等级
                Optional<DistributorOneScalePriceDO> defaultScalePrice = scalePriceDOS.stream()
                        .filter(scalePriceDO -> (scalePriceDO.getBrandId() == null || scalePriceDO.getBrandId() == 0)
                                && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0))
                        .findFirst();

                //查询业务员的信息
                UserRpcDTO userRpcDTO = commonRpcExe.getUserById(saleDTO.getSalesId());
                //有事务，此时查到的还是还是改之前的部门，因此需要将新部门设置进去
                userRpcDTO.setDepartmentId(saleDTO.getNewDepartmentId());

                //查询分销商的可视品牌
                DistributorBrandRelevanceDO brandRelevanceDO =
                        distributorBrandRelevanceMapper.selectByDistributorId(distributorId);

                //过滤出品牌
                List<Integer> brandIds = new ArrayList<>();
                if (brandRelevanceDO != null) {
                    brandIds
                            .addAll(Arrays
                                    .stream(brandRelevanceDO.getBrandIds()
                                            .substring(1, brandRelevanceDO.getBrandIds().length() - 1).split(","))
                                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
                }

                //更新商品可视
                updateVisibleGoodsIds(distributorId, scalePriceDOS, saleDTO.getSalesId(), userRpcDTO.getDepartmentId(), null);

                // 更新活动可视范围
                updateVisiblePromotion(distributorId, defaultScalePrice.get().getScalePriceId(), saleDTO.getSalesId(),
                        userRpcDTO.getDepartmentId());
            });
        }
    }

    /**
     * 根据分销商Id刷新分销商可视范围
     *
     * @param distributorId
     */
    @Async
    public void updateDistributorVisibleByDistributorId(String tenantNo, Integer distributorId) {
        TenantContext.setTenantNo(tenantNo);
        updateDistributorVisibleByDistributorId(distributorId);
        TenantContext.removeTenantNo();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDistributorVisibleByDistributorId(Integer distributorId) {
        DistributorBusinessDO businessDO = distributorBusinessMapper.getByDistributorId(distributorId);
        Integer salesId = businessDO.getSalesId();
        List<DistributorOneScalePriceDO> scalePriceDOS = oneScalePriceMapper.listByDistributorId(distributorId);
        // 更新品牌可视范围(指定分销商情况,暂不支持其他情况)
        rpcCmdExe.brandDistributorRelevance(scalePriceDOS, distributorId);
        // 分销商默认价格等级
        Optional<DistributorOneScalePriceDO> defaultScalePrice = scalePriceDOS.stream()
                .filter(scalePriceDO -> (scalePriceDO.getBrandId() == null || scalePriceDO.getBrandId() == 0)
                        && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0))
                .findFirst();
        // 更新商品可视范围
        UserRpcDTO userRpcDTO = commonRpcExe.getUserById(salesId);
        DistributorBrandRelevanceDO brandRelevanceDO =
                distributorBrandRelevanceMapper.selectByDistributorId(distributorId);
        List<Integer> brandIds = new ArrayList<>();
        if (brandRelevanceDO != null) {
            brandIds.addAll(Arrays
                    .stream(
                            brandRelevanceDO.getBrandIds().substring(1, brandRelevanceDO.getBrandIds().length() - 1).split(","))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }
        updateVisibleGoodsIds(distributorId, scalePriceDOS, salesId, userRpcDTO.getDepartmentId(),
                businessDO.getDistributorGroupIds());
        // 更新活动可视范围
        updateVisiblePromotion(distributorId, defaultScalePrice.get().getScalePriceId(), salesId,
                userRpcDTO.getDepartmentId());
    }

    /**
     * 分销商资料审批
     *
     * @param cmd
     * @param userId
     * @param userName
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkDistributor(DistributorCheckCmd cmd, String userId, String userName) {
        Date date = new Date(System.currentTimeMillis());
        DistributorCheckDO checkDO = distributorCheckMapper.selectByPrimaryKey(cmd.getId());
        if (checkDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_NULL);
        }
        if (!checkDO.getCheckStatus().equals(CHECK_STATUS_0)) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_FINISH);
        }
        if (!checkDO.getCheckUserId().equals(Integer.valueOf(userId))) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_USER_ERROR);
        }
        DistributorDO distributorDO = mapper.selectByPrimaryKey(checkDO.getDistributorId());
        if (distributorDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_NULL);
        }
        // 更新当前审批流状态
        List<DistributorCheckFlowDO> checkFlowDOS = distributorCheckFlowMapper.listByDistributorCheckId(cmd.getId());
        DistributorCheckFlowDO distributorCheckFlowDO = checkFlowDOS.stream()
                .filter(checkFlowDO -> checkFlowDO.getUserId().equals(checkDO.getCheckUserId())).findFirst().get();
        distributorCheckFlowDO.setCheckStatus(cmd.getCheckStatus());
        distributorCheckFlowDO.setRemark(cmd.getRemark());
        distributorCheckFlowDO.setCheckTime(date);
        distributorCheckFlowDO.setUpdateTime(date);
        distributorCheckFlowMapper.updateByPrimaryKey(distributorCheckFlowDO);
        if (cmd.getCheckStatus().equals(CHECK_STATUS_2)) {
            // 审批拒绝时
            checkDO.setCheckStatus(cmd.getCheckStatus());
            checkDO.setUpdateTime(date);
            distributorCheckMapper.updateByPrimaryKey(checkDO);
            // 删除未完成的审批流
            List<DistributorCheckFlowDO> deleteCheckFlowDOS = checkFlowDOS.stream()
                    .filter(checkFlowDO -> checkFlowDO.getCheckSort().intValue() > distributorCheckFlowDO.getCheckSort())
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(deleteCheckFlowDOS)) {
                List<Integer> deleteIds =
                        deleteCheckFlowDOS.stream().map(DistributorCheckFlowDO::getId).collect(Collectors.toList());
                distributorCheckFlowMapper.deleteByIds(deleteIds);
            }
            // 处理新增或前台申请的合作分销商
            if (distributorDO.getProfileStatus().equals(PROFILE_STATUS_1)) {
                // 前台申请的分销商，重新修改分销商为申请中
                if (distributorDO.getApplyType().equals(APPLY_TYPE_2)) {
                    distributorDO.setProfileStatus(PROFILE_STATUS_0);
                    distributorDO.setApplyStatus(APPLY_STATUS_1);
                    distributorDO.setCheckTime(date);
                } else {
                    distributorDO.setProfileStatus(PROFILE_STATUS_3);
                }
                distributorDO.setUpdateTime(date);
                mapper.updateByPrimaryKey(distributorDO);
            }
        } else {
            // 审批同意时，更新审批单（当是最后一个审批人审批时需更新审批结果）
            Optional<DistributorCheckFlowDO> optional = checkFlowDOS.stream()
                    .filter(checkFlowDO -> checkFlowDO.getCheckSort().equals(distributorCheckFlowDO.getCheckSort() + 1))
                    .findFirst();
            if (optional != null && optional.isPresent()) {
                // 非最后一个审批人审批情况，更新当前审批人
                DistributorCheckFlowDO checkingFlowDO = optional.get();
                checkDO.setCheckUserId(checkingFlowDO.getUserId());
                checkDO.setCheckUserName(checkingFlowDO.getUserName());
                checkDO.setUpdateTime(date);
                distributorCheckMapper.updateByPrimaryKey(checkDO);
            } else {
                // 最后一个审批人审批时，审批单审批结束
                checkDO.setCheckStatus(cmd.getCheckStatus());
                DistributorExtendDataDO extendDataDO = null;
                if (distributorDO.getProfileStatus().equals(PROFILE_STATUS_1)) {
                    // 新增或前台申请的合作分销商，更新分销商资料审核状态就可以
                    // 前台申请的分销商，重新修改分销商为申请中
                    if (distributorDO.getApplyType().equals(APPLY_TYPE_2)) {
                        // 更新申请通过时间
                        distributorDO.setCheckTime(date);
                    }
                    distributorDO.setProfileStatus(PROFILE_STATUS_2);
                    distributorDO.setUpdateTime(date);
                    mapper.updateByPrimaryKey(distributorDO);
                    extendDataDO = distributorExtendDataMapper.getByDistributorId(distributorDO.getId());
                    sendService.distributorLogPackage(distributorDO.getId(), "新增分销商审批", "审批成功",
                            JSONObject.toJSONString(distributorDO));
                } else {
                    // 修改分销商资料审批，需更新分销商修改内容
                    if (StringUtils.isNotBlank(checkDO.getCheckContent())) {
                        Map<String, List<List<Object>>> changeMap =
                                JSONObject.parseObject(checkDO.getCheckContent(), Map.class);
                        distributorDO = checkConvertor.toDistributorDO(distributorDO, changeMap, date);
                        distributorDO.setProfileStatus(PROFILE_STATUS_2);
                        updateDistributor(distributorDO, changeMap);
                        extendDataDO = distributorDO.getAfterExtendDataDO();
                        if (extendDataDO == null) {
                            extendDataDO = distributorDO.getBeforeExtendDataDO();
                        }
                        // 发送更新分销商日志
                        sendService.distributorLogPackage(distributorDO.getId(), "更新分销商审批", "审批成功",
                                JSONObject.toJSONString(changeMap));
                    }
                }
                checkDO.setUpdateTime(date);
                distributorCheckMapper.updateByPrimaryKey(checkDO);
                // 审批成功发送消息
                if (checkDO.getCheckStatus().equals(CHECK_STATUS_1)) {
                    // 刷新分销商品牌和商品的可视范围(一级)
                    if (distributorDO.getTreeNode() == 1) {
                        sendService.updateDistributorVisibleByDistributorId(distributorDO.getId());
                    }
                    // 发送同步erp消息
                    if (extendDataDO.getErpFlag() != null && extendDataDO.getErpFlag().equals(ERP_FLAG_1)) {
                        sendService.distributorSyncErp(distributorDO.getId());
                    }
                }
            }
        }
    }
}
