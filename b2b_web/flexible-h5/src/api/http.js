// 引用axios
// var axios = require('axios')
import request from '@/utils/request'
function apiAxios (method, url, params, success, failure) {
  request({
    method: method,
    url: url,
    data: method === 'POST' || method === 'PUT' || method === 'DELETE' ? params : null,
    params: method === 'GET' ? params : null,
    // headers: headers,
    withCredentials: false
  }).then(function (res) {
    if (success) {
      success(res.data)
    } else {
      if (failure) {
        failure(res.data)
      }
    }
  }).catch(function (err) {
    if (failure) {
      failure(err.message)
    } else {
      window.alert('api error: ' + err.message)
    }
  })
}

export default {
  request: function (method, url, params, success, failure) {
    return apiAxios(method, url, params, success, failure)
  }
}
