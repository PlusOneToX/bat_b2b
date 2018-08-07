package com.bat.distributor.web.distributor;

import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.distributor.DistributorCustomPriceServiceI;
import com.bat.distributor.api.distributor.dto.DistributorCustomPriceCmd;
import com.bat.distributor.api.distributor.dto.DistributorId;
import com.bat.distributor.api.distributor.dto.data.DistributorCustomPriceDTO;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.base.BaseController;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "分销商定制价格接口")
@RequestMapping(value = "/distributor/v1/web")
public class DistributorCustomPriceController extends BaseController {

    @Autowired
    private DistributorCustomPriceServiceI distributorCustomPriceServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.DistributorCustomPrice, value = CommonLogTypeConstantDTO.DistributorCustomPriceSave)
    @PostMapping(value = {"/admin/u/p/distributorCustomPrice","/admin/u/po/distributorCustomPrice","/user/distributorCustomPrice"})
    @ApiOperation(value = "保存分销商定制价格、B2B前后台")
    public Response save(@Valid @RequestBody DistributorCustomPriceCmd distributorCustomPriceCmd){
        System.out.println("进来了");
        return distributorCustomPriceServiceI.saveList(distributorCustomPriceCmd,getUserId(),getUserName());
    }

    @GetMapping(value = {"/admin/u/p/distributorCustomPrice","/admin/u/po/distributorCustomPrice","/user/distributorCustomPrice"})
    @ApiOperation(value = "根据分销商id查询列表")
    public Response<List<DistributorCustomPriceDTO>> listDTOByDistributorId(@Valid DistributorId distributorId){
        return distributorCustomPriceServiceI.listDTOByDistributorId(distributorId.getId());
    }


  /*  @GetMapping(value = {"/admin/u/p/distributorCustomPrice/page","/admin/u/po/distributorCustomPrice/page","/user/distributorCustomPrice/page"})
    @ApiOperation(value = "分页查询分销商定制价格列表")
    public Response page(@Valid DictributorPricePageQry dictributorPricePageQry){
        return distributorCustomPriceServiceI.pageByDistributorId(dictributorPricePageQry.getDistributorId(),dictributorPricePageQry.getPage(),
                dictributorPricePageQry.getSize());
    }*/

}
