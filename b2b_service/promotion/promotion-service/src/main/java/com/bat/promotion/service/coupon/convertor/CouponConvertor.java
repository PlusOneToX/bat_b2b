package com.bat.promotion.service.coupon.convertor;

import static com.bat.promotion.service.common.Constant.COUPON_STATUS_3;
import static com.bat.promotion.service.common.Constant.COUPON_STATUS_5;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bat.promotion.api.coupon.dto.data.*;
import com.bat.promotion.dao.coupon.dataobject.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.promotion.api.coupon.dto.CouponCmd;
import com.bat.promotion.api.coupon.dto.CouponDistributorScopeCmd;
import com.bat.promotion.api.coupon.dto.CouponMaterialScopeCmd;
import com.bat.promotion.api.coupon.dto.CouponModelScopeCmd;
import com.bat.promotion.api.coupon.dto.data.*;
import com.bat.promotion.dao.coupon.dataobject.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/24 10:42
 */
public class CouponConvertor {

    public static CouponDTO toCouponDTO(CouponDO couponDO) {
        CouponDTO dto = new CouponDTO();
        BeanUtils.copyProperties(couponDO, dto);
        return dto;
    }

    public static CouponDO toCouponDO(CouponCmd cmd, Date date) {
        CouponDO couponDO = new CouponDO();
        BeanUtils.copyProperties(cmd, couponDO);
        if (couponDO.getGenerateCount() == null) {
            couponDO.setGenerateCount(0);
        }
        if (couponDO.getGeneratedCount() == null) {
            couponDO.setGeneratedCount(0);
        }
        couponDO.setCreateTime(date);
        couponDO.setUpdateTime(date);
        return couponDO;
    }

    public static List<CouponDistributorRelevanceDO> toCouponDistributorRelevanceDOList(Integer couponId,
                                                                                        List<CouponDistributorScopeCmd> cmds) {
        List<CouponDistributorRelevanceDO> distributorRelevanceDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                CouponDistributorRelevanceDO distributorRelevanceDO = new CouponDistributorRelevanceDO();
                BeanUtils.copyProperties(cmd, distributorRelevanceDO);
                distributorRelevanceDO.setCouponId(couponId);
                distributorRelevanceDOS.add(distributorRelevanceDO);
            });
        }
        return distributorRelevanceDOS;
    }

    public static List<CouponMaterialRelevanceDO> toCouponMaterialRelevanceDOList(Integer couponId,
                                                                                  List<CouponMaterialScopeCmd> cmds) {
        List<CouponMaterialRelevanceDO> relevanceDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                CouponMaterialRelevanceDO relevanceDO = new CouponMaterialRelevanceDO();
                BeanUtils.copyProperties(cmd, relevanceDO);
                relevanceDO.setCouponId(couponId);
                relevanceDOS.add(relevanceDO);
            });
        }
        return relevanceDOS;
    }

    public static List<CouponModelRelevanceDO> toCouponModelRelevanceDOList(Integer couponId,
                                                                            List<CouponModelScopeCmd> cmds) {
        List<CouponModelRelevanceDO> relevanceDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cmds)) {
            cmds.forEach(cmd -> {
                CouponModelRelevanceDO relevanceDO = new CouponModelRelevanceDO();
                BeanUtils.copyProperties(cmd, relevanceDO);
                relevanceDO.setCouponId(couponId);
                relevanceDOS.add(relevanceDO);
            });
        }
        return relevanceDOS;
    }

    public static List<CouponDTO> toCouponDTOList(List<CouponDO> couponDOS) {
        List<CouponDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(couponDOS)) {
            couponDOS.forEach(couponDO -> {
                CouponDTO dto = new CouponDTO();
                BeanUtils.copyProperties(couponDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<CouponCustomerDTO> toCouponCustomerDTOList(List<CouponCustomerDO> couponDOS) {
        List<CouponCustomerDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(couponDOS)) {
            couponDOS.forEach(couponDO -> {
                CouponCustomerDTO dto = new CouponCustomerDTO();
                BeanUtils.copyProperties(couponDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<CouponDistributorScopeDTO>
        toCouponDistributorScopeDTOList(List<CouponDistributorRelevanceDO> relevanceDOS) {
        List<CouponDistributorScopeDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relevanceDOS)) {
            relevanceDOS.forEach(relevanceDO -> {
                CouponDistributorScopeDTO dto = new CouponDistributorScopeDTO();
                BeanUtils.copyProperties(relevanceDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<CouponMaterialScopeDTO>
        toCouponMaterialScopeDTOList(List<CouponMaterialRelevanceDO> relevanceDOS) {
        List<CouponMaterialScopeDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relevanceDOS)) {
            relevanceDOS.forEach(relevanceDO -> {
                CouponMaterialScopeDTO dto = new CouponMaterialScopeDTO();
                BeanUtils.copyProperties(relevanceDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<CouponModelScopeDTO> toCouponModelScopeDTOList(List<CouponModelRelevanceDO> relevanceDOS) {
        List<CouponModelScopeDTO> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(relevanceDOS)) {
            relevanceDOS.forEach(relevanceDO -> {
                CouponModelScopeDTO dto = new CouponModelScopeDTO();
                BeanUtils.copyProperties(relevanceDO, dto);
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public static List<CouponStatusDO> toCouponStatusDOList(List<CouponDO> couponDOS, Short couponStatus,
                                                            String invalidExplain) {
        List<CouponStatusDO> statusDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(couponDOS)) {
            couponDOS.forEach(couponDO -> {
                if (!couponDO.getCouponStatus().equals(COUPON_STATUS_5)) {
                    CouponStatusDO statusDO = new CouponStatusDO();
                    Date date = new Date(System.currentTimeMillis());
                    statusDO.setUpdateTime(date);
                    statusDO.setId(couponDO.getId());
                    statusDO.setCouponStatus(couponStatus);
                    statusDO.setInvalidExplain(invalidExplain);
                    statusDOS.add(statusDO);
                }
            });
        }
        return statusDOS;
    }

    public static List<CouponStatusDO> toCustomerCouponStatusDOList(List<String> couponNos, Short couponStatus,
        String invalidExplain) {
        List<CouponStatusDO> statusDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(couponNos)) {
            couponNos.forEach(couponNo -> {
                CouponStatusDO statusDO = new CouponStatusDO();
                Date date = new Date(System.currentTimeMillis());
                statusDO.setUpdateTime(date);
                statusDO.setCouponNo(couponNo);
                statusDO.setCouponStatus(couponStatus);
                if (StringUtils.isNotBlank(invalidExplain)) {
                    statusDO.setInvalidExplain(invalidExplain);
                }
                statusDOS.add(statusDO);
            });
        }
        return statusDOS;
    }

    public static List<CouponStatusDO> toCouponStatusDOListByUserCouponDOS(List<UserCustomerCouponDO> couponDOS,
        Short couponStatus) {
        List<CouponStatusDO> statusDOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(couponDOS)) {
            Date date = new Date(System.currentTimeMillis());
            couponDOS.forEach(couponDO -> {
                CouponStatusDO statusDO = new CouponStatusDO();
                statusDO.setUpdateTime(date);
                statusDO.setCouponNo(couponDO.getCouponNo());
                if (couponDO.getEndTime().getTime() < date.getTime()) {
                    statusDO.setCouponStatus(COUPON_STATUS_3);
                } else {
                    statusDO.setCouponStatus(couponStatus);
                }
                statusDOS.add(statusDO);
            });
        }
        return statusDOS;
    }

}
