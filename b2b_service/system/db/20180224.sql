CREATE TABLE `check` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '审批编号',
  `ext` smallint(6) unsigned NOT NULL COMMENT '审批模块 1.商品管理上下架审批 2. 分销商编辑审批 3. 仓库库存调整审批 4. 仓库库存预留审批 5. 订单价格审批 6. 订单对账折扣审批 7促销活动新增审批 8促销活动编辑审批 9 10商品等级变动 11 12 ',
  `sub_ext` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '审批子模块 ',
  `sub_ext1` int(10) unsigned DEFAULT NULL COMMENT '审批子模块扩展类型 ',
  `apply_user` int(10) unsigned NOT NULL COMMENT '申请人',
  `status` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '审批状态 0.未审批 1.审批中，2.审批通过 3.审批未通过',
  `last_check_user` int(10) unsigned DEFAULT '0' COMMENT '最后一个审批过的人',
  `next_check_user` int(10) unsigned DEFAULT '0' COMMENT '下一个审批人',
  `through_check_count` smallint(6) unsigned DEFAULT '0' COMMENT '已审批人数',
  `check_user_count` smallint(6) unsigned DEFAULT '0' COMMENT '审批总人数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `last_check_time` datetime DEFAULT NULL COMMENT '最后审批时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `apply_user` (`apply_user`,`ext`,`update_time`) USING BTREE,
  KEY `next_check_user` (`next_check_user`,`ext`,`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='审批表';

CREATE TABLE `check_config` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '审批单编号',
  `ext` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '审批单类型 1.商品管理上下架审批（在用） 2. 分销商编辑审批（在用） 3. 仓库库存调整审批 4. 仓库库存预留审批 5. 订单价格审批 6. 订单对账折扣审批 7促销活动新增审批 8促销活动编辑审批 9\r\n     * 10商品等级变动（在用） 11 12 13 促销活动审批（在用） 14 优惠券（在用） 15 拼团（在用）',
  `check_user` int(10) unsigned DEFAULT '0' COMMENT '审批人',
  `check_order` smallint(6) DEFAULT '0' COMMENT '审批排序',
  `open_flag` smallint(6) unsigned NOT NULL DEFAULT '1' COMMENT '审批是否开启 1.审批开启,2.审批关闭',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ext` (`ext`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='审批配置表';

CREATE TABLE `check_flow` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '审批流程编号',
  `check_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '审批编号',
  `check_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '审批人 ',
  `check_status` smallint(6) unsigned DEFAULT '0' COMMENT '审批状态 0, 未审批 1,审批通过，2审批未通过',
  `check_time` datetime NOT NULL COMMENT '审批时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '审批备注',
  `check_order` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '审批顺序',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `check_id` (`check_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='审批流程表';

CREATE TABLE `check_relation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '审批模块关联编号',
  `ext` smallint(6) unsigned NOT NULL COMMENT '审批模块 1.商品管理上下架审批 2. 分销商编辑审批 3. 仓库库存调整审批 4. 仓库库存预留审批 5. 订单价格审批 6. 订单对账折扣审批',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ext` (`ext`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='审批模块关联表';

CREATE TABLE `distributor_goods_promotion_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `goods_promotion_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品推广id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='一级分销商可视商品推广';

CREATE TABLE `download_center` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) unsigned NOT NULL COMMENT '父id',
  `sort` int(5) NOT NULL DEFAULT '0' COMMENT '排序值',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '0禁用 1启用',
  `title_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 中文',
  `title_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 英文',
  `content_url_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '内容 资源地址 中文',
  `content_url_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '内容 资源地址 英文',
  `thumbnail_url_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '缩略图 资源地址(未启用) 中文',
  `thumbnail_url_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '缩略图 资源地址(未启用) 英文',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='下载中心';

CREATE TABLE `export_download` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `platform` smallint(6) NOT NULL COMMENT '平台来源 web admin,前台管理后台',
  `operator_id` int(11) NOT NULL COMMENT '分销商id 业务员id',
  `business_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务名称 什么导出',
  `download_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '下载链接',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='导出下载记录表';

CREATE TABLE `global_agreement_distributor` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agreement_id` int(11) NOT NULL COMMENT '协议id',
  `distributor_id` int(11) NOT NULL COMMENT '分销商id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_distributor_id_agreement_id` (`distributor_id`,`agreement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='全站设置协议分销商关联';

CREATE TABLE `global_agreement_setting` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '协议名称',
  `agreement_area` smallint(6) NOT NULL COMMENT '协议发布区域 0国内 1国外',
  `type` smallint(6) NOT NULL COMMENT '协议类型 1品牌协议 2用户协议',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '协议内容',
  `status` smallint(6) NOT NULL COMMENT '协议状态 0禁用 1启用',
  `brand_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属品牌',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='全站设置协议设置';

CREATE TABLE `global_agreement_setting_brand` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agreement_setting_id` int(11) NOT NULL COMMENT '协议设置id',
  `agreement_area` smallint(6) NOT NULL COMMENT '协议区域',
  `brand_id` int(11) NOT NULL COMMENT '所属品牌',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_agreement_area_brand` (`agreement_area`,`brand_id`) USING BTREE COMMENT '协议区域 品牌 协议设置唯一约束'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='全站设置协议设置';

CREATE TABLE `global_base_setting` (
  `key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'value',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述备注',
  PRIMARY KEY (`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='全站设置基础设置表（系统常量）';

CREATE TABLE `global_factory_setting_delay_push` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `factory` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工厂',
  `push_time` datetime DEFAULT NULL COMMENT '推送时间',
  `use_range` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '相关分销商',
  `type` int(1) DEFAULT NULL COMMENT '0全部可用 1指定可用 2指定不可用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='全站设置工厂设置工厂推送表';

CREATE TABLE `global_factory_setting_order_invalid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道名称',
  `order_source` int(2) DEFAULT NULL COMMENT '渠道来源',
  `invalid` int(5) DEFAULT NULL COMMENT '订单时效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='订单时效表';

CREATE TABLE `global_shop_setting` (
  `key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'value',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述备注',
  PRIMARY KEY (`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='全站设置购物设置表（系统常量）';

CREATE TABLE `goods_promotion` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `extension_goods_name` varchar(55) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '推广商品名称',
  `pc_en_extension_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外pc推广商品图片',
  `pc_en_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外pc推广商品跳转链接',
  `pc_cn_extension_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内pc推广商品图片',
  `pc_cn_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内pc推广商品跳转链接',
  `distributor_scope` smallint(6) DEFAULT '1' COMMENT '分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组',
  `extension_start_time` date DEFAULT NULL COMMENT '推广开始时间',
  `extension_end_time` date DEFAULT NULL COMMENT '推广结束时间',
  `mo_en_extension_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外移动端推广商品图片',
  `mo_en_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国外移动端推广商品跳转链接',
  `mo_cn_extension_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内移动端推广商品图片',
  `mo_cn_extension_goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国内移动端推广商品跳转链接',
  `status` smallint(6) DEFAULT '0' COMMENT '0:正常；1:伪删除（作废）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广父表';

CREATE TABLE `goods_promotion_department` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_promotion_id` int(10) unsigned NOT NULL COMMENT '商品推广表ID',
  `department_id` int(10) unsigned NOT NULL COMMENT '业务部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与业务部门关联表';

CREATE TABLE `goods_promotion_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_promotion_id` int(10) unsigned NOT NULL COMMENT '商品推广表ID',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与分销商关联表';

CREATE TABLE `goods_promotion_distributor_grade` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_promotion_id` int(10) unsigned NOT NULL COMMENT '商品推广表的ID',
  `distributor_grade_id` int(10) unsigned NOT NULL COMMENT '分销商等级ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与分销商等级关联表';

CREATE TABLE `goods_promotion_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_promotion_id` int(10) unsigned NOT NULL COMMENT '商品推广表ID',
  `user_id` int(10) unsigned NOT NULL COMMENT '业务员ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品推广与业务员关联表';

CREATE TABLE `logistics` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配送名称',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `enable` smallint(6) NOT NULL DEFAULT '1' COMMENT '是否启用, 0为停用，1为启用',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '描述',
  `logistics_erp_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'erp配送方式编号',
  `billing_method` smallint(6) NOT NULL DEFAULT '1' COMMENT '计费方式 1 重量计费(默认) 2 体积计费',
  `first_weight` double(16,2) DEFAULT '0.00' COMMENT '首重重量',
  `first_volume` double(16,2) DEFAULT '0.00' COMMENT '首重体积',
  `additional_weight` double(16,2) DEFAULT '0.00' COMMENT '续重重量',
  `additional_volume` double(16,2) DEFAULT '0.00' COMMENT '续重体积',
  `min_weight` double(16,2) unsigned DEFAULT '0.00' COMMENT '最低重量',
  `min_volume` double(16,2) DEFAULT '0.00' COMMENT '最低体积',
  `max_weight` double(16,2) unsigned DEFAULT '0.00' COMMENT '最高重量',
  `max_volume` double(16,2) DEFAULT '0.00' COMMENT '最高体积',
  `min_cost` double(16,2) unsigned DEFAULT '0.00' COMMENT '最低运费',
  `use_range` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '适用范围，1.普通商品，2.定制商品 3普通商品和定制商品',
  `logistics_factory_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '工厂配送方式编号',
  `logistics_kdn_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '快递鸟快递公司编码',
  `logistics_kdn_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '快递鸟快递公司名称',
  `appoint_area_flag` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '是否指定地区 0为统一设置 1为指定地区',
  `distributor_scope` smallint(6) NOT NULL DEFAULT '1' COMMENT '1全部分销商,2等级分销商,3事业部,4指定业务部门,5指定业务员,6指定分销商',
  `manufactors` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生产商 YC.云创 LHW.联辉王（同时支持，中间用","号隔开）',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '快递官网地址',
  `material_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '材质id 多个材质逗号分隔',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='配送表';

CREATE TABLE `logistics_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `logistics_id` int(11) unsigned NOT NULL COMMENT '配送id',
  `first_weight_cost` double(16,2) DEFAULT NULL COMMENT '首重费用',
  `first_volume_cost` double(16,2) DEFAULT NULL COMMENT '首体积费用',
  `additional_weight_cost` double(16,2) DEFAULT NULL COMMENT '续重费用',
  `additional_volume_cost` double(16,2) DEFAULT NULL COMMENT '续体积费用',
  `default_flag` smallint(6) unsigned DEFAULT '1' COMMENT '是否使用默认 0为否 1为是',
  `formula_flag` smallint(6) unsigned DEFAULT '1' COMMENT '是否使用公式, 0为否，1为是',
  `formula` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公式',
  `group_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '相同配送配置分组编码',
  `country_id` int(11) DEFAULT NULL COMMENT '国家id',
  `province_id` int(11) DEFAULT NULL COMMENT '省份id',
  `city_id` int(11) DEFAULT NULL COMMENT '城市id',
  `district_id` int(11) DEFAULT NULL COMMENT '区域id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `logistics_id` (`logistics_id`) USING BTREE,
  KEY `uk_logistic_area` (`logistics_id`,`country_id`,`province_id`,`city_id`,`district_id`) USING BTREE COMMENT '同一区域唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='配送计费表';

CREATE TABLE `logistics_department` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `logistics_id` int(11) unsigned NOT NULL COMMENT '配送方式ID',
  `department_id` int(11) unsigned NOT NULL COMMENT '业务部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='配送方式业务部门关联表';

CREATE TABLE `logistics_distributor` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `logistics_id` int(11) unsigned NOT NULL COMMENT '配送方式ID',
  `distributor_id` int(11) unsigned NOT NULL COMMENT '分销商ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='配送方式分销商关联表';

CREATE TABLE `logistics_distributor_grade` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `logistics_id` int(11) unsigned NOT NULL COMMENT '配送方式ID',
  `distributor_grade_id` int(11) unsigned NOT NULL COMMENT '分销商等级ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='配送方式分销商等级关联表';

CREATE TABLE `logistics_third_mapping` (
  `id` int(11) NOT NULL,
  `logistics_id` int(11) DEFAULT NULL COMMENT 'bat配送方式Id',
  `third_type` smallint(6) DEFAULT NULL COMMENT '第三方类型 1、麦客',
  `status` smallint(6) DEFAULT NULL COMMENT '状态 1、启用 0、禁用',
  `third_delivery_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '第三方配送编号',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='配送第三方关联表';

CREATE TABLE `logistics_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `logistics_id` int(11) unsigned NOT NULL COMMENT '配送方式ID',
  `user_id` int(11) unsigned NOT NULL COMMENT '业务员ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='配送方式业务员关联表';

CREATE TABLE `region` (
  `id` int(11) NOT NULL COMMENT '地域id',
  `region_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地域名',
  `parent_id` int(11) NOT NULL COMMENT '父节点id ',
  `have_next` smallint(6) DEFAULT NULL COMMENT '是否还有下一级 1是  0否',
  `region_name_en` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区域英文名称',
  `level` smallint(6) DEFAULT NULL COMMENT '区域级数 国家/省/市/区 /1/2/3/4',
  `erp_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ERP编码',
  `open_flag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='省市区表';

CREATE TABLE `region_comparison` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `region_id` int(11) NOT NULL COMMENT '地域id',
  `region_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地域名',
  `another_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对照地域名',
  `parent_id` int(11) NOT NULL COMMENT '父节点id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='省市对照表';

CREATE TABLE `store_banner` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `image_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '推广图片地址',
  `banner_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '推广链接地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `banner_area` smallint(6) unsigned DEFAULT '0' COMMENT '推广区域 0-国内 1 海外',
  `sort` int(11) DEFAULT NULL COMMENT '排序值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置首页推广数据表';

CREATE TABLE `store_column` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `title_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '栏目英文标题',
  `banner_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '栏目banner图',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `release_status` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '发布状态，0,未发布，1 发布 2 取消发布',
  `column_area` smallint(6) NOT NULL DEFAULT '0' COMMENT '栏目使用区域 0-国内 1 海外 2 国内和海外',
  `distributor_scope` smallint(6) NOT NULL COMMENT '1全部分销商,2等级分销商,3事业部,4指定业务部门,5指定业务员,6指定分销商',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置首页栏目表';

CREATE TABLE `store_column_department` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `column_id` int(10) unsigned NOT NULL COMMENT '栏目ID',
  `department_id` int(10) unsigned NOT NULL COMMENT '业务部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置栏目业务部门关联表';

CREATE TABLE `store_column_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `column_id` int(10) unsigned NOT NULL COMMENT '栏目ID',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置栏目分销商关联表';

CREATE TABLE `store_column_distributor_grade` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `column_id` int(10) unsigned NOT NULL COMMENT '栏目ID',
  `distributor_grade_id` int(10) unsigned NOT NULL COMMENT '分销商等级ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置栏目分销商等级关联表';

CREATE TABLE `store_column_goods` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `column_id` int(11) unsigned NOT NULL COMMENT '栏目编号',
  `goods_id` int(11) unsigned NOT NULL COMMENT '商品编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `column_id` (`column_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置栏目商品关联表(暂时不用)';

CREATE TABLE `store_column_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `column_id` int(10) unsigned NOT NULL COMMENT '栏目ID',
  `user_id` int(10) unsigned NOT NULL COMMENT '业务员ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置栏目业务员关联表';

CREATE TABLE `store_mobile` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sort` int(11) NOT NULL COMMENT '排序值',
  `module_type` smallint(6) NOT NULL COMMENT '模块类型 1轮播图 2图片模块 3商品推广模块 4商品列表模块',
  `status` smallint(6) NOT NULL COMMENT '0 隐藏 1显示',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商店配置移动端首页设置表';

CREATE TABLE `store_mobile_child` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) unsigned NOT NULL COMMENT '父级id',
  `appoint_type` smallint(5) unsigned NOT NULL COMMENT '指定类型 1.指定分类 2.指定品牌 3.指定商品 4.全部商品',
  `title` varchar(255) DEFAULT NULL COMMENT '分类标题',
  `sort` int(11) unsigned DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端商品子选项（只针对商品列表模块有效）';

CREATE TABLE `store_mobile_goods` (
  `id` int(11) NOT NULL,
  `mobile_id` int(11) NOT NULL COMMENT '移动端设置id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商店配置移动端首页设置商品关联表';

CREATE TABLE `store_mobile_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `mobile_id` int(11) NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '轮播图时为轮播图url 图片模块为图片url 商品推广模块时为背景url',
  `width_percentage` decimal(16,3) unsigned DEFAULT NULL COMMENT '图片宽度百分比',
  `jump_type` smallint(6) DEFAULT '0' COMMENT '0 无链接 1 跳转商品 2跳转分类 3跳转其他页面 4跳转搜索结果页',
  `jump_params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '跳转目标（跳转参数）',
  `sub_sort` int(11) DEFAULT '0' COMMENT '子内容排序',
  `style_type` smallint(6) DEFAULT '0' COMMENT '商品推广模块下：样式类型0 无效 1 3列商品 2 4列商品 3 多列商品',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='商店配置移动端首页设置详情表';

CREATE TABLE `store_mobile_point` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mobile_child_id` int(11) unsigned NOT NULL COMMENT '移动端首页指定子类id',
  `point_id` int(11) unsigned NOT NULL COMMENT '指定id(分类id或者品牌id)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端首页子类品牌或者分类指定表';

CREATE TABLE `store_notice` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `attachment_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '附件名称',
  `attachment_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '附件url',
  `release_area` smallint(6) unsigned NOT NULL COMMENT '发布地区 0 国内 1国外',
  `release_status` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '发布状态，0,未发布，1 发布',
  `release_time` datetime NOT NULL COMMENT '发布时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消发布时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置首页公告表';

CREATE TABLE `store_section` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '标题',
  `title_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '板块英文标题',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `release_status` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '发布状态，0,未发布，1 发布',
  `image_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '推广图片地址',
  `extension_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '推广链接地址',
  `image_url_en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '板块英文图片链接',
  `extension_url_en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '板块英文图片链接',
  `style_type` smallint(6) DEFAULT '1' COMMENT '1.样式1 2.样式2 3.样式3 4.样式4',
  `style_type_en` smallint(6) DEFAULT '1' COMMENT '1.样式1 2.样式2 3.样式3 4.样式4',
  `section_area` smallint(6) NOT NULL DEFAULT '0' COMMENT '板块使用区域 0-国内 1 海外 2 国内和海外',
  `style_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式图片1地址(国内1)',
  `style_extension_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式连接1地址',
  `style_url1` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式图片2地址(国内2)',
  `style_extension_url1` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式连接2地址',
  `style_url2` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式图片3地址(海外1)',
  `style_extension_url2` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式连接3地址',
  `style_url3` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式图片4地址(海外2)',
  `style_extension_url3` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '样式连接4地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `section_order` (`sort`,`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商店配置首页板块表';

CREATE TABLE `store_section_goods` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `section_id` int(11) unsigned NOT NULL COMMENT '板块编号',
  `goods_id` int(11) unsigned NOT NULL COMMENT '商品编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `section_id` (`section_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='板块商品关联表(暂时不用)';

CREATE TABLE `sys_department` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` int(11) unsigned DEFAULT NULL COMMENT '父id',
  `organization_id` int(10) unsigned DEFAULT '0' COMMENT '组织id',
  `department_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `department_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名称英文',
  `sort` int(11) unsigned DEFAULT NULL COMMENT '排序值',
  `erp_department_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp系统中的销售部门id',
  `description` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门描述',
  `sale_type` smallint(6) unsigned DEFAULT '0' COMMENT '是否销售部门，0否 1是 ',
  `status` smallint(6) unsigned DEFAULT NULL COMMENT '0 禁用 1启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_org_id_erp_dep_id` (`organization_id`,`erp_department_id`) USING BTREE COMMENT '组织部门唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='销售部门信息表';

CREATE TABLE `sys_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `service` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务',
  `service_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '服务英文',
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块（一级标题）',
  `module_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模块英文',
  `menu` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单（二级标题）',
  `menu_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单英文(link)',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';

CREATE TABLE `sys_organization` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组织名',
  `erp_organization_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ERP_组织代码',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `status` smallint(6) unsigned NOT NULL COMMENT '0 禁用 1 启用 2删除(目前物理删除)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_erp_organization_id
uk_erp_organization_id` (`erp_organization_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='销售组织表';

CREATE TABLE `sys_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `service` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '服务',
  `service_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '服务英文',
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模块',
  `module_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模块英文',
  `permission_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名称',
  `permission_module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限英文名称',
  `url_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url路径',
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '方法',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `status` smallint(6) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_url_path_method` (`url_path`,`method`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限表';

CREATE TABLE `sys_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名',
  `role_name_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色英文名',
  `role_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

CREATE TABLE `sys_role_menu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) unsigned NOT NULL COMMENT '角色id',
  `menu_id` int(11) unsigned NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

CREATE TABLE `sys_role_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色权限关联表';

CREATE TABLE `sys_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '手机号',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `ding_avatar` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '钉钉头像',
  `admin_type` smallint(4) DEFAULT NULL COMMENT '管理员类型, 1.超级管理员 2.普通用户',
  `brand_scope` smallint(4) unsigned DEFAULT '0' COMMENT '品牌范围,0无 1.全局 2.其他',
  `sale_scope` smallint(4) unsigned DEFAULT '0' COMMENT '业务范围,1.全局 2.单业务范围 3.多业务范围',
  `status` smallint(4) DEFAULT '1' COMMENT '状态, 1.启用 0.禁用',
  `erp_user_no` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'ERP_用户编码',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织id',
  `department_id` int(11) unsigned DEFAULT '0' COMMENT '部门id',
  `rock_account_id` bigint(20) DEFAULT '0' COMMENT '账户中心id',
  `fictitious_flag` smallint(4) DEFAULT '0' COMMENT '是否虚拟销售员，0否 1是',
  `sale_flag` smallint(4) DEFAULT '1' COMMENT '是否销售员，0否 1是 ',
  `remark` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_name` (`user_name`) USING BTREE COMMENT '用户名唯一主键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统后台用户表';

CREATE TABLE `sys_user_brand` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户品牌表';

CREATE TABLE `sys_user_login` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'user_id',
  `last_login_time` datetime NOT NULL COMMENT '最后登录时间',
  `login_times` int(11) NOT NULL COMMENT '登录总次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户登录记录';

CREATE TABLE `sys_user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

CREATE TABLE `sys_user_sale` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `sale_user_id` int(10) unsigned NOT NULL COMMENT '业务员ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户业务范围表';

CREATE TABLE `training_center` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) unsigned NOT NULL COMMENT '父id',
  `sort` int(5) NOT NULL DEFAULT '0' COMMENT '排序值',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '0禁用 1启用',
  `title_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 中文',
  `title_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 英文',
  `content_url_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '内容 资源地址 中文',
  `content_url_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '内容 资源地址 英文',
  `thumbnail_url_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '缩略图 资源地址(未启用) 中文',
  `thumbnail_url_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '缩略图 资源地址(未启用) 英文',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `IDX_PARENT_ID` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='培训中心';