package com.bat.goods.web.tag;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.tag.TagServiceI;
import com.bat.goods.api.tag.dto.TagCmd;
import com.bat.goods.api.tag.dto.TagId;
import com.bat.goods.api.tag.dto.TagListQry;
import com.bat.goods.api.tag.dto.TagOpenCmd;
import com.bat.goods.api.tag.dto.data.TagDTO;
import com.bat.goods.web.base.BaseController;
import com.bat.goods.web.base.Response;
import com.bat.goods.web.annotation.SysLog;
import com.bat.goods.web.constants.CommonLogTypeConstantDTO;
import com.bat.goods.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "商品标签后台接口", value = "AdminTagController")
@RestController
@RequestMapping("/goods/v1/web/admin/tag")
public class AdminTagController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminTagController.class);

    @Resource
    private TagServiceI tagService;

    @ApiOperation(value = "根据搜索条件查找商品标签列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<TagDTO>> listTag(@Valid TagListQry qry) {
        PageInfo<TagDTO> pageInfo = tagService.listTag(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找商品标签列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<TagDTO>> listTagPo(@Valid TagListQry qry) {
        PageInfo<TagDTO> pageInfo = tagService.listTag(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据商品标签id获取商品标签详情")
    @GetMapping()
    public Response<TagDTO> getTag(@Valid TagId qry) {
        TagDTO dto = tagService.getTag(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTag, value = CommonLogTypeConstantDTO.AdminTagAdd)
    @ApiOperation(value = "添加商品标签")
    @PostMapping()
    public Response createTag(@RequestBody @Valid TagCmd cmd) {
        tagService.createTag(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTag, value = CommonLogTypeConstantDTO.AdminTagUpdate)
    @ApiOperation(value = "更新商品标签")
    @PutMapping()
    public Response updateTag(@RequestBody @Valid TagCmd cmd) {
        tagService.updateTag(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTag, value = CommonLogTypeConstantDTO.AdminTagUpdateStatus)
    @ApiOperation(value = "更新商品标签状态")
    @PutMapping(value = "/open")
    public Response openTag(@RequestBody @Valid TagOpenCmd cmd) {
        tagService.openTag(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminTag, value = CommonLogTypeConstantDTO.AdminTagDelete)
    @ApiOperation(value = "根据商品标签id删除商品标签")
    @DeleteMapping()
    public Response deleteTag(@RequestBody @Valid TagId cmd) {
        tagService.deleteTag(cmd);
        return Response.buildSuccess();
    }

}
