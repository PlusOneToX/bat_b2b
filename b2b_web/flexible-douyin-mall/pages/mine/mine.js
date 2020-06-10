// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Page({
  data: {
    avatarImg: '../../assets/images/my_avator.png', // 头像
    nikeName: '游客', // 昵称
    showDialog: false, // 在线客服弹窗
    phoneNumber: "17302671334", // 客服电话
    wechatStr: "w-666357", // 微信客服
  },

  // 获取用户信息
  getUserInfo(userId) {
    $request
      .get(api.getUserInfo, {
        id: userId,
      })
      .then((res) => {
        if (res.success) {
          if (
            res.data.nikeName !== null &&
            res.data.nikeName !== "" &&
            res.data.nikeName !== undefined
          ) {
            this.setData({
              nikeName: res.data.nikeName
            })
          }
          if (
            res.data.headPortrait !== null &&
            res.data.headPortrait !== "" &&
            res.data.headPortrait !== undefined
          ) {
            this.setData({
              avatarImg: res.data.headPortrait
            })
          }
        } else {
          tt.showToast({
            title: res.errMessage,
            icon: "none",
            duration: 2000,
          });
        }
      });
  },

  // 在线客服弹窗 - 显示/隐藏
  handleDialog(e) {
    let status = e.currentTarget.dataset.status;

    this.setData({
      showDialog: status
    })
  },

  // 客服热线
  handlePhone() {
    let that = this;
    tt.showModal({
      title: "拨打热线",
      content: that.data.phoneNumber,
      confirmColor: "#333333",
      cancelColor: "#999999",
      success(res) {
        if (res.confirm) {
          // 拨打电话
          tt.makePhoneCall({
            phoneNumber: that.data.phoneNumber
          })

          that.setData({
            showDialog: false
          })
        }
      }
    });
  },

  // 微信客服
  handleWechat() {
    let that = this;
    tt.showModal({
      title: "客服微信号：" + that.data.wechatStr,
      content: "是否复制微信号？",
      confirmColor: "#333333",
      cancelColor: "#999999",
      success(res) {
        if (res.confirm) {
          // 拨打电话
          tt.setClipboardData({
            data: that.data.wechatStr,
            success(res) {
              tt.showToast({
                title: '复制成功',
                icon: "none",
                duration: 2000,
              });
            }
          });

          that.setData({
            showDialog: false
          })
        }
      }
    });
  },

  /* 页面跳转 */
  // 订单
  goOrder(e) {
    let sid = e.currentTarget.dataset.sid;
    let enterParams = JSON.stringify({
      sid: sid
    });
    tt.navigateTo({
      url: "/pages/webview/webview?enterFlag=orderList&enterParams=" + enterParams,
    });
  },

  // 购物车
  goShopcart() {
    tt.switchTab({
      url: '/pages/shopcart/shopcart',
    })
  },

  // 优惠券
  goCoupon() {
    tt.navigateTo({
      url: '/pages/coupon/coupon',
    })
  },

  // 地址管理
  goAddress() {
    let enterParams = JSON.stringify({});
    tt.navigateTo({
      url: '/pages/webview/webview?enterFlag=address&enterParams=' + enterParams,
    })
  },
  
  onLoad: function (options) {

  },
  
  onShow: function () {
    let userId = tt.getStorageSync('userId')
    if (userId) {
      this.getUserInfo(userId)
    }
  },
})