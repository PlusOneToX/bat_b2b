ALTER TABLE `label` ADD COLUMN `third_sku_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签编码，多鸿工厂生产会用到' AFTER `relevance_user_upload`;
