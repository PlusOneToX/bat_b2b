package com.bat.thirdparty.msgcenter.service.executor;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.msgcenter.ThirdMsgCenterErrorCode;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.dao.*;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterLogDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterWechatTemplateDO;
import com.bat.dubboapi.distributor.common.Response;
import com.bat.dubboapi.distributor.distributor.api.DistributorServiceRpc;
import com.bat.dubboapi.distributor.distributor.dto.data.DistributorNameRpcDTO;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.common.MsgCenterConstant;
import com.bat.thirdparty.msgcenter.convertor.MsgConvertor;
import com.bat.thirdparty.msgcenter.dao.*;
import com.bat.thirdparty.msgcenter.dao.dataobject.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bat.thirdparty.msgcenter.common.MsgCenterConstant.*;

@Component
public class MsgCenterCmdExe {

    @Autowired
    private MsgCenterMapper msgCenterMapper;

    @Autowired
    private MsgCenterWechatTemplateMapper msgCenterWechatTemplateMapper;

    @Autowired
    private MsgCenterLogMapper msgCenterLogMapper;

    @Autowired
    private WechatProgramMsgExe wechatProgramMsgExe;

    @Autowired
    private MsgCenterAdminRelevanceMapper msgCenterAdminRelevanceMapper;

    @Autowired
    private MsgCenterDepartmentRelevanceMapper msgCenterDepartmentRelevanceMapper;

    @Autowired
    private MsgCenterDistributorGroupRelevanceMapper msgCenterDistributorGroupRelevanceMapper;

    @Autowired
    private MsgCenterDistributorRelevanceMapper msgCenterDistributorRelevanceMapper;

    @Autowired
    private MsgCenterScalePriceRelevanceMapper msgCenterScalePriceRelevanceMapper;

    @DubboReference(check = false, timeout = 50000)
    private DistributorServiceRpc distributorServiceRpc;

    public void delete(Integer id) {
        msgCenterMapper.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(MsgCenterCmd cmd) {
        MsgCenterDO msgCenterDO = new MsgCenterDO();
        BeanUtils.copyProperties(cmd, msgCenterDO);
        msgCenterDO.setCreateTime(new Date());
        msgCenterMapper.insert(msgCenterDO);
        saveMsgScope(msgCenterDO.getId(), cmd);
        List<DistributorNameRpcDTO> list = new ArrayList<>();
        //分销商登记
        if (cmd.getDistributorScope().equals(SCOPE_SCALE_PRICE)) {
            Response<List<DistributorNameRpcDTO>> response = distributorServiceRpc.listDistributorNameByDefaultScalePriceIdsAndOneTreeNode(cmd.getScalePriceIds());
            list = response.getData();
        } else if (cmd.getDistributorScope().equals(SCOPE_DISTRIBUTOR)) {
            Response<List<DistributorNameRpcDTO>> response = distributorServiceRpc.getDistributorNameByDistributorIds(cmd.getDistributorIds());
            list = response.getData();
        } else if (cmd.getDistributorScope().equals(SCOPE_DEPARTMENT)) {
            Response<List<DistributorNameRpcDTO>> response = distributorServiceRpc.listDistributorNameByDepartmentIdsAndOneTreeNode(cmd.getDepartmentIds());
            list = response.getData();
        } else if (cmd.getDistributorScope().equals(SCOPE_ADMIN)) {
            Response<List<DistributorNameRpcDTO>> response = distributorServiceRpc.listDistributorNameBySalesIdAndOneTreeNode(cmd.getAdminIds());
            list = response.getData();
        } else if (cmd.getDistributorScope().equals(SCOPE_DISTRIBUTOR_GROUP)) {
            Response<List<DistributorNameRpcDTO>> response = distributorServiceRpc.listNameByDistributorGroupIdsAndOneTreeNode(cmd.getDistributorGroupIds());
            list = response.getData();
        } else if (cmd.getDistributorScope().equals(SCOPE_ALL)) {
            Response<List<DistributorNameRpcDTO>> response = distributorServiceRpc.getAllDistributorNameOneTreeNode();
            list = response.getData();
        }
        if (list.size() > 0) {
            List<MsgCenterLogDO> msgCenterLogDOS=new ArrayList<>();
              for(DistributorNameRpcDTO distributorNameRpcDTO:list){
                  MsgCenterLogDO msgCenterLogDO = new MsgCenterLogDO();
                  msgCenterLogDO.setCenterId(msgCenterDO.getId());
                  msgCenterLogDO.setChannel(msgCenterDO.getChannel());
                  msgCenterLogDO.setTitle(msgCenterDO.getTitle());
                  msgCenterLogDO.setContent(msgCenterDO.getContent());
                  msgCenterLogDO.setPushSwitch(msgCenterDO.getPushSwitch());
                  msgCenterLogDO.setMiniLink(msgCenterDO.getMiniLink());
                  msgCenterLogDO.setPushFlag(ThirdCommonConstant.COMMON_FLAG_NO);
                  msgCenterLogDO.setReadFlag(ThirdCommonConstant.COMMON_FLAG_NO);
                  msgCenterLogDO.setPushTerminal(MsgCenterConstant.PUSH_TERMINAL_2);
                  msgCenterLogDO.setMsgType(MSG_TYPE_CUSTOMIZE);
                  msgCenterLogDO.setToUserId(distributorNameRpcDTO.getId());
                  msgCenterLogDO.setToUsername(distributorNameRpcDTO.getName());
                  msgCenterLogDO.setUserType(USER_TYPE_1);
                  msgCenterLogDO.setCreateTime(new Date());
                  msgCenterLogDO.setMiniBody("{}");
                  msgCenterLogDO.setPcLink(msgCenterDO.getPcLink());
                  msgCenterLogDOS.add(msgCenterLogDO);
              }
            msgCenterLogMapper.insertList(msgCenterLogDOS);
        }

    }



    private void saveMsgScope(Integer centerId, MsgCenterCmd cmd) {
        Short distributorScope = cmd.getDistributorScope();
        // 可视关系
        List list = MsgConvertor.toMsgCenterRelevance(centerId, cmd);
        if (list != null && list.size() > 0) {
            if (distributorScope.equals(SCOPE_SCALE_PRICE)) {
                msgCenterScalePriceRelevanceMapper.insertList(list);
            } else if (distributorScope.equals(SCOPE_DISTRIBUTOR)) {
                msgCenterDistributorRelevanceMapper.insertList(list);
            } else if (distributorScope.equals(SCOPE_DEPARTMENT)) {
                msgCenterDepartmentRelevanceMapper.insertList(list);
            } else if (distributorScope.equals(SCOPE_ADMIN)) {
                msgCenterAdminRelevanceMapper.insertList(list);
            } else if (distributorScope.equals(SCOPE_DISTRIBUTOR_GROUP)) {
                msgCenterDistributorGroupRelevanceMapper.insertList(list);
            }
        }
    }

    public void update(MsgCenterCmd cmd) {
        if (cmd.getId() == null) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.COMMON_ID_NULL);
        }
        MsgCenterDO msgCenterDO = new MsgCenterDO();
        BeanUtils.copyProperties(cmd, msgCenterDO);
        msgCenterDO.setUpdateTime(new Date());
        msgCenterMapper.updateByPrimaryKeySelective(msgCenterDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWechatTemplate(List<MsgCenterWechatTemplateCmd> cmds) {
        msgCenterWechatTemplateMapper.deleteAll();
        Date date = new Date();
        List<MsgCenterWechatTemplateDO> list = new ArrayList<>();
        for (MsgCenterWechatTemplateCmd cmd : cmds) {
            MsgCenterWechatTemplateDO msgCenterWechatTemplateDO = new MsgCenterWechatTemplateDO();
            BeanUtils.copyProperties(cmd, msgCenterWechatTemplateDO);
            msgCenterWechatTemplateDO.setCreateTime(date);
            list.add(msgCenterWechatTemplateDO);
        }
        if (list.size() > 0) {
            msgCenterWechatTemplateMapper.insertList(list);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void logSend(Integer logId) {
        MsgCenterLogDO msgCenterLogDO = msgCenterLogMapper.selectByPrimaryKey(logId);
        if (msgCenterLogDO.getPushFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
            throw ThirdPartyException.buildException(ThirdMsgCenterErrorCode.T_MSG_CENTER_HAS_SEND);
        }
        if(StringUtils.isBlank(msgCenterLogDO.getMiniBody())){
            throw ThirdPartyException.buildException(ThirdMsgCenterErrorCode.T_MSG_CENTER_SEND_NO_BODY);
        }
        SubscribeSend subscribeSend=JSONObject.parseObject(msgCenterLogDO.getMiniBody(), SubscribeSend.class);
        //处理发送逻辑
        String result= wechatProgramMsgExe.programMessageSend(subscribeSend);
        SendResult sendResult = JSONObject.parseObject(result, SendResult.class);
        if (sendResult.getErrCode() == 0) {
            msgCenterLogDO.setPushFlag(ThirdCommonConstant.COMMON_FLAG_YES);
        }else{
            msgCenterLogDO.setSendFailError(sendResult.getErrMsg());
        }
        msgCenterLogMapper.updateByPrimaryKey(msgCenterLogDO);
    }

    public void addLog(MsgCenterLogCmd cmd) {
        MsgCenterLogDO msgCenterLogDO = new MsgCenterLogDO();
        BeanUtils.copyProperties(cmd, msgCenterLogDO);
        msgCenterLogDO.setCreateTime(new Date());
        msgCenterLogMapper.insert(msgCenterLogDO);
        cmd.setId(msgCenterLogDO.getId());
    }

    public void updatePush(Integer id,Short pushFlag,String sendFailError) {
        MsgCenterLogDO msgCenterLogDO = new MsgCenterLogDO();
        msgCenterLogDO.setId(id);
        msgCenterLogDO.setPushFlag(pushFlag);
        msgCenterLogDO.setSendFailError(sendFailError);
        msgCenterLogMapper.updateByPrimaryKeySelective(msgCenterLogDO);
    }
}
