
import axios from 'axios'
import {
  setToken
} from './auth'
import {
  Toast,
  Dialog
} from 'vant'
// api
import wx from "weixin-js-sdk";

// 设置请求次数/请求间隙
axios.defaults.retry = 3; // 请求次数
axios.defaults.retryDelay = 2000; // 请求间隙

const service = axios.create({
  timeout: 30000, // 请求超时时间
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'Version': '1.0.0',
    'Authorization': '0',
    'Cache-Control': 'no-cache',
    'Pragma': 'no-cache',
    'appkey': '10A00C1CD1F04F058BD09F55F6A76877',
    'openId': '0',
    'orderSource': '',
    'platform': ''
  },
  withCredentials: false
})

// // 重新请求
// function retryRequest(err) {
//   let back = new Promise(function (resolve) {
//     resolve();
//   })
//   return back.then(function () {
//     return service(err.config);
//   })
// }

// request拦截器，两个参数function,第一个在请求发送之前做一些事,第二个当出现请求错误时做一些事
service.interceptors.request.use(config => {
  // 判断请求 url 是否包含域名
  if (config.url.indexOf("http://") < 0 && config.url.indexOf("https://") < 0) {
    config.url = localStorage.getItem("tenantUrl") + config.url
  }

  // 非获取租户地址
  if (config.url.indexOf("platform/v1/web/tenant/url") < 0) {
    // 判断缓存是否有auth
    if (localStorage.getItem('auth') && localStorage.getItem('auth') !== 'undefined') {
      config.headers.token = localStorage.getItem('auth') ? localStorage.getItem('auth') : ''
    }
  }

  // 兑换小程序
  config.headers.platform = localStorage.getItem('platform') ? localStorage.getItem('platform') : '28'
  config.headers.orderSource = localStorage.getItem('orderSource') ? localStorage.getItem('orderSource') : '28'
  config.headers.distributorId = localStorage.getItem('distributorId') ? localStorage.getItem('distributorId') : '2601' // 正式
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
    const res = response.data
    const headers = response.headers

    // 获取返回数据请求头 token
    if (headers['token']) {
      setToken(headers['token'])
    }

    if (headers['content-type'] === 'application/octet-stream;charset=utf-8') { // 请求头类型：数据流
      return response.data
    } else {
      return res
    }
  },
  error => {
    if (error.toString().indexOf('Error: timeout') !== -1) {
      Toast("网络请求超时，请稍后再试");
      // Dialog.confirm({
      //   message: '网络请求超时，请稍后再试',
      //   showCancelButton: false,
      // }).then(() => {
      //   retryRequest(error);
      // });
      return Promise.reject(error);
    }

    if (error.toString().indexOf('Error: Network Error') !== -1) {
      Toast("网络请求错误，请稍后再试");
      return Promise.reject(error);
    }

    if (error.response) {
      const err = error.response.data
      // 非加入购物车，且401
      if (error.response.config.url.indexOf("order/v1/web/user/customer/shoppingcart/diy") < 0 && err.errCode === 'B_AUTH_FAIL') {
        // token 失效，跳转授权
        var enterParams = JSON.stringify({});

        let platform = localStorage.getItem('platform')
        if (platform === 'GF60006') {
          // 字节小程序
          tt.miniProgram.navigateTo({
            url: "/pages/login/login?enterFlag=index&enterParams=" + enterParams,
          });
        } else if (platform === 'GF60007' || platform === 'GF60008') {
          // 支付宝小程序
          my.navigateTo({
            url: "/pages/login/login?enterFlag=index&enterParams=" + enterParams,
          });
        } else {
          wx.miniProgram.navigateTo({
            url: "/pages/login/login?enterFlag=index&enterParams=" + enterParams,
          });
        }
      } else {
        Toast(err.errMessage);
      }
    }
    return Promise.reject(error.response.data);
  }
)

export default service
