/*
 * @Author: 陈良顺
 * @Date:   2018-03-22 15:13:38
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-04-Sa 09:18:03
 */
//  菜单栏下相对应页面的路由，侧边栏显示中间页面路由路径，link
// child.name 用于渲染链接名
// child.link 对应路由中的child.name, 点击item时,$router.push({name: child.link})
// 刷新时注入侧栏 该行为在Navbar的mounted触发
export const sidebarItems = {
  // 系统
  system: [{
      title: '组织架构',
      children: [{
          name: '销售组织',
          link: 'saleOrganization',
          permission: 'organization'
        },
        // {
        //   name: '事业部',
        //   link: 'businessUnit',
        //   permission: 'businessUnit'
        // },
        {
          name: '销售部门',
          link: 'saleDepartment',
          permission: 'department'
        }
      ]
    }, {
      title: '权限管理',
      children: [{
        name: '角色列表',
        link: 'rolelist',
        permission: 'role'
      }, {
        name: '用户列表',
        link: 'userlist',
        permission: 'admin'
      }, {
        name: '审批配置',
        link: 'approve',
        permission: 'checkSetting'
      }]
    },
    // {
    //   title: '支付管理',
    //   children: [{
    //     name: '收款条件',
    //     link: 'paylist',
    //     permission: 'settleAccount'
    //   }]
    // },
    {
      title: '配送管理',
      children: [{
        name: '配送方式列表',
        link: 'managelist',
        permission: 'distribution'
      }, {
        name: '停发列表',
        link: 'deliveryStopList',
        permission: 'deliveryStopList'
      }]
    },
    {
      title: '消息通知',
      children: [{
        name: '消息通知列表',
        link: 'newsNotice',
        permission: 'newsNotice'
      }, {
        name: '微信消息模板设置',
        link: 'wechatNewsBoard',
        permission: 'wechatNewsBoard'
      },]
    },
    {
      title: '系统日志',
      children: [{
        name: '系统操作日志',
        link: 'uLog',
        permission: 'ulog'
      }, {
        name: '接口调用日志',
        link: 'interfaceLog',
        permission: 'interfacelog'
      },{
        name: '消息发送日志',
        link: 'sentNews',
        permission: 'sentNews'
      }]
    }
  ],
  
  // 分销商管理
  distributor: [{
      title: '分销商设置',
      children: [{
        name: '销售区域',
        link: 'distributorlist',
        permission: 'distributor-salearea'
      }, {
        name: '分销商分组',
        link: 'distributorGrouping',
        permission: 'distributor-group'
      }, {
        name: '分销商类别',
        link: 'distributorClasses',
        permission: 'distributor-category'
      }, {
        name: '收款条件',
        link: 'paylist',
        permission: 'pay-list'
      }]
    },
    {
      title: '一级分销商',
      children: [{
        name: '合作中的分销商列表',
        link: 'distributorcooperating',
        permission: 'distributorCooperating-list'
      }, {
        name: '分销商申请列表',
        link: 'Dapplication',
        permission: 'distributorApplying'
      }, {
        name: '已冻结的分销商列表',
        link: 'Dcooperatingfreeze',
        permission: 'distributorFreezing'
      }]
    },
    {
      title: '多级分销商',
      children: [{
        name: '合作中的分销商列表',
        link: 'distributorcooperatingn',
        permission: 'distributorcooperatingn-list'
      }, {
        name: '已冻结的分销商列表',
        link: 'DcooperatingfreezeN',
        permission: 'distributorFreezingN'
      }]
    },
    {
      title: '分销商审批',
      children: [{
        name: '分销商审批列表',
        link: 'distributorcheckN',
        permission: 'distributorCooperating-checkN'
      }]
    },
    {
      title: '合作平台管理',
      children: [{
        name: '合作平台列表',
        link: 'sysPlatformList',
        permission: 'sys-platform-list'
      }, {
        name: '自有平台列表',
        link: 'ownPlatformList',
        permission: 'platformInfo'
      }, {
        name: '微信公众平台列表',
        link: 'wxPlatformList',
        permission: 'wx-platform-list'
      }]
    },
    {
      title: '用户管理',
      children: [{
        name: '柔性用户列表',
        link: 'customerlist',
        permission: 'distributor-customer'
      }]
    },
    {
      title: '分账管理',
      children: [{
        name: '分账记录',
        link: 'subAccountlist',
        permission: 'sub-account-list'
      }, {
        name: '分账配置',
        link: 'subAccountConfig',
        permission: 'sub-account-config'
      }, {
        name: '分账业务员',
        link: 'subAccountSaleman',
        permission: 'sub-account-saleman'
      }]
    }
  ],
  // 柔性管理
  material: [{
    title: '图库管理',
    children: [{
      name: '图库分类列表',
      link: 'pictureCategoryList',
      permission: 'pictureCategory-list'
    }, {
      name: '图片列表',
      link: 'pictureList',
      permission: 'picture-list'
    }]
  }, {
    title: '标签管理',
    children: [{
      name: '标签列表',
      link: 'labelList',
      permission: 'label-list'
    }]
  }, {
    title: '字体管理',
    children: [{
      name: '字体列表',
      link: 'fontList',
      permission: 'font-list'
    }]
  }, {
    title: '定制商品管理',
    children: [{
      name: '定制商品材料列表',
      link: 'materialList',
      permission: 'material-list'
    }, {
      name: '定制商品型号列表',
      link: 'modelList',
      permission: 'model-list'
    }, {
      name: '材料型号关联列表',
      link: 'materialModelList',
      permission: 'material-model-list'
    }, {
      name: '第三方材质型号关联',
      link: 'thirdSkuList',
      permission: 'thirdskurela'
    }]
  }, {
    title: '柔性店铺配置',
    children: [{
      name: '轮播图管理',
      link: 'rxBanner',
      permission: 'distributorbanner'
    }, {
      name: '系列展示管理',
      link: 'rxCategory',
      permission: 'distributorseriestheme'
    }, {
      name: '推荐管理',
      link: 'rxRecommend',
      permission: 'distributorindexrecommend'
    }, {
      name: '官方主题管理',
      link: 'rxTheme',
      permission: 'picture-theme-manage'
    }, {
      name: '店铺管理',
      link: 'storeManage',
      permission: 'storemanage'
    }, {
      name: '公告管理',
      link: 'rxNotice',
      permission: 'exchange-notice-manage'
    }]
  }, {
    title: '卡券兑换',
    children: [{
      name: '兑换活动列表',
      link: 'exchangeActivity',
      permission: 'exchangecard'
    }, {
      name: '兑换码列表',
      link: 'exchangeCode',
      permission: 'exchangecode'
    }, {
      name: '兑换数据列表',
      link: 'exchangeCodeOrder',
      permission: 'exchangecodeorder'
    }, {
      name: '电子兑换卡导出记录表',
      link: 'exchangeExport',
      permission: 'exchangeExport'
    }, {
      name: '权益兑换列表',
      link: 'equityExchange',
      permission: 'equityExchange'
    }]
  }, {
    title: '营销&专题活动',
    children: [{
      name: '转发活动配置',
      link: 'exchangeShare',
      permission: 'exchangeShare'
    }, {
      name: '营销专题活动',
      link: 'exchangeSpecial',
      permission: 'exchangeSpecial'
    }]
  }, {
    title: '产品类型',
    children: [{
      name: '产品类型列表',
      link: 'productCategoryList',
      permission: 'productCategory-list'
    }]
  }],
  // 仓库
  warehouse: [{
      title: '仓库库存',
      children: [{
        name: '商品库存列表',
        link: 'stockList',
        permission: 'warehouseStockList'
      }, {
        name: '预留明细',
        link: 'stockReservedList',
        permission: 'warehousestockReserved'
      }]
    }, {
      title: '仓库设置',
      children: [{
          name: '基本设置',
          link: 'warehouseTimeSet',
          permission: 'warehouseSetting'
        }, {
          name: '仓库列表', // 渲染用
          link: 'warehouseList', // route.name 使用: $router.push({name: link})
          permission: 'warehouseList'
        },
        // , {
        //   name: '添加仓库',
        //   link: 'warehouseAdd',
        //   permission: 'warehouseAdd'
        // },
        {
          name: '仓库回收站',
          link: 'warehouseRecycle',
          permission: 'warehouseRecycle'
        }
      ]
    }
    // , {
    //   name: '库存调整',
    //   link: 'stockAdjust'
    // }
    // , {
    //   name: '库存预留',
    //   link: 'stockReserved'
    // }, {
    //   name: '库存明细',
    //   link: 'stockDetail'
    // }
    // , {
    //   title: '库存审批',
    //   children: [{
    //     name: '预留审批',
    //     link: 'reserveApproval'
    //   }, {
    //     name: '调整审批',
    //     link: 'adjustApproval'
    //   }]
    // }
    // {
    //   title: '库存调拨',
    //   children: [{
    //     name: '库存调拨列表',
    //     link: 'stockAllocationList'
    //   }, {
    //     name: '库存调拨',
    //     link: 'stockAllocation'
    //   }]
    // }
  ],
  // 订单
  order: [{
      title: '订单管理',
      children: [{
          name: '订单列表', // 渲染用
          link: 'orderList', // route.name 使用: $router.push({name: link})
          permission: 'order-list' // 页面上的权限判断
        },
        //  {
        //   name: '价格申请',
        //   link: 'applyForPrice'
        // },
        // {
        //   name: '缺货登记',
        //   link: 'loseGoods',
        //   permission: 'order-losegoods'
        // },
        {
          name: '分销订单',
          link: 'orderDistrList',
          permission: 'order-distr-list'
        },
        {
          name: '批量导入订单',
          link: 'batchChannel',
          permission: 'importorder'
        },
        // {
        //   name: '同步订单列表',
        //   link: 'abnormalOrder',
        //   permission: 'abnormal-order'
        // }, {
        //   name: '柔性定制订单列表',
        //   link: 'diyOrderList',
        //   permission: 'diy_order_list'
        // }, {
        //   name: '柔性定制店铺订单列表',
        //   link: 'diyStoreOrderList',
        //   permission: 'diy_store_order_list'
        // }
      ]
    },
    {
      title: 'C端订单',
      children: [{
          name: '柔性定制订单',
          link: 'customerDiyOrderList',
          permission: 'customer_diy_order_list'
        },
        {
          name: '柔性同步订单',
          link: 'abnormalOrder',
          permission: 'abnormal-order'
        }
      ]
    },
    {
      title: '异常订单',
      children: [{
          name: '同步ERP失败订单',
          link: 'syncERPFailList',
          permission: 'sync-erp-fail-list'
        },
        {
          name: '同步工厂失败订单',
          link: 'syncFactoryFailList',
          permission: 'sync-factory-fail-list'
        },
        {
          name: '长时间未发货订单',
          link: 'syncUndeliveredFailList',
          permission: 'sync-undelivered-fail-list'
        }
      ]
    },
    {
      title: '订单统计',
      children: [
        // {
        //     name: '订单增长率',
        //     link: 'orderIncreaseList',
        //     permission: 'order-growthrate'
        // },
        {
          name: '订单明细导出',
          link: 'orderDiscount',
          permission: 'order-discount'
        },
        {
          name: 'VMI订单明细',
          link: 'orderVmi',
          permission: 'order-vmi'
        }
      ]
    },
    // {
    //   title: '售后服务',
    //   children: [{
    //     name: '退换货申请',
    //     link: 'orderExchangeGoodsList'
    //   }]
    // },
    {
      title: '单据管理',
      children: [{
        name: '发货单',
        link: 'deliverGoods',
        permission: 'order-delivergoods-list'
      }]
      // , {
      //   name: '退货单',
      //   link: 'orderbills'
      // }, {
      //   name: '换货单',
      //   link: 'exchangeGoods'
      // }
    }, {
      title: '订单设置',
      children: [{
        name: '订单类型',
        link: 'orderSetting',
        permission: 'orderType'
      }]
      // , {
      //   name: '退货单',
      //   link: 'orderbills'
      // }, {
      //   name: '换货单',
      //   link: 'exchangeGoods'
      // }
    }
    // , {
    //   title: '销售对账',
    //   children: [{
    //     name: '对账单列表', // 渲染用
    //     link: 'billList' // route.name 使用: $router.push({name: link})
    //   }, {
    //     name: '对账折扣',
    //     link: 'billDiscount'
    //   }]
    // }
    // {
    //   title: '销售对账',
    //   children: [{
    //     name: '对账单列表', // 渲染用
    //     link: 'billList' // route.name 使用: $router.push({name: link})
    //   }
    //   // {
    //     // name: '对账折扣',
    //     // link: 'billDiscount'
    //   // }
    //   ]
    // }
    // {
    //   title: '单据归档',
    //   children: [{
    //     name: '归档操作', // 渲染用
    //     link: 'archiveOperation' // route.name 使用: $router.push({name: link})
    //   }, {
    //     name: '订单',
    //     link: 'order'
    //   }, {
    //     name: '退换货单',
    //     link: 'orderExchangeGoods'
    //   }, {
    //     name: '换货单',
    //     link: 'swapOrder'
    //   }, {
    //     name: '发票',
    //     link: 'invoice'
    //   }, {
    //     name: '发货单',
    //     link: 'deliverGoods'
    //   }, {
    //     name: '退货单',
    //     link: 'returnOrder'
    //   }]
    // }
  ],
  // 财务
  financial: [{
    title: '基本设置',
    children: [
      // {
      //   name: '线下账户设置',
      //   link: 'underlineSet',
      //   permission: 'bank'
      // },
      {
        name: '汇率设置',
        link: 'exchangeRate',
        permission: 'rate'
      }, {
        name: '币别',
        link: 'currency',
        permission: 'currency'
      }
    ]
  }, {
    title: '平台账户',
    children: [{
        name: '微信支付账户',
        link: 'wxPayList',
        permission: 'wx-pay-list'
      }, {
        name: '支付宝支付账户',
        link: 'alipayList',
        permission: 'alipay-list'
      },
      // {
      //   name: '快钱支付账户',
      //   link: 'quickPayList',
      //   permission: 'quick-pay-list'
      // },
      {
        name: '线下转账账户',
        link: 'offlineList',
        permission: 'offline-list'
      }
    ]
  }, {
    title: '分销商账户',
    children: [{
        name: '微信支付账户列表',
        link: 'wxAccountList',
        permission: 'wx-account-list'
      },
      {
        name: '支付宝账户列表',
        link: 'alipayAccountList',
        permission: 'alipay-account-list'
      }
    ]
  }, {
    title: '预存款',
    children: [
      // {
      //   name: '收款列表',
      //   link: 'PrechargeList',
      //   permission: 'deposit-recharge'
      // },
      // {
      //   name: '提现列表',
      //   link: 'drawing',
      //   permission: 'deposit-withdraw'
      // },
      {
        name: '冻结列表',
        link: 'frozen',
        permission: 'deposit-freezing'
      }
    ]
  }, {
    title: '预存款明细',
    children: [{
      name: '明细账',
      link: 'prechargeDetail',
      permission: 'deposit-available'
    }, {
      name: '余额表',
      link: 'balanceList',
      permission: 'deposit-detail'
    }]
  }, {
    title: '代金券',
    children: [{
      name: '代金券列表',
      link: 'voucherList',
      permission: 'voucherList'
    }, {
      name: '代金券审批',
      link: 'voucherCheck',
      permission: 'voucherCheck'
    }]
  }, {
    title: '销售往来单据',
    children: [{
        name: '收款单列表',
        link: 'receipt',
        permission: 'order-voucher'
      },
      {
        name: '退款单列表',
        link: 'refundOrder',
        permission: 'order-refund'
      },
      {
        name: '退款申请单列表',
        link: 'refundApply',
        permission: 'order-refund-apply'
      }
    ]
  }],
  // 商品管理
  goods: [{
      title: '商品管理',
      children: [{
          name: '商品列表',
          link: 'goodslist',
          permission: 'goods-list'
        },
        // {
        //   name: '限购规则列表',
        //   link: 'restrictionlist',
        //   permission: 'goods-purchaselimit'
        // },
        {
          name: '冻结商品列表',
          link: 'freezegoodslist',
          permission: 'goods-freezed'
        },
        // {
        //   name: '商品销量管理',
        //   link: 'salesManage',
        //   permission: 'salesManage'
        // },
        {
          name: '价格等级',
          link: 'distributorarea',
          permission: 'distributor-grade'
        }
      ]
    },
    // {
    //   title: '商品等级管理',
    //   children: [{
    //     name: '等级列表',
    //     link: 'goodGradeBase',
    //     permission: 'good-grade-base'
    //   }, {
    //     name: '等级规则设置',
    //     link: 'goodGradeRule',
    //     permission: 'item-grade-setting'
    //   }, {
    //     name: '商品等级列表',
    //     link: 'goodGradeList',
    //     permission: 'item-grade-list'
    //   }, {
    //     name: '等级变动列表',
    //     link: 'goodGradeChangeList',
    //     permission: 'item-grade-change-list'
    //   }]
    // },
    {
      title: '商品分类管理',
      children: [{
        name: '商品分类列表',
        link: 'categorylist',
        permission: 'category-list'
      }, {
        name: '商品标签列表',
        link: 'goodslabellist',
        permission: 'goods-label-list'
      }]
    }, {
      title: '品牌品类管理',
      children: [{
        name: '品牌列表',
        link: 'brandlist',
        permission: 'brand-list'
      }, {
        name: '品类列表',
        link: 'productlinelist',
        permission: 'productline-list'
      }]
    }, {
      title: '商品属性管理',
      children: [{
        name: '商品属性列表',
        link: 'specificationlist',
        permission: 'specification-list'
      }]
    }
    // {
    //   title: '商品审批',
    //   children: [{
    //     name: '商品上下架审批列表',
    //     link: 'goodInfoCheckList',
    //     permission: 'goods-check'
    //   }, {
    //     name: '商品等级变动审批',
    //     link: 'goodGradeCheckList',
    //     permission: 'goods-grade-check'
    //   }]
    // }
  ],
  // 商店配置
  storeLayout: [{
    title: '商店配置',
    children: [{
      name: '首页推广',
      link: 'homePagePromotion',
      permission: 'extension'
    }, {
      name: '产品推广',
      link: 'productPromotion',
      permission: 'productPromotion'
    }, {
      name: '首页公告',
      link: 'homePageAnnouncement',
      permission: 'announcement'
    }, {
      name: '首页栏目',
      link: 'frontPageColumn',
      permission: 'column'
    }, {
      name: '首页板块',
      link: 'frontPagePlate',
      permission: 'section'
    }, {
      name: '移动端首页',
      link: 'mobile',
      permission: 'mobile'
    }]
  }, {
    title: '全站设置',
    children: [{
      name: '购物设置',
      link: 'totalStationSet',
      permission: 'shopping-setting'
    }, {
      name: '工厂设置',
      link: 'factorySet',
      permission: 'factory-setting'
    }, {
      name: '基本设置',
      link: 'baseSet',
      permission: 'base-setting'
    }, {
      name: '协议设置',
      link: 'agreementSet',
      permission: 'agreement-setting'
    }]
  }, {
    title: '培训中心管理',
    children: [{
      name: '培训中心分类列表',
      link: 'trainCategoryList',
      permission: 'training-category-list'
    }, {
      name: '培训中心列表',
      link: 'trainList',
      permission: 'training-list'
    }]
  }, {
    title: '下载中心管理',
    children: [{
      name: '下载中心分类列表',
      link: 'downCategoryList',
      permission: 'download-category-list'
    }, {
      name: '下载中心列表',
      link: 'downList',
      permission: 'download-list'
    }]
  }],
  // 营销推广
  marketingPromotion: [{
      title: '促销活动',
      children: [{
          name: '促销活动列表',
          link: 'salesPromotion',
          permission: 'promotion-list'
        },
        {
          name: '批量活动导入',
          link: 'promotionImportList',
          permission: 'promotion-import'
        }
      ]
    },
    // {
    //   title: '统一政策',
    //   children: [{
    //     name: '商品等级折扣',
    //     link: 'policyGradePromotion',
    //     permission: 'policy-post'
    //   }, {
    //     name: '提货增长返利',
    //     link: 'pickUpRatePromotion',
    //     permission: 'policy-rebate'
    //   }]
    // },
    // {
    //   title: '活动审批',
    //   children: [{
    //     name: '促销活动审批列表',
    //     link: 'checkSalesPromotion',
    //     permission: 'promotion-check'
    //   }, {
    //     name: '统一政策审批列表',
    //     link: 'checkPolicyPromotion',
    //     permission: 'policy-check'
    //   }]
    // },
    {
      title: '优惠券',
      children: [{
        name: '优惠券列表',
        link: 'coupon',
        permission: 'coupon-list'
      }]
    },
    {
      title: '拼团',
      children: [{
        name: '拼团列表',
        link: 'groupBuy',
        permission: 'spellGroup'
      }, {
        name: '秒杀列表',
        link: 'seckill',
        permission: 'secKill'
      }]
    },
    {
      title: '活动审批',
      children: [{
        name: '促销活动审批',
        link: 'checkSalesPromotion',
        permission: 'promotion-check'
      }, {
        name: '优惠券审批',
        link: 'checkCoupon',
        permission: 'coupon-check'
      }, {
        name: '拼团秒杀审批',
        link: 'checkGroupSeckill',
        permission: 'group-secKill-check'
      }]
    }
    // {
    //   title: '服务费专区',
    //   children: [{
    //     name: '服务费列表',
    //     link: 'serviceFeeList',
    //     permission: 'serviceFeeList'
    //   }]
    // }
  ]
}
