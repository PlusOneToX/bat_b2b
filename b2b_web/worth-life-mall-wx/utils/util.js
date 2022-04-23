/**
 * 封封微信的的request
 */

// var API_BASE_URL = 'https://valuelife.vip/platform-api/app/';
var API_BASE_URL = 'http://192.168.1.167:8889/platform-api/app/';
// var API_BASE_URL = 'http://192.168.1.82:8889/platform-api/app/';


function request(url, data = {}, method = "GET", contentType = "application/json") {
  wx.showLoading({
    title: '请稍候...',
    mask: true
  })
  return new Promise(function (resolve, reject) {
    wx.request({
      url: API_BASE_URL + url,
      data: data,
      method: method,
      header: {
        'Content-Type': contentType,
        'token': wx.getStorageSync('token')
      },
      success: function (res) {
        if (res.statusCode === 200) {
          if (res.data.code === 401) {
            //返回码401说明token过期或者用户未登录
            wx.removeStorage({
              key: 'token',
              success() {
                //个人中心页不跳转
                if (wx.getStorageSync("navUrl") != "/pages/ucenter/index/index") {
                  wx.showModal({
                    title: '温馨提示',
                    content: '您还没有登录，是否去登录',
                    success(res) {
                      if (res.confirm) {
                        wx.redirectTo({
                          url: '/pages/auth/btnAuth/btnAuth',
                        })
                      } else if (res.cancel) {
                        wx.navigateBack({
                          delta: 1,
                          fail: (res) => {
                            wx.switchTab({
                              url: '/pages/index/index',
                            })
                          }
                        })
                      }
                    }
                  })
                }
              }
            })
          } else if (res.data.code === 500) {
            showMsg(res.data.msg)
          } else if (res.data.code === 404) {
            showMsg(res.data.msg)
          } else {
            resolve(res.data);
          }
        } else {
          reject(res.data.msg);
        }
      },
      fail: function (err) {
        showMsg('网络不给力，请稍后再试~')
        reject(err)
      },
      complete: function (res) {
        if (res.statusCode === 200) {
          if (res.data.code === 0 || res.data.code === 401) {
            wx.hideLoading()
          } else {
            showMsg(res.data.msg)
          }
        } else {
          showMsg('服务器开小差了~')
        }
      }
    })
  });
}

function showMsg(msg, mask = true, duration = 2000, icon = 'none') {
  wx.showToast({
    title: msg,
    icon: icon,
    mask: mask,
    duration: duration
  })
}

function formatTime(date) {
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}

/**
 * 判断字符串是否为空
 */
function isEmpty(str) {
  if (str === '' || str === undefined || str === null) {
    return true;
  } else {
    return false;
  }
}

//todo 订单列表页面使用有bug
function countdown(weixin, that, name, index) {

  if (index == null) {
    var EndTime = that.expireTime || [];
  } else {
    var EndTime = that[index].expireTime || [];
  }

  var NowTime = new Date().getTime();
  //IOS系统直接使用new Date('2018-10-29 11:25:21')，在IOS上获取不到对应的时间对象。
  var total_micro_second = Date.parse(EndTime.replace(/-/g, '/')) - NowTime || [];
  // 渲染倒计时时钟
  var para = {};
  if (index == null) {
    that.dateformat = dateformat(total_micro_second)
    para[name] = that
  } else {
    let str = `${name}[${index}].dateformat`;
    para[str] = dateformat(total_micro_second)
  }
  weixin.setData(para)

  if (total_micro_second <= 0) {
    if (index == null) {
      that.dateformat = {
        day: 0,
        hr: 0,
        min: 0,
        sec: 0,
        second: 0
      }
      para[name] = that
    } else {
      let str = `${name}[${index}].dateformat`;
      para[str] = {
        day: 0,
        hr: 0,
        min: 0,
        sec: 0,
        second: 0
      }
    }

    weixin.setData(para)
    return;
  }
  setTimeout(function () {
    total_micro_second -= 1000;
    countdown(weixin, that, name, index);
  }, 1000)
}

// 时间格式化输出，如11:03 25:19 每1s都会调用一次
function dateformat(micro_second) {
  // 总秒数
  var second = Math.floor(micro_second / 1000);
  // 天数
  var day = Math.floor(second / 3600 / 24);
  // 小时
  var hr = Math.floor(second / 3600 % 24);
  // 分钟
  var min = Math.floor(second / 60 % 60);
  // 秒
  var sec = Math.floor(second % 60);
  return {
    day,
    hr: hr < 10 ? '0' + hr : hr,
    min: min < 10 ? '0' + min : min,
    sec: sec < 10 ? '0' + sec : sec,
    second: second
  }
}

function getLocation(callback) {
  wx.getLocation({
    type: 'gcj02', //返回可以用于wx.openLocation的经纬度
    success: function (res) {
      var latitude = res.latitude;
      var longitude = res.longitude;
      callback && callback(longitude, latitude)
    }
  })
}

function chooseLocation(callback) {
  wx.chooseLocation({
    success: function (res) {
      callback && callback(res)
    }
  })
}

function getNowTime() {
  var now = new Date();
  var year = now.getFullYear();
  var month = now.getMonth() + 1;
  var day = now.getDate();
  if (month < 10) {
    month = '0' + month;
  };
  if (day < 10) {
    day = '0' + day;
  };
  //  如果需要时分秒，就放开
  // var h = now.getHours();
  // var m = now.getMinutes();
  // var s = now.getSeconds();
  var formatDate = year + '-' + month + '-' + day;
  return formatDate;
}

function getDateStr(today, addDayCount) {
  var dd;
  if (today) {
    dd = new Date(today);
  } else {
    dd = new Date();
  }
  dd.setDate(dd.getDate() + addDayCount); //获取AddDayCount天后的日期
  var y = dd.getFullYear();
  var m = dd.getMonth() + 1; //获取当前月份的日期
  var d = dd.getDate();
  if (m < 10) {
    m = '0' + m;
  };
  if (d < 10) {
    d = '0' + d;
  };
  return y + "-" + m + "-" + d;
}

const timeArray = ['9:00-09:30', '09:30-10:00', '10:30-11:00', '11:00-11:30', '11:30-12:00', '12:00-12:30', '12:30-13:00', '13:00-13:30', '13:30-14:00', '14:00-14:30', '14:30-15:00', '15:00-15:30', '15:30-16:00', '16:00-16:30', '16:30-17:00', '17:00-17:30', '17:30-18:00', '18:00-18:30', '18:30-19:00', '19:00-19:30', '19:30-20:00', '20:00-20:30', '20:30-21:00'];

function getNowTimeArray() {
  var now = new Date();
  var hh = now.getHours(); //获取当前小时数(0-23)
  var mm = now.getMinutes(); //获取当前分钟数(0-59)
  var index = hh * 2 + (mm >= 30 ? 1 : 0);
  var result = timeArray.slice(0); // 深copy
  if (index > 17) {
    result = result.splice(index - 17 - 1)
  }
  var startArray = new Array();
  startArray.push('尽快送达');
  return startArray.concat(result);
}

function uuid() {
  var s = [];
  var hexDigits = "0123456789abcdef";
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = "-";

  var uuid = s.join("");
  return uuid;
}

Array.prototype.contains = function (obj) {
  var i = this.length;
  while (i--) {
    if (this[i] === obj) {
      return true;
    }
  }
  return false;
}

function isiPhoneX(callback) {
  var isIphoneX = false;
  wx.getSystemInfo({
    success: function (res) {
      if (res.model.indexOf("iPhone X") != -1) {
        isIphoneX = true;
        callback(isIphoneX);
      } else {
        isIphoneX = false;
        callback(isIphoneX);
      }
    }
  })
}

/**
 * @param date
 */
function transDate(date, fmt) {
  if (!date) {
    return '--'
  }
  let _this = new Date(date * 1000)
  var o = {
    'M+': _this.getMonth() + 1,
    'd+': _this.getDate(),
    'h+': _this.getHours(),
    'm+': _this.getMinutes(),
    's+': _this.getSeconds(),
    'q+': Math.floor((_this.getMonth() + 3) / 3),
    'S': _this.getMilliseconds()
  }
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (_this.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt
}

function isNumber(val) {

  var regPos = /^\d+(\.\d+)?$/; //非负浮点数
  var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
  if (regPos.test(val) || regNeg.test(val)) {
    return true;
  } else {
    return false;
  }
}

function getDistance(lat1, lng1, lat2, lng2) {
  if (!lat1 || !lng1) {
    return '';
  }
  // lat1用户的纬度
  // lng1用户的经度
  // lat2商家的纬度
  // lng2商家的经度
  let radLat1 = Rad(lat1);
  let radLat2 = Rad(lat2);
  let a = radLat1 - radLat2;
  let b = Rad(lng1) - Rad(lng2);
  let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
  s = s * 6378.137;
  s = Math.round(s * 10000) / 10000;
  s = '(距您' + s.toFixed(2) + '公里)' //保留两位小数
  return s
}

function Rad(d) {
  return d * Math.PI / 180.0;
}
function isMobile (mobile) {
  if (!mobile) {
    showMsg('请输入手机号码');
    return false
  }
  if (!mobile.match(/^1[3-9][0-9]\d{8}$/)) {
    showMsg('手机号不正确');
    return false
  }
  return true
}

function isMoney(money) {
  let re = new RegExp(/((^[1-9]\d*)|^0)(\.\d{0,2}){0,1}$/);
  if (re.test(money)) {
    return true;
  } else {
    return false;
  }
}/**
 * 当面付
 */
function faceToface(money, shopsId) {
  return new Promise(function(resolve, reject) {
    request('pay/faceToface', {
      fromType: 1,
      shopsId: shopsId,
      money: money,
      tradeType: 'JSAPI'
    }, 'POST').then((res) => {
      if (res.code === 0) {
        let payParam = res.data;
        wx.requestPayment({
          'timeStamp': payParam.timeStamp,
          'nonceStr': payParam.nonceStr,
          'package': payParam.packageValue,
          'signType': payParam.signType,
          'paySign': payParam.paySign,
          'success': function(res) {
            console.log(res)
            resolve(res);
          },
          'fail': function(res) {
            console.log(res)
            reject(res);
          },
          'complete': function(res) {
            console.log(res)
            reject(res);
          }
        });
      } else {
        reject(res);
      }
    });
  });
}
module.exports = {
  formatTime,
  isEmpty,
  request,
  showMsg,
  countdown,
  getLocation,
  chooseLocation,
  getNowTime,
  getDateStr,
  getNowTimeArray,
  timeArray,
  isiPhoneX,
  uuid,
  dateformat,
  isNumber,
  transDate,
  getDistance,
  isMobile,
  isMoney,
  faceToface,
  API_BASE_URL: API_BASE_URL
}
