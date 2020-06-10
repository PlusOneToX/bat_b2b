package com.bat.flexible.web.material;

import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.exchange.ExchangeMaterialRelevanceServiceI;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.material.dto.DistributorPriceQry;
import com.bat.flexible.api.material.dto.GoodsItemRpcQry;
import com.bat.flexible.api.material.dto.MaterialItemQry;
import com.bat.flexible.api.material.dto.MaterialTreeQry;
import com.bat.flexible.dao.exchange.dataobject.ExchangeMaterialRelevanceDO;
import com.bat.flexible.dao.material.co.MaterialTreeCO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/flexible/v1/web/user/u/material")
@Api(tags = "材质前台接口")
public class UserMaterialController extends BaseController {

    @Autowired
    private MaterialServiceI materialServiceI;


    @Autowired
    private ExchangeMaterialRelevanceServiceI exchangeMaterialRelevanceServiceI;

    @GetMapping(value = "/tree")
    @ApiOperation(value = "材质树查询")
    public Response<List<MaterialTreeCO>> tree(MaterialTreeQry materialTreeQry){
        List<MaterialTreeCO> treeCOList = materialServiceI.tree(materialTreeQry);
        //针对结果进行处理
        materialServiceI.swapTree(treeCOList,materialTreeQry.getDistributorId());
        return Response.of(treeCOList);
    }

    @GetMapping(value = "/treeNew")
    @ApiOperation(value = "材质树查询(新)")
    public Response<List<MaterialTreeCO>> treeNew(MaterialTreeQry materialTreeQry){
        List<MaterialTreeCO> treeCOList = materialServiceI.treeNew(materialTreeQry);
        return Response.of(treeCOList);
    }

    @GetMapping(value = "/getItemMsgForMaterial")
    @ApiOperation(value = "获取材质关联的货品信息")
    public Response<GoodsItemRpcQry> getItemMsgForMaterial(MaterialItemQry materialItemQry){
        GoodsItemRpcQry goodsItemRpcQry =materialServiceI.getItemMsgForMaterial(materialItemQry);
        return Response.of(goodsItemRpcQry);
    }

    @GetMapping(value = "/price")
    @ApiOperation(value = "计算价格")
    public Response price(@Valid DistributorPriceQry distributorPriceQry){
        return materialServiceI.getPriceByCondition(distributorPriceQry.getDistributorId(),distributorPriceQry.getMaterialId(),distributorPriceQry.getOrderSource());
    }

    @GetMapping
    @ApiOperation(value = "根据材质id查询材质对象")
    public Response<MaterialDO> getById(@Valid FlexibleIdDTO flexibleIdDTO){
        MaterialDO materialDO = materialServiceI.getById(flexibleIdDTO.getId());
        return Response.of(materialDO);
    }

    /**
     * 根据兑换卡找出支持的材质
     * @return
     */
    @GetMapping(value = "/listMaterialIdListByExchangeId")
    @ApiOperation(value = "根据兑换卡活动id查询材质id列表")
    public Response listMaterialIdListByExchangeId(@Valid FlexibleIdDTO flexibleIdDTO) {
        List<ExchangeMaterialRelevanceDO> relevanceDOList = exchangeMaterialRelevanceServiceI.listByExchangeId(flexibleIdDTO.getId());
        List<Integer> materialIdList = new ArrayList<>();
        relevanceDOList.stream().forEach(exchangeMaterialRelevanceDO -> {
            materialIdList.add(exchangeMaterialRelevanceDO.getMaterialId());
        });
        return Response.of(materialIdList);
    }

    @GetMapping(value = "/test")
    @ApiOperation(value = "测试队列")
    public void test(){
        materialServiceI.test();
    }
}
