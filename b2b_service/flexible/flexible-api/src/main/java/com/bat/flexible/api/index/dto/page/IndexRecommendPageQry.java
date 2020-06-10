package com.bat.flexible.api.index.dto.page;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class IndexRecommendPageQry extends BasePageParamQry {
    /**
     * 分销商名称/公司名称
     */
    @ApiModelProperty(value = "搜索关键词")
    private String content;

    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    private List<Integer> distributorIds;

    private Integer categoryId;
    private Integer exchangeId;
    private Integer modelId;
    private Integer materialId;


}
