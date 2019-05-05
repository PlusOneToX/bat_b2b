-- 增加采购订单入库类型字段
ALTER TABLE `platform_tenant_erp`
    ADD COLUMN `po_inbound_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp 采购入库单据类型,多个情况中间用逗号隔开' AFTER `vmi_warehouse`;