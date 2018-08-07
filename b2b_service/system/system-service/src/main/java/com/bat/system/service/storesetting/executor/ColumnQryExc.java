package com.bat.system.service.storesetting.executor;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.dto.ColumnQry;
import com.bat.system.api.storesetting.dto.data.ColumnDTO;
import com.bat.system.dao.storesetting.*;
import com.bat.system.dao.storesetting.dataobject.*;
import com.bat.system.service.storesetting.convertor.ColumnConvertor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.system.dao.storesetting.*;
import com.bat.system.dao.storesetting.dataobject.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class ColumnQryExc {
    @Resource
    private ColumnMapper columnMapper;

    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;

    @Resource
    private ColumnDepartmentMapper columnDepartmentMapper;

    @Resource
    private ColumnDistributorMapper columnDistributorMapper;

    @Resource
    private ColumnDistributorGradeMapper columnDistributorGradeMapper;

    @Resource
    private ColumnGoodsMapper columnGoodsMapper;

    @Resource
    private ColumnUserMapper columnUserMapper;

    public PageInfo<ColumnDTO> listColumn(ColumnQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        BeanMap map = BeanMap.create(qry);
        List<ColumnDO> columnDOS = columnMapper.listByParams(map);
        PageInfo pageInfo = new PageInfo(columnDOS);
        List<ColumnDTO> toRoleDTOList = ColumnConvertor.toColumnDTOList(pageInfo.getList());
        pageInfo.setList(toRoleDTOList);
        return pageInfo;
    }

    public ColumnDTO getColumn(Integer id) {
        ColumnDO columnDO = columnMapper.selectByPrimaryKey(id);
        if (columnDO != null) {
            ColumnDTO columnDTO = ColumnConvertor.toColumnDTO(columnDO);
            // columnDTO.setGoodsIds(null);
            columnDTO.setDepartmentIds(columnDepartmentMapper.listByColumnId(id).stream()
                .map(ColumnDepartmentDO::getDepartmentId).collect(Collectors.toList()));
            columnDTO.setGradeIds(columnDistributorGradeMapper.listByColumnId(id).stream()
                .map(ColumnDistributorGradeDO::getDistributorGradeId).collect(Collectors.toList()));
            columnDTO.setDistributorIds(columnDistributorMapper.listByColumnId(id).stream()
                .map(ColumnDistributorDO::getDistributorId).collect(Collectors.toList()));
            columnDTO.setUserIds(
                columnUserMapper.listByColumnId(id).stream().map(ColumnUserDO::getUserId).collect(Collectors.toList()));
            return columnDTO;
        }
        return null;
    }
}
