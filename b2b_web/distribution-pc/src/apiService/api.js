import http from './http.js'

export const tenantUrl = (params) => http.get(this, '/platform/v1/web/tenant/url', params) //获取域名api

export const regionList = (params) => http.get(this, '/system/v1/web/user/region/list', params) //查询区域列表

export const login = (params) => http.post(this, '/distributor/v1/web/user/login', params) //登录

export const contactList = (params) => http.get(this, '/distributor/v1/web/user/contact/list', params) //分销商联系人列表

export const contactApi = (method, params) => http.allRequest(method, '/distributor/v1/web/user/contact', params) //分销商联系人(修改：PUT,新增：POST,删除：DELETE

export const roleList = (params) => http.get(this, '/distributor/v1/web/user/role/list', params) //查询分销商联系人角色列表

export const addressList = (params) => http.get(this, '/distributor/v1/web/user/address/list', params) //查询收货地址列表(分页)

export const addressApi = (method, params) => http.allRequest(method, '/distributor/v1/web/user/address', params) //收货地址(修改：PUT,新增：POST,删除：DELET

export const addressDefault = (params) => http.put(this, '/distributor/v1/web/user/address/default', params) //设置分销商收货地址为默认地址

export const userInfo = (params) => http.get(this, '/distributor/v1/web/user/info', params) //查询分销商个人信息

export const userPassword = (params) => http.put(this, '/distributor/v1/web/user/password', params) //分销商修改密码

export const oneLevelApply = (params) => http.post(this, '/distributor/v1/web/user/one-level/apply', params) //一级分销商前台注册

export const userNextList = (params) => http.get(this, '/distributor/v1/web/user/next/list', params) //查询下级分销商列表

export const distributorNextCheck = (params) => http.put(this, '/distributor/v1/web/user/next/check', params) //审核下级分销商

export const brandList = (data) => http.get(this, '/distributor/v1/web/user/up/brand/list', data) //获取分销商可视品牌

export const nobrandList = (method, data) => http.allRequest(method, '/distributor/v1/web/user/next/nobrand/list', data) //获取下级分销商不可视品牌 (GET:获取，POST:设置)

export const goodsListD = (data) => http.post(this, '/distributor/v1/web/user/up/goods/list', data) //获取分销商可视商品

export const nogoodsList = (methods, data) => http.allRequest(methods, '/distributor/v1/web/user/next/nogoods/list', data) //获取下级分销商不可视商品 (GET:获取，POST:设置)

export const scalepriceList = (data) => http.get(this, '/distributor/v1/web/user/next/scaleprice/list', data) //分销商获取价格等级

export const adjustScaleprice = (data) => http.put(this, '/distributor/v1/web/user/next/distributor/scaleprice', data) //调整下级分销商价格等级

export const scaleprice = (methods, data) => http.allRequest(methods, '/distributor/v1/web/user/next/scaleprice', data) //分销商获取价格等级（GET:获取详情；PUT：修改价格等级，POST:新增价格等级,DELETE:删除）

export const scalepriceSpecial = (methods, data) => http.allRequest(methods, '/distributor/v1/web/user/next/scaleprice/special', data) //设置特殊公式 (PUT:修改，POST:新增，DETELE:删除)

export const orderMbnextList = (data) => http.get(this, '/order/v1/web/user/distributor/order/mb/next/list', data) //下级订单列表

export const orderChecktOrder = (data) => http.put(this, '/order/v1/web/user/distributor/order/check/order', data) //批量审核

export const orderMbDetail = (data) => http.get(this, '/order/v1/web/user/distributor/order/mb/detail', data) //分销订单详情

export const phoneVerifyApi = (method, params) => http.allRequest(method, '/distributor/v1/web/user/phone/verify', params) //验证码(手机号码验证验证码：PUT,获取验证码：POST

export const goodList = (params) => http.post(this, '/distributor/v1/web/user/goods/list', params) //根据分销商id查询商品列表

export const columnList = (params) => http.get(this, '/system/v1/web/user/column/list', params) //查询栏目列表

export const columnGoodsList = (params) => http.get(this, '/goods/v1/web/user/column/goods/list', params) //根据栏目获取商品列表

export const goodsList = (params) => http.get(this, '/goods/v1/web/user/goods/list', params) //商品列表列表查询（包括个性定制和新品筛选、收藏）

export const goodsDetails = (params) => http.get(this, '/goods/v1/web/user/goods', params) //根据商品id获取商品详情（也是快速订单商品列表）

export const priceGoodsList = (params) => http.post(this, '/goods/v1/web/user/price/goods/list', params) //根据商品ids获取价格列表

export const priceItemList = (params) => http.post(this, '/goods/v1/web/user/price/item/list', params) //根据商品货品ids获取价格列表

export const sectionList = (params) => http.get(this, '/system/v1/web/user/section/list', params) //查询板块列表

export const noticeList = (params) => http.get(this, '/system/v1/web/user/notice/list', params) //公告列表

export const noticeDetail = (params) => http.get(this, '/system/v1/web/user/notice', params) //公告详情

export const classifyList = (params) => http.get(this, '/goods/v1/web/user/classify/list', params) //获取商品分类列表

export const columnClassifyList = (params) => http.get(this, '/goods/v1/web/user/column/classify/list', params) //根据栏目id获取商品分类列表

export const sectionClassifyList = (params) => http.get(this, '/goods/v1/web/user/section/classify/list', params) //根据板块id获取商品分类列表

export const sectionGoodsList = (params) => http.get(this, '/goods/v1/web/user/section/goods/list', params) //根据板块id获取商品列表

export const subclassifyList = (params) => http.get(this, '/goods/v1/web/user/subclassify/list', params) //根据父级id获取下级分类列表

export const goodsCollection = (method, params) => http.allRequest(method, '/goods/v1/web/user/goods/collection', params) //GET:根据商品id查询商品收藏状态  POST:收藏商品  DELETE:删除收藏的商品

export const listStockByCondition = (params) => http.post(this, '/warehouse/v1/web/user/u/warehouse/stock/listStockByCondition', params) //查询货品库存

export const diyGetByModelIdAndMaterialId = (params) => http.get(this, '/flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId', params) //定制商品查询是否缺货

export const userShopSetting = (params) => http.get(this, '/system/v1/web/user/shopSetting', params) //查询是否显示模糊价和订单结算提示)

export const trainingCenterList = (params) => http.get(this, '/system/v1/web/user/trainingCenter/list', params) //培训中心

export const downloadCenterList = (params) => http.get(this, '/system/v1/web/user/downloadCenter/list', params) //下载中心

export const bannerList = (params) => http.get(this, '/system/v1/web/user/banner/list', params) //首页轮播图

export const agreementSignStatus = (params) => http.get(this, '/system/v1/web/user/agreementSetting/agreementSign/status', params) //查询协议签署状态

export const agreementSignList = (params) => http.get(this, '/system/v1/web/user/agreementSetting/list', params) //查询协议

export const agreementSignId = (params) => http.get(this, '/system/v1/web/user/agreementSetting/id', params) //根据协议id查询协议内容

export const agreementSignDetail = (params) => http.get(this, '/system/v1/web/user/agreementSetting', params) //根据协议id查询协议内容

export const agreementSignDrandId = (params) => http.get(this, '/system/v1/web/user/agreementSetting/brandId', params) //根据品牌id查询协议内容

export const signAgreement = (params) => http.post(this, '/system/v1/web/user/agreementSetting/signAgreement', params) //签署协议

export const groupseckillGoodList = (params) => http.get(this, '/promotion/v1/web/user/groupseckill/goods/list', params) //根据搜索条件查找拼团秒杀商品列表(分页)

export const promotionList = (params) => http.get(this, '/promotion/v1/web/user/promotion/list', params) //根据搜索条件查找促销活动列表(分页)

export const promotionRuleGoodList = (params) => http.get(this, '/promotion/v1/web/user/promotion/rule/goods/list', params) //根据促销活动规格id查找货品列表（分页）

export const promotionRuleList = (params) => http.get(this, '/promotion/v1/web/user/promotion/rule/list', params) //根据促销活动id查找规则列表

export const promotiongroupseckill = (params) => http.get(this, '/promotion/v1/web/user/goods/promotiongroupseckill', params) //根据商品id查找活动

export const goodShopSetting = (params) => http.get(this, '/system/v1/web/user/shopSetting', params) //活动设置

export const userDeposit = (params) => http.get(this, '/financial/v1/web/user/deposit', params) //查询余额

export const depositDetails = (params) => http.get(this, '/financial/v1/web/user/deposit/available/ids', params) //预存款明细余额详情

export const depositList = (params) => http.get(this, '/financial/v1/web/user/deposit/available/list', params) //预存款明细余额列表

export const depositDetailList = (params) => http.get(this, '/financial/v1/web/user/deposit/detail/list', params) //预存款明细明细账

export const depositRecharge = (params) => http.post(this, '/financial/v1/web/user/deposit/recharge', params) //预存款充值

export const depositWithdraw = (params) => http.post(this, '/financial/v1/web/user/deposit/withdraw', params) //预存款提现

export const depositRechargeStatus = (params) => http.get(this, '/financial/v1/web/user/deposit/rechargeStatus', params) //预存款充值状态

export const addShoppingcart = (params) => http.post(this, '/order/v1/web/user/shoppingcart/list', params) //批量加入购物车

export const addShoppingcartOne = (params) => http.post(this, '/order/v1/web/user/shoppingcart', params) //单个加入购物车

export const ModifyShoppingcart = (params) => http.put(this, '/order/v1/web/user/shoppingcart', params) //修改购物车

export const shoppingcartList = (params) => http.get(this, '/order/v1/web/user/shoppingcart/list', params) //获取购物车列表

export const deleteShoppingcartOne = (params) => http.delete(this, '/order/v1/web/user/shoppingcart', params) //单个删除购物车列表

export const deleteShoppingcart = (params) => http.delete(this, '/order/v1/web/user/shoppingcart/ids', params) //批量删除购物车列表 

export const logisticsList = (params) => http.get(this, '/system/v1/web/user/logistics/list', params) //查询配送列表

export const logisticsCalculations = (params) => http.post(this, '/system/v1/web/user/logistics/calculations', params) //根据地区和购买商品获取配送方式

export const logisticsCalculationss = (params) => http.post(this, '/system/v1/web/user/logistics/calculationss', params) //根据地区和购买商品获取配送方式

export const paymentMode = (params) => http.get(this, '/distributor/v1/web/user/payment/mode', params) //获取支付方式

export const placeOrder = (params) => http.post(this, '/order/v1/web/user/distributor/order', params) //下单接口

export const promotionGoodsList = (params) => http.get(this, '/promotion/v1/web/user/promotion/rule/goods/list', params) //根据促销活动规则id查找货品列表

export const promotionPresentList = (params) => http.get(this, '/promotion/v1/web/user/order/promotion/present/list', params) //根据促销活动规则条件id查找赠品列表

export const orderResults = (params) => http.get(this, '/order/v1/web/user/distributor/order/ids', params) //获取订单下单结果

export const payCreateTrade = (params) => http.post(this, '/financial/v1/web/user/pay/createTrade', params) //支付

export const businessGetBusiness = (params) => http.get(this, '/distributor/v1/web/user/business/getBusiness', params) //根据分销商id获取业务数据

export const addStore = (params) => http.post(this, '/flexible/v1/web/user/shop', params) //新增店铺

export const modifyStore = (params) => http.put(this, '/flexible/v1/web/user/shop', params) //修改店铺

export const deleteStore = (params) => http.delete(this, '/flexible/v1/web/user/shop', params) //单个删除店铺

export const deleteStoreS = (params) => http.delete(this, '/flexible/v1/web/user/shop/batch', params) //批量删除店铺

export const queryStore = (params) => http.get(this, '/flexible/v1/web/user/shop/detailById', params) //根据id查询店铺信息

export const queryShopCodeStore = (params) => http.get(this, '/flexible/v1/web/user/shop/getShopByDistributorIdAndShopCode', params) //根据门店编码查询

export const storeList = (params) => http.get(this, '/flexible/v1/web/user/shop/page', params) //查询店铺列表

export const orderExport = (params) => http.post(this, '/order/v1/web/admin/orderInfo/export', params) //订单导出

export const exportStore = (params) => http.post(this, '/flexible/v1/web/user/shop/export', params) //店铺导出

export const importStore = (params) => http.post(this, '/flexible/v1/web/user/shop/p/shop/import', params) //店铺导入

export const tempDownLoadStore = (params) => http.post(this, '/flexible/v1/web/user/shop/tempDownLoad', params) //店铺模板下载

export const platformList = (params) => http.get(this, '/distributor/v1/web/user/wx/platform/list', params) //微信平台列表

export const updateOpenFlagStore = (params) => http.put(this, '/flexible/v1/web/user/shop/updateOpenFlag', params) //修改店铺

export const payQueryTrade = (params) => http.get(this, '/financial/v1/web/user/pay/queryTrade', params) //查询交易

export const orderLists = (params) => http.get(this, '/order/v1/web/user/distributor/order/pc/list', params) //订单列表接口

export const orderDetails = (params) => http.get(this, '/order/v1/web/user/distributor/order/pc/detail', params) //订单详情

export const orderDiyLists = (params) => http.get(this, '/order/v1/web/user/distributor/order/pc/shop/list', params) //柔性店铺订单列表接口

export const orderTypeList = (params) => http.get(this, '/order/v1/web/user/orderType/list', params) //订单类型列表

export const orderDeliverBillDetail = (params) => http.get(this, '/order/v1/web/user/distributor/order/pc/deliverBillDetail', params) //订单发货单详情

export const orderonemore = (params) => http.get(this, '/order/v1/web/user/distributor/order/one/more', params) //再来一单

export const currencyRate = (params) => http.get(this, '/financial/v1/web/user/currencyRate', params) //查询汇率

export const subAccountUserConfig = (params) => http.get(this, '/distributor/v1/web/user/subAccountUserConfig/page', params) // 分账配置列表

export const subAccountUserConfigDetail = (params) => http.get(this, '/distributor/v1/web/user/subAccountUserConfig', params) // 分账配置详情

export const subAccountUserConfigAdd = (params) => http.post(this, '/distributor/v1/web/user/subAccountUserConfig', params) // 新增分账配置

export const subAccountUserConfigUpdate = (params) => http.put(this, '/distributor/v1/web/user/subAccountUserConfig', params) // 修改分账配置

export const subAccountUserConfigDel = (params) => http.delete(this, '/distributor/v1/web/user/subAccountUserConfig', params) // 删除分账配置

export const subAccountUserConfigBy = (params) => http.get(this, '/distributor/v1/web/user/subAccountUserConfig/listByCondition', params) // 分账配置下拉列表

export const shoplistByCondition = (params) => http.get(this, '/flexible/v1/web/user/shop/listByCondition', params) // 关联店铺列表

export const subAccountSaleman = (params) => http.get(this, '/distributor/v1/web/user/subAccountSaleman/page', params) // 分账业务员列表

export const subAccountSalemanAdd = (params) => http.post(this, '/distributor/v1/web/user/subAccountSaleman', params) // 新增分账业务员

export const subAccountSalemanUpdate = (params) => http.put(this, '/distributor/v1/web/user/subAccountSaleman', params) // 更新分账业务员

export const subAccountSalemanStatus = (params) => http.put(this, '/distributor/v1/web/user/subAccountSaleman/updateOpenFlag', params) // 更新账业务员状态

export const subLevelListByCondition = (params) => http.get(this, '/distributor/v1/web/user/subAccountLevel/listByCondition', params) // 分账等级下拉列表

export const subSalemanListByCondition = (params) => http.get(this, '/distributor/v1/web/user/subAccountSaleman/listByCondition', params) // 上级业务员下拉列表

export const subAccountSalemanBy = (params) => http.get(this, '/distributor/v1/web/user/subAccountSaleman/listByCondition', params) // 分账业务员下拉列表

export const getWechatProgramCode = (params) => http.get(this, '/distributor/v1/web/user/subAccountSaleman/getWechatProgramCodeUrl', params) // 获取分销商业务员绑定二维码

export const orderSubAccountList = (params) => http.get(this, '/financial/v1/web/user/orderSubAccount/page', params) // 获取分账记录列表

export const orderSubAccountDetail = (params) => http.get(this, '/financial/v1/web/user/orderSubAccountBill/listByOrderSubAccountId', params) //查询分账详情列表

export const orderSubAccountRatio = (params) => http.get(this, '/financial/v1/web/user/distributor/accountWx/getSubAccountRatioByDistributor', params) //查询分销商服务商的分账比例

export const updateActualSubAccountAmount = (params) => http.put(this, '/financial/v1/web/user/orderSubAccountBill/updateActualSubAccountAmount', params) //修改分账金额、实时分账

export const subAccountAgain = (params) => http.put(this, '/financial/v1/web/user/orderSubAccountBill/subAccountAgain', params) // 重新分账

// 批量导入订单
export const getImportOrderDetail = (params) => http.get(this, '/order/v1/web/user/importOrder', params) // 用户导入订单详情
export const updateImportOrderDetail = (params) => http.put(this, '/order/v1/web/user/importOrder', params) // 用户导入订单修改
export const deleteImportOrder = (params) => http.delete(this, '/order/v1/web/user/importOrder/delete', params) // 删除导入项
export const importOrder = (params) => http.post(this, '/order/v1/web/user/importOrder/import', params) // 用户订单导入接口
export const getImportOrder = (params) => http.get(this, '/order/v1/web/user/importOrder/list', params) // 用户导入订单列表
export const ordersImportOrder = (params) => http.put(this, '/order/v1/web/user/importOrder/orders', params) // 用户导入订单批量下单
export const getTempDownload = (params) => http.get(this, '/order/v1/web/user/importOrder/tempDownLoad', params) // 获取下载模板URL

// 消息中心
export const msgcenterList = (params) => http.get(this, '/thirdparty/v1/web/user/distributor/msgcenter/list', params) // 查询当前用户消息列表
export const msgcenterNotRead = (params) => http.get(this, '/thirdparty/v1/web/user/distributor/msgcenter/not/read/num', params) // 查询当前用户未读消息
export const msgcenterRead = (params) => http.put(this, '/thirdparty/v1/web/user/distributor/msgcenter/read', params) // 消息变为已读
export const msgcenterReadAll = (params) => http.put(this, '/thirdparty/v1/web/user/distributor/msgcenter/read/all', params) // 全部消息变为已读

export const userAgreementSign = (params) => http.get(this, '/system/v1/web/user/agreementSetting/agreementSign/user', params) // 分销商签署协议

// 代金券
export const voucherList = (params) => http.get(this, '/promotion/v1/web/user/rebateVoucher/list', params) // 根据搜索条件查找返利代金券列表(分页)
export const voucherAmount = (params) => http.get(this, '/promotion/v1/web/user/rebateVoucher/getAvailableRebateVoucherBalanceSum', params) // 查询分销商可用代金券总额
export const getVoucherUsedList = (params) => http.get(this, '/promotion/v1/web/user/rebateVoucher/listUsageRecord', params) // 代金券使用记录列表(分页)

// 分账业务员
export const getSubAccountSalesmanTemp = (params) => http.get(this, '/distributor/v1/web/user/subAccountSaleman/tempDownLoad', params) // 获取下载模板URL

// 产品推广
export const proPromotionData = (params) => http.get(this, '/system/v1/web/admin/promotion/getGoodsPromotionByUserId', params) // 根据用户id查询商品推广数据
