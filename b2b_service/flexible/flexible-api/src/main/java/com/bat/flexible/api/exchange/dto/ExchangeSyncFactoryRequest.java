package com.bat.flexible.api.exchange.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class ExchangeSyncFactoryRequest {


    @NotNull(message = "兑换卡活动id不能为空")
    private Long id;

    /**
     * 正面url
     */
    @NotBlank(message = "正面url不能为空")
    private String positiveUrl;

    /**
     * 反面url
     */
    @NotBlank(message = "反面URL不能为空")
    private String reverseUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPositiveUrl() {
        return positiveUrl;
    }

    public void setPositiveUrl(String positiveUrl) {
        this.positiveUrl = positiveUrl;
    }

    public String getReverseUrl() {
        return reverseUrl;
    }

    public void setReverseUrl(String reverseUrl) {
        this.reverseUrl = reverseUrl;
    }
}
