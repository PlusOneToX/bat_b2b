ALTER TABLE `flexible_db`.`shop` ADD COLUMN `saleman_id` int(10) NULL DEFAULT NULL COMMENT '分账业务员id（非系统业务员）' AFTER `platform`;

ALTER TABLE `flexible_db`.`shop` ADD COLUMN `saleman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务员名称' AFTER `saleman_id`;

ALTER TABLE `flexible_db`.`shop` ADD COLUMN `user_config_id` int(10) NULL DEFAULT NULL COMMENT '分销商端分账配置id' AFTER `saleman_name`;

ALTER TABLE `flexible_db`.`shop` ADD COLUMN `user_config_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销商分账配置名称' AFTER `user_config_id`;