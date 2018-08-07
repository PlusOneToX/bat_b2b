package com.bat.system.api.user.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/23 22:07
 */
@NoArgsConstructor
@Data
public class MenuShowSyncDTO {

    /**
     * title : 促销活动 children :
     * [{"name":"促销活动列表","link":"salesPromotion","permission":"promotion-list"},{"name":"批量活动导入","link":"promotionImportList","permission":"promotion-import"}]
     */

    @JsonProperty("title")
    private String title;
    @JsonProperty("children")
    private List<ChildrenDTO> children;
    private Integer sort;

    @NoArgsConstructor
    @Data
    public static class ChildrenDTO {
        /**
         * name : 促销活动列表 link : salesPromotion permission : promotion-list
         */

        @JsonProperty("name")
        private String name;
        @JsonProperty("link")
        private String link;
        private Integer sort;
        // @JsonProperty("permission")
        // private String permission;
    }
}
