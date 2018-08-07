package com.bat.goods.api.scaleprice;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.scaleprice.dto.ScalePriceCmd;
import com.bat.goods.api.scaleprice.dto.ScalePriceId;
import com.bat.goods.api.scaleprice.dto.ScalePriceListQry;
import com.bat.goods.api.scaleprice.dto.ScalePriceOpenCmd;
import com.bat.goods.api.scaleprice.dto.data.ScalePriceDTO;

public interface ScalePriceServiceI {
    /**
     * 添加价格等级
     * 
     * @param cmd
     * @return
     */
    public void createScalePrice(ScalePriceCmd cmd);

    /**
     * 更新价格等级
     * 
     * @param cmd
     * @return
     */
    public void updateScalePrice(ScalePriceCmd cmd);

    /**
     * 更新价格等级状态
     * 
     * @param cmd
     * @return
     */
    public void openScalePrice(ScalePriceOpenCmd cmd);

    /**
     * 获取价格等级列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<ScalePriceDTO> listScalePrice(ScalePriceListQry qry);

    /**
     * 根据ID删除价格等级
     * 
     * @param cmd
     * @return
     */
    public void deleteScalePrice(ScalePriceId cmd);

    /**
     * 根据价格等级id获取详情
     * 
     * @param scalePriceId
     * @return
     */
    public ScalePriceDTO getScalePrice(ScalePriceId scalePriceId);

}
