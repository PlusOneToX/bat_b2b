package com.bat.distributor.service.distributor.convertor;

import static com.bat.distributor.service.common.Constant.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.distributor.api.distributor.dto.data.DistributorCheckContentDTO;
import com.bat.distributor.api.distributor.dto.data.DistributorCheckDTO;
import com.bat.distributor.api.distributor.dto.data.DistributorCheckFlowDTO;
import com.bat.distributor.api.distributor.dto.data.DistributorDTO;
import com.bat.distributor.dao.category.CategoryMapper;
import com.bat.distributor.dao.category.dataobject.CategoryDO;
import com.bat.distributor.dao.distributor.DistributorAddressMapper;
import com.bat.distributor.dao.distributor.DistributorBusinessMapper;
import com.bat.distributor.dao.distributor.DistributorExtendDataMapper;
import com.bat.distributor.dao.distributor.DistributorFinancialMapper;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.dao.group.GroupMapper;
import com.bat.distributor.dao.group.dataobject.GroupDO;
import com.bat.distributor.dao.role.RoleMapper;
import com.bat.distributor.dao.role.dataobject.RoleDO;
import com.bat.distributor.dao.salesarea.SalesAreaMapper;
import com.bat.distributor.dao.salesarea.dataobject.SalesAreaDO;
import com.bat.distributor.dao.trade.TradeMapper;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import com.bat.distributor.service.distributor.executor.DistributorRpcQryExe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.service.common.CommonRpcExe;
import com.bat.distributor.service.common.CommonUtils;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;

@Component
public class DistributorCheckConvertor {

    @Resource
    private DistributorRpcQryExe rpcQryExe;
    @Resource
    private CommonRpcExe commonRpcExe;

    @Resource
    private SalesAreaMapper salesAreaMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TradeMapper tradeMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private DistributorAddressMapper distributorAddressMapper;
    @Resource
    private DistributorBusinessMapper distributorBusinessMapper;
    @Resource
    private DistributorExtendDataMapper distributorExtendDataMapper;
    @Resource
    private DistributorFinancialMapper distributorFinancialMapper;

    public List<DistributorCheckContentDTO> toDistributorCheckContentDTOList(String checkContent,
                                                                             Map<String, String> checkMap) {
        String ln = "\n";
        List<DistributorCheckContentDTO> checkContentDTOS = new ArrayList<>();
        Map<String, List<List<Object>>> changeMap = JSONObject.parseObject(checkContent, Map.class);
        changeMap.forEach((k, vss) -> {
            if (k.equals("addressType") || k.equals("distributorId")) {
                return;
            }
            String field = checkMap.get(k);
            if (StringUtils.isEmpty(field)) {
                field = k;
            }
            if (!CollectionUtils.isEmpty(vss)) {
                String finalField = field;
                vss.forEach(vs -> {
                    DistributorCheckContentDTO checkContentDTO = new DistributorCheckContentDTO();
                    checkContentDTOS.add(checkContentDTO);
                    checkContentDTO.setField(finalField);
                    String beforeContent = null;
                    if (vs.get(0) instanceof String) {
                        beforeContent = vs.get(0).toString();
                    } else {
                        beforeContent = JSONObject.toJSONString(vs.get(0));
                    }
                    String afterContent = null;
                    if (vs.get(1) instanceof String) {
                        afterContent = vs.get(1).toString();
                    } else {
                        afterContent = JSONObject.toJSONString(vs.get(1));
                    }
                    if (StringUtils.isBlank(beforeContent) || beforeContent.equals("null")) {
                        checkContentDTO.setBeforeContent("-");
                    } else {
                        checkContentDTO.setBeforeContent(beforeContent);
                    }
                    if (StringUtils.isBlank(afterContent) || afterContent.equals("null")) {
                        checkContentDTO.setAfterContent("-");
                    } else {
                        checkContentDTO.setAfterContent(afterContent);
                    }
                    // 密码
                    if (k.equals("password")) {
                        checkContentDTO.setBeforeContent("******");
                        checkContentDTO.setAfterContent("******");
                        return;
                    }
                    // 国家、省、市、区
                    if (k.equals("countryId") || k.equals("provinceId") || k.equals("cityId")
                        || k.equals("districtId")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            checkContentDTO.setBeforeContent(rpcQryExe.getRegionById(Integer.valueOf(beforeContent)));
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            checkContentDTO.setAfterContent(rpcQryExe.getRegionById(Integer.valueOf(afterContent)));
                        }
                        return;
                    }
                    // 公司类型
                    if (k.equals("companyType")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            if (Short.valueOf(beforeContent).equals(COMPANY_TYPE_1)) {
                                checkContentDTO.setBeforeContent("公司");
                            } else if (Short.valueOf(beforeContent).equals(COMPANY_TYPE_2)) {
                                checkContentDTO.setBeforeContent("个体商户");
                            } else if (Short.valueOf(beforeContent).equals(COMPANY_TYPE_3)) {
                                checkContentDTO.setBeforeContent("个人");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            if (Short.valueOf(afterContent).equals(COMPANY_TYPE_1)) {
                                checkContentDTO.setAfterContent("公司");
                            } else if (Short.valueOf(afterContent).equals(COMPANY_TYPE_2)) {
                                checkContentDTO.setAfterContent("个体商户");
                            } else if (Short.valueOf(afterContent).equals(COMPANY_TYPE_3)) {
                                checkContentDTO.setAfterContent("个人");
                            }
                        }
                        return;
                    }
                    // 税种
                    if (k.equals("taxType")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            if (Short.valueOf(beforeContent).equals(TAX_TYPE_1)) {
                                checkContentDTO.setBeforeContent("一般纳税人");
                            } else if (Short.valueOf(beforeContent).equals(TAX_TYPE_2)) {
                                checkContentDTO.setBeforeContent("小规模纳税人");
                            } else if (Short.valueOf(beforeContent).equals(TAX_TYPE_3)) {
                                checkContentDTO.setBeforeContent("个人");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            if (Short.valueOf(afterContent).equals(TAX_TYPE_1)) {
                                checkContentDTO.setAfterContent("一般纳税人");
                            } else if (Short.valueOf(afterContent).equals(TAX_TYPE_2)) {
                                checkContentDTO.setAfterContent("小规模纳税人");
                            } else if (Short.valueOf(afterContent).equals(TAX_TYPE_3)) {
                                checkContentDTO.setAfterContent("个人");
                            }
                        }
                        return;
                    }
                    // 是否同步ERP 是否直发 是否有商品导出权限
                    // 是否开启C端模式 是否启用柔性店铺 是否短信提示物流信息
                    // 是否支持在途 是否开启分销 分销活动是否同步 erp余额是否同步
                    if (k.equals("erpFlag") || k.equals("autoDelivery") || k.equals("canExportPrice")
                        || k.equals("customerFlag") || k.equals("rxShopSwitch") || k.equals("logisticsSmsSwitch")
                        || k.equals("onWayFlag") || k.equals("distributionFlag")
                        || k.equals("distributionPromotionFlag") || k.equals("erpBalanceFlag")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            if (Short.valueOf(beforeContent).equals(FLAG_0)) {
                                checkContentDTO.setBeforeContent("否");
                            } else if (Short.valueOf(beforeContent).equals(FLAG_1)) {
                                checkContentDTO.setBeforeContent("是");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            if (Short.valueOf(afterContent).equals(FLAG_0)) {
                                checkContentDTO.setAfterContent("否");
                            } else if (Short.valueOf(afterContent).equals(FLAG_1)) {
                                checkContentDTO.setAfterContent("是");
                            }
                        }
                        return;
                    }
                    // 分销商区域
                    if (k.equals("salesAreaIds")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            List<Integer> salesAreaIds = JSONObject.parseArray(beforeContent, Integer.class);
                            if (!CollectionUtils.isEmpty(salesAreaIds)) {
                                List<SalesAreaDO> salesAreaDOS = salesAreaMapper.listSalesAreaByIds(salesAreaIds);
                                beforeContent =
                                    salesAreaDOS.stream().map(SalesAreaDO::getName).collect(Collectors.joining(","));
                                checkContentDTO.setBeforeContent(beforeContent);
                            } else {
                                checkContentDTO.setBeforeContent("-");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            List<Integer> salesAreaIds = JSONObject.parseObject(afterContent, List.class);
                            if (!CollectionUtils.isEmpty(salesAreaIds)) {
                                List<SalesAreaDO> salesAreaDOS = salesAreaMapper.listSalesAreaByIds(salesAreaIds);
                                afterContent =
                                    salesAreaDOS.stream().map(SalesAreaDO::getName).collect(Collectors.joining(","));
                                checkContentDTO.setAfterContent(afterContent);
                            } else {
                                checkContentDTO.setAfterContent("-");
                            }
                        }
                        return;
                    }
                    // 业务员
                    if (k.equals("salesId")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            UserRpcDTO userRpcDTO = commonRpcExe.getUserById(Integer.valueOf(beforeContent));
                            if (userRpcDTO != null) {
                                checkContentDTO.setBeforeContent(userRpcDTO.getRealName());
                            } else {
                                checkContentDTO.setBeforeContent(beforeContent);
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            UserRpcDTO userRpcDTO = commonRpcExe.getUserById(Integer.valueOf(afterContent));
                            if (userRpcDTO != null) {
                                checkContentDTO.setAfterContent(userRpcDTO.getRealName());
                            } else {
                                checkContentDTO.setAfterContent(afterContent);
                            }
                        }
                        return;
                    }
                    // 分销商分组
                    if (k.equals("distributorGroupIds")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            List<String> idsStr = Arrays.asList(beforeContent.split(","));
                            List<String> newIdsStr = new ArrayList<>();
                            for (String idStr : idsStr) {
                                if (StringUtils.isNotBlank(idStr)) {
                                    newIdsStr.add(idStr);
                                }
                            }
                            List<Integer> idList = newIdsStr.stream().map(Integer::valueOf).collect(Collectors.toList());
                            List<GroupDO> groupDOS = groupMapper.listByDistributorIds(idList);
                            if (groupDOS.size() > 0) {
                                String content = "";
                                for (GroupDO groupDO : groupDOS) {
                                    content = content + "," + groupDO.getName();
                                }
                                if (StringUtils.isNotBlank(content)) {
                                    content = content.substring(1);
                                }
                                checkContentDTO.setBeforeContent(content);
                            } else {
                                checkContentDTO.setBeforeContent(beforeContent);
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            List<String> idsStr = Arrays.asList(afterContent.split(","));
                            List<String> newIdsStr = new ArrayList<>();
                            for (String idStr : idsStr) {
                                if (StringUtils.isNotBlank(idStr)) {
                                    newIdsStr.add(idStr);
                                }
                            }
                            List<Integer> idList = newIdsStr.stream().map(Integer::valueOf).collect(Collectors.toList());
                            List<GroupDO> groupDOS = groupMapper.listByDistributorIds(idList);
                            if (groupDOS.size() > 0) {
                                String content = "";
                                for (GroupDO groupDO : groupDOS) {
                                    content = content + "," + groupDO.getName();

                                }
                                if (StringUtils.isNotBlank(content)) {
                                    content = content.substring(1);
                                }
                                checkContentDTO.setAfterContent(content);
                            } else {
                                checkContentDTO.setAfterContent(afterContent);
                            }
                        }
                        return;
                    }
                    // 分销商分类
                    if (k.equals("distributorCategoryId")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(Integer.valueOf(beforeContent));
                            if (categoryDO != null) {
                                checkContentDTO.setBeforeContent(categoryDO.getName());
                            } else {
                                checkContentDTO.setBeforeContent(beforeContent);
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            CategoryDO categoryDO = categoryMapper.selectByPrimaryKey(Integer.valueOf(afterContent));
                            if (categoryDO != null) {
                                checkContentDTO.setAfterContent(categoryDO.getName());
                            } else {
                                checkContentDTO.setAfterContent(afterContent);
                            }
                        }
                        return;
                    }
                    // 分销商收款条件
                    if (k.equals("tradeId")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            TradeDO tradeDO = tradeMapper.selectByPrimaryKey(Integer.valueOf(beforeContent));
                            if (tradeDO != null) {
                                checkContentDTO.setBeforeContent(tradeDO.getName());
                            } else {
                                checkContentDTO.setBeforeContent(beforeContent);
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            TradeDO tradeDO = tradeMapper.selectByPrimaryKey(Integer.valueOf(afterContent));
                            if (tradeDO != null) {
                                checkContentDTO.setAfterContent(tradeDO.getName());
                            } else {
                                checkContentDTO.setAfterContent(afterContent);
                            }
                        }
                        return;
                    }
                    // 币种
                    if (k.equals("coinType")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            if (Short.valueOf(beforeContent).equals(COIN_TYPE_1)) {
                                checkContentDTO.setBeforeContent("人民币");
                            } else if (Short.valueOf(beforeContent).equals(COIN_TYPE_2)) {
                                checkContentDTO.setBeforeContent("美元");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            if (Short.valueOf(afterContent).equals(COIN_TYPE_1)) {
                                checkContentDTO.setAfterContent("人民币");
                            } else if (Short.valueOf(afterContent).equals(COIN_TYPE_2)) {
                                checkContentDTO.setAfterContent("美元");
                            }
                        }
                        return;
                    }
                    // 参与活动
                    if (k.equals("promotionScope")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            if (Short.valueOf(beforeContent).equals(PROMOTION_SCOPE_0)) {
                                checkContentDTO.setBeforeContent("不参与活动");
                            } else if (Short.valueOf(beforeContent).equals(PROMOTION_SCOPE_1)) {
                                checkContentDTO.setBeforeContent("全部活动");
                            } else if (Short.valueOf(beforeContent).equals(PROMOTION_SCOPE_2)) {
                                checkContentDTO.setBeforeContent("指定活动类型");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            if (Short.valueOf(afterContent).equals(PROMOTION_SCOPE_0)) {
                                checkContentDTO.setAfterContent("不参与活动");
                            } else if (Short.valueOf(afterContent).equals(PROMOTION_SCOPE_1)) {
                                checkContentDTO.setAfterContent("全部活动");
                            } else if (Short.valueOf(afterContent).equals(PROMOTION_SCOPE_2)) {
                                checkContentDTO.setAfterContent("指定活动类型");
                            }
                        }
                        return;
                    }
                    // 可参与活动类型
                    if (k.equals("promotionTypes")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            String[] splits = beforeContent.split(",");
                            String beforeContentStr = "";
                            for (String split : splits) {
                                if (split.equals(PROMOTION_TYPE_1)) {
                                    beforeContentStr = beforeContentStr + "营销活动,";
                                } else if (split.equals(PROMOTION_TYPE_2)) {
                                    beforeContentStr = beforeContentStr + "阶梯活动,";
                                } else if (split.equals(PROMOTION_TYPE_3)) {
                                    beforeContentStr = beforeContentStr + "拼团活动,";
                                }
                            }
                            checkContentDTO.setBeforeContent(beforeContentStr);
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            String[] splits = afterContent.split(",");
                            String afterContentStr = "";
                            for (String split : splits) {
                                if (split.equals(PROMOTION_TYPE_1)) {
                                    afterContentStr = afterContentStr + "营销活动,";
                                } else if (split.equals(PROMOTION_TYPE_2)) {
                                    afterContentStr = afterContentStr + "阶梯活动,";
                                } else if (split.equals(PROMOTION_TYPE_3)) {
                                    afterContentStr = afterContentStr + "拼团活动,";
                                }
                            }
                            checkContentDTO.setAfterContent(afterContentStr);
                        }
                        return;
                    }
                    // C端结算模式
                    if (k.equals("customerMode")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            if (Short.valueOf(beforeContent).equals(CUSTOMER_MODE_1)) {
                                checkContentDTO.setBeforeContent("平台方收款");
                            } else if (Short.valueOf(beforeContent).equals(CUSTOMER_MODE_2)) {
                                checkContentDTO.setBeforeContent("上级收款");
                            } else if (Short.valueOf(beforeContent).equals(CUSTOMER_MODE_3)) {
                                checkContentDTO.setBeforeContent("自己收款");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            if (Short.valueOf(afterContent).equals(CUSTOMER_MODE_1)) {
                                checkContentDTO.setAfterContent("平台方收款");
                            } else if (Short.valueOf(afterContent).equals(CUSTOMER_MODE_2)) {
                                checkContentDTO.setAfterContent("上级收款");
                            } else if (Short.valueOf(afterContent).equals(CUSTOMER_MODE_3)) {
                                checkContentDTO.setAfterContent("自己收款");
                            }
                        }
                        return;
                    }
                    // 分销结算模式
                    if (k.equals("distributionMode")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            if (Short.valueOf(beforeContent).equals(DISTRIBUTION_MODE_1)) {
                                checkContentDTO.setBeforeContent("平台方收款");
                            } else if (Short.valueOf(beforeContent).equals(DISTRIBUTION_MODE_2)) {
                                checkContentDTO.setBeforeContent("上级收款");
                            } else if (Short.valueOf(beforeContent).equals(DISTRIBUTION_MODE_3)) {
                                checkContentDTO.setBeforeContent("自己收款");
                            }
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            if (Short.valueOf(afterContent).equals(DISTRIBUTION_MODE_1)) {
                                checkContentDTO.setAfterContent("平台方收款");
                            } else if (Short.valueOf(afterContent).equals(DISTRIBUTION_MODE_2)) {
                                checkContentDTO.setAfterContent("上级收款");
                            } else if (Short.valueOf(afterContent).equals(DISTRIBUTION_MODE_3)) {
                                checkContentDTO.setAfterContent("自己收款");
                            }
                        }
                        return;
                    }
                    // 价格等级
                    if (k.equals("scalePriceAdd") || k.equals("scalePriceUpdate") || k.equals("scalePriceDelete")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            DistributorOneScalePriceDO scalePriceDO =
                                JSONObject.parseObject(beforeContent, DistributorOneScalePriceDO.class);
                            String scalePriceName =
                                rpcQryExe.getScalePriceByScalePriceId(scalePriceDO.getScalePriceId());
                            String distributionScalePriceName = null;
                            if (scalePriceDO.getDistributionScalePriceId() != null) {
                                distributionScalePriceName =
                                    rpcQryExe.getScalePriceByScalePriceId(scalePriceDO.getDistributionScalePriceId());
                            }
                            // 默认价格等级
                            if ((scalePriceDO.getBrandId() == null || scalePriceDO.getBrandId() == 0)) {
                                beforeContent = "默认购买价格等级:" + scalePriceName;
                                if (StringUtils.isNotBlank(distributionScalePriceName)) {
                                    beforeContent = beforeContent + ln + "默认分销价格等级:" + distributionScalePriceName;
                                }
                            } else {
                                String brandName = rpcQryExe.getBrandById(scalePriceDO.getBrandId());
                                beforeContent = brandName + "购买价格等级：" + scalePriceName;
                                if (StringUtils.isNotBlank(distributionScalePriceName)) {
                                    beforeContent =
                                        beforeContent + ln + brandName + "分销价格等级:" + distributionScalePriceName;
                                }
                            }
                            checkContentDTO.setBeforeContent(beforeContent);
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            DistributorOneScalePriceDO scalePriceDO =
                                JSONObject.parseObject(afterContent, DistributorOneScalePriceDO.class);
                            String scalePriceName =
                                rpcQryExe.getScalePriceByScalePriceId(scalePriceDO.getScalePriceId());
                            String distributionScalePriceName = null;
                            if (scalePriceDO.getDistributionScalePriceId() != null) {
                                distributionScalePriceName =
                                    rpcQryExe.getScalePriceByScalePriceId(scalePriceDO.getDistributionScalePriceId());
                            }
                            // 默认价格等级
                            if ((scalePriceDO.getBrandId() == null || scalePriceDO.getBrandId() == 0)) {
                                afterContent = "默认购买价格等级:" + scalePriceName;
                                if (StringUtils.isNotBlank(distributionScalePriceName)) {
                                    afterContent = afterContent + ln + "默认分销价格等级:" + distributionScalePriceName;
                                }
                            } else {
                                String brandName = rpcQryExe.getBrandById(scalePriceDO.getBrandId());
                                afterContent = brandName + "购买价格等级：" + scalePriceName;
                                if (StringUtils.isNotBlank(distributionScalePriceName)) {
                                    afterContent =
                                        afterContent + ln + brandName + "分销价格等级:" + distributionScalePriceName;
                                }
                            }
                            checkContentDTO.setAfterContent(afterContent);
                        }
                    }
                    // 特价商品
                    if (k.equals("specialGoodsAdd") || k.equals("specialGoodsUpdate")
                        || k.equals("specialGoodsDelete")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            DistributorSpecialGoodsDO specialGoodsDO =
                                JSONObject.parseObject(beforeContent, DistributorSpecialGoodsDO.class);
                            List<Integer> goodsItemIds = new ArrayList<>();
                            goodsItemIds.add(specialGoodsDO.getGoodsItemId());
                            List<GoodsItemRpcDTO> itemRpcDTOS = rpcQryExe.listGoodsItem(goodsItemIds);
                            if (!CollectionUtils.isEmpty(itemRpcDTOS)) {
                                GoodsItemRpcDTO goodsItem = itemRpcDTOS.get(0);
                                beforeContent = goodsItem.getItemName() + "(" + goodsItem.getItemCode() + "):"
                                    + specialGoodsDO.getDistributorPrice();
                            } else {
                                beforeContent =
                                    specialGoodsDO.getGoodsItemId() + ":" + specialGoodsDO.getDistributorPrice();
                            }
                            checkContentDTO.setBeforeContent(beforeContent);
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            DistributorSpecialGoodsDO specialGoodsDO =
                                JSONObject.parseObject(afterContent, DistributorSpecialGoodsDO.class);
                            List<Integer> goodsItemIds = new ArrayList<>();
                            goodsItemIds.add(specialGoodsDO.getGoodsItemId());
                            List<GoodsItemRpcDTO> itemRpcDTOS = rpcQryExe.listGoodsItem(goodsItemIds);
                            if (!CollectionUtils.isEmpty(itemRpcDTOS)) {
                                GoodsItemRpcDTO goodsItem = itemRpcDTOS.get(0);
                                afterContent = goodsItem.getItemName() + "(" + goodsItem.getItemCode() + "):"
                                    + specialGoodsDO.getDistributorPrice();
                            } else {
                                afterContent =
                                    specialGoodsDO.getGoodsItemId() + ":" + specialGoodsDO.getDistributorPrice();
                            }
                            checkContentDTO.setAfterContent(afterContent);
                        }
                    }
                    // 联系人信息
                    if (k.equals("contactsAdd") || k.equals("contactsUpdate") || k.equals("contactsDelete")) {
                        if (StringUtils.isNotBlank(beforeContent) && !beforeContent.equals("null")) {
                            DistributorContactsDO contactsDO =
                                JSONObject.parseObject(beforeContent, DistributorContactsDO.class);
                            beforeContent = contactsDO.getName();
                            if (contactsDO.getSex() != null && contactsDO.getSex() != 0) {
                                if (contactsDO.getSex().equals(SEX_1)) {
                                    beforeContent = beforeContent + "," + "男";
                                } else if (contactsDO.getSex().equals(SEX_2)) {
                                    beforeContent = beforeContent + "," + "女";
                                }
                            }
                            if (StringUtils.isNotBlank(contactsDO.getPhone())) {
                                beforeContent = beforeContent + "," + contactsDO.getPhone();
                            }
                            if (contactsDO.getOwnerFlag() != null) {
                                if (contactsDO.getOwnerFlag().equals(OWNER_FLAG_1)) {
                                    beforeContent = beforeContent + "," + "是主账号";
                                } else {
                                    beforeContent = beforeContent + "," + "非主账号";
                                }
                            }
                            if (contactsDO.getRoleId() != null) {
                                RoleDO roleDO = roleMapper.selectByPrimaryKey(contactsDO.getRoleId());
                                if (roleDO != null) {
                                    beforeContent = beforeContent + "," + roleDO.getName();
                                } else {
                                    beforeContent = beforeContent + "," + contactsDO.getRoleId();
                                }
                            }
                            checkContentDTO.setBeforeContent(beforeContent);
                        }
                        if (StringUtils.isNotBlank(afterContent) && !afterContent.equals("null")) {
                            DistributorContactsDO contactsDO =
                                JSONObject.parseObject(afterContent, DistributorContactsDO.class);
                            afterContent = contactsDO.getName();
                            if (contactsDO.getSex() != null && contactsDO.getSex() != 0) {
                                if (contactsDO.getSex().equals(SEX_1)) {
                                    afterContent = afterContent + "," + "男";
                                } else if (contactsDO.getSex().equals(SEX_2)) {
                                    afterContent = afterContent + "," + "女";
                                }
                            }
                            if (StringUtils.isNotBlank(contactsDO.getPhone())) {
                                afterContent = afterContent + "," + contactsDO.getPhone();
                            }
                            if (contactsDO.getOwnerFlag() != null) {
                                if (contactsDO.getOwnerFlag().equals(OWNER_FLAG_1)) {
                                    afterContent = afterContent + "," + "是主账号";
                                } else {
                                    afterContent = afterContent + "," + "非主账号";
                                }
                            }
                            if (contactsDO.getRoleId() != null) {
                                RoleDO roleDO = roleMapper.selectByPrimaryKey(contactsDO.getRoleId());
                                if (roleDO != null) {
                                    afterContent = afterContent + "," + roleDO.getName();
                                } else {
                                    afterContent = afterContent + "," + contactsDO.getRoleId();
                                }
                            }
                            checkContentDTO.setAfterContent(afterContent);
                        }
                    }
                });
            }
        });
        return checkContentDTOS;
    }

    public DistributorCheckDTO toDistributorCheckDTO(DistributorCheckDO checkDO,
                                                     List<DistributorCheckFlowDO> checkFlowDOS, DistributorDTO distributorDTO,
                                                     List<DistributorCheckContentDTO> checkContentDTOS) {
        DistributorCheckDTO checkDTO = new DistributorCheckDTO();
        BeanUtils.copyProperties(checkDO, checkDTO);
        List<DistributorCheckFlowDTO> checkFlows = new ArrayList<>();
        checkDTO.setCheckFlows(checkFlows);
        checkFlowDOS.forEach(checkFlowDO -> {
            DistributorCheckFlowDTO checkFlowDTO = new DistributorCheckFlowDTO();
            BeanUtils.copyProperties(checkFlowDO, checkFlowDTO);
            checkFlows.add(checkFlowDTO);
        });
        if (distributorDTO != null) {
            checkDTO.setDistributor(distributorDTO);
        }
        if (!CollectionUtils.isEmpty(checkContentDTOS)) {
            checkDTO.setCheckContents(checkContentDTOS);
        }
        return checkDTO;
    }

    public DistributorDO toDistributorDO(DistributorDO distributorDO, Map<String, List<List<Object>>> changeMap,
        Date date) {
        Map<String, List<String>> changeMapStr = new HashMap<>();
        changeMap.forEach((k, vss) -> {
            if (vss != null && vss.size() == 1) {
                List<String> vStrs = new ArrayList<>();
                List<Object> vs = vss.get(0);
                vs.forEach(v -> {
                    if (v instanceof String) {
                        vStrs.add(v.toString());
                    } else {
                        vStrs.add(JSONObject.toJSONString(v));
                    }
                });
                changeMapStr.put(k, vStrs);
            }
        });
        DistributorDO afterDistributorDO = CommonUtils.changeFieldValues(distributorDO, changeMapStr);
        if (afterDistributorDO == null) {
            afterDistributorDO = distributorDO;
        } else {
            afterDistributorDO.setUpdateTime(date);
        }
        Integer distributorId = afterDistributorDO.getId();
        // 分销商销售区域
        List<List<Object>> salesAreass = changeMap.get("salesAreaIds");
        if (salesAreass != null && !CollectionUtils.isEmpty(salesAreass)
            && !CollectionUtils.isEmpty(salesAreass.get(0))) {
            List<DistributorSalesAreaDO> doList =
                DistributorConvertor.toDistributorSalesAreaList(salesAreass.get(0), distributorId);
            distributorDO.setSalesAreaDOS(doList);
        }
        // 分销商地址
        List<DistributorAddressDO> addressDOS =
            distributorAddressMapper.listByDistributorIdAndAddressType(distributorId, ADDRESS_TYPE_1);
        DistributorAddressDO beforeAddressDO = new DistributorAddressDO();
        if (!CollectionUtils.isEmpty(addressDOS)) {
            beforeAddressDO = addressDOS.get(0);
        } else {
            beforeAddressDO.setDefaultFlag(DEFAULT_FLAG_0);
            beforeAddressDO.setDistributorId(afterDistributorDO.getId());
            beforeAddressDO.setCreateTime(date);
        }
        beforeAddressDO.setUpdateTime(date);
        DistributorAddressDO afterAddressDO = CommonUtils.changeFieldValues(beforeAddressDO, changeMapStr);
        afterDistributorDO.setAfterAddressDO(afterAddressDO);
        // 业务数据
        DistributorBusinessDO beforeBusinessDO = distributorBusinessMapper.getByDistributorId(distributorId);
        if (beforeBusinessDO == null) {
            beforeBusinessDO = new DistributorBusinessDO();
            beforeBusinessDO.setDistributorId(afterDistributorDO.getId());
            beforeBusinessDO.setCreateTime(date);
        }
        beforeBusinessDO.setUpdateTime(date);
        DistributorBusinessDO afterBusinessDO = CommonUtils.changeFieldValues(beforeBusinessDO, changeMapStr);
        afterDistributorDO.setBusinessDO(afterBusinessDO);
        // 业务数据价格等级
        List<DistributorOneScalePriceDO> addOneScalePriceDOS = new ArrayList<>();
        List<DistributorOneScalePriceDO> updateOneScalePriceDOS = new ArrayList<>();
        List<DistributorOneScalePriceDO> deleteOneScalePriceDOS = new ArrayList<>();
        afterDistributorDO.setAddScalePriceDOS(addOneScalePriceDOS);
        afterDistributorDO.setUpdateScalePriceDOS(updateOneScalePriceDOS);
        afterDistributorDO.setDeleteScalePriceDOS(deleteOneScalePriceDOS);
        List<List<Object>> scalePriceAdds = changeMap.get("scalePriceAdd");
        if (!CollectionUtils.isEmpty(scalePriceAdds)) {
            scalePriceAdds.forEach(scalePriceAdd -> {
                DistributorOneScalePriceDO scalePriceDO = JSONObject
                    .parseObject(JSONObject.toJSONString(scalePriceAdd.get(1)), DistributorOneScalePriceDO.class);
                addOneScalePriceDOS.add(scalePriceDO);
            });
        }
        List<List<Object>> scalePriceUpdates = changeMap.get("scalePriceUpdate");
        if (!CollectionUtils.isEmpty(scalePriceUpdates)) {
            scalePriceUpdates.forEach(scalePriceUpdate -> {
                DistributorOneScalePriceDO scalePriceDO = JSONObject
                    .parseObject(JSONObject.toJSONString(scalePriceUpdate.get(1)), DistributorOneScalePriceDO.class);
                updateOneScalePriceDOS.add(scalePriceDO);
            });
        }
        List<List<Object>> scalePriceDeletes = changeMap.get("scalePriceDelete");
        if (!CollectionUtils.isEmpty(scalePriceDeletes)) {
            scalePriceDeletes.forEach(scalePriceDelete -> {
                DistributorOneScalePriceDO scalePriceDO = JSONObject
                    .parseObject(JSONObject.toJSONString(scalePriceDelete.get(0)), DistributorOneScalePriceDO.class);
                deleteOneScalePriceDOS.add(scalePriceDO);
            });
        }
        // 联系人
        List<DistributorContactsDO> addContacts = new ArrayList<>();
        List<DistributorContactsDO> updateContacts = new ArrayList<>();
        List<DistributorContactsDO> deleteContacts = new ArrayList<>();
        afterDistributorDO.setAddContacts(addContacts);
        afterDistributorDO.setUpdateContacts(updateContacts);
        afterDistributorDO.setDeleteContacts(deleteContacts);
        List<List<Object>> contactsAdds = changeMap.get("contactsAdd");
        if (!CollectionUtils.isEmpty(contactsAdds)) {
            contactsAdds.forEach(contactsAdd -> {
                DistributorContactsDO contactsDO =
                    JSONObject.parseObject(JSONObject.toJSONString(contactsAdd.get(1)), DistributorContactsDO.class);
                addContacts.add(contactsDO);
            });
        }
        List<List<Object>> contactsUpdates = changeMap.get("contactsUpdate");
        if (!CollectionUtils.isEmpty(contactsUpdates)) {
            contactsUpdates.forEach(contactsUpdate -> {
                DistributorContactsDO contactsDO =
                    JSONObject.parseObject(JSONObject.toJSONString(contactsUpdate.get(1)), DistributorContactsDO.class);
                updateContacts.add(contactsDO);
            });
        }
        List<List<Object>> contactsDeletes = changeMap.get("contactsDelete");
        if (!CollectionUtils.isEmpty(contactsDeletes)) {
            contactsDeletes.forEach(contactsDelete -> {
                DistributorContactsDO contactsDO =
                    JSONObject.parseObject(JSONObject.toJSONString(contactsDelete.get(0)), DistributorContactsDO.class);
                deleteContacts.add(contactsDO);
            });
        }
        // 分销商扩展数据
        DistributorExtendDataDO beforeExtendDataDO = distributorExtendDataMapper.getByDistributorId(distributorId);
        if (beforeExtendDataDO == null) {
            beforeExtendDataDO = new DistributorExtendDataDO();
            beforeExtendDataDO.setDistributorId(afterDistributorDO.getId());
            beforeExtendDataDO.setCreateTime(date);
        }
        beforeAddressDO.setUpdateTime(date);
        DistributorExtendDataDO afterExtendDataDO = CommonUtils.changeFieldValues(beforeExtendDataDO, changeMapStr);
        afterDistributorDO.setAfterExtendDataDO(afterExtendDataDO);
        afterDistributorDO.setBeforeExtendDataDO(beforeExtendDataDO);
        // 分销财务数据
        DistributorFinancialDO beforeFinancialDO = distributorFinancialMapper.getByDistributorId(distributorId);
        if (beforeFinancialDO == null) {
            beforeFinancialDO = new DistributorFinancialDO();
            beforeFinancialDO.setDistributorId(afterDistributorDO.getId());
            beforeFinancialDO.setCreateTime(date);
        }
        beforeFinancialDO.setUpdateTime(date);
        DistributorFinancialDO afterFinancialDO = CommonUtils.changeFieldValues(beforeFinancialDO, changeMapStr);
        afterDistributorDO.setAfterFinancialDO(afterFinancialDO);
        // 分销商商品特价
        List<DistributorSpecialGoodsDO> addSpecialGoodsDOs = new ArrayList<>();
        List<DistributorSpecialGoodsDO> updateSpecialGoodsDOs = new ArrayList<>();
        List<DistributorSpecialGoodsDO> deleteSpecialGoodsDOs = new ArrayList<>();
        afterDistributorDO.setAddSpecialGoodsDOS(addSpecialGoodsDOs);
        afterDistributorDO.setUpdateSpecialGoodsDOS(updateSpecialGoodsDOs);
        afterDistributorDO.setDeleteSpecialGoodsDOS(deleteSpecialGoodsDOs);
        List<List<Object>> specialGoodsAdds = changeMap.get("specialGoodsAdd");
        if (!CollectionUtils.isEmpty(specialGoodsAdds)) {
            specialGoodsAdds.forEach(specialGoodsAdd -> {
                DistributorSpecialGoodsDO specialGoodsDO = JSONObject
                    .parseObject(JSONObject.toJSONString(specialGoodsAdd.get(1)), DistributorSpecialGoodsDO.class);
                addSpecialGoodsDOs.add(specialGoodsDO);
            });
        }
        List<List<Object>> specialGoodsUpdates = changeMap.get("specialGoodsUpdate");
        if (!CollectionUtils.isEmpty(specialGoodsUpdates)) {
            specialGoodsUpdates.forEach(specialGoodsUpdate -> {
                DistributorSpecialGoodsDO specialGoodsDO = JSONObject
                    .parseObject(JSONObject.toJSONString(specialGoodsUpdate.get(1)), DistributorSpecialGoodsDO.class);
                updateSpecialGoodsDOs.add(specialGoodsDO);
            });
        }
        List<List<Object>> specialGoodsDeletes = changeMap.get("specialGoodsDelete");
        if (!CollectionUtils.isEmpty(specialGoodsDeletes)) {
            specialGoodsDeletes.forEach(specialGoodsDelete -> {
                DistributorSpecialGoodsDO specialGoodsDO = JSONObject
                    .parseObject(JSONObject.toJSONString(specialGoodsDelete.get(0)), DistributorSpecialGoodsDO.class);
                deleteSpecialGoodsDOs.add(specialGoodsDO);
            });
        }
        return afterDistributorDO;
    }
}
