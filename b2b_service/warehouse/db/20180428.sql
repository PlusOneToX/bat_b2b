CREATE TABLE `goods_on_way_stock` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '标准id',
  `goods_id` int(10) NOT NULL DEFAULT '0' COMMENT '商品id',
  `item_id` int(10) NOT NULL DEFAULT '0' COMMENT '货品id',
  `item_erp_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品货号ID',
  `num_on_way` int(10) NOT NULL DEFAULT '0' COMMENT '在途数量',
  `num_lock` int(10) NOT NULL DEFAULT '0' COMMENT '锁定数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `item_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品编码',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `item_id_index` (`item_id`) USING BTREE COMMENT '货品id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品在途数量信息表';

CREATE TABLE `goods_vmi_stock` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '标准id',
  `goods_id` int(10) NOT NULL DEFAULT '0' COMMENT '商品id',
  `item_id` int(10) NOT NULL DEFAULT '0' COMMENT '货品id',
  `item_erp_id` int(11) NOT NULL DEFAULT '0' COMMENT '商品货号ID',
  `num_vmi` int(10) NOT NULL DEFAULT '0' COMMENT 'vmi数量',
  `num_lock` int(10) NOT NULL DEFAULT '0' COMMENT '锁定数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `item_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品编码',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `item_index` (`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品vmi数量信息表';

CREATE TABLE `warehouse` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `warehouse_no` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '旧仓库编号',
  `area_id` int(10) unsigned NOT NULL COMMENT '区域ID',
  `sync_type` smallint(4) unsigned DEFAULT '1' COMMENT '集成类型，1集成 0、不集成',
  `is_platform` smallint(4) unsigned DEFAULT '1' COMMENT '是否平台仓, 0为否，1为是',
  `open_flag` smallint(4) NOT NULL COMMENT '启用状态 1、是 0、否',
  `del_flag` smallint(4) unsigned DEFAULT '0' COMMENT '是否删除，用来表示回收站 1、是 0、否',
  `calculation_type` smallint(4) DEFAULT '1' COMMENT '参与存销比计算:1-是 0、否',
  `sort_no` int(10) NOT NULL DEFAULT '1' COMMENT '仓库排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人用户名称',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `area_id` (`area_id`) USING BTREE,
  KEY `no` (`warehouse_no`) USING BTREE,
  KEY `open_flag` (`open_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='在库仓库表';

CREATE TABLE `warehouse_in_stock` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `warehouse_id` int(10) unsigned NOT NULL COMMENT '仓库ID',
  `goods_id` int(10) unsigned DEFAULT '0' COMMENT '商品ID',
  `item_id` int(10) unsigned DEFAULT '0' COMMENT '商品货号ID',
  `item_erp_id` int(10) NOT NULL COMMENT '商品erp物料id',
  `num_in_warehouse` int(10) DEFAULT '0' COMMENT '在库数量',
  `num_on_way` int(10) DEFAULT NULL COMMENT '在途数量',
  `num_in_warehouse_lock` int(11) DEFAULT NULL COMMENT '在库锁定数量(num_lock)',
  `num_on_way_lock` int(11) DEFAULT NULL COMMENT '在途锁定数量',
  `num_lock` int(10) DEFAULT '0' COMMENT '锁定数量',
  `num_reserved` int(10) unsigned DEFAULT '0' COMMENT '预留数量',
  `erp_num_in_warehouse` int(10) DEFAULT '0' COMMENT 'erp即时库存',
  `erp_num_lock` int(10) DEFAULT '0' COMMENT 'erp锁定数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `item_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品编码',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `warehouse_id` (`warehouse_id`,`item_id`),
  KEY `item_erp_id` (`item_erp_id`) USING BTREE,
  KEY `goods_id` (`goods_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='仓库在库库存表';

CREATE TABLE `warehouse_setting` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` smallint(4) NOT NULL COMMENT '类型，1同步时间，2交期时间',
  `value` int(11) NOT NULL COMMENT '参数值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `type_index_unique` (`type`) USING BTREE COMMENT '类型唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='仓库设置表';

CREATE TABLE `warehouse_stock_change_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `change_type` smallint(1) unsigned DEFAULT '1' COMMENT '变更类型 1.增加，2.减少',
  `source` smallint(1) unsigned DEFAULT '1' COMMENT '变更来源 1.ERP',
  `bill_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据编号',
  `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货品编号',
  `warehouse_no` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp编号',
  `num` int(10) DEFAULT '0' COMMENT '变更数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='库存变更日志';

CREATE TABLE `warehouse_stock_reserved` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单号id',
  `warehouse_id` int(10) unsigned NOT NULL COMMENT '仓库ID',
  `status` smallint(4) unsigned DEFAULT '0' COMMENT '状态，0未处理, 1处理',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '预留备注',
  `business_id` int(10) unsigned DEFAULT NULL COMMENT '业务单号',
  `source` smallint(4) unsigned DEFAULT '1' COMMENT '预留来源 1：手工添加，2：收单工具',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id(手工添加时填写B2B用户信息、收单工具填写用户信息)',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='库存预留表';

CREATE TABLE `warehouse_stock_reserved_detail` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品ID',
  `item_id` int(10) unsigned NOT NULL COMMENT '商品货号ID',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货品名称',
  `num_reserved` int(10) DEFAULT '0' COMMENT '预留数量',
  `reserved_id` int(10) unsigned NOT NULL COMMENT '预留单号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `warehouse_id` (`goods_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='库存预留明细表';