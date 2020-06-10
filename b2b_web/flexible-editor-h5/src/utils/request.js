/*
 * @Author: yaowei
 * @Date: 2019-11-22 11:21:56
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-23 00:05:08
 */
import axios from 'axios'
const service = axios.create({
  timeout: 60000, // 请求超时时间
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Version': '1.0.0',
    'Authorization': '0',
    'Cache-Control': 'no-cache',
    'Pragma': 'no-cache',
    'appkey': '10A00C1CD1F04F058BD09F55F6A76877',
    'openId': '0'
  },
  withCredentials: false
})

// request拦截器，两个参数function,第一个在请求发送之前做一些事,第二个当出现请求错误时做一些事
service.interceptors.request.use(config => {
  // 判断请求 url 是否包含域名
  if (config.url.indexOf("http://") < 0 && config.url.indexOf("https://") < 0) {
    config.url = localStorage.getItem("tenantUrl") + config.url
  }

  config.headers.distributorId = localStorage.getItem('distributorId') ? localStorage.getItem('distributorId') : '752'
  config.headers.orderSource = localStorage.getItem('orderSource') ? localStorage.getItem('orderSource') : '5'
  config.headers.Platform = localStorage.getItem('Platform') ? localStorage.getItem('Platform') : '5'
  // 租户编码
  config.headers.tenantNo = localStorage.getItem('tenantNo') ? localStorage.getItem('tenantNo') : ''

  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非0是抛错 可结合自己业务进行修改
     */
    const res = response.data
    const headers = response.headers
    if (headers['content-type'] === 'application/octet-stream;charset=utf-8') { // 请求头类型：数据流
      return response.data
    } else {
      if (res.code === 1115 || res.code === 1121 || res.code === 1122) { // 配送费用计算code
        return res
      } else if (res.code !== 0 && res.code !== 3) {
        if (res.msg !== '') { // ===修改弹出提示
          // Message({
          //   message: res.msg,
          //   type: 'error',
          //   duration: 5 * 1000
          // })
          return res
        } else if (res.msg === undefined) {
          return res
        }
      } else {
        return res
      }
    }
  },
  error => {
    console.log(error) // for debug
    // Message({
    //   message: '网络异常，请检查网络', //  error.msg'网络异常，请检查网络'
    //   type: 'error',
    //   duration: 5 * 1000
    // })
    return Promise.reject(error)
  }
)

export default service
