package com.bat.distributor.web.subaccount;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bat.distributor.api.base.BeanUtils;
import com.bat.distributor.api.base.DistributorOpenFlagCmd;
import com.bat.distributor.api.base.Response;
import com.bat.distributor.api.subaccount.DistributorSubAccountSalemanServiceI;
import com.bat.distributor.api.subaccount.dto.*;
import com.bat.distributor.dao.subaccount.dataobject.DistributorSubAccountSalemanDO;
import com.bat.distributor.web.constants.CommonLogTypeConstantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.bat.distributor.api.subaccount.dto.*;
import com.bat.distributor.web.annotation.SysLog;
import com.bat.distributor.web.base.BaseController;
import com.bat.distributor.web.constants.CommonLogTypeTitleConstantDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "分销商业务员前台接口")
@RestController
@RequestMapping(value = "/distributor/v1/web/user/subAccountSaleman")
public class UserDistributorSubAccountSalemanController extends BaseController {

    @Autowired
    private DistributorSubAccountSalemanServiceI distributorSubAccountSalemanServiceI;

    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody SubAccountSalemanCmd subAccountSalemanCmd) {
        distributorSubAccountSalemanServiceI.create(subAccountSalemanCmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Response update(@Valid @RequestBody SubAccountSalemanCmd subAccountSalemanCmd) {
        distributorSubAccountSalemanServiceI.update(subAccountSalemanCmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @PutMapping(value = "/updateOpenFlag")
    @ApiOperation(value = "启用禁用")
    public Response updateOpenFlag(@Valid @RequestBody DistributorOpenFlagCmd subAccountSalemanCmd) {
        distributorSubAccountSalemanServiceI.updateOpenFlag(subAccountSalemanCmd, getUserId(), getUserName());
        return Response.buildSuccess();
    }

    @GetMapping(value = "/page")
    @ApiOperation(value = "分页查询业务员")
    public Response<PageInfo<SubAccountSalemanPageDTO>> page(@Valid SubAccountSalemanPageQry subAccountSalemanPageQry) {
        subAccountSalemanPageQry.setDistributorId(Integer.parseInt(getUserId()));
        PageInfo<SubAccountSalemanPageDTO> pageInfo =
            distributorSubAccountSalemanServiceI.pageAdmin(subAccountSalemanPageQry);
        return Response.of(pageInfo);
    }

    @PutMapping(value = "/bindWechat")
    @ApiOperation(value = "业务员绑定微信")
    public Response bindWechat(@Valid @RequestBody SubAccountSalemanWechatCmd subAccountSalemanWechatCmd) {
        DistributorSubAccountSalemanDO distributorSubAccountSalemanDO =
            distributorSubAccountSalemanServiceI.bindWechat(subAccountSalemanWechatCmd);
        return Response.of(distributorSubAccountSalemanDO.getName());
    }

    @GetMapping(value = "/listByCondition")
    @ApiOperation(value = "条件查询业务员")
    public Response<List<DistributorSubAccountSalemanDOQry>>
        listByCondition(@Valid DistributorOpenFlagQry distributorOpenFlagQry) {
        List<DistributorSubAccountSalemanDO> doList =
            distributorSubAccountSalemanServiceI.listByCondition(distributorOpenFlagQry.getDistributorId(), null,
                distributorOpenFlagQry.getOpenFlag(), distributorOpenFlagQry.getSonLevelId());
        List<DistributorSubAccountSalemanDOQry> distributorSubAccountSalemanDOQries =
            BeanUtils.copyList(doList, DistributorSubAccountSalemanDOQry.class);
        return Response.of(distributorSubAccountSalemanDOQries);
    }

    @GetMapping(value = "/getWechatProgramCodeUrl")
    @ApiOperation(value = "获取分销商业务员绑定二维码")
    public Response<String>
        getWechatProgramCodeUrl(@RequestParam(value = "qrType", required = false, defaultValue = "h5") String qrType) {
        String url = null;
        if ("program".equals(qrType)) {
            url = distributorSubAccountSalemanServiceI.getWechatProgramCodeUrl(Integer.parseInt(getUserId()));
        } else if ("h5".equals(qrType)) {
            url = distributorSubAccountSalemanServiceI.getWechatH5CodeUrl(Integer.parseInt(getUserId()));
        }
        return Response.of(url);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.User, value = CommonLogTypeConstantDTO.UserImportSaleman)
    @PostMapping(value = "/import")
    @ApiOperation(value = "业务员excel导入")
    public Response importExcel(HttpServletRequest request) {
        try {
            MultipartHttpServletRequest multipart = (MultipartHttpServletRequest)request;
            Iterator<String> itr = multipart.getFileNames();
            MultipartFile file = null;
            if (itr.hasNext()) {
                String str = itr.next(); // 这个文件并不是原来的文件名
                file = multipart.getFile(str);
            }
            InputStream inputStream = file.getInputStream();
            return distributorSubAccountSalemanServiceI.importSaleman(inputStream, Integer.parseInt(getUserId()),
                getUserName());
        } catch (Exception e) {
            e.printStackTrace();
            return Response.buildFailure("100", e.getMessage());
        }
    }

    @GetMapping(value = "/test")
    @ApiOperation(value = "测试")
    public Response test() {
        distributorSubAccountSalemanServiceI.test();
        return Response.buildSuccess();
    }
}
