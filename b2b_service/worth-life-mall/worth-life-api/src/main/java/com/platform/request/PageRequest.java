package com.platform.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页请求")
public class PageRequest {
    @ApiModelProperty("第几页，默认1")
    private Integer current;
    @ApiModelProperty("每页数量，默认10")
    private Integer limit;

    public Integer getCurrent() {
        return (current == null || current < 1) ? 0 : current;
    }

    public Integer getLimit() {
        return (limit == null || limit < 1) ? 10 : limit;
    }
}
