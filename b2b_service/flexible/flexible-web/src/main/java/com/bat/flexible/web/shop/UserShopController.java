package com.bat.flexible.web.shop;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.shop.ShopServiceI;
import com.bat.flexible.api.shop.dto.*;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.shop.dto.*;
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
@RequestMapping(value ="/flexible/v1/web/user/shop")
@Api(tags = "门店前台接口")
public class UserShopController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserShopController.class);

    @Autowired
    private ShopServiceI shopServiceI;


    @GetMapping(value = "/getShopByDistributorIdAndShopCode")
    @ApiOperation(value = "根据门店编码查询")
    public Response<ShopDO> getShopByDistributorIdAndShopCode(@Valid ShopCodeQry shopCodeQry) {
        ShopDO shopDO = shopServiceI.getShopByDistributorIdAndShopCode(shopCodeQry.getDistributorId(),shopCodeQry.getShopCode());
        return Response.of(shopDO);
    }


    @GetMapping
    @ApiOperation(value = "根据id查询店铺信息")
    public Response<ShopDO> getById(@Valid FlexibleIdDTO flexibleIdDTO) {
        LOGGER.info("into getShop params:{}", flexibleIdDTO);
        ShopDO shopDO = shopServiceI.getById(flexibleIdDTO.getId());
        return Response.of(shopDO);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopUpdateStatus)
    @PutMapping(value = "/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return shopServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }


    @GetMapping(value = "/detailById")
    @ApiOperation(value = "查询门店详情")
    public Response<ShopPageCO> detailById(@Valid FlexibleIdDTO flexibleIdDTO){
        return shopServiceI.detailById(flexibleIdDTO.getId());
    }


    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<ShopPageCO>> pageByUser(@Valid UserShopPageQry userShopPageQry){;
      PageInfo<ShopPageCO> pageInfo = shopServiceI.pageByUser(userShopPageQry);
      return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopAdd)
    @PostMapping
    @ApiOperation(value = "新增门店")
    public Response create(@Valid @RequestBody ShopCmd shopCmd){
        return shopServiceI.create(shopCmd,getCurrentAdmin(),false);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopUpdate)
    @PutMapping
    @ApiOperation(value = "修改门店信息")
    public Response update(@Valid @RequestBody ShopCmd shopCmd){
        return shopServiceI.update(shopCmd,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopDelete)
    @DeleteMapping
    @ApiOperation(value = "单个删除")
    public Response deleteShop(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO) {
        LOGGER.info("into deleteShop params:{}", flexibleIdDTO.getId());
        shopServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopDeleteBatch)
    @DeleteMapping(value = "/batch")
    @ApiOperation(value = "批量删除")
    public Response deleteListShop(@Valid @RequestBody FlexibleIdListDTO flexibleIdListDTO) {

        return shopServiceI.batchDelete(flexibleIdListDTO.getIdList(),getCurrentAdmin());
    }

    /**
     * 下载导入模板
     * 
     * @param request
     * @param response
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopTempDownLoad)
    @PostMapping(value = "/tempDownLoad")
    @ApiOperation(value = "模板导出")
    public void ossAction(HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = shopServiceI.tempDownLoad(false);
        if(inputStream !=null){
            exportExcelByInputStream(request,response,inputStream,"门店导入模板.xls");
        }
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopImport)
    @PostMapping(value = "/import")
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
            return shopServiceI.importShop(inputStream, getCurrentAdmin(),false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return Response.buildFailure("100", e.getMessage());
        }
    }

    /**
     * 导出店铺 带图片
     *
     * @param request
     * @param response
     * @param shopExcelQry
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserShop, value = CommonLogTypeConstantDTO.UserShopExport)
    @PostMapping(value = "/export")
    @ApiOperation(value = "excel导出")
    public Response excelDownLoad(HttpServletRequest request, HttpServletResponse response,
                                  ShopExcelQry shopExcelQry) throws IOException {
        shopExcelQry.setDistributorId(getUserId());
        HSSFWorkbook wb = shopServiceI.export(shopExcelQry);
        if(wb ==null){
            throw new FlexibleCustomException("无可用数据导出");
        }
        exportExcel("门店清单.xls",wb,request,response);
        return null;
    }

    @GetMapping(value = "/listByCondition")
    @ApiOperation(value = "条件查询门店")
    public Response<List<ShopDO>> listByCondition(ShopListQry shopListQry){
        List<ShopDO> list = shopServiceI.listByCondition(shopListQry.getUserConfigId());
        return Response.of(list);
    }

}
