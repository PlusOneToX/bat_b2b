package com.bat.flexible.web.exchange;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.api.exchange.ExchangeCodeServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeCodeOrderQry;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 兑换码兑换订单控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/admin/u/p/exchangeCodeOrder")
@Api(value = "兑换码兑换订单后台接口")
public class ExchangeCodeOrderController extends BaseController {

    @Autowired
    private ExchangeCodeServiceI exchangeCodeServiceI;



   
    /**
     * 分页查询
     * @return
     */
    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response page(ExchangeCodeOrderQry exchangeCodeOrderQry){

        PageInfo pageInfo = exchangeCodeServiceI.pageOrder(exchangeCodeOrderQry);
        return Response.of(pageInfo);
    }

   
   

    /**
     * 导出
     */
    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.ExchangeCodeOrder, value = CommonLogTypeConstantDTO.ExchangeCodeOrderExport)
    @PostMapping(value = "/export")
    @ApiOperation(value = "excel导出")
    public Response export(HttpServletRequest request, HttpServletResponse response,@RequestBody ExchangeCodeOrderQry exchangeCodeOrderQry) throws IOException {

        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = exchangeCodeServiceI.createExchangeExcelByCondition(exchangeCodeOrderQry, getCurrentAdmin(),  getLanguage());

        String fileName = "兑换码兑换清单.xls";
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

}
