package com.bat.goods.api.tag;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.tag.dto.TagCmd;
import com.bat.goods.api.tag.dto.TagId;
import com.bat.goods.api.tag.dto.TagListQry;
import com.bat.goods.api.tag.dto.TagOpenCmd;
import com.bat.goods.api.tag.dto.data.TagDTO;

public interface TagServiceI {
    /**
     * 添加商品标签
     * 
     * @param tagCmd
     * @return
     */
    public void createTag(TagCmd tagCmd);

    /**
     * 更新商品标签
     * 
     * @param tagCmd
     * @return
     */
    public void updateTag(TagCmd tagCmd);

    /**
     * 更新商品标签状态
     * 
     * @param cmd
     * @return
     */
    public void openTag(TagOpenCmd cmd);

    /**
     * 获取商品标签列表（分页）
     * 
     * @param classifyListQry
     * @return
     */
    public PageInfo<TagDTO> listTag(TagListQry classifyListQry);

    /**
     * 根据ID删除商品标签
     * 
     * @param cmd
     * @return
     */
    public void deleteTag(TagId cmd);

    /**
     * 根据商品标签id获取详情
     * 
     * @param tagId
     * @return
     */
    public TagDTO getTag(TagId tagId);

}
