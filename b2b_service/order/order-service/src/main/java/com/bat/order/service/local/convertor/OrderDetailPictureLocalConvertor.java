package com.bat.order.service.local.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailFTPDTO;
import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailLocalUploadCmd;
import com.bat.dubboapi.thirdparty.maike.dto.order.PictureLocalUrlCmd;
import com.bat.order.dao.local.dataobject.OrderDetailPictureLocalDO;
import com.bat.order.dao.order.dataobject.OrderGoodsDiyDO;
import com.bat.order.service.local.executor.OrderDetailPictureLocalCmdExe;

@Component
public class OrderDetailPictureLocalConvertor {

    @Autowired
    private OrderDetailPictureLocalCmdExe orderDetailPictureLocalCmdExe;

    /**
     * 查询本地存储图片、得到还没同步工厂FTP的列表
     * 
     * @param orderDetailPictureLocalDO
     * @param orderGoodsDiyDO
     * @return
     */
    public static OrderDetailLocalUploadCmd toOrderDetailLocalUploadCmd(
        OrderDetailPictureLocalDO orderDetailPictureLocalDO, OrderGoodsDiyDO orderGoodsDiyDO) {
        List<String> urlList = new ArrayList<>();
        OrderDetailLocalUploadCmd localUploadCmd = new OrderDetailLocalUploadCmd();
        if (StringUtils.isBlank(orderDetailPictureLocalDO.getGenerateImageUrl())) {
            urlList.add(orderGoodsDiyDO.getGenerateImage());
        }
        if (StringUtils.isBlank(orderDetailPictureLocalDO.getImageUrl())) {
            urlList.add(orderGoodsDiyDO.getPreviewImage());
        }
        if (StringUtils.isBlank(orderDetailPictureLocalDO.getLabelUrl())) {
            urlList.add(orderGoodsDiyDO.getLabelUrl());
        }
        if (urlList == null || urlList.size() == 0) {
            return null;
        }
        localUploadCmd.setUrlList(urlList);
        localUploadCmd.setOrderGoodsDiyId(orderGoodsDiyDO.getId());

        return localUploadCmd;
    }

    /**
     * 设置FTP URL
     * 
     * @param orderDetailPictureLocalDOList
     * @param ftpdtoList
     * @param orderGoodsDiyDOList
     */
    public void setFtpUrl(List<OrderDetailPictureLocalDO> orderDetailPictureLocalDOList,
        List<OrderDetailFTPDTO> ftpdtoList, List<OrderGoodsDiyDO> orderGoodsDiyDOList) {
        Map<Integer, OrderDetailFTPDTO> ftpdtoMap = ftpdtoList.stream()
            .collect(Collectors.toMap(OrderDetailFTPDTO::getOrderGoodsDiyId, orderDetailFTPDTO -> orderDetailFTPDTO));

        Map<Integer, OrderGoodsDiyDO> orderGoodsDiyDOMap = orderGoodsDiyDOList.stream()
            .collect(Collectors.toMap(OrderGoodsDiyDO::getId, orderGoodsDiyDO -> orderGoodsDiyDO));

        // 批量修改的
        List<OrderDetailPictureLocalDO> updateLocalDOList = new ArrayList<>();
        // 批量插入
        List<OrderDetailPictureLocalDO> insertList = new ArrayList<>();

        orderDetailPictureLocalDOList.stream().forEach(pictureLocalDO -> {
            OrderDetailFTPDTO orderDetailFTPDTO = ftpdtoMap.get(pictureLocalDO.getOrderGoodsDiyId());
            if (orderDetailFTPDTO != null) {
                // 进行了FTP上传操作
                List<PictureLocalUrlCmd> localUrlCmdList = orderDetailFTPDTO.getLocalUrlCmdList();
                OrderGoodsDiyDO orderGoodsDiyDO = orderGoodsDiyDOMap.get(pictureLocalDO.getOrderGoodsDiyId());
                Map<String, String> urlCmdMap = new HashMap<>();
                localUrlCmdList.stream().forEach(pictureLocalUrlCmd -> {
                    // key为原来的URL、value为FTP 的URL
                    urlCmdMap.put(pictureLocalUrlCmd.getSourceUrl(), pictureLocalUrlCmd.getLocalUrl());
                });
                if (StringUtils.isBlank(pictureLocalDO.getLabelUrl())) {
                    // 标签进行了同步
                    pictureLocalDO.setLabelUrl(urlCmdMap.get(orderGoodsDiyDO.getLabelUrl()));
                    pictureLocalDO.setRootPath(orderDetailFTPDTO.getRootPath());
                }
                if (StringUtils.isBlank(pictureLocalDO.getGenerateImageUrl())) {
                    // 生产图
                    pictureLocalDO.setGenerateImageUrl(urlCmdMap.get(orderGoodsDiyDO.getGenerateImage()));
                    pictureLocalDO.setRootPath(orderDetailFTPDTO.getRootPath());
                }
                if (StringUtils.isBlank(pictureLocalDO.getImageUrl())) {
                    // 预览图
                    pictureLocalDO.setImageUrl(urlCmdMap.get(orderGoodsDiyDO.getPreviewImage()));
                    pictureLocalDO.setRootPath(orderDetailFTPDTO.getRootPath());
                }
                if (pictureLocalDO.getId() == null) {
                    insertList.add(pictureLocalDO);
                } else {
                    updateLocalDOList.add(pictureLocalDO);
                }
            }
        });
        // 批量操作
        if (insertList.size() > 0) {
            orderDetailPictureLocalCmdExe.batchCreate(insertList);
        }
        if (updateLocalDOList.size() > 0) {
            orderDetailPictureLocalCmdExe.batchUpdate(updateLocalDOList);
        }
    }

    public static void main(String[] args) {
        OrderDetailPictureLocalDO localDO = new OrderDetailPictureLocalDO();
        localDO.setId(1);

        List<OrderDetailPictureLocalDO> list = new ArrayList<>();
        list.add(localDO);
        setUrl(list);
        System.out.println(JSON.toJSONString(localDO));
    }

    public static void setUrl(List<OrderDetailPictureLocalDO> list) {
        list.stream().forEach(pictureLocalDO -> {
            pictureLocalDO.setOrderId(122);
            pictureLocalDO.setImageUrl("222");
        });
    }
}
