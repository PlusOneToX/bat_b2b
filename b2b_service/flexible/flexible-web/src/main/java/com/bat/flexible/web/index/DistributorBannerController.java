package com.bat.flexible.web.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.DistributorBannerServiceI;
import com.bat.flexible.api.index.dto.banner.DistributorBannerDTO;
import com.bat.flexible.api.index.dto.page.DistributorBannerPageQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.index.co.DistributorBannerPageCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * 分销商banner控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/admin/u/p/distributorBanner")
@Api(tags = "分销商banner后台管理接口")
public class DistributorBannerController extends BaseController {

    @Autowired
    private DistributorBannerServiceI distributorBannerServiceI;



    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<DistributorBannerPageCO>> page(@Valid DistributorBannerPageQry distributorBannerPageQry){
        PageInfo<DistributorBannerPageCO> pageInfo = distributorBannerServiceI.page(distributorBannerPageQry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorBanner, value = CommonLogTypeConstantDTO.DistributorBannerAdd)
    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody DistributorBannerDTO distributorBannerDTO){

        distributorBannerDTO.setId(null);
        return distributorBannerServiceI.create(distributorBannerDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorBanner, value = CommonLogTypeConstantDTO.DistributorBannerUpdate)
    @PutMapping
    @ApiOperation(value = "修改")
    public Response update(@Valid @RequestBody DistributorBannerDTO distributorBannerDTO){

        return distributorBannerServiceI.update(distributorBannerDTO,getCurrentAdmin());
    }

    /**
     * 查询详情
     * @return
     */
    @GetMapping(value = "/detail")
    @ApiOperation(value = "查询详情")
    public Response detail(@Valid FlexibleIdDTO flexibleIdDTO){

        return distributorBannerServiceI.detail(flexibleIdDTO.getId());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorBanner, value = CommonLogTypeConstantDTO.DistributorBannerDelete)
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value = "删除")
    public Response delete(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){

        List<Integer> list = new ArrayList<>();
        list.add(flexibleIdDTO.getId());
        return distributorBannerServiceI.delete(list);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorBanner, value = CommonLogTypeConstantDTO.DistributorBannerBatchDelete)
    @DeleteMapping(value = "/batchDelete")
    @ApiOperation(value = "批量删除")
    public Response batchDelete(@Valid @RequestBody FlexibleIdListDTO flexibleIdListDTO){
        return distributorBannerServiceI.delete(flexibleIdListDTO.getIdList());
    }

   
}
