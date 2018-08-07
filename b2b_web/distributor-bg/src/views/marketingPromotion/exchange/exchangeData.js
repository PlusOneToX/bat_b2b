/*
 * @Author: yaowei
 * @Date: 2018-05-21 09:13:10
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-08 17:27:39
 */

// 新增活动
export function addActivity(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/exchangeCard', params)
}
// 编辑活动
export function editActivity(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/exchangeCard', params)
}
// 活动详情
export function getActivityDetail(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/exchangeCard/detail', params)
}

// 兑换活动列表
export function getExchangeCard(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/exchangeCard/page', params)
}

// 兑换数据列表
export function getExchangeCodeOrder(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/exchangeCodeOrder/page', params)
}

// 导出兑换数据
export function ExportExchangeCodeOrder(vm, params) {
  return vm.$api.exportData(vm, 'admin/u/p/exchangeCodeOrder/export', params)
}

// 兑换活动 - 发布/启用/停用
export function handleExchangeStatus(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/exchangeCard/updateStatus', params)
}

// 码库
export function getCodeList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/exchangeCode/page', params)
}
// 码库 - 作废
export function invalidCode(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/exchangeCode/invalid', params)
}
// 码库 - 批量作废
export function invalidCodeMore(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/exchangeCode/invalidBatch', params)
}

// 链接/二维码
export function qrcodeLink(vm, id) {
  return vm.$api.put(vm, 'admin/u/p/exchangeCard/qrCode?id=' + id)
}
// 生成券码
export function handleCreateCode(vm, params) {
  return vm.$api.putCode(vm, 'admin/u/p/exchangeCode/createCode', params)
}

// 导入模板下载
export function handleTempDownLoad(vm, params) {
  return vm.$api.exportData(vm, 'admin/u/p/exchangeCode/tempDownLoad', params)
}

// 导出券码
export function handleExportCode(vm, params) {
  return vm.$api.exportData(vm, 'admin/u/p/exchangeCode/export', params)
}

// 同步至工厂
export function handleSyncFactory(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/exchangeCard/syncFactory', params)
}

// 查看暗码
export function getEncodeList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/exchangeCode/pageEncode', params)
}
