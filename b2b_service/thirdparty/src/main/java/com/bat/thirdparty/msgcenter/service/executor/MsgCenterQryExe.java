package com.bat.thirdparty.msgcenter.service.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.dao.*;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterLogDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterWechatTemplateDO;

import static com.bat.thirdparty.msgcenter.common.MsgCenterConstant.*;

@Component
public class MsgCenterQryExe {

    @Autowired
    private MsgCenterMapper msgCenterMapper;

    @Autowired
    private MsgCenterWechatTemplateMapper msgCenterWechatTemplateMapper;

    @Autowired
    private MsgCenterLogMapper msgCenterLogMapper;

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

    public PageInfo<MsgCenterDTO> list(MsgCenterQry qry) {
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<MsgCenterDO> list = msgCenterMapper.listByParams(map);
        PageInfo pageInfo = new PageInfo(list);
        List<MsgCenterDTO> dtoList = new ArrayList<>();
        for (MsgCenterDO msgCenterDO : list) {
            MsgCenterDTO msgCenterDTO = new MsgCenterDTO();
            BeanUtils.copyProperties(msgCenterDO, msgCenterDTO);
            dtoList.add(msgCenterDTO);
        }
        pageInfo.setList(dtoList);
        return pageInfo;
    }

    public MsgCenterDTO detail(Integer id) {
        MsgCenterDO msgCenterDO = msgCenterMapper.selectByPrimaryKey(id);
        if (msgCenterDO == null) {
            return null;
        }
        MsgCenterDTO msgCenterDTO = new MsgCenterDTO();
        BeanUtils.copyProperties(msgCenterDO, msgCenterDTO);
        getCenterScopeIds(msgCenterDTO);
        return msgCenterDTO;
    }

    /**
     * 获取消息中心可视关联关系
     *
     * @param msgCenterDTO
     */
    public void getCenterScopeIds(MsgCenterDTO msgCenterDTO) {
        Integer canterId = msgCenterDTO.getId();
        if (msgCenterDTO.getDistributorScope() == null) {
            return;
        }
        if (msgCenterDTO.getDistributorScope().equals(SCOPE_SCALE_PRICE)) {
            msgCenterDTO.setScalePriceIds(msgCenterScalePriceRelevanceMapper.listScalePriceIdByCenterId(canterId));
        } else if (msgCenterDTO.getDistributorScope().equals(SCOPE_DISTRIBUTOR)) {
            msgCenterDTO
                    .setDistributorIds(msgCenterDistributorRelevanceMapper.listDistributorIdByCenterId(canterId));
        } else if (msgCenterDTO.getDistributorScope().equals(SCOPE_DEPARTMENT)) {
            msgCenterDTO.setDepartmentIds(msgCenterDepartmentRelevanceMapper.listDepartmentIdByCenterId(canterId));
        } else if (msgCenterDTO.getDistributorScope().equals(SCOPE_ADMIN)) {
            msgCenterDTO.setAdminIds(msgCenterAdminRelevanceMapper.listAdminIdsByCenterId(canterId));
        } else if (msgCenterDTO.getDistributorScope().equals(SCOPE_DISTRIBUTOR_GROUP)) {
            msgCenterDTO.setDistributorGroupIds(msgCenterDistributorGroupRelevanceMapper.listDistributorGroupIdByCenterId(canterId));
        }
    }


    public List<MsgCenterWechatTemplateDTO> wechatTemplateList() {
        List<MsgCenterWechatTemplateDO> list = msgCenterWechatTemplateMapper.list();
        List<MsgCenterWechatTemplateDTO> dtoList = new ArrayList<>();
        for (MsgCenterWechatTemplateDO msgCenterWechatTemplateDO : list) {
            MsgCenterWechatTemplateDTO msgCenterWechatTemplateDTO = new MsgCenterWechatTemplateDTO();
            BeanUtils.copyProperties(msgCenterWechatTemplateDO, msgCenterWechatTemplateDTO);
            dtoList.add(msgCenterWechatTemplateDTO);
        }
        return dtoList;
    }

    public PageInfo<MsgCenterLogDTO> logList(MsgCenterLogQry qry) {
        Map<String,Object> map = new HashMap<>(BeanMap.create(qry));
        map.put("startTime", qry.getStartTime());
        map.put("endTime", qry.getEndTime());
        PageHelper.startPage(qry.getPage(), qry.getSize());
        List<MsgCenterLogDO> list = msgCenterLogMapper.listByParams(map);
        PageInfo pageInfo = new PageInfo(list);
        List<MsgCenterLogDTO> dtoList = new ArrayList<>();
        for (MsgCenterLogDO msgCenterLogDO : list) {
            MsgCenterLogDTO msgCenterLogDTO = new MsgCenterLogDTO();
            BeanUtils.copyProperties(msgCenterLogDO, msgCenterLogDTO);
            dtoList.add(msgCenterLogDTO);
        }
        pageInfo.setList(dtoList);
        return pageInfo;
    }

}
