package com.bat.flexible.web.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.DistributorIndexRecommendRelevanceServiceI;
import com.bat.flexible.api.index.DistributorIndexRecommendServiceI;
import com.bat.flexible.api.index.dto.page.DistributorIndexRecommendPageQry;
import com.bat.flexible.api.index.dto.recommend.DistributorPictureIdListDTO;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.index.co.IndexRecommendPageCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 首页推荐控制器
 */
@RestController
@RequestMapping(value="/flexible/v1/web/admin/u/p/distributorIndexRecommend")
@Api(tags = "首页推荐管理接口")
public class DistributorIndexRecommendController extends BaseController {

    @Autowired
    private DistributorIndexRecommendServiceI distributorIndexRecommendServiceI;

    @Autowired
    private DistributorIndexRecommendRelevanceServiceI distributorIndexRecommendRelevanceServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorIndexRecommend, value = CommonLogTypeConstantDTO.DistributorIndexRecommendAdd)
    @PostMapping
    @ApiOperation("新增")
    public Response create(@Valid @RequestBody DistributorPictureIdListDTO distributorPictureIdListDTO){

        distributorPictureIdListDTO.setId(null);
        return distributorIndexRecommendServiceI.add(distributorPictureIdListDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorIndexRecommend, value = CommonLogTypeConstantDTO.DistributorIndexRecommendUpdate)
    @PutMapping
    @ApiOperation(value = "修改")
    public Response update(@Valid @RequestBody DistributorPictureIdListDTO distributorPictureIdListDTO){

        return distributorIndexRecommendServiceI.update(distributorPictureIdListDTO,getCurrentAdmin());
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页")
    public Response<PageInfo<IndexRecommendPageCO>> page(@Valid DistributorIndexRecommendPageQry indexRecommendPageQry){
        PageInfo<IndexRecommendPageCO> pageInfo = distributorIndexRecommendRelevanceServiceI.page(indexRecommendPageQry);
        return Response.of(pageInfo);
    }
    @GetMapping(value = "/detail")
    @ApiOperation(value = "查询详情")
    public Response datail(@Valid FlexibleIdDTO flexibleIdDTO){

        return distributorIndexRecommendServiceI.detail(flexibleIdDTO.getId());
    }
}
