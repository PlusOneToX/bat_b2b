package com.bat.promotion.service.coupon.executor;

import static com.bat.promotion.service.common.Constant.*;

import java.util.List;

import javax.annotation.Resource;

import com.bat.promotion.dao.coupon.*;
import com.bat.promotion.dao.coupon.dataobject.*;
import com.bat.promotion.service.coupon.convertor.CouponConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.base.PromotionException;
import com.bat.promotion.api.coupon.dto.CouponCustomerListQry;
import com.bat.promotion.api.coupon.dto.CouponListQry;
import com.bat.promotion.api.coupon.dto.data.CouponCustomerDTO;
import com.bat.promotion.api.coupon.dto.data.CouponDTO;
import com.bat.promotion.api.coupon.dto.data.CouponListDTO;
import com.bat.promotion.dao.coupon.*;
import com.bat.promotion.dao.coupon.dataobject.*;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/24 10:43
 */
@Component
public class CouponQryExe {

    @Resource
    private CouponMapper couponMapper;
    @Resource
    private CouponDistributorRelevanceMapper distributorRelevanceMapper;
    @Resource
    private CouponMaterialRelevanceMapper materialRelevanceMapper;
    @Resource
    private CouponModelRelevanceMapper modelRelevanceMapper;
    @Resource
    private CouponCustomerMapper couponCustomerMapper;

    /**
     * 根据搜索条件查找优惠券列表(分页)
     * 
     * @param qry
     * @return
     */
    public PageInfo<CouponListDTO> listCoupon(CouponListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CouponDO> couponDOS = null;
        // 搜索内容类型，1 优惠券名称 2 材料名称 3 型号名称 4 分销商
        if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_2)) {
            couponDOS = couponMapper.listCouponByMaterial(qryMap);
        } else if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_3)) {
            couponDOS = couponMapper.listCouponByModel(qryMap);
        } else if (StringUtils.isNotBlank(qry.getContent()) && qry.getContentType() != null
            && qry.getContentType().equals(CONTENT_TYPE_4)) {
            couponDOS = couponMapper.listCouponByDistributorName(qryMap);
        } else {
            couponDOS = couponMapper.listCoupon(qryMap);
        }
        PageInfo pageInfo = new PageInfo(couponDOS);
        List<CouponDTO> dtos = CouponConvertor.toCouponDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }

    /**
     * 根据id查询优惠券详情
     * 
     * @param qry
     * @return
     */
    public CouponDTO getCoupon(BaseId qry) {
        return getCoupon(qry.getId());
    }

    public CouponDTO getCoupon(Integer id) {
        CouponDO couponDO = couponMapper.selectByPrimaryKey(id);
        if (couponDO == null) {
            throw PromotionException.buildException(ErrorCode.B_PROMOTION_COUPON_NULL);
        }
        CouponDTO dto = CouponConvertor.toCouponDTO(couponDO);
        getCouponScope(dto);
        return dto;
    }

    /**
     * 获取优惠券关系数据
     * 
     * @param dto
     */
    private void getCouponScope(CouponDTO dto) {
        if (dto.getCouponScope().equals(SCOPE_DISTRIBUTOR)) {
            List<CouponDistributorRelevanceDO> distributorRelevanceDOS =
                distributorRelevanceMapper.listDistributorRelevanceByCouponId(dto.getId());
            dto.setDistributors(CouponConvertor.toCouponDistributorScopeDTOList(distributorRelevanceDOS));
        }
        if (dto.getMaterialScope().equals(MATERIAL_SCOPE_2)) {
            List<CouponMaterialRelevanceDO> materialRelevanceDOS =
                materialRelevanceMapper.listMaterialRelevanceByCouponId(dto.getId());
            dto.setMaterials(CouponConvertor.toCouponMaterialScopeDTOList(materialRelevanceDOS));
        }
        if (dto.getModelScope().equals(MODEL_SCOPE_2)) {
            List<CouponModelRelevanceDO> modelRelevanceDOS =
                modelRelevanceMapper.listModelRelevanceByCouponId(dto.getId());
            dto.setModels(CouponConvertor.toCouponModelScopeDTOList(modelRelevanceDOS));
        }
    }

    /**
     * 获取券码列表
     * 
     * @param qry
     */
    public PageInfo<CouponCustomerDTO> listCouponNo(CouponCustomerListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<CouponCustomerDO> customerDOS = couponCustomerMapper.listByMap(qryMap);
        PageInfo pageInfo = new PageInfo(customerDOS);
        List<CouponCustomerDTO> dtos = CouponConvertor.toCouponCustomerDTOList(pageInfo.getList());
        pageInfo.setList(dtos);
        return pageInfo;
    }
}
