package com.bat.distributor.service.distributor;

// package by domain, not by duty

import java.util.List;

import javax.annotation.Resource;

import com.bat.distributor.dao.distributor.dataobject.DistributorTreePathDO;
import com.bat.distributor.service.distributor.executor.DistributorRpcCmdExe;
import com.bat.distributor.service.distributor.executor.DistributorRpcQryExe;
import org.apache.dubbo.config.annotation.DubboService;

import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.DistributorChangeBrandRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.DistributorErpRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.DistributorGoodsScalePriceRpcQry;
import com.bat.dubboapi.distributor.distributor.dto.data.*;

@DubboService
public class DistributorServiceRpcImpl implements DistributorServiceRpc {

    @Resource
    private DistributorRpcQryExe rpcQryExe;

    @Resource
    private DistributorRpcCmdExe rpcCmdExe;

    @Override
    public Response<List<Integer>> listDistributorIdBySalesIdsAndOneTreeNode(List<Integer> ids) {
        List<Integer> distributorIds = rpcQryExe.listDistributorIdBySalesIdsAndOneTreeNode(ids);
        return Response.of(distributorIds);
    }

    @Override
    public Response<List<DistributorNameRpcDTO>> listDistributorNameBySalesIdAndOneTreeNode(List<Integer> salesIds) {
        List<DistributorNameRpcDTO> list = rpcQryExe.listDistributorNameBySalesIdAndOneTreeNode(salesIds);
        return Response.of(list);
    }

    @Override
    public Response<List<Integer>> listDistributorIdByDepartmentIds(List<Integer> ids) {
        List<Integer> distributorIds = rpcQryExe.listDistributorIdByDepartmentIds(ids);
        return Response.of(distributorIds);
    }

    @Override
    public Response<List<DistributorNameRpcDTO>> listDistributorNameByDepartmentIdsAndOneTreeNode(List<Integer> ids) {
        List<DistributorNameRpcDTO> list = rpcQryExe.listDistributorNameByDepartmentIdsAndOneTreeNode(ids);
        return Response.of(list);
    }

    @Override
    public Response<List<Integer>> listDistributorIdByScalePriceIds(List<Integer> ids, Integer brandId) {
        List<Integer> distributorIds = rpcQryExe.listDistributorIdByScalePriceIds(ids, brandId);
        return Response.of(distributorIds);
    }

    @Override
    public Response<List<Integer>> listDistributorIdByDefaultScalePriceIds(List<Integer> ids) {
        List<Integer> distributorIds = rpcQryExe.listDistributorIdByDefaultScalePriceIds(ids);
        return Response.of(distributorIds);
    }

    @Override
    public Response<List<DistributorNameRpcDTO>>
        listDistributorNameByDefaultScalePriceIdsAndOneTreeNode(List<Integer> scalePriceIds) {
        List<DistributorNameRpcDTO> list =
            rpcQryExe.listDistributorNameByDefaultScalePriceIdsAndOneTreeNode(scalePriceIds);
        return Response.of(list);
    }

    @Override
    public Response<List<Integer>> listIdByDistributorGroupIdsAndOneTreeNode(List<Integer> distributorGroupIds) {
        List<Integer> distributorIds = rpcQryExe.listIdByDistributorGroupIdsAndOneTreeNode(distributorGroupIds);
        return Response.of(distributorIds);
    }

    @Override
    public Response<List<DistributorNameRpcDTO>>
        listNameByDistributorGroupIdsAndOneTreeNode(List<Integer> distributorGroupIds) {
        List<DistributorNameRpcDTO> list = rpcQryExe.listNameByDistributorGroupIdsAndOneTreeNode(distributorGroupIds);
        return Response.of(list);
    }

    @Override
    public Response distributorBrandRelevanceByBrandId(List<Integer> distributorIds, Integer brandId) {
        rpcCmdExe.distributorBrandRelevanceByBrandId(distributorIds, brandId);
        return Response.buildSuccess();
    }

    @Override
    public Response distributorCategoryRelevanceByCategoryId(List<Integer> distributorIds, Integer categoryId) {
        rpcCmdExe.distributorCategoryRelevanceByCategoryId(distributorIds, categoryId);
        return Response.buildSuccess();
    }

    @Override
    public Response distributorGoodsRelevanceByGoodsId(List<Integer> distributorIds, Integer goodsId) {
        rpcCmdExe.distributorGoodsRelevanceByGoodsId(distributorIds, goodsId);
        return Response.buildSuccess();
    }

    @Override
    public Response distributorPromotionRelevanceByPromotionId(List<Integer> list, Integer promotionId) {
        rpcCmdExe.distributorPromotionRelevanceByPromotionId(list, promotionId);
        return Response.buildSuccess();
    }

    @Override
    public Response distributorGroupSeckillRelevanceByGroupSeckillId(List<Integer> list, Integer groupSeckillId) {
        rpcCmdExe.distributorGroupSeckillRelevanceByGroupSeckillId(list, groupSeckillId);
        return Response.buildSuccess();
    }

    @Override
    public Response deletePromotionRelevanceByPromotionId(Integer promotionId) {
        rpcCmdExe.deletePromotionRelevanceByPromotionId(promotionId);
        return Response.buildSuccess();
    }

    @Override
    public Response deleteGroupSeckillRelevanceByGroupSeckillId(Integer groupSeckillId) {
        rpcCmdExe.deleteGroupSeckillRelevanceByGroupSeckillId(groupSeckillId);
        return Response.buildSuccess();
    }

    @Override
    public Response changeDistributorBrandRelevance(DistributorChangeBrandRpcCmd cmd) {
        rpcCmdExe.changeDistributorBrandRelevance(cmd);
        return Response.buildSuccess();
    }

    @Override
    public Response distributorBrandRelevance(List<Integer> brandIds, Integer distributorId) {
        rpcCmdExe.distributorBrandRelevance(brandIds, distributorId);
        return Response.buildSuccess();
    }

    @Override
    public Response<DistributorGoodsControlRpcDTO> distributorGoodsControl(Integer distributorId) {
        DistributorGoodsControlRpcDTO dto = rpcQryExe.distributorGoodsControl(distributorId);
        return Response.of(dto);
    }

    @Override
    public Response<DistributorBrandCategoryControlRpcDTO> distributorBrandCategoryControl(Integer distributorId) {
        DistributorBrandCategoryControlRpcDTO controlRpcDTO = rpcQryExe.distributorBrandCategoryControl(distributorId);
        return Response.of(controlRpcDTO);
    }

    @Override
    public Response<List<Integer>> getDistributorIdsByBrandId(Integer brandId) {
        List<Integer> distributorIds = rpcQryExe.getDistributorIdsByBrandId(brandId);
        return Response.of(distributorIds);
    }

    @Override
    public Response<List<Integer>> getDistributorIdsByCategoryId(Integer categoryId) {
        List<Integer> distributorIds = rpcQryExe.getDistributorIdsByCategoryId(categoryId);
        return Response.of(distributorIds);
    }

    @Override
    public Response<DistributorScalePriceControlRpcDTO> distributorScalePriceControl(Integer distributorId,
        List<Integer> itemIds) {
        DistributorScalePriceControlRpcDTO dto = rpcQryExe.distributorScalePriceControl(distributorId, itemIds);
        return Response.of(dto);
    }

    @Override
    public Response<DistributorScalePriceControlRpcDTO> distributorScalePriceControlByGoodsIds(Integer distributorId,
        List<Integer> goodsIds) {
        DistributorScalePriceControlRpcDTO dto =
            rpcQryExe.distributorScalePriceControlByGoodsIds(distributorId, goodsIds);
        return Response.of(dto);
    }

    @Override
    public Response<DistributorGoodsScalePriceControlRpcDTO>
        distributorGoodsScalePriceControl(DistributorGoodsScalePriceRpcQry qry) {
        DistributorGoodsScalePriceControlRpcDTO dto = rpcQryExe.distributorGoodsScalePriceControl(qry);
        return Response.of(dto);
    }

    @Override
    public Response<List<DistributorRpcDTO>> distributorByIds(List<Integer> ids) {
        List<DistributorRpcDTO> dtos = rpcQryExe.distributorByIds(ids);
        return Response.of(dtos);
    }

    @Override
    public Response<DistributorRpcDTO> distributorById(Integer id) {
        DistributorRpcDTO dto = rpcQryExe.distributorById(id);
        return Response.of(dto);
    }

    @Override
    public Response<Integer> distributorCountryById(Integer id) {
        Integer country = rpcQryExe.distributorCountryById(id);
        return Response.of(country);
    }

    @Override
    public Response<DistributorBusinessRpcDTO> getDistributorBusiness(Integer id) {
        DistributorBusinessRpcDTO dto = rpcQryExe.getDistributorBusiness(id);
        return Response.of(dto);
    }

    @Override
    public DistributorPromitonGroupSeckillRpcDTO getDistributorPromotionGroupSeckill(Integer id) {
        DistributorPromitonGroupSeckillRpcDTO rpcDTO = rpcQryExe.getDistributorPromotionGroupSeckill(id);
        return rpcDTO;
    }

    @Override
    public List<Integer> getDistributorIdsBySalesIds(List<Integer> salesIds) {
        return rpcQryExe.getDistributorIdsBySalesIds(salesIds);
    }

    @Override
    public List<DistributorNextNameRpcDTO> getDistributorNextByErpIds(List<Integer> erpIds) {
        return rpcQryExe.getDistributorNextByErpIds(erpIds);
    }

    @Override
    public DistributorInfoRpcDTO getDistributorInfoByErpId(Integer erpId) {
        return rpcQryExe.getDistributorInfoByErpId(erpId);
    }

    @Override
    public List<DistributorNextNameRpcDTO> getDistributorNextByDistributorIds(List<Integer> distributorIds) {
        return rpcQryExe.getDistributorNextByDistributorIds(distributorIds);
    }

    @Override
    public Response<List<DistributorNameRpcDTO>> getDistributorNameByDistributorIds(List<Integer> distributorIds) {
        List<DistributorNameRpcDTO> nameRpcDTOS = rpcQryExe.getDistributorNameByDistributorIds(distributorIds);
        return Response.of(nameRpcDTOS);
    }

    @Override
    public Response<List<DistributorNameRpcDTO>> getAllDistributorNameOneTreeNode() {
        List<DistributorNameRpcDTO> nameRpcDTOS = rpcQryExe.getAllDistributorNameOneTreeNode();
        return Response.of(nameRpcDTOS);
    }

    @Override
    public Response<DistributorExtendDataRpcDTO> getDistributorExtendData(Integer distributorId) {
        DistributorExtendDataRpcDTO extendData = rpcQryExe.getDistributorExtendData(distributorId);
        return Response.of(extendData);
    }

    @Override
    public Response<DistributorExtendDataRpcDTO> getAncestorDistributorExtendData(Integer distributorId) {
        //查询该分销商的上级分销商
        DistributorTreePathDO ancestorDistributorId = rpcQryExe.getAncestorDistributorId(distributorId);
        if (ancestorDistributorId != null) {
            //查询上级分销商的拓展信息
            return getDistributorExtendData(ancestorDistributorId.getDistributorAncestorId());
        }
        return Response.buildSuccess();
    }

    @Override
    public Response<DistributorERPRpcDTO> getDistributorERPData(Integer distributorId) {
        DistributorERPRpcDTO erpRpcDTO = rpcQryExe.getDistributorERPData(distributorId);
        return Response.of(erpRpcDTO);
    }

    @Override
    public Response updateDistributorErpData(DistributorErpRpcCmd cmd) {
        rpcCmdExe.updateDistributorErpData(cmd);
        return Response.buildSuccess();
    }

    @Override
    public Response<List<DistributorGroupRpcDTO>>
        getDistributorGroupByDistributorGroupIds(List<Integer> distributorGroupIds) {
        List<DistributorGroupRpcDTO> groupRpcDTOS =
            rpcQryExe.getDistributorGroupByDistributorGroupIds(distributorGroupIds);
        return Response.of(groupRpcDTOS);
    }

    @Override
    public Response<List<DistributorTreePathRpcDTO>> getDistributorTreePaths(Integer distributorId) {
        List<DistributorTreePathRpcDTO> distributorTreePath = rpcQryExe.getDistributorTreePaths(distributorId);
        return Response.of(distributorTreePath);
    }

    @Override
    public Response<UpperDistributorRpcDTO> getUpperDistributorId(Integer distributorId) {
        UpperDistributorRpcDTO upperDistributorRpcDTO = rpcQryExe.getUpperDistributorId(distributorId);
        return Response.of(upperDistributorRpcDTO);
    }

    /**
     * <h2>根据分销商id获取分销商的的支付方式</h2>
     * @param distributorId
     * @return
     */
    @Override
    public Response<DistributorPayWayRpcDTO> distributorPaymentWayById(Integer distributorId) {
        DistributorPayWayRpcDTO dto =rpcQryExe.distributorPaymentWayById(distributorId);
        return Response.of(dto);
    }
}