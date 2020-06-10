package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.dto.*;
import com.bat.flexible.api.exchange.dto.order.ExchangeAdminUnboundCmd;
import com.bat.flexible.dao.exchange.co.ExchangeCodePageCO;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 兑换码控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/admin/u/p/exchangeCode")
@Api(tags = "兑换码后端后台接口")
public class ExchangeCodeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeCodeController.class);

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;

    /**
     * 分页查询
     * @return
     */
    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<List<ExchangeCodePageCO>>> page(ExchangeCodePageQry exchangeCodePageQry){
        PageInfo<List<ExchangeCodePageCO>> pageInfo = exchangeCodeServiceI.page(exchangeCodePageQry);
        return Response.of(pageInfo);
    }

    /**
     * 作废
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCode, value = CommonLogTypeConstantDTO.ExchangeCodeInvalid)
    @DeleteMapping(value = "/invalid")
    @ApiOperation(value = "作废")
    public Response invalid(@Valid @RequestBody ExchangeCodeStatusRequest exchangeCodeStatusRequest, BindingResult result){

        List<ExchangeCodeStatusRequest> requestList = new ArrayList<>();
        requestList.add(exchangeCodeStatusRequest);
        return exchangeCodeServiceI.invalid(requestList, getCurrentAdmin());
    }

    /**
     * 批量作废
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCode, value = CommonLogTypeConstantDTO.ExchangeCodeInvalidBatch)
    @DeleteMapping(value = "/invalidBatch")
    @ApiOperation(value = "批量作废")
    public Response invalid(@Valid @RequestBody ExchangeCodeStatusBatchRequest batchRequest, BindingResult result){

        List<ExchangeCodeStatusRequest> requestList = new ArrayList<>();
        batchRequest.getIdList().stream().forEach(id -> {
            ExchangeCodeStatusRequest request = new ExchangeCodeStatusRequest();
            request.setId(id);
            request.setReason(batchRequest.getReason());
            requestList.add(request);
        });
        return exchangeCodeServiceI.invalid(requestList,getCurrentAdmin());
    }

    /**
     * 生成兑换码数量
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCode, value = CommonLogTypeConstantDTO.ExchangeCodeCreateCode)
    @PutMapping("/createCode")
    @ApiOperation(value = "生成兑换码")
    public Response createCode(@RequestBody @Valid ExchangeFactoryRequest exchangeFactoryRequest, BindingResult result){

        return exchangeCodeServiceI.createCode(exchangeFactoryRequest,getCurrentAdmin());
    }

    /**
     * 兑换码excel模板导出
     * @param request
     * @param response
     * @param downLoadRequest
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCode, value = CommonLogTypeConstantDTO.ExchangeCodeTempDownLoad)
    @PostMapping(value = "/tempDownLoad")
    @ApiOperation(value = "兑换码模板导出")
    public void OssAction(HttpServletRequest request, HttpServletResponse response, @RequestBody DownLoadRequest downLoadRequest) {

        InputStream inputStream = exchangeCodeServiceI.tempDownLoad(downLoadRequest);
        if(inputStream !=null){
           exportExcelByInputStream(request,response,inputStream,"兑换码导入模板.xls");
        }
    }

    /**
     * 兑换码excel导入
     * @param request
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCode, value = CommonLogTypeConstantDTO.ExchangeCodeImport)
    @PostMapping(value = "/import")
    @ApiOperation(value = "兑换码excel导入")
    public Response importCode( HttpServletRequest request, @Valid @RequestBody ExchangeCodeImportDTO exchangeCodeImportDTO)  {
        try {
            MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
            Iterator<String> itr = multipart.getFileNames();
            MultipartFile file = null;
            if (itr.hasNext()) {
                String str = itr.next();    //这个文件并不是原来的文件名
                file = multipart.getFile(str);
            }
            InputStream inputStream = file.getInputStream();
            return exchangeCodeServiceI.importCode(inputStream, getCurrentAdmin(),exchangeCodeImportDTO.getIsEntity());
        } catch (Exception e) {
            return Response.buildFailure("100", e.getMessage());
        }
    }

    /**
     * 导出
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCode, value = CommonLogTypeConstantDTO.ExchangeCodeExport)
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出")
    public Response export(HttpServletRequest request, HttpServletResponse response,@RequestBody ExchangeCodePageQry exchangeCodePageQry) throws IOException {

        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = exchangeCodeServiceI.createExcelByCondition(exchangeCodePageQry, getCurrentAdmin(), getLanguage());
        if (wb == null) {
            return Response.buildFailure("11", "excel导出失败");
        }
        String fileName = "兑换码清单.xls";
        final String userAgent = request.getHeader("USER-AGENT");
        if (StringUtils.contains(userAgent, "MSIE")) {//IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");//其他浏览器
        }
        response.setContentType("application/octet-stream;charset=utf-8");
//        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        //输出Excel文件
        OutputStream output = response.getOutputStream();
        wb.write(output);
        output.close();
        return null;
    }

    /**
     * 分页查询(解密)
     * @return
     */
    @GetMapping(value = "/pageEncode")
    @ApiOperation(value = "分页查询、解密")
    public Response<PageInfo<List<ExchangeCodePageCO>>> pageEncode(ExchangeCodePageQry exchangeCodeRequest){
        //解密
        exchangeCodeRequest.setEncodeFlag(true);
        PageInfo<List<ExchangeCodePageCO>> pageInfo = exchangeCodeServiceI.page(exchangeCodeRequest);
        return Response.of(pageInfo);
    }


    /**
     * 后台进行兑换码解绑
     * @return
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminExchangeUnbound, value = CommonLogTypeConstantDTO.AdminExchangeUnbound)
    @DeleteMapping(value = "/unboundExchange")
    @ApiOperation(value = "后台兑换码解绑")
    public Response unboundExchange(@Valid @RequestBody ExchangeAdminUnboundCmd exchangeAdminUnboundCmd) {
        LOGGER.info("后台兑换码解绑");
        LOGGER.info(String.valueOf(exchangeAdminUnboundCmd));
        return exchangeCodeServiceI.unbound(exchangeAdminUnboundCmd);
    }


}
