/*
 * @Author: yaowei
 * @Date: 2019-10-19 17:23:44
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-11 12:03:43
 */
import request from '@/utils/request'
import api from '@/api/allApi'

/** 租户 */
// 添加租户
export function addTenant(data) {
  return request({
    url: api.addTenant,
    method: 'post',
    data
  })
}

// 租户详情
export function tenantDetail(data) {
  return request({
    url: api.tenantDetail,
    method: 'get',
    params: data
  })
}

// 删除租户
export function deleteTenant(data) {
  return request({
    url: api.deleteTenant,
    method: 'delete',
    data
  })
}

// 租户列表
export function tenantList(data) {
  return request({
    url: api.tenantList,
    method: 'get',
    params: data
  })
}

// 更新租户
export function updateTenant(data) {
  return request({
    url: api.updateTenant,
    method: 'put',
    data
  })
}

// 汇总
export function tenantSummary(data) {
  return request({
    url: api.tenantSummary,
    method: 'get',
    params: data
  })
}
/** 租户 */


/** 租户配置 - 基础信息配置 - 分销小程序配置 */
// 添加
export function addCommonSetting(data) {
  return request({
    url: api.addCommonSetting,
    method: 'post',
    data
  })
}

// 详情
export function commonSettingDetail(data) {
  return request({
    url: api.commonSettingDetail,
    method: 'get',
    params: data
  })
}

// 更新
export function updateCommonSetting(data) {
  return request({
    url: api.updateCommonSetting,
    method: 'put',
    data
  })
}
/** 租户配置 - 基础信息配置 - 分销小程序配置 */


/** 租户配置 - 基础信息配置 - URL配置 */
// 添加
export function addUrlSetting(data) {
  return request({
    url: api.addUrlSetting,
    method: 'post',
    data
  })
}

// 删除
export function deleteUrlSetting(data) {
  return request({
    url: api.deleteUrlSetting,
    method: 'delete',
    data
  })
}

// 列表
export function urlSettingList(data) {
  return request({
    url: api.urlSettingList,
    method: 'get',
    params: data
  })
}

// 更新
export function updateUrlSetting(data) {
  return request({
    url: api.updateUrlSetting,
    method: 'put',
    data
  })
}
/** 租户配置 - 基础信息配置 - URL配置 */


/** 租户配置 - 定制信息配置 */
// 添加
export function addDiySetting(data) {
  return request({
    url: api.addDiySetting,
    method: 'post',
    data
  })
}

// 删除
export function deleteDiySetting(data) {
  return request({
    url: api.deleteDiySetting,
    method: 'delete',
    data
  })
}

// 列表
export function diySettingList(data) {
  return request({
    url: api.diySettingList,
    method: 'get',
    params: data
  })
}

// 更新
export function updateDiySetting(data) {
  return request({
    url: api.updateDiySetting,
    method: 'put',
    data
  })
}
/** 租户配置 - 定制信息配置 */


/** 租户配置 - ERP信息配置 */
// 添加
export function addErpSetting(data) {
  return request({
    url: api.addErpSetting,
    method: 'post',
    data
  })
}

// 详情
export function erpSettingDetail(data) {
  return request({
    url: api.erpSettingDetail,
    method: 'get',
    params: data
  })
}

// 更新
export function updateErpSetting(data) {
  return request({
    url: api.updateErpSetting,
    method: 'put',
    data
  })
}
/** 租户配置 - ERP信息配置 */


/** 租户配置 - 文件存储配置 */
// 添加
export function addOssSetting(data) {
  return request({
    url: api.addOssSetting,
    method: 'post',
    data
  })
}

// 详情
export function ossSettingDetail(data) {
  return request({
    url: api.ossSettingDetail,
    method: 'get',
    params: data
  })
}

// 更新
export function updateOssSetting(data) {
  return request({
    url: api.updateOssSetting,
    method: 'put',
    data
  })
}
/** 租户配置 - 文件存储配置 */


/** 租户配置 - 短信配置 */
// 添加
export function addSmsSetting(data) {
  return request({
    url: api.addSmsSetting,
    method: 'post',
    data
  })
}

// 详情
export function smsSettingDetail(data) {
  return request({
    url: api.smsSettingDetail,
    method: 'get',
    params: data
  })
}

// 更新
export function updateSmsSetting(data) {
  return request({
    url: api.updateSmsSetting,
    method: 'put',
    data
  })
}
/** 租户配置 - 短信配置 */


/** 租户配置 - 数据库配置 */
// 添加
export function addDBSetting(data) {
  return request({
    url: api.dbSetting,
    method: 'post',
    data
  })
}

// 编辑
export function editDBSetting(data) {
  return request({
    url: api.dbSetting,
    method: 'post',
    data
  })
}

// 详情
export function dbSettingDetail(data) {
  return request({
    url: api.dbSetting,
    method: 'get',
    params: data
  })
}

// 删除
export function deleteDBSetting(data) {
  return request({
    url: api.dbSetting,
    method: 'delete',
    data
  })
}

// 创建数据库
export function dbTableSetting(data) {
  return request({
    url: api.dbTableSetting,
    method: 'post',
    data
  })
}
/** 租户配置 - 数据库配置 */
