package com.bat.flexible.manager.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.picture.PictureMaterialRelevanceServiceI;
import com.bat.flexible.manager.picture.executor.PictureMaterialReferencesCmdExe;
import com.bat.flexible.manager.picture.executor.PictureMaterialReferencesQryExe;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.api.material.dto.MaterialRelaSimpleDTO;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.picture.dataobject.PictureMaterialRelevanceDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PictureMaterialReferencesServiceImpl implements PictureMaterialRelevanceServiceI {

    @Autowired
    private MaterialServiceI materialServiceI;

    @Autowired
    private PictureMaterialReferencesQryExe pictureMaterialReferencesQryExe;

    @Autowired
    private PictureMaterialReferencesCmdExe pictureMaterialReferencesCmdExe;

    /**
     * 保存关联关系
     * @param fromPicture true表示在图片详情处理关联关系 false表示在材质详情页面处理
     * @param pictureIdList 图片id列表
     * @param materialIdList 材质id列表
     * @param adminResponse
     * @param isAdd true表示新增 false表示修改
     */
    @Transactional
    @Override
    public void saveRela(Boolean fromPicture, List<Integer> pictureIdList, List<Integer> materialIdList, AdminResponse adminResponse, Boolean isAdd) {
        //判断id列表是否准确
        List<MaterialDO> materialDOList = materialServiceI.listByMaterialIdList(materialIdList);
        dealWithByUpdate(fromPicture, pictureIdList,materialIdList,adminResponse,isAdd);
        if(materialIdList !=null && materialIdList.size()>0 && pictureIdList !=null && pictureIdList.size()>0){
            materialIdList.stream().forEach(materialId -> {
                pictureIdList.stream().forEach(pictureId -> {
                    PictureMaterialRelevanceDO relevanceDO = new PictureMaterialRelevanceDO();
                    relevanceDO.setMaterialId(materialId);
                    relevanceDO.setPictureId(pictureId);
                    relevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                    setAdminMsg(relevanceDO,adminResponse);
                    pictureMaterialReferencesCmdExe.create(relevanceDO);
                });
            });
        }
    }

    private void setAdminMsg(PictureMaterialRelevanceDO relevanceDO, AdminResponse adminResponse) {
        if(relevanceDO.getId() ==null){
            relevanceDO.setCreateTime(new Date());
            relevanceDO.setCreateUserId(adminResponse.getId());
            relevanceDO.setCreateUserName(adminResponse.getUserName());
        }
        relevanceDO.setUpdateTime(new Date());
        relevanceDO.setUpdateUserId(adminResponse.getId());
        relevanceDO.setUpdateUserName(adminResponse.getUserName());
    }

    /**
     * 处理编辑时关联关系
     * @param fromPicture
     * @param pictureIdList
     * @param materialIdList
     * @param adminResponse
     * @param isAdd
     */
    private void dealWithByUpdate(Boolean fromPicture, List<Integer> pictureIdList, List<Integer> materialIdList, AdminResponse adminResponse, Boolean isAdd) {
        if(isAdd){
            return;
        }
        Integer pictureId=null;
        Integer materialId = null;
        if(fromPicture){
            pictureId = pictureIdList.get(0);
        }else{
            materialId = materialIdList.get(0);
        }
        List<PictureMaterialRelevanceDO> relevanceDOList = pictureMaterialReferencesQryExe.listByPictureIdAndMaterialId(pictureId,materialId);
        if(relevanceDOList !=null && relevanceDOList.size()>0){
            for(int x=0;x<relevanceDOList.size();x++){
                //处理图片页面、关联的材质id列表
                if(fromPicture && materialIdList !=null && materialIdList.size()>0){
                    for(int y=0;y<materialIdList.size();y++){
                        if(relevanceDOList.get(x).getMaterialId() - materialIdList.get(y)==0){
                            //材质没有改变
                            relevanceDOList.remove(x);
                            x--;
                            materialIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
                //处理材质页面、关联的图片id列表
                if(!fromPicture && pictureIdList !=null && pictureIdList.size()>0){
                    for(int i=0;i<pictureIdList.size();i++){
                        if(relevanceDOList.get(x).getPictureId() - pictureIdList.get(i)==0){
                            //图片没有改变
                            relevanceDOList.remove(x);
                            x--;
                            pictureIdList.remove(i);
                            i--;
                            break;
                        }
                    }
                }
            }
            if(relevanceDOList !=null && relevanceDOList.size()>0){
                //已被移除
                relevanceDOList.stream().forEach(pictureMaterialRelevanceDO -> {
                    pictureMaterialRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                    setAdminMsg(pictureMaterialRelevanceDO,adminResponse);
                    pictureMaterialReferencesCmdExe.update(pictureMaterialRelevanceDO);
                });
            }
        }
    }

    @Override
    public List<MaterialRelaSimpleDTO> listSimpleByPictureIdAndDelFlag(Integer pictureId, Short delFlag) {
        List<PictureMaterialRelevanceDO> relevanceDOList = pictureMaterialReferencesQryExe.listByPictureIdAndDelFlag(pictureId,delFlag);
        if(relevanceDOList ==null || relevanceDOList.size()==0){
            return null;
        }
        List<MaterialRelaSimpleDTO> simpleDTOList = new ArrayList<>();
        relevanceDOList.stream().forEach(pictureMaterialRelevanceDO -> {
            MaterialRelaSimpleDTO dto = new MaterialRelaSimpleDTO();
            dto.setId(pictureMaterialRelevanceDO.getId());
            dto.setMaterialId(pictureMaterialRelevanceDO.getMaterialId());
            //redis取材质数据
            MaterialDO materialDO = materialServiceI.getById(pictureMaterialRelevanceDO.getMaterialId());
            dto.setName(materialDO.getName());
            dto.setEnglishName(materialDO.getEnglishName());
            simpleDTOList.add(dto);
        });
        return simpleDTOList;
    }

    @Override
    public List<PictureMaterialRelevanceDO> listByPictureIdAndMaterialId(Integer pictureId,Integer materialId) {
        return pictureMaterialReferencesQryExe.listByPictureIdAndMaterialId(pictureId,materialId);
    }

    /**
     * 删除关联关系
     * @param materialId
     * @param pictureId
     * @param currentAdmin
     */
    @Override
    public void delete(Integer materialId, Integer pictureId, AdminResponse currentAdmin) {
        List<PictureMaterialRelevanceDO> updateList = pictureMaterialReferencesQryExe.listByPictureIdAndMaterialId(pictureId,materialId);
        if(updateList ==null || updateList.size()==0){
            return;
        }
        updateList.stream().forEach(pictureMaterialRelevanceDO -> {
            pictureMaterialRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
            setAdminMsg(pictureMaterialRelevanceDO,currentAdmin);

        });
        pictureMaterialReferencesCmdExe.batchUpdate(updateList);
    }
}
