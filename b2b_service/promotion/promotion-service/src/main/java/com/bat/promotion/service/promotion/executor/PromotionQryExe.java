package com.bat.promotion.service.promotion.executor;

import static com.bat.promotion.service.common.Constant.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.promotion.convertor.PromotionConvertor;
import com.bat.promotion.service.promotion.dto.ImportPromotionExcelDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.goods.goods.dto.data.GoodsRpcDTO;
import com.bat.dubboapi.promotion.dto.GoodsItemPriceRpcQry;
import com.bat.dubboapi.promotion.dto.data.GoodsItemPromotionPriceRpcDTO;
import com.bat.dubboapi.system.organization.dto.data.DepartmentRpcDTO;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.promotion.dto.PromotionExportQry;
import com.bat.promotion.api.promotion.dto.PromotionListQry;
import com.bat.promotion.api.promotion.dto.data.PromotionDTO;
import com.bat.promotion.api.promotion.dto.data.PromotionListDTO;
import com.bat.promotion.api.promotion.dto.data.PromotionRuleDTO;
import com.bat.promotion.dao.promotion.*;
import com.bat.promotion.dao.promotion.dataobject.*;
import com.bat.promotion.service.common.CommonUtils;
import com.bat.promotion.service.common.PromotionConfig;
import com.bat.promotion.service.common.rpc.PromotionRpcQryExe;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/20 11:15
 */
@Component
public class PromotionQryExe {

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
    private PromotionRpcQryExe rpcQryExe;
    @Resource
    private PromotionConfig promotionConfig;

    /**
     * 根据搜索条件查找促销活动列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<PromotionListDTO> listPromotion(PromotionListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<PromotionDO> promotionDOS = null;
        // 搜索内容类型，1 促销活动名称 2 商品编号 3 货品编号 4 分销商 5 规则标签 6 条件标签 搜索
        if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && (qry.getContentType().equals(CONTENT_TYPE_2) || qry.getContentType().equals(CONTENT_TYPE_3))) {
            promotionDOS = promotionMapper.listPromotionByGoods(qryMap);
        } else if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_4)) {
            promotionDOS = promotionMapper.listPromotionByDistributorName(qryMap);
        } else if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_5)) {
            promotionDOS = promotionMapper.listPromotionByRuleName(qryMap);
        } else if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_6)) {
            promotionDOS = promotionMapper.listPromotionByConditionName(qryMap);
        } else {
            promotionDOS = promotionMapper.listPromotion(qryMap);
        }
        PageInfo pageInfo = new PageInfo(promotionDOS);
        List<PromotionListDTO> dtos = PromotionConvertor.toPromotionListDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据id查询促销活动详情
     * 
     * @param qry
     * @return
     */
    public PromotionDTO getPromotion(BaseId qry) {
        return getPromotion(qry.getId());
    }

    public PromotionDTO getPromotion(Integer id) {
        PromotionDO promotionDO = promotionMapper.selectByPrimaryKey(id);
        if (promotionDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_NULL);
        }
        PromotionDTO dto = PromotionConvertor.toPromotionDTO(promotionDO);
        getPromotionScope(dto);
        getPromotionScopeNo(dto);
        getPromotionRule(dto);
        return dto;
    }

    /**
     * 获取促销活动规则
     * 
     * @param dto
     */
    private void getPromotionRule(PromotionDTO dto) {
        List<PromotionRuleDO> ruleDOS = ruleMapper.listByPromotionId(dto.getId());
        List<PromotionRuleGoodsDO> ruleGoodsDOS = ruleGoodsMapper.listByPromotionId(dto.getId());
        List<PromotionRuleConditionDO> conditionDOS = ruleConditionMapper.listByPromotionId(dto.getId());
        List<PromotionRuleConditionSpecialDO> specialDOS = ruleConditionSpecialMapper.listByPromotionId(dto.getId());
        List<PromotionRuleConditionPresentDO> presentDOS = ruleConditionPresentMapper.listByPromotionId(dto.getId());
        List<Integer> goodsIds = new ArrayList<>();
        List<Integer> goodsItemIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ruleGoodsDOS)) {
            List<Integer> goodsCollect = ruleGoodsDOS.stream()
                .filter(ruleGoodsDO -> ruleGoodsDO.getItemId() == null || ruleGoodsDO.getItemId() == 0)
                .map(PromotionRuleGoodsDO::getGoodsId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(goodsCollect)) {
                goodsIds.addAll(goodsCollect);
            }
            List<Integer> itemCollect = ruleGoodsDOS.stream()
                .filter(ruleGoodsDO -> ruleGoodsDO.getItemId() != null && ruleGoodsDO.getItemId() != 0)
                .map(PromotionRuleGoodsDO::getItemId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(itemCollect)) {
                goodsItemIds.addAll(itemCollect);
            }
        }
        if (!CollectionUtils.isEmpty(specialDOS)) {
            goodsItemIds.addAll(
                specialDOS.stream().map(PromotionRuleConditionSpecialDO::getItemId).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(presentDOS)) {
            goodsItemIds.addAll(
                presentDOS.stream().map(PromotionRuleConditionPresentDO::getItemId).collect(Collectors.toList()));
        }
        List<GoodsRpcDTO> goodsRpcDTOS = null;
        if (!CollectionUtils.isEmpty(goodsIds)) {
            goodsRpcDTOS = rpcQryExe.listGoods(goodsIds);
        }
        List<GoodsItemRpcDTO> itemRpcDTOS = null;
        if (!CollectionUtils.isEmpty(goodsItemIds)) {
            itemRpcDTOS = rpcQryExe.listGoodsItem(goodsItemIds);
        }
        List<PromotionRuleDTO> ruleDTOS = PromotionConvertor.toPromotionRuleDTOList(ruleDOS, ruleGoodsDOS, conditionDOS,
            specialDOS, presentDOS, goodsRpcDTOS, itemRpcDTOS);
        dto.setRules(ruleDTOS);
    }

    /**
     * 获取促销活动可视范围数据
     * 
     * @param dto
     */
    private void getPromotionScope(PromotionDTO dto) {
        Integer promotionId = dto.getId();
        if (dto.getDistributorScope() == null) {
            return;
        }
        if (dto.getDistributorScope().equals(SCOPE_SCALE_PRICE)) {
            dto.setScalePriceIds(scalePriceRelevanceMapper.listScalePriceRelevanceIdByPromotionId(promotionId));
        } else if (dto.getDistributorScope().equals(SCOPE_DISTRIBUTOR)) {
            List<PromotionDistributorRelevanceDO> distributorRelevanceDOS =
                distributorRelevanceMapper.listDistributorRelevanceByPromotionId(promotionId);
            dto.setDistributors(PromotionConvertor.toPromotionDistributorScopeDTOList(distributorRelevanceDOS));
        } else if (dto.getDistributorScope().equals(SCOPE_DEPARTMENT)) {
            dto.setDepartmentIds(departmentRelevanceMapper.listDepartmentRelevanceIdByPromotionId(promotionId));
        } else if (dto.getDistributorScope().equals(SCOPE_ADMIN)) {
            dto.setAdminIds(adminRelevanceMapper.listAdminRelevanceIdByPromotionId(promotionId));
        }
    }

    /**
     * 获取促销活动不可视范围数据
     * 
     * @param dto
     */
    private void getPromotionScopeNo(PromotionDTO dto) {
        Integer promotionId = dto.getId();
        if (dto.getDistributorScopeNo() == null) {
            return;
        }
        if (dto.getDistributorScopeNo().equals(SCOPE_SCALE_PRICE)) {
            dto.setScalePriceNoIds(scalePriceRelevanceNoMapper.listScalePriceRelevanceNoIdByPromotionId(promotionId));
        } else if (dto.getDistributorScopeNo().equals(SCOPE_DISTRIBUTOR)) {
            List<Integer> distributorIds =
                distributorRelevanceNoMapper.listDistributorRelevanceNoIdByPromotionId(promotionId);
            List<DistributorRpcDTO> rpcDTOS = rpcQryExe.listDistributorNameRpcDTO(distributorIds);
            dto.setDistributorNos(PromotionConvertor.toPromotionDistributorScopeDTOListForRpc(rpcDTOS));
        } else if (dto.getDistributorScopeNo().equals(SCOPE_DEPARTMENT)) {
            dto.setDepartmentNoIds(departmentRelevanceNoMapper.listDepartmentRelevanceNoIdByPromotionId(promotionId));
        } else if (dto.getDistributorScopeNo().equals(SCOPE_ADMIN)) {
            dto.setAdminNoIds(adminRelevanceNoMapper.listAdminRelevanceNoIdByPromotionId(promotionId));
        }
    }

    /**
     * 计算促销活动价格
     * 
     * @param goodsPromotionPrices
     * @return
     */
    public List<GoodsItemPromotionPriceRpcDTO> goodsPromotionPrices(List<GoodsItemPriceRpcQry> goodsPromotionPrices,
        Date time) {
        List<Integer> goodsIds = new ArrayList<>();
        List<Integer> conditionIds = new ArrayList<>();
        goodsPromotionPrices.forEach(goodsPromotionPrice -> {
            if (!goodsIds.contains(goodsPromotionPrice.getGoodsId())) {
                goodsIds.add(goodsPromotionPrice.getGoodsId());
            }
            if (goodsPromotionPrice.getGoodsPromotionId() != null
                && !conditionIds.contains(goodsPromotionPrice.getGoodsPromotionId())) {
                conditionIds.add(goodsPromotionPrice.getGoodsPromotionId());
            }
            if (goodsPromotionPrice.getOrderPromotionId() != null
                && !conditionIds.contains(goodsPromotionPrice.getOrderPromotionId())) {
                conditionIds.add(goodsPromotionPrice.getOrderPromotionId());
            }
        });
        List<PromotionRuleConditionDO> conditionDOS = ruleConditionMapper.listByIds(conditionIds);
        List<Integer> promotionRuleIds = new ArrayList<>();
        List<Integer> promotionIds = new ArrayList<>();
        conditionDOS.forEach(conditionDO -> {
            if (!promotionRuleIds.contains(conditionDO.getPromotionRuleId())) {
                promotionRuleIds.add(conditionDO.getPromotionRuleId());
            }
            if (!promotionIds.contains(conditionDO.getPromotionId())) {
                promotionIds.add(conditionDO.getPromotionId());
            }
        });
        List<PromotionRuleDO> ruleDOS = ruleMapper.listByIds(promotionRuleIds);
        List<PromotionRuleConditionSpecialDO> specialDOS = ruleConditionSpecialMapper.listByConditionIds(conditionIds);
        List<PromotionRuleConditionPresentDO> presentDOS = ruleConditionPresentMapper.listByConditionIds(conditionIds);
        List<PromotionDO> promotionDOS = promotionMapper.listByIds(promotionIds);
        List<GoodsItemPromotionPriceRpcDTO> rpcDTOS = PromotionConvertor.toGoodsItemPromotionPriceRpcDTOList(
            goodsPromotionPrices, promotionDOS, ruleDOS, conditionDOS, specialDOS, presentDOS, time);
        return rpcDTOS;
    }

    /**
     * 获取活动导入模板URL
     * 
     * @return
     */
    public String getTempUrl() {
        return promotionConfig.getImportTemp();
    }

    /**
     * 促销活动导出
     * 
     * @param qry
     * @return
     */
    public HSSFWorkbook promotionExport(PromotionExportQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        List<PromotionDO> promotionDOS = promotionMapper.listPromotionExport(qryMap);
        Map<Integer, List<PromotionRuleDO>> ruleDOSMap = new HashMap<>();
        Map<Integer, List<PromotionRuleConditionDO>> conditionDOSMap = new HashMap<>();
        Map<Integer, List<PromotionRuleGoodsDO>> ruleGoodsDOSMap = new HashMap<>();
        Map<Integer, List<PromotionRuleConditionSpecialDO>> specialDOSMap = new HashMap<>();
        Map<Integer, List<PromotionDepartmentRelevanceDO>> departmentRelevanceDOSMap = new HashMap<>();
        List<ImportPromotionExcelDTO> promotionExcelDTOS = new ArrayList<>();
        List<DepartmentRpcDTO> departments = new ArrayList<>();
        if (!CollectionUtils.isEmpty(promotionDOS)) {
            List<Integer> promotionIds = promotionDOS.stream().map(PromotionDO::getId).collect(Collectors.toList());
            List<PromotionRuleDO> ruleDOS = ruleMapper.listByPromotionIds(promotionIds);
            List<PromotionRuleGoodsDO> ruleGoodsDOS = ruleGoodsMapper.listByPromotionIds(promotionIds);
            List<PromotionRuleConditionDO> conditionDOS = ruleConditionMapper.listByPromotionIds(promotionIds);
            if (!CollectionUtils.isEmpty(ruleDOS)) {
                ruleDOSMap = ruleDOS.stream().collect(Collectors.groupingBy(PromotionRuleDO::getPromotionId));
            }
            if (!CollectionUtils.isEmpty(conditionDOS)) {
                conditionDOSMap =
                    conditionDOS.stream().collect(Collectors.groupingBy(PromotionRuleConditionDO::getPromotionRuleId));
            }
            if (!CollectionUtils.isEmpty(ruleGoodsDOS)) {
                ruleGoodsDOSMap =
                    ruleGoodsDOS.stream().collect(Collectors.groupingBy(PromotionRuleGoodsDO::getPromotionRuleId));
            }
            List<PromotionRuleConditionSpecialDO> specialDOS =
                ruleConditionSpecialMapper.listByPromotionIds(promotionIds);
            if (!CollectionUtils.isEmpty(specialDOS)) {
                specialDOSMap = specialDOS.stream()
                    .collect(Collectors.groupingBy(PromotionRuleConditionSpecialDO::getPromotionRuleConditionId));
            }
            List<PromotionDepartmentRelevanceDO> departmentRelevanceDOS =
                departmentRelevanceMapper.listByPromotionIds(promotionIds);

            if (!CollectionUtils.isEmpty(departmentRelevanceDOS)) {
                List<Integer> departmentIds = departmentRelevanceDOS.stream()
                    .map(PromotionDepartmentRelevanceDO::getDepartmentId).distinct().collect(Collectors.toList());
                departments = rpcQryExe.getDepartmentsByIds(departmentIds);
                departmentRelevanceDOSMap = departmentRelevanceDOS.stream()
                    .collect(Collectors.groupingBy(PromotionDepartmentRelevanceDO::getPromotionId));
            }
            promotionExcelDTOS = PromotionConvertor.toImportPromotionExcelDTOList(promotionDOS, ruleDOSMap,
                conditionDOSMap, ruleGoodsDOSMap, specialDOSMap, departmentRelevanceDOSMap, departments);
        }
        List<String> titles = new ArrayList<>();
        List<Map<String, Object>> maps = new ArrayList<>();
        String[] head = {"* 活动序号", "* 商品编码/条形码/货品编号", "* 规则对象（单个商品填2，单个货品填3）", "* 活动类型(阶梯活动填2)", "* 活动名称", "活动英文名称",
            "* 活动描述", "*在途是否参与（1表示参与，0表示不参与）", "*是否支持货品混批（填1为是，填0为否）", "* 满减方式（指定价格填1，打折填2，减免填3）", "* 标签1", " 英文标签1",
            "* 条件1", "* 满减额度1", "标签2", "英文标签2", "条件2", "满减额度2", "标签3", " 英文标签3", "条件3", "满减额度3", "* 开始时间", "* 结束时间",
            "提前天数（未填写为不提前）", "活动范围（多选，逗号(英文半角)隔开，如（1,2,3）,如果不填默认全部分销商）"};
        if (!CollectionUtils.isEmpty(promotionExcelDTOS)) {
            promotionExcelDTOS.forEach(promotionExcelDTO -> {
                Map<String, Object> stringObjectMap = new Hashtable<>();
                for (String title : head) {
                    if (title.equals(head[0])) {
                        stringObjectMap.put(title, promotionExcelDTO.getPromotionNo());
                    } else if (title.equals(head[1])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getGoodsItemNo() == null ? "" : promotionExcelDTO.getGoodsItemNo());
                    } else if (title.equals(head[2])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getRuleType() == null ? "" : promotionExcelDTO.getRuleType());
                    } else if (title.equals(head[3])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getPromoType() == null ? "" : promotionExcelDTO.getPromoType());
                    } else if (title.equals(head[4])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getName() == null ? "" : promotionExcelDTO.getName());
                    } else if (title.equals(head[5])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getNameEn() == null ? "" : promotionExcelDTO.getNameEn());
                    } else if (title.equals(head[6])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getPromoDesc() == null ? "" : promotionExcelDTO.getPromoDesc());
                    } else if (title.equals(head[7])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getOnWayFlag() == null ? "" : promotionExcelDTO.getOnWayFlag());
                    } else if (title.equals(head[8])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getAddUpFlag() == null ? "" : promotionExcelDTO.getAddUpFlag());
                    } else if (title.equals(head[9])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getConditionType() == null ? "" : promotionExcelDTO.getConditionType());
                    } else if (title.equals(head[10])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getConditionName1() == null ? "" : promotionExcelDTO.getConditionName1());
                    } else if (title.equals(head[11])) {
                        stringObjectMap.put(title, promotionExcelDTO.getConditionNameEn1() == null ? ""
                            : promotionExcelDTO.getConditionNameEn1());
                    } else if (title.equals(head[12])) {
                        stringObjectMap.put(title, promotionExcelDTO.getOneBuyCount1() == null ? ""
                            : promotionExcelDTO.getOneBuyCount1() == null);
                    } else if (title.equals(head[13])) {
                        stringObjectMap.put(title, promotionExcelDTO.getDiscountReduction1() == null ? ""
                            : promotionExcelDTO.getDiscountReduction1());
                    } else if (title.equals(head[14])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getConditionName2() == null ? "" : promotionExcelDTO.getConditionName2());
                    } else if (title.equals(head[15])) {
                        stringObjectMap.put(title, promotionExcelDTO.getConditionNameEn2() == null ? ""
                            : promotionExcelDTO.getConditionNameEn2());
                    } else if (title.equals(head[16])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getOneBuyCount2() == null ? "" : promotionExcelDTO.getOneBuyCount2());
                    } else if (title.equals(head[17])) {
                        stringObjectMap.put(title, promotionExcelDTO.getDiscountReduction2() == null ? ""
                            : promotionExcelDTO.getDiscountReduction2());
                    } else if (title.equals(head[18])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getConditionName3() == null ? "" : promotionExcelDTO.getConditionName3());
                    } else if (title.equals(head[19])) {
                        stringObjectMap.put(title, promotionExcelDTO.getConditionNameEn3() == null ? ""
                            : promotionExcelDTO.getConditionNameEn3());
                    } else if (title.equals(head[20])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getOneBuyCount3() == null ? "" : promotionExcelDTO.getOneBuyCount3());
                    } else if (title.equals(head[21])) {
                        stringObjectMap.put(title, promotionExcelDTO.getDiscountReduction3() == null ? ""
                            : promotionExcelDTO.getDiscountReduction3());
                    } else if (title.equals(head[22])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getStartTime() == null ? "" : promotionExcelDTO.getStartTime());
                    } else if (title.equals(head[23])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getEndTime() == null ? "" : promotionExcelDTO.getEndTime());
                    } else if (title.equals(head[24])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getAdvanceDay() == null ? "" : promotionExcelDTO.getAdvanceDay());
                    } else if (title.equals(head[25])) {
                        stringObjectMap.put(title,
                            promotionExcelDTO.getDepartmentSStr() == null ? "" : promotionExcelDTO.getDepartmentSStr());
                    }
                    if (!titles.contains(title)) {
                        titles.add(title);
                    }
                }
                maps.add(stringObjectMap);
            });
        }
        HSSFWorkbook sheets = CommonUtils.excelOut(titles, promotionExcelDTOS.size(), maps, "zh");
        return sheets;
    }
}
