import { login, logout, getInfo } from '@/api/login'
import { getToken, removeToken, setId, getId } from '@/utils/auth'
import api from '@/api/api'
import url from '@/api/allUrl'
import store from '@/store'
import hasPermis from '@/store/assets/hasPermis'

const defaultPermissons = {
  warehouse: {
    actions: [],
    menus: []
  },
  goods: {
    actions: [],
    menus: []
  },
  distributor: {
    actions: [],
    menus: []
  },
  system: {
    actions: [],
    menus: []
  },
  other: {
    actions: [],
    menus: []
  }
}

const user = {
  state: {
    token: getToken(),
    userinfo: {},
    logintime: '',
    id: '',
    permissions: defaultPermissons, // 废弃,这个数据格式不好用，建议使用下面的hasPermis ,system里有一个假的权限数据，记得删除(不删也没有大碍)
    hasPermis // 每个权限对应的true||false hasPermis[模块名][actions || menus][权限名]: boolean
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USERINFO: (state, userinfo) => {
      state.userinfo = userinfo
    },
    SET_LOGINTIME: (state, logintime) => {
      state.logintime = logintime
    },
    SET_PERMISSIONS: (state, payLoad) => {
      state.permissions = payLoad
    },
    SET_HASPERMIS: (state, payLoad) => {
      state.hasPermis = payLoad
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.userName.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          const data = response
          setId(data.data.id)
          commit('SET_TOKEN', getToken())
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息 > 获取用户权限
    GetInfo({ commit, state }) {
      const Promi = new Promise((resolve, reject) => {
        getInfo(getId()).then(response => {
          const data = response
          commit('SET_USERINFO', data.data)
          resolve(response.data)
        }).catch(error => {
          reject(error)
        })
      })
      // 用userInfo中的角色ids获取用户权限
      Promi.then(res => {
        const ids = res.roleIds
        return res.adminType !== 1 && res.roleIds.length > 0 &&
          api.get(null, url.roleIds, { ids: ids }).then(res => {
            /** {actions: {}[], menus: {}[]} [] */
            if (res.success === false) {
              return
            }
            const usersPermissions = { actions: [], menus: [] }
            const finalPermissions = {}
            usersPermissions.actions = res.data.permissions
            usersPermissions.menus = res.data.menus
            // 2.格式化为 string[]
            usersPermissions.actions = usersPermissions.actions.map(action => action.permissionModule)
            usersPermissions.menus = usersPermissions.menus.map(menu => menu.menuEn)

            // 3. 去重
            usersPermissions.actions = Array.from(new Set(usersPermissions.actions))
            usersPermissions.menus = Array.from(new Set(usersPermissions.menus))
            // 6. 转化为布尔值
            const boolPermis = JSON.parse(JSON.stringify(hasPermis))
            for (const action of usersPermissions.actions) {
              if (action) boolPermis.actions[action] = true
            }
            for (const menu of usersPermissions.menus) {
              if (menu) boolPermis.menus[menu] = true
            }
            commit('SET_HASPERMIS', boolPermis)

            // 4. 格式化为 {module:string}[]
            usersPermissions.actions = usersPermissions.actions.map(action => ({ module: action }))
            usersPermissions.menus = usersPermissions.menus.map(menu => ({ module: menu }))
            // 5. 根据模块划分
            let { actions, menus } = usersPermissions
            /** 可以再优化,把filter里的函数独立出来 */
            finalPermissions.warehouse = {
              actions: usersPermissions.actions.filter((action, index, array) => {
                if (!action) { // 检测该项，如果是undefined,直接返回false
                  return action
                }
                // if (action.permissionModule.match(/^warehouse/)) {
                if (action.module.match(/^warehouse/)) {
                  array[index] = undefined
                } // 若匹配则筛除该项
                // return action.permissionModule.match(/^warehouse/)
                return action.module.match(/^warehouse/)
              }),
              menus: usersPermissions.menus.filter((menu, index, array) => {
                if (!menu) { // 检测该项，如果是undefined,直接返回false
                  return menu
                }
                // if (menu.menuModule.match(/^warehouse/)) {
                if (menu.module.match(/^warehouse/)) {
                  array[index] = undefined
                } // 若匹配则筛除该项
                // return menu.menuModule.match(/^warehouse/)
                return menu.module.match(/^warehouse/)
              })
            }
            finalPermissions.goods = {
              actions: usersPermissions.actions.filter((action, index, array) => {
                if (!action) { // 检测该项，如果是undefined,直接返回false
                  return action
                }
                // if (action.permissionModule.match(/^goods/)) {
                if (action.module.match(/^goods/)) {
                  // array[index] = undefined
                } // 若匹配则筛除该项
                return action.module.match(/^goods/)
                // return action.permissionModule.match(/^goods/)
              }),
              menus: usersPermissions.menus.filter((menu, index, array) => {
                if (!menu) { // 检测该项，如果是undefined,直接返回false
                  return menu
                }
                if (menu.module.match(/^goods/)) {
                  // array[index] = undefined
                } // 若匹配则筛除该项
                // return menu.module.match(/^goods/)
                return menu.module
              })
            }
            finalPermissions.distributor = {
              actions: usersPermissions.actions.filter((action, index, array) => {
                if (!action) { // 检测该项，如果是undefined,直接返回false
                  return action
                }
                if (action.module.match(/^distributor/)) {
                  array[index] = undefined
                } // 若匹配则筛除该项
                return action.module.match(/^distributor/)
              }),
              menus: usersPermissions.menus.filter((menu, index, array) => {
                if (!menu) { // 检测该项，如果是undefined,直接返回false
                  return menu
                }
                if (menu.module.match(/^distributor/)) {
                  array[index] = undefined
                } // 若匹配则筛除该项
                return menu.module.match(/^distributor/)
              })
            }
            finalPermissions.system = {
              actions: usersPermissions.actions.filter((action, index, array) => {
                if (!action) { // 检测该项，如果是undefined,直接返回false
                  return action
                }
                if (action.module.match(/^system/)) {
                  array[index] = undefined
                } // 若匹配则筛除该项
                return action.module.match(/^system/)
              }),
              menus: usersPermissions.menus.filter((menu, index, array) => {
                if (!menu) { // 检测该项，如果是undefined,直接返回false
                  return menu
                }
                if (menu.module.match(/^system/)) {
                  array[index] = undefined
                } // 若匹配则筛除该项
                return menu.module.match(/^system/)
              })
            }
            // 筛除other中的undefined
            actions = actions.filter(obj => obj)
            menus = menus.filter(obj => obj)
            finalPermissions.other = { actions, menus }
            // 赋值
            commit('SET_PERMISSIONS', finalPermissions)
            store.dispatch('fiterSideBarMap', finalPermissions)
            // store.dispatch('getSideBarItems', boolPermis) // 过滤菜单权限
          })
      })
      return Promi
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_USERINFO', {})
          removeToken()
          resolve()
          localStorage.removeItem('tenantId');
          localStorage.removeItem('tenantNo');
          localStorage.removeItem('tenantHost');
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
