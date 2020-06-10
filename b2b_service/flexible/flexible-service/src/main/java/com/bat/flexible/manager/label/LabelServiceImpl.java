package com.bat.flexible.manager.label;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.base.external.distributor.dto.DistributorSimpleRelaQry;
import com.bat.flexible.api.label.LabelDistributorRelevanceServiceI;
import com.bat.flexible.api.label.LabelServiceI;
import com.bat.flexible.api.label.dto.LabelCmd;
import com.bat.flexible.api.label.dto.LabelDetailQry;
import com.bat.flexible.api.label.dto.LabelPageQry;
import com.bat.flexible.api.picture.PictureLabelRelevanceServiceI;
import com.bat.flexible.api.picture.dto.PictureRelaSimpleDTO;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.label.LabelConstant;
import com.bat.flexible.manager.common.utils.pdf.PDFLabelService;
import com.bat.flexible.manager.label.executor.LabelCmdExe;
import com.bat.flexible.manager.label.executor.LabelQryExe;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.dao.label.co.LabelPageListQry;
import com.bat.flexible.dao.label.dataobject.LabelDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.label.LabelErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelServiceI {

    @Autowired
    private LabelQryExe labelQryExe;

    @Autowired
    private LabelCmdExe labelCmdExe;

    @Autowired
    private LabelDistributorRelevanceServiceI labelDistributorRelevanceServiceI;

    @Autowired
    private PictureLabelRelevanceServiceI pictureLabelRelevanceServiceI;

    /**
     * 根据标签id列表查询标签
     *
     * @param labelIdList
     * @return
     */
    @Override
    public List<LabelDO> listByLabelIdList(List<Integer> labelIdList) {
        if (labelIdList == null || labelIdList.size() == 0) {
            return null;
        }
        return labelQryExe.listByLabelIdList(labelIdList);
    }

    /**
     * 新增标签
     *
     * @param labelCmd
     * @param currentAdmin
     * @return
     */
    @Override
    @Transactional
    public Response create(LabelCmd labelCmd, AdminResponse currentAdmin) {
        //参数校验
        LabelDO labelDO = BeanUtils.copy(labelCmd, LabelDO.class);
        //设置操作人信息
        setAdminMsg(labelDO, currentAdmin);
        labelDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
        labelCmdExe.create(labelDO);
        //设置和分销商的关联关系
        if (labelDO.getScope() - LabelConstant.SCOPE_DISTRIBUTOR_ASSIGN == 0) {
            labelDistributorRelevanceServiceI.saveRela(labelCmd.getDistributorIdList(), labelDO.getId(), currentAdmin, true);
        }
        //标签图片关联
        pictureLabelRelevanceServiceI.saveRela(false, labelCmd.getPictureIdList(), Lists.newArrayList(labelDO.getId()), true, currentAdmin);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response update(LabelCmd labelCmd, AdminResponse currentAdmin) {
        LabelDO labelDO = labelQryExe.getById(labelCmd.getId());
        //参数校验
        checkParam(labelCmd);
        BeanUtils.copyAndCover(labelCmd, labelDO);
        setAdminMsg(labelDO, currentAdmin);
        labelCmdExe.update(labelDO);
        //设置和分销商的关联关系
        if (labelDO.getScope() - LabelConstant.SCOPE_DISTRIBUTOR_ASSIGN == 0) {
            labelDistributorRelevanceServiceI.saveRela(labelCmd.getDistributorIdList(), labelDO.getId(), currentAdmin, false);
        }

        //标签图片关联
        pictureLabelRelevanceServiceI.saveRela(false, labelCmd.getPictureIdList(), Lists.newArrayList(labelDO.getId()), false, currentAdmin);
        return Response.buildSuccess();
    }

    /**
     * 参数校验
     *
     * @param labelCmd
     */
    private void checkParam(LabelCmd labelCmd) {
        //校验定制参数
        checkDiyParam(labelCmd);
        //校验范围
        if (LabelConstant.SCOPE_DISTRIBUTOR_ASSIGN.equals(labelCmd.getScope()) && (labelCmd.getDistributorIdList() == null ||
                labelCmd.getDistributorIdList().size() == 0)) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_SCOPE_ASSIGN_DISTRIBUTOR_ID_LIST_NULL);
        }
    }

    private void checkDiyParam(LabelCmd labelCmd) {
        if (LabelConstant.TYPE_GENERAL.equals(labelCmd.getType())) {
            return;
        }
        //产品名称位置
        if (labelCmd.getProductNamePositionX() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_PRODUCT_NAME_POSITION_X_NULL);
        }
        if (labelCmd.getProductNamePositionY() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_PRODUCT_NAME_POSITION_Y_NULL);
        }
        if (labelCmd.getProductNamePositionWidth() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_PRODUCT_NAME_POSITION_WIDTH_NULL);
        }
        if (labelCmd.getProductNamePositionHeight() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_PRODUCT_NAME_POSITION_HEIGHT_NULL);
        }
        if (labelCmd.getProductNamePositionFont() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_PRODUCT_NAME_POSITION_FONT_NULL);
        }
        if (labelCmd.getProductNamePositionFontSize() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_PRODUCT_NAME_POSITION_FONT_SIZE_NULL);
        }
        //产品型号位置
        if (labelCmd.getModelPositionX() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_MODEL_POSITION_X_NULL);
        }
        if (labelCmd.getModelPositionY() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_MODEL_POSITION_Y_NULL);
        }
        if (labelCmd.getModelPositionWidth() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_MODEL_POSITION_WIDTH_NULL);
        }
        if (labelCmd.getModelPositionHeight() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_MODEL_POSITION_HEIGHT_NULL);
        }
        if (labelCmd.getModelPositionFont() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_MODEL_POSITION_FONT_NULL);
        }
        if (labelCmd.getModelPositionFontSize() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_MODEL_POSITION_FONT_SIZE_NULL);
        }
        //条形码位置
        if (labelCmd.getBarCodePositionX() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_BAR_CODE_POSITION_X_NULL);
        }
        if (labelCmd.getBarCodePositionY() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_BAR_CODE_POSITION_Y_NULL);
        }
        if (labelCmd.getBarCodePositionWidth() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_BAR_CODE_POSITION_WIDTH_NULL);
        }
        if (labelCmd.getBarCodePositionHeight() == null) {
            throw new FlexibleCustomException(LabelErrorCode.LABEL_BAR_CODE_POSITION_HEIGHT_NULL);
        }

    }


    /**
     * 标签详情
     *
     * @param id
     * @return
     */
    @Override
    public LabelDetailQry detailById(Integer id) {

        LabelDO labelDO = labelQryExe.getById(id);
        LabelDetailQry labelDetailQry = BeanUtils.copy(labelDO, LabelDetailQry.class);
        List<PictureRelaSimpleDTO> simpleDTOList = pictureLabelRelevanceServiceI.listPictureRelaByLabelIdAndDelFlag(id);
        labelDetailQry.setPictureRelaList(simpleDTOList);
        List<DistributorSimpleRelaQry> distributorSimpleRelaQryList = labelDistributorRelevanceServiceI.listDTOByCondition(id, null);
        labelDetailQry.setDistributorRelaList(distributorSimpleRelaQryList);
        return labelDetailQry;
    }

    /**
     * 分页查询
     *
     * @param labelPageQry
     * @return
     */
    @Override
    public Response page(LabelPageQry labelPageQry) {
        PageHelper.startPage(labelPageQry.getPage(), labelPageQry.getSize());
        List<LabelPageListQry> list = labelQryExe.listByCondition(labelPageQry.getType(), labelPageQry.getOpenFlag(), labelPageQry.getContent());
        PageInfo pageInfo = new PageInfo(list);
        return Response.of(pageInfo);
    }


    /**
     * 标签启用禁用
     *
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin
     * @return
     */
    @Transactional
    @Override
    public Response upOrDown(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        LabelDO labelDO = labelQryExe.getById(flexibleUpdateStatusDTO.getId());
        if (labelDO.getOpenFlag() - flexibleUpdateStatusDTO.getOpenFlag() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        labelDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(labelDO, currentAdmin);
        labelCmdExe.update(labelDO);
        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response delete(Integer id, AdminResponse currentAdmin) {
        LabelDO labelDO = labelQryExe.getById(id);
        if (FlexibleCommonConstant.COMMON_OPEN_FLAG_YES.equals(labelDO.getOpenFlag())) {

            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DELETE_AFTER_DISABLE_ERROR);
        }
        setAdminMsg(labelDO, currentAdmin);
        labelDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        labelCmdExe.update(labelDO);
        return Response.buildSuccess();
    }

    @Override
    public LabelDO getById(Integer id) {
        return labelQryExe.getById(id);
    }

    @Override
    public String generateBasicLabel(File file, LabelDO label, String fileName) {
        return PDFLabelService.generateBasicLabel(file, label, fileName);
    }

    private void setAdminMsg(LabelDO labelDO, AdminResponse currentAdmin) {
        if (labelDO.getId() == null) {
            labelDO.setCreateTime(new Date());
            labelDO.setCreateUserId(currentAdmin.getId());
            labelDO.setCreateUserName(currentAdmin.getUserName());
        }
        labelDO.setUpdateTime(new Date());
        labelDO.setUpdateUserId(currentAdmin.getId());
        labelDO.setUpdateUserName(currentAdmin.getUserName());
    }

    /**
     * 条件查询图片关联的标签列表
     *
     * @param distributorIds   分销商id列表
     * @param categoryId       产品类型id
     * @param pictureId        图片id
     * @param distributorScope 分销商属于国内还是国外 2、国内 3、国外
     * @return
     */
    @Override
    public List<LabelDO> listDistributorsDiyLableByCondition(List<Integer> distributorIds, Integer categoryId, Integer pictureId, Short distributorScope) {
        return labelQryExe.listDiyLableByCondition(distributorIds, categoryId, pictureId, distributorScope);
    }
}
