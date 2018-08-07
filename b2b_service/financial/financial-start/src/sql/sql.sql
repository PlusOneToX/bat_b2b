ALTER TABLE `financial_db`.`account_wx_distributor`
ADD COLUMN `account_type` smallint(4) NOT NULL DEFAULT 1 COMMENT '账户类型 1、自有账户 2、服务商二级商户' AFTER `id`,
ADD COLUMN `sub_account_ratio` decimal(6, 4) NULL COMMENT '最大分账比例、账户类型选择服务商二级商户才有值' AFTER `account_type`;

ALTER TABLE `financial_db`.`account_wx_distributor`
ADD COLUMN `sub_mchid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '服务商的子商户号（服务商的钱收到这个商户里面）' AFTER `update_time`;

ALTER TABLE `financial_db`.`pay_bills_customer`
ADD COLUMN `mchid` varchar(50) NULL COMMENT '收款的商户号（服务商存的是收款的子商户号、服务商退款需要传这个）' AFTER `app_id`,
ADD COLUMN `sp_mchid` varchar(50) NULL COMMENT '服务商归属商户号' AFTER `mchid`;