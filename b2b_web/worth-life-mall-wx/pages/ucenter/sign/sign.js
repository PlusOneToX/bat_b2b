var util = require('../../../utils/util.js');

Page({
  data: {
    monthDaySize: 1, //当月天数
    calendarSignData: [], //当前月签到的list
    date: 1, //当天的日期 4
    calendarSignDay: 0 //当月签到次数
  },
  //事件处理函数
  calendarSign: function() {
    let that = this;
    util.request('sign/userSign').then(function(res) {
      if (res.code === 0) {
        if (res.data === 1) {
          util.showMsg('签到成功');
          that.getSignRecord();
        }
      }
    });
  },
  onLoad: function() {
    let that = this;
    let mydate = new Date();
    let year = mydate.getFullYear();
    let month = mydate.getMonth() + 1;
    let date = mydate.getDate();
    let day = mydate.getDay();
    let nbsp;
    if ((date - day) <= 0) {
      nbsp = day - date + 1;
    } else {
      nbsp = 7 - ((date - day) % 7) + 1;
    }
    if (nbsp > 7) {
      nbsp = nbsp - 7
    }
    this.setData({
      year: year,
      month: month,
      nbsp: nbsp,
      date: date
    })

    that.getSignRecord();
  },
  getSignRecord: function() {
    let that = this;
    util.request('sign/getMonthSign').then(function(res) {
      if (res.code === 0) {
        that.setData({
          monthDaySize: res.monthDaySize,
          calendarSignData: res.calendarSignData,
          calendarSignDay: res.calendarSignDay
        })
      }
    });
  }
})
