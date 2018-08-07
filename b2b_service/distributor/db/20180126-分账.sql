ALTER TABLE `distributor_db`.`distributor_tree_path` ADD INDEX `tree_node`(`tree_node`) USING BTREE;
ALTER TABLE `distributor_db`.`distributor_extend_data` ADD COLUMN `sub_account_flag` smallint(4) NULL DEFAULT NULL COMMENT '是否开启分账 1、是 0、否（开启C端模式并且C端结算模式属于自己收款才有值）' AFTER `erp_balance_flag`;

CREATE TABLE `distributor_db`.`distributor_sub_account_admin_config`  (
                                                                          `id` int(10) NOT NULL AUTO_INCREMENT,
                                                                          `distributor_id` int(10) NOT NULL COMMENT '分销商id',
                                                                          `aging_type` smallint(4) NOT NULL COMMENT '分账时效类型 1、实时 2、延迟',
                                                                          `delay_time` decimal(6, 2) NULL DEFAULT NULL COMMENT '延迟分账时间（单位小时）',
                                                                          `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                                          `create_user_id` int(10) NULL DEFAULT NULL COMMENT '创建人用户id',
                                                                          `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                                                          `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                                                          `update_user_id` int(10) NULL DEFAULT NULL COMMENT '修改人用户id',
                                                                          `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                                                          PRIMARY KEY (`id`) USING BTREE,
                                                                          UNIQUE INDEX `distributor_index`(`distributor_id`) USING BTREE COMMENT '分销商唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分销商分账运营后台配置表' ROW_FORMAT = Dynamic;

CREATE TABLE `distributor_db`.`distributor_sub_account_level`  (
                                                                   `id` int(11) NOT NULL AUTO_INCREMENT,
                                                                   `distributor_id` int(10) NOT NULL COMMENT '分销商id',
                                                                   `level_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '等级名称',
                                                                   `sequence` int(6) NOT NULL COMMENT '排序号',
                                                                   `delete_flag` smallint(4) NOT NULL DEFAULT 0 COMMENT '是否删除 1、是 0、否、默认是0',
                                                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                                   `create_user_id` int(10) NULL DEFAULT NULL COMMENT '创建人用户id',
                                                                   `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                                                   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                                                   `update_user_id` int(10) NULL DEFAULT NULL COMMENT '修改人用户id',
                                                                   `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                                                   PRIMARY KEY (`id`) USING BTREE,
                                                                   INDEX `distributor_index`(`distributor_id`, `sequence`) USING BTREE COMMENT '分销商和排序索引',
                                                                   INDEX `delete_flag_index`(`delete_flag`) USING BTREE COMMENT '是否删除索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分销商分账等级表' ROW_FORMAT = Dynamic;

CREATE TABLE `distributor_db`.`distributor_sub_account_ratio`  (
                                                                   `id` int(10) NOT NULL AUTO_INCREMENT,
                                                                   `sub_account_config_id` int(10) NOT NULL COMMENT '分销商侧分账配置id',
                                                                   `level_id` int(10) NOT NULL COMMENT '分账等级id',
                                                                   `ratio` decimal(6, 4) NOT NULL COMMENT '分账比例、存进去的值除以100',
                                                                   `delete_flag` smallint(4) NOT NULL DEFAULT 0 COMMENT '是否删除 1、是 0、否、默认是0',
                                                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                                   `create_user_id` int(10) NULL DEFAULT NULL COMMENT '创建人用户id',
                                                                   `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                                                   `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                                                   `update_user_id` int(10) NULL DEFAULT NULL COMMENT '修改人用户id',
                                                                   `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                                                   PRIMARY KEY (`id`) USING BTREE,
                                                                   INDEX `sub_account_config_index`(`sub_account_config_id`) USING BTREE COMMENT '分销商端分账配置id索引',
                                                                   INDEX `level_index`(`level_id`) USING BTREE COMMENT '等级id索引',
                                                                   INDEX `delete_flag_index`(`delete_flag`) USING BTREE COMMENT '是否删除标记索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账比例' ROW_FORMAT = Dynamic;

CREATE TABLE `distributor_db`.`distributor_sub_account_saleman`  (
                                                                     `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                                                     `type` smallint(4) NOT NULL COMMENT '身份类型 1、企业、2 个人',
                                                                     `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务员姓名',
                                                                     `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务员手机号',
                                                                     `level_id` int(10) NOT NULL COMMENT '分账等级id',
                                                                     `parent_id` int(10) NOT NULL COMMENT '上级业务员id、默认是0、表示顶级',
                                                                     `open_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销商配置的微信支付appid生成的openid',
                                                                     `merchant_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务员分账的商户号',
                                                                     `distributor_id` int(10) NOT NULL COMMENT '归属分销商id',
                                                                     `open_flag` smallint(4) NOT NULL COMMENT '状态 1、启用 0、禁用',
                                                                     `delete_flag` smallint(4) NOT NULL DEFAULT 0 COMMENT '是否删除 1、是 0、否、默认是0',
                                                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                                     `create_user_id` int(10) NULL DEFAULT NULL COMMENT '创建人分销商id',
                                                                     `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人分销商名称',
                                                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                                                     `update_user_id` int(10) NULL DEFAULT NULL COMMENT '修改人用户id',
                                                                     `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                                                     PRIMARY KEY (`id`) USING BTREE,
                                                                     INDEX `distributor_index`(`distributor_id`) USING BTREE COMMENT '归属分销商id索引',
                                                                     INDEX `parent_index`(`parent_id`) USING BTREE COMMENT '父id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账业务员' ROW_FORMAT = Dynamic;

CREATE TABLE `distributor_db`.`distributor_sub_account_user_config`  (
                                                                         `id` int(10) NOT NULL AUTO_INCREMENT,
                                                                         `distributor_id` int(10) NOT NULL COMMENT '分销商id',
                                                                         `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
                                                                         `amount_type` smallint(4) NOT NULL COMMENT '分账金额类型 1、按照实付金额 2、按照利润金额',
                                                                         `delete_flag` smallint(4) NOT NULL DEFAULT 0 COMMENT '是否删除 1、是 0、否 默认是0',
                                                                         `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                                         `create_user_id` int(10) NULL DEFAULT NULL COMMENT '创建人用户id',
                                                                         `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                                                         `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                                                         `update_user_id` int(10) NULL DEFAULT NULL COMMENT '修改人用户id',
                                                                         `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                                                         PRIMARY KEY (`id`) USING BTREE,
                                                                         INDEX `distributor_index`(`distributor_id`) USING BTREE COMMENT '分销商id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分销商分账设置（分销商端）' ROW_FORMAT = Dynamic;