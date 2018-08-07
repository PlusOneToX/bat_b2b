
export function editPrecharge(vm, params) { // 管理员(财务预存款模块) - 预存款设置更新
  return vm.$api.put(vm, 'admin/u/p/deposit', params)
}

export function getBankCounts(vm, params) { // 管理员(银行账号模块) - 银行账号列表查询
  return vm.$api.get(vm, 'admin/u/p/bankAccount/list', params)
}

export function getSettings(vm, params) { // 预存款设置
  return vm.$api.get(vm, 'admin/u/p/deposit', params)
}

export function getSettingsCount(vm, params) { // 管理员(银行账号模块) - 银行账号总数
  return vm.$api.get(vm, 'admin/u/p/bankAccount/count', params)
}

export function deleteCount(vm, params) { // 管理员(银行账号模块) - 银行账号删除
  return vm.$api.delete(vm, 'admin/u/p/bankAccount', params)
}

export function addAccount(vm, params) {
  return vm.$api.post(vm, 'admin/u/p/bankAccount', params)
}

export function updateAccount(vm, params) { // 管理员(银行账号模块) - 银行账号更新
  return vm.$api.put(vm, 'admin/u/p/bankAccount', params)
}

export function prechargeList(vm, params) { // 管理员(银行账号模块) - 银行账号列表查询
  return vm.$api.get(vm, 'admin/u/p/recharge/list', params)
}

export function prechargeListCount(vm, params) { // 预存款条数
  return vm.$api.get(vm, 'admin/u/p/recharge/count', params)
}

export function confirmPrecharge(vm, params) { // 预存款确认
  return vm.$api.put(vm, 'admin/u/p/recharge/confirm', params)
}

export function getPrechargeDetail(vm, params) { // 预存款充值详情
  return vm.$api.get(vm, 'admin/u/p/recharge', params)
}

export function getChargeLogs(vm, params) { // 管理员(充值模块) - 操作日志
  return vm.$api.get(vm, 'admin/u/p/recharge/logs', params)
}

export function getFrozenList(vm, params) { // 管理员(冻结模块) - 冻结列表
  return vm.$api.get(vm, 'admin/u/p/freezing/list', params)
}

export function getFrozenListCount(vm, params) { // 管理员(冻结模块) - 冻结列表总
  return vm.$api.get(vm, 'admin/u/p/freezing/count', params)
}

export function getDistributorsToFroze(vm, params) { // 冻结 * n
  return vm.$api.get(vm, 'admin/u/p/freezing/distributor/list', params)
}

export function getDistributorsToFrozeCount(vm, params) { // 管理员(冻结模块) - 可冻结分销商列表总数
  return vm.$api.get(vm, 'admin/u/p/freezing/distributor/count', params)
}

export function getPrechargesByIds(vm, params) { // 管理员(财务预存款模块) - 预存款余额详情
  return vm.$api.get(vm, 'admin/u/p/deposit/available/ids', params)
}

export function getDistributorsIds(vm, params) { // 管理员(分销商通用模块) - 通用分销商列表
  return vm.$api.get(vm, 'admin/u/po/distributor/common/list', params)
}

export function freezeCash(vm, params) { // 管理员(冻结模块) - 添加冻结
  return vm.$api.post(vm, 'admin/u/p/freezing', params)
}

export function getWithdrawList(vm, params) { // 管理员(提现模块) - 提现列表
  return vm.$api.get(vm, 'admin/u/p/withdraw/list', params)
}

export function getWithdrawListCount(vm, params) { // 管理员(提现模块) - 提现列表总数
  return vm.$api.get(vm, 'admin/u/p/withdraw/count', params)
}

export function getWithdrawDetail(vm, params) { // 管理员(提现模块) - 提现详情
  return vm.$api.get(vm, 'admin/u/p/withdraw', params)
}

export function getWithdrawlogs(vm, params) { // 管理员(提现模块) - 操作日志
  return vm.$api.get(vm, 'admin/u/p/withdraw/logs', params)
}

export function confirmWithdraw(vm, params) { // 管理员(提现模块) - 提现确认
  return vm.$api.put(vm, 'admin/u/p/withdraw/confirm', params)
}

export function getPrechargeDetails(vm, params) { // 管理员(财务预存款模块) - 预存款明细列表
  return vm.$api.get(vm, 'admin/u/p/deposit/detail/list', params)
}

export function getPrechargeDetailsCount(vm, params) { // 管理员(财务预存款模块) - 预存款明细列表总数
  return vm.$api.get(vm, 'admin/u/p/deposit/detail/count', params)
}

export function getBalanceList(vm, params) { // 管理员(财务预存款模块) - 预存款余额列表
  return vm.$api.get(vm, 'admin/u/p/deposit/available/list', params)
}

export function getBalanceListCount(vm, params) { // 管理员(财务预存款模块) - 预存款余额列表总数
  return vm.$api.get(vm, 'admin/u/p/deposit/available/count', params)
}

export function getInvoices(vm, params) { // 发票列表 params: {payState Allowed content page}
  return vm.$api.get(vm, 'admin/u/p/invoice/list', params)
}

export function getInvoicesCount(vm, params) { // 管理员(发票管理) - 发票列表总数
  return vm.$api.get(vm, 'admin/u/p/invoice/count', params)
}

export function addFreezings(vm, params) { // 添加冻结用户的金额
  return vm.$api.post(vm, 'admin/u/p/freezings', params)
}

export function disFreeze(vm, params) { // 解冻
  return vm.$api.put(vm, 'admin/u/p/freezing/thaw', params)
}

export function createInvoice(vm, params) { // 管理员(发票管理) - 发票开票
  return vm.$api.post(vm, 'admin/u/p/invoice', params)
}

export function cancelInvoice(vm, params) { // 管理员(发票管理) - 发票取消
  return vm.$api.put(vm, 'admin/u/p/invoice/cancel', params)
}

export function receiveMoney(vm, params) { // 管理员(发票管理) - 发票收款
  return vm.$api.put(vm, 'admin/u/p/invoice/receivemoney', params)
}

export function getInvoiceDistributor(vm, params) { // 管理员(发票管理) - 分销商列表, 可开票的分销商
  return vm.$api.get(vm, 'admin/u/p/invoice/distributor', params)
}

export function getbillsToInvoice(vm, params) { // 可开票的对账单(单个详情)
  return vm.$api.get(vm, 'admin/u/p/invoice/bills', params)
}

export function getDistrDetail(vm, params) { // 管理员(合作中分销商模块) - 合作中分销商详情
  return vm.$api.get(vm, 'admin/u/p/distributor/cooperating', params)
}

export function getDistributions(vm, params) { // 管理员(配送模块) - 配送方式列表
  return vm.$api.get(vm, 'admin/u/p/distribution/list', params)
}

export function getInvoDetail(vm, params) { // 管理员(发票管理) - 发票详情
  return vm.$api.get(vm, 'admin/u/p/invoice', params)
}
