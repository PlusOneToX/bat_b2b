package com.bat.goods.web.classify;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.bat.goods.api.classify.ClassifyServiceI;
import com.bat.goods.api.classify.dto.*;
import com.bat.goods.api.classify.dto.data.ClassifyDTO;
import com.bat.goods.api.classify.dto.data.ClassifyRecommendDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.bat.goods.api.classify.dto.*;
import com.bat.goods.web.base.BaseController;
import com.bat.goods.web.base.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "商品分类后台接口", value = "AdminClassifyController")
@RestController
@RequestMapping("/goods/v1/web/admin/classify")
public class AdminClassifyController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(AdminClassifyController.class);

    @Resource
    private ClassifyServiceI classifyService;

    @ApiOperation(value = "根据搜索条件查找商品分类列表(分页)")
    @GetMapping(value = "/list")
    public Response<PageInfo<ClassifyDTO>> listClassify(@Valid ClassifyListQry qry) {
        PageInfo<ClassifyDTO> pageInfo = classifyService.listClassify(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据搜索条件查找商品分类列表(分页,不受权限控制接口)")
    @GetMapping(value = "/po/list")
    public Response<PageInfo<ClassifyDTO>> listClassifyPo(@Valid ClassifyListQry qry) {
        PageInfo<ClassifyDTO> pageInfo = classifyService.listClassify(qry);
        return Response.of(pageInfo);
    }

    @ApiOperation(value = "根据商品分类id获取子分类列表")
    @GetMapping(value = "/sub/list")
    public Response<List<ClassifyDTO>> listSubClassify(@Valid ClassifySubListQry qry) {
        List<ClassifyDTO> list = classifyService.listSubClassify(qry);
        return Response.of(list);
    }

    @ApiOperation(value = "根据商品分类id获取子分类列表(不受权限控制接口)")
    @GetMapping(value = "po/sub/list")
    public Response<List<ClassifyDTO>> listSubClassifyPo(@Valid ClassifySubListQry qry) {
        List<ClassifyDTO> list = classifyService.listSubClassify(qry);
        return Response.of(list);
    }

    @ApiOperation(value = "根据商品分类id获取商品分类详情")
    @GetMapping()
    public Response<ClassifyDTO> getClassify(@Valid ClassifyId qry) {
        ClassifyDTO classify = classifyService.getClassify(qry);
        return Response.of(classify);
    }

    @ApiOperation(value = "添加商品分类")
    @PostMapping()
    public Response createClassify(@RequestBody @Valid ClassifyCmd cmd) {
        classifyService.createClassify(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新商品分类")
    @PutMapping()
    public Response updateClassify(@RequestBody @Valid ClassifyCmd cmd) {
        classifyService.updateClassify(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "更新商品分类状态")
    @PutMapping(value = "/open")
    public Response openClassify(@RequestBody @Valid ClassifyOpenCmd cmd) {
        classifyService.openClassify(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "根据商品分类id删除商品分类")
    @DeleteMapping()
    public Response deleteClassify(@RequestBody @Valid ClassifyId cmd) {
        classifyService.deleteClassify(cmd);
        return Response.buildSuccess();
    }

    @ApiOperation(value = "获取商品推荐分类信息")
    @GetMapping(value = "/recommend/info")
    public Response<ClassifyRecommendDTO> recommendInfo() {
        ClassifyRecommendDTO classifyRecommendDTO = classifyService.recommendInfo();
        return Response.of(classifyRecommendDTO);
    }

    @ApiOperation(value = "更新商品推荐分类信息")
    @PostMapping(value = "/recommend/info")
    public Response updateRecommendInfo(@RequestBody @Valid ClassifyRecommendCmd cmd) {
        classifyService.updateRecommendInfo(cmd);
        return Response.buildSuccess();
    }

}
