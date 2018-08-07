/*
 * @Author: 陈良顺
 * @Date:   2018-03-19 10:47:51
 * @Last Modified by: lijiemin
 * @Last Modified time: 2018-03-01 22:07:18
 */

export const auth = [{
  title: '商品模块',
  auth: {
    actions: [{
      name: '商品分类管理',
      module: '',
      children: [{
        name: '查看',
        module: 'category-get'
      }, {
        name: '管理',
        module: 'category-manage'
      }]
    }, {
      name: '商品品牌管理',
      module: '',
      children: [{
        name: '查看',
        module: 'brand-get'
      }, {
        name: '管理',
        module: 'brand-manage'
      }]
    }, {
      name: '商品品类管理',
      module: '',
      children: [{
        name: '查看',
        module: 'productline-get'
      }, {
        name: '管理',
        module: 'productline-manage'
      }]
    }, {
      name: '商品属性管理',
      module: '',
      children: [{
        name: '查看',
        module: 'specification-get'
      }, {
        name: '管理',
        module: 'specification-manage'
      }]
    },
    // {
    //   name: '商品等级管理',
    //   module: '',
    //   children: [{
    //     name: '查看',
    //     module: 'item-grade'
    //   }, {
    //     name: '等级设置',
    //     module: 'goods-grade-setting'
    //   }, {
    //     name: '规则设置',
    //     module: 'item-grade-setting'
    //   }, {
    //     name: '等级管理',
    //     module: 'item-grade-change'
    //   }, {
    //     name: '审批',
    //     module: 'item-grade-change-check'
    //   }]
    // },
    {
      name: '商品管理',
      module: '',
      children: [{
        name: '查看',
        module: 'goods-get'
      }, {
        name: '新增',
        module: 'goods-post'
      }, {
        name: '编辑',
        module: 'goods-put'
      }, {
        name: '删除',
        module: 'goods-delete'
      }, {
        name: '冻结解冻',
        module: 'goods-freeze'
      }, {
        name: '上下架',
        module: 'goods-updown'
      }, {
        name: '审批',
        module: 'goods-check'
      }, {
        name: '同步信息',
        module: 'goods-syncgoodsitem'
      }]
    }, {
      name: '限制购买量管理',
      module: '',
      children: [{
        name: '查看',
        module: 'goods-purchaselimit-get'
      }, {
        name: '管理',
        module: 'goods-purchaselimit-manage'
      }]
    }, {
      name: '报价单',
      module: '',
      children: [{
        name: '导出',
        module: 'goods-exportPrice'
      }]
    }, {
      name: '商品属性管理',
      module: '',
      children: [{
        name: '第三方材质型号关联',
        module: 'thirdskurela'
      }]
    }, {
      name: '商品销量管理',
      module: '',
      children: [{
        name: '查看',
        module: 'goods-get'
      }, {
        name: '设置',
        module: 'goods-post'
      }]
    }],
    menus: [{
      name: '商品管理',
      module: '',
      children: [{
        name: '商品列表',
        module: 'goods-list'
      }, {
        name: '限购规则列表',
        module: 'goods-purchaselimit'
      }, {
        name: '冻结商品列表',
        module: 'goods-freezed'
      }, {
        name: '商品销量管理',
        module: 'goods-get'
      }]
    }, {
      name: '商品等级管理',
      module: '',
      children: [{
        name: '等级列表',
        module: 'good-grade-base'
      }, {
        name: '等级规则设置',
        module: 'item-grade-setting'
      }, {
        name: '商品等级列表',
        module: 'item-grade-list'
      }, {
        name: '等级变动列表',
        module: 'item-grade-change-list'
      }]
    }, {
      name: '商品审批',
      module: '',
      children: [{
        name: '商品审批列表',
        module: 'goods-check'
      }, {
        name: '商品等级变动审批',
        module: 'goods-grade-check'
      }]
    }, {
      name: '商品分类管理',
      module: '',
      children: [{
        name: '商品分类列表',
        module: 'category-list'
      }, {
        name: '商品标签列表',
        module: 'goods-label-list'
      }]
    }, {
      name: '品牌品类管理',
      module: '',
      children: [{
        name: '品牌列表',
        module: 'brand-list'
      }, {
        name: '品类列表',
        module: 'productline-list'
      }]
    }, {
      name: '商品属性管理',
      module: '',
      children: [{
        name: '商品属性列表',
        module: 'specification-list'
      }, {
        name: '定制商品材料列表',
        module: 'material-list'
      }, {
        name: '定制商品型号列表',
        module: 'model-list'
      }, {
        name: '材料型号关联列表',
        module: 'material-model-list'
      }, {
        name: '第三方材质型号关联',
        module: 'thirdskurela'
      }]
    }]
  }
},
{
  title: '分销商模块',
  auth: {
    actions: [{
      name: '销售区域管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distributor-salearea-get'
      }, {
        name: '管理',
        module: 'distributor-salearea-manage'
      }]
    }, {
      name: '分销商分组管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distributor-group-get'
      }, {
        name: '管理',
        module: 'distributor-group-manage'
      }]
    }, {
      name: '合作中分销商管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distributor-cooperating-get'
      }, {
        name: '新增',
        module: 'distributor-cooperating-post'
      }, {
        name: '编辑',
        module: 'distributor-cooperating-put'
      }, {
        name: '审批',
        module: 'distributor-cooperating-check'
      }, {
        name: '冻结解冻',
        module: 'distributor-cooperating-freeze'
      }, {
        name: '同步ERP',
        module: 'distributor-erp-put'
      }]
    }, {
      name: '申请中分销商管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distributor-applying-get'
      }, {
        name: '编辑',
        module: 'distributor-applying-put'
      }, {
        name: '审批',
        module: 'distributor-applying-check'
      }, {
        name: '删除',
        module: 'distributor-applying-delete'
      }]
    }, {
      name: '分销商等级管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distributor-grade-get'
      }, {
        name: '管理',
        module: 'distributor-grade-manage'
      }]
    }, {
      name: '分销商类别管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distributor-category-get'
      }, {
        name: '管理',
        module: 'distributor-category-manage'
      }]
    }],
    menus: [{
      name: '分销商设置',
      module: '',
      children: [{
        name: '销售区域',
        module: 'distributor-salearea'
      }, {
        name: '分销商等级',
        module: 'distributor-grade'
      }, {
        name: '分销商分组',
        module: 'distributor-group'
      }, {
        name: '分销商类别',
        module: 'distributor-category'
      }]
    }, {
      name: '合作中分销商列表',
      module: 'distributorCooperating-list',
      children: [
        //   {
        //   name: '合作中分销商列表',
        //   module: 'distributorCooperating-list'
        // }
        // , {
        //   name: '添加合作分销商',
        //   module: 'distributorCooperating-add'
        // }
      ]
    }, {
      name: '申请中的分销商列表',
      module: 'distributorApplying',
      children: []
    }, {
      name: '分销商审批列表',
      module: 'distributorCooperating-check',
      children: []
    }, {
      name: '已冻结的分销商列表',
      module: 'distributorFreezing',
      children: []
    }]
  }
}, {
  title: '柔性模块',
  auth: {
    actions: [{
      name: '定制材质型号管理',
      module: 'rx-material-model-manage',
      children: []
    }, {
      name: '定制标签管理',
      module: 'rx-label-manage',
      children: []
    }, {
      name: '定制图片管理',
      module: 'rx-picture-manage',
      children: []
    }, {
      name: '定制字体管理',
      module: 'rx-font-manage',
      children: []
    }],
    menus: [{
      name: '图库管理',
      module: '',
      children: [{
        name: '图库分类列表',
        module: 'pictureCategory-list'
      }, {
        name: '图片列表',
        module: 'picture-list'
      }]
    }, {
      name: '标签管理',
      module: '',
      children: [{
        name: '标签列表',
        module: 'label-list'
      }]
    }, {
      name: '字体管理',
      module: '',
      children: [{
        name: '字体列表',
        module: 'font-list'
      }]
    }, {
      name: '柔性店铺配置',
      module: '',
      children: [{
        name: '轮播图管理',
        module: 'distributorbanner'
      }, {
        name: '系列展示管理',
        module: 'distributorseriestheme'
      }, {
        name: '推荐管理',
        module: 'distributorindexrecommend'
      }, {
        name: '官方主题管理',
        module: 'thememanage'
      }, {
        name: '店铺管理',
        module: 'storemanage'
      }]
    }, {
      title: '卡券兑换',
      module: '',
      children: [{
        name: '兑换活动列表',
        module: 'exchangecard'
      }, {
        name: '兑换码列表',
        module: 'exchangecode'
      }, {
        name: '兑换数据列表',
        module: 'exchangecodeorder'
      }, {
        name: '电子兑换卡导出记录表',
        module: 'exchangeExport'
      }]
    }, {
      title: '营销&专题活动',
      module: '',
      children: [{
        name: '转发活动配置',
        module: 'exchangeShare'
      }, {
        name: '营销专题活动',
        module: 'exchangeSpecial'
      }]
    }, {
      name: '产品管理',
      module: '',
      children: [{
        name: '产品类型列表',
        module: 'productCategory-list'
      }]
    }]
  }
}, {
  title: '仓库模块',
  auth: {
    actions: [{
      name: '基本设置',
      module: 'warehouseSetting',
      children: []
    }, {
      name: '仓库管理',
      module: '',
      children: [{
        name: '新增',
        module: 'warehouse-post'
      }, {
        name: '编辑',
        module: 'warehouse-put'
      }, {
        name: '查看',
        module: 'warehouse-get'
      }, {
        name: '删除',
        module: 'warehouse-delete'
      }, {
        name: '重置锁定数量',
        module: 'warehouse-resetlockstock'
      }, {
        name: '预留',
        module: 'warehousestock-reserved'
      }]
    }],
    menus: [{
      name: '仓库设置',
      module: '',
      children: [{
        name: '基本设置',
        module: 'warehouseSetting'
      }, {
        name: '仓库列表',
        module: 'warehouseList'
      }, {
        name: '添加仓库',
        module: 'warehouseAdd'
      }, {
        name: '仓库回收站',
        module: 'warehouseRecycle'
      }]
    }, {
      name: '商品库存列表',
      module: 'warehouseStockList',
      children: []
    }, {
      name: '预留明细',
      module: 'warehousestockReserved',
      children: []
    }]
  }
}, {
  title: '订单模块',
  auth: {
    actions: [{
      name: '发货单管理',
      module: 'order-delivergoods',
      children: []
    }, {
      name: '订单类型管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distributor-ordertype-get'
      }, {
        name: '管理',
        module: 'distributor-ordertype-manage'
      }]
    }, {
      name: '订单统计',
      module: 'order-growthrate',
      children: [{
        name: '订单增长率',
        module: 'order-growthrate'
      }, {
        name: '导出订单折扣明细',
        module: 'order-discount-download-post'
      }, {
        name: '查看VMI订单明细',
        module: 'order-vmi-post'
      }, {
        name: '导出VMI订单明细',
        module: 'order-vmi-download-post'
      }]
    }, {
      name: '订单管理',
      module: '',
      children: [{
        name: '查看',
        module: 'order-get'
      },
      // {
      //   name: '确认订单',
      //   module: 'order-confirm'
      // },
      // {
      //     name: '作废订单',
      //     module: 'order-invalid'
      // },
      {
        name: '订单操作',
        module: 'order-operate'
      },
      // {
      //   name: '设为已付款',
      //   module: 'order-pay'
      // },
      // {
      //     name: '取消订单',
      //     module: 'order-cancel'
      // },
      {
        name: '订单导入',
        module: 'importorder'
      },
      {
        name: '订单发货',
        module: 'order-put'
      }
      ]
    }, {
      name: '缺货登记管理',
      module: '',
      children: [{
        name: '查看',
        module: 'order-losegoods-get'
      }, {
        name: '管理',
        module: 'order-losegoods-manage'
      }]
    }],
    menus: [{
      name: '订单管理',
      module: '',
      children: [{
        name: '订单列表',
        module: 'order-list'
      }, {
        name: '缺货登记',
        module: 'order-losegoods'
      }, {
        name: '批量导入订单',
        module: 'importorder'
      }, {
        name: '柔性定制订单列表',
        module: 'diy_order_list'
      }, {
        name: '柔性定制店铺订单列表',
        module: 'diy_store_order_list'
      }]
    }, {
      name: '发货单',
      module: 'order-delivergoods-list',
      children: []
    }, {
      name: '订单类型',
      module: 'orderType',
      children: []
    }, {
      name: '订单统计',
      module: '',
      children: [{
        name: '订单增长率',
        module: 'order-growthrate'
      }, {
        name: '订单折扣明细',
        module: 'order-discount'
      }, {
        name: 'VMI订单明细',
        module: 'order-vmi'
      }]
    }]
  }
}, {
  title: '财务模块',
  auth: {
    actions: [{
      name: '预存款设置',
      module: 'financial-deposit',
      children: []
    }, {
      name: '线下账户设置',
      module: '',
      children: [{
        name: '管理',
        module: 'bank'
      }]
    }, {
      name: '汇率设置',
      module: '',
      children: [{
        name: '查看',
        module: 'rate-get'
      }, {
        name: '管理',
        module: 'rate-manage'
      }]
    }, {
      name: '充值管理',
      module: '',
      children: [{
        name: '查看',
        module: 'deposit-recharge-get'
      }, {
        name: '管理',
        module: 'deposit-recharge-manage'
      }]
    }, {
      name: '分销商账户管理',
      module: '',
      children: [{
        name: '微信支付账户管理',
        module: 'wx-account-manage'
      }, {
        name: '微信公众号管理',
        module: 'wx-platform-manage'
      }, {
        name: '支付账户管理',
        module: 'alipay-account-manage'
      }]
    }, {
      name: '提现管理',
      module: '',
      children: [{
        name: '查看',
        module: 'deposit-withdraw-get'
      }, {
        name: '管理',
        module: 'deposit-withdraw-manage'
      }]
    }, {
      name: '预存款管理',
      module: '',
      children: [{
        name: '查看明细账',
        module: 'deposit-detail-get'
      }, {
        name: '查看余额表',
        module: 'deposit-available-get'
      }, {
        name: '冻结解冻余额',
        module: 'deposit-freezing'
      }]
    }, {
      name: '销售往来单据',
      module: '',
      children: [{
        name: '查看收款单',
        module: 'order-voucher'
      }, {
        name: '同步收款单',
        module: 'order-syncvoucher'
      }, {
        name: '查看退款单',
        module: 'order-refund'
      }, {
        name: '查看退款申请单',
        module: 'order-refund-apply'
      }, {
        name: '管理退款单',
        module: 'order-refund-manage'
      }, {
        name: '管理退款申请单',
        module: 'order-refund-apply-manage'
      }, {
        name: '同步退款单',
        module: 'order-syncrefund'
      }]
    }],
    menus: [{
      name: '基本设置',
      module: '',
      children: [{
        name: '预存款设置',
        module: 'financial-deposit'
      }, {
        name: '线下账户设置',
        module: 'bank'
      }, {
        name: '汇率设置',
        module: 'rate'
      }, {
        name: '币别',
        module: 'currency'
      }]
    }, {
      name: '分销商账户',
      module: '',
      children: [{
        name: '微信支付账户列表',
        module: 'wx-account-list'
      }, {
        name: '微信公众号列表',
        module: 'wx-platform-list'
      }, {
        name: '支付宝账户列表',
        module: 'alipay-account-list'
      }]
    }, {
      name: '预存款',
      module: '',
      children: [{
        name: '充值列表',
        module: 'deposit-recharge'
      }, {
        name: '提现列表',
        module: 'deposit-withdraw'
      }, {
        name: '冻结列表',
        module: 'deposit-freezing'
      }]
    }, {
      name: '预存款明细',
      module: '',
      children: [{
        name: '明细账',
        module: 'deposit-available'
      }, {
        name: '余额表',
        module: 'deposit-detail'
      }]
    }, {
      name: '销售往来单据',
      module: '',
      children: [{
        name: '收款单列表',
        module: 'order-voucher'
      }, {
        name: '退款单列表',
        module: 'order-refund'
      }, {
        name: '退款申请单列表',
        module: 'order-refund-apply'
      }]
    }]
  }
}, {
  title: '营销推广模块',
  auth: {
    actions: [{
      name: '促销活动管理',
      module: '',
      children: [{
        name: '查看',
        module: 'promotion'
      }, {
        name: '管理',
        module: 'promotion-edit'
      }, {
        name: '审批',
        module: 'promotion-check'
      }, {
        name: '提前结束',
        module: 'promotion-retract'
      }, {
        name: '批量导入',
        module: 'promotion-import'
      }, {
        name: '导出活动',
        module: 'promotion-export'
      }, {
        name: '优惠券管理',
        module: 'coupon-manage'
      }]
    }, {
      name: '统一政策管理',
      module: '',
      children: [{
        name: '查看',
        module: 'policy'
      }, {
        name: '管理',
        module: 'policy-edit'
      }, {
        name: '审批',
        module: 'policy-check'
      }]
    }, {
      name: '拼团管理',
      module: '',
      children: [{
        name: '拼团列表',
        module: 'spellGroup'
      }, {
        name: '删除',
        module: 'groupGoods'
      }]

    }, {
      name: '卡券兑换',
      module: '',
      children: [{
        name: '兑换活动列表',
        module: 'exchangecard'
      }, {
        name: '兑换码列表',
        module: 'exchangecode'
      }, {
        name: '查看暗码',
        module: 'exchangecodeencode'
      }]
    }, {
      name: '兑换数据列表',
      module: '',
      children: [{
        name: '查看',
        module: 'exchangecodeorder'
      }, {
        name: '导出',
        module: 'exchangecodeorder-export'
      }]
    }, {
      name: '服务费专区',
      module: '',
      children: [{
        name: '查看',
        module: 'service-fee-get'
      }, {
        name: '管理',
        module: 'service-fee-manage'
      }]
    }],
    menus: [{
      name: '促销活动',
      module: '',
      children: [{
        name: '促销活动列表',
        module: 'promotion-list'
      }, {
        name: '批量活动导入',
        module: 'promotion-import'
      }]
    }, {
      name: '统一政策',
      module: '',
      children: [{
        name: '商品等级折扣',
        module: 'policy-post'
      }, {
        name: '提货增长返利',
        module: 'policy-rebate'
      }]
    }, {
      name: '活动审核',
      module: '',
      children: [{
        name: '促销活动审批列表',
        module: 'promotion-check'
      }, {
        name: '统一政策审批列表',
        module: 'policy-check'
      }]
    }, {
      name: '优惠券',
      module: '',
      children: [{
        name: '优惠券列表',
        module: 'coupon-list'
      }]
    }, {
      name: '拼团',
      module: '',
      children: [{
        name: '拼团列表',
        module: 'spellGroup'
      }]
    }, {
      name: '卡券兑换',
      module: '',
      children: [{
        name: '',
        module: 'exchangecard'
      }, {
        name: '兑换码列表',
        module: 'exchangecode'
      }, {
        name: '兑换数据列表',
        module: 'exchangecodeorder'
      }]
    }, {
      name: '服务费专区',
      module: '',
      children: [{
        name: '服务费列表',
        module: 'serviceFeeList'
      }]
    }]
  }
}, {
  title: '系统',
  auth: {
    actions: [{
      name: '销售组织管理',
      module: '',
      children: [{
        name: '查看',
        module: 'organization-get'
      }, {
        name: '管理',
        module: 'organization-manage'
      }]
    }, {
      name: '事业部管理',
      module: '',
      children: [{
        name: '查看',
        module: 'businessUnit-get'
      }, {
        name: '管理',
        module: 'businessUnit-manage'
      }]
    }, {
      name: '销售部门管理',
      module: '',
      children: [{
        name: '查看',
        module: 'department-get'
      }, {
        name: '管理',
        module: 'department-manage'
      }]
    },
    // {
    //   name: '角色管理',
    //   module: 'role',
    //   children: []
    // },
    {
      name: '用户管理',
      module: '',
      children: [{
        name: '查看',
        module: 'admin-get'
      }, {
        name: '管理',
        module: 'admin-manage'
      }]
    }, {
      name: '审批配置',
      module: 'checkSetting',
      children: []
    }, {
      name: '收款条件管理',
      module: '',
      children: [{
        name: '查看',
        module: 'settleAccount-get'
      }, {
        name: '管理',
        module: 'settleAccount-manage'
      }]
    }, {
      name: '配送方式管理',
      module: '',
      children: [{
        name: '查看',
        module: 'distribution-get'
      }, {
        name: '管理',
        module: 'distribution-manage'
      }]
    }
    ],
    menus: [{
      name: '组织架构',
      module: '',
      children: [{
        name: '销售组织',
        module: 'organization'
      }, {
        name: '销售部门',
        module: 'department'
      }, {
        name: '事业部',
        module: 'businessUnit'
      }]
    }, {
      name: '权限管理',
      module: '',
      children: [{
        name: '角色管理',
        module: 'role'
      }, {
        name: '用户列表',
        module: 'admin'
      }, {
        name: '审批配置',
        module: 'checkSetting'
      }]
    }, {
      name: '收款条件',
      module: 'settleAccount',
      children: []
    }, {
      name: '配送方式',
      module: 'distribution',
      children: []
    }]
  }
}, {
  title: '商店配置',
  auth: {
    actions: [{
      name: '商店配置管理',
      module: '',
      children: [{
        name: '查看',
        module: 'storeSetting-get'
      }, {
        name: '管理',
        module: 'storeSetting-manage'
      }]
    }, {
      name: '购物配置管理',
      module: '',
      children: [{
        name: '查看',
        module: 'shopping-setting-get'
      }, {
        name: '修改',
        module: 'shopping-setting-put'
      }]
    }, {
      name: '工厂配置管理',
      module: '',
      children: [{
        name: '查看',
        module: 'factory-setting-get'
      }, {
        name: '修改',
        module: 'factory-setting-put'
      }]
    }, {
      name: '基本配置管理',
      module: '',
      children: [{
        name: '查看',
        module: 'base-setting-get'
      }, {
        name: '修改',
        module: 'base-setting-put'
      }]
    }, {
      name: '协议配置管理',
      module: '',
      children: [{
        name: '查看',
        module: 'agreement-setting-get'
      }, {
        name: '修改',
        module: 'agreement-setting-put'
      }]
    }, {
      name: '培训中心',
      module: '',
      children: [{
        name: '查看',
        module: 'training-center-get'
      }, {
        name: '管理',
        module: 'training-center-manage'
      }]
    }, {
      name: '下载中心',
      module: '',
      children: [{
        name: '查看',
        module: 'download-center-get'
      }, {
        name: '管理',
        module: 'download-center-manage'
      }]
    }],
    menus: [{
      name: '商店配置',
      module: '',
      children: [{
        name: '首页推广',
        module: 'extension'
      }, {
        name: '产品推广',
        module: 'productPromotion'
      }, {
        name: '首页广告',
        module: 'announcement'
      }, {
        name: '首页栏目',
        module: 'column'
      }, {
        name: '首页板块',
        module: 'section'
      }]
    }, {
      name: '全站设置',
      module: '',
      children: [{
        name: '购物设置',
        module: 'shopping-setting'
      }]
    }, {
      name: '培训中心',
      module: '',
      children: [{
        name: '培训中心分类列表',
        module: 'training-category-list'
      }, {
        name: '培训中心列表',
        module: 'training-list'
      }]
    }, {
      name: '下载中心',
      module: '',
      children: [{
        name: '下载中心分类列表',
        module: 'download-category-list'
      }, {
        name: '下载中心列表',
        module: 'download-list'
      }]
    }]
  }
}
]
