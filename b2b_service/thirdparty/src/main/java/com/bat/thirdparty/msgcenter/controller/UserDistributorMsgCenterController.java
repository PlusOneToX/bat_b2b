package com.bat.thirdparty.msgcenter.controller;

import com.github.pagehelper.PageInfo;
import com.bat.thirdparty.common.base.BaseController;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.dto.ThirdIdDTO;
import com.bat.thirdparty.msgcenter.api.UserDistributorMsgCenterServiceI;
import com.bat.thirdparty.msgcenter.api.dto.DistributorMsgTemplateQry;
import com.bat.thirdparty.msgcenter.api.dto.MsgCenterWechatTemplateDTO;
import com.bat.thirdparty.msgcenter.api.dto.UserDistributorMsgCenterLogDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/thirdparty/v1/web/user/distributor/msgcenter")
@Api(tags = "消息中心分销商前台接口")
public class UserDistributorMsgCenterController extends BaseController {

    @Autowired
    private UserDistributorMsgCenterServiceI userDistributorMsgCenterServiceI;
    

    @ApiOperation(value = "查询当前用户消息列表")
    @GetMapping(value = "/list")
    public Response<PageInfo<UserDistributorMsgCenterLogDTO>> list() {
        PageInfo<UserDistributorMsgCenterLogDTO> pageInfo = userDistributorMsgCenterServiceI.list(Integer.valueOf(getUserId()));
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "查询当前用户未读消息数量")
    @GetMapping(value = "/not/read/num")
    public Response<Integer> notReadNum() {
        Integer num = userDistributorMsgCenterServiceI.notReadNum(Integer.valueOf(getUserId()));
        return Response.of(num);
    }

    @ApiOperation(value = "当前消息变为已读")
    @PutMapping(value = "/read")
    public Response read(@Valid @RequestBody ThirdIdDTO thirdIdDTO) {
        userDistributorMsgCenterServiceI.read(Integer.valueOf(getUserId()),thirdIdDTO.getId());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "全部消息变为已读")
    @PutMapping(value = "/read/all")
    public Response readAll() {
        userDistributorMsgCenterServiceI.readAll(Integer.valueOf(getUserId()));
        return Response.buildSuccess();
    }

    @ApiOperation(value = "微信消息模板")
    @PostMapping(value = "/wechat/template/list")
    public Response<List<MsgCenterWechatTemplateDTO>> wechatTemplateList(@Valid @RequestBody DistributorMsgTemplateQry qry) {
        List<MsgCenterWechatTemplateDTO> list = userDistributorMsgCenterServiceI.wechatTemplateList(qry);
        return Response.of(list);
    }

/*    @PostMapping(value = "/test")
    public Response wechatTemplateList(@Valid @RequestBody DistributorNewOrderMsgDTO dto) {
        wechatProgramMsgExe.sendFromDistributor(dto.getOrderId(), dto.getDistributorId(),
                MsgCenterConstant.MSG_TYPE_NEW);
        return null;
    }

    @PostMapping(value = "/test2")
    public Response wechatTemplateList2(@Valid @RequestBody DistributorNewOrderMsgDTO dto) {
        wechatProgramMsgExe.sendFromDistributor(null, dto.getDistributorId(), MsgCenterConstant.MSG_TYPE_EXAMINE);
        return null;
    }

    @PostMapping(value = "/test3")
    public Response wechatTemplateList3(@Valid @RequestBody DistributorNewOrderMsgDTO dto) {
        wechatProgramMsgExe.sendFromOrder(dto.getOrderId(), null, MsgCenterConstant.MSG_TYPE_STATUS);
        return null;
    }

    @PostMapping(value = "/test4")
    public Response wechatTemplateList4(@Valid @RequestBody DistributorNewOrderMsgDTO dto) {
        wechatProgramMsgExe.sendFromOrder(null, dto.getOrderId(), MsgCenterConstant.MSG_TYPE_DELIVERY);
        return null;
    }

    @PostMapping(value = "/test5")
    public Response wechatTemplateList5(@Valid @RequestBody DistributorNewOrderMsgDTO dto) {
        wechatProgramMsgExe.sendFromOrder(dto.getOrderId(), null, MsgCenterConstant.MSG_TYPE_UNPAID);
        return null;
    }*/



}
