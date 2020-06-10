package com.bat.flexible.api.index.dto;

import com.bat.flexible.api.base.common.dto.BasePageParamQry;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ThemeDTO extends BasePageParamQry {

    @ApiModelProperty(value = "分销商id")
    @NotNull(message = "COMMON_DISTRIBUTOR_ID_NULL")
    private Integer distributorId;

    @ApiModelProperty(value = "分销商id列表")
    private List<Integer> distributorIds;

    /**
     * 系列主题id
     */
    @ApiModelProperty(value = "主题id")
    @NotNull(message = "INDEX_SERIES_THEME_ID_NULL")
    private Integer themeId;

    @ApiModelProperty(value = "一级分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "二级分类id")
    private Integer secondCategoryId;

    private Integer exchangeId;
    private Integer modelId;
    private Integer materialId;
    private Short type;





}
