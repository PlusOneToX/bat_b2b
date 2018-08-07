package com.bat.system.api.user.dto.data;

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
public class PermissionSyncDTO {

    /**
     * title : 商品模块 auth :
     * {"actions":[{"name":"商品分类管理","module":"","children":[{"name":"查看","module":"category-get"},{"name":"管理","module":"category-manage"}]},{"name":"商品品牌管理","module":"","children":[{"name":"查看","module":"brand-get"},{"name":"管理","module":"brand-manage"}]},{"name":"商品品类管理","module":"","children":[{"name":"查看","module":"productline-get"},{"name":"管理","module":"productline-manage"}]},{"name":"商品规格管理","module":"","children":[{"name":"查看","module":"specification-get"},{"name":"管理","module":"specification-manage"}]},{"name":"商品等级管理","module":"","children":[{"name":"查看","module":"item-grade"},{"name":"等级设置","module":"goods-grade-setting"},{"name":"规则设置","module":"item-grade-setting"},{"name":"等级管理","module":"item-grade-change"},{"name":"审批","module":"item-grade-change-check"}]},{"name":"商品管理","module":"","children":[{"name":"查看","module":"goods-get"},{"name":"新增","module":"goods-post"},{"name":"编辑","module":"goods-put"},{"name":"删除","module":"goods-delete"},{"name":"冻结解冻","module":"goods-freeze"},{"name":"上下架","module":"goods-updown"},{"name":"审批","module":"goods-check"},{"name":"同步信息","module":"goods-syncgoodsitem"}]},{"name":"限制购买量管理","module":"","children":[{"name":"查看","module":"goods-purchaselimit-get"},{"name":"管理","module":"goods-purchaselimit-manage"}]},{"name":"报价单","module":"","children":[{"name":"导出","module":"goods-exportPrice"}]},{"name":"商品属性管理","module":"","children":[{"name":"第三方材质型号关联","module":"thirdskurela"}]},{"name":"商品销量管理","module":"","children":[{"name":"查看","module":"goods-get"},{"name":"设置","module":"goods-post"}]}],"menus":[{"name":"商品管理","module":"","children":[{"name":"商品列表","module":"goods-list"},{"name":"限购规则列表","module":"goods-purchaselimit"},{"name":"冻结商品列表","module":"goods-freezed"},{"name":"商品销量管理","module":"goods-get"}]},{"name":"商品等级管理","module":"","children":[{"name":"等级列表","module":"good-grade-base"},{"name":"等级规则设置","module":"item-grade-setting"},{"name":"商品等级列表","module":"item-grade-list"},{"name":"等级变动列表","module":"item-grade-change-list"}]},{"name":"商品审批","module":"","children":[{"name":"商品审批列表","module":"goods-check"},{"name":"商品等级变动审批","module":"goods-grade-check"}]},{"name":"商品分类管理","module":"","children":[{"name":"商品分类列表","module":"category-list"},{"name":"商品标签列表","module":"goods-label-list"}]},{"name":"品牌品类管理","module":"","children":[{"name":"品牌列表","module":"brand-list"},{"name":"品类列表","module":"productline-list"}]},{"name":"商品属性管理","module":"","children":[{"name":"商品属性列表","module":"specification-list"},{"name":"定制商品材料列表","module":"material-list"},{"name":"定制商品型号列表","module":"model-list"},{"name":"材料型号关联列表","module":"material-model-list"},{"name":"第三方材质型号关联","module":"thirdskurela"}]}]}
     */

    @JsonProperty("title")
    private String title;
    @JsonProperty("auth")
    private AuthDTO auth;
    private Integer sort;

    @NoArgsConstructor
    @Data
    public static class AuthDTO {
        @JsonProperty("actions")
        private List<ActionsDTO> actions;
        @JsonProperty("menus")
        private List<MenusDTO> menus;

        @NoArgsConstructor
        @Data
        public static class ActionsDTO {
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

        @NoArgsConstructor
        @Data
        public static class MenusDTO {
            /**
             * name : 商品管理 module : children :
             * [{"name":"商品列表","module":"goods-list"},{"name":"限购规则列表","module":"goods-purchaselimit"},{"name":"冻结商品列表","module":"goods-freezed"},{"name":"商品销量管理","module":"goods-get"}]
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
                 * name : 商品列表 module : goods-list
                 */

                @JsonProperty("name")
                private String name;
                @JsonProperty("module")
                private String module;
            }
        }
    }
}
