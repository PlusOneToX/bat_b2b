package com.bat.system.service.storesetting.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.bat.system.api.storesetting.dto.MobileQry;
import com.bat.system.api.storesetting.dto.data.MobileChildDTO;
import com.bat.system.api.storesetting.dto.data.MobileDTO;
import com.bat.system.api.storesetting.dto.data.MobileItemDTO;
import com.bat.system.dao.storesetting.MobileChildMapper;
import com.bat.system.dao.storesetting.MobileItemMapper;
import com.bat.system.dao.storesetting.MobileMapper;
import com.bat.system.dao.storesetting.MobilePointMapper;
import com.bat.system.dao.storesetting.dataobject.MobileChildDO;
import com.bat.system.dao.storesetting.dataobject.MobileDO;
import com.bat.system.dao.storesetting.dataobject.MobileItemDO;
import com.bat.system.dao.storesetting.dataobject.MobilePointDO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.dubboapi.goods.brand.api.GoodsBrandServiceRpc;
import com.bat.dubboapi.goods.brand.dto.data.UserBrandRpcDTO;
import com.bat.dubboapi.goods.classify.api.GoodsClassifyServiceRpc;
import com.bat.dubboapi.goods.classify.dto.data.ClassifyRpcDTO;
import com.bat.system.service.storesetting.constant.MobileModuleType;
import com.bat.system.service.storesetting.convertor.MobileConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/10 18:14
 */
@Component
@Slf4j
public class MobileQryExc {
    @Resource
    private MobileMapper mobileMapper;

    @Resource
    private MobileItemMapper mobileItemMapper;

    @Resource
    private MobileChildMapper mobileChildMapper;

    @Resource
    private MobilePointMapper mobilePointMapper;

    @DubboReference(check = false, timeout = 30000)
    private GoodsBrandServiceRpc goodsBrandServiceRpc;

    @DubboReference(check = false, timeout = 30000)
    private GoodsClassifyServiceRpc goodsClassifyServiceRpc;

    public MobileDTO getMobileById(Integer id) {
        MobileDO mobileDO = mobileMapper.selectByPrimaryKey(id);
        if (mobileDO != null) {
            MobileDTO dto = MobileConvertor.toMobileDTO(mobileDO);
            List<MobileItemDO> mobileItemDOS = mobileItemMapper.listByModuleId(dto.getId());
            List<MobileItemDTO> mobileItemDTOS = MobileConvertor.toMobileItemDTOList(mobileItemDOS);
            dto.setList(mobileItemDTOS);
            List<MobileChildDO> mobileChildDOS = mobileChildMapper.listByParentId(mobileDO.getId());
            List<MobileChildDTO> mobileChildDTOS = new ArrayList<>();
            for (MobileChildDO mobileChildDO : mobileChildDOS) {
                MobileChildDTO mobileChildDTO = new MobileChildDTO();
                BeanUtils.copyProperties(mobileChildDO, mobileChildDTO);
                List<MobilePointDO> mobilePointDOS = mobilePointMapper.listByMobileChildId(mobileChildDTO.getId());
                List<Integer> pointIds = mobilePointDOS.stream().map(a -> a.getPointId()).collect(Collectors.toList());
                if (mobileChildDTO.getAppointType() == MobileModuleType.APPOINT_TYPE_CATEGORY) {
                    List<ClassifyRpcDTO> classifyRpcDTOS = goodsClassifyServiceRpc.listByIds(pointIds).getData();
                    List<MobileChildDTO.Classify> classifies = new ArrayList<>();
                    for (ClassifyRpcDTO ClassifyRpcDTO : classifyRpcDTOS) {
                        MobileChildDTO.Classify classify = new MobileChildDTO.Classify();
                        BeanUtils.copyProperties(ClassifyRpcDTO, classify);
                        classifies.add(classify);
                    }
                    mobileChildDTO.setClassifies(classifies);
                }
                if (mobileChildDTO.getAppointType() == MobileModuleType.APPOINT_BRAND) {
                    List<MobileChildDTO.Brand> brands = new ArrayList<>();
                    List<UserBrandRpcDTO> userBrandRpcDTOS =
                        goodsBrandServiceRpc.getBrandByBrandIds(pointIds).getData();
                    for (UserBrandRpcDTO userBrandRpcDTO : userBrandRpcDTOS) {
                        MobileChildDTO.Brand brand = new MobileChildDTO.Brand();
                        BeanUtils.copyProperties(userBrandRpcDTO, brand);
                        brands.add(brand);
                    }
                    mobileChildDTO.setBrands(brands);
                }
                mobileChildDTOS.add(mobileChildDTO);
            }
            dto.setMobileChildDTOS(mobileChildDTOS);
            return dto;
        }
        return null;
    }

    public PageInfo<MobileDTO> listMobile(MobileQry qry) {
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<MobileDO> mobileDOS = mobileMapper.listAll(qry.getStatus(), qry.getModuleType());
        PageInfo pageInfo = new PageInfo(mobileDOS);
        List<MobileDTO> mobileDTOList = MobileConvertor.toMobileDTOList(pageInfo.getList());
        List<MobileDTO> collect = mobileDTOList.stream().peek(mobileDO -> {
            List<MobileItemDO> mobileItemDOS = mobileItemMapper.listByModuleId(mobileDO.getId());
            List<MobileItemDTO> mobileItemDTOS = MobileConvertor.toMobileItemDTOList(mobileItemDOS);
            mobileDO.setList(mobileItemDTOS);
            if (mobileDO.getModuleType() == MobileModuleType.GOODS_LIST.shortValue()) {
                List<MobileChildDO> mobileChildDOS = mobileChildMapper.listByParentId(mobileDO.getId());
                List<MobileChildDTO> mobileChildDTOS = new ArrayList<>();
                for (MobileChildDO mobileChildDO : mobileChildDOS) {
                    MobileChildDTO mobileChildDTO = new MobileChildDTO();
                    BeanUtils.copyProperties(mobileChildDO, mobileChildDTO);
                    mobileChildDTOS.add(mobileChildDTO);
                    List<MobilePointDO> mobilePointDOS = mobilePointMapper.listByMobileChildId(mobileChildDO.getId());
                    List<Integer> pointIds =
                        mobilePointDOS.stream().map(a -> a.getPointId()).collect(Collectors.toList());
                    mobileChildDTO.setPointIds(pointIds);
                }
                mobileDO.setMobileChildDTOS(mobileChildDTOS);
            }
        }).collect(Collectors.toList());
        pageInfo.setList(collect);
        return pageInfo;
    }

    public MobileDTO getMobileByModuleType(Short moduleType) {
        List<MobileDO> mobileDOS = mobileMapper.listByModuleType(moduleType);
        if (CollectionUtils.isNotEmpty(mobileDOS)) {
            // 如果有两组以上 取第一组
            MobileDO mobileDO = mobileDOS.get(0);
            MobileDTO dto = MobileConvertor.toMobileDTO(mobileDO);
            List<MobileItemDO> mobileItemDOS = mobileItemMapper.listByModuleId(dto.getId());
            List<MobileItemDTO> mobileItemDTOS = MobileConvertor.toMobileItemDTOList(mobileItemDOS);
            dto.setList(mobileItemDTOS);
            return dto;
        }
        return null;
    }
}
