package com.bat.system.service.storesetting;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.storesetting.ColumnService;
import com.bat.system.api.storesetting.dto.ColumnCreateCmd;
import com.bat.system.api.storesetting.dto.ColumnQry;
import com.bat.system.api.storesetting.dto.ColumnReleaseCmd;
import com.bat.system.api.storesetting.dto.ColumnUpdateCmd;
import com.bat.system.api.storesetting.dto.data.ColumnDTO;
import com.bat.system.service.storesetting.executor.ColumnCmdExc;
import com.bat.system.service.storesetting.executor.ColumnQryExc;
import com.bat.system.service.storesetting.executor.ErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/28 17:13
 */
@Service
public class ColumnServiceImpl implements ColumnService {

    @Resource
    private ColumnQryExc columnQryExc;

    @Resource
    private ColumnCmdExc columnCmdExc;

    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;

    @Override
    public PageInfo<ColumnDTO> listColumn(ColumnQry qry) {
        return columnQryExc.listColumn(qry);
    }

    @Override
    public ColumnDTO getColumn(Integer id) {
        return columnQryExc.getColumn(id);
    }

    @Override
    public void clearanceGoodsStoreColumn(Integer id) {
        try {
            Response response = goodsServiceRpc.clearanceGoodsStoreColumn(id);
            if (!response.isSuccess()) {
                throw SystemException.buildException(response.getErrCode(), response.getErrMessage());
            }
        } catch (SystemException e) {
            throw SystemException.buildException(ErrorCode.B_CLEARANCE_GOODS_STORE_COLUMN_ERROR);
        }
    }

    @Override
    public void clearGoodsStoreColumn(Integer id) {
        try {
            Response response = goodsServiceRpc.clearGoodsStoreColumn(id);
            if (!response.isSuccess()) {
                throw SystemException.buildException(response.getErrCode(), response.getErrMessage());
            }
        } catch (SystemException e) {
            throw SystemException.buildException(ErrorCode.B_CLEAR_GOODS_STORE_COLUMN_ERROR);
        }
    }

    @Override
    public boolean createColumn(ColumnCreateCmd cmd) {
        return columnCmdExc.createColumn(cmd);
    }

    @Override
    public boolean updateColumn(ColumnUpdateCmd cmd) {
        return columnCmdExc.updateColumn(cmd);
    }

    @Override
    public boolean releaseColumn(ColumnReleaseCmd cmd) {
        return columnCmdExc.releaseColumn(cmd);
    }

    @Override
    public boolean deleteColumnById(Integer id) {
        return columnCmdExc.deleteColumnById(id);
    }

}
