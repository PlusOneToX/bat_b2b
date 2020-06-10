const app = getApp()

const request = (url, options) => {
  return new Promise((resolve, reject) => {
    let distributorId = tt.getStorageSync("distributorId"); // 分销商id
    let orderSource = tt.getStorageSync("orderSource"); // 订单来源
    let platform = tt.getStorageSync("platform"); // 平台

    let auth = tt.getStorageSync("auth") || ""; // token
    tt.request({
      url: `${app.globalData.host}${url}`,
      method: options.method,
      data: options.method === 'GET' ? options.data : JSON.stringify(options.data),
      header: {
        'Access-Control-Allow-Origin': '*',
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Version': '1.0.0',
        'Authorization': '0',
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache',
        'appkey': '10A00C1CD1F04F058BD09F55F6A76877',
        'distributorId': distributorId || 2601,
        'platform': platform || 'GF60006',
        'orderSource': orderSource || 'GF60006',
        'token': auth
      },
      success(request) {
        if (request.data.success) {
          resolve(request.data)
        } else {
          // 非购物车/优惠券列表接口，token 失效，跳转授权
          if ((url.indexOf("/shoppingcart/list") < 0 && url.indexOf("/coupon/list") < 0 ) && request.data.errCode === 'B_AUTH_FAIL') {
            tt.navigateTo({
              url: '/pages/login/login',
            })
            tt.removeStorageSync("auth");
          }
          resolve(request.data)
        }

        // 获取返回请求头，缓存 token
        if (request.header && request.header["token"]) {
          let auth = request.header["token"].toString();
          tt.setStorageSync('auth', auth)
        }
      },
      fail(error) {
        reject(error)
      }
    })
  })
}

const get = (url, options = {}) => {
  return request(url, {
    method: 'GET',
    data: options
  })
}

const post = (url, options) => {
  return request(url, {
    method: 'POST',
    data: options
  })
}

const put = (url, options) => {
  return request(url, {
    method: 'PUT',
    data: options
  })
}

// 不能声明DELETE（关键字）
const remove = (url, options) => {
  return request(url, {
    method: 'DELETE',
    data: options
  })
}

module.exports = {
  get,
  post,
  put,
  remove
}