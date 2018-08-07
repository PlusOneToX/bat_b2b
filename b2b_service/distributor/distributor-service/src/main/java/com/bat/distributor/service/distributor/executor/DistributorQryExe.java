package com.bat.distributor.service.distributor.executor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.base.BaseId;
import com.bat.distributor.api.base.BaseIds;
import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import com.bat.distributor.api.subaccount.DistributorSubAccountAdminConfigServiceI;
import com.bat.distributor.api.subaccount.dto.SubAccountAdminConfigCmd;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.api.distributor.dto.*;
import com.bat.distributor.api.distributor.dto.data.*;
import com.bat.distributor.dao.distributor.*;
import com.bat.distributor.dao.distributor.dataobject.*;
import com.bat.distributor.service.common.CheckMapConfig;
import com.bat.distributor.service.common.DistributorConstant;
import com.bat.distributor.service.distributor.convertor.DistributorCheckConvertor;
import com.bat.distributor.service.distributor.convertor.DistributorConvertor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bat.distributor.service.common.Constant.*;
import static com.bat.distributor.service.trade.executor.ErrorCode.B_DISTRIBUTOR_TRADE_NULL;

@Component
public class DistributorQryExe {

    @Resource
    private DistributorMapper mapper;
    @Resource
    private DistributorSalesAreaMapper distributorSalesAreaMapper;
    @Resource
    private DistributorAddressMapper distributorAddressMapper;
    @Resource
    private DistributorBusinessMapper distributorBusinessMapper;
    @Resource
    private DistributorContactsMapper distributorContactsMapper;
    @Resource
    private DistributorExtendDataMapper distributorExtendDataMapper;
    @Resource
    private DistributorFinancialMapper distributorFinancialMapper;
    @Resource
    private DistributorSpecialGoodsMapper distributorSpecialGoodsMapper;
    @Resource
    private DistributorOneScalePriceMapper distributorOneScalePriceMapper;
    @Resource
    private DistributorRpcQryExe rpcQryExe;
    @Resource
    private DistributorCheckMapper distributorCheckMapper;
    @Resource
    private DistributorCheckFlowMapper distributorCheckFlowMapper;
    @Resource
    CheckMapConfig checkMapConfig;
    @Resource
    DistributorCheckConvertor checkConvertor;

    @Autowired
    private DistributorSubAccountAdminConfigServiceI distributorSubAccountAdminConfigServiceI;

    /**
     * 查询一级分销商列表（分页）
     * 
     * @param qry
     * @return
     */
    public PageInfo<DistributorOneListDTO> executeOneList(DistributorOneListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DistributorOneListDO> distributorListDOList = mapper.listOneDistributor(qryMap);
        PageInfo pageInfo = new PageInfo(distributorListDOList);
        List<DistributorOneListDTO> distributorListDTOList =
            DistributorConvertor.toDistributorOneListDTOList(pageInfo.getList());
        pageInfo.setList(distributorListDTOList);
        return pageInfo;
    }

    /**
     * 查询分销商列表（分页，包含所有的分销商）
     *
     * @param qry
     * @return
     */
    public PageInfo<DistributorListDTO> executeList(DistributorListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DistributorListDO> distributorListDOList = mapper.listDistributor(qryMap);
        PageInfo pageInfo = new PageInfo(distributorListDOList);
        List<DistributorListDTO> distributorListDTOList =
            DistributorConvertor.toDistributorListDTOList(pageInfo.getList());
        pageInfo.setList(distributorListDTOList);
        return pageInfo;
    }

    /**
     * 查询多级分销商列表（分页）
     *
     * @param qry
     * @return
     */
    public PageInfo<DistributorNextListDTO> executeNextList(DistributorNextListQry qry) {
        BeanMap qryMap = BeanMap.create(qry);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DistributorNextListDO> distributorListDOList = mapper.listNextDistributor(qryMap);
        PageInfo pageInfo = new PageInfo(distributorListDOList);
        List<DistributorNextListDTO> distributorListDTOList =
            DistributorConvertor.toDistributorNextListDTO(pageInfo.getList());
        pageInfo.setList(distributorListDTOList);
        return pageInfo;
    }

    /**
     * 根据分销商ID查询分销商详情
     * 
     * @param qry
     * @return
     */
    public DistributorDTO execute(DistributorId qry) {
        return execute(qry.getId());
    }

    /**
     * 根据分销商ID查询分销商详情
     *
     * @param distributorId
     * @return
     */
    public DistributorDTO execute(Integer distributorId) {
        DistributorDO distributorDO = mapper.selectByPrimaryKey(distributorId);
        if (distributorDO == null) {
            throw DistributorException.buildException(B_DISTRIBUTOR_TRADE_NULL);
        }
        DistributorDTO dto = DistributorConvertor.toDistributorDTO(distributorDO);
        // 销售区域
        List<DistributorSalesAreaDO> salesAreaDOS = distributorSalesAreaMapper.listByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(salesAreaDOS)) {
            dto.setSalesAreaIds(DistributorConvertor.toDistributorSalesAreaIds(salesAreaDOS));
        }
        // 分销商地址(一般只有一个)
        List<DistributorAddressDO> addressDOS =
            distributorAddressMapper.listByDistributorIdAndAddressType(distributorId, ADDRESS_TYPE_1);
        if (!CollectionUtils.isEmpty(addressDOS)) {
            dto.setAddress(DistributorConvertor.toDistributorAddressDTO(addressDOS.get(0)));
        }
        // 分销商业务数据
        DistributorBusinessDO businessDO = distributorBusinessMapper.getByDistributorId(distributorId);
        if (businessDO != null) {
        } else {
            businessDO = new DistributorBusinessDO();
        }
        //分销商分组处理(去除前后逗号)
        if (StringUtils.isNotBlank(businessDO.getDistributorGroupIds()) &&businessDO.getDistributorGroupIds().length() >=3) {
            String distributorGroupIds = businessDO.getDistributorGroupIds();
            distributorGroupIds = distributorGroupIds.substring(0, distributorGroupIds.length() - 1);
            distributorGroupIds = distributorGroupIds.substring(1);
            businessDO.setDistributorGroupIds(distributorGroupIds);
        }
        dto.setBusiness(DistributorConvertor.toDistributorBusinessDTO(businessDO));
        // 分销商价格等级列表
        List<DistributorOneScalePriceDO> scalePriceDOS =
            distributorOneScalePriceMapper.listByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(scalePriceDOS)) {
            Optional<DistributorOneScalePriceDO> defaultScalePrice = scalePriceDOS.stream()
                .filter(scalePriceDO -> (scalePriceDO.getBrandId() == null || scalePriceDO.getBrandId() == 0)
                    && (scalePriceDO.getCategoryId() == null || scalePriceDO.getCategoryId() == 0))
                .findFirst();
            if (defaultScalePrice != null && defaultScalePrice.isPresent()) {
                DistributorOneScalePriceDO scalePriceDO = defaultScalePrice.get();
                dto.getBusiness().setScalePriceId(scalePriceDO.getScalePriceId());
                dto.getBusiness().setDistributionScalePriceId(scalePriceDO.getDistributionScalePriceId());
            }
            scalePriceDOS = scalePriceDOS.stream()
                .filter(scalePriceDO -> (scalePriceDO.getBrandId() != null && scalePriceDO.getBrandId() != 0))
                .collect(Collectors.toList());
            dto.setScalePrices(DistributorConvertor.toDistributorOneScalePriceDTOList(scalePriceDOS));
        }
        // 分销商联系人列表
        List<DistributorContactsDO> contactsDOS = distributorContactsMapper.listByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(contactsDOS)) {
            dto.setContacts(DistributorConvertor.toDistributorContactsDTOList(contactsDOS));
        }
        // 分销商扩展数据（第三方编码（ERP等））
        DistributorExtendDataDO extendDataDO = distributorExtendDataMapper.getByDistributorId(distributorId);
        if (extendDataDO != null) {
            dto.setExtendData(DistributorConvertor.toDistributorExtendDataDTO(extendDataDO));
        }
        // 分销商财务信息
        DistributorFinancialDO financialDO = distributorFinancialMapper.getByDistributorId(distributorId);
        if (financialDO != null) {
            dto.setFinancial(DistributorConvertor.toDistributorFinancialDTO(financialDO));
        }
        // 分销商特价商品
        List<DistributorSpecialGoodsDO> specialGoodsDOS =
            distributorSpecialGoodsMapper.listByDistributorId(distributorId);
        if (!CollectionUtils.isEmpty(specialGoodsDOS)) {
            List<DistributorSpecialGoodsDTO> specialGoodsDTOS =
                DistributorConvertor.toDistributorSpecialGoodsDTOList(specialGoodsDOS);
            rpcQryExe.listSpecialGoodsDTO(specialGoodsDTOS);
            dto.setSpecialGoods(specialGoodsDTOS);
        }
        // 当分销级数>1时需获取上级分销商
        if (dto.getTreeNode().intValue() > 1) {
            DistributorDO ancestorDO = mapper.selectAncestorByDescendantId(distributorId);
            dto.setUpName(ancestorDO.getName());
        }
        //分账
        //处理分账
        if (DistributorConstant.DISTRIBUTOR_CUSTOMER_FLAG_YES.equals(extendDataDO.getCustomerFlag())
                && DistributorConstant.DISTRIBUTOR_CUSTOMER_MODE_SELF.equals(extendDataDO.getCustomerMode())
                && DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_YES.equals(extendDataDO.getSubAccountFlag())
        ) {

            SubAccountAdminConfigCmd subAccountAdminConfigCmd = distributorSubAccountAdminConfigServiceI.detailByDistributorId(distributorId);
            dto.setSubAccountAdminConfigCmd(subAccountAdminConfigCmd);
        }
        return dto;
    }

    public DistributorBusinessDO findBusinessByCheck(Integer chekId) {
        DistributorCheckDO checkDO = distributorCheckMapper.selectByPrimaryKey(chekId);
        if (checkDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_NULL);
        }
        return distributorBusinessMapper.getByDistributorId(checkDO.getDistributorId());
    }
    /**
     * 根据ids查找分销商基本数据
     * 
     * @param qry
     * @return
     */
    public List<DistributorIdsDTO> distributorIds(BaseIds qry) {
        List<DistributorIdsDO> distributorIdsDOS = mapper.distributorIds(qry.getIds());
        List<DistributorIdsDTO> dtos = DistributorConvertor.toDistributorIdsDTOList(distributorIdsDOS);
        return dtos;
    }

    /**
     * 根据分销商id查询分销商业务数据
     * 
     * @param distributorId
     * @return
     */
    public DistributorBusinessDO getDistributorBusinessDOByDistributorId(Integer distributorId) {
        return distributorBusinessMapper.getByDistributorId(distributorId);
    }

    /**
     * 分销商资料审批列表
     *
     * @param qry
     * @return
     */
    public PageInfo<DistributorCheckListDTO> listDistributorCheck(DistributorCheckListQry qry, String userId) {
        BeanMap beanMap = BeanMap.create(qry);
        Map<String, Object> qryMap = new HashMap();
        qryMap.putAll(beanMap);
        qryMap.put("userId", userId);
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<DistributorCheckListDO> distributorCheckListDOS = null;
        // 标签类型：1,我发起的 2,待我审批 3,我审批的
        if (qry.getLabelType().equals(LABEL_TYPE_1)) {
            distributorCheckListDOS = distributorCheckMapper.listByUserId(qryMap);
        } else {
            distributorCheckListDOS = distributorCheckMapper.listByCheckUserId(qryMap);
        }
        PageInfo pageInfo = new PageInfo(distributorCheckListDOS);
        List<DistributorCheckListDTO> distributorCheckListDTOS =
            DistributorConvertor.toDistributorCheckListDTOList(pageInfo.getList());
        pageInfo.setList(distributorCheckListDTOS);
        return pageInfo;
    }

    /**
     * 分销商资料审批详情
     *
     * @param qry
     * @return
     */
    public DistributorCheckDTO getDistributorCheck(BaseId qry) {
        DistributorCheckDO checkDO = distributorCheckMapper.selectByPrimaryKey(qry.getId());
        if (checkDO == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_CHECK_NULL);
        }
        List<DistributorCheckFlowDO> checkFlowDOS = distributorCheckFlowMapper.listByDistributorCheckId(qry.getId());
        DistributorDTO distributorDTO = null;
        List<DistributorCheckContentDTO> checkContentDTOS = null;
        if (checkDO.getCheckType().equals(CHECK_TYPE_1)) {
            distributorDTO = execute(checkDO.getDistributorId());
        } else if (checkDO.getCheckType().equals(CHECK_TYPE_2)) {
            checkContentDTOS =
                checkConvertor.toDistributorCheckContentDTOList(checkDO.getCheckContent(), checkMapConfig.getMap());
        }
        DistributorCheckDTO checkDTO =
            checkConvertor.toDistributorCheckDTO(checkDO, checkFlowDOS, distributorDTO, checkContentDTOS);
        return checkDTO;
    }

    public DistributorDO getDistributorById(Integer distributorId) {
        return mapper.selectByPrimaryKey(distributorId);
    }
}
