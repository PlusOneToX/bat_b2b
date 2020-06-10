import Router from 'vue-router'

const router = new Router({
  mode: 'history',
  base: process.env.NODE_ENV === 'production' ? '/exchange' : '/minih5',
  routes: [{ // 默认主页
      path: '/',
      name: '/index',
      component: resolve => require(['view/index/index'], resolve)
    },
    { // 首页
      path: '/index',
      name: 'index',
      component: resolve => require(['view/index/index'], resolve),
      meta: {
        title: "首页",
        keepAlive: false,
        requireAuth: false
      }
    },
    { // 详情
      path: '/detail',
      name: 'detail',
      component: resolve => require(['view/detail/detail'], resolve),
      meta: {
        title: "详情",
        keepAlive: false,
        requireAuth: false
      }
    },
    { // 官方主题
      path: '/theme',
      name: 'theme',
      component: resolve => require(['view/theme/theme'], resolve),
      meta: {
        title: "官方主题",
        keepAlive: false,
        requireAuth: false
      }
    },
    { // 主题详情
      path: '/themeDetail',
      name: 'themeDetail',
      component: resolve => require(['view/theme/themeDetail'], resolve),
      meta: {
        title: "主题详情",
        keepAlive: false,
        requireAuth: false
      }
    },
    { // 传图定制
      path: '/custom-intro',
      name: 'customIntro',
      component: resolve => require(['view/custom/intro'], resolve),
      meta: {
        title: "传图定制",
        keepAlive: true,
        requireAuth: false
      }
    },
    { // 定制-编辑器
      path: '/custom',
      name: 'custom',
      component: resolve => require(['view/custom/custom'], resolve),
      meta: {
        title: "",
        keepAlive: true,
        requireAuth: false
      }
    },
    { // 购物车
      path: '/shopcart',
      name: 'shopcart',
      component: resolve => require(['view/shopcart/shopcart'], resolve),
      meta: {
        title: "购物车",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 确认订单
      path: '/order',
      name: 'order',
      component: resolve => require(['view/order/index'], resolve),
      meta: {
        title: "提交订单",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 我的订单
      path: '/orderList',
      name: 'orderList',
      component: resolve => require(['view/order/orderList'], resolve),
      meta: {
        title: "我的订单",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 订单详情
      path: '/orderDetail',
      name: 'orderDetail',
      component: resolve => require(['view/order/orderDetail'], resolve),
      meta: {
        title: "订单详情",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 支付结果
      path: '/payResult',
      name: 'payResult',
      component: resolve => require(['view/order/payResult'], resolve),
      meta: {
        title: "兑换结果",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 物流详情
      path: '/logisticsDetail',
      name: 'logisticsDetail',
      component: resolve => require(['view/order/logisticsDetail'], resolve),
      meta: {
        title: "物流详情",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 地址管理
      path: '/address',
      name: 'address',
      component: resolve => require(['view/address/addressList'], resolve),
      meta: {
        title: "地址管理",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 编辑地址
      path: '/editAddress',
      name: 'editAddress',
      component: resolve => require(['view/address/editAddress'], resolve),
      meta: {
        title: "编辑地址",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 卡包中心
      path: '/code',
      name: 'code',
      component: resolve => require(['view/exchange/codeList'], resolve),
      meta: {
        title: "卡包中心",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 我的
      path: '/mine',
      name: 'mine',
      component: resolve => require(['view/mine/mine'], resolve),
      meta: {
        title: "我的",
        keepAlive: false,
        requireAuth: true
      }
    },
    { // 用户
      path: '/user',
      name: 'user',
      component: resolve => require(['view/user/user'], resolve),
      meta: {
        title: "用户详情",
        keepAlive: false,
        requireAuth: true
      }
    },
  ]
})

export default router
