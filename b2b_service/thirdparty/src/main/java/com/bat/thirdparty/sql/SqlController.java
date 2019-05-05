package com.bat.thirdparty.sql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/open/sql")
@Api(tags = "数据迁移")
public class SqlController {

    @Autowired
    private OrderBusinessLogServiceI orderBusinessLogServiceI;

    @GetMapping(value = "/import")
    @ApiOperation(value = "数据迁移")
    public Response importSql(){
        JSONArray jsonArray = HttpUtil.getJson("http://127.0.0.1:9997/open/sql/export/8001", JSONArray.class);
        if(jsonArray !=null && jsonArray.size() >0){
            //
            for(int x=0 ; x< jsonArray.size();x++){
                JSONObject jsonObject = jsonArray.getJSONObject(x);
                Integer orderStatus = jsonObject.getInteger("orderStatus");
                if(orderStatus ==5){
                    continue;
                }
                Integer id = jsonObject.getInteger("id");
                if(id<100000){
                    continue;
                }
                String userId = jsonObject.getString("userId");
                if(StringUtils.isBlank(userId)){
                    continue;
                }
                String otherOrderNo = jsonObject.getString("otherOrderNo");
                if(StringUtils.isBlank(otherOrderNo)){
                    continue;
                }
                Integer orderSource = jsonObject.getInteger("orderSource");
                Integer distributorId = jsonObject.getInteger("distributorId");
                if(orderSource==28){
                    continue;
                }
                if(distributorId==2529 || distributorId==2601){
                    continue;
                }
                OrderBusinessLogDO businessLogDO = new OrderBusinessLogDO();
                businessLogDO.setTowardType(OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_ID.getTowardType());
                businessLogDO.setLogType(OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_ID.getLogType());
                if(distributorId==2834 || distributorId ==2354){
                    businessLogDO.setLogType(OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_CODE.getLogType());
                }
                businessLogDO.setDistributorId(distributorId);
                businessLogDO.setPlatform(String.valueOf(orderSource));
                businessLogDO.setOtherOrderNo(otherOrderNo);
                businessLogDO.setBusinessData(userId);
                businessLogDO.setRequestParamJson("数据迁移");
                businessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_SUCCESS);
                businessLogDO.setUpdateTime(new Date());
                orderBusinessLogServiceI.create(businessLogDO);
            }
        }
        return Response.buildSuccess();
    }
}
