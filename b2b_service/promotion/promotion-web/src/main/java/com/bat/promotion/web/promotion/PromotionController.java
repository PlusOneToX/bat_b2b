package com.bat.promotion.web.promotion;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.web.annotation.SysLog;
import com.bat.promotion.web.base.BaseController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.BaseIds;
import com.bat.promotion.api.promotion.PromotionServiceI;
import com.bat.promotion.api.promotion.dto.*;
import com.bat.promotion.api.promotion.dto.data.PromotionDTO;
import com.bat.promotion.api.promotion.dto.data.PromotionListDTO;
import com.bat.promotion.web.base.Response;
import com.bat.promotion.web.constants.CommonLogTypeConstantDTO;
import com.bat.promotion.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "促销活动后台接口", value = "PromotionController")
@RestController
@RequestMapping("/promotion/v1/web/admin/promotion")
public class PromotionController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(PromotionController.class);

    @Resource
    private PromotionServiceI service;

    @ApiOperation(value = "根据搜索条件查找促销活动列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<PromotionListDTO>> listPromotion(@Valid PromotionListQry qry) {
        PageInfo<PromotionListDTO> pageInfo = service.listPromotion(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Promotion, value = CommonLogTypeConstantDTO.PromotionAdd)
    @ApiOperation(value = "新增促销活动")
    @PostMapping()
    public Response createPromotion(@RequestBody @Valid PromotionCmd cmd) {
        service.createPromotion(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Promotion,
        value = CommonLogTypeConstantDTO.PromotionUpdate)
    @ApiOperation(value = "修改促销活动(草稿状态的促销活动修改)")
    @PutMapping()
    public Response updatePromotion(@RequestBody @Valid PromotionCmd cmd) {
        service.updatePromotion(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据id查询促销活动详情")
    @GetMapping()
    public Response<PromotionDTO> getPromotion(@Valid BaseId qry) {
        PromotionDTO dto = service.getPromotion(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Promotion,
        value = CommonLogTypeConstantDTO.PromotionDelete)
    @ApiOperation(value = "根据id删除促销活动")
    @DeleteMapping()
    public Response deletePromotion(@RequestBody @Valid BaseId cmd) {
        service.deletePromotion(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Promotion,
        value = CommonLogTypeConstantDTO.PromotionDeletes)
    @ApiOperation(value = "根据id删除促销活动")
    @DeleteMapping(value = "/ids")
    public Response deletePromotions(@RequestBody @Valid BaseIds cmd) {
        service.deletePromotions(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Promotion,
        value = CommonLogTypeConstantDTO.PromotionUpdateStatus)
    @ApiOperation(value = "根据促销活动id变更状态")
    @PutMapping(value = "/status")
    public Response updatePromotionStatus(@RequestBody @Valid PromotionStatusCmd cmd) {
        service.updatePromotionStatus(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.Promotion,
        value = CommonLogTypeConstantDTO.PromotionSubmits)
    @ApiOperation(value = "批量提交促销活动")
    @PutMapping(value = "/submits")
    public Response promotionSubmits(@RequestBody @Valid PromotionSubmitsCmd cmd) {
        service.promotionSubmits(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @GetMapping("/tempDownLoad")
    @ApiOperation(value = "获取促销活动导入模板URL")
    public Response<String> getTempUrl() {
        String url = service.getTempUrl();
        return Response.of(url);
    }

    @PostMapping("/import")
    @ApiOperation(value = "促销活动导入接口")
    public Response promotionImport(MultipartFile file) throws IOException {
        service.promotionImport(file.getInputStream(), getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @PostMapping("/export")
    @ApiOperation(value = "促销活动导出接口")
    public Response promotionExport(HttpServletResponse response, @RequestBody PromotionExportQry qry)
        throws IOException {
        String fileName = "活动.xls";
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        // response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        // 输出Excel文件
        OutputStream output = response.getOutputStream();
        HSSFWorkbook sheets = service.promotionExport(qry);
        sheets.write(output);
        output.flush();
        output.close();
        return Response.buildSuccess();
    }

}
