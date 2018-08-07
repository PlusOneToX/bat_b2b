package com.bat.warehouse.web.inStock;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.bat.warehouse.api.base.Response;
import com.bat.warehouse.web.annotation.SysLog;
import com.bat.warehouse.web.constants.CommonLogTypeConstantDTO;
import com.bat.warehouse.web.constants.CommonLogTypeTitleConstantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.warehouse.Tenant.TenantContext;
import com.bat.warehouse.api.inStock.WarehouseInStockServiceI;
import com.bat.warehouse.api.inStock.dto.GoodsItemErpIdDTO;
import com.bat.warehouse.api.inStock.dto.WarehouseInStockPageQry;
import com.bat.warehouse.api.inStock.dto.WarehouseStockQry;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventoryCO;
import com.bat.warehouse.dao.inStock.co.GoodsItemInventorySummaryCO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/warehouse/v1/web/admin/u")
@Api(tags = "货品库存接口", value = "AdminWarehouseInStockController")
public class AdminWarehouseInStockController {

    @Autowired
    private WarehouseInStockServiceI warehouseInStockServiceI;

    @GetMapping(value = {"/p/warehouse/stock/page", "/po/warehouse/stock/page"})
    @ApiOperation(value = "货品库存分页查询")
    public Response<PageInfo<GoodsItemInventoryCO>> page(@Valid WarehouseInStockPageQry warehouseInStockPageQry) {
        PageInfo<GoodsItemInventoryCO> pageInfo = warehouseInStockServiceI.page(warehouseInStockPageQry);
        return Response.of(pageInfo);
    }

    @GetMapping("/p/warehouse/stock/import")
    @ApiOperation(value = "导入")
    public Response importSql(@ApiParam(value = "url") @RequestParam(value = "url") String url) {
        return warehouseInStockServiceI.importSql(url);
    }

    @PostMapping(value = {"/p/warehouse/stock/listStockByCondition", "/po/warehouse/stock/listStockByCondition"})
    @ApiOperation(value = "后台根据货品id列表查询货品库存")
    public Response<List<GoodsItemInventorySummaryCO>>
        listStockByCondition(@Valid @RequestBody WarehouseStockQry warehouseStockQry) {
        List<GoodsItemInventorySummaryCO> list =
            warehouseInStockServiceI.listByAdmin(warehouseStockQry.getItemIdList(), warehouseStockQry.getWarehouseId());
        return Response.of(list);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouseInStock,
        value = CommonLogTypeConstantDTO.AdminWarehouseInStockSyncStock)
    @PutMapping(value = "/p/warehouse/stock/syncStock")
    @ApiOperation(value = "同步ERP库存")
    public Response syncStock() {
        return warehouseInStockServiceI.syncStock(TenantContext.getTenantNo());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminWarehouseInStock,
        value = CommonLogTypeConstantDTO.AdminWarehouseInStockInitStock)
    @GetMapping(value = "/p/warehouse/stock/initStock")
    @ApiOperation(value = "初始化库存（VMI和在途没有数据、初始化记录、删除已删除的货品库存数据包括缓存数据）")
    public Response initStock() {
        warehouseInStockServiceI.initStock(TenantContext.getTenantNo());
        return Response.buildSuccess();
    }

    @PutMapping(value = "/p/warehouse/stock/syncStockByItemErpId")
    @ApiOperation(value = "同步单个库存")
    public Response syncStockByItemErpId(@RequestBody @Valid GoodsItemErpIdDTO goodsItemErpIdDTO) {
        List<Long> itemErpIdList = new ArrayList<>();
        itemErpIdList.add(goodsItemErpIdDTO.getItemErpId());
        return warehouseInStockServiceI.syncStock(itemErpIdList);
    }

    @GetMapping(value = "/resetLockStock")
    @ApiOperation(value = "根据货品id重置锁库数量")
    public Response resetLockStock(
        @ApiParam(value = "货品id", required = false) @RequestParam(value = "itemId", required = false) Integer itemId) {
        warehouseInStockServiceI.resetLockStock(itemId);
        return Response.buildSuccess();
    }

    @GetMapping(value = "/initResetLockStock")
    @ApiOperation(value = "初始化重置锁库数量")
    public Response initResetLockStock() {
        warehouseInStockServiceI.initResetLockStock();
        return Response.buildSuccess();
    }

    @GetMapping(value = "/initItemUnderStockFlag")
    @ApiOperation(value = "初始化设置B2B货品是否缺货")
    public Response initItemUnderStockFlag() {
        warehouseInStockServiceI.initItemUnderStockFlag();
        return Response.buildSuccess();
    }
}
