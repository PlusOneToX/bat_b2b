/*
 * @Author: yaowei
 * @Date: 2018-03-24 09:06:57
 * @LastEditors: yaowei
 * @LastEditTime: 2018-03-24 09:08:14
 */

// 商品销量列表
export function getList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/goods/sale/list', params)
}
// 销量编辑
export function editSales(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/goods/sale/edit', params)
}
