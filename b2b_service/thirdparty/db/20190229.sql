CREATE TABLE `country_comparison` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `country_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分销商系统国家id',
  `erp_country_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'erp国家code',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `country_id` (`country_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='国家对照表';

CREATE TABLE `msg_center` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel` smallint(4) unsigned NOT NULL COMMENT '消息渠道 1.B2b 2.兑换商城 3.定制商城',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '消息内容',
  `mini_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序跳转链接',
  `pc_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'PC端跳转链接',
  `push_switch` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '推送开关 0关 1开；如开启推送，则会通知小程序或公众号进行消息推送，B2B暂不支持（没有合适场景）',
  `distributor_scope` smallint(4) unsigned DEFAULT NULL COMMENT '分销商推送范围1全部 2分销商等级 3.指定销售部门 4.指定业务员 5.指定分销商分组 6.指定分销商',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息中心配置表';

CREATE TABLE `msg_center_admin_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `center_id` int(10) unsigned NOT NULL COMMENT '消息中心id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `admin_id` (`admin_id`) USING BTREE,
  KEY `center_id` (`center_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息中心与业务员可视关系表';

CREATE TABLE `msg_center_department_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `center_id` int(10) unsigned NOT NULL COMMENT '消息中心id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `department_id` (`department_id`) USING BTREE,
  KEY `center_id` (`center_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息中心与销售部门可视关系表';

CREATE TABLE `msg_center_distributor_group_relevance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `center_id` int(11) DEFAULT NULL,
  `distributor_group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息中心与分销商分组可视关系表';

CREATE TABLE `msg_center_distributor_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `center_id` int(10) unsigned NOT NULL COMMENT '消息中心id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `center_id` (`center_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息中心与分销商可视关系表';

CREATE TABLE `msg_center_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `center_id` int(11) unsigned NOT NULL COMMENT '消息中心id',
  `channel` smallint(4) unsigned NOT NULL COMMENT '消息渠道 1.B2b 2.兑换商城 3.定制商城',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '消息内容',
  `mini_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序跳转链接',
  `pc_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'PC端跳转链接',
  `push_switch` smallint(4) unsigned NOT NULL COMMENT '推送开关 0关 1开；如开启推送，则会通知小程序或公众号进行消息推送，B2B暂不支持（没有合适场景）',
  `push_flag` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '结果是否已经推送 0否 1是',
  `read_flag` smallint(4) unsigned NOT NULL DEFAULT '0' COMMENT '是否已经阅读 0否 1是',
  `push_terminal` smallint(4) unsigned DEFAULT NULL COMMENT '推送终端 1短信 2微信B2B小程序 3微信定制小程序4.抖音定制小程序',
  `mini_body` json DEFAULT NULL COMMENT '小程序推送消息体',
  `msg_type` smallint(4) unsigned NOT NULL COMMENT '消息类型 1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知 6自定义消息',
  `to_user_id` int(11) NOT NULL COMMENT '接收的用户id',
  `to_username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收的用户名称',
  `user_type` smallint(4) unsigned NOT NULL DEFAULT '1' COMMENT '用户类型 1B2B用户 2.C端用户',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收用户openid',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机',
  `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '固定电话',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `send_fail_error` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '推送失败原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息日志';

CREATE TABLE `msg_center_scale_price_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `center_id` int(10) unsigned NOT NULL COMMENT '消息中心id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `scale_price_id` (`scale_price_id`) USING BTREE,
  KEY `center_id` (`center_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息中心与分销商等级可视关系表';

CREATE TABLE `msg_center_wechat_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `channel` smallint(4) unsigned NOT NULL COMMENT '消息渠道 1.B2b 2.兑换商城 3.定制商城',
  `type` smallint(4) unsigned NOT NULL COMMENT '通知类型:1.订单状态通知 2.发货通知 3.订单未支付提醒 4.分销商审核通知 5新订单通知',
  `template_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息中心微信模板配置';

CREATE TABLE `order_business_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `log_type` smallint(4) NOT NULL COMMENT '日志类型 1、推送定制信息给第三方 2、接收第三方订单（基于ID）3、接收第三方订单（基于编码） 4、推送销售单给ERP 5、B2B推单给工厂 6、工厂发货 7、推送物流信息给第三方 8、第三方取消订单 9、工厂取消订单 10、推送核销信息给第三方 11.同步erp上的物流单号 12、接收摩乐吉的订单 13.拉取淘宝订单',
  `toward_type` smallint(255) NOT NULL COMMENT '请求朝向 1、B2B->第三方 2、第三方->B2B 3、B2B->ERP 4、ERP->B2B 5、工厂-->B2B 6、B2B->>工厂 7、天猫--> B2B',
  `distributor_id` int(10) DEFAULT NULL COMMENT '分销商id',
  `platform` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台编码',
  `other_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方订单编号',
  `business_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '业务数据 例如第三方推送订单记录第三方用户编码（或者id）',
  `header_param_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求头参数json汇总',
  `request_param_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求体参数json汇总',
  `status` smallint(4) NOT NULL COMMENT '调用状态 1、成功 0、失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `response_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应',
  `order_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'B2B订单号',
  `error_msg` text COLLATE utf8mb4_general_ci COMMENT '错误信息（有可能请求是成功、但没有返回数据的情况）',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间（可能会失败后台手动重试）',
  `delete_flag` smallint(4) DEFAULT NULL COMMENT '删除标记1、已删除 0、未删除 （定时器会定时清理）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `log_type_index` (`log_type`) USING BTREE,
  KEY `status_index` (`status`) USING BTREE,
  KEY `platform` (`platform`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='第三方业务日志记录';

CREATE TABLE `order_cancel_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `distributor_id` int(10) DEFAULT NULL COMMENT '分销商id',
  `order_id` int(10) NOT NULL COMMENT 'B2B订单ID',
  `source` smallint(4) NOT NULL COMMENT '操作来源 1、第三方分销商 2、工厂 3、ERP',
  `request_param_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求体参数json汇总',
  `status` smallint(4) NOT NULL COMMENT '调用状态 1、成功 0、失败',
  `extend_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '扩展参数 例如存储工厂转过来的签名和时间戳之类的',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `response_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `order_index` (`order_id`) USING BTREE COMMENT '订单id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='第三方系统取消订单日志';

CREATE TABLE `order_detail_push_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `distributor_id` int(10) DEFAULT NULL COMMENT '分销商id',
  `platform` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台编码',
  `skip_link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '提交成功跳转地址（前端跳转）',
  `header_param_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求头参数json汇总',
  `request_param_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求体参数json汇总',
  `status` smallint(4) NOT NULL COMMENT '调用状态 1、成功 0、失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `response_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应',
  `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '错误信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='定制信息推送第三方日志记录';

CREATE TABLE `order_logistics_push_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` int(10) NOT NULL COMMENT 'order表主键id',
  `express_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物流单号',
  `status` smallint(4) NOT NULL COMMENT '推送状态 1、成功 0、失败',
  `type` smallint(4) DEFAULT NULL COMMENT '错误类型 1、查询无响应 2、查询接口报错 3、同步失败 4、订单不给修改（系统订单状态不给修改物流单号）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `response_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应',
  `param_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='物流单号推送记录表';

CREATE TABLE `qianniu_business` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_nick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `open_id` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `app_id` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE `qianniu_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `business_id` int(11) DEFAULT NULL,
  `num_iid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `detail_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `material_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE `qianniu_sku` (
  `id` int(11) NOT NULL,
  `item_id` int(11) DEFAULT NULL,
  `sku_id` int(11) DEFAULT NULL,
  `properties_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `material_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

CREATE TABLE `taobao_order` (
  `oid` bigint(20) NOT NULL COMMENT '子订单编号',
  `adjust_fee` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	手工调整金额.格式为:1.01;单位:元;精确到小数点后两位.',
  `buyer_rate` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	买家是否已评价。可选值：true(已评价)，false(未评价)',
  `customization` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '定制信息',
  `cid` int(11) DEFAULT NULL COMMENT '	交易商品对应的类目ID',
  `discount_fee` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	子订单级订单优惠金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `divide_order_fee` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	分摊之后的实付金额',
  `end_time` datetime DEFAULT NULL COMMENT '交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间 。格式:yyyy-MM-dd HH:mm:ss',
  `is_daixiao` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true；反之，该值为false。',
  `is_oversold` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	是否超卖',
  `is_sh_ship` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	是否屏蔽发货',
  `num` int(11) DEFAULT NULL COMMENT '	购买数量。取值范围:大于零的整数',
  `num_iid` bigint(20) DEFAULT NULL COMMENT '商品数字ID',
  `order_from` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	子订单来源,如jhs(聚划算)、taobao(淘宝)、wap(无线)',
  `outer_sku_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	外部网店自己定义的Sku编号',
  `part_mjz_discount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	优惠分摊',
  `payment` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。建议使用退款前的实付金额减去退款单中的实际退款金额计算。',
  `pic_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品图片的绝对路径',
  `price` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `propoint` int(11) DEFAULT NULL COMMENT '	使用淘金币的数量，以分为单位，和订单标propoint中间那一段一样',
  `refund_id` bigint(20) DEFAULT NULL COMMENT '	最近退款ID',
  `refund_status` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '退款状态。退款状态。可选值 WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)',
  `seller_rate` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	卖家是否已评价。可选值：true(已评价)，false(未评价)',
  `seller_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	卖家类型，可选值为：B（商城商家），C（普通卖家）',
  `shipper` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '仓储信息',
  `sku_id` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	商品的最小库存单位Sku的id.可以通过taobao.item.sku.get获取详细的Sku信息',
  `sku_properties_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'SKU的值。如：机身颜色:黑色;手机套餐:官方标配',
  `snapshot_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单快照URL',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。可选值:\r\nTRADE_NO_CREATE_PAY(没有创建支付宝交易)\r\nWAIT_BUYER_PAY(等待买家付款)\r\nWAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)\r\nWAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)\r\nTRADE_BUYER_SIGNED(买家已签收,货到付款专用)\r\nTRADE_FINISHED(交易成功)\r\nTRADE_CLOSED(付款以后用户退款成功，交易自动关闭)\r\nTRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)\r\nPAY_PENDING(国际信用卡支付付款确认中)',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	商品标题',
  `total_fee` decimal(20,2) DEFAULT NULL COMMENT '应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  PRIMARY KEY (`oid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='淘宝订单表';

CREATE TABLE `taobao_trade` (
  `tid` bigint(20) NOT NULL COMMENT '交易编号 (父订单的交易编号) 目前19位',
  `payment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '	实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '交易状态。可选值: * TRADE_NO_CREATE_PAY(没有创建支付宝交易) * WAIT_BUYER_PAY(等待买家付款) * SELLER_CONSIGNED_PART(卖家部分发货) * WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款) * WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) * TRADE_BUYER_SIGNED(买家已签收,货到付款专用) * TRADE_FINISHED(交易成功) * TRADE_CLOSED(付款以后用户退款成功，交易自动关闭) * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易) * PAY_PENDING(国际信用卡支付付款确认中) * WAIT_PRE_AUTH_CONFIRM(0元购合约中) * PAID_FORBID_CONSIGN(拼团中订单或者发货强管控的订单，已付款但禁止发货)',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '	交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade, auto_delivery, ec, cod的4种交易类型的数据 可选值 fixed(一口价) auction(拍卖) guarantee_trade(一口价、拍卖) auto_delivery(自动发货) independent_simple_trade(旺店入门版交易) independent_shop_trade(旺店标准版交易) ec(直冲) cod(货到付款) fenxiao(分销) game_equipment(游戏装备) shopex_trade(ShopEX交易) netcn_trade(万网交易) external_trade(统一外部交易)o2o_offlinetrade（O2O交易）step (万人团)nopaid(无付款订单)pre_auth_type(预授权0元购机交易)',
  `buyer_nick` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '买家昵称',
  `created` datetime DEFAULT NULL COMMENT '交易创建时间。格式:yyyy-MM-dd HH:mm:ss',
  `pay_time` datetime DEFAULT NULL COMMENT '付款时间。格式:yyyy-MM-dd HH:mm:ss。订单的付款时间即为物流订单的创建时间。',
  `new_presell` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预售单为true，否则false (云店订单专用)',
  `you_xiang` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '	优享购为true，否则false(云店订单专用)',
  `buyer_open_uid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '买家OpenUid',
  `post_fee` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮费',
  `seller_nick` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '卖家昵称',
  `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的姓名',
  `receiver_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的电话号码',
  `receiver_mobile` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的手机号码',
  `receiver_zip` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的邮编',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的详细地址',
  `receiver_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的所在省份',
  `receiver_town` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人街道地址',
  `receiver_city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的所在城市<br>注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面<br>建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
  `receiver_district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '*收货人的所在地区<br>注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面；而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面<br>建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市',
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='淘宝交易表';

CREATE TABLE `taobao_trade_order_relevance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` bigint(20) NOT NULL COMMENT '父订单id',
  `oid` bigint(20) NOT NULL COMMENT '子订单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='淘宝父子订单关联表';

CREATE TABLE `third_quanyi` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `distributor_id` int(11) unsigned NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `third_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方手机号',
  `third_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方验证码（核心，在苏宁表示订单号）',
  `third_sku_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方最终sku',
  `third_order_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '第三方明细json串',
  `last_modify_request` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '第三方最后一次请求修改明细',
  `third_user_remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方用户备注',
  `exchange_id` int(11) unsigned DEFAULT NULL COMMENT '兑换活动id',
  `exchange_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '兑换活动名称',
  `exchange_code_id` int(11) unsigned DEFAULT NULL COMMENT '兑换码id',
  `plain_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '明码',
  `secret_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '暗码',
  `material_id` int(11) unsigned DEFAULT NULL COMMENT '材质id',
  `material_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '材质名称',
  `customer_id` int(11) unsigned DEFAULT NULL COMMENT 'c端客户id',
  `customer_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'c端客户编号',
  `order_id` int(11) unsigned DEFAULT NULL COMMENT 'b2b系统订单id',
  `dispatch_flag` smallint(4) unsigned DEFAULT '0' COMMENT '是否已派工 0否 1是',
  `sign_in_flag` smallint(4) unsigned DEFAULT '0' COMMENT '是否已签到 0否 1是',
  `destroy_flag` smallint(4) unsigned DEFAULT '0' COMMENT '是否已核销 0否 1是',
  `cancel_flag` smallint(4) unsigned DEFAULT '0' COMMENT '是否已取消 0否 1是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `destroy_time` datetime DEFAULT NULL COMMENT '核销时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_distributor_code` (`distributor_id`,`third_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权益';

CREATE TABLE `third_quanyi_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `third_quanyi_id` int(11) DEFAULT NULL COMMENT '对应的权益id',
  `direction` smallint(4) DEFAULT NULL COMMENT '请求方向 1第三方访问-本系统 2.本系统访问第三方',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求地址',
  `header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求头',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应',
  `operator_user_type` smallint(4) NOT NULL COMMENT '操作人分类 1、苏宁 2、b2b系统 3、系统管理员 4、c端用户',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人名称',
  `operate_type` smallint(4) DEFAULT NULL COMMENT '操作类型',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `key_third_quanyi_id` (`third_quanyi_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;