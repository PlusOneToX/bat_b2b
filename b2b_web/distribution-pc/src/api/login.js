import request from '@/utils/request'
export function login (userName, password) {
  return request({
    // url: '/user/login',
    url:'/distributor/v1/web/user/login',
    method: 'post',
    data: {
      userName,
      password
    }
  })
}
export function getInfo (id) {
  return request({
    url: '/user/u/user',
    method: 'get',
    data: {}
  })
}
export function logout () {
  return request({
    url: '/user/u/login',
    method: 'delete'
  })
}
