DROP TABLE IF EXISTS `order_type`;
CREATE TABLE `order_type`
(
    `id`          int(10) UNSIGNED                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '订单类型名称',
    `erp_type`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT 'erp订单类型',
    `desc`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '订单类型描述',
    `editable`    smallint(5) UNSIGNED                                          NOT NULL DEFAULT 1 COMMENT '是否可编辑：1-可编辑 0-不可编辑',
    `open_flag`   smallint(5) UNSIGNED                                          NOT NULL DEFAULT 1 COMMENT '是否启用, 0为停用，1为启用',
    `create_time` datetime(0)                                                   NOT NULL COMMENT '创建时间',
    `update_time` datetime(0)                                                   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '订单类型表'
  ROW_FORMAT = DYNAMIC;