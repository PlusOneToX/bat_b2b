package com.bat.goods.service.scaleprice.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.service.scaleprice.convertor.ScalePriceConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.goods.scaleprice.dto.ScalePriceRpcDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.scaleprice.dto.ScalePriceId;
import com.bat.goods.api.scaleprice.dto.ScalePriceListQry;
import com.bat.goods.api.scaleprice.dto.data.ScalePriceDTO;
import com.bat.goods.dao.scaleprice.ScalePriceMapper;
import com.bat.goods.dao.scaleprice.dataobject.ScalePriceDO;

@Component
public class ScalePriceQryExe {

    @Resource
    private ScalePriceMapper scalePriceMapper;

    /**
     * 查询价格等级列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<ScalePriceDTO> executeList(ScalePriceListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<ScalePriceDO> scalePriceDOList = scalePriceMapper.listScalePrice(qryMap);
        PageInfo pageInfo = new PageInfo(scalePriceDOList);
        List<ScalePriceDTO> scalePriceDTOList = ScalePriceConvertor.toScalePriceDTOList(pageInfo.getList());
        pageInfo.setList(scalePriceDTOList);
        return pageInfo;
    }

    public List<ScalePriceDO> executeFieldList(Short openFlag) {
        return scalePriceMapper.listScalePriceField(openFlag);
    }

    /**
     * 根据价格等级ID查询价格等级详情
     * 
     * @param scalePriceId
     * @return
     */
    public ScalePriceDTO execute(ScalePriceId scalePriceId) {
        ScalePriceDO scalePriceDO = scalePriceMapper.getById(scalePriceId.getId());
        if (scalePriceDO == null) {
            throw GoodsException.buildException(ErrorCode.B_SCALE_PRICE_NULL);
        }
        ScalePriceDTO scalePriceDTO = ScalePriceConvertor.toScalePriceDTO(scalePriceDO);
        return scalePriceDTO;
    }

    /**
     * 根据id获取价格等级信息
     * 
     * @param scalePriceId
     * @return
     */
    public ScalePriceRpcDTO getScalePriceByScalePriceId(Integer scalePriceId) {
        ScalePriceDO scalePriceDO = scalePriceMapper.getById(scalePriceId);
        return ScalePriceConvertor.toScalePriceRpcDTO(scalePriceDO);
    }
}
