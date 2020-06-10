/*
 * @Author: yaowei
 * @Date: 2019-05-08 09:24:26
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-08 12:06:42
 */
import request from './request'
import { getToken } from './auth'

function apiAxios (method, url, params) {
  return request({
    method: method,
    url: url,
    data: method === 'POST' || method === 'PUT' || method === 'DELETE' ? params : null,
    params: method === 'GET' ? params : null
  })
}

export default {
  get: function (vue, path, params) {
    return apiAxios('GET', path, params)
  },
  post: function (vue, path, params) {
    return apiAxios('POST', path, params)
  },
  put: function (vue, path, params) {
    return apiAxios('PUT', path, params)
  },
  delete: function (vue, path, params) {
    return apiAxios('DELETE', path, params)
  },
  exportData: function (vue, path, params) {
    return exportData('POST', path, params)
  }
}
export function exportData (method, url, params) {
  return request({
    url: url,
    method: method,
    data: params,
    responseType: 'arraybuffer',
    timeout: 3600000, // 请求超时时间1个小时
    Authorization: getToken()
  })
}
