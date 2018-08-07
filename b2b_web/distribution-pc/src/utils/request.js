import axios from 'axios'
import { Message } from 'element-ui'
import store from '../store'
import {setToken, getToken } from '@/utils/auth'
import {isNumber} from '@/assets/js/common.js'

// 创建axios实例
const service = axios.create({
    baseURL: process.env.BASE_API,
    timeout: 15000, // 请求超时时间
    headers: {
        'Access-Control-Allow-Origin': '*',
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Platform': 'web',
        'Version': '1.0.0',
        'Authorization': '',
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache',
        'appkey': '',
        'openId': '',
        'language': ''
    },
    withCredentials: false,
    onDownloadProgress: function (ProgressEvent) {
        const load = ProgressEvent.loaded
        const total = ProgressEvent.total
        if (store.getters.loadingProgress !== undefined && store.getters.loadingProgress !== null) {
            if (isNumber(Math.round((load / total) * 100))) {
                store.getters.loadingProgress.percentage = Math.round((load / total) * 100)
                if (store.getters.loadingProgress !== null && !store.getters.loadingProgress.showProgress) {
                    if (store.getters.loadingProgress.locale === 'zh') {
                        store.getters.loadingProgress.title = '报价单正在下载中'
                    } else {
                        store.getters.loadingProgress.title = 'The quotation is being downloaded'
                    }
                    store.getters.loadingProgress.showProgress = true
                }
            }
        }
      }
})

// request拦截器
service.defaults.timeout = 60000 // 超时时间
service.interceptors.request.use(config => {
    // if (store.getters.token) {
        // if (getToken() === undefined) {
        //     config.headers['Authorization'] = ''
        // } else {
        //     config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改-
        // }
        config.headers['Authorization'] ='8651e012f5e64ab3881430d8cf38ca9d-3798';  //展示调试使用
    // }
    let value = window.localStorage.getItem('bLang')
    if (value) {
        config.headers['language'] = value
    }
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
        // setToken(headers.authorization);  //所有接口统一存储token
        if (headers['content-type'] === 'application/octet-stream;charset=utf-8') { // 请求头类型：数据流
            return response.data
        } else {      
        //     if (res.code === 1115 || res.code === 1121 || res.code === 1122) { // 配送费用计算code
        //         return res
        //     } else if (res.code !== 0 && res.code !== 3) {
        //         if (res.msg !== undefined && res.msg !== '') { // ===修改弹出提示
        //             Message({
        //                 message: res.msg,
        //                 type: 'error',
        //                 duration: 2 * 1000
        //             })
        //             return res
        //         } else if (res.msg === undefined) {
        //             return res
        //         } else {

        //         }
        //     } else {
        //         return res
        //     }
        


            // author:liu-----2018.04.22
            if(res.status==200){
                return res
            }else{
                if (res.statusText !== undefined && res.statusText !== '') { // ===修改弹出提示
                    Message({
                        message: res.statusText,
                        type: 'error',
                        duration: 2 * 1000
                    })
                    return res
                } else if (res.statusText === undefined) {
                    return res
                } 
            }
        }


    },
    error => {
        console.log(error) // for debug
   
        Message({
            message: '网络异常，请检查网络', //  error.msg'网络异常，请检查网络'this.$t('P.PaymentSuccessful')
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(error)
    }
)

export default service
