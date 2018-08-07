--增加特殊订单类型字段
alter table `order_type` add `special_flag` smallint unsigned NOT NULL DEFAULT '1' COMMENT '特殊类型, 1 普通,2 mto,3 现款,4 定制,5 直运' AFTER `erp_type`;
--增加订单类型默认仓库
alter table `order_type` add `warehouse_id` int unsigned DEFAULT NULL COMMENT '默认仓库id' AFTER `erp_type`;