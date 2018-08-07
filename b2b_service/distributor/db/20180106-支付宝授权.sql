SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `distributor_db_101`.`alipay_platform`  (
                                                         `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                         `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台编码',
                                                         `type` smallint(5) UNSIGNED NOT NULL COMMENT '平台类型：1小程序',
                                                         `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝平台名称',
                                                         `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝平台appid',
                                                         `app_private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付宝平台密钥',
                                                         `app_public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付宝平台应用公钥',
                                                         `create_time` datetime NOT NULL COMMENT '创建时间',
                                                         `update_time` datetime NOT NULL COMMENT '更新时间',
                                                         PRIMARY KEY (`id`) USING BTREE,
                                                         UNIQUE INDEX `platform_app_id`(`app_id`, `platform`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分销商支付宝平台配置表' ROW_FORMAT = Dynamic;

CREATE TABLE `distributor_db_101`.`alipay_platform_distributor`  (
                                                                     `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                                     `alipay_platform_id` int(10) UNSIGNED NOT NULL COMMENT '抖音平台id',
                                                                     `distributor_id` int(10) UNSIGNED NOT NULL COMMENT '分销商ID',
                                                                     `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名(登录名)',
                                                                     `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名',
                                                                     PRIMARY KEY (`id`) USING BTREE,
                                                                     INDEX `distributor_id`(`distributor_id`) USING BTREE,
                                                                     INDEX `dy_platform_id`(`alipay_platform_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分销商支付宝平台关联关系表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS=1;