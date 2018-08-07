package com.bat.goods.service.tag.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.dao.tag.TagMapper;
import com.bat.goods.dao.tag.dataobject.TagDO;
import com.bat.goods.service.tag.convertor.TagConvertor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.tag.dto.TagId;
import com.bat.goods.api.tag.dto.TagListQry;
import com.bat.goods.api.tag.dto.data.TagDTO;

@Component
public class TagQryExe {

    @Resource
    private TagMapper tagMapper;

    /**
     * 查询商品标签列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<TagDTO> executeList(TagListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<TagDO> tagDOList = tagMapper.listTag(qryMap);
        PageInfo pageInfo = new PageInfo(tagDOList);
        List<TagDTO> tagDTOList = TagConvertor.toTagDTOList(pageInfo.getList());
        pageInfo.setList(tagDTOList);
        return pageInfo;
    }

    /**
     * 根据商品标签ID查询商品标签详情
     * 
     * @param tagId
     * @return
     */
    public TagDTO execute(TagId tagId) {
        TagDO tagDO = tagMapper.getById(tagId.getId());
        if (tagDO == null) {
            throw GoodsException.buildException(ErrorCode.B_TAG_NULL);
        }
        TagDTO dto = TagConvertor.toTagDTO(tagDO);
        return dto;
    }
}
