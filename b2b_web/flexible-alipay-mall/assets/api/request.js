const app = getApp()

const request = (url, options) => {
  return new Promise((resolve, reject) => {
    let distributorId = my.getStorageSync({
      key: "distributorId"
    }).data; // 分销商id
    let orderSource = my.getStorageSync({
      key: "orderSource"
    }).data; // 订单来源
    let platform = my.getStorageSync({
      key: "platform"
    }).data; // 平台

    let auth = my.getStorageSync({
      key: "auth"
    }).data || ""; // token
    let tenantNo = my.getStorageSync({
      key: "tenantNo"
    }).data || ""; // token
    
    let token = "";
    if (url.indexOf("flexible/v1/web/user/") < 0) {
      token = auth
    }

    my.request({
      url: `${app.globalData.host}${url}`,
      method: options.method,
      data: options.method === 'GET' ? options.data : JSON.stringify(options.data),
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Version': '1.0.0',
        'Authorization': '0',
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache',
        'appkey': '10A00C1CD1F04F058BD09F55F6A76877',
        'distributorId': distributorId,
        'platform': platform,
        'orderSource': orderSource,
        'token': token,
        'tenantNo': tenantNo || 100,
      },
      success(request) {
        if (request.data.success) {
          resolve(request.data)
        }

        // 获取返回请求头，缓存 token
        if (request.headers && request.headers["token"]) {
          let auth = request.headers["token"].toString();
          my.setStorageSync({
            key: "auth",
            data: auth
          })
        }
      },
      fail(error) {
        console.log(error)
        // 非购物车/优惠券列表接口，token 失效，跳转授权
        if ((url && url.indexOf("/shoppingcart/list") < 0 && url.indexOf("/coupon/list") < 0) && error.data.errCode === 'B_AUTH_FAIL') {
          my.navigateTo({
            url: '/pages/login/login',
          })
          my.removeStorageSync("auth");
        }
        resolve(error.data)
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

// 不能声明DELETE（关键字）(data 参数传不过去)
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