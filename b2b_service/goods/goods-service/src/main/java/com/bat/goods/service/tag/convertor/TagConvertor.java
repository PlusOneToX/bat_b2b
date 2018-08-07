package com.bat.goods.service.tag.convertor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.goods.dao.tag.dataobject.TagDO;
import com.bat.goods.service.common.Constant;
import org.springframework.beans.BeanUtils;

import com.bat.goods.api.tag.dto.TagCmd;
import com.bat.goods.api.tag.dto.data.TagDTO;

public class TagConvertor {
    /**
     * 商品标签领域类和数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static TagDO toTagDo(TagCmd cmd, Short operationType) {
        TagDO tagDO = new TagDO();
        BeanUtils.copyProperties(cmd, tagDO);
        if (operationType.equals(Constant.OPERATION_TYPE_1)) {
            tagDO.setId(null);
        }
        Date date = new Date(System.currentTimeMillis());
        tagDO.setCreateTime(date);
        tagDO.setUpdateTime(date);
        return tagDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param tagDOList
     * @return
     */
    public static List<TagDTO> toTagDTOList(List<TagDO> tagDOList) {
        List<TagDTO> tagDTOList = new ArrayList<>();
        tagDOList.forEach(tagDO -> {
            TagDTO tagDTO = new TagDTO();
            BeanUtils.copyProperties(tagDO, tagDTO);
            tagDTOList.add(tagDTO);
        });
        return tagDTOList;
    }

    /**
     * do转dto
     * 
     * @param tagDO
     * @return
     */
    public static TagDTO toTagDTO(TagDO tagDO) {
        TagDTO tagDTO = new TagDTO();
        BeanUtils.copyProperties(tagDO, tagDTO);
        return tagDTO;
    }

}
