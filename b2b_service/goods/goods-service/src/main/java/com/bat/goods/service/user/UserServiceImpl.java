package com.bat.goods.service.user;

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;
import com.bat.goods.service.user.executor.UserCmdExe;
import com.bat.goods.service.user.executor.UserQryExe;
import com.bat.goods.api.base.BaseIds;
import com.bat.goods.api.tag.dto.data.TagsDTO;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.user.UserServiceI;
import com.bat.goods.api.user.dto.*;
import com.bat.goods.api.user.dto.data.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/29 10:55
 */
@Service
public class UserServiceImpl implements UserServiceI {
    @Resource
    private UserQryExe qryExe;

    @Resource
    private UserCmdExe cmdExe;

    @Override
    public UserClassifyInfoDTO listClassify(UserClassifyListQry qry) {
        return qryExe.listClassify(qry);
    }

    @Override
    public List<UserClassifyDTO> listSubClassify(UserSubClassifyListQry qry) {
        return qryExe.listSubClassify(qry);
    }

    @Override
    public List<UserGoodsPriceDTO> priceGoodsList(UserGoodsPriceQry qry, String userId) {
        return qryExe.priceGoodsList(qry, userId);
    }

    @Override
    public List<UserGoodsItemPriceDTO> priceGoodsItemList(UserGoodsItemPriceQry qry, String userId) {
        return qryExe.priceGoodsItemList(qry, userId);
    }

    @Override
    public PageInfo<UserGoodsListDTO> columnGoodsList(UserGoodsListColumnQry qry, String userId) {
        return qryExe.columnGoodsList(qry, userId);
    }

    @Override
    public PageInfo<UserGoodsListDTO> sectionGoodsList(UserGoodsListSectionQry qry, String userId) {
        return qryExe.sectionGoodsList(qry, userId);
    }

    @Override
    public PageInfo<UserGoodsListDTO> mobileGoodsList(UserGoodsListMobileQry qry, String userId) {
        return qryExe.mobileGoodsList(qry, userId);
    }

    @Override
    public PageInfo<UserGoodsListDTO> goodsList(UserGoodsListQry qry, String userId) {
        return qryExe.goodsList(qry, userId);
    }

    @Override
    public List<UserClassifyDTO> columnClassifyList(UserClassifyListColumnQry qry) {
        return qryExe.columnClassifyList(qry);
    }

    @Override
    public List<UserClassifyDTO> sectionClassifyList(UserClassifyListSectionQry qry) {
        return qryExe.sectionClassifyList(qry);
    }

    @Override
    public List<UserClassifyDTO> mobileClassifyList(UserClassifyListMobileQry qry) {
        return qryExe.mobileClassifyList(qry);
    }

    @Override
    public UserGoodsDTO goods(UserGoodsId qry) {
        return qryExe.goods(qry);
    }

    @Override
    public Boolean getGoodsCollection(UserGoodsId qry, String userId) {
        return qryExe.getGoodsCollection(qry, userId);
    }

    @Override
    public void createGoodsCollection(UserGoodsId cmd, String userId) {
        cmdExe.createGoodsCollection(cmd, userId);
    }

    @Override
    public void deleteGoodsCollection(UserGoodsCollection cmd, String userId) {
        cmdExe.deleteGoodsCollection(cmd, userId);
    }

    @Override
    public List<TagsDTO> getGoodsLabelByGoodsIds(BaseIds qry) {
       return qryExe.getGoodsLabelByGoodsIds(qry);
    }

}
