package com.bat.system.service.storesetting.executor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.storesetting.dto.SectionCreateCmd;
import com.bat.system.api.storesetting.dto.SectionReleaseCmd;
import com.bat.system.api.storesetting.dto.SectionUpdateCmd;
import com.bat.system.dao.storesetting.SectionMapper;
import com.bat.system.dao.storesetting.dataobject.SectionDO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.GoodsStoreSectionRpcCmd;
import com.bat.system.service.storesetting.convertor.SectionConvertor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 16:53
 */
@Component
public class SectionCmdExc {

    @Resource
    private SectionMapper sectionMapper;

    @DubboReference(check = false, timeout = 30000)
    private GoodsServiceRpc goodsServiceRpc;

    @Transactional(rollbackFor = Exception.class)
    public boolean createSection(SectionCreateCmd cmd) {
        BeanMap map = BeanMap.create(cmd);
        CheckUtils.checkSectionParams(map);
        SectionDO sectionDO = SectionConvertor.toSectionDO(cmd);
        sectionMapper.insert(sectionDO);
        saveGoods(cmd, sectionDO.getId());
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateSection(SectionUpdateCmd cmd) {
        BeanMap map = BeanMap.create(cmd);
        CheckUtils.checkSectionParams(map);
        SectionDO sectionDO = SectionConvertor.toSectionDO(cmd);
        sectionMapper.updateByPrimaryKeySelective(sectionDO);
        saveGoods(cmd, sectionDO.getId());
        return true;
    }

    private void saveGoods(SectionCreateCmd cmd, Integer id) {
        if (CollectionUtils.isNotEmpty(cmd.getGoodsIds())) {
            List<GoodsStoreSectionRpcCmd> goodsStoreSectionRpcCmds = new ArrayList<>();
            for (int i = 0; i < cmd.getGoodsIds().size(); i++) {
                GoodsStoreSectionRpcCmd rpcCmd = new GoodsStoreSectionRpcCmd();
                rpcCmd.setId(cmd.getGoodsIds().get(i).getSectionId());
                rpcCmd.setSectionId(id);
                rpcCmd.setGoodsId(cmd.getGoodsIds().get(i).getGoodsId());
                rpcCmd.setSort(cmd.getGoodsIds().get(i).getSort());
                rpcCmd.setOperationType(cmd.getGoodsIds().get(i).getOperationType());
                goodsStoreSectionRpcCmds.add(rpcCmd);
            }
            goodsServiceRpc.insertUpdateGoodsStoreSection(goodsStoreSectionRpcCmds);
        }
    }

    private void saveGoods(SectionUpdateCmd cmd, Integer id) {
        if (CollectionUtils.isNotEmpty(cmd.getGoodsIds())) {
            List<GoodsStoreSectionRpcCmd> goodsStoreSectionRpcCmds = new ArrayList<>();
            for (int i = 0; i < cmd.getGoodsIds().size(); i++) {
                GoodsStoreSectionRpcCmd rpcCmd = new GoodsStoreSectionRpcCmd();
                rpcCmd.setId(cmd.getGoodsIds().get(i).getSectionId());
                rpcCmd.setSectionId(id);
                rpcCmd.setGoodsId(cmd.getGoodsIds().get(i).getGoodsId());
                rpcCmd.setSort(cmd.getGoodsIds().get(i).getSort());
                rpcCmd.setOperationType(cmd.getGoodsIds().get(i).getOperationType());
                goodsStoreSectionRpcCmds.add(rpcCmd);
            }
            goodsServiceRpc.insertUpdateGoodsStoreSection(goodsStoreSectionRpcCmds);
        }
    }

    public boolean releaseSection(SectionReleaseCmd cmd) {
        SectionDO sectionDO = sectionMapper.selectByPrimaryKey(cmd.getId());
        if (sectionDO == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_ID_NOT_EXISTS);
        }
        sectionDO.setReleaseStatus(cmd.getReleaseStatus());
        Date date = new Date();
        sectionDO.setReleaseStatus(cmd.getReleaseStatus());
        sectionDO.setUpdateTime(date);
        sectionMapper.updateByPrimaryKeySelective(sectionDO);
        return true;
    }

    public boolean deleteSectionById(Integer id) {
        if (sectionMapper.selectByPrimaryKey(id) == null) {
            throw SystemException.buildException(ErrorCode.B_SECTION_ID_NOT_EXISTS);
        }
        sectionMapper.deleteByPrimaryKey(id);
        return true;
    }

}
