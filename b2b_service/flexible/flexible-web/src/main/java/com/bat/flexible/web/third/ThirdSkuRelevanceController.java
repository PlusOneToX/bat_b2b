package com.bat.flexible.web.third;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.third.ThirdSkuRelevanceServiceI;
import com.bat.flexible.api.third.dto.page.ThirdSkuRelevancePageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.third.co.ThirdSkuRelevancePageCO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import java.util.Map;

@RestController
@RequestMapping("/flexible/v1/web/admin/u/p/thirdSkuRelevance")
@Api(tags = "三方sku关联后台管理接口")
public class ThirdSkuRelevanceController extends BaseController {

    @Autowired
    private ThirdSkuRelevanceServiceI thirdSkuRelevanceServiceI;


    @GetMapping(value = "/page")
    @ApiOperation(value = "分页")
    public Response<PageInfo<ThirdSkuRelevancePageCO>> page(@Valid ThirdSkuRelevancePageQry thirdSkuRelevancePageQry){
        PageInfo<ThirdSkuRelevancePageCO> pageInfo = thirdSkuRelevanceServiceI.page(thirdSkuRelevancePageQry);
        return Response.of(pageInfo);
    }
    /**
     * 三方商品skuexcel导出（摩乐吉）
     * @param request
     * @param response
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ThirdSkuRelevance, value = CommonLogTypeConstantDTO.ThirdSkuRelevanceExport)
    @PostMapping(value = "/export")
    @ApiOperation(value = "excel导出")
    public void export(HttpServletRequest request, HttpServletResponse response,ThirdSkuRelevancePageQry thirdSkuRelevancePageQry) throws IOException {
        HSSFWorkbook wb = thirdSkuRelevanceServiceI.export(thirdSkuRelevancePageQry);
        if(wb ==null){
            throw new FlexibleCustomException("无可用数据导出");
        }
        exportExcel("第三方sku对照.xls",wb,request,response);
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ThirdSkuRelevance, value = CommonLogTypeConstantDTO.ThirdSkuRelevanceUpdateStatus)
    @PutMapping(value = "/updateOpenFlag")
    @ApiOperation(value = "启用、禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){

        return thirdSkuRelevanceServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }

    /**
     * 批量删除
     * @param flexibleIdListDTO
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ThirdSkuRelevance, value = CommonLogTypeConstantDTO.ThirdSkuRelevanceBatchDelete)
    @DeleteMapping(value = "/batch")
    @ApiOperation(value = "批量删除")
    public Response deleteById(@Valid @RequestBody FlexibleIdListDTO flexibleIdListDTO){

        return thirdSkuRelevanceServiceI.deteleBatch(flexibleIdListDTO,getCurrentAdmin());
    }

    /**
     * 三方商品关联关系excel导入
     * @param request
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ThirdSkuRelevance, value = CommonLogTypeConstantDTO.ThirdSkuRelevanceImport)
    @PostMapping(value = "/import")
    @ApiOperation(value = "三方商品关联关系excel导入")
    public Response importCode(HttpServletRequest request, @RequestParam(value = "distributorId",required = false)Integer distributorId) throws Exception {

        Map<String,Object> map = null;
        Boolean isDelete = false;
        try {
            MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
            Iterator<String> itr = multipart.getFileNames();
            MultipartFile file = null;
            if (itr.hasNext()) {
                String str = itr.next();    //这个文件并不是原来的文件名
                file = multipart.getFile(str);
            }
            InputStream inputStream = file.getInputStream();
            map = thirdSkuRelevanceServiceI.deleteByDistributorId(distributorId,getCurrentAdmin());
            //删除完
            isDelete = true;
            return thirdSkuRelevanceServiceI.importSku(inputStream, getCurrentAdmin(),distributorId);
        } catch (Exception e ) {
            //重新插数据回去
            if(isDelete && map !=null){
                thirdSkuRelevanceServiceI.insertAgain(map);
            }
            return Response.buildFailure("THIRD_SKU_RELEVANCE_IMPORT_ERROR",e.getMessage());
        }
    }

    /**
     * 三方商品skuexcel模板导出（摩乐吉）
     * @param request
     * @param response
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ThirdSkuRelevance, value = CommonLogTypeConstantDTO.ThirdSkuRelevanceTempDownLoad)
    @PostMapping(value = "/tempDownLoad")
    @ApiOperation(value = "下载模板")
    public void tempDownLoad(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HSSFWorkbook wb =   thirdSkuRelevanceServiceI.downLoadExcel();
        if(wb ==null){
            throw new FlexibleCustomException("无可用数据导出");
        }
        exportExcel("第三方sku对照模板.xls",wb,request,response);
    }
}
