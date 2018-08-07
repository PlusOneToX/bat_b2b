package com.bat.system.web.logistics;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.system.api.base.SystemException;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.system.api.logistics.LogisticsService;
import com.bat.system.api.logistics.dto.LogisticsCalculationQry;
import com.bat.system.api.logistics.dto.LogisticsId;
import com.bat.system.api.logistics.dto.LogisticsQry;
import com.bat.system.api.logistics.dto.data.LogisticsDTO;
import com.bat.system.api.logistics.dto.data.LogisticsQryDTO;
import com.bat.system.web.base.BaseController;
import com.bat.system.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/9 19:10
 */
@Api(tags = "系统模块-配送前台接口", value = "UserLogisticsController")
@Slf4j
@RestController
@RequestMapping("/system/v1/web/user/logistics")
public class UserLogisticsController extends BaseController {

    @Resource
    private LogisticsService logisticsService;

    @GetMapping("/list")
    @ApiOperation(value = "查询配送列表(分页)")
    public Response<PageInfo<LogisticsDTO>> listLogistics(@Valid LogisticsQry qry) {
        PageInfo pageInfo = logisticsService.listLogistics(qry);
        return Response.of(pageInfo);
    }

    @PostMapping("/calculations")
    @ApiOperation(value = "根据地区和购买商品获取配送方式和计算配送费用(疑是主要逻辑)(/user/u/distribution/calculations)")
    public Response<List<LogisticsQryDTO>> listLogisticsCalculation(@RequestBody @Valid LogisticsCalculationQry qry) {
        return Response.of(logisticsService.listLogisticsCalculation(qry));
    }

    @PostMapping("/calculationss")
    @ApiOperation(value = "根据地区和购买商品获取配送方式和计算配送费用 列表")
    public Response<List<LogisticsQryDTO>>
        listLogisticsCalculations(@RequestBody @Valid List<LogisticsCalculationQry> qrys) {
        for (LogisticsCalculationQry qry : qrys) {
            if (qry.getCountryId() == null) {
                throw SystemException.buildException("P_LOGISTICS_COUNTRY_ID_NULL_ERROR");
            }
        }
        return Response.of(logisticsService.listLogisticsCalculations(qrys));
    }

    // @GetMapping("/manufactors")
    // @ApiOperation(value = "按工厂查询配送方式")
    // public Response<List<LogisticsDTO>> listLogisticsByManufactor(@Valid LogisticsQry qry) {
    // qry.setPage(1);
    // qry.setSize(10000);
    // qry.setEnable((short)1);
    // return Response.of(logisticsService.listLogistics(qry).getList());
    // }

    @GetMapping()
    @ApiOperation(value = "通过ID查询单个配送")
    public Response<LogisticsDTO> getLogistics(@Valid LogisticsId id) {
        return Response.of(logisticsService.getLogisticsById(id.getId()));
    }

}
