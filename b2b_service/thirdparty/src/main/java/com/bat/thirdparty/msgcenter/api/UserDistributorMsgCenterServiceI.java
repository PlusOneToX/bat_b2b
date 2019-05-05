package com.bat.thirdparty.msgcenter.api;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.msgcenter.api.dto.UserDistributorMsgCenterLogDTO;
import com.bat.thirdparty.msgcenter.api.dto.DistributorMsgTemplateQry;
import com.bat.thirdparty.msgcenter.api.dto.MsgCenterWechatTemplateDTO;

import java.util.List;

public interface UserDistributorMsgCenterServiceI {

    PageInfo<UserDistributorMsgCenterLogDTO> list(Integer distributorId);

    Integer notReadNum(Integer distributorId);

    void read(Integer distributorId, Integer logId);

    void readAll(Integer distributorId);

    List<MsgCenterWechatTemplateDTO> wechatTemplateList(DistributorMsgTemplateQry qry);
}
