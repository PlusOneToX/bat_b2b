package com.bat.thirdparty.msgcenter.controller;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.common.base.BaseController;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.dto.ThirdIdDTO;
import com.bat.thirdparty.msgcenter.api.MsgCenterServiceI;
import com.bat.thirdparty.msgcenter.api.dto.*;
import com.bat.thirdparty.msgcenter.api.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/admin/msgcenter")
@Api(tags = "消息中心后台接口")
public class AdminMsgCenterController extends BaseController {

    @Autowired
    private MsgCenterServiceI msgCenterServiceI;

    @GetMapping(value = "/list")
    @ApiOperation(value = "消息列表")
    public Response<PageInfo<MsgCenterDTO>> list(@Valid MsgCenterQry qry) {
        PageInfo<MsgCenterDTO> pageInfo = msgCenterServiceI.list(qry);
        return Response.of(pageInfo);
    }

    @GetMapping(value = "/detail")
    @ApiOperation(value = "消息详情")
    public Response<MsgCenterDTO> detail(@Valid ThirdIdDTO thirdIdDTO) {
        MsgCenterDTO msgCenterDTO = msgCenterServiceI.detail(thirdIdDTO.getId());
        return Response.of(msgCenterDTO);
    }

    @DeleteMapping
    @ApiOperation(value = "消息删除")
    public Response delete(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        msgCenterServiceI.delete(thirdIdDTO.getId());
        return Response.buildSuccess();
    }

    @PostMapping
    @ApiOperation(value = "添加消息")
    public Response add(@Valid @RequestBody MsgCenterCmd cmd) {
        msgCenterServiceI.add(cmd);
        return Response.buildSuccess();
    }

    @PutMapping
    @ApiOperation(value = "修改消息")
    public Response update(@Valid @RequestBody MsgCenterCmd cmd) {
        msgCenterServiceI.update(cmd);
        return Response.buildSuccess();
    }

    @GetMapping(value = "/wechat/template/list")
    @ApiOperation(value = "微信消息模板")
    public Response<List<MsgCenterWechatTemplateDTO>> wechatTemplateList() {
        List<MsgCenterWechatTemplateDTO> list = msgCenterServiceI.wechatTemplateList();
        return Response.of(list);
    }

    @PutMapping(value = "/wechat/template")
    @ApiOperation(value = "微信消息模板更新")
    public Response updateWechatTemplate(@Valid @RequestBody List<MsgCenterWechatTemplateCmd> cmds) {
        msgCenterServiceI.updateWechatTemplate(cmds);
        return Response.buildSuccess();
    }


    @GetMapping(value = "/log/list")
    @ApiOperation(value = "消息发送日志列表")
    public Response<PageInfo<MsgCenterLogDTO>> logList(@Valid MsgCenterLogQry qry) {
        PageInfo<MsgCenterLogDTO> pageInfo = msgCenterServiceI.logList(qry);
        return Response.of(pageInfo);
    }

    @PutMapping(value = "/log/send/again")
    @ApiOperation(value = "消息日志再次推送")
    public Response logSend(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        msgCenterServiceI.logSend(thirdIdDTO.getId());
        return Response.buildSuccess();
    }


}
