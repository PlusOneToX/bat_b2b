// return '20180330'
import { MessageBox, Message } from 'element-ui'
import store from '../../store'
import router from '../../router'
import api from '@/api/api'
import { regionList,tenantUrl} from '@/apiService/api'
import { userShopSetting} from '@/apiService/api'
import axios from 'axios'

export function formatDate (date, fmt) {
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
    }
    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + ''
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
        }
    }
    return fmt
}

function padLeftZero (str) {
    return ('00' + str).substr(str.length)
}
export function dateToNum (date) {
    return date.toISOString().substring(0, 10).replace(/-/g, '')
}
export function tenantUrlFun () {
    let host = window.location.host.split(":")[0];
	if (process.env.NODE_ENV == "development") {
	    host = "www.bat.com";
	}
    let tenantNo = getUrlCode()?getUrlCode().tenantNo:'';
    let params={
      gainUrlType: 6,
      host:host,
      qryUrlType: 2,
    }
    if(tenantNo&&tenantNo!=''){
       params={...params,...{tenantNo:getUrlCode().tenantNo}} 
    }
	
    let hostUrl = "https://api.bat.com/";  //正式服
	//let hostUrl = "https://test.bat.com/";  //测试服
    if(process.env.NODE_ENV=='development'){
      hostUrl='https://api.bat.com/';
    }
	
    axios.get(hostUrl+'platform/v1/web/tenant/url',{
      params:params
    }).then(res=>{
         console.log('后台配置接口地址：',res);
        if(res.data.success){
          let data=res.data.data;
          localStorage.setItem('tenantId',data.tenantId);
          localStorage.setItem('tenantNo',data.tenantNo);
          localStorage.setItem('tenantHost',data.host);
          localStorage.setItem('tenantUrl',data.url);
        }else{
          localStorage.removeItem('tenantId');
          localStorage.removeItem('tenantNo');
          localStorage.removeItem('tenantHost');
        }
    })
    return
}
//获取地址栏方法
export function getUrlCode() {
    let url = location.href;
     if(url.split('?')[1]){
         url=url.split('?')[1];
     }else{
       return
     }
     
    /* eslint-disable */
    let theRequest = new Object();
   
    // if (url.indexOf("?") !== -1) {
      let str = url;
      let strs = str.split("&");
      for (let i = 0; i < strs.length; i++) {
        theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
      }
      return theRequest;
    // }
  }
export function loginOut () {
    return MessageBox.confirm('登录已失效，请重新登录', '确定登出', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        showCancelButton: false, // 取消按钮隐藏
        closeOnClickModal: false, // 禁止点击遮罩层关闭提示
        type: 'warning'
    }).then(() => {
		window.localStorage.removeItem('name')
		window.localStorage.removeItem('userId')
		window.localStorage.removeItem('gradeId')
		window.localStorage.removeItem('capitalStatus')
		window.localStorage.removeItem('freezeStatus')
		window.sessionStorage.removeItem('hasShowPromotion')
		
		router.push({ name: 'Login', query: { login_state: 1 } }) // 被登出
		localStorage.setItem('isLoginNum',1)
		return;
    }).catch(() => {})
}
export function loginOutEn () {
    return MessageBox.confirm('Login expired. Please log in again.', 'Confirm logout', {
        confirmButtonText: 'Log in again',
        cancelButtonText: 'Cancel',
        showCancelButton: false, // 取消按钮隐藏
        closeOnClickModal: false, // 禁止点击遮罩层关闭提示
        type: 'warning'
    }).then(() => {
        // store.dispatch('LogOut').then(() => {
            window.localStorage.removeItem('name')
            window.localStorage.removeItem('userId')
            window.localStorage.removeItem('gradeId')
            window.localStorage.removeItem('capitalStatus')
            window.localStorage.removeItem('freezeStatus')
            window.sessionStorage.removeItem('hasShowPromotion')
            router.push({ name: 'Login', query: { login_state: 1 } }) // 被登出
        // })
    }).catch(() => {})
}

// num为传入的值，n为保留的小数位
export function fomatFloat (num, n) {
  var f = parseFloat(num);
  let isthreeFour=false;
  if(num&&num.toString().split('.').length>=2&&(num.toString().split('.')[1].length>=3)){
   if([...num.toString().split('.')[1]][2]==4){
     isthreeFour=true;
   }
  }
  if (isNaN(f)) {
      return false;
  }
  // 判断第三位是否是五
  let ls=Math.round(4.225 * 1000).toString().split('.');
  let threeNum=ls[0].charAt(ls[0].length-1);
  if(threeNum==5){
      num=(num*1000+1)/1000
  }
  // f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
  f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
  if(isthreeFour){
    f =(f * 100 - 0.01 * 100) / 100;
  }
  
  
  var s = f.toString();
  var rs = s.indexOf('.');
 

  // 判定如果是整数，增加小数点再补0
  if (rs < 0) {
      rs = s.length;
      s += '.';
  }
  while (s.length <= rs + n) {
      s += '0';
  }
  return s;
}

// num为传入的值，n为保留的小数位
export function fomatFloor (num, n) {
  var f = parseFloat(num);
  if (isNaN(f)) {
      return false;
  }
  f = Math.floor(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
  var s = f.toString();
  // var rs = s.indexOf('.');
  // 判定如果是整数，增加小数点再补0
  // if (rs < 0) {
  //     rs = s.length;
  //     s += '.';
  // }
  // while (s.length <= rs + n) {
  //     s += '0';
  // }
  return s;
}

export function fixNum (num, length) {
    return ('' + num).length < length ? ((new Array(length + 1)).join('0') + num).slice(-length) : '' + num;
}
export function debounce (func, delay) {
  let timer

  return function (...args) {
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      func.apply(this, args)
    }, delay)
  }
}
export function isNumber (val) {
    var regPos = /^\d+(\.\d+)?$/; // 非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; // 负浮点数
    if (regPos.test(val) || regNeg.test(val)) {
        return true;
    } else {
        return false;
    }
}

// 订单结算提示
export function orderHint () {
    return new Promise((resolve, reject) => {
      userShopSetting().then(res => {
        let msg = ''
        if (res.success) {
          if (( res.data.stiffUseHint === 1) || (res.data.noStiffUseHint === 1)) {
            //autoDelivery 直发分销商 或 非直发分销商
            msg = res.data.hint
            MessageBox.confirm(msg, '提示', {
              confirmButtonText: '确定提交',
              cancelButtonText: '我再想想',
              customClass: 'orderHint'
            }).then(() => {
              resolve(res.data)
            }).catch(() => {
              resolve(null)
            })
          } else {
            resolve(res.data)
          }
        } else {
            Message.error(res.errMessage)
            reject(res.errMessage)
        }
      })
    })
  }

// 获取区域接口
export function region (id) {
    return new Promise((resolve)=> {
       regionList({parentId:id,page:1,size:3000}).then(res => {
            if (res.success) {
                resolve (res)
            } 
        }).catch(err => {
        
            
        })
    })
   
}

