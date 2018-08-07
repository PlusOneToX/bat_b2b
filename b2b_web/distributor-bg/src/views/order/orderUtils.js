/*
该文件放置各种方法
 */

/**
 * 高阶函数,相当于一个修饰器
 * 在执行callback前请用户确定，防止误操作
 * 柯里化，分步传参，第一步传参this给vm，之后就不必再输入this
 * @param  {vue instance}   vm       vue实例，传入this
 * @param  {string}   operation   操作名称
 * @param  {Function} callback 如果确认，将会执行的函数,应返回一个promise
 * operation应能解释callback的行为
 */
export const confirmCreator = vm => (operation, callback) => {
  return vm.$confirm(`${operation}？`, '提示', {
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
    }
  }).catch(e => {
    vm.$message({
      type: 'info',
      message: '已取消操作'
    })
    throw e
  })
}

/* ====Formatters 数据格式转化(时间，订单状态等等)==== */
/* 都是纯函数 */

// ======== 订单列表部分 ========
export function deliverStatusFormatter(stateCode) { // 订单列表发货状态
  switch (stateCode) {
    case 1:
      return '待确认'
    case 2:
      return '已确认'
    case 3:
      return '已作废'
    case 5:
      return '确认中'
    default:
      return stateCode
  }
}

export function payStatusFormatter(stateCode) { // 订单列表付款状态
  switch (stateCode) {
    case 1:
      return '未付款'
    case 2:
      return '部分付款'
    case 3:
      return '已付款'
    case 4:
      return '部分退款'
    case 5:
      return '退款申请中'
    case 6:
      return '已退款'
    default:
      return stateCode
  }
}

export function category(stateCode) { // 订单列表订单类别
  switch (stateCode) {
    case 1:
      return '在途商品'
    case 2:
      return '在库商品'
    case 3:
      return '定制商品'
    default:
      return stateCode
  }
}

export function orderPaymentFormatter(stateCode) { // 订单列表支付方式
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

export function orderStatusFormatter(stateCode) { // 订单列表订单状态
  switch (stateCode) {
    case 1:
      return '待确认'
    case 2:
      return '待发货'
    case 3:
      return '部分发货'
    case 4:
      return '待收货'
    case 5:
      return '已关闭'
    case 6:
      return '已完成'
    case 7:
      return '待确认'  
    case 9:
      return '出库中'  
    default:
      return stateCode
  }
}
export function orderSourceFormatter(stateCode) { // 订单来源状态
  switch (stateCode) {
    default:
      return '其他'
  }
}

export function stockTypeFormatter(stateCode) { // 订单类型
  switch (stateCode) {
    case 1:
      return '在库'
    case 2:
      return '在途'
    case 3:
      return '预售（MTO）'
    case 4:
      return '在库+在途'
    case 5:
      return '委外'
    default:
      return stateCode
  }
}

export function orderGoodsTypeFormatter(stateCode) { // 订单类型
  switch (stateCode) {
    case '1':
      return '在库订单'
    case '2':
      return '在途订单'
    case '3':
      return '定制商品订单'
    case '4':
      return 'MTO订单'
    case '5':
      return '直运订单'
    case '7':
      return '新定制商品订单'
    case '8':
      return '拼团订单'
    default:
      return stateCode
  }
}

// ========  ========

export function booleanFormatter(stateCode) { // 布尔值解释
  switch (stateCode) {
    case 0:
      return '否'
    case 1:
      return '是'
    default:
      return stateCode
  }
}
export function booleanProcess(stateCode) { // 布尔值解释
  switch (stateCode) {
    case 0:
      return '否'
    case 1:
      return '是'
    default:
      return stateCode
  }
}

export function invoiceTypeFormatter(stateCode) { // 发票类型
  switch (stateCode) {
    case 1:
      return '普通'
    case 2:
      return '增值税'
    default:
      return stateCode
  }
}

export function invoiceTitleFormatter(stateCode) { // 发票抬头
  switch (stateCode) {
    case 1:
      return '个人'
    case 2:
      return '单位'
    default:
      return stateCode
  }
}

export function capitalStatusFormatter(stateCode) { // 审核状态
  // 0.未审批 1.审批中，2.审批通过 3.审批未通过
  switch (stateCode) {
    case 0:
      return '未审批'
    case 1:
      return '审批中'
    case 2:
      return '审批通过'
    case 3:
      return '审批未通过'
    default:
      return stateCode
  }
}

// eslint-disable-next-line one-var
let
  // eslint-disable-next-line prefer-const
  orderListEventBus = require('@/views/order/eventBus').default,
  // eslint-disable-next-line prefer-const
  applyEventBus = require('@/views/order/applyForPrice/eventBus').default

export function chooseBus(parentName) {
  switch (parentName) {
    case 'orderList':
      return orderListEventBus
    case 'applyForPrice':
      return applyEventBus
    default:
      return orderListEventBus
  }
}

/**
 * 拼装耦合的连续两次异步操作的数据
 * 先柯里化——
 * 局部传参第1步 patchTable(tIdentity, pIdentity) 对暗号的方式
 * 局部传参第2步 patchTable(tableData, patchs) 要拼接的数据
 * 局部传参第3步 patchTable(tAttr, pAttr) 拼接的映射方式
 * @param  {array} tableData 主数据
 * @param  {array} patchs 用主数据的id请求回的数据
 * @param  {string} tIdentity id在tableData上的属性名
 * @param  {string} pIdentity id在patchs上的属性名
 * @param  {string[]} attrs 要拼接的属性
 * @return {void}
 */
export function asyncPatch(vm, tIdentity, pIdentity, tableData, patchs, tAttr, pAttr) {
  const orderedPatchs = tableData.map(tableRow => {
    for (const patch of patchs) {
      if (tableRow[tIdentity] === patch[pIdentity]) return patch
    }
  }) // 映射出和tableData一一对应的orderedPatchs
  tableData.forEach((val, index, array) => {
    if (orderedPatchs[index]) {
      vm.$set(val, tAttr, orderedPatchs[index][pAttr])
    }
  }) // 拼装完毕
}

export function saleStatusFormatter(stateCode) { // 导入订单详情上下架状态
  switch (stateCode) {
    case 1:
      return '已下架'
    case 2:
      return '已下架'
    case 3:
      return '上架中'
    case 4:
      return '上架中'
    default:
      return '无状态'
  }
}
