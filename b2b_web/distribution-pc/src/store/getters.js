const getters = {
  token: state => state.user.token,
  userinfo: state => state.user.userinfo,
  code: state => state.user.code,
  loadingProgress: state => state.user.loadingProgress
}
export default getters
