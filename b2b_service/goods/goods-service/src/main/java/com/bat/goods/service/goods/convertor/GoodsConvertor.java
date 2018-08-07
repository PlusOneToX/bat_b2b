package com.bat.goods.service.goods.convertor;

import static com.bat.goods.service.goods.executor.ErrorCode.B_GOODS_DISTRIBUTOR_SCOPE_ERROR;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.manager.mq.dto.GoodsSaleDTO;
import com.bat.goods.service.common.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.distributor.distributor.dto.data.DistributorGroupRpcDTO;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.dubboapi.goods.goods.dto.GoodsStoreColumnRpcCmd;
import com.bat.dubboapi.goods.goods.dto.GoodsStoreMobileRpcCmd;
import com.bat.dubboapi.goods.goods.dto.GoodsStoreSectionRpcCmd;
import com.bat.dubboapi.goods.goods.dto.data.*;
import com.bat.dubboapi.goods.stock.dto.GoodsStockFlagDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.ErpPriceFieldListRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.ErpPriceFieldRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemBoxRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemErpRpcDTO;
import com.bat.goods.api.base.GoodsException;
import com.bat.goods.api.goods.dto.*;
import com.bat.goods.api.goods.dto.data.*;
import com.bat.goods.api.user.dto.data.UserGoodsItemPriceDTO;
import com.bat.goods.api.utils.MessageUtils;
import com.bat.goods.dao.common.CommonUtils;
import com.bat.goods.dao.goods.dataobject.*;
import com.bat.goods.dao.scaleprice.dataobject.ScalePriceDO;
import com.bat.goods.dao.stock.dataobject.GoodsStockFlagDO;

public class GoodsConvertor {

    public static GoodsDO toGoodsDo(GoodsCmd cmd, Short operationType) {
        GoodsDO goodsDO = new GoodsDO();
        BeanUtils.copyProperties(cmd, goodsDO);
        Date date = new Date(System.currentTimeMillis());
        if (operationType.equals(Constant.OPERATION_TYPE_1)) {
            goodsDO.setId(null);
            String goodsNo = "G" + Long.toHexString(date.getTime()).toUpperCase();
            goodsDO.setGoodsNo(goodsNo);
        }
        if (cmd.getSaleStatus().equals(Constant.SALE_STATUS_3)) {
            goodsDO.setSaleTime(date);
        }
        goodsDO.setFreezeStatus(Constant.FREEZE_STATUS_1);
        goodsDO.setCreateTime(date);
        goodsDO.setUpdateTime(date);
        return goodsDO;
    }

    public static GoodsItemDO toGoodsItemDO(Integer goodsId, GoodsItemCmd cmd) {
        GoodsItemDO goodsItemDO = new GoodsItemDO();
        BeanUtils.copyProperties(cmd, goodsItemDO);
        goodsItemDO.setGoodsId(goodsId);
        Date date = new Date(System.currentTimeMillis());
        if (cmd.getSaleStatus().equals(Constant.SALE_STATUS_3)) {
            goodsItemDO.setSaleTime(date);
        }
        goodsItemDO.setCreateTime(date);
        goodsItemDO.setUpdateTime(date);
        return goodsItemDO;
    }

    public static List<GoodsClassifyRelevanceDO> toGoodsClassifyRelevanceDOList(Integer goodsId,
        List<Integer> classifyIds) {
        List<GoodsClassifyRelevanceDO> classifyRelevanceDOS = new ArrayList<>();
        classifyIds.forEach(classifyId -> {
            GoodsClassifyRelevanceDO classifyRelevanceDO = new GoodsClassifyRelevanceDO();
            classifyRelevanceDO.setClassifyId(classifyId);
            classifyRelevanceDO.setGoodsId(goodsId);
            classifyRelevanceDOS.add(classifyRelevanceDO);
        });
        return classifyRelevanceDOS;
    }

    public static List<GoodsTagRelevanceDO> toGoodsTagRelevanceDOList(Integer goodsId, List<Integer> tagIds) {
        List<GoodsTagRelevanceDO> tagRelevanceDOS = new ArrayList<>();
        tagIds.forEach(tagId -> {
            GoodsTagRelevanceDO tagRelevanceDO = new GoodsTagRelevanceDO();
            tagRelevanceDO.setTagId(tagId);
            tagRelevanceDO.setGoodsId(goodsId);
            tagRelevanceDOS.add(tagRelevanceDO);
        });
        return tagRelevanceDOS;
    }

    public static List<GoodsParamRelevanceDO> toGoodsParamRelevanceDOList(Integer goodsId, List<Integer> paramIds) {
        List<GoodsParamRelevanceDO> paramRelevanceDOS = new ArrayList<>();
        paramIds.forEach(paramId -> {
            GoodsParamRelevanceDO paramRelevanceDO = new GoodsParamRelevanceDO();
            paramRelevanceDO.setParamId(paramId);
            paramRelevanceDO.setGoodsId(goodsId);
            paramRelevanceDOS.add(paramRelevanceDO);
        });
        return paramRelevanceDOS;
    }

    public static List<GoodsItemBoxDO> toGoodsItemBoxDOList(Integer goodsItemId, List<GoodsItemBoxCmd> cmds) {
        List<GoodsItemBoxDO> goodsItemBoxDOS = new ArrayList<>();
        cmds.forEach(cmd -> {
            GoodsItemBoxDO goodsItemBoxDO = new GoodsItemBoxDO();
            BeanUtils.copyProperties(cmd, goodsItemBoxDO);
            goodsItemBoxDO.setGoodsItemId(goodsItemId);
            Date date = new Date(System.currentTimeMillis());
            goodsItemBoxDO.setId(null);
            goodsItemBoxDO.setCreateTime(date);
            goodsItemBoxDO.setUpdateTime(date);
            goodsItemBoxDOS.add(goodsItemBoxDO);
        });
        return goodsItemBoxDOS;
    }

    public static List<GoodsItemBoxDO> toGoodsItemBoxDOList(List<GoodsItemBoxCmd> cmds) {
        List<GoodsItemBoxDO> goodsItemBoxDOS = new ArrayList<>();
        cmds.forEach(cmd -> {
            GoodsItemBoxDO goodsItemBoxDO = new GoodsItemBoxDO();
            BeanUtils.copyProperties(cmd, goodsItemBoxDO);
            Date date = new Date(System.currentTimeMillis());
            goodsItemBoxDO.setUpdateTime(date);
            goodsItemBoxDOS.add(goodsItemBoxDO);
        });
        return goodsItemBoxDOS;
    }

    public static GoodsItemDataDO toGoodsItemDataDO(Integer goodsItemId, GoodsItemDataCmd cmd) {
        GoodsItemDataDO goodsItemDataDO = new GoodsItemDataDO();
        BeanUtils.copyProperties(cmd, goodsItemDataDO);
        goodsItemDataDO.setId(null);
        goodsItemDataDO.setGoodsItemId(goodsItemId);
        return goodsItemDataDO;
    }

    public static GoodsItemDataDO toGoodsItemDataDO(GoodsItemDataCmd cmd) {
        GoodsItemDataDO goodsItemDataDO = new GoodsItemDataDO();
        BeanUtils.copyProperties(cmd, goodsItemDataDO);
        return goodsItemDataDO;
    }

    public static List<GoodsItemSpecsColorDO> toGoodsItemSpecsColorDOList(Integer goodsItemId, Integer specsId,
                                                                          Integer colorId) {
        List<GoodsItemSpecsColorDO> specsColorDOS = new ArrayList<>();
        if (specsId != null) {
            GoodsItemSpecsColorDO specsColorDO = new GoodsItemSpecsColorDO();
            specsColorDO.setId(null);
            specsColorDO.setGoodsItemId(goodsItemId);
            specsColorDO.setAttributeValueId(specsId);
            specsColorDO.setAttributeType(Constant.ATTRIBUTE_TYPE_1);
            specsColorDOS.add(specsColorDO);
        }
        if (colorId != null) {
            GoodsItemSpecsColorDO specsColorDO = new GoodsItemSpecsColorDO();
            specsColorDO.setId(null);
            specsColorDO.setGoodsItemId(goodsItemId);
            specsColorDO.setAttributeValueId(colorId);
            specsColorDO.setAttributeType(Constant.ATTRIBUTE_TYPE_2);
            specsColorDOS.add(specsColorDO);
        }
        return specsColorDOS;
    }

    public static List<GoodsItemScalePriceDO> toGoodsItemScalePriceDOList(Integer goodsId, Integer goodsItemId,
                                                                          List<GoodsItemScalePriceCmd> cmds) {
        List<GoodsItemScalePriceDO> scalePriceDOS = new ArrayList<>();
        cmds.forEach(cmd -> {
            GoodsItemScalePriceDO scalePriceDO = new GoodsItemScalePriceDO();
            BeanUtils.copyProperties(cmd, scalePriceDO);
            scalePriceDO.setGoodsId(goodsId);
            scalePriceDO.setId(null);
            scalePriceDO.setGoodsItemId(goodsItemId);
            scalePriceDOS.add(scalePriceDO);
        });
        return scalePriceDOS;
    }

    public static List<GoodsDTO> toGoodsDTOList(List<GoodsDO> doList) {
        List<GoodsDTO> dtoList = new ArrayList<>();
        doList.forEach(item -> {
            GoodsDTO dto = new GoodsDTO();
            BeanUtils.copyProperties(item, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    public static List<GoodsListDTO> toGoodsListDTOList(List<GoodsListDO> doList) {
        List<GoodsListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(item -> {
                GoodsListDTO dto = new GoodsListDTO();
                BeanUtils.copyProperties(item, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    public static List<GoodsListStoreDTO> toGoodsListStoreDTOList(List<GoodsListStoreDO> doList) {
        List<GoodsListStoreDTO> dtoList = new ArrayList<>();
        doList.forEach(item -> {
            GoodsListStoreDTO dto = new GoodsListStoreDTO();
            BeanUtils.copyProperties(item, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    public static GoodsDTO toGoodsDTO(GoodsDO goodsDO) {
        GoodsDTO dto = new GoodsDTO();
        BeanUtils.copyProperties(goodsDO, dto);
        return dto;
    }

    public static List<GoodsItemDTO> toGoodsItemDTOList(List<GoodsItemDO> doList) {
        List<GoodsItemDTO> dtoList = new ArrayList<>();
        doList.forEach(item -> {
            GoodsItemDTO dto = new GoodsItemDTO();
            BeanUtils.copyProperties(item, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    public static List<GoodsItemListDTO> toGoodsItemListDTOList(List<GoodsItemListDO> doList,
                                                                List<GoodsItemSpecsColorListDO> specsColorListDOS, List<MaterialDTORpcQry> materialDTORpcQryList) {
        List<GoodsItemListDTO> dtoList = new ArrayList<>();
        Map<Integer, List<GoodsItemSpecsColorListDO>> specsColorListMap =
            specsColorListDOS.stream().collect(Collectors.groupingBy(GoodsItemSpecsColorListDO::getGoodsItemId));
        Map<Integer, MaterialDTORpcQry> materialDTORpcQryMap = null;
        if (materialDTORpcQryList != null && materialDTORpcQryList.size() > 0) {
            materialDTORpcQryMap = materialDTORpcQryList.stream()
                .collect(Collectors.toMap(MaterialDTORpcQry::getItemId, materialDTORpcQry -> materialDTORpcQry));
        }
        Map<Integer, MaterialDTORpcQry> finalMaterialDTORpcQryMap = materialDTORpcQryMap;
        doList.forEach(item -> {
            GoodsItemListDTO dto = new GoodsItemListDTO();
            BeanUtils.copyProperties(item, dto);
            if (!CollectionUtils.isEmpty(specsColorListMap)) {
                List<GoodsItemSpecsColorListDO> specsColorList = specsColorListMap.get(dto.getId());
                if (!CollectionUtils.isEmpty(specsColorList)) {
                    specsColorList.forEach(specsColor -> {
                        if (specsColor.getAttributeType().equals(Constant.ATTRIBUTE_TYPE_1)) {
                            dto.setSpecsId(specsColor.getAttributeValueId());
                            dto.setSpecsName(specsColor.getAttributeValueName());
                        } else if (specsColor.getAttributeType().equals(Constant.ATTRIBUTE_TYPE_2)) {
                            dto.setColorId(specsColor.getAttributeValueId());
                            dto.setColorName(specsColor.getAttributeValueName());
                        }
                    });
                }
            }
            if (!CollectionUtils.isEmpty(finalMaterialDTORpcQryMap)) {
                MaterialDTORpcQry materialDTORpcQry = finalMaterialDTORpcQryMap.get(dto.getId());
                if (materialDTORpcQry != null) {
                    dto.setMaterialName(materialDTORpcQry.getName());
                }
            }
            dtoList.add(dto);
        });
        return dtoList;
    }

    public static GoodsItemBoxDTO toGoodsItemBoxDTO(GoodsItemBoxDO goodsItemBoxDO) {
        GoodsItemBoxDTO dto = new GoodsItemBoxDTO();
        BeanUtils.copyProperties(goodsItemBoxDO, dto);
        return dto;
    }

    public static List<GoodsItemBoxDTO> toGoodsItemBoxDTOList(List<GoodsItemBoxDO> goodsItemBoxDOS) {
        List<GoodsItemBoxDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItemBoxDOS)) {
            goodsItemBoxDOS.forEach(goodsItemBoxDO -> {
                GoodsItemBoxDTO dto = new GoodsItemBoxDTO();
                BeanUtils.copyProperties(goodsItemBoxDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GoodsItemDataDTO> toGoodsItemDataDTOList(List<GoodsItemDataDO> doList) {
        List<GoodsItemDataDTO> dtoList = new ArrayList<>();
        doList.forEach(item -> {
            GoodsItemDataDTO dto = new GoodsItemDataDTO();
            BeanUtils.copyProperties(item, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    public static GoodsItemScalePriceDTO toGoodsItemScalePriceDTO(GoodsItemScalePriceDO scalePriceDO) {
        GoodsItemScalePriceDTO dto = new GoodsItemScalePriceDTO();
        BeanUtils.copyProperties(scalePriceDO, dto);
        return dto;
    }

    /**
     * 获取商品可视关系列表
     * 
     * @param goodsId
     * @param cmd
     * @return
     */
    public static List toGoodsRelevance(Integer goodsId, GoodsCmd cmd, Short brandDistributorScope,
        List<Integer> brandDistributorIds) {
        List<Integer> scalePriceIds = cmd.getScalePriceIds();
        List<Integer> distributorIds = cmd.getDistributorIds();
        List<Integer> departmentIds = cmd.getDepartmentIds();
        List<Integer> adminIds = cmd.getAdminIds();
        List<Integer> distributorGroupIds = cmd.getDistributorGroupIds();
        if (cmd.getDistributorScope().equals(Constant.SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceIds)) {
            List<GoodsScalePriceRelevanceDO> scalePriceRelevanceDOS = new ArrayList<>();
            scalePriceIds.forEach(id -> {
                GoodsScalePriceRelevanceDO scalePriceRelevanceDO = new GoodsScalePriceRelevanceDO();
                scalePriceRelevanceDO.setGoodsId(goodsId);
                scalePriceRelevanceDO.setScalePriceId(id);
                scalePriceRelevanceDOS.add(scalePriceRelevanceDO);
            });
            return scalePriceRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(distributorIds)) {
            List<GoodsDistributorRelevanceDO> distributorRelevanceDOS = new ArrayList<>();
            List<Integer> errorDistributorIds = new ArrayList<>();
            distributorIds.forEach(id -> {
                if (!brandDistributorScope.equals(Constant.SCOPE_ALL)) {
                    if (!CollectionUtils.isEmpty(brandDistributorIds) && !brandDistributorIds.contains(id)) {
                        errorDistributorIds.add(id);
                    } else if (CollectionUtils.isEmpty(brandDistributorIds)) {
                        throw GoodsException.buildException(B_GOODS_DISTRIBUTOR_SCOPE_ERROR,
                            MessageUtils.get("B_GOODS_DISTRIBUTOR_SCOPE_ERROR3"));
                    }
                }
                GoodsDistributorRelevanceDO distributorRelevanceDO = new GoodsDistributorRelevanceDO();
                distributorRelevanceDO.setGoodsId(goodsId);
                distributorRelevanceDO.setDistributorId(id);
                distributorRelevanceDOS.add(distributorRelevanceDO);
            });
            if (!CollectionUtils.isEmpty(errorDistributorIds)) {
                String join = String.join(",",
                    errorDistributorIds.stream().map(x -> String.valueOf(x)).collect(Collectors.toList()));
                throw GoodsException.buildException(B_GOODS_DISTRIBUTOR_SCOPE_ERROR,
                    MessageUtils.get("B_GOODS_DISTRIBUTOR_SCOPE_ERROR1") + "\"" + join + "\""
                        + MessageUtils.get("B_GOODS_DISTRIBUTOR_SCOPE_ERROR2"));
            }
            return distributorRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentIds)) {
            List<GoodsDepartmentRelevanceDO> departmentRelevanceDOS = new ArrayList<>();
            departmentIds.forEach(id -> {
                GoodsDepartmentRelevanceDO departmentRelevanceDO = new GoodsDepartmentRelevanceDO();
                departmentRelevanceDO.setGoodsId(goodsId);
                departmentRelevanceDO.setDepartmentId(id);
                departmentRelevanceDOS.add(departmentRelevanceDO);
            });
            return departmentRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminIds)) {
            List<GoodsAdminRelevanceDO> adminRelevanceDOS = new ArrayList<>();
            adminIds.forEach(id -> {
                GoodsAdminRelevanceDO adminRelevanceDO = new GoodsAdminRelevanceDO();
                adminRelevanceDO.setGoodsId(goodsId);
                adminRelevanceDO.setAdminId(id);
                adminRelevanceDOS.add(adminRelevanceDO);
            });
            return adminRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(Constant.SCOPE_DISTRIBUTOR_GROUP)
            && !CollectionUtils.isEmpty(distributorGroupIds)) {
            List<GoodsDistributorGroupRelevanceDO> distributorGroupRelevanceDOS = new ArrayList<>();
            distributorGroupIds.forEach(id -> {
                GoodsDistributorGroupRelevanceDO distributorGroupRelevanceDO = new GoodsDistributorGroupRelevanceDO();
                distributorGroupRelevanceDO.setGoodsId(goodsId);
                distributorGroupRelevanceDO.setDistributorGroupId(id);
                distributorGroupRelevanceDOS.add(distributorGroupRelevanceDO);
            });
            return distributorGroupRelevanceDOS;
        } else {
            return null;
        }
    }

    /**
     * 获取商品不可视关系列表
     * 
     * @param goodsId
     * @param cmd
     * @return
     */
    public static List toGoodsRelevanceNO(Integer goodsId, GoodsCmd cmd) {
        List<Integer> scalePriceNoIds = cmd.getScalePriceNoIds();
        List<Integer> distributorNoIds = cmd.getDistributorNoIds();
        List<Integer> departmentNoIds = cmd.getDepartmentNoIds();
        List<Integer> adminNoIds = cmd.getAdminNoIds();
        if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceNoIds)) {
            List<GoodsScalePriceRelevanceNoDO> scalePriceRelevanceNoDOS = new ArrayList<>();
            scalePriceNoIds.forEach(id -> {
                GoodsScalePriceRelevanceNoDO scalePriceRelevanceNoDO = new GoodsScalePriceRelevanceNoDO();
                scalePriceRelevanceNoDO.setGoodsId(goodsId);
                scalePriceRelevanceNoDO.setScalePriceId(id);
                scalePriceRelevanceNoDOS.add(scalePriceRelevanceNoDO);
            });
            return scalePriceRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_DISTRIBUTOR)
            && !CollectionUtils.isEmpty(distributorNoIds)) {
            List<GoodsDistributorRelevanceNoDO> distributorRelevanceNoDOS = new ArrayList<>();
            distributorNoIds.forEach(id -> {
                GoodsDistributorRelevanceNoDO distributorRelevanceNoDO = new GoodsDistributorRelevanceNoDO();
                distributorRelevanceNoDO.setGoodsId(goodsId);
                distributorRelevanceNoDO.setDistributorId(id);
                distributorRelevanceNoDOS.add(distributorRelevanceNoDO);
            });
            return distributorRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentNoIds)) {
            List<GoodsDepartmentRelevanceNoDO> departmentRelevanceNoDOS = new ArrayList<>();
            departmentNoIds.forEach(id -> {
                GoodsDepartmentRelevanceNoDO departmentRelevanceNoDO = new GoodsDepartmentRelevanceNoDO();
                departmentRelevanceNoDO.setGoodsId(goodsId);
                departmentRelevanceNoDO.setDepartmentId(id);
                departmentRelevanceNoDOS.add(departmentRelevanceNoDO);
            });
            return departmentRelevanceNoDOS;
        } else if (cmd.getDistributorScopeNo().equals(Constant.SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminNoIds)) {
            List<GoodsAdminRelevanceNoDO> adminRelevanceNoDOS = new ArrayList<>();
            adminNoIds.forEach(id -> {
                GoodsAdminRelevanceNoDO adminRelevanceNoDO = new GoodsAdminRelevanceNoDO();
                adminRelevanceNoDO.setGoodsId(goodsId);
                adminRelevanceNoDO.setAdminId(id);
                adminRelevanceNoDOS.add(adminRelevanceNoDO);
            });
            return adminRelevanceNoDOS;
        } else {
            return null;
        }
    }

    public static List<GoodsItemErpDTO> toGoodsItemErpDTO(List<GoodsItemErpRpcDTO> rpcDTOList,
                                                          List<UserGoodsNameDO> goodsNameDOS) {
        List<GoodsItemErpDTO> erpDTOList = new ArrayList<>();
        rpcDTOList.forEach(rpcDTO -> {
            GoodsItemErpDTO erpDTO = new GoodsItemErpDTO();
            BeanUtils.copyProperties(rpcDTO, erpDTO);
            List<ErpPriceFieldRpcDTO> priceList = rpcDTO.getPriceList();
            List<GoodsItemErpPriceDTO> priceDTOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(priceList)) {
                priceList.forEach(price -> {
                    GoodsItemErpPriceDTO priceDTO = new GoodsItemErpPriceDTO();
                    BeanUtils.copyProperties(price, priceDTO);
                    priceDTOS.add(priceDTO);
                });
            }
            erpDTO.setScalePrices(priceDTOS);
            List<GoodsItemBoxRpcDTO> boxRpcDTOList = rpcDTO.getBoxInfoList();
            if (!CollectionUtils.isEmpty(boxRpcDTOList)) {
                List<GoodsItemBoxErpDTO> boxInfos = new ArrayList<>();
                boxRpcDTOList.forEach(boxRpcDTO -> {
                    GoodsItemBoxErpDTO boxErpDTO = new GoodsItemBoxErpDTO();
                    BeanUtils.copyProperties(boxRpcDTO, boxErpDTO);
                    boxInfos.add(boxErpDTO);
                });
                erpDTO.setBoxInfos(boxInfos);
            }
            if (!CollectionUtils.isEmpty(goodsNameDOS)) {
                Optional<UserGoodsNameDO> optional = goodsNameDOS.stream()
                    .filter(goodsNameDO -> goodsNameDO.getItemCode().equals(rpcDTO.getItemCode())).findFirst();
                if (optional != null && optional.isPresent()) {
                    erpDTO.setGoodsId(optional.get().getId());
                    erpDTO.setGoodsName(optional.get().getGoodsName());
                }
            }
            erpDTOList.add(erpDTO);
        });
        return erpDTOList;
    }

    public static List<UserGoodsRpcDTO> toUserGoodsRpcDTOList(List goodsDOS) {
        List<UserGoodsRpcDTO> rpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsDOS)) {
            goodsDOS.forEach(goodsDO -> {
                UserGoodsRpcDTO dto = new UserGoodsRpcDTO();
                BeanUtils.copyProperties(goodsDO, dto);
                rpcDTOS.add(dto);
            });
        }
        return rpcDTOS;
    }

    public static List<GoodsStoreColumnDO> toGoodsStoreColumnDOList(List<GoodsStoreColumnRpcCmd> cmds) {
        List<GoodsStoreColumnDO> storeColumnDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                GoodsStoreColumnDO storeColumnDO = new GoodsStoreColumnDO();
                BeanUtils.copyProperties(cmd, storeColumnDO);
                storeColumnDOS.add(storeColumnDO);
            });
        }
        return storeColumnDOS;
    }

    public static List<GoodsStoreSectionDO> toGoodsStoreSectionDOList(List<GoodsStoreSectionRpcCmd> cmds) {
        List<GoodsStoreSectionDO> storeSectionDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                GoodsStoreSectionDO storeSectionDO = new GoodsStoreSectionDO();
                BeanUtils.copyProperties(cmd, storeSectionDO);
                storeSectionDOS.add(storeSectionDO);
            });
        }
        return storeSectionDOS;
    }

    public static List<GoodsStoreMobileDO> toGoodsStoreMobileDOList(List<GoodsStoreMobileRpcCmd> cmds) {
        List<GoodsStoreMobileDO> storeMobileDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                GoodsStoreMobileDO storeMobileDO = new GoodsStoreMobileDO();
                BeanUtils.copyProperties(cmd, storeMobileDO);
                storeMobileDOS.add(storeMobileDO);
            });
        }
        return storeMobileDOS;
    }

    public static List<GoodsStoreColumnRpcDTO> toGoodsStoreColumnRpcDTOList(List<GoodsStoreColumnDO> storeColumnDOS) {
        List<GoodsStoreColumnRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(storeColumnDOS)) {
            storeColumnDOS.forEach(storeColumnDO -> {
                GoodsStoreColumnRpcDTO dto = new GoodsStoreColumnRpcDTO();
                BeanUtils.copyProperties(storeColumnDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GoodsStoreSectionRpcDTO>
        toGoodsStoreSectionRpcDTOList(List<GoodsStoreSectionDO> storeSectionDOS) {
        List<GoodsStoreSectionRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(storeSectionDOS)) {
            storeSectionDOS.forEach(storeSectionDO -> {
                GoodsStoreSectionRpcDTO dto = new GoodsStoreSectionRpcDTO();
                BeanUtils.copyProperties(storeSectionDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GoodsStoreMobileRpcDTO> toGoodsStoreMobileRpcDTOList(List<GoodsStoreMobileDO> storeMobileDOS) {
        List<GoodsStoreMobileRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(storeMobileDOS)) {
            storeMobileDOS.forEach(storeMobileDO -> {
                GoodsStoreMobileRpcDTO dto = new GoodsStoreMobileRpcDTO();
                BeanUtils.copyProperties(storeMobileDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GoodsItemRpcDTO> toGoodsItemRpcDTOList(List<GoodsItemListDO> goodsItemListDOS,
        List<GoodsItemSpecsColorListDO> specsColorListDOS) {
        List<GoodsItemRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsItemListDOS)) {
            goodsItemListDOS.forEach(goodsItemListDO -> {
                GoodsItemRpcDTO dto = toGoodsItemRpcDTO(goodsItemListDO, specsColorListDOS);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static GoodsItemRpcDTO toGoodsItemRpcDTO(GoodsItemListDO goodsItemListDO,
        List<GoodsItemSpecsColorListDO> specsColorListDOS) {
        GoodsItemRpcDTO dto = new GoodsItemRpcDTO();
        BeanUtils.copyProperties(goodsItemListDO, dto);
        if (!CollectionUtils.isEmpty(specsColorListDOS)) {
            Optional<GoodsItemSpecsColorListDO> specsOptional = specsColorListDOS.stream()
                .filter(specsColorListDO -> specsColorListDO.getGoodsItemId().equals(dto.getId())
                    && specsColorListDO.getAttributeType().equals(Constant.ATTRIBUTE_TYPE_1))
                .findFirst();
            if (specsOptional != null && specsOptional.isPresent()) {
                GoodsItemSpecsColorListDO specsColorListDO = specsOptional.get();
                dto.setSpecsId(specsColorListDO.getAttributeValueId());
                dto.setSpecsName(specsColorListDO.getAttributeValueName());
                dto.setSpecsNameEn(specsColorListDO.getAttributeValueNameEn());
            }
            Optional<GoodsItemSpecsColorListDO> colorOptional = specsColorListDOS.stream()
                .filter(specsColorListDO -> specsColorListDO.getGoodsItemId().equals(dto.getId())
                    && specsColorListDO.getAttributeType().equals(Constant.ATTRIBUTE_TYPE_2))
                .findFirst();
            if (colorOptional != null && colorOptional.isPresent()) {
                GoodsItemSpecsColorListDO specsColorListDO = colorOptional.get();
                dto.setColorId(specsColorListDO.getAttributeValueId());
                dto.setColorName(specsColorListDO.getAttributeValueName());
                dto.setColorNameEn(specsColorListDO.getAttributeValueNameEn());
            }
        }
        return dto;
    }

    public static List<GoodsRpcDTO> toGoodsRpcDTOList(List<GoodsListDO> goodsListDOS) {
        List<GoodsRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsListDOS)) {
            goodsListDOS.forEach(goodsListDO -> {
                GoodsRpcDTO dto = new GoodsRpcDTO();
                BeanUtils.copyProperties(goodsListDO, dto);
                dto.setGoodsId(goodsListDO.getId());
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GoodsItemOnwaySaleFlagRpcDTO>
        toGoodsItemOnwaySaleFlagRpcDTOList(List<GoodsItemOnwaySaleFlagDO> onwaySaleFlagDOS) {
        List<GoodsItemOnwaySaleFlagRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(onwaySaleFlagDOS)) {
            onwaySaleFlagDOS.forEach(onwaySaleFlagDO -> {
                GoodsItemOnwaySaleFlagRpcDTO dto = new GoodsItemOnwaySaleFlagRpcDTO();
                BeanUtils.copyProperties(onwaySaleFlagDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<ErpPriceFieldListRpcQry> toErpPriceFieldKeyListRpcQry(List<ScalePriceDO> scalePriceDOS) {
        List<ErpPriceFieldListRpcQry> rpcQryList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            scalePriceDOS.forEach(scalePriceDO -> {
                ErpPriceFieldListRpcQry qry = new ErpPriceFieldListRpcQry();
                BeanUtils.copyProperties(scalePriceDO, qry);
                rpcQryList.add(qry);
            });
        }
        return rpcQryList;
    }

    public static List<GoodsParamDTO> toGoodsParamDTO(List<GoodsParamDO> paramDOS) {
        List<GoodsParamDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(paramDOS)) {
            paramDOS.forEach(paramDO -> {
                GoodsParamDTO dto = new GoodsParamDTO();
                BeanUtils.copyProperties(paramDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GoodsTagDTO> toGoodsTagDTO(List<GoodsTagDO> tagDOS) {
        List<GoodsTagDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tagDOS)) {
            tagDOS.forEach(tagDO -> {
                GoodsTagDTO dto = new GoodsTagDTO();
                BeanUtils.copyProperties(tagDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<GoodsItemPriceRpcDTO>
        toGoodsItemPriceRpcDTORetailList(List<GoodsItemScalePriceDO> retailPriceDOS) {
        List<GoodsItemPriceRpcDTO> rpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(retailPriceDOS)) {
            retailPriceDOS.forEach(retailPriceDO -> {
                GoodsItemPriceRpcDTO rpcDTO = new GoodsItemPriceRpcDTO();
                rpcDTO.setGoodsId(retailPriceDO.getGoodsId());
                rpcDTO.setItemId(retailPriceDO.getGoodsItemId());
                rpcDTO.setRetailPrice(retailPriceDO.getPrice());
                rpcDTOS.add(rpcDTO);
            });
        }
        return rpcDTOS;
    }

    public static List<GoodsItemPriceRpcDTO> toGoodsItemPriceRpcDTOList(List<UserGoodsItemPriceDTO> priceDTOS) {
        List<GoodsItemPriceRpcDTO> rpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(priceDTOS)) {
            priceDTOS.forEach(priceDTO -> {
                GoodsItemPriceRpcDTO rpcDTO = new GoodsItemPriceRpcDTO();
                BeanUtils.copyProperties(priceDTO, rpcDTO);
                rpcDTOS.add(rpcDTO);
            });
        }
        return rpcDTOS;
    }

    public static List<com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO>
        toGoodsItemBoxRpcDTOList(List<GoodsItemBoxDO> boxDOS) {
        List<com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(boxDOS)) {
            boxDOS.forEach(boxDO -> {
                com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO rpcDTO =
                    new com.bat.dubboapi.goods.goods.dto.data.GoodsItemBoxRpcDTO();
                BeanUtils.copyProperties(boxDO, rpcDTO);
                dtos.add(rpcDTO);
            });
        }
        return dtos;
    }

    public static List<GoodsItemSimpleDTO> toGoodsItemSimpleDTO(List<GoodsItemSimpleDO> list,
                                                                List<MaterialDTORpcQry> materialDTORpcQryList) {
        if (list == null || list.size() == 0) {
            return null;
        }
        Map<Integer, MaterialDTORpcQry> materialDTORpcQryMap = null;
        if (materialDTORpcQryList != null && materialDTORpcQryList.size() > 0) {
            materialDTORpcQryMap = materialDTORpcQryList.stream()
                .collect(Collectors.toMap(MaterialDTORpcQry::getItemId, materialDTORpcQry -> materialDTORpcQry));
        }
        List<GoodsItemSimpleDTO> resultList = new ArrayList<>();
        Map<Integer, MaterialDTORpcQry> finalMaterialDTORpcQryMap = materialDTORpcQryMap;
        list.stream().forEach(goodsItemSimpleDO -> {
            GoodsItemSimpleDTO simpleDTO = new GoodsItemSimpleDTO();
            BeanUtils.copyProperties(goodsItemSimpleDO, simpleDTO);
            if (finalMaterialDTORpcQryMap != null && finalMaterialDTORpcQryMap.size() > 0) {
                MaterialDTORpcQry materialDTORpcQry = finalMaterialDTORpcQryMap.get(simpleDTO.getItemId());
                simpleDTO.setMaterialName(materialDTORpcQry.getName());
            }
            resultList.add(simpleDTO);
        });
        return resultList;
    }

    public static GoodsItemRpcDTO toGoodsItemRpcDTOByOne(GoodsItemDO goodsItemDO) {
        if (goodsItemDO == null) {
            return null;
        }
        GoodsItemRpcDTO goodsItemRpcDTO = new GoodsItemRpcDTO();
        BeanUtils.copyProperties(goodsItemDO, goodsItemRpcDTO);
        return goodsItemRpcDTO;
    }

    public static List<GoodsStoreColumnDO> toGoodsStoreColumnDOList(List<Integer> goodsIds, Integer columnId) {
        List<GoodsStoreColumnDO> columnDOS = new ArrayList<>();
        Integer sort = 1;
        goodsIds.forEach(goodsId -> {
            GoodsStoreColumnDO columnDO = new GoodsStoreColumnDO();
            columnDO.setColumnId(columnId);
            columnDO.setGoodsId(goodsId);
            columnDO.setSort(sort + 1);
            columnDOS.add(columnDO);
        });
        return columnDOS;
    }

    public static void toUpdateGoodsPrice(List<GoodsItemSyncDO> goodsItemSyncDOS,
        List<com.bat.dubboapi.thirdparty.erp.dto.goods.data.GoodsItemPriceRpcDTO> rpcDTOS,
        List<GoodsItemScalePriceDO> scalePriceDOS, List<GoodsItemSyncDO> updateGoodsItemSyncDOS,
        List<GoodsItemScalePriceDO> addScalePriceDOS, List<GoodsItemScalePriceDO> updateScalePriceDOS) {
        Map<Integer, GoodsItemSyncDO> goodsItemSyncDOMap = goodsItemSyncDOS.stream()
            .collect(Collectors.toMap(GoodsItemSyncDO::getItemErpId, goodsItemSyncDO -> goodsItemSyncDO));
        Map<String, GoodsItemScalePriceDO> scalePriceDOMap = new HashMap<>();
        for (GoodsItemScalePriceDO scalePriceDO : scalePriceDOS) {
            scalePriceDOMap.put(scalePriceDO.getGoodsItemId() + "," + scalePriceDO.getGoodsItemGradeId(), scalePriceDO);
        }
        if (!CollectionUtils.isEmpty(rpcDTOS)) {
            rpcDTOS.forEach(rpcDTO -> {
                GoodsItemSyncDO goodsItemSyncDO = goodsItemSyncDOMap.get(rpcDTO.getItemErpId());
                if (goodsItemSyncDO.getCostPrice() == null || goodsItemSyncDO.getSalePrice() == null
                    || (rpcDTO.getCostPrice() != null && goodsItemSyncDO.getCostPrice() != null
                        && goodsItemSyncDO.getCostPrice().compareTo(goodsItemSyncDO.getCostPrice()) != 0)
                    || (rpcDTO.getSalePrice() != null && goodsItemSyncDO.getSalePrice() != null
                        && goodsItemSyncDO.getSalePrice().compareTo(goodsItemSyncDO.getSalePrice()) != 0)) {
                    goodsItemSyncDO.setCostPrice(rpcDTO.getCostPrice());
                    goodsItemSyncDO.setSalePrice(rpcDTO.getSalePrice());
                    updateGoodsItemSyncDOS.add(goodsItemSyncDO);
                }
                List<ErpPriceFieldRpcDTO> priceList = rpcDTO.getPriceList();
                if (!CollectionUtils.isEmpty(priceList)) {
                    priceList.forEach(price -> {
                        GoodsItemScalePriceDO scalePriceDO =
                            scalePriceDOMap.get(goodsItemSyncDO.getId() + "," + price.getGoodsItemGradeId());
                        if (scalePriceDO == null) {
                            GoodsItemScalePriceDO addScalePriceDO = new GoodsItemScalePriceDO();
                            addScalePriceDO.setGoodsItemId(goodsItemSyncDO.getId());
                            addScalePriceDO.setGoodsId(goodsItemSyncDO.getGoodsId());
                            addScalePriceDO.setGoodsItemGradeId(price.getGoodsItemGradeId());
                            addScalePriceDO.setPrice(price.getPrice());
                            addScalePriceDOS.add(addScalePriceDO);
                        } else if (price.getPrice() == null
                            || price.getPrice().compareTo(scalePriceDO.getPrice()) != 0) {
                            scalePriceDO.setPrice(price.getPrice());
                            updateScalePriceDOS.add(scalePriceDO);
                        }
                    });
                }
            });
        }
    }

    public static List<GoodsItemDO> toUpdateGoodsItem(List<GoodsItemDO> goodsItemDOS,
        List<GoodsItemErpRpcDTO> itemErpRpcDTOS) {
        List<GoodsItemDO> updateGoodsItemDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(itemErpRpcDTOS)) {
            Map<Integer, GoodsItemErpRpcDTO> itemErpRpcDTOMap = itemErpRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemErpRpcDTO::getItemErpId, itemErpRpcDTO -> itemErpRpcDTO));
            goodsItemDOS.forEach(goodsItemDO -> {
                GoodsItemErpRpcDTO afterGoodsItemErpRpcDTO = itemErpRpcDTOMap.get(goodsItemDO.getItemErpId());
                if (afterGoodsItemErpRpcDTO != null) {
                    afterGoodsItemErpRpcDTO.setSalePrice(goodsItemDO.getSalePrice());
                    afterGoodsItemErpRpcDTO.setCostPrice(goodsItemDO.getCostPrice());
                    GoodsItemErpRpcDTO beforeItemErpRpcDTO = new GoodsItemErpRpcDTO();
                    BeanUtils.copyProperties(goodsItemDO, beforeItemErpRpcDTO);
                    beforeItemErpRpcDTO.setCategoryErpNo(afterGoodsItemErpRpcDTO.getCategoryErpNo());
                    try {
                        Map<String, List<Object>> changeFields =
                            CommonUtils.compareFields(beforeItemErpRpcDTO, afterGoodsItemErpRpcDTO);
                        // 说明有更新
                        if (!CollectionUtils.isEmpty(changeFields)) {
                            BeanUtils.copyProperties(afterGoodsItemErpRpcDTO, goodsItemDO);
                            updateGoodsItemDOS.add(goodsItemDO);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return updateGoodsItemDOS;
    }

    public static void toUpdateGoodsItemBox(List<GoodsItemSyncDO> goodsItemSyncDOS,
        List<GoodsItemBoxDO> goodsItemBoxDOS, List<GoodsItemBoxRpcDTO> goodsItemBoxRpcDTOS,
        List<GoodsItemBoxDO> updateGoodsItemBoxDOS, List<GoodsItemBoxDO> addGoodsItemBoxDOS) {
        if (!CollectionUtils.isEmpty(goodsItemBoxRpcDTOS)) {
            Map<String, GoodsItemSyncDO> goodsItemSyncDOMap = goodsItemSyncDOS.stream()
                .collect(Collectors.toMap(GoodsItemSyncDO::getItemCode, goodsItemSyncDO -> goodsItemSyncDO));
            Map<String, GoodsItemBoxDO> goodsItemBoxDOMap = new HashMap<>();
            goodsItemBoxDOS.forEach(goodsItemBoxDO -> {
                GoodsItemBoxDO boxDO = goodsItemBoxDOMap
                    .get(String.valueOf(goodsItemBoxDO.getGoodsItemId()) + "," + goodsItemBoxDO.getBoxErpId());
                if (boxDO == null) {
                    goodsItemBoxDOMap.put(
                        String.valueOf(goodsItemBoxDO.getGoodsItemId()) + "," + goodsItemBoxDO.getBoxErpId(),
                        goodsItemBoxDO);
                }
            });
            Date time = new Date(System.currentTimeMillis());
            goodsItemBoxRpcDTOS.forEach(goodsItemBoxRpcDTO -> {
                GoodsItemSyncDO goodsItemSyncDO = goodsItemSyncDOMap.get(goodsItemBoxRpcDTO.getItemCode());
                GoodsItemBoxDO goodsItemBoxDO = goodsItemBoxDOMap
                    .get(String.valueOf(goodsItemSyncDO.getId()) + "," + goodsItemBoxRpcDTO.getBoxErpId());
                if (goodsItemBoxDO == null) {
                    goodsItemBoxDO = new GoodsItemBoxDO();
                    BeanUtils.copyProperties(goodsItemBoxRpcDTO, goodsItemBoxDO);
                    goodsItemBoxDO.setGoodsItemId(goodsItemSyncDO.getId());
                    goodsItemBoxDO.setSort(0);
                    goodsItemBoxDO.setCreateTime(time);
                    goodsItemBoxDO.setUpdateTime(time);
                    addGoodsItemBoxDOS.add(goodsItemBoxDO);
                } else {
                    GoodsItemBoxRpcDTO beforeGoodsItemBoxRpcDTO = new GoodsItemBoxRpcDTO();
                    BeanUtils.copyProperties(goodsItemBoxDO, beforeGoodsItemBoxRpcDTO);
                    beforeGoodsItemBoxRpcDTO.setItemCode(goodsItemSyncDO.getItemCode());
                    try {
                        Map<String, List<Object>> changeFields =
                            CommonUtils.compareFields(beforeGoodsItemBoxRpcDTO, goodsItemBoxRpcDTO);
                        // 说明有更新
                        if (!CollectionUtils.isEmpty(changeFields)) {
                            BeanUtils.copyProperties(goodsItemBoxRpcDTO, goodsItemBoxDO);
                            goodsItemBoxDO.setUpdateTime(time);
                            updateGoodsItemBoxDOS.add(goodsItemBoxDO);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void toGoodsSaleDataDOList(List<GoodsSaleDataDO> goodsSaleDataDOS,
        List<GoodsSaleDataDO> updateGoodsSaleDataDOS, List<GoodsSaleDataDO> addGoodsSaleDataDOS,
        List<GoodsSaleDTO> saleDTOS) {
        Map<Integer, GoodsSaleDataDO> goodsSaleDataDOMap = new HashMap<>();
        Map<Integer, GoodsSaleDataDO> addGoodsSaleDataDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(goodsSaleDataDOS)) {
            goodsSaleDataDOMap.putAll(goodsSaleDataDOS.stream()
                .collect(Collectors.toMap(GoodsSaleDataDO::getGoodsId, goodsSaleDataDO -> goodsSaleDataDO)));
        }
        saleDTOS.forEach(saleDTO -> {
            GoodsSaleDataDO goodsSaleDataDO = null;
            if (!CollectionUtils.isEmpty(goodsSaleDataDOMap)) {
                goodsSaleDataDO = goodsSaleDataDOMap.get(saleDTO.getGoodsId());
            }
            if (goodsSaleDataDO == null) {
                goodsSaleDataDO = addGoodsSaleDataDOMap.get(saleDTO.getGoodsId());
                if (goodsSaleDataDO == null) {
                    goodsSaleDataDO = new GoodsSaleDataDO();
                    goodsSaleDataDO.setGoodsId(saleDTO.getGoodsId());
                    goodsSaleDataDO.setSaleNum(saleDTO.getSaleNum());
                    goodsSaleDataDO.setVirtualNum(0);
                    goodsSaleDataDO.setShowType(Constant.SHOW_TYPE_1);
                    addGoodsSaleDataDOS.add(goodsSaleDataDO);
                    addGoodsSaleDataDOMap.put(saleDTO.getGoodsId(), goodsSaleDataDO);
                } else {
                    goodsSaleDataDO.setSaleNum(goodsSaleDataDO.getSaleNum() + saleDTO.getSaleNum());
                }
            } else {
                if (saleDTO.getChangeType().equals(Constant.CHANGE_TYPE_1)) {
                    goodsSaleDataDO.setSaleNum(goodsSaleDataDO.getSaleNum() + saleDTO.getSaleNum());
                } else {
                    goodsSaleDataDO.setSaleNum(goodsSaleDataDO.getSaleNum() - saleDTO.getSaleNum());
                }
                updateGoodsSaleDataDOS.add(goodsSaleDataDO);
            }
            if (goodsSaleDataDO.getSaleNum() < 0) {
                goodsSaleDataDO.setSaleNum(0);
            }
        });
    }

    public static List<GoodsDistributorScopeDTO> toGoodsDistributorScopeDTOList(List<Integer> distributorIds,
        List<DistributorNameRpcDTO> distributorNameRpcDTOS) {
        List<GoodsDistributorScopeDTO> distributorScopeDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorIds)) {
            Map<Integer, DistributorNameRpcDTO> distributorNameRpcDTOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(distributorNameRpcDTOS)) {
                distributorNameRpcDTOMap.putAll(distributorNameRpcDTOS.stream().collect(
                    Collectors.toMap(DistributorNameRpcDTO::getId, distributorNameRpcDTO -> distributorNameRpcDTO)));
            }
            distributorIds.forEach(distributorId -> {
                GoodsDistributorScopeDTO distributorScopeDTO = new GoodsDistributorScopeDTO();
                distributorScopeDTO.setDistributorId(distributorId);
                DistributorNameRpcDTO nameRpcDTO = distributorNameRpcDTOMap.get(distributorId);
                if (nameRpcDTO != null) {
                    distributorScopeDTO.setName(nameRpcDTO.getName());
                    distributorScopeDTO.setCompanyName(nameRpcDTO.getCompanyName());
                }
                distributorScopeDTOS.add(distributorScopeDTO);
            });
        }
        return distributorScopeDTOS;
    }

    public static List<GoodsDistributorGroupDTO> toGoodsDistributorGroupDTOList(List<Integer> distributorGroupIds,
        List<DistributorGroupRpcDTO> groupRpcDTOS) {
        List<GoodsDistributorGroupDTO> distributorGroupDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorGroupIds)) {
            Map<Integer, DistributorGroupRpcDTO> groupRpcDTOMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(groupRpcDTOS)) {
                groupRpcDTOMap.putAll(groupRpcDTOS.stream()
                    .collect(Collectors.toMap(DistributorGroupRpcDTO::getId, groupRpcDTO -> groupRpcDTO)));
            }
            distributorGroupIds.forEach(distributorGroupId -> {
                GoodsDistributorGroupDTO distributorGroupDTO = new GoodsDistributorGroupDTO();
                distributorGroupDTO.setDistributorGroupId(distributorGroupId);
                DistributorGroupRpcDTO groupRpcDTO = groupRpcDTOMap.get(distributorGroupId);
                if (groupRpcDTO != null) {
                    distributorGroupDTO.setName(groupRpcDTO.getName());
                    distributorGroupDTO.setErpGroupNo(groupRpcDTO.getErpGroupNo());
                }
                distributorGroupDTOS.add(distributorGroupDTO);
            });
        }
        return distributorGroupDTOS;
    }

    public static List<GoodsItemSaleStatusCmd> toGoodsItemSaleStatusCmdList(
        List<GoodsStockFlagDTO> goodsStockFlagDTOList, List<GoodsItemStatusDO> goodsItemStatusDOS) {
        List<GoodsItemSaleStatusCmd> saleStatusCmds = new ArrayList<>();
        goodsItemStatusDOS.forEach(goodsItemStatusDO -> {
            List<GoodsStockFlagDTO> haveStocks = goodsStockFlagDTOList.stream()
                .filter(goodsStockFlagDTO -> goodsStockFlagDTO.getItemId().equals(goodsItemStatusDO.getId())
                    && goodsStockFlagDTO.getUnderStockFlag() == 0)
                .collect(Collectors.toList());
            // 有库存且未上架情况,没库存已上架情况都需要调整货品上下架状态
            GoodsItemSaleStatusCmd saleStatusCmd = new GoodsItemSaleStatusCmd();
            saleStatusCmd.setId(goodsItemStatusDO.getId());
            if (!CollectionUtils.isEmpty(haveStocks) && goodsItemStatusDO.getSaleStatus().equals(Constant.SALE_STATUS_1)) {
                saleStatusCmd.setSaleStatus(Constant.SALE_STATUS_3);
            } else if (CollectionUtils.isEmpty(haveStocks) && goodsItemStatusDO.getSaleStatus().equals(Constant.SALE_STATUS_3)) {
                saleStatusCmd.setSaleStatus(Constant.SALE_STATUS_1);
            }
            if (saleStatusCmd.getSaleStatus() != null) {
                saleStatusCmds.add(saleStatusCmd);
            }
        });
        return saleStatusCmds;
    }

    public static List<GoodsItemSaleStatusCmd> toGoodsItemSaleStatusCmdListByGoodsStockFlagDOS(
        List<GoodsStockFlagDO> goodsStockFlagDOS, List<GoodsItemStatusDO> goodsItemStatusDOS) {
        List<GoodsItemSaleStatusCmd> saleStatusCmds = new ArrayList<>();
        goodsItemStatusDOS.forEach(goodsItemStatusDO -> {
            List<GoodsStockFlagDO> haveStocks = goodsStockFlagDOS.stream()
                .filter(goodsStockFlagDTO -> goodsStockFlagDTO.getItemId().equals(goodsItemStatusDO.getId())
                    && goodsStockFlagDTO.getUnderStockFlag() == 0)
                .collect(Collectors.toList());
            // 有库存且未上架情况,没库存已上架情况都需要调整货品上下架状态
            GoodsItemSaleStatusCmd saleStatusCmd = new GoodsItemSaleStatusCmd();
            saleStatusCmd.setId(goodsItemStatusDO.getId());
            if (!CollectionUtils.isEmpty(haveStocks) && goodsItemStatusDO.getSaleStatus().equals(Constant.SALE_STATUS_1)) {
                saleStatusCmd.setSaleStatus(Constant.SALE_STATUS_3);
            } else if (CollectionUtils.isEmpty(haveStocks) && goodsItemStatusDO.getSaleStatus().equals(Constant.SALE_STATUS_3)) {
                saleStatusCmd.setSaleStatus(Constant.SALE_STATUS_1);
            }
            if (saleStatusCmd.getSaleStatus() != null) {
                saleStatusCmds.add(saleStatusCmd);
            }
        });
        return saleStatusCmds;
    }
}
