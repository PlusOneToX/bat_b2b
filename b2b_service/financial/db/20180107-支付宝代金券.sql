ALTER TABLE `pay_bills_distributor` ADD COLUMN `receipt_amount` decimal(10, 3) NULL DEFAULT NULL COMMENT '实收金额' AFTER `app_id`;

ALTER TABLE `pay_bills_distributor` ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `receipt_amount`;

ALTER TABLE `pay_bills_customer` ADD COLUMN `receipt_amount` decimal(10, 3) NULL DEFAULT NULL COMMENT '实收金额' AFTER `app_id`;

ALTER TABLE `pay_bills_customer` ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `receipt_amount`;