INSERT INTO order_type(id,
                       `name`,
                       erp_type,
                       `desc`,
                       editable,
                       open_flag,
                       create_time,
                       update_time)
select id,
       order_type_name                                          `name`,
       order_type_value                                         erp_type,
       order_type_desc                                          `desc`,
       0                                                        editable,
       editable                                                 open_flag,
       FROM_UNIXTIME(`create_time` / 1000, '%Y-%m-%d %H:%i:%S') create_time,
       FROM_UNIXTIME(`update_time` / 1000, '%Y-%m-%d %H:%i:%S') update_time
from b2bdev.order_type