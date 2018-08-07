/*
 * @Author: yaowei
 * @Date: 2018-05-17 15:53:42
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-17 17:39:30
 */

// 第三方材质型号关联 - 列表
export function getList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/thirdSkuRela/page', params)
}
// 第三方材质型号关联 - 启用/停用
export function handleStatus(vm, id) {
  return vm.$api.put(vm, 'admin/u/p/thirdSkuRela/updateStatus?id=' + id)
}
// 第三方材质型号关联 - 删除（批量）
export function batchDelete(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/thirdSkuRela/batch', params)
}
