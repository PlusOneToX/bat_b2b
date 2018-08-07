package com.bat.distributor.api.salesarea;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.salesarea.dto.SalesAreaCmd;
import com.bat.distributor.api.salesarea.dto.SalesAreaId;
import com.bat.distributor.api.salesarea.dto.SalesAreaListQry;
import com.bat.distributor.api.salesarea.dto.SalesAreaOpenCmd;
import com.bat.distributor.api.salesarea.dto.data.SalesAreaDTO;

public interface SalesAreaServiceI {
    /**
     * 添加销售区域
     * 
     * @param cmd
     * @return
     */
    public void createSalesArea(SalesAreaCmd cmd);

    /**
     * 更新销售区域
     * 
     * @param cmd
     * @return
     */
    public void updateSalesArea(SalesAreaCmd cmd);

    /**
     * 更新销售区域状态
     * 
     * @param cmd
     * @return
     */
    public void openSalesArea(SalesAreaOpenCmd cmd);

    /**
     * 获取销售区域列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<SalesAreaDTO> listSalesArea(SalesAreaListQry qry);

    /**
     * 根据ID删除销售区域
     * 
     * @param cmd
     * @return
     */
    public void deleteSalesArea(SalesAreaId cmd);

    /**
     * 根据销售区域id获取详情
     * 
     * @param qry
     * @return
     */
    public SalesAreaDTO getSalesArea(SalesAreaId qry);

}
