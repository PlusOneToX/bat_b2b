import request from '@/utils/request'

const qs = require('qs')

function apiAxios(method, url, params) {
  return request({
    method: method,
    url: url,
    data: method === 'POST' || method === 'PUT' || method === 'DELETE' ? params : null,
    params: method === 'GET' ? params : null,
    paramsSerializer: method === 'GET' ? function(params) {
      return qs.stringify(params, { arrayFormat: 'repeat' })
    } : null
    // responseType: 'arraybuffer'
    // headers: headers,
    // withCredentials: false
  })
}
// function request (vue, method, url, params, success, failure) {
//   // var headers = {
//   //   'Accept': 'application/json',
//   //   'Content-Type': 'application/json',
//   //   'Platform': 'web',
//   //   'Version': '1.0.0',
//   //   'Authorization': ''
//   // }
//   return http.request(method, url, params, result => {
//     success(result)
//     // if (result.code === 0) {
//     //   if (success) {
//     //     success(result)
//     //   }
//     // } else if (result.code === 3) {
//     //   vue.$router.replace({ name: 'Login' })
//     // } else {
//     //   if (failure) {
//     //     failure(result)
//     //   } else {
//     //     vue.$message({
//     //       showClose: true,
//     //       message: result.msg,
//     //       type: 'error'
//     //     })
//     //   }
//     // }
//   }, failure => {
//     // if (failure) {
//     //   failure({code: -1, msg: failure1})
//     // } else {
//     //   vue.$message({
//     //     showClose: true,
//     //     message: failure1,
//     //     type: 'error'
//     //   })
//     // }
//   })
// }

export default {
  get: function(vue, path, params) {
    return apiAxios('GET', path, params)
  },
  post: function(vue, path, params) {
    return apiAxios('POST', path, params)
  },
  put: function(vue, path, params) {
    return apiAxios('PUT', path, params)
  },
  delete: function(vue, path, params) {
    return apiAxios('DELETE', path, params)
  },
  exportData: function(vue, path, params) {
    return exportData('POST', path, params)
  },
  getData: function(vue, path, params) {
    return getData('GET', path, params)
  },
  putCode: function(vue, path, params) {
    return putCode('PUT', path, params)
  }
}

export function exportData(method, url, params) {
  return request({
    url: url,
    method: method,
    data: params,
    responseType: 'arraybuffer',
    timeout: 3600000 // 请求超时时间1个小时
  })
}

export function getData(method, url, params) {
  return request({
    url: url,
    method: method,
    data: params,
    responseType: 'arraybuffer',
    timeout: 3600000 // 请求超时时间1个小时
  })
}

export function putCode(method, url, params) {
  return request({
    url: url,
    method: method,
    data: params,
    timeout: 3600000 // 请求超时时间1个小时
  })
}
