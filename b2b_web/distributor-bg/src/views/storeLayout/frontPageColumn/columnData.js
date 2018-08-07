/*
 * @Author: Jason
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by:   Jason
 * @Last Modified time: 2018-05-10
 */

// 管理员(商业配置-首页栏目) - 首页栏目列表
export function getColumnList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/store/column/list', params)
}

// 管理员(商业配置-首页栏目) - 首页栏目列表总数
export function columnData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/store/column/count', params)
}

// 管理员(商业配置-首页栏目) - 首页栏目发布
export function release(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/store/column/release', params)
}

// 管理员(商业配置-首页栏目) - 首页栏目修改
export function columnPut(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/store/column', params)
}
// 管理员(商业配置-首页栏目) - 首页栏目添加
export function columnPost(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/store/column', params)
}
// 管理员(商业配置-首页栏目) - 首页栏目详情
export function columnGet(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/store/column', params)
}
// 管理员(营销推广-活动促销) - 促销活动获取商品id列表
export function getPromotionGoods(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion/goods/ids', params)
}
// 管理员(分类模块) - 分类商品查询列表
export function getGoods(vm, params) {
  return vm.$api.get(vm, 'admin/u/po/goods/ids', params)
}

// 管理员(营销推广-活动促销) - 活动促销列表
export function getPromotionList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/marketing/promotion/list', params)
}

