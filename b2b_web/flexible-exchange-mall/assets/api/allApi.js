module.exports = {
  // 获取租户地址
  getTenant: 'platform/v1/web/tenant/url', // 获取租户地址

  // 用户
  authLogin: 'distributor/v1/web/user/customer/wx/program/mini/login', // 授权登录
  getUserInfo: 'distributor/v1/web/user/customer', // 获取用户信息

  // 兑换卡
  getDefaultExchangeId: 'flexible/v1/web/user/exchangeCard/getDefaultExchange', // 获取默认 exchangeId
  getMaterialByExchangeId: 'flexible/v1/web/user/u/material/listMaterialIdListByExchangeId', // 根据兑换id获取材质信息
  getCardNum: 'flexible/v1/web/user/exchangeCodeUser/count', // 获取兑换卡数量
  getCodeList: 'flexible/v1/web/user/exchangeCodeUser/page', // 获取兑换卡列表
  bindExchangeCard: 'flexible/v1/web/user/exchangeCodeUser', // 绑定兑换卡
  getCardData: 'flexible/v1/web/user/exchangeCodeUser/order/pre/add', // 下单页，兑换卡匹配

  // 首页
  getModelByNetwork: 'flexible/v1/web/user/u/model/getOneByNetworkModel', // 根据入网型号查询机型
  getModelAndMaterial: 'flexible/v1/web/user/exchangeCard/listModelAndMaterial', // 根据 exchangeId 查询型号/材质列表
  getBanner: 'flexible/v1/web/user/distributorBanner/listByDistributorId', // 轮播图banner列表
  getSeriesList: 'flexible/v1/web/user/distributorSeriesTheme/tab/list', // 获取首页系列
  getRecommendData: 'flexible/v1/web/user/distributorIndexRecommend/page', // 推荐
  getSeriesData: 'flexible/v1/web/user/distributorSeriesTheme/picture/list', // 获取首页系列数据

  // 主题
  getThemeList: 'flexible/v1/web/user/pictureTheme/picture/category/theme/list', // 官方主题

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
  addOrder: 'order/v1/web/user/customer/order/exchange', // 下单

  // 支付
  handlePayment: 'financial/v1/web/user/pay/createTrade', // 创建交易

  // 转赠
  exchangeTransferSend: 'flexible/v1/web/user/exchangeTransfer/sendOut', // 兑换码发起转赠
  exchangeTransferDetail: 'flexible/v1/web/user/exchangeTransfer/detail', // 兑换码转赠详情
  exchangeTransferReceive: 'flexible/v1/web/user/exchangeTransfer/receive', // 兑换码接收

  // 转发
  exchangeShareFind: 'flexible/v1/web/user/exchangeShare/find', // 查看转发活动
  exchangeShareSend: 'flexible/v1/web/user/exchangeShare/carryOut', // 进行转发
  exchangeShareReceive: 'flexible/v1/web/user/exchangeShare/receive', // 转发接收
  exchangeShareDetail: 'flexible/v1/web/user/exchangeShare/releaseDetail', // 查看发布详情
  exchangeShareSecond: 'flexible/v1/web/user/exchangeShare/second/carryOut', // 进行转发

  // 苏宁权益
  exchangeEquity: 'flexible/v1/web/user/exchangeQuanyi/exchange', // 验证领取
  getCodeDetail: 'flexible/v1/web/user/exchangeCodeUser/findByExchangeCodeId', // 兑换码详情

  // 快递停发区域
  findMatchDeliveryStop: 'order/v1/web/user/orderDeliverStopPlace/findMatch', // 查询该地址是否停发

  // 微信客服
  getWechatInfo: 'system/v1/web/user/baseSetting', // 获取微信客服链接、企业ID

  // 解绑
  unboundCard: 'flexible/v1/web/user/exchangeCodeUser/unboundExchange', // 解绑兑换卡
}