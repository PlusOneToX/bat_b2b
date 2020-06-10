ALTER TABLE `flexible_db`.`shop`
ADD COLUMN `saleman_id` int(10) NULL COMMENT '分账业务员id（非系统业务员）' AFTER `platform`,
ADD COLUMN `saleman_name` varchar(100) NULL COMMENT '业务员名称' AFTER `saleman_id`,
ADD COLUMN `user_config_id` int(10) NULL COMMENT '分销商端分账配置id' AFTER `saleman_name`,
ADD COLUMN `user_config_name` varchar(255) NULL COMMENT '分销商分账配置名称' AFTER `user_config_id`;