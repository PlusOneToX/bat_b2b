if (process.env.NODE_ENV === "production") {
   baseURL = 'https://api.bat.com/' // 正式
} else if (process.env.NODE_ENV === "staging") {
   baseURL = 'https://test.bat.com/' // 测试
} else {
   baseURL = 'https://test.bat.com/' // 本地
}

module.exports = {
  // 系统设置 - 管理员（平台用户接口）
  login: baseURL + 'platform/v1/web/user/login', // 用户登录
  addUser: baseURL + 'platform/v1/web/user/add', // 添加用户
  userDetail: baseURL + 'platform/v1/web/user/detail', // 用户详情
  deleteUser: baseURL + 'platform/v1/web/user/delete', // 删除用户
  userList: baseURL + 'platform/v1/web/user/list', // 查询平台用户列表
  updateUser: baseURL + 'platform/v1/web/user/update', // 更新用户

  // 租户管理 - 租户（平台租户接口）
  tenantList: baseURL + 'platform/v1/web/tenant/list', // 租户列表
  addTenant: baseURL + 'platform/v1/web/tenant/add', // 租户添加
  tenantDetail: baseURL + 'platform/v1/web/tenant/detail', // 租户详情
  deleteTenant: baseURL + 'platform/v1/web/tenant/delete', // 租户删除
  updateTenant: baseURL + 'platform/v1/web/tenant/update', // 租户更新
  tenantSummary: baseURL + 'platform/v1/web/tenant/summary', // 汇总

  // 租户配置 - 基础信息配置 - 分销小程序配置（平台租户公共配置接口）
  addCommonSetting: baseURL + 'platform/v1/web/tenant/common/add', // 添加配置
  commonSettingDetail: baseURL + 'platform/v1/web/tenant/common/config', // 获取配置
  updateCommonSetting: baseURL + 'platform/v1/web/tenant/common/update', // 更新配置

  // 租户配置 - 基础信息配置 - URL配置（平台租户url配置接口）
  addUrlSetting: baseURL + 'platform/v1/web/tenant/url/add', // 添加配置
  deleteUrlSetting: baseURL + 'platform/v1/web/tenant/url/delete', // 删除配置
  urlSettingList: baseURL + 'platform/v1/web/tenant/url/list', // 配置列表
  updateUrlSetting: baseURL + 'platform/v1/web/tenant/url/update', // 更新配置

  // 租户配置 - 定制信息配置（平台租户定制工厂配置接口）
  addDiySetting: baseURL + 'platform/v1/web/tenant/diyFactory/add', // 添加配置
  deleteDiySetting: baseURL + 'platform/v1/web/tenant/diyFactory/delete', // 删除配置
  diySettingList: baseURL + 'platform/v1/web/tenant/diyFactory/list', // 获取列表
  updateDiySetting: baseURL + 'platform/v1/web/tenant/diyFactory/update', // 更新配置

  // 租户配置 - ERP信息配置（平台租户ERP配置接口）
  addErpSetting: baseURL + 'platform/v1/web/tenant/erp/add', // 添加配置
  erpSettingDetail: baseURL + 'platform/v1/web/tenant/erp/config', // 获取配置
  updateErpSetting: baseURL + 'platform/v1/web/tenant/erp/update', // 更新配置

  // 租户配置 - 文件存储配置（平台租户文件存储配置接口）
  addOssSetting: baseURL + 'platform/v1/web/tenant/oss/add', // 添加配置
  ossSettingDetail: baseURL + 'platform/v1/web/tenant/oss/config', // 获取配置
  updateOssSetting: baseURL + 'platform/v1/web/tenant/oss/update', // 更新配置

  // 租户配置 - 短信配置（平台租户短信配置接口）
  addSmsSetting: baseURL + 'platform/v1/web/tenant/sms/add', // 添加配置
  smsSettingDetail: baseURL + 'platform/v1/web/tenant/sms/config', // 获取配置
  updateSmsSetting: baseURL + 'platform/v1/web/tenant/sms/update', // 更新配置

  // 租户配置 - 数据库配置（平台租户数据库接口）
  dbSetting: baseURL + 'platform/v1/web/tenant/db', // 新增/编辑/查询列表/删除
  dbTableSetting: baseURL + 'platform/v1/web/tenant/db/table', // 自动生成平台租户服务数据表
}
