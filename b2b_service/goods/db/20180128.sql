CREATE TABLE `attribute` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品属性名称',
  `name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品属性英文版名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品属性备注',
  `open_flag` smallint(5) unsigned DEFAULT '0' COMMENT '状态, 0启用,1停用',
  `attribute_type` smallint(6) DEFAULT '1' COMMENT '类型 1.货品规格 2.货品颜色 3.商品参数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品属性表';

CREATE TABLE `attribute_value` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品属性值名称',
  `name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '规格值英文版名称',
  `attribute_id` int(10) unsigned NOT NULL COMMENT '商品属性值关联id',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `attribute_id` (`attribute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品属性值表';

CREATE TABLE `brand` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品牌名称',
  `name_en` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '品牌英文版名称',
  `logo` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '品牌图标',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '品牌描述',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '是否启用, 0为停用，1为启用',
  `sort` int(11) DEFAULT '0' COMMENT '排序号',
  `distributor_scope` tinyint(3) unsigned DEFAULT '0' COMMENT '分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组',
  `distributor_scope_no` tinyint(3) unsigned DEFAULT '0' COMMENT '分销商不可视范围,0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌表';

CREATE TABLE `brand_admin_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与业务员可视关系表';

CREATE TABLE `brand_admin_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与业务员不可视关系表';

CREATE TABLE `brand_department_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与销售部门可视关系表';

CREATE TABLE `brand_department_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与销售部门不可视关系表';

CREATE TABLE `brand_distributor_group_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `distributor_group_id` int(10) unsigned NOT NULL COMMENT '分销商分组id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_group_id` (`distributor_group_id`) USING BTREE,
  KEY `brand_id` (`brand_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与分销商分组可视关系表';

CREATE TABLE `brand_distributor_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与分销商可视关系表';

CREATE TABLE `brand_distributor_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与分销商不可视关系表';

CREATE TABLE `brand_scale_price_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`),
  KEY `scale_price_id` (`scale_price_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与分销商等级可视关系表';

CREATE TABLE `brand_scale_price_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `brand_id` int(10) unsigned NOT NULL COMMENT '品牌id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`),
  KEY `scale_price_id` (`scale_price_id`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品牌与分销商等级不可视关系表';

CREATE TABLE `category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '品类名称',
  `name_en` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '品类英文版名称',
  `thirdparty_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方编码',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品类描述',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '是否启用, 0为停用，1为启用',
  `sort` int(11) DEFAULT '0' COMMENT '排序号',
  `distributor_scope` tinyint(3) unsigned DEFAULT '0' COMMENT '分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组',
  `distributor_scope_no` tinyint(3) unsigned DEFAULT '0' COMMENT '分销商不可视范围,0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类表';

CREATE TABLE `category_admin_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与业务员可视关系表';

CREATE TABLE `category_admin_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与业务员不可视关系表';

CREATE TABLE `category_department_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与销售部门可视关系表';

CREATE TABLE `category_department_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与销售部门不可视关系表';

CREATE TABLE `category_distributor_group_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `distributor_group_id` int(10) unsigned NOT NULL COMMENT '分销商分组id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `distributor_group_id` (`distributor_group_id`) USING BTREE,
  KEY `category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与分销商分组可视关系表';

CREATE TABLE `category_distributor_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与分销商可视关系表';

CREATE TABLE `category_distributor_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与分销商不可视关系表';

CREATE TABLE `category_scale_price_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`),
  KEY `scale_price_id` (`scale_price_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与分销商等级可视关系表';

CREATE TABLE `category_scale_price_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category_id` int(10) unsigned NOT NULL COMMENT '品类id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`),
  KEY `scale_price_id` (`scale_price_id`),
  KEY `category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='品类与分销商等级不可视关系表';

CREATE TABLE `classify` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分类英文版名称',
  `sort` int(11) DEFAULT '0' COMMENT '排序号',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1是启用，0停用',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父级ID',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '描述',
  `image_url` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分类图片地址',
  `image_url_en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分类英文图片地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `recommend_flag` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '是否推荐分类 0否 1是',
  `applet_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序分类名称',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品分类表';

CREATE TABLE `classify_banner_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `classify_id` int(10) unsigned DEFAULT NULL COMMENT '分类id',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '轮播图地址',
  `jump_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跳转地址',
  `sort` smallint(5) unsigned DEFAULT NULL COMMENT '排序',
  `recommend_flag` smallint(5) unsigned DEFAULT '0' COMMENT '是否推荐轮播图 0否 1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分类轮播图关系表';

CREATE TABLE `goods` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `goods_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品英文名称',
  `goods_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品编号',
  `keywords` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '关键字',
  `introduce` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品简介',
  `introduce_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '商品英文简介',
  `sale_status` tinyint(3) unsigned DEFAULT '1' COMMENT '上架状态，1未上架，2审批中，3已上架',
  `sale_time` datetime DEFAULT NULL COMMENT '上架时间',
  `freeze_status` tinyint(3) unsigned NOT NULL COMMENT '冻结状态，1未冻结，2冻结',
  `freeze_time` datetime DEFAULT NULL COMMENT '冻结时间',
  `goods_type` tinyint(4) DEFAULT '1' COMMENT '商品类型 1-普通 2-定制',
  `diy_type` tinyint(4) DEFAULT '1' COMMENT '定制类型 0-标准定制 1-DIY定制',
  `image_url1` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图片地址1',
  `image_url2` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图片地址2',
  `image_url3` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图片地址3',
  `image_url4` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图片地址4',
  `image_url5` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图片地址5',
  `image_url6` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图片地址6',
  `image_url1en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文版图片地址1',
  `image_url2en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文版图片地址2',
  `image_url3en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文版图片地址3',
  `image_url4en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文版图片地址4',
  `image_url5en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文版图片地址5',
  `image_url6en` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文版图片地址6',
  `content_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `content_url_en` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '英文版内容',
  `distributor_scope` tinyint(3) unsigned DEFAULT '0' COMMENT '分销商可视范围,0.不指定,1全部,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员,6.指定分销商分组',
  `distributor_scope_no` tinyint(3) unsigned DEFAULT '0' COMMENT '分销商不可视范围,0.不指定,2分销商等级,3.指定分销商,4.指定销售部门,5.指定业务员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `brand_id` int(10) unsigned DEFAULT '0' COMMENT '品牌id',
  `category_id` int(10) unsigned DEFAULT NULL COMMENT '品类id',
  PRIMARY KEY (`id`),
  KEY `goods_no` (`goods_no`),
  KEY `sale_time` (`sale_time`),
  KEY `goods_type` (`goods_type`),
  KEY `category_id` (`category_id`),
  KEY `goods_name` (`goods_name`),
  KEY `goods_name_en` (`goods_name_en`),
  KEY `keywords` (`keywords`),
  KEY `brand_id` (`brand_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品表';

CREATE TABLE `goods_admin_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与业务员可视关系表';

CREATE TABLE `goods_admin_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `admin_id` int(10) unsigned NOT NULL COMMENT '业务员（后台用户）id',
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与业务员不可视关系表';

CREATE TABLE `goods_classify_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `classify_id` int(10) unsigned NOT NULL COMMENT '商品分类id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  PRIMARY KEY (`id`),
  KEY `classify_id` (`classify_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与商品分类关联关系表';

CREATE TABLE `goods_department_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与销售部门可视关系表';

CREATE TABLE `goods_department_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `department_id` int(10) unsigned NOT NULL COMMENT '销售部门id',
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与销售部门不可视关系表';

CREATE TABLE `goods_distributor_collection` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `distributor_goods_idx` (`distributor_id`,`goods_id`) USING BTREE,
  KEY `goods_id` (`goods_id`),
  KEY `distributor_id` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品分销商收藏表';

CREATE TABLE `goods_distributor_group_relevance` (
  `id` int(11) NOT NULL,
  `goods_id` int(11) DEFAULT NULL,
  `distributor_group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='品类与分销商分组可视关系表';

CREATE TABLE `goods_distributor_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与分销商可视关系表';

CREATE TABLE `goods_distributor_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `distributor_id` int(10) unsigned NOT NULL COMMENT '分销商id',
  PRIMARY KEY (`id`),
  KEY `distributor_id` (`distributor_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与分销商不可视关系表';

CREATE TABLE `goods_item` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `item_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货品名称',
  `item_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '货品英文名称',
  `item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '货品编号',
  `item_erp_id` int(11) NOT NULL COMMENT '商品erp物料id',
  `bar_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '条形码',
  `sale_price` decimal(16,3) DEFAULT NULL COMMENT '默认销售价',
  `cost_price` decimal(16,3) DEFAULT NULL COMMENT '成本价',
  `weight` decimal(16,3) DEFAULT '0.000' COMMENT '重量，克',
  `length` decimal(16,3) DEFAULT '0.000' COMMENT '长度',
  `width` decimal(16,3) DEFAULT '0.000' COMMENT '宽',
  `height` decimal(16,3) DEFAULT '0.000' COMMENT '高',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位',
  `item_img` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '货品图片',
  `moq` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预售最少购买数量',
  `advance_sale_flag` tinyint(4) DEFAULT '0' COMMENT '是否支持预售：0-否 1-是',
  `life_cycle` tinyint(3) unsigned DEFAULT NULL COMMENT '商品生命周期 1.导入初期，2.成长期，3.成熟期，4.衰退期，5.项目终止',
  `promotion_status` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应ERP促销状态 清仓 5caebbb86c7863 ; BCD  5e9fec57c26d75',
  `onway_sale_flag` tinyint(4) DEFAULT '0' COMMENT '直发客户是否支持在途：0-否 1-是',
  `sale_status` tinyint(3) unsigned DEFAULT '1' COMMENT '货品上架状态，1未上架，2审批中，3已上架',
  `sale_time` datetime DEFAULT NULL COMMENT '上架时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_item_item_code` (`item_code`),
  KEY `item_erp_id` (`item_erp_id`),
  KEY `goods_id` (`goods_id`),
  KEY `bar_code` (`bar_code`),
  KEY `item_code` (`item_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='货品表';

CREATE TABLE `goods_item_box` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_item_id` int(10) unsigned NOT NULL COMMENT '货品id',
  `box_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '箱子名称',
  `box_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '箱子类型',
  `box_length` decimal(16,3) DEFAULT NULL COMMENT '箱子长度',
  `box_height` decimal(16,3) DEFAULT NULL COMMENT '箱子高度',
  `box_width` decimal(16,3) DEFAULT NULL COMMENT '箱子宽度',
  `box_weight` decimal(16,3) DEFAULT NULL COMMENT '箱子重量',
  `box_num` decimal(16,3) DEFAULT NULL COMMENT '箱子装箱数量',
  `box_erp_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '箱子erpID',
  `sort` int(11) DEFAULT '0' COMMENT '排序号',
  `default_flag` smallint(6) DEFAULT '0' COMMENT '是否按装箱数量售卖 1、是 0、否',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `goods_item_id` (`goods_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='货品装箱规格表';

CREATE TABLE `goods_item_data` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_item_id` int(10) unsigned NOT NULL COMMENT '货品id',
  `series` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '对应erp系列：F_PAEZ_XL',
  `applied_device` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '对应erp应用设备：F_PAEZ_YYSB',
  `model_en` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应erp产品型号en：F_PAEZ_CPXH',
  `series_en` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应erp系列en：F_PAEZ_XL1',
  `applied_device_en` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应erp应用设备en：F_PAEZ_YYSB1',
  `purchase_cycle` int(11) DEFAULT '0' COMMENT '对应erp固定提前期:FFixLeadTime',
  `plan_strategy` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '对应erp计划策略模式:F_PAEZ_MRP1',
  PRIMARY KEY (`id`),
  KEY `goods_item_id` (`goods_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='货品扩展数据表';

CREATE TABLE `goods_item_scale_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `goods_item_id` int(10) unsigned NOT NULL COMMENT '货品id',
  `goods_item_grade_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  `price` decimal(16,6) NOT NULL COMMENT '等级价格',
  PRIMARY KEY (`id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='货品分销商价格表';

CREATE TABLE `goods_item_specs_color` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_item_id` int(10) unsigned NOT NULL COMMENT '货品id',
  `attribute_type` tinyint(3) unsigned NOT NULL COMMENT '值类型：1.规格，2.颜色',
  `attribute_value_id` int(10) unsigned NOT NULL COMMENT '货品规格或颜色id',
  PRIMARY KEY (`id`),
  KEY `goods_item_id` (`goods_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='货品规格颜色表';

CREATE TABLE `goods_min_max_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品编号',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  `min_price` decimal(16,3) DEFAULT NULL COMMENT '商品最小销售价',
  `max_price` decimal(16,3) DEFAULT NULL COMMENT '商品最大销售价',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_goods_scale_price` (`goods_id`,`scale_price_id`),
  KEY `goods_id` (`goods_id`),
  KEY `scale_price_id_key` (`scale_price_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品中每个价格等级最小和最大价表';

CREATE TABLE `goods_param_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `param_id` int(10) unsigned NOT NULL COMMENT '参数id',
  PRIMARY KEY (`id`),
  KEY `param_id` (`param_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与参数关联关系表';

CREATE TABLE `goods_promotion_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品编号',
  `special_flag` smallint(5) unsigned DEFAULT NULL COMMENT '是否特价，1是 0否',
  `reduce_or_present` smallint(5) unsigned DEFAULT NULL COMMENT '促销统计方式：1满减  2满赠',
  `group_seckill_type` smallint(5) unsigned DEFAULT NULL COMMENT ' 拼团秒杀：1拼团 2秒杀',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  `promotion_id` int(10) unsigned DEFAULT NULL COMMENT '促销活动id',
  `group_seckill_id` int(10) unsigned DEFAULT NULL COMMENT '拼团秒杀活动id',
  `min_price` decimal(16,3) DEFAULT NULL COMMENT '商品最小价',
  `max_price` decimal(16,3) DEFAULT NULL COMMENT '商品最大价',
  `promotion_min_price` decimal(16,3) DEFAULT NULL COMMENT '商品活动最小价',
  `promotion_max_price` decimal(16,3) DEFAULT NULL COMMENT '商品活动最大价',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `goods_id` (`goods_id`) USING BTREE,
  KEY `scale_price_id` (`scale_price_id`) USING BTREE,
  KEY `promotion_id` (`promotion_id`) USING BTREE,
  KEY `group_seckill_id` (`group_seckill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品中每个价格等级最小和最大活动价表';

CREATE TABLE `goods_sale_data` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品编号',
  `sale_num` int(10) unsigned DEFAULT '0' COMMENT '销售数量',
  `virtual_num` int(10) unsigned DEFAULT '0' COMMENT '虚拟销量',
  `show_type` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '销量展示类型: 1 销售数量,2虚拟销量+销售数量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `goods_id` (`goods_id`) USING BTREE,
  KEY `sale_num` (`sale_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品扩展数据表';

CREATE TABLE `goods_scale_price_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`),
  KEY `scale_price_id` (`scale_price_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与分销商等级可视关系表';

CREATE TABLE `goods_scale_price_relevance_no` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `scale_price_id` int(10) unsigned NOT NULL COMMENT '价格等级id',
  PRIMARY KEY (`id`),
  KEY `scale_price_id` (`scale_price_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与分销商等级不可视关系表';

CREATE TABLE `goods_stock_flag` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `item_id` int(10) NOT NULL COMMENT '货品id',
  `warehouse_id_arr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仓库id数组、id用_隔开 0、表示VMI、id按照由低到高升序、VMI在前面',
  `under_stock_flag` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '在库是否缺货 0否 1是',
  `whether_out_of_stock_in_transit` smallint(5) NOT NULL DEFAULT '0' COMMENT '在途是否缺货 0否 1是',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouse_item_unique` (`item_id`,`warehouse_id_arr`) USING BTREE COMMENT '货品和仓库组合唯一',
  KEY `item_id_index` (`item_id`) USING BTREE COMMENT '货品id索引',
  KEY `under_stock_flag_index` (`under_stock_flag`) USING BTREE COMMENT '是否缺货索引',
  KEY `warehouse_id_arr` (`warehouse_id_arr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='货品在库库存是否缺货标记表';

CREATE TABLE `goods_store_column` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `column_id` int(10) unsigned NOT NULL COMMENT '栏目编号',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品编号',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `column_goods_idx` (`column_id`,`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='栏目商品关联表';

CREATE TABLE `goods_store_mobile` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `mobile_id` int(11) NOT NULL COMMENT '移动端设置id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品编号',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `module_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序分类名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `mobile_goods_idx` (`mobile_id`,`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='移动端商店配置商品关联表';

CREATE TABLE `goods_store_section` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `section_id` int(10) unsigned NOT NULL COMMENT '板块编号',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品编号',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `section_goods_idx` (`section_id`,`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='板块商品关联表';

CREATE TABLE `goods_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签英文名称',
  `open_flag` smallint(6) NOT NULL DEFAULT '1' COMMENT '状态: 1为启用，0 停用',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品标签表';

CREATE TABLE `goods_tag_relevance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` int(10) unsigned NOT NULL COMMENT '商品id',
  `tag_id` int(10) unsigned NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`),
  KEY `tag_id` (`tag_id`),
  KEY `goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='商品与标签关联关系表';

CREATE TABLE `scale_price` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '价格等级名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '价格等级描述',
  `retail_flag` smallint(5) unsigned DEFAULT '0' COMMENT '否建议零售价, 0否，1是',
  `open_flag` smallint(5) unsigned DEFAULT '1' COMMENT '状态, 1启用,0停用',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `erp_field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '对应erp价目表价格字段',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='价格等级表';