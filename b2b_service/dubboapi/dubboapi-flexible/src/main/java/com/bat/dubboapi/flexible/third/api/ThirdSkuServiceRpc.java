package com.bat.dubboapi.flexible.third.api;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.order.dto.OrderGoodsDiyParamDTO;
import com.bat.dubboapi.flexible.third.dto.MolejiCaseCmd;
import com.bat.dubboapi.flexible.third.dto.MolejiLogisticsCmd;
import com.bat.dubboapi.flexible.third.dto.ThirdSkuRelevanceRpcDTO;

import java.util.List;

public interface ThirdSkuServiceRpc {

    /**
     * 转换为摩乐吉的物流配送参数
     * @param distributorId
     * @param expressCode
     * @param expressTime
     * @param expressNo
     * @param otherOrderNo
     * @return
     */
     Response<MolejiLogisticsCmd> trandformLogistics(Integer distributorId, String expressCode, Long expressTime, String expressNo, String otherOrderNo);

    /**
     * 转换摩乐吉下单明细
     * @param caseCmdList 摩乐吉传的明细列表是
     * @param molejiDistributorId
     * @return
     */
    Response<List<OrderGoodsDiyParamDTO>> transformOrderSync(List<MolejiCaseCmd> caseCmdList, Integer molejiDistributorId);

    /**
     * 根据分销商id以及sku编码获取sku关联信息
     * @param distributorId
     * @param thirdSkuNo
     * @return
     */
    Response<ThirdSkuRelevanceRpcDTO> findSkuRelevance(Integer distributorId, String thirdSkuNo);
}
