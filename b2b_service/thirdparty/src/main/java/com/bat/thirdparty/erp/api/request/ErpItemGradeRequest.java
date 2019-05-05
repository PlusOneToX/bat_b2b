package com.bat.thirdparty.erp.api.request;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


public class ErpItemGradeRequest implements Serializable {

    private static final long serialVersionUID = -8382757041362407491L;

    //货品编码
    @NotBlank(message = "货品编码不能为空")
    private String itemCode;

    //等级field
    @NotBlank(message = "等级字段不能为空")
    private String field;

    //价格
    @NotNull(message = "等级价格不能为空")
    @Min(value = 0, message = "必须要大于0")
    private BigDecimal price;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
