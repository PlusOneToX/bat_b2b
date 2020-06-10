package com.bat.flexible.web.relevance;

import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevancePageQry;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceSkuNoAndBomCmd;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "/flexible/v1/web/admin/u")
@Api(tags = "型号材质关联后台管理接口")
public class AdminModelMaterialRelevanceController  extends BaseController {

    @Autowired
    private ModelMaterialRelevanceServiceI modelMaterialRelevanceServiceI;


    @GetMapping("/p/modelMaterialRelevance/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<ModelMaterialRelevanceCO>> page(@Valid ModelMaterialRelevancePageQry modelMaterialRelevancePageQry){
        PageInfo<ModelMaterialRelevanceCO> pageInfo = modelMaterialRelevanceServiceI.page(modelMaterialRelevancePageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModelMaterialRelevance, value = CommonLogTypeConstantDTO.AdminModelMaterialRelevanceUpdateSkuNoAndBomCode)
    @PutMapping(value = "/p/modelMaterialRelevance")
    @ApiOperation(value = "修改sku编码和BOM编码")
    public Response updateSkuNoAndBomCode(@Valid @RequestBody ModelMaterialRelevanceSkuNoAndBomCmd relevanceSkuNoAndBomCmd){
        return modelMaterialRelevanceServiceI.updateSkuAndBomCode(relevanceSkuNoAndBomCmd,getCurrentAdmin());
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModelMaterialRelevance, value = CommonLogTypeConstantDTO.AdminModelMaterialRelevanceUpdateStatus)
    @PutMapping(value = "/p/modelMaterialRelevance/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody FlexibleUpdateStatusDTO flexibleUpdateStatusDTO){
        return modelMaterialRelevanceServiceI.updateOpenFlag(flexibleUpdateStatusDTO,getCurrentAdmin());
    }
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModelMaterialRelevance, value = CommonLogTypeConstantDTO.AdminModelMaterialRelevanceDelete)
    @DeleteMapping(value = "/p/modelMaterialRelevance")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){
        return modelMaterialRelevanceServiceI.deleteById(flexibleIdDTO.getId(),getCurrentAdmin());
    }

    /**
     * 导出
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminModelMaterialRelevance, value = CommonLogTypeConstantDTO.AdminModelMaterialRelevanceExcelExport)
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出")
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = modelMaterialRelevanceServiceI.exportExcel();

        exportExcel("导出型号材质关联关系.xls",wb,request,response);

    }
}
