package com.bat.flexible.manager.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.ModelRelaSimpleDTO;
import com.bat.flexible.api.picture.PictureModelRelevanceServiceI;
import com.bat.flexible.manager.picture.executor.PictureModelReferencesCmdExe;
import com.bat.flexible.manager.picture.executor.PictureModelReferencesQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.picture.dataobject.PictureModelRelevanceDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.picture.PictureConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PictureModelReferencesServiceImpl implements PictureModelRelevanceServiceI {


    @Autowired
    private PictureModelReferencesQryExe pictureModelReferencesQryExe;


    @Autowired
    private PictureModelReferencesCmdExe pictureModelReferencesCmdExe;

    @Autowired
    private ModelServiceI modelServiceI;

    @Override
    @Transactional
    public void saveRela(Integer pictureId, Short modelScope, Boolean isAdd, AdminResponse adminResponse, List<Integer> modelIdList) {
        if(modelScope - PictureConstant.MODEL_SCOPE_MODEL_ASSIGN !=0){
            //非指定型号
            return;
        }
       List<ModelDO> modelDOList = modelServiceI.listByModelIdList(modelIdList);
        //处理编辑的关联关系
       dealWithByUpdate(isAdd,modelIdList,adminResponse,pictureId);
       if(modelIdList != null && modelIdList.size()>0){
           modelIdList.stream().forEach(modelId -> {
               PictureModelRelevanceDO relevanceDO = new PictureModelRelevanceDO();
               relevanceDO.setPictureId(pictureId);
               relevanceDO.setModelId(modelId);
               relevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
               setAdminMsg(relevanceDO,adminResponse);
               pictureModelReferencesCmdExe.create(relevanceDO);
           });
       }
    }

    private void setAdminMsg(PictureModelRelevanceDO relevanceDO, AdminResponse adminResponse) {
        if(relevanceDO.getId() ==null){
            relevanceDO.setCreateTime(new Date());
            relevanceDO.setCreateUserId(adminResponse.getId());
            relevanceDO.setCreateUserName(adminResponse.getUserName());
        }
        relevanceDO.setUpdateTime(new Date());
        relevanceDO.setUpdateUserId(adminResponse.getId());
        relevanceDO.setUpdateUserName(adminResponse.getUserName());
    }

    @Override
    public List<ModelRelaSimpleDTO> listModelRelaByPictureIdAndDelFlag(Integer pictureId, Short delFlag) {
        List<PictureModelRelevanceDO> modelRelevanceDOList = pictureModelReferencesQryExe.listByCondition(pictureId,null,null);
        if(modelRelevanceDOList ==null|| modelRelevanceDOList.size() ==0){
            return null;
        }
        List<ModelRelaSimpleDTO> list = new ArrayList<>();
        modelRelevanceDOList.stream().forEach(pictureModelRelevanceDO -> {
            ModelRelaSimpleDTO relaSimpleDTO = new ModelRelaSimpleDTO();
            relaSimpleDTO.setModelId(pictureModelRelevanceDO.getModelId());
            relaSimpleDTO.setId(pictureModelRelevanceDO.getId());
            //处理redis数据
            list.add(relaSimpleDTO);
        });
        return list;
    }

    @Override
    public List<PictureModelRelevanceDO> listByCondition(Integer pictureId, Integer modelId,List<Integer> pictureIdList) {
        return pictureModelReferencesQryExe.listByCondition(pictureId,modelId,pictureIdList);
    }

    /**
     * 删除关联关系
     * @param modelId
     * @param pictureId
     * @param currentAdmin
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer modelId, Integer pictureId, AdminResponse currentAdmin) {
        if(modelId ==null && pictureId==null){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        List<PictureModelRelevanceDO> pictureModelRelevanceDOList = pictureModelReferencesQryExe.listByCondition(pictureId, modelId,null);

    }

    /**
     * 处理编辑时的关联关系
     * @param isAdd
     * @param modelIdList
     * @param adminResponse
     */
    private void dealWithByUpdate(Boolean isAdd, List<Integer> modelIdList, AdminResponse adminResponse,Integer pictureId) {
        if(isAdd){
            return;
        }
        List<PictureModelRelevanceDO> referencesDOList = pictureModelReferencesQryExe.listByCondition(pictureId, null,null);
        if(referencesDOList != null && referencesDOList.size()>0){
            for(int x=0;x<referencesDOList.size();x++){
                if(modelIdList !=null && modelIdList.size()>0){
                    for(int y=0;y<modelIdList.size();y++){
                        if(referencesDOList.get(x).getModelId() - modelIdList.get(y) ==0){
                            modelIdList.remove(y);
                            y--;
                            referencesDOList.remove(x);
                            x--;
                            break;
                        }
                    }
                }
            }
            if(referencesDOList !=null && referencesDOList.size()>0){
                referencesDOList.stream().forEach(pictureModelRelevanceDO -> {
                    setAdminMsg(pictureModelRelevanceDO,adminResponse);
                    pictureModelRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                    pictureModelReferencesCmdExe.update(pictureModelRelevanceDO);
                });
            }
        }


    }
}
