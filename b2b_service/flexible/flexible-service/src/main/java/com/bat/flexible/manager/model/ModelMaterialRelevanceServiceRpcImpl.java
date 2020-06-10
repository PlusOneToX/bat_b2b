package com.bat.flexible.manager.model;

import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.model.api.ModelMaterialRelevanceServiceRpc;
import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoBomCmd;
import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoListCmd;
import com.bat.dubboapi.flexible.model.dto.ModelMaterialRelevanceDTORpcQry;
import com.bat.dubboapi.flexible.model.dto.OrderGoodDiyQry;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.manager.model.executor.ModelMaterialRelevanceCmdExe;
import com.bat.flexible.manager.model.executor.ModelMaterialRelevanceQryExe;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
public class ModelMaterialRelevanceServiceRpcImpl implements ModelMaterialRelevanceServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelMaterialRelevanceServiceRpcImpl.class);

    @Autowired
    private ModelMaterialRelevanceQryExe modelMaterialRelevanceQryExe;

    @Autowired
    private ModelMaterialRelevanceCmdExe modelMaterialRelevanceCmdExe;

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private ModelServiceI modelServiceI;

    private static final Logger logger = LoggerFactory.getLogger(ModelMaterialRelevanceServiceRpcImpl.class);

    @Override
    public Response<ModelMaterialRelevanceDTORpcQry> getByModelIdAndMaterialId(Integer modelId, Integer materialId) {

        ModelMaterialRelevanceDO modelMaterialRelevanceDO = modelMaterialRelevanceQryExe.getByModelIdAndMaterialId(modelId, materialId, false);
        MaterialDO materialDO = materialServiceI.getById(materialId);
        ModelDO modelDO = modelServiceI.getById(modelId);
        ModelMaterialRelevanceDTORpcQry modelMaterialRelevanceDTORpcQry = BeanUtils.copy(modelMaterialRelevanceDO, ModelMaterialRelevanceDTORpcQry.class);
        modelMaterialRelevanceDTORpcQry.setModelSku(modelDO.getModelNo());
        modelMaterialRelevanceDTORpcQry.setModelName(modelDO.getName());
        modelMaterialRelevanceDTORpcQry.setMaterialSku(materialDO.getMaterialNo());
        modelMaterialRelevanceDTORpcQry.setMaterialName(materialDO.getName());
        return Response.of(modelMaterialRelevanceDTORpcQry);
    }

    @Override
    public Response<List<OrderGoodDiyQry>> listByModelIdAndMaterialId(List<OrderGoodDiyQry> orderGoodDiys) {
        try {
            orderGoodDiys.forEach(orderGoodDiyQry -> {
                ModelMaterialRelevanceDO modelMaterialRelevanceDO = modelMaterialRelevanceQryExe
                        .getByModelIdAndMaterialId(orderGoodDiyQry.getModelId(), orderGoodDiyQry.getMaterialId(), false);
                orderGoodDiyQry.setWeight(modelMaterialRelevanceDO.getWeight());
            });
            return Response.of(orderGoodDiys);
        } catch (FlexibleCustomException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getMessage(), StringUtils.isNotBlank(MessageUtils.get(e.getMessage()))
                    ? MessageUtils.get(e.getMessage()) : e.getMessage());
        } catch (FlexibleDubboApiException e) {
            e.printStackTrace();
            return Response.buildFailure(e.getCode(),
                    StringUtils.isNotBlank(MessageUtils.get(e.getMsg())) ? MessageUtils.get(e.getMsg()) : e.getMsg());
        }
    }

    /**
     * 设置型号和材质关系是否缺货
     *
     * @param erpGoodsCustomInfoListCmd
     * @return
     */
    @Transactional
    @Override
    public Response syncMaterialAndModelRelaStockOutStatus(ErpGoodsCustomInfoListCmd erpGoodsCustomInfoListCmd) {
        for (int x = 0; x < erpGoodsCustomInfoListCmd.getBomCodeList().size(); x++) {
            ErpGoodsCustomInfoBomCmd erpGoodsCustomInfoBomCmd = erpGoodsCustomInfoListCmd.getBomCodeList().get(x);
            MaterialDO materialDO = materialServiceI.getByItemCode(erpGoodsCustomInfoBomCmd.getItemCode());
            if (materialDO == null) {
                /*  throw new BaseException(GoodsErrorConstant.ItemCodeErrorCode,
                        "【"+erpGoodsCustomInfoBomCmd.getItemCode()+"】"+GoodsErrorConstant.ItemCodeErrorMsg);*/
                LOGGER.info("B2B没有这个物料编码{}，忽略本次请求", erpGoodsCustomInfoBomCmd.getItemCode());
                continue;
            }

            List<ModelMaterialRelevanceDO> relevanceDOList = modelMaterialRelevanceQryExe
                    .listByCondition(materialDO.getId(), null, null, erpGoodsCustomInfoBomCmd.getBomCode());
            if (relevanceDOList == null || relevanceDOList.size() == 0) {
                String msg = "【" + erpGoodsCustomInfoBomCmd.getBomCode() + "】和物料编码【"
                        + erpGoodsCustomInfoBomCmd.getItemCode() + "】没有关联关系";
                // throw new BaseException(GoodsCustomInfoErrorConstant.BomCodeAndItemCodeNoRelaError.getCode(),msg);
                LOGGER.info("B2B没有这个材质对应的bom编码{}，忽略本次请求：" + msg, erpGoodsCustomInfoBomCmd.getBomCode());
                continue;
            }
            for (int i = 0; i < relevanceDOList.size(); i++) {
                ModelMaterialRelevanceDO relevanceDO = relevanceDOList.get(i);

                if (relevanceDO.getUnderStockFlag() != null
                        && relevanceDO.getUnderStockFlag() - erpGoodsCustomInfoBomCmd.getStockOutStatus() == 0) {
                    // 没有变化
                    continue;
                }
                relevanceDO.setUnderStockFlag(erpGoodsCustomInfoBomCmd.getStockOutStatus());

                /*  String operatorMsg = "型号【"+relevanceDO.getModelName()+"】和材质【"+goodsCustomInfo.getMaterialName()+"】组合";
                if(erpGoodsCustomInfoBomCmd.getStockOutStatus() - GoodsCustomInfoConstant.IsStockOutNO==0){
                    operatorMsg=operatorMsg+"修改为不缺货";
                }else {
                    operatorMsg=operatorMsg+"修改为缺货";
                }
                //新增一条型号操作记录
                iModelOperateLog.addModelOperateLog(JSON.toJSONString(erpGoodsCustomInfoBomCmd), erpGoodsCustomInfoListCmd.getOperateUserName(), goodsCustomInfo.getModelId().longValue(),
                        operatorMsg, ModelOperateLogConstant.OperatePlatformByERP, erpGoodsCustomInfoListCmd.getRemark());*/
                //软壳不修改缺货状态
                if (relevanceDO.getMaterialId() == 82 || relevanceDO.getMaterialId() == 87) {
                    relevanceDO.setUnderStockFlag(null);
                }
                modelMaterialRelevanceCmdExe.update(relevanceDO);

            }
        }
        return Response.buildSuccess();
    }
}
