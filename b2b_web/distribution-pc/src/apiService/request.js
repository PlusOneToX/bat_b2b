import axios from 'axios'
import { Message } from 'element-ui'
import store from '../store'
import {setToken, getToken } from '@/utils/auth'
import {isNumber,loginOut,tenantUrlFun} from '@/assets/js/common.js'


let idLogin=0;
// 创建axios实例
const service = axios.create({
    baseURL: process.env.BASE_API,
    timeout: 15000, // 请求超时时间
    headers: {
        'Access-Control-Allow-Origin': '*',
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Platform': 'GF60001',
        'Version': '1.0.0',
        'token': '',
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache',
        'appkey': '',
        'openId': '',
        'language-currency': '',
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
	if (getToken()==='undefined' ||getToken()===undefined ||getToken() ==null) {
		config.headers['token'] = ''
	} else {     
		config.headers['token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改-
	}
    
    let value = window.localStorage.getItem('bLang')
    if (value) {
        config.headers['language-currency'] = value
    }
    let basrUrlHost = localStorage.getItem('tenantHost')
    let basrUrl = localStorage.getItem('tenantUrl');
    if(basrUrlHost){
        config.baseURL = basrUrl;
        config.headers['tenantNo'] = localStorage.getItem('tenantNo');
    }
    return config
}, error => {
    // Do something with request error
    Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
    response => {
        const res = response.data;
        const headers = response.headers
        setToken(headers.token);   //所有接口统一存储token
        if (headers['content-type'] === 'application/octet-stream;charset=utf-8') { // 请求头类型：数据流
            return response.data
        } else {          
            if(res.errCode=='B_PLATFORM_GAIN_URL_NULL'||res.errCode=='B_TENANT_ERROR'||res.errCode=='B_PLATFORM_QRY_URL_ERROR'){
                Message({
                    message: res.errMessage,
                    type: 'error',
                    duration: 2 * 1000
                })
             
                idLogin=idLogin+1;
                if(idLogin==1){
                    tenantUrlFun();
                }
                
                return
            }
            if(res.status==200){
                return res
            }else{
                if (res.errMessage !== undefined && res.errMessage !== '') { // ===修改弹出提示
                    Message({
                        message: res.errMessage,
                        type: 'error',
                        duration: 2 * 1000
                    })
                    return res
                } else if (res.errMessage === undefined) {
                    return res
                } 
            }
        }


    },
    error => {
        setInterval(function(){
            idLogin=0;
        },(24*60*60*1000))
        idLogin=idLogin+1;
        
        // console.log('errr:',error.response) // for debug
        if (error.response.status === 401 ||error.response.status === 403) { // 401token失效 ,重新获取token //403无auth授权，后台不允许访问
            if(idLogin==1){
                loginOut();      
                Message({
                    // message: that.$t('P.PaymentSuccessful'), //  error.msg'网络异常，请检查网络'
                    message:error.response.data.errMessage,
                    type: 'error',
                    duration: 5 * 1000
                })
            }
        }else if(error.response.status === 503||error.response.status === 500){
            Message({ 
                // message:error.response.data.error,
                message:'服务器异常',
                type: 'error',
                duration: 5 * 1000
            })
           
        } 
        
        return Promise.reject(error)
    }
)

export default service
