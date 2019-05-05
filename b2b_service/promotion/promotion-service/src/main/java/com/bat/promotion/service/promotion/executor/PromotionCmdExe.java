package com.bat.promotion.service.promotion.executor;

import static com.bat.promotion.service.common.CommonErrorCode.*;
import static com.bat.promotion.service.common.Constant.*;
import static com.bat.promotion.service.promotion.executor.ErrorCode.*;

import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.promotion.convertor.PromotionConvertor;
import com.bat.promotion.service.promotion.dto.ImportPromotionExcelDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.bat.dubboapi.goods.goods.dto.GoodsItemRpc;
import com.bat.dubboapi.system.check.dto.data.CheckConfigDetailRpcDTO;
import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.check.executor.PromotionCheckCmdExe;
import com.bat.promotion.api.base.MessageUtils;
import com.bat.promotion.service.common.PromotionConfig;
import com.bat.promotion.service.common.rpc.PromotionRpcQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:12
 */
@Component
public class PromotionCmdExe {

    @Resource
    private PromotionMapper promotionMapper;
    @Resource
    private PromotionScalePriceRelevanceMapper scalePriceRelevanceMapper;
    @Resource
    private PromotionScalePriceRelevanceNoMapper scalePriceRelevanceNoMapper;
    @Resource
    private PromotionDepartmentRelevanceMapper departmentRelevanceMapper;
    @Resource
    private PromotionDepartmentRelevanceNoMapper departmentRelevanceNoMapper;
    @Resource
    private PromotionAdminRelevanceMapper adminRelevanceMapper;
    @Resource
    private PromotionAdminRelevanceNoMapper adminRelevanceNoMapper;
    @Resource
    private PromotionDistributorRelevanceMapper distributorRelevanceMapper;
    @Resource
    private PromotionDistributorRelevanceNoMapper distributorRelevanceNoMapper;
    @Resource
    private PromotionRuleMapper ruleMapper;
    @Resource
    private PromotionRuleGoodsMapper ruleGoodsMapper;
    @Resource
    private PromotionRuleConditionMapper ruleConditionMapper;
    @Resource
    private PromotionRuleConditionPresentMapper ruleConditionPresentMapper;
    @Resource
    private PromotionRuleConditionSpecialMapper ruleConditionSpecialMapper;
    @Resource
    private PromotionRpcCmdExe rpcCmdExe;
    @Resource
    private PromotionConfig config;
    @Resource
    private PromotionRpcQryExe rpcQryExe;
    @Resource
    private PromotionCheckCmdExe checkCmdExe;

    /**
     * 新增促销活动
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void createPromotion(PromotionCmd cmd, String userId, String userName) {
        Date date = new Date(System.currentTimeMillis());
        PromotionDO promotionDO = PromotionConvertor.toPromotionDO(cmd, date);
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_13);
        promotionApplyAndStatus(promotionDO, detailRpcDTO);
        promotionMapper.insert(promotionDO);
        savePromotionScope(promotionDO.getId(), cmd, OPERATION_TYPE_1);
        savePromotionRule(promotionDO.getId(), cmd, OPERATION_TYPE_1);
        if (promotionDO.getApplyStatus().equals(APPLY_STATUS_2)) {
            // 商品价格更新
            if (promotionDO.getPromoStatus().equals(PROMO_STATUS_1)) {
                rpcCmdExe.goodsPromotionPriceByPromotion(promotionDO.getId(), cmd);
            }
            // 促销活动定时任务
            cronJobPromotion(promotionDO);
        } else if (promotionDO.getApplyStatus().equals(APPLY_STATUS_1)) {
            checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_1, promotionDO.getId(), detailRpcDTO, date, userId,
                userName);
        }
    }

    /**
     * 促销活动定时任务
     * 
     * @param promotionDO
     */
    public void cronJobPromotion(PromotionDO promotionDO) {
        try {
            rpcCmdExe.promotionAddXxlJob(promotionDO);
        } catch (ParseException e) {
            throw PromotionException.buildException(B_PROMOTION_TIME_ERROR);
        }
    }

    /**
     * 促销活动的审批和状态处理
     * 
     * @param promotionDO
     */
    private void promotionApplyAndStatus(PromotionDO promotionDO, CheckConfigDetailRpcDTO detailRpcDTO) {
        if (promotionDO.getApplyStatus() != null && promotionDO.getApplyStatus().equals(APPLY_STATUS_0)) {
            promotionDO.setPromoStatus(GROUP_SECKILL_STATUS_0);
        } else {
            if (detailRpcDTO != null && detailRpcDTO.getOpenFlag().equals(FLAG_1)) {
                promotionDO.setApplyStatus(APPLY_STATUS_1);
                promotionDO.setPromoStatus(PROMO_STATUS_0);
            } else {
                promotionDO.setApplyStatus(APPLY_STATUS_2);
                // 促销活动进行状态(如果开始时间与创建时间间隔10秒情况，活动直接开始)
                if (promotionDO.getStartTime().getTime()
                    - promotionDO.getCreateTime().getTime() < config.getIntervalTime() * 1000) {
                    promotionDO.setPromoStatus(PROMO_STATUS_1);
                } else {
                    promotionDO.setPromoStatus(PROMO_STATUS_0);
                }
            }
        }
    }

    /**
     * 促销活动规则新增更新
     * 
     * @param promotionId
     * @param cmd
     * @param operationType
     */
    private void savePromotionRule(Integer promotionId, PromotionCmd cmd, Short operationType) {
        // 非新增时，先删除历史数据
        if (!operationType.equals(OPERATION_TYPE_1)) {
            deletePromotionRule(promotionId);
        }
        // 促销活动规则
        List<PromotionRuleCmd> rules = cmd.getRules();
        if (!CollectionUtils.isEmpty(rules)) {
            rules.forEach(rule -> {
                PromotionRuleDO ruleDO = PromotionConvertor.toPromotionRuleDO(promotionId, rule);
                ruleMapper.insert(ruleDO);
                // 促销活动规则商品货品
                List<PromotionRuleGoodsCmd> goods = rule.getGoods();
                if (!ruleDO.getRuleType().equals(RULE_TYPE_1) && !CollectionUtils.isEmpty(goods)) {
                    List<PromotionRuleGoodsDO> ruleGoodsDOS =
                        PromotionConvertor.toPromotionRuleGoodsDOList(promotionId, ruleDO.getId(), goods);
                    ruleGoodsMapper.insertList(ruleGoodsDOS);
                }
                // 促销活动规则条件
                List<PromotionRuleConditionCmd> conditions = rule.getConditions();
                conditions.forEach(condition -> {
                    PromotionRuleConditionDO ruleConditionDO =
                        PromotionConvertor.toPromotionRuleConditionDO(promotionId, ruleDO.getId(), condition);
                    ruleConditionMapper.insert(ruleConditionDO);
                    // 条件特价
                    List<PromotionRuleConditionSpecialCmd> specials = condition.getSpecials();
                    if (ruleDO.getRuleType().equals(RULE_TYPE_3) && condition.getSpecialFlag().equals(SPECIAL_FLAG_1)
                        && !CollectionUtils.isEmpty(specials)) {
                        List<PromotionRuleConditionSpecialDO> specialDOS = PromotionConvertor
                            .toPromotionRuleConditionSpecialDOList(promotionId, ruleConditionDO.getId(), specials);
                        ruleConditionSpecialMapper.insertList(specialDOS);
                    }
                    // 条件赠品
                    List<PromotionRuleConditionPresentCmd> presents = condition.getPresents();
                    if (condition.getSpecialFlag().equals(SPECIAL_FLAG_0)
                        && condition.getReduceOrPresent().equals(CONDITION_PRESENT)
                        && !CollectionUtils.isEmpty(presents)) {
                        List<PromotionRuleConditionPresentDO> presentDOS = PromotionConvertor
                            .toPromotionRuleConditionPresentDOList(promotionId, ruleConditionDO.getId(), presents);
                        ruleConditionPresentMapper.insertList(presentDOS);
                    }
                });
            });
        }
    }

    /**
     * 删除促销规则和相关数据
     * 
     * @param promotionId
     */
    private void deletePromotionRule(Integer promotionId) {
        ruleMapper.deleteByPromotionId(promotionId);
        ruleGoodsMapper.deleteByPromotionId(promotionId);
        ruleConditionMapper.deleteByPromotionId(promotionId);
        ruleConditionPresentMapper.deleteByPromotionId(promotionId);
        ruleConditionSpecialMapper.deleteByPromotionId(promotionId);
    }

    /**
     * 删除促销活动可视范围
     * 
     * @param promotionId
     */
    private void deletePromotionScope(Integer promotionId) {
        scalePriceRelevanceMapper.deleteByPromotionId(promotionId);
        scalePriceRelevanceNoMapper.deleteByPromotionId(promotionId);
        departmentRelevanceMapper.deleteByPromotionId(promotionId);
        departmentRelevanceNoMapper.deleteByPromotionId(promotionId);
        adminRelevanceMapper.deleteByPromotionId(promotionId);
        adminRelevanceNoMapper.deleteByPromotionId(promotionId);
        distributorRelevanceMapper.deleteByPromotionId(promotionId);
        distributorRelevanceNoMapper.deleteByPromotionId(promotionId);
    }

    /**
     * 促销活动可视范围
     * 
     * @param promotionId
     * @param cmd
     * @param operationType
     */
    private void savePromotionScope(Integer promotionId, PromotionCmd cmd, Short operationType) {
        // 非新增时，需先删除历史关系
        if (!operationType.equals(OPERATION_TYPE_1)) {
            deletePromotionScope(promotionId);
        }
        // 可视关系
        List list = PromotionConvertor.toPromotionRelevance(promotionId, cmd);
        if (list != null && list.size() > 0) {
            Short distributorScope = cmd.getDistributorScope();
            if (distributorScope.equals(SCOPE_SCALE_PRICE)) {
                scalePriceRelevanceMapper.createScalePriceRelevanceList(list);
            } else if (distributorScope.equals(SCOPE_DISTRIBUTOR)) {
                distributorRelevanceMapper.createDistributorRelevanceList(list);
            } else if (distributorScope.equals(SCOPE_DEPARTMENT)) {
                departmentRelevanceMapper.createDepartmentRelevanceList(list);
            } else if (distributorScope.equals(SCOPE_ADMIN)) {
                adminRelevanceMapper.createAdminRelevanceList(list);
            }
        }
        // 不可视关系
        List listNo = PromotionConvertor.toPromotionRelevanceNO(promotionId, cmd);
        if (listNo != null && listNo.size() > 0) {
            Short distributorScopeNo = cmd.getDistributorScopeNo();
            if (distributorScopeNo.equals(SCOPE_SCALE_PRICE)) {
                scalePriceRelevanceNoMapper.createScalePriceRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(SCOPE_DISTRIBUTOR)) {
                distributorRelevanceNoMapper.createDistributorRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(SCOPE_DEPARTMENT)) {
                departmentRelevanceNoMapper.createDepartmentRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(SCOPE_ADMIN)) {
                adminRelevanceNoMapper.createAdminRelevanceNoList(listNo);
            }
        }
        // 更新分销商促销活动可视数据
        rpcCmdExe.distributorPromotionRelevance(promotionId, cmd);
    }

    /**
     * 修改促销活动(草稿状态的促销活动修改)
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePromotion(PromotionCmd cmd, String userId, String userName) {
        if (cmd.getId() == null) {
            throw PromotionException.buildException(P_PROMOTION_ID_NULL);
        }
        PromotionDO beforePromotionDO = promotionMapper.selectByPrimaryKey(cmd.getId());
        if (beforePromotionDO == null) {
            throw PromotionException.buildException(B_PROMOTION_NULL);
        }
        if (!beforePromotionDO.getApplyStatus().equals(APPLY_STATUS_0)) {
            throw PromotionException.buildException(B_PROMOTION_UPDATE_ERROR);
        }
        Date date = new Date(System.currentTimeMillis());
        PromotionDO afterPromotionDO = PromotionConvertor.toPromotionDO(cmd, date);
        afterPromotionDO.setCreateTime(beforePromotionDO.getCreateTime());
        // 判断是否需要审批
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_13);
        promotionApplyAndStatus(afterPromotionDO, detailRpcDTO);
        promotionMapper.updateByPrimaryKey(afterPromotionDO);
        savePromotionScope(afterPromotionDO.getId(), cmd, OPERATION_TYPE_2);
        savePromotionRule(afterPromotionDO.getId(), cmd, OPERATION_TYPE_2);
        if (afterPromotionDO.getApplyStatus().equals(APPLY_STATUS_2)) {
            // 商品价格更新
            rpcCmdExe.goodsPromotionPriceByPromotion(afterPromotionDO.getId(), cmd);
            // 判断是否开启定时任务
            cronJobPromotion(afterPromotionDO);
        } else if (afterPromotionDO.getApplyStatus().equals(APPLY_STATUS_1)) {
            checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_1, afterPromotionDO.getId(), detailRpcDTO, date, userId,
                userName);
        }
    }

    /**
     * 根据id删除促销活动
     * 
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePromotion(Integer id) {
        PromotionDO promotionDO = promotionMapper.selectByPrimaryKey(id);
        if (promotionDO == null) {
            throw PromotionException.buildException(B_PROMOTION_NULL);
        }
        // 只有草稿和申请失败的促销活动可以删除删除
        if (!promotionDO.getApplyStatus().equals(APPLY_STATUS_0) && !promotionDO.getApplyStatus().equals(APPLY_STATUS_3)
            && !promotionDO.getPromoStatus().equals(PROMO_STATUS_0)) {
            throw PromotionException.buildException(B_PROMOTION_DELETE_ERROR);
        }
        deletePromotionScope(id);
        deletePromotionRule(id);
        promotionMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据ids删除促销活动
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePromotions(List<Integer> ids) {
        ids.forEach(id -> {
            deletePromotion(id);
        });
    }

    /**
     * 根据促销活动id变更状态
     * 
     * @param cmd
     */
    public void updatePromotionStatus(PromotionStatusCmd cmd) {
        List<Integer> ids = new ArrayList<>();
        ids.add(cmd.getId());
        updateListPromotionStatus(ids, cmd.getPromoStatus());
    }

    /**
     * 更新促销活动状态
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateListPromotionStatus(List<Integer> ids, Short promotionStatus) {
        List<PromotionStatusDO> statusDOS = PromotionConvertor.toPromotionStatusDOList(ids, promotionStatus);
        if (!CollectionUtils.isEmpty(statusDOS)) {
            promotionMapper.updateListPromotionStatus(statusDOS);
            statusDOS.forEach(statusDO -> {
                // 更新商品最小价格(促销活动开始)
                PromotionDO promotionDO = promotionMapper.selectByPrimaryKey(statusDO.getId());
                if (promotionDO != null) {
                    if (statusDO.getPromoStatus().equals(PROMO_STATUS_1)) {
                        changeGoodsPromotionPrice(promotionDO);
                    } else if (!statusDO.getPromoStatus().equals(PROMO_STATUS_1)) {// 促销活动非开始时，删除商品最小价格
                        rpcCmdExe.deletePromotionPriceByPromotionId(statusDO.getId());
                        // 活动结束，删除活动数据
                        if (statusDO.getPromoStatus().equals(PROMO_STATUS_2)
                            || statusDO.getPromoStatus().equals(PROMO_STATUS_3)) {
                            rpcCmdExe.deletePromotionRelevanceByPromotionId(statusDO.getId());
                        }
                    }
                }
            });
        }
    }

    /**
     * 根据活动id更新商品活动价格
     * 
     * @param promotionId
     */
    public void changeGoodsPromotionPrice(Integer promotionId) {
        PromotionDO promotionDO = promotionMapper.selectByPrimaryKey(promotionId);
        changeGoodsPromotionPrice(promotionDO);
    }

    /**
     * 根据活动更新商品活动价格
     * 
     * @param promotionDO
     */
    public void changeGoodsPromotionPrice(PromotionDO promotionDO) {
        List<PromotionRuleDO> ruleDOS = ruleMapper.listByPromotionId(promotionDO.getId());
        List<PromotionRuleGoodsDO> ruleGoodsDOS = ruleGoodsMapper.listByPromotionId(promotionDO.getId());
        List<PromotionRuleConditionDO> ruleConditionDOS = ruleConditionMapper.listByPromotionId(promotionDO.getId());
        List<PromotionRuleConditionSpecialDO> conditionSpecialDOS =
            ruleConditionSpecialMapper.listByPromotionId(promotionDO.getId());
        rpcCmdExe.goodsPromotionPriceByPromotion(promotionDO, ruleDOS, ruleGoodsDOS, ruleConditionDOS,
            conditionSpecialDOS);
    }

    /**
     * 刷新活动数据
     * 
     * @param promotionDO
     */
    public void updatePromotion(PromotionDO promotionDO) {
        long timeMillis = System.currentTimeMillis();
        if (promotionDO.getStartTime().getTime() - timeMillis < config.getIntervalTime() * 1000) {
            promotionDO.setPromoStatus(PROMO_STATUS_1);
        } else {
            promotionDO.setPromoStatus(PROMO_STATUS_0);
        }
        // 商品价格更新
        if (promotionDO.getPromoStatus().equals(PROMO_STATUS_1)) {
            changeGoodsPromotionPrice(promotionDO);
        }
        // 促销活动定时任务
        cronJobPromotion(promotionDO);
        promotionMapper.updateByPrimaryKey(promotionDO);
    }

    /**
     * 
     * @param inputStream
     */
    public void promotionImport(InputStream inputStream, String userId, String userName) {
        List<ImportPromotionExcelDTO> dtos = new ArrayList<>();
        // 全部读完 然后在内存中校验 出错的数据，本次就不能导入
        // 可能内存溢出
        ExcelReader excelReader = null;
        // 读取除了模板之外的所有sheet
        try {
            excelReader = EasyExcel.read(inputStream).build();
            List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();
            List<ReadSheet> readSheets = new ArrayList<>();
            for (int i = 1; i <= sheets.size(); i++) {
                ReadSheet readSheet = EasyExcel.readSheet(i).head(ImportPromotionExcelDTO.class)
                    .registerReadListener(new PageReadListener<ImportPromotionExcelDTO>(dtos::addAll)).build();
                readSheets.add(readSheet);
            }
            excelReader.read(readSheets);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
        if (!CollectionUtils.isEmpty(dtos)) {
            createPromotionList(dtos, userId, userName);
        } else {
            throw PromotionException.buildException(B_FILE_IS_EMPTY);
        }
    }

    /**
     * 批量提交促销活动
     * 
     * @param cmd
     */
    @Transactional(rollbackFor = Exception.class)
    public void promotionSubmits(PromotionSubmitsCmd cmd, String userId, String userName) {
        Date date = new Date(System.currentTimeMillis());
        List<PromotionDO> promotionDOS = promotionMapper.listByIds(cmd.getIds());
        CheckConfigDetailRpcDTO detailRpcDTO = rpcQryExe.getCheckFlows(PROMOTION_TYPE_13);
        promotionDOS.forEach(promotionDO -> {
            promotionDO.setApplyStatus(cmd.getApplyStatus());
            promotionApplyAndStatus(promotionDO, detailRpcDTO);
            promotionMapper.updateByPrimaryKey(promotionDO);
            // 活动是否开启审批
            if (promotionDO.getApplyStatus().equals(APPLY_STATUS_2)) {
                // 商品价格更新
                if (promotionDO.getPromoStatus().equals(PROMO_STATUS_1)) {
                    changeGoodsPromotionPrice(promotionDO);
                }
                // 促销活动定时任务
                cronJobPromotion(promotionDO);
            } else if (promotionDO.getApplyStatus().equals(APPLY_STATUS_1)) {
                checkCmdExe.promotionCheck(PROMOTION_CHECK_TYPE_1, promotionDO.getId(), detailRpcDTO, date, userId,
                    userName);
            }
        });
    }

    /**
     * 批量创建促销活动列表
     * 
     * @param dtos
     * @param userId
     * @param userName
     */
    @Transactional(rollbackFor = Exception.class)
    public void createPromotionList(List<ImportPromotionExcelDTO> dtos, String userId, String userName) {
        List<PromotionDO> promotions = new ArrayList<>();
        Map<String, PromotionDO> promotionMap = new HashMap<>();
        Map<String, PromotionRuleDO> promotionRuleMap = new HashMap<>();
        Map<String, List<DepartmentRpcDTO>> departmentListMap = new HashMap<>();
        // 规则对应商品或货品
        Map<String, List<PromotionRuleGoodsDO>> ruleGoodsListMap = new HashMap<>();
        // 规则对应条件
        Map<String, List<PromotionRuleConditionDO>> conditionMap = new HashMap<>();
        // 活动条件特价
        Map<PromotionRuleConditionDO, List<PromotionRuleConditionSpecialDO>> specialMap = new HashMap<>();
        dtos.forEach(dto -> {
            Date date = new Date(System.currentTimeMillis());
            String indexMsg = "第" + (dtos.indexOf(dto) + 1);
            String promotionNo = dto.getPromotionNo();
            List<DepartmentRpcDTO> departments = new ArrayList<>();
            String departmentSStr = dto.getDepartmentSStr();
            if (StringUtils.isNotBlank(departmentSStr)) {
                String[] departmentsIds = departmentSStr.split(",");
                List<Integer> departmentIds =
                    Arrays.stream(departmentsIds).map(Integer::parseInt).collect(Collectors.toList());
                departments = rpcQryExe.getDepartmentsByIds(departmentIds);
            }
            // 活动层数据
            PromotionDO promotionDO = PromotionConvertor.toPromotionDO(indexMsg, departments, dto, date, promotions,
                promotionMap, departmentListMap);
            // 活动规则层数据
            PromotionRuleDO ruleDO = PromotionConvertor.toPromotionRuleDO(indexMsg, dto, promotionRuleMap);
            String goodsItemNo = dto.getGoodsItemNo();
            if (StringUtils.isBlank(goodsItemNo)) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_GOODS_NO_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_GOODS_NO_NULL));
            }
            // 活动规则商品层数据
            Integer goodsId = null;
            GoodsItemRpc itemRpc = null;
            if (ruleDO.getRuleType().equals(RULE_TYPE_2)) {
                goodsId = rpcQryExe.getGoodsIdByGoodsNo(goodsItemNo);
            } else if (ruleDO.getRuleType().equals(RULE_TYPE_3)) {
                List<GoodsItemRpc> itemRpcs = rpcQryExe.getGoodsItemIdsByItemCodeOrBarCode(goodsItemNo);
                if (!CollectionUtils.isEmpty(itemRpcs)) {
                    if (itemRpcs.size() > 1) {
                        throw PromotionException.buildException(B_IMPORT_PROMOTION_GOODS_ITEM_ERROR,
                            indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_GOODS_ITEM_ERROR));
                    } else {
                        itemRpc = itemRpcs.get(0);
                    }
                }
            }
            if (goodsId == null && itemRpc == null) {
                throw PromotionException.buildException(B_IMPORT_PROMOTION_GOODS_ITEM_NULL,
                    indexMsg + MessageUtils.get(B_IMPORT_PROMOTION_GOODS_ITEM_NULL));
            }
            PromotionRuleGoodsDO ruleGoodsDO = PromotionConvertor.toPromotionRuleGoodsDO(indexMsg, promotionNo, goodsId,
                goodsItemNo, itemRpc, ruleDO, ruleGoodsListMap);
            // 活动规则条件层数据
            List<PromotionRuleConditionDO> conditionDOS = PromotionConvertor.toPromotionRuleConditionDOList(indexMsg,
                itemRpc, conditionMap, dto, specialMap, ruleDO);
        });
        // 活动 规则 条件组装完成，保存数据
        promotionMapper.insertList(promotions);// 保存活动数据
        List<PromotionRuleDO> rules = new ArrayList<>();
        promotionRuleMap.forEach((k, v) -> {
            PromotionDO promotion = promotionMap.get(k);
            v.setPromotionId(promotion.getId());
            rules.add(v);
        });
        List<PromotionDepartmentRelevanceDO> departmentScopes = new ArrayList<>();
        departmentListMap.forEach((k, vs) -> {
            PromotionDO promotionDO = promotionMap.get(k);
            vs.forEach(v -> {
                PromotionDepartmentRelevanceDO departmentRelevanceDO = new PromotionDepartmentRelevanceDO();
                departmentRelevanceDO.setPromotionId(promotionDO.getId());
                departmentRelevanceDO.setDepartmentId(v.getId());
                departmentScopes.add(departmentRelevanceDO);
            });
        });
        if (!CollectionUtils.isEmpty(departmentScopes)) {
            departmentRelevanceMapper.createDepartmentRelevanceList(departmentScopes);
        }
        ruleMapper.insertList(rules);// 保存规则数据
        List<PromotionRuleGoodsDO> promotionRuleGoodss = new ArrayList<>();
        ruleGoodsListMap.forEach((k, vs) -> {
            PromotionRuleDO ruleDO = promotionRuleMap.get(k);
            vs.forEach(v -> {
                v.setPromotionId(ruleDO.getPromotionId());
                v.setPromotionRuleId(ruleDO.getId());
                promotionRuleGoodss.add(v);
            });
        });
        ruleGoodsMapper.insertList(promotionRuleGoodss);
        List<PromotionRuleConditionDO> promotionRuleConditions = new ArrayList<>();
        conditionMap.forEach((k, vs) -> {
            PromotionRuleDO ruleDO = promotionRuleMap.get(k);
            vs.forEach(v -> {
                v.setPromotionId(ruleDO.getPromotionId());
                v.setPromotionRuleId(ruleDO.getId());
                promotionRuleConditions.add(v);
            });
        });
        ruleConditionMapper.insertList(promotionRuleConditions);
        // 保存条件指定价格数据
        if (!CollectionUtils.isEmpty(specialMap)) {
            List<PromotionRuleConditionSpecialDO> specials = new ArrayList<>();
            specialMap.forEach((k, vs) -> {
                vs.forEach(v -> {
                    v.setPromotionId(k.getPromotionId());
                    v.setPromotionRuleConditionId(k.getId());
                    specials.add(v);
                });
            });
            ruleConditionSpecialMapper.insertList(specials);
        }
    }
}
