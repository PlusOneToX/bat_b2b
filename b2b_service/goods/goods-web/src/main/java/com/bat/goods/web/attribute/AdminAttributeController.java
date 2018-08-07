package com.bat.goods.web.attribute;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.attribute.AttributeServiceI;
import com.bat.goods.api.attribute.dto.*;
import com.bat.goods.api.attribute.dto.data.AttributeDTO;
import com.bat.goods.api.attribute.dto.data.AttributeValueDTO;
import com.bat.goods.web.annotation.SysLog;
import com.bat.goods.web.base.BaseController;
import com.bat.goods.web.base.Response;
import com.bat.goods.web.constants.CommonLogTypeConstantDTO;
import com.bat.goods.web.constants.CommonLogTypeTitleConstantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.attribute.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "商品属性后台接口", value = "AdminAttributeController")
@RestController
@RequestMapping("/goods/v1/web/admin/attribute")
public class AdminAttributeController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminAttributeController.class);

    @Resource
    private AttributeServiceI attributeService;

    @ApiOperation(value = "根据搜索条件查找商品属性列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<AttributeDTO>> listAttribute(@Valid AttributeListQry qry) {
        PageInfo<AttributeDTO> pageInfo = attributeService.listAttribute(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找商品属性列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<AttributeDTO>> listAttributePo(@Valid AttributeListQry qry) {
        PageInfo<AttributeDTO> pageInfo = attributeService.listAttribute(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据商品属性ID查找商品属性值列表(分页)")
    @GetMapping(value = "/value/list")
    public Response<PageInfo<AttributeValueDTO>> listAttributeValue(@Valid AttributeValueListQry qry) {
        PageInfo<AttributeValueDTO> pageInfo = attributeService.listAttributeValue(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据商品属性ID查找所有商品属性值列表(不分页)")
    @GetMapping(value = "/allvalue/list")
    public Response<List<AttributeValueDTO>> listAllAttributeValue(@Valid AttributeId qry) {
        List<AttributeValueDTO> dtoList = attributeService.listAllAttributeValue(qry);
        return Response.of(dtoList);
    }

    @ApiOperation(value = "根据商品属性id获取商品属性详情")
    @GetMapping()
    public Response<AttributeDTO> getAttribute(@Valid AttributeId qry) {
        AttributeDTO dto = attributeService.getAttribute(qry);
        return Response.of(dto);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAttribute, value = CommonLogTypeConstantDTO.AdminAttributeAdd)
    @ApiOperation(value = "添加商品属性")
    @PostMapping()
    public Response createAttribute(@RequestBody @Valid AttributeCmd cmd) {
        attributeService.createAttribute(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAttribute, value = CommonLogTypeConstantDTO.AdminAttributeUpdate)
    @ApiOperation(value = "更新商品属性")
    @PutMapping()
    public Response updateAttribute(@RequestBody @Valid AttributeCmd cmd) {
        attributeService.updateAttribute(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAttribute, value = CommonLogTypeConstantDTO.AdminAttributeUpdateStatus)
    @ApiOperation(value = "更新商品属性状态")
    @PutMapping(value = "/open")
    public Response openAttribute(@RequestBody @Valid AttributeOpenCmd cmd) {
        attributeService.openAttribute(cmd);
        return Response.buildSuccess();
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.AdminAttribute, value = CommonLogTypeConstantDTO.AdminAttributeDelete)
    @ApiOperation(value = "根据商品属性id删除商品属性")
    @DeleteMapping()
    public Response deleteAttribute(@RequestBody @Valid AttributeId cmd) {
        attributeService.deleteAttribute(cmd);
        return Response.buildSuccess();
    }

}
