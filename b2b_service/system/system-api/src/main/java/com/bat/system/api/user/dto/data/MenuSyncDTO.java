package com.bat.system.api.user.dto.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/25 16:18
 */
public class MenuSyncDTO {
    /**
     * name : 商品分类管理 module : children :
     * [{"name":"查看","module":"category-get"},{"name":"管理","module":"category-manage"}]
     */

    @JsonProperty("name")
    private String name;
    @JsonProperty("module")
    private String module;
    @JsonProperty("children")
    private List<ChildrenDTO> children;

    @NoArgsConstructor
    @Data
    public static class ChildrenDTO {
        /**
         * name : 查看 module : category-get
         */

        @JsonProperty("name")
        private String name;
        @JsonProperty("module")
        private String module;
    }
}
