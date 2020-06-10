package com.bat.flexible.api.shop;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.shop.dto.ShopCmd;
import com.bat.flexible.api.shop.dto.ShopExcelQry;
import com.bat.flexible.api.shop.dto.ShopPageQry;
import com.bat.flexible.api.shop.dto.UserShopPageQry;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.dao.shop.co.ShopPageCO;
import com.bat.flexible.dao.shop.dataobject.ShopDO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.InputStream;
import java.util.List;

public interface ShopServiceI {
    Response create(ShopCmd shopCmd, AdminResponse currentAdmin, Boolean fromAdmin);

    Response update(ShopCmd shopCmd, AdminResponse currentAdmin);

    Response updateCreate(ShopCmd shopCmd, AdminResponse currentAdmin);

    PageInfo<ShopPageCO> page(ShopPageQry shopPageQry);

    Response<ShopPageCO> detailById(Integer id);

    HSSFWorkbook export(ShopExcelQry shopExcelQry);

    Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin);

    Response deleteById(Integer id, AdminResponse currentAdmin);

    /**
     * 门店模板导出
     * @param fromAdmin true 后台 、false前台
     * @return
     */
    InputStream tempDownLoad(Boolean fromAdmin);

    /**
     * EXCEL导入门店
     * @param inputStream
     * @param currentAdmin
     * @param fromAdmin
     * @return
     */
    Response importShop(InputStream inputStream, AdminResponse currentAdmin,Boolean fromAdmin);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ShopDO getById(Integer id);

    /**
     * 根据分销商id和编码查询
     * @param distributorId
     * @param shopCode
     * @return
     */
    ShopDO getShopByDistributorIdAndShopCode(Integer distributorId, String shopCode);

    /**
     * 批量删除
     * @param idList
     * @param currentAdmin
     * @return
     */
    Response batchDelete(List<Integer> idList, AdminResponse currentAdmin);

    PageInfo<ShopPageCO> pageByUser(UserShopPageQry userShopPageQry);

    /**
     * 条件查询门店列表
     * @param userConfigId
     * @return
     */
    List<ShopDO> listByCondition(Integer userConfigId);


}
