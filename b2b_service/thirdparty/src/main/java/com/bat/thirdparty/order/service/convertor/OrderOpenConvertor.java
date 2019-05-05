package com.bat.thirdparty.order.service.convertor;

import java.util.ArrayList;
import java.util.List;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.distributor.ThirdDistributorPlatformApiConstant;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.flexible.AddressConstant;
import com.bat.thirdparty.common.order.OrderDeliveryConstant;
import com.bat.thirdparty.common.util.BeanUtils;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.common.util.Sha1Handler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.platform.api.DistributorSysPlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.distributor.platform.dto.SysPlatformRpcDTO;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.order.api.FlexibleOrderServiceRpc;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnCodeQry;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.dubboapi.flexible.order.dto.OrderGoodsDiyParamDTO;
import com.bat.dubboapi.flexible.product.api.ProductCategoryServiceRpc;
import com.bat.dubboapi.flexible.product.dto.ProductCategoryRpcQryDTO;
import com.bat.dubboapi.order.delivery.dto.OrderLogisticsSyncParam;
import com.bat.dubboapi.order.order.dto.*;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.system.region.api.SystemRegionComparisonServiceRpc;
import com.bat.dubboapi.system.region.api.SystemRegionServiceRpc;
import com.bat.dubboapi.system.region.dto.data.RegionComparisonRpcDTO;
import com.bat.dubboapi.system.region.dto.data.RegionRpcDTO;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;
import com.bat.thirdparty.order.api.dto.OrderBaseOnCodeCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.common.AddressQry;
import com.bat.thirdparty.order.api.dto.common.UserInfoQry;
import com.bat.thirdparty.order.api.dto.zhaolianji.ZhaoLiangJiCustomInfo;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.service.executor.OrderQryExe;

@Component
public class OrderOpenConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderOpenConvertor.class);

    @Autowired
    private OrderQryExe orderQryExe;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private ProductCategoryServiceRpc productCategoryServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private FlexibleOrderServiceRpc flexibleOrderServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemRegionServiceRpc systemRegionServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemRegionComparisonServiceRpc systemRegionComparisonServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorSysPlatformServiceRpc distributorSysPlatformServiceRpc;

    /**
     * 参数转换为同步第三方物流信息
     * 
     * @param syncParam
     * @return
     */
    public static OrderLogistics toOrderLogistics(OrderLogisticsSyncParam syncParam) {
        OrderLogistics orderLogistics = new OrderLogistics();
        // 物流传过去的是B2B的单号
        orderLogistics.setOtherOrderNo(syncParam.getOrderNo());
        orderLogistics.setExpressTime(syncParam.getExpressTime());
        orderLogistics.setExpressName(syncParam.getExpressName());
        orderLogistics.setExpressNo(syncParam.getExpressNo());
        orderLogistics.setExpressCode(syncParam.getExpressCode());
        orderLogistics.setDistributorId(syncParam.getDistributorId());
        return orderLogistics;
    }

    /**
     * 转换为第三方下单明细
     * 
     * @param orderGoodsDiyParamDTOS
     * @return
     */
    public static List<OrderGoodsCmd> toOrderGoodsCmdList(List<OrderGoodsDiyParamDTO> orderGoodsDiyParamDTOS) {
        List<OrderGoodsCmd> orderGoodsCmdList = new ArrayList<>();
        for (int x = 0; x < orderGoodsDiyParamDTOS.size(); x++) {
            OrderGoodsDiyParamDTO orderGoodsDiyParamDTO = orderGoodsDiyParamDTOS.get(x);
            OrderGoodsDiyCmd orderGoodsDiyCmd = BeanUtils.copy(orderGoodsDiyParamDTO, OrderGoodsDiyCmd.class);
            OrderGoodsCmd orderGoodsCmd = new OrderGoodsCmd();
            orderGoodsCmd.setDiy(orderGoodsDiyCmd);
            orderGoodsCmd.setItemCount(orderGoodsDiyParamDTO.getItemCount());
            orderGoodsCmd.setItemCode(orderGoodsDiyParamDTO.getItemCode());
            orderGoodsCmd.setSerialNumber(x + 1);
            orderGoodsCmdList.add(orderGoodsCmd);
        }
        return orderGoodsCmdList;
    }

    /**
     * 参数转换为下单参数 基于ID2
     * 
     * @param orderBaseOnIdCmd
     * @param businessLogDO
     * @return
     */
    public OrderInfoCmd toOrderInfoBaseId(OrderBaseOnIdCmd orderBaseOnIdCmd, OrderBusinessLogDO businessLogDO) {
        OrderInfoCmd orderInfoCmd = new OrderInfoCmd();
        orderInfoCmd.setDistributorId(businessLogDO.getDistributorId());
        orderInfoCmd.setOrderSource(businessLogDO.getPlatform());
        orderInfoCmd.setCurrencyType("CNY");
        // 固定区间结算
        orderInfoCmd.setPayWay((short)3);
        orderInfoCmd.setRemark(orderBaseOnIdCmd.getRemark());
        orderInfoCmd.setOrderThirdpartyNo(orderBaseOnIdCmd.getOrderNo());
        // 转换为订单商品明细
        List<OrderGoodsCmd> orderGoodsCmdList = toOrderGoodsBaseOnId(orderBaseOnIdCmd.getOrderDetails());
        orderInfoCmd.setGoodss(orderGoodsCmdList);
        // 组装收货地址
        OrderDeliveryCmd orderDeliveryCmd =
            toOrderDeliveryCmd(orderBaseOnIdCmd.getAddress(), orderBaseOnIdCmd.getUserInfo());
        orderInfoCmd.setDelivery(orderDeliveryCmd);
        List<OrderLogisticsCmd> logisticsCmdList = listOrderLogisticsCmdByCondition(orderInfoCmd.getDistributorId(),
            orderGoodsCmdList.get(0).getDiy().getManufactors(), "2", orderDeliveryCmd.getCityId());
        orderInfoCmd.setLogisticss(logisticsCmdList);

        return orderInfoCmd;
    }

    public OrderInfoCmd toOrderInfoBaseOnCode(OrderBaseOnCodeCmd orderBaseOnCodeCmd, OrderBusinessLogDO businessLogDO) {
        OrderInfoCmd orderInfoCmd = new OrderInfoCmd();
        orderInfoCmd.setDistributorId(businessLogDO.getDistributorId());
        orderInfoCmd.setOrderSource(businessLogDO.getPlatform());
        orderInfoCmd.setCurrencyType("CNY");
        // 固定区间结算
        orderInfoCmd.setPayWay((short)3);
        orderInfoCmd.setRemark(orderBaseOnCodeCmd.getRemark());
        orderInfoCmd.setOrderThirdpartyNo(orderBaseOnCodeCmd.getOrderNo());
        // 转换为订单商品明细
        List<OrderGoodsCmd> orderGoodsCmdList = toOrderGoodsBaseOnCode(orderBaseOnCodeCmd.getOrderDetails());
        orderInfoCmd.setGoodss(orderGoodsCmdList);
        // 组装收货地址
        OrderDeliveryCmd orderDeliveryCmd =
            toOrderDeliveryCmd(orderBaseOnCodeCmd.getAddress(), orderBaseOnCodeCmd.getUserInfo());
        orderInfoCmd.setDelivery(orderDeliveryCmd);
        List<OrderLogisticsCmd> logisticsCmdList = listOrderLogisticsCmdByCondition(orderInfoCmd.getDistributorId(),
            orderGoodsCmdList.get(0).getDiy().getManufactors(), "2", orderDeliveryCmd.getCityId());
        orderInfoCmd.setLogisticss(logisticsCmdList);

        return orderInfoCmd;
    }

    /**
     * 根据分销商id和工厂编码、使用范围、城市id查询柔性配送方式列表
     * 
     * @param distributorId
     * @param manufactors
     * @param useRange
     * @param cityId
     * @return
     */
    public List<OrderLogisticsCmd> listOrderLogisticsCmdByCondition(Integer distributorId, String manufactors,
        String useRange, Integer cityId) {
        LogisticsRpcQry logisticsRpcQry = new LogisticsRpcQry();
        logisticsRpcQry.setDistributorId(distributorId);
        logisticsRpcQry.setUseRange(useRange);
        logisticsRpcQry.setManufactors(manufactors);
        logisticsRpcQry.setCityId(cityId);
        logisticsRpcQry.setEnable((short)1);
        List<OrderLogisticsCmd> logisticsCmdList = new ArrayList<>();

        com.bat.dubboapi.system.common.Response<List<LogisticsRpcDTO>> response =
            systemLogisticsServiceRpc.listLogisticsFromRpcByParams(logisticsRpcQry);
        if (response == null) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw new ThirdPartyOpenApiException(response.getErrMessage());
        }
        List<LogisticsRpcDTO> logisticsRpcDTOList = response.getData();
        if (logisticsRpcDTOList == null || logisticsRpcDTOList.size() == 0) {
            throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.T_DISTRIBUTOR_LOGISTICS_RELEVANCE_NULL);
        }
        logisticsRpcDTOList.stream().forEach(logisticsRpcDTO -> {
            OrderLogisticsCmd cmd = new OrderLogisticsCmd();
            cmd.setLogisticsId(logisticsRpcDTO.getId());
            cmd.setLogisticsName(logisticsRpcDTO.getName());
            cmd.setLogisticsType(OrderDeliveryConstant.ORDER_LOGISTRICS_TYPE_DIY);
            cmd.setManufactors(manufactors);
            logisticsCmdList.add(cmd);
        });
        return logisticsCmdList;
    }

    public OrderDeliveryCmd toOrderDeliveryCmd(AddressQry address, UserInfoQry userInfo) {
        OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
        RegionRpcDTO province = getRegionByLevelAndName((short)2, address.getProvince());
        if (province == null) {
            // 找不到省信息
            LOGGER.error("找不到省信息:{}", JSON.toJSONString(address));
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_ADDRESS_PROVINCE_ERROR));
        }

        // 省
        orderDeliveryCmd.setProvinceName(province.getRegionName());
        orderDeliveryCmd.setProvinceId(province.getId());
        /*else{
            RegionComparisonRpcDTO provinceComparison = getRegionComparisonRpcDTOByParentIdAndName(37, address.getProvince());
        
        }*/
        // 反过来查询国家
        RegionRpcDTO country = getRegionById(province.getParentId());
        orderDeliveryCmd.setCountryId(country.getId());
        orderDeliveryCmd.setCountryName(country.getRegionName());
        // 市
        RegionRpcDTO city = getRegionByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
        if (city != null) {
            orderDeliveryCmd.setCityId(city.getId());
            orderDeliveryCmd.setCityName(address.getCity());
        } else {
            RegionComparisonRpcDTO comparisonRpcDTO =
                getRegionComparisonRpcDTOByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
            LOGGER.info("查询到的城市对照关系{}，省份id{},城市{}", JSON.toJSONString(comparisonRpcDTO),
                orderDeliveryCmd.getProvinceId(), address.getCity());
            if (comparisonRpcDTO == null) {
                StringBuilder errorCode=new StringBuilder();
                //海南省处理过滤
                boolean isSuccess = dealForHaiNan(orderDeliveryCmd, address, errorCode);

                //河南省处理过滤
                if (!isSuccess) {
                    errorCode=new StringBuilder();
                    isSuccess = dealForHeNan(orderDeliveryCmd, address, errorCode);
                }

                //湖北省处理过滤
                if(!isSuccess){
                    errorCode=new StringBuilder();
                    isSuccess = dealForHuBei(orderDeliveryCmd, address, errorCode);
                }

                //新疆维吾尔自治区处理过滤
                if(!isSuccess){
                    errorCode=new StringBuilder();
                    isSuccess = dealForXinJiang(orderDeliveryCmd, address, errorCode);
                }

                if(!isSuccess){
                    throw new ThirdPartyOpenApiException(MessageUtils.get(errorCode.toString()));
                }

            }else {
                orderDeliveryCmd.setCityId(comparisonRpcDTO.getRegionId());
                orderDeliveryCmd.setCityName(comparisonRpcDTO.getRegionName());
            }
        }

        // 
        RegionRpcDTO area = getRegionByParentIdAndName(orderDeliveryCmd.getCityId(), address.getArea());
        LOGGER.info("area:{}", JSONObject.toJSONString(area));
        if (area != null) {
            LOGGER.info("orderDeliveryCmd:{}", JSONObject.toJSONString(orderDeliveryCmd));
            orderDeliveryCmd.setDistrictId(area.getId());
            orderDeliveryCmd.setDistrictName(area.getRegionName());
        } else {
            RegionComparisonRpcDTO comparisonRpcDTO =
                getRegionComparisonRpcDTOByParentIdAndName(orderDeliveryCmd.getCityId(), address.getArea());
            LOGGER.info("查询到的区对照关系{}，id{},区{}", JSON.toJSONString(comparisonRpcDTO), orderDeliveryCmd.getCityId(),
                address.getArea());
            if (comparisonRpcDTO == null) {
                // 找不到区信息
                LOGGER.error("找不到区信息:{}", JSON.toJSONString(address));
                orderDeliveryCmd.setDistrictId(null);
                orderDeliveryCmd.setDistrictName( address.getArea());
            }else {
                orderDeliveryCmd.setDistrictId(comparisonRpcDTO.getRegionId());
                orderDeliveryCmd.setDistrictName(comparisonRpcDTO.getRegionName());
            }
        }
        orderDeliveryCmd.setAddress(address.getDetail());

        orderDeliveryCmd.setUserName(userInfo.getUserName());
        orderDeliveryCmd.setMobile(userInfo.getPhoneNumber());
        orderDeliveryCmd.setPhone(userInfo.getPhoneNumber());
        orderDeliveryCmd.setDeliveryType((short)2);

        return orderDeliveryCmd;
    }

    /**
     * 处理海南省地址
     * @param orderDeliveryCmd
     * @param address
     */
    private boolean dealForHaiNan(OrderDeliveryCmd orderDeliveryCmd, AddressQry address,StringBuilder errorCode) {
        //不是海南省不处理
        if (orderDeliveryCmd.getProvinceId() != AddressConstant.HAINAN_PROVINCE_ID) {
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }

        //判断是否命中地址
        boolean match=false;
        //遍历海南省直辖县
        for(String directlyCounty:AddressConstant.HAINAN_DIRECTLY_COUNTY_LIST){
            if (address.getCity().contains(directlyCounty)) {
                match=true;
                break;
            }
        }
        //最终没匹配则异常并退出
        if(!match){
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }
        //重新定义地址
        String newCity = AddressConstant.PROVINCE_DIRECTLY_COUNTY;
        String newArea = address.getCity();
        String newDetail = address.getArea() + address.getDetail();
        LOGGER.info("旧的地址:{}",JSONObject.toJSONString(address));
        address.setCity(newCity);
        address.setArea(newArea);
        address.setDetail(newDetail);
        LOGGER.info("新的地址:{}",JSONObject.toJSONString(address));
        RegionRpcDTO city = getRegionByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
        if (city != null) {
            orderDeliveryCmd.setCityId(city.getId());
            orderDeliveryCmd.setCityName(address.getCity());
        } else {
            RegionComparisonRpcDTO comparisonRpcDTO =
                    getRegionComparisonRpcDTOByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
            LOGGER.info("组装地址后查询到的城市对照关系{}，省份id{},城市{}", JSON.toJSONString(comparisonRpcDTO),
                    orderDeliveryCmd.getProvinceId(), address.getCity());
            if (comparisonRpcDTO == null) {
                LOGGER.info("组装地址后找不到对应市信息:{}",JSONObject.toJSONString(address));
                errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
                return false;
            }
            orderDeliveryCmd.setCityId(comparisonRpcDTO.getRegionId());
            orderDeliveryCmd.setCityName(comparisonRpcDTO.getRegionName());
        }
        return true;
    }


    /**
     * 处理河南省地址
     * @param orderDeliveryCmd
     * @param address
     */
    private boolean dealForHeNan(OrderDeliveryCmd orderDeliveryCmd, AddressQry address,StringBuilder errorCode) {
        //不是河南省不处理
        if (orderDeliveryCmd.getProvinceId() != AddressConstant.HENAN_PROVINCE_ID) {
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }
        //不是济源市不处理
        if (!address.getCity().contains(AddressConstant.HENAN_JIYUAN_NAME)) {
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }
        //重新定义地址
        String newCity = AddressConstant.PROVINCE_DIRECTLY_COUNTY;
        String newArea = address.getCity();
        String newDetail = address.getArea() + address.getDetail();
        LOGGER.info("旧的地址:{}",JSONObject.toJSONString(address));
        address.setCity(newCity);
        address.setArea(newArea);
        address.setDetail(newDetail);
        LOGGER.info("新的地址:{}",JSONObject.toJSONString(address));
        RegionRpcDTO city = getRegionByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
        if (city != null) {
            orderDeliveryCmd.setCityId(city.getId());
            orderDeliveryCmd.setCityName(address.getCity());
        } else {
            RegionComparisonRpcDTO comparisonRpcDTO =
                    getRegionComparisonRpcDTOByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
            LOGGER.info("组装地址后查询到的城市对照关系{}，省份id{},城市{}", JSON.toJSONString(comparisonRpcDTO),
                    orderDeliveryCmd.getProvinceId(), address.getCity());
            if (comparisonRpcDTO == null) {
                LOGGER.info("组装地址后找不到对应市信息:{}",JSONObject.toJSONString(address));
                errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
                return false;
            }
            orderDeliveryCmd.setCityId(comparisonRpcDTO.getRegionId());
            orderDeliveryCmd.setCityName(comparisonRpcDTO.getRegionName());
        }
        return true;
    }

    /**
     * 处理湖北省地址
     * @param orderDeliveryCmd
     * @param address
     */
    private boolean dealForHuBei(OrderDeliveryCmd orderDeliveryCmd, AddressQry address,StringBuilder errorCode) {
        //不是湖北省不处理
        if (orderDeliveryCmd.getProvinceId() != AddressConstant.HUBEI_PROVINCE_ID) {
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }
        //判断是否命中地址
        boolean match=false;
        //遍历湖北省直辖县
        for(String directlyCounty:AddressConstant.HUBEI_DIRECTLY_COUNTY_LIST){
            if (address.getCity().contains(directlyCounty)) {
                match=true;
                break;
            }
        }
        //最终没匹配则异常并退出
        if(!match){
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }
        //重新定义地址
        String newCity = AddressConstant.PROVINCE_DIRECTLY_COUNTY;
        String newArea = address.getCity();
        String newDetail = address.getArea() + address.getDetail();
        LOGGER.info("旧的地址:{}",JSONObject.toJSONString(address));
        address.setCity(newCity);
        address.setArea(newArea);
        address.setDetail(newDetail);
        LOGGER.info("新的地址:{}",JSONObject.toJSONString(address));
        RegionRpcDTO city = getRegionByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
        if (city != null) {
            orderDeliveryCmd.setCityId(city.getId());
            orderDeliveryCmd.setCityName(address.getCity());
        } else {
            RegionComparisonRpcDTO comparisonRpcDTO =
                    getRegionComparisonRpcDTOByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
            LOGGER.info("组装地址后查询到的城市对照关系{}，省份id{},城市{}", JSON.toJSONString(comparisonRpcDTO),
                    orderDeliveryCmd.getProvinceId(), address.getCity());
            if (comparisonRpcDTO == null) {
                LOGGER.info("组装地址后找不到对应市信息:{}",JSONObject.toJSONString(address));
                errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
                return false;
            }
            orderDeliveryCmd.setCityId(comparisonRpcDTO.getRegionId());
            orderDeliveryCmd.setCityName(comparisonRpcDTO.getRegionName());
        }
        return true;
    }

    /**
     * 处理新疆地址
     * @param orderDeliveryCmd
     * @param address
     */
    private boolean dealForXinJiang(OrderDeliveryCmd orderDeliveryCmd, AddressQry address,StringBuilder errorCode) {
        //不是新疆不处理
        if (orderDeliveryCmd.getProvinceId() != AddressConstant.XINJIANG_REGION_ID) {
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }
        //判断是否命中地址
        boolean match=false;
        //遍历新疆自治区直辖县
        for(String directlyCounty:AddressConstant.XINJIANG_REGION_COUNTY_LIST){
            if (address.getCity().contains(directlyCounty)) {
                match=true;
                break;
            }
        }
        //最终没匹配则异常并退出
        if(!match){
            errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
            return false;
        }
        //重新定义地址
        String newCity = AddressConstant.REGION_DIRECTLY_COUNTY;
        String newArea = address.getCity();
        String newDetail = address.getArea() + address.getDetail();
        LOGGER.info("旧的地址:{}",JSONObject.toJSONString(address));
        address.setCity(newCity);
        address.setArea(newArea);
        address.setDetail(newDetail);
        LOGGER.info("新的地址:{}",JSONObject.toJSONString(address));
        RegionRpcDTO city = getRegionByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
        if (city != null) {
            orderDeliveryCmd.setCityId(city.getId());
            orderDeliveryCmd.setCityName(address.getCity());
        } else {
            RegionComparisonRpcDTO comparisonRpcDTO =
                    getRegionComparisonRpcDTOByParentIdAndName(orderDeliveryCmd.getProvinceId(), address.getCity());
            LOGGER.info("组装地址后查询到的城市对照关系{}，省份id{},城市{}", JSON.toJSONString(comparisonRpcDTO),
                    orderDeliveryCmd.getProvinceId(), address.getCity());
            if (comparisonRpcDTO == null) {
                LOGGER.info("组装地址后找不到对应市信息:{}",JSONObject.toJSONString(address));
                errorCode.append(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_ERROR);
                return false;
            }
            orderDeliveryCmd.setCityId(comparisonRpcDTO.getRegionId());
            orderDeliveryCmd.setCityName(comparisonRpcDTO.getRegionName());
        }
        return true;
    }

    /**
     * 根据父id和名称模糊匹配
     * 
     * @param parentId
     * @param name
     * @return
     */
    private RegionRpcDTO getRegionByParentIdAndName(Integer parentId, String name) {
        com.bat.dubboapi.system.common.Response<List<RegionRpcDTO>> response =
            systemRegionServiceRpc.listRegionByParentIdAndRegionName(parentId, name);
        if (response == null) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw new ThirdPartyOpenApiException(response.getErrMessage());
        }
        return response.getData() == null || response.getData().size() == 0 ? null : response.getData().get(0);
    }

    /**
     * 根据父id和名称模糊匹配
     * 
     * @param id
     * @return
     */
    private RegionRpcDTO getRegionById(Integer id) {
        com.bat.dubboapi.system.common.Response<RegionRpcDTO> response = systemRegionServiceRpc.getRegionById(id);
        if (response == null) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw new ThirdPartyOpenApiException(response.getErrMessage());
        }
        return response.getData();
    }

    /**
     * 根据父id和名称模糊匹配
     * 
     * @param level
     * @param name
     * @return
     */
    private RegionRpcDTO getRegionByLevelAndName(Short level, String name) {
        com.bat.dubboapi.system.common.Response<List<RegionRpcDTO>> response =
            systemRegionServiceRpc.listRegionByLevelAndRegionName(level, name);
        if (response == null) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw new ThirdPartyOpenApiException(response.getErrMessage());
        }
        return response.getData() == null || response.getData().size() == 0 ? null : response.getData().get(0);
    }

    /**
     * 根据父id和名称模糊匹配别名
     * 
     * @param parentId
     * @param name
     * @return
     */
    private RegionComparisonRpcDTO getRegionComparisonRpcDTOByParentIdAndName(Integer parentId, String name) {
        com.bat.dubboapi.system.common.Response<List<RegionComparisonRpcDTO>> response =
            systemRegionComparisonServiceRpc.listRegionByParentIdAndAnotherName(parentId, name);
        if (response == null) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_SYSTEM_SERVICE_EXCEPTION);
        }
        if (!response.isSuccess()) {
            throw new ThirdPartyOpenApiException(response.getErrMessage());
        }
        return response.getData() == null || response.getData().size() == 0 ? null : response.getData().get(0);
    }

    private List<OrderGoodsCmd> toOrderGoodsBaseOnId(List<OrderDetailBaseOnIdQry> orderDetails) {
        Response<List<OrderGoodsDiyParamDTO>> flexibleResp =
            flexibleOrderServiceRpc.validThirdOrderParamBaseId(orderDetails);
        LOGGER.info("参数校验完返回:{}", JSON.toJSONString(flexibleResp));
        if (flexibleResp == null) {
            throw new ThirdPartyOpenApiException(
                MessageUtils.get(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION));
        }
        if (!flexibleResp.isSuccess()) {
            throw new ThirdPartyOpenApiException(flexibleResp.getErrMessage());
        }
        List<OrderGoodsDiyParamDTO> goodsDiyParamDTOList = flexibleResp.getData();
        List<OrderGoodsCmd> orderGoodsCmdList = new ArrayList<>();
        for (int x = 0; x < goodsDiyParamDTOList.size(); x++) {
            OrderGoodsDiyParamDTO diyParamDTO = goodsDiyParamDTOList.get(x);
            OrderGoodsCmd orderGoodsCmd = new OrderGoodsCmd();
            orderGoodsCmd.setSerialNumber(x + 1);
            orderGoodsCmd.setItemCode(orderDetails.get(x).getSkuNo());
            orderGoodsCmd.setItemCount(orderDetails.get(x).getCount());
            OrderGoodsDiyCmd orderGoodsDiyCmd = BeanUtils.copy(diyParamDTO, OrderGoodsDiyCmd.class);
            orderGoodsDiyCmd.setManufactors(diyParamDTO.getManufactors());
            orderGoodsCmd.setDiy(orderGoodsDiyCmd);
            orderGoodsCmd.setSalePrice(orderDetails.get(x).getSalePrice());
            orderGoodsCmdList.add(orderGoodsCmd);
        }
        return orderGoodsCmdList;
    }

    private List<OrderGoodsCmd> toOrderGoodsBaseOnCode(List<OrderDetailBaseOnCodeQry> orderDetails) {
        Response<List<OrderGoodsDiyParamDTO>> flexibleResp =
            flexibleOrderServiceRpc.validThirdOrderParamBaseOnCode(orderDetails);
        LOGGER.info("参数校验完返回:{}", JSON.toJSONString(flexibleResp));
        if (flexibleResp == null) {
            throw new ThirdPartyOpenApiException(
                MessageUtils.get(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION));
        }
        if (!flexibleResp.isSuccess()) {
            throw new ThirdPartyOpenApiException(flexibleResp.getErrMessage());
        }
        List<OrderGoodsDiyParamDTO> goodsDiyParamDTOList = flexibleResp.getData();
        List<OrderGoodsCmd> orderGoodsCmdList = new ArrayList<>();
        for (int x = 0; x < goodsDiyParamDTOList.size(); x++) {
            OrderGoodsDiyParamDTO diyParamDTO = goodsDiyParamDTOList.get(x);
            OrderGoodsCmd orderGoodsCmd = new OrderGoodsCmd();
            orderGoodsCmd.setSerialNumber(x + 1);
            orderGoodsCmd.setItemCode(diyParamDTO.getItemCode());
            orderGoodsCmd.setItemCount(orderDetails.get(x).getCount());
            OrderGoodsDiyCmd orderGoodsDiyCmd = BeanUtils.copy(diyParamDTO, OrderGoodsDiyCmd.class);
            orderGoodsDiyCmd.setManufactors(diyParamDTO.getManufactors());
            orderGoodsCmd.setDiy(orderGoodsDiyCmd);
            orderGoodsCmdList.add(orderGoodsCmd);
        }
        return orderGoodsCmdList;
    }

    private ProductCategoryRpcQryDTO getByMaterialIdOrMaterialNo(Integer materialId, String materialNo) {
        Response<ProductCategoryRpcQryDTO> productCategoryResp =
            productCategoryServiceRpc.getByMaterialIdOrMaterialNo(materialId, materialNo);
        if (productCategoryResp == null) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_FLEXIBLE_SERVICE_EXCEPTION);
        }
        if (!productCategoryResp.isSuccess()) {
            throw new ThirdPartyOpenApiException(productCategoryResp.getErrMessage());
        }
        return productCategoryResp.getData();
    }

    /**
     * 转换为第三方参数
     * 
     * @param distributorId
     *            分销商id
     * @param distributorPlatformApiRpcDTO
     *            分销商url配置
     * @param orderDetailBaseOnIdQry
     *            明细
     * @param zhaoLiangJiCustomInfo
     *            专属明细
     */
    public void toProvisionalOrderParam(Integer distributorId,
        DistributorPlatformApiRpcDTO distributorPlatformApiRpcDTO, OrderDetailBaseOnIdQry orderDetailBaseOnIdQry,
        ZhaoLiangJiCustomInfo zhaoLiangJiCustomInfo) {

        orderDetailBaseOnIdQry.setBrandName(orderDetailBaseOnIdQry.getBrandName().trim());
        orderDetailBaseOnIdQry.setModelName(orderDetailBaseOnIdQry.getModelName().trim());
        if (distributorId == 1783 || distributorId == 2560
            || (distributorPlatformApiRpcDTO.getDevelopSource() != null
                && ThirdDistributorPlatformApiConstant.DevelopSourceZhaoLianJi
                    .equals(distributorPlatformApiRpcDTO.getDevelopSource()))) {
            zhaoLiangJiCustomInfo = BeanUtils.copy(orderDetailBaseOnIdQry, ZhaoLiangJiCustomInfo.class);
            /*            json = JSON.toJSONString(customInfo, SerializerFeature.WriteMapNullValue);
            signStr = stringBuilder.append(zhaoliangjiAppKey).append(json).append(currentTimeMillis);
            httpPost.setHeader("appId", zhaoliangjiAppId);*/
            return;
        }
        if (distributorPlatformApiRpcDTO.getDevelopSource() != null
            && ThirdDistributorPlatformApiConstant.DevelopSourceJiuQuanQiuTx
                .equals(distributorPlatformApiRpcDTO.getDevelopSource())) {
            // 云南全球通信开发的

            // 去除锦城潮品的的userId
            orderDetailBaseOnIdQry.getExtendField().setUserId(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderType(null);
            orderDetailBaseOnIdQry.getExtendField().setUserProductId(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderId(null);
            orderDetailBaseOnIdQry.getExtendField().setBackUrl(null);
            orderDetailBaseOnIdQry.getExtendField().setSkuCodeAndQtys(null);
            orderDetailBaseOnIdQry.getExtendField().setAttach(null);
            if (org.apache.commons.lang3.StringUtils.isBlank(orderDetailBaseOnIdQry.getExtendField().getUrlParam())) {
                throw ThirdPartyException.buildException("链接不能为空");
            }
            return;
        }
        if (distributorPlatformApiRpcDTO.getDevelopSource() != null
            && ThirdDistributorPlatformApiConstant.DevelopSourceJinChengCP
                .equals(distributorPlatformApiRpcDTO.getDevelopSource())) {
            // 针对锦诚潮品（B站）、加用户id
            // 去除全球通信的urlParam
            orderDetailBaseOnIdQry.getExtendField().setUrlParam(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderType(null);
            orderDetailBaseOnIdQry.getExtendField().setUserProductId(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderId(null);
            orderDetailBaseOnIdQry.getExtendField().setBackUrl(null);
            orderDetailBaseOnIdQry.getExtendField().setSkuCodeAndQtys(null);
            orderDetailBaseOnIdQry.getExtendField().setAttach(null);
            if (org.apache.commons.lang3.StringUtils.isBlank(orderDetailBaseOnIdQry.getExtendField().getUserId())) {
                throw ThirdPartyException.buildException("下单用户id不能为空");
            }
            return;
        }
        if (distributorPlatformApiRpcDTO.getDevelopSource() != null
            && ThirdDistributorPlatformApiConstant.DevelopSourceBaiLi
                .equals(distributorPlatformApiRpcDTO.getDevelopSource())) {
            // 百礼挑壹
            // 去除锦城潮品的的userId
            orderDetailBaseOnIdQry.getExtendField().setUserId(null);
            orderDetailBaseOnIdQry.getExtendField().setUrlParam(null);
            orderDetailBaseOnIdQry.getExtendField().setUserProductId(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderId(null);
            orderDetailBaseOnIdQry.getExtendField().setBackUrl(null);
            orderDetailBaseOnIdQry.getExtendField().setSkuCodeAndQtys(null);
            orderDetailBaseOnIdQry.getExtendField().setAttach(null);
            if (org.apache.commons.lang3.StringUtils.isBlank(orderDetailBaseOnIdQry.getExtendField().getOrderType())) {
                throw ThirdPartyException.buildException("订单类型不能为空");
            }
            return;
        }
        if (distributorPlatformApiRpcDTO.getDevelopSource() != null
            && ThirdDistributorPlatformApiConstant.DevelopSourceHywh
                .equals(distributorPlatformApiRpcDTO.getDevelopSource())) {
            // 恒裕文化传媒
            orderDetailBaseOnIdQry.getExtendField().setUrlParam(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderType(null);
            orderDetailBaseOnIdQry.getExtendField().setSkuCodeAndQtys(null);
            orderDetailBaseOnIdQry.getExtendField().setAttach(null);
            if (org.apache.commons.lang3.StringUtils.isBlank(orderDetailBaseOnIdQry.getExtendField().getUserId())) {
                throw ThirdPartyException.buildException("userId不能为空");
            }
            return;
        }

        if (distributorPlatformApiRpcDTO.getDevelopSource() != null
            && ThirdDistributorPlatformApiConstant.DevelopSourceHwVmall
                .equals(distributorPlatformApiRpcDTO.getDevelopSource())) {
            orderDetailBaseOnIdQry.getExtendField().setUserId(null);
            orderDetailBaseOnIdQry.getExtendField().setUrlParam(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderType(null);
            orderDetailBaseOnIdQry.getExtendField().setUserProductId(null);
            orderDetailBaseOnIdQry.getExtendField().setOrderId(null);
            orderDetailBaseOnIdQry.getExtendField().setBackUrl(null);
            orderDetailBaseOnIdQry.getExtendField().setAttach(null);
            if (org.apache.commons.lang3.StringUtils
                .isBlank(orderDetailBaseOnIdQry.getExtendField().getSkuCodeAndQtys())) {
                throw ThirdPartyException.buildException("skuCodeAndQtys不能为空");
            }
            return;
        }

        if (distributorPlatformApiRpcDTO.getDevelopSource() != null
            && ThirdDistributorPlatformApiConstant.DevelopSourceSzAdp
                .equals(distributorPlatformApiRpcDTO.getDevelopSource())) {
            if (orderDetailBaseOnIdQry.getExtendField() != null) {
                // 深圳爱达普
                orderDetailBaseOnIdQry.getExtendField().setUserId(null);
                orderDetailBaseOnIdQry.getExtendField().setUrlParam(null);
                orderDetailBaseOnIdQry.getExtendField().setOrderType(null);
                orderDetailBaseOnIdQry.getExtendField().setUserProductId(null);
                orderDetailBaseOnIdQry.getExtendField().setOrderId(null);
                orderDetailBaseOnIdQry.getExtendField().setBackUrl(null);
                orderDetailBaseOnIdQry.getExtendField().setSkuCodeAndQtys(null);
            }
            return;
        }
    }

    /**
     * 获取签名
     * 
     * @param orderSource
     * @param distributorId
     * @param requestBody
     * @param timestamp
     * @return
     */
    public String getSign(String orderSource, String distributorId, String requestBody, String timestamp) {
        com.bat.dubboapi.distributor.common.Response<SysPlatformRpcDTO> platformRpcDTOResponse =
            distributorSysPlatformServiceRpc.getByPlatformAndDistributorId(orderSource,
                Integer.parseInt(distributorId));
        if (platformRpcDTOResponse == null || !platformRpcDTOResponse.isSuccess()) {

            throw new ThirdPartyOpenApiException("访问客户服务异常");
        }
        SysPlatformRpcDTO platformRpcDTO = platformRpcDTOResponse.getData();
        if (platformRpcDTO == null) {
            throw new ThirdPartyOpenApiException("该分销商尚未在平台进行配置、请与客服沟通处理");
        }
        // 2. 校验签名是否正确；
        String signStr = distributorId + platformRpcDTO.getAppKey() + orderSource + requestBody + timestamp;

        LOGGER.info("订单验签字符串：" + signStr);
        String checkSignature = Sha1Handler.encryption(signStr);
        return checkSignature;
    }

    public static void main(String[] args) {
        OrderBaseOnIdCmd orderBaseOnIdCmd = new OrderBaseOnIdCmd();

        orderBaseOnIdCmd.setOrderNo("122001");
        String jsonString = JSON.toJSONString(orderBaseOnIdCmd);
        System.out.println(JSON.toJSONString(jsonString));
        System.out.println(Sha1Handler.encryption(jsonString));
    }
}
