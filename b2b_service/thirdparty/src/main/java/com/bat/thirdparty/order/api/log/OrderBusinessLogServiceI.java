package com.bat.thirdparty.order.api.log;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.log.api.dto.ThirdLogPageQry;
import com.bat.thirdparty.order.api.dto.log.LogAddressUpdateDTO;
import com.bat.thirdparty.order.api.dto.log.OrderBusinessPageQry;
import com.bat.thirdparty.order.api.dto.log.ThirdOrderSyncLogDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface OrderBusinessLogServiceI {
    /**
     * 新增
     * @param orderBusinessLogDO
     */
    void create(OrderBusinessLogDO orderBusinessLogDO);

    /**
     * 条件查询
     * @param otherOrderNo 第三方单号
     * @param status
     * @return
     */
    List<OrderBusinessLogDO> listByCondition(Short logType, Date startTime, Date endTime, Short status, String content, String otherOrderNo,Short searchType,
                                             List<String> platformList,Integer distributorId);

    /**
     * 分页查询第三方同步订单到B2B日志
     * @param orderBusinessPageQry
     * @return
     */
    PageInfo<ThirdOrderSyncLogDTO> pageSyncOrderLog(OrderBusinessPageQry orderBusinessPageQry);

    PageInfo<OrderBusinessLogDO> page(ThirdLogPageQry thirdLogPageQry);

    /**
     * 重推失败日志
     * @param id 业务日志id
     * @param userName 后台用户名
     * @return
     */
    Response pushAgian(Integer id, String userName, HttpServletRequest request);

    /**
     * 修改订单地址
     * @param addressUpdateDTO
     * @return
     */
    Response updateAddress(LogAddressUpdateDTO addressUpdateDTO);



    List<OrderBusinessLogDO> listReceiveOrderByCondition(Date startTime, Date endTime, Short status, String content, String otherOrderNo, Short searchType, String platform,
                                                         Integer distributorId);

    /**
     * 保存日志
     * @param logDO
     */
    void save(OrderBusinessLogDO logDO);

    /**
     * 删除业务日志、软删除
     * @param id
     * @return
     */
    Response deleteById(Integer id);

    OrderBusinessLogDO getById(Integer id);
}
