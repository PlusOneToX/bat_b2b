package com.bat.goods.service.scaleprice.executor;

import static com.bat.goods.service.common.Constant.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.scaleprice.dto.ScalePriceCmd;
import com.bat.goods.api.scaleprice.dto.ScalePriceId;
import com.bat.goods.api.scaleprice.dto.ScalePriceOpenCmd;
import com.bat.goods.dao.scaleprice.ScalePriceMapper;
import com.bat.goods.dao.scaleprice.dataobject.ScalePriceDO;
import com.bat.goods.service.scaleprice.convertor.ScalePriceConvertor;

@Component
public class ScalePriceCmdExe {

    @Resource
    private ScalePriceMapper scalePriceMapper;

    /**
     * 创建价格等级
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createScalePrice(ScalePriceCmd cmd) {
        ScalePriceDO scalePriceDO = ScalePriceConvertor.toScalePriceDO(cmd, OPERATION_TYPE_1);
        scalePriceMapper.createScalePrice(scalePriceDO);
    }

    /**
     * 更新价格等级
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateScalePrice(ScalePriceCmd cmd) {
        ScalePriceDO scalePriceDO = ScalePriceConvertor.toScalePriceDO(cmd, OPERATION_TYPE_2);
        scalePriceMapper.updateScalePrice(scalePriceDO);
    }

    /**
     * 更新价格等级状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openScalePrice(ScalePriceOpenCmd cmd) {
        scalePriceMapper.openScalePrice(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 删除价格等级
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteScalePrice(ScalePriceId cmd) {
        ScalePriceDO scalePriceDO = scalePriceMapper.getById(cmd.getId());
        if (scalePriceDO == null) {
            throw GoodsException.buildException(ErrorCode.B_SCALE_PRICE_NULL);
        }
        // 停用的商品标签才允许删除
        if (!scalePriceDO.getOpenFlag().equals(OPEN_NO)) {
            throw GoodsException.buildException(ErrorCode.B_SCALE_PRICE_DELETE_OPEN_ERROR);
        }
        // TODO 删除关系数据
        scalePriceMapper.deleteScalePrice(cmd.getId());
    }

}
