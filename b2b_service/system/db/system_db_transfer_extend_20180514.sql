CREATE TABLE `goods_promotion`
(
    `id`                        int unsigned NOT NULL AUTO_INCREMENT,
    `extension_goods_name`      varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '推广商品名称',
    `pc_en_extension_img_url`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外pc推广商品图片',
    `pc_en_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外pc推广商品跳转链接',
    `pc_cn_extension_img_url`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内pc推广商品图片',
    `pc_cn_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内pc推广商品跳转链接',
    `distributor_scope`         smallint DEFAULT '1' COMMENT '分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组',
    `extension_start_time`      date                                                          DEFAULT NULL COMMENT '推广开始时间',
    `extension_end_time`        date                                                          DEFAULT NULL COMMENT '推广结束时间',
    `mo_en_extension_img_url`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外移动端推广商品图片',
    `mo_en_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外移动端推广商品跳转链接',
    `mo_cn_extension_img_url`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内移动端推广商品图片',
    `mo_cn_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内移动端推广商品跳转链接',
    `status`                    smallint DEFAULT '0' COMMENT '0:正常；1:伪删除（作废）',
    `create_time`               datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time`               datetime                                                      DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广父表';


CREATE TABLE `goods_promotion_department`
(
    `id`                 int unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `goods_promotion_id` int unsigned NOT NULL COMMENT '商品推广表ID',
    `department_id`      int unsigned NOT NULL COMMENT '业务部门ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与业务部门关联表';



CREATE TABLE `goods_promotion_distributor`
(
    `id`                 int unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `goods_promotion_id` int unsigned NOT NULL COMMENT '商品推广表ID',
    `distributor_id`     int unsigned NOT NULL COMMENT '分销商ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与分销商关联表';


CREATE TABLE `goods_promotion_distributor_grade`
(
    `id`                   int unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `goods_promotion_id`   int unsigned NOT NULL COMMENT '商品推广表的ID',
    `distributor_grade_id` int unsigned NOT NULL COMMENT '分销商等级ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与分销商等级关联表';


CREATE TABLE `distributor_goods_promotion_relevance`
(
    `id`                 int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `distributor_id`     int unsigned NOT NULL COMMENT '分销商id',
    `goods_promotion_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品推广id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='一级分销商可视商品推广';


CREATE TABLE `goods_promotion_user`
(
    `id`                 int unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
    `goods_promotion_id` int unsigned NOT NULL COMMENT '商品推广表ID',
    `user_id`            int unsigned NOT NULL COMMENT '业务员ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与业务员关联表';