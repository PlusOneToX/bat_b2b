/*
 * @Author: 戴伟明
 * @Date:   2018-04-31 09:42:07
 * @Last Modified by: li.tian
 * @Last Modified time: 2018-07-Th 05:58:55
 */
/*
该文件放置各种方法
 */

/**
 * 高阶函数,相当于一个修饰器
 * 在执行callback前请用户确定，防止误操作
 * 柯里化，分步传参，第一步传参this给vm，之后就不必再输入this
 * @param  {vue instance}   vm       vue实例，传入this
 * @param  {string}   operation   操作名称
 * @param  {Function} callback 如果确认，将会执行的函数
 * operation应能解释callback的行为
 */
export const confirmCreator = vm => (operation, callback) => {
  return vm.$confirm(`确认要${operation}吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  }).then(_ => {
    return callback()
  }).then(res => {
    if (res.code === 0) {
      vm.$message({
        type: 'success',
        message: `操作成功`
      })
      return res
    }
  }).catch(e => {
    // throw e;
    vm.$message({
      type: 'info',
      message: '您已取消操作'
    })
  })
}

// ======== 收款列表部分 =========

// 收款列表充值方式: 1支付宝，2微信，3区间结算 4线下转款 5余额支付 6快钱支付
export function rechargeTypeFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '支付宝'
    case 2:
      return '微信'
    case 3:
      return '区间结算'
    case 4:
      return '线下转款'
    case 5:
      return '余额支付'
    case 6:
      return '快钱支付'
    default:
      return '-'
  }
}

// 收款列表确认状态 1,待确认2确认3未通过
export function ConfirmStatusFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '待确认'
    case 2:
      return '已确认'
    case 3:
      return '已取消'
    default:
      return stateCode
  }
}

// ======== 充值详情部分 =========

// 充值详情页面，充值状态
export function rechargeFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '待确认'
    case 2:
      return '已到账'
    case 3:
      return '已关闭'
    default:
      return stateCode
  }
}

// 充值详情，充值方式
export function topUpWay(stateCode) {
  switch (stateCode) {
    case 1:
      return '支付宝'
    case 2:
      return '微信'
    case 3:
      return '线下'
    case 4:
      return '销售订单退款'
    default:
      return stateCode
  }
}

// 提现方式 1 银行汇款 2 网上支付
export function withdrawTypeFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '支付宝'
    case 2:
      return '微信'
    case 3:
      return '银行卡'
    default:
      return stateCode
  }
}

// 日志操作状态1.提现创建 2.提现确认支付 3.提现拒绝
export function operationTypeLogs(stateCode) {
  switch (stateCode) {
    case 1:
      return '提现创建'
    case 2:
      return '提现确认支付 '
    case 3:
      return '提现拒绝'
    default:
      return stateCode
  }
}

// 提现确认方式 1 待确认 2 确认通过, 3 不通过
export function confirmTypeFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '申请中'
    case 2:
      return '已确认'
    case 3:
      return '已拒绝'
    default:
      return stateCode
  }
}

// 提现类型 0全部, 1,银行汇款 2,网上支付
export function payType(stateCode) {
  switch (stateCode) {
    case 1:
      return '银行汇款'
    case 2:
      return '网上支付'
    default:
      return stateCode
  }
}

// 性别
export function sexFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '男'
    case 2:
      return '女'
    default:
      return stateCode
  }
}

// perateType = 1（1支付宝2微信3线下汇款），operateType = 2（1银行汇款2网上支付），operateType = 3（1支付宝2微信3区间结算4公司转账5余额支付），operateType = 4（1在线退款2退回余额3线下退款）
export function paytypeFormatter(operateType, stateCode) {
  if (operateType === 1) {
    switch (stateCode) {
      case 1:
        return '支付宝支付'
      case 2:
        return '微信支付'
      case 3:
        return '线下汇款'
      default:
        return '-'
    }
  } else if (operateType === 3) {
    switch (stateCode) {
      case 1:
        return '银行汇款'
      case 2:
        return '网上支付'
      default:
        return '-'
    }
  } else if (operateType === 2) {
    switch (stateCode) {
      case 1:
        return '支付宝支付'
      case 2:
        return '微信支付'
      case 3:
        return '区间结算'
      case 4:
        return '线下转账'
      case 5:
        return '余额支付'
      default:
        return '-'
    }
  } else if (operateType === 4) {
    switch (stateCode) {
      case 1:
        return '在线退款'
      case 2:
        return '退回余额'
      case 3:
        return '线下退款'
      default:
        return '-'
    }
  } else if (operateType === 5) {
    return '-'
  }
}

// 业务类型 0全部,1充值 2 提现 3 订单消费 4 订单退款 5人工调整 6 ERP增量变化 7 ERP全量变化 8 分销佣金
export function operationMold(stateCode) {
  switch (stateCode) {
    case 0:
      return '全部'
    case 1:
      return '充值'
    case 2:
      return '提现'
    case 3:
      return '订单消费'
    case 4:
      return '订单退款'
    case 5:
      return '人工调整'
    case 6:
      return 'ERP增量变化'
    case 7:
      return 'ERP全量变化'
    case 8:
      return '分销佣金'
    default:
      return stateCode
  }
}

// 操作方式 1 充值提现 2 消费
export function chargeOperateFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '充值提现'
    case 2:
      return '消费'
    default:
      return stateCode
  }
}

// 发票类型 1 普通发票 2 增值税发票
export function invoiceTypeFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '普通发票'
    case 2:
      return '增值税发票'
    default:
      return stateCode
  }
}

// 对账单支付方式，1 立即支付 2 期间结算
export function billPayTpFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '立即支付'
    case 2:
      return '期间结算'
    default:
      return stateCode
  }
}

// 对账单付款状态 0 全部  1 未付款  2部分付款  3 已付款
export function billPayStFormatter(stateCode) {
  switch (stateCode) {
    case 0:
      return '全部'
    case 1:
      return '未付款'
    case 2:
      return '未付款'
    case 3:
      return '已付款'
    default:
      return stateCode
  }
}

// 开票区域
export function invoAreaFormatter(stateCode) {
  switch (stateCode) {
    case 1:
      return '国内'
    case 2:
      return '国外'
    default:
      return stateCode
  }
}

// 收款单，详情，订单状态
export function orderStatusRefund(stateCode) {
  switch (stateCode) {
    case 1:
      return '待确认'
    case 2:
      return '已确认'
    case 3:
      return '已核销'
    // case 4:
    //   return '拒绝'
  }
}

// 收款单，详情，支付币别
export function orderStatusCurrency(stateCode) {
  switch (stateCode) {
    case 1:
      return '人民币'
  }
}

// 收款单，详情，支付方式
export function orderStatusWay(stateCode) {
  switch (stateCode) {
    case 1:
      return '支付宝'
    case 2:
      return '微信'
    case 3:
      return '区间结算'
    case 4:
      return '线下转账'
    case 5:
      return '余额支付'
    case 6:
      return '快钱支付'
    case 7:
      return '余额+支付宝'
    case 8:
      return '余额+微信'
    case 9:
      return '余额+快钱支付'
    case 10:
      return '头条支付'
    default:
      return stateCode
  }
}
