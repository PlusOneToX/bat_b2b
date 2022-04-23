const util = require('../../../utils/util.js')

//获取应用实例
const app = getApp()

Page({
  data: {
    userId: '',
    nickname: '',
    invitationCode: '',
    headImgUrl: '',
    qrCodeUrl: '',
    tempHeadImg: '',
    tempQrCode: '',
    imagePath: '',
  },
  onShow: function () {
    this.initial();
  },
  initial: function () {
    let that = this;
    let userInfo = wx.getStorageSync('userInfo');
    let token = wx.getStorageSync('token');

    // 页面显示
    if (userInfo && token) {
      app.globalData.userInfo = userInfo;
      app.globalData.token = token;
    }
    that.getInvitationInfo();
  },
  // 获取订单列表
  getInvitationInfo: function () {
    let that = this;
    util.request('distributor/getInvitationInfo').then(function(res) {
      if (res.code === 0) {
        that.setData({
          userId: res.data.userId,
          nickname: res.data.nickname,
          invitationCode: res.data.invitationCode,
          headImgUrl: res.data.headImgUrl,
          qrCodeUrl: res.data.qrCodeUrl,
        });
        wx.downloadFile({
          url: res.data.headImgUrl,
          success(res) {
            if (res.statusCode === 200) {
              that.setData({
                tempHeadImg: res.tempFilePath
              })
            }
            that.drawInvitation();
          }
        })
      }

    });
  },
  // 下拉刷新
  onPullDownRefresh: function () {
    // 显示顶部刷新图标
    wx.showNavigationBarLoading();
    var that = this;

    that.getInvitationInfo();
    // 隐藏导航栏加载框
    wx.hideNavigationBarLoading();
    // 停止下拉动作
    wx.stopPullDownRefresh();
  },
  drawInvitation: function () {
    let that = this;
    wx.downloadFile({
      //url网络图片地址必须要在小程序中配备合法域名
      url: that.data.qrCodeUrl,
      success(res) {
        if (res.statusCode === 200) {
          that.setData({
            tempQrCode: res.tempFilePath
          })

          const context = wx.createCanvasContext('invitationCanvas')
          // 背景图
          let canvasWidth = wx.getSystemInfoSync().windowWidth * 0.9
          let canvasHeight = wx.getSystemInfoSync().windowHeight * 0.9
          context.drawImage('/static/images/img_invitation_bg.jpg', 0, 0, canvasWidth, canvasHeight)

          // 邀请人信息：头像
          let headImgX = 50
          let headImgY = canvasHeight * 0.11
          let headImgWidth = canvasWidth * 0.12
          let headImgHeight = canvasWidth * 0.12
          context.save()
          context.beginPath()
          context.arc(headImgWidth / 2 + headImgX, headImgHeight / 2 + headImgY, headImgWidth / 2, 0, Math.PI * 2)
          context.clip()
          context.drawImage(that.data.tempHeadImg, headImgX, headImgY, headImgWidth, headImgHeight)
          context.restore()

          // 邀请人信息：描述
          let headImgYCenter = headImgHeight / 2 + headImgY
          let discWidth = headImgWidth + headImgX + 10
          context.save()
          context.setFontSize(15)
          context.fillText(that.data.nickname, discWidth, headImgYCenter)
          context.setFillStyle('#8B8D8B')
          context.setFontSize(14)
          context.fillText("邀您一起分享赚佣金", discWidth, headImgYCenter + 16)
          context.restore()

          // 小程序码
          let qrcodeWidth = canvasWidth * 0.25
          let qrcodeHeight = canvasWidth * 0.25
          context.drawImage(that.data.tempQrCode, 50, canvasHeight * 0.74, qrcodeWidth, qrcodeHeight)

          // 描述
          context.save()
          let contextWidth = qrcodeWidth + 70
          let contextCenterHeight = canvasHeight * 0.74 + qrcodeWidth / 2
          let contextLineHeight = 20
          context.setFillStyle('#8B8D8B')
          context.setFontSize(12)
          context.fillText('商城小程序', contextWidth, contextCenterHeight - contextLineHeight)
          context.fillText('长按识别小程序码，即刻体验', contextWidth, contextCenterHeight)
          context.fillText('邀请码：', contextWidth, contextCenterHeight + contextLineHeight)
          context.setFillStyle('#2CA244')
          context.fillText(that.data.invitationCode, contextWidth + 48, contextCenterHeight + contextLineHeight)
          context.restore()

          context.draw()

           //将生成好的图片保存到本地
           setTimeout(function () {
            wx.canvasToTempFilePath({
              canvasId: 'invitationCanvas',
              success: function (res) {
                var tempFilePath = res.tempFilePath;
                that.setData({
                  imagePath: tempFilePath,
                  canvasHidden: true
                });
              },
              fail: function (res) {
                console.log(res);
              }
            });
          }, 200);
        }
      }
    })
  },
  //点击保存到相册
  savePoster: function () {
    var that = this
    wx.saveImageToPhotosAlbum({
      filePath: that.data.imagePath,
      success(res) {
        wx.showModal({
          content: '已保存到相册',
          showCancel: false,
          confirmText: '确定',
          confirmColor: '#2BF39D',
          success: function (res) {
            if (res.confirm) {
              /* 该隐藏的隐藏 */
              that.setData({
                maskHidden: false
              })
            }
          },
          fail: function (res) {}
        })
      }
    })
  },
})