/*
 * @Author: 陈良顺
 * @Date:   2018-03-22 15:13:38
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2018-03-28 10:54:02
 */
//  菜单栏下相对应页面的路由，侧边栏显示中间页面路由路径，link
// child.name 用于渲染链接名
// child.link 对应路由中的child.name, 点击item时,$router.push({name: child.link})
// 刷新时注入侧栏 该行为在Navbar的mounted触发
export const sidebarItems = {
  system: [{
    title: '组织架构',
    children: [{
      name: '销售组织',
      link: 'saleOrganization',
      permission: 'organization'
    }, {
      name: '事业部',
      link: 'businessUnit',
      permission: 'businessUnit'
    }, {
      name: '销售部门',
      link: 'saleDepartment',
      permission: 'department'
    }]
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
  }, {
    title: '支付管理',
    children: [{
      name: '收款条件',
      link: 'paylist',
      permission: 'settleAccount'
    }]
  }, {
    title: '配送管理',
    children: [{
      name: '配送方式列表',
      link: 'managelist',
      permission: 'distribution'
    }
    // , {
    //   name: '地区管理',
    //   link: 'region'
    // }
    ] }
    //, {
    //   title: '日志管理',
    //   children: [{
    //     name: '系统日志',
    //     link: ''
    //   }, {
    //     name: '操作日志',
    //     link: ''
    //   }]
    // }
  ],
  // 分销商管理
  distributor: [{
    title: '分销商设置',
    children: [{
      name: '销售区域',
      link: 'distributorlist',
      permission: 'distributor-salearea'
    }, {
      name: '价格等级',
      link: 'distributorarea',
      permission: 'distributor-grade'
    }, {
      name: '分销商分组',
      link: 'distributorGrouping',
      permission: 'distributor-group'
    }, {
      name: '分销商类别',
      link: 'distributorClasses',
      permission: 'distributor-category'
    }]
  }, {
    title: '合作中分销商',
    children: [{
      name: '合作中的分销商列表',
      link: 'distributorcooperating',
      permission: 'distributorCooperating-list'
    }
      // {
      //   name: '添加合作分销商',
      //   link: 'distributorcooperatingadd',
      //   permission: 'distributorCooperating-add'
      // },
    ]
  }, {
    title: '申请中分销商审批',
    children: [{
      name: '申请中的分销商列表',
      link: 'Dapplication',
      permission: 'distributorApplying'
    }]
  }, {
    title: '已冻结分销商',
    children: [{
      name: '已冻结的分销商列表',
      link: 'Dcooperatingfreeze',
      permission: 'distributorFreezing'
    }]
  }, {
    title: '分销商审批',
    children: [{
      name: '分销商审批列表',
      link: 'distributorcheck',
      permission: 'distributorCooperating-check'
    }]
  }],
  warehouse: [{
    title: '仓库库存',
    children: [{
      name: '商品库存列表',
      link: 'stockList',
      permission: 'warehouseStockList'
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
    }, {
      name: '添加仓库',
      link: 'warehouseAdd',
      permission: 'warehouseAdd'
    }, {
      name: '仓库回收站',
      link: 'warehouseRecycle',
      permission: 'warehouseRecycle'
    }]
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
    {
      name: '缺货登记',
      link: 'loseGoods',
      permission: 'order-losegoods'
    }, {
      name: '批量导入订单',
      link: 'batchChannel',
      permission: 'importorder'
    }
    ]
  },
  // {
  //     title: '订单统计',
  //     children: [{
  //         name: '订单增长率',
  //         link: 'orderIncreaseList',
  //         permission: 'order-growthrate'
  //     }]
  // },
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
  financial: [{
    title: '基本设置',
    // {
    //   name: '预存款设置',
    //   link: 'rechargeSet',
    //   permission: 'financial-deposit'
    // },
    children: [{
      name: '线下账户设置',
      link: 'underlineSet',
      permission: 'bank'
    }]
  }, {
    title: '预存款',
    children: [{
      name: '充值列表',
      link: 'PrechargeList',
      permission: 'deposit-recharge'
    }, {
      name: '提现列表',
      link: 'drawing',
      permission: 'deposit-withdraw'
    }, {
      name: '冻结列表',
      link: 'frozen',
      permission: 'deposit-freezing'
    }]
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
    title: '销售往来单据',
    children: [{
      name: '收款单列表',
      link: 'receipt',
      permission: 'order-voucher'
    }
    // {
    //   name: '退款单列表',
    //   link: 'refundOrder',
    //   permission: 'order-refund'
    // }
    ]
  }],
  // , {
  //   title: '应收管理',
  //   children: [{
  //     name: '对账',
  //     link: 'account'
  //   }, {
  //     name: '发票管理',
  //     link: 'invoiceList'
  //   }, {
  //     name: '账龄分析',
  //     link: 'billsAge'
  //   }]
  // }
  goods: [{
    title: '商品管理',
    children: [{
      name: '商品列表',
      link: 'goodslist',
      permission: 'goods-list'
    },
    // {
    //   name: '添加商品',
    //   link: 'addgoods',
    //   permission: 'goods-add'
    // },
    {
      name: '限购规则列表',
      link: 'restrictionlist',
      permission: 'goods-purchaselimit'
    }, {
      name: '冻结商品列表',
      link: 'freezegoodslist',
      permission: 'goods-freezed'
    }
    ]
  }, {
    title: '商品分类管理',
    children: [{
      name: '商品分类列表',
      link: 'categorylist',
      permission: 'category-list'
    }]
  },
  // {
  //     title: '等级管理',
  //     children: [{
  //         name: '等级规则设置',
  //         link: 'goodGradeRule',
  //         permission: 'item-grade-setting'
  //     }, {
  //         name: '商品等级列表',
  //         link: 'goodGradeList',
  //         permission: 'item-grade-list'
  //     }, {
  //         name: '等级变动列表',
  //         link: 'goodGradeChangeList',
  //         permission: 'item-grade-change-list'
  //     }]
  // },
  {
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
    title: '规格管理',
    children: [{
      name: '商品规格列表',
      link: 'specificationlist',
      permission: 'specification-list'
    }]
  }, {
    title: '商品审批',
    children: [{
      name: '商品审批列表',
      link: 'goodInfoCheckList',
      permission: 'goods-check'
    }
      // , {
      //     name: '商品等级变动审批',
      //     link: 'goodGradeCheckList',
      //     permission: 'goods-grade-check'
      // }
    ]
  }
  ],
  storeLayout: [{
    title: '商店配置',
    children: [{
      name: '首页推广',
      link: 'homePagePromotion',
      permission: 'extension'
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
    }]
  }],
  marketingPromotion: [{
    title: '促销活动',
    children: [{
      name: '促销活动列表',
      link: 'salesPromotion',
      permission: 'promotion-list'
    }
      // , {
      //     name: '添加活动',
      //     link: 'addPromotion',
      //     permission: 'promotion-add'
      // }
    ]
  },
  // {
  //     title: '统一政策',
  //     children: [{
  //         name: '商品等级折扣',
  //         link: 'policyGradePromotion',
  //         permission: 'policy-post'
  //     }, {
  //         name: '提货增长返利',
  //         link: 'pickUpRatePromotion',
  //         permission: 'policy-rebate'
  //     }]
  // },
  {
    title: '活动审核',
    children: [{
      name: '促销活动审批列表',
      link: 'checkSalesPromotion',
      permission: 'promotion-check'
    }
      // , {
      //     name: '统一政策审核列表',
      //     link: 'checkPolicyPromotion',
      //     permission: 'policy-check'
      // }
    ]
  }
  ]
}
