/*
 * @Author: 李杰敏
 * @Date:   2018-06-06 14:59:20
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-05-Th 10:37:56
 */
import url from '../../api/allUrl'

//  系统，权限管理，审批配置，添加的用户列表
export function adminAddData(vm, params) {
  // return vm.$api.get(vm, 'admin/u/p/admin/list', params)
  return vm.$api.get(vm, url.getUserPoList, params)
}
// 系统，权限管理，审批配置，添加的用户列表总数
export function adminCountData(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/admin/count', params)
}
// 管理员(配送模块) - 删除配送方式
export function deleteDistribution(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/distribution', params)
}
// 管理员(结算方式模块) - 结算方式删除
export function deleteSettleAccount(vm, params) {
  return vm.$api.delete(vm, 'admin/u/p/settleAccount', params)
}
// 管理员(结算方式模块) - 结算方式更新
export function editSettleAccount(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/settleAccount', params)
}
// 管理员(结算方式模块) - 结算方式添加
export function addSettleAccount(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/settleAccount', params)
}
// 管理员(结算方式模块) - 结算方式停用
export function enableSettleAccount(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/settleAccount/enable', params)
}
// (区域模块) - 查找区域列表
export function getRegion(vm, params) {
  return vm.$api.get(vm, 'region', params)
}
// 管理员(配送模块) - 配送地区列表
export function getDistributionAreaList(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/distribution/distributionAreaList', params)
}
// (区域模块) - 查找区域列表
export function getRegionList(vm, params) {
  return vm.$api.get(vm, 'regionList', params)
}
// 管理员(配送模块) - 配送方式添加
export function addDistribution(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/distribution', params)
}
// 管理员(配送模块) - 配送方式编辑
export function editDistribution(vm, params) {
  return vm.$api.put(vm, 'admin/u/p/distribution', params)
}
// 查找所有区域列表
export function getAllRegionList(vm, params) {
  return vm.$api.get(vm, 'allRegionList', params)
}
// 管理员(审批模块) - 审批配置修改
export function checkApprove(vm, params) {
  // return vm.$api.put(vm, 'admin/u/p/check', params)
  return vm.$api.put(vm, url.check, params)
}
// 管理员(用户模块) - 用户查询-根据ids
export function getUserInfos(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/admin/ids', params)
}
// 管理员(审批模块) - 审批配置详情
export function getCheckDetail(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/checkDetail', params)
}
// 管理员(用户模块) - 用户查询-根据ids
export function getUserIfos(vm, params) {
  return vm.$api.get(vm, 'admin/u/po/admin/ids', params)
}
// 收款条件详情查询
export function getSettleAccount(vm, params) {
  return vm.$api.get(vm, 'admin/u/p/settleAccount/id', params)
}
