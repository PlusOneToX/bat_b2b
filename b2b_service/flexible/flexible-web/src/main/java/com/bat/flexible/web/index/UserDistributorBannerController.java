package com.bat.flexible.web.index;


import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.BannerSeriesPictureRelevanceServiceI;
import com.bat.flexible.api.index.DistributorBannerServiceI;
import com.bat.flexible.api.index.dto.page.UserBannerQry;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 分销商banner移动端控制器
 */
@RestController
@RequestMapping("/flexible/v1/web/user/distributorBanner")
@Api(tags = "分销商banner用户端接口")
public class UserDistributorBannerController extends BaseController {

    @Autowired
    private DistributorBannerServiceI distributorBannerServiceI;

    @Autowired
    private BannerSeriesPictureRelevanceServiceI bannerSeriesPictureRelevanceServiceI;


    /**
     * 查询分销商banner列表
     * @param distributorId
     * @return
     */
    @GetMapping(value = "/listByDistributorId")
    @ApiOperation(value = "查询分销商banner列表")
    public Response listByDistributorId(@RequestParam(value = "distributorId",required = false)Integer distributorId){

        return distributorBannerServiceI.listByDistributorIdAndStatus(distributorId, (short)1);
    }


    /**
     * 查询分销商banner列表
     * @param userBannerQry
     * @return
     */
    @GetMapping(value = "/pagePictureByBannerId")
    @ApiOperation(value = "查询分销商banner列表")
    public Response pagePictureByBannerId(@Valid UserBannerQry userBannerQry){
        PageInfo pageInfo = bannerSeriesPictureRelevanceServiceI.pagePictureByBannerId(userBannerQry);
        return Response.of(pageInfo);
    }
   
}
