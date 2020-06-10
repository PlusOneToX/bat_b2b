// 云函数入口文件
const cloud = require('wx-server-sdk')

cloud.init()

// 云函数入口函数
exports.main = async (event, context) => {
  const wxContext = cloud.getWXContext()
  switch (event.action) {
    case 'getUrlScheme': {
      return getUrlScheme(event.data)
    }
  }

  return 'action not found'
}

async function getUrlScheme(data) {
  return cloud.openapi.urlscheme.generate({
    jumpWxa: {
      path: data.path, // <!-- replace -->
      query: 'id=' + data.id,
    },
    // 如果想不过期则置为 false，并可以存到数据库
    isExpire: true,
    // 一分钟有效期
    expireTime: parseInt(Date.now() / 1000 + 60),
  })
}