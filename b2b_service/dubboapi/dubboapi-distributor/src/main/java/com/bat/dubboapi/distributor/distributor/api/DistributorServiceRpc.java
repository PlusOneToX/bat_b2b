package com.bat.dubboapi.distributor.distributor.api;

import java.util.List;

import com.bat.dubboapi.distributor.distributor.dto.DistributorChangeBrandRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.DistributorErpRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.DistributorGoodsScalePriceRpcQry;
import com.bat.dubboapi.distributor.distributor.dto.data.*;
import com.bat.dubboapi.distributor.common.Response;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/4/27 20:38
 */
public interface DistributorServiceRpc {
    /**
     * 根据业务员ids查询一级分销商ids列表
     *
     * @param salesIds
     * @return
     */
    Response<List<Integer>> listDistributorIdBySalesIdsAndOneTreeNode(List<Integer> salesIds);

    /**
     * 根据业务员ids查询分销商名称列表
     *
     * @param salesIds
     * @return
     */
    Response<List<DistributorNameRpcDTO>> listDistributorNameBySalesIdAndOneTreeNode(List<Integer> salesIds);

    /**
     * 根据部门ids查询分销商ids列表
     *
     * @param ids
     * @return
     */
    Response<List<Integer>> listDistributorIdByDepartmentIds(List<Integer> ids);

    /**
     * 根据部门ids查询分销商名称列表
     *
     * @param ids
     * @return
     */
    Response<List<DistributorNameRpcDTO>> listDistributorNameByDepartmentIdsAndOneTreeNode(List<Integer> ids);

    /**
     * 根据分销商价格等级ids查询分销商ids列表
     *
     * @param scalePriceIds
     * @param brandId
     * @return
     */
    Response<List<Integer>> listDistributorIdByScalePriceIds(List<Integer> scalePriceIds, Integer brandId);

    /**
     * 根据分销商价格等级ids查询分销商ids列表
     *
     * @param scalePriceIds
     * @return
     */
    Response<List<Integer>> listDistributorIdByDefaultScalePriceIds(List<Integer> scalePriceIds);

    /**
     * 根据分销商价格等级ids查询分销商名称列表
     *
     * @param scalePriceIds
     * @return
     */
    Response<List<DistributorNameRpcDTO>>
        listDistributorNameByDefaultScalePriceIdsAndOneTreeNode(List<Integer> scalePriceIds);

    /**
     * 根据分销商分组id查询分销商ids列表
     *
     * @param distributorGroupIds
     * @return
     */
    Response<List<Integer>> listIdByDistributorGroupIdsAndOneTreeNode(List<Integer> distributorGroupIds);

    /**
     * 根据分销商分组id查询分销商名称列表
     *
     * @param distributorGroupIds
     * @return
     */
    Response<List<DistributorNameRpcDTO>>
        listNameByDistributorGroupIdsAndOneTreeNode(List<Integer> distributorGroupIds);

    /**
     * 根据品牌可视范围调整分销商可视品牌关系
     *
     * @param distributorIds
     * @param brandId
     * @return
     */
    Response distributorBrandRelevanceByBrandId(List<Integer> distributorIds, Integer brandId);

    /**
     * 根据品类可视范围调整分销商可视品类关系
     *
     * @param distributorIds
     * @param categoryId
     * @return
     */
    Response distributorCategoryRelevanceByCategoryId(List<Integer> distributorIds, Integer categoryId);

    /**
     * 根据商品可视范围调整分销商可视商品关系
     *
     * @param distributorIds
     * @param goodsId
     * @return
     */
    Response distributorGoodsRelevanceByGoodsId(List<Integer> distributorIds, Integer goodsId);

    /**
     * 根据促销活动可视范围调整分销商可视促销活动关系
     *
     * @param distributorIds
     * @param promotionId
     * @return
     */
    Response distributorPromotionRelevanceByPromotionId(List<Integer> distributorIds, Integer promotionId);

    /**
     * 根据拼团秒杀可视范围调整分销商可视拼团秒杀关系
     *
     * @param distributorIds
     * @param groupSeckillId
     * @return
     */
    Response distributorGroupSeckillRelevanceByGroupSeckillId(List<Integer> distributorIds, Integer groupSeckillId);

    /**
     * 根据促销活动id删除分销商可视促销活动关系
     *
     * @param promotionId
     * @return
     */
    Response deletePromotionRelevanceByPromotionId(Integer promotionId);

    /**
     * 根据拼团秒杀id删除分销商可视拼团秒杀关系
     *
     * @param groupSeckillId
     * @return
     */
    Response deleteGroupSeckillRelevanceByGroupSeckillId(Integer groupSeckillId);

    /**
     * 更新分销商品牌可视
     *
     * @param cmd
     * @return
     */
    Response changeDistributorBrandRelevance(DistributorChangeBrandRpcCmd cmd);

    /**
     * 刷新分销商品牌可视
     *
     * @return
     */
    Response distributorBrandRelevance(List<Integer> brandIds, Integer distributorId);

    /**
     * 获取分销商商品可视范围数据
     *
     * @param distributorId
     * @return
     */
    Response<DistributorGoodsControlRpcDTO> distributorGoodsControl(Integer distributorId);

    /**
     * 获取一级分销商品牌品类可视范围数据
     *
     * @param distributorId
     * @return
     */
    Response<DistributorBrandCategoryControlRpcDTO> distributorBrandCategoryControl(Integer distributorId);

    /**
     * 根据品牌id获取可视分销商范围
     *
     * @param brandId
     * @return
     */
    Response<List<Integer>> getDistributorIdsByBrandId(Integer brandId);

    /**
     * 根据品类id获取可视分销商范围
     *
     * @param categoryId
     * @return
     */
    Response<List<Integer>> getDistributorIdsByCategoryId(Integer categoryId);

    /**
     * 获取分销商所有价格等级数据(货品)
     *
     * @param distributorId
     * @return
     */
    Response<DistributorScalePriceControlRpcDTO> distributorScalePriceControl(Integer distributorId,
                                                                              List<Integer> itemIds);

    /**
     * 获取分销商所有价格等级数据(商品)
     *
     * @param distributorId
     * @return
     */
    Response<DistributorScalePriceControlRpcDTO> distributorScalePriceControlByGoodsIds(Integer distributorId,
        List<Integer> goodsIds);

    /**
     * 获取分销商对应货品价格等级数据（包括分销商特价）
     *
     * @param qry
     * @return
     */
    Response<DistributorGoodsScalePriceControlRpcDTO>
        distributorGoodsScalePriceControl(DistributorGoodsScalePriceRpcQry qry);

    /**
     * 根据分销商Ids查询分销商基本信息
     *
     * @param ids
     * @return
     */
    Response<List<DistributorRpcDTO>> distributorByIds(List<Integer> ids);

    /**
     * 根据分销商Id查询分销商基本信息
     *
     * @param id
     * @return
     */
    Response<DistributorRpcDTO> distributorById(Integer id);

    /**
     * 根据分销商Id查询分销商地址国家
     *
     * @param id
     * @return
     */
    Response<Integer> distributorCountryById(Integer id);

    /**
     * 根据分销商id获取业务数据
     *
     * @param id
     * @return
     */
    Response<DistributorBusinessRpcDTO> getDistributorBusiness(Integer id);

    /**
     * 根据分销商id获取活动id列表
     *
     * @param id
     * @return
     */
    DistributorPromitonGroupSeckillRpcDTO getDistributorPromotionGroupSeckill(Integer id);

    /**
     * 根据业务员ids查找业务员管理的所有分销商ids（包括多级分销商）
     *
     * @param salesIds
     * @return
     */
    List<Integer> getDistributorIdsBySalesIds(List<Integer> salesIds);

    /**
     * 根据分销商erp内码获取分销商信息
     *
     * @param erpIds
     * @return
     */
    List<DistributorNextNameRpcDTO> getDistributorNextByErpIds(List<Integer> erpIds);

    /**
     * 根据分销商erp内码获取分销商基本信息
     *
     * @param erpId
     * @return
     */
    DistributorInfoRpcDTO getDistributorInfoByErpId(Integer erpId);

    /**
     * 根据分销商ids获取分销商信息
     *
     * @param distributorIds
     * @return
     */
    List<DistributorNextNameRpcDTO> getDistributorNextByDistributorIds(List<Integer> distributorIds);

    /**
     * 根据分销商ids获取分销商名称
     *
     * @param distributorIds
     * @return
     */
    Response<List<DistributorNameRpcDTO>> getDistributorNameByDistributorIds(List<Integer> distributorIds);

    /**
     * 获取所有一级分销商名称
     *
     * @return
     */
    Response<List<DistributorNameRpcDTO>> getAllDistributorNameOneTreeNode();

    /**
     * 根据分销商id获取分销商扩展信息
     *
     * @param distributorId
     * @return
     */
    Response<DistributorExtendDataRpcDTO> getDistributorExtendData(Integer distributorId);

    /**
     * 根据分销商id获取上级分销商扩展信息
     *
     * @param distributorId
     * @return
     */
    Response<DistributorExtendDataRpcDTO> getAncestorDistributorExtendData(Integer distributorId);

    /**
     * 根据分销商id获取同步erp相关信息
     *
     * @param distributorId
     * @return
     */
    Response<DistributorERPRpcDTO> getDistributorERPData(Integer distributorId);

    /**
     * 更新分销商erpid和erpNo
     *
     * @param cmd
     * @return
     */
    Response updateDistributorErpData(DistributorErpRpcCmd cmd);

    /**
     * 根据分销商分组ids获取分销商分组数据
     *
     * @param distributorGroupIds
     * @return
     */
    Response<List<DistributorGroupRpcDTO>> getDistributorGroupByDistributorGroupIds(List<Integer> distributorGroupIds);

    /**
     * 根据分销商id获取一级分销商信息
     *
     * @param distributorId
     * @return
     */
    Response<List<DistributorTreePathRpcDTO>> getDistributorTreePaths(Integer distributorId);

    /**
     * 获取上级分销商信息
     *
     * @param distributorId
     * @return
     */
    Response<UpperDistributorRpcDTO> getUpperDistributorId(Integer distributorId);

    /**
     *根据分销商id获取分销商的的支付方式
     * @param distributorId
     * @return
     */
    Response<DistributorPayWayRpcDTO> distributorPaymentWayById(Integer distributorId);

}
