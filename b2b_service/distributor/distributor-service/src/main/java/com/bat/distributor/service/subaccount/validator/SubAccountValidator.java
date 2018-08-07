package com.bat.distributor.service.subaccount.validator;

import static com.bat.distributor.service.common.financial.FinancialConstant.AMOUNT_TYPE_1;
import static com.bat.distributor.service.common.financial.FinancialConstant.AMOUNT_TYPE_2;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.bat.distributor.api.base.DistributorException;
import com.bat.distributor.api.distributor.dto.DistributorExtendDataCmd;
import com.bat.distributor.api.subaccount.dto.SubAccountAdminConfigCmd;
import com.bat.distributor.api.subaccount.dto.SubAccountLevelRatioDTO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountLevelDO;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import com.bat.distributor.service.distributor.executor.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.bat.distributor.service.common.CommonErrorCode;
import com.bat.distributor.service.common.DistributorConstant;
import com.bat.distributor.service.common.ExcelUtil;
import com.bat.distributor.service.common.MessageUtils;
import com.bat.distributor.service.common.financial.FinancialConstant;
import com.bat.distributor.service.common.subaccount.SubAccountConstant;
import com.bat.distributor.service.common.subaccount.SubAccountErrorCode;
import com.bat.distributor.service.subaccount.executor.DistributorSubAccountSalemanQryExe;
import com.bat.dubboapi.financial.account.dto.AccountWxDistributorRpcQryDTO;
import com.bat.dubboapi.financial.basesetting.api.FinancialDistributorAccountServiceRpc;
import com.bat.dubboapi.financial.common.Response;

@Component
public class SubAccountValidator {

    @Autowired
    private DistributorSubAccountSalemanQryExe distributorSubAccountSalemanQryExe;

    @DubboReference(check = false, timeout = 7000, retries = 0)
    private FinancialDistributorAccountServiceRpc financialDistributorAccountServiceRpc;

    /**
     * 校验延迟时间
     * 
     * @param subAccountAdminConfigCmd
     */
    public static void validateDelayTime(SubAccountAdminConfigCmd subAccountAdminConfigCmd) {
        if (SubAccountConstant.AGING_TYPE_DELAY.equals(subAccountAdminConfigCmd.getAgingType())
            && subAccountAdminConfigCmd.getDelayTime() == null) {
            throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_DELAY_TIME_NULL,
                MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_DELAY_TIME_NULL));
        }
        if (SubAccountConstant.AGING_TYPE_DELAY.equals(subAccountAdminConfigCmd.getAgingType())
            && subAccountAdminConfigCmd.getDelayTime().compareTo(BigDecimal.ZERO) <= 0) {
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_NUMBER_ILLEGAL);
        }
    }

    /**
     * 校验分账比例
     * 
     * @param levelRatioList
     */
    public static void validateRatio(List<SubAccountLevelRatioDTO> levelRatioList,
        List<DistributorSubAccountLevelDO> levelDOList) {
        BigDecimal sum = BigDecimal.ZERO;
        // 判断是否漏设置等级id
        Map<Integer, DistributorSubAccountLevelDO> levelDOMap = levelDOList.stream().collect(Collectors
            .toMap(DistributorSubAccountLevelDO::getId, distributorSubAccountLevelDO -> distributorSubAccountLevelDO));
        for (int x = 0; x < levelRatioList.size(); x++) {
            SubAccountLevelRatioDTO subAccountLevelRatioDTO = levelRatioList.get(x);
            sum = sum.add(subAccountLevelRatioDTO.getRatio());
            if (!levelDOMap.containsKey(subAccountLevelRatioDTO.getLevelId())) {
                throw DistributorException.buildException(CommonErrorCode.D_COMMON_ID_ERROR,
                    MessageUtils.get(CommonErrorCode.D_COMMON_ID_ERROR) + "【" + subAccountLevelRatioDTO.getLevelId()
                        + "】");
            }
        }
        if (sum.compareTo(new BigDecimal("100")) == 1) {
            throw DistributorException.buildException(CommonErrorCode.D_COMMON_NUMBER_PERCENTAGE_GREATER_THEN_HUNDRED);
        }

        Set<Integer> collect =
            levelRatioList.stream().map(SubAccountLevelRatioDTO::getLevelId).collect(Collectors.toSet());
        for (int x = 0; x < levelDOList.size(); x++) {
            if (!collect.contains(levelDOList.get(x).getId())) {
                throw DistributorException.buildException("【" + levelDOList.get(x).getLevelName() + "】"
                    + MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_LEVEL_NOT_SET_RATIO));
            }
        }

    }

    public static void validateAdminConfigParam(DistributorExtendDataCmd extendData,
                                                SubAccountAdminConfigCmd subAccountAdminConfigCmd) {
        if (DistributorConstant.DISTRIBUTOR_CUSTOMER_FLAG_NO.equals(extendData.getCustomerFlag())) {
            return;
        }
        if (!DistributorConstant.DISTRIBUTOR_CUSTOMER_MODE_SELF.equals(extendData.getCustomerMode())) {
            return;
        }
        if (extendData.getSubAccountFlag() == null) {
            throw DistributorException.buildException(ErrorCode.B_DISTRIBUTOR_SUB_ACCOUNT_FLAG_NULL);
        }
        if (DistributorConstant.DISTRIBUTOR_SUB_ACCOUNT_FLAG_NO.equals(extendData.getSubAccountFlag())) {
            return;
        }
        // 判断分账对象
        if (subAccountAdminConfigCmd == null) {
            throw DistributorException.buildException(ErrorCode.P_DISTRIBUTOR_SUB_ACCOUNT_CONFIG_NULL);
        }
        if (subAccountAdminConfigCmd.getLevelNameList() == null
            || subAccountAdminConfigCmd.getLevelNameList().size() == 0) {
            throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_LEVEL_NAME_LIST_NULL);
        }
        if (subAccountAdminConfigCmd.getAgingType() == null) {
            throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_AGING_TYPE_NULL);
        }
        // 时间校验
        validateDelayTime(subAccountAdminConfigCmd);
    }

    /**
     * 判断业务员电话号码是否重复
     * 
     * @param id
     * @param mobile
     * @param distributorId
     */
    public void validateSalemanMobile(Integer id, String mobile, Integer distributorId) {
        List<DistributorSubAccountSalemanDO> list =
            distributorSubAccountSalemanQryExe.listByCondition(distributorId, mobile, null, null);
        if (list == null || list.size() == 0) {
            return;
        }
        if (id != null && list.get(0).getId() - id == 0 && list.size() == 1) {
            // 前后属于同一个人
            return;
        }
        throw DistributorException.buildException(SubAccountErrorCode.D_SALEMAN_MOBILE_RELEVANCE_DISTRIBUTOR_EXISTS,
            MessageUtils.get(SubAccountErrorCode.D_SALEMAN_MOBILE_RELEVANCE_DISTRIBUTOR_EXISTS) + "【" + mobile + "】");
    }

    public static List<DistributorSubAccountSalemanDO> validImportData(Integer distributorId, String distributorName,
        Workbook workbook, List<DistributorSubAccountSalemanDO> salemanDOList,
        List<DistributorSubAccountLevelDO> levelDOList) {
        List<DistributorSubAccountSalemanDO> list = new ArrayList<>();
        Map<String, List<DistributorSubAccountSalemanDO>> salemanMap = new HashMap<>();
        Map<String, List<DistributorSubAccountSalemanDO>> salemanMobileMap = new HashMap<>();
        // 定义map、判断传进来的mobile是否重复
        Map<String, String> mobileMap = new HashMap<>();

        if (salemanDOList != null && salemanDOList.size() > 0) {
            salemanMap = salemanDOList.stream()
                .collect(Collectors.toMap(DistributorSubAccountSalemanDO::getName, sale -> Lists.newArrayList(sale),
                    (List<DistributorSubAccountSalemanDO> oldList, List<DistributorSubAccountSalemanDO> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
            salemanMobileMap = salemanDOList.stream()
                .collect(Collectors.toMap(DistributorSubAccountSalemanDO::getMobile, sale -> Lists.newArrayList(sale),
                    (List<DistributorSubAccountSalemanDO> oldList, List<DistributorSubAccountSalemanDO> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
        }

        Map<String, List<DistributorSubAccountLevelDO>> levelMap = new HashMap<>();
        if (levelDOList != null && levelDOList.size() > 0) {
            levelMap = levelDOList.stream().collect(
                Collectors.toMap(DistributorSubAccountLevelDO::getLevelName, level -> Lists.newArrayList(level),
                    (List<DistributorSubAccountLevelDO> oldList, List<DistributorSubAccountLevelDO> newList) -> {
                        oldList.addAll(newList);
                        return oldList;
                    }));
        }
        for (int numSheet = 0; numSheet < 1; numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            String sheetName = hssfSheet.getSheetName();
            int lastRowNum = hssfSheet.getLastRowNum() - hssfSheet.getFirstRowNum() + 1;// 获取所有的行项目

            for (int rowNum = 2; rowNum < lastRowNum; rowNum++) {
                Row xssfRow = hssfSheet.getRow(rowNum);
                Cell salemanNameCell = xssfRow.getCell(0);
                String salemanName = ExcelUtil.getValue(salemanNameCell);
                if (StringUtils.isBlank(salemanName)) {
                    throw DistributorException.buildException(
                        sheetName + "表第" + (rowNum + 1) + MessageUtils.get(CommonErrorCode.D_COMMON_NAME_NULL));
                }
                DistributorSubAccountSalemanDO accountSalemanDO = new DistributorSubAccountSalemanDO();
                accountSalemanDO.setName(salemanName);
                Cell typeCell = xssfRow.getCell(1);
                String type = ExcelUtil.getValue(typeCell);
                if (StringUtils.isBlank(type)) {
                    throw DistributorException.buildException(sheetName + "表第" + (rowNum + 1)
                        + MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_IDENTITY_TYPE_NULL));
                }
                if (SubAccountConstant.SALEMAN_TYPE_COMPANY_NAME.equals(type)) {
                    accountSalemanDO.setType(SubAccountConstant.SALEMAN_TYPE_COMPANY);
                }
                if (SubAccountConstant.SALEMAN_TYPE_PERSONAL_NAME.equals(type)) {
                    accountSalemanDO.setType(SubAccountConstant.SALEMAN_TYPE_PERSONAL);
                }
                if (accountSalemanDO.getType() == null) {
                    throw DistributorException.buildException(salemanName + "类型错误");
                }
                Cell mobileCell = xssfRow.getCell(2);
                String mobile = ExcelUtil.getValue(mobileCell);
                if (StringUtils.isBlank(mobile)) {
                    throw DistributorException.buildException(CommonErrorCode.D_COMMON_MOBILE_NULL);
                }
                accountSalemanDO.setMobile(mobile);
                List<DistributorSubAccountSalemanDO> doList = salemanMobileMap.get(mobile);
                if (doList != null && doList.size() > 0) {
                    throw DistributorException.buildException(CommonErrorCode.D_COMMON_MOBILE_EXIST,
                        MessageUtils.get(CommonErrorCode.D_COMMON_MOBILE_EXIST) + "【" + mobile + "】");
                }
                // 判断传进来的电话号码是否重复
                String s = mobileMap.get(mobile);
                if (StringUtils.isNotBlank(s)) {
                    throw DistributorException.buildException(CommonErrorCode.D_COMMON_MOBILE_EXIST,
                        MessageUtils.get(CommonErrorCode.D_COMMON_MOBILE_EXIST) + "【" + mobile + "】");
                }
                mobileMap.put(mobile, mobile);
                Cell levelCell = xssfRow.getCell(3);
                String level = ExcelUtil.getValue(levelCell);
                if (StringUtils.isBlank(level)) {
                    throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_LEVEL_NAME_NULL);
                }
                List<DistributorSubAccountLevelDO> levelDOS = levelMap.get(level);
                if (levelDOS == null || levelDOS.size() == 0) {
                    throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_LEVEL_NAME_ERROR,
                        MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_LEVEL_NAME_ERROR) + "【" + level + "】");
                }
                if (levelDOList.size() > 1) {
                    throw DistributorException.buildException(
                        SubAccountErrorCode.D_SUB_ACCOUNT_LEVEL_NAME_MORE_THEN_ONE,
                        MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_LEVEL_NAME_MORE_THEN_ONE) + "【" + level
                            + "】");
                }
                accountSalemanDO.setLevelId(levelDOS.get(0).getId());
                Cell parentCell = xssfRow.getCell(4);
                String parentName = ExcelUtil.getValue(parentCell);
                if (StringUtils.isNotBlank(parentName)) {
                    //
                    List<DistributorSubAccountSalemanDO> salemanDOS = salemanMap.get(parentName);
                    if (salemanDOS == null || salemanDOS.size() == 0) {
                        throw DistributorException.buildException(
                            SubAccountErrorCode.D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_ERROR,
                            MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_ERROR) + "【"
                                + parentName + "】");
                    }
                    if (salemanDOS.size() > 1) {
                        throw DistributorException.buildException(
                            SubAccountErrorCode.D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_MORE_THEN_ONE,
                            MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_PARENT_SALEMAN_NAME_MORE_THEN_ONE) + "【"
                                + parentName + "】");
                    }
                    accountSalemanDO.setParentId(salemanDOS.get(0).getId());
                } else {
                    accountSalemanDO.setParentId(0);
                }
                if (SubAccountConstant.SALEMAN_TYPE_PERSONAL.equals(accountSalemanDO.getType())) {
                    Cell mchidCell = xssfRow.getCell(5);
                    String mchid = ExcelUtil.getValue(mchidCell);
                    if (StringUtils.isBlank(mchid)) {
                        throw DistributorException.buildException(SubAccountErrorCode.D_MERCHANT_NUMBER_NULL,
                            MessageUtils.get(SubAccountErrorCode.D_MERCHANT_NUMBER_NULL) + "【" + parentName + "】");
                    }
                    accountSalemanDO.setMerchantNumber(mchid);
                }

            }
        }
        return list;
    }

    public void validateRatioSum(BigDecimal sum, Integer distributorId, Short amountType) {
        BigDecimal maxRatio = BigDecimal.ZERO;
        if (amountType.equals(AMOUNT_TYPE_1)) {
            Response<List<AccountWxDistributorRpcQryDTO>> response =
                financialDistributorAccountServiceRpc.listWxPayAccountByCondition(distributorId, null,
                    FinancialConstant.ACCOUNT_WX_DISTRIBUTOR_ACCOUNT_TYPE_SERVICE);
            if (!response.isSuccess()) {
                throw DistributorException.buildException(response.getErrCode(), response.getErrMessage());
            }
            // 判断等级分账比例和加起来是否大于服务商配置的比例
            List<AccountWxDistributorRpcQryDTO> rpcQryDTOS = response.getData();
            AccountWxDistributorRpcQryDTO rpcQryDTO = rpcQryDTOS.get(0);
            maxRatio = rpcQryDTO.getSubAccountRatio().multiply(new BigDecimal("100"));
        } else if (amountType.equals(AMOUNT_TYPE_2)) {
            // 按照利润金额 不受最大分账限制 最大100
            maxRatio = BigDecimal.valueOf(100.00);
        }
        // 前台填写的分账总和大于预设的最大分账总和
        if (sum.compareTo(maxRatio) > 0) {
            throw DistributorException.buildException(SubAccountErrorCode.D_SUB_ACCOUNT_RATIO_GREATER_THEN_PAY,
                MessageUtils.get(SubAccountErrorCode.D_SUB_ACCOUNT_RATIO_GREATER_THEN_PAY) + "【" + maxRatio + "】");
        }
    }
}
