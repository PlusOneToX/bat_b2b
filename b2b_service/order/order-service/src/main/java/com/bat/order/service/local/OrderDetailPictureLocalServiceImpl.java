package com.bat.order.service.local;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailFTPDTO;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailLocalUploadCmd;
import com.bat.order.api.local.api.OrderDetailPictureLocalServiceI;
import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.service.local.convertor.OrderDetailPictureLocalConvertor;
import com.bat.order.service.local.executor.MaikeFTPCmdExe;
import com.bat.order.service.local.executor.OrderDetailPictureLocalCmdExe;
import com.bat.order.service.local.executor.OrderDetailPictureLocalQryExe;

@Service
public class OrderDetailPictureLocalServiceImpl implements OrderDetailPictureLocalServiceI {

    @Autowired
    private OrderDetailPictureLocalQryExe orderDetailPictureLocalQryExe;

    @Autowired
    private OrderDetailPictureLocalCmdExe orderDetailPictureLocalCmdExe;

    @Autowired
    private MaikeFTPCmdExe maikeFTPCmdExe;

    @Autowired
    private OrderDetailPictureLocalConvertor orderDetailPictureLocalConvertor;

    @Override
    public OrderDetailPictureLocalDO getByOrderGoodsDiyId(Integer orderGoodsDiyId) {
        return orderDetailPictureLocalQryExe.getByOrderGoodsDiyId(orderGoodsDiyId);
    }

    @Override
    public List<OrderDetailPictureLocalDO> listByOrderId(Integer orderId) {
        return orderDetailPictureLocalQryExe.listByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDetailPictureLocalDO save(String previewImage, String generateImage, String labelUrl,
        OrderGoodsDiyDO orderGoodsDiyDO) {
        OrderDetailPictureLocalDO orderDetailPictureLocalDO =
            orderDetailPictureLocalQryExe.getByOrderGoodsDiyId(orderGoodsDiyDO.getId());
        if (orderDetailPictureLocalDO == null) {
            orderDetailPictureLocalDO = new OrderDetailPictureLocalDO();
            orderDetailPictureLocalDO.setCreateTime(new Date());
            orderDetailPictureLocalDO.setOrderId(orderGoodsDiyDO.getOrderId());
            orderDetailPictureLocalDO.setOrderGoodsDiyId(orderGoodsDiyDO.getId());
        }

        orderDetailPictureLocalDO.setUpdateTime(new Date());
        List<OrderDetailLocalUploadCmd> uploadCmdList = new ArrayList<>();
        OrderDetailLocalUploadCmd localUploadCmd =
            OrderDetailPictureLocalConvertor.toOrderDetailLocalUploadCmd(orderDetailPictureLocalDO, orderGoodsDiyDO);
        if (localUploadCmd != null) {
            localUploadCmd.setFactoryCode(orderGoodsDiyDO.getManufactors());
            uploadCmdList.add(localUploadCmd);
        }
        if (uploadCmdList == null || uploadCmdList.size() == 0) {
            // 都同步工厂FTP、直接返回
            return orderDetailPictureLocalDO;
        }
        List<OrderDetailFTPDTO> detailFTPDTOList = maikeFTPCmdExe.uploadFTP(uploadCmdList);
        List<OrderGoodsDiyDO> orderGoodsDiyDOList = new ArrayList<>();
        orderGoodsDiyDOList.add(orderGoodsDiyDO);
        List<OrderDetailPictureLocalDO> orderDetailPictureLocalDOList = new ArrayList<>();
        orderDetailPictureLocalDOList.add(orderDetailPictureLocalDO);
        orderDetailPictureLocalConvertor.setFtpUrl(orderDetailPictureLocalDOList, detailFTPDTOList,
            orderGoodsDiyDOList);

        return orderDetailPictureLocalDO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String setLabelUrl(Integer orderGoodsDiyId, String labelUrl) {
        return null;
    }

    /**
     * 处理尚未同步到工厂的图片
     * 
     * @param orderDetailPictureLocalDOList
     * @param orderGoodsDiyDOList
     * @return
     */
    @Override
    public List<OrderDetailPictureLocalDO> setUploadUnSyncToFTP(
        List<OrderDetailPictureLocalDO> orderDetailPictureLocalDOList, List<OrderGoodsDiyDO> orderGoodsDiyDOList) {
        if (orderGoodsDiyDOList == null || orderGoodsDiyDOList.size() == 0) {
            return null;
        }
        List<OrderDetailLocalUploadCmd> uploadCmdList = new ArrayList<>();
        if (orderDetailPictureLocalDOList == null) {
            orderDetailPictureLocalDOList = new ArrayList<>();
        }
        // 返回的result
        List<OrderDetailPictureLocalDO> resultList = new ArrayList<>();
        Map<Integer, OrderDetailPictureLocalDO> orderDetailPictureLocalDOMap =
            orderDetailPictureLocalDOList.stream().collect(Collectors.toMap(
                OrderDetailPictureLocalDO::getOrderGoodsDiyId, orderDetailPictureLocal -> orderDetailPictureLocal));
        orderGoodsDiyDOList.stream().forEach(orderGoodsDiyDO -> {
            OrderDetailPictureLocalDO orderDetailPictureLocalDO =
                orderDetailPictureLocalDOMap.get(orderGoodsDiyDO.getId());
            if (orderDetailPictureLocalDO == null) {
                orderDetailPictureLocalDO = new OrderDetailPictureLocalDO();
                orderDetailPictureLocalDO.setCreateTime(new Date());
                orderDetailPictureLocalDO.setOrderId(orderGoodsDiyDO.getOrderId());
                orderDetailPictureLocalDO.setOrderGoodsDiyId(orderGoodsDiyDO.getId());
                orderDetailPictureLocalDO.setCreateTime(new Date());
            }
            OrderDetailLocalUploadCmd localUploadCmd = OrderDetailPictureLocalConvertor
                .toOrderDetailLocalUploadCmd(orderDetailPictureLocalDO, orderGoodsDiyDO);
            if (localUploadCmd != null) {
                localUploadCmd.setFactoryCode(orderGoodsDiyDO.getManufactors());
                orderDetailPictureLocalDO.setUpdateTime(new Date());
                uploadCmdList.add(localUploadCmd);
            }
            resultList.add(orderDetailPictureLocalDO);
        });
        if (uploadCmdList == null || uploadCmdList.size() == 0) {
            // 都同步工厂FTP、直接返回
            return orderDetailPictureLocalDOList;
        }
        List<OrderDetailFTPDTO> ftpdtoList = maikeFTPCmdExe.uploadFTP(uploadCmdList);
        orderDetailPictureLocalConvertor.setFtpUrl(resultList, ftpdtoList, orderGoodsDiyDOList);
        return resultList;
    }
}
