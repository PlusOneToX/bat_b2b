package com.bat.financial.api.pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/5/22 16:11
 */
@NoArgsConstructor
@Data
public class SceneInfo {

    /**
     * payer_client_ip : 127.0.0.1 h5_info : {"type":"Wap"}
     */

    @JsonProperty("payer_client_ip")
    @ApiModelProperty(value = "用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。", example = "127.0.0.1")
    private String payerClientIp;
    @JsonProperty("h5_info")
    private H5InfoDTO h5Info;

    @NoArgsConstructor
    @Data
    public static class H5InfoDTO {
        /**
         * type : Wap
         */
        @ApiModelProperty(value = "场景类型 示例值：iOS, Android, Wap", example = "Wap")
        @JsonProperty("type")
        private String type;
    }
}
