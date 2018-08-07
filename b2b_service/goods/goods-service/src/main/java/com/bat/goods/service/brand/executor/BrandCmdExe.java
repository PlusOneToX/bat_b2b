package com.bat.goods.service.brand.executor;

import java.util.List;

import javax.annotation.Resource;

import com.bat.goods.dao.brand.BrandDistributorGroupRelevanceMapper;
import com.bat.goods.dao.brand.BrandMapper;
import com.bat.goods.dao.brand.dataobject.BrandDO;
import com.bat.goods.service.brand.convertor.BrandConvertor;
import com.bat.goods.service.common.Constant;
import com.bat.goods.service.message.MessageSendService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.brand.dto.BrandCmd;
import com.bat.goods.api.brand.dto.BrandId;
import com.bat.goods.api.brand.dto.BrandOpenCmd;

@Component
public class BrandCmdExe {

    @Resource
    private BrandMapper brandMapper;
    @Resource
    private BrandRpcCmdExe rpcCmdExe;
    @Resource
    private BrandDistributorGroupRelevanceMapper distributorGroupRelevanceMapper;
    @Resource
    private MessageSendService sendService;

    /**
     * 创建品牌
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createBrand(BrandCmd cmd) {
        BrandDO brandDO = BrandConvertor.toBrandDo(cmd, Constant.OPERATION_TYPE_1);
        brandMapper.createBrand(brandDO);
        saveBrandScope(brandDO.getId(), cmd);
    }

    /**
     * 更新品牌
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBrand(BrandCmd cmd) {
        BrandDO brandDO = BrandConvertor.toBrandDo(cmd, Constant.OPERATION_TYPE_2);
        brandMapper.updateBrand(brandDO);
        deleteBrandScope(brandDO.getId());
        saveBrandScope(brandDO.getId(), cmd);
    }

    /**
     * 品牌状态变更
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void openBrand(BrandOpenCmd cmd) {
        if (cmd.getOpenFlag().equals(Constant.OPEN_NO)) {
            // 查看品牌下是否还有商品
            Integer count = brandMapper.getBrandGoodsCount(cmd.getId());
            if (count > 0) {
                throw GoodsException.buildException(ErrorCode.B_BRAND_GOODSNOTNULL);
            }
        }
        brandMapper.openBrand(cmd.getId(), cmd.getOpenFlag());
    }

    /**
     * 删除品牌
     * 
     * @param cmd
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBrand(BrandId cmd) {
        BrandDO brandDO = brandMapper.getById(cmd.getId());
        if (brandDO == null) {
            throw GoodsException.buildException(ErrorCode.B_BRAND_NULL);
        }
        // 停用的品牌才允许删除
        if (!brandDO.getOpenFlag().equals(Constant.OPEN_NO)) {
            throw GoodsException.buildException(ErrorCode.B_BRAND_DELETE_OPEN_ERROR);
        }
        // TODO 删除品牌关联数据
        brandMapper.deleteBrand(cmd.getId());
    }

    /**
     * 保存品牌可视和不可视关系
     * 
     * @param brandId
     */
    public void saveBrandScope(Integer brandId, BrandCmd cmd) {
        // 可视关系
        List list = BrandConvertor.toBrandRelevance(brandId, cmd);
        if (list != null && list.size() > 0) {
            Short distributorScope = cmd.getDistributorScope();
            if (distributorScope.equals(Constant.SCOPE_SCALE_PRICE)) {
                brandMapper.createBrandScalePriceRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR)) {
                brandMapper.createBrandDistributorRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DEPARTMENT)) {
                brandMapper.createBrandDepartmentRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_ADMIN)) {
                brandMapper.createBrandAdminRelevanceList(list);
            } else if (distributorScope.equals(Constant.SCOPE_DISTRIBUTOR_GROUP)) {
                distributorGroupRelevanceMapper.insertList(list);
            }
        }
        // 不可视关系
        List listNo = BrandConvertor.toBrandRelevanceNO(brandId, cmd);
        if (listNo != null && listNo.size() > 0) {
            Short distributorScopeNo = cmd.getDistributorScopeNo();
            if (distributorScopeNo.equals(Constant.SCOPE_SCALE_PRICE)) {
                brandMapper.createBrandScalePriceRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_DISTRIBUTOR)) {
                brandMapper.createBrandDistributorRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_DEPARTMENT)) {
                brandMapper.createBrandDepartmentRelevanceNoList(listNo);
            } else if (distributorScopeNo.equals(Constant.SCOPE_ADMIN)) {
                brandMapper.createBrandAdminRelevanceNoList(listNo);
            }
        }
        // 更新分销商品牌可视范围
        rpcCmdExe.distributorBrandRelevance(brandId, cmd);
        // 更新分销商商品可视范围
        sendService.syncBrandGoodsScope(brandId);
    }

    /**
     * 删除品牌可视和不可视关系
     * 
     * @param brandId
     */
    public void deleteBrandScope(Integer brandId) {
        brandMapper.deleteBrandScalePriceRelevance(brandId);
        brandMapper.deleteBrandScalePriceRelevanceNo(brandId);
        brandMapper.deleteBrandDistributorRelevance(brandId);
        brandMapper.deleteBrandDistributorRelevanceNo(brandId);
        brandMapper.deleteBrandDepartmentRelevance(brandId);
        brandMapper.deleteBrandDepartmentRelevanceNo(brandId);
        brandMapper.deleteBrandAdminRelevance(brandId);
        brandMapper.deleteBrandAdminRelevanceNo(brandId);
        distributorGroupRelevanceMapper.deleteByBrandId(brandId);
    }

}
