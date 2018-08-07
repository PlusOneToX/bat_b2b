package com.bat.distributor.service.Message;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.distributor.api.message.MessageSendServiceI;
import com.bat.distributor.dao.distributor.dataobject.DistributorDO;
import com.bat.distributor.mq.dto.CommonLogDTO;
import com.bat.distributor.mq.dto.DistributorNameChangeDTO;
import com.bat.distributor.mq.service.SenderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bat.distributor.service.common.DistributorConfig;
import com.bat.dubboapi.thirdparty.mongodb.dto.data.DistributorLogDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/6/4 9:07
 */
@Slf4j
@Component
public class MessageSendService implements MessageSendServiceI {

    @Resource
    private SenderService service;

    @Resource
    private HttpServletRequest request;

    @Resource
    private DistributorConfig distributorConfig;

    /**
     * 分销商名称修改消息
     * 
     * @param changeMap
     */
    public void distributorNameChange(Map<String, List<Object>> changeMap, DistributorDO afterDO) {
        List<Object> names = changeMap.get("name");
        List<Object> companyNames = changeMap.get("companyName");
        if ((names != null && names.size() > 0) || (companyNames != null && companyNames.size() > 0)) {
            DistributorNameChangeDTO nameChange = new DistributorNameChangeDTO();
            nameChange.setId(afterDO.getId());
            nameChange.setName(afterDO.getName());
            nameChange.setCompanyName(afterDO.getCompanyName());
            service.sendObject(nameChange, "distributorNameChange", "distributorNameChange-" + nameChange.getId());
        }
    }

    /**
     * 发送分销商日志
     */
    private void distributorLog(Integer distributorId, String operateSource, Integer operateId, String operator,
        String operateType, String operateDes, String operateData, Date time) {
        DistributorLogDTO dto = MessageConvertor.toDistributorLogDTO(distributorId, operateSource, operateId, operator,
            operateType, operateDes, operateData, time);
        service.sendObject(dto, "distributorLog", "distributorLog-" + dto.getDistributorId());
    }

    /**
     * 发送分销商日志
     */
    public void distributorLogPackage(Integer distributorId, String operateType, String operateDes,
        String operateData) {
        try {
            Integer userId = null;
            String platform = "";
            String userName = "";
            try {
                String userIdStr = request.getHeader("userId");
                if (StringUtils.isNotBlank(userIdStr)) {
                    userId = Integer.valueOf(userIdStr);
                }
                platform = request.getHeader("platform");
                userName = getUserName();
            } catch (Exception e) {
                userName = "系统";
                log.error("从request上获取不到信息:{}", e.getMessage());
            }
            distributorLog(distributorId, platform, userId, userName, operateType, operateDes, operateData, new Date());
        } catch (Exception e) {
            log.error("记录分销商日志出现异常:{}", e);
        }
    }

    @Override
    public void commonLog(String businessModule, String businessFunction, Integer businessId, String operateSource,
        Integer operateId, String operator, String operateType, String operateDes, String operateData, Date time) {
        CommonLogDTO dto = MessageConvertor.toCommonLogDTOList(businessModule, businessFunction, businessId,
            operateSource, operateId, operator, operateType, operateDes, operateData, time);
        service.sendObject(dto, "commonLog", "commonLog-" + dto.getOperateId());
    }

    /**
     * 发送同步erp消息
     * 
     * @param distributorId
     */
    public void distributorSyncErp(Integer distributorId) {
        service.sendObject(distributorId, "distributorSyncErp", "distributorSyncErp-" + distributorId);
    }


    /**
     * 发送分销商审核通知消息
     *
     * @param distributorId
     */
    public void distributorExamineMsg(Integer distributorId) {
        service.sendObject(distributorId, "distributorExamineMsg", "distributorExamineMsg-" + distributorId);
    }

    private String getUserName() {
        String userName = request.getHeader("userName");
        if (StringUtils.isNotBlank(userName)) {
            try {
                return URLDecoder.decode(userName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 分销商修改消息
     *
     * @param distributorId
     */
    public void updateDistributorVisibleByDistributorId(Integer distributorId) {
        service.sendObject(distributorId, "updateDistributorVisibleByDistributorId",
            "updateDistributorVisibleByDistributorId-" + distributorId,
            distributorConfig.getUpdateDistributorVisibleTime());
    }
}
