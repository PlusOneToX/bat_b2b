package com.bat.flexible.manager.model;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.bat.flexible.api.base.common.dto.FlexibleUpdateStatusDTO;
import com.bat.flexible.api.base.common.response.AdminResponse;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.model.ModelMaterialRelevanceServiceI;
import com.bat.flexible.api.model.ModelServiceI;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceDTO;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevancePageQry;
import com.bat.flexible.api.model.dto.rela.ModelMaterialRelevanceSkuNoAndBomCmd;
import com.bat.flexible.api.util.BeanUtils;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.manager.common.constant.FlexibleCommonConstant;
import com.bat.flexible.manager.common.constant.excel.ModelExcelConstant;
import com.bat.flexible.manager.common.constant.relevance.ModelMaterialRelevanceConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.api.material.MaterialServiceI;
import com.bat.flexible.dao.material.dataobject.MaterialDO;
import com.bat.flexible.dao.model.co.ModelMaterialRelevanceCO;
import com.bat.flexible.dao.model.co.ModelSkuExportQry;
import com.bat.flexible.dao.model.dataobject.ModelDO;
import com.bat.flexible.dao.model.dataobject.ModelMaterialRelevanceDO;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.model.ModelMaterialRelevanceErrorCode;
import com.bat.flexible.manager.model.executor.ModelMaterialRelevanceCmdExe;
import com.bat.flexible.manager.model.executor.ModelMaterialRelevanceQryExe;

@Service
public class ModelMaterialRelevanceServiceImpl implements ModelMaterialRelevanceServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelMaterialRelevanceServiceImpl.class);

    @Autowired
    private ModelMaterialRelevanceQryExe modelMaterialRelevanceQryExe;

    @Autowired
    private ModelMaterialRelevanceCmdExe modelMaterialRelevanceCmdExe;

    @Autowired
    private ModelServiceI modelServiceI;

    @Autowired
    private MaterialServiceI materialServiceI;

    /**
     * @param fromModel
     *            操作来源 来源型号true 来源材质false
     * @param materialRelevanceDTOList
     * @param currentAdmin
     * @param isAdd
     */
    @Transactional
    @Override
    public void saveModelMaterialRele(Boolean fromModel, List<ModelMaterialRelevanceDTO> materialRelevanceDTOList,
                                      AdminResponse currentAdmin, Boolean isAdd) {
        if (!fromModel && isAdd && (materialRelevanceDTOList == null || materialRelevanceDTOList.size() == 0)) {
            // 材质新增如果不关联型号
            return;
        }
        // 参数校验
        // checkParam(fromModel,materialRelevanceDTOList);
        // 处理修改删除
        Integer modelId = null;
        Integer materialId = null;
        if (materialRelevanceDTOList != null && materialRelevanceDTOList.size() > 0) {
            // id塞在第一个对象
            modelId = materialRelevanceDTOList.get(0).getModelId();
            materialId = materialRelevanceDTOList.get(0).getMaterialId();
        }
        if (fromModel) {
            //
            materialId = null;
        } else {
            // 将型号设置为空、查询当前材质的所有型号列表
            modelId = null;
        }
        List<ModelMaterialRelevanceDO> relevanceDTOList =
            modelMaterialRelevanceQryExe.listByCondition(materialId, modelId, null, null);
        dealWithByUpdate(materialRelevanceDTOList, isAdd, currentAdmin, relevanceDTOList);

        if (materialRelevanceDTOList != null && materialRelevanceDTOList.size() > 0) {
            Map<String, Integer> map = new HashMap<>();
            if (relevanceDTOList != null && relevanceDTOList.size() > 0) {
                relevanceDTOList.stream().forEach(modelMaterialRelevanceDO -> {
                    map.put(modelMaterialRelevanceDO.getMaterialId() + "_" + modelMaterialRelevanceDO.getModelId(), 1);
                });
            }
            materialRelevanceDTOList.stream().forEach(modelMaterialRelevanceDTO -> {
                ModelMaterialRelevanceDO relevanceDO = new ModelMaterialRelevanceDO();
                setParam(modelMaterialRelevanceDTO, currentAdmin, relevanceDO);
                relevanceDO.setMaterialId(modelMaterialRelevanceDTO.getMaterialId());
                relevanceDO.setModelId(modelMaterialRelevanceDTO.getModelId());
                if (map != null && map.size() > 0
                    && map.containsKey(relevanceDO.getMaterialId() + "_" + relevanceDO.getModelId())) {
                    String msg = "";
                    if (fromModel) {
                        // redis取数据
                        MaterialDO materialDO = new MaterialDO();
                        msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                            + materialDO.getMaterialNo();
                        msg +=
                            MessageUtils.get(ModelMaterialRelevanceErrorCode.MATERIAL_HAS_BEEN_ASSOCIATED_MODEL_ERROR);
                    } else {
                        ModelDO modelDO = new ModelDO();
                        msg =
                            MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL) + modelDO.getModelNo();
                        msg +=
                            MessageUtils.get(ModelMaterialRelevanceErrorCode.MODEL_HAS_BEEN_ASSOCIATED_MATERIAL_ERROR);
                    }
                    // 型号和材质关联关系已存在
                    LOGGER.error("材质和型号关联已存在", msg);
                    throw new FlexibleCustomException(msg);
                }
                relevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_NO);
                relevanceDO.setUnderStockFlag(ModelMaterialRelevanceConstant.UNDER_STOCK_FLAG_NO);
                modelMaterialRelevanceCmdExe.create(relevanceDO);
            });
        }
    }

    /* private void checkParam(Boolean fromModel, List<ModelMaterialRelevanceDTO> materialRelevanceDTOList) {
        if(materialRelevanceDTOList !=null && materialRelevanceDTOList.size() >0){
            materialRelevanceDTOList.stream().forEach(modelMaterialRelevanceDTO -> {
                if(fromModel){
                    //来源型号、校验材质
                    MaterialDO materialDO = ma
                }
            });
        }
    }*/

    @Override
    public List<ModelMaterialRelevanceCO> listCOByModelIdAndMaterialId(Integer modelId, Integer materialId) {
        List<ModelMaterialRelevanceDO> relevanceDOList =
            modelMaterialRelevanceQryExe.listByCondition(materialId, modelId, null, null);
        if (relevanceDOList == null || relevanceDOList.size() == 0) {
            return null;
        }
        List<ModelMaterialRelevanceCO> list = new ArrayList<>();
        // 材质所有的型号
        List<MaterialDO> materialDOList = materialServiceI.listAll();
        Map<Integer, MaterialDO> materialDOMap =
            materialDOList.stream().collect(Collectors.toMap(MaterialDO::getId, materialDO -> materialDO));
        List<ModelDO> modelDOList = modelServiceI.listAll();
        Map<Integer, ModelDO> modelDOMap =
            modelDOList.stream().collect(Collectors.toMap(ModelDO::getId, modelDO -> modelDO));
        relevanceDOList.stream().forEach(modelMaterialRelevanceDO -> {
            ModelMaterialRelevanceCO relevanceCO =
                BeanUtils.copy(modelMaterialRelevanceDO, ModelMaterialRelevanceCO.class);
            // redis取数据设置型号名称和材质名称
            MaterialDO materialDO = materialDOMap.get(relevanceCO.getMaterialId());
            relevanceCO.setMaterialName(materialDO.getName());
            ModelDO modelDO = modelDOMap.get(relevanceCO.getModelId());
            if (modelDO != null) {
                relevanceCO.setModelName(modelDO.getName());
                relevanceCO.setModelParentId(modelDO.getParentId());
                list.add(relevanceCO);
            }
        });
        return list;
    }

    @Override
    public PageInfo<ModelMaterialRelevanceCO> page(ModelMaterialRelevancePageQry modelMaterialRelevancePageQry) {
        PageHelper.startPage(modelMaterialRelevancePageQry.getPage(), modelMaterialRelevancePageQry.getSize());
        List<ModelMaterialRelevanceCO> list = modelMaterialRelevanceQryExe.listCOByCondition(
            modelMaterialRelevancePageQry.getProductCategoryId(), modelMaterialRelevancePageQry.getMaterialId(),
            modelMaterialRelevancePageQry.getOpenFlag(), modelMaterialRelevancePageQry.getContent());
        return new PageInfo<>(list);

    }

    @Override
    @Transactional
    public Response updateSkuAndBomCode(ModelMaterialRelevanceSkuNoAndBomCmd relevanceSkuNoAndBomCmd,
                                        AdminResponse currentAdmin) {
        ModelMaterialRelevanceDO modelMaterialRelevanceDO =
            modelMaterialRelevanceQryExe.getById(relevanceSkuNoAndBomCmd.getId());
        modelMaterialRelevanceDO.setThirdSku(relevanceSkuNoAndBomCmd.getThirdSku());
        modelMaterialRelevanceDO.setBomCode(relevanceSkuNoAndBomCmd.getBomCode());
        setAdminMsg(modelMaterialRelevanceDO, currentAdmin);
        modelMaterialRelevanceDO.setUnderStockFlag(null);
        modelMaterialRelevanceCmdExe.update(modelMaterialRelevanceDO);
        // 更新redis

        return Response.buildSuccess();
    }

    /**
     * 启用禁用
     * 
     * @param flexibleUpdateStatusDTO
     * @param currentAdmin
     * @return
     */
    @Override
    public Response updateOpenFlag(FlexibleUpdateStatusDTO flexibleUpdateStatusDTO, AdminResponse currentAdmin) {
        ModelMaterialRelevanceDO relevanceDO = modelMaterialRelevanceQryExe.getById(flexibleUpdateStatusDTO.getId());
        if (relevanceDO.getOpenFlag() - flexibleUpdateStatusDTO.getOpenFlag() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPERATE_REPEAT);
        }
        relevanceDO.setOpenFlag(flexibleUpdateStatusDTO.getOpenFlag());
        setAdminMsg(relevanceDO, currentAdmin);
        relevanceDO.setUnderStockFlag(null);
        modelMaterialRelevanceCmdExe.update(relevanceDO);
        // 更新redis

        return Response.buildSuccess();
    }

    @Override
    @Transactional
    public Response deleteById(Integer id, AdminResponse currentAdmin) {
        ModelMaterialRelevanceDO relevanceDO = modelMaterialRelevanceQryExe.getById(id);
        relevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
        setAdminMsg(relevanceDO, currentAdmin);
        relevanceDO.setUnderStockFlag(null);
        modelMaterialRelevanceCmdExe.update(relevanceDO);
        // 处理redis
        return Response.buildSuccess();
    }

    /**
     * 处理修改时被删除
     * 
     * @param materialRelevanceDTOList
     * @param isAdd
     * @param currentAdmin
     * @param relevanceDTOList
     */
    private void dealWithByUpdate(List<ModelMaterialRelevanceDTO> materialRelevanceDTOList, Boolean isAdd,
        AdminResponse currentAdmin, List<ModelMaterialRelevanceDO> relevanceDTOList) {
        // 新增直接返回
        if (isAdd) {
            return;
        }
        if (relevanceDTOList == null || relevanceDTOList.size() == 0) {
            return;
        }
        if (materialRelevanceDTOList != null && materialRelevanceDTOList.size() > 0) {
            for (int x = 0; x < materialRelevanceDTOList.size(); x++) {
                for (int y = 0; y < relevanceDTOList.size(); y++) {
                    if (materialRelevanceDTOList.get(x).getMaterialId() - relevanceDTOList.get(y).getMaterialId() == 0
                        && materialRelevanceDTOList.get(x).getModelId() - relevanceDTOList.get(y).getModelId() == 0) {
                        // 相同材质和型号
                        ModelMaterialRelevanceDO relevanceDO = relevanceDTOList.get(y);
                        // 设置参数
                        setParam(materialRelevanceDTOList.get(x), currentAdmin, relevanceDO);
                        relevanceDO.setUnderStockFlag(materialRelevanceDTOList.get(x).getUnderStockFlag());
                        modelMaterialRelevanceCmdExe.update(relevanceDO);
                        materialRelevanceDTOList.remove(x);
                        x--;
                        relevanceDTOList.remove(y);
                        y--;
                        break;
                    }
                }
            }
        }
        if (relevanceDTOList != null && relevanceDTOList.size() > 0) {
            relevanceDTOList.stream().forEach(modelMaterialRelevanceDO -> {
                // 删除
                modelMaterialRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
                setAdminMsg(modelMaterialRelevanceDO, currentAdmin);
                modelMaterialRelevanceDO.setUnderStockFlag(null);
                modelMaterialRelevanceCmdExe.update(modelMaterialRelevanceDO);
            });
        }

    }

    private void setParam(ModelMaterialRelevanceDTO materialRelevanceDTO, AdminResponse currentAdmin,
        ModelMaterialRelevanceDO relevanceDO) {
        relevanceDO.setBomCode(materialRelevanceDTO.getBomCode());
        relevanceDO.setLength(materialRelevanceDTO.getLength());
        relevanceDO.setWidth(materialRelevanceDTO.getWidth());
        relevanceDO.setWeight(materialRelevanceDTO.getWeight());
        relevanceDO.setHeight(materialRelevanceDTO.getHeight());
        relevanceDO.setOutImage(materialRelevanceDTO.getOutImage());
        relevanceDO.setFloorImage(materialRelevanceDTO.getFloorImage());
        relevanceDO.setOpenFlag(materialRelevanceDTO.getOpenFlag());
        relevanceDO.setThirdSku(materialRelevanceDTO.getThirdSku());
        relevanceDO.setWarnMsg(materialRelevanceDTO.getWarnMsg());
        relevanceDO.setCutType(materialRelevanceDTO.getCutType());
        relevanceDO.setSlittingHeight(materialRelevanceDTO.getSlittingHeight());
        relevanceDO.setSlittingWhite(materialRelevanceDTO.getSlittingWhite());
        relevanceDO.setCrosscuttingWhite(materialRelevanceDTO.getCrosscuttingWhite());
        relevanceDO.setCrosscuttingWidth(materialRelevanceDTO.getCrosscuttingWidth());
        relevanceDO.setUnderStockFlag(ModelMaterialRelevanceConstant.UNDER_STOCK_FLAG_NO);
        relevanceDO.setTopFrame(materialRelevanceDTO.getTopFrame());
        relevanceDO.setUnderFrame(materialRelevanceDTO.getUnderFrame());
        relevanceDO.setLeftFrame(materialRelevanceDTO.getLeftFrame());
        relevanceDO.setRightFrame(materialRelevanceDTO.getRightFrame());
        setAdminMsg(relevanceDO, currentAdmin);
    }

    private void setAdminMsg(ModelMaterialRelevanceDO relevanceDO, AdminResponse currentAdmin) {
        if (relevanceDO.getId() == null) {
            relevanceDO.setCreateTime(new Date());
            relevanceDO.setCreateUserId(currentAdmin.getId());
            relevanceDO.setCreateUserName(currentAdmin.getUserName());
        }
        relevanceDO.setUpdateTime(new Date());
        relevanceDO.setUpdateUserId(currentAdmin.getId());
        relevanceDO.setUpdateUserName(currentAdmin.getUserName());
    }

    @Override
    public void checkModelMaterialRelevanceList(Boolean fromModel,
        List<ModelMaterialRelevanceDTO> materialRelevanceDTOList, Boolean isAdd, Integer id) {
        if (materialRelevanceDTOList == null || materialRelevanceDTOList.size() == 0) {
            return;
        }
        Integer materialId = materialRelevanceDTOList.get(0).getMaterialId();
        Integer modelId = materialRelevanceDTOList.get(0).getModelId();

        if (!fromModel && !isAdd && materialId != null && materialId - id != 0) {
            LOGGER.info("修改材质、型号材质关联材质id与id不一致：{}", id);
            throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.MODEL_MATERIAL_RELA_MATERIAL_ID_ERROR);
        }
        if (fromModel && !isAdd && modelId != null && modelId - id != 0) {
            LOGGER.info("修改型号、型号材质关联型号id与id不一致：{}", id);
            throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.MODEL_MATERIAL_RELA_MODELL_ID_ERROR);
        }
        materialRelevanceDTOList.stream().forEach(modelMaterialRelevanceDTO -> {
            if (!fromModel && isAdd && modelMaterialRelevanceDTO.getMaterialId() != null) {
                LOGGER.info("新增材质、型号材质关联乱传id：{}", JSON.toJSON(materialRelevanceDTOList));
                throw new FlexibleCustomException(FlexibleCommonErrorCode.FLEXIBLE_ID_MUST_BE_NULL_WHEN_ADD);
            }
            if (!fromModel && !isAdd && modelMaterialRelevanceDTO.getMaterialId() == null) {
                LOGGER.info("编辑材质、型号材质关联乱传id：{}", JSON.toJSON(materialRelevanceDTOList));
                throw new FlexibleCustomException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                    + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL));
            }
            if (!isAdd && !fromModel && modelMaterialRelevanceDTO.getMaterialId() - materialId != 0) {
                LOGGER.info("编辑材质、型号材质关联非同一个材质：{}", JSON.toJSON(materialRelevanceDTOList));
                throw new FlexibleCustomException(
                    ModelMaterialRelevanceErrorCode.MATERIAL_ASSOCIATED_MODEL_MATERIAL_ID_NOT_ONLY_ERROR);
            }
            if (fromModel) {
                // 操作型号详情、判断材质
                if (modelMaterialRelevanceDTO.getMaterialId() == null) {
                    LOGGER.info("材质和型号的关联关系、缺少材质id：{}", JSON.toJSONString(materialRelevanceDTOList));
                    String msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                        + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
                    throw new FlexibleCustomException(msg);
                }
                // 校验材质id
                MaterialDO materialDO = materialServiceI.getById(modelMaterialRelevanceDTO.getMaterialId());
                if (FlexibleCommonConstant.AT_LAST_TRADEMARK_NO.equals(materialDO.getAtLastTrademark())) {
                    LOGGER.info("材质和型号的关联关系、材质非最终材质：{}", JSON.toJSONString(materialDO));
                    String msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                        + materialDO.getName()
                        + MessageUtils.get(ModelMaterialRelevanceErrorCode.M_MATERIAL_MODEL_RELA_FORBID_BY_NOT_LAST);
                    throw new FlexibleCustomException(msg);
                }
            } else {
                if (modelMaterialRelevanceDTO.getModelId() == null) {
                    LOGGER.info("材质和型号的关联关系、缺少型号id：{}", JSON.toJSONString(materialRelevanceDTOList));
                    String msg = MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL)
                        + MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
                    throw new FlexibleCustomException(msg);
                }
                // 校验型号
                ModelDO modelDO = modelServiceI.getById(modelMaterialRelevanceDTO.getModelId());
            }
            if (modelMaterialRelevanceDTO.getLength() == null) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_LENGTH_NULL);
            }
            if (modelMaterialRelevanceDTO.getLength().compareTo(BigDecimal.ZERO) == -1) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_LENGTH_LESS_THEN_ZERO);
            }
            if (modelMaterialRelevanceDTO.getHeight() == null) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_HEIGHT_NULL);
            }
            if (modelMaterialRelevanceDTO.getHeight().compareTo(BigDecimal.ZERO) == -1) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_HEIGHT_LESS_THEN_ZERO);
            }
            if (modelMaterialRelevanceDTO.getWidth() == null) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_WIDTH_NULL);
            }
            if (modelMaterialRelevanceDTO.getWidth().compareTo(BigDecimal.ZERO) == -1) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_WIDTH_LESS_THEN_ZERO);
            }

            if (modelMaterialRelevanceDTO.getWeight() == null) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_WEIGHT_NULL);
            }
            if (modelMaterialRelevanceDTO.getWeight().compareTo(BigDecimal.ZERO) == -1) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_WEIGHT_LESS_THEN_ZERO);
            }

            if (StringUtils.isBlank(modelMaterialRelevanceDTO.getOutImage())) {
                throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.M_OUT_IMAGE_NULL);
            }
            if (StringUtils.isBlank(modelMaterialRelevanceDTO.getFloorImage())) {
                throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.M_FLOOR_IMAGE_NULL);
            }
            if (modelMaterialRelevanceDTO.getOpenFlag() == null) {
                throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_OPEN_FLAG_NULL);
            }
            if (StringUtils.isBlank(modelMaterialRelevanceDTO.getThirdSku())) {
                throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.M_THIRD_SKU_NULL);
            }
            if (StringUtils.isBlank(modelMaterialRelevanceDTO.getBomCode())) {
                throw new FlexibleCustomException(ModelMaterialRelevanceErrorCode.M_BOM_CODE_NULL);
            }
        });
    }

    /**
     * 根据材质、型号id、状态查询
     * 
     * @param materialId
     * @param modelId
     * @param openFlag
     * @param bomCode
     * @return
     */
    @Override
    public List<ModelMaterialRelevanceDO> listByCondition(Integer materialId, Integer modelId, Short openFlag,
        String bomCode) {
        return modelMaterialRelevanceQryExe.listByCondition(materialId, modelId, openFlag, bomCode);
    }

    @Override
    public Response<ModelMaterialRelevanceDO> getByModelIdAndMaterialId(Integer modelId, Integer materialId,
        boolean needUsable) {
        if (materialId == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_MATERIAL_ID_NULL);
        }
        if (modelId == null) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_MODEL_ID_NULL);
        }
        ModelMaterialRelevanceDO relevanceDO =
            modelMaterialRelevanceQryExe.getByModelIdAndMaterialId(modelId, materialId, needUsable);
        return Response.of(relevanceDO);
    }

    @Override
    public List<ModelMaterialRelevanceDO> listByMaterialIdListAndOpenFlag(List<Integer> materialIdList,
        Short openFlag) {
        return modelMaterialRelevanceQryExe.listByMaterialIdListAndOpenFlag(materialIdList, openFlag);
    }

    @Override
    public HSSFWorkbook exportExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<ModelSkuExportQry> list = modelMaterialRelevanceQryExe.listExcelCO();
        if (list == null || list.size() == 0) {
            throw new FlexibleCustomException(FlexibleCommonErrorCode.COMMON_DATA_NULL);
        }
        HSSFSheet hssfSheet = workbook.createSheet("型号长宽高汇总");
        HSSFFont hssfFont = workbook.createFont();
        hssfFont.setColor(IndexedColors.BLACK.getIndex());
        // 设置字体
        hssfFont.setFontName("宋体");
        // 设置字号
        hssfFont.setFontHeightInPoints((short)10);
        // 设置单元格内容水平垂直居中
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 4.设置单元格背景色
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 填充单元格
        titleStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);// 设置单元格背景色
        titleStyle.setFont(hssfFont);

        HSSFRow row = hssfSheet.createRow(0);

        HSSFCell hssfCell = row.createCell(0);

        // 自适应列宽度
        hssfSheet.autoSizeColumn(1, true);
        row.setHeightInPoints(25);
        hssfCell = row.createCell(0);
        hssfCell.setCellValue(ModelExcelConstant.SerialNUMColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(1);
        hssfCell.setCellValue(ModelExcelConstant.ModelNameColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(2);
        hssfCell.setCellValue(ModelExcelConstant.ModelNoColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(3);
        hssfCell.setCellValue(ModelExcelConstant.NetworkModelColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(4);
        hssfCell.setCellValue(ModelExcelConstant.MaterialNameColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(5);
        hssfCell.setCellValue(ModelExcelConstant.LengthColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(6);
        hssfCell.setCellValue(ModelExcelConstant.WidthColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(7);
        hssfCell.setCellValue(ModelExcelConstant.HeightColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(8);
        hssfCell.setCellValue(ModelExcelConstant.ThirdSkuColumn);
        hssfCell.setCellStyle(titleStyle);
        hssfCell = row.createCell(9);
        hssfCell.setCellValue(ModelExcelConstant.BomCodeColumn);
        hssfCell.setCellStyle(titleStyle);

        for (int x = 0; x < list.size(); x++) {
            HSSFRow dataRow = hssfSheet.createRow(x + 1);
            dataRow.createCell(0).setCellValue(x + 1);
            dataRow.createCell(1).setCellValue(list.get(x).getModelName());
            dataRow.createCell(2).setCellValue(list.get(x).getModelNo());
            dataRow.createCell(3).setCellValue(list.get(x).getNetworkModel());
            dataRow.createCell(4).setCellValue(list.get(x).getMaterialName());
            dataRow.createCell(5).setCellValue(String.valueOf(list.get(x).getLength()));
            dataRow.createCell(6).setCellValue(String.valueOf(list.get(x).getWidth()));
            dataRow.createCell(7).setCellValue(String.valueOf(list.get(x).getHeight()));
            dataRow.createCell(8).setCellValue(list.get(x).getThirdSku());
            dataRow.createCell(9).setCellValue(list.get(x).getBomCode());
        }
        workbook.setSheetName(0, "型号资料");

        return workbook;

    }

    /**
     * 删除材质和型号的关联关系
     * 
     * @param materialId
     *            材质id
     * @param modelId
     *            型号id
     * @param currentAdmin
     */
    @Transactional
    @Override
    public void deleteByMaterialIdAndModelId(Integer materialId, Integer modelId, AdminResponse currentAdmin) {
        List<ModelMaterialRelevanceDO> relevanceDOList =
            modelMaterialRelevanceQryExe.listByCondition(materialId, modelId, null, null);
        if (relevanceDOList == null || relevanceDOList.size() == 0) {
            return;
        }
        relevanceDOList.stream().forEach(modelMaterialRelevanceDO -> {
            modelMaterialRelevanceDO.setDelFlag(FlexibleCommonConstant.COMMON_DEL_FLAG_YES);
            setAdminMsg(modelMaterialRelevanceDO, currentAdmin);
            modelMaterialRelevanceDO.setUnderStockFlag(null);
            modelMaterialRelevanceCmdExe.update(modelMaterialRelevanceDO);
        });

    }
}
