ALTER TABLE `distributor_db`.`distributor_extend_data`
ADD COLUMN `sub_account_flag` smallint(4) NULL COMMENT '是否开启分账 1、是 0、否（开启C端模式并且C端结算模式属于自己收款才有值）' AFTER `erp_balance_flag`;
ALTER TABLE `distributor_db`.`distributor_contacts`
ADD COLUMN `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '小程序open_id' AFTER `freeze_status`;