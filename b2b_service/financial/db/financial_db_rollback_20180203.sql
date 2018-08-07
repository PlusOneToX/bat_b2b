/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 127.0.0.1:3306
 Source Schema         : financial_db

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 03/08/2018 22:38:59
*/
DROP DATABASE IF EXISTS `financial_db`;
CREATE DATABASE `financial_db` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

use financial_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_alipay
-- ----------------------------
DROP TABLE IF EXISTS `account_alipay`;
CREATE TABLE `account_alipay`
(
    `id`                 int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `organization_id`    int(10) UNSIGNED                                             NOT NULL COMMENT '销售组织id',
    `account_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
    `app_id`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付宝appid',
    `bank_no`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp绑定名称',
    `app_public_secret`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NOT NULL COMMENT '支付宝公钥',
    `app_private_secret` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NOT NULL COMMENT '支付宝私钥',
    `back_type`          smallint(5) UNSIGNED                                         NOT NULL DEFAULT 1 COMMENT '取消订单是否原路返回 1、是 2、否',
    `open_flag`          smallint(5)                                                  NOT NULL COMMENT '状态, 1启用,0停用',
    `create_time`        datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `update_time`        datetime(0)                                                  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_organization_id` (`organization_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '平台支付宝收款账户配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for account_alipay_distributor
-- ----------------------------
DROP TABLE IF EXISTS `account_alipay_distributor`;
CREATE TABLE `account_alipay_distributor`
(
    `id`                       int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `distributor_id`           int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '分销商名称',
    `distributor_company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分销商公司名称',
    `account_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '收款账户名称',
    `app_id`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '支付宝appid',
    `app_public_secret`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '支付宝公钥',
    `app_private_secret`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '支付宝私钥',
    `create_time`              datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`              datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `distributor_id` (`distributor_id`) USING BTREE,
    INDEX `idx_appid` (`app_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销客户支付宝收款账户配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for account_kuaiqian
-- ----------------------------
DROP TABLE IF EXISTS `account_kuaiqian`;
CREATE TABLE `account_kuaiqian`
(
    `id`                  int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `organization_id`     int(10) UNSIGNED                                              NULL DEFAULT NULL COMMENT '销售组织id',
    `account_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '收款账户名称',
    `bank_no`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'erp绑定名称',
    `merchan_acct_id`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '快钱人民币网关账号',
    `sign_file_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名文件路径',
    `sign_pwd`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '签名密码',
    `sign_private_key`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '私钥',
    `check_sign_file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '快钱支付通知回调验证签名文件路径',
    `back_type`           smallint(6)                                                   NULL DEFAULT NULL COMMENT '取消订单是否原路返回 1、是 2、否',
    `open_flag`           smallint(5)                                                   NULL DEFAULT NULL COMMENT '状态, 1启用,0停用',
    `create_time`         datetime(0)                                                   NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime(0)                                                   NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '平台快钱收款账户配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for account_offline
-- ----------------------------
DROP TABLE IF EXISTS `account_offline`;
CREATE TABLE `account_offline`
(
    `id`              int(11) UNSIGNED                                              NOT NULL AUTO_INCREMENT,
    `organization_id` int(11)                                                       NOT NULL COMMENT '销售组织id',
    `currency_code`   varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '币别编码',
    `account_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '收款账户名称',
    `bank_no`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'erp绑定名称',
    `card_no`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '卡号',
    `bank_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '收款银行',
    `bank_addr`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款银行地址',
    `open_flag`       smallint(5)                                                   NOT NULL COMMENT '状态, 1启用,0停用',
    `create_time`     datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '平台线下收款账户配置表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_wx
-- ----------------------------
DROP TABLE IF EXISTS `account_wx`;
CREATE TABLE `account_wx`
(
    `id`                       int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT,
    `organization_id`          int(10) UNSIGNED                                             NOT NULL COMMENT '销售组织id',
    `account_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款账户名称',
    `bank_no`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'erp绑定名称',
    `app_id`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '微信appid（公众号小程序不可信/以前端传值为准）',
    `apiclient_key`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL COMMENT '商户API证书私钥',
    `serial_number`            char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci    NULL     DEFAULT NULL COMMENT '商户证书序列号',
    `certificate_invalid_time` datetime(0)                                                  NULL     DEFAULT NULL COMMENT '商户证书失效时间',
    `account_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信商户号(mchid)',
    `app_key`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信appKey V3秘钥与 api秘钥',
    `app_type`                 smallint(5) UNSIGNED                                         NOT NULL DEFAULT 1 COMMENT '应用类型 1、微信公众号 2、微信小程序',
    `back_type`                smallint(5) UNSIGNED                                         NOT NULL DEFAULT 1 COMMENT '取消订单是否原路返回 1、是 2、否',
    `open_flag`                smallint(5) UNSIGNED                                         NOT NULL DEFAULT 1 COMMENT '状态, 1启用,0停用',
    `version`                  varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT 'api版本 V2版本 V3版本',
    `create_time`              datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `update_time`              datetime(0)                                                  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_organization_id` (`organization_id`, `app_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '平台微信收款账户配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for account_wx_distributor
-- ----------------------------
DROP TABLE IF EXISTS `account_wx_distributor`;
CREATE TABLE `account_wx_distributor`
(
    `id`                       int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `distributor_id`           int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '分销商名称',
    `distributor_company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商公司名称',
    `account_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '收款账户名称',
    `app_id`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '微信appid（公众号小程序不可信/以前端传值为准）',
    `apiclient_key`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '商户API证书私钥',
    `serial_number`            char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL     DEFAULT NULL COMMENT '商户证书序列号',
    `certificate_invalid_time` datetime(0)                                                   NULL     DEFAULT NULL COMMENT '商户证书失效时间',
    `app_key`                  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信appKey V3秘钥与 api秘钥',
    `app_type`                 smallint(5) UNSIGNED                                          NOT NULL DEFAULT 1 COMMENT '应用类型 1、微信公众号 2、微信小程序',
    `account_no`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '微信商户号',
    `version`                  varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT 'api版本 V2版本 V3版本',
    `create_time`              datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`              datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `distributor_id` (`distributor_id`, `app_type`) USING BTREE,
    INDEX `idx_appid` (`app_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销客户微信收款账户配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for currency
-- ----------------------------
DROP TABLE IF EXISTS `currency`;
CREATE TABLE `currency`
(
    `id`             int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`           varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种名称',
    `currency_code`  varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种编码',
    `money_accuracy` smallint(5) UNSIGNED                                         NULL DEFAULT NULL COMMENT '货币精度',
    `erp_no`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp编码',
    `open_flag`      smallint(5) UNSIGNED                                         NULL DEFAULT 1 COMMENT '状态, 1启用,0停用',
    `create_time`    datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `update_time`    datetime(0)                                                  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `currency_code` (`currency_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '币别表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for currency_rate
-- ----------------------------
DROP TABLE IF EXISTS `currency_rate`;
CREATE TABLE `currency_rate`
(
    `id`              int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `exchange_rate`   decimal(16, 4)                                               NOT NULL COMMENT '直接汇率',
    `reverse_ex_rate` decimal(16, 4)                                               NOT NULL COMMENT '间接汇率',
    `cy_forid`        varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原币',
    `cy_toid`         varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目标币',
    `beg_date`        datetime(0)                                                  NOT NULL COMMENT '开始生效时间',
    `create_time`     datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)                                                  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '币种汇率表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for deposit
-- ----------------------------
DROP TABLE IF EXISTS `deposit`;
CREATE TABLE `deposit`
(
    `id`                    int(10) UNSIGNED    NOT NULL AUTO_INCREMENT,
    `is_show_prestore`      tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '显示预存款 0为否 1为是',
    `is_open_online_topup`  tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '开启线上充值 0为否 1为是',
    `online_min_amount`     float               NULL DEFAULT 0 COMMENT '充值最小额度',
    `is_open_offline_topup` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '开启线下充值 0为否 1为是',
    `is_open_withdrawal`    tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '开启提现 0为否 1为是',
    `create_time`           bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '创建时间',
    `update_time`           bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '财务预存款表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for deposit_distributor
-- ----------------------------
DROP TABLE IF EXISTS `deposit_distributor`;
CREATE TABLE `deposit_distributor`
(
    `id`                        int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `distributor_id`            int(10) UNSIGNED                                             NOT NULL COMMENT '分销商id',
    `distributor_name`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分销商名称',
    `erp_distributor_id`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '分销商内码 erp分销商id',
    `tree_node`                 int(10) UNSIGNED                                             NOT NULL COMMENT '多级分销级数',
    `distributor_ancestor_id`   int(10) UNSIGNED                                             NULL     DEFAULT NULL COMMENT '父分销商id',
    `distributor_ancestor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '父分销商名称',
    `account_balance`           decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '账户余额',
    `account_available`         decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '账户可用余额',
    `freezing_amount`           decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '账户冻结金额',
    `recharge_amount`           decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '账户充值总金额',
    `commission_amount`         decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '分销所得佣金',
    `withdraw_amount`           decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '账户提现总金额',
    `consumer_amount`           decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '账户消费总金额',
    `refund_amount`             decimal(16, 3)                                               NOT NULL DEFAULT 0.000 COMMENT '订单取消增加总金额',
    `create_time`               datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `update_time`               datetime(0)                                                  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_distributor_id` (`distributor_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销客户预存款账户表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for deposit_distributor_freezing
-- ----------------------------
DROP TABLE IF EXISTS `deposit_distributor_freezing`;
CREATE TABLE `deposit_distributor_freezing`
(
    `id`                     int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `deposit_distributor_id` int(10) UNSIGNED                                              NOT NULL COMMENT '分销客户预存款账户id',
    `distributor_id`         int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '分销商名称',
    `freezing_amount`        decimal(16, 3) UNSIGNED                                       NULL     DEFAULT 0.000 COMMENT '冻结金额',
    `status`                 smallint(6) UNSIGNED                                          NOT NULL DEFAULT 0 COMMENT '冻结状态 0全部, 1,冻结 2,解冻',
    `business_type`          smallint(5) UNSIGNED                                          NOT NULL COMMENT '业务类型 1,提现冻结 2,其他冻结',
    `business_id`            int(11) UNSIGNED                                              NULL     DEFAULT NULL COMMENT '业务单号 (业务类型为 2时，为空 ) ',
    `remark`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '处理备注',
    `create_time`            datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`            datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `deposit_distributor_id` (`deposit_distributor_id`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销商账户预存款冻结明细表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for deposit_distributor_subsidiary_book
-- ----------------------------
DROP TABLE IF EXISTS `deposit_distributor_subsidiary_book`;
CREATE TABLE `deposit_distributor_subsidiary_book`
(
    `id`                     int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `deposit_distributor_id` int(10) UNSIGNED                                              NOT NULL COMMENT '分销客户预存款账户id',
    `distributor_id`         int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '分销商用户名',
    `business_type`          smallint(5) UNSIGNED                                          NOT NULL COMMENT '业务类型 1充值 2 提现 3 订单消费 4 订单取消增加 5调整 6 ERP增量变化 7 ERP全量变化 8 分销佣金 9 充值退款',
    `business_id`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号 (业务类型为 6或7时，为空 ) ',
    `pay_way`                smallint(6)                                                   NULL DEFAULT NULL COMMENT '交易方式付款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
    `change_type`            smallint(5) UNSIGNED                                          NULL DEFAULT NULL COMMENT '变更类型 1.增加，2.减少 ',
    `amount`                 decimal(16, 3)                                                NULL DEFAULT NULL COMMENT '变动金额数',
    `before_deposit_amount`  decimal(16, 3)                                                NULL DEFAULT NULL COMMENT '变化前账户余额',
    `after_deposit_amount`   decimal(16, 3)                                                NULL DEFAULT NULL COMMENT '变化后账户余额',
    `remark`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注说明',
    `create_time`            datetime(0)                                                   NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `deposit_distributor_id` (`deposit_distributor_id`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销客户预存款账户明细表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pay_bills_customer
-- ----------------------------
DROP TABLE IF EXISTS `pay_bills_customer`;
CREATE TABLE `pay_bills_customer`
(
    `id`              int(10) UNSIGNED                                               NOT NULL AUTO_INCREMENT COMMENT '订单id',
    `out_trade_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '支付凭证号(注意：全平台唯一)',
    `customer_id`     int(10) UNSIGNED                                               NOT NULL COMMENT '支付分销客户id',
    `customer_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT 'C端用户名',
    `pay_type`        smallint(5) UNSIGNED                                           NOT NULL COMMENT '支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付',
    `business_type`   smallint(5) UNSIGNED                                           NOT NULL COMMENT '业务类型 1订单收款2在线充值收款',
    `business_id`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
    `pay_status`      smallint(5) UNSIGNED                                           NULL DEFAULT 0 COMMENT '订单状态: 0未支付， 1已支付，2已取消',
    `total_fee`       decimal(10, 3) UNSIGNED                                        NOT NULL COMMENT '支付金额',
    `order_title`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '订单标题',
    `order_describe`  varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '订单描述',
    `product_id`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '商品id',
    `online_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '支付平台凭证号',
    `expire_time`     datetime(0)                                                    NULL DEFAULT NULL COMMENT '失效时间',
    `pay_time`        datetime(0)                                                    NULL DEFAULT NULL COMMENT '支付成功时间',
    `create_time`     datetime(0)                                                    NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)                                                    NOT NULL COMMENT '更新时间',
    `pay_method`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。支付方法（子支付渠道）例pay_type = 2时。有WXPAY_JSAPI(21),WXPAY_APP(22),WXPAY_H5(23),WXPAY_NATIVE(24),WXPAY_MINI_PROGRAM(25)',
    `trade_mode`      smallint(6)                                                    NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)',
    `payee_id`        int(11)                                                        NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。2 自己收款(分销商自己收款) 收款人id 分销商id',
    `organization_id` int(11)                                                        NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款) 平台收款时 组织id',
    `app_id`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '扩展：定位的收款账户appid 可能错误。目前支付以前端传的appid为主，此appid 为前端传的appid',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `out_trade_no` (`out_trade_no`) USING BTREE,
    INDEX `customer_id` (`customer_id`) USING BTREE,
    INDEX `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '订单C端客户支付凭证表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pay_bills_distributor
-- ----------------------------
DROP TABLE IF EXISTS `pay_bills_distributor`;
CREATE TABLE `pay_bills_distributor`
(
    `id`               int(10) UNSIGNED                                               NOT NULL AUTO_INCREMENT COMMENT '订单id',
    `out_trade_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '支付凭证号(注意：全平台唯一)',
    `distributor_id`   int(10) UNSIGNED                                               NOT NULL COMMENT '支付分销客户id',
    `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '分销商用户名',
    `pay_type`         smallint(5) UNSIGNED                                           NOT NULL COMMENT '支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付',
    `business_type`    smallint(5) UNSIGNED                                           NOT NULL COMMENT '业务类型 1订单收款2在线充值收款',
    `business_id`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
    `pay_status`       smallint(5) UNSIGNED                                           NULL DEFAULT 0 COMMENT '订单状态: 0未支付， 1已支付，2已取消',
    `total_fee`        decimal(10, 3) UNSIGNED                                        NOT NULL COMMENT '支付金额',
    `order_title`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '订单标题',
    `order_describe`   varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '订单描述',
    `product_id`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '商品id',
    `online_trade_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '支付平台凭证号',
    `expire_time`      datetime(0)                                                    NULL DEFAULT NULL COMMENT '失效时间',
    `pay_time`         datetime(0)                                                    NULL DEFAULT NULL COMMENT '支付成功时间',
    `create_time`      datetime(0)                                                    NOT NULL COMMENT '创建时间',
    `update_time`      datetime(0)                                                    NOT NULL COMMENT '更新时间',
    `pay_method`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。支付方法（子支付渠道）例pay_type = 2时。有WXPAY_JSAPI(21),WXPAY_APP(22),WXPAY_H5(23),WXPAY_NATIVE(24),WXPAY_MINI_PROGRAM(25)',
    `trade_mode`       smallint(6)                                                    NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款), 2 自己收款(分销商自己收款)',
    `payee_id`         int(11)                                                        NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。2 自己收款(分销商自己收款) 收款人id 分销商id',
    `organization_id`  int(11)                                                        NULL DEFAULT NULL COMMENT '扩展：为了定位收款账户。1 平台方收款(比如：bat收款，bat收款) 平台收款时 组织id',
    `app_id`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '扩展：定位的收款账户appid 可能错误。目前支付以前端传的appid为主，此appid 为前端传的appid',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `out_trade_no` (`out_trade_no`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE,
    INDEX `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '订单分销客户支付凭证表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for refund_bills_customer
-- ----------------------------
DROP TABLE IF EXISTS `refund_bills_customer`;
CREATE TABLE `refund_bills_customer`
(
    `id`              int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '订单id',
    `customer_id`     int(10) UNSIGNED                                             NOT NULL COMMENT '退款分销客户id',
    `customer_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '分销商用户名',
    `out_trade_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付凭证号(注意：全平台唯一，根据支付凭证号进行退款)',
    `out_refund_no`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款凭证号(注意：全平台唯一)',
    `refund_type`     smallint(5) UNSIGNED                                         NOT NULL COMMENT '退款类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
    `business_type`   smallint(5) UNSIGNED                                         NOT NULL COMMENT '业务类型 1订单退款2充值退款',
    `business_id`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
    `refund_status`   smallint(5) UNSIGNED                                         NULL DEFAULT 1 COMMENT '退款状态: 1退款处理中， 2退款成功，3退款失败',
    `total_fee`       decimal(10, 3) UNSIGNED                                      NOT NULL COMMENT '退款金额(不能大于支付凭证金额)',
    `online_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台凭证号',
    `refund_time`     datetime(0)                                                  NULL DEFAULT NULL COMMENT '退款成功时间',
    `create_time`     datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)                                                  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `out_refund_no` (`out_refund_no`) USING BTREE,
    INDEX `customer_id` (`customer_id`) USING BTREE,
    INDEX `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '订单C端客户退款凭证表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for refund_bills_distributor
-- ----------------------------
DROP TABLE IF EXISTS `refund_bills_distributor`;
CREATE TABLE `refund_bills_distributor`
(
    `id`               int(10) UNSIGNED                                             NOT NULL AUTO_INCREMENT COMMENT '订单id',
    `distributor_id`   int(10) UNSIGNED                                             NOT NULL COMMENT '退款分销客户id',
    `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '分销商用户名',
    `out_trade_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付凭证号(注意：全平台唯一，根据支付凭证号进行退款)',
    `out_refund_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '退款凭证号(注意：全平台唯一)',
    `refund_type`      smallint(5) UNSIGNED                                         NOT NULL COMMENT '退款类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
    `business_type`    smallint(5) UNSIGNED                                         NOT NULL COMMENT '业务类型 1订单退款2充值退款',
    `business_id`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
    `refund_status`    smallint(5) UNSIGNED                                         NULL DEFAULT 1 COMMENT '退款状态: 1退款处理中， 2退款成功，3退款失败',
    `total_fee`        decimal(10, 3) UNSIGNED                                      NOT NULL COMMENT '退款金额(不能大于支付凭证金额)',
    `online_trade_no`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台凭证号',
    `refund_time`      datetime(0)                                                  NULL DEFAULT NULL COMMENT '退款成功时间',
    `create_time`      datetime(0)                                                  NOT NULL COMMENT '创建时间',
    `update_time`      datetime(0)                                                  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `out_refund_no` (`out_refund_no`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE,
    INDEX `online_trade_no` (`online_trade_no`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '订单分销客户退款凭证表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for refund_customer_apply
-- ----------------------------
DROP TABLE IF EXISTS `refund_customer_apply`;
CREATE TABLE `refund_customer_apply`
(
    `id`                    int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `customer_id`           int(10) UNSIGNED                                              NOT NULL COMMENT '退款接收方C端客户id',
    `customer_name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT 'C端客户用户名',
    `distributor_refund_id` int(10) UNSIGNED                                              NULL     DEFAULT NULL COMMENT '支付接收方分销商id（退款账号为2时有值）',
    `business_types`        smallint(5) UNSIGNED                                          NOT NULL DEFAULT 1 COMMENT '业务类型 1订单取消 2订单变更',
    `business_id`           int(10) UNSIGNED                                              NOT NULL COMMENT '业务单号',
    `amount`                decimal(16, 3)                                                NULL     DEFAULT 0.000 COMMENT '退款总金额',
    `cash_amount`           decimal(16, 3)                                                NULL     DEFAULT NULL COMMENT '现金支付退款金额',
    `deposit_amount`        decimal(16, 3)                                                NULL     DEFAULT NULL COMMENT '余额支付退款金额',
    `refund_type`           smallint(5) UNSIGNED                                          NULL     DEFAULT NULL COMMENT '退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式',
    `refund_mode`           smallint(6) UNSIGNED                                          NULL     DEFAULT NULL COMMENT '退款账号（谁的账号出）1 平台方退款(比如：bat收款，bat收款), 2 分销商',
    `apply_status`          smallint(5) UNSIGNED                                          NOT NULL DEFAULT 0 COMMENT '处理状态 0 未处理 1 已处理 2 已取消',
    `remark`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '备注',
    `operator_id`           int(11)                                                       NULL     DEFAULT NULL COMMENT '操作人id',
    `operator_name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '操作人名称',
    `create_time`           datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`           datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `customer_id` (`customer_id`) USING BTREE,
    INDEX `business_id` (`business_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'C端客户退款申请表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for refund_distributor
-- ----------------------------
DROP TABLE IF EXISTS `refund_distributor`;
CREATE TABLE `refund_distributor`
(
    `id`               int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `distributor_id`   int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '分销商用户名',
    `company_name`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名',
    `amount`           decimal(16, 3)                                                NOT NULL COMMENT '退款金额',
    `refund_way`       smallint(5) UNSIGNED                                          NOT NULL COMMENT '退款方式 1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付',
    `out_refund_no`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '分销商退款凭证id( 线上退款情况才有值)',
    `customer_flag`    smallint(6)                                                   NOT NULL COMMENT 'C端客户标志 0 不是C端客户(默认B2B) 1是C端客户',
    `currency_type`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '币种',
    `business_type`    smallint(5) UNSIGNED                                          NOT NULL COMMENT '业务类型 1订单退款2充值退款',
    `business_id`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)',
    `refund_status`    smallint(5) UNSIGNED                                          NOT NULL COMMENT '退款单状态,0未生效（微信退款回调中间状态）,1待确认,2已确认,3已取消',
    `remark`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '备注',
    `refund_erp_no`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT 'erp退款单编号',
    `create_time`      datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_out_refund_no` (`out_refund_no`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE,
    INDEX `business_id` (`business_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销商退款单表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for refund_distributor_apply
-- ----------------------------
DROP TABLE IF EXISTS `refund_distributor_apply`;
CREATE TABLE `refund_distributor_apply`
(
    `id`                    int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `distributor_id`        int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT '分销商用户名',
    `distributor_refund_id` int(10) UNSIGNED                                              NULL     DEFAULT NULL COMMENT '支付接收方分销商id（退款账号为2时有值）',
    `business_types`        smallint(5) UNSIGNED                                          NOT NULL DEFAULT 1 COMMENT '业务类型 1订单取消 2订单变更',
    `business_id`           int(10) UNSIGNED                                              NOT NULL COMMENT '业务单号',
    `amount`                decimal(16, 3)                                                NULL     DEFAULT 0.000 COMMENT '退款总金额',
    `cash_amount`           decimal(16, 3)                                                NULL     DEFAULT NULL COMMENT '现金支付退款金额',
    `deposit_amount`        decimal(16, 3)                                                NULL     DEFAULT NULL COMMENT '余额支付退款金额',
    `refund_type`           smallint(5) UNSIGNED                                          NULL     DEFAULT NULL COMMENT '退款方式，1为退回支付账户,2为退回用户余额,3其他退款方式',
    `refund_mode`           smallint(6) UNSIGNED                                          NULL     DEFAULT NULL COMMENT '退款账号（谁的账号出）1 平台方退款(比如：bat收款，bat收款), 2 分销商',
    `apply_status`          smallint(5) UNSIGNED                                          NOT NULL DEFAULT 0 COMMENT '处理状态 0 未处理 1 已处理 2 已取消',
    `remark`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '备注',
    `operator_id`           int(11)                                                       NULL     DEFAULT NULL COMMENT '操作人id',
    `operator_name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '操作人名称',
    `create_time`           datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`           datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE,
    INDEX `business_id` (`business_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销商退款申请表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for voucher_distributor
-- ----------------------------
DROP TABLE IF EXISTS `voucher_distributor`;
CREATE TABLE `voucher_distributor`
(
    `id`               int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `distributor_id`   int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '分销商用户名',
    `company_name`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名',
    `amount`           decimal(16, 3)                                                NOT NULL COMMENT '收款金额',
    `pay_way`          smallint(5) UNSIGNED                                          NOT NULL COMMENT '支付类型：1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,7.余额+支付宝,8.余额+微信,9.余额+快钱支付',
    `out_trade_no`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '分销客户支付凭证号(现款线上支付情况才有值)',
    `customer_flag`    smallint(6)                                                   NOT NULL COMMENT 'C端客户标志 0 不是C端客户(默认B2B) 1是C端客户',
    `currency_type`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '币种',
    `business_type`    smallint(5) UNSIGNED                                          NOT NULL COMMENT '业务类型 1订单收款2在线充值收款',
    `business_id`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '业务单号(业务类型为1时为订单id)(多个id,分割)',
    `voucher_status`   smallint(5) UNSIGNED                                          NOT NULL COMMENT '收款单状态,1待确认,2已确认,3已取消',
    `remark`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '备注',
    `voucher_erp_no`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT 'erp收款单编号',
    `create_time`      datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`      datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_out_trade_no` (`out_trade_no`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE,
    INDEX `business_id` (`business_id`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销商收款单表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for withdraw_deposits_distributor_apply
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_deposits_distributor_apply`;
CREATE TABLE `withdraw_deposits_distributor_apply`
(
    `id`                    int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `distributor_id`        int(10) UNSIGNED                                              NOT NULL COMMENT '分销商id',
    `distributor_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '分销商名称',
    `withdraw_amount`       decimal(16, 3)                                                NOT NULL DEFAULT 0.000 COMMENT '提现金额',
    `apply_status`          smallint(5) UNSIGNED                                          NOT NULL DEFAULT 1 COMMENT '确认状态 1,申请中,待确认 2,已确认 3,已拒绝',
    `withdraw_account_type` smallint(5) UNSIGNED                                          NULL     DEFAULT 0 COMMENT '提现账户类型： 1.支付宝,2.微信,3.银行',
    `wx_alipay_account`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT ' 支付宝或微信账户',
    `card_no`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '银行卡号',
    `payee`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '银行卡号',
    `bank_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT '收款银行',
    `remark`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '提现备注',
    `create_time`           datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time`           datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `distributor_id` (`distributor_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '分销客户提现申请表'
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
