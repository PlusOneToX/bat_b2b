package com.bat.goods.service.user.executor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.bat.goods.dao.goods.GoodsDistributorCollectionMapper;
import com.bat.goods.service.user.convertor.UserConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.user.dto.UserGoodsCollection;
import com.bat.goods.api.user.dto.UserGoodsId;
import com.bat.goods.dao.goods.dataobject.GoodsDistributorCollectionDO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/5/4 16:01
 */
@Component
public class UserCmdExe {

    @Resource
    private GoodsDistributorCollectionMapper collectionMapper;

    /**
     * 商品收藏
     * 
     * @param cmd
     * @param userId
     */
    public void createGoodsCollection(UserGoodsId cmd, String userId) {
        if (StringUtils.isBlank(userId)) {
            throw GoodsException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        GoodsDistributorCollectionDO collectionDO =
            UserConvertor.toGoodsDistributorCollectionDO(cmd.getId(), Integer.valueOf(userId));
        try {
            collectionMapper.insert(collectionDO);
        } catch (DuplicateKeyException e) {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_COLLECTION_ERROR);
        }
    }

    /**
     * 删除商品收藏
     * 
     * @param cmd
     * @param userId
     */
    public void deleteGoodsCollection(UserGoodsCollection cmd, String userId) {
        if (StringUtils.isBlank(userId)) {
            throw GoodsException.buildException(ErrorCode.B_USER_LOGIN_ERROR);
        }
        Map<String, Object> qryMap = new HashMap();
        if (cmd.getId() != null) {
            qryMap.put("goodsId", cmd.getId());
        }
        qryMap.put("distributorId", Integer.valueOf(userId));
        collectionMapper.deleteByMap(qryMap);
    }

}
