package com.bat.flexible.api.base.common.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class FlexibleIdListDTO {

    @NotEmpty(message = "COMMON_ID_LIST_NULL")
    @ApiModelProperty(value = "id列表")
    private List<Integer> idList;


}
