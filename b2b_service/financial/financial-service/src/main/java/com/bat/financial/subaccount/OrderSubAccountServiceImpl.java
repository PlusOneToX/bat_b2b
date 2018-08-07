package com.bat.financial.subaccount;

import static com.bat.financial.common.constant.subaccount.OrderSubAccountConstant.ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_YES;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.bat.financial.subaccount.executor.OrderSubAccountQryExe;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.pay.constant.TradeMode;
import com.bat.financial.api.subaccount.OrderSubAccountServiceI;
import com.bat.financial.api.subaccount.dto.OrderSubAccountPageDTO;
import com.bat.financial.api.subaccount.dto.OrderSubAccountPageQry;
import com.bat.financial.api.subaccount.dto.WechatPaySubAccountCmd;
import com.bat.financial.common.constant.subaccount.FrontSubAccountStatus;
import com.bat.financial.dao.subaccount.dataobject.OrderSubAccountDO;
import com.bat.financial.pay.config.WechatConfig;
import com.bat.financial.pay.data.WxConfig;
import com.bat.financial.pay.executor.ErrorCode;
import com.bat.financial.pay.executor.PayCmdExc;
import com.bat.financial.pay.utils.WechatUtil;

import javax.annotation.Resource;

@Service
public class OrderSubAccountServiceImpl implements OrderSubAccountServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSubAccountServiceImpl.class);

    @Autowired
    private PayCmdExc payCmdExc;

    @Autowired
    private OrderSubAccountQryExe orderSubAccountQryExe;

    @Resource
    private WechatConfig wechatConfig;

    @Override
    public void test(WechatPaySubAccountCmd cmd) {
        WxConfig wxConfig = payCmdExc.getWxConfig(TradeMode.DISTRIBUTOR_SELF, (short)1, 1879, 1);
        LOGGER.info("微信服务商分账{}", JSON.toJSONString(cmd));
        HttpClient httpClient = WechatUtil.getWechatHttpClientV3(wxConfig);
        HttpPost httpPost =
            WechatUtil.getHttpPost(wechatConfig.getWxPayPartnerSubAccountCreateUrl(), JSON.toJSONString(cmd));
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity, Charset.defaultCharset());
                LOGGER.info("微信服务商分账返回{}", result);
            } else {
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity, Charset.defaultCharset());
                LOGGER.info("微信服务商分账、失败返回{}", result);
                throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, "分账失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw FinancialException.buildException(ErrorCode.B_CREATE_TRADE_ERROR, "分账失败");
        }
    }

    @Override
    public PageInfo<OrderSubAccountPageDTO> page(OrderSubAccountPageQry orderSubAccountPageQry) {
        if (orderSubAccountPageQry.getStatus() != null
            && FrontSubAccountStatus.SUB_ACCOUNT_FAIL.getId().equals(orderSubAccountPageQry.getStatus())) {
            orderSubAccountPageQry.setSubAccountFailFlag(ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_YES);
            // 订单状态设置为空
            orderSubAccountPageQry.setStatus(null);
        }
        PageHelper.startPage(orderSubAccountPageQry.getPage(), orderSubAccountPageQry.getSize());
        List<OrderSubAccountDO> accountDOList = orderSubAccountQryExe.pageByCondition(
            orderSubAccountPageQry.getDistributorId(), orderSubAccountPageQry.getStatus(),
            orderSubAccountPageQry.getOrderNo(), orderSubAccountPageQry.getSubAccountFailFlag(),
            orderSubAccountPageQry.getStartTime(), orderSubAccountPageQry.getEndTime(),
            orderSubAccountPageQry.getContentType(), orderSubAccountPageQry.getContent());
        PageInfo pageInfo = new PageInfo(accountDOList);
        List<OrderSubAccountPageDTO> list = new ArrayList<>();
        if (accountDOList.size() > 0) {
            for (OrderSubAccountDO orderSubAccountDO : accountDOList) {
                OrderSubAccountPageDTO orderSubAccountPageDTO = new OrderSubAccountPageDTO();
                BeanUtils.copyProperties(orderSubAccountDO, orderSubAccountPageDTO);
                if (orderSubAccountPageDTO.getSubAccountFailFlag() != null
                    && orderSubAccountPageDTO.getSubAccountFailFlag().equals(ORDER_SUB_ACCOUNT_EXIST_FAIL_FLAG_YES)) {
                    // 存在失败
                    orderSubAccountPageDTO.setStatus(FrontSubAccountStatus.SUB_ACCOUNT_FAIL.getId());
                }
                list.add(orderSubAccountPageDTO);
            }
        }
        pageInfo.setList(list);
        return pageInfo;
    }

}
