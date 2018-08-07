package com.bat.warehouse.web.inStock;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.api.inStock.dto.WarehouseStockQry;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventorySummaryCO;
import com.bat.warehouse.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/warehouse/v1/web/user/u/warehouse/stock")
@Api(tags = "库存前台接口")
public class UserWarehouseStockController extends BaseController {

    @Autowired
    private WarehouseInStockServiceI warehouseInStockServiceI;


    @PostMapping("/listStockByCondition")
    @ApiOperation(value ="查询货品库存")
    public Response<List<GoodsItemInventorySummaryCO>> listStockByCondition(@Valid @RequestBody WarehouseStockQry warehouseStockQry){
        List<GoodsItemInventorySummaryCO> list = warehouseInStockServiceI.summaryByItemIdListAndAreaIdListAndDistributorId(warehouseStockQry.getItemIdList(),warehouseStockQry.getAreaIdList(),
                warehouseStockQry.getDistributorId(),false,null);
        return Response.of(list);
    }

    @GetMapping("/test")
    public void test(){
        warehouseInStockServiceI.testLock(1);
    }
}
