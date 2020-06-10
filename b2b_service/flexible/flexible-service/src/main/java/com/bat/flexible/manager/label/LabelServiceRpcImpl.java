package com.bat.flexible.manager.label;

import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.common.constant.label.LabelConstant;
import com.bat.flexible.manager.common.utils.pdf.PDFLabelService;
import com.bat.flexible.manager.distributor.dubbo.executor.FlexibleDistributorQryExe;
import com.bat.flexible.manager.label.executor.LabelQryExe;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.label.api.LabelServiceRpc;
import com.bat.dubboapi.flexible.label.dto.LabelDTORpcQry;
import com.bat.dubboapi.flexible.label.dto.OrderDiyLabelDTO;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static com.bat.dubboapi.order.order.dto.enmus.FactoryEnum.MK;

@DubboService
public class LabelServiceRpcImpl implements LabelServiceRpc {

    @Autowired
    private LabelQryExe labelQryExe;

    @Autowired
    private PDFLabelService pdfLabelUtils;

    @Autowired
    private FlexibleDistributorQryExe flexibleDistributorQryExe;

    @Value("${country.china.id}")
    private Integer chinaCountryId;

    /**
     * 条件查询图片关联的标签列表
     * 
     * @param distributorIds
     *            分销商id列表
     * @param categoryId
     *            产品类型id
     * @param pictureId
     *            图片id
     * @param distributorScope
     *            分销商属于国内还是国外 2、国内 3、国外
     * @return
     */
    @Override
    public Response<List<LabelDTORpcQry>> listDiyLableByCondition(List<Integer> distributorIds, Integer categoryId,
        Integer pictureId, Short distributorScope) {
        List<LabelDTORpcQry> qryList = null;
        List<LabelDO> list =
            labelQryExe.listDiyLableByCondition(distributorIds, categoryId, pictureId, distributorScope);
        if (list == null || list.size() == 0) {
            return Response.of(qryList);
        }
        qryList = BeanUtils.copyList(list, LabelDTORpcQry.class);
        return Response.of(qryList);
    }

    /**
     * 获取标签 上面的RPC接口 没有查询到调用的地方
     * 
     * @param distributorId
     * @param categoryId
     * @param pictureId
     * @return
     */
    @Override
    public Response<List<LabelDTORpcQry>> listDiyLabelByCondition(Integer distributorId, Integer categoryId,
        Integer pictureId) {
        Integer countryId = flexibleDistributorQryExe.getCountryIdByDistributorId(distributorId);
        Short distributorScope = LabelConstant.SCOPE_DISTRIBUTOR_INTERNAL;
        if (countryId != chinaCountryId.intValue()) {
            // 国外
            distributorScope = LabelConstant.SCOPE_DISTRIBUTOR_FOREIGN;
        }
        List<Integer> distributorIds = flexibleDistributorQryExe.getDistributorTreePaths(distributorId);
        return listDiyLableByCondition(distributorIds, categoryId, pictureId, distributorScope);
    }

    @Override
    public Response<LabelDTORpcQry> getById(Integer id) {
        LabelDO labelDO = labelQryExe.getById(id);
        return Response.of(BeanUtils.copy(labelDO, LabelDTORpcQry.class));
    }

    /**
     * 原来的方法 生成标签 如果不传参数 默认是麦克
     * @param orderLableCmd
     * @return
     */
    @Override
    public Response<List<OrderDiyLabelDTO>> createOrderDiyLabel(OrderLableCmd orderLableCmd) {
        return createOrderDiyLabel(MK.name(),orderLableCmd);
    }

    @Override
    public Response<List<OrderDiyLabelDTO>> createOrderDiyLabel(String factoryNo,
        OrderLableCmd orderLableCmd) {
        try {
            List<OrderDiyLabelDTO> list = pdfLabelUtils.generateLabelFile(factoryNo, orderLableCmd);
            return Response.of(list);
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(), e.getMsg());
        }
    }
}
