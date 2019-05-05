package com.bat.dubboapi.flexible.label.api;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.label.dto.LabelDTORpcQry;
import com.bat.dubboapi.flexible.label.dto.OrderDiyLabelDTO;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;

import java.util.List;

public interface LabelServiceRpc {

    /**
     * 条件查询图片关联的标签列表
     * @param distributorIds 分销商id列表
     * @param categoryId 产品类型id
     * @param pictureId 图片id
     * @param distributorScope  分销商属于国内还是国外 2、国内 3、国外
     * @return
     */
    Response<List<LabelDTORpcQry>> listDiyLableByCondition(List<Integer> distributorIds, Integer categoryId, Integer pictureId, Short distributorScope);

    /**
     * 获取标签
     * @param distributorId
     * @param categoryId
     * @param pictureId
     * @return
     */
    Response<List<LabelDTORpcQry>> listDiyLabelByCondition(Integer distributorId,Integer categoryId,Integer pictureId);


    Response<LabelDTORpcQry> getById(Integer id);

    /**
     * 订单生成标签
     * @param orderLableCmd
     * @return
     */
    Response<List<OrderDiyLabelDTO>> createOrderDiyLabel(OrderLableCmd orderLableCmd);

    /**
     * 订单生成标签 通过工厂
     * @param orderLableCmd
     * @return
     */
    Response<List<OrderDiyLabelDTO>> createOrderDiyLabel(String factoryNo,OrderLableCmd orderLableCmd);
}
