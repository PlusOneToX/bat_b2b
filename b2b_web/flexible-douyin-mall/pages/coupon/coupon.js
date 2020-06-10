// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Page({
  data: {
    couponTabs: [{
      id: 1,
      title: "待领取",
      value: "立即领取",
    },
    {
      id: 3,
      title: "未使用",
      value: "未使用",
    },
    {
      id: 4,
      title: "已使用",
      value: "已使用",
    },
    {
      id: 5,
      title: "已过期",
      value: "已过期",
    }],
    curTab: 1, // 当前默认优惠券状态，待领取
    couponList: [], // 优惠券列表
    couponTotal: 0, // 优惠券总数
    couponPage: 1,  // 优惠券页数
    couponSize: 8, // 优惠券数量
    // loading
    showLoading: false,
    message: "数据加载中",
  },
  
  // 获取优惠券列表
  getCouponList(status) {
    this.setData({
      showLoading: true, // loading
    })

    // 优惠券状态：0 全部，1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
    $request.get(api.getCouponList, {
        page: this.data.couponPage,
        size: this.data.couponSize,
        statuss: status,
      })
      .then((res) => {
        if (res.success) {
          let couponList = [];
          if (this.data.couponPage === 1) {
            couponList = res.data.list;
          } else {
            couponList = this.data.couponList.concat(res.data.list);
          }

          couponList.forEach((item) => {
            // 优惠券说明
            if (item.couponExplain !== "") {
              item.couponExplainArr = item.couponExplain
                .trim()
                .split(/[\r|\n]/);
            }
            // 是否今天到期
            item.isToday = this.isToday(item.endTime)
            // 是否显示优惠券说明
            item.showExplain = false

            // 开始日期
            let startStr = new Date(item.startTime).getTime();
            item.startTimeStr = startStr;
            // 结束日期
            let endStr = new Date(item.endTime).getTime();
            item.endTimeStr = endStr;
            // 领取时间
            if (item.createTime !== "") {
              let createStr = new Date(item.createTime).getTime();
              item.createTimeStr = createStr;
            }
          });

          this.setData({
            couponList: couponList,
            couponTotal: res.data.total
          })
        } else {
          if (res.errCode === "B_AUTH_FAIL") {
            let enterParams = JSON.stringify({});
            tt.redirectTo({
              url: '/pages/login/login?enterFlag=coupon&enterParams=' + enterParams,
            })
          } else {
            tt.showToast({
              title: res.errMessage,
              icon: "none",
              duration: 2000,
            });
          }
        }

        this.setData({
          showLoading: false, // loading
        })
      });
  },

  // 上拉加载
  loadMore() {
    if (this.data.couponList.length < this.data.couponTotal) {
      let couponPage = this.data.couponPage + 1
      this.setData({
        couponPage: couponPage,
      })
      
      this.getCouponList(this.data.curTab);
    }
  },

  // 判断到期日期是否为当天
  isToday(date) {
    if (new Date(date).toDateString() === new Date().toDateString()) {
      return true;
    } else {
      return false;
    }
  },

  // 优惠券说明
  handleExplain(e) {
    let index = e.detail;
    let couponList = this.data.couponList;
    couponList.forEach((coupon, i) => {
      if (i === index && !coupon.showExplain) {
        coupon.showExplain = true;
      } else {
        coupon.showExplain = false;
      }
    })

    this.setData({
      couponList: couponList
    });
  },

  // 优惠券状态
  handleTab(e) {
    let curTab = e.detail;
    this.setData({
      curTab: curTab,
      couponPage: 1,
    });
    this.getCouponList(curTab);
  },

  onLoad: function (options) {
    this.getCouponList(this.data.curTab);
  },

  // 页面上拉触底事件的处理函数
  onReachBottom: function() {
    // 上拉加载
    this.loadMore();
  }
})