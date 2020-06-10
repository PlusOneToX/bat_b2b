let app = "";

const request = (url, options) => {
  return new Promise((resolve, reject) => {
    let distributorId = wx.getStorageSync("distributorId"); // 分销商id
    let orderSource = wx.getStorageSync("orderSource"); // 订单来源
    let platform = wx.getStorageSync("platform"); // 平台

    let auth = wx.getStorageSync("auth"); // token
    let tenantNo = wx.getStorageSync("tenantNo"); // tenantNo

    // 判断请求 url 是否包含域名
    let rUrl = url;
    if ((url.indexOf("http://") < 0 && url.indexOf("https://") < 0)) {
      rUrl = wx.getStorageSync("tenantUrl") + `/${url}`
    }

    const requestTask = wx.request({
      url: rUrl,
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
        'platform': platform || 28,
        'orderSource': orderSource || 28,
        'token': auth,
        'tenantNo': tenantNo
      },
      success(request) {
        if (request.data.success) {
          resolve(request.data)
        } else {
          if (request.data.status !== 404) {
            // 非领取接口/转赠/转发详情/苏宁权益码，token 失效，跳转授权
            if ((url.indexOf("/receive") < 0 && url.indexOf("exchangeShare/releaseDetail") < 0 && url.indexOf("exchangeTransfer/detail") < 0 && url.indexOf("/exchangeQuanyi/exchange") < 0) && request.data.errCode === 'B_AUTH_FAIL') {
              wx.navigateTo({
                url: '/pages/login/login',
              })
              wx.removeStorageSync("auth");
            }
            resolve(request.data)
          }
        }
      },
      fail(error) {
        reject(error)
      }
    })

    // 返回请求头
    requestTask.onHeadersReceived((res) => {
      // 缓存 token
      if (res.header && res.header["token"]) {
        let auth = res.header["token"].toString();
        wx.setStorageSync('auth', auth)
      }
    });
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