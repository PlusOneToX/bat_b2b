// pages/code/codeList/codeList.js
// Toast
import Toast from '../../../miniprogram_npm/@vant/weapp/toast/toast';
// Api
import $request from '../../../assets/api/request'
import api from '../../../assets/api/allApi'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    curTab: 1, // 当前 tab
    // 卡包状态
    codeTitle: [{
        name: "未使用",
        id: 1,
      },
      {
        name: "已使用",
        id: 2,
      },
      {
        name: "已失效",
        id: 3,
      },
    ],
    size: 10, // 卡包分页条数
    codeList: [], // 卡包列表
    codePage: 1, // 卡包分页
    codeTotal: 0, // 卡包总数
  },

  // 获取卡包列表
  getCodeListData() {
    let userId = wx.getStorageSync('userId');
    Toast.loading({
      duration: 0, // 持续展示 toast
      message: '加载中...',
      forbidClick: true,
      loadingType: 'spinner',
      selector: '#van-toast',
    });

    $request
      .get(api.getCodeList, {
        userId: userId,
        page: this.data.codePage,
        size: this.data.size,
        status: this.data.curTab,
      })
      .then((res) => {
        if (res.success) {
          if (res.data.list && res.data.list.length > 0) {
            let codeData = [];
            if (this.data.codePage === 1) {
              codeData = res.data.list;
            } else {
              codeData = this.data.codeList.concat(res.data.list)
            }

            this.setData({
              codeList: codeData,
              codeTotal: res.data.total
            })
          } else {
            this.setData({
              codeList: [],
              codeTotal: 0
            })
          }
        } else {
          Toast.fail(res.errMessage);
        }
        Toast.clear();
      });
  },

  // 上拉加载
  loadMore() {
    if (this.data.codeList.length < this.data.codeTotal) {
      let codePage = this.data.codePage + 1
      this.setData({
        codePage: codePage,
        hiddenDivider: true
      })
      this.getCodeListData();
    } else {
      this.setData({
        hiddenDivider: false
      })
    }
  },

  // 卡包状态切换
  handleClickCode(e) {
    let codeId = e.detail.name; // 获取卡包状态id
    if (codeId) {
      this.setData({
        curTab: codeId,
        codePage: 1,
      })
      this.getCodeListData();
    }
  },

  // 立即定制
  goCustom(e) {
    let id = e.currentTarget.dataset.id;

    // 获取默认 exchangeId
    $request
      .get(api.getDefaultExchangeId, {
        id: id,
      })
      .then((res) => {
        if (res.success) {
          let exchangeId = res.data.exchangeId;
          let distributorId = 2601;
          if (res.data.distributorId) {
            distributorId = res.data.distributorId;
          }

          wx.setStorageSync("distributorId", distributorId);
          wx.setStorageSync("exchangeId", exchangeId);

          // 根据兑换码id获取可兑换材质
          $request
            .get(api.getMaterialByExchangeId, {
              id: exchangeId,
            })
            .then((res) => {
              if (res.success) {
                let params = "";
                if (res.data && res.data.length > 0) {
                  // 默认取第一个id
                  params = JSON.stringify({
                    materialId: res.data[0],
                    modelId: wx.getStorageSync('modelId'),
                    modelName: encodeURIComponent(encodeURIComponent(wx.getStorageSync('modelName'))),
                    canSelectMaterial: "no", // 是否可选材质
                  })
                } else {
                  params = JSON.stringify({
                    modelId: wx.getStorageSync('modelId'),
                    modelName: encodeURIComponent(encodeURIComponent(wx.getStorageSync('modelName'))),
                    canSelectMaterial: "no", // 是否可选材质
                  })
                }

                wx.navigateTo({
                  url: '/pages/webview/webview?enterFlag=diyCustom&enterParams=' + params,
                })
              } else {
                Toast.fail(res.errMessage);
              }
            });
        } else {
          Toast.fail(res.errMessage);
        }
      });
  },

  // 绑定兑换卡
  goBindCode() {
    wx.navigateTo({
      url: '/pages/code/bindCode/bindCode',
    })
  },

  // 解绑
  handleUnbound(e) {
    let id = e.currentTarget.dataset.id;
    let userId = wx.getStorageSync('userId');

    $request
      .remove(api.unboundCard, {
        userId: userId,
        exchangeCodeId: id,
      })
      .then((res) => {
        if (res.success) {
          Toast.success('解绑成功~');
          setTimeout(() => {
            this.getCodeListData();
          }, 1000);
        } else {
          Toast.fail(res.errMessage);
        }
      });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getCodeListData();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    this.loadMore();
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function (res) {
    if (res.from === "button") {
      let id = res.target.dataset.id;
      return $request
        .post(api.exchangeTransferSend, {
          id: id,
        })
        .then((res) => {
          if (res.success) {
            return {
              title: res.data.transferText,
              path: "/pages/transfer/transfer?receiveId=" + res.data.id,
              imageUrl: res.data.transferImg,
            }
          } else {
            Toast.fail(res.errMessage);
          }
        })
    }
  }
})