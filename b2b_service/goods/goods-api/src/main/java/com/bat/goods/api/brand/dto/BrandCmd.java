package com.bat.goods.api.brand.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BrandCmd", description = "品牌信息")
public class BrandCmd {

    @ApiModelProperty(value = "品牌ID", required = false)
    private Integer id;
    @NotBlank(message = "P_GOODS_BRAND_NAME_NULL")
    @ApiModelProperty(value = "品牌名称,最长20个字符", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "品牌英文名称，最长100个字符", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "品牌图片Logo", required = false, example = "http://bat")
    private String logo;
    @ApiModelProperty(value = "品牌描述,最长300个字符", required = false, example = "bat品牌")
    private String description;
    @NotNull(message = "P_GOODS_OPEN_FLAG_NULL")
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @NotNull(message = "P_GOODS_SORT_NULL")
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;
    @NotNull(message = "P_GOODS_DISTRIBUTOR_SCOPE_NULL")
    @ApiModelProperty(value = "分销商可视范围:分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组", required = true,
        example = "1")
    private Short distributorScope;
    @NotNull(message = "P_GOODS_DISTRIBUTOR_SCOPE_NO_NULL")
    @ApiModelProperty(value = "分销商不可视范围:0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", required = true, example = "1")
    private Short distributorScopeNo;
    @ApiModelProperty(value = "分销商等级id列表，可视范围为2时需传值", required = false)
    private List<Integer> scalePriceIds;
    @ApiModelProperty(value = "销售部门id列表，可视范围为4时需传值", required = false)
    private List<Integer> departmentIds;
    @ApiModelProperty(value = "业务员id列表，可视范围为5时需传值", required = false)
    private List<Integer> adminIds;
    @ApiModelProperty(value = "分销商id列表，可视范围为3时需传值", required = false)
    private List<Integer> distributorIds;
    @ApiModelProperty(value = "分销商分组id列表，可视范围为6时需传值", required = false)
    private List<Integer> distributorGroupIds;
    @ApiModelProperty(value = "分销商等级id列表，不可视范围为2时需传值", required = false)
    private List<Integer> scalePriceNoIds;
    @ApiModelProperty(value = "销售部门id列表，不可视范围为4时需传值", required = false)
    private List<Integer> departmentNoIds;
    @ApiModelProperty(value = "业务员id列表，不可视范围为5时需传值", required = false)
    private List<Integer> adminNoIds;
    @ApiModelProperty(value = "分销商id列表，不可视范围为3时需传值", required = false)
    private List<Integer> distributorNoIds;

}
