package com.bat.flexible.manager.material;

import com.alibaba.fastjson.JSON;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.material.convertor.MaterialConvertor;
import com.bat.flexible.manager.material.executor.MaterialCmdExe;
import com.bat.flexible.manager.material.executor.MaterialQryExe;
import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.material.api.MaterialServiceRpc;
import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService
public class MaterialServiceRpcImpl implements MaterialServiceRpc {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialServiceRpcImpl.class);

    @Autowired
    private MaterialQryExe materialQryExe;

    @Autowired
    private MaterialCmdExe materialCmdExe;

    @Override
    public Response getByMaterialId(Integer materialId) {
        MaterialDO materialDO =materialQryExe.getById(materialId);
        return Response.of(MaterialConvertor.ToMaterialDTORpcQry(materialDO));
    }

    @Override
    public Response<List<MaterialDTORpcQry>> listByItemIdList(List<Integer> itemIdList) {
        List<MaterialDO> list = materialQryExe.listByItemIdList(itemIdList);
        List<MaterialDTORpcQry> resultList = BeanUtils.copyList(list,MaterialDTORpcQry.class);
        return Response.of(resultList);
    }

    /**
     * 查询材质关联的所有货品id列表
     * @return
     */
    @Override
    public Response<List<MaterialDTORpcQry>> listAllGroupByItemId() {
        List<MaterialDO> materialDOList = materialQryExe.listAllGroupByItemId();
        if(materialDOList ==null || materialDOList.size()==0){
            return Response.of(null);
        }
       List<MaterialDTORpcQry> list = BeanUtils.copyList(materialDOList,MaterialDTORpcQry.class);
        return Response.of(list);
    }

    @Override
    public Response<List<MaterialDTORpcQry>> listAll() {
        List<MaterialDO> materialDOList = materialQryExe.listAll();
        if(materialDOList ==null || materialDOList.size()==0){
            return Response.of(new ArrayList<>());
        }
        List<MaterialDTORpcQry> list = BeanUtils.copyList(materialDOList,MaterialDTORpcQry.class);
        return Response.of(list);
    }

    @Transactional
    @Override
    public Response soldOutByItem(List<Integer> itemIdList) {
        LOGGER.info("货品下架、处理关联材质的下架：{}", JSON.toJSON(itemIdList));
        if(itemIdList ==null || itemIdList.size()==0){
            return Response.buildSuccess();
        }
        List<MaterialDO> materialDOList = materialQryExe.listByItemIdList(itemIdList);
        if(materialDOList !=null && materialDOList.size()>0){
            //下架
            materialDOList.stream().forEach(materialDO -> {
                if(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(materialDO.getOpenFlag())){
                    //已上架要改为下架
                    materialDO.setOpenFlag(FlexibleCommonConstant.COMMON_OPEN_FLAG_NO);
                    materialDO.setUpdateTime(new Date());
                    LOGGER.info("货品下架、下架材质：{}", JSON.toJSON(materialDO));
                    materialCmdExe.update(materialDO);
                }
            });
        }
        return Response.buildSuccess();
    }
}
