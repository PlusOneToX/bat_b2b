let originBaseURL = "";
let newBaseURL = "";

// #ifdef H5
newBaseURL = "https://api.bat.com/"; // 正式
originBaseURL = "https://api.bat.com/"; // 正式
// newBaseURL = "https://test.bat.com/"; // 测试
// originBaseURL = "https://test.bat.com/"; // 测试
// #endif

// #ifdef MP-WEIXIN
if (uni.getAccountInfoSync().miniProgram.appId == "wx05cb4496de7a20d7") {
    originBaseURL = "https://api.bat.com/"; // 正式
    newBaseURL = "https://api.bat.com/";
} else {
    newBaseURL = "https://test.bat.com/"; // 测试
    originBaseURL = "https://test.bat.com/"; // 测试
}
// #endif

const API_URL_CN = "https://cn-auth2.samsungosp.com.cn/"; // cn-auth.samsungosp.com.cn / https://cn-api.samsungosp.com/
const API_URL = "https://api.samsungosp.com";

module.exports = {
    wechatLogin:
        originBaseURL +
        "distributor/v1/web/user/customer/wx/program/mini/phone",

    // 获取租户地址
    getTenant: originBaseURL + "platform/v1/web/tenant/url", // 获取租户地址
    // 获取OSS
    uploadFile: originBaseURL + "system/v1/web/admin/oss/sts", //  获取OSS
    // 用户（C端客户前台接口）
    getVerify: newBaseURL + "distributor/v1/web/user/customer/verify", // 获取验证码
    verifyLogin: newBaseURL + "distributor/v1/web/user/customer/verify/login", // 验证码登录
    thirdpartyLogin:
        newBaseURL + "distributor/v1/web/user/customer/thirdparty/login", // 第三方系统登录
    getUserInfo: newBaseURL + "distributor/v1/web/user/customer", // 获取用户信息
    updateUserInfo: newBaseURL + "distributor/v1/web/user/customer", // 修改用户信息
    updatePswByPsw: newBaseURL + "distributor/v1/web/user/customer/password", // 旧密码修改密码
    passwordLogin:
        newBaseURL + "distributor/v1/web/user/customer/password/login", // 密码登录

    userIdLogin: newBaseURL + "distributor/v1/web/user/customer/id/login", // userId登录

    wxLogin:
        newBaseURL +
        "distributor/v1/web/user/customer/wx/program/official/login", // 微信授权登陆

    // 首页
    getBanner:
        newBaseURL +
        "flexible/v1/web/user/distributorBanner/listByDistributorId", // 轮播图banner列表
    getBannerListById:
        newBaseURL +
        "flexible/v1/web/user/distributorBanner/pagePictureByBannerId", // 根据bannerId分页查询
    getCategory:
        newBaseURL +
        "flexible/v1/web/user/distributorSeriesTheme/listByDistributorId", // 系列列表
    getCategoryListById:
        newBaseURL +
        "flexible/v1/web/user/distributorSeriesTheme/pageBySeriesId", // 根据系列id分页查询
    getRecommend:
        newBaseURL + "flexible/v1/web/user/distributorIndexRecommend/page", // 推荐

    // 编辑器
    getMaterialList: newBaseURL + "flexible/v1/web/user/u/material/tree", // 获取材质列表
    getModelList: newBaseURL + "flexible/v1/web/user/u/model/tree", // 获取型号列表
    getPictureList: newBaseURL + "flexible/v1/web/user/u/picture/tree", // 获取图片素材
    getFontList: newBaseURL + "flexible/v1/web/user/u/font/listUsable", // 获取字体
    getCustomInfo:
        newBaseURL +
        "flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId", // 获取定制信息

    getCacheIP: newBaseURL + "flexible/v1/web/user/u/pictureModelMaterialDiy", // 获取缓存IP图
    handleCacheIP:
        newBaseURL + "flexible/v1/web/user/u/pictureModelMaterialDiy", // 缓存IP图
    getCanvaToken: newBaseURL + "flexible/v1/web/user/canva/token", // 获取 canva token
    recordPoint: newBaseURL + "flexible/v1/web/user/buryingPoint", // 记录数据埋点
    getModelByNetwork:
        newBaseURL + "flexible/v1/web/user/u/model/getOneByNetworkModel", // 根据入网型号查询机型
    // getItemInfoByMaterial: newBaseURL + 'flexible/v1/web/user/u/material/getItemMsgForMaterial', // 根据材质获取货品信息

    getOSSSts: newBaseURL + "system/v1/web/admin/oss/assume/sts", // 获取OSS对象存储
    // getOSSSts: newBaseURL + "​system​/v1​/web​/admin​/oss​/assume​/sts", // 获取OSS对象存储

    // 购物车
    addToShopcart: newBaseURL + "order/v1/web/user/customer/shoppingcart/diy", // 加入购物车
    getShopcartList:
        newBaseURL + "order/v1/web/user/customer/shoppingcart/list", // 获取购物车列表
    updataShopcart: newBaseURL + "order/v1/web/user/customer/shoppingcart", // 修改购物车商品
    deleteShopcart: newBaseURL + "order/v1/web/user/customer/shoppingcart/ids", // 删除购物车商品（批量）

    // 订单
    addOrder: newBaseURL + "order/v1/web/user/customer/order", // 下单
    getOrderList: newBaseURL + "order/v1/web/user/customer/order/list", // 订单列表
    auditpua: newBaseURL + "order/v1/web/user/distributor/order/c/check", // 审核订单
    cgetOrderList: newBaseURL + "order/v1/web/user/distributor/order/c/list", // c端订单列表
    getOrderDetail: newBaseURL + "order/v1/web/user/customer/order/detail", // 订单详情
    handleCancelOrder: newBaseURL + "order/v1/web/user/customer/order/cancel", // 取消订单
    getOrderTime: newBaseURL + "order/v1/web/user/customer/order/time", // 订单失效时间

    // 支付
    handlePayment: newBaseURL + "financial/v1/web/user/pay/createTrade", // 创建交易

    // 配送方式
    getCalculations: newBaseURL + "system/v1/web/user/logistics/calculations", // 根据地区和购买商品获取配送方式和计算配送费用
    getLogistics: newBaseURL + "system/v1/web/user/logistics", // 通过ID查询单个配送

    // 优惠券
    getCouponList: newBaseURL + "promotion/v1/web/user/customer/coupon/list", // 优惠券列表
    receiveCoupon: newBaseURL + "promotion/v1/web/user/customer/coupon/receive", // 领取优惠券
    getSpecialCoupon:
        newBaseURL + "promotion/v1/web/user/customer/coupon/special/list", // 根据优惠券类型获取优惠券列表(不分页)
    getGoodsCoupn:
        newBaseURL + "promotion/v1/web/user/customer/coupon/goods/list", // 根据商品列表获取优惠券列表
    getGoodsCoupnCount:
        newBaseURL + "promotion/v1/web/user/customer/coupon/goods/enable/count", // 根据商品列表获取优惠券列表数量
    getOptimalCoupon:
        newBaseURL + "promotion/v1/web/user/customer/coupon/goods/optimal", // 根据商品列表获取最优优惠券

    // 店铺
    getShopStatus: newBaseURL + "flexible/v1/web/user/shop", // 获取店铺信息
    getShopStatusByShopCode:
        newBaseURL +
        "flexible/v1/web/user/shop/getShopByDistributorIdAndShopCode", // 根据店铺编码获取店铺信息

    // 地址管理
    getAddrList: newBaseURL + "distributor/v1/web/user/customer/address/list", // 地址列表
    setDefaultAddr:
        newBaseURL + "distributor/v1/web/user/customer/address/default", // 设置默认地址
    updateAddr: newBaseURL + "distributor/v1/web/user/customer/address", // 新增/修改/删除地址
    regionList: newBaseURL + "system/v1/web/user/region/list", // 查看区域

    /* 荣耀 */
    getCodeStatus:
        newBaseURL + "order/v1/web/user/customer/order/find/by/thirdCode", // 根据第三方兑换码获取订单信息
    // 订单
    thirdAddOrder: newBaseURL + "order/v1/web/user/customer/order/exchange", // 兑换码下单
    writeOffOrder:
        newBaseURL + "order/v1/web/user/customer/order/writeOff/by/thirdCode", // 根据第三方兑换码核销订单

    // 业务员绑定
    phoneVerify: newBaseURL + "distributor/v1/web/user/phone/verify", // 获取验证码
    getInfoByDistributorId:
        newBaseURL + "distributor/v1/web/user/getBaseMsgByDistributorId", // 根据分销商id查找分销商基本数据
    bindWechat:
        newBaseURL + "distributor/v1/web/user/subAccountSaleman/bindWechat", // 业务员绑定微信

    // 获取公众号签名配置
    getWxConfig: newBaseURL + "flexible/v1/web/user/wechat/gz/config", // 获取公众号签名配置

    // 获取分销商信息
    getDistributorInfo:
        newBaseURL + "distributor/v1/web/user/getBaseMsgByDistributorId", // 根据分销商id查找分销商基本数据

    // 快递停发区域
    findMatchDeliveryStop:
        newBaseURL + "order/v1/web/user/orderDeliverStopPlace/findMatch", // 查询该地址是否停发

    //分页查询官方主题
    getThemeList:
        newBaseURL + "flexible/v1/web/user/pictureTheme/series/theme/list", //分页查询官方主题

    //根据id查询主题详情
    getThemeDateil:
        newBaseURL +
        "flexible/v1/web/user/pictureTheme/series/theme/picture/list", //根据id查询主题详情

    //查询图片分类
    getimgTypeList:
        newBaseURL + "flexible/v1/web/user/u/picture/detail/picture/category", //查询图片分类

    //根据类型获取图片
    getimgList: newBaseURL + "flexible/v1/web/admin/u/p/picture/page", //根据类型获取图片

    //订单发货单详情
    orderDeliverBillDetail:
        newBaseURL + "order/v1/web/user/distributor/order/mb/deliverBillDetail", //订单发货单详情

    //根据父级获取子级材质分类或材质列表
    getNewmaterialList: newBaseURL + "flexible/v1/web/user/u/material/list", //根据父级获取子级材质分类或材质列表

    //根据父级ID查找图片分类列表
    getImageTypeList:
        newBaseURL + "flexible/v1/web/user/u/picture/category/list", //根据父级ID查找图片分类列表

    //根据图片分类Id和材质、型号查找图片列表
    getImageList:
        newBaseURL + "flexible/v1/web/user/u/picture/category/picture/list", //根据图片分类Id和材质、型号查找图片列表

    getPrice: newBaseURL + "flexible/v1/web/user/u/material/price", // 获取价格
    //获取手机型号
    getphoneList: newBaseURL + "flexible/v1/web/user/u/model/list", // 获取手机型号

    //根据机型编码或机型名称获取默认材质关系线框图和底图
    GetmodelMaterialRelevance:
        newBaseURL + "flexible/v1/web/user/u/model/modelMaterialRelevance", // 根据机型编码或机型名称获取默认材质关系线框图和底图

    //根据机型编码或机型名称获取型号和材质数据
    defaultmodelMaterialRelevance:
        newBaseURL +
        "flexible/v1/web/user/u/model/default/modelMaterialRelevance", // 根据机型编码或机型名称获取型号和材质数据

    //型号材质关联前台管理接口
    getByModelIdAndMaterialId:
        newBaseURL +
        "flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId", // 型号材质关联前台管理接口
    //定制商品作品收藏
    collection: newBaseURL + "goods/v1/web/customer/goods/diy/collection", // 定制商品作品收藏

    //定制商品作品收藏列表
    getCollectionList:
        newBaseURL + "goods/v1/web/customer/goods/diy/collection", // 定制商品作品收藏列表

    //根据材质id查询材质对象
    getmaterialData: newBaseURL + "flexible/v1/web/user/u/material", // 根据材质id查询材质对象

    //获取openid
    getOpenId:
        newBaseURL +
        "distributor/v1/web/user/customer/wx/program/official/getOpenId", // 获取openid

    openidlogin:
        newBaseURL + "distributor/v1/web/user/customer/thirdparty/login",
    //根据文字生成图片
    txtPicture:
        newBaseURL + "flexible/v1/web/user/u/picture/characters/picture",

    // 获取柔性定制商城链接(C端H5,微信小程序等链接)
    getMiniappUrl: newBaseURL + "thirdparty/v1/web/user/common/url",
    getMiniUrl: newBaseURL + "thirdparty/v1/web/user/common/mini/url",

    //店铺分销员申请
    applyforsub: newBaseURL + "distributor/v1/web/user/customer/shop/sub",

    //查询店铺分销员
    getdistributorUser:
        newBaseURL + "distributor/v1/web/user/customer/shop/sub/list",

    //审核分销员
    checkDistributor:
        newBaseURL + "distributor/v1/web/user/customer/shop/sub/check",
    //店铺订单统计
    shopOrderStatistics:
        newBaseURL +
        "order/v1/web/user/distributor/order/shop/count/statistics",

    //店铺金额统计
    shopAmountStatistics:
        newBaseURL +
        "order/v1/web/user/distributor/order/shop/amount/statistics",

    //查询店铺余额
    userDeposit: newBaseURL + "financial/v1/web/user/deposit",

    //获取余额明细列表
    depositDetailList: newBaseURL + "financial/v1/web/user/deposit/detail/list",

    //按时间范围查询余额明细
    depositDetailListTime:
        newBaseURL + "financial/v1/web/user/deposit/detail/summary",

    //预存款充值
    depositRecharge: newBaseURL + "financial/v1/web/user/deposit/recharge",

    //查看分销员业务设置
    distributorSet: newBaseURL + "distributor/v1/web/user/business",

    //获取分享二维码
    getQrcode: newBaseURL + "distributor/v1/web/user/customer/shop/staff/qr",

    //获取分销商分销二维码
    distributorQrcode: newBaseURL + "flexible/v1/web/user/shop/sub/qr",

    //分配订单
    staff: newBaseURL + "order/v1/web/user/distributor/order/shop/staff",

    //分销员分佣统计
    subcommissionStatistics:
        newBaseURL + "financial/v1/web/user/commission/staff/statistics",

    //分销员分佣明细统计
    subcommissionStatisticsDetail:
        newBaseURL + "financial/v1/web/user/commission/staff/book/statistics",

    //分销员分佣明细列表
    subcommissionStatisticsList:
        newBaseURL + "financial/v1/web/user/commission/staff/book/list",

    //店铺奖金统计
    shopStatistics:
        newBaseURL + "financial/v1/web/user/commission/shop/statistics",

    //授权支付宝
    authorization:
        newBaseURL + "distributor/v1/web/user/customer/shop/sub/bind",
    //解绑
    unbind: newBaseURL + "distributor/v1/web/user/customer/shop/sub/unbind",

    //根据分销商查询支付宝appid
    GetAlipayId: newBaseURL + "financial/v1/web/user/distributor/account/ali",

    //根据分销商查询微信appid
    GetwxId: newBaseURL + "financial/v1/web/user/distributor/account/wx",
    //苏宁订单验证兑换券
    suningVerifycode: newBaseURL + "thirdparty/v1/api/open/suning/order/verify",
};
