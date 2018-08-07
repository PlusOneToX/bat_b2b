/*
 * @Author: Jason
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by:   Jason
 * @Last Modified time: 2018-05-10
 */

// 管理员(商业配置-首页板块) - 首页板块列表
export function getSectionList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/store/section/list', params)
}

// 管理员(商业配置-首页板块) - 首页板块列表总数
export function sectionData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/store/section/count', params)
}

// 管理员(商业配置-首页板块) - 首页板块发布
export function release(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/store/section/release', params)
}

// 管理员(商业配置-首页板块) - 首页板块编辑
export function sectionPut(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/store/section', params)
}
// 管理员(商业配置-首页板块) - 首页板块添加
export function sectionPost(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/store/section', params)
}
// 管理员(商业配置-首页板块) - 首页板块详情
export function sectionGet(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/store/section', params)
}
// 管理员(分类模块) - 分类商品查询列表
export function getCategoryGoods(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/category/goods/ids', params)
}
// 管理员(商品模块) - 商品基本信息-根据ids
export function getGoods(vm, params) {
  return vm.$api.get(vm, 'admin/u/po/goods/ids', params)
}

