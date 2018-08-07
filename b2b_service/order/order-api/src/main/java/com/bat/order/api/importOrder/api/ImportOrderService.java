package com.bat.order.api.importOrder.api;

import java.io.InputStream;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.importOrder.dto.*;
import com.bat.order.api.importOrder.dto.*;

/**
 * @author Lim
 * @version 1.0
 * @description: TODO
 * @date 2018/05/2 13:46
 */
public interface ImportOrderService {
    /**
     * 获取下载路径
     * 
     * @param fileType
     * @param adminFlag
     * @return
     */
    String getTempUrl(Integer fileType, Boolean adminFlag);

    /**
     * 订单导入接口
     * 
     * @param inputStream
     * @param userId
     */
    void importOrder(InputStream inputStream, String userId);

    /**
     * 获取导入订单列表
     * 
     * @param qry
     * @return
     */
    PageInfo<ImportOrderDTO> importOrderList(ImportOrderListQry qry);

    /**
     * 导入订单详情
     * 
     * @param qry
     * @return
     */
    ImportOrderDetailDTO importOrderDetail(ImportOrderDetailQry qry);

    /**
     * 删除导入项
     * 
     * @param request
     */
    void deleteImportOrderInfo(ImportOrderIds request);

    /**
     * 导入订单批量下单
     * 
     * @param request
     * @param contactId
     * @param contactName
     * @param language
     */
    void importOrders(ImportOrderIds request, Boolean adminFlag, String contactId, String contactName, String language);

    /**
     * 用户导入订单修改
     * 
     * @param cmd
     * @return
     */
    void importOrderUpdate(ImportOrderUpdateCmd cmd);

    /**
     * 下载订购清单 以及 缺货清单
     * 
     * @param qry
     */
    void downLoad(ImportOrderDownloadQry qry);
}
