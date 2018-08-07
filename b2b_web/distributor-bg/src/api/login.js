import request from '@/utils/request'
import url from '@/api/allUrl'
export function login(userName, password) {
  return request({
    url: url.login,
    method: 'post',
    data: {
      userName,
      password
    }
  })
}

// 获取userInfo
export function getInfo(id) {
  return request({
    url: url.user + '?id=' + id,
    method: 'get',
    data: { }
  })
}

export function logout() {
  return request({
    // url: api.logout,
    url: url.logout,
    method: 'post'
  })
}
