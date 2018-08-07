package com.bat.system.service.storesetting.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.storesetting.dto.*;
import com.bat.system.dao.storesetting.MobileChildMapper;
import com.bat.system.dao.storesetting.MobileItemMapper;
import com.bat.system.dao.storesetting.MobileMapper;
import com.bat.system.dao.storesetting.MobilePointMapper;
import com.bat.system.dao.storesetting.dataobject.MobileChildDO;
import com.bat.system.dao.storesetting.dataobject.MobileDO;
import com.bat.system.dao.storesetting.dataobject.MobileItemDO;
import com.bat.system.dao.storesetting.dataobject.MobilePointDO;
import com.bat.system.service.storesetting.constant.MobileModuleType;
import com.bat.system.service.storesetting.convertor.MobileConvertor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsStoreMobileRpcCmd;
import com.bat.system.api.storesetting.dto.*;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class MobileCmdExc {

    @Resource
    private MobileMapper mobileMapper;

    @Resource
    private MobileItemMapper mobileItemMapper;

    @Resource
    private MobileChildMapper mobileChildMapper;

    @Resource
    private MobilePointMapper mobilePointMapper;

    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;

    @Transactional(rollbackFor = Exception.class)
    public boolean createMobile(MobileCreateCmd cmd) {
        if (cmd.getModuleType().equals(MobileModuleType.GOODS_LIST)) {
            List<MobileDO> mobileDOS = mobileMapper.listByModuleType(MobileModuleType.GOODS_LIST);
            if (mobileDOS.size() > 0) {
                throw SystemException.buildException(ErrorCode.B_MOBILE_CANT_TWO_GOODS_LIST_EXISTS);
            }
        }
        BeanMap map = BeanMap.create(cmd);
        CheckUtils.checkMobileParams(map);
        MobileDO mobileDO = MobileConvertor.toMobileDO(cmd);
        mobileMapper.insert(mobileDO);
        if (!cmd.getModuleType().equals(MobileModuleType.GOODS_LIST)) {
            cmd.getList().forEach(mobileItemCmd -> {
                MobileItemDO mobileItemDO = new MobileItemDO();
                mobileItemDO.setMobileId(mobileDO.getId());
                mobileItemDO.setImageUrl(mobileItemCmd.getImageUrl());
                mobileItemDO.setWidthPercentage(mobileItemCmd.getWidthPercentage());
                mobileItemDO.setJumpType(mobileItemCmd.getJumpType());
                mobileItemDO.setJumpParams(mobileItemCmd.getJumpParams());
                mobileItemDO.setSubSort(mobileItemCmd.getSubSort());
                mobileItemDO.setStyleType(mobileItemCmd.getStyleType());
                mobileItemMapper.insert(mobileItemDO);
            });
        }
        // 商品推广处理逻辑
        if (cmd.getModuleType().equals(MobileModuleType.GOODS_EXTENSION)) {
            saveGoods(cmd, mobileDO.getId());
        }
        // 商品列表处理逻辑
        if (cmd.getModuleType().equals(MobileModuleType.GOODS_LIST)) {
            // 遍历每个分类
            for (MobileChildCmd mobileChildCmd : cmd.getMobileChildCmds()) {
                saveMobileChild(mobileChildCmd, mobileDO.getId());
            }
        }
        return true;
    }

    private void saveMobileChild(MobileChildCmd mobileChildCmd, Integer mobileId) {
        Integer mobileChildId = mobileChildCmd.getId();
        MobileChildDO mobileChildDO = new MobileChildDO();
        BeanUtils.copyProperties(mobileChildCmd, mobileChildDO);
        mobileChildDO.setParentId(mobileId);
        mobileChildMapper.insert(mobileChildDO);
        // 不是指定商品
        if (mobileChildCmd.getAppointType() != MobileModuleType.APPOINT_ITEM) {
            if (mobileChildId != null) {
                mobilePointMapper.deleteByMobileChildId(mobileChildId);
            }
            List<MobilePointDO> mobilePointDOS = new ArrayList<>();
            for (MobilePointCmd mobilePointCmd : mobileChildCmd.getMobilePoints()) {
                MobilePointDO mobilePointDO = new MobilePointDO();
                BeanUtils.copyProperties(mobilePointCmd, mobilePointDO);
                mobilePointDO.setMobileChildId(mobileChildDO.getId());
                mobilePointDOS.add(mobilePointDO);
            }
            if (mobilePointDOS.size() > 0) {
                mobilePointMapper.insertList(mobilePointDOS);
            }
            return;
        }
        // 指定商品
        saveGoods(mobileChildCmd.getGoodsIds(), mobileChildDO.getId());
    }

    // 商品列表处理
    private void saveGoods(List<MobileGoodsItem> goodsIds, Integer mobileChildId) {
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            List<GoodsStoreMobileRpcCmd> goodsStoreMobileRpcCmds = new ArrayList<>();
            for (int i = 0; i < goodsIds.size(); i++) {
                GoodsStoreMobileRpcCmd rpcCmd = new GoodsStoreMobileRpcCmd();
                rpcCmd.setId(goodsIds.get(i).getId());
                rpcCmd.setMobileId(mobileChildId);
                rpcCmd.setGoodsId(goodsIds.get(i).getGoodsId());
                rpcCmd.setSort(goodsIds.get(i).getSort());
                rpcCmd.setOperationType(goodsIds.get(i).getOperationType());
                rpcCmd.setModuleType(MobileModuleType.GOODS_LIST);
                goodsStoreMobileRpcCmds.add(rpcCmd);
            }
            goodsServiceRpc.insertUpdateGoodsStoreMobile(goodsStoreMobileRpcCmds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateMobile(MobileUpdateCmd cmd) {
        MobileDO mobileDO = mobileMapper.selectByPrimaryKey(cmd.getId());
        if (mobileDO == null) {
            throw SystemException.buildException(ErrorCode.B_MOBILE_ID_NOT_EXISTS);
        }
        BeanMap map = BeanMap.create(cmd);
        CheckUtils.checkMobileParams(map);
        MobileDO mobileDO1 = MobileConvertor.toMobileDO(cmd);
        mobileMapper.updateByPrimaryKeySelective(mobileDO1);
        mobileItemMapper.deleteByMobileId(mobileDO1.getId());
        cmd.getList().forEach(mobileItemCmd -> {
            MobileItemDO mobileItemDO = new MobileItemDO();
            mobileItemDO.setMobileId(cmd.getId());
            mobileItemDO.setImageUrl(mobileItemCmd.getImageUrl());
            mobileItemDO.setWidthPercentage(mobileItemCmd.getWidthPercentage());
            mobileItemDO.setJumpType(mobileItemCmd.getJumpType());
            mobileItemDO.setJumpParams(mobileItemCmd.getJumpParams());
            mobileItemDO.setSubSort(mobileItemCmd.getSubSort());
            mobileItemDO.setStyleType(mobileItemCmd.getStyleType());
            mobileItemMapper.insert(mobileItemDO);
        });
        // 如果是商品推广 更新商品
        if (cmd.getModuleType().equals(MobileModuleType.GOODS_EXTENSION)) {
            saveGoods(cmd, mobileDO1.getId());
        }

        // 如果商品列表
        if (cmd.getModuleType().equals(MobileModuleType.GOODS_LIST)) {
            mobileChildMapper.deleteByParentId(mobileDO.getId());
            // 遍历每个分类
            for (MobileChildCmd mobileChildCmd : cmd.getMobileChildCmds()) {
                saveMobileChild(mobileChildCmd, mobileDO.getId());
            }
            if (cmd.getModuleType().equals(MobileModuleType.GOODS_LIST)) {
                List<MobileDO> mobileDOS = mobileMapper.listByModuleType(MobileModuleType.GOODS_LIST);
                if (mobileDOS.size() > 1) {
                    throw SystemException.buildException(ErrorCode.B_MOBILE_CANT_TWO_GOODS_LIST_EXISTS);
                }
            }
        }
        return true;
    }

    // 商品推广添加处理
    private void saveGoods(MobileCreateCmd cmd, Integer id) {
        if (CollectionUtils.isNotEmpty(cmd.getGoodsIds())) {
            List<GoodsStoreMobileRpcCmd> goodsStoreMobileRpcCmds = new ArrayList<>();
            for (int i = 0; i < cmd.getGoodsIds().size(); i++) {
                GoodsStoreMobileRpcCmd rpcCmd = new GoodsStoreMobileRpcCmd();
                rpcCmd.setId(cmd.getGoodsIds().get(i).getId());
                rpcCmd.setMobileId(id);
                rpcCmd.setGoodsId(cmd.getGoodsIds().get(i).getGoodsId());
                rpcCmd.setSort(cmd.getGoodsIds().get(i).getSort());
                rpcCmd.setOperationType(cmd.getGoodsIds().get(i).getOperationType());
                rpcCmd.setModuleType(MobileModuleType.GOODS_EXTENSION);
                goodsStoreMobileRpcCmds.add(rpcCmd);
            }
            goodsServiceRpc.insertUpdateGoodsStoreMobile(goodsStoreMobileRpcCmds);
        }
    }

    // 商品推广更新
    private void saveGoods(MobileUpdateCmd cmd, Integer id) {
        if (CollectionUtils.isNotEmpty(cmd.getGoodsIds())) {
            List<GoodsStoreMobileRpcCmd> goodsStoreMobileRpcCmds = new ArrayList<>();
            for (int i = 0; i < cmd.getGoodsIds().size(); i++) {
                GoodsStoreMobileRpcCmd rpcCmd = new GoodsStoreMobileRpcCmd();
                rpcCmd.setId(cmd.getGoodsIds().get(i).getId());
                rpcCmd.setMobileId(id);
                rpcCmd.setGoodsId(cmd.getGoodsIds().get(i).getGoodsId());
                rpcCmd.setSort(cmd.getGoodsIds().get(i).getSort());
                rpcCmd.setOperationType(cmd.getGoodsIds().get(i).getOperationType());
                rpcCmd.setModuleType(MobileModuleType.GOODS_EXTENSION);
                goodsStoreMobileRpcCmds.add(rpcCmd);
            }
            goodsServiceRpc.insertUpdateGoodsStoreMobile(goodsStoreMobileRpcCmds);
        }
    }

    public boolean releaseMobile(MobileReleaseCmd cmd) {
        MobileDO mobileDO = mobileMapper.selectByPrimaryKey(cmd.getId());
        if (mobileDO == null) {
            throw SystemException.buildException(ErrorCode.B_MOBILE_ID_NOT_EXISTS);
        }
        mobileDO.setStatus(cmd.getReleaseStatus());
        Date date = new Date();
        mobileDO.setStatus(cmd.getReleaseStatus());
        mobileDO.setUpdateTime(date);
        mobileMapper.updateByPrimaryKeySelective(mobileDO);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMobileById(Integer id) {
        if (mobileMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_MOBILE_ID_NOT_EXISTS);
        }
        mobileMapper.deleteByPrimaryKey(id);
        mobileItemMapper.deleteByMobileId(id);
        return true;
    }

    public boolean deleteMobileItemById(Integer id) {
        mobileItemMapper.deleteByMobileId(id);
        return true;
    }

}
