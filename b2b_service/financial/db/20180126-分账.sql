ALTER TABLE `financial_db`.`account_wx_distributor` ADD COLUMN `account_type` smallint(4) NOT NULL DEFAULT 1 COMMENT '账户类型 1、自有账户 2、服务商二级商户' AFTER `id`;
ALTER TABLE `financial_db`.`account_wx_distributor` ADD COLUMN `sub_account_ratio` decimal(6, 4) NULL DEFAULT NULL COMMENT '最大分账比例、账户类型选择服务商二级商户才有值' AFTER `account_type`;
ALTER TABLE `financial_db`.`account_wx_distributor` MODIFY COLUMN `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户号对应的appid（公众号小程序不可信/以前端传值为准）' AFTER `account_name`;
ALTER TABLE `financial_db`.`account_wx_distributor` ADD COLUMN `sub_mchid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务商的子商户号（服务商的钱收到这个商户里面）' AFTER `update_time`;

ALTER TABLE `financial_db`.`pay_bills_customer` ADD COLUMN `mchid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款的商户号（服务商存的是收款的子商户号、服务商退款需要传这个）' AFTER `app_id`;
ALTER TABLE `financial_db`.`pay_bills_customer` ADD COLUMN `sp_mchid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务商归属商户号' AFTER `mchid`;

CREATE TABLE `financial_db`.`order_sub_account`  (
                                                     `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                     `distributor_id` int(10) NOT NULL COMMENT '分销商id',
                                                     `distributor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
                                                     `order_id` int(10) NOT NULL COMMENT 'B2B订单id',
                                                     `order_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号（B2B）',
                                                     `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信支付凭证（返回的微信流水号）',
                                                     `last_transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后微信分账成功返回的流水号（要拿这个流水号作为out_order_no）',
                                                     `shop_id` int(10) NULL DEFAULT NULL COMMENT '门店id',
                                                     `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店名称',
                                                     `pay_amount` decimal(8, 2) NOT NULL COMMENT '实际支付金额',
                                                     `max_sub_account_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '最大分账金额',
                                                     `profit_account` decimal(8, 2) NULL DEFAULT NULL COMMENT '利润金额（按照利润分账有值）',
                                                     `actual_sub_account_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '实际分账金额（不能超过最大分账金额）',
                                                     `status` smallint(4) NOT NULL COMMENT '分账状态 0、已关闭 1、待分账 2、部分分账 3、全部分账 ',
                                                     `sub_mchid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务商收款的子商户号（相当于从这个服务商商户号分账）',
                                                     `sub_account_fail_flag` smallint(4) NULL DEFAULT NULL COMMENT '是否存在分账失败 1、是 0、否',
                                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                                     `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     `plan_time` datetime(0) NULL DEFAULT NULL COMMENT '计划分账时间',
                                                     PRIMARY KEY (`id`) USING BTREE,
                                                     UNIQUE INDEX `order_id_index`(`order_id`) USING BTREE COMMENT '一个订单只有一个分账'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单分账表' ROW_FORMAT = Dynamic;

CREATE TABLE `financial_db`.`order_sub_account_bill`  (
                                                          `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                          `order_sub_account_id` int(10) NOT NULL COMMENT '订单分账表id',
                                                          `level_id` int(10) NULL DEFAULT NULL COMMENT '分账等级id',
                                                          `level_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '等级名称',
                                                          `max_sub_account_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '最大分账金额（下单时默认的分账金额）',
                                                          `actual_sub_account_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '实际分账金额（不能超过最大分账金额）',
                                                          `ratio` decimal(6, 4) NOT NULL COMMENT '分账比例（实际分账比例）',
                                                          `status` smallint(4) NOT NULL COMMENT '分账状态 0、已关闭 1、待分账 2、部分分账 3、全部分账 ',
                                                          `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账的openid',
                                                          `merchant_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账接收方商户号',
                                                          `out_trade_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账单号',
                                                          `saleman_id` int(10) NULL DEFAULT NULL COMMENT '分销商业务员ID',
                                                          `saleman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销商业务员名称',
                                                          `success_flag` smallint(4) NULL DEFAULT NULL COMMENT '是否分账成功 1、是 0、否',
                                                          `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败原因',
                                                          `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                          `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
                                                          `success_time` datetime(0) NULL DEFAULT NULL COMMENT '分账成功时间',
                                                          `sub_transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信分账成功返回的流水号（要拿这个流水号作为out_order_no）',
                                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;