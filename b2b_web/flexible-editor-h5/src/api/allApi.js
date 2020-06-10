
let originBaseURL = "";
if (process.env.NODE_ENV === "production") {
  originBaseURL = 'https://api.bat.com/' // 正式
} else if (process.env.NODE_ENV === "test") {
  originBaseURL = 'https://test.bat.com/' // 测试
} else {
  originBaseURL = 'https://test.bat.com/' // 本地
}
let newBaseURL = "";

module.exports = {
  // 获取租户地址
  getTenant: originBaseURL + 'platform/v1/web/tenant/url', // 获取租户地址

  // 编辑器
  getMaterialList: newBaseURL + 'flexible/v1/web/user/u/material/tree', // 获取材质列表
  getModelList: newBaseURL + 'flexible/v1/web/user/u/model/tree', // 获取型号列表
  getPictureList: newBaseURL + 'flexible/v1/web/user/u/picture/tree', // 获取图片素材
  getFontList: newBaseURL + 'flexible/v1/web/user/u/font/listUsable', // 获取字体
  getCustomInfo: newBaseURL + 'flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId', // 获取定制信息
  getPrice: newBaseURL + 'flexible/v1/web/user/u/material/price', // 获取价格
  getCacheIP: newBaseURL + 'flexible/v1/web/user/u/pictureModelMaterialDiy', // 获取缓存IP图
  handleCacheIP: newBaseURL + 'flexible/v1/web/user/u/pictureModelMaterialDiy', // 缓存IP图
  recordPoint: newBaseURL + 'flexible/v1/web/user/buryingPoint', // 记录数据埋点
  getModelByNetwork: newBaseURL + 'flexible/v1/web/user/u/model/getOneByNetworkModel', // 根据入网型号查询机型

  getOSSSts: newBaseURL + 'system/v1/web/admin/oss/sts', // 获取OSS对象存储

  // 订单
  handleOrder: newBaseURL + 'thirdparty/v1/web/open/order/create', // 创建临时订单、提交到第三方接口
}
