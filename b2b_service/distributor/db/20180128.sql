CREATE TABLE `alipay_platform` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台编码',
  `type` smallint(5) unsigned NOT NULL COMMENT '平台类型：1小程序',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付宝平台名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付宝平台appid',
  `app_private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '支付宝平台密钥',
  `app_public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '支付宝平台应用公钥',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `platform_app_id` (`app_id`,`platform`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商支付宝平台配置表';

CREATE TABLE `alipay_platform_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `alipay_platform_id` int(10) unsigned NOT NULL COMMENT '抖音平台id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名(登录名)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `dy_platform_id` (`alipay_platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商支付宝平台关联关系表';

CREATE TABLE `customer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '归属分销商id',
  `no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编号',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `nike_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `sex` smallint(6) DEFAULT NULL COMMENT '性别 1为男性，2为女性',
  `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册来源编码(分销商平台编码)',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `head_portrait` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `agreement_flag` smallint(6) DEFAULT '0' COMMENT '确认协议:1 已确认 0未确认',
  `status` smallint(6) NOT NULL DEFAULT '0' COMMENT '此用户是否已经被冻结 1为否,2为已冻结',
  `customer_type` smallint(6) NOT NULL DEFAULT '1' COMMENT '用户类型 1、有效用户(有手机号码)  2、游客（没有填写手机号码）',
  `parent_id` int(10) unsigned DEFAULT '0' COMMENT '父用户id（账户合并使用）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `no` (`no`),
  UNIQUE KEY `distributor_phone_id` (`distributor_id`,`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='C端客户信息表';

CREATE TABLE `customer_address` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货人姓名',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货人手机号',
  `customer_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'C端客户id',
  `province_id` int(10) unsigned DEFAULT NULL COMMENT '省份id',
  `city_id` int(10) unsigned DEFAULT NULL COMMENT '城市id',
  `district_id` int(10) unsigned DEFAULT NULL COMMENT '区id',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `zip_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮编',
  `default_flag` smallint(5) unsigned DEFAULT '1' COMMENT '是否默认收货地址 0.否 1.是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `province_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省份名称',
  `city_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市名称',
  `district_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `customer_id` (`customer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='C端客户地址信息表';

CREATE TABLE `customer_platform` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` int(10) unsigned NOT NULL COMMENT 'C端客户ID',
  `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台绑定编码(分销商平台编码)',
  `open_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方唯一标识码',
  `other_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方其他Id(如手机助手的uid)',
  `status` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '此用户是否已经被冻结 0为否,1为已冻结',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `customer_id` (`customer_id`) USING BTREE,
  KEY `platform` (`platform`) USING BTREE,
  KEY `open_id` (`open_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='C端客户第三方平台绑定表';

CREATE TABLE `distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名(登录名)',
  `password` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户登录密码',
  `company_type` smallint(5) unsigned DEFAULT NULL COMMENT '公司类型 1-公司 2-个体商户 3-个人',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  `apply_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '来源类型 1.后台添加 2.前台注册申请 3.分销邀请',
  `apply_status` smallint(5) unsigned DEFAULT '0' COMMENT '资质申请状态 0未提交 1申请中 2申请通过 3申请失败',
  `profile_status` smallint(5) unsigned DEFAULT '0' COMMENT '资料审核状态 0未提交 1资料审核中  2资料审核通过  3资料审核失败 ',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `freeze_status` smallint(5) unsigned DEFAULT '2' COMMENT '冻结状态 1,未冻结 2,冻结',
  `freeze_time` datetime DEFAULT NULL COMMENT '冻结时间',
  `tree_node` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '多级分销级数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `check_time` datetime DEFAULT NULL COMMENT '申请审核时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商基本信息表';

CREATE TABLE `distributor_address` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货人姓名',
  `distributor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '分销商id',
  `country_id` int(11) DEFAULT NULL COMMENT '国家编号',
  `province_id` int(10) unsigned DEFAULT NULL COMMENT '省份id',
  `city_id` int(10) unsigned DEFAULT NULL COMMENT '城市id',
  `district_id` int(10) unsigned DEFAULT NULL COMMENT '区id',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地址',
  `zip_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮编',
  `address_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '地址类型 1.公司地址 2.收货地址',
  `default_flag` smallint(5) unsigned DEFAULT '1' COMMENT '是否默认收货地址 0.否 1.是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收货人手机号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商地址信息表';

CREATE TABLE `distributor_brand_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `brand_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品牌id 中间用逗号隔开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='一级分销商可视品牌表';

CREATE TABLE `distributor_brand_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `brand_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品牌id 中间用逗号隔开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='多级分销商不可视品牌表';

CREATE TABLE `distributor_business` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `sales_id` int(10) unsigned DEFAULT '0' COMMENT '业务员id',
  `distributor_group_ids` text COLLATE utf8mb4_general_ci COMMENT '分销商分组ids',
  `distributor_category_id` int(10) unsigned DEFAULT NULL COMMENT '分销商分类id',
  `auto_delivery` smallint(5) unsigned DEFAULT '0' COMMENT '自动下推出库 1.是 0.否',
  `can_export_price` smallint(5) unsigned DEFAULT '1' COMMENT '是否能导出报价 1.能 0.不能',
  `promotion_scope` smallint(5) unsigned DEFAULT '1' COMMENT '参与活动 0-不参与活动 1-全部活动 2-指定活动类型',
  `promotion_types` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '可参与活动类型 1-营销活动 2-阶梯活动 3-拼团活动(多选中间用逗号隔开)',
  `rx_shop_switch` smallint(5) unsigned DEFAULT '0' COMMENT '是否启用柔性店铺开关，1启用 0 不启用',
  `logistics_sms_switch` smallint(6) DEFAULT '2' COMMENT '发货短信提醒客户 1是 0否 默认是0',
  `on_way_flag` smallint(6) DEFAULT '1' COMMENT '是否支持在途库存 1是 0否 默认是1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `coordinator_id` int(10) unsigned DEFAULT NULL COMMENT '商务员id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商业务信息表';

CREATE TABLE `distributor_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商类别名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商类别描述',
  `erp_category_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ERP分销商类别编号',
  `order_type_id` int(10) unsigned NOT NULL COMMENT '订单类型',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1启用0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商类别表';

CREATE TABLE `distributor_category_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `category_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品类id 中间用逗号隔开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='一级分销商可视品牌表';

CREATE TABLE `distributor_check` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `user_id` int(10) unsigned NOT NULL COMMENT '发起人id',
  `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起人名称',
  `check_user_id` int(10) unsigned NOT NULL COMMENT '当前审批人id',
  `check_user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前审批人名称',
  `check_type` smallint(6) NOT NULL COMMENT '审批类型: 1 新增 2 修改',
  `check_status` smallint(5) unsigned DEFAULT '0' COMMENT '审批状态 0, 审批中 1,审批通过，2审批未通过',
  `check_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '审批内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商审批表';

CREATE TABLE `distributor_check_flow` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_check_id` int(10) unsigned NOT NULL COMMENT '分销商审批id',
  `user_id` int(10) unsigned NOT NULL COMMENT '审批人id',
  `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审批人名称',
  `check_status` smallint(5) unsigned DEFAULT '0' COMMENT '审批状态 0, 未审批 1,审批通过，2审批未通过',
  `check_time` datetime DEFAULT NULL COMMENT '审批时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审批备注',
  `check_sort` int(10) unsigned NOT NULL COMMENT '审批顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_check_id` (`distributor_check_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商审批流表';

CREATE TABLE `distributor_contacts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '分销商id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人名称',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号登录密码',
  `sex` smallint(5) unsigned DEFAULT '0' COMMENT '性别 0,未设置, 1,男 2,女',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人邮箱',
  `owner_flag` smallint(5) unsigned DEFAULT NULL COMMENT '是否账号拥有: 0 否, 1 是',
  `role_id` int(10) unsigned DEFAULT NULL COMMENT '联系人角色id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `freeze_status` smallint(5) unsigned DEFAULT '1' COMMENT '冻结状态 1,未冻结 2,冻结',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序open_id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商联系人表';

CREATE TABLE `distributor_contacts_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1启用0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商联系人角色表';

CREATE TABLE `distributor_custom_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_id` int(10) NOT NULL COMMENT '货品ID',
  `distributor_id` int(10) NOT NULL COMMENT '分销商ID',
  `price` decimal(8,2) DEFAULT '0.00' COMMENT '定制价格',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人用户名称',
  `del_flag` smallint(4) DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  PRIMARY KEY (`id`),
  KEY `distributor_item_index` (`distributor_id`,`item_id`) USING BTREE COMMENT '分销商普通索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商柔性定制C端客户定价表';

CREATE TABLE `distributor_electricity_relation_mapping` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `seller_nick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卖家名称 电商平台',
  `erp_shop_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ERP店铺名称',
  `order_source_id` int(10) NOT NULL COMMENT '订单来源Id',
  `e_platfrom` int(10) DEFAULT NULL COMMENT '电商平台id（现有平台id混乱 另外维护）10000 淘系',
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '淘系网关地址',
  `session_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商电商名称id关系映射';

CREATE TABLE `distributor_extend_data` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '分销商id',
  `cert_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '营业执照或身份证号',
  `erp_flag` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '信息是否同步到erp 1.是 0.否',
  `erp_id` int(10) unsigned DEFAULT NULL COMMENT 'erp客户id',
  `erp_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp客户编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商备注',
  `language` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台显示语言',
  `currency_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台显示币种',
  `distribution_qr_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商分销二维码',
  `distribution_flag` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '是否开启分销模式 0 不开启, 1 开启',
  `distribution_pay_way` smallint(5) DEFAULT '0' COMMENT '分销商付款方式：0默认 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
  `distribution_mode` smallint(5) unsigned DEFAULT '1' COMMENT '分销结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)',
  `distribution_promotion_flag` smallint(5) unsigned DEFAULT '1' COMMENT '分销活动是否同步： 1 是(上级分销商活动同步下级分销商) 0 否(上级分销商活动不同步下级分销商)',
  `customer_flag` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '是否开启C端模式 0 不开启, 1 开启',
  `customer_mode` smallint(5) unsigned DEFAULT '1' COMMENT 'C端结算方式： 1 平台方收款(比如：bat收款，bat收款), 2 上级收款 3 自己收款(分销商自己收款)',
  `distribution_auto_flag` smallint(5) unsigned DEFAULT '1' COMMENT '分销订单是否自动审核： 1 是 2 否（注意：下级分销订单自动审核）',
  `erp_balance_flag` smallint(5) unsigned DEFAULT '0' COMMENT 'ERP余额是否同步 1.是 0.否',
  `sub_account_flag` smallint(4) DEFAULT NULL COMMENT '是否开启分账 1、是 0、否（开启C端模式并且C端结算模式属于自己收款才有值）',
  `open_id` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最新一个人登录的open_id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商扩展数据表';

CREATE TABLE `distributor_financial` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `trade_id` int(10) unsigned DEFAULT NULL COMMENT '结算方式id',
  `coin_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '货币类型 1-人民币 2-美元',
  `bank_account_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '银行账户名',
  `bank_deposit_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开户行全称',
  `bank_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '银行账户',
  `tax_type` smallint(5) unsigned DEFAULT NULL COMMENT '税种类型 1-一般纳税人 2-小规模纳税人 3-个人',
  `invoice_title_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发票抬头名称',
  `taxpayer_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '纳税人识别号',
  `registered_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发票注册地址',
  `registered_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发票注册电话',
  `registered_bank_deposit_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发票开户银行',
  `registered_bank_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发票银行账户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商财务信息表';

CREATE TABLE `distributor_goods_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `goods_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品ids 中间用逗号隔开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='一级分销商可视商品表';

CREATE TABLE `distributor_goods_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `goods_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品ids 中间用逗号隔开',
  PRIMARY KEY (`id`),
  UNIQUE KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='多级分销商不可视商品表';

CREATE TABLE `distributor_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商分组名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商分组描述',
  `erp_group_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ERP分销商分组编号',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1启用0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商分组表';

CREATE TABLE `distributor_group_seckill_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `group_seckill_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼团秒杀id 中间用逗号隔开(注意：每个拼团秒杀id前后都有一个逗号)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='一级分销商拼团秒杀活动可视表（非全部分销商可视情况）';

CREATE TABLE `distributor_next_scale_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '多级分销商id',
  `next_scale_price_id` int(10) unsigned NOT NULL COMMENT '多级分销价格等级id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `next_scale_price_id` (`next_scale_price_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='多级分销商价格等级关系表';

CREATE TABLE `distributor_one_scale_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `category_id` int(10) unsigned DEFAULT NULL COMMENT '品类id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  `distribution_scale_price_id` int(10) unsigned DEFAULT NULL COMMENT '多级分销默认价格等级id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `brand_id` (`brand_id`) USING BTREE,
  KEY `category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='一级分销商价格等级关系表';

CREATE TABLE `distributor_promotion_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `promotion_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '促销活动id 中间用逗号隔开(注意：每个促销活动id前后都有一个逗号)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='一级分销商促销活动可视表（非全部分销商可视情况）';

CREATE TABLE `distributor_sales_area` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `sales_area_id` int(10) unsigned NOT NULL COMMENT '销售区域id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `sales_area_id` (`sales_area_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商销售区域关联表';

CREATE TABLE `distributor_special_goods` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '分销商id',
  `goods_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '商品id',
  `goods_item_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '货品id',
  `distributor_price` decimal(16,6) NOT NULL COMMENT '商品特价',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_distributor_id_goods_item_id` (`distributor_id`,`goods_item_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`),
  KEY `goods_id` (`goods_id`),
  KEY `goods_item_id` (`goods_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商特价商品表';

CREATE TABLE `distributor_sub_account_admin_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `aging_type` smallint(4) NOT NULL COMMENT '分账时效类型 1、实时 2、延迟',
  `delay_time` decimal(6,2) DEFAULT NULL COMMENT '延迟分账时间（单位小时）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_index` (`distributor_id`) USING BTREE COMMENT '分销商唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商分账运营后台配置表';

CREATE TABLE `distributor_sub_account_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `level_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '等级名称',
  `sequence` int(6) NOT NULL COMMENT '排序号',
  `delete_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_distributor_id_name` (`distributor_id`,`level_name`) USING BTREE,
  KEY `distributor_index` (`distributor_id`,`sequence`) USING BTREE COMMENT '分销商和排序索引',
  KEY `delete_flag_index` (`delete_flag`) USING BTREE COMMENT '是否删除索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商分账等级表';

CREATE TABLE `distributor_sub_account_ratio` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `sub_account_config_id` int(10) NOT NULL COMMENT '分销商侧分账配置id',
  `level_id` int(10) NOT NULL COMMENT '分账等级id',
  `ratio` decimal(6,4) NOT NULL COMMENT '分账比例、存进去的值除以100',
  `delete_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sub_account_config_index` (`sub_account_config_id`) USING BTREE COMMENT '分销商端分账配置id索引',
  KEY `level_index` (`level_id`) USING BTREE COMMENT '等级id索引',
  KEY `delete_flag_index` (`delete_flag`) USING BTREE COMMENT '是否删除标记索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分账比例';

CREATE TABLE `distributor_sub_account_saleman` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `type` smallint(4) NOT NULL COMMENT '身份类型 1、企业、2 个人',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务员姓名',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务员手机号',
  `level_id` int(10) NOT NULL COMMENT '分账等级id',
  `parent_id` int(10) NOT NULL COMMENT '上级业务员id、默认是0、表示顶级',
  `open_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商配置的微信支付appid生成的openid',
  `merchant_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务员分账的商户号',
  `distributor_id` int(10) NOT NULL COMMENT '归属分销商id',
  `open_flag` smallint(4) NOT NULL COMMENT '状态 1、启用 0、禁用',
  `delete_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1、是 0、否、默认是0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人分销商id',
  `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人分销商名称',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_distributor_id_mobile` (`distributor_id`,`mobile`) USING BTREE,
  KEY `parent_index` (`parent_id`) USING BTREE COMMENT '父id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分账业务员';

CREATE TABLE `distributor_sub_account_user_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `amount_type` smallint(4) NOT NULL COMMENT '分账金额类型 1、按照实付金额 2、按照利润金额',
  `delete_flag` smallint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 1、是 0、否 默认是0',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` int(10) DEFAULT NULL COMMENT '修改人用户id',
  `update_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_index` (`distributor_id`) USING BTREE COMMENT '分销商id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商分账设置（分销商端）';

CREATE TABLE `distributor_trade` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式名称',
  `name_en` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式名称 英文',
  `pay_way` smallint(6) DEFAULT '1' COMMENT '结算方式，1为立即支付，2为期间结算',
  `settling_time` int(11) DEFAULT '0' COMMENT '结算时长',
  `erp_settle_account_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'erp收款条件编号',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1启用0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商收款条件表';

CREATE TABLE `distributor_tree_path` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_ancestor_id` int(10) unsigned NOT NULL COMMENT '父分销商id',
  `distributor_descendant_id` int(10) unsigned NOT NULL COMMENT '子分销商id',
  `tree_node` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '归属分销级数',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_ancestor_id` (`distributor_ancestor_id`) USING BTREE,
  KEY `distributor_descendant_id` (`distributor_descendant_id`) USING BTREE,
  KEY `tree_node` (`tree_node`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='多级分销关联关系表';

CREATE TABLE `dy_platform` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台编码',
  `type` smallint(5) unsigned NOT NULL COMMENT '平台类型：1小程序',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '抖音平台名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '抖音平台appid',
  `app_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '抖音平台密钥',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `platform_app_id` (`app_id`,`platform`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分销商抖音平台配置表';

CREATE TABLE `dy_platform_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dy_platform_id` int(10) unsigned NOT NULL COMMENT '抖音平台id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名(登录名)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `dy_platform_id` (`dy_platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分销商抖音平台关联关系表';

CREATE TABLE `next_scale_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '价格等级归属分销商id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格等级名称',
  `arithmetic_type` smallint(5) unsigned DEFAULT NULL COMMENT '算数运算符：1 乘2 加 3 除 4 减',
  `arithmetic_num` decimal(16,6) unsigned DEFAULT NULL COMMENT '参加运算的数值',
  `special_flag` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '是否有特殊公式, 1是,0否',
  `open_flag` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '状态, 1启用,0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='多级分销价格等级表';

CREATE TABLE `next_scale_price_special` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `next_scale_price_id` int(10) unsigned NOT NULL COMMENT '多级分销价格等级id',
  `arithmetic_type` smallint(5) unsigned DEFAULT NULL COMMENT '算数运算符：1 乘2 加 3 除 4 减',
  `arithmetic_num` decimal(16,6) unsigned DEFAULT NULL COMMENT '参加运算的数值',
  PRIMARY KEY (`id`),
  KEY `next_scale_price_id` (`next_scale_price_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='多级分销价格等级特殊公式表';

CREATE TABLE `next_scale_price_special_brand_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `next_scale_price_special_id` int(10) unsigned NOT NULL COMMENT '多级分销价格等级特殊公式id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `category_id` int(10) unsigned DEFAULT NULL COMMENT '品类id',
  PRIMARY KEY (`id`),
  KEY `next_scale_price_special_id` (`next_scale_price_special_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='多级分销价格等级特殊公式品牌品类表';

CREATE TABLE `platform` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `platform_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台绑定编码(分销商平台编码)',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台名称',
  `open_flag` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '状态, 1启用,0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `platform_no` (`platform_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商平台信息表';

CREATE TABLE `sales_area` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '销售区域名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '销售区域描述',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1启用0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='销售区域表';

CREATE TABLE `sys_platform` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台绑定编码(分销商平台编码)',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分配的appid',
  `app_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分配的appkey',
  `app_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分配的appSecret',
  `host_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '域名或者IP',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `platform` (`platform`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商系统平台配置表';

CREATE TABLE `sys_platform_api` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sys_platform_id` int(10) unsigned NOT NULL COMMENT '分销商系统配置id',
  `api_type` smallint(5) unsigned NOT NULL COMMENT '事务类型: 1为物流信息推送，2为订单信息推送，3 定制信息推送，4 获取定制价格  5、订单编号回传（bat手动触发失败日志、回传单号回第三方）6、取消订单进行回传 若后期有扩展，会进行追加',
  `uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口',
  `develop_source` smallint(5) unsigned NOT NULL COMMENT '',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式: GET,PUT,POST,DELETE',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_sys_id` (`sys_platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商系统对接配置表';

CREATE TABLE `sys_platform_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sys_platform_id` int(10) unsigned NOT NULL COMMENT '分销商系统配置id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名(登录名)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_sys_id` (`sys_platform_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商与系统平台关联关系表';

CREATE TABLE `wx_platform` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台编码',
  `type` smallint(5) unsigned NOT NULL COMMENT '平台类型：1 公众号 2 小程序',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信平台名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信平台appid',
  `app_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信平台密钥',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `platform_app_id` (`app_id`,`platform`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商微信平台配置表';

CREATE TABLE `wx_platform_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `wx_platform_id` int(10) unsigned NOT NULL COMMENT '微信公众号平台id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名(登录名)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `wx_platform_id` (`wx_platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商微信公众号平台关联关系表';
