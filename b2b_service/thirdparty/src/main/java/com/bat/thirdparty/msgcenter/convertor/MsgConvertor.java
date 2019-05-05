package com.bat.thirdparty.msgcenter.convertor;

import com.bat.thirdparty.msgcenter.api.dto.MsgCenterCmd;
import com.bat.thirdparty.msgcenter.common.MsgCenterConstant;
import com.bat.thirdparty.msgcenter.dao.dataobject.*;
import com.bat.thirdparty.msgcenter.dao.dataobject.*;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MsgConvertor {

    /**
     * 获取消息中心可视关系列表
     *
     * @param centerId
     * @param cmd
     * @return
     */
    public static List toMsgCenterRelevance(Integer centerId, MsgCenterCmd cmd) {
        List<Integer> scalePriceIds = cmd.getScalePriceIds();
        List<Integer> distributorIds = cmd.getDistributorIds();
        List<Integer> departmentIds = cmd.getDepartmentIds();
        List<Integer> adminIds = cmd.getAdminIds();
        List<Integer> distributorGroupIds = cmd.getDistributorGroupIds();
        if (cmd.getDistributorScope().equals(MsgCenterConstant.SCOPE_SCALE_PRICE) && !CollectionUtils.isEmpty(scalePriceIds)) {
            List<MsgCenterScalePriceRelevanceDO> scalePriceRelevanceDOS = new ArrayList<>();
            scalePriceIds.forEach(id -> {
                MsgCenterScalePriceRelevanceDO scalePriceRelevanceDO = new MsgCenterScalePriceRelevanceDO();
                scalePriceRelevanceDO.setCenterId(centerId);
                scalePriceRelevanceDO.setScalePriceId(id);
                scalePriceRelevanceDOS.add(scalePriceRelevanceDO);
            });
            return scalePriceRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(MsgCenterConstant.SCOPE_DISTRIBUTOR) && !CollectionUtils.isEmpty(distributorIds)) {
            List<MsgCenterDistributorRelevanceDO> distributorRelevanceDOS = new ArrayList<>();
            distributorIds.forEach(id -> {
                MsgCenterDistributorRelevanceDO distributorRelevanceDO = new MsgCenterDistributorRelevanceDO();
                distributorRelevanceDO.setCenterId(centerId);
                distributorRelevanceDO.setDistributorId(id);
                distributorRelevanceDOS.add(distributorRelevanceDO);
            });
            return distributorRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(MsgCenterConstant.SCOPE_DEPARTMENT) && !CollectionUtils.isEmpty(departmentIds)) {
            List<MsgCenterDepartmentRelevanceDO> departmentRelevanceDOS = new ArrayList<>();
            departmentIds.forEach(id -> {
                MsgCenterDepartmentRelevanceDO departmentRelevanceDO = new MsgCenterDepartmentRelevanceDO();
                departmentRelevanceDO.setCenterId(centerId);
                departmentRelevanceDO.setDepartmentId(id);
                departmentRelevanceDOS.add(departmentRelevanceDO);
            });
            return departmentRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(MsgCenterConstant.SCOPE_ADMIN) && !CollectionUtils.isEmpty(adminIds)) {
            List<MsgCenterAdminRelevanceDO> adminRelevanceDOS = new ArrayList<>();
            adminIds.forEach(id -> {
                MsgCenterAdminRelevanceDO adminRelevanceDO = new MsgCenterAdminRelevanceDO();
                adminRelevanceDO.setCenterId(centerId);
                adminRelevanceDO.setAdminId(id);
                adminRelevanceDOS.add(adminRelevanceDO);
            });
            return adminRelevanceDOS;
        } else if (cmd.getDistributorScope().equals(MsgCenterConstant.SCOPE_DISTRIBUTOR_GROUP)
                && !CollectionUtils.isEmpty(distributorGroupIds)) {
            List<MsgCenterDistributorGroupRelevanceDO> distributorGroupRelevanceDOS = new ArrayList<>();
            distributorGroupIds.forEach(id -> {
                MsgCenterDistributorGroupRelevanceDO distributorGroupRelevanceDO = new MsgCenterDistributorGroupRelevanceDO();
                distributorGroupRelevanceDO.setCenterId(centerId);
                distributorGroupRelevanceDO.setDistributorGroupId(id);
                distributorGroupRelevanceDOS.add(distributorGroupRelevanceDO);
            });
            return distributorGroupRelevanceDOS;
        } else {
            return null;
        }
    }
}
