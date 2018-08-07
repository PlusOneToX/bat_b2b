import { logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken, setId, getId } from '@/utils/auth'
import { SET_CODE, SET_TIMES } from '../mutation-type'
import { Message } from 'element-ui'
// liu
import { login} from '@/apiService/api'
const user = {
    state: {
        token: getToken(),
        userinfo: {},
        logintime: '',
        id: '',
        code: '',
        times: 0,
        loadingProgress: null,
        userName: ''
    },

    mutations: {
        [SET_CODE]: (state, code) => {
            state.code = code
        },
        [SET_TIMES]: (state, times) => {
            state.times = times
        },
        SET_TOKEN: (state, token) => {
            state.token = token
        },
        SET_USERINFO: (state, userinfo) => {
            state.userinfo = userinfo
        },
        SET_LOGINTIME: (state, logintime) => {
            state.logintime = logintime
        },
        SET_PROGRESS: (state, loadingProgress) => {
            state.loadingProgress = loadingProgress
        },
        SET_USERNAME: (state, username) => {
            state.userName = username
        }
    },
    getters: {
        getCode() {
            return user.state.code
        },
        getTimes() {
            return user.state.times
        }
    },
    actions: {
        // 登录
        Login({ commit, state }, userInfo) {
            const username = userInfo.userName.trim()
            return new Promise((resolve, reject) => {
                let params={
                    userName:username,
                    password:userInfo.password
                }
                login(params).then(response => {
                    console.log('登录1',response);
                    const data = response
                    if (data.success) {
                        // setToken(data.token)
                        // setId(getToken(), data.user.id)
                        // window.localStorage.setItem('userId', data.user.id)   //用户id
                        // window.localStorage.setItem('gradeId', data.user.gradeId) //分销商等级编号
                        // window.localStorage.setItem('name', data.user.name)
                        // window.localStorage.setItem('capitalStatus', data.user.capitalStatus)  //'资质状态 0,未提交 1,申请中,2,合作中 3,申请失败',
                        // window.localStorage.setItem('freezeStatus', data.user.freezeStatus)   //冻结状态 1,冻结  2,未冻结
                        // window.localStorage.setItem('canExportPrice', data.user.canExportPrice)
                        // window.localStorage.setItem('userName', data.user.name)
                        // 是否直发客户：1 是，2 否
                        // window.localStorage.setItem('autoDelivery', data.user.autoDelivery)
                        // commit('SET_TOKEN', data.token)
                        // commit('SET_USERNAME', data.user.name)
                        // commit(SET_CODE, data.code)
                        // commit(SET_TIMES, data.user.times)

                        // liu---2018.04.22
                        setId(getToken(), data.data.id)
                        localStorage.setItem('accountType', data.data.accountType)   //登录账号类型
                        localStorage.setItem('userId', data.data.id)   //用户id
                        localStorage.setItem('name', data.data.userName)
                        localStorage.setItem('capitalStatus', data.data.applyStatus)  //'资质状态 0,未提交 1,申请中,2,合作中 3,申请失败',
                        localStorage.setItem('freezeStatus', data.data.freezeStatus)   //冻结状态 2,冻结  1,未冻结
                        localStorage.setItem('canExportPrice', data.data.canExportPrice)
                        localStorage.setItem('userName', data.data.userName) 
                        localStorage.setItem('autoDelivery', data.data.autoDelivery)// 是否直发客户：1 是，0 否
                        localStorage.setItem('companyName',data.data.companyName) //分销商公司名称
                        localStorage.setItem('onWayFlag',data.data.onWayFlag) //是否支持在途库存 1是 0否 默认是1
                        localStorage.setItem('promotionScope',data.data.promotionScope) //参与活动 0-不参与活动 1-全部活动 2-指定活动类型
                        localStorage.setItem('distributionFlag',data.data.distributionFlag) //是否分销
                        localStorage.setItem('erpNo',data.data.erpNo) //售后编码
                        localStorage.setItem('subAccountFlag',data.data.subAccountFlag) //是否开启分账功能
                        
                        commit('SET_USERNAME', data.data.name)
                        resolve()
                    } else {  
                        Message({
                            message: data.errMessage, 
                            type: 'error',
                            duration: 5 * 1000
                        })
                        reject(data)
                    }
                }).catch(error => {
                    
                    reject(error)
                })
            })
        },

        // 获取用户信息
        GetInfo({ commit, state }) {
            return new Promise((resolve, reject) => {
                getInfo(getId(getToken())).then(response => {
                    const data = response
                    commit('SET_USERINFO', data.data)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },

        // 登出
        LogOut({ commit, state }) {
            return new Promise((resolve, reject) => {
                logout(state.token).then(() => {
                    commit('SET_TOKEN', '')
                    commit('SET_USERINFO', {})
                    removeToken()
                    resolve()
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
        },
        
    }
}

export default user
