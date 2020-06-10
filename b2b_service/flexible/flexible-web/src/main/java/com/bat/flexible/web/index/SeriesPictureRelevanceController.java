package com.bat.flexible.web.index;


import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.SeriesPictureRelevanceServiceI;
import com.bat.flexible.web.annotation.SysLog;
import com.bat.flexible.web.base.BaseController;
import com.bat.flexible.web.constants.CommonLogTypeConstantDTO;
import com.bat.flexible.web.constants.CommonLogTypeTitleConstantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页推荐控制器
 */
@RestController
@RequestMapping(value="/flexible/v1/web/admin/u/p/seriesPictureRelevance")
@Api(tags = "主题系列图片关联管理接口")
public class SeriesPictureRelevanceController extends BaseController {

    @Autowired
    private SeriesPictureRelevanceServiceI seriesPictureRelevanceServiceI;


    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.SeriesPictureRelevance, value = CommonLogTypeConstantDTO.SeriesPictureRelevanceUpOrDown)
    @PutMapping(value = "/upOrDown")
    @ApiOperation(value = "上下移动")
    public Response updateSortNo(@Valid @RequestBody FlexibleUpOrDownDTO flexibleUpOrDownDTO){

        return seriesPictureRelevanceServiceI.updateSortNo(flexibleUpOrDownDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.SeriesPictureRelevance, value = CommonLogTypeConstantDTO.SeriesPictureRelevanceDeleteById)
    @DeleteMapping(value = "/deleteById")
    @ApiOperation("删除")
    public Response deleteById(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){

        List<Integer> list = new ArrayList<>();
        list.add(flexibleIdDTO.getId());
        return seriesPictureRelevanceServiceI.delete(list);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.SeriesPictureRelevance, value = CommonLogTypeConstantDTO.SeriesPictureRelevanceBatchDelete)
    @DeleteMapping(value = "/batchDelete")
    @ApiOperation(value = "批量删除")
    public Response batchDelete(@Valid @RequestBody FlexibleIdListDTO flexibleIdListDTO){

        return seriesPictureRelevanceServiceI.delete(flexibleIdListDTO.getIdList());
    }
}
