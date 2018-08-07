package com.bat.order.api.deliver;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseSearchQry;
import com.bat.order.api.deliver.dto.OrderDeliverStopPlaceCmd;
import com.bat.order.api.deliver.dto.data.OrderDeliverStopPlaceDTO;

public interface OrderDeliveryStopPlaceServiceI {

    /**
     * 新增加停发地点
     * @param cmd
     */
    void create(OrderDeliverStopPlaceCmd cmd);

    /**
     * 更新停发地点
     * @param cmd
     */
    void update(OrderDeliverStopPlaceCmd cmd);

    /**
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 查看该地点是否停发
     * @param placeName
     * @return
     */
    boolean findMatch(String placeName);

    /**
     * 查询停发列表
     * @return
     */
    PageInfo<OrderDeliverStopPlaceDTO> selectList(BaseSearchQry qry);


}
