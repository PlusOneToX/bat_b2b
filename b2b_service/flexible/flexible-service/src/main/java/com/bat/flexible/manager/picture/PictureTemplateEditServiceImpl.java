package com.bat.flexible.manager.picture;


import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.picture.PictureTemplateEditServiceI;
import com.bat.flexible.manager.picture.executor.PictureTemplateEditCmdExe;
import com.bat.flexible.manager.picture.executor.PictureTemplateEditQryExe;
import com.bat.flexible.dao.picture.dataobject.PictureTemplateEditDO;
import com.bat.flexible.dao.picture.co.PictureTemplateEditCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PictureTemplateEditServiceImpl implements PictureTemplateEditServiceI {

    @Autowired
    private PictureTemplateEditCmdExe pictureTemplateEditCmdExe;

    @Autowired
    private PictureTemplateEditQryExe pictureTemplateEditQryExe;


    @Override
    @Transactional
    public void saveTemplate(Integer pictureId, List<PictureTemplateEditCmd> templateEditList, Short type, Boolean isAdd, AdminResponse adminResponse) {
        //处理编辑的
        dealWithByUpdate(pictureId,isAdd,templateEditList,adminResponse);
        if(templateEditList !=null && templateEditList.size()>0){
            templateEditList.stream().forEach( pictureTemplateEditCmd -> {
                PictureTemplateEditDO edit = new PictureTemplateEditDO();
                edit.setPictureId(pictureId);
                edit.setEditCenterX(pictureTemplateEditCmd.getEditCenterX());
                edit.setEditCenterY(pictureTemplateEditCmd.getEditCenterY());
                edit.setLength(pictureTemplateEditCmd.getLength());
                edit.setWidth(pictureTemplateEditCmd.getWidth());
                edit.setInternalEditUrl(pictureTemplateEditCmd.getInternalEditUrl());
                setAdminMsg(adminResponse, edit);
                pictureTemplateEditCmdExe.create(edit);
            });
        }
    }

    @Override
    public List<PictureTemplateEditCmd> listByPictureId(Integer pictureId) {
        return pictureTemplateEditQryExe.listSimpleByPictureId(pictureId);
    }

    private void dealWithByUpdate(Integer pictureId, Boolean isAdd, List<PictureTemplateEditCmd> templateEditList, AdminResponse adminResponse) {
        if(isAdd){
           return;
        }

        //判断是否修改
        List<PictureTemplateEditDO> editList = pictureTemplateEditQryExe.listPictureId(pictureId);
        if(editList !=null && editList.size()>0){
            for(int x=0;x<editList.size();x++){
                for(int y=0;y<templateEditList.size();y++){
                    if(templateEditList.get(y).getId() !=null &&  (editList.get(x).getId() - templateEditList.get(y).getId() ==0)){
                        PictureTemplateEditDO edit = editList.get(x);
                        PictureTemplateEditCmd pictureTemplateEditCmd = templateEditList.get(y);
                        edit.setEditCenterX(pictureTemplateEditCmd.getEditCenterX());
                        edit.setEditCenterY(pictureTemplateEditCmd.getEditCenterY());
                        edit.setLength(pictureTemplateEditCmd.getLength());
                        edit.setWidth(pictureTemplateEditCmd.getWidth());
                        edit.setInternalEditUrl(pictureTemplateEditCmd.getInternalEditUrl());
                        setAdminMsg(adminResponse, edit);
                        pictureTemplateEditCmdExe.update(edit);
                        editList.remove(x);
                        templateEditList.remove(y);
                        x--;
                        y--;
                        break;
                    }
                }
            }
            //删除
            if(editList !=null && editList.size()>0){
                editList.stream().forEach(pictureTemplateEdit -> {
                    pictureTemplateEditCmdExe.delete(pictureTemplateEdit.getId());
                });
            }
        }
    }

    private void setAdminMsg(AdminResponse adminResponse, PictureTemplateEditDO edit) {
        if(edit.getId()==null){
            edit.setCreateTime(new Date());
            edit.setCreateUserId(adminResponse.getId());
            edit.setCreateUserName(adminResponse.getUserName());
        }
        edit.setUpdateTime(new Date());
        edit.setUpdateUserId(adminResponse.getId());
        edit.setUpdateUserName(adminResponse.getUserName());
    }
}
