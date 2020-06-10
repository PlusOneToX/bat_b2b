package com.bat.flexible.api.canva.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserNoDTO {


    @ApiModelProperty(value = "的userNo")
    @NotBlank(message = "F_USER_NO_NULL")
    private String userNo;
}
