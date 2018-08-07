package com.bat.distributor.service.user.executor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.user.dto.user.UserGoodsListQry;
import com.bat.distributor.api.user.dto.user.UserWxMiniProgramLoginCmd;
import com.bat.distributor.api.user.dto.user.UserWxMiniProgramOpenIdCmd;
import com.bat.distributor.dao.distributor.DistributorBrandRelevanceMapper;
import com.bat.distributor.dao.distributor.DistributorGoodsRelevanceMapper;
import com.bat.distributor.dao.distributor.dataobject.DistributorBrandRelevanceDO;
import com.bat.distributor.dao.distributor.dataobject.DistributorGoodsRelevanceDO;
import com.bat.distributor.service.common.DistributorConfig;
import com.bat.distributor.service.customer.convertor.CustomerConvertor;
import com.bat.distributor.service.distributor.executor.DistributorConfigExe;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.goods.brand.api.GoodsBrandServiceRpc;
import com.bat.dubboapi.goods.brand.dto.UserBrandListRpcQry;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.UserGoodsListRpcQry;
import com.bat.dubboapi.goods.goods.dto.data.UserGoodsRpcDTO;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.dubboapi.thirdparty.wx.dto.WxMiniProgramAuthorizeRpcCmd;
import com.bat.dubboapi.thirdparty.wx.dto.data.WxMiniProgramAuthorizeRpcDTO;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/4/25 14:17
 */
@Component
public class UserRpcQryExe {

    @DubboReference(check = false, timeout = 5000)
    private GoodsBrandServiceRpc brandServiceRpc;

    @DubboReference(check = false, timeout = 5000)
    private GoodsServiceRpc goodsServiceRpc;
    @Resource
    DistributorBrandRelevanceMapper brandRelevanceMapper;
    @Resource
    DistributorGoodsRelevanceMapper goodsRelevanceMapper;

    @DubboReference(check = false, timeout = 5000)
    private WxServiceRpc wxServiceRpc;

    @Resource
    private DistributorConfigExe distributorConfigExe;


    /**
     * 通过商品服务获取分销商品牌列表
     * 
     * @param distributorId
     * @return
     */
    public List<UserBrandRpcDTO> listBrand(Integer distributorId) {
        UserBrandListRpcQry qry = new UserBrandListRpcQry();
        /** 最上级分销商品牌可视范围 */
        DistributorBrandRelevanceDO relevanceDO = brandRelevanceMapper.selectByDistributorId(distributorId);
        if (relevanceDO != null) {
            List<Integer> brandIds =
                Arrays.stream(relevanceDO.getBrandIds().substring(1, relevanceDO.getBrandIds().length() - 1).split(","))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
            qry.setBrandIds(brandIds);
        }
        Response<List<UserBrandRpcDTO>> rpcResponse = brandServiceRpc.listBrand(qry);
        if (rpcResponse.isSuccess()) {
            return rpcResponse.getData();
        } else {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_USER_BRAND_ERROR);
        }
    }

    /**
     * 微信小程序授权
     *
     * appid 是前端传的 appSecret 是配置文件写死的 其实还是写死的
     *
     * @param cmd
     * @return
     */
    WxMiniProgramAuthorizeRpcDTO wxMiniProgramAuthorize(UserWxMiniProgramOpenIdCmd cmd) {
        DistributorConfig distributorConfig=distributorConfigExe.getConfig();
        WxMiniProgramAuthorizeRpcCmd rpcCmd =
            CustomerConvertor.toWxMiniProgramAuthorizeRpcDTO(cmd,distributorConfig.getWxMiniProgramAppId(), distributorConfig.getWxMiniProgramAppSecret());
        com.bat.dubboapi.thirdparty.common.Response<WxMiniProgramAuthorizeRpcDTO> rpcDTOResponse =
            wxServiceRpc.wxMiniProgramAuthorize(rpcCmd);
        if (!rpcDTOResponse.isSuccess()) {
            throw DistributorException.buildException(rpcDTOResponse.getErrCode(), rpcDTOResponse.getErrMessage());
        }
        return rpcDTOResponse.getData();
    }

    /**
     * 微信小程序授权
     *
     * @param cmd
     * @return
     */
    WxMiniProgramAuthorizeRpcDTO wxMiniProgramAuthorize(UserWxMiniProgramLoginCmd cmd) {
        DistributorConfig distributorConfig= distributorConfigExe.getConfig();
        WxMiniProgramAuthorizeRpcCmd rpcCmd =
            CustomerConvertor.toWxMiniProgramAuthorizeRpcDTO(cmd,distributorConfig.getWxMiniProgramAppId(), distributorConfig.getWxMiniProgramAppSecret());
        com.bat.dubboapi.thirdparty.common.Response<WxMiniProgramAuthorizeRpcDTO> rpcDTOResponse =
            wxServiceRpc.wxMiniProgramAuthorize(rpcCmd);
        if (!rpcDTOResponse.isSuccess()) {
            throw DistributorException.buildException(rpcDTOResponse.getErrCode(), rpcDTOResponse.getErrMessage());
        }
        return rpcDTOResponse.getData();
    }

    /**
     * 通过商品服务获取分销商商品列表
     * 
     * @param qry
     * @return
     */
    public PageInfo<UserGoodsRpcDTO> listGoods(UserGoodsListQry qry, List<Integer> noGoodsIds, Integer distributorId) {
        /** 最上级节点业务数据 */
        UserGoodsListRpcQry rpcQry = new UserGoodsListRpcQry();
        BeanUtils.copyProperties(qry, rpcQry);
        /** 最上级分销商商品可视范围 */
        DistributorGoodsRelevanceDO relevanceDO = goodsRelevanceMapper.selectByDistributorId(distributorId);
        if (relevanceDO != null) {
            List<Integer> goodsIds =
                Arrays.stream(relevanceDO.getGoodsIds().substring(1, relevanceDO.getGoodsIds().length() - 1).split(","))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
            rpcQry.setGoodsIds(goodsIds);
        }
        rpcQry.setNoGoodsIds(noGoodsIds);
        Response<PageInfo<UserGoodsRpcDTO>> rpcResponse = goodsServiceRpc.listGoods(rpcQry);
        if (rpcResponse.isSuccess()) {
            return rpcResponse.getData();
        } else {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_USER_GOODS_ERROR);
        }
    }

}
