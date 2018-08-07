package com.bat.order.api.local.api;

import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;

import java.util.List;

public interface OrderDetailPictureLocalServiceI {

    /**
     * 根据定制订单明细id查询
     * @param orderGoodsDiyId
     * @return
     */
    OrderDetailPictureLocalDO getByOrderGoodsDiyId(Integer orderGoodsDiyId);

    /**
     * 根据订单id查询本地图片列表
     * @param orderId
     * @return
     */
    List<OrderDetailPictureLocalDO> listByOrderId(Integer orderId);

    /**
     *
     * @param previewImage
     * @param generateImage
     * @param labelUrl
     * @param orderGoodsDiyDO
     * @return
     */
    OrderDetailPictureLocalDO save(String previewImage, String generateImage, String labelUrl, OrderGoodsDiyDO orderGoodsDiyDO);

    /**
     * 保存标签url
     * @param orderGoodsDiyId
     * @param labelUrl
     * @return
     */
    String setLabelUrl(Integer orderGoodsDiyId, String labelUrl);

    /**
     * 处理尚未同步到工厂的图片
     * @param orderDetailPictureLocalDOList
     * @param orderGoodsDiyDOList
     * @return
     */
    List<OrderDetailPictureLocalDO> setUploadUnSyncToFTP(List<OrderDetailPictureLocalDO> orderDetailPictureLocalDOList, List<OrderGoodsDiyDO> orderGoodsDiyDOList);
}
