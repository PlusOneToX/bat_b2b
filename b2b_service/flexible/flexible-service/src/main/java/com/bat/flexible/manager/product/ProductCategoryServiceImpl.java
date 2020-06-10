package com.bat.flexible.manager.product;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.product.ProductCategoryServiceI;
import com.bat.flexible.api.product.dto.ProductCategoryCmd;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.product.executor.ProductCategoryQryExe;
import com.bat.flexible.api.product.dto.ProductCategoryPageQry;
import com.bat.flexible.dao.product.dataobject.ProductCategoryDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.product.convertor.ProductCategoryConvertor;
import com.bat.flexible.manager.product.executor.ProductCategoryCmdExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryServiceI {

    @Autowired
    private ProductCategoryQryExe productCategoryQryExe;

    @Autowired
    private ProductCategoryCmdExe productCategoryCmdExe;

    @Override
    public ProductCategoryDO getById(Integer categoryId) {
        return productCategoryQryExe.getById(categoryId);
    }

    @Override
    @Transactional
    public Response create(ProductCategoryCmd productCategoryCmd, AdminResponse currentAdmin) {
        ProductCategoryDO productCategoryDO = new ProductCategoryDO();
        productCategoryDO.setName(productCategoryCmd.getName());
        productCategoryDO.setEnglishName(productCategoryCmd.getEnglishName());
        productCategoryDO.setRemark(productCategoryCmd.getRemark());
        productCategoryDO.setOpenFlag(productCategoryCmd.getOpenFlag());
        productCategoryDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        setAdminMsg(productCategoryDO,currentAdmin);
        productCategoryCmdExe.create(productCategoryDO);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response update(ProductCategoryCmd productCategoryCmd, AdminResponse currentAdmin) {
        ProductCategoryDO productCategoryDO = productCategoryQryExe.getById(productCategoryCmd.getId());
        productCategoryDO.setName(productCategoryCmd.getName());
        productCategoryDO.setEnglishName(productCategoryCmd.getEnglishName());
        productCategoryDO.setRemark(productCategoryCmd.getRemark());
        productCategoryDO.setOpenFlag(productCategoryCmd.getOpenFlag());
        setAdminMsg(productCategoryDO,currentAdmin);
        productCategoryCmdExe.update(productCategoryDO);
        return Response.buildSuccess();
    }

    @Override
    public PageInfo<ProductCategoryCmd> page(ProductCategoryPageQry categoryPageQry) {
        PageHelper.startPage(categoryPageQry.getPage(),categoryPageQry.getSize());
        List<ProductCategoryDO> categoryDOList = productCategoryQryExe.listByCondition(categoryPageQry.getContent(),null);
        List<ProductCategoryCmd> list = BeanUtils.copyList(categoryDOList,ProductCategoryCmd.class);
        if(list ==null || list.size()==0){
            list = new ArrayList<>();
        }
        return new PageInfo<>(list);
    }

    @Override
    public List<ProductCategoryDO> listByCondition(String content) {
        return productCategoryQryExe.listByCondition(content,null);
    }

    @Override
    public Response<ProductCategoryCmd> detailById(Integer id) {
        ProductCategoryDO productCategoryDO = productCategoryQryExe.getById(id);
        ProductCategoryCmd categoryCmd =BeanUtils.copy(productCategoryDO,ProductCategoryCmd.class);
        return Response.of(categoryCmd);
    }

    @Override
    public List<ProductCategoryCmd> listDTOUsable() {
        List<ProductCategoryDO> doList = productCategoryQryExe.listByCondition(null,FlexibleCommonConstant.COMMON_OPEN_FLAG_YES);
        return ProductCategoryConvertor.toProductCategoryCmdList(doList);
    }


    private void setAdminMsg(ProductCategoryDO productCategoryDO, AdminResponse currentAdmin) {
        if(productCategoryDO.getId()==null){
            productCategoryDO.setCreateTime(new Date());
            productCategoryDO.setCreateUserId(currentAdmin.getId());
            productCategoryDO.setCreateUserName(currentAdmin.getUserName());
        }
        productCategoryDO.setUpdateTime(new Date());
        productCategoryDO.setUpdateUserId(currentAdmin.getId());
        productCategoryDO.setUpdateUserName(currentAdmin.getUserName());
    }
}
