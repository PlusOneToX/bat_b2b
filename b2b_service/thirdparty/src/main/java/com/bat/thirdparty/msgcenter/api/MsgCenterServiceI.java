package com.bat.thirdparty.msgcenter.api;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.api.dto.*;

import java.util.List;

public interface MsgCenterServiceI {
    PageInfo<MsgCenterDTO> list(MsgCenterQry qry);

    MsgCenterDTO detail(Integer id);

    void delete(Integer id);

    void add(MsgCenterCmd cmd);

    void update(MsgCenterCmd cmd);

    PageInfo<MsgCenterLogDTO> logList(MsgCenterLogQry qry);

    List<MsgCenterWechatTemplateDTO> wechatTemplateList();

    void updateWechatTemplate(List<MsgCenterWechatTemplateCmd> cmds);

    void logSend(Integer logId);
}
