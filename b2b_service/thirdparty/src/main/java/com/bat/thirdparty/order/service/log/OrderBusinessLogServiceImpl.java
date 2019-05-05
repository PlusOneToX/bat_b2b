package com.bat.thirdparty.order.service.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.error.log.OrderBusinessLogErrorCode;
import com.bat.thirdparty.common.log.OrderBusinessLogConstant;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.common.order.OrderHeaderConstant;
import com.bat.thirdparty.common.util.BeanUtils;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.executor.log.OrderBusinessLogCmdExe;
import com.bat.thirdparty.order.executor.log.OrderBusinessLogFailCmdExe;
import com.bat.thirdparty.order.executor.log.OrderBusinessLogQryExe;
import com.bat.thirdparty.order.service.convertor.OrderOpenConvertor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.platform.api.PlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.PlatformRpcDTO;
import com.bat.thirdparty.log.api.dto.ThirdLogPageQry;
import com.bat.thirdparty.order.api.dto.OrderBaseOnCodeCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.common.AddressQry;
import com.bat.thirdparty.order.api.dto.common.UserInfoQry;
import com.bat.thirdparty.order.api.dto.log.LogAddressUpdateDTO;
import com.bat.thirdparty.order.api.dto.log.OrderBusinessPageQry;
import com.bat.thirdparty.order.api.dto.log.ThirdOrderSyncLogDTO;

@Service
public class OrderBusinessLogServiceImpl implements OrderBusinessLogServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBusinessLogServiceImpl.class);

    @Autowired
    private OrderBusinessLogCmdExe orderReceiveLogCmdExe;

    @Autowired
    private OrderBusinessLogQryExe orderBusinessLogQryExe;

    @DubboReference(check = false, timeout = 9000, retries = 0)
    private PlatformServiceRpc platformServiceRpc;

    @Autowired
    private OrderBusinessLogFailCmdExe orderBusinessLogFailCmdExe;

    @Autowired
    private OrderOpenConvertor orderOpenConvertor;

    @Override
    public void create(OrderBusinessLogDO orderBusinessLogDO) {
        orderReceiveLogCmdExe.create(orderBusinessLogDO);
    }

    @Override
    public List<OrderBusinessLogDO> listByCondition(Short logType, Date startTime, Date endTime, Short status,
        String content, String otherOrderNo, Short searchType, List<String> platformList, Integer distributorId) {
        return orderBusinessLogQryExe.listByCondition(logType, startTime, endTime, status, content, otherOrderNo,
            searchType, platformList, distributorId);
    }

    /**
     * 分页查询第三方同步订单到B2B日志
     * 
     * @param orderBusinessPageQry
     * @return
     */
    @Override
    public PageInfo<ThirdOrderSyncLogDTO> pageSyncOrderLog(OrderBusinessPageQry orderBusinessPageQry) {
        if (orderBusinessPageQry.getSearchType() != null && orderBusinessPageQry.getSearchType() - 1 == 0) {
            // 直接查询第三方单号
            orderBusinessPageQry.setOtherOrderNo(orderBusinessPageQry.getContent());
            orderBusinessPageQry.setContent(null);
        }

        PageHelper.startPage(orderBusinessPageQry.getPage(), orderBusinessPageQry.getSize());
        List<OrderBusinessLogDO> logDOList =
            orderBusinessLogQryExe.listReceiveOrderByCondition(orderBusinessPageQry.getStartTime(),
                orderBusinessPageQry.getEndTime(), orderBusinessPageQry.getStatus(), orderBusinessPageQry.getContent(),
                orderBusinessPageQry.getOtherOrderNo(), null, orderBusinessPageQry.getPlatform(), null);
        PageInfo pageInfo = new PageInfo<>(logDOList);
        if (logDOList != null && logDOList.size() > 0) {
            Response<List<PlatformRpcDTO>> listResponse = platformServiceRpc.listByOpenFlag(null);
            if (listResponse == null) {
                throw ThirdPartyException
                    .buildException(ThirdDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
            }
            if (!listResponse.isSuccess()) {
                throw ThirdPartyException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
            }
            List<PlatformRpcDTO> platformRpcDTOS = listResponse.getData();
            Map<String, PlatformRpcDTO> platformRpcDTOMap = platformRpcDTOS.stream()
                .collect(Collectors.toMap(PlatformRpcDTO::getPlatformNo, platformRpcDTO -> platformRpcDTO));

            List<ThirdOrderSyncLogDTO> list = new ArrayList<>();
            logDOList.stream().forEach(orderBusinessLogDO -> {
                ThirdOrderSyncLogDTO syncLogDTO = BeanUtils.copy(orderBusinessLogDO, ThirdOrderSyncLogDTO.class);
                PlatformRpcDTO platformRpcDTO = platformRpcDTOMap.get(orderBusinessLogDO.getPlatform());
                if (platformRpcDTO != null) {
                    syncLogDTO.setPlatform(platformRpcDTO.getName());
                }
                if (orderBusinessLogDO.getLogType() == 2) {
                    OrderBaseOnIdCmd orderBaseOnIdCmd =
                        JSONObject.parseObject(orderBusinessLogDO.getRequestParamJson(), OrderBaseOnIdCmd.class);
                    LOGGER.info(orderBusinessLogDO.getRequestParamJson());
                    if (orderBaseOnIdCmd != null) {
                        UserInfoQry userInfo = orderBaseOnIdCmd.getUserInfo();
                        if (userInfo != null) {
                            syncLogDTO.setReceiver(userInfo.getUserName());
                            syncLogDTO.setMobile(userInfo.getPhoneNumber());
                        }
                        AddressQry address = orderBaseOnIdCmd.getAddress();
                        if (address != null) {
                            syncLogDTO.setAddress(
                                address.getProvince() + address.getCity() + address.getArea() + address.getDetail());
                        }
                    }
                } else {
                    OrderBaseOnCodeCmd orderBaseOnCodeCmd =
                        JSONObject.parseObject(orderBusinessLogDO.getRequestParamJson(), OrderBaseOnCodeCmd.class);
                    if (orderBaseOnCodeCmd != null) {
                        UserInfoQry userInfo = orderBaseOnCodeCmd.getUserInfo();
                        if (userInfo != null) {
                            syncLogDTO.setReceiver(userInfo.getUserName());
                            syncLogDTO.setMobile(userInfo.getPhoneNumber());
                        }
                        AddressQry address = orderBaseOnCodeCmd.getAddress();
                        if (address != null) {
                            syncLogDTO.setAddress(
                                address.getProvince() + address.getCity() + address.getArea() + address.getDetail());
                        }
                    }
                }
                list.add(syncLogDTO);
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public PageInfo<OrderBusinessLogDO> page(ThirdLogPageQry thirdLogPageQry) {
        // 先设置平台编码列表
        if (OrderBusinessLogConstant.LOG_PAGE_SEARCH_TYPE_PLATFORM_NAME.equals(thirdLogPageQry.getSearchType())
            && StringUtils.isNotBlank(thirdLogPageQry.getContent())) {
            Response<List<PlatformRpcDTO>> listResponse = platformServiceRpc
                .listByOpenFlagAndNameLike(ThirdCommonConstant.COMMON_OPEN_FLAG_YES, thirdLogPageQry.getContent());
            if (listResponse == null) {
                throw ThirdPartyException
                    .buildException(ThirdDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
            }
            if (!listResponse.isSuccess()) {
                throw ThirdPartyException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
            }
            List<PlatformRpcDTO> platformRpcDTOList = listResponse.getData();
            if (platformRpcDTOList == null || platformRpcDTOList.size() == 0) {
                return new PageInfo<>();
            }
            List<String> collect =
                platformRpcDTOList.stream().map(PlatformRpcDTO::getPlatformNo).collect(Collectors.toList());
            // 设置平台编码列表
            thirdLogPageQry.setPlatformList(collect);
        }
        PageHelper.startPage(thirdLogPageQry.getPage(), thirdLogPageQry.getSize());
        List<OrderBusinessLogDO> list =
            orderBusinessLogQryExe.listByCondition(thirdLogPageQry.getLogType(), thirdLogPageQry.getStartTime(),
                thirdLogPageQry.getEndTime(), thirdLogPageQry.getStatus(), thirdLogPageQry.getContent(), null,
                thirdLogPageQry.getSearchType(), thirdLogPageQry.getPlatformList(), null);
        return new PageInfo(list);
    }

    @Override
    public com.bat.thirdparty.common.base.Response pushAgian(Integer id, String userName,
                                                               HttpServletRequest request) {
        LOGGER.info("业务日志{}失败重试，操作人{}", id, userName);
        OrderBusinessLogDO orderBusinessLogDO = orderBusinessLogQryExe.getById(id);
        if (ThirdCommonConstant.API_REQUEST_SUCCESS.equals(orderBusinessLogDO)) {
            // 日志已经成功、不需要再推
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        try {
            orderBusinessLogFailCmdExe.pushAgain(orderBusinessLogDO, request);
            return com.bat.thirdparty.common.base.Response.buildSuccess();
        } catch (ThirdPartyException e) {
            e.printStackTrace();
            LOGGER.error("业务日志接口重推失败:{}", e.getMsg());
            return com.bat.thirdparty.common.base.Response.buildFailure(e.getCode(), e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("业务日志接口重推失败:{}", e.getMessage());
            return com.bat.thirdparty.common.base.Response.buildFailure(ThirdCommonErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
        }

    }

    /**
     * 修改订单地址
     * 
     * @param addressUpdateDTO
     * @return
     */
    @Override
    public com.bat.thirdparty.common.base.Response updateAddress(LogAddressUpdateDTO addressUpdateDTO) {
        LOGGER.info("修改第三方推送异常订单地址,{}", JSON.toJSONString(addressUpdateDTO));
        OrderBusinessLogDO orderBusinessLogDO = orderBusinessLogQryExe.getById(addressUpdateDTO.getId());
        if (ThirdCommonConstant.API_REQUEST_SUCCESS.equals(orderBusinessLogDO)) {
            // 日志已经成功、不需要再修改
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        if (!OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_ID.getLogType().equals(orderBusinessLogDO.getLogType())
            && !OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_CODE.getLogType().equals(orderBusinessLogDO.getLogType())) {
            // 非接收第三方订单日志、不允许修改
            throw ThirdPartyException
                .buildException(OrderBusinessLogErrorCode.T_LOG_UPDATE_FAIL_NOT_BELONG_RECEIVE_ORDER);
        }
        if (StringUtils.isBlank(orderBusinessLogDO.getRequestParamJson())) {
            throw ThirdPartyException.buildException(OrderBusinessLogErrorCode.T_LOG_REQUEST_BODY_NULL);
        }
        JSONObject jsonObject = JSON.parseObject(orderBusinessLogDO.getRequestParamJson(), Feature.OrderedField);
        // 修改地址
        jsonObject.put("address", addressUpdateDTO.getAddress());
        orderBusinessLogDO.setUpdateTime(new Date());
        orderBusinessLogDO.setRequestParamJson(JSON.toJSONString(jsonObject));
        // 还得修改签名
        updateSign(orderBusinessLogDO);
        orderReceiveLogCmdExe.update(orderBusinessLogDO, true);
        return com.bat.thirdparty.common.base.Response.buildSuccess();
    }

    @Override
    public List<OrderBusinessLogDO> listReceiveOrderByCondition(Date startTime, Date endTime, Short status,
        String content, String otherOrderNo, Short searchType, String platform, Integer distributorId) {
        return orderBusinessLogQryExe.listReceiveOrderByCondition(startTime, endTime, status, content, otherOrderNo,
            searchType, platform, distributorId);
    }

    /**
     * 删除不要调用这个接口
     * 
     * @param logDO
     */
    @Transactional
    @Override
    public void save(OrderBusinessLogDO logDO) {
        logDO.setUpdateTime(new Date());
        if (logDO.getId() == null) {
            orderReceiveLogCmdExe.create(logDO);
        } else {
            orderReceiveLogCmdExe.update(logDO, true);
        }
    }

    @Transactional
    @Override
    public com.bat.thirdparty.common.base.Response deleteById(Integer id) {
        OrderBusinessLogDO orderBusinessLogDO = orderBusinessLogQryExe.getById(id);
        if (ThirdCommonConstant.API_REQUEST_SUCCESS.equals(orderBusinessLogDO.getStatus())) {
            // 调用成功的不允许删除
            throw ThirdPartyException.buildException(OrderBusinessLogErrorCode.T_LOG_DELETE_FAIL_BY_API_STATUS_SUCCESS);
        }
        orderBusinessLogDO.setDeleteFlag(ThirdCommonConstant.COMMON_DEL_FLAG_YES);
        orderBusinessLogDO.setUpdateTime(new Date());
        // 不要删除其他的
        orderReceiveLogCmdExe.update(orderBusinessLogDO, false);
        return com.bat.thirdparty.common.base.Response.buildSuccess();
    }

    @Override
    public OrderBusinessLogDO getById(Integer id) {
        return orderBusinessLogQryExe.getById(id);
    }

    /**
     * 重新设置签名
     * 
     * @param orderBusinessLogDO
     */
    private void updateSign(OrderBusinessLogDO orderBusinessLogDO) {
        if (StringUtils.isBlank(orderBusinessLogDO.getHeaderParamJson())) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(orderBusinessLogDO.getHeaderParamJson());
        String distributorId = jsonObject.getString(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID);
        String orderSource = jsonObject.getString(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE);
        if (StringUtils.isBlank(distributorId) || StringUtils.isBlank(orderSource)) {
            return;
        }
        String timeStramp = jsonObject.getString(OrderHeaderConstant.HEADER_NAME_TIMESTAMP);
        if (StringUtils.isBlank(timeStramp)) {
            timeStramp = String.valueOf(System.currentTimeMillis());
            // 重置时间戳
            jsonObject.put(OrderHeaderConstant.HEADER_NAME_TIMESTAMP, timeStramp);
        }
        String sign = orderOpenConvertor.getSign(orderSource, distributorId, orderBusinessLogDO.getRequestParamJson(),
            timeStramp);
        // 重置签名
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_SIGN, sign);
        orderBusinessLogDO.setHeaderParamJson(JSON.toJSONString(jsonObject));
    }

    public static void main(String[] args) {
        String param =
        String sssss =
        OrderBaseOnIdCmd orderBaseOnIdCmd = JSONObject.parseObject(param, OrderBaseOnIdCmd.class);
        System.out.println(JSON.toJSONString(orderBaseOnIdCmd));
    }
}
