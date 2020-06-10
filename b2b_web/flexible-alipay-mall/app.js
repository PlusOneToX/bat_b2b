App({
  // 开源框架中，很多appid这类数据会留空，请自行填写 
  onLaunch: function () {
    // 分销商
    if (this.globalData.host === 'https://api.bat.com/') {
      // 正式
      my.setStorageSync({
        key: 'distributorId',
        data: '7152'
      });
    } else {
      // 测试
      my.setStorageSync({
        key: 'distributorId',
        data: '2601'
      });
    }

    if (this.globalData.appId === '') {
      // BAT
      my.setStorageSync({
        key: 'orderSource',
        data: 'GF60007'
      });
      my.setStorageSync({
        key: 'platform',
        data: 'GF60007'
      });
    } else {
      // 其他
      my.setStorageSync({
        key: 'orderSource',
        data: 'GF60008'
      });
      my.setStorageSync({
        key: 'platform',
        data: 'GF60008'
      });
    }
  },
  globalData: {
    appId: '', // 其他
    host: 'https://api.bat.com/', // 正式       -    当前使用的是BAT正式服，请更改
    // host: 'http://120.78.221.134:8083/', // 测试
    // host: 'http://120.78.221.134:8083/', // 本地
  }
})
