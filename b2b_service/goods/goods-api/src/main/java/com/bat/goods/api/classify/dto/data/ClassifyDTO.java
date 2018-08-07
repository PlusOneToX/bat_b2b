package com.bat.goods.api.classify.dto.data;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import com.bat.goods.api.user.dto.data.UserClassifyBannerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商品分类信息封装")
public class ClassifyDTO {

    private Integer id;
    @ApiModelProperty(value = "商品分类名称", required = true, example = "bat")
    private String name;
    @ApiModelProperty(value = "商品分类英文名称", required = false, example = "bat")
    private String nameEn;
    @ApiModelProperty(value = "商品分类描述", required = false, example = "保护类")
    private String description;
    @ApiModelProperty(value = "是否开启:0 停用 1 启用", required = true, example = "0")
    private Short openFlag;
    @ApiModelProperty(value = "排序,数据越小排在越前面", required = true, example = "1")
    private Integer sort;
    @ApiModelProperty(value = "父级ID", required = false, example = "1001")
    private Integer parentId;
    @ApiModelProperty(value = "分类图片地址", required = false, example = "http://bat")
    private String imageUrl;
    @ApiModelProperty(value = "分类英文图片地址", required = false, example = "http://bat")
    private String imageUrlEn;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "是否推荐分类 0否 1是", required = true, example = "0")
    private Short recommendFlag;

    @ApiModelProperty(value = "小程序分类名称", required = true, example = "0")
    private String appletName;

    @ApiModelProperty(value = "子分类")
    private List<ClassifyDTO> subClassifies;

    @ApiModelProperty(value = "banner图")
    private List<UserClassifyBannerDTO> banners;
}
