/*
 *
 *      Copyright (c) 2018-2099, lipengjun All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the fly2you.cn developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lipengjun (939961241@qq.com)
 *
 */
package com.platform.modules.mall.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Query;
import com.platform.common.utils.StringUtils;
import com.platform.modules.mall.dao.MallGoodsSkuDao;
import com.platform.modules.mall.dao.MallGoodsSpecificationDao;
import com.platform.modules.mall.dao.MallSpecificationDao;
import com.platform.modules.mall.entity.KeyValue;
import com.platform.modules.mall.entity.MallGoodsSkuEntity;
import com.platform.modules.mall.entity.MallGoodsSpecificationEntity;
import com.platform.modules.mall.entity.MallSpecificationEntity;
import com.platform.modules.mall.service.MallGoodsSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 商品SKUService实现类
 *
 * @author 李鹏军
 * @since 2019-07-04 00:05:33
 */
@Service("mallGoodsSkuService")
public class MallGoodsSkuServiceImpl extends ServiceImpl<MallGoodsSkuDao, MallGoodsSkuEntity> implements MallGoodsSkuService {

    @Autowired
    private MallSpecificationDao specificationDao;
    @Autowired
    private MallGoodsSpecificationDao goodsSpecificationDao;

    @Override
    public List<MallGoodsSkuEntity> queryAll(Map<String, Object> params) {
        List<MallGoodsSkuEntity> result = baseMapper.queryAll(params);
        if (null != result && result.size() > 0) {
            List<MallGoodsSpecificationEntity> list = goodsSpecificationDao.queryAll(params);
            List<KeyValue> keyValues;
            for (int i = 0; i < result.size(); i++) {
                keyValues = new ArrayList<>();
                for (MallGoodsSpecificationEntity entity : list) {
                    KeyValue m = new KeyValue();
                    m.setKey(entity.getName());
                    m.setValue(entity.getValue());
                    if (result.get(i).getId().equals(entity.getSkuId())) {
                        keyValues.add(m);
                    }
                }
                result.get(i).setKeyValue(keyValues);
            }
        }
        return result;
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.ID");
        params.put("asc", false);
        Page<MallGoodsSkuEntity> page = new Query<MallGoodsSkuEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMallGoodsSkuPage(page, params));
    }

    @Override
    public boolean add(MallGoodsSkuEntity mallGoodsSku) {
        return this.save(mallGoodsSku);
    }

    @Override
    public boolean update(MallGoodsSkuEntity mallGoodsSku) {
        return this.updateById(mallGoodsSku);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveGoodsProduct(List<MallGoodsSkuEntity> goodsSkuEntities) {
        if (null == goodsSkuEntities || goodsSkuEntities.size() == 0) {
            return 0;
        }
        String goodsId = goodsSkuEntities.get(0).getGoodsId();

        Map<String, Object> map = new HashMap<>(2);
        map.put("goodsId", goodsId);

        //产品修改先遍历出本次修改删除的条数
        List<MallGoodsSkuEntity> productEntities = baseMapper.queryAll(map);
        //库中原来存在的
        List<String> oldList = new ArrayList<>();
        if (productEntities != null && productEntities.size() > 0) {
            for (MallGoodsSkuEntity productEntity : productEntities) {
                oldList.add(productEntity.getId());
            }
        }
        //本次操作的
        List<String> newList = new ArrayList<>();
        for (MallGoodsSkuEntity product : goodsSkuEntities) {
            if (!StringUtils.isNullOrEmpty(product.getId())) {
                newList.add(product.getId());
            }
        }
        //求差集，取出本此操作删除的
        oldList.removeAll(newList);

        //删除
        if (oldList.size() > 0) {
            for (String id : oldList) {
                baseMapper.deleteById(id);
            }
        }

        map = new HashMap<>(4);
        map.put("GOODS_ID", goodsId);
        goodsSpecificationDao.deleteByMap(map);

        map.put("goodsId", goodsId);
        for (MallGoodsSkuEntity goodsSkuEntity : goodsSkuEntities) {
            //不存在的
            if (StringUtils.isNullOrEmpty(goodsSkuEntity.getId())) {
                baseMapper.insert(goodsSkuEntity);
            } else {
                baseMapper.updateById(goodsSkuEntity);
            }
            List<MallSpecificationEntity> specificationEntities = specificationDao.queryAll(map);
            if (null != specificationEntities && specificationEntities.size() > 0) {
                for (int i = 0; i < specificationEntities.size(); i++) {
                    MallGoodsSpecificationEntity goodsSpecificationEntity = new MallGoodsSpecificationEntity();
                    goodsSpecificationEntity.setGoodsId(goodsId);
                    goodsSpecificationEntity.setSkuId(goodsSkuEntity.getId());
                    goodsSpecificationEntity.setName(goodsSkuEntity.getKeyValue().get(i).getKey());
                    if ("".equals(goodsSkuEntity.getKeyValue().get(i).getValue())) {
                        throw new BusinessException("规格都为必填项");
                    }
                    goodsSpecificationEntity.setValue(goodsSkuEntity.getKeyValue().get(i).getValue());
                    goodsSpecificationDao.insert(goodsSpecificationEntity);
                }
            }
        }
        return 1;
    }
}
