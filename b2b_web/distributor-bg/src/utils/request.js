import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import { Uint82Str,getUrlCode,isNumber } from '@/utils/common'
import store from '../store'
import { getToken, setToken } from '@/utils/auth'
// Vue.prototype.$alert = MessageBox.alert

// 创建axios实例
// 类似于局部传参
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  timeout: 60000, // 请求超时时间
  headers: {
    'Accept': 'application/json',
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'Platform': 'web',
    'Version': '1.0.0',
    'token': '',
    'Cache-Control': 'no-cache',
    'Pragma': 'no-cache'
  },
  withCredentials: false
})

// service.interceptors.general
// request拦截器，两个参数function,第一个在请求发送之前做一些事,第二个当出现请求错误时做一些事
service.interceptors.request.use(config => {
    if (store.getters.token) {
	  config.headers['token'] = getToken()
    }
    
    let basrUrlHost = '';
    let tenantNo = '';
    basrUrlHost = localStorage.getItem('tenantHost');
    tenantNo = localStorage.getItem('tenantNo');
    let tenantUrl = localStorage.getItem('tenantUrl');
    let basrUrlV = tenantUrl;
    if(basrUrlHost){
        config.baseURL = basrUrlV;
        config.headers['tenantNo'] = tenantNo;
    }
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  // 对返回的数据进行一些处理
  response => {
    /**
      * code为非0是抛错 可结合自己业务进行修改
      */
    const res = response.data
    const headers = response.headers
    const token = headers['token']
    if (token !== undefined) {
      setToken(token)
    }
    if (headers['content-type'] === 'application/octet-stream;charset=utf-8' || headers['content-type'] === 'application/vnd.ms-excel;charset=utf-8') {
      return response.data
    } else {
      if (res.success !== undefined) {
        if (res.success) {
          return res
        } else {
          if(res.errCode == 'B_TENANT_ERROR'){
            localStorage.removeItem('tenantId');
            localStorage.removeItem('tenantNo');
            localStorage.removeItem('tenantHost');
            Message({
              message: res.errMessage,
              type: 'error',
              duration: 3 * 1000
            })
            this.$route.push('/login');
            return res
          }
          if (res.errCode === 'B_AUTH_FAIL') {
            // 用户过期
            this.$route.push('/login')
          } else if (res.errMessage !== '' && res.errMessage !== undefined) {
            Message({
              message: res.errMessage,
              type: 'error',
              duration: 3 * 1000
            })
            return res
          } else if (res.errMessage === undefined) {
            return res // ===修改弹出提示
          }
        }
      } else if (res.code === 3) {
        MessageBox.confirm('登录已失效，请重新登录', '确定登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          showClose: false,
          showCancelButton: false, // 取消按钮隐藏
          closeOnClickModal: false, // 禁止点击遮罩层关闭提示
          type: 'warning'
        }).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        })
        return Promise.reject('error')
      } else if (res.code === undefined && res.success === undefined) {
        // eslint-disable-next-line prefer-const
        let strs = JSON.parse(Uint82Str(res))
        if (strs.success !== undefined && !strs.success) {
          Message({
            message: strs.msg,
            type: 'error',
            duration: 3 * 1000
          })
          return null
        }
      }
    }
  },
  // 对返回的错误进行一些处理
  error => {
    var offlineTemps = localStorage.getItem('offlineTemp') // 是否断网连接请求的抛错提示
    // eslint-disable-next-line eqeqeq
    if (offlineTemps == 'false') { // 断网状态
      console.log('err' + error) // for debug
      Message({
        message: '您的电脑当前无网络，请检查您的网络后重试！',
        type: 'error',
        duration: 3 * 1000
      })
      return Promise.reject(error)
    } else { // 联网状态
      // 用户过期
      if (error.toString().indexOf('401') > -1) {
        MessageBox.confirm('登录已失效，请重新登录', '确定登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          showClose: false,
          showCancelButton: false, // 取消按钮隐藏
          closeOnClickModal: false, // 禁止点击遮罩层关闭提示
          type: 'warning'
        }).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        })
      } else {
        Message({
          message: error.message,
          type: 'error',
          duration: 3 * 1000
        })
      }
      return Promise.reject(error)
    }
  }
)

export default service
