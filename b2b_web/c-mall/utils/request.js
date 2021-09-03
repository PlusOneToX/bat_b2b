
const request = (url, options = {}) => {
  const baseUrl = "https://test.bat.com/";
  return new Promise((resolve,reject) => {

    let token = wx.getStorageSync("token");

    const requestTask = wx.request({
      url: baseUrl+url,
      data: options.data,
      method: options.method,
      header: {
        'Access-Control-Allow-Origin': '*',
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Version': '1.0.0',
        'Authorization': '0',
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache',
        'appkey': '10A00C1CD1F04F058BD09F55F6A76877',
        'distributorId': 2601,
        'platform': 28,
        'orderSource': 28,
        'token': token,
        'tenantNo': 100
      },
      success: (result) => {
        resolve(result)
      },
      fail: (error) => {
        reject(error)
      }
    });

    // 返回请求头
    requestTask.onHeadersReceived((res) => {
      // 缓存 token
      if (res.header && res.header["token"]) {
        let token = res.header["token"].toString();
        wx.setStorageSync('token', token)
      }
    });

  })
}

const post = (url, options = {}) => {
  return request(url, {
    method: 'POST',
    data: options
  })
}

const get = (url, options = {}) => {
  return request(url, {
    method: 'GET',
    data: options
  })
}

const put = (url, options = {}) => {
  return request(url, {
    method: 'PUT',
    data: options
  })
}

// 不能声明DELETE（关键字）
const remove = (url, options = {}) => {
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