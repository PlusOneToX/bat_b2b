package com.bat.flexible.api.index.dto.page;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributorIndexRecommendPageQry extends BasePageParamQry {
    /**
     * 分销商名称/公司名称
     */
    @ApiModelProperty(value = "搜索关键词")
    private String content;




}
