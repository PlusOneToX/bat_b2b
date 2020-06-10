exports.main = async (context) => {
  try {
    const _data = context.data
    console.log("request 云函数接收请求data：", _data)
    if (_data.method == "POST") {
      return { code: "-1", msg: "POST 暂时不支持" }
    }
    const result = await context.cloud.httpApi.invoke(
      {
        'domain': _data.baseUrl,
        "path": _data.url,
        'params': _data.data,
        'method': _data.method ? _data.method : "GET",
        'headers': {
          'Content-Type': 'application/json;charset=UTF-8',
          'distributorId': _data.distributorId,
          'platform': _data.platform,
          'Authorization': '0',
          'Cache-Control': 'no-cache',
          'Pragma': 'no-cache',
          'appkey': '10A00C1CD1F04F058BD09F55F6A76877',
          'openId': '0',
          'orderSource': _data.orderSource,
          'tenantNo': _data.tenantNo,
        }
      })
    return result
  } catch (error) {
    console.log('fail:', error);
    return {
      "message": "云函数请求异常",
      "error": "云函数请求异常",
      "status": -1,
      "data": null,
      "timestamp": null
    }
  }