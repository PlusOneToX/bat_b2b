package com.bat.goods.service.user.executor;

import java.util.List;
import java.util.stream.Collectors;

import com.bat.goods.service.common.error.dubbo.WarehouseDubboErrorCode;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorBusinessRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGoodsControlRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorPromitonGroupSeckillRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorScalePriceControlRpcDTO;
import com.bat.dubboapi.promotion.api.PromotionServiceDistributorRpc;
import com.bat.dubboapi.promotion.dto.data.PromotionGroupSeckillGoodsRpcDTO;
import com.bat.dubboapi.promotion.dto.data.PromotionGroupSeckillIdsByAllDistributorDTO;
import com.bat.dubboapi.system.storesetting.api.SystemGlobeShopSettingServiceRpc;
import com.bat.dubboapi.warehouse.warehouse.api.WarehouseServiceRpc;
import com.bat.dubboapi.warehouse.warehouse.dto.WarehouseDTORpcQry;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.utils.MessageUtils;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2018/4/29 11:27
 */
@Component
public class UserRpcQryExe {

    @DubboReference(check = false, timeout = 5000)
    private DistributorServiceRpc distributorServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private SystemGlobeShopSettingServiceRpc shopSettingServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private PromotionServiceDistributorRpc promotionServiceRpc;

    @DubboReference(check = false, timeout = 7000)
    private WarehouseServiceRpc warehouseServiceRpc;

    /**
     * 根据分销获取商品可视范围（非全部可视部分）
     * 
     * @param distributorId
     * @return
     */
    public DistributorGoodsControlRpcDTO getDistributorGoodsRange(Integer distributorId) {
        Response<DistributorGoodsControlRpcDTO> response = distributorServiceRpc.distributorGoodsControl(distributorId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_GOODS_RANGE_ERROR);
        }
    }

    public DistributorScalePriceControlRpcDTO getDistributorScalePrice(Integer distributorId, List<Integer> itemIds) {
        Response<DistributorScalePriceControlRpcDTO> response =
            distributorServiceRpc.distributorScalePriceControl(distributorId, itemIds);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_SCALE_PRICE_ERROR);
        }
    }

    public DistributorScalePriceControlRpcDTO getDistributorScalePriceByGoodsIds(Integer distributorId,
        List<Integer> goodsIds) {
        Response<DistributorScalePriceControlRpcDTO> response =
            distributorServiceRpc.distributorScalePriceControlByGoodsIds(distributorId, goodsIds);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_SCALE_PRICE_ERROR);
        }
    }

    /**
     * 上架多少天内为新品时间
     *
     * @return
     */
    public Integer getNewproductTime() {
        com.bat.dubboapi.system.common.Response<Integer> newproductTime = shopSettingServiceRpc.getNewproductTime();
        if (newproductTime.isSuccess()) {
            return newproductTime.getData();
        } else {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_NEW_PRODUCT_TIME_ERROR);
        }
    }

    public PromotionGroupSeckillGoodsRpcDTO getPromotionGroupSeckillGoods(List<Integer> promotionIds,
        List<Integer> groupSeckillIds) {
        return promotionServiceRpc.getPromotionGroupSeckillGoods(promotionIds, groupSeckillIds);
    }

    /**
     * 根据分销商id获取活动id列表
     *
     * @param id
     * @return
     */
    public DistributorPromitonGroupSeckillRpcDTO getDistributorPromotionGroupSeckill(Integer id) {
        return distributorServiceRpc.getDistributorPromotionGroupSeckill(id);
    }

    /**
     * 获取所有分销商可视活动ids
     * 
     * @return
     */
    public PromotionGroupSeckillIdsByAllDistributorDTO promotionGroupSeckillIdsByAllDistributor() {
        com.bat.dubboapi.promotion.common.Response<PromotionGroupSeckillIdsByAllDistributorDTO> response =
            promotionServiceRpc.promotionGroupSeckillIdsByAllDistributor();
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 根据分销商id获取业务数据
     * 
     * @param distributorId
     * @return
     */
    public DistributorBusinessRpcDTO getDistributorBusinessData(Integer distributorId) {
        Response<DistributorBusinessRpcDTO> distributorBusinessRpcDTOResponse =
            distributorServiceRpc.getDistributorBusiness(distributorId);
        if (!distributorBusinessRpcDTOResponse.isSuccess()) {
            throw GoodsException.buildException(distributorBusinessRpcDTOResponse.getErrCode(),
                distributorBusinessRpcDTOResponse.getErrMessage());
        }
        return distributorBusinessRpcDTOResponse.getData();
    }

    /**
     * 根据销售区域Id列表查询仓库id列表
     * 
     * @param salesAreaIds
     * @return
     */
    public List<Integer> listWarehouseIdListByAreaIdList(List<Integer> salesAreaIds) {
        com.bat.dubboapi.warehouse.common.Response<List<WarehouseDTORpcQry>> response =
            warehouseServiceRpc.listByAreaIdList(salesAreaIds);
        if (!response.isSuccess()) {
            throw GoodsException.buildException(response.getErrCode(), response.getErrMessage());
        }
        List<WarehouseDTORpcQry> qryList = response.getData();
        if (qryList == null || qryList.size() == 0) {
            throw GoodsException.buildException(WarehouseDubboErrorCode.DISTRIBUTOR_RELEVANCE_USABLE_WAREHOUSE_NULL,
                MessageUtils.get(WarehouseDubboErrorCode.DISTRIBUTOR_RELEVANCE_USABLE_WAREHOUSE_NULL));
        }
        return qryList.stream().map(WarehouseDTORpcQry::getId).collect(Collectors.toList());
    }

    // 查询分销商的业务信息
    public DistributorBusinessRpcDTO distributorBusiness(String userId) {
        Response<DistributorBusinessRpcDTO> response =
            distributorServiceRpc.getDistributorBusiness(Integer.parseInt(userId));
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw GoodsException.buildException(ErrorCode.B_GOODS_USER_SCALE_PRICE_ERROR);
        }
    }
}
