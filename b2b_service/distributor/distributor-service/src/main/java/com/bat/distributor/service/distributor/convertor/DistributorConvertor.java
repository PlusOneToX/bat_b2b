package com.bat.distributor.service.distributor.convertor;

import static com.bat.distributor.service.common.Constant.*;

import java.util.*;
import java.util.stream.Collectors;

import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import com.bat.distributor.dao.category.dataobject.CategoryDO;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.dao.group.dataobject.GroupDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceFormulaDO;
import com.bat.distributor.dao.nextscaleprice.dataobject.NextScalePriceSpecialFormulaDO;
import com.bat.distributor.dao.trade.dataobject.TradeDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.dubboapi.distributor.distributor.dto.DistributorErpRpcCmd;
import com.bat.dubboapi.distributor.distributor.dto.data.*;
import com.bat.dubboapi.goods.brand.dto.BrandDistributorRpcCmd;
import com.bat.dubboapi.goods.goods.dto.BrandScaleRpc;
import com.bat.dubboapi.goods.goods.dto.data.GoodsItemPriceRpcDTO;
import com.bat.dubboapi.system.check.dto.data.CheckConfigRpcDTO;
import com.bat.dubboapi.system.organization.dto.data.OrganizationRpcDTO;
import com.bat.dubboapi.system.user.dto.data.UserRpcDTO;

public class DistributorConvertor {

    private static final Logger log = LoggerFactory.getLogger(DistributorConvertor.class);
    /**
     * 分销商数据源DO的适配
     * 
     * @param cmd
     * @return
     */
    public static DistributorDO toDistributorDo(DistributorCmd cmd, Short openFlag, Date date) {
        DistributorDO distributorDO = new DistributorDO();
        BeanUtils.copyProperties(cmd, distributorDO);
        distributorDO.setApplyType(APPLY_TYPE_1);
        if (openFlag.equals(OPEN_FLAG_1)) {
            distributorDO.setProfileStatus(PROFILE_STATUS_1);
        } else {
            distributorDO.setProfileStatus(PROFILE_STATUS_2);
        }
        distributorDO.setFreezeStatus(FREEZE_STATUS_1);
        distributorDO.setTreeNode(1);
        distributorDO.setCreateTime(date);
        distributorDO.setUpdateTime(date);
        return distributorDO;
    }

    /**
     * do列表转dto列表
     * 
     * @param doList
     * @return
     */
    public static List<DistributorOneListDTO> toDistributorOneListDTOList(List<DistributorOneListDO> doList) {
        List<DistributorOneListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(distributorListDO -> {
                DistributorOneListDTO dto = new DistributorOneListDTO();
                BeanUtils.copyProperties(distributorListDO, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    /**
     * do列表转dto列表
     *
     * @param doList
     * @return
     */
    public static List<DistributorListDTO> toDistributorListDTOList(List<DistributorListDO> doList) {
        List<DistributorListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(distributorListDO -> {
                DistributorListDTO dto = new DistributorListDTO();
                BeanUtils.copyProperties(distributorListDO, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    public static List<DistributorCheckListDTO> toDistributorCheckListDTOList(List<DistributorCheckListDO> doList) {
        List<DistributorCheckListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(distributorCheckListDO -> {
                DistributorCheckListDTO dto = new DistributorCheckListDTO();
                BeanUtils.copyProperties(distributorCheckListDO, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    public static List<DistributorNextListDTO> toDistributorNextListDTO(List<DistributorNextListDO> doList) {
        List<DistributorNextListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(distributorListDO -> {
                DistributorNextListDTO dto = new DistributorNextListDTO();
                BeanUtils.copyProperties(distributorListDO, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    /**
     * do转dto
     * 
     * @param distributorDO
     * @return
     */
    public static DistributorDTO toDistributorDTO(DistributorDO distributorDO) {
        DistributorDTO dto = new DistributorDTO();
        BeanUtils.copyProperties(distributorDO, dto);
        return dto;
    }

    /**
     * 通过销售区域ids和分销商id获取分销商销售区域关系数据
     * 
     * @param salesAreaIds
     * @param distributorId
     * @return
     */
    public static List<DistributorSalesAreaDO> toDistributorSalesAreaDOList(List<Integer> salesAreaIds,
        Integer distributorId) {
        List<DistributorSalesAreaDO> doList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesAreaIds)) {
            salesAreaIds.forEach(id -> {
                DistributorSalesAreaDO salesAreaDO = new DistributorSalesAreaDO();
                salesAreaDO.setDistributorId(distributorId);
                salesAreaDO.setSalesAreaId(id);
                doList.add(salesAreaDO);
            });
        }
        return doList;
    }

    public static List<DistributorSalesAreaDO> toDistributorSalesAreaList(List<Object> salesAreaIdss,
        Integer distributorId) {
        List<DistributorSalesAreaDO> doList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesAreaIdss) && salesAreaIdss.get(1) instanceof List) {
            List<Integer> salesAreaIds =
                (List<Integer>)((List)salesAreaIdss.get(1)).stream().distinct().collect(Collectors.toList());
            salesAreaIds.forEach(id -> {
                DistributorSalesAreaDO salesAreaDO = new DistributorSalesAreaDO();
                salesAreaDO.setDistributorId(distributorId);
                salesAreaDO.setSalesAreaId(id);
                doList.add(salesAreaDO);
            });
        }
        return doList;
    }

    public static List<Integer> toDistributorSalesAreaIds(List<DistributorSalesAreaDO> salesAreaDOS) {
        List<Integer> salesAreaIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesAreaDOS)) {
            salesAreaDOS.forEach(salesAreaDO -> {
                salesAreaIds.add(salesAreaDO.getSalesAreaId());
            });
        }
        return salesAreaIds;
    }

    /**
     * 分销商地址数据源DO的适配
     *
     * @param cmd
     * @return
     */
    public static DistributorAddressDO toDistributorAddressDo(DistributorAddressCmd cmd, Integer distributorId) {
        DistributorAddressDO addressDO = new DistributorAddressDO();
        BeanUtils.copyProperties(cmd, addressDO);
        addressDO.setDistributorId(distributorId);
        Date date = new Date(System.currentTimeMillis());
        addressDO.setCreateTime(date);
        addressDO.setUpdateTime(date);
        return addressDO;
    }

    public static DistributorAddressDTO toDistributorAddressDTO(DistributorAddressDO addressDO) {
        DistributorAddressDTO addressDTO = new DistributorAddressDTO();
        BeanUtils.copyProperties(addressDO, addressDTO);
        return addressDTO;
    }

    /**
     * 分销商业务数据源DO的适配
     *
     * @param cmd
     * @return
     */
    public static DistributorBusinessDO toDistributorBusinessDo(DistributorBusinessCmd cmd, Integer distributorId) {
        DistributorBusinessDO businessDO = new DistributorBusinessDO();
        BeanUtils.copyProperties(cmd, businessDO);
        businessDO.setDistributorId(distributorId);
        Date date = new Date(System.currentTimeMillis());
        businessDO.setCreateTime(date);
        businessDO.setUpdateTime(date);
        return businessDO;
    }

    public static List<DistributorOneScalePriceDO>
        toDistributorOneScalePriceDOList(List<DistributorOneScalePriceCmd> cmds, Integer distributorId) {
        List<DistributorOneScalePriceDO> doList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                DistributorOneScalePriceDO scalePriceDO = toDistributorOneScalePriceDO(cmd, distributorId);
                doList.add(scalePriceDO);
            });
        }
        return doList;
    }

    public static DistributorOneScalePriceDO toDistributorOneScalePriceDO(DistributorOneScalePriceCmd cmd,
        Integer distributorId) {
        DistributorOneScalePriceDO scalePriceDO = new DistributorOneScalePriceDO();
        BeanUtils.copyProperties(cmd, scalePriceDO);
        scalePriceDO.setDistributorId(distributorId);
        return scalePriceDO;
    }

    public static DistributorBusinessDTO toDistributorBusinessDTO(DistributorBusinessDO businessDO) {
        DistributorBusinessDTO businessDTO = new DistributorBusinessDTO();
        BeanUtils.copyProperties(businessDO, businessDTO);
        return businessDTO;
    }

    /**
     * 分销商联系人列表数据
     *
     * @param contacts
     * @param distributorId
     * @return
     */
    public static List<DistributorContactsDO> toDistributorContactsDOList(List<DistributorContactsCmd> contacts,
        Integer distributorId) {
        List<DistributorContactsDO> doList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(contacts)) {
            contacts.forEach(contact -> {
                DistributorContactsDO contactsDO = toDistributorContactsDO(contact, distributorId);
                doList.add(contactsDO);
            });
        }
        return doList;
    }

    public static List<DistributorContactsDTO> toDistributorContactsDTOList(List<DistributorContactsDO> contactsDOS) {
        List<DistributorContactsDTO> contactsDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(contactsDOS)) {
            contactsDOS.forEach(contactsDO -> {
                DistributorContactsDTO contactsDTO = new DistributorContactsDTO();
                BeanUtils.copyProperties(contactsDO, contactsDTO);
                contactsDTOS.add(contactsDTO);
            });
        }
        return contactsDTOS;
    }

    public static List<DistributorOneScalePriceDTO>
        toDistributorOneScalePriceDTOList(List<DistributorOneScalePriceDO> scalePriceDOS) {
        List<DistributorOneScalePriceDTO> scalePriceDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            scalePriceDOS.forEach(scalePriceDO -> {
                DistributorOneScalePriceDTO scalePriceDTO = new DistributorOneScalePriceDTO();
                BeanUtils.copyProperties(scalePriceDO, scalePriceDTO);
                scalePriceDTOS.add(scalePriceDTO);
            });
        }
        return scalePriceDTOS;
    }

    /**
     * 分销商联系人数据
     *
     * @param contact
     * @param distributorId
     * @return
     */
    public static DistributorContactsDO toDistributorContactsDO(DistributorContactsCmd contact, Integer distributorId) {
        DistributorContactsDO contactsDO = new DistributorContactsDO();
        BeanUtils.copyProperties(contact, contactsDO);
        contactsDO.setFreezeStatus(FREEZE_STATUS_1);
        contactsDO.setDistributorId(distributorId);
        Date date = new Date(System.currentTimeMillis());
        contactsDO.setCreateTime(date);
        contactsDO.setUpdateTime(date);
        return contactsDO;
    }

    /**
     * 分销商扩展数据源DO的适配
     *
     * @param cmd
     * @return
     */
    public static DistributorExtendDataDO toDistributorExtendDataDO(DistributorExtendDataCmd cmd,
        Integer distributorId) {
        DistributorExtendDataDO extendDataDO = new DistributorExtendDataDO();
        BeanUtils.copyProperties(cmd, extendDataDO);
        extendDataDO.setDistributorId(distributorId);
        Date date = new Date(System.currentTimeMillis());
        extendDataDO.setCreateTime(date);
        extendDataDO.setUpdateTime(date);
        return extendDataDO;
    }

    public static DistributorExtendDataDTO toDistributorExtendDataDTO(DistributorExtendDataDO extendDataDO) {
        DistributorExtendDataDTO extendDataDTO = new DistributorExtendDataDTO();
        BeanUtils.copyProperties(extendDataDO, extendDataDTO);
        return extendDataDTO;
    }

    /**
     * 分销商财务数据源DO的适配
     *
     * @param cmd
     * @return
     */
    public static DistributorFinancialDO toDistributorFinancialDO(DistributorFinancialCmd cmd, Integer distributorId) {
        DistributorFinancialDO financialDO = new DistributorFinancialDO();
        BeanUtils.copyProperties(cmd, financialDO);
        financialDO.setDistributorId(distributorId);
        Date date = new Date(System.currentTimeMillis());
        financialDO.setCreateTime(date);
        financialDO.setUpdateTime(date);
        return financialDO;
    }

    public static DistributorFinancialDTO toDistributorFinancialDTO(DistributorFinancialDO financialDO) {
        DistributorFinancialDTO financialDTO = new DistributorFinancialDTO();
        BeanUtils.copyProperties(financialDO, financialDTO);
        return financialDTO;
    }

    /**
     * 分销商特价商品列表数据
     *
     * @param cmds
     * @param distributorId
     * @return
     */
    public static List<DistributorSpecialGoodsDO> toDistributorSpecialGoodsDOList(List<DistributorSpecialGoodsCmd> cmds,
        Integer distributorId) {
        List<DistributorSpecialGoodsDO> doList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                DistributorSpecialGoodsDO specialGoodsDO = new DistributorSpecialGoodsDO();
                BeanUtils.copyProperties(cmd, specialGoodsDO);
                specialGoodsDO.setDistributorId(distributorId);
                Date date = new Date(System.currentTimeMillis());
                specialGoodsDO.setCreateTime(date);
                specialGoodsDO.setUpdateTime(date);
                doList.add(specialGoodsDO);
            });
        }
        return doList;
    }

    public static List<DistributorSpecialGoodsDTO>
        toDistributorSpecialGoodsDTOList(List<DistributorSpecialGoodsDO> specialGoodsDOS) {
        List<DistributorSpecialGoodsDTO> specialGoodsDTOS = new ArrayList<>();
        specialGoodsDOS.forEach(specialGoodsDO -> {
            DistributorSpecialGoodsDTO specialGoodsDTO = new DistributorSpecialGoodsDTO();
            BeanUtils.copyProperties(specialGoodsDO, specialGoodsDTO);
            specialGoodsDTOS.add(specialGoodsDTO);
        });
        return specialGoodsDTOS;
    }

    /**
     * 分销商特价商品数据
     *
     * @param cmd
     * @param distributorId
     * @return
     */
    public static DistributorSpecialGoodsDO toDistributorSpecialGoodsDO(DistributorSpecialGoodsCmd cmd,
        Integer distributorId) {
        DistributorSpecialGoodsDO specialGoodsDO = new DistributorSpecialGoodsDO();
        BeanUtils.copyProperties(cmd, specialGoodsDO);
        specialGoodsDO.setId(null);
        specialGoodsDO.setDistributorId(distributorId);
        Date date = new Date(System.currentTimeMillis());
        specialGoodsDO.setCreateTime(date);
        specialGoodsDO.setUpdateTime(date);
        return specialGoodsDO;
    }

    public static List<DistributorIdsDTO> toDistributorIdsDTOList(List<DistributorIdsDO> distributorIdsDOS) {
        List<DistributorIdsDTO> dtos = new ArrayList<>();
        distributorIdsDOS.forEach(distributorIdsDO -> {
            DistributorIdsDTO dto = new DistributorIdsDTO();
            BeanUtils.copyProperties(distributorIdsDO, dto);
            dtos.add(dto);
        });
        return dtos;
    }

    public static List<BrandDistributorRpcCmd> toBrandDistributorRpcCmd(List<DistributorOneScalePriceDO> scalePriceDOS,
        Integer distributorId) {
        List<BrandDistributorRpcCmd> cmds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            scalePriceDOS.forEach(scalePriceDO -> {
                BrandDistributorRpcCmd cmd = new BrandDistributorRpcCmd();
                BeanUtils.copyProperties(scalePriceDO, cmd);
                cmd.setDistributorId(distributorId);
                cmd.setScalePriceId(scalePriceDO.getScalePriceId());
                cmds.add(cmd);
            });
        }
        return cmds;
    }

    public static List<OneScalePriceRpcDTO>
        toOneScalePriceRpcDTOList(List<DistributorOneScalePriceDO> oneScalePriceDOS) {
        List<OneScalePriceRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oneScalePriceDOS)) {
            oneScalePriceDOS.forEach(oneScalePriceDO -> {
                OneScalePriceRpcDTO dto = new OneScalePriceRpcDTO();
                BeanUtils.copyProperties(oneScalePriceDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static OneScalePriceRpcDTO toOneScalePriceRpcDTO(DistributorOneScalePriceDO oneScalePriceDO) {
        OneScalePriceRpcDTO dto = new OneScalePriceRpcDTO();
        BeanUtils.copyProperties(oneScalePriceDO, dto);
        return dto;
    }

    public static List<NextScalePriceRpcDTO> toNextScalePriceRpcDTOList(
        List<NextScalePriceFormulaDO> nextScalePriceFormulaDOS,
        List<NextScalePriceSpecialFormulaDO> scalePriceSpecialDOS) {
        List<NextScalePriceRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(nextScalePriceFormulaDOS)) {
            nextScalePriceFormulaDOS.forEach(nextScalePriceDO -> {
                NextScalePriceRpcDTO dto = new NextScalePriceRpcDTO();
                BeanUtils.copyProperties(nextScalePriceDO, dto);
                if (!CollectionUtils.isEmpty(scalePriceSpecialDOS)) {
                    List<NextScalePriceSpecialFormulaDO> specialFormulaDOS = scalePriceSpecialDOS.stream()
                        .filter(scalePriceSpecialDO -> scalePriceSpecialDO.getNextScalePriceId().equals(dto.getId()))
                        .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(specialFormulaDOS)) {
                        List<NextScalePriceSpecialRpcDTO> specialRpcDTOS = new ArrayList<>();
                        specialFormulaDOS.forEach(specialFormulaDO -> {
                            NextScalePriceSpecialRpcDTO specialRpcDTO = new NextScalePriceSpecialRpcDTO();
                            BeanUtils.copyProperties(specialFormulaDO, specialRpcDTO);
                            specialRpcDTOS.add(specialRpcDTO);
                        });
                        dto.setNextScalePriceSpecials(specialRpcDTOS);
                    }
                }
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<DistributorSpecialGoodsRpcDTO>
        toDistributorSpecialGoodsRpcDTO(List<DistributorSpecialGoodsDO> specialGoodsDOS) {
        List<DistributorSpecialGoodsRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(specialGoodsDOS)) {
            specialGoodsDOS.forEach(specialGoodsDO -> {
                DistributorSpecialGoodsRpcDTO dto = new DistributorSpecialGoodsRpcDTO();
                BeanUtils.copyProperties(specialGoodsDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static DistributorRpcDTO toDistributorRpcDTO(DistributorRpcDO distributorDO,
        Map<Integer, UserRpcDTO> userRpcDTOMap, List<DistributorSalesAreaDO> salesAreaDOS,
        DistributorDO ancestorDistributorDO, DistributorExtendDataDO ancestorExtendDataDO) {
        DistributorRpcDTO dto = new DistributorRpcDTO();
        BeanUtils.copyProperties(distributorDO, dto);
        if (dto.getSalesId() != null) {
            UserRpcDTO userRpcDTO = userRpcDTOMap.get(dto.getSalesId());
            if (userRpcDTO != null) {
                dto.setSalesName(userRpcDTO.getUserName());
                OrganizationRpcDTO organizationDTO = userRpcDTO.getOrganizationDTO();
                if (organizationDTO != null) {
                    dto.setOrganizationId(organizationDTO.getId());
                    dto.setOrganizationErp(organizationDTO.getErpOrganizationId());
                    dto.setOrganizationName(organizationDTO.getName());
                }
            }
        }
        if (dto.getCoordinatorId() != null) {
            UserRpcDTO userRpcDTO = userRpcDTOMap.get(dto.getCoordinatorId());
            if (userRpcDTO != null) {
                dto.setCoordinatorName(userRpcDTO.getUserName());
            }
        }
        if (!CollectionUtils.isEmpty(salesAreaDOS)) {
            dto.setSalesAreaIds(salesAreaDOS.stream().map(DistributorSalesAreaDO::getSalesAreaId).distinct()
                .collect(Collectors.toList()));
        }
        if (ancestorDistributorDO != null) {
            dto.setDistributorAncestorId(ancestorDistributorDO.getId());
            dto.setDistributorAncestorName(ancestorDistributorDO.getName());
            dto.setDistributorAncestorCompanyName(ancestorDistributorDO.getCompanyName());

        }
        if (ancestorExtendDataDO != null) {
            dto.setAncestorDistributionMode(ancestorExtendDataDO.getDistributionMode());
            dto.setAncestorDistributionAutoFlag(ancestorExtendDataDO.getDistributionAutoFlag());
        } else {
            dto.setAncestorDistributionMode(DISTRIBUTION_MODE_1);
            dto.setAncestorDistributionAutoFlag(AUTO_FLAG_1);
        }
        return dto;
    }

    public static DistributorRpcDTO toDistributorRpcDTO(DistributorRpcDO distributorDO,
        List<DistributorSalesAreaDO> salesAreaDOS) {
        DistributorRpcDTO dto = new DistributorRpcDTO();
        BeanUtils.copyProperties(distributorDO, dto);
        if (!CollectionUtils.isEmpty(salesAreaDOS)) {
            dto.setSalesAreaIds(
                salesAreaDOS.stream().map(DistributorSalesAreaDO::getSalesAreaId).collect(Collectors.toList()));
        }
        return dto;
    }

    public static List<DistributorRpcDTO> toDistributorRpcDTOList(List<DistributorRpcDO> distributorDOS,
        Map<Integer, UserRpcDTO> userRpcDTOMap) {
        List<DistributorRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorDOS)) {
            distributorDOS.forEach(distributorDO -> {
                DistributorRpcDTO dto = toDistributorRpcDTO(distributorDO, userRpcDTOMap, null, null, null);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static DistributorBusinessRpcDTO toDistributorBusinessRpcDTO(DistributorBusinessDO businessDO,
        DistributorDO distributorDO, List<DistributorSalesAreaDO> salesAreaDOS) {
        DistributorBusinessRpcDTO dto = new DistributorBusinessRpcDTO();
        BeanUtils.copyProperties(businessDO, dto);
        BeanUtils.copyProperties(distributorDO, dto);
        if (!CollectionUtils.isEmpty(salesAreaDOS)) {
            dto.setSalesAreaIds(
                salesAreaDOS.stream().map(DistributorSalesAreaDO::getSalesAreaId).collect(Collectors.toList()));

        }
        return dto;
    }

    public static List<DistributorNextNameRpcDTO> toDistributorNextNameRpcDTOList(
        List<DistributorErpIdRpcDO> erpIdRpcDOS, Map<Integer, DistributorTreePathDO> treePathDOMap,
        List<DistributorIdsDO> distributorIdsDOS) {
        Map<Integer, DistributorIdsDO> distributorIdsDOMap = distributorIdsDOS.stream()
            .collect(Collectors.toMap(DistributorIdsDO::getId, distributorIdsDO -> distributorIdsDO));
        List<DistributorNextNameRpcDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(erpIdRpcDOS)) {
            erpIdRpcDOS.forEach(erpIdRpcDO -> {
                DistributorNextNameRpcDTO dto = new DistributorNextNameRpcDTO();
                BeanUtils.copyProperties(erpIdRpcDO, dto);
                dtos.add(dto);
                if (treePathDOMap != null && treePathDOMap.size() > 0) {
                    DistributorTreePathDO distributorTreePathDO = treePathDOMap.get(dto.getId());
                    if (distributorTreePathDO != null) {
                        DistributorIdsDO distributorIdsDO =
                            distributorIdsDOMap.get(distributorTreePathDO.getDistributorAncestorId());
                        if (distributorIdsDO != null) {
                            dto.setDistributorAncestorId(distributorIdsDO.getId());
                            dto.setDistributorAncestorCompanyName(distributorIdsDO.getCompanyName());
                            dto.setDistributorAncestorName(distributorIdsDO.getName());
                        }
                    }
                }
            });
        }
        return dtos;
    }

    public static DistributorCustomerPriceDTO toDistributorCustomerPriceDTO(Integer itemId,
        DistributorCustomPriceDO customPriceDO, GoodsItemPriceRpcDTO priceRpcDTO) {
        DistributorCustomerPriceDTO dto = new DistributorCustomerPriceDTO();
        dto.setItemId(itemId);
        if (priceRpcDTO != null) {
            dto.setPrice(priceRpcDTO.getRetailPrice());
        }
        if (customPriceDO != null) {
            dto.setPrice(customPriceDO.getPrice());
        }
        return dto;
    }

    public static List<DistributorCustomerPriceDTO> toDistributorCustomerPriceDTOList(List<Integer> itemIds,
        List<DistributorCustomPriceDO> customPriceDOS, List<GoodsItemPriceRpcDTO> priceRpcDTOS) {
        List<DistributorCustomerPriceDTO> dtos = new ArrayList<>();
        Map<Integer, DistributorCustomPriceDO> customPriceDOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(customPriceDOS)) {
            customPriceDOMap.putAll(customPriceDOS.stream()
                .collect(Collectors.toMap(DistributorCustomPriceDO::getItemId, customPriceDO -> customPriceDO)));
        }
        Map<Integer, GoodsItemPriceRpcDTO> priceRpcDTOMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(priceRpcDTOS)) {
            priceRpcDTOMap.putAll(priceRpcDTOS.stream()
                .collect(Collectors.toMap(GoodsItemPriceRpcDTO::getItemId, priceRpcDTO -> priceRpcDTO)));
        }
        itemIds.forEach(itemId -> {
            DistributorCustomPriceDO customPriceDO = customPriceDOMap.get(itemId);
            GoodsItemPriceRpcDTO priceRpcDTO = priceRpcDTOMap.get(itemId);
            DistributorCustomerPriceDTO dto = toDistributorCustomerPriceDTO(itemId, customPriceDO, priceRpcDTO);
            dtos.add(dto);
        });
        return dtos;
    }

    public static DistributorExtendDataRpcDTO toDistributorExtendDataRpcDTO(DistributorExtendDataDO extendDataDO) {
        if (extendDataDO != null) {
            DistributorExtendDataRpcDTO rpcDTO = new DistributorExtendDataRpcDTO();
            BeanUtils.copyProperties(extendDataDO, rpcDTO);
            return rpcDTO;
        }
        return null;
    }

    public static void toUpdateDistributorExtendDataDO(DistributorExtendDataDO extendDataDO, DistributorErpRpcCmd cmd) {
        extendDataDO.setErpId(cmd.getErpId());
        extendDataDO.setErpNo(cmd.getErpNo());
        Date time = new Date(System.currentTimeMillis());
        extendDataDO.setUpdateTime(time);
    }

    public static DistributorERPRpcDTO toDistributorERPRpcDTO(DistributorDO distributorDO,
                                                              DistributorExtendDataDO extendDataDO, DistributorBusinessDO businessDO, GroupDO groupDO, CategoryDO categoryDO,
                                                              DistributorFinancialDO financialDO, TradeDO tradeDO, List<DistributorAddressDO> addressDOS,
                                                              List<DistributorContactsDO> contactsDOS) {
        DistributorERPRpcDTO erpRpcDTO = new DistributorERPRpcDTO();
        DistributorAddressRpcDTO addressRpcDTO = new DistributorAddressRpcDTO();
        erpRpcDTO.setAddress(addressRpcDTO);
        DistributorContactsRpcDTO contactsRpcDTO = new DistributorContactsRpcDTO();
        erpRpcDTO.setContacts(contactsRpcDTO);
        DistributorFinancialRpcDO financialRpcDO = new DistributorFinancialRpcDO();
        erpRpcDTO.setFinancial(financialRpcDO);
        erpRpcDTO.setId(distributorDO.getId());
        erpRpcDTO.setCompanyName(distributorDO.getCompanyName());
        erpRpcDTO.setSalesId(businessDO.getSalesId());
        erpRpcDTO.setErpId(extendDataDO.getErpId());
        erpRpcDTO.setErpNo(extendDataDO.getErpNo());
        erpRpcDTO.setAutoDelivery(businessDO.getAutoDelivery());
        if (groupDO != null) {
            erpRpcDTO.setErpGroupNo(groupDO.getErpGroupNo());
        }
        if (categoryDO != null) {
            erpRpcDTO.setErpCategoryNo(categoryDO.getErpCategoryNo());
        }
        if (financialDO != null) {
            BeanUtils.copyProperties(financialDO, financialRpcDO);
        }
        financialRpcDO.setErpSettleAccountNo(tradeDO.getErpSettleAccountNo());
        financialRpcDO.setPayWay(tradeDO.getPayWay());
        financialRpcDO.setErpBalanceFlag(extendDataDO.getErpBalanceFlag());
        if (!CollectionUtils.isEmpty(addressDOS)) {
            BeanUtils.copyProperties(addressDOS.get(0), addressRpcDTO);
        }
        if (!CollectionUtils.isEmpty(contactsDOS)) {
            DistributorContactsDO contactsDO = contactsDOS.get(0);
            contactsRpcDTO.setName(contactsDO.getName());
            contactsRpcDTO.setPhone(contactsDO.getPhone());
        }
        return erpRpcDTO;
    }

    public static List<BrandScaleRpc> toBrandScaleRpcList(List<DistributorOneScalePriceDO> scalePriceDOS) {
        List<BrandScaleRpc> scaleRpcs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            scalePriceDOS.forEach(scalePriceDO -> {
                if (scalePriceDO.getBrandId() != null && scalePriceDO.getBrandId() != 0
                    && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0)) {
                    BrandScaleRpc scaleRpc = new BrandScaleRpc();
                    scaleRpc.setBrandId(scalePriceDO.getBrandId());
                    scaleRpc.setScalePriceId(scalePriceDO.getScalePriceId());
                    scaleRpcs.add(scaleRpc);
                }
            });
        }
        return scaleRpcs;
    }

    public static List<DistributorNameRpcDTO> toDistributorNameRpcDTOList(List<DistributorNameDO> distributorNameDOS) {
        List<DistributorNameRpcDTO> nameRpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributorNameDOS)) {
            distributorNameDOS.forEach(distributorNameDO -> {
                DistributorNameRpcDTO nameRpcDTO = new DistributorNameRpcDTO();
                BeanUtils.copyProperties(distributorNameDO, nameRpcDTO);
                nameRpcDTOS.add(nameRpcDTO);
            });
        }
        return nameRpcDTOS;
    }

    public static List<DistributorGroupRpcDTO> toDistributorGroupRpcDTOList(List<GroupDO> groupDOS) {
        List<DistributorGroupRpcDTO> groupRpcDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupDOS)) {
            groupDOS.forEach(groupDO -> {
                DistributorGroupRpcDTO groupRpcDTO = new DistributorGroupRpcDTO();
                BeanUtils.copyProperties(groupDO, groupRpcDTO);
                groupRpcDTOS.add(groupRpcDTO);
            });
        }
        return groupRpcDTOS;
    }

    public static DistributorCheckDO toDistributorCheckDO(List<CheckConfigRpcDTO> checkFlows, Integer distributorId,
        Date date, String userId, String userName) {
        CheckConfigRpcDTO checkConfigRpcDTO = checkFlows.get(0);
        DistributorCheckDO checkDO = new DistributorCheckDO();
        checkDO.setDistributorId(distributorId);
        checkDO.setUserId(Integer.valueOf(userId));
        checkDO.setUserName(userName);
        checkDO.setCheckUserId(checkConfigRpcDTO.getCheckUser());
        checkDO.setCheckUserName(checkConfigRpcDTO.getCheckUserName());
        checkDO.setCheckType(CHECK_TYPE_1);
        checkDO.setCheckStatus(CHECK_STATUS_0);
        checkDO.setCreateTime(date);
        checkDO.setUpdateTime(date);
        return checkDO;
    }

    public static DistributorCheckDO toDistributorCheckDO(List<CheckConfigRpcDTO> checkFlows, Integer distributorId,
        Map<String, List<List<Object>>> changeMap, Date date, String userId, String userName) {
        CheckConfigRpcDTO checkConfigRpcDTO = checkFlows.get(0);
        DistributorCheckDO checkDO = new DistributorCheckDO();
        checkDO.setDistributorId(distributorId);
        checkDO.setUserId(Integer.valueOf(userId));
        checkDO.setUserName(userName);
        checkDO.setCheckUserId(checkConfigRpcDTO.getCheckUser());
        checkDO.setCheckUserName(checkConfigRpcDTO.getCheckUserName());
        checkDO.setCheckType(CHECK_TYPE_2);
        checkDO.setCheckStatus(CHECK_STATUS_0);
        checkDO.setCheckContent(JSONObject.toJSONString(changeMap));
        checkDO.setCreateTime(date);
        checkDO.setUpdateTime(date);
        return checkDO;
    }

    public static List<DistributorCheckFlowDO> toDistributorCheckFlowDOList(List<CheckConfigRpcDTO> checkFlows,
        Integer distributorCheckId, Date date) {
        List<DistributorCheckFlowDO> checkFlowDOS = new ArrayList<>();
        checkFlows = checkFlows.stream().sorted(Comparator.comparing(CheckConfigRpcDTO::getCheckOrder))
            .collect(Collectors.toList());
        checkFlows.forEach(checkFlow -> {
            DistributorCheckFlowDO checkFlowDO = new DistributorCheckFlowDO();
            checkFlowDO.setDistributorCheckId(distributorCheckId);
            checkFlowDO.setCheckSort(checkFlow.getCheckOrder());
            checkFlowDO.setUserId(checkFlow.getCheckUser());
            checkFlowDO.setUserName(checkFlow.getCheckUserName());
            checkFlowDO.setCheckStatus(CHECK_STATUS_0);
            checkFlowDO.setCreateTime(date);
            checkFlowDO.setUpdateTime(date);
            checkFlowDOS.add(checkFlowDO);
        });
        return checkFlowDOS;
    }

    public static DistributorOneDTO toDistributorOneDTO(DistributorDO distributorDO,
        DistributorDO ancestorDistributorDO) {
        DistributorOneDTO oneDTO = new DistributorOneDTO();
        BeanUtils.copyProperties(distributorDO, oneDTO);
        if (oneDTO.getTreeNode() > 1 && ancestorDistributorDO != null) {
            oneDTO.setDistributorOneId(ancestorDistributorDO.getId());
            oneDTO.setDistributorOneName(ancestorDistributorDO.getName());
            oneDTO.setDistributorOneCompanyName(ancestorDistributorDO.getCompanyName());
        }
        return oneDTO;
    }

}
