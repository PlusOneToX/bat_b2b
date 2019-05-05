
/**枚举EPlatform*/
let EPlatform= {
  /**App*/
  AppPlus : 'APP-PLUS',
  /**App nvue*/
  AppPlusNvue :'APP-PLUS-NVUE',
  /**H5*/
  H5 : 'H5',
  /**微信小程序*/
  MpWeixin : 'MP-WEIXIN',
  /**支付宝小程序*/
  MpAlipay : 'MP-ALIPAY',
  /**百度小程序*/
  MpBaidu :'MP-BAIDU',
  /**字节跳动小程序*/
  MpToutiao : 'MP-TOUTIAO',
  /**QQ小程序*/
  MpQq : 'MP-QQ',
  /**360小程序*/
  Mp360 : 'MP-360',
  /**微信小程序/支付宝小程序/百度小程序/字节跳动小程序/QQ小程序/360小程序*/
  Mp : 'MP',
  /**快应用通用(包含联盟、华为)*/
  QuickappWebview : 'quickapp-webview',
  /**快应用联盟*/
  QuickappWebviewUnion : 'quickapp-webview-union',
  /**快应用华为*/
  QuickappWebviewHuawei : 'quickapp-webview-huawei',
}
/**使用条件编译获取平台信息*/
export function ifDefPlatform() {
  let platform='';
  //#ifdef APP-PLUS
  platform = EPlatform.AppPlus;
  //#endif
  //#ifdef APP-PLUS-NVUE
  platform = EPlatform.AppPlusNvue;
  //#endif
  //#ifdef H5
  platform = EPlatform.H5;
  //#endif
  //#ifdef MP-WEIXIN
  platform = EPlatform.MpWeixin;
  //#endif
  //#ifdef MP-ALIPAY
  platform = EPlatform.MpAlipay;
  //#endif
  //#ifdef MP-BAIDU
  platform = EPlatform.MpBaidu;
  //#endif
  //#ifdef MP-TOUTIAO
  platform = EPlatform.MpToutiao;
  //#endif
  //#ifdef MP-QQ
  platform = EPlatform.MpQq;
  //#endif
  //#ifdef MP-360
  platform = EPlatform.Mp360;
  //#endif
  //#ifdef MP
  platform = EPlatform.Mp;
  //#endif
  //#ifdef quickapp-webview
  platform = EPlatform.QuickappWebview;
  //#endif
  //#ifdef quickapp-webview-union
  platform = EPlatform.QuickappWebviewUnion;
  //#endif
  //#ifdef quickapp-webview-huawei
  platform = EPlatform.QuickappWebviewHuawei;
  //#endif
  return platform
}

/**平台类型*/
export const Platform = ifDefPlatform()
/**默认导出平台类型*/
export default Platform

/**App*/
export const isAppPlus = Platform == EPlatform.AppPlus
/**App nvue*/
export const isAppPlusNvue = Platform == EPlatform.AppPlusNvue
/**H5*/
export const isH5 = Platform == EPlatform.H5
/**微信小程序*/
export const isMpWeixin = Platform == EPlatform.MpWeixin
/**支付宝小程序*/
export const isMpAlipay = Platform == EPlatform.MpAlipay
/**百度小程序*/
export const isMpBaidu = Platform == EPlatform.MpBaidu
/**字节跳动小程序*/
export const isMpToutiao = Platform == EPlatform.MpToutiao
/**QQ小程序*/
export const isMpQq = Platform == EPlatform.MpQq
/**360小程序*/
export const isMp360 = Platform == EPlatform.Mp360
/**微信小程序/支付宝小程序/百度小程序/字节跳动小程序/QQ小程序/360小程序*/
export const isMp = Platform == EPlatform.Mp
/**快应用通用(包含联盟、华为)*/
export const isQuickappWebview = Platform == EPlatform.QuickappWebview
/**快应用联盟*/
export const isQuickappWebviewUnion = Platform == EPlatform.QuickappWebviewUnion
/**快应用华为*/
export const isQuickappWebviewHuawei = Platform == EPlatform.QuickappWebviewHuawei
/**是否开发环境*/
export const isDevelopment = process.env.NODE_ENV == 'development'
/**是否线上环境*/
export const isProduction = process.env.NODE_ENV == 'production'
/**抖音小程序*/
export const isMpDouyinApp = uni.getSystemInfoSync().appName == 'Douyin'
/**头条小程序*/
export const isMpToutiaoApp = uni.getSystemInfoSync().appName == 'Toutiao'


export function fomatFloat (num, n) {
    var f = parseFloat(num);
    if (isNaN(f)) {
        return false;
    }
    // 判断第三位是否是五
    let ls=Math.round(4.225 * 1000).toString().split('.');
    let threeNum=ls[0].charAt(ls[0].length-1);
    if(threeNum==5){
        num=(num*1000+1)/1000
    }
    f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
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