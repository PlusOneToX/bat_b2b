package com.bat.goods.api.brand;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.brand.dto.BrandCmd;
import com.bat.goods.api.brand.dto.BrandId;
import com.bat.goods.api.brand.dto.BrandListQry;
import com.bat.goods.api.brand.dto.BrandOpenCmd;
import com.bat.goods.api.brand.dto.data.BrandDTO;

public interface BrandServiceI {
    /**
     * 添加品牌
     * 
     * @param brandCmd
     * @return
     */
    public void createBrand(BrandCmd brandCmd);

    /**
     * 更新品牌
     * 
     * @param brandCmd
     * @return
     */
    public void updateBrand(BrandCmd brandCmd);

    /**
     * 更新品牌状态
     * 
     * @param cmd
     * @return
     */
    public void openBrand(BrandOpenCmd cmd);

    /**
     * 获取品牌列表（分页）
     * 
     * @param brandListQry
     * @return
     */
    public PageInfo<BrandDTO> listBrand(BrandListQry brandListQry);

    /**
     * 根据ID删除品牌
     * 
     * @param id
     * @return
     */
    public void deleteBrand(BrandId id);

    /**
     * 根据品牌id获取详情
     * 
     * @param brandId
     * @return
     */
    public BrandDTO getBrand(BrandId brandId);

}
