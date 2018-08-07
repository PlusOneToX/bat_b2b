package com.bat.goods.service.tag;

// package by domain, not by duty

import javax.annotation.Resource;

import com.bat.goods.service.tag.executor.TagCmdExe;
import com.bat.goods.service.tag.executor.TagQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.tag.TagServiceI;
import com.bat.goods.api.tag.dto.TagCmd;
import com.bat.goods.api.tag.dto.TagId;
import com.bat.goods.api.tag.dto.TagListQry;
import com.bat.goods.api.tag.dto.TagOpenCmd;
import com.bat.goods.api.tag.dto.data.TagDTO;

@Service
public class TagServiceImpl implements TagServiceI {

    @Resource
    private TagCmdExe tagCmdExe;

    @Resource
    private TagQryExe tagQryExe;

    @Override
    public void createTag(TagCmd cmd) {
        tagCmdExe.createTag(cmd);
    }

    @Override
    public void updateTag(TagCmd cmd) {
        tagCmdExe.updateTag(cmd);
    }

    @Override
    public void openTag(TagOpenCmd cmd) {
        tagCmdExe.openTag(cmd);
    }

    @Override
    public PageInfo<TagDTO> listTag(TagListQry tagListQry) {
        return tagQryExe.executeList(tagListQry);
    }

    @Override
    public void deleteTag(TagId cmd) {
        tagCmdExe.deleteTag(cmd);
    }

    @Override
    public TagDTO getTag(TagId tagId) {
        return tagQryExe.execute(tagId);
    }

}