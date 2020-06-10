package com.bat.flexible.web.index;


import com.bat.flexible.api.base.common.dto.FlexibleIdDTO;
import com.bat.flexible.api.base.common.dto.FlexibleIdListDTO;
import com.bat.flexible.api.base.common.dto.FlexibleUpOrDownDTO;
import com.bat.flexible.api.base.common.response.Response;
import com.bat.flexible.api.index.IndexRecommendPictureRelevanceServiceI;
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
@RequestMapping(value="/flexible/v1/web/admin/u/p/indexRecommendPictureRelevance")
@Api(tags = "首页推荐图片关联接口")
public class IndexRecommendPictureRelevanceController extends BaseController {

    @Autowired
    private IndexRecommendPictureRelevanceServiceI indexRecommendPictureRelevanceServiceI;


    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.IndexRecommendPictureRelevance, value = CommonLogTypeConstantDTO.IndexRecommendPictureRelevanceUpOrDown)
    @PutMapping(value = "/upOrDown")
    @ApiOperation(value = "上下移动")
    public Response updateSortNo(@Valid @RequestBody FlexibleUpOrDownDTO flexibleUpOrDownDTO){

        return indexRecommendPictureRelevanceServiceI.updateSortNo(flexibleUpOrDownDTO,getCurrentAdmin());
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.IndexRecommendPictureRelevance, value = CommonLogTypeConstantDTO.IndexRecommendPictureRelevanceDelete)
    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "单个删除")
    public Response deleteById(@Valid @RequestBody FlexibleIdDTO flexibleIdDTO){

        List<Integer> list = new ArrayList<>();
        list.add(flexibleIdDTO.getId());
        return indexRecommendPictureRelevanceServiceI.delete(list);
    }

    @SysLog(businessFunction = CommonLogTypeTitleConstantDTO.IndexRecommendPictureRelevance, value = CommonLogTypeConstantDTO.IndexRecommendPictureRelevanceBatchDelete)
    @DeleteMapping(value = "/batchDelete")
    @ApiOperation(value = "批量删除")
    public Response batchDelete(@Valid @RequestBody FlexibleIdListDTO flexibleIdListDTO){

        return indexRecommendPictureRelevanceServiceI.delete(flexibleIdListDTO.getIdList());
    }
}
