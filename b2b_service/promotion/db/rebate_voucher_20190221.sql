DROP TABLE IF EXISTS `rebate_voucher`;
CREATE TABLE `rebate_voucher`  (
                                   `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                                   `distributor_id` int(11) NOT NULL COMMENT '分销商id',
                                   `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
                                   `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券名称',
                                   `voucher_no` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券编号',
                                   `face_value` decimal(10, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '面值',
                                   `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
                                   `apply_status` smallint(6) UNSIGNED NOT NULL DEFAULT 0 COMMENT '申请状态 0草稿 1待审核 2审核通过(可用) 3审核拒绝',
                                   `freeze_status` smallint(6) UNSIGNED NOT NULL COMMENT '冻结状态 10未冻结(可用) 11冻结',
                                   `voucher_status` smallint(6) UNSIGNED NOT NULL DEFAULT 1 COMMENT '代金券状态（汇总） 0草稿 1待审核 3审核拒绝 4待生效 5 可用 7已过期 9已用完 11已冻结',
                                   `start_time` datetime NOT NULL COMMENT '开始时间',
                                   `end_time` datetime NOT NULL COMMENT '结束时间',
                                   `sort` int(11) NULL DEFAULT NULL COMMENT '排序号',
                                   `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `update_time` datetime NOT NULL COMMENT '更新时间',
                                   `batch_id` int(11) NULL DEFAULT NULL COMMENT '批量id(一起导入的第一个代金券id)',
                                   `batch_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批量名称(一起导入的第一个代金券名称)',
                                   `create_user_id` int(10) NULL DEFAULT NULL COMMENT '创建人用户id',
                                   `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE INDEX `uk_voucher_no`(`voucher_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '返利代金券' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `rebate_voucher_usage_record`;
CREATE TABLE `rebate_voucher_usage_record`  (
                                                `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                                                `rebate_voucher_id` int(11) NOT NULL COMMENT '代金券id',
                                                `rebate_voucher_no` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券编号',
                                                `use_amount` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '使用金额 负数为使用 正数为退还',
                                                `balance` decimal(10, 4) NOT NULL DEFAULT 0.0000 COMMENT '余额',
                                                `order_id` int(11) NOT NULL COMMENT '订单id',
                                                `order_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
                                                `use_time` datetime NOT NULL COMMENT '使用时间',
                                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '返利代金券使用记录' ROW_FORMAT = Dynamic;

ALTER TABLE `order_distributor_cost` ADD COLUMN `rebate_voucher_amount` decimal(16, 4) NULL DEFAULT NULL COMMENT '代金券抵扣金额' AFTER `order_promotion_amount`;
ALTER TABLE `order_goods_distributor_cost` ADD COLUMN `rebate_voucher_amount` decimal(16, 4) NULL DEFAULT NULL COMMENT '商品单价抵扣 金额' AFTER `actual_price`;
