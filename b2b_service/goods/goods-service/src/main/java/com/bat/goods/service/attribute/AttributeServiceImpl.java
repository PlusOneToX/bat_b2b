package com.bat.goods.service.attribute;

// package by domain, not by duty

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.api.attribute.dto.*;
import com.bat.goods.service.attribute.executor.AttributeCmdExe;
import com.bat.goods.service.attribute.executor.AttributeQryExe;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.attribute.AttributeServiceI;
import com.bat.goods.api.attribute.dto.*;
import com.bat.goods.api.attribute.dto.data.AttributeDTO;
import com.bat.goods.api.attribute.dto.data.AttributeValueDTO;

@Service
public class AttributeServiceImpl implements AttributeServiceI {

    @Resource
    private AttributeCmdExe attributeCmdExe;

    @Resource
    private AttributeQryExe attributeQryExe;

    @Override
    public void createAttribute(AttributeCmd cmd) {
        attributeCmdExe.createAttribute(cmd);
    }

    @Override
    public void updateAttribute(AttributeCmd cmd) {
        attributeCmdExe.updateAttribute(cmd);
    }

    @Override
    public void openAttribute(AttributeOpenCmd cmd) {
        attributeCmdExe.openAttribute(cmd);
    }

    @Override
    public PageInfo<AttributeDTO> listAttribute(AttributeListQry qry) {
        return attributeQryExe.executeList(qry);
    }

    @Override
    public void deleteAttribute(AttributeId cmd) {
        attributeCmdExe.deleteAttribute(cmd);
    }

    @Override
    public AttributeDTO getAttribute(AttributeId id) {
        return attributeQryExe.execute(id);
    }

    @Override
    public PageInfo<AttributeValueDTO> listAttributeValue(AttributeValueListQry qry) {
        return attributeQryExe.executeAttributeValues(qry);
    }

    @Override
    public List<AttributeValueDTO> listAllAttributeValue(AttributeId id) {
        return attributeQryExe.executeAllAttributeValues(id);
    }

}