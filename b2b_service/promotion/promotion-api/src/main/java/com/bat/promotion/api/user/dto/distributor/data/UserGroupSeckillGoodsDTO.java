package com.bat.promotion.api.user.dto.distributor.data;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author bat(b2b_bat @ 163.com)
 * @date 2019/5/19 17:30
 */
@Data
@ApiModel(description = "拼团秒杀活动商品信息")
public class UserGroupSeckillGoodsDTO {
    @ApiModelProperty(value = "拼团秒杀活动商品id", example = "123456")
    private Integer id;
    @ApiModelProperty(value = "拼团秒杀活动id", example = "123456")
    private Integer groupSeckillId;
    @ApiModelProperty(value = "开始时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间", example = "2019-02-23 05:39:38")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "商品编码", example = "商品名称")
    private String goodsNo;
    @ApiModelProperty(value = "商品名称", example = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品类型 1-普通 2-定制", example = "1")
    private Short goodsType;
    @ApiModelProperty(value = "定制类型 0-标准定制 1-DIY定制", example = "1")
    private Short diyType;
    @ApiModelProperty(value = "商品图片地址1", example = "商品图片地址1")
    private String imageUrl1;
    @ApiModelProperty(value = "商品英文图片地址1", example = "商品英文图片地址1")
    private String imageUrl1En;
    @ApiModelProperty(value = "最大拼团或秒杀数量、默认不限制", example = "10")
    private Integer maxNum;
    @ApiModelProperty(value = "拼团秒杀价", example = "10.00")
    private BigDecimal groupSeckillPrice;
    @ApiModelProperty(value = "虚拟拼团秒杀数", example = "100")
    private Integer virtualSum;
    @ApiModelProperty(value = "已拼或已秒实际数量", example = "100")
    private Integer realSum;
}
