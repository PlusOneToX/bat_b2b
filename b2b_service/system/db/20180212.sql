--增加图片宽度百分百
alter table `store_mobile_item` add `width_percentage` decimal(16,3) unsigned DEFAULT NULL COMMENT '图片宽度百分百' AFTER `image_url`;