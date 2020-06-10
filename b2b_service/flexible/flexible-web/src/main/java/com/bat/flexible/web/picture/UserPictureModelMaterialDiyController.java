package com.bat.flexible.web.picture;

import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.picture.PictureModelMaterialDiyServiceI;
import com.bat.flexible.api.picture.dto.diy.PictureModelMaterialDiyCmd;
import com.bat.flexible.api.picture.dto.diy.PictureModelMaterialDiyQry;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import com.bat.flexible.dao.picture.dataobject.PictureModelMaterialDiyDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value ="/flexible/v1/web/user/u/pictureModelMaterialDiy")
@Api(tags = "IP图片缓存前端接口")
public class UserPictureModelMaterialDiyController {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserPictureModelMaterialDiyController.class);


    @Autowired
    private PictureModelMaterialDiyServiceI pictureModelMaterialDiyServiceI;

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.UserPictureModelMaterialDiy, value = CommonLogTypeConstantDTO.UserPictureModelMaterialDiyAdd)
    @PostMapping
    @ApiOperation(value = "新增")
    public Response create(@Valid @RequestBody PictureModelMaterialDiyCmd modelMaterialDiyCmd){
        return pictureModelMaterialDiyServiceI.create(modelMaterialDiyCmd);
    }

    @GetMapping
    @ApiOperation(value = "根据材质id、型号id、图片id查询")
    public Response<PictureModelMaterialDiyDO> getByMaterialIdAndModelIdAndPictureId(@Valid PictureModelMaterialDiyQry pictureModelMaterialDiyQry){
        return pictureModelMaterialDiyServiceI.getByMaterialIdAndModelIdAndPictureId(pictureModelMaterialDiyQry.getMaterialId(),
                pictureModelMaterialDiyQry.getModelId(),pictureModelMaterialDiyQry.getPictureId());
    }
}
