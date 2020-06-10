package com.bat.flexible.manager.font;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.font.FontServiceI;
import com.bat.flexible.api.font.dto.FontCmd;
import com.bat.flexible.api.font.dto.FontPageQry;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.font.executor.FontCmdExe;
import com.bat.flexible.manager.font.executor.FontQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.font.co.FontCO;
import com.bat.flexible.dao.font.dataobject.FontDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class FontServiceImpl implements FontServiceI {

    @Autowired
    private FontQryExe fontQryExe;

    @Autowired
    private FontCmdExe fontCmdExe;

    @Override
    @Transactional
    public Response create(FontCmd fontCmd, AdminResponse currentAdmin) {
        FontDO fontDO = BeanUtils.copy(fontCmd,FontDO.class);
        //设置排序号
        Integer maxSequence = fontQryExe.findMaxSequence();
        fontDO.setSequence(maxSequence+1);
        setAdminMsg(fontDO,currentAdmin);
        fontDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        fontCmdExe.create(fontDO);
        //处理redis
        return Response.buildSuccess();
    }

    @Override
    public PageInfo<FontCO> page(FontPageQry fontPageQry) {
        PageHelper.startPage(fontPageQry.getPage(),fontPageQry.getSize());
        List<FontCO> list = fontQryExe.listCOByOpenFlagAndContent(fontPageQry.getOpenFlag(),fontPageQry.getContent());
        return new PageInfo<>(list);
    }

    @Override
    @Transactional
    public Response update(FontCmd fontCmd, AdminResponse currentAdmin) {
        FontDO fontDO = fontQryExe.getById(fontCmd.getId());
        fontDO.setDescription(fontCmd.getDescription());
        fontDO.setName(fontCmd.getName());
        fontDO.setEnglishName(fontCmd.getEnglishName());
        fontDO.setFileName(fontCmd.getFileName());
        fontDO.setFontFile(fontCmd.getFontFile());
        fontDO.setOpenFlag(fontCmd.getOpenFlag());
        setAdminMsg(fontDO,currentAdmin);
        fontCmdExe.update(fontDO);
        return Response.buildSuccess();
    }

    @Override
    public FontCmd detailById(Integer id) {
        FontDO fontDO = fontQryExe.getById(id);
        FontCmd fontCmd = BeanUtils.copy(fontDO,FontCmd.class);
        return fontCmd;
    }

    @Override
    @Transactional
    public Response upOrDown(FlexibleUpOrDownDTO flexibleUpOrDownDTO, AdminResponse currentAdmin) {
        FontDO fontDO = fontQryExe.getById(flexibleUpOrDownDTO.getId());
        FontDO effectiveFont = fontQryExe.getEffect(fontDO.getSequence(),flexibleUpOrDownDTO.getFlag());
        if(effectiveFont==null && flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_UP_TOP_ERROR);
        }
        if(effectiveFont==null && !flexibleUpOrDownDTO.getFlag()){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DOWN_LOWEST_ERROR);
        }
        setAdminMsg(fontDO,currentAdmin);
        setAdminMsg(effectiveFont,currentAdmin);
        Integer temp = fontDO.getSequence();
        fontDO.setSequence(effectiveFont.getSequence());
        effectiveFont.setSequence(temp);
        fontCmdExe.update(fontDO);
        fontCmdExe.update(effectiveFont);
        return Response.buildSuccess();
    }


    @Transactional
    @Override
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        FontDO fontDO = fontQryExe.getById(id);
        setAdminMsg(fontDO,currentAdmin);
        fontDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        fontCmdExe.update(fontDO);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        FontDO fontDO = fontQryExe.getById(flexibleUpdateStatusDTO.getId());
        if(fontDO.getOpenFlag() - flexibleUpdateStatusDTO.getOpenFlag()==0){
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        fontDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(fontDO,currentAdmin);
        fontCmdExe.update(fontDO);
        return Response.buildSuccess();
    }

    @Override
    public List<FontCO> listUsable() {
        return fontQryExe.listCOByOpenFlagAndContent(FlexibleCommonConstant.COMMON_OPEN_FLAG_YES,null);
    }

    private void setAdminMsg(FontDO fontDO, AdminResponse currentAdmin) {
        if(fontDO.getId() ==null){
            fontDO.setCreateTime(new Date());
            fontDO.setCreateUserId(currentAdmin.getId());
            fontDO.setCreateUserName(currentAdmin.getUserName());
        }
        fontDO.setUpdateTime(new Date());
        fontDO.setUpdateUserId(currentAdmin.getId());
        fontDO.setUpdateUserName(currentAdmin.getUserName());
    }


}
