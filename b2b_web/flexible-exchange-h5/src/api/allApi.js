let originBaseURL = "";
if (process.env.NODE_ENV === "production") {
  originBaseURL = 'https://api.bat.com/' // 正式
} else if (process.env.NODE_ENV === "testing") {
  originBaseURL = 'https://test.bat.com/' // 测试
} else {
  originBaseURL = 'https://test.bat.com/' // 本地
}
let newBaseURL = "";

module.exports = {
  // 获取租户地址
  getTenant: originBaseURL + 'platform/v1/web/tenant/url', // 获取租户地址

  // 首页
  getModelAndMaterial: newBaseURL + 'flexible/v1/web/user/exchangeCard/listModelAndMaterial', // 根据 exchangeId 查询型号/材质列表
  getBanner: newBaseURL + 'flexible/v1/web/user/distributorBanner/listByDistributorId', // 轮播图banner列表
  getRecommend: newBaseURL + 'flexible/v1/web/user/distributorIndexRecommend/page', // 推荐
  getSeries: newBaseURL + 'flexible/v1/web/user/distributorSeriesTheme/tab/list', // 获取首页系列
  getSeriesData: newBaseURL + 'flexible/v1/web/user/distributorSeriesTheme/picture/list', // 获取首页系列数据

  // 主题
  getThemeList: newBaseURL + 'flexible/v1/web/user/pictureTheme/picture/category/theme/list', // 官方主题
  getTheme: newBaseURL + 'flexible/v1/web/user/pictureTheme/picture/category/theme/detail', // 官方主题详情

  // 公告
  getNotice: newBaseURL + 'flexible/v1/web/user/exchangeNotice/list', // 获取公告

  // 编辑器
  getMaterialList: newBaseURL + 'flexible/v1/web/user/u/material/tree', // 获取材质列表
  getMaterialListNew: newBaseURL + 'flexible/v1/web/user/u/material/treeNew', // 获取材质列表（新增不适应）
  getMaterialDetail: newBaseURL + 'flexible/v1/web/user/u/material', // 材质详情介绍
  getPrice: newBaseURL + 'flexible/v1/web/user/u/material/price', // 获取价格

  getModelByNetwork: newBaseURL + 'flexible/v1/web/user/u/model/getOneByNetworkModel', // 根据入网型号查询机型
  getModelList: newBaseURL + 'flexible/v1/web/user/u/model/tree', // 获取型号列表
  getModelListNew: newBaseURL + 'flexible/v1/web/user/u/model/treeNew', // 获取型号列表（新增不适应）

  getPictureCategory: newBaseURL + 'flexible/v1/web/user/distributorSeriesTheme/detail/tab/list', // 获取图库分类
  getPictureCategoryMore: newBaseURL + 'flexible/v1/web/user/u/picture/detail/picture/category', // 获取图库二级分类
  getPicture: newBaseURL + 'flexible/v1/web/user/u/picture/detail/picture/list', // 获取图库图片
  getMapCategory: newBaseURL + 'flexible/v1/web/user/u/pictureCategory/detail/tab/mapp/list', // 获取贴图分类
  getMap: newBaseURL + 'flexible/v1/web/user/u/picture/tree', // 获取贴图
  getPictureInfoById: newBaseURL + 'flexible/v1/web/user/u/picture/detail/picture/find/one', // 根据图片id获取图片信息

  getFontList: newBaseURL + 'flexible/v1/web/user/u/font/listUsable', // 获取字体
  getCustomInfo: newBaseURL + 'flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId', // 获取定制信息
  recordPoint: newBaseURL + 'flexible/v1/web/user/buryingPoint', // 记录数据埋点

  getCacheIP: newBaseURL + 'flexible/v1/web/user/u/pictureModelMaterialDiy', // 获取缓存IP图
  handleCacheIP: newBaseURL + 'flexible/v1/web/user/u/pictureModelMaterialDiy', // 缓存IP图

  getOSSSts: newBaseURL + 'system/v1/web/admin/oss/sts', // 获取OSS对象存储

  // 购物车
  addToShopcart: newBaseURL + 'order/v1/web/user/customer/shoppingcart/diy', // 加入购物车
  getShopcartList: newBaseURL + 'order/v1/web/user/customer/shoppingcart/list', // 获取购物车列表
  updataShopcart: newBaseURL + 'order/v1/web/user/customer/shoppingcart', // 修改购物车商品
  deleteShopcart: newBaseURL + 'order/v1/web/user/customer/shoppingcart/ids', // 删除购物车商品（批量）

  // 订单
  addOrder: newBaseURL + 'order/v1/web/user/customer/order/exchange', // 下单
  getOrderList: newBaseURL + 'order/v1/web/user/customer/order/list', // 订单列表
  getOrderDetail: newBaseURL + 'order/v1/web/user/customer/order/detail', // 订单详情
  handleCancelOrder: newBaseURL + 'order/v1/web/user/customer/order/cancel', // 取消订单
  getOrderTime: newBaseURL + 'order/v1/web/user/customer/order/time', // 订单失效时间
  handleOrder: newBaseURL + 'thirdparty/v1/web/open/order/create', // 创建临时订单、提交到第三方接口

  // 支付
  handlePayment: newBaseURL + 'financial/v1/web/user/pay/createTrade', // 创建交易

  // 配送方式
  getCalculations: newBaseURL + 'system/v1/web/user/logistics/calculations', // 根据地区和购买商品获取配送方式和计算配送费用
  getLogistics: newBaseURL + 'system/v1/web/user/logistics', // 通过ID查询单个配送

  // 兑换卡
  getDefaultExchangeId: newBaseURL + 'flexible/v1/web/user/exchangeCard/getDefaultExchange', // 获取默认 exchangeId
  getCardNum: newBaseURL + 'flexible/v1/web/user/exchangeCodeUser/count', // 获取兑换卡数量
  getCodeList: newBaseURL + 'flexible/v1/web/user/exchangeCodeUser/page', // 获取兑换卡列表
  getMaterialByExchangeId: newBaseURL + 'flexible/v1/web/user/u/material/listMaterialIdListByExchangeId', // 根据兑换id获取材质信息
  getCardData: newBaseURL + 'flexible/v1/web/user/exchangeCodeUser/order/pre/add', // 下单页，兑换卡匹配

  // 用户
  getUserInfo: newBaseURL + 'distributor/v1/web/user/customer', // 获取用户信息
  thirdpartyLogin: newBaseURL + 'distributor/v1/web/user/customer/thirdparty/login', // 第三方系统登录

  // 优惠券
  getCouponList: newBaseURL + 'promotion/v1/web/user/customer/coupon/list', // 优惠券列表
  receiveCoupon: newBaseURL + 'promotion/v1/web/user/customer/coupon/receive', // 领取优惠券
  getSpecialCoupon: newBaseURL + 'promotion/v1/web/user/customer/coupon/special/list', // 根据优惠券类型获取优惠券列表(不分页)
  getGoodsCoupn: newBaseURL + 'promotion/v1/web/user/customer/coupon/goods/list', // 根据商品列表获取最优优惠券
  getGoodsCoupnCount: newBaseURL + 'promotion/v1/web/user/customer/coupon/goods/enable/count', // // 根据商品列表获取优惠券列表
  getOptimalCoupon: newBaseURL + 'promotion/v1/web/user/customer/coupon/goods/optimal', // // 根据商品列表获取优惠券列表数量

  // 地址管理
  getAddrList: newBaseURL + 'distributor/v1/web/user/customer/address/list', // 地址列表
  setDefaultAddr: newBaseURL + 'distributor/v1/web/user/customer/address/default', // 设置默认地址
  updateAddr: newBaseURL + 'distributor/v1/web/user/customer/address', // 新增/修改/删除地址

  // 转发
  exchangeShareFind: newBaseURL + 'flexible/v1/web/user/exchangeShare/find', // 查看转发活动
  exchangeShareSend: newBaseURL + 'flexible/v1/web/user/exchangeShare/carryOut', // 进行转发
  
  // 快递停发区域
  findMatchDeliveryStop: newBaseURL + 'order/v1/web/user/orderDeliverStopPlace/findMatch', // 查询该地址是否停发
}
