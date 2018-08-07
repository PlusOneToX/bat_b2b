package com.bat.system.service.storesetting.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.storesetting.dto.ColumnCreateCmd;
import com.bat.system.api.storesetting.dto.ColumnReleaseCmd;
import com.bat.system.api.storesetting.dto.ColumnUpdateCmd;
import com.bat.system.dao.storesetting.*;
import com.bat.system.dao.storesetting.dataobject.*;
import com.bat.system.service.storesetting.convertor.ColumnConvertor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsStoreColumnRpcCmd;
import com.bat.system.dao.storesetting.*;
import com.bat.system.dao.storesetting.dataobject.*;
import com.bat.system.service.common.utils.CommonCheckUtils;
import com.bat.system.service.logistics.constant.DistributorScopeConsts;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class ColumnCmdExc {

    @Resource
    private ColumnMapper columnMapper;

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

    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;

    @Transactional(rollbackFor = Exception.class)
    public boolean createColumn(ColumnCreateCmd cmd) {
        BeanMap map = BeanMap.create(cmd);
        CommonCheckUtils.checkDistributorScope(map);
        ColumnDO columnDO = ColumnConvertor.toColumnDO(cmd);
        columnMapper.insert(columnDO);
        saveDistributorScope(map, columnDO);
        saveGoods(cmd, columnDO.getId());
        return true;

    }

    private void saveGoods(ColumnCreateCmd cmd, Integer id) {
        if (CollectionUtils.isNotEmpty(cmd.getGoodsIds())) {
            List<GoodsStoreColumnRpcCmd> goodsStoreColumnRpcCmds = new ArrayList<>();
            for (int i = 0; i < cmd.getGoodsIds().size(); i++) {
                GoodsStoreColumnRpcCmd rpcCmd = new GoodsStoreColumnRpcCmd();
                rpcCmd.setId(cmd.getGoodsIds().get(i).getId());
                rpcCmd.setColumnId(id);
                rpcCmd.setGoodsId(cmd.getGoodsIds().get(i).getGoodsId());
                rpcCmd.setSort(cmd.getGoodsIds().get(i).getSort());
                rpcCmd.setOperationType(cmd.getGoodsIds().get(i).getOperationType());
                goodsStoreColumnRpcCmds.add(rpcCmd);
            }
            goodsServiceRpc.insertUpdateGoodsStoreColumn(goodsStoreColumnRpcCmds);
        }
    }

    private void saveGoods(ColumnUpdateCmd cmd, Integer id) {
        if (CollectionUtils.isNotEmpty(cmd.getGoodsIds())) {
            List<GoodsStoreColumnRpcCmd> goodsStoreColumnRpcCmds = new ArrayList<>();
            for (int i = 0; i < cmd.getGoodsIds().size(); i++) {
                GoodsStoreColumnRpcCmd rpcCmd = new GoodsStoreColumnRpcCmd();
                rpcCmd.setId(cmd.getGoodsIds().get(i).getId());
                rpcCmd.setColumnId(id);
                rpcCmd.setGoodsId(cmd.getGoodsIds().get(i).getGoodsId());
                rpcCmd.setSort(cmd.getGoodsIds().get(i).getSort());
                rpcCmd.setOperationType(cmd.getGoodsIds().get(i).getOperationType());
                goodsStoreColumnRpcCmds.add(rpcCmd);
            }
            Response response = goodsServiceRpc.insertUpdateGoodsStoreColumn(goodsStoreColumnRpcCmds);
            System.out.println(response);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateColumn(ColumnUpdateCmd cmd) {
        if (columnMapper.selectByPrimaryKey(cmd.getId()) == null) {
            throw SystemException.buildException(ErrorCode.B_COLUMN_ID_NOT_EXISTS);
        }
        BeanMap map = BeanMap.create(cmd);
        CommonCheckUtils.checkDistributorScope(map);
        ColumnDO columnDO = ColumnConvertor.toColumnDO(cmd);
        columnMapper.updateByPrimaryKeySelective(columnDO);
        // 更新：先删除 后新增
        deleteRelationTable(columnDO.getId());
        saveDistributorScope(map, columnDO);
        saveGoods(cmd, columnDO.getId());
        return false;
    }

    private void deleteRelationTable(Integer id) {
        columnDepartmentMapper.deleteByColumnId(id);
        columnDistributorMapper.deleteByColumnId(id);
        columnDistributorGradeMapper.deleteByColumnId(id);
        columnUserMapper.deleteByColumnId(id);
    }

    private void saveDistributorScope(Map<String, Object> map, ColumnDO columnDO) {
        // 可视范围
        switch ((short)map.get("distributorScope")) {
            // 全部分销商
            case DistributorScopeConsts.ALL_DISTRIBUTOR:
                break;
            // 指定的分销商等级
            case DistributorScopeConsts.APPOINT_DISTRIBUTOR_GRADE:
                List<Integer> gradeIds = (ArrayList)map.get("gradeIds");
                gradeIds.forEach(s -> {
                    ColumnDistributorGradeDO aDo = new ColumnDistributorGradeDO();
                    aDo.setColumnId(columnDO.getId());
                    aDo.setDistributorGradeId(s);
                    columnDistributorGradeMapper.insert(aDo);
                });
                break;
            // 指定的销售部门
            case DistributorScopeConsts.APPOINT_DEPARTMENT:
                List<Integer> departmentIds = (ArrayList)map.get("departmentIds");
                departmentIds.forEach(s -> {
                    ColumnDepartmentDO aDo = new ColumnDepartmentDO();
                    aDo.setColumnId(columnDO.getId());
                    aDo.setDepartmentId(s);
                    columnDepartmentMapper.insert(aDo);
                });
                break;
            // 指定的业务员（后台用户）
            case DistributorScopeConsts.APPOINT_USER:
                List<Integer> userIds = (ArrayList)map.get("userIds");
                userIds.forEach(s -> {
                    ColumnUserDO aDo = new ColumnUserDO();
                    aDo.setColumnId(columnDO.getId());
                    aDo.setUserId(s);
                    columnUserMapper.insert(aDo);
                });
                break;
            // 指定的分销商
            case DistributorScopeConsts.APPOINT_DISTRIBUTOR:
                List<Integer> distributorIds = (ArrayList)map.get("distributorIds");
                distributorIds.forEach(s -> {
                    ColumnDistributorDO aDo = new ColumnDistributorDO();
                    aDo.setColumnId(columnDO.getId());
                    aDo.setDistributorId(s);
                    columnDistributorMapper.insert(aDo);
                });
                break;
            default:
                break;
        }
    }

    public boolean releaseColumn(ColumnReleaseCmd cmd) {
        ColumnDO columnDO = columnMapper.selectByPrimaryKey(cmd.getId());
        if (columnDO == null) {
            throw SystemException.buildException(ErrorCode.B_NOTICE_ID_NOT_EXISTS);
        }
        columnDO.setReleaseStatus(cmd.getReleaseStatus());
        Date date = new Date();
        columnDO.setUpdateTime(date);
        columnMapper.updateByPrimaryKeySelective(columnDO);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteColumnById(Integer id) {
        ColumnDO columnDO = columnMapper.selectByPrimaryKey(id);
        if (columnDO == null) {
            throw SystemException.buildException(ErrorCode.B_NOTICE_ID_NOT_EXISTS);
        }
        columnMapper.deleteByPrimaryKey(id);
        deleteRelationTable(id);
        return true;
    }

}
