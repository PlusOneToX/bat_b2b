CREATE TABLE `coupon` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券名称',
  `coupon_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '优惠券描述',
  `coupon_explain` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券使用说明',
  `invalid_explain` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '优惠券作废说明',
  `generate_count` int(10) unsigned DEFAULT NULL COMMENT '优惠券总数量',
  `generated_count` int(10) unsigned DEFAULT NULL COMMENT '已发放和领取数量',
  `used_count` int(10) unsigned DEFAULT NULL COMMENT '发放已使用数量',
  `limit_count` int(10) unsigned DEFAULT NULL COMMENT '每个客户限领数量',
  `apply_status` smallint(5) unsigned DEFAULT '0' COMMENT '申请状态：0草稿 1申请中 2申请通过 3申请失败',
  `coupon_status` smallint(5) unsigned DEFAULT '0' COMMENT '优惠券状态： 0 未发布,1 未开始, 2 进行中, 3 已过期,4 提前结束 5已作废',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `received_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '领券方式，1 自主领取, 2 人工发放, 3 自动发放',
  `coupon_method` smallint(5) unsigned DEFAULT NULL COMMENT '优惠形式，1满减  2满折 3兑换',
  `order_money` decimal(16,3) DEFAULT '0.000' COMMENT '订单满金额',
  `reduction` decimal(16,3) DEFAULT '0.000' COMMENT '减免金额',
  `discount` decimal(16,3) DEFAULT '0.000' COMMENT '折扣',
  `coupon_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '优惠类型：1 普通,4 新人, 2 Note 20专题, 3 S20 FE专题 ...',
  `coupon_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '优惠券编码',
  `delivery_fee_flag` smallint(5) unsigned DEFAULT NULL COMMENT '是否收取快递费 0否 1是',
  `delivery_fee` decimal(16,3) unsigned DEFAULT '0.000' COMMENT '快递费',
  `model_scope` smallint(5) unsigned DEFAULT '1' COMMENT '适用型号，1全部型号可用 2指定型号可用',
  `material_scope` smallint(5) unsigned DEFAULT NULL COMMENT '适用材质，1全部材质可用 2指定材质可用',
  `coupon_scope` smallint(5) unsigned DEFAULT NULL COMMENT '适用范围，1全部可用 3指定分销商可用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='优惠券信息表';

CREATE TABLE `coupon_customer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `coupon_id` int(10) unsigned NOT NULL COMMENT '优惠券id',
  `coupon_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券码',
  `coupon_status` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '状态，1 未开始,2 进行中, 3 已过期,5 已作废 6已使用',
  `customer_id` int(10) unsigned NOT NULL COMMENT 'C端客户id',
  `open_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方用户系统编号,渠道ID',
  `platform` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销平台编码',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '归属分销商id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `invalid_explain` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '优惠券作废说明',
  `pieces` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '领取的第几张',
  `customer_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户名称',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `coupon_id` (`coupon_id`) USING BTREE,
  KEY `coupon_no` (`coupon_no`) USING BTREE,
  KEY `customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='C端客户已领取优惠券表';

CREATE TABLE `coupon_distributor_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `coupon_id` int(10) unsigned NOT NULL COMMENT '优惠券id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名(登录名)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `coupon_id` (`coupon_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='优惠券与分销商可视关系表';

CREATE TABLE `coupon_material_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `coupon_id` int(10) unsigned NOT NULL COMMENT '优惠券id',
  `material_id` int(10) unsigned DEFAULT NULL COMMENT '材质id',
  `material_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '材质名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `coupon_id` (`coupon_id`) USING BTREE,
  KEY `material_id` (`material_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='优惠券与材质关联表';

CREATE TABLE `coupon_model_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `coupon_id` int(10) unsigned NOT NULL COMMENT '优惠券id',
  `model_id` int(10) unsigned DEFAULT NULL COMMENT '型号id',
  `model_name` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '型号名称',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `coupon_id` (`coupon_id`) USING BTREE,
  KEY `model_id` (`model_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='优惠券与型号关联表';

CREATE TABLE `group_seckill` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼团秒杀名称',
  `group_seckill_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拼团秒杀描述',
  `group_seckill_type` smallint(5) unsigned DEFAULT '0' COMMENT ' 拼团秒杀：1拼团 2秒杀',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `apply_status` smallint(5) unsigned DEFAULT '0' COMMENT '申请状态：0草稿 1申请中 2申请通过 3申请失败',
  `group_seckill_status` smallint(5) unsigned DEFAULT '0' COMMENT '拼团秒杀状态： 0、未开始、1、进行中 2、已暂停 3、已过期 4 提前结束',
  `sort` int(11) NOT NULL COMMENT '排序号',
  `advance_flag` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '是否提前展示：1 准时2 提前',
  `distributor_scope` smallint(5) unsigned DEFAULT '1' COMMENT '分销商可视范围：1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员',
  `distributor_scope_no` smallint(5) unsigned DEFAULT '0' COMMENT '分销商不可视范围：0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `group_seckill_type` (`group_seckill_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动基本信息表';

CREATE TABLE `group_seckill_admin_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `admin_id` (`admin_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与业务员可视关系表';

CREATE TABLE `group_seckill_admin_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `admin_id` (`admin_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与业务员不可视关系表';

CREATE TABLE `group_seckill_department_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `department_id` (`department_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与销售部门可视关系表';

CREATE TABLE `group_seckill_department_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `department_id` (`department_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与销售部门不可视关系表';

CREATE TABLE `group_seckill_distributor_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名(登录名)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与分销商可视关系表';

CREATE TABLE `group_seckill_distributor_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与分销商不可视关系表';

CREATE TABLE `group_seckill_goods` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `item_id` int(10) unsigned NOT NULL COMMENT '货品id',
  `goods_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品编号',
  `item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品编号',
  `sort` int(11) NOT NULL COMMENT '排序号',
  `exist_flag` smallint(5) unsigned NOT NULL COMMENT '优先使用现有库存 1、是 0、否',
  `mto_flag` smallint(5) unsigned DEFAULT '1' COMMENT '是否支持超卖或预售（当先试用现有库存时为超卖，不先试用库存时为预售） 1、支持 0、不支持',
  `max_num` int(10) unsigned DEFAULT NULL COMMENT '最大拼团或秒杀数量、默认不限制',
  `min_num` int(10) unsigned DEFAULT NULL COMMENT '最小起拼或秒杀数量、默认不限制',
  `group_seckill_price` decimal(10,2) NOT NULL COMMENT '拼团秒杀价',
  `virtual_sum` int(10) unsigned NOT NULL COMMENT '虚拟拼团秒杀数',
  `real_sum` int(10) unsigned DEFAULT '0' COMMENT '已拼或已秒实际数量',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE,
  KEY `goods_id` (`goods_id`) USING BTREE,
  KEY `item_id` (`item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与商品关联表';

CREATE TABLE `group_seckill_scale_price_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `scale_price_id` (`scale_price_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与分销商等级可视关系表';

CREATE TABLE `group_seckill_scale_price_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_seckill_id` int(10) unsigned NOT NULL COMMENT '拼团秒杀活动id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `scale_price_id` (`scale_price_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='拼团秒杀活动与分销商等级不可视关系表';

CREATE TABLE `promotion` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '促销活动名称',
  `name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '活动英文名称',
  `promo_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '促销活动描述',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `advance_flag` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '是否提前展示：1 准时2 提前',
  `apply_status` smallint(5) unsigned DEFAULT '0' COMMENT '申请状态：0草稿 1申请中 2申请通过 3申请失败',
  `promo_status` smallint(5) unsigned DEFAULT '0' COMMENT '促销状态：0 未开始, 1 促销中 2 已过期 3 提前结束',
  `promo_source` smallint(5) unsigned DEFAULT '1' COMMENT '活动来源，1 后台新增, 2 批量导入',
  `promo_type` smallint(5) unsigned DEFAULT '1' COMMENT '活动类型，1 普通活动，2 阶梯活动',
  `on_way_flag` smallint(5) unsigned DEFAULT '0' COMMENT '在途商品是否参与活动 1.参与，0.不参与',
  `distributor_scope` smallint(5) unsigned DEFAULT '1' COMMENT '分销商可视范围：1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员',
  `distributor_scope_no` smallint(5) unsigned DEFAULT '0' COMMENT '分销商不可视范围：0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动基本信息表';

CREATE TABLE `promotion_admin_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `admin_id` (`admin_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与业务员可视关系表';

CREATE TABLE `promotion_admin_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `admin_id` (`admin_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与业务员不可视关系表';

CREATE TABLE `promotion_check` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `promotion_type` smallint(5) unsigned NOT NULL COMMENT '活动类型：1 促销活动 2 拼团秒杀活动 3 优惠券',
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
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='活动审批表';

CREATE TABLE `promotion_check_flow` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `promotion_check_id` int(10) unsigned NOT NULL COMMENT '活动审批id',
  `user_id` int(10) unsigned NOT NULL COMMENT '审批人id',
  `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审批人名称',
  `check_status` smallint(5) unsigned DEFAULT '0' COMMENT '审批状态 0, 未审批 1,审批通过，2审批未通过',
  `check_time` datetime DEFAULT NULL COMMENT '审批时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审批备注',
  `check_sort` int(10) unsigned NOT NULL COMMENT '审批顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `promotion_check_id` (`promotion_check_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='活动审批流表';

CREATE TABLE `promotion_department_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `department_id` (`department_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与销售部门可视关系表';

CREATE TABLE `promotion_department_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `department_id` (`department_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与销售部门不可视关系表';

CREATE TABLE `promotion_distributor_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名(登录名)',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与分销商可视关系表';

CREATE TABLE `promotion_distributor_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_id` (`distributor_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与分销商不可视关系表';

CREATE TABLE `promotion_rule` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `rule_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '规则名称',
  `rule_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '规则英文标签',
  `rule_type` smallint(5) unsigned NOT NULL COMMENT '规则对象，1订单，2商品，3货品',
  `add_up_flag` smallint(5) unsigned DEFAULT '0' COMMENT '是否累计，1是 0否( 规则对象是2或3时有效)',
  `money_or_count` smallint(5) unsigned DEFAULT '1' COMMENT '规则形式：1金额  2数量',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动规则信息表';

CREATE TABLE `promotion_rule_condition` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `promotion_rule_id` int(10) unsigned NOT NULL COMMENT '促销活动规则id',
  `condition_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '条件名称',
  `condition_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '条件英文名称',
  `special_flag` smallint(5) unsigned DEFAULT '0' COMMENT '是否特价，1是 0否',
  `one_buy_count` int(10) unsigned DEFAULT '0' COMMENT '一次性购满数量',
  `one_buy_money` decimal(16,3) DEFAULT '0.000' COMMENT '一次性购满金额',
  `reduce_or_present` smallint(5) unsigned DEFAULT '1' COMMENT '促销统计方式：1满减  2满赠',
  `reduce_type` smallint(5) unsigned DEFAULT '1' COMMENT '满减类型，1减免  2折扣',
  `reduction_present_add_flag` smallint(5) unsigned DEFAULT '0' COMMENT '满减满赠是否叠加，1是  0否',
  `discount` decimal(16,3) DEFAULT '0.000' COMMENT '折扣',
  `reduction` decimal(16,3) DEFAULT '0.000' COMMENT '减免',
  `present_count` int(10) unsigned DEFAULT '0' COMMENT '满赠数量',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE,
  KEY `promotion_rule_id` (`promotion_rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动规则条件信息表';

CREATE TABLE `promotion_rule_condition_present` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `promotion_rule_condition_id` int(10) unsigned NOT NULL COMMENT '促销活动规则条件id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品ID',
  `item_id` int(10) unsigned NOT NULL COMMENT '货品ID',
  `count` int(10) unsigned DEFAULT NULL COMMENT '单次赠送数量(为空时不限制)',
  `total_count` int(10) unsigned DEFAULT NULL COMMENT '赠送总数量(为空时不限制)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE,
  KEY `promotion_rule_condition_id` (`promotion_rule_condition_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动规则条件赠品信息表';

CREATE TABLE `promotion_rule_condition_special` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `promotion_rule_condition_id` int(10) unsigned NOT NULL COMMENT '促销活动规则条件id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品ID',
  `item_id` int(10) unsigned NOT NULL COMMENT '货品ID',
  `special_price` decimal(16,2) NOT NULL COMMENT '活动特价',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE,
  KEY `promotion_rule_condition_id` (`promotion_rule_condition_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动规则条件商品特价信息表';

CREATE TABLE `promotion_rule_goods` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `promotion_rule_id` int(10) unsigned NOT NULL COMMENT '促销活动规则id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `item_id` int(10) unsigned DEFAULT NULL COMMENT '货品id',
  `goods_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品编号',
  `item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `promotion_rule_goods_item_id` (`promotion_rule_id`,`goods_id`,`item_id`),
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动规则与商品关联表';

CREATE TABLE `promotion_scale_price_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `scale_price_id` (`scale_price_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与分销商等级可视关系表';

CREATE TABLE `promotion_scale_price_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `promotion_id` int(10) unsigned NOT NULL COMMENT '促销活动id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `scale_price_id` (`scale_price_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='促销活动与分销商等级不可视关系表';

CREATE TABLE `rebate_voucher` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `distributor_id` int(11) NOT NULL COMMENT '分销商id',
  `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券名称',
  `voucher_no` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券编号',
  `face_value` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '面值',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `apply_status` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '申请状态 0草稿 1待审核 2审核通过(可用) 3审核拒绝',
  `freeze_status` smallint(6) unsigned NOT NULL COMMENT '冻结状态 10未冻结(可用) 11冻结',
  `voucher_status` smallint(6) unsigned NOT NULL DEFAULT '1' COMMENT '代金券状态（汇总） 0草稿 1待审核 3审核拒绝 4待生效 5 可用 7已过期 9已用完 11已冻结',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `sort` int(11) DEFAULT NULL COMMENT '排序号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `batch_id` int(11) DEFAULT NULL COMMENT '批量id(一起导入的第一个代金券id)',
  `batch_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '批量名称(一起导入的第一个代金券名称)',
  `create_user_id` int(10) DEFAULT NULL COMMENT '创建人用户id',
  `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_voucher_no` (`voucher_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='返利代金券';

CREATE TABLE `rebate_voucher_usage_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `rebate_voucher_id` int(11) NOT NULL COMMENT '代金券id',
  `rebate_voucher_no` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券编号',
  `use_amount` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '使用金额 负数为使用 正数为退还',
  `balance` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '余额',
  `order_id` int(11) NOT NULL COMMENT '订单id',
  `order_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `use_time` datetime NOT NULL COMMENT '使用时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='返利代金券使用记录';

