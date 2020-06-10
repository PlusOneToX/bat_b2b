/*
 * @Author: yaowei
 * @Date: 2019-11-16 15:00:47
 * @LastEditors: yaowei
 * @LastEditTime: 2019-07-20 14:45:43
 */

export default [
  /**
   * 默认主页
   */
  {
    path: '/',
    name: '/index',
    component: () => import('components/index/index')
  }, { // 首页
    path: '/index',
    name: 'index',
    component: () => import('components/index/index'),
    meta: {
      keepAlive: false,
      requireAuth: false
    }
  },
  { // 定制
    path: '/phone',
    name: 'phone',
    component: () => import('components/phone/phone'),
    meta: {
      keepAlive: true,
      requireAuth: false
    }
  },
  { // 登录
    path: '/login',
    name: 'login',
    component: () => import('components/login/login')
  }, { // 确认订单
    path: '/order',
    name: 'order',
    component: () => import('components/order/index'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 订单列表
    path: '/orderList',
    name: 'orderList',
    component: () => import('components/order/orderList'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 订单详情
    path: '/orderDetail',
    name: 'orderDetail',
    component: () => import('components/order/orderDetail'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 支付结果
    path: '/payResult',
    name: 'payResult',
    component: () => import('components/order/payResult'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 支付结果（荣耀）
    path: '/result',
    name: 'result',
    component: () => import('components/order/result'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 个人中心
    path: '/user',
    name: 'user',
    component: () => import('components/user/user'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 地址管理
    path: '/address',
    name: 'address',
    component: () => import('components/address/addressList'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 优惠券列表
    path: '/couponList',
    name: 'couponList',
    component: () => import('components/couponList/couponList'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  },
  { // 添加地址
    path: '/editAddress',
    name: 'editAddress',
    component: () => import('components/address/editAddress'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 预览
    path: '/preview',
    name: 'preview',
    component: () => import('components/preview/preview'),
    meta: {
      keepAlive: false,
      requireAuth: false
    }
  }, { // 购物车
    path: '/shopcart',
    name: 'shopcart',
    component: () => import('components/shopcart/shopcart'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 物流详情
    path: '/logisticsDetail',
    name: 'logisticsDetail',
    component: () => import('components/order/logisticsDetail'),
    meta: {
      keepAlive: false,
      requireAuth: true
    }
  }, { // 活动礼券
    path: '/activityCoupon',
    name: 'activityCoupon',
    component: () => import('components/activity/activityCoupon'),
    meta: {
      keepAlive: false,
      requireAuth: false
    }
  }, { // 活动礼券-物流兑换
    path: '/deliveryCoupon',
    name: 'deliveryCoupon',
    component: () => import('components/activity/deliveryCoupon'),
    meta: {
      keepAlive: false,
      requireAuth: false
    }
  }, { // 错误页面
    path: '/error',
    name: 'error',
    component: () => import('components/error/index'),
    meta: {
      keepAlive: false,
      requireAuth: false
    }
  }
]
