import http from './http.js'

// 编辑器
export const getMaterial = (params) => http.get(this, '/flexible/v1/web/user/u/material/tree', params)   // 获取材质列表
export const getModel = (params) => http.get(this, '/flexible/v1/web/user/u/model/tree', params)   // 获取型号列表
export const getPicture = (params) => http.get(this, '/flexible/v1/web/user/u/picture/tree', params)   // 获取图片素材
export const getFont = (params) => http.get(this, '/flexible/v1/web/user/u/font/listUsable', params)   // 获取字体
export const getInfo = (params) => http.get(this, '/flexible/v1/web/user/u/modelMaterialRelevance/getByModelIdAndMaterialId', params)   // 获取定制信息
export const getCacheIP = (params) => http.get(this, '/flexible/v1/web/user/u/pictureModelMaterialDiy', params)   // 获取缓存IP图
export const handleCacheIP = (params) => http.post(this, '/flexible/v1/web/user/u/pictureModelMaterialDiy', params)   // 缓存IP图
export const recordPoint = (params) => http.post(this, '/flexible/v1/web/user/buryingPoint', params)   // 缓存IP图

export const getOSSSts = (params) => http.get(this, '/system/v1/web/admin/oss/sts', params)   // 获取OSS对象存储

// 购物车
export const addToShopcart = (params) => http.post(this, '/order/v1/web/user/shoppingcart', params)   // 加入购物车

