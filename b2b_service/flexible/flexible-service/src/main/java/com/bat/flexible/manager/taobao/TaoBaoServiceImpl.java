package com.bat.flexible.manager.taobao;

import com.alibaba.fastjson.JSON;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.taobao.TaoBaoServiceI;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.distributor.electricity.api.DistributorElectricityServiceRpc;
import com.bat.dubboapi.distributor.electricity.dto.DistributorElectricityRelationMappingRpcDTO;
import com.bat.dubboapi.thirdparty.alibaba.taobao.api.ThirdPartyTaoBaoServiceRpc;
import com.bat.dubboapi.thirdparty.alibaba.taobao.dto.TaoBaoResp;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaoBaoServiceImpl implements TaoBaoServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaoBaoServiceImpl.class);

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private ThirdPartyTaoBaoServiceRpc thirdPartyTaoBaoServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private DistributorElectricityServiceRpc distributorElectricityServiceRpc;

    @Override
    public Response getItemSeller(Integer distributorId, Long numId, Long skuId, String sellerNick) {
        String appkey = "32397633";
        String secret = "2a53bac701bc09035c8e6430fb90eada";
        String sessionKey = "6101c218f708fa706ca2cc96b57571f19eff2ec1f045e151103997617";
        String url = "http://gw.api.taobao.com/router/rest";

        com.bat.dubboapi.distributor.common.Response<DistributorElectricityRelationMappingRpcDTO> distributorResp = distributorElectricityServiceRpc.getByDistributorIdAndSellerNick(distributorId, sellerNick);
        LOGGER.info("查询淘宝配置返回{}", JSON.toJSONString(distributorResp));
        if (!distributorResp.isSuccess()) {
            throw new FlexibleCustomException("获取分销商电商映射关系异常");
        }
        DistributorElectricityRelationMappingRpcDTO data = distributorResp.getData();
        if (data == null) {
            throw new FlexibleCustomException("该分销商尚未配置电商映射关系");
        }
        com.bat.dubboapi.thirdparty.common.Response<TaoBaoResp> taoBaoRespResponse = thirdPartyTaoBaoServiceRpc.getItemSeller(data.getAppKey(), data.getSecret(),
                data.getSessionKey(), data.getUrl(), numId);
        LOGGER.info(data.getAppKey() + "淘宝接口查询返回{}", JSON.toJSONString(taoBaoRespResponse));
        if (!taoBaoRespResponse.isSuccess()) {
            throw new FlexibleCustomException("获取淘宝货品信息异常");
        }
        return Response.of(taoBaoRespResponse.getData());
    }
}
