package com.bat.system.service.logistics.executor;

import java.util.Map;

import javax.annotation.Resource;

import com.bat.system.api.base.SystemException;
import com.bat.system.api.logistics.dto.FormulaCheckCmd;
import com.bat.system.api.logistics.dto.LogisticsCreateCmd;
import com.bat.system.api.logistics.dto.LogisticsUpdateCmd;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bat.system.service.logistics.constant.BillingMethodConsts;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/19 14:22
 */
@Component
public class CheckUtils {

    @Resource
    private LogisticsQryExc logisticsQryExc;

    /**
     * 检查参数 适用商品 包含定制商品时 工厂配送方式编号 不能为空
     *
     * @param map
     */
    public static void checkLogisticsFactoryId(Map<String, Object> map) {
        // // 定制商品2 工厂配送方式ID不能为空
        if (("2".equals(String.valueOf(map.get("UseRange"))) || "3".equals(String.valueOf(map.get("UseRange"))))
            && map.get("logisticsFactoryId") == null) {
            throw SystemException.buildException(ErrorCode.B_LOGISTICS_DIY_FACTORY_ID_NULL);
        }
    }

    /**
     * 检查 地区费用类型 和 支持的配送地区
     *
     * @param cmd
     */
    public void checkLogisticsCost(LogisticsCreateCmd cmd) {
        if (CollectionUtils.isEmpty(cmd.getLogisticsCost())) {
            throw SystemException.buildException(ErrorCode.B_LOGISTICS_COST_NULL);
        }
        cmd.getLogisticsCost().forEach(logisticsCostCmd -> {
            // 使用公式时，公式不能为空
            if (logisticsCostCmd.getFormulaFlag() != null && logisticsCostCmd.getFormulaFlag() == 1) {
                if (StringUtils.isBlank(logisticsCostCmd.getFormula())) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_FORMULA_NULL);
                }
                // 验证公式的正确性
                FormulaCheckCmd checkCmd = new FormulaCheckCmd();
                checkCmd.setFormula(logisticsCostCmd.getFormula());
                checkCmd.setPrice(10.00);
                checkCmd.setWeight(1000.00);
                checkCmd.setVolume(1000.00);
                try {
                    logisticsQryExc.verificationDistributionFormula(checkCmd);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_FORMULA_ERROR);
                }
            }
            // billingMethod(计费方式 重量1 体积2)
            switch (cmd.getBillingMethod()) {
                // 在重量计费的情况下 统一配送费用中的首重重量费用 续重重量费用不能为空
                case BillingMethodConsts.BILLING_METHOD_WEIGHT:
                    if (logisticsCostCmd.getFirstWeightCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_FIRST_WEIGHT_COST_NULL);
                    } else if (logisticsCostCmd.getAdditionalWeightCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_ADDITIONAL_WEIGHT_COST_NULL);
                    }
                    break;
                // 在体积计费的情况下 统一配送费用中的首重体积费用 续重体积费用不能为空
                case BillingMethodConsts.BILLING_METHOD_VOLUME:
                    if (logisticsCostCmd.getFirstVolumeCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_FIRST_VOLUME_COST_NULL);
                    } else if (logisticsCostCmd.getAdditionalVolumeCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_ADDITIONAL_VOLUME_COST_NULL);
                    }
                    break;
                default:
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_BILLING_METHOD_ILLEGAL);
            }
        });
    }

    /**
     * 检查 地区费用类型 和 支持的配送地区
     *
     * @param cmd
     */
    public void checkLogisticsCost(LogisticsUpdateCmd cmd) {
        if (CollectionUtils.isEmpty(cmd.getLogisticsCost())) {
            throw SystemException.buildException(ErrorCode.B_LOGISTICS_COST_NULL);
        }
        cmd.getLogisticsCost().forEach(logisticsCostCmd -> {
            // 使用公式时，公式不能为空
            if (logisticsCostCmd.getFormulaFlag() != null && logisticsCostCmd.getFormulaFlag() == 1) {
                if (StringUtils.isBlank(logisticsCostCmd.getFormula())) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_FORMULA_NULL);
                }
                // 验证公式的正确性
                FormulaCheckCmd checkCmd = new FormulaCheckCmd();
                checkCmd.setFormula(logisticsCostCmd.getFormula());
                checkCmd.setPrice(10.00);
                checkCmd.setWeight(1000.00);
                checkCmd.setVolume(1000.00);
                try {
                    logisticsQryExc.verificationDistributionFormula(checkCmd);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_FORMULA_ERROR);
                }
            }
            // billingMethod(计费方式 重量1 体积2)
            switch (cmd.getBillingMethod()) {
                // 在重量计费的情况下 统一配送费用中的首重重量费用 续重重量费用不能为空
                case BillingMethodConsts.BILLING_METHOD_WEIGHT:
                    if (logisticsCostCmd.getFirstWeightCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_FIRST_WEIGHT_COST_NULL);
                    } else if (logisticsCostCmd.getAdditionalWeightCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_ADDITIONAL_WEIGHT_COST_NULL);
                    }
                    break;
                // 在体积计费的情况下 统一配送费用中的首重体积费用 续重体积费用不能为空
                case BillingMethodConsts.BILLING_METHOD_VOLUME:
                    if (logisticsCostCmd.getFirstVolumeCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_FIRST_VOLUME_COST_NULL);
                    } else if (logisticsCostCmd.getAdditionalVolumeCost() == null) {
                        throw SystemException.buildException(ErrorCode.B_LOGISTICS_ADDITIONAL_VOLUME_COST_NULL);
                    }
                    break;
                default:
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_BILLING_METHOD_ILLEGAL);
            }
        });
    }

    /**
     * 检查计费方式 是重量计费 还是体积计费
     *
     * @param map
     */
    public static void checkBillingMethod(Map<String, Object> map) {
        // billingMethod(计费方式 重量1 体积2)
        switch ((short)map.get("billingMethod")) {
            // 在重量计费的情况下 首重重量与续重重量单位 不能为空
            case BillingMethodConsts.BILLING_METHOD_WEIGHT:
                if (map.get("firstWeight") == null) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_FIRST_WEIGHT_NULL);
                } else if (map.get("additionalWeight") == null) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_ADDITIONAL_WEIGHT_NULL);
                }
                break;
            // 在体积计费的情况下 首重体积与续重体积单位 不能为空
            case BillingMethodConsts.BILLING_METHOD_VOLUME:
                if (map.get("firstVolume") == null) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_FIRST_VOLUME_NULL);
                } else if (map.get("additionalVolume") == null) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_ADDITIONAL_VOLUME_NULL);
                }
                break;
            default:
                throw SystemException.buildException(ErrorCode.B_LOGISTICS_BILLING_METHOD_ILLEGAL);
        }
    }
}
