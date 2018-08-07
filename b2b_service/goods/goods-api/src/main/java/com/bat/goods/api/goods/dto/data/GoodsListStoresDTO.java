package com.bat.goods.api.goods.dto.data;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "商店配置商品信息")
public class GoodsListStoresDTO {

    @ApiModelProperty(value = "商店配置商品信息列表")
    private PageInfo<GoodsListStoreDTO> pageInfo;

    @ApiModelProperty(value = "移动端配置id")
    private Integer mobileId;

}
