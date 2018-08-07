-- ----------------------------
-- Table transfer for check module
-- ----------------------------
INSERT INTO system_db.`check`
(id,
 ext,
 sub_ext,
 sub_ext1,
 apply_user,
 status,
 last_check_user,
 next_check_user,
 through_check_count,
 check_user_count,
 create_time,
 update_time,
 last_check_time)
SELECT id,
       ext,
       sub_ext,
       sub_ext1,
       apply_user,
       `status`,
       last_check_user,
       next_check_user,
       through_check_count,
       check_user_count,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`last_check_time` / 1000, '%Y-%m-%d %H:%i:%S')
FROM b2b.`check`;

INSERT INTO system_db.check_config(id,
                                   ext,
                                   check_user,
                                   check_order,
                                   open_flag,
                                   create_time,
                                   update_time)
SELECT `id`,
       `ext`,
       `check_user`,
       `check_order`,
       `is_open`,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S')
FROM b2b.check_config;

INSERT INTO system_db.check_flow(id,
                                 check_id,
                                 check_user,
                                 check_status,
                                 check_time,
                                 remark,
                                 check_order)
SELECT id,
       check_id,
       check_user,
       check_status,
       FROM_UNIXTIME(`check_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       remark,
       check_order
FROM b2b.check_flow;

INSERT INTO system_db.check_relation(id,
                                     ext,
                                     create_time,
                                     update_time)
SELECT `id`,
       `ext`,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S')
FROM b2b.check_relation;
-- ----------------------------
-- Table transfer for export_download module nothing
-- ----------------------------

-- ----------------------------
-- Table transfer for download_center module and training_center module
-- ----------------------------

INSERT INTO system_db.download_center(id,
                                      parent_id,
                                      sort,
                                      status,
                                      title_zh,
                                      title_en,
                                      content_url_zh,
                                      content_url_en,
                                      thumbnail_url_zh,
                                      thumbnail_url_en,
                                      create_time,
                                      update_time)
SELECT id,
       parent_id,
       sort,
       status,
       title_zh,
       title_en,
       content_url_zh,
       content_url_en,
       thumbnail_url_zh,
       thumbnail_url_en,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S')
from b2b.download_center;

INSERT INTO system_db.training_center(id,
                                      parent_id,
                                      sort,
                                      status,
                                      title_zh,
                                      title_en,
                                      content_url_zh,
                                      content_url_en,
                                      thumbnail_url_zh,
                                      thumbnail_url_en,
                                      create_time,
                                      update_time)
SELECT id,
       parent_id,
       sort,
       status,
       title_zh,
       title_en,
       content_url_zh,
       content_url_en,
       thumbnail_url_zh,
       thumbnail_url_en,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S')
from b2b.training_center;

-- ----------------------------
-- Table transfer for global setting module
-- ----------------------------

-- global_agreement-xx is new table
-- global_base_setting is new table
INSERT INTO `system_db`.`global_base_setting`(`key`, `value`, `desc`)
VALUES ('distribution_layers', '3', '分销商层级');
INSERT INTO `system_db`.`global_base_setting`(`key`, `value`, `desc`)
VALUES ('login_open_flag', '1', '是否开启登录 0否 1是');
INSERT INTO `system_db`.`global_base_setting`(`key`, `value`, `desc`)
VALUES ('login_tips', '11尊敬的客户，请您仔细核对订单明细，订单下达后将直接到仓库发货，无法中途取消，下错订单取消需要走退货流程，退款在15个工作日左右完成.', '登录提示 关闭登录时必填');
INSERT INTO `system_db`.`global_base_setting`(`key`, `value`, `desc`)
VALUES ('login_tips_en', 'welcome', '登录英文提示 关闭登录时必填');
INSERT INTO `system_db`.`global_base_setting`(`key`, `value`, `desc`)
VALUES ('not_delivery_unit', '2', '长时间未发货定义天数');


INSERT INTO system_db.global_factory_setting_delay_push(id,
                                                        factory,
                                                        push_time,
                                                        use_range,
                                                        type)
SELECT id,
       factory,
       FROM_UNIXTIME(`push_time` / 1000, '%Y-%m-%d %H:%i:%S') push_time,
       use_range,
       type
from b2b.delay_push;

INSERT INTO system_db.global_factory_setting_order_invalid(id,
                                                           name,
                                                           order_source,
                                                           invalid)
SELECT id,
       name,
       order_source,
       invalid
from b2b.order_invalid;

INSERT INTO global_shop_setting(`key`,
                                `value`,
                                `desc`)
SELECT "customized_attend_event_flag" AS `key`,
       customized_attend_event_flag   AS `value`,
       '定制商品是否参与活动'                   AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "direct_transportation_event_flag" AS `key`,
       direct_transportation_event_flag   AS `value`,
       '直运订单是否参与活动'                       AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "hint" AS `key`,
       hint   AS `value`,
       '提示语'  AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "mto_attend_event_flag" AS `key`,
       mto_attend_event_flag   AS `value`,
       'mto订单是否参与活动'           AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "newproduct_time" AS `key`,
       newproduct_time   AS `value`,
       '上架多少天内为新品时间'     AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "no_stiff_use_hint" AS `key`,
       no_stiff_use_hint   AS `value`,
       '非直发分销商采用提示语 0否 1是' AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "on_way_attend_event_flag" AS `key`,
       on_way_attend_event_flag   AS `value`,
       '在途商品是否参与活动'               AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "stiff_use_hint"   AS `key`,
       stiff_use_hint     AS `value`,
       '直发分销商采用提示语 0否 1是' AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "stock_show_flag"          AS `key`,
       stock_show_flag            AS `value`,
       '库存显示方式：0-显示实际库存，1-显示模糊库存' AS `desc`
FROM b2b.system_config
UNION ALL
SELECT "stock_show_number" AS `key`,
       stock_show_number   AS `value`,
       '库存显示临界值'           AS `desc`
FROM b2b.system_config;

-- ----------------------------
-- Table transfer for system_db.logistics module
-- ----------------------------
INSERT INTO system_db.logistics (id,
                                 `name`,
                                 sort,
                                 `enable`,
                                 description,
                                 logistics_erp_id,
                                 billing_method,
                                 first_weight,
                                 first_volume,
                                 additional_weight,
                                 additional_volume,
                                 min_weight,
                                 min_volume,
                                 max_weight,
                                 max_volume,
                                 min_cost,
                                 use_range,
                                 logistics_factory_id,
                                 logistics_kdn_code,
                                 logistics_kdn_name,
                                 appoint_area_flag,
                                 distributor_scope,
                                 manufactors,
                                 website,
                                 material_id,
                                 create_time,
                                 update_time)
SELECT id,
       `name`,
       distribution_order AS sort,
       is_open            AS `enable`,
       description,
       distribution_erp_id,
       1,
       first_weight,
       0,
       additional_weight,
       0,
       min_weight,
       0,
       max_weight,
       0,
       min_cost,
       case use_range
           when '1' then '1'
           when '2' then '2'
           when
               '1,2' then '3'
           end,
       distribution_factory_id,
       distribution_kdn_code,
       distribution_kdn_name,
       is_appoint_area    AS appoint_area_flag,
       distributor_scope,
       manufactors,
       website,
       null               as material_id,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S')
FROM b2b.distribution;

INSERT INTO system_db.logistics_area (id,
                                      logistics_id,
                                      first_weight_cost,
                                      first_volume_cost,
                                      additional_weight_cost,
                                      additional_volume_cost,
                                      default_flag,
                                      formula_flag,
                                      formula,
                                      create_time,
                                      update_time,
                                      group_id,
                                      country_id,
                                      province_id,
                                      city_id,
                                      district_id)
SELECT id,
       distribution_id,
       first_weight_cost,
       0,
       additional_weight_cost,
       0,
       is_default AS default_flag,
       is_formula AS formula_flag,
       formula,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       group_id,
       37            country_id,
       province_id,
       city_id,
       0             district_id
FROM b2b.distribution_area
GROUP BY distribution_id, formula, province_id, city_id;

INSERT INTO system_db.logistics_distributor_grade(id,
                                                  logistics_id,
                                                  distributor_grade_id)
SELECT id,
       distribution_id,
       distributor_grade_id
FROM b2b.distribution_distributor_grade_scope;

INSERT INTO system_db.logistics_department(id,
                                           logistics_id,
                                           department_id)
SELECT id,
       distribution_id,
       department_id
FROM b2b.distribution_department_scope;

INSERT INTO system_db.logistics_user(id,
                                     logistics_id,
                                     user_id)
SELECT id,
       distribution_id,
       admin_id user_id
FROM b2b.distribution_admin_scope;

INSERT INTO system_db.logistics_distributor(id,
                                            logistics_id,
                                            distributor_id)
SELECT id,
       distribution_id,
       distributor_id
FROM b2b.distribution_distributor_scope;

INSERT INTO system_db.logistics_third_mapping(id,
                                              logistics_id,
                                              third_type,
                                              status,
                                              third_delivery_no,
                                              remark)
SELECT id,
       distribution_id,
       third_type,
       status,
       third_delivery_no,
       remark
FROM b2b.third_delivery_rela;

-- ----------------------------
-- Table transfer for region module
-- ----------------------------

INSERT INTO system_db.region(id,
                             region_name,
                             parent_id,
                             have_next,
                             region_name_en,
                             level)
SELECT id,
       region_name,
       parent_id,
       have_next,
       region_name_en,
       0
FROM b2b.region;

update system_db.region
set level=1
where parent_id = 0;

update system_db.region
set level=2
where parent_id in (select id from (select id from region where level = 1) a);

update system_db.region
set level=3
where parent_id in (select id from (select id from region where level = 2) a);

INSERT INTO system_db.region_comparison(id,
                                        region_id,
                                        region_name,
                                        another_name,
                                        parent_id)
SELECT id,
       region_id,
       region_name,
       another_name,
       parent_id
FROM b2b.region_comparison;

-- ----------------------------
-- Table transfer for store module
-- ----------------------------

INSERT INTO system_db.`store_banner`(id,
                                     image_url,
                                     banner_url,
                                     create_time,
                                     banner_area,
                                     sort)
SELECT id,
       image_url,
       extension_url,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       extension_area,
       order_value
FROM b2b.`extension`;

INSERT INTO store_notice(id,
                         title,
                         content,
                         attachment_name,
                         attachment_url,
                         release_area,
                         release_status,
                         release_time,
                         cancel_time,
                         create_time,
                         update_time)
SELECT id,
       title,
       content_url,
       attachment_name,
       attachment_url,
       0                                                         release_area,
       release_status,
       FROM_UNIXTIME(`release_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       FROM_UNIXTIME(`cancel_time` / 1000, '%Y-%m-%d %H:%i:%S')  create_time,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S')  create_time,
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S')  create_time
FROM b2b.`announcement`;

INSERT INTO system_db.`store_column`(id,
                                     title,
                                     title_en,
                                     banner_img,
                                     sort,
                                     release_status,
                                     column_area,
                                     distributor_scope,
                                     create_time,
                                     update_time)
SELECT id,
       title,
       title_en,
       null,
       column_order,
       release_status,
       column_area,
       distributor_scope,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time
FROM b2b.`column`;

INSERT INTO system_db.`store_column_department`(id,
                                                column_id,
                                                department_id)
SELECT id,
       column_id,
       department_id
FROM b2b.`column_department_scope`;

INSERT INTO system_db.`store_column_distributor`(id,
                                                 column_id,
                                                 distributor_id)
SELECT id,
       column_id,
       distributor_id
FROM b2b.`column_distributor_scope`;

INSERT INTO system_db.`store_column_distributor_grade`(id,
                                                       column_id,
                                                       distributor_grade_id)
SELECT id,
       column_id,
       distributor_grade_id
FROM b2b.`column_distributor_grade_scope`;

INSERT INTO system_db.`store_column_user`(id,
                                          column_id,
                                          user_id)
SELECT id,
       column_id,
       admin_id
FROM b2b.`column_admin_scope`;

INSERT INTO system_db.`store_section`(id,
                                      title,
                                      title_en,
                                      sort,
                                      release_status,
                                      image_url,
                                      extension_url,
                                      image_url_en,
                                      extension_url_en,
                                      style_type,
                                      style_type_en,
                                      section_area,
                                      style_url,
                                      style_extension_url,
                                      style_url1,
                                      style_extension_url1,
                                      style_url2,
                                      style_extension_url2,
                                      style_url3,
                                      style_extension_url3,
                                      create_time,
                                      update_time)
SELECT id,
       title,
       title_en,
       section_order,
       release_status,
       image_url,
       extension_url,
       image_url_en,
       extension_url_en,
       style_type,
       style_type_en,
       section_area,
       style_url,
       style_extension_url,
       style_url1,
       style_extension_url1,
       style_url2,
       style_extension_url2,
       style_url3,
       style_extension_url3,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time
FROM b2b.`section`;

-- ----------------------------
-- Table transfer for system module
-- ----------------------------

INSERT INTO system_db.sys_user(id,
                               user_name,
                               real_name,
                               password,
                               mobile,
                               email,
                               ding_avatar,
                               admin_type,
                               brand_scope,
                               sale_scope,
                               status,
                               erp_user_no,
                               organization_id,
                               department_id,
                               rock_account_id,
                               fictitious_flag,
                               sale_flag,
                               remark,
                               create_time,
                               update_time)
SELECT `id`,
       `user_name`,
       `name`          as real_name,
       `password`,
       `mobile`,
       null            as email,
       `ding_avatar`,
       `admin_type`,
       `brand_scope`,
       `sale_scope`,
       `status`,
       `number`        as erp_user_no,
       `organization_id`,
       `department_id`,
       `rock_account_id`,
       `is_fictitious` as fictitious_flag,
       `is_sale`       as sale_flag,
       `remark`,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S'),
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S')
FROM b2b.admin;

INSERT INTO system_db.sys_role(id,
                               role_name,
                               role_name_en,
                               role_description,
                               create_time,
                               update_time)
SELECT id,
       role_name,
       null                                                     role_name_en,
       role_description,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S') update_time
FROM b2b.role;

INSERT INTO system_db.sys_user_role(id,
                                    user_id,
                                    role_id)
SELECT id,
       user_id,
       role_id
FROM b2b.admin_role;

INSERT INTO system_db.sys_user_brand(id,
                                     user_id,
                                     brand_id)
SELECT id,
       user_id,
       brand_id
from b2b.admin_brand;

INSERT INTO system_db.sys_user_sale(id,
                                    user_id,
                                    sale_user_id)
SELECT id,
       user_id,
       sale_user_id
from b2b.admin_sale;

INSERT INTO system_db.sys_user_login(id,
                                     last_login_time,
                                     login_times)
SELECT -- id,
       user_id,
       -- web_token,
       -- last_online_time,
       FROM_UNIXTIME(`last_login_time` / 1000, '%Y-%m-%d %H:%i:%S') last_login_time,
       times
from b2b.admin_login;


-- INSERT INTO system_db.sys_role_menu (id, role_id, menu_id)
-- SELECT b.id,
--        b.role_id,
--        a.id
-- FROM system_db.sys_menu a,
--      b2b.role_menu b
-- WHERE a.menu = b.module;

INSERT INTO system_db.`sys_department`(id,
                                       parent_id,
                                       organization_id,
                                       department_name,
                                       department_name_en,
                                       sort,
                                       erp_department_id,
                                       description,
                                       sale_type,
                                       status,
                                       create_time,
                                       update_time)
SELECT id,
       0                                                        parent_id,
       organization_id,
       name                                                     department_name,
       null                                                     department_name_en,
       0,
       erp_department_id,
       description,
       is_sale                                                  sale_type,
       1                                                        status,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S') update_time
FROM b2b.`department`;

INSERT INTO system_db.`sys_organization`(id,
                                         name,
                                         erp_organization_id,
                                         description,
                                         status,
                                         create_time,
                                         update_time)
SELECT id,
       name,
       erp_organization_id,
       description,
       1                                                        status,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S') update_time
FROM b2b.`sale_organization`;