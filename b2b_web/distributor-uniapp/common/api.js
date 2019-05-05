import {request} from './request.js'

export const  commonConfig = (data) => request('/platform/v1/web/tenant/common/config','GET',data)  //获取平台配置

export const  regionList = (data) => request('/system/v1/web/user/region/list','GET',data)  //查看区域

export const  region = (data) => request('/system/v1/web/user/region','GET',data)  //通过id查单个区域

export const  login = (data) => request('/distributor/v1/web/user/login','POST',data)  //登录

export const  oneLevelApply = (data) => request('/distributor/v1/web/user/one-level/apply','POST',data)  //一级注册

export const  changePassword = (data) => request('/distributor/v1/web/user/password','PUT',data)  //修改密码

export const  phoneVerify = (methods,data) => request('/distributor/v1/web/user/phone/verify',methods,data) //POST:获取验证码,PUT:验证验证码

export const bindWechat = (data) => request('/distributor/v1/web/user/subAccountSaleman/bindWechat', 'PUT',data) // 业务员绑定微信

export const  loginByPhoneAndSmsCode = (data) => request('/distributor/v1/web/user/wx/program/mini/loginByPhoneAndSmsCode','POST',data)  //分销微信小程序根据手机号码和验证码登录

export const  wechatLogin = (data) => request('/distributor/v1/web/user/wx/program/mini/login','POST',data)  //分销微信小程序授权登录

export const  loginOut = (data) => request('/distributor/v1/web/user/wx/program/mini/logOut','PUT',data)  //分销登出

export const  loginByOpenId = (data) => request('/distributor/v1/web/user/wx/program/mini/loginByOpenId','POST',data)  //根据openid登录

export const  mimiOpenid = (data) => request('/distributor/v1/web/user/wx/program/mini/openid','POST',data)  //小程序授权openId

export const  getOpenId = (data) => request('/distributor/v1/web/user/customer/wx/program/official/getOpenId','POST',data)  //公众号获取openId

export const  userInfo = (data) => request('/distributor/v1/web/user/info','GET',data)  //查询个人信息

export const  addressList = (data) => request('/distributor/v1/web/user/address/list','GET',data)  //收货地址列表

export const  addressDefault = (data) => request('/distributor/v1/web/user/address/default','PUT',data)  //设置默认收货地址

export const  addressSet = (methods,data) => request('/distributor/v1/web/user/address',methods,data)  //收货地址(PUT:修改 POST：新增 DELETE：删除)

export const  changePhone = (data) => request('/distributor/v1/web/user/change/phone','PUT',data)  //修改手机号

export const  distributorNextList = (data) => request('/distributor/v1/web/user/next/list','GET',data)  //查看下级分销商列表

export const  distributorNextDetails = (data) => request('/distributor/v1/web/user/next','GET',data)  //查看下级分销商详情

export const  distributorNextCheck = (data) => request('/distributor/v1/web/user/next/check','PUT',data)  //审核下级分销商

export const  distributorNextApply = (data) => request('/distributor/v1/web/user/next/apply','POST',data)  //下级分销商注册申请

export const  distributorQrcode = (data) => request('/distributor/v1/web/user/distribution/qrcode','GET',data)  //分销商获取分销商二维码

export const  scalepriceList = (data) => request('/distributor/v1/web/user/next/scaleprice/list','GET',data)  //分销商获取价格等级

export const  scaleprice = (methods,data) => request('/distributor/v1/web/user/next/scaleprice',methods,data)  //分销商获取价格等级（GET:获取详情；PUT：修改价格等级，POST:新增价格等级,DELETE:删除）

export const  adjustScaleprice = (data) => request('/distributor/v1/web/user/next/distributor/scaleprice','PUT',data)  //调整下级分销商价格等级

export const  brandList = (data) => request('/distributor/v1/web/user/up/brand/list','GET',data)  //获取分销商可视品牌

export const  nobrandList = (methods,data) => request('/distributor/v1/web/user/next/nobrand/list',methods,data)  //获取下级分销商不可视品牌 (GET:获取，POST:设置)

export const  goodsList = (data) => request('/distributor/v1/web/user/up/goods/list','POST',data)  //获取分销商可视商品

export const  nogoodsList = (methods,data) => request('/distributor/v1/web/user/next/nogoods/list',methods,data)  //获取下级分销商不可视商品 (GET:获取，POST:设置)

export const getInfoByDistributorId = (data) =>request('/distributor/v1/web/user/getBaseMsgByDistributorId','GET',data) // 根据分销商id查找分销商基本数据

export const  scalepriceSpecial = (methods,data) => request('/distributor/v1/web/user/next/scaleprice/special',methods,data)  //设置特殊公式 (PUT:修改，POST:新增，DETELE:删除)

export const  userMoblie = (data) => request('/system/v1/web/user/mobile','GET',data)  //通过模块类型查询移动端首页内容

export const  userMoblieList = (data) => request('/system/v1/web/user/mobile/list','GET',data)  //查询首页列表

export const  classifyList = (data) => request('/goods/v1/web/user/classify/list/h5','GET',data)  //商品分类列表

export const  mobileGoodsList= (data) => request('/goods/v1/web/user/mobile/goods/list','GET', data)   //根据板块id获取商品列表

export const  mobileGoodsListP= (data) => request('/goods/v1/web/user/mobile/goods/list','POST', data)   //根据板块id获取商品列表

export const  priceGoodsList = (data) => request('/goods/v1/web/user/price/goods/list','POST', data)   //根据商品ids获取价格列表

export const  priceItemList = (data) => request('/goods/v1/web/user/price/item/list','POST', data)   //根据商品货品ids获取价格列表

export const  goodsLabels = (data) => request('/goods/v1/web/user/goods/labels','POST', data)   //根据商品货品ids获取商品标签

export const  goodsCollection = (methods,data) => request('/goods/v1/web/user/goods/collection',methods,data)
//GET:根据商品id查询商品收藏状态  POST:收藏商品  DELETE:删除收藏的商品

export const  userGoodsList= (data) => request('/goods/v1/web/user/goods/list','GET', data)   //商品列表查询（个性定制、收藏、新品筛选）

export const  goodsDetails = (data) => request('/goods/v1/web/user/goods','GET', data)   //根据商品id获取商品详情（也是快速订单商品列表）

export const  userShopSetting = (data) => request('/system/v1/web/user/shopSetting','GET', data)   //查询是否显示模糊价和订单结算提示)

export const  listStockByCondition = (data) => request('/warehouse/v1/web/user/u/warehouse/stock/listStockByCondition','POST', data)   //查询货品库存

export const diyGetByModelIdAndMaterialId = (data) => request('/flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId','GET', data) //定制商品查询是否缺货

export const  promotiongroupseckill = (data) => request('/promotion/v1/web/user/goods/promotiongroupseckill','GET', data)   //根据商品id查找活动

export const  userDeposit = (data) => request('/financial/v1/web/user/deposit','GET', data)   //预存款设置查询

export const  paymentMode = (params) => request('/distributor/v1/web/user/payment/mode','GET', params)   //获取支付方式

export const  depositDetailList = (data) => request('/financial/v1/web/user/deposit/detail/list','GET', data)   //预存款明细明细账

export const  depositRecharge = (data) => request('/financial/v1/web/user/deposit/recharge','POST', data)   //预存款充值

export const  depositDetailSummary = (data) => request('/financial/v1/web/user/deposit/detail/summary','GET', data)   //按时间范围查询资产明细

export const  addShoppingcart = (data) => request('/order/v1/web/user/shoppingcart/list','POST', data)   //批量加入购物车

export const  addShoppingcartOne = (data) => request('/order/v1/web/user/shoppingcart','POST', data)   //单个加入购物车

export const  ModifyShoppingcart = (data) => request('/order/v1/web/user/shoppingcart','PUT', data)   //修改购物车

export const  shoppingcartList = (data) => request('/order/v1/web/user/shoppingcart/list','GET', data)   //获取购物车列表

export const  deleteShoppingcartOne = (data) => request('/order/v1/web/user/shoppingcart','DELETE', data)   //单个删除购物车列表

export const  deleteShoppingcart = (data) => request('/order/v1/web/user/shoppingcart/ids','DELETE', data)   //批量删除购物车列表

export const  promotionPresentList = (data) => request('/promotion/v1/web/user/order/promotion/present/list','GET', data)   //根据促销活动规则条件id查找赠品列表

export const  logisticsCalculationss = (data) => request('/system/v1/web/user/logistics/calculationss', 'POST',data)   //根据地区和购买商品获取配送方式

export const  payCreateTrade = (data) => request('/financial/v1/web/user/pay/createTrade', 'POST',data)   //支付

export const  payQueryTrade = (data) => request('/financial/v1/web/user/pay/queryTrade', 'GET',data)   //查询支付状态

export const  placeOrder = (data) => request('/order/v1/web/user/distributor/order', 'POST', data)   //下单接口

export const  orderMbList = (data) => request('/order/v1/web/user/distributor/order/mb/list','GET', data)   //订单列表

export const  orderMbNextCount = (data) => request('/order/v1/web/user/distributor/order/mb/next/count','GET', data)   //移动端获取下级分销订单总数

export const  orderMbDetail = (data) => request('/order/v1/web/user/distributor/order/mb/detail','GET', data)   //订单详情

export const  orderDeliverBillDetail = (data) => request('/order/v1/web/user/distributor/order/mb/deliverBillDetail','GET', data)   //订单发货单详情

export const  orderMbnextList = (data) => request('/order/v1/web/user/distributor/order/mb/next/list','GET', data)   //下级订单列表

export const  orderChecktOrder = (data) => request('/order/v1/web/user/distributor/order/check/order','PUT', data)   //批量审核

export const  agreementSettingList = (data) => request('/system/v1/web/user/agreementSetting/list','GET', data)   //查询协议

export const  agreementSettingId = (data) => request('/system/v1/web/user/agreementSetting/id','GET', data)   //根据协议id查询协议内容

export const  orderMbCount = (data) => request('/order/v1/web/user/distributor/order/mb/count','GET', data)   //获取订单数量

export const  programMiniOpenId = (data) => request('/distributor/v1/web/user/wx/program/mini/openid','POST', data)   //微信小程序授权登录

export const  versionBaseSetting = (data) => request('/system/v1/web/user/baseSetting','GET', data)   //获取构建版本号

export const  updateOpenId = (data) => request('/distributor/v1/web/user/update/openid','PUT', data)   //更新当前分销商openId


// 消息中心
export const msgcenterList = (params) => request('/thirdparty/v1/web/user/distributor/msgcenter/list','GET', params) // 查询当前用户消息列表
export const msgcenterNotRead = (params) => request('/thirdparty/v1/web/user/distributor/msgcenter/not/read/num','GET', params) // 查询当前用户未读消息
export const msgcenterRead = (params) => request('/thirdparty/v1/web/user/distributor/msgcenter/read','PUT', params) // 消息变为已读
export const msgcenterReadAll = (params) => request('/thirdparty/v1/web/user/distributor/msgcenter/read/all','PUT', params) // 全部消息变为已读

// 代金券
export const voucherList = (params) => request('/promotion/v1/web/user/rebateVoucher/list', 'GET', params) // 根据搜索条件查找返利代金券列表(分页)
export const voucherAmount = (params) => request('/promotion/v1/web/user/rebateVoucher/getAvailableRebateVoucherBalanceSum', 'GET', params) // 查询分销商可用代金券总额
export const getVoucherUsedList = (params) => request('/promotion/v1/web/user/rebateVoucher/listUsageRecord', 'GET', params) // 代金券使用记录列表(分页)

// 首页栏目
export const getColumn = (params) => request('/system/v1/web/user/column','GET', params) // 通过ID查询栏目
export const getColumnList = (params) => request('/goods/v1/web/user/column/goods/list','GET', params) // 通过栏目ID查询商品列表

// 联系客服
export const getWechatInfo = (params) => request('/system/v1/web/user/baseSetting' + params, 'GET') // 通过栏目ID查询商品列表

// 产品推广
export const proPromotionData = (params) => request('/system/v1/web/admin/promotion/getGoodsPromotionByUserId', 'GET', params) // 根据用户id查询商品推广数据

// 二级分销冻结
export const  twoDistributorFreeze = (data) => request('/distributor/v1/web/admin/distributor/open','PUT' ,data)  //二级分销冻结


export function region2 (id) {
    return new Promise((resolve)=> {
       regionList({parentId:id,page:1,size:3000}).then(res => {
            if (res.success) {
                resolve (res)
            }
        }).catch(err => {


        })
    })

}
