CREATE TABLE `account_alipay` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `organization_id` int(10) unsigned NOT NULL COMMENT '销售组织id',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
  `erp_account_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp账户编码',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝appid',
  `bank_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp绑定名称',
  `app_public_secret` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝公钥',
  `app_private_secret` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝私钥',
  `back_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '取消订单是否原路返回 1、是 2、否',
  `open_flag` smallint(5) NOT NULL COMMENT '状态, 1启用,0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_organization_id` (`organization_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='平台支付宝收款账户配置表';

CREATE TABLE `account_alipay_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商名称',
  `distributor_company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商公司名称',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝appid',
  `app_public_secret` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝公钥',
  `app_private_secret` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝私钥',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `idx_appid` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销客户支付宝收款账户配置表';

CREATE TABLE `account_kuaiqian` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `organization_id` int(10) unsigned DEFAULT NULL COMMENT '销售组织id',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收款账户名称',
  `erp_account_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp账户编码',
  `bank_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp绑定名称',
  `merchan_acct_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '快钱人民币网关账号',
  `sign_file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '签名文件路径',
  `sign_pwd` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '签名密码',
  `sign_private_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '私钥',
  `check_sign_file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '快钱支付通知回调验证签名文件路径',
  `back_type` smallint(6) DEFAULT NULL COMMENT '取消订单是否原路返回 1、是 2、否',
  `open_flag` smallint(5) DEFAULT NULL COMMENT '状态, 1启用,0停用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='平台快钱收款账户配置表';

CREATE TABLE `account_offline` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) NOT NULL COMMENT '销售组织id',
  `currency_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '币别编码',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
  `erp_account_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp账户编码',
  `bank_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp绑定名称',
  `card_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡号',
  `bank_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款银行',
  `bank_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收款银行地址',
  `open_flag` smallint(5) NOT NULL COMMENT '状态, 1启用,0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='平台线下收款账户配置表';

CREATE TABLE `account_toutiao` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `organization_id` int(10) unsigned NOT NULL COMMENT '销售组织id',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户号',
  `bank_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp绑定名称',
  `salt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盐',
  `back_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '取消订单是否原路返回 1、是 2、否',
  `open_flag` smallint(5) NOT NULL COMMENT '状态, 1启用,0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_organization_id` (`organization_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='平台支付宝收款账户配置表';

CREATE TABLE `account_wx` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(10) unsigned NOT NULL COMMENT '销售组织id',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
  `erp_account_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp账户编码',
  `bank_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp绑定名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信appid（公众号小程序不可信/以前端传值为准）',
  `apiclient_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '商户API证书私钥',
  `serial_number` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户证书序列号',
  `certificate_invalid_time` datetime DEFAULT NULL COMMENT '商户证书失效时间',
  `account_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信商户号(mchid)',
  `app_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信appKey V3秘钥与 api秘钥',
  `app_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '应用类型 1、微信公众号 2、微信小程序',
  `back_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '取消订单是否原路返回 1、是 2、否',
  `open_flag` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '状态, 1启用,0停用',
  `version` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'api版本 V2版本 V3版本',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_organization_id` (`organization_id`,`app_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='平台微信收款账户配置表';

CREATE TABLE `account_wx_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `account_type` smallint(4) NOT NULL DEFAULT '1' COMMENT '账户类型 1、自有账户 2、服务商二级商户',
  `sub_account_ratio` decimal(6,4) DEFAULT NULL COMMENT '最大分账比例、账户类型选择服务商二级商户才有值',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `distributor_company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商公司名称',
  `account_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户号对应的appid（公众号小程序不可信/以前端传值为准）',
  `apiclient_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '商户API证书私钥',
  `serial_number` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户证书序列号',
  `certificate_invalid_time` datetime DEFAULT NULL COMMENT '商户证书失效时间',
  `app_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信appKey V3秘钥与 api秘钥',
  `app_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '应用类型 1、微信公众号 2、微信小程序',
  `account_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信商户号',
  `version` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'api版本 V2版本 V3版本',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `sub_mchid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务商的子商户号（服务商的钱收到这个商户里面）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_id` (`distributor_id`,`app_type`) USING BTREE,
  KEY `idx_appid` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销客户微信收款账户配置表';

CREATE TABLE `currency` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '币种名称',
  `currency_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '币种编码',
  `money_accuracy` smallint(5) unsigned DEFAULT NULL COMMENT '货币精度',
  `erp_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp编码',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1启用,0停用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `currency_code` (`currency_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='币别表';
CREATE TABLE `currency_rate` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `exchange_rate` decimal(16,4) NOT NULL COMMENT '直接汇率',
  `reverse_ex_rate` decimal(16,4) NOT NULL COMMENT '间接汇率',
  `cy_forid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原币',
  `cy_toid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目标币',
  `beg_date` datetime NOT NULL COMMENT '开始生效时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='币种汇率表';

CREATE TABLE `deposit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `is_show_prestore` tinyint(1) unsigned DEFAULT '1' COMMENT '显示预存款 0为否 1为是',
  `is_open_online_topup` tinyint(1) unsigned DEFAULT '1' COMMENT '开启线上充值 0为否 1为是',
  `online_min_amount` float DEFAULT '0' COMMENT '充值最小额度',
  `is_open_offline_topup` tinyint(1) unsigned DEFAULT '1' COMMENT '开启线下充值 0为否 1为是',
  `is_open_withdrawal` tinyint(1) unsigned DEFAULT '1' COMMENT '开启提现 0为否 1为是',
  `create_time` bigint(20) unsigned DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) unsigned DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='财务预存款表';

CREATE TABLE `deposit_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `erp_distributor_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商内码 erp分销商id',
  `tree_node` int(10) unsigned NOT NULL COMMENT '多级分销级数',
  `distributor_ancestor_id` int(10) unsigned DEFAULT NULL COMMENT '父分销商id',
  `distributor_ancestor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '父分销商名称',
  `account_balance` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '账户余额',
  `account_available` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '账户可用余额',
  `freezing_amount` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '账户冻结金额',
  `recharge_amount` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '账户充值总金额',
  `commission_amount` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '分销所得佣金',
  `withdraw_amount` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '账户提现总金额',
  `consumer_amount` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '账户消费总金额',
  `refund_amount` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '订单取消增加总金额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销客户预存款账户表';

CREATE TABLE `deposit_distributor_freezing` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `deposit_distributor_id` int(10) unsigned NOT NULL COMMENT '分销客户预存款账户id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分销商名称',
  `freezing_amount` decimal(16,3) unsigned DEFAULT '0.000' COMMENT '冻结金额',
  `status` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '冻结状态 0全部, 1,冻结 2,解冻',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1,提现冻结 2,其他冻结',
  `business_id` int(11) unsigned DEFAULT NULL COMMENT '业务单号 (业务类型为 2时，为空 ) ',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `deposit_distributor_id` (`deposit_distributor_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商账户预存款冻结明细表';

CREATE TABLE `deposit_distributor_subsidiary_book` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `deposit_distributor_id` int(10) unsigned NOT NULL COMMENT '分销客户预存款账户id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分销商用户名',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1充值 2 提现 3 订单消费 4 订单取消增加 5调整 6 ERP增量变化 7 ERP全量变化 8 分销佣金 9 充值退款',
  `business_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务单号 (业务类型为 6或7时，为空 ) ',
  `pay_way` smallint(6) DEFAULT NULL COMMENT '交易方式付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
  `change_type` smallint(5) unsigned DEFAULT NULL COMMENT '变更类型 1.增加，2.减少 ',
  `amount` decimal(16,3) DEFAULT NULL COMMENT '变动金额数',
  `before_deposit_amount` decimal(16,3) DEFAULT NULL COMMENT '变化前账户余额',
  `after_deposit_amount` decimal(16,3) DEFAULT NULL COMMENT '变化后账户余额',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `deposit_distributor_id` (`deposit_distributor_id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销客户预存款账户明细表';

CREATE TABLE `order_sub_account` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `distributor_id` int(10) NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `order_id` int(10) NOT NULL COMMENT 'B2B订单id',
  `order_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号（B2B）',
  `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信支付凭证（返回的微信流水号）',
  `last_transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后微信分账成功返回的流水号（要拿这个流水号作为out_order_no）',
  `shop_id` int(10) DEFAULT NULL COMMENT '门店id',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '门店名称',
  `pay_amount` decimal(8,2) NOT NULL COMMENT '实际支付金额',
  `max_sub_account_amount` decimal(8,2) DEFAULT NULL COMMENT '最大分账金额',
  `profit_account` decimal(8,2) DEFAULT NULL COMMENT '利润金额（按照利润分账有值）',
  `actual_sub_account_amount` decimal(8,2) DEFAULT NULL COMMENT '实际分账金额（不能超过最大分账金额）',
  `status` smallint(4) NOT NULL COMMENT '分账状态 0、已关闭 1、待分账 2、部分分账 3、全部分账 ',
  `sub_mchid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务商收款的子商户号（相当于从这个服务商商户号分账）',
  `sub_account_fail_flag` smallint(4) DEFAULT NULL COMMENT '是否存在分账失败 1、是 0、否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `plan_time` datetime DEFAULT NULL COMMENT '计划分账时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `order_id_index` (`order_id`) USING BTREE COMMENT '一个订单只有一个分账'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='订单分账表';

CREATE TABLE `order_sub_account_bill` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_sub_account_id` int(10) NOT NULL COMMENT '订单分账表id',
  `level_id` int(10) DEFAULT NULL COMMENT '分账等级id',
  `level_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '等级名称',
  `max_sub_account_amount` decimal(8,2) DEFAULT NULL COMMENT '最大分账金额（下单时默认的分账金额）',
  `actual_sub_account_amount` decimal(8,2) DEFAULT NULL COMMENT '实际分账金额（不能超过最大分账金额）',
  `ratio` decimal(6,4) NOT NULL COMMENT '分账比例（实际分账比例）',
  `status` smallint(4) NOT NULL COMMENT '分账状态 0、已关闭 1、待分账 2、部分分账 3、全部分账 ',
  `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分账的openid',
  `merchant_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分账接收方商户号',
  `out_trade_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分账单号',
  `saleman_id` int(10) DEFAULT NULL COMMENT '分销商业务员ID',
  `saleman_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商业务员名称',
  `success_flag` smallint(4) DEFAULT NULL COMMENT '是否分账成功 1、是 0、否',
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '失败原因',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `success_time` datetime DEFAULT NULL COMMENT '分账成功时间',
  `sub_transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信分账成功返回的流水号（要拿这个流水号作为out_order_no）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE `pay_bills_customer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付凭证号(注意：全平台唯一)',
  `customer_id` int(10) unsigned NOT NULL COMMENT '支付分销客户id',
  `customer_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'C端用户名',
  `pay_type` smallint(5) unsigned NOT NULL COMMENT '支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1订单收款2在线充值收款',
  `business_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
  `pay_status` smallint(5) unsigned DEFAULT '0' COMMENT '订单状态: 0未支付， 1已支付，2已取消',
  `total_fee` decimal(10,3) unsigned NOT NULL COMMENT '支付金额',
  `order_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '订单标题',
  `order_describe` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '订单描述',
  `product_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品id',
  `online_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付平台凭证号',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `pay_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展：为了定位收款账户。支付方法（子支付渠道）例pay_type = 2时。有WXPAY_JSAPI(21),WXPAY_APP(22),WXPAY_H5(23),WXPAY_NATIVE(24),WXPAY_MINI_PROGRAM(25)',
  `trade_mode` smallint(6) DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)',
  `payee_id` int(11) DEFAULT NULL COMMENT '扩展：为了定位收款账户。2 自己收款(分销商自己收款) 收款人id 分销商id',
  `organization_id` int(11) DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款) 平台收款时 组织id',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展：定位的收款账户appid 可能错误。目前支付以前端传的appid为主，此appid 为前端传的appid',
  `receipt_amount` decimal(10,3) DEFAULT NULL COMMENT '实收金额',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `mchid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收款的商户号（服务商存的是收款的子商户号、服务商退款需要传这个）',
  `sp_mchid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务商归属商户号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `out_trade_no` (`out_trade_no`) USING BTREE,
  KEY `customer_id` (`customer_id`) USING BTREE,
  KEY `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='订单C端客户支付凭证表';

CREATE TABLE `pay_bills_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付凭证号(注意：全平台唯一)',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '支付分销客户id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分销商用户名',
  `pay_type` smallint(5) unsigned NOT NULL COMMENT '支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1订单收款2在线充值收款',
  `business_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
  `pay_status` smallint(5) unsigned DEFAULT '0' COMMENT '订单状态: 0未支付， 1已支付，2已取消',
  `total_fee` decimal(10,3) unsigned NOT NULL COMMENT '支付金额',
  `order_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '订单标题',
  `order_describe` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '订单描述',
  `product_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品id',
  `online_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付平台凭证号',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `pay_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展：为了定位收款账户。支付方法（子支付渠道）例pay_type = 2时。有WXPAY_JSAPI(21),WXPAY_APP(22),WXPAY_H5(23),WXPAY_NATIVE(24),WXPAY_MINI_PROGRAM(25)',
  `trade_mode` smallint(6) DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)',
  `payee_id` int(11) DEFAULT NULL COMMENT '扩展：为了定位收款账户。2 自己收款(分销商自己收款) 收款人id 分销商id',
  `organization_id` int(11) DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款) 平台收款时 组织id',
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展：定位的收款账户appid 可能错误。目前支付以前端传的appid为主，此appid 为前端传的appid',
  `receipt_amount` decimal(10,3) DEFAULT NULL COMMENT '实收金额',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `out_trade_no` (`out_trade_no`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='订单分销客户支付凭证表';

CREATE TABLE `pay_trade_qr_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '扩展：为了定位收款账户。支付方法（子支付渠道）例pay_type = 2时。有WXPAY_JSAPI(21),WXPAY_APP(22),WXPAY_H5(23),WXPAY_NATIVE(24),WXPAY_MINI_PROGRAM(25)',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '	商户的订单号',
  `qr_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '当前预下单请求生成的二维码码串，可以用二维码生成工具根据该码串值生成对应的二维码',
  `status` smallint(6) NOT NULL COMMENT '状态 0 未支付 1 已经支付 2关闭取消（可能不会有） ',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商户订单号与二维码关联表';

CREATE TABLE `refund_bills_customer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `customer_id` int(10) unsigned NOT NULL COMMENT '退款分销客户id',
  `customer_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分销商用户名',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付凭证号(注意：全平台唯一，根据支付凭证号进行退款)',
  `out_refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款凭证号(注意：全平台唯一)',
  `refund_type` smallint(5) unsigned NOT NULL COMMENT '退款类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1订单退款2充值退款',
  `business_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
  `refund_status` smallint(5) unsigned DEFAULT '1' COMMENT '退款状态: 1退款处理中， 2退款成功，3退款失败',
  `total_fee` decimal(10,3) unsigned NOT NULL COMMENT '退款金额(不能大于支付凭证金额)',
  `online_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台凭证号',
  `refund_time` datetime DEFAULT NULL COMMENT '退款成功时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `out_refund_no` (`out_refund_no`) USING BTREE,
  KEY `customer_id` (`customer_id`) USING BTREE,
  KEY `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='订单C端客户退款凭证表';

CREATE TABLE `refund_bills_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '退款分销客户id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分销商用户名',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付凭证号(注意：全平台唯一，根据支付凭证号进行退款)',
  `out_refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款凭证号(注意：全平台唯一)',
  `refund_type` smallint(5) unsigned NOT NULL COMMENT '退款类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1订单退款2充值退款',
  `business_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
  `refund_status` smallint(5) unsigned DEFAULT '1' COMMENT '退款状态: 1退款处理中， 2退款成功，3退款失败',
  `total_fee` decimal(10,3) unsigned NOT NULL COMMENT '退款金额(不能大于支付凭证金额)',
  `online_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台凭证号',
  `refund_time` datetime DEFAULT NULL COMMENT '退款成功时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `out_refund_no` (`out_refund_no`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='订单分销客户退款凭证表';

CREATE TABLE `refund_customer_apply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_id` int(10) unsigned NOT NULL COMMENT '退款接收方C端客户id',
  `customer_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'C端客户用户名',
  `distributor_refund_id` int(10) unsigned DEFAULT NULL COMMENT '支付接收方分销商id（退款账号为2时有值）',
  `business_types` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '业务类型 1订单取消 2订单变更',
  `business_id` int(10) unsigned NOT NULL COMMENT '业务单号',
  `amount` decimal(16,3) DEFAULT '0.000' COMMENT '退款总金额',
  `cash_amount` decimal(16,3) DEFAULT NULL COMMENT '现金支付退款金额',
  `deposit_amount` decimal(16,3) DEFAULT NULL COMMENT '余额支付退款金额',
  `refund_type` smallint(5) unsigned DEFAULT NULL COMMENT '退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式',
  `refund_mode` smallint(6) unsigned DEFAULT NULL COMMENT '退款账号（谁的账号出）1 平台方退款(比如：bat收款，bat收款), 2 分销商',
  `apply_status` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '处理状态 0 未处理 1 已处理 2 已取消',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `business_id` (`business_id`) USING BTREE,
  KEY `customer_id` (`customer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='C端客户退款申请表';

CREATE TABLE `refund_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分销商用户名',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名',
  `amount` decimal(16,3) NOT NULL COMMENT '退款金额',
  `refund_way` smallint(5) unsigned NOT NULL COMMENT '退款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
  `out_refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销商退款凭证id( 线上退款情况才有值)',
  `customer_flag` smallint(6) NOT NULL COMMENT 'C端客户标志 0 不是C端客户(默认B2B) 1是C端客户',
  `currency_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '币种',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1订单退款2充值退款',
  `business_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
  `refund_status` smallint(5) unsigned NOT NULL COMMENT '退款单状态,0未生效（微信退款回调中间状态）,1待确认,2已确认,3已取消',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `refund_erp_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp退款单编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_out_refund_no` (`out_refund_no`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `business_id` (`business_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商退款单表';

CREATE TABLE `refund_distributor_apply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分销商用户名',
  `distributor_refund_id` int(10) unsigned DEFAULT NULL COMMENT '支付接收方分销商id（退款账号为2时有值）',
  `business_types` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '业务类型 1订单取消 2订单变更',
  `business_id` int(10) unsigned NOT NULL COMMENT '业务单号',
  `amount` decimal(16,3) DEFAULT '0.000' COMMENT '退款总金额',
  `cash_amount` decimal(16,3) DEFAULT NULL COMMENT '现金支付退款金额',
  `deposit_amount` decimal(16,3) DEFAULT NULL COMMENT '余额支付退款金额',
  `refund_type` smallint(5) unsigned DEFAULT NULL COMMENT '退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式',
  `refund_mode` smallint(6) unsigned DEFAULT NULL COMMENT '退款账号（谁的账号出）1 平台方退款(比如：bat收款，bat收款), 2 分销商',
  `apply_status` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '处理状态 0 未处理 1 已处理 2 已取消',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `business_id` (`business_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商退款申请表';

CREATE TABLE `voucher_distributor` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分销商用户名',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名',
  `amount` decimal(16,3) NOT NULL COMMENT '收款金额',
  `pay_way` smallint(5) unsigned NOT NULL COMMENT '支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分销客户支付凭证号(现款线上支付情况才有值)',
  `customer_flag` smallint(6) NOT NULL COMMENT 'C端客户标志 0 不是C端客户(默认B2B) 1是C端客户',
  `currency_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '币种',
  `business_type` smallint(5) unsigned NOT NULL COMMENT '业务类型 1订单收款2在线充值收款',
  `business_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)(多个id,分割)',
  `voucher_status` smallint(5) unsigned NOT NULL COMMENT '收款单状态,1待确认,2已确认,3已取消',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `voucher_erp_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'erp收款单编号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_out_trade_no` (`out_trade_no`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `business_id` (`business_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销商收款单表';

CREATE TABLE `withdraw_deposits_distributor_apply` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `withdraw_amount` decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '提现金额',
  `apply_status` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '确认状态 1,申请中,待确认 2,已确认 3,已拒绝',
  `withdraw_account_type` smallint(5) unsigned DEFAULT '0' COMMENT '提现账户类型： 1.支付宝,2.微信,3.银行',
  `wx_alipay_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT ' 支付宝或微信账户',
  `card_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行卡号',
  `payee` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行卡号',
  `bank_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '收款银行',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '提现备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='分销客户提现申请表';

