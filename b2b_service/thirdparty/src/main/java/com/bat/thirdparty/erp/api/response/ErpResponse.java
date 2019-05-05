package com.bat.thirdparty.erp.api.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "ERP请求返回参数")
public class ErpResponse<T> implements Serializable {
    @ApiModelProperty(value = "返回状态码：0 请求成功 非0 请求失败", example = "0")
    private Integer code = 0;
    @ApiModelProperty(value = "返回信息：一般请求失败情况有返回错误信息", example = "请求失败")
    private String msg = "";
    @ApiModelProperty(value = "请求成功：有参数返回时有值")
    private T data;

    public ErpResponse() {}

    public static <T> ErpResponse<T> of(T data) {
        ErpResponse<T> response = new ErpResponse();
        response.setCode(0);
        response.setData(data);
        return response;
    }

    public static ErpResponse buildSuccess() {
        ErpResponse response = new ErpResponse();
        response.setCode(0);
        return response;
    }

    public static ErpResponse buildFailure(String msg) {
        ErpResponse response = new ErpResponse();
        response.setCode(-1);
        response.setMsg(msg);
        return response;
    }
}
