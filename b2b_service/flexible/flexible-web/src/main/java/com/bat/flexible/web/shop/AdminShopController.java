package com.bat.flexible.web.shop;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.shop.ShopServiceI;
import com.bat.flexible.api.shop.dto.ShopCmd;
import com.bat.flexible.api.shop.dto.ShopExcelQry;
import com.bat.flexible.api.shop.dto.ShopListQry;
import com.bat.flexible.api.shop.dto.ShopPageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.shop.co.ShopPageCO;
import com.bat.flexible.dao.shop.dataobject.ShopDO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping(value ="/flexible/v1/web/admin/u")
@Api(tags = "门店管理接口")
public class AdminShopController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminShopController.class);

    @Autowired
    private ShopServiceI shopServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShop, value = CommonLogTypeConstantDTO.AdminShopAdd)
    @PostMapping(value = "/p/shop")
    @ApiOperation(value = "新增门店")
    public Response create(@Valid @RequestBody ShopCmd shopCmd){
        return shopServiceI.create(shopCmd,getCurrentAdmin(),true);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShop, value = CommonLogTypeConstantDTO.AdminShopUpdate)
    @PutMapping(value = "/p/shop")
    @ApiOperation(value = "修改门店")
    public Response update(@Valid @RequestBody ShopCmd shopCmd){
        return shopServiceI.update(shopCmd,getCurrentAdmin());
    }

    @PutMapping(value = "/p/shop/create")
    @ApiOperation(value = "修改门店-生成图片")
    public Response updateCreate(@RequestBody ShopCmd shopCmd){
        return shopServiceI.updateCreate(shopCmd,getCurrentAdmin());
    }

    @GetMapping(value = "/p/shop/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<ShopPageCO>> page(@Valid ShopPageQry shopPageQry){
        PageInfo<ShopPageCO> pageInfo = shopServiceI.page(shopPageQry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/p/shop/detailById")
    @ApiOperation(value = "查询门店详情")
    public Response<ShopPageCO> detailById(@Valid FlexibleIdDTO flexibleIdDTO){
        return shopServiceI.detailById(flexibleIdDTO.getId());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShop, value = CommonLogTypeConstantDTO.AdminShopUpdateStatus)
    @PutMapping(value = "/p/shop/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return shopServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }

    /**
     * 导出店铺 带图片
     *
     * @param request
     * @param response
     * @param shopExcelQry
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShop, value = CommonLogTypeConstantDTO.AdminShopExport)
    @PostMapping(value = "/p/shop/export")
    @ApiOperation(value = "excel导出")
    public Response excelDownLoad(HttpServletRequest request, HttpServletResponse response,
                                  @RequestBody ShopExcelQry shopExcelQry) throws IOException {
        HSSFWorkbook wb = shopServiceI.export(shopExcelQry);
        if (wb == null) {
            throw new FlexibleCustomException("无可用数据导出");
        }
        exportExcel("门店清单.xls", wb, request, response);
        return null;
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShop, value = CommonLogTypeConstantDTO.AdminShopDelete)
    @DeleteMapping(value = "/p/shop")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO) {
        return shopServiceI.deleteById(flexibleIdDTO.getId(), getCurrentAdmin());
    }

    /**
     * 下载导入模板
     *
     * @param request
     * @param response
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShop, value = CommonLogTypeConstantDTO.AdminShopTempDownLoad)
    @PostMapping(value = "/p/importShop/tempDownLoad")
    @ApiOperation(value = "门店模板导出")
    public void ossAction(HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = shopServiceI.tempDownLoad(true);
        if(inputStream !=null){
            exportExcelByInputStream(request,response,inputStream,"门店导入模板.xls");
        }

    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminShop, value = CommonLogTypeConstantDTO.AdminShopImport)
    @PostMapping(value = "/p/shop/import")
    @ApiOperation(value = "门店excel导入")
    public Response importCode( HttpServletRequest request)  {
        try {
            MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
            Iterator<String> itr = multipart.getFileNames();
            MultipartFile file = null;
            if (itr.hasNext()) {
                String str = itr.next();    //这个文件并不是原来的文件名
                file = multipart.getFile(str);
            }
            InputStream inputStream = file.getInputStream();
            return shopServiceI.importShop(inputStream, getCurrentAdmin(),true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return Response.buildFailure("100", e.getMessage());
        }
    }

    @GetMapping(value = "/listByCondition")
    @ApiOperation(value = "条件查询门店")
    public Response<List<ShopDO>> listByCondition(ShopListQry shopListQry){
         List<ShopDO> list = shopServiceI.listByCondition(shopListQry.getUserConfigId());
        return Response.of(list);
    }
}
