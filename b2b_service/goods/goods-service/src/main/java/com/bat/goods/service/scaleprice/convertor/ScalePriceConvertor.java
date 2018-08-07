package com.bat.goods.service.scaleprice.convertor;

import static com.bat.goods.service.common.Constant.OPERATION_TYPE_1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.bat.dubboapi.goods.scaleprice.dto.ScalePriceRpcDTO;
import com.bat.goods.api.scaleprice.dto.ScalePriceCmd;
import com.bat.goods.api.scaleprice.dto.data.ScalePriceDTO;
import com.bat.goods.dao.scaleprice.dataobject.ScalePriceDO;

public class ScalePriceConvertor {
    /**
     * 价格等级数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static ScalePriceDO toScalePriceDO(ScalePriceCmd cmd, Short operationType) {
        ScalePriceDO scalePriceDO = new ScalePriceDO();
        BeanUtils.copyProperties(cmd, scalePriceDO);
        if (operationType.equals(OPERATION_TYPE_1)) {
            scalePriceDO.setId(null);
        }
        Date date = new Date(System.currentTimeMillis());
        scalePriceDO.setCreateTime(date);
        scalePriceDO.setUpdateTime(date);
        return scalePriceDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param scalePriceDOList
     * @return
     */
    public static List<ScalePriceDTO> toScalePriceDTOList(List<ScalePriceDO> scalePriceDOList) {
        List<ScalePriceDTO> scalePriceDTOList = new ArrayList<>();
        scalePriceDOList.forEach(scalePriceDO -> {
            ScalePriceDTO scalePriceDTO = new ScalePriceDTO();
            BeanUtils.copyProperties(scalePriceDO, scalePriceDTO);
            scalePriceDTOList.add(scalePriceDTO);
        });
        return scalePriceDTOList;
    }

    /**
     * do转dto
     * 
     * @param scalePriceDO
     * @return
     */
    public static ScalePriceDTO toScalePriceDTO(ScalePriceDO scalePriceDO) {
        ScalePriceDTO scalePriceDTO = new ScalePriceDTO();
        BeanUtils.copyProperties(scalePriceDO, scalePriceDTO);
        return scalePriceDTO;
    }

    public static ScalePriceRpcDTO toScalePriceRpcDTO(ScalePriceDO scalePriceDO) {
        if (scalePriceDO != null) {
            ScalePriceRpcDTO rpcDTO = new ScalePriceRpcDTO();
            BeanUtils.copyProperties(scalePriceDO, rpcDTO);
            return rpcDTO;
        }
        return null;
    }

}
