package com.bat.flexible.manager.exchange;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.goods.dto.GoodsItemSimplePageBean;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.exchange.executor.ExchangeGeneralItemPoolQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.dubboapi.goods.common.Response;
import com.bat.dubboapi.goods.goods.api.GoodsServiceRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemRpcDTO;
import com.bat.flexible.api.exchange.ExchangeGeneralItemPoolServiceI;
import com.bat.flexible.api.exchange.dto.ExchangeGeneralItemQry;
import com.bat.flexible.dao.exchange.dataobject.ExchangeGeneralItemPoolDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExchangeGeneralItemPoolServiceImpl implements ExchangeGeneralItemPoolServiceI {

    @Autowired
    private ExchangeGeneralItemPoolQryExe exchangeGeneralItemPoolQryExe;

    @DubboReference(check = false,timeout = 5000,retries = 0)
    private GoodsServiceRpc goodsServiceRpc;

    @Override
    public PageInfo<GoodsItemSimplePageBean> page(ExchangeGeneralItemQry exchangeGeneralItemQry) {
        PageHelper.startPage(exchangeGeneralItemQry.getPage(),exchangeGeneralItemQry.getSize());
        List<ExchangeGeneralItemPoolDO> doList = exchangeGeneralItemPoolQryExe.listByItemCodeDimAndOpenFlag(
                exchangeGeneralItemQry.getContent(), FlexibleCommonConstant.COMMON_OPEN_FLAG_YES
        );
        PageInfo pageInfo = new PageInfo<>(doList);
        if(doList ==null || doList.size()==0){
            return pageInfo;
        }
        List<GoodsItemSimplePageBean> list = BeanUtils.copyList(doList,GoodsItemSimplePageBean.class);
        List<String> itemCodeList = new ArrayList<>();
        list.stream().forEach(goodsItemSimplePageBean -> {
            itemCodeList.add(goodsItemSimplePageBean.getItemCode());
        });
        Response<List<GoodsItemRpcDTO>> itemResponse = goodsServiceRpc.listGoodsItemByCodes(itemCodeList);
        if(itemResponse ==null || !itemResponse.isSuccess()){
            throw new FlexibleCustomException("访问商品服务异常");
        }
        List<GoodsItemRpcDTO> goodsItemRpcDTOS = itemResponse.getData();
        Map<String, GoodsItemRpcDTO> rpcDTOMap = goodsItemRpcDTOS.stream().collect(Collectors.toMap(GoodsItemRpcDTO::getItemCode, goodsItemRpcDTO -> goodsItemRpcDTO));
        list.stream().forEach(goodsItemSimplePageBean -> {
            GoodsItemRpcDTO goodsItemRpcDTO = rpcDTOMap.get(goodsItemSimplePageBean.getItemCode());
            goodsItemSimplePageBean.setGoodsNo(goodsItemRpcDTO.getGoodsNo());
            goodsItemSimplePageBean.setItemName(goodsItemRpcDTO.getItemName());
            goodsItemSimplePageBean.setItemId(goodsItemRpcDTO.getId());
            goodsItemSimplePageBean.setGoodsName(goodsItemRpcDTO.getGoodsName());
            goodsItemSimplePageBean.setGoodsId(goodsItemRpcDTO.getGoodsId());
        });
        pageInfo.setList(list);
        return pageInfo;
    }
}
