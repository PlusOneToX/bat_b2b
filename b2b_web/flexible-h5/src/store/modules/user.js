import {
  login,
  loginCaptcha,
  logout,
  getInfo
} from '@/api/login'
import {
  getToken,
  setToken,
  removeToken
} from '@/utils/auth'
import {
  SET_USERNO,
  SET_PHONE,
  SET_USERID,
  SET_NIKENAME,
  SET_AVATAR,
  SET_STATUS,
  SET_TOKEN
} from '../mutation-type'
import {
  Toast
} from 'vant'

const user = {
  state: {
    userno: '',
    phone: '',
    userId: '',
    nikename: '',
    avatar: '',
    auth: getToken(),
    token: getToken(),
    status: '',
    firstLoadOSS: true, // 标记第一次加载 OSS JS
    firstLoadCanva: true, // 标记第一次加载 Canva JS
    canvaApi: '',
    pdfImg: '', // 切图
    spriteArr: [] // Canvas 图层信息
  },
  mutations: {
    [SET_USERNO]: (state, userno) => {
      state.userno = userno
    },
    [SET_PHONE]: (state, phone) => {
      state.phone = phone
    },
    [SET_USERID]: (state, userId) => {
      state.userId = userId
    },
    [SET_NIKENAME]: (state, nikename) => {
      state.nikename = nikename
    },
    [SET_AVATAR]: (state, avatar) => {
      state.avatar = avatar
    },
    [SET_STATUS]: (state, status) => {
      state.status = status
    },
    [SET_TOKEN]: (state, token) => {
      state.auth = token
      state.token = token
    },
    // 标记第一次加载 OSS JS
    handleNotFirstLoadOSS (state) {
      state.firstLoadOSS = false
    },
    // 标记第一次加载 Canva JS
    handleNotFirstLoadCanva (state) {
      state.firstLoadCanva = false
    },
  },
  getters: {
    userno () {
      return user.state.userno
    },
    phone () {
      return user.state.phone
    },
    userId () {
      return user.state.userId
    },
    nikename () {
      return user.state.nikename
    },
    avatar () {
      return user.state.avatar
    },
    auth () {
      return user.state.auth
    },
    token () {
      return user.state.token
    },
    status () {
      return user.state.status
    },
    timer () {
      return user.state.timer
    }
  },
  actions: {
    // 密码登录
    Login ({
      commit,
      state
    }, userInfo) {
      const phone = userInfo.phone.trim()
      return new Promise((resolve, reject) => {
        login(phone, userInfo.password, userInfo.otherUid, userInfo.openId).then(response => {
          const res = response
          if (res.status === 200) {
            resolve(res)
          } else if (res.status === 500) {
            Toast.fail(res.error)
            commit('SET_STATUS', res.status)
            // reject(res)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 验证码登录
    LoginCaptcha ({
      commit,
      state
    }, userInfo) {
      const phone = userInfo.phone.trim()
      return new Promise((resolve, reject) => {
        loginCaptcha(phone, userInfo.code, userInfo.otherUid, userInfo.openId).then(response => {
          const res = response
          if (res.status === 200) {
            // frequency是否首次登录、confirm是否同意协议
            // if (res.data.confirm === 0 || (res.data.user.frequency && res.data.user.frequency === 1)) {
            //   this.$router.push('/protocol')
            // } else {
            //   setToken(res.data.auth)
            //   localStorage.setItem('userId', res.data.user.id)
            //   localStorage.setItem('userNo', res.data.user.no)
            //   localStorage.setItem('phone', res.data.user.phone)
            //   localStorage.setItem('auth', res.data.auth)
            //   state.userno = res.data.user.no
            //   state.phone = res.data.user.phone
            //   state.nikename = res.data.user.nikeName
            //   state.avatar = res.data.user.headPortrait
            //   state.auth = res.data.auth
            //   commit('SET_TOKEN', res.data.auth)
            //   commit('SET_STATUS', res.status)
            //   resolve(res)
            // }
            resolve(res)
          } else if (res.status === 500) {
            Toast.fail(res.error)
            commit('SET_STATUS', res.status)
            // reject(res)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({
      commit,
      state
    }, userInfo) {
      return new Promise((resolve, reject) => {
        getInfo(userInfo.userId).then(response => {
          const res = response
          if (res.status === 200) {
            commit('SET_STATUS', res.status)
            resolve(res)
          } else if (res.status === 500) {
            commit('SET_STATUS', res.status)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut ({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then((res) => {
          let platform = localStorage.getItem('platform')
          if (platform) {
            localStorage.removeItem('userId')
            localStorage.removeItem('phone')
            localStorage.removeItem('goods')
            localStorage.removeItem('auth')
            localStorage.removeItem('userNo')
            if (platform !== '7') {
              localStorage.removeItem('uid')
              localStorage.removeItem('authenticateUserID')
            }
          } else {
            localStorage.removeItem('userId')
            localStorage.removeItem('phone')
            localStorage.removeItem('goods')
            localStorage.removeItem('uid')
            localStorage.removeItem('authenticateUserID')
            localStorage.removeItem('auth')
            localStorage.removeItem('userNo')
            localStorage.removeItem('sa')
          }
          commit('SET_USERNO', '')
          commit('SET_PHONE', '')
          commit('SET_USERID', '')
          commit('SET_NIKENAME', '')
          commit('SET_AVATAR', '')
          commit('SET_TOKEN', '')
          commit('SET_STATUS', '')
          removeToken()
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
