package com.bat.promotion.service.check.convertor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.bat.promotion.service.common.Constant;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.bat.dubboapi.system.check.dto.data.CheckConfigRpcDTO;
import com.bat.promotion.api.check.dto.data.PromotionCheckDTO;
import com.bat.promotion.api.check.dto.data.PromotionCheckFlowDTO;
import com.bat.promotion.api.check.dto.data.PromotionCheckListDTO;
import com.bat.promotion.dao.check.dataobject.PromotionCheckDO;
import com.bat.promotion.dao.check.dataobject.PromotionCheckFlowDO;
import com.bat.promotion.dao.check.dataobject.PromotionCheckListDO;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/24 10:42
 */
public class PromotionCheckConvertor {

    public static List<PromotionCheckListDTO> toPromotionCheckListDTOList(List<PromotionCheckListDO> doList) {
        List<PromotionCheckListDTO> dtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(doList)) {
            doList.forEach(promotionCheckListDO -> {
                PromotionCheckListDTO dto = new PromotionCheckListDTO();
                BeanUtils.copyProperties(promotionCheckListDO, dto);
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    public static PromotionCheckDTO toPromotionCheckDTO(PromotionCheckDO checkDO,
        List<PromotionCheckFlowDO> checkFlowDOS) {
        PromotionCheckDTO checkDTO = new PromotionCheckDTO();
        BeanUtils.copyProperties(checkDO, checkDTO);
        List<PromotionCheckFlowDTO> checkFlows = new ArrayList<>();
        checkDTO.setCheckFlows(checkFlows);
        checkFlowDOS.forEach(checkFlowDO -> {
            PromotionCheckFlowDTO checkFlowDTO = new PromotionCheckFlowDTO();
            BeanUtils.copyProperties(checkFlowDO, checkFlowDTO);
            checkFlows.add(checkFlowDTO);
        });
        return checkDTO;
    }

    public static PromotionCheckDO toPromotionCheckDO(List<CheckConfigRpcDTO> checkFlows, Short promotionType,
        Integer promotionId, Date date, String userId, String userName) {
        CheckConfigRpcDTO checkConfigRpcDTO = checkFlows.get(0);
        PromotionCheckDO checkDO = new PromotionCheckDO();
        checkDO.setPromotionId(promotionId);
        checkDO.setPromotionType(promotionType);
        checkDO.setUserId(Integer.valueOf(userId));
        checkDO.setUserName(userName);
        checkDO.setCheckUserId(checkConfigRpcDTO.getCheckUser());
        checkDO.setCheckUserName(checkConfigRpcDTO.getCheckUserName());
        checkDO.setCheckType(Constant.CHECK_TYPE_1);
        checkDO.setCheckStatus(Constant.CHECK_STATUS_0);
        checkDO.setCreateTime(date);
        checkDO.setUpdateTime(date);
        return checkDO;
    }

    public static List<PromotionCheckFlowDO> toPromotionCheckFlowDOList(List<CheckConfigRpcDTO> checkFlows,
        Integer promotionCheckId, Date date) {
        List<PromotionCheckFlowDO> checkFlowDOS = new ArrayList<>();
        checkFlows = checkFlows.stream().sorted(Comparator.comparing(CheckConfigRpcDTO::getCheckOrder))
            .collect(Collectors.toList());
        checkFlows.forEach(checkFlow -> {
            PromotionCheckFlowDO checkFlowDO = new PromotionCheckFlowDO();
            checkFlowDO.setPromotionCheckId(promotionCheckId);
            checkFlowDO.setCheckSort(checkFlow.getCheckOrder());
            checkFlowDO.setUserId(checkFlow.getCheckUser());
            checkFlowDO.setUserName(checkFlow.getCheckUserName());
            checkFlowDO.setCheckStatus(Constant.CHECK_STATUS_0);
            checkFlowDO.setCreateTime(date);
            checkFlowDO.setUpdateTime(date);
            checkFlowDOS.add(checkFlowDO);
        });
        return checkFlowDOS;
    }

}
