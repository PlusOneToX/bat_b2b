// Api
import $request from '/assets/api/request'
import api from '/assets/api/allApi'

Page({
  data: {
    couponTabs: [{
      id: 1,
      title: "待领取",
      value: "立即领取",
      count: 0,
    },
    {
      id: 3,
      title: "未使用",
      value: "未使用",
      count: 0,
    },
    {
      id: 4,
      title: "已使用",
      value: "已使用",
      count: 0,
    },
    {
      id: 5,
      title: "已过期",
      value: "已过期",
      count: 0,
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

  // 优惠券汇总
  getCouponCount() {
    // 优惠券状态：0 全部，1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
    $request.get(api.getCouponCount)
      .then((res) => {
        if (res.success) {
          let data = res.data;
          let couponTabs = this.data.couponTabs;
          couponTabs.forEach((item) => {
            switch (item.id) {
              case 1:
                // 待领取
                item.count = data.unReceiveCount;
                break;
              case 3:
                // 未使用
                item.count = data.unUsedCount;
                break;
              case 4:
                // 已使用
                item.count = data.usedCount;
                break;
              case 5:
                // 已过期
                item.count = data.expiredCount;
                break;
              default:
                break;
            }
          })

          console.log(couponTabs)

          this.setData({
            couponTabs: couponTabs
          });
        }
      });
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
            if (item.startTime && item.startTime !== "") {
              let startStr = new Date(item.startTime.replace(/\-/g, "/")).getTime();
              item.startTimeStr = startStr;
            }
            // 结束日期
            if (item.endTime && item.endTime !== "") {
              let endStr = new Date(item.endTime.replace(/\-/g, "/")).getTime();
              item.endTimeStr = endStr;
            }
            // 领取时间
            if (item.createTime && item.createTime !== "") {
              let createStr = new Date(item.createTime.replace(/\-/g, "/")).getTime();
              item.createTimeStr = createStr;
            }
          });

          this.setData({
            couponList: couponList,
            couponTotal: res.data.total
          });

          // 优惠券汇总
          this.getCouponCount();
        } else {
          if (res.errCode === "B_AUTH_FAIL") {
            let enterParams = JSON.stringify({});
            my.redirectTo({
              url: '/pages/login/login?enterFlag=coupon&enterParams=' + enterParams,
            })
          } else {
            my.showToast({
              content: res.errMessage,
              type: "none",
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
    let index = e;
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
    let curTab = e;
    console.log("handleTab: " + e);
    this.setData({
      curTab: curTab,
      couponPage: 1,
    });
    this.getCouponList(curTab);
  },

  // 监听页面加载 获页面传参
  onLoad: function (options) {
    //Tab获取
    console.log("上一个页面到优惠券页面指定Tab获取");
    console.log(options.curTab); // 输出1和3
    if (options.curTab == undefined) {
      this.getCouponList(1);
    } else {
      this.setData({
        curTab: Number(options.curTab)
      });
      this.getCouponList(this.data.curTab);
    }
  },

  // 页面上拉触底事件的处理函数
  onReachBottom: function () {
    // 上拉加载
    this.loadMore();
  }
})