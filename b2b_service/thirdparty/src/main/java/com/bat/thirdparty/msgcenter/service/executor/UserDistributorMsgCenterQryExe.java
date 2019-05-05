package com.bat.thirdparty.msgcenter.service.executor;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.msgcenter.api.dto.DistributorMsgTemplateQry;
import com.bat.thirdparty.msgcenter.api.dto.MsgCenterWechatTemplateDTO;
import com.bat.thirdparty.msgcenter.api.dto.UserDistributorMsgCenterLogDTO;
import com.bat.thirdparty.msgcenter.common.MsgCenterConstant;
import com.bat.thirdparty.msgcenter.dao.MsgCenterLogMapper;
import com.bat.thirdparty.msgcenter.dao.MsgCenterWechatTemplateMapper;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterLogDO;
import com.bat.thirdparty.msgcenter.dao.dataobject.MsgCenterWechatTemplateDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserDistributorMsgCenterQryExe {

    @Autowired
    private MsgCenterLogMapper msgCenterLogMapper;

    @Autowired
    private MsgCenterWechatTemplateMapper msgCenterWechatTemplateMapper;


    public PageInfo<UserDistributorMsgCenterLogDTO> list(Integer distributorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", distributorId);
        map.put("userType", MsgCenterConstant.USR_TYPE_B2B);
        map.put("sortType", MsgCenterConstant.SORT_TYPE_READ_CREATE);
        List<MsgCenterLogDO> list = msgCenterLogMapper.listByParams(map);
        PageInfo pageInfo = new PageInfo(list);
        List<UserDistributorMsgCenterLogDTO> dtoList = new ArrayList<>();
        for (MsgCenterLogDO msgCenterLogDO : list) {
            UserDistributorMsgCenterLogDTO msgCenterLogDTO = new UserDistributorMsgCenterLogDTO();
            BeanUtils.copyProperties(msgCenterLogDO, msgCenterLogDTO);
            dtoList.add(msgCenterLogDTO);
        }
        pageInfo.setList(dtoList);
        return pageInfo;

    }

    public Integer notReadNum(Integer distributorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", distributorId);
        map.put("userType", MsgCenterConstant.USR_TYPE_B2B);
        map.put("readFlag", ThirdCommonConstant.COMMON_FLAG_NO);
        int count = msgCenterLogMapper.countByParams(map);
        return count;
    }

    public List<MsgCenterWechatTemplateDTO> wechatTemplateList(DistributorMsgTemplateQry qry) {
        if (qry.getTypes() == null || qry.getTypes().size() == 0) {
            return new ArrayList<>();
        }
        List<MsgCenterWechatTemplateDO> list = msgCenterWechatTemplateMapper.list();

        List<MsgCenterWechatTemplateDTO> dtoList = new ArrayList<>();

        Map<Short, MsgCenterWechatTemplateDO> map = list.stream().collect(Collectors.toMap(MsgCenterWechatTemplateDO::getType, a -> a, (k1, k2) -> k1));

        for (Short type : qry.getTypes()) {

            MsgCenterWechatTemplateDO msgCenterWechatTemplateDO = map.get(type);

            if (msgCenterWechatTemplateDO != null) {
                MsgCenterWechatTemplateDTO msgCenterWechatTemplateDTO = new MsgCenterWechatTemplateDTO();
                BeanUtils.copyProperties(msgCenterWechatTemplateDO, msgCenterWechatTemplateDTO);
                dtoList.add(msgCenterWechatTemplateDTO);
            }
        }
        return dtoList;
    }
}
