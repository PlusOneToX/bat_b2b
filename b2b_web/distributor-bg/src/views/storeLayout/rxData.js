/*
 * @Author: yaowei
 * @Date: 2018-05-25 14:31:51
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-26 08:53:51
 */

import Axios from "axios"

// 推荐管理 - 列表
export function getRecommendList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorIndexRecommend/page', params)
}
// 推荐管理 - 新增
export function addRecommend(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/distributorIndexRecommend', params)
}
// 推荐管理 - 详情
export function recommendDetail(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorIndexRecommend/detail', params)
}
// 推荐管理 - 编辑
export function editRecommend(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributorIndexRecommend', params)
}
// 推荐管理 - 上移下移
export function moveRecommend(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/indexRecommendPictureRela/upOrDown', params)
}
// 推荐管理 - 删除（单个）
export function deleteRecommend(vm, id) {
  return vm.$api.delete(vm, 'admin/u/p/indexRecommendPictureRela/deleteById?id=' + id)
}
// 推荐管理 - 删除（批量）
export function batchDeleteRecommend(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/indexRecommendPictureRela/batchDelete', params)
}

// 添加图片
export function pictureList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/picture/pageByDistributor', params)
}

// 系列展示管理 - 列表
export function categoryList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorSeriesTheme/page', params)
}
// 系列展示管理 - 列表（无分页）
export function categoryListAll(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorSeriesTheme/list', params)
}
// 系列展示管理 - 新增
export function addCategory(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/distributorSeriesTheme', params)
}
// 系列展示管理 - 详情
export function categoryDetail(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorSeriesTheme/detail', params)
}
// 系列展示管理 - 编辑
export function editCategory(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributorSeriesTheme', params)
}
// 系列展示管理 - 上移下移
export function moveCategory(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/seriesPictureRela/upOrDown', params)
}
// 系列展示管理 - 主题名称
export function themeName(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorSeriesTheme/listSimpleByCondition', params)
}
// 系列展示管理 - 删除（单个）
export function deleteCategory(vm, id) {
  return vm.$api.delete(vm, 'admin/u/p/seriesPictureRela/deleteById?id=' + id)
}
// 系列展示管理 - 删除（批量）
export function batchDeleteCategory(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/seriesPictureRela/batchDelete', params)
}
// 系列展示管理 - 删除主题系列
export function deleteTheme(vm, id) {
  return vm.$api.delete(vm, 'admin/u/p/distributorSeriesTheme?id=' + id)
}

// 轮播图管理 - 列表
export function bannerList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorBanner/page', params)
}
// 轮播图管理 - 新增
export function addBanner(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/distributorBanner', params)
}
// 轮播图管理 - 详情
export function bannerDetail(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distributorBanner/detail', params)
}
// 轮播图管理 - 编辑
export function editBanner(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distributorBanner', params)
}
// 轮播图管理 - 删除（单个）
export function deleteBanner(vm, id) {
  return vm.$api.delete(vm, 'admin/u/p/distributorBanner/deleteById?id=' + id)
}
// 轮播图管理 - 删除（批量）
export function batchDeleteBanner(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/distributorBanner/batchDelete', params)
}

// 查询店铺列表
export function getStoreList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/shop/list', params)
}
// 查询单个店铺
export function selectStore(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/shop', params)
}
// 添加店铺
export function addStore(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/shop', params)
}
// 编辑店铺
export function editStore(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/shop', params)
}
// 删除单个店铺
export function deleteStore(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/shop', params)
}
// 批量删除店铺
export function batchDeleteStore(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/shop/list', params)
}
// 导出店铺信息
export function exportShop(vm, params) {
  return vm.$api.exportData(vm, 'admin/u/p/exportShop', params)
}
// 根据分销商用户名获取appid
export function getAppid(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/shop/appId', params)
}

// 官方主题配置 - 列表
export function getThemeList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/picture/theme/list', params)
}
// 官方主题配置 - 增加
export function addTheme(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/picture/theme', params)
}
// 官方主题配置 - 修改/启用/停用
export function editTheme(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/picture/theme', params)
}
// 官方主题配置 - 删除
export function deleteCTheme(vm, id) {
  return vm.$api.delete(vm, 'admin/u/p/picture/theme/' + id)
}
// 官方主题配置 - 详情
export function getThemeDetail(vm, id) {
  return vm.$api.get(vm, 'admin/u/p/picture/theme/' + id)
}
// 官方主题配置 - 获取图片分类
export function getCategory(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/picture/category/not/child/page', params)
}
// 官方主题配置 - 关联主题
export function bindTheme(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/picture/theme/relation', params)
}

// 公告管理 - 列表
export function getNoticeList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/exchangeNotice/list', params)
}
// 公告管理 - 增加
export function addNotice(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/exchangeNotice', params)
}
// 公告管理 - 修改/启用/停用
export function editNotice(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/exchangeNotice', params)
}
// 公告管理 - 删除
export function deleteNotice(vm, id) {
  return vm.$api.delete(vm, 'admin/u/p/exchangeNotice/' + id)
}
// 公告管理 - 详情
export function getNoticeDetail(vm, id) {
  return vm.$api.get(vm, 'admin/u/p/exchangeNotice/' + id)
}
