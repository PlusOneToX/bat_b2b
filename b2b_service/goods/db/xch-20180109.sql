--删除不存在商品里的货品
select *
FROM `goods_item` gi
where NOT exists(select 1 from `goods` g where g.id = gi.goods_id)
DELETE
gi FROM `goods_item` gi WHERE not EXISTS(SELECT 1 FROM `goods` g WHERE g.id = gi.goods_id)

--货品库存是否缺货表中新增商品字段
ALTER TABLE `goods_db`.`goods_stock_flag`
    ADD COLUMN `goods_id` int NULL COMMENT '商品id' AFTER `id`;


ALTER TABLE `goods_db`.`goods_stock_flag`
    ADD COLUMN `whether_out_of_stock_in_transit` smallint(5) NULL COMMENT '在途是否缺货 0否 1是' AFTER `warehouse_id_arr`,
MODIFY COLUMN `under_stock_flag` smallint(5) UNSIGNED NOT NULL DEFAULT 0 COMMENT '在库是否缺货 0否 1是' AFTER `warehouse_id_arr`;