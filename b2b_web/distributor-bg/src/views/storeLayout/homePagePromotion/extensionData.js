/*
 * @Author: Jason
 * @Date:   2018-03-28 09:39:05
 * @Last Modified by:   Jason
 * @Last Modified time: 2018-05-10
 */

// 管理员(商业配置-首页推广) - 首页推广列表
export function getExtensionList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/store/extension/list', params)
}
// 管理员(商业配置-首页推广) - 首页推广添加
export function saveExtension(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/store/extension', params)
}
// 管理员(商业配置-首页推广) - 首页推广编辑
export function editExtension(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/store/extension', params)
}
