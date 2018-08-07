package com.bat.distributor.web.electricity;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.electricity.DistributorElectricityRelationMappingServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/distributor/v1/web/user/distributorElectricityRelationMapping")
@Api(tags = "分销商电商映射关系控制器")
public class DistributorElectricityRelationMappingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributorElectricityRelationMappingController.class);

    @Autowired
    private DistributorElectricityRelationMappingServiceI distributorElectricityRelationMappingServiceI;

    @GetMapping("/taobao/getConfig")
    @ApiOperation(value = "获取淘宝配置")
    public Response getConfig(@ApiParam(value = "电商平台名称") @RequestParam(value = "sellerNick", required = false) String sellerNick, @ApiParam(value = "电商平台名称2")  @RequestParam("distributorId") String distributorId) {
        LOGGER.info("sellerNick:{},distributorId:{}",sellerNick,distributorId);
        if (sellerNick == null){
            //审核前 备用参数
            if(distributorId==null){
                sellerNick = "旗舰店";
            }else{
                sellerNick = distributorId;
            }
        }
        if(sellerNick.startsWith("交易开放测试账号")){
            sellerNick = "旗舰店";
        }
        return distributorElectricityRelationMappingServiceI.getBySellerNick(sellerNick);
    }

}
