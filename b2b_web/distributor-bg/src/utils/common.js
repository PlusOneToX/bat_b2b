export function sortBy(attr, rev) {
  // 第二个参数没有传递 默认升序排列
  if (rev === undefined) {
    rev = 1
  } else {
    rev = (rev) ? 1 : -1
  }
  return function(a, b) {
    a = a[attr]
    b = b[attr]
    if (a < b) {
      return rev * -1
    }
    if (a > b) {
      return rev * 1
    }
    return 0
  }
}
// 获取地址栏的参数
export function getUrlCode() {
    let url = location.href;
    if(url.split('?')[1]){
       url=url.split('?')[1];
    }else{
       return
    }
    let theRequest = new Object();
    let str = url;
    console.log(str);
    let strs = str.split("&");
    for (let i = 0; i < strs.length; i++) {
      theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
    }
    return theRequest;
  
}

// ArrayBuffer转字符串
export function Uint82Str(arraybuffer) {
  const arr = Array.prototype.slice.call(new Uint8Array(arraybuffer))
  var str = ''
  for (var i = 0; i < arr.length; i++) {
    str += String.fromCharCode(arr[i])
  }

  return decodeURIComponent(escape(str))
  // return String.fromCharCode.apply(null, new Uint8Array(arraybuffer));
}
// 判断是否为数字
export function isNumber(val) {
  var regPos = /^\d+(\.\d+)?$/ // 非负浮点数
  var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/ // 负浮点数
  if (regPos.test(val) || regNeg.test(val)) {
    return true
  } else {
    return false
  }
}

// num为传入的值，n为保留的小数位
export function fomatFloat(num, n) {
  var f = parseFloat(num)
  if (isNaN(f)) {
    return false
  }
  f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n) // n 幂
  var s = f.toString()
  var rs = s.indexOf('.')
  // 判定如果是整数，增加小数点再补0
  if (rs < 0) {
    rs = s.length
    s += '.'
  }
  while (s.length <= rs + n) {
    s += '0'
  }
  return s
}

// 去重，根据ID
export function setArr(arr) {
  const obj = {}
  const temp = []
  for (let i = 0; i < arr.length; i++) {
    const type = Object.prototype.toString.call(arr[i].id)// 不加类型 分不清 1 '1'
    if (!obj[ arr[i].id + type]) {
      temp.push(arr[i])
      obj[ arr[i].id + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
    }
  }
  return temp
}

// 去重
export function setArr2(arr) {
  const obj = {}
  const temp = []
  for (let i = 0; i < arr.length; i++) {
    const type = Object.prototype.toString.call(arr[i])// 不加类型 分不清 1 '1'
    if (!obj[ arr[i] + type]) {
      temp.push(arr[i])
      obj[ arr[i] + type ] = true// 这里给true 利于代码阅读和判断。  如果给 0,'' ,false ,undefined 都会在if那里判断为 false 不利于代码阅读
    }
  }
  return temp
}

// 去重
export function setArrByKey(arr, value) {
  let temp = []
  temp = arr.reduce((all, next) => all.some((atom) => atom[value] === next[value]) ? all : [...all, next], [])
  return temp
}

// Export2Excel 导出格式
export function formatJson(filterVal, jsonData) {
  return jsonData.map(v => filterVal.map(j => {
    if (j === 'timestamp') {
      return 'parseTime(v[j])'
    } else {
      return v[j]
    }
  }))
}

// 若有小数，最多只允许输入两位小数
export function inputLimitNegative (e) {
  let key = e.key
  let value = e.target.value
  let  reg = /[0-9]/
  // 只能输入数字，回退和小数点
  if(!(reg.test(Number(key)) || key==='Backspace' || key==='.')){
    e.returnValue = false
    return false
  }
  // 不允许第一个子是“.”
  if(value===""){
    if(key === '.' ){
      e.returnValue = false
      return false
    }
  }
  // 只允许存在一个小数点
  if(value.indexOf(".")!==-1){
    if(key === '.' ){
      e.returnValue = false
      return false
    }
  }
  // 有小数的话最多输入两位小数
  if(value.indexOf(".")!==-1){
    let index = value.indexOf(".")
    if(value[index+2] && key!=='Backspace'){
      e.returnValue = false
      return false
    }
  }

  return true
}
