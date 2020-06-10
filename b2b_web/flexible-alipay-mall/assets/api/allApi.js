module.exports = {
  // 用户
  authLogin: 'distributor/v1/web/user/customer/alipay/program/mini/login', // 授权登录
  getUserInfo: 'distributor/v1/web/user/customer', // 获取用户信息
  thirdpartyLogin: 'distributor/v1/web/user/customer/thirdparty/login', // 第三方系统登录

  // 型号
  getModelList: 'flexible/v1/web/user/u/model/tree', // 获取型号列表

  // 材质
  getMaterialList: 'flexible/v1/web/user/u/material/tree', // 获取材质列表

  // 首页
  getBanner: 'flexible/v1/web/user/distributorBanner/listByDistributorId', // 轮播图banner列表
  getSeriesList: 'flexible/v1/web/user/distributorSeriesTheme/tab/list', // 获取首页系列
  getRecommendData: 'flexible/v1/web/user/distributorIndexRecommend/page', // 推荐
  getSeriesData: 'flexible/v1/web/user/distributorSeriesTheme/picture/list', // 获取首页系列数据

  // 购物车
  addToShopcart: 'order/v1/web/user/customer/shoppingcart/diy', // 加入购物车
  getShopcartList: 'order/v1/web/user/customer/shoppingcart/list', // 获取购物车列表
  updataShopcart: 'order/v1/web/user/customer/shoppingcart', // 修改购物车商品
  deleteShopcart: 'order/v1/web/user/customer/shoppingcart/ids', // 删除购物车商品（批量）

  // 配送方式
  getCalculations: 'system/v1/web/user/logistics/calculations', // 根据地区和购买商品获取配送方式和计算配送费用
  getLogistics: 'system/v1/web/user/logistics', // 通过ID查询单个配送

  // 通知
  getNoticeList: 'flexible/v1/web/user/exchangeNotice/list', // 获取通知列表

  // 地址管理
  getAddrList: 'distributor/v1/web/user/customer/address/list', // 地址列表

  // 订单
  addOrder: 'order/v1/web/user/customer/order', // 下单

  // 支付
  handlePayment: 'financial/v1/web/user/pay/createTrade', // 创建交易

  // 优惠券
  getCouponList: 'promotion/v1/web/user/customer/coupon/list', // 优惠券列表
  getOptimalCoupon: 'promotion/v1/web/user/customer/coupon/goods/optimal', // 根据商品列表获取最优优惠券
  getGoodsCoupn: 'promotion/v1/web/user/customer/coupon/goods/list', // 根据商品列表获取优惠券列表
  receiveCoupon: 'promotion/v1/web/user/customer/coupon/receive', // 领取优惠券
  getCouponCount: 'promotion/v1/web/user/customer/coupon/count', // 优惠券汇总
  
  // 快递停发区域
  findMatchDeliveryStop: 'order/v1/web/user/orderDeliverStopPlace/findMatch', // 查询该地址是否停发
}