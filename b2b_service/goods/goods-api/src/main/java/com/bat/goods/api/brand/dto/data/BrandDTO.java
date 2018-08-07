package com.bat.goods.api.brand.dto.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "品牌信息封装")
public class BrandDTO implements Serializable {

    private Integer id;
    @ApiModelProperty(value = "品牌名称", example = "bat")
    private String name;
    @ApiModelProperty(value = "品牌英文名称", example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "品牌图片Logo", example = "http://bat")
    private String logo;
    @ApiModelProperty(value = "品牌描述", example = "bat品牌")
    private String description;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", example = "0")
    private Short openFlag;
    @ApiModelProperty(value = "排序,数据越小排在越前面", example = "1")
    private Integer sort;
    @ApiModelProperty(value = "分销商可视范围:分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "1")
    private Short distributorScope;
    @ApiModelProperty(value = "分销商不可视范围:0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员", example = "1")
    private Short distributorScopeNo;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "可视，分销商等级id列表")
    private List<Integer> scalePriceIds;
    @ApiModelProperty(value = "可视，销售部门id列表")
    private List<Integer> departmentIds;
    @ApiModelProperty(value = "可视，业务员id列表")
    private List<Integer> adminIds;
    @ApiModelProperty(value = "可视，分销商id列表")
    private List<BrandDistributorScopeDTO> distributors;
    @ApiModelProperty(value = "可视，分销商分组id列表")
    private List<BrandDistributorGroupDTO> distributorGroups;
    @ApiModelProperty(value = "不可视，分销商等级id列表")
    private List<Integer> scalePriceNoIds;
    @ApiModelProperty(value = "不可视，销售部门id列表")
    private List<Integer> departmentNoIds;
    @ApiModelProperty(value = "不可视，业务员id列表")
    private List<Integer> adminNoIds;
    @ApiModelProperty(value = "不可视，分销商id列表")
    private List<BrandDistributorScopeDTO> distributorNos;

}
