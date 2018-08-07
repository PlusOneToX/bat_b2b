/*
 * @Author: yaowei
 * @Date: 2018-05-21 09:02:53
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-30 11:47:36
 */

// 新增拼团
export function addGroupBuy(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/spellGroup', params)
}

// 查看拼团详情
export function getGroupBuyDetail(vm, id) {
  return vm.$api.get(vm, 'admin/u/p/spellGroup/detail/' + id)
}

// 编辑拼团
export function editGroupBuy(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/spellGroup', params)
}

// 拼团列表
export function getGroupBuyList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/spellGroup/list', params)
}

// 拼团 - 上移
export function handleUpGroup(vm, id, params) {
  return vm.$api.put(vm, 'admin/u/p/spellGroup/up/' + id, params)
}
// 拼团 - 下移
export function handleDownGroup(vm, id, params) {
  return vm.$api.put(vm, 'admin/u/p/spellGroup/down/' + id, params)
}
// 拼团 - 发布/启用/停用
export function handleGroupStatus(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/spellGroup/updateStatus', params)
}
// 拼团 - 删除
export function handleDeleteGroup(vm, id, params) {
  return vm.$api.delete(vm, 'admin/u/p/spellGroup/' + id, params)
}
// 拼团 - 复制
export function handleCopyGroup(vm, id, params) {
  return vm.$api.post(vm, 'admin/u/p/spellGroup/copy/' + id, params)
}
// 拼团 - 置顶
export function handleTop(vm, id, params) {
  return vm.$api.put(vm, 'admin/u/p/spellGroup/top/' + id, params)
}
