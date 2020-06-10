package com.bat.flexible.manager.picture;


import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.picture.PictureModelMaterialDiyServiceI;
import com.bat.flexible.api.picture.dto.diy.PictureModelMaterialDiyCmd;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.picture.executor.PictureModelMaterialDiyCmdExe;
import com.bat.flexible.manager.picture.executor.PictureModelMaterialDiyQryExe;
import com.bat.flexible.manager.picture.validtor.PictureModelMaterialDiyValidator;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.picture.dataobject.PictureModelMaterialDiyDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PictureModelMaterialDiyServiceImpl implements PictureModelMaterialDiyServiceI {


    @Autowired
    private PictureModelMaterialDiyQryExe pictureModelMaterialDiyQryExe;

    @Autowired
    private PictureModelMaterialDiyCmdExe pictureModelMaterialDiyCmdExe;

    @Autowired
    private PictureModelMaterialDiyValidator pictureModelMaterialDiyValidtor;

    @Transactional
    @Override
    public Response create(PictureModelMaterialDiyCmd modelMaterialDiyCmd) {
        PictureModelMaterialDiyDO diyDO = pictureModelMaterialDiyQryExe.getByMaterialIdAndModelIdAndPictureId(
                modelMaterialDiyCmd.getMaterialId(),modelMaterialDiyCmd.getModelId(),modelMaterialDiyCmd.getPictureId()
        );
        if (diyDO != null){
            return Response.buildSuccess();
        }
        pictureModelMaterialDiyValidtor.valid(modelMaterialDiyCmd);
        diyDO =  BeanUtils.copy(modelMaterialDiyCmd,PictureModelMaterialDiyDO.class);
        diyDO.setCreateTime(new Date());
        pictureModelMaterialDiyCmdExe.create(diyDO);
        return Response.buildSuccess();
    }

    /**
     * 根据材质id、型号id、图片id查询
     * @param materialId 材质id
     * @param modelId 型号id
     * @param pictureId 图片id
     * @return
     */
    @Override
    public Response<PictureModelMaterialDiyDO> getByMaterialIdAndModelIdAndPictureId(Integer materialId, Integer modelId, Integer pictureId) {
        PictureModelMaterialDiyDO diyDO = pictureModelMaterialDiyQryExe.getByMaterialIdAndModelIdAndPictureId(materialId,modelId,pictureId);
        return Response.of(diyDO);
    }


    /**
     * 删除关联关系
     * @param materialId
     * @param modelId
     * @param pictureId
     */
    @Transactional
    @Override
    public void delete(Integer materialId, Integer modelId, Integer pictureId) {
        if(materialId==null && modelId ==null && pictureId==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        List<PictureModelMaterialDiyDO> pictureModelMaterialDiyDOList = pictureModelMaterialDiyQryExe.listByCondition(materialId,modelId,pictureId);
        if(pictureModelMaterialDiyDOList ==null || pictureModelMaterialDiyDOList.size()==0){
            return;
        }
        pictureModelMaterialDiyDOList.stream().forEach(pictureModelMaterialDiyDO -> {
            pictureModelMaterialDiyCmdExe.delete(pictureModelMaterialDiyDO.getId());
            pictureModelMaterialDiyCmdExe.removeCache(pictureModelMaterialDiyDO.getMaterialId(),pictureModelMaterialDiyDO.getModelId(),pictureModelMaterialDiyDO.getPictureId());
        });
    }
}
