alter table order_goods modify column `goods_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品名称';
alter table order_goods modify column `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称';
alter table shopping_cart_customer modify column `goods_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品名称';
alter table shopping_cart_customer modify column `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称';
alter table shopping_cart_distributor modify column `goods_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品名称';
alter table shopping_cart_distributor modify column `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称';
alter table order_deliver_bill_detail modify column `goods_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品名称';
alter table order_deliver_bill_detail modify column `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称';

--查询购物车里被删除的商品列表
SELECT * FROM order_db.`shopping_cart_customer` scc WHERE NOT EXISTS(SELECT 1 FROM goods_db.`goods_item` gi WHERE gi.id = scc.item_id)
--删除购物车里被删除的商品列表
delete scc FROM order_db.`shopping_cart_customer` scc WHERE NOT EXISTS(SELECT 1 FROM goods_db.`goods_item` gi WHERE gi.id = scc.item_id)
--查询购物车里被删除的商品列表
SELECT * FROM order_db.`shopping_cart_distributor` scc WHERE NOT EXISTS(SELECT 1 FROM goods_db.`goods_item` gi WHERE gi.id = scc.item_id)
--删除购物车里被删除的商品列表
delete scc FROM order_db.`shopping_cart_distributor` scc WHERE NOT EXISTS(SELECT 1 FROM goods_db.`goods_item` gi WHERE gi.id = scc.item_id)
--添加表索引
alter table order_info add KEY `create_time` (`create_time`) USING BTREE;
alter table order_distributor_data add KEY `direct_flag` (`direct_flag`) USING BTREE;
alter table order_distributor_data add KEY `erp_flag` (`erp_flag`) USING BTREE;