package com.bat.goods.service.tag.executor;

import static com.bat.goods.service.tag.executor.ErrorCode.*;

import javax.annotation.Resource;

import com.bat.goods.dao.tag.TagMapper;
import com.bat.goods.dao.tag.dataobject.TagDO;
import com.bat.goods.service.common.Constant;
import com.bat.goods.service.tag.convertor.TagConvertor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.tag.dto.TagCmd;
import com.bat.goods.api.tag.dto.TagId;
import com.bat.goods.api.tag.dto.TagOpenCmd;

@Component
public class TagCmdExe {

    @Resource
    private TagMapper tagMapper;

    /**
     * 创建商品标签
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createTag(TagCmd cmd) {
        TagDO tagDO = TagConvertor.toTagDo(cmd, Constant.OPERATION_TYPE_1);
        tagMapper.createTag(tagDO);
    }

    /**
     * 更新商品标签
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(TagCmd cmd) {
        TagDO tagDO = TagConvertor.toTagDo(cmd, Constant.OPERATION_TYPE_2);
        tagMapper.updateTag(tagDO);
    }

    /**
     * 更新商品标签状态
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openTag(TagOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(Constant.OPEN_NO)) {
            // 停用商品标签前，确保标签下的商品都已转移或删除
            Integer count = tagMapper.getTagGoodsCount(cmd.getId());
            if (count > 0) {
                throw GoodsException.buildException(B_TAG_GOODSNOTNUL);
            }
        }
        tagMapper.openTag(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 删除商品标签
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(TagId cmd) {
        TagDO tagDO = tagMapper.getById(cmd.getId());
        if (tagDO == null) {
            throw GoodsException.buildException(B_TAG_NULL);
        }
        // 停用的商品标签才允许删除
        if (!tagDO.getOpenFlag().equals(Constant.OPEN_NO)) {
            throw GoodsException.buildException(B_TAG_DELETE_OPEN_ERROR);
        }
        tagMapper.deleteTag(cmd.getId());
    }

}
