package com.bat.distributor.api.user.dto.customer;

import javax.validation.constraints.NotNull;

import com.bat.distributor.api.base.BaseId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CustomerAddressListQry", description = "C端客户地址列表查询")
public class CustomerAddressListQry extends BaseId {
    @NotNull(message = "P_DISTRIBUTOR_PAGE_SIZE_NULL")
    @ApiModelProperty(value = "每页数量", required = true, example = "10")
    private Integer size;
    @NotNull(message = "P_DISTRIBUTOR_PAGE_NULL")
    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer page;
}
