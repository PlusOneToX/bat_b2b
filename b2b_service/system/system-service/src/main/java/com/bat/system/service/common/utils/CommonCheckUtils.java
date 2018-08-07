package com.bat.system.service.common.utils;

import java.util.List;
import java.util.Map;

import com.bat.system.api.base.SystemException;
import com.bat.system.service.logistics.constant.DistributorScopeConsts;
import com.bat.system.service.logistics.executor.ErrorCode;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/29 19:09
 */
public class CommonCheckUtils {
    /**
     * 检查可视范围 很多地方使用 抽离成公用检查
     *
     * @param cmd
     */
    public static void checkDistributorScope(Map<String, Object> cmd) {
        // 可视范围
        switch ((short)cmd.get("distributorScope")) {
            // 全部分销商
            case DistributorScopeConsts.ALL_DISTRIBUTOR:
                break;
            // 指定的分销商等级
            case DistributorScopeConsts.APPOINT_DISTRIBUTOR_GRADE:
                if (CollectionUtils.isEmpty((List<Integer>)cmd.get("gradeIds"))) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_GRADE_IDS_NULL);
                }
                break;
            // 指定的销售部门
            case DistributorScopeConsts.APPOINT_DEPARTMENT:
                if (CollectionUtils.isEmpty((List<Integer>)cmd.get("departmentIds"))) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_DEPARTMENT_IDS_NULL);
                }
                break;
            // 指定的业务员（后台用户）
            case DistributorScopeConsts.APPOINT_USER:
                if (CollectionUtils.isEmpty((List<Integer>)cmd.get("userIds"))) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_USER_IDS_NULL);
                }
                break;
            // 指定的分销商
            case DistributorScopeConsts.APPOINT_DISTRIBUTOR:
                if (CollectionUtils.isEmpty((List<Integer>)cmd.get("distributorIds"))) {
                    throw SystemException.buildException(ErrorCode.B_LOGISTICS_DISTRIBUTOR_IDS_NULL);
                }
                break;
            default:
                throw SystemException.buildException(ErrorCode.B_LOGISTICS_DISTRIBUTOR_SCOPE_ILLEGAL);
        }
    }
}
