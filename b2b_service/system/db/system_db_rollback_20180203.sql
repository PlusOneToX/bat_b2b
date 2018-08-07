/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 127.0.0.1:3306
 Source Schema         : system_db

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 03/08/2018 16:02:45
*/
DROP DATABASE IF EXISTS `system_db`;
CREATE DATABASE `system_db` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

USE system_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for check
-- ----------------------------
DROP TABLE IF EXISTS `check`;
CREATE TABLE `check`
(
    `id`                  int(10) UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '审批编号',
    `ext`                 smallint(6) UNSIGNED NOT NULL COMMENT '审批模块 1.商品管理上下架审批 2. 分销商编辑审批 3. 仓库库存调整审批 4. 仓库库存预留审批 5. 订单价格审批 6. 订单对账折扣审批',
    `sub_ext`             int(10) UNSIGNED     NOT NULL DEFAULT 0 COMMENT '审批子模块 ',
    `sub_ext1`            int(10) UNSIGNED     NULL     DEFAULT NULL COMMENT '审批子模块扩展类型 ',
    `apply_user`          int(10) UNSIGNED     NOT NULL COMMENT '申请人',
    `status`              smallint(6) UNSIGNED NOT NULL DEFAULT 0 COMMENT '审批状态 0.未审批 1.审批中，2.审批通过 3.审批未通过',
    `last_check_user`     int(10) UNSIGNED     NULL     DEFAULT 0 COMMENT '最后一个审批过的人',
    `next_check_user`     int(10) UNSIGNED     NULL     DEFAULT 0 COMMENT '下一个审批人',
    `through_check_count` smallint(6) UNSIGNED NULL     DEFAULT 0 COMMENT '已审批人数',
    `check_user_count`    smallint(6) UNSIGNED NULL     DEFAULT 0 COMMENT '审批总人数',
    `create_time`         datetime(0)          NOT NULL COMMENT '创建时间',
    `update_time`         datetime(0)          NOT NULL COMMENT '更新时间',
    `last_check_time`     datetime(0)          NULL     DEFAULT NULL COMMENT '最后审批时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `apply_user` (`apply_user`, `ext`, `update_time`) USING BTREE,
    INDEX `next_check_user` (`next_check_user`, `ext`, `update_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '审批表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for check_config
-- ----------------------------
DROP TABLE IF EXISTS `check_config`;
CREATE TABLE `check_config`
(
    `id`          int(10) UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '审批单编号',
    `ext`         tinyint(1) UNSIGNED  NOT NULL DEFAULT 0 COMMENT '审批单类型 ',
    `check_user`  int(10) UNSIGNED     NULL     DEFAULT 0 COMMENT '审批人',
    `check_order` smallint(6)          NULL     DEFAULT 0 COMMENT '审批排序',
    `open_flag`   smallint(6) UNSIGNED NOT NULL DEFAULT 1 COMMENT '审批是否开启 1.审批开启,2.审批关闭',
    `create_time` datetime(0)          NOT NULL COMMENT '创建时间',
    `update_time` datetime(0)          NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `ext` (`ext`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '审批配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for check_flow
-- ----------------------------
DROP TABLE IF EXISTS `check_flow`;
CREATE TABLE `check_flow`
(
    `id`           int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '审批流程编号',
    `check_id`     int(10) UNSIGNED                                              NOT NULL DEFAULT 0 COMMENT '审批编号',
    `check_user`   int(10) UNSIGNED                                              NOT NULL DEFAULT 0 COMMENT '审批人 ',
    `check_status` smallint(6) UNSIGNED                                          NULL     DEFAULT 0 COMMENT '审批状态 0, 未审批 1,审批通过，2审批未通过',
    `check_time`   datetime(0)                                                   NOT NULL COMMENT '审批时间',
    `remark`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '审批备注',
    `check_order`  int(10) UNSIGNED                                              NOT NULL DEFAULT 1 COMMENT '审批顺序',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `check_id` (`check_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '审批流程表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for check_relation
-- ----------------------------
DROP TABLE IF EXISTS `check_relation`;
CREATE TABLE `check_relation`
(
    `id`          int(10) UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '审批模块关联编号',
    `ext`         smallint(6) UNSIGNED NOT NULL COMMENT '审批模块 1.商品管理上下架审批 2. 分销商编辑审批 3. 仓库库存调整审批 4. 仓库库存预留审批 5. 订单价格审批 6. 订单对账折扣审批',
    `create_time` datetime(0)          NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0)          NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `ext` (`ext`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '审批模块关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for download_center
-- ----------------------------
DROP TABLE IF EXISTS `download_center`;
CREATE TABLE `download_center`
(
    `id`               int(11)                                                       NOT NULL AUTO_INCREMENT,
    `parent_id`        int(11) UNSIGNED                                              NOT NULL COMMENT '父id',
    `sort`             int(5)                                                        NOT NULL DEFAULT 0 COMMENT '排序值',
    `status`           smallint(6)                                                   NOT NULL DEFAULT 0 COMMENT '0禁用 1启用',
    `title_zh`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 中文',
    `title_en`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 英文',
    `content_url_zh`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '内容 资源地址 中文',
    `content_url_en`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '内容 资源地址 英文',
    `thumbnail_url_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '缩略图 资源地址(未启用) 中文',
    `thumbnail_url_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '缩略图 资源地址(未启用) 英文',
    `create_time`      datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '下载中心'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for export_download
-- ----------------------------
DROP TABLE IF EXISTS `export_download`;
CREATE TABLE `export_download`
(
    `id`            int(11) UNSIGNED                                        NOT NULL AUTO_INCREMENT,
    `platform`      smallint(6)                                             NOT NULL COMMENT '平台来源 web admin,前台管理后台',
    `operator_id`   int(11)                                                 NOT NULL COMMENT '分销商id 业务员id',
    `business_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '业务名称 什么导出',
    `download_url`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '下载链接',
    `create_time`   datetime(0)                                             NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '导出下载记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for global_agreement_distributor
-- ----------------------------
DROP TABLE IF EXISTS `global_agreement_distributor`;
CREATE TABLE `global_agreement_distributor`
(
    `id`             int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `agreement_id`   int(11)          NOT NULL COMMENT '协议id',
    `distributor_id` int(11)          NOT NULL COMMENT '分销商id',
    `create_time`    datetime(0)      NOT NULL COMMENT '创建时间',
    `update_time`    datetime(0)      NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '全站设置协议分销商关联'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for global_agreement_setting
-- ----------------------------
DROP TABLE IF EXISTS `global_agreement_setting`;
CREATE TABLE `global_agreement_setting`
(
    `id`             int(11) UNSIGNED                                        NOT NULL AUTO_INCREMENT,
    `name`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '协议名称',
    `agreement_area` smallint(6)                                             NOT NULL COMMENT '协议发布区域 0国内 1国外',
    `type`           smallint(6)                                             NOT NULL COMMENT '协议类型 1品牌协议 2用户协议',
    `content`        longtext CHARACTER SET utf8 COLLATE utf8_general_ci     NOT NULL COMMENT '协议内容',
    `status`         smallint(6)                                             NOT NULL COMMENT '协议状态 0禁用 1启用',
    `brand_id`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属品牌',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '全站设置协议设置'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for global_agreement_setting_brand
-- ----------------------------
DROP TABLE IF EXISTS `global_agreement_setting_brand`;
CREATE TABLE `global_agreement_setting_brand`
(
    `id`                   int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `agreement_setting_id` int(11)          NOT NULL COMMENT '协议设置id',
    `agreement_area`       smallint(6)      NOT NULL COMMENT '协议区域',
    `brand_id`             int(11)          NOT NULL COMMENT '所属品牌',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_agreement_area_brand` (`agreement_area`, `brand_id`) USING BTREE COMMENT '协议区域 品牌 协议设置唯一约束'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '全站设置协议设置'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for global_base_setting
-- ----------------------------
DROP TABLE IF EXISTS `global_base_setting`;
CREATE TABLE `global_base_setting`
(
    `key`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
    `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'value',
    `desc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述备注',
    PRIMARY KEY (`key`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '全站设置基础设置表（系统常量）'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for global_factory_setting_delay_push
-- ----------------------------
DROP TABLE IF EXISTS `global_factory_setting_delay_push`;
CREATE TABLE `global_factory_setting_delay_push`
(
    `id`        int(11)                                                      NOT NULL AUTO_INCREMENT,
    `factory`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工厂',
    `push_time` datetime(0)                                                  NULL DEFAULT NULL COMMENT '推送时间',
    `use_range` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关分销商',
    `type`      int(1)                                                       NULL DEFAULT NULL COMMENT '0全部可用 1指定可用 2指定不可用',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '全站设置工厂设置工厂推送表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for global_factory_setting_order_invalid
-- ----------------------------
DROP TABLE IF EXISTS `global_factory_setting_order_invalid`;
CREATE TABLE `global_factory_setting_order_invalid`
(
    `id`           int(11)                                                      NOT NULL AUTO_INCREMENT,
    `name`         varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道名称',
    `order_source` int(2)                                                       NULL DEFAULT NULL COMMENT '渠道来源',
    `invalid`      int(5)                                                       NULL DEFAULT NULL COMMENT '订单时效',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '订单时效表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for global_shop_setting
-- ----------------------------
DROP TABLE IF EXISTS `global_shop_setting`;
CREATE TABLE `global_shop_setting`
(
    `key`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
    `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'value',
    `desc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述备注',
    PRIMARY KEY (`key`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '全站设置购物设置表（系统常量）'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logistics
-- ----------------------------
DROP TABLE IF EXISTS `logistics`;
CREATE TABLE `logistics`
(
    `id`                   int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `name`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '配送名称',
    `sort`                 int(11)                                                       NOT NULL DEFAULT 0 COMMENT '排序号',
    `enable`               smallint(6)                                                   NOT NULL DEFAULT 1 COMMENT '是否启用, 0为停用，1为启用',
    `description`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '描述',
    `logistics_erp_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'erp配送方式编号',
    `billing_method`       smallint(6)                                                   NOT NULL DEFAULT 1 COMMENT '计费方式 1 重量计费(默认) 2 体积计费',
    `first_weight`         double(16, 2)                                                 NULL     DEFAULT 0.00 COMMENT '首重重量',
    `first_volume`         double(16, 2)                                                 NULL     DEFAULT 0.00 COMMENT '首重体积',
    `additional_weight`    double(16, 2)                                                 NULL     DEFAULT 0.00 COMMENT '续重重量',
    `additional_volume`    double(16, 2)                                                 NULL     DEFAULT 0.00 COMMENT '续重体积',
    `min_weight`           double(16, 2) UNSIGNED                                        NULL     DEFAULT 0.00 COMMENT '最低重量',
    `min_volume`           double(16, 2)                                                 NULL     DEFAULT 0.00 COMMENT '最低体积',
    `max_weight`           double(16, 2) UNSIGNED                                        NULL     DEFAULT 0.00 COMMENT '最高重量',
    `max_volume`           double(16, 2)                                                 NULL     DEFAULT 0.00 COMMENT '最高体积',
    `min_cost`             double(16, 2) UNSIGNED                                        NULL     DEFAULT 0.00 COMMENT '最低运费',
    `use_range`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '1' COMMENT '适用范围，1.普通商品，2.定制商品 3普通商品和定制商品',
    `logistics_factory_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT '工厂配送方式编号',
    `logistics_kdn_code`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '快递鸟快递公司编码',
    `logistics_kdn_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT '快递鸟快递公司名称',
    `appoint_area_flag`    smallint(6) UNSIGNED                                          NOT NULL DEFAULT 0 COMMENT '是否指定地区 0为统一设置 1为指定地区',
    `distributor_scope`    smallint(6)                                                   NOT NULL DEFAULT 1 COMMENT '1全部分销商,2等级分销商,3事业部,4指定业务部门,5指定业务员,6指定分销商',
    `manufactors`          varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '生产商 YC.云创 LHW.联辉王（同时支持，中间用\",\"号隔开）',
    `website`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '快递官网地址',
    `material_id`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '材质id 多个材质逗号分隔',
    `create_time`          datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`          datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '配送表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for logistics_area
-- ----------------------------
DROP TABLE IF EXISTS `logistics_area`;
CREATE TABLE `logistics_area`
(
    `id`                     int(11) UNSIGNED                                               NOT NULL AUTO_INCREMENT,
    `logistics_id`           int(11) UNSIGNED                                               NOT NULL COMMENT '配送id',
    `first_weight_cost`      double(16, 2)                                                  NULL DEFAULT NULL COMMENT '首重费用',
    `first_volume_cost`      double(16, 2)                                                  NULL DEFAULT NULL COMMENT '首体积费用',
    `additional_weight_cost` double(16, 2)                                                  NULL DEFAULT NULL COMMENT '续重费用',
    `additional_volume_cost` double(16, 2)                                                  NULL DEFAULT NULL COMMENT '续体积费用',
    `default_flag`           smallint(6) UNSIGNED                                           NULL DEFAULT 1 COMMENT '是否使用默认 0为否 1为是',
    `formula_flag`           smallint(6) UNSIGNED                                           NULL DEFAULT 1 COMMENT '是否使用公式, 0为否，1为是',
    `formula`                varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公式',
    `group_id`               varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '相同配送配置分组编码',
    `country_id`             int(11)                                                        NULL DEFAULT NULL COMMENT '国家id',
    `province_id`            int(11)                                                        NULL DEFAULT NULL COMMENT '省份id',
    `city_id`                int(11)                                                        NULL DEFAULT NULL COMMENT '城市id',
    `district_id`            int(11)                                                        NULL DEFAULT NULL COMMENT '区域id',
    `create_time`            datetime(0)                                                    NOT NULL COMMENT '创建时间',
    `update_time`            datetime(0)                                                    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `logistics_id` (`logistics_id`) USING BTREE,
    INDEX `uk_logistic_area` (`logistics_id`, `country_id`, `province_id`, `city_id`, `district_id`) USING BTREE COMMENT '同一区域唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '配送计费表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for logistics_department
-- ----------------------------
DROP TABLE IF EXISTS `logistics_department`;
CREATE TABLE `logistics_department`
(
    `id`            int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `logistics_id`  int(11) UNSIGNED NOT NULL COMMENT '配送方式ID',
    `department_id` int(11) UNSIGNED NOT NULL COMMENT '业务部门ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '配送方式业务部门关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for logistics_distributor
-- ----------------------------
DROP TABLE IF EXISTS `logistics_distributor`;
CREATE TABLE `logistics_distributor`
(
    `id`             int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `logistics_id`   int(11) UNSIGNED NOT NULL COMMENT '配送方式ID',
    `distributor_id` int(11) UNSIGNED NOT NULL COMMENT '分销商ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '配送方式分销商关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for logistics_distributor_grade
-- ----------------------------
DROP TABLE IF EXISTS `logistics_distributor_grade`;
CREATE TABLE `logistics_distributor_grade`
(
    `id`                   int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `logistics_id`         int(11) UNSIGNED NOT NULL COMMENT '配送方式ID',
    `distributor_grade_id` int(11) UNSIGNED NOT NULL COMMENT '分销商等级ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '配送方式分销商等级关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for logistics_third_mapping
-- ----------------------------
DROP TABLE IF EXISTS `logistics_third_mapping`;
CREATE TABLE `logistics_third_mapping`
(
    `id`                int(11)                                                 NOT NULL,
    `logistics_id`      int(11)                                                 NULL DEFAULT NULL COMMENT 'bat配送方式Id',
    `third_type`        smallint(6)                                             NULL DEFAULT NULL COMMENT '第三方类型 1、麦客',
    `status`            smallint(6)                                             NULL DEFAULT NULL COMMENT '状态 1、启用 0、禁用',
    `third_delivery_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '第三方配送编号',
    `remark`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '配送第三方关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logistics_user
-- ----------------------------
DROP TABLE IF EXISTS `logistics_user`;
CREATE TABLE `logistics_user`
(
    `id`           int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `logistics_id` int(11) UNSIGNED NOT NULL COMMENT '配送方式ID',
    `user_id`      int(11) UNSIGNED NOT NULL COMMENT '业务员ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '配送方式业务员关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`
(
    `id`             int(11)                                                      NOT NULL COMMENT '地域id',
    `region_name`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地域名',
    `parent_id`      int(11)                                                      NOT NULL COMMENT '父节点id ',
    `have_next`      smallint(6)                                                  NULL DEFAULT NULL COMMENT '是否还有下一级 1是  0否',
    `region_name_en` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '区域英文名称',
    `level`          smallint(6)                                                  NULL DEFAULT NULL COMMENT '区域级数 国家/省/市/区 /1/2/3',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `parent_id` (`parent_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '省市区表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for region_comparison
-- ----------------------------
DROP TABLE IF EXISTS `region_comparison`;
CREATE TABLE `region_comparison`
(
    `id`           int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `region_id`    int(11)                                                      NOT NULL COMMENT '地域id',
    `region_name`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地域名',
    `another_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对照地域名',
    `parent_id`    int(11)                                                      NOT NULL COMMENT '父节点id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `parent_id` (`parent_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '省市对照表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_banner
-- ----------------------------
DROP TABLE IF EXISTS `store_banner`;
CREATE TABLE `store_banner`
(
    `id`          int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '编号',
    `image_url`   varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '推广图片地址',
    `banner_url`  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '推广链接地址',
    `create_time` datetime(0)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `banner_area` smallint(6) UNSIGNED                                          NULL     DEFAULT 0 COMMENT '推广区域 0-国内 1 海外',
    `sort`        int(11)                                                       NULL     DEFAULT NULL COMMENT '排序值',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置首页推广数据表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_column
-- ----------------------------
DROP TABLE IF EXISTS `store_column`;
CREATE TABLE `store_column`
(
    `id`                int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '编号',
    `title`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
    `title_en`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '栏目英文标题',
    `banner_img`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '栏目banner图',
    `sort`              int(11)                                                       NOT NULL DEFAULT 0 COMMENT '排序号',
    `release_status`    smallint(6) UNSIGNED                                          NOT NULL DEFAULT 0 COMMENT '发布状态，0,未发布，1 发布 2 取消发布',
    `column_area`       smallint(6)                                                   NOT NULL DEFAULT 0 COMMENT '栏目使用区域 0-国内 1 海外 2 国内和海外',
    `distributor_scope` smallint(6)                                                   NOT NULL COMMENT '1全部分销商,2等级分销商,3事业部,4指定业务部门,5指定业务员,6指定分销商',
    `create_time`       datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`       datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置首页栏目表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_column_department
-- ----------------------------
DROP TABLE IF EXISTS `store_column_department`;
CREATE TABLE `store_column_department`
(
    `id`            int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `column_id`     int(10) UNSIGNED NOT NULL COMMENT '栏目ID',
    `department_id` int(10) UNSIGNED NOT NULL COMMENT '业务部门ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置栏目业务部门关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_column_distributor
-- ----------------------------
DROP TABLE IF EXISTS `store_column_distributor`;
CREATE TABLE `store_column_distributor`
(
    `id`             int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `column_id`      int(10) UNSIGNED NOT NULL COMMENT '栏目ID',
    `distributor_id` int(10) UNSIGNED NOT NULL COMMENT '分销商ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置栏目分销商关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_column_distributor_grade
-- ----------------------------
DROP TABLE IF EXISTS `store_column_distributor_grade`;
CREATE TABLE `store_column_distributor_grade`
(
    `id`                   int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `column_id`            int(10) UNSIGNED NOT NULL COMMENT '栏目ID',
    `distributor_grade_id` int(10) UNSIGNED NOT NULL COMMENT '分销商等级ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置栏目分销商等级关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_column_goods
-- ----------------------------
DROP TABLE IF EXISTS `store_column_goods`;
CREATE TABLE `store_column_goods`
(
    `id`        int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `column_id` int(11) UNSIGNED NOT NULL COMMENT '栏目编号',
    `goods_id`  int(11) UNSIGNED NOT NULL COMMENT '商品编号',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `column_id` (`column_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置栏目商品关联表(暂时不用)'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_column_user
-- ----------------------------
DROP TABLE IF EXISTS `store_column_user`;
CREATE TABLE `store_column_user`
(
    `id`        int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `column_id` int(10) UNSIGNED NOT NULL COMMENT '栏目ID',
    `user_id`   int(10) UNSIGNED NOT NULL COMMENT '业务员ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置栏目业务员关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_mobile
-- ----------------------------
DROP TABLE IF EXISTS `store_mobile`;
CREATE TABLE `store_mobile`
(
    `id`          int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `sort`        int(11)          NOT NULL COMMENT '排序值',
    `module_type` smallint(6)      NOT NULL COMMENT '模块类型 1轮播图 2图片模块 3商品推广模块 4商品列表模块',
    `status`      smallint(6)      NOT NULL COMMENT '0 隐藏 1显示',
    `create_time` datetime(0)      NULL DEFAULT NULL,
    `update_time` datetime(0)      NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '商店配置移动端首页设置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_mobile_goods
-- ----------------------------
DROP TABLE IF EXISTS `store_mobile_goods`;
CREATE TABLE `store_mobile_goods`
(
    `id`        int(11) NOT NULL,
    `mobile_id` int(11) NOT NULL COMMENT '移动端设置id',
    `goods_id`  int(11) NOT NULL COMMENT '商品id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '商店配置移动端首页设置商品关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_mobile_item
-- ----------------------------
DROP TABLE IF EXISTS `store_mobile_item`;
CREATE TABLE `store_mobile_item`
(
    `id`          int(11) UNSIGNED                                        NOT NULL AUTO_INCREMENT,
    `mobile_id`   int(11)                                                 NOT NULL,
    `image_url`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '轮播图时为轮播图url 图片模块为图片url 商品推广模块时为背景url',
    `jump_type`   smallint(6)                                             NULL DEFAULT 0 COMMENT '0 无链接 1 跳转商品 2跳转分类 3跳转其他页面 4跳转搜索结果页',
    `jump_params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '跳转目标（跳转参数）',
    `sub_sort`    int(11)                                                 NULL DEFAULT 0 COMMENT '子内容排序',
    `style_type`  smallint(6)                                             NULL DEFAULT 0 COMMENT '商品推广模块下：样式类型0 无效 1 3列商品 2 4列商品 3 多列商品',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '商店配置移动端首页设置详情表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for store_notice
-- ----------------------------
DROP TABLE IF EXISTS `store_notice`;
CREATE TABLE `store_notice`
(
    `id`              int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '编号',
    `title`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
    `content`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '内容',
    `attachment_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '附件名称',
    `attachment_url`  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '附件url',
    `release_area`    smallint(6) UNSIGNED                                          NOT NULL COMMENT '发布地区 0 国内 1国外',
    `release_status`  smallint(6) UNSIGNED                                          NOT NULL DEFAULT 0 COMMENT '发布状态，0,未发布，1 发布',
    `release_time`    datetime(0)                                                   NOT NULL COMMENT '发布时间',
    `cancel_time`     datetime(0)                                                   NULL     DEFAULT NULL COMMENT '取消发布时间',
    `create_time`     datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置首页公告表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_section
-- ----------------------------
DROP TABLE IF EXISTS `store_section`;
CREATE TABLE `store_section`
(
    `id`                   int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '编号',
    `title`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '标题',
    `title_en`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '板块英文标题',
    `sort`                 int(11)                                                       NOT NULL DEFAULT 0 COMMENT '排序号',
    `release_status`       smallint(6) UNSIGNED                                          NOT NULL DEFAULT 0 COMMENT '发布状态，0,未发布，1 发布',
    `image_url`            varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '推广图片地址',
    `extension_url`        varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '推广链接地址',
    `image_url_en`         varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '板块英文图片链接',
    `extension_url_en`     varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '板块英文图片链接',
    `style_type`           smallint(6)                                                   NULL     DEFAULT 1 COMMENT '1.样式1 2.样式2 3.样式3 4.样式4',
    `style_type_en`        smallint(6)                                                   NULL     DEFAULT 1 COMMENT '1.样式1 2.样式2 3.样式3 4.样式4',
    `section_area`         smallint(6)                                                   NOT NULL DEFAULT 0 COMMENT '板块使用区域 0-国内 1 海外 2 国内和海外',
    `style_url`            varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式图片1地址(国内1)',
    `style_extension_url`  varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式连接1地址',
    `style_url1`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式图片2地址(国内2)',
    `style_extension_url1` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式连接2地址',
    `style_url2`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式图片3地址(海外1)',
    `style_extension_url2` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式连接3地址',
    `style_url3`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式图片4地址(海外2)',
    `style_extension_url3` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '样式连接4地址',
    `create_time`          datetime(0)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time`          datetime(0)                                                   NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `section_order` (`sort`, `update_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '商店配置首页板块表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_section_goods
-- ----------------------------
DROP TABLE IF EXISTS `store_section_goods`;
CREATE TABLE `store_section_goods`
(
    `id`         int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
    `section_id` int(11) UNSIGNED NOT NULL COMMENT '板块编号',
    `goods_id`   int(11) UNSIGNED NOT NULL COMMENT '商品编号',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `section_id` (`section_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '板块商品关联表(暂时不用)'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`
(
    `id`                 int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT 'id',
    `parent_id`          int(11) UNSIGNED                                              NULL     DEFAULT NULL COMMENT '父id',
    `organization_id`    int(10) UNSIGNED                                              NULL     DEFAULT 0 COMMENT '组织id',
    `department_name`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '部门名称',
    `department_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '部门名称英文',
    `sort`               int(11) UNSIGNED                                              NULL     DEFAULT NULL COMMENT '排序值',
    `erp_department_id`  varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp系统中的销售部门id',
    `description`        varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '部门描述',
    `sale_type`          smallint(6) UNSIGNED                                          NULL     DEFAULT 0 COMMENT '是否销售部门，0否 1是 ',
    `status`             smallint(6) UNSIGNED                                          NULL     DEFAULT NULL COMMENT '0 禁用 1启用',
    `create_time`        datetime(0)                                                   NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time`        datetime(0)                                                   NULL     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_org_id_erp_dep_id` (`organization_id`, `erp_department_id`) USING BTREE COMMENT '组织部门唯一'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '销售部门信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`         int(11) UNSIGNED                                        NOT NULL AUTO_INCREMENT,
    `service`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务',
    `service_en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务英文',
    `module`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块（一级标题）',
    `module_en`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块英文',
    `menu`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单（二级标题）',
    `menu_en`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单英文(link)',
    `sort`       int(11)                                                 NULL DEFAULT NULL COMMENT '排序',
    `status`     smallint(6)                                             NULL DEFAULT NULL COMMENT '状态',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '菜单表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (1, '商品管理', 'goods', '商品管理', NULL, '商品列表', 'goodslist', 111, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (2, '系统管理', 'system', '组织架构', NULL, '销售组织', 'saleOrganization', 111, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (3, '系统管理', 'system', '组织架构', NULL, '销售部门', 'saleDepartment', 112, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (4, '商品管理', 'goods', '商品管理', NULL, '冻结商品列表', 'freezegoodslist', 112, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (5, '商品管理', 'goods', '商品管理', NULL, '商品销量管理', 'salesManage', 113, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (6, '商品管理', 'goods', '商品管理', NULL, '价格等级', 'distributorarea', 114, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (7, '商品管理', 'goods', '商品分类管理', NULL, '商品分类列表', 'categorylist', 121, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (8, '系统管理', 'system', '权限管理', NULL, '角色列表', 'rolelist', 121, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (9, '系统管理', 'system', '权限管理', NULL, '用户列表', 'userlist', 122, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (10, '商品管理', 'goods', '商品分类管理', NULL, '商品标签列表', 'goodslabellist', 122, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (11, '系统管理', 'system', '权限管理', NULL, '审批配置', 'approve', 123, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (12, '系统管理', 'system', '配送管理', NULL, '配送方式列表', 'managelist', 131, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (13, '商品管理', 'goods', '品牌品类管理', NULL, '品牌列表', 'brandlist', 131, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (14, '商品管理', 'goods', '品牌品类管理', NULL, '品类列表', 'productlinelist', 132, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (15, '系统管理', 'system', '系统日志', NULL, '系统操作日志', 'uLog', 141, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (16, '商品管理', 'goods', '商品属性管理', NULL, '商品属性列表', 'specificationlist', 141, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (17, '系统管理', 'system', '系统日志', NULL, '接口调用日志', 'interfaceLog', 142, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (18, '客户管理', 'distributor', '分销商设置', NULL, '销售区域', 'distributorlist', 211, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (19, '客户管理', 'distributor', '分销商设置', NULL, '分销商分组', 'distributorGrouping', 212, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (20, '客户管理', 'distributor', '分销商设置', NULL, '分销商类别', 'distributorClasses', 213, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (21, '客户管理', 'distributor', '分销商设置', NULL, '收款条件', 'paylist', 214, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (22, '客户管理', 'distributor', '一级分销商', NULL, '合作中的分销商列表', 'distributorcooperating', 221, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (23, '客户管理', 'distributor', '一级分销商', NULL, '分销商申请列表', 'Dapplication', 222, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (24, '客户管理', 'distributor', '一级分销商', NULL, '已冻结的分销商列表', 'Dcooperatingfreeze', 223, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (25, '客户管理', 'distributor', '多级分销商', NULL, '合作中的分销商列表', 'distributorcooperatingn', 231, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (26, '客户管理', 'distributor', '多级分销商', NULL, '已冻结的分销商列表', 'DcooperatingfreezeN', 232, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (27, '客户管理', 'distributor', '合作平台管理', NULL, '合作平台列表', 'sysPlatformList', 241, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (28, '客户管理', 'distributor', '合作平台管理', NULL, '自有平台列表', 'ownPlatformList', 242, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (29, '客户管理', 'distributor', '合作平台管理', NULL, '微信公众平台列表', 'wxPlatformList', 243, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (30, '客户管理', 'distributor', '用户管理', NULL, '柔性用户列表', 'customerlist', 251, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (31, '柔性管理', 'material', '图库管理', NULL, '图库分类列表', 'pictureCategoryList', 311, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (32, '柔性管理', 'material', '图库管理', NULL, '图片列表', 'pictureList', 312, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (33, '柔性管理', 'material', '标签管理', NULL, '标签列表', 'labelList', 321, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (34, '柔性管理', 'material', '字体管理', NULL, '字体列表', 'fontList', 331, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (35, '柔性管理', 'material', '定制商品管理', NULL, '定制商品材料列表', 'materialList', 341, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (36, '柔性管理', 'material', '定制商品管理', NULL, '定制商品型号列表', 'modelList', 342, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (37, '柔性管理', 'material', '定制商品管理', NULL, '材料型号关联列表', 'materialModelList', 343, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (38, '柔性管理', 'material', '定制商品管理', NULL, '第三方材质型号关联', 'thirdSkuList', 344, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (39, '柔性管理', 'material', '柔性店铺配置', NULL, '轮播图管理', 'rxBanner', 351, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (40, '柔性管理', 'material', '柔性店铺配置', NULL, '系列展示管理', 'rxCategory', 352, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (41, '柔性管理', 'material', '柔性店铺配置', NULL, '推荐管理', 'rxRecommend', 353, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (42, '柔性管理', 'material', '柔性店铺配置', NULL, '官方主题管理', 'rxTheme', 354, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (43, '柔性管理', 'material', '柔性店铺配置', NULL, '店铺管理', 'storeManage', 355, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (44, '柔性管理', 'material', '柔性店铺配置', NULL, '公告管理', 'rxNotice', 356, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (45, '柔性管理', 'material', '卡券兑换', NULL, '兑换活动列表', 'exchangeActivity', 361, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (46, '柔性管理', 'material', '卡券兑换', NULL, '兑换码列表', 'exchangeCode', 362, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (47, '柔性管理', 'material', '卡券兑换', NULL, '兑换数据列表', 'exchangeCodeOrder', 363, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (48, '柔性管理', 'material', '产品类型', NULL, '产品类型列表', 'productCategoryList', 371, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (49, '仓库管理', 'warehouse', '仓库库存', NULL, '商品库存列表', 'stockList', 411, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (50, '仓库管理', 'warehouse', '仓库库存', NULL, '预留明细', 'stockReservedList', 412, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (51, '仓库管理', 'warehouse', '仓库设置', NULL, '基本设置', 'warehouseTimeSet', 421, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (52, '仓库管理', 'warehouse', '仓库设置', NULL, '仓库列表', 'warehouseList', 422, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (53, '仓库管理', 'warehouse', '仓库设置', NULL, '仓库回收站', 'warehouseRecycle', 423, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (54, '订单管理', 'order', '订单管理', NULL, '订单列表', 'orderList', 511, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (55, '订单管理', 'order', '订单管理', NULL, '批量导入订单', 'batchChannel', 512, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (56, '订单管理', 'order', '订单管理', NULL, '分销订单', 'orderDistrList', 513, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (57, '订单管理', 'order', 'C端订单', NULL, '柔性定制订单', 'customerDiyOrderList', 521, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (58, '订单管理', 'order', 'C端订单', NULL, '柔性同步订单', 'customerSyncOrderList', 522, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (59, '订单管理', 'order', '异常订单', NULL, '同步ERP失败订单', 'syncERPFailList', 531, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (60, '订单管理', 'order', '异常订单', NULL, '同步工厂失败订单', 'syncFactoryFailList', 532, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (61, '订单管理', 'order', '异常订单', NULL, '长时间未发货订单', 'syncUndeliveredFailList', 533, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (62, '订单管理', 'order', '订单统计', NULL, '订单明细导出', 'orderDiscount', 541, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (63, '订单管理', 'order', '订单统计', NULL, 'VMI订单明细', 'orderVmi', 542, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (64, '订单管理', 'order', '单据管理', NULL, '发货单', 'deliverGoods', 551, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (65, '订单管理', 'order', '订单设置', NULL, '订单类型', 'orderSetting', 561, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (66, '财务管理', 'financial', '基本设置', NULL, '汇率设置', 'exchangeRate', 611, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (67, '财务管理', 'financial', '基本设置', NULL, '币别', 'currency', 612, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (68, '财务管理', 'financial', '平台帐户', NULL, '微信支付账户', 'wxPayList', 621, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (69, '财务管理', 'financial', '平台帐户', NULL, '支付宝支付账户', 'alipayList', 622, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (70, '财务管理', 'financial', '平台帐户', NULL, '线下转账账户', 'offlineList', 623, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (71, '财务管理', 'financial', '分销商账户', NULL, '微信支付账户列表', 'wxAccountList', 631, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (72, '财务管理', 'financial', '分销商账户', NULL, '支付宝账户列表', 'alipayAccountList', 632, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (73, '财务管理', 'financial', '预存款', NULL, '收款列表', 'PrechargeList', 641, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (74, '财务管理', 'financial', '预存款', NULL, '提现列表', 'drawing', 642, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (75, '财务管理', 'financial', '预存款', NULL, '冻结列表', 'frozen', 643, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (76, '财务管理', 'financial', '预存款明细', NULL, '明细账', 'prechargeDetail', 651, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (77, '财务管理', 'financial', '预存款明细', NULL, '余额表', 'balanceList', 652, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (78, '财务管理', 'financial', '销售往来单据', NULL, '收款单列表', 'receipt', 661, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (79, '财务管理', 'financial', '销售往来单据', NULL, '退款单列表', 'refundOrder', 662, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (80, '财务管理', 'financial', '销售往来单据', NULL, '退款申请单列表', 'refundApply', 663, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (81, '商店管理', 'storeLayout', '商店配置', NULL, '首页推广', 'homePagePromotion', 811, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (82, '商店管理', 'storeLayout', '商店配置', NULL, '首页公告', 'homePageAnnouncement', 812, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (83, '商店管理', 'storeLayout', '商店配置', NULL, '首页栏目', 'frontPageColumn', 813, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (84, '商店管理', 'storeLayout', '商店配置', NULL, '首页板块', 'frontPagePlate', 814, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (85, '商店管理', 'storeLayout', '商店配置', NULL, '移动端首页', 'mobile', 815, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (86, '商店管理', 'storeLayout', '全站设置', NULL, '购物设置', 'totalStationSet', 821, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (87, '商店管理', 'storeLayout', '全站设置', NULL, '工厂设置', 'factorySet', 822, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (88, '商店管理', 'storeLayout', '全站设置', NULL, '基本设置', 'baseSet', 823, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (89, '商店管理', 'storeLayout', '全站设置', NULL, '协议设置', 'agreementSet', 824, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (90, '商店管理', 'storeLayout', '培训中心管理', NULL, '培训中心分类列表', 'trainCategoryList', 831, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (91, '商店管理', 'storeLayout', '培训中心管理', NULL, '培训中心列表', 'trainList', 832, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (92, '商店管理', 'storeLayout', '下载中心管理', NULL, '下载中心分类列表', 'downCategoryList', 841, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (93, '商店管理', 'storeLayout', '下载中心管理', NULL, '下载中心列表', 'downList', 842, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (94, '营销管理', 'marketingPromotion', '促销活动', NULL, '促销活动列表', 'salesPromotion', 911, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (95, '营销管理', 'marketingPromotion', '促销活动', NULL, '批量活动导入', 'promotionImportList', 912, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (96, '营销管理', 'marketingPromotion', '优惠券', NULL, '优惠券列表', 'coupon', 921, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (97, '营销管理', 'marketingPromotion', '拼团', NULL, '拼团列表', 'groupBuy', 931, 1);
INSERT INTO `system_db`.`sys_menu`(`id`, `service`, `service_en`, `module`, `module_en`, `menu`, `menu_en`, `sort`,
                                   `status`)
VALUES (98, '营销管理', 'marketingPromotion', '拼团', NULL, '秒杀列表', 'seckill', 932, 1);


-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization`
(
    `id`                  int(11) UNSIGNED                                        NOT NULL AUTO_INCREMENT,
    `name`                varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组织名',
    `erp_organization_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'ERP_组织代码',
    `description`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
    `status`              smallint(6) UNSIGNED                                    NOT NULL COMMENT '0 禁用 1 启用 2删除(目前物理删除)',
    `create_time`         datetime(0)                                             NOT NULL COMMENT '创建时间',
    `update_time`         datetime(0)                                             NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_erp_organization_id
uk_erp_organization_id` (`erp_organization_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '销售组织表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`                int(11) UNSIGNED                                        NOT NULL AUTO_INCREMENT,
    `service`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务',
    `service_en`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务英文',
    `module`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块',
    `module_en`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块英文',
    `permission_name`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
    `permission_module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限英文名称',
    `url_path`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url路径',
    `method`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法',
    `sort`              int(11)                                                 NULL DEFAULT NULL COMMENT '排序',
    `status`            smallint(6)                                             NULL DEFAULT NULL COMMENT '状态',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_url_path_method` (`url_path`, `method`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 451
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '权限表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (1, '订单服务', NULL, '订单类型管理', NULL, '管理', 'orderType-manager', '/order/v1/web/admin/orderType', 'post', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (2, '订单服务', NULL, '订单类型管理', NULL, '查看', 'orderType-query', '/order/v1/web/admin/orderType', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (3, '订单服务', NULL, '订单类型管理', NULL, '管理', 'orderType-manager', '/order/v1/web/admin/orderType', 'delete', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (4, '订单服务', NULL, '订单类型管理', NULL, '管理', 'orderType-manager', '/order/v1/web/admin/orderType', 'put', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (5, '订单服务', NULL, '柔性定制订单列表管理', NULL, '查看', 'customerDiyOrder-query',
        '/order/v1/web/admin/customerDiyOrder/detail', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (6, '订单服务', NULL, '长时间未发货订单管理', NULL, '查看', 'syncUndeliveredFail-query',
        '/order/v1/web/admin/syncUndeliveredFail/export', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (7, '订单服务', NULL, 'VMI订单明细', NULL, '查看', 'vimOrderDetail-query', '/order/v1/web/admin/vimOrderDetail/list',
        'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (8, '订单服务', NULL, '订单库存管理', NULL, '查看', 'orderGoodsStock-query', '/order/v1/web/admin/orderGoodsStock/list',
        'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (9, '订单服务', NULL, '分销订单列表管理', NULL, '管理', 'distributionOrder-manager',
        '/order/v1/web/admin/distributionOrder/cancel', 'put', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (10, '订单服务', NULL, '同步erp失败订单管理', NULL, '查看', 'syncERPFail-query', '/order/v1/web/admin/syncERPFail/export',
        'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (11, '订单服务', NULL, '分销订单列表管理', NULL, '查看', 'distributionOrder-query',
        '/order/v1/web/admin/distributionOrder/promotion', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (12, '订单服务', NULL, '兑换卡订单管理', NULL, '查看', 'exchangeCard-query', '/order/v1/web/admin/exchangeCard/test', 'get',
        511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (13, '订单服务', NULL, '同步ERP订单列表管理', NULL, '查看', 'erpOrder-query', '/order/v1/web/admin/erpOrder/list', 'get', 511,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (14, '订单服务', NULL, '同步erp失败订单管理', NULL, '查看', 'syncERPFail-query', '/order/v1/web/admin/syncERPFail/list', 'get',
        511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (15, '订单服务', NULL, '分销订单列表管理', NULL, '查看', 'distributionOrder-query',
        '/order/v1/web/admin/distributionOrder/list', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (16, '订单服务', NULL, '同步erp失败订单管理', NULL, '查看', 'syncERPFail-query', '/order/v1/web/admin/syncERPFail/detail',
        'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (17, '订单服务', NULL, '发货单列表订单管理', NULL, '查看', 'orderDeliverBill-query',
        '/order/v1/web/admin/orderDeliverBill/detail', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (18, '订单服务', NULL, '发货单列表订单管理', NULL, '查看', 'orderDeliverBill-query',
        '/order/v1/web/admin/orderDeliverBill/logistics/view', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (19, '订单服务', NULL, '长时间未发货订单管理', NULL, '查看', 'syncUndeliveredFail-query',
        '/order/v1/web/admin/syncUndeliveredFail/list', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (20, '订单服务', NULL, '发货单列表订单管理', NULL, '查看', 'orderDeliverBill-query',
        '/order/v1/web/admin/orderDeliverBill/list', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (21, '订单服务', NULL, '订单类型管理', NULL, '查看', 'orderType-query', '/order/v1/web/admin/orderType/list', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (22, '订单服务', NULL, '同步ERP订单列表管理', NULL, '查看', 'erpOrder-query',
        '/order/v1/web/admin/erpOrder/po/getOrderIdByOrderNo', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (23, '订单服务', NULL, '同步工厂失败订单管理', NULL, '查看', 'syncFactoryFail-query', '/order/v1/web/admin/syncFactoryFail/list',
        'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (24, '订单服务', NULL, '同步工厂失败订单管理', NULL, '查看', 'syncFactoryFail-query',
        '/order/v1/web/admin/syncFactoryFail/detail', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (25, '订单服务', NULL, '分销订单列表管理', NULL, '查看', 'distributionOrder-query',
        '/order/v1/web/admin/distributionOrder/detail', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (26, '订单服务', NULL, 'VMI订单明细', NULL, '查看', 'vimOrderDetail-query', '/order/v1/web/admin/vimOrderDetail/detail',
        'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (27, '订单服务', NULL, '柔性定制订单列表管理', NULL, '查看', 'customerDiyOrder-query',
        '/order/v1/web/admin/customerDiyOrder/list', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (28, '订单服务', NULL, '同步工厂失败订单管理', NULL, '查看', 'syncFactoryFail-query',
        '/order/v1/web/admin/syncFactoryFail/export', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (29, '订单服务', NULL, '兑换卡订单管理', NULL, '查看', 'exchangeCard-query', '/order/v1/web/admin/exchangeCard/pageExchange',
        'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (30, '订单服务', NULL, '长时间未发货订单管理', NULL, '查看', 'syncUndeliveredFail-query',
        '/order/v1/web/admin/syncUndeliveredFail/detail', 'get', 511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (31, '订单服务', NULL, '同步ERP订单列表管理', NULL, '查看', 'erpOrder-query', '/order/v1/web/admin/erpOrder/detail', 'get',
        511, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (32, '系统模块', NULL, '后台用户管理', NULL, '查看', 'user-query', '/system/v1/web/admin/user/ids', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (33, '系统模块', NULL, '角色管理', NULL, '管理', 'role-manager', '/system/v1/web/admin/role', 'post', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (34, '系统模块', NULL, '角色管理', NULL, '查看', 'role-query', '/system/v1/web/admin/role', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (35, '系统模块', NULL, '角色管理', NULL, '管理', 'role-manager', '/system/v1/web/admin/role', 'delete', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (36, '系统模块', NULL, '角色管理', NULL, '管理', 'role-manager', '/system/v1/web/admin/role', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (37, '商店配置模块', NULL, '首页板块管理', NULL, '管理', 'section-manager', '/system/v1/web/admin/section', 'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (38, '商店配置模块', NULL, '首页板块管理', NULL, '查看', 'section-query', '/system/v1/web/admin/section', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (39, '商店配置模块', NULL, '首页板块管理', NULL, '管理', 'section-manager', '/system/v1/web/admin/section', 'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (40, '商店配置模块', NULL, '首页板块管理', NULL, '管理', 'section-manager', '/system/v1/web/admin/section', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (41, '系统模块', NULL, '区域管理', NULL, '查看', 'region-query', '/system/v1/web/admin/region/list/tree', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (42, '商店配置模块', NULL, '移动端首页管理', NULL, '查看', 'mobile-query', '/system/v1/web/admin/mobile/list', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (43, '系统模块', NULL, '审批配置管理', NULL, '管理', 'check-manager', '/system/v1/web/admin/check', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (44, '商店配置模块', NULL, '协议设置管理', NULL, '查看', 'agreementSetting-query',
        '/system/v1/web/admin/agreementSetting/list', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (45, '系统模块', NULL, '权限管理', NULL, '查看', 'permission-query', '/system/v1/web/admin/permission/po/list/tree', 'get',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (46, '系统模块', NULL, '后台用户管理', NULL, '查看', 'user-query', '/system/v1/web/admin/user/po/ids', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (47, '系统模块', NULL, '菜单管理', NULL, '管理', 'menu-manager', '/system/v1/web/admin/menu', 'post', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (48, '系统模块', NULL, '菜单管理', NULL, '查看', 'menu-query', '/system/v1/web/admin/menu', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (49, '系统模块', NULL, '菜单管理', NULL, '管理', 'menu-manager', '/system/v1/web/admin/menu', 'delete', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (50, '系统模块', NULL, '菜单管理', NULL, '管理', 'menu-manager', '/system/v1/web/admin/menu', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (51, '商店配置模块', NULL, '首页公告管理', NULL, '管理', 'notice-manager', '/system/v1/web/admin/notice/ids', 'delete', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (52, '系统模块', NULL, '后台用户管理', NULL, '查看', 'user-query', '/system/v1/web/admin/user/po/list', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (53, '商店配置模块', NULL, '基本设置管理', NULL, '查看', 'baseSetting-query', '/system/v1/web/admin/baseSetting', 'get', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (54, '商店配置模块', NULL, '基本设置管理', NULL, '管理', 'baseSetting-manager', '/system/v1/web/admin/baseSetting', 'put', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (55, '商店配置模块', NULL, '协议设置管理', NULL, '管理', 'agreementSetting-manager', '/system/v1/web/admin/agreementSetting',
        'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (56, '商店配置模块', NULL, '协议设置管理', NULL, '查看', 'agreementSetting-query', '/system/v1/web/admin/agreementSetting',
        'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (57, '商店配置模块', NULL, '协议设置管理', NULL, '管理', 'agreementSetting-manager', '/system/v1/web/admin/agreementSetting',
        'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (58, '商店配置模块', NULL, '协议设置管理', NULL, '管理', 'agreementSetting-manager', '/system/v1/web/admin/agreementSetting',
        'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (59, '商店配置模块', NULL, '首页推广管理', NULL, '查看', 'banner-query', '/system/v1/web/admin/banner/list', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (60, '商店配置模块', NULL, '首页公告管理', NULL, '管理', 'notice-manager', '/system/v1/web/admin/notice', 'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (61, '商店配置模块', NULL, '首页公告管理', NULL, '查看', 'notice-query', '/system/v1/web/admin/notice', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (62, '商店配置模块', NULL, '首页公告管理', NULL, '管理', 'notice-manager', '/system/v1/web/admin/notice', 'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (63, '商店配置模块', NULL, '首页公告管理', NULL, '管理', 'notice-manager', '/system/v1/web/admin/notice', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (64, '系统模块', NULL, '区域管理', NULL, '查看', 'region-query', '/system/v1/web/admin/region/list', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (65, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user/po/sync', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (66, '系统模块', NULL, '后台用户管理', NULL, '查看', 'user-query', '/system/v1/web/admin/user/po/rockAccountInfoList', 'get',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (67, '商店配置模块', NULL, '购物设置管理', NULL, '查看', 'shopSetting-query', '/system/v1/web/admin/shopSetting', 'get', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (68, '商店配置模块', NULL, '购物设置管理', NULL, '管理', 'shopSetting-manager', '/system/v1/web/admin/shopSetting', 'put', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (69, '商店配置模块', NULL, '下载中心管理', NULL, '管理', 'downloadCenter-manager', '/system/v1/web/admin/downloadCenter',
        'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (70, '商店配置模块', NULL, '下载中心管理', NULL, '查看', 'downloadCenter-query', '/system/v1/web/admin/downloadCenter', 'get',
        911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (71, '商店配置模块', NULL, '下载中心管理', NULL, '管理', 'downloadCenter-manager', '/system/v1/web/admin/downloadCenter',
        'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (72, '商店配置模块', NULL, '下载中心管理', NULL, '管理', 'downloadCenter-manager', '/system/v1/web/admin/downloadCenter',
        'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (73, '系统模块', NULL, '角色管理', NULL, '查看', 'role-query', '/system/v1/web/admin/role/list', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (74, '系统模块', NULL, '配送管理', NULL, '管理', 'logistics-manager', '/system/v1/web/admin/logistics', 'post', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (75, '系统模块', NULL, '配送管理', NULL, '查看', 'logistics-query', '/system/v1/web/admin/logistics', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (76, '系统模块', NULL, '配送管理', NULL, '管理', 'logistics-manager', '/system/v1/web/admin/logistics', 'delete', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (77, '系统模块', NULL, '配送管理', NULL, '管理', 'logistics-manager', '/system/v1/web/admin/logistics', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (78, '商店配置模块', NULL, '首页板块管理', NULL, '管理', 'section-manager', '/system/v1/web/admin/section/release', 'put', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (79, '商店配置模块', NULL, '首页栏目管理', NULL, '管理', 'column-manager', '/system/v1/web/admin/column/release', 'put', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (80, '系统模块', NULL, '组织管理', NULL, '管理', 'organization-manager', '/system/v1/web/admin/organization', 'post', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (81, '系统模块', NULL, '组织管理', NULL, '查看', 'organization-query', '/system/v1/web/admin/organization', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (82, '系统模块', NULL, '组织管理', NULL, '管理', 'organization-manager', '/system/v1/web/admin/organization', 'delete',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (83, '系统模块', NULL, '组织管理', NULL, '管理', 'organization-manager', '/system/v1/web/admin/organization', 'put', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (84, '商店配置模块', NULL, '工厂设置管理', NULL, '查看', 'factorySetting-query', '/system/v1/web/admin/factorySetting', 'get',
        911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (85, '商店配置模块', NULL, '工厂设置管理', NULL, '管理', 'factorySetting-manager', '/system/v1/web/admin/factorySetting',
        'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (86, '系统模块', NULL, '菜单管理', NULL, '查看', 'menu-query', '/system/v1/web/admin/menu/po/list/managerTree', 'get', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (87, '系统模块', NULL, '权限管理', NULL, '查看', 'permission-query', '/system/v1/web/admin/permission/list', 'get', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (88, '系统模块', NULL, '区域管理', NULL, '查看', 'region-query', '/system/v1/web/admin/region/ids', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (89, '系统模块', NULL, '审批配置管理', NULL, '查看', 'check-query', '/system/v1/web/admin/check/checkDetail', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (90, '系统模块', NULL, '区域管理', NULL, '查看', 'region-query', '/system/v1/web/admin/region', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (91, '商店配置模块', NULL, '下载中心管理', NULL, '查看', 'downloadCenter-query', '/system/v1/web/admin/downloadCenter/list',
        'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (92, '商店配置模块', NULL, '训练中心管理', NULL, '管理', 'trainingCenter-manager',
        '/system/v1/web/admin/trainingCenter/sort/down', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (93, '商店配置模块', NULL, '下载中心管理', NULL, '管理', 'downloadCenter-manager',
        '/system/v1/web/admin/downloadCenter/sort/down', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (94, '系统模块', NULL, '角色管理', NULL, '查看', 'role-query', '/system/v1/web/admin/role/ids', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (95, '系统模块', NULL, '后台用户管理', NULL, '查看', 'user-query', '/system/v1/web/admin/user/list', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (96, '系统模块', NULL, '配送管理', NULL, '管理', 'logistics-manager', '/system/v1/web/admin/logistics/formula', 'post',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (97, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user/status', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (98, '系统模块', NULL, '菜单管理', NULL, '查看', 'menu-query', '/system/v1/web/admin/menu/list', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (99, '系统模块', NULL, '审批配置管理', NULL, '查看', 'check-query', '/system/v1/web/admin/check/po/checkDetail', 'get', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (100, '商店配置模块', NULL, '移动端首页管理', NULL, '管理', 'mobile-manager', '/system/v1/web/admin/mobile', 'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (101, '商店配置模块', NULL, '移动端首页管理', NULL, '查看', 'mobile-query', '/system/v1/web/admin/mobile', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (102, '商店配置模块', NULL, '移动端首页管理', NULL, '管理', 'mobile-manager', '/system/v1/web/admin/mobile', 'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (103, '商店配置模块', NULL, '移动端首页管理', NULL, '管理', 'mobile-manager', '/system/v1/web/admin/mobile', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (104, '商店配置模块', NULL, '首页栏目管理', NULL, '管理', 'column-manager', '/system/v1/web/admin/column/ids', 'delete', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (105, '商店配置模块', NULL, '首页栏目管理', NULL, '管理', 'column-manager',
        '/system/v1/web/admin/column/clearanceGoodsStoreColumn', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (106, '商店配置模块', NULL, '首页板块管理', NULL, '管理', 'section-manager', '/system/v1/web/admin/section/ids', 'delete', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (107, '商店配置模块', NULL, '下载中心管理', NULL, '管理', 'downloadCenter-manager',
        '/system/v1/web/admin/downloadCenter/sort/up', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (108, '商店配置模块', NULL, '移动端首页管理', NULL, '管理', 'mobile-manager', '/system/v1/web/admin/mobile/item', 'delete', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (109, '系统模块', NULL, '组织管理', NULL, '查看', 'organization-query', '/system/v1/web/admin/organization/ids', 'get',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (110, '系统模块', NULL, '配送管理', NULL, '查看', 'logistics-query', '/system/v1/web/admin/logistics/list', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (111, '商店配置模块', NULL, '移动端首页管理', NULL, '管理', 'mobile-manager', '/system/v1/web/admin/mobile/releaseById', 'put',
        911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (112, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user/password', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (113, '商店配置模块', NULL, '基本设置管理', NULL, '查看', 'baseSetting-query', '/system/v1/web/admin/baseSetting/loginSetting',
        'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (114, '商店配置模块', NULL, '基本设置管理', NULL, '管理', 'baseSetting-manager',
        '/system/v1/web/admin/baseSetting/loginSetting', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (115, '商店配置模块', NULL, '首页推广管理', NULL, '管理', 'banner-manager', '/system/v1/web/admin/banner/ids', 'delete', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (116, '系统模块', NULL, '销售部门管理', NULL, '查看', 'department-query', '/system/v1/web/admin/department/list', 'get', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (117, '商店配置模块', NULL, '首页栏目管理', NULL, '管理', 'column-manager',
        '/system/v1/web/admin/column/clearGoodsStoreColumn', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (118, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user/login', 'post', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (119, '商店配置模块', NULL, '训练中心管理', NULL, '查看', 'trainingCenter-query', '/system/v1/web/admin/trainingCenter/list',
        'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (120, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user', 'post', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (121, '系统模块', NULL, '后台用户管理', NULL, '查看', 'user-query', '/system/v1/web/admin/user', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (122, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user', 'delete', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (123, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (124, '商店配置模块', NULL, '首页推广管理', NULL, '管理', 'banner-manager', '/system/v1/web/admin/banner', 'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (125, '商店配置模块', NULL, '首页推广管理', NULL, '管理', 'banner-manager', '/system/v1/web/admin/banner', 'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (126, '商店配置模块', NULL, '首页推广管理', NULL, '管理', 'banner-manager', '/system/v1/web/admin/banner', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (127, '商店配置模块', NULL, '首页推广管理', NULL, '管理', 'banner-manager', '/system/v1/web/admin/banner/sort/down', 'put',
        911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (128, '商店配置模块', NULL, '首页栏目管理', NULL, '管理', 'column-manager', '/system/v1/web/admin/column', 'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (129, '商店配置模块', NULL, '首页栏目管理', NULL, '查看', 'column-query', '/system/v1/web/admin/column', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (130, '商店配置模块', NULL, '首页栏目管理', NULL, '管理', 'column-manager', '/system/v1/web/admin/column', 'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (131, '商店配置模块', NULL, '首页栏目管理', NULL, '管理', 'column-manager', '/system/v1/web/admin/column', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (132, '系统模块', NULL, '组织管理', NULL, '查看', 'organization-query', '/system/v1/web/admin/organization/po/list', 'get',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (133, '商店配置模块', NULL, '首页栏目管理', NULL, '查看', 'column-query', '/system/v1/web/admin/column/list', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (134, '商店配置模块', NULL, '训练中心管理', NULL, '管理', 'trainingCenter-manager', '/system/v1/web/admin/trainingCenter',
        'post', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (135, '商店配置模块', NULL, '训练中心管理', NULL, '查看', 'trainingCenter-query', '/system/v1/web/admin/trainingCenter', 'get',
        911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (136, '商店配置模块', NULL, '训练中心管理', NULL, '管理', 'trainingCenter-manager', '/system/v1/web/admin/trainingCenter',
        'delete', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (137, '商店配置模块', NULL, '训练中心管理', NULL, '管理', 'trainingCenter-manager', '/system/v1/web/admin/trainingCenter',
        'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (138, '商店配置模块', NULL, '训练中心管理', NULL, '查看', 'trainingCenter-query',
        '/system/v1/web/admin/trainingCenter/list/menu', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (139, '系统模块', NULL, '销售部门管理', NULL, '管理', 'department-manager', '/system/v1/web/admin/department', 'post', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (140, '系统模块', NULL, '销售部门管理', NULL, '查看', 'department-query', '/system/v1/web/admin/department', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (141, '系统模块', NULL, '销售部门管理', NULL, '管理', 'department-manager', '/system/v1/web/admin/department', 'delete', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (142, '系统模块', NULL, '销售部门管理', NULL, '管理', 'department-manager', '/system/v1/web/admin/department', 'put', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (143, '系统模块', NULL, '菜单管理', NULL, '管理', 'menu-manager', '/system/v1/web/admin/menu/po/sync', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (144, '商店配置模块', NULL, '训练中心管理', NULL, '管理', 'trainingCenter-manager',
        '/system/v1/web/admin/trainingCenter/sort/up', 'put', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (145, '系统模块', NULL, '对象存储管理', NULL, '查看', 'oss-query', '/system/v1/web/admin/oss/sts', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (146, '系统模块', NULL, '组织管理', NULL, '查看', 'organization-query', '/system/v1/web/admin/organization/list', 'get',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (147, '商店配置模块', NULL, '首页板块管理', NULL, '查看', 'section-query', '/system/v1/web/admin/section/list', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (148, '商店配置模块', NULL, '下载中心管理', NULL, '查看', 'downloadCenter-query',
        '/system/v1/web/admin/downloadCenter/list/menu', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (149, '系统模块', NULL, '菜单管理', NULL, '查看', 'menu-query', '/system/v1/web/admin/menu/ids', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (150, '系统模块', NULL, '后台用户管理', NULL, '查看', 'user-query', '/system/v1/web/admin/user/po/list/salesman', 'get', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (151, '系统模块', NULL, '销售部门管理', NULL, '查看', 'department-query', '/system/v1/web/admin/department/po/list', 'get',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (152, '系统模块', NULL, '销售部门管理', NULL, '管理', 'department-manager', '/system/v1/web/admin/department/po/sync', 'put',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (153, '系统模块', NULL, '权限管理', NULL, '管理', 'permission-manager', '/system/v1/web/admin/permission/po/syncCache',
        'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (154, '系统模块', NULL, '后台用户管理', NULL, '管理', 'user-manager', '/system/v1/web/admin/user/logout', 'post', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (155, '系统模块', NULL, '权限管理', NULL, '管理', 'permission-manager', '/system/v1/web/admin/permission', 'post', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (156, '系统模块', NULL, '权限管理', NULL, '查看', 'permission-query', '/system/v1/web/admin/permission', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (157, '系统模块', NULL, '权限管理', NULL, '管理', 'permission-manager', '/system/v1/web/admin/permission', 'delete', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (158, '系统模块', NULL, '权限管理', NULL, '管理', 'permission-manager', '/system/v1/web/admin/permission', 'put', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (159, '商店配置模块', NULL, '移动端首页管理', NULL, '管理', 'mobile-manager', '/system/v1/web/admin/mobile/releaseByIds', 'put',
        911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (160, '系统模块', NULL, '销售部门管理', NULL, '查看', 'department-query', '/system/v1/web/admin/department/po/list/id',
        'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (161, '商店配置模块', NULL, '首页公告管理', NULL, '管理', 'notice-manager', '/system/v1/web/admin/notice/release', 'put', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (162, '系统模块', NULL, '区域管理', NULL, '查看', 'region-query', '/system/v1/web/admin/region/list/tree/old', 'get', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (163, '商店配置模块', NULL, '首页推广管理', NULL, '管理', 'banner-manager', '/system/v1/web/admin/banner/sort/up', 'put', 911,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (164, '系统模块', NULL, '配送管理', NULL, '查看', 'logistics-query', '/system/v1/web/admin/logistics/po/list', 'get', 811,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (165, '商店配置模块', NULL, '首页公告管理', NULL, '查看', 'notice-query', '/system/v1/web/admin/notice/list', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (166, '系统模块', NULL, '菜单管理', NULL, '查看', 'menu-query', '/system/v1/web/admin/menu/po/list/tree', 'get', 811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (167, '商店配置模块', NULL, '协议设置管理', NULL, '查看', 'agreementSetting-query',
        '/system/v1/web/admin/agreementSetting/checkBrand', 'get', 911, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (168, '系统模块', NULL, '权限管理', NULL, '管理', 'permission-manager', '/system/v1/web/admin/permission/po/sync', 'put',
        811, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (169, '第三方模块', NULL, '业务日志管理', NULL, '管理', 'orderBusinessLog-manager',
        '/thirdparty/v1/web/admin/orderBusinessLog/pushAgain', 'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (170, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '管理', 'order-manager',
        '/thirdparty/v1/web/admin/order/syncDiyDeliveryOrderToERP', 'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (171, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '查看', 'order-query', '/thirdparty/v1/web/admin/order/add', 'get', NULL,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (172, '第三方模块', NULL, '业务日志管理', NULL, '查看', 'orderBusinessLog-query',
        '/thirdparty/v1/web/admin/orderBusinessLog/pageSyncOrderLog', 'get', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (173, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '管理', 'order-manager', '/thirdparty/v1/web/admin/order/sync/expressNo',
        'post', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (174, '第三方模块', NULL, '业务日志管理', NULL, '查看', 'orderBusinessLog-query',
        '/thirdparty/v1/web/admin/orderBusinessLog/page', 'get', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (175, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '查看', 'order-query', '/thirdparty/v1/web/admin/order/test', 'get',
        NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (176, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '管理', 'order-manager',
        '/thirdparty/v1/web/admin/order/syncOrderToFactory', 'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (177, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '管理', 'order-manager', '/thirdparty/v1/web/admin/order/createLable',
        'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (178, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '管理', 'order-manager', '/thirdparty/v1/web/admin/order/syncOrderToERP',
        'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (179, '第三方模块', NULL, '业务日志管理', NULL, '管理', 'orderBusinessLog-manager',
        '/thirdparty/v1/web/admin/orderBusinessLog/updateAddress', 'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (180, '第三方模块', NULL, '订单触发第三方调用管理', NULL, '管理', 'order-manager',
        '/thirdparty/v1/web/admin/order/syncLogisticsToThird', 'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (181, '第三方模块', NULL, '财务触发第三方调用管理', NULL, '管理', 'financial-manager',
        '/thirdparty/v1/web/admin/financial/syncRefundBillToERP', 'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (182, '第三方模块', NULL, '第三方触发的分销商模块管理', NULL, '管理', 'distributor-manager',
        '/thirdparty/v1/web/admin/distributor/sync/distributor', 'post', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (183, '第三方模块', NULL, '财务触发第三方调用管理', NULL, '管理', 'financial-manager',
        '/thirdparty/v1/web/admin/financial/syncVouchersToERP', 'put', NULL, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (184, '柔性服务', NULL, '门店管理接口', NULL, '查看', 'shop-query', '/flexible/v1/web/admin/u/p/shop/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (185, '柔性服务', NULL, '三方sku关联后台管理接口', NULL, '查看', 'thirdSkuRelevance-query',
        '/flexible/v1/web/admin/u/p/thirdSkuRelevance/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (186, '柔性服务', NULL, '产品类型后台管理接口', NULL, '查看', 'productCategory-query',
        '/flexible/v1/web/admin/u/p/productCategory/detail', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (187, '柔性服务', NULL, '图片后台管理接口', NULL, '管理', 'picture-manager', '/flexible/v1/web/admin/u/p/picture/upOrDown',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (188, '柔性服务', NULL, '型号后台管理接口', NULL, '管理', 'model-manager', '/flexible/v1/web/admin/u/p/model/updateOpenFlag',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (189, '柔性服务', NULL, '主题系列展示管理', NULL, '查看', 'distributorSeriesTheme-query',
        '/flexible/v1/web/admin/u/p/distributorSeriesTheme/listSimpleByCondition', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (190, '柔性服务', NULL, '门店管理接口', NULL, '管理', 'importShop-manager',
        '/flexible/v1/web/admin/u/p/importShop/tempDownLoad', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (191, '柔性服务', NULL, '首页推荐管理接口', NULL, '查看', 'distributorIndexRecommend-query',
        '/flexible/v1/web/admin/u/p/distributorIndexRecommend/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (192, '柔性服务', NULL, '材质管理', NULL, '管理', 'material-manager', '/flexible/v1/web/admin/u/p/material', 'post', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (193, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/p/material', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (194, '柔性服务', NULL, '材质管理', NULL, '管理', 'material-manager', '/flexible/v1/web/admin/u/p/material', 'delete', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (195, '柔性服务', NULL, '材质管理', NULL, '管理', 'material-manager', '/flexible/v1/web/admin/u/p/material', 'put', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (196, '柔性服务', NULL, '型号后台管理接口', NULL, '查看', 'test-query', '/flexible/v1/web/admin/u/test', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (197, '柔性服务', NULL, '产品类型后台管理接口', NULL, '查看', 'productCategory-query',
        '/flexible/v1/web/admin/u/po/productCategory/listUsable', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (198, '柔性服务', NULL, '型号材质关联后台管理接口', NULL, '管理', 'modelMaterialRelevance-manager',
        '/flexible/v1/web/admin/u/p/modelMaterialRelevance', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (199, '柔性服务', NULL, '型号材质关联后台管理接口', NULL, '管理', 'modelMaterialRelevance-manager',
        '/flexible/v1/web/admin/u/p/modelMaterialRelevance', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (200, '柔性服务', NULL, '门店管理接口', NULL, '管理', 'shop-manager', '/flexible/v1/web/admin/u/p/shop', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (201, '柔性服务', NULL, '门店管理接口', NULL, '管理', 'shop-manager', '/flexible/v1/web/admin/u/p/shop', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (202, '柔性服务', NULL, '门店管理接口', NULL, '管理', 'shop-manager', '/flexible/v1/web/admin/u/p/shop', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (203, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/p/material/listWithoutTree',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (204, '柔性服务', NULL, '主题系列展示管理', NULL, '查看', 'distributorSeriesTheme-query',
        '/flexible/v1/web/admin/u/p/distributorSeriesTheme/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (205, '柔性服务', NULL, '兑换码后端管理', NULL, '查看', 'exchangeCode-query', '/flexible/v1/web/admin/u/p/exchangeCode/page',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (206, '柔性服务', NULL, '型号材质关联后台管理接口', NULL, '管理', 'modelMaterialRelevance-manager',
        '/flexible/v1/web/admin/u/p/modelMaterialRelevance/updateOpenFlag', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (207, '柔性服务', NULL, '图片分类后台管理接口', NULL, '查看', 'pictureCategory-query',
        '/flexible/v1/web/admin/u/po/pictureCategory/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (208, '柔性服务', NULL, '柔性合作的分销商后台管理接口', NULL, '查看', 'flexibleDistributorCooperation-query',
        '/flexible/v1/web/admin/u/p/flexibleDistributorCooperation/listUsable', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (209, '柔性服务', NULL, '兑换码后端管理', NULL, '管理', 'exchangeCode-manager',
        '/flexible/v1/web/admin/u/p/exchangeCode/invalidBatch', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (210, '柔性服务', NULL, '字体管理', NULL, '管理', 'font-manager', '/flexible/v1/web/admin/u/p/font/updateOpenFlag', 'put',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (211, '柔性服务', NULL, '标签后台管理接口', NULL, '管理', 'label-manager', '/flexible/v1/web/admin/u/p/label', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (212, '柔性服务', NULL, '标签后台管理接口', NULL, '查看', 'label-query', '/flexible/v1/web/admin/u/p/label', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (213, '柔性服务', NULL, '标签后台管理接口', NULL, '管理', 'label-manager', '/flexible/v1/web/admin/u/p/label', 'delete', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (214, '柔性服务', NULL, '标签后台管理接口', NULL, '管理', 'label-manager', '/flexible/v1/web/admin/u/p/label', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (215, '柔性服务', NULL, '产品类型后台管理接口', NULL, '查看', 'productCategory-query',
        '/flexible/v1/web/admin/u/p/productCategory/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (216, '柔性服务', NULL, '主题系列展示管理', NULL, '查看', 'distributorSeriesTheme-query',
        '/flexible/v1/web/admin/u/p/distributorSeriesTheme/list', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (217, '柔性服务', NULL, '首页推荐图片关联接口', NULL, '管理', 'indexRecommendPictureRelevance-manager',
        '/flexible/v1/web/admin/u/p/indexRecommendPictureRelevance/delete', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (218, '柔性服务', NULL, '门店管理接口', NULL, '管理', 'shop-manager', '/flexible/v1/web/admin/u/p/shop/export', 'post', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (219, '柔性服务', NULL, '兑换卡后台管理管理', NULL, '管理', 'exchangeCard-manager',
        '/flexible/v1/web/admin/u/p/exchangeCard/qrCode', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (220, '柔性服务', NULL, '材质管理', NULL, '管理', 'material-manager', '/flexible/v1/web/admin/u/p/material/updateOpenFlag',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (221, '柔性服务', NULL, '型号后台管理接口', NULL, '查看', 'model-query', '/flexible/v1/web/admin/u/po/model/page', 'get', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (222, '柔性服务', NULL, 'exchange-code-order-controller', NULL, '管理', 'exchangeCodeOrder-manager',
        '/flexible/v1/web/admin/u/p/exchangeCodeOrder/export', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (223, '柔性服务', NULL, '分销商banner后台管理接口', NULL, '管理', 'distributorBanner-manager',
        '/flexible/v1/web/admin/u/p/distributorBanner/batchDelete', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (224, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/p/material/page', 'get', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (225, '柔性服务', NULL, '主题系列图片关联管理接口', NULL, '管理', 'seriesPictureRelevance-manager',
        '/flexible/v1/web/admin/u/p/seriesPictureRelevance/batchDelete', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (226, '柔性服务', NULL, '兑换卡通知管理', NULL, '管理', 'exchangeNotice-manager', '/flexible/v1/web/admin/u/p/exchangeNotice',
        'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (227, '柔性服务', NULL, '兑换卡通知管理', NULL, '查看', 'exchangeNotice-query', '/flexible/v1/web/admin/u/p/exchangeNotice',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (228, '柔性服务', NULL, '兑换卡通知管理', NULL, '管理', 'exchangeNotice-manager', '/flexible/v1/web/admin/u/p/exchangeNotice',
        'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (229, '柔性服务', NULL, '兑换卡通知管理', NULL, '管理', 'exchangeNotice-manager', '/flexible/v1/web/admin/u/p/exchangeNotice',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (230, '柔性服务', NULL, '兑换卡后台管理管理', NULL, '管理', 'exchangeCard-manager', '/flexible/v1/web/admin/u/p/exchangeCard',
        'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (231, '柔性服务', NULL, '兑换卡后台管理管理', NULL, '管理', 'exchangeCard-manager', '/flexible/v1/web/admin/u/p/exchangeCard',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (232, '柔性服务', NULL, '型号后台管理接口', NULL, '管理', 'model-manager', '/flexible/v1/web/admin/u/p/model', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (233, '柔性服务', NULL, '型号后台管理接口', NULL, '查看', 'model-query', '/flexible/v1/web/admin/u/p/model', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (234, '柔性服务', NULL, '型号后台管理接口', NULL, '管理', 'model-manager', '/flexible/v1/web/admin/u/p/model', 'delete', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (235, '柔性服务', NULL, '型号后台管理接口', NULL, '管理', 'model-manager', '/flexible/v1/web/admin/u/p/model', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (236, '柔性服务', NULL, '兑换卡后台管理管理', NULL, '查看', 'exchangeCard-query',
        '/flexible/v1/web/admin/u/p/exchangeCard/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (237, '柔性服务', NULL, '分销商banner后台管理接口', NULL, '管理', 'distributorBanner-manager',
        '/flexible/v1/web/admin/u/p/distributorBanner/deleteById', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (238, '柔性服务', NULL, '标签后台管理接口', NULL, '管理', 'label-manager', '/flexible/v1/web/admin/u/p/label/upOpenFlag',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (239, '柔性服务', NULL, '主题系列展示管理', NULL, '管理', 'distributorSeriesTheme-manager',
        '/flexible/v1/web/admin/u/p/distributorSeriesTheme', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (240, '柔性服务', NULL, '主题系列展示管理', NULL, '管理', 'distributorSeriesTheme-manager',
        '/flexible/v1/web/admin/u/p/distributorSeriesTheme', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (241, '柔性服务', NULL, '主题系列展示管理', NULL, '管理', 'distributorSeriesTheme-manager',
        '/flexible/v1/web/admin/u/p/distributorSeriesTheme', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (242, '柔性服务', NULL, '兑换码后端管理', NULL, '管理', 'exchangeCode-manager',
        '/flexible/v1/web/admin/u/p/exchangeCode/tempDownLoad', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (243, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/po/material/listWithoutTree',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (244, '柔性服务', NULL, '兑换码后端管理', NULL, '管理', 'exchangeCode-manager',
        '/flexible/v1/web/admin/u/p/exchangeCode/export', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (245, '柔性服务', NULL, '官方主题管理', NULL, '管理', 'pictureTheme-manager', '/flexible/v1/web/admin/u/pictureTheme',
        'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (246, '柔性服务', NULL, '官方主题管理', NULL, '查看', 'pictureTheme-query', '/flexible/v1/web/admin/u/pictureTheme', 'get',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (247, '柔性服务', NULL, '官方主题管理', NULL, '管理', 'pictureTheme-manager', '/flexible/v1/web/admin/u/pictureTheme',
        'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (248, '柔性服务', NULL, '官方主题管理', NULL, '管理', 'pictureTheme-manager', '/flexible/v1/web/admin/u/pictureTheme', 'put',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (249, '柔性服务', NULL, '兑换卡后台管理管理', NULL, '查看', 'exchangeCard-query',
        '/flexible/v1/web/admin/u/p/exchangeCard/detail', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (250, '柔性服务', NULL, '图片后台管理接口', NULL, '查看', 'picture-query',
        '/flexible/v1/web/admin/u/p/picture/pageByDistributor', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (251, '柔性服务', NULL, '三方sku关联后台管理接口', NULL, '管理', 'thirdSkuRelevance-manager',
        '/flexible/v1/web/admin/u/p/thirdSkuRelevance/batch', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (252, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/po/material/treeAdmin',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (253, '柔性服务', NULL, '首页推荐管理接口', NULL, '管理', 'distributorIndexRecommend-manager',
        '/flexible/v1/web/admin/u/p/distributorIndexRecommend', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (254, '柔性服务', NULL, '首页推荐管理接口', NULL, '管理', 'distributorIndexRecommend-manager',
        '/flexible/v1/web/admin/u/p/distributorIndexRecommend', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (255, '柔性服务', NULL, '兑换码后端管理', NULL, '管理', 'exchangeCode-manager',
        '/flexible/v1/web/admin/u/p/exchangeCode/import', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (256, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/p/material/treeAdmin', 'get',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (257, '柔性服务', NULL, '图片后台管理接口', NULL, '管理', 'picture-manager', '/flexible/v1/web/admin/u/p/picture/top', 'put',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (258, '柔性服务', NULL, '兑换码后端管理', NULL, '管理', 'exchangeCode-manager',
        '/flexible/v1/web/admin/u/p/exchangeCode/createCode', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (259, '柔性服务', NULL, '主题系列图片关联管理接口', NULL, '管理', 'seriesPictureRelevance-manager',
        '/flexible/v1/web/admin/u/p/seriesPictureRelevance/upOrDown', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (260, '柔性服务', NULL, '三方sku关联后台管理接口', NULL, '管理', 'thirdSkuRelevance-manager',
        '/flexible/v1/web/admin/u/p/thirdSkuRelevance/updateOpenFlag', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (261, '柔性服务', NULL, '图片分类后台管理接口', NULL, '查看', 'pictureCategory-query',
        '/flexible/v1/web/admin/u/p/pictureCategory/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (262, '柔性服务', NULL, '兑换码后端管理', NULL, '查看', 'exchangeCode-query',
        '/flexible/v1/web/admin/u/p/exchangeCode/pageEncode', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (263, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/p/material/pageWithoutTree',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (264, '柔性服务', NULL, '分销商banner后台管理接口', NULL, '查看', 'distributorBanner-query',
        '/flexible/v1/web/admin/u/p/distributorBanner/detail', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (265, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/po/material/page', 'get',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (266, '柔性服务', NULL, '型号后台管理接口', NULL, '管理', 'model-manager', '/flexible/v1/web/admin/u/p/model/upOrDown', 'put',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (267, '柔性服务', NULL, '图片后台管理接口', NULL, '查看', 'picture-query', '/flexible/v1/web/admin/u/po/picture/page', 'get',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (268, '柔性服务', NULL, '柔性合作的分销商后台管理接口', NULL, '管理', 'flexibleDistributorCooperation-manager',
        '/flexible/v1/web/admin/u/p/flexibleDistributorCooperation/updateOpenFlag', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (269, '柔性服务', NULL, '分销商banner后台管理接口', NULL, '查看', 'distributorBanner-query',
        '/flexible/v1/web/admin/u/p/distributorBanner/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (270, '柔性服务', NULL, 'exchange-code-order-controller', NULL, '查看', 'exchangeCodeOrder-query',
        '/flexible/v1/web/admin/u/p/exchangeCodeOrder/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (271, '柔性服务', NULL, '图片分类后台管理接口', NULL, '管理', 'pictureCategory-manager',
        '/flexible/v1/web/admin/u/p/pictureCategory', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (272, '柔性服务', NULL, '图片分类后台管理接口', NULL, '查看', 'pictureCategory-query',
        '/flexible/v1/web/admin/u/p/pictureCategory', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (273, '柔性服务', NULL, '图片分类后台管理接口', NULL, '管理', 'pictureCategory-manager',
        '/flexible/v1/web/admin/u/p/pictureCategory', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (274, '柔性服务', NULL, '图片分类后台管理接口', NULL, '管理', 'pictureCategory-manager',
        '/flexible/v1/web/admin/u/p/pictureCategory', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (275, '柔性服务', NULL, '首页推荐图片关联接口', NULL, '管理', 'indexRecommendPictureRelevance-manager',
        '/flexible/v1/web/admin/u/p/indexRecommendPictureRelevance/batchDelete', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (276, '柔性服务', NULL, '兑换码后端管理', NULL, '管理', 'exchangeCode-manager',
        '/flexible/v1/web/admin/u/p/exchangeCode/invalid', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (277, '柔性服务', NULL, '产品类型后台管理接口', NULL, '查看', 'productCategory-query',
        '/flexible/v1/web/admin/u/po/productCategory/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (278, '柔性服务', NULL, '三方sku关联后台管理接口', NULL, '管理', 'thirdSkuRelevance-manager',
        '/flexible/v1/web/admin/u/p/thirdSkuRelevance/tempDownLoad', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (279, '柔性服务', NULL, '图片后台管理接口', NULL, '查看', 'picture-query',
        '/flexible/v1/web/admin/u/po/picture/pageByDistributor', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (280, '柔性服务', NULL, '型号后台管理接口', NULL, '查看', 'model-query', '/flexible/v1/web/admin/u/p/model/page', 'get', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (281, '柔性服务', NULL, '图片后台管理接口', NULL, '查看', 'picture-query', '/flexible/v1/web/admin/u/p/picture/page', 'get',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (282, '柔性服务', NULL, '柔性合作的分销商后台管理接口', NULL, '查看', 'flexibleDistributorCooperation-query',
        '/flexible/v1/web/admin/u/po/flexibleDistributorCooperation/listUsable', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (283, '柔性服务', NULL, '柔性合作的分销商后台管理接口', NULL, '管理', 'flexibleDistributorCooperation-manager',
        '/flexible/v1/web/admin/u/p/flexibleDistributorCooperation', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (284, '柔性服务', NULL, '柔性合作的分销商后台管理接口', NULL, '管理', 'flexibleDistributorCooperation-manager',
        '/flexible/v1/web/admin/u/p/flexibleDistributorCooperation', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (285, '柔性服务', NULL, '柔性合作的分销商后台管理接口', NULL, '管理', 'flexibleDistributorCooperation-manager',
        '/flexible/v1/web/admin/u/p/flexibleDistributorCooperation', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (286, '柔性服务', NULL, '图片分类后台管理接口', NULL, '管理', 'pictureCategory-manager',
        '/flexible/v1/web/admin/u/p/pictureCategory/upOrDown', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (287, '柔性服务', NULL, '字体管理', NULL, '管理', 'font-manager', '/flexible/v1/web/admin/u/p/font', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (288, '柔性服务', NULL, '字体管理', NULL, '查看', 'font-query', '/flexible/v1/web/admin/u/p/font', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (289, '柔性服务', NULL, '字体管理', NULL, '管理', 'font-manager', '/flexible/v1/web/admin/u/p/font', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (290, '柔性服务', NULL, '字体管理', NULL, '管理', 'font-manager', '/flexible/v1/web/admin/u/p/font', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (291, '柔性服务', NULL, '字体管理', NULL, '查看', 'font-query', '/flexible/v1/web/admin/u/p/font/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (292, '柔性服务', NULL, '分销商banner后台管理接口', NULL, '管理', 'distributorBanner-manager',
        '/flexible/v1/web/admin/u/p/distributorBanner', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (293, '柔性服务', NULL, '分销商banner后台管理接口', NULL, '管理', 'distributorBanner-manager',
        '/flexible/v1/web/admin/u/p/distributorBanner', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (294, '柔性服务', NULL, '字体管理', NULL, '管理', 'font-manager', '/flexible/v1/web/admin/u/p/font/upOrDown', 'put', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (295, '柔性服务', NULL, '主题系列图片关联管理接口', NULL, '管理', 'seriesPictureRelevance-manager',
        '/flexible/v1/web/admin/u/p/seriesPictureRelevance/deleteById', 'delete', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (296, '柔性服务', NULL, '图片后台管理接口', NULL, '管理', 'picture-manager',
        '/flexible/v1/web/admin/u/p/picture/updateOpenFlag', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (297, '柔性服务', NULL, '门店管理接口', NULL, '管理', 'shop-manager', '/flexible/v1/web/admin/u/p/shop/updateOpenFlag',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (298, '柔性服务', NULL, '柔性合作的分销商后台管理接口', NULL, '查看', 'flexibleDistributorCooperation-query',
        '/flexible/v1/web/admin/u/p/flexibleDistributorCooperation/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (299, '柔性服务', NULL, '标签后台管理接口', NULL, '查看', 'label-query', '/flexible/v1/web/admin/u/p/label/preview', 'get',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (300, '柔性服务', NULL, '图片分类后台管理接口', NULL, '管理', 'pictureCategory-manager',
        '/flexible/v1/web/admin/u/p/pictureCategory/updateOpenFlag', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (301, '柔性服务', NULL, '材质管理', NULL, '管理', 'material-manager', '/flexible/v1/web/admin/u/p/material/upOrDown',
        'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (302, '柔性服务', NULL, '型号后台管理接口', NULL, '管理', 'model-manager', '/flexible/v1/web/admin/u/p/model/top', 'put', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (303, '柔性服务', NULL, '型号后台管理接口', NULL, '查看', 'model-query',
        '/flexible/v1/web/admin/u/po/model/pageSimpleCOByCondition', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (304, '柔性服务', NULL, '标签后台管理接口', NULL, '查看', 'label-query', '/flexible/v1/web/admin/u/p/label/page', 'get', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (305, '柔性服务', NULL, '门店管理接口', NULL, '管理', 'shop-manager', '/flexible/v1/web/admin/u/p/shop/import', 'post', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (306, '柔性服务', NULL, '兑换卡通知管理', NULL, '查看', 'exchangeNotice-query',
        '/flexible/v1/web/admin/u/p/exchangeNotice/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (307, '柔性服务', NULL, '门店管理接口', NULL, '查看', 'shop-query', '/flexible/v1/web/admin/u/p/shop/detailById', 'get', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (308, '柔性服务', NULL, '首页推荐图片关联接口', NULL, '管理', 'indexRecommendPictureRelevance-manager',
        '/flexible/v1/web/admin/u/p/indexRecommendPictureRelevance/upOrDown', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (309, '柔性服务', NULL, '图片后台管理接口', NULL, '管理', 'picture-manager', '/flexible/v1/web/admin/u/p/picture', 'post', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (310, '柔性服务', NULL, '图片后台管理接口', NULL, '查看', 'picture-query', '/flexible/v1/web/admin/u/p/picture', 'get', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (311, '柔性服务', NULL, '图片后台管理接口', NULL, '管理', 'picture-manager', '/flexible/v1/web/admin/u/p/picture', 'delete',
        311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (312, '柔性服务', NULL, '图片后台管理接口', NULL, '管理', 'picture-manager', '/flexible/v1/web/admin/u/p/picture', 'put', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (313, '柔性服务', NULL, '兑换卡后台管理管理', NULL, '管理', 'exchangeCard-manager',
        '/flexible/v1/web/admin/u/p/exchangeCard/updateStatus', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (314, '柔性服务', NULL, '兑换卡关联的通用版货品池管理', NULL, '查看', 'exchangeGeneralItemPool-query',
        '/flexible/v1/web/admin/u/po/exchangeGeneralItemPool/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (315, '柔性服务', NULL, '型号材质关联后台管理接口', NULL, '管理', 'export-manager', '/flexible/v1/web/admin/u/export', 'post', 311,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (316, '柔性服务', NULL, '型号后台管理接口', NULL, '查看', 'model-query',
        '/flexible/v1/web/admin/u/p/model/pageSimpleCOByCondition', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (317, '柔性服务', NULL, '官方主题管理', NULL, '查看', 'pictureTheme-query', '/flexible/v1/web/admin/u/pictureTheme/page',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (318, '柔性服务', NULL, '三方sku关联后台管理接口', NULL, '管理', 'thirdSkuRelevance-manager',
        '/flexible/v1/web/admin/u/p/thirdSkuRelevance/import', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (319, '柔性服务', NULL, '官方主题管理', NULL, '管理', 'pictureTheme-manager',
        '/flexible/v1/web/admin/u/pictureTheme/relation', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (320, '柔性服务', NULL, '兑换卡关联的通用版货品池管理', NULL, '查看', 'exchangeGeneralItemPool-query',
        '/flexible/v1/web/admin/u/p/exchangeGeneralItemPool/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (321, '柔性服务', NULL, '型号材质关联后台管理接口', NULL, '查看', 'modelMaterialRelevance-query',
        '/flexible/v1/web/admin/u/p/modelMaterialRelevance/page', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (322, '柔性服务', NULL, '首页推荐管理接口', NULL, '查看', 'distributorIndexRecommend-query',
        '/flexible/v1/web/admin/u/p/distributorIndexRecommend/detail', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (323, '柔性服务', NULL, '产品类型后台管理接口', NULL, '管理', 'productCategory-manager',
        '/flexible/v1/web/admin/u/p/productCategory', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (324, '柔性服务', NULL, '产品类型后台管理接口', NULL, '管理', 'productCategory-manager',
        '/flexible/v1/web/admin/u/p/productCategory', 'put', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (325, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/p/material/listLowest',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (326, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/po/material/listLowest',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (327, '柔性服务', NULL, '三方sku关联后台管理接口', NULL, '管理', 'thirdSkuRelevance-manager',
        '/flexible/v1/web/admin/u/p/thirdSkuRelevance/export', 'post', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (328, '柔性服务', NULL, '材质管理', NULL, '查看', 'material-query', '/flexible/v1/web/admin/u/po/material/pageWithoutTree',
        'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (329, '柔性服务', NULL, '主题系列展示管理', NULL, '查看', 'distributorSeriesTheme-query',
        '/flexible/v1/web/admin/u/p/distributorSeriesTheme/detail', 'get', 311, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (330, '仓库服务', NULL, '货品库存接口', NULL, '管理', 'warehouse-manager',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/syncStock', 'put', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (331, '仓库服务', NULL, '库存预留接口', NULL, '管理', 'warehouse-manager',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/reserved/release', 'put', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (332, '仓库服务', NULL, '货品库存接口', NULL, '查看', 'warehouse-query', '/warehouse/v1/web/admin/u/po/warehouse/stock/page',
        'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (333, '仓库服务', NULL, '仓库管理', NULL, '查看', 'warehouse-query', '/warehouse/v1/web/admin/u/po/warehouse/page', 'get',
        411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (334, '仓库服务', NULL, '货品库存接口', NULL, '查看', 'warehouse-query',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/initStock', 'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (335, '仓库服务', NULL, '仓库设置管理', NULL, '查看', 'warehouseSetting-query',
        '/warehouse/v1/web/admin/u/p/warehouseSetting/list', 'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (336, '仓库服务', NULL, '仓库管理', NULL, '查看', 'warehouse-query', '/warehouse/v1/web/admin/u/p/warehouse/page', 'get',
        411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (337, '仓库服务', NULL, '仓库管理', NULL, '管理', 'warehouse-manager', '/warehouse/v1/web/admin/u/p/warehouse/upOrDown',
        'put', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (338, '仓库服务', NULL, '货品库存接口', NULL, '查看', 'warehouse-query',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/import', 'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (339, '仓库服务', NULL, '仓库管理', NULL, '管理', 'warehouse-manager', '/warehouse/v1/web/admin/u/p/warehouse', 'post',
        411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (340, '仓库服务', NULL, '仓库管理', NULL, '查看', 'warehouse-query', '/warehouse/v1/web/admin/u/p/warehouse', 'get', 411,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (341, '仓库服务', NULL, '仓库管理', NULL, '管理', 'warehouse-manager', '/warehouse/v1/web/admin/u/p/warehouse', 'delete',
        411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (342, '仓库服务', NULL, '仓库管理', NULL, '管理', 'warehouse-manager', '/warehouse/v1/web/admin/u/p/warehouse', 'put', 411,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (343, '仓库服务', NULL, '货品库存接口', NULL, '查看', 'warehouse-query', '/warehouse/v1/web/admin/u/p/warehouse/stock/test',
        'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (344, '仓库服务', NULL, '库存预留接口', NULL, '查看', 'warehouse-query',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/reserved/page', 'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (345, '仓库服务', NULL, '货品库存接口', NULL, '查看', 'warehouse-query', '/warehouse/v1/web/admin/u/p/warehouse/stock/page',
        'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (346, '仓库服务', NULL, '仓库设置管理', NULL, '管理', 'warehouseSetting-manager',
        '/warehouse/v1/web/admin/u/p/warehouseSetting', 'post', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (347, '仓库服务', NULL, '仓库设置管理', NULL, '管理', 'warehouseSetting-manager',
        '/warehouse/v1/web/admin/u/p/warehouseSetting', 'put', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (348, '仓库服务', NULL, '库存预留接口', NULL, '管理', 'warehouse-manager',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/reserved', 'post', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (349, '仓库服务', NULL, '库存预留接口', NULL, '查看', 'warehouse-query',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/reserved', 'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (350, '仓库服务', NULL, '货品库存接口', NULL, '管理', 'warehouse-manager',
        '/warehouse/v1/web/admin/u/p/warehouse/stock/listStockByCondition', 'post', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (351, '仓库服务', NULL, '货品库存接口', NULL, '管理', 'warehouse-manager',
        '/warehouse/v1/web/admin/u/po/warehouse/stock/listStockByCondition', 'post', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (352, '仓库服务', NULL, '数据迁移', NULL, '查看', 'data-query', '/warehouse/v1/web/admin/u/data', 'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (353, '仓库服务', NULL, '仓库管理', NULL, '管理', 'warehouse-manager',
        '/warehouse/v1/web/admin/u/p/warehouse/updateStatus', 'put', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (354, '仓库服务', NULL, '数据迁移', NULL, '查看', 'data-query', '/warehouse/v1/web/admin/u/data/query', 'get', 411, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (355, '商品模块', NULL, '品类管理', NULL, '查看', 'category-query', '/goods/v1/web/admin/category/po/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (356, '商品模块', NULL, '商品属性管理', NULL, '查看', 'attribute-query', '/goods/v1/web/admin/attribute/allvalue/list',
        'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (357, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/list/mobile', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (358, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/po/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (359, '商品模块', NULL, '价格等级管理', NULL, '查看', 'scaleprice-query', '/goods/v1/web/admin/scaleprice/list', 'get', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (360, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/po/list/ids', 'post', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (361, '商品模块', NULL, '商品分类管理', NULL, '管理', 'classify-manager', '/goods/v1/web/admin/classify', 'post', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (362, '商品模块', NULL, '商品分类管理', NULL, '查看', 'classify-query', '/goods/v1/web/admin/classify', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (363, '商品模块', NULL, '商品分类管理', NULL, '管理', 'classify-manager', '/goods/v1/web/admin/classify', 'delete', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (364, '商品模块', NULL, '商品分类管理', NULL, '管理', 'classify-manager', '/goods/v1/web/admin/classify', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (365, '商品模块', NULL, '品牌管理', NULL, '查看', 'brand-query', '/goods/v1/web/admin/brand/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (366, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/item/salestatus', 'put', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (367, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (368, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/sync/batch/item/box', 'put',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (369, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods', 'post', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (370, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (371, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods', 'delete', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (372, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (373, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/po/pageDiyItem', 'get', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (374, '商品模块', NULL, '商品属性管理', NULL, '查看', 'attribute-query', '/goods/v1/web/admin/attribute/po/list', 'get', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (375, '商品模块', NULL, '品牌管理', NULL, '管理', 'brand-manager', '/goods/v1/web/admin/brand', 'post', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (376, '商品模块', NULL, '品牌管理', NULL, '查看', 'brand-query', '/goods/v1/web/admin/brand', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (377, '商品模块', NULL, '品牌管理', NULL, '管理', 'brand-manager', '/goods/v1/web/admin/brand', 'delete', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (378, '商品模块', NULL, '品牌管理', NULL, '管理', 'brand-manager', '/goods/v1/web/admin/brand', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (379, '商品模块', NULL, '商品分类管理', NULL, '查看', 'classify-query', '/goods/v1/web/admin/classify/sub/list', 'get', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (380, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/item/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (381, '商品模块', NULL, '商品分类管理', NULL, '查看', 'classify-query', '/goods/v1/web/admin/classify/po/list', 'get', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (382, '商品模块', NULL, '商品属性管理', NULL, '管理', 'attribute-manager', '/goods/v1/web/admin/attribute/open', 'put', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (383, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/sync/all/price', 'put', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (384, '商品模块', NULL, '商品标签管理', NULL, '查看', 'tag-query', '/goods/v1/web/admin/tag/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (385, '商品模块', NULL, '商品标签管理', NULL, '管理', 'tag-manager', '/goods/v1/web/admin/tag', 'post', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (386, '商品模块', NULL, '商品标签管理', NULL, '查看', 'tag-query', '/goods/v1/web/admin/tag', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (387, '商品模块', NULL, '商品标签管理', NULL, '管理', 'tag-manager', '/goods/v1/web/admin/tag', 'delete', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (388, '商品模块', NULL, '商品标签管理', NULL, '管理', 'tag-manager', '/goods/v1/web/admin/tag', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (389, '商品模块', NULL, '商品标签管理', NULL, '查看', 'tag-query', '/goods/v1/web/admin/tag/po/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (390, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/sync/all/item', 'put', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (391, '商品模块', NULL, '品牌管理', NULL, '管理', 'brand-manager', '/goods/v1/web/admin/brand/open', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (392, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/item/box', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (393, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/item/box', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (394, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/po/pageAssignByDistributorId',
        'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (395, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/list/section', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (396, '商品模块', NULL, '商品分类管理', NULL, '管理', 'classify-manager', '/goods/v1/web/admin/classify/open', 'put', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (397, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/sync/batch/price', 'put',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (398, '商品模块', NULL, '价格等级管理', NULL, '管理', 'scaleprice-manager', '/goods/v1/web/admin/scaleprice', 'post', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (399, '商品模块', NULL, '价格等级管理', NULL, '查看', 'scaleprice-query', '/goods/v1/web/admin/scaleprice', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (400, '商品模块', NULL, '价格等级管理', NULL, '管理', 'scaleprice-manager', '/goods/v1/web/admin/scaleprice', 'delete', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (401, '商品模块', NULL, '价格等级管理', NULL, '管理', 'scaleprice-manager', '/goods/v1/web/admin/scaleprice', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (402, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/po/item/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (403, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/item/erp/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (404, '商品模块', NULL, '商品管理', NULL, '查看', 'goods-query', '/goods/v1/web/admin/goods/list/column', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (405, '商品模块', NULL, '品牌管理', NULL, '查看', 'brand-query', '/goods/v1/web/admin/brand/po/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (406, '商品模块', NULL, '品类管理', NULL, '查看', 'category-query', '/goods/v1/web/admin/category/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (407, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/sync/batch/item', 'put', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (408, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/salestatus', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (409, '商品模块', NULL, '价格等级管理', NULL, '管理', 'scaleprice-manager', '/goods/v1/web/admin/scaleprice/open', 'put',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (410, '商品模块', NULL, '商品分类管理', NULL, '查看', 'classify-query', '/goods/v1/web/admin/classify/list', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (411, '商品模块', NULL, '商品分类管理', NULL, '查看', 'classify-query', '/goods/v1/web/admin/classify/po/sub/list', 'get',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (412, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/po/list/classifyIds', 'post',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (413, '商品模块', NULL, '商品标签管理', NULL, '管理', 'tag-manager', '/goods/v1/web/admin/tag/open', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (414, '商品模块', NULL, '商品属性管理', NULL, '管理', 'attribute-manager', '/goods/v1/web/admin/attribute', 'post', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (415, '商品模块', NULL, '商品属性管理', NULL, '查看', 'attribute-query', '/goods/v1/web/admin/attribute', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (416, '商品模块', NULL, '商品属性管理', NULL, '管理', 'attribute-manager', '/goods/v1/web/admin/attribute', 'delete', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (417, '商品模块', NULL, '商品属性管理', NULL, '管理', 'attribute-manager', '/goods/v1/web/admin/attribute', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (418, '商品模块', NULL, '品类管理', NULL, '管理', 'category-manager', '/goods/v1/web/admin/category/open', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (419, '商品模块', NULL, '价格等级管理', NULL, '查看', 'scaleprice-query', '/goods/v1/web/admin/scaleprice/po/list', 'get',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (420, '商品模块', NULL, '商品属性管理', NULL, '查看', 'attribute-query', '/goods/v1/web/admin/attribute/list', 'get', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (421, '商品模块', NULL, '商品属性管理', NULL, '查看', 'attribute-query', '/goods/v1/web/admin/attribute/value/list', 'get',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (422, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/freezestatus', 'put', 111,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (423, '商品模块', NULL, '品类管理', NULL, '管理', 'category-manager', '/goods/v1/web/admin/category', 'post', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (424, '商品模块', NULL, '品类管理', NULL, '查看', 'category-query', '/goods/v1/web/admin/category', 'get', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (425, '商品模块', NULL, '品类管理', NULL, '管理', 'category-manager', '/goods/v1/web/admin/category', 'delete', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (426, '商品模块', NULL, '品类管理', NULL, '管理', 'category-manager', '/goods/v1/web/admin/category', 'put', 111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (427, '商品模块', NULL, '商品管理', NULL, '管理', 'goods-manager', '/goods/v1/web/admin/goods/sync/all/item/box', 'put',
        111, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (428, '客户模块', NULL, '分销商管理', NULL, '管理', 'distributor-manager', '/distributor/v1/web/admin/distributor', 'post',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (429, '客户模块', NULL, '分销商管理', NULL, '查看', 'distributor-query', '/distributor/v1/web/admin/distributor', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (430, '客户模块', NULL, '分销商管理', NULL, '管理', 'distributor-manager', '/distributor/v1/web/admin/distributor',
        'delete', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (431, '客户模块', NULL, '分销商管理', NULL, '管理', 'distributor-manager', '/distributor/v1/web/admin/distributor', 'put',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (432, '客户模块', NULL, '分销商管理', NULL, '查看', 'distributor-query', '/distributor/v1/web/admin/distributor/po/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (433, '客户模块', NULL, '分销商类别管理', NULL, '管理', 'category-manager', '/distributor/v1/web/admin/category/open', 'put',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (434, '客户模块', NULL, '分销商定制价格接口', NULL, '管理', 'distributorCustomPrice-manager',
        '/distributor/v1/web/admin/u/p/distributorCustomPrice', 'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (435, '客户模块', NULL, '分销商定制价格接口', NULL, '查看', 'distributorCustomPrice-query',
        '/distributor/v1/web/admin/u/p/distributorCustomPrice', 'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (436, '客户模块', NULL, '分销商分组管理', NULL, '查看', 'group-query', '/distributor/v1/web/admin/group/list', 'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (437, '客户模块', NULL, '分销商管理', NULL, '查看', 'distributor-query', '/distributor/v1/web/admin/distributor/one/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (438, '客户模块', NULL, '分销商销售区域管理', NULL, '管理', 'salesarea-manager', '/distributor/v1/web/admin/salesarea', 'post',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (439, '客户模块', NULL, '分销商销售区域管理', NULL, '查看', 'salesarea-query', '/distributor/v1/web/admin/salesarea', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (440, '客户模块', NULL, '分销商销售区域管理', NULL, '管理', 'salesarea-manager', '/distributor/v1/web/admin/salesarea',
        'delete', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (441, '客户模块', NULL, '分销商销售区域管理', NULL, '管理', 'salesarea-manager', '/distributor/v1/web/admin/salesarea', 'put',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (442, '客户模块', NULL, 'C端客户管理', NULL, '管理', 'customer-manager', '/distributor/v1/web/admin/customer/status', 'put',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (443, '客户模块', NULL, '分销商联系人角色管理', NULL, '查看', 'role-query', '/distributor/v1/web/admin/role/list', 'get', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (444, '客户模块', NULL, '分销商收款条件管理', NULL, '管理', 'trade-manager', '/distributor/v1/web/admin/trade/open', 'put', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (445, '客户模块', NULL, '分销商系统平台接口', NULL, '管理', 'sysplatform-manager', '/distributor/v1/web/admin/sysplatform',
        'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (446, '客户模块', NULL, '分销商系统平台接口', NULL, '查看', 'sysplatform-query', '/distributor/v1/web/admin/sysplatform', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (447, '客户模块', NULL, '分销商系统平台接口', NULL, '管理', 'sysplatform-manager', '/distributor/v1/web/admin/sysplatform',
        'delete', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (448, '客户模块', NULL, '分销商系统平台接口', NULL, '管理', 'sysplatform-manager', '/distributor/v1/web/admin/sysplatform',
        'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (449, '客户模块', NULL, '分销商平台接口', NULL, '管理', 'platform-manager', '/distributor/v1/web/admin/platform', 'post', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (450, '客户模块', NULL, '分销商平台接口', NULL, '查看', 'platform-query', '/distributor/v1/web/admin/platform', 'get', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (451, '客户模块', NULL, '分销商平台接口', NULL, '管理', 'platform-manager', '/distributor/v1/web/admin/platform', 'delete',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (452, '客户模块', NULL, '分销商平台接口', NULL, '管理', 'platform-manager', '/distributor/v1/web/admin/platform', 'put', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (453, '客户模块', NULL, '分销商联系人角色管理', NULL, '查看', 'role-query', '/distributor/v1/web/admin/role/po/list', 'get', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (454, '客户模块', NULL, '分销商管理', NULL, '查看', 'distributor-query', '/distributor/v1/web/admin/distributor/next/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (455, '客户模块', NULL, '分销商微信平台接口', NULL, '管理', 'wxplatform-manager', '/distributor/v1/web/admin/wxplatform',
        'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (456, '客户模块', NULL, '分销商微信平台接口', NULL, '查看', 'wxplatform-query', '/distributor/v1/web/admin/wxplatform', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (457, '客户模块', NULL, '分销商微信平台接口', NULL, '管理', 'wxplatform-manager', '/distributor/v1/web/admin/wxplatform',
        'delete', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (458, '客户模块', NULL, '分销商微信平台接口', NULL, '管理', 'wxplatform-manager', '/distributor/v1/web/admin/wxplatform', 'put',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (459, '客户模块', NULL, '分销商销售区域管理', NULL, '查看', 'salesarea-query', '/distributor/v1/web/admin/salesarea/po/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (460, '客户模块', NULL, '分销商微信平台接口', NULL, '查看', 'wxplatform-query', '/distributor/v1/web/admin/wxplatform/po/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (461, '客户模块', NULL, '分销商联系人角色管理', NULL, '管理', 'role-manager', '/distributor/v1/web/admin/role/open', 'put', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (462, '客户模块', NULL, '分销商管理', NULL, '管理', 'distributor-manager', '/distributor/v1/web/admin/distributor/refuse',
        'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (463, '客户模块', NULL, '分销商收款条件管理', NULL, '查看', 'trade-query', '/distributor/v1/web/admin/trade/po/list', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (464, '客户模块', NULL, '分销商管理', NULL, '管理', 'distributor-manager', '/distributor/v1/web/admin/distributor/po/ids',
        'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (465, '客户模块', NULL, '分销商系统平台接口', NULL, '查看', 'sysplatform-query',
        '/distributor/v1/web/admin/sysplatform/po/list', 'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (466, '客户模块', NULL, '分销商平台接口', NULL, '管理', 'platform-manager', '/distributor/v1/web/admin/platform/open', 'put',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (467, '客户模块', NULL, '分销商定制价格接口', NULL, '管理', 'distributorCustomPrice-manager',
        '/distributor/v1/web/admin/u/po/distributorCustomPrice', 'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (468, '客户模块', NULL, '分销商定制价格接口', NULL, '查看', 'distributorCustomPrice-query',
        '/distributor/v1/web/admin/u/po/distributorCustomPrice', 'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (469, '客户模块', NULL, '分销商销售区域管理', NULL, '查看', 'salesarea-query', '/distributor/v1/web/admin/salesarea/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (470, '客户模块', NULL, '分销商类别管理', NULL, '查看', 'category-query', '/distributor/v1/web/admin/category/list', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (471, '客户模块', NULL, '分销商类别管理', NULL, '查看', 'category-query', '/distributor/v1/web/admin/category/po/list', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (472, '客户模块', NULL, '分销商分组管理', NULL, '管理', 'group-manager', '/distributor/v1/web/admin/group', 'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (473, '客户模块', NULL, '分销商分组管理', NULL, '查看', 'group-query', '/distributor/v1/web/admin/group', 'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (474, '客户模块', NULL, '分销商分组管理', NULL, '管理', 'group-manager', '/distributor/v1/web/admin/group', 'delete', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (475, '客户模块', NULL, '分销商分组管理', NULL, '管理', 'group-manager', '/distributor/v1/web/admin/group', 'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (476, '客户模块', NULL, '分销商分组管理', NULL, '查看', 'group-query', '/distributor/v1/web/admin/group/po/list', 'get', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (477, '客户模块', NULL, '分销商系统平台接口', NULL, '查看', 'sysplatform-query', '/distributor/v1/web/admin/sysplatform/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (478, '客户模块', NULL, '分销商平台接口', NULL, '查看', 'platform-query', '/distributor/v1/web/admin/platform/list', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (479, '客户模块', NULL, '分销商销售区域管理', NULL, '管理', 'salesarea-manager', '/distributor/v1/web/admin/salesarea/open',
        'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (480, '客户模块', NULL, '分销商类别管理', NULL, '管理', 'category-manager', '/distributor/v1/web/admin/category', 'post', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (481, '客户模块', NULL, '分销商类别管理', NULL, '查看', 'category-query', '/distributor/v1/web/admin/category', 'get', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (482, '客户模块', NULL, '分销商类别管理', NULL, '管理', 'category-manager', '/distributor/v1/web/admin/category', 'delete',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (483, '客户模块', NULL, '分销商类别管理', NULL, '管理', 'category-manager', '/distributor/v1/web/admin/category', 'put', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (484, '客户模块', NULL, '分销商微信平台接口', NULL, '查看', 'wxplatform-query', '/distributor/v1/web/admin/wxplatform/list',
        'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (485, '客户模块', NULL, '分销商平台接口', NULL, '查看', 'platform-query', '/distributor/v1/web/admin/platform/po/list', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (486, '客户模块', NULL, '分销商分组管理', NULL, '管理', 'group-manager', '/distributor/v1/web/admin/group/open', 'put', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (487, '客户模块', NULL, '分销商管理', NULL, '管理', 'distributor-manager', '/distributor/v1/web/admin/distributor/open',
        'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (488, '客户模块', NULL, '分销商收款条件管理', NULL, '查看', 'trade-query', '/distributor/v1/web/admin/trade/list', 'get', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (489, '客户模块', NULL, '分销商管理', NULL, '管理', 'distributor-manager',
        '/distributor/v1/web/admin/distributor/next/check', 'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (490, '客户模块', NULL, '分销商联系人角色管理', NULL, '管理', 'role-manager', '/distributor/v1/web/admin/role', 'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (491, '客户模块', NULL, '分销商联系人角色管理', NULL, '查看', 'role-query', '/distributor/v1/web/admin/role', 'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (492, '客户模块', NULL, '分销商联系人角色管理', NULL, '管理', 'role-manager', '/distributor/v1/web/admin/role', 'delete', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (493, '客户模块', NULL, '分销商联系人角色管理', NULL, '管理', 'role-manager', '/distributor/v1/web/admin/role', 'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (494, '客户模块', NULL, 'C端客户管理', NULL, '查看', 'customer-query', '/distributor/v1/web/admin/customer/list', 'get',
        211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (495, '客户模块', NULL, '分销商收款条件管理', NULL, '管理', 'trade-manager', '/distributor/v1/web/admin/trade', 'post', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (496, '客户模块', NULL, '分销商收款条件管理', NULL, '查看', 'trade-query', '/distributor/v1/web/admin/trade', 'get', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (497, '客户模块', NULL, '分销商收款条件管理', NULL, '管理', 'trade-manager', '/distributor/v1/web/admin/trade', 'delete', 211,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (498, '客户模块', NULL, '分销商收款条件管理', NULL, '管理', 'trade-manager', '/distributor/v1/web/admin/trade', 'put', 211, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (499, '财务模块', NULL, '销售往来单据-退款单列表管理', NULL, '查看', 'refund-query', '/financial/v1/web/admin/refund', 'get', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (500, '财务模块', NULL, '预存款-提现列表管理', NULL, '管理', 'deposit-manager', '/financial/v1/web/admin/deposit/withdrawal',
        'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (501, '财务模块', NULL, '预存款-提现列表管理', NULL, '管理', 'deposit-manager', '/financial/v1/web/admin/deposit/withdrawal',
        'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (502, '财务模块', NULL, '支付管理', NULL, '查看', 'pay-query', '/financial/v1/web/admin/pay/queryRefund', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (503, '财务模块', NULL, '预存款-冻结列表管理', NULL, '管理', 'deposit-manager', '/financial/v1/web/admin/deposit/freezing',
        'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (504, '财务模块', NULL, '预存款-提现列表管理', NULL, '查看', 'deposit-query', '/financial/v1/web/admin/deposit/withdrawal/list',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (505, '财务模块', NULL, '预存款明细-余额表管理', NULL, '查看', 'deposit-query',
        '/financial/v1/web/admin/deposit/available/detail', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (506, '财务模块', NULL, '平台账户-线下支付账户管理', NULL, '查看', 'platform-query',
        '/financial/v1/web/admin/platform/offline/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (507, '财务模块', NULL, '销售往来单据-退款单列表管理', NULL, '查看', 'refund-query', '/financial/v1/web/admin/refund/list', 'get',
        611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (508, '财务模块', NULL, '平台账户-支付宝支付账户管理', NULL, '管理', 'platform-manager',
        '/financial/v1/web/admin/platform/accountAlipay', 'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (509, '财务模块', NULL, '平台账户-支付宝支付账户管理', NULL, '查看', 'platform-query',
        '/financial/v1/web/admin/platform/accountAlipay', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (510, '财务模块', NULL, '平台账户-支付宝支付账户管理', NULL, '管理', 'platform-manager',
        '/financial/v1/web/admin/platform/accountAlipay', 'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (511, '财务模块', NULL, '平台账户-支付宝支付账户管理', NULL, '管理', 'platform-manager',
        '/financial/v1/web/admin/platform/accountAlipay', 'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (512, '财务模块', NULL, '汇率设置管理', NULL, '查看', 'currencyRate-query', '/financial/v1/web/admin/currencyRate/list',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (513, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountWx/appId', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (514, '财务模块', NULL, '汇率设置管理', NULL, '管理', 'currencyRate-manager', '/financial/v1/web/admin/currencyRate/po/sync',
        'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (515, '财务模块', NULL, '预存款-冻结列表管理', NULL, '查看', 'deposit-query', '/financial/v1/web/admin/deposit/freezing/list',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (516, '财务模块', NULL, '平台账户-支付宝支付账户管理', NULL, '查看', 'platform-query',
        '/financial/v1/web/admin/platform/accountAlipay/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (517, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountWx/distributorId', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (518, '财务模块', NULL, '预存款-冻结列表管理', NULL, '管理', 'deposit-manager', '/financial/v1/web/admin/deposit/freezings',
        'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (519, '财务模块', NULL, '分销商账户-支付宝账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountAlipay/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (520, '财务模块', NULL, '汇率设置管理', NULL, '管理', 'currencyRate-manager', '/financial/v1/web/admin/currencyRate', 'post',
        611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (521, '财务模块', NULL, '汇率设置管理', NULL, '查看', 'currencyRate-query', '/financial/v1/web/admin/currencyRate', 'get',
        611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (522, '财务模块', NULL, '汇率设置管理', NULL, '管理', 'currencyRate-manager', '/financial/v1/web/admin/currencyRate',
        'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (523, '财务模块', NULL, '汇率设置管理', NULL, '管理', 'currencyRate-manager', '/financial/v1/web/admin/currencyRate', 'put',
        611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (524, '财务模块', NULL, '销售往来单据-退款单申请列表管理', NULL, '查看', 'refundApply-query',
        '/financial/v1/web/admin/refundApply/distributor/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (525, '财务模块', NULL, '平台账户-快钱支付账户管理', NULL, '管理', 'platform-manager', '/financial/v1/web/admin/platform/KuaiQian',
        'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (526, '财务模块', NULL, '平台账户-快钱支付账户管理', NULL, '查看', 'platform-query', '/financial/v1/web/admin/platform/KuaiQian',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (527, '财务模块', NULL, '平台账户-快钱支付账户管理', NULL, '管理', 'platform-manager', '/financial/v1/web/admin/platform/KuaiQian',
        'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (528, '财务模块', NULL, '平台账户-快钱支付账户管理', NULL, '管理', 'platform-manager', '/financial/v1/web/admin/platform/KuaiQian',
        'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (529, '财务模块', NULL, '支付管理', NULL, '管理', 'pay-manager', '/financial/v1/web/admin/pay/refundTrade', 'post', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (530, '财务模块', NULL, '支付管理', NULL, '管理', 'pay-manager',
        '/financial/v1/web/admin/pay/wxpay/v3/pay/notify/{tradeMode}/{appType}/{customerFlag}/{organizationOrPayeeId}',
        'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (531, '财务模块', NULL, '预存款-冻结列表管理', NULL, '管理', 'deposit-manager', '/financial/v1/web/admin/deposit/freezing/thaw',
        'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (532, '财务模块', NULL, '销售往来单据-收款单列表管理', NULL, '查看', 'voucher-query', '/financial/v1/web/admin/voucher/list', 'get',
        611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (533, '财务模块', NULL, '币别管理', NULL, '管理', 'currency-manager', '/financial/v1/web/admin/currency/po/sync', 'put',
        611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (534, '财务模块', NULL, '支付管理', NULL, '管理', 'pay-manager',
        '/financial/v1/web/admin/pay/alipay/pay/notify/{tradeMode}/{customerFlag}/{organizationOrPayeeId}', 'post', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (535, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '管理', 'distributor-manager',
        '/financial/v1/web/admin/distributor/accountWx', 'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (536, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountWx', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (537, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '管理', 'distributor-manager',
        '/financial/v1/web/admin/distributor/accountWx', 'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (538, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '管理', 'distributor-manager',
        '/financial/v1/web/admin/distributor/accountWx', 'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (539, '财务模块', NULL, '预存款-收款列表管理', NULL, '查看', 'deposit-query', '/financial/v1/web/admin/deposit/receipt/list',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (540, '财务模块', NULL, '平台账户-线下支付账户管理', NULL, '管理', 'platform-manager', '/financial/v1/web/admin/platform/offline',
        'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (541, '财务模块', NULL, '平台账户-线下支付账户管理', NULL, '查看', 'platform-query', '/financial/v1/web/admin/platform/offline',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (542, '财务模块', NULL, '平台账户-线下支付账户管理', NULL, '管理', 'platform-manager', '/financial/v1/web/admin/platform/offline',
        'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (543, '财务模块', NULL, '平台账户-线下支付账户管理', NULL, '管理', 'platform-manager', '/financial/v1/web/admin/platform/offline',
        'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (544, '财务模块', NULL, '平台账户-快钱支付账户管理', NULL, '查看', 'platform-query',
        '/financial/v1/web/admin/platform/KuaiQian/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (545, '财务模块', NULL, '支付管理', NULL, '管理', 'pay-manager',
        '/financial/v1/web/admin/pay/wxpay/v2/pay/notify/{tradeMode}/{appType}/{customerFlag}/{organizationOrPayeeId}',
        'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (546, '财务模块', NULL, '支付管理', NULL, '管理', 'pay-manager', '/financial/v1/web/admin/pay/closeTrade', 'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (547, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountWx/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (548, '财务模块', NULL, '销售往来单据-退款单申请列表管理', NULL, '查看', 'refundApply-query',
        '/financial/v1/web/admin/refundApply/customer/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (549, '财务模块', NULL, '支付管理', NULL, '管理', 'pay-manager', '/financial/v1/web/admin/pay/createTrade', 'post', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (550, '财务模块', NULL, '平台账户-微信支付账户管理', NULL, '管理', 'platform-manager',
        '/financial/v1/web/admin/platform/accountWx', 'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (551, '财务模块', NULL, '平台账户-微信支付账户管理', NULL, '查看', 'platform-query', '/financial/v1/web/admin/platform/accountWx',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (552, '财务模块', NULL, '平台账户-微信支付账户管理', NULL, '管理', 'platform-manager',
        '/financial/v1/web/admin/platform/accountWx', 'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (553, '财务模块', NULL, '平台账户-微信支付账户管理', NULL, '管理', 'platform-manager',
        '/financial/v1/web/admin/platform/accountWx', 'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (554, '财务模块', NULL, '支付管理', NULL, '管理', 'pay-manager',
        '/financial/v1/web/admin/pay/wxpay/v3/refund/notify/{tradeMode}/{appType}/{customerFlag}/{organizationOrPayeeId}',
        'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (555, '财务模块', NULL, '预存款明细-余额表管理', NULL, '查看', 'deposit-query', '/financial/v1/web/admin/deposit/available/list',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (556, '财务模块', NULL, '预存款明细-余额表管理', NULL, '查看', 'deposit-query', '/financial/v1/web/admin/deposit/sync', 'get',
        611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (557, '财务模块', NULL, '销售往来单据-退款单申请列表管理', NULL, '查看', 'refundApply-query',
        '/financial/v1/web/admin/refundApply/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (558, '财务模块', NULL, '支付管理', NULL, '查看', 'pay-query', '/financial/v1/web/admin/pay/queryTrade', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (559, '财务模块', NULL, '平台账户-微信支付账户管理', NULL, '查看', 'platform-query',
        '/financial/v1/web/admin/platform/accountWx/list', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (560, '财务模块', NULL, '币别管理', NULL, '查看', 'currency-query', '/financial/v1/web/admin/currency/list', 'get', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (561, '财务模块', NULL, '销售往来单据-收款单列表管理', NULL, '查看', 'voucher-query', '/financial/v1/web/admin/voucher', 'get', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (562, '财务模块', NULL, '币别管理', NULL, '管理', 'currency-manager', '/financial/v1/web/admin/currency', 'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (563, '财务模块', NULL, '币别管理', NULL, '查看', 'currency-query', '/financial/v1/web/admin/currency', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (564, '财务模块', NULL, '币别管理', NULL, '管理', 'currency-manager', '/financial/v1/web/admin/currency', 'delete', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (565, '财务模块', NULL, '币别管理', NULL, '管理', 'currency-manager', '/financial/v1/web/admin/currency', 'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (566, '财务模块', NULL, '销售往来单据-退款单申请列表管理', NULL, '管理', 'refundApply-manager',
        '/financial/v1/web/admin/refundApply/manualConfirmRefundApply', 'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (567, '财务模块', NULL, '预存款-冻结列表管理', NULL, '查看', 'deposit-query',
        '/financial/v1/web/admin/deposit/freezing/balance', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (568, '财务模块', NULL, '分销商账户-支付宝账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountAlipay/checkDistributor', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (569, '财务模块', NULL, '分销商账户-微信支付账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountWx/checkDistributor', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (570, '财务模块', NULL, '预存款明细-明细账管理', NULL, '查看', 'deposit-query', '/financial/v1/web/admin/deposit/detail/list',
        'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (571, '财务模块', NULL, '分销商账户-支付宝账户列表管理', NULL, '管理', 'distributor-manager',
        '/financial/v1/web/admin/distributor/accountAlipay', 'post', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (572, '财务模块', NULL, '分销商账户-支付宝账户列表管理', NULL, '查看', 'distributor-query',
        '/financial/v1/web/admin/distributor/accountAlipay', 'get', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (573, '财务模块', NULL, '分销商账户-支付宝账户列表管理', NULL, '管理', 'distributor-manager',
        '/financial/v1/web/admin/distributor/accountAlipay', 'delete', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (574, '财务模块', NULL, '分销商账户-支付宝账户列表管理', NULL, '管理', 'distributor-manager',
        '/financial/v1/web/admin/distributor/accountAlipay', 'put', 611, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (575, '财务模块', NULL, '预存款明细-余额表管理', NULL, '查看', 'deposit-query', '/financial/v1/web/admin/deposit', 'get', 611,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (576, '营销推广模块', NULL, '优惠券管理', NULL, '管理', 'coupon-manager', '/promotion/v1/web/admin/coupon/status', 'put', 711,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (577, '营销推广模块', NULL, '优惠券管理', NULL, '管理', 'coupon-manager', '/promotion/v1/web/admin/coupon', 'post', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (578, '营销推广模块', NULL, '优惠券管理', NULL, '查看', 'coupon-query', '/promotion/v1/web/admin/coupon', 'get', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (579, '营销推广模块', NULL, '优惠券管理', NULL, '管理', 'coupon-manager', '/promotion/v1/web/admin/coupon', 'delete', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (580, '营销推广模块', NULL, '优惠券管理', NULL, '管理', 'coupon-manager', '/promotion/v1/web/admin/coupon', 'put', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (581, '营销推广模块', NULL, '优惠券管理', NULL, '管理', 'coupon-manager', '/promotion/v1/web/admin/coupon/customer/status',
        'put', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (582, '营销推广模块', NULL, '优惠券管理', NULL, '查看', 'coupon-query', '/promotion/v1/web/admin/coupon/no', 'get', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (583, '营销推广模块', NULL, '拼团秒杀管理', NULL, '查看', 'groupseckill-query', '/promotion/v1/web/admin/groupseckill/list',
        'get', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (584, '营销推广模块', NULL, '拼团秒杀管理', NULL, '管理', 'groupseckill-manager', '/promotion/v1/web/admin/groupseckill/sort',
        'put', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (585, '营销推广模块', NULL, '促销活动管理', NULL, '管理', 'promotion-manager', '/promotion/v1/web/admin/promotion', 'post',
        711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (586, '营销推广模块', NULL, '促销活动管理', NULL, '查看', 'promotion-query', '/promotion/v1/web/admin/promotion', 'get', 711,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (587, '营销推广模块', NULL, '促销活动管理', NULL, '管理', 'promotion-manager', '/promotion/v1/web/admin/promotion', 'delete',
        711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (588, '营销推广模块', NULL, '促销活动管理', NULL, '管理', 'promotion-manager', '/promotion/v1/web/admin/promotion', 'put', 711,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (589, '营销推广模块', NULL, '优惠券管理', NULL, '管理', 'coupon-manager', '/promotion/v1/web/admin/coupon/count', 'put', 711,
        1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (590, '营销推广模块', NULL, '拼团秒杀管理', NULL, '管理', 'groupseckill-manager', '/promotion/v1/web/admin/groupseckill',
        'post', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (591, '营销推广模块', NULL, '拼团秒杀管理', NULL, '查看', 'groupseckill-query', '/promotion/v1/web/admin/groupseckill', 'get',
        711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (592, '营销推广模块', NULL, '拼团秒杀管理', NULL, '管理', 'groupseckill-manager', '/promotion/v1/web/admin/groupseckill',
        'delete', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (593, '营销推广模块', NULL, '拼团秒杀管理', NULL, '管理', 'groupseckill-manager', '/promotion/v1/web/admin/groupseckill',
        'put', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (594, '营销推广模块', NULL, '促销活动管理', NULL, '管理', 'promotion-manager', '/promotion/v1/web/admin/promotion/status',
        'put', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (595, '营销推广模块', NULL, '优惠券管理', NULL, '查看', 'coupon-query', '/promotion/v1/web/admin/coupon/list', 'get', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (596, '营销推广模块', NULL, '促销活动管理', NULL, '查看', 'promotion-query', '/promotion/v1/web/admin/promotion/list', 'get',
        711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (597, '营销推广模块', NULL, '拼团秒杀管理', NULL, '管理', 'groupseckill-manager',
        '/promotion/v1/web/admin/groupseckill/status', 'put', 711, 1);
INSERT INTO `system_db`.`sys_permission`(`id`, `service`, `service_en`, `module`, `module_en`, `permission_name`,
                                         `permission_module`, `url_path`, `method`, `sort`, `status`)
VALUES (598, '营销推广模块', NULL, '拼团秒杀管理', NULL, '管理', 'groupseckill-manager',
        '/promotion/v1/web/admin/groupseckill/goods/sort', 'put', 711, 1);


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`               int(11) UNSIGNED                                        NOT NULL AUTO_INCREMENT,
    `role_name`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
    `role_name_en`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色英文名',
    `role_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
    `create_time`      datetime(0)                                             NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`      datetime(0)                                             NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `id`      int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `role_id` int(11) UNSIGNED NOT NULL COMMENT '角色id',
    `menu_id` int(11) UNSIGNED NOT NULL COMMENT '菜单id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色菜单关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `id`            int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `role_id`       int(11)          NOT NULL,
    `permission_id` int(11)          NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色权限关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`              int(11) UNSIGNED                                               NOT NULL AUTO_INCREMENT,
    `user_name`       varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '用户名',
    `real_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '真实姓名',
    `password`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '密码',
    `mobile`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '手机号',
    `email`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '邮箱',
    `ding_avatar`     varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '钉钉头像',
    `admin_type`      smallint(4)                                                    NULL DEFAULT NULL COMMENT '管理员类型, 1.超级管理员 2.普通用户',
    `brand_scope`     smallint(4) UNSIGNED                                           NULL DEFAULT 0 COMMENT '品牌范围,0无 1.全局 2.其他',
    `sale_scope`      smallint(4) UNSIGNED                                           NULL DEFAULT 0 COMMENT '业务范围,1.全局 2.单业务范围 3.多业务范围',
    `status`          smallint(4)                                                    NULL DEFAULT 1 COMMENT '状态, 1.启用 0.禁用',
    `erp_user_no`     varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT 'ERP_用户编码',
    `organization_id` int(11)                                                        NULL DEFAULT NULL COMMENT '组织id',
    `department_id`   int(11) UNSIGNED                                               NULL DEFAULT 0 COMMENT '部门id',
    `rock_account_id` bigint(20)                                                     NULL DEFAULT 0 COMMENT '账户中心id',
    `fictitious_flag` smallint(4)                                                    NULL DEFAULT 0 COMMENT '是否虚拟销售员，0否 1是',
    `sale_flag`       smallint(4)                                                    NULL DEFAULT 1 COMMENT '是否销售员，0否 1是 ',
    `remark`          varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '备注',
    `create_time`     datetime(0)                                                    NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)                                                    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_name` (`user_name`) USING BTREE COMMENT '用户名唯一主键'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统后台用户表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_brand
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_brand`;
CREATE TABLE `sys_user_brand`
(
    `id`       int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`  int(10) UNSIGNED NOT NULL,
    `brand_id` int(10) UNSIGNED NOT NULL COMMENT '品牌ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户品牌表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_login`;
CREATE TABLE `sys_user_login`
(
    `id`              int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'user_id',
    `last_login_time` datetime(0)      NOT NULL COMMENT '最后登录时间',
    `login_times`     int(11)          NOT NULL COMMENT '登录总次数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户登录记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`      int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id` int(11)          NOT NULL COMMENT '用户id',
    `role_id` int(11)          NOT NULL COMMENT '角色id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户角色关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_sale
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_sale`;
CREATE TABLE `sys_user_sale`
(
    `id`           int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`      int(10) UNSIGNED NOT NULL,
    `sale_user_id` int(10) UNSIGNED NOT NULL COMMENT '业务员ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户业务范围表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for training_center
-- ----------------------------
DROP TABLE IF EXISTS `training_center`;
CREATE TABLE `training_center`
(
    `id`               int(11)                                                       NOT NULL AUTO_INCREMENT,
    `parent_id`        int(11) UNSIGNED                                              NOT NULL COMMENT '父id',
    `sort`             int(5)                                                        NOT NULL DEFAULT 0 COMMENT '排序值',
    `status`           smallint(6)                                                   NOT NULL DEFAULT 0 COMMENT '0禁用 1启用',
    `title_zh`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 中文',
    `title_en`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题 英文',
    `content_url_zh`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '内容 资源地址 中文',
    `content_url_en`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '内容 资源地址 英文',
    `thumbnail_url_zh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '缩略图 资源地址(未启用) 中文',
    `thumbnail_url_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '缩略图 资源地址(未启用) 英文',
    `create_time`      datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `IDX_PARENT_ID` (`parent_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '培训中心'
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
