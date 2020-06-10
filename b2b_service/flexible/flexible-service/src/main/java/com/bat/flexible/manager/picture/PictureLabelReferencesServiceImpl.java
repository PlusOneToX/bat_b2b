package com.bat.flexible.manager.picture;

import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.label.LabelServiceI;
import com.bat.flexible.api.label.dto.LabelRelaSimpleDTO;
import com.bat.flexible.api.picture.PictureLabelRelevanceServiceI;
import com.bat.flexible.api.picture.PictureServiceI;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.manager.picture.convertor.PictureRelevanceConvertor;
import com.bat.flexible.manager.picture.executor.PictureLabelRelevanceCmdExe;
import com.bat.flexible.manager.picture.executor.PictureLabelRelevanceQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import com.bat.flexible.dao.picture.dataobject.PictureDO;
import com.bat.flexible.dao.picture.dataobject.PictureLabelRelevanceDO;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PictureLabelReferencesServiceImpl implements PictureLabelRelevanceServiceI {

    @Autowired
    private PictureLabelRelevanceQryExe pictureLabelRelevanceQryExe;

    @Autowired
    private PictureLabelRelevanceCmdExe pictureLabelRelevanceCmdExe;

    @Autowired
    private LabelServiceI labelServiceI;

    @Autowired
    private PictureServiceI pictureServiceI;

    /**
     * 报错图片和标签的关联关系
     * @param fromPicture true 来源图片详情 false 来源标签详情
     * @param pictureIdList 图片id
     * @param labelIdList 标签id列表
     * @param isAdd true 新增 false 修改
     * @param adminResponse
     */
    @Transactional
    @Override
    public void saveRela(Boolean fromPicture, List<Integer> pictureIdList, List<Integer> labelIdList, Boolean isAdd, AdminResponse adminResponse) {
        //校验标签id是否传错
        List<LabelDO> labelDOList = labelServiceI.listByLabelIdList(labelIdList);
        //校验图片id是否传错
        List<PictureDO> pictureDOList = pictureServiceI.listByPictureIdList(pictureIdList);
        //处理修改
        dealWithByUpdate(fromPicture,labelIdList, pictureIdList, isAdd, adminResponse);
        if(pictureIdList !=null && pictureIdList.size()>0 && labelIdList !=null && labelIdList.size()>0){
            labelIdList.stream().forEach(labelId -> {
                pictureIdList.stream().forEach( pictureId-> {
                    create(pictureId,labelId,adminResponse);
                });
            });
        }
    }




    @Override
    public List<LabelRelaSimpleDTO> listLabelRelaByPictureIdAndDelFlag(Integer pictureId) {
        List<PictureLabelRelevanceDO> relevanceDOList = pictureLabelRelevanceQryExe.listByLabelIdAndPictureId(null,pictureId);
        List<LabelRelaSimpleDTO> relaSimpleDTOList = new ArrayList<>();
        relevanceDOList.stream().forEach(pictureLabelRelevanceDO -> {
            LabelRelaSimpleDTO simpleDTO = new LabelRelaSimpleDTO();
            simpleDTO.setId(pictureLabelRelevanceDO.getId());
            simpleDTO.setLabelId(pictureLabelRelevanceDO.getLabelId());
            LabelDO labelDO = labelServiceI.getById(pictureLabelRelevanceDO.getLabelId());
            simpleDTO.setName(labelDO.getName());
            simpleDTO.setOpenFlag(labelDO.getOpenFlag());
            simpleDTO.setType(labelDO.getType());
            //设置redis数据
            relaSimpleDTOList.add(simpleDTO);
        });
        return relaSimpleDTOList;
    }

    @Override
    public List<PictureRelaSimpleDTO> listPictureRelaByLabelIdAndDelFlag(Integer labelId) {
        List<PictureLabelRelevanceDO> relevanceDOList = pictureLabelRelevanceQryExe.listByLabelIdAndPictureId(labelId,null);
        /*List<PictureRelaSimpleDTO> relaSimpleDTOList = new ArrayList<>();
        relevanceDOList.stream().forEach(pictureLabelRelevanceDO -> {
            PictureRelaSimpleDTO simpleDTO = new PictureRelaSimpleDTO();
            simpleDTO.setId(pictureLabelRelevanceDO.getId());
            simpleDTO.setPictureId(pictureLabelRelevanceDO.getPictureId());
            //设置redis数据

            relaSimpleDTOList.add(simpleDTO);
        });*/
        return PictureRelevanceConvertor.toPictureRelaSimpleDTOListFromLabel(relevanceDOList);
    }

    /**
     * 删除图片和标签关联关系
     * @param labelId
     * @param pictureId
     * @param currentAdmin
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer labelId, Integer pictureId, AdminResponse currentAdmin) {
        if(labelId ==null && pictureId ==null){
            throw new  FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
        }
        List<PictureLabelRelevanceDO> relevanceDOList = pictureLabelRelevanceQryExe.listByLabelIdAndPictureId(labelId, pictureId);
        if(relevanceDOList ==null || relevanceDOList.size()==0){
            return;
        }
        relevanceDOList.stream().forEach(pictureLabelRelevanceDO -> {
            pictureLabelRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
            setAdminMsg(pictureLabelRelevanceDO,currentAdmin);
        });
        //批量修改
        pictureLabelRelevanceCmdExe.batchUpate(relevanceDOList);
    }

    private void dealWithByUpdate(Boolean fromPicture, List<Integer> labelIdList, List<Integer> pictureIdList, Boolean isAdd, AdminResponse adminResponse) {
        if(isAdd){
           return;
        }
        Integer pictureId = null;
        Integer labelId = null;
        if(fromPicture){
            //来源图片、拿图片id去查询关联关系
            pictureId = pictureIdList.get(0);
        }else{
            labelId = labelIdList.get(0);
        }
        //修改、找出被删的
        List<PictureLabelRelevanceDO> referencesDOList = pictureLabelRelevanceQryExe.listByLabelIdAndPictureId(labelId,pictureId);
        if(referencesDOList !=null && referencesDOList.size()>0){
            for(int x=0;x<referencesDOList.size();x++){
                //处理标签页面、关联的图片id列表
                if(!fromPicture && pictureIdList !=null && pictureIdList.size()>0){
                    //可能出现不关联标签
                    for(int y=0;y<pictureIdList.size();y++){
                        if(referencesDOList.get(x).getPictureId() - pictureIdList.get(y)==0){
                            //找到了、剔除掉
                            referencesDOList.remove(x);
                            x--;
                            pictureIdList.remove(y);
                            y--;
                            break;
                        }
                    }
                }
                //处理图片页面、关联的标签id列表
                if(fromPicture && labelIdList !=null && labelIdList.size()>0){
                    //可能出现不关联标签
                    for(int i=0;i<labelIdList.size();i++){
                        if(referencesDOList.get(x).getPictureId() - labelIdList.get(i)==0){
                            //找到了、剔除掉
                            referencesDOList.remove(x);
                            x--;
                            labelIdList.remove(i);
                            i--;
                            break;
                        }
                    }
                }
            }
        }
        //已删除的
        if(referencesDOList !=null && referencesDOList.size()>0){
            referencesDOList.stream().forEach(pictureLabelReferencesDO -> {
                setAdminMsg(pictureLabelReferencesDO,adminResponse);
                pictureLabelReferencesDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                pictureLabelRelevanceCmdExe.update(pictureLabelReferencesDO);
            });
        }
    }

    private void create(Integer pictureId, Integer labelId, AdminResponse adminResponse) {
        PictureLabelRelevanceDO referencesDO = new PictureLabelRelevanceDO();
        referencesDO.setLabelId(labelId);
        referencesDO.setPictureId(pictureId);
        referencesDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        setAdminMsg(referencesDO,adminResponse);
        pictureLabelRelevanceCmdExe.create(referencesDO);
    }

    private void setAdminMsg(PictureLabelRelevanceDO referencesDO, AdminResponse adminResponse) {
        if(referencesDO.getId() ==null){
            referencesDO.setCreateTime(new Date());
            referencesDO.setCreateUserId(adminResponse.getId());
            referencesDO.setCreateUserName(adminResponse.getUserName());
        }
        referencesDO.setUpdateTime(new Date());
        referencesDO.setUpdateUserId(adminResponse.getId());
        referencesDO.setUpdateUserName(adminResponse.getUserName());
    }

}
