package com.bat.promotion.web.groupseckill;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.promotion.api.groupseckill.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.promotion.api.base.BaseId;
import com.bat.promotion.api.groupseckill.GroupSeckillServiceI;
import com.bat.promotion.api.groupseckill.dto.*;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillDTO;
import com.bat.promotion.api.groupseckill.dto.data.GroupSeckillListDTO;
import com.bat.promotion.web.annotation.SysLog;
import com.bat.promotion.web.base.BaseController;
import com.bat.promotion.web.base.Response;
import com.bat.promotion.web.constants.CommonLogTypeConstantDTO;
import com.bat.promotion.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "拼团秒杀后台接口", value = "GroupSeckillController")
@RestController
@RequestMapping("/promotion/v1/web/admin/groupseckill")
public class GroupSeckillController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(GroupSeckillController.class);

    @Resource
    private GroupSeckillServiceI service;

    @ApiOperation(value = "根据搜索条件查找拼团秒杀列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<GroupSeckillListDTO>> listGroupSeckill(@Valid GroupSeckillListQry qry) {
        PageInfo<GroupSeckillListDTO> pageInfo = service.listGroupSeckill(qry);
        return Response.of(pageInfo);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.GroupSeckill,
        value = CommonLogTypeConstantDTO.GroupSeckillAdd)
    @ApiOperation(value = "新增拼团秒杀活动")
    @PostMapping()
    public Response createGroupSeckill(@RequestBody @Valid GroupSeckillCmd cmd) {
        service.createGroupSeckill(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.GroupSeckill,
        value = CommonLogTypeConstantDTO.GroupSeckillUpdate)
    @ApiOperation(value = "修改拼团秒杀活动(草稿状态的拼团秒杀活动修改)")
    @PutMapping()
    public Response updateGroupSeckill(@RequestBody @Valid GroupSeckillCmd cmd) {
        service.updateGroupSeckill(cmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据id查询拼团秒杀活动详情")
    @GetMapping()
    public Response<GroupSeckillDTO> getGroupSeckill(@Valid BaseId qry) {
        GroupSeckillDTO dto = service.getGroupSeckill(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.GroupSeckill,
        value = CommonLogTypeConstantDTO.GroupSeckillDelete)
    @ApiOperation(value = "根据id删除拼团秒杀活动")
    @DeleteMapping()
    public Response deleteGroupSeckill(@RequestBody @Valid BaseId cmd) {
        service.deleteGroupSeckill(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.GroupSeckill,
        value = CommonLogTypeConstantDTO.GroupSeckillUpdateStatus)
    @ApiOperation(value = "根据拼团秒杀活动id变更状态")
    @PutMapping(value = "/status")
    public Response updateGroupSeckillStatus(@RequestBody @Valid GroupSeckillStatusCmd cmd) {
        service.updateGroupSeckillStatus(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.GroupSeckill,
        value = CommonLogTypeConstantDTO.GroupSeckillSortUpdate)
    @ApiOperation(value = "拼团秒杀活动排序更新")
    @PutMapping(value = "/sort")
    public Response updateGroupSeckillSort(@RequestBody @Valid List<GroupSeckillSortCmd> cmds) {
        service.updateGroupSeckillSort(cmds);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.GroupSeckill,
        value = CommonLogTypeConstantDTO.GroupSeckillGroupSortUpdate)
    @ApiOperation(value = "拼团秒杀商品排序更新")
    @PutMapping(value = "/goods/sort")
    public Response updateGroupSeckillGoodsSort(@RequestBody @Valid List<GroupSeckillGoodsSortCmd> cmds) {
        service.updateGroupSeckillGoodsSort(cmds);
        return Response.buildSuccess();
    }

}
