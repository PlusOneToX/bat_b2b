--增加价格等级是否建议零售价字段
alter table `scale_price`
    add `retail_flag` smallint unsigned DEFAULT '0' COMMENT '否建议零售价, 0否，1是' AFTER `description`;