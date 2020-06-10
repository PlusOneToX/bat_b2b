CREATE TABLE `banner_series_picture_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `banner_id` int(10) NOT NULL COMMENT '系列id',
  `picture_id` int(10) DEFAULT NULL COMMENT '图片id',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商首页banner和主题系列图片关联';

CREATE TABLE `burying_point` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源',
  `user_id` int(10) unsigned DEFAULT NULL COMMENT '用户id',
  `distributor_id` int(10) unsigned DEFAULT NULL COMMENT '分销商id',
  `network_type` smallint(4) unsigned NOT NULL COMMENT '网络类型 1.未知 2.wifi 3.3g 4.4g 5.5g',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='网络埋点';

CREATE TABLE `distributor_banner` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `banner_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片链接',
  `sort_no` int(10) NOT NULL COMMENT '排序号',
  `status` tinyint(4) NOT NULL COMMENT '状态 0、未开始、1、展示中 2、已结束',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) NOT NULL COMMENT '结束时间',
  `show_location` smallint(4) DEFAULT NULL COMMENT '运营位置类型 1、首页主banner',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备注信息',
  `type` smallint(4) DEFAULT NULL COMMENT '运营类型 1、首页IP系列 2、活动页链接',
  `series_id` int(10) DEFAULT NULL COMMENT '系列id',
  `external_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '外部链接',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商banner';

CREATE TABLE `distributor_banner_relevance` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `banner_id` int(20) NOT NULL COMMENT 'banner id',
  `distributor_id` int(20) NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名称',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='banner分销商关联表';

CREATE TABLE `distributor_index_recommend` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商首页推荐';

CREATE TABLE `distributor_index_recommend_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_recommend_id` int(10) NOT NULL COMMENT '首页推荐id',
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名称',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商首页推荐关联';

CREATE TABLE `distributor_material_exclude` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `material_id` int(10) NOT NULL COMMENT '材质ID',
  `distributor_id` int(10) NOT NULL COMMENT '分销商ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '状态 0、正常 1、删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `material_id` (`material_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商柔性定制材质不可视表';

CREATE TABLE `distributor_model_exclude` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `model_id` int(10) NOT NULL COMMENT '型号ID',
  `distributor_id` int(10) NOT NULL COMMENT '分销商ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `model_id` (`model_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商柔性定制型号不可视表';

CREATE TABLE `distributor_picture_exclude` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `picture_id` int(10) NOT NULL COMMENT '型号ID',
  `distributor_id` int(10) NOT NULL COMMENT '分销商ID',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '状态 0、正常 1、删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `picture_id` (`picture_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商柔性定制图片不可视表';

CREATE TABLE `distributor_series_theme` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `theme_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题名称',
  `sort_no` int(10) NOT NULL COMMENT '排序号',
  `category_id` int(10) NOT NULL COMMENT '图片分类id',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商首页系列';

CREATE TABLE `distributor_series_theme_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `series_theme_id` int(10) NOT NULL COMMENT '主题系列id',
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属公司',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商主题系列关联表';

CREATE TABLE `exchange_card` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `item_id` int(10) DEFAULT NULL COMMENT '对应的卡片id（货品）、兑换商城才有值',
  `code_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '兑换码名称',
  `status` smallint(4) NOT NULL COMMENT '状态  0、草稿（初始化） 1、已发布（未开始） 2、启用 3、停用 4、结束',
  `code_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '兑换码备注',
  `type` smallint(4) DEFAULT NULL COMMENT '券码类型 1、专属码',
  `source` smallint(4) DEFAULT NULL COMMENT '生成来源 1、系统生成 2、人工导入（is_entity为0时、根据这个来判断）',
  `code_quantity` int(10) DEFAULT NULL COMMENT '生成数量(累加)',
  `limit_quantity` int(10) DEFAULT NULL COMMENT '限制使用上限、为null表示不限制',
  `sale_quantity` int(10) DEFAULT NULL COMMENT '销售数量',
  `exchange_quantity` int(10) DEFAULT NULL COMMENT '用户已兑换的数量',
  `refund_quantity` int(10) DEFAULT NULL COMMENT '退货数量（B2B和erp合计、已销售数量',
  `invalid_count` int(10) DEFAULT NULL COMMENT '作废数量（还没销售出去的作废）',
  `start_time` bigint(20) DEFAULT NULL COMMENT '有效时间区间开始',
  `end_time` bigint(20) DEFAULT NULL COMMENT '有效时间区间结束',
  `exchange_way` smallint(4) DEFAULT NULL COMMENT '兑换方式  1、兑换',
  `order_use_threshold` decimal(10,2) DEFAULT NULL COMMENT '订单适用门槛 空表示不限制',
  `goods_scope` smallint(4) DEFAULT NULL COMMENT '适用商品 1、全部商品可用2、指定商品 ',
  `qr_code_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二维码路径',
  `is_entity` smallint(4) DEFAULT NULL COMMENT '是否生成实体卡 1、是 0、否',
  `is_use_mall` smallint(4) DEFAULT NULL COMMENT '是否使用兑换商城 1、是 0、否',
  `mall_type` smallint(4) DEFAULT NULL COMMENT '兑换商城类型 1、定制商城',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `model_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '兑换卡物料卡片型号（兑换商城使用）',
  `head_img` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '兑换卡标识头图片地址',
  `distributor_scope` smallint(4) NOT NULL COMMENT '分销商范围 1、全部可用 2、指定分销商',
  `mail_setting` smallint(4) NOT NULL DEFAULT '1' COMMENT '快递收费模式 1、包邮（普通卡）2、收运费（赠卡）3、收运费（普通卡加收用户运费）',
  `mail_fee` decimal(6,2) DEFAULT '0.00' COMMENT '邮费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡';

CREATE TABLE `exchange_card_transfer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_id` int(11) unsigned NOT NULL COMMENT '兑换卡id',
  `transfer_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '转赠文案',
  `transfer_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '转赠封面图',
  `receive_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收取页面文案',
  `receive_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收取卡片页面配图',
  `switch_flag` smallint(4) unsigned NOT NULL DEFAULT '1' COMMENT '是否开启转增 0否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_exchange_id` (`exchange_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='兑换卡转赠配置';

CREATE TABLE `exchange_code` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_id` int(10) DEFAULT NULL,
  `plain_code` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '明码',
  `secret_code` varchar(155) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暗码',
  `box_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '盒码',
  `status` smallint(4) DEFAULT NULL COMMENT '核销状态 0、未激活、1、未使用 2、已核销 3、已过期 4、已作废',
  `distributor_order_id` int(10) DEFAULT NULL COMMENT 'b2b对应的订单id',
  `distributor_order_goods_id` int(10) DEFAULT NULL COMMENT 'b2b对应的订单详情id',
  `distributor_quanyi_price` decimal(16,3) unsigned DEFAULT '0.000' COMMENT '分销商权益价格（分销商等级价）',
  `distributor_id` int(10) DEFAULT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `distributor_company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'B2B对应的分销商公司名称',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id（核销用户）',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '核销人',
  `user_order_id` int(10) DEFAULT NULL COMMENT '核销订单id（B2B的）',
  `user_order_goods_id` int(10) DEFAULT NULL COMMENT '核销订单商品id',
  `user_third_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '核销订单号（三方、例如柔性）',
  `use_time` datetime DEFAULT NULL COMMENT '核销时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(20) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `exchange_factory_id` int(20) DEFAULT NULL COMMENT '同步工厂记录id',
  `outbound_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '出库单号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `plain_unique_index` (`plain_code`) USING BTREE,
  UNIQUE KEY `secret_unique_index` (`secret_code`) USING BTREE,
  KEY `exchange_index` (`exchange_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换码';

CREATE TABLE `exchange_code_invalid_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_id` int(10) DEFAULT NULL COMMENT '兑换卡活动id',
  `code_id` int(10) DEFAULT NULL COMMENT '兑换码id exchange_code主键',
  `refund_order_id` int(10) DEFAULT NULL COMMENT '退款单id',
  `type` smallint(4) DEFAULT NULL COMMENT '类型、1、ERP退货作废，2、B2B作废 ',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原因',
  `create_user_id` int(20) DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换码作废记录表';

CREATE TABLE `exchange_code_outbound_restore_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `box_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '盒码',
  `new_outbound_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '新的出库单号',
  `old_outbound_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原来出库单号',
  `distributor_order_id` int(10) DEFAULT NULL COMMENT 'b2b对应的订单id',
  `distributor_order_goods_id` int(10) DEFAULT NULL COMMENT 'b2b对应的订单详情id',
  `distributor_id` int(10) DEFAULT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换码盒码发错重置表';

CREATE TABLE `exchange_code_restore_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_code_id` int(10) NOT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `old_order_goods_id` int(10) DEFAULT NULL COMMENT '原来的b2b订单商品id',
  `old_order_id` int(10) DEFAULT NULL COMMENT '原来的b2b订单id',
  `old_user_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原来的用户核销订单号',
  `old_user_id` bigint(20) DEFAULT NULL COMMENT '原来的用户id',
  `use_time` datetime DEFAULT NULL COMMENT '原来的核销时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡核销重置记录表';

CREATE TABLE `exchange_code_sync_back_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `item_id` int(10) NOT NULL COMMENT '货品id',
  `exchange_id` int(10) NOT NULL COMMENT '兑换卡活动id',
  `exchange_code_id` int(10) DEFAULT NULL COMMENT '兑换码id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `already_sync_box_count` int(10) DEFAULT NULL COMMENT '该货品已同步的盒码数量',
  `already_sync_plain_code_count` int(10) DEFAULT NULL COMMENT '该货品已同步的明码数量',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `exchange_code_id_index` (`exchange_code_id`) USING BTREE,
  KEY `item_id_index` (`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='同步盒码回B2B记录批次表';

CREATE TABLE `exchange_code_transfer_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_id` int(11) unsigned NOT NULL COMMENT '兑换活动id',
  `exchange_code_id` int(11) unsigned NOT NULL COMMENT '兑换码id',
  `from_user_id` int(11) unsigned NOT NULL COMMENT '转赠发起人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `receive_flag` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '是否已收取 0否 1是',
  `to_user_id` int(11) unsigned DEFAULT NULL COMMENT '收卡人',
  `receive_time` datetime DEFAULT NULL COMMENT '收卡时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='兑换码转赠记录';

CREATE TABLE `exchange_code_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_code_id` int(10) unsigned NOT NULL COMMENT '兑换码id',
  `user_id` int(10) unsigned NOT NULL COMMENT 'c端用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_code_id` (`exchange_code_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='c端用户与兑换码关联关系';

CREATE TABLE `exchange_distributor_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `exchange_id` int(10) NOT NULL COMMENT '兑换卡活动id',
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商归属公司',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `exchange_index` (`exchange_id`) USING BTREE COMMENT '兑换活动id索引',
  KEY `distributor_id_index` (`distributor_id`) USING BTREE COMMENT '分销商id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡指定分销商关联表';

CREATE TABLE `exchange_entity_rule` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `exchange_id` int(10) NOT NULL COMMENT '兑换卡活动id',
  `card_type` smallint(4) DEFAULT NULL COMMENT '卡码设置类型 1、系统生成 2、手动导入',
  `rule_type` smallint(4) NOT NULL COMMENT '卡片码生成规则 1、系统随机 2、按规则生成',
  `rise_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '抬头值',
  `float_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '浮动值',
  `random_value` int(10) DEFAULT NULL COMMENT '卡片码生成规则为1系统随机时填写、数字和英文随机位数',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_sync_factory` smallint(4) DEFAULT '1' COMMENT '是否在线同步工厂生产 1、是 0、否',
  `box_num` int(4) DEFAULT '10' COMMENT '每盒装数量（默认是10张、1张时取明码做盒码）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡实体卡生成规则';

CREATE TABLE `exchange_export` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `distributor_id` int(11) unsigned NOT NULL COMMENT '发卡分销商id',
  `export_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '导出记录单名称',
  `exchange_id` int(11) unsigned NOT NULL COMMENT '兑换卡活动id',
  `out_num` int(11) unsigned NOT NULL COMMENT '出库数量',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '导出文件url',
  `examine_flag` smallint(4) unsigned NOT NULL COMMENT '是否已审核 0否 1是',
  `has_use_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '已使用数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='电子兑换卡导出记录';

CREATE TABLE `exchange_export_code` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_export_id` int(11) unsigned NOT NULL COMMENT '导出记录id',
  `exchange_code_id` int(11) unsigned NOT NULL COMMENT '兑换码id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `exchange_factory` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `exchange_id` int(10) NOT NULL COMMENT '兑换卡id',
  `add_quantity` int(10) NOT NULL COMMENT '添加数量',
  `invalid_quantity_init` int(10) DEFAULT NULL COMMENT '作废数量（未同步到工厂前）',
  `synchronized_quantity` int(10) NOT NULL DEFAULT '0' COMMENT '同步数量',
  `status` smallint(4) NOT NULL COMMENT '同步工厂状态 0、未同步 1、同步中 2、同步成功 3、同步失败',
  `batch_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '同步到工厂批次单号',
  `factory` smallint(4) DEFAULT NULL COMMENT '同步工厂 1、印想',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源url、保存生成的txt路径',
  `positive_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '正面url',
  `reverse_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '反面url',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡工厂同步表';

CREATE TABLE `exchange_general_item_pool` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `item_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货品编码',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态 1、启用 0、禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡卡片关联货品（通用版）编码池';

CREATE TABLE `exchange_material_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `exchange_id` int(10) NOT NULL COMMENT '兑换卡活动id',
  `material_id` int(10) NOT NULL COMMENT '材质id',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `exchange_index` (`exchange_id`) USING BTREE COMMENT '兑换活动id索引',
  KEY `material_index` (`material_id`) USING BTREE COMMENT '材质id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡材质关联表';

CREATE TABLE `exchange_model_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `exchange_id` int(10) NOT NULL COMMENT '兑换卡活动id',
  `model_id` int(10) DEFAULT NULL COMMENT '型号id',
  `type` smallint(4) NOT NULL DEFAULT '2' COMMENT '使用范围 1、全部使用 2、部分可用（model_id有值）',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `exchange_index` (`exchange_id`) USING BTREE COMMENT '兑换活动id索引',
  KEY `model_index` (`model_id`) USING BTREE COMMENT '型号索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡型号关联表';

CREATE TABLE `exchange_notice` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `start_time` bigint(20) unsigned NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) unsigned NOT NULL COMMENT '结束时间',
  `status` smallint(4) unsigned NOT NULL COMMENT '状态 0禁用 1启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换商城公告';

CREATE TABLE `exchange_picture_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `exchange_id` int(10) NOT NULL COMMENT '兑换卡活动id',
  `picture_id` int(10) DEFAULT NULL COMMENT '图片id',
  `type` smallint(4) NOT NULL DEFAULT '2' COMMENT '使用范围 1、全部使用 2、部分可用（picture_id有值）',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `exchange_index` (`exchange_id`) USING BTREE COMMENT '兑换活动id索引',
  KEY `picture_index` (`picture_id`) USING BTREE COMMENT '图片id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡图片关联表';

CREATE TABLE `exchange_refund_order` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `refund_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '退款单号',
  `refund_time` datetime DEFAULT NULL COMMENT '退货时间',
  `type` smallint(4) DEFAULT NULL COMMENT '类型 1、erp 2、B2B',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原因',
  `create_user_id` int(20) DEFAULT NULL COMMENT '创建人、erp创建为空',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='兑换卡退货单';

CREATE TABLE `exchange_share` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_platform` smallint(4) NOT NULL COMMENT '活动所属平台 1兑换商城 2定制商城',
  `prefer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '减免优惠活动名称',
  `seat` smallint(4) unsigned NOT NULL COMMENT '活动位置 1确认订单页 2订单详情页',
  `forward_button_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '转发按钮文案',
  `reduce_flag` smallint(4) unsigned NOT NULL COMMENT '是否减免优惠 0否 1是',
  `reduce_amount` decimal(16,3) unsigned DEFAULT NULL COMMENT '减免金额',
  `exchange_special_id` int(11) unsigned NOT NULL COMMENT '专题活动id',
  `distributor_visual_type` smallint(1) NOT NULL COMMENT '分销商可视类型 1全部 2指定',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` smallint(4) unsigned NOT NULL COMMENT '状态 0禁用 1启用',
  `forward_num` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '转发次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='转发活动配置';

CREATE TABLE `exchange_share_distributor` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_share_id` int(11) unsigned NOT NULL COMMENT '转发活动配置id',
  `distributor_id` int(11) unsigned NOT NULL COMMENT '分销商id',
  `type` smallint(4) unsigned NOT NULL COMMENT '关系类型 1当前分销商可视 2当前分销商不可视（活动为指定全部分销商生效）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='转发活动配置分销商配置信息';

CREATE TABLE `exchange_special` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_platform` smallint(4) NOT NULL COMMENT '活动所属平台 1兑换商城',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '活动标题',
  `page_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '页面配图',
  `page_guide_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '活动页面引导文案',
  `type` smallint(4) unsigned NOT NULL COMMENT '活动福利类型;1兑换卡',
  `exchange_id` int(11) unsigned NOT NULL COMMENT '兑换活动id',
  `forward_text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '转发文案',
  `forward_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '活动转发封面',
  `friend_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '朋友圈分享图',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` smallint(4) unsigned NOT NULL COMMENT '状态 0停用 1启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='营销专题';

CREATE TABLE `exchange_special_distributor` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_special_id` int(11) unsigned NOT NULL COMMENT '专题id',
  `distributor_id` int(11) unsigned NOT NULL COMMENT '分销商id',
  `page_visits` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '页面访问次数',
  `one_forward_times` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '一次转发次数',
  `two_forward_times` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '二次转发次数',
  `receive_times` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '福利领取次数',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接',
  `status` smallint(4) unsigned NOT NULL COMMENT '状态 0禁用 1启用',
  `qr_code_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序二维码链接',
  `short_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '短链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='专题分销商配置信息';

CREATE TABLE `exchange_special_receive` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `special_release_id` int(11) unsigned NOT NULL COMMENT '转发专题发布id',
  `user_id` int(11) unsigned NOT NULL COMMENT '领取人id',
  `exchange_code_id` int(11) unsigned NOT NULL COMMENT '领取的兑换卡id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='转发专题领取';

CREATE TABLE `exchange_special_release` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `exchange_share_id` int(11) unsigned NOT NULL COMMENT '转发id',
  `exchange_special_id` int(11) unsigned NOT NULL COMMENT '专题id',
  `exchange_special_distributor_id` int(11) unsigned DEFAULT NULL COMMENT '专题分销商配置id',
  `distributor_id` int(11) unsigned NOT NULL COMMENT '发卡分销商id',
  `user_id` int(11) unsigned NOT NULL COMMENT '发起人用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='转发专题发布';

CREATE TABLE `flexible_distributor_cooperation` (
  `id` int(10) NOT NULL COMMENT '主键id',
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `sequence` int(8) DEFAULT NULL COMMENT '排序号（优先级）',
  `default_choose` smallint(4) DEFAULT NULL COMMENT '是否默认选中（针对新增图片指定分销商之类的默认选中） 1、是 0、否',
  `cooperation_type` smallint(4) DEFAULT '2' COMMENT '合作类型 1、全部渠道类型 2、部分渠道合作、默认是2',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '1、启用 0、禁用',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 1、已删除 0、正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='柔性合作分销商表';

CREATE TABLE `font` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `file_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名稱',
  `english_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字体英文名称',
  `sequence` int(6) NOT NULL COMMENT '排序',
  `font_file` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字体包路径',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述信息',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态: 1为启用，0为禁用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '状态: 1为已删除 0、为正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='字体表';

CREATE TABLE `index_recommend_picture_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_recommend_id` int(10) NOT NULL COMMENT '首页推荐id',
  `picture_id` int(10) DEFAULT NULL COMMENT '图片id',
  `sort_no` int(10) DEFAULT NULL COMMENT '排序号',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='首页推荐图片关联';

CREATE TABLE `label` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `template_file` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板文件',
  `type` smallint(10) NOT NULL COMMENT '类型: 1. 普通标签，2.定制标签',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态: 1为启用，0为弃用',
  `bar_code_position_x` decimal(6,1) NOT NULL COMMENT '条形码位置X轴',
  `bar_code_position_y` decimal(6,1) NOT NULL COMMENT '条形码位置Y轴',
  `bar_code_position_width` decimal(6,1) NOT NULL COMMENT 'bar_code文本内容所占宽度',
  `bar_code_position_height` decimal(6,1) NOT NULL COMMENT 'bar_code文本内容所占高度',
  `product_name_position_x` decimal(6,1) DEFAULT NULL COMMENT '产品名称位置X坐标',
  `product_name_position_y` decimal(6,1) DEFAULT NULL COMMENT '产品名称位置Y坐标',
  `product_name_position_width` decimal(6,1) DEFAULT NULL COMMENT '产品名称内容在标签上所占宽度',
  `product_name_position_height` decimal(6,1) DEFAULT NULL COMMENT '产品名称内容在标签上所占高度',
  `product_name_position_font` int(11) DEFAULT NULL COMMENT '产品名位置显示字体',
  `product_name_position_font_size` int(11) DEFAULT NULL COMMENT '产品名称位置字体大小',
  `model_position_x` decimal(6,1) DEFAULT NULL COMMENT '型号内容在标签上的横坐标',
  `model_position_y` decimal(6,1) DEFAULT NULL COMMENT '型号内容在标签上的坐标',
  `model_position_width` decimal(6,1) DEFAULT NULL COMMENT '型号内容所占宽度',
  `model_position_height` decimal(6,1) DEFAULT NULL COMMENT '型号内容所占高度',
  `model_position_font` int(11) DEFAULT NULL COMMENT '产品型号位置内容显示字体',
  `model_position_font_size` int(11) DEFAULT NULL COMMENT '产品型号位置字体大小',
  `scope` smallint(4) NOT NULL COMMENT '适用分销商范围 1为全部，2 为国内， 3为国外，4 为指定',
  `category_id` int(10) NOT NULL DEFAULT '10010' COMMENT '类型id',
  `en_name_position_x` decimal(6,1) DEFAULT NULL COMMENT '英文名称位置X轴',
  `en_name_position_y` decimal(6,1) DEFAULT NULL COMMENT '英文名称位置Y轴',
  `en_name_position_width` decimal(6,1) DEFAULT NULL COMMENT '英文名称位置宽度',
  `en_name_position_height` decimal(6,1) DEFAULT NULL COMMENT '英文名称位置高度',
  `en_name_position_font` int(11) DEFAULT NULL COMMENT '英文产品名称位置字体信息',
  `en_name_position_font_size` int(11) DEFAULT NULL COMMENT '英文产品名称位置字体大小',
  `stuff_name_position_x` decimal(6,1) DEFAULT NULL COMMENT '材质名称内容在标签上的横坐标',
  `stuff_name_position_y` decimal(6,1) DEFAULT NULL COMMENT '材质名称内容在标签上的纵坐标',
  `stuff_name_position_width` decimal(6,1) DEFAULT NULL COMMENT '材质名称内容在标签上所占宽度',
  `stuff_name_position_height` decimal(6,1) DEFAULT NULL COMMENT '材质名称内容在标签上所占高度',
  `stuff_name_position_font` int(11) DEFAULT NULL COMMENT '材质名称字体名称',
  `stuff_name_position_font_size` int(11) DEFAULT NULL COMMENT '材质名称字体大小',
  `stuff_en_name_position_x` decimal(6,1) DEFAULT NULL COMMENT '材质英文名称内容在标签上的横坐标',
  `stuff_en_name_position_y` decimal(6,1) DEFAULT NULL COMMENT '材质英文名称内容在标签上的纵坐标',
  `stuff_en_name_position_width` decimal(6,1) DEFAULT NULL COMMENT '材质英文名称内容在标签上所占宽度',
  `stuff_en_name_position_height` decimal(6,1) DEFAULT NULL COMMENT '材质英文名称内容在标签上所占高度',
  `stuff_en_name_position_font` int(11) DEFAULT NULL COMMENT '材质英文名称字体名称',
  `stuff_en_name_position_font_size` int(11) DEFAULT NULL COMMENT '材质英文名称字体大小',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  `relevance_user_upload` smallint(4) DEFAULT NULL COMMENT '是否关联用户自己上传的图片 1、是 0、否 （定制标签才有值）',
  `third_sku_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签编码，多鸿工厂生产会用到',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `type_index` (`type`) USING BTREE COMMENT '标签类型索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='标签表';

CREATE TABLE `label_distributor_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `label_id` int(10) NOT NULL COMMENT '标签id',
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1、是 0、否',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `label_id` (`label_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='标签与分销商之间的关联关系表';

CREATE TABLE `material` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(10) NOT NULL COMMENT '上一级id',
  `material_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '材质编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `english_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文名称',
  `category_id` int(10) NOT NULL COMMENT '分类id',
  `sequence` int(6) NOT NULL COMMENT '序号',
  `image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片',
  `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述信息',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详情信息',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '1是启用，0是禁用，默认值为1',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  `manufactor` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '生产商 YC云创 MK麦客 LHW联辉王',
  `colour` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '颜色',
  `at_last_trademark` smallint(4) NOT NULL COMMENT '是否为最终材质 1、是 0、否',
  `stuff_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '材质术语',
  `stuff_en_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '材质英文术语',
  `subtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '副标题',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `item_id` int(10) DEFAULT NULL COMMENT '关联的货品id',
  `item_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品编码',
  `allow_upload_flag` smallint(4) DEFAULT NULL COMMENT '是否允许用户上传图片 1、是 0、否（最终材质才有）',
  `mandatory_covered_bleed_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否强制铺满出血位 1、是 0、否',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `categroy_id` (`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='材料表';

CREATE TABLE `model` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `english_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文名称',
  `category_id` int(10) NOT NULL COMMENT '分类: 暂定为写死的数据类型 1为手机壳，2为杯子',
  `parent_id` int(10) NOT NULL COMMENT '父级id',
  `sequence` int(8) NOT NULL COMMENT '排序',
  `image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '型号图片',
  `depict` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '型号描述',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态: 1为开启，0为弃用',
  `at_last_trademark` smallint(4) NOT NULL COMMENT '是否为品牌 1、是 0、否',
  `network_model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入网型号',
  `model_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '型号编码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `cn` (`category_id`,`name`) USING BTREE,
  KEY `parent_index` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='型号表';

CREATE TABLE `model_material_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `model_id` int(10) NOT NULL COMMENT '型号id',
  `material_id` int(10) NOT NULL COMMENT '材料id',
  `length` decimal(6,2) NOT NULL COMMENT '长度',
  `width` decimal(6,2) NOT NULL COMMENT '宽度',
  `height` decimal(6,2) NOT NULL COMMENT '高度',
  `weight` decimal(6,2) NOT NULL COMMENT '重量',
  `out_image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '外框图',
  `floor_image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '底图',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态值 1、启用 0、禁用',
  `third_sku` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方sku编码',
  `warn_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提醒信息',
  `bom_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'bom编码',
  `cut_type` smallint(4) unsigned DEFAULT NULL COMMENT '切割类型 1纵切 2横切',
  `slitting_height` int(11) unsigned DEFAULT NULL COMMENT '纵切高度',
  `slitting_white` int(11) unsigned DEFAULT NULL COMMENT '纵切高度留白',
  `crosscutting_width` int(11) unsigned DEFAULT NULL COMMENT '横切宽度',
  `crosscutting_white` int(11) unsigned DEFAULT NULL COMMENT '横切宽度留白',
  `under_stock_flag` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '是否缺货 0否 1是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  `top_frame` decimal(6,2) unsigned DEFAULT NULL COMMENT '上边框',
  `under_frame` decimal(6,2) unsigned DEFAULT NULL COMMENT '下边框',
  `left_frame` decimal(6,2) unsigned DEFAULT NULL COMMENT '左边框',
  `right_frame` decimal(6,2) unsigned DEFAULT NULL COMMENT '右边框',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `model_index` (`model_id`) USING BTREE COMMENT '型号id索引',
  KEY `material_index` (`material_id`) USING BTREE COMMENT '材质id索引',
  KEY `open_flag_index` (`open_flag`) USING BTREE COMMENT '状态索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='材质型号关联信息表';

CREATE TABLE `picture` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编码，用于承接外部业务的专用',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `english_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文名称',
  `type` smallint(4) NOT NULL COMMENT '图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图',
  `category_id` int(10) NOT NULL COMMENT '图片所属分类id 外键',
  `sequence` int(6) NOT NULL COMMENT '序号',
  `origin_image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原图',
  `thumbnail` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '缩略图',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述信息',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态 0为关闭，1为启用',
  `model_scope` smallint(4) NOT NULL COMMENT '图片的适用范围(哪些型号可以适用这个图片信息): 1为全部可用，2为指定型号可用,3为指定类型可用',
  `reseller_scope` smallint(4) NOT NULL COMMENT '分销商适用范围 1为全部，2为国内，3为国外，4 指定',
  `theme_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主题链接',
  `template_center_x` decimal(8,2) DEFAULT NULL COMMENT '模板中心点x轴',
  `template_center_y` decimal(8,2) DEFAULT NULL COMMENT '模板中心点Y轴',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `copyright_cost` decimal(6,2) DEFAULT '0.00' COMMENT '版权费用',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  `background_preview_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '背景预览图',
  `no_camera_vacancy_preview` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '无相机空位预览图',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `categroy_id` (`category_id`) USING BTREE,
  KEY `open_flag` (`open_flag`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='图片表';

CREATE TABLE `picture_category` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(10) NOT NULL COMMENT '父id',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `english_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文名称',
  `sequence` decimal(14,10) NOT NULL COMMENT '序号(1级没有小数、2级XX.X一位小数、3级XX.XXX3位小数 4级5位小数)',
  `image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类图片',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '介绍信息',
  `at_last_trademark` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否为最底级图片分类 1、是 0、否',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态 1为启用，0为关闭',
  `type` smallint(4) NOT NULL DEFAULT '1' COMMENT '图片类型: 1 为普通素材，2.IP素材，3、模板，4、贴图',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1为删除，0为正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='图片分类表';

CREATE TABLE `picture_category_theme_relevance` (
  `id` int(10) unsigned NOT NULL COMMENT '图片主题id',
  `category_id` int(10) unsigned NOT NULL COMMENT '图片类别id',
  `sequence` int(10) unsigned NOT NULL COMMENT '排序号',
  PRIMARY KEY (`id`,`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='图片主题与图片关系表';

CREATE TABLE `picture_distributor_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `picture_id` int(10) NOT NULL COMMENT '图片编号',
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '状态：0为正常，1为删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `picture_id` (`picture_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='图片与分销商关联关系表';

CREATE TABLE `picture_label_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `picture_id` int(10) NOT NULL COMMENT '图片编号',
  `label_id` int(10) NOT NULL COMMENT '标签id',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '状态: 0为正常，1为删除，默认值为0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `picture_id` (`picture_id`) USING BTREE,
  KEY `label_id` (`label_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='图片与标签的关联关系表';

CREATE TABLE `picture_material_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `picture_id` int(10) NOT NULL COMMENT '图片id',
  `material_id` int(10) NOT NULL COMMENT '材质id',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '状态: 0为正常，1为删除，默认值为0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `material_id_index` (`material_id`) USING BTREE COMMENT '材质id索引',
  KEY `picture_id_index` (`picture_id`) USING BTREE COMMENT '图片id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='图片材质关联关系';

CREATE TABLE `picture_model_material_diy` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `picture_id` int(10) NOT NULL COMMENT '图片编号',
  `material_id` int(10) NOT NULL COMMENT '材质编号',
  `model_id` int(10) NOT NULL COMMENT '型号编号',
  `generate_image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '定制生成的图片PDF',
  `preview_image` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '定制图片预览地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `picture_id` (`picture_id`) USING BTREE,
  KEY `material_id` (`material_id`) USING BTREE,
  KEY `model_id` (`model_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='图片（IP图）、型号材质材质定制信息表';

CREATE TABLE `picture_model_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `picture_id` int(10) NOT NULL COMMENT '图片id',
  `model_id` int(10) NOT NULL COMMENT '型号id',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '状态: 0为正常，1为删除，默认值为0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `picture_id` (`picture_id`) USING BTREE,
  KEY `model_id` (`model_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

CREATE TABLE `picture_product_category_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `picture_id` int(10) NOT NULL COMMENT 'id',
  `category_id` int(10) NOT NULL COMMENT '分类: 暂定为写死的数据类型 10010为手机壳，10020手机背膜',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `picture_id` (`picture_id`) USING BTREE,
  KEY `category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='图片与型号类型关联表';

CREATE TABLE `picture_template_edit` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `picture_id` int(10) NOT NULL COMMENT '图片id',
  `internal_edit_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '内部编辑图url',
  `edit_center_x` decimal(8,2) NOT NULL COMMENT '可编辑区域中心点X轴',
  `edit_center_y` decimal(8,2) NOT NULL COMMENT '可编辑区域中心点Y轴',
  `length` decimal(8,2) NOT NULL COMMENT '长度（毫米）',
  `width` decimal(8,2) NOT NULL COMMENT '宽度（毫米）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='图片模板可编辑区域';

CREATE TABLE `picture_theme` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主题名称',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `open_flag` smallint(4) unsigned NOT NULL DEFAULT '1' COMMENT '状态 0禁用 1启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE `product_category` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品类型名称',
  `english_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品类型名称（英文）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `open_flag` smallint(4) NOT NULL DEFAULT '1' COMMENT '1、启用 0、禁用',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 1、已删除 0、正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='产品类型';

CREATE TABLE `series_picture_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `series_id` int(10) NOT NULL COMMENT '系列id',
  `sort_no` int(10) DEFAULT NULL COMMENT '排序号',
  `picture_id` int(10) DEFAULT NULL COMMENT '图片id',
  `create_user_id` int(10) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `series_picture_index` (`series_id`,`picture_id`) USING BTREE,
  KEY `series_index` (`series_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='首页系列图片关联表';

CREATE TABLE `shop` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) DEFAULT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名称',
  `shop_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '店铺名称',
  `shop_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '店铺编码',
  `wx_platform_id` int(10) DEFAULT NULL COMMENT '微信支付账户id',
  `open_flag` smallint(4) NOT NULL COMMENT '店铺状态 1 正常 0、禁用',
  `del_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1、是 0、正常、默认是正常',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注 30字符',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信url',
  `qr_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二维码url',
  `extend_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '第三方url(扩展参数)',
  `third_qr_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '第三方二维码url',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) NOT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'APP_ID',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'app名称',
  `source` smallint(4) DEFAULT '1' COMMENT '创建来源 1、B2B后台 2、B2B前台',
  `type` smallint(4) DEFAULT '1' COMMENT '类型 1、微信公众号 2、微信小程序 ',
  `platform` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '微信关联的平台编码',
  `saleman_id` int(10) DEFAULT NULL COMMENT '分账业务员id（非系统业务员）',
  `saleman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务员名称',
  `user_config_id` int(10) DEFAULT NULL COMMENT '分销商端分账配置id',
  `user_config_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商分账配置名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='门店';

CREATE TABLE `third_courier_contrast` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `distributor_id` int(10) NOT NULL COMMENT '分销商Id',
  `distribution_kdn_code` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '快递公司编码（国家统一）',
  `distributor_shipper_name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商自定义名称',
  `distributor_shipper_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商自定义编码',
  `open_flag` smallint(4) DEFAULT NULL COMMENT '状态 1、启用 2、禁用',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人id',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='第三方快递公司对照表';

CREATE TABLE `third_sku_no_name_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `distributor_id` int(10) NOT NULL COMMENT '对应分销商Id',
  `third_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方编码（手机壳、膜之类的）',
  `third_ext_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拓展名称（型号系列之类的）',
  `third_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方编码对应名称',
  `type` smallint(4) DEFAULT NULL COMMENT '类型、1、物料（材质分类、手机壳、膜之类的）2、品牌（型号品牌）3、型号（最低级的型号）4、材质（最终材质）5、材质颜色',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `third_type_index` (`third_no`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='三方商品编码名称信息';

CREATE TABLE `third_sku_relevance` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `distributor_id` int(10) NOT NULL COMMENT '对应分销商Id',
  `material_id` int(10) DEFAULT NULL COMMENT '材质id',
  `model_id` int(10) DEFAULT NULL COMMENT '型号id',
  `third_material_category_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方物料分类编码（手机壳、膜之类的）',
  `third_brand_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方品牌编码',
  `third_model_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方型号编码',
  `third_material_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方材质编码',
  `third_colour_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方颜色编码',
  `third_sku_no` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '三方最终sku',
  `open_flag` smallint(4) DEFAULT NULL COMMENT '状态 1、启用 0、禁用',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_thirdSkuNo_index` (`distributor_id`,`third_sku_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='柔性商品信息外部sku关联';