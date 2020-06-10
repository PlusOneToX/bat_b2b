package com.bat.flexible.web.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.DistributorIndexRecommendServiceI;
import com.bat.flexible.api.index.dto.page.IndexRecommendPageQry;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.dao.picture.co.CommonPicturePageCO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 首页推荐控制器（移动端）
 */
@RestController
@RequestMapping(value="/flexible/v1/web/user/distributorIndexRecommend")
@Api(tags = "用户端首页推荐接口")
public class UserIndexRecommendController extends BaseController {

    @Autowired
    private DistributorIndexRecommendServiceI distributorIndexRecommendServiceI;


    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询")
    public Response<PageInfo<CommonPicturePageCO>> page(@Valid IndexRecommendPageQry pageRequest){
        PageInfo<CommonPicturePageCO> pageInfo=  distributorIndexRecommendServiceI.pageByDistributorIdNew(pageRequest);
        return Response.of(pageInfo);
    }

}
