package com.bat.order.web.order;

import com.github.pagehelper.PageInfo;
import com.bat.order.api.basic.BaseId;
import com.bat.order.api.common.response.Response;
import com.bat.order.api.order.OrderInfoServiceI;
import com.bat.order.api.order.constants.SearchType;
import com.bat.order.api.order.dto.orderquery.admin.AdminDistributorOrderInfoDetailDTO;
import com.bat.order.api.order.dto.orderquery.admin.AdminDistributorOrderInfoListQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderDetailQry;
import com.bat.order.api.order.dto.orderquery.admin.AdminOrderInfoListDTO;
import com.bat.order.web.annotation.SearchMethod;
import com.bat.order.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.InputStream;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/6/21 15:03
 */
@Api(tags = "同步ERP订单列表后台接口")
@RequestMapping(value = "/order/v1/web/admin/erpOrder")
@RestController
public class AdminErpOrderController extends BaseController {

    @Autowired
    private OrderInfoServiceI orderInfoServiceI;

    @GetMapping(value = "/list")
    @ApiOperation(value = "订单列表")
    @SearchMethod
    public Response<PageInfo<AdminOrderInfoListDTO>> listOrderInfo(@Valid AdminDistributorOrderInfoListQry qry) {
        PageInfo<AdminOrderInfoListDTO> pageInfo = orderInfoServiceI.listDistributorOrderInfo(qry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/detail")
    @ApiOperation(value = "订单详情")
    public Response<AdminDistributorOrderInfoDetailDTO> getOrderDetail(@Valid BaseId id) {
        AdminOrderDetailQry qry = new AdminOrderDetailQry();
        qry.setId(id.getId());
        qry.setSearchType(SearchType.ADMIN_ERP_ORDER_LIST);
        return Response.of(orderInfoServiceI.getDistributorOrderDetail(qry));
    }

    @GetMapping(value = "/po/getOrderIdByOrderNo")
    @ApiOperation(value = "根据订单Id查询订单编码")
    public Response<String> getOrderIdByOrderNo(@Valid BaseId id) {
        return Response.of(orderInfoServiceI.getOrderIdByOrderNo(id.getId()));
    }

    /**
     * 兑换码excel导入
     */

    @PostMapping(value = "/import")
    @ApiOperation(value = "ERP订单设置行序号脚本")
    public Response importCode(@RequestParam("file") MultipartFile file)  {
        try {

          /*  MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
            Iterator<String> itr = multipart.getFileNames();
            MultipartFile file = null;
            if (itr.hasNext()) {
                String str = itr.next();    //这个文件并不是原来的文件名
                file = multipart.getFile(str);
            }*/
            InputStream inputStream = file.getInputStream();
            return orderInfoServiceI.setOrderGoodsSerialNumber(inputStream);
        } catch (Exception e) {
            return Response.buildFailure("100", e.getMessage());
        }
    }
}
