ALTER TABLE `platform_db`.`platform_tenant_diy_factory`
    ADD COLUMN `shop_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称（B2B在生产系统中展示名称，多鸿会用到）' AFTER `config_other`;