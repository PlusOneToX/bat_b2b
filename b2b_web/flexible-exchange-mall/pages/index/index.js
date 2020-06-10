// pages/index/index.js
// Toast
import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
// Dialog
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    distributorId: "", // 分销商id
    userId: "", // 用户id
    materialId: "", // 材质id
    materialName: "", // 材质名称
    modelId: "", // 型号id
    modelName: "", // 型号名称
    brandId: "", // 品牌id
    brandName: "", // 品牌名称
    bannerList: [], // 轮播图
    cardNum: 0, // 卡包数量
    seriesList: [], // 系列列表
    recommendPage: 0, // 推荐当前分页
    recommendCount: 8, // 推荐分页条数
    recommendTotal: 0, // 推荐总数
    recommendData: [], // 推荐数据
    seriesPage: 0, // 系列当前分页
    seriesCount: 8, // 系列分页条数
    seriesTotal: 0, // 系列总数
    seriesData: [], // 系列数据
    curTab: 0, // 当前 tab
    isLoading: false, // 加载
    scrollHeight: 0, // 高度
    showModelDialog: true, // 选择型号弹窗
    mobile: "", // 手机型号
    curMobile: "", // 当前手机型号
    curIndex: 0, // 默认展示第一项
    curChooseModel: {}, // 当前选中机型
    clickedTab: false, // 是否点击tab
    noticeText: '', // 通知栏
  },

  // 获取默认兑换卡id
  initExchangeId(exchangeId) {
    $request
      .get(api.getDefaultExchangeId, {
        id: exchangeId,
      })
      .then((res) => {
        if (res.success) {
          let distributorId = res.data.distributorId ? res.data.distributorId : 2601

          let exchangeId = res.data.exchangeId;
          if (!app.globalData.hasClickDialog) {
            if (Number(exchangeId) === 82 || Number(exchangeId) === 93 || Number(exchangeId) === 157) {
              Dialog.confirm({
                title: "温馨提示",
                message: "亮彩玻璃壳已升级为保护性更佳的炫彩肤感壳。\n亲肤手感、全包保护，诚邀您一同感受。",
                className: "pop-dialog",
                confirmButtonColor: "#333333",
                showCancelButton: false,
              }).then(() => {
                app.globalData.hasClickDialog = true;
              }).catch(() => {
                // on cancel
              });
            }
          }

          this.setData({
            exchangeId: exchangeId,
            distributorId: distributorId
          })

          wx.setStorageSync('distributorId', distributorId)
          wx.setStorageSync('exchangeId', this.data.exchangeId)

          this.getModelAndMaterial(this.data.exchangeId);
        } else {
          Toast.fail(res.errMessage);
          Toast.clear();
        }
      });
  },

  // 根据 exchangeId 获取型号/材质列表
  getModelAndMaterial(exchangeId) {
    if (Number(exchangeId) === 82 || Number(exchangeId) === 93 || Number(exchangeId) === 157) {
      exchangeId = 158
    }
    $request
      .get(api.getModelAndMaterial, {
        id: exchangeId,
      })
      .then((res) => {
        if (res.success) {
          // 获取型号列表
          if (res.data.modelList && res.data.materialList.length > 0) {
            this.setData({
              modelList: res.data.modelList
            })

            // 根据型号查询品牌并显示
            if (
              this.data.mobile !== "" &&
              this.data.mobile !== undefined &&
              this.data.mobile !== null
            ) {
              this.getModelId(this.data.mobile);
            } else {
              // 显示选择型号弹窗
              this.setData({
                showModelDialog: false
              })
            }
          }

          // 获取材质列表
          if (res.data.materialList && res.data.materialList.length > 0) {
            this.setData({
              materialList: res.data.materialList
            })
            if (res.data.materialList[0].childrenList) {
              this.setData({
                materialName: res.data.materialList[0].name +
                  "-" +
                  res.data.materialList[0].childrenList[0].name,
                materialId: res.data.materialList[0].childrenList[0].materialId
              })
            } else {
              this.setData({
                materialName: res.data.materialList[0].name,
                materialId: res.data.materialList[0].materialId
              })
            }
          }

          // 缓存材质id
          wx.setStorageSync("materialId", this.data.materialId);

          // 获取推荐数据
          this.getRecommendData(1);
          // 获取首页系列
          this.getSeriesList();
        } else {
          // Toast.fail(res.errMessage);
          Toast.clear();
        }
      });
  },

  // 根据手机型号查询型号及品牌
  getModelId(mname) {
    if (this.data.modelList.length > 0) {
      let flag = false;
      try {
        this.data.modelList.forEach((brand, pIndex) => {
          if (brand.childrenList.length > 0) {
            brand.childrenList.forEach((model, index) => {
              if (
                model.name.replace(/\s*/g, "").toLowerCase() ===
                mname.replace(/\s*/g, "").toLowerCase()
              ) {
                this.setData({
                  // 品牌/型号
                  modelId: model.modelId,
                  modelName: model.name,
                  mobile: model.name,
                  brandId: brand.modelId,
                  brandName: brand.name,
                  // 获取当前选中大类
                  curIndex: pIndex,
                  curMobile: model.name,
                  // 隐藏选择型号弹窗
                  showModelDialog: true,
                })

                // 缓存机型id、机型名称
                wx.setStorageSync("modelId", model.modelId);
                wx.setStorageSync("modelName", model.name);
                // 缓存机型品牌id、品牌名称
                wx.setStorageSync("brandId", brand.modelId);
                wx.setStorageSync("brandName", brand.name);

                flag = true;

                throw new Error("end");
              }
            });
          }
        });

        if (!flag) {
          // 显示选择型号弹窗
          this.setData({
            showModelDialog: false
          })
        }
      } catch (e) {
        Toast.clear();
      }
    }
  },

  // 获取轮播图
  getBannerList() {
    $request
      .get(api.getBanner, {
        distributorId: this.data.distributorId,
      })
      .then((res) => {
        if (res.success) {
          this.setData({
            bannerList: res.data
          })
        } else {
          Toast.fail(res.errMessage);
          Toast.clear();
        }
      });
  },

  // 获取首页系列
  getSeriesList() {
    $request
      .get(api.getSeriesList, {
        distributorId: this.data.distributorId,
        materialId: this.data.materialId,
        modelId: this.data.modelId,
      })
      .then((res) => {
        if (res.success) {
          let seriesList = res.data;
          seriesList.unshift({
            themeName: "推荐",
          });
          this.setData({
            seriesList: seriesList
          })
        } else {
          Toast.fail(res.errMessage);
          Toast.clear();
        }
      });
  },

  // 获取推荐数据
  getRecommendData(recommendPage) {
    this.setData({
      recommendPage: recommendPage
    })
    $request
      .get(api.getRecommendData, {
        distributorId: this.data.distributorId,
        modelId: this.data.modelId,
        materialId: this.data.materialId,
        page: recommendPage,
        count: this.data.recommendCount,
      })
      .then((res) => {
        if (res.success) {
          if (res.data.list && res.data.list.length > 0) {
            let recommendData = [];
            if (recommendPage === 1) {
              recommendData = res.data.list;
            } else {
              recommendData = this.data.recommendData.concat(res.data.list)
            }
            this.setData({
              recommendData: recommendData,
              recommendTotal: res.data.total
            })

            // 设置推荐/系列区域高度
            let H = wx.getSystemInfoSync().windowHeight
            this.setData({
              scrollHeight: H - 103
            })
          } else {
            this.setData({
              recommendData: [],
              recommendTotal: 0
            })
          }
        } else {
          Toast.fail(res.errMessage);
        }
        Toast.clear();
      });
  },

  // 获取系列数据
  getSeriesData(seriesId, seriesPage) {
    this.setData({
      seriesPage: seriesPage
    })
    $request
      .get(api.getSeriesData, {
        distributorId: this.data.distributorId,
        themeId: seriesId,
        page: seriesPage,
        size: this.data.seriesCount,
        materialId: this.data.materialId,
        modelId: this.data.modelId,
      })
      .then((res) => {
        if (res.success) {
          if (res.data.list && res.data.list.length > 0) {
            let seriesData = [];
            if (seriesPage === 1) {
              seriesData = res.data.list;
            } else {
              seriesData = this.data.seriesData.concat(res.data.list);
            }
            this.setData({
              seriesData: seriesData,
              seriesTotal: res.data.total
            })

            // 设置推荐/系列区域高度
            let H = wx.getSystemInfoSync().windowHeight
            this.setData({
              scrollHeight: H - 103
            })
          } else {
            this.setData({
              seriesData: [],
              seriesTotal: 0
            })
          }
        } else {
          Toast.fail(res.errMessage);
        }
        Toast.clear();
      });
  },

  // 推荐/系列
  handleClickSeries(e) {
    // 设置是否点击tab（首次进入tab选中状态显示bug处理）
    this.setData({
      clickedTab: true
    })

    // 判断是否点击的是系列
    let seriesId = e.detail.name; // 获取系列id
    if (seriesId) {
      // 系列
      this.setData({
        curTab: seriesId,
        seriesPage: 1,
      })
      Toast.loading({
        duration: 0, // 持续展示 toast
        message: '加载中...',
        forbidClick: true,
        loadingType: 'spinner',
        selector: '#van-toast',
      });
      this.getSeriesData(seriesId, 1);
    } else {
      // 推荐
      this.setData({
        curTab: 0,
        recommendPage: 1,
      })
      this.getRecommendData(1);
    }
  },

  // 上拉加载
  loadMore() {
    if (this.data.curTab) {
      // 系列
      if (this.data.seriesData.length < this.data.seriesTotal) {
        let seriesPage = this.data.seriesPage + 1
        this.setData({
          seriesPage: seriesPage,
        })
        this.getSeriesData(this.data.curTab, this.data.seriesPage);
      }
    } else {
      // 推荐
      if (this.data.recommendData.length < this.data.recommendTotal) {
        let recommendPage = this.data.recommendPage + 1
        this.setData({
          recommendPage: recommendPage,
        })
        this.getRecommendData(this.data.recommendPage);
      }
    }
  },


  // 显示（机型选择）
  handleShowModel() {
    // 显示选择型号弹窗
    this.setData({
      showModelDialog: false
    })
  },

  // 切换型号（机型选择）
  changeModel(e) {
    let index = e.currentTarget.dataset.index;
    this.setData({
      curIndex: index
    })
  },

  // 选择机型（机型选择）
  chooseModel(e) {
    let item = e.currentTarget.dataset.item;

    if (!item.underStockFlag) {
      this.setData({
        curMobile: item.name,
        mobile: "",
      })
    }
  },

  // 跳过（机型选择）
  handleCancelModel() {
    // 隐藏选择型号弹窗
    this.setData({
      showModelDialog: true
    })
  },

  // 确定（机型选择）
  handleConfirmModel() {
    this.data.modelList.forEach((brand, pIndex) => {
      if (brand.childrenList.length > 0) {
        brand.childrenList.forEach((model) => {
          if (
            model.name.replace(/\s*/g, "").toLowerCase() ===
            this.data.curMobile.replace(/\s*/g, "").toLowerCase()
          ) {
            this.setData({
              // 隐藏选择型号弹窗
              showModelDialog: true,
              curChooseModel: model,
              // 品牌/型号
              modelId: model.modelId,
              modelName: model.name,
              mobile: model.name,
              brandId: brand.modelId,
              brandName: brand.name,
              // 获取当前选中大类
              curIndex: pIndex,
              curMobile: model.name,
            })

            // 缓存机型id、机型名称
            wx.setStorageSync("modelId", model.modelId);
            wx.setStorageSync("modelName", model.name);
            // 缓存机型品牌id、品牌名称
            wx.setStorageSync("brandId", brand.modelId);
            wx.setStorageSync("brandName", brand.name);

            // 获取推荐数据
            this.getRecommendData(1);
            // 获取首页系列
            this.getSeriesList();
          }
        });
      }
    });
  },

  // 获取可用兑换卡数量
  getCardNum(userId) {
    $request
      .get(api.getCardNum, {
        distributorId: this.data.distributorId,
        userId: userId,
        status: 1, // 1、未使用 2、已使用 3、已过期
      })
      .then((res) => {
        if (res.success) {
          this.setData({
            cardNum: res.data
          })
        } else {
          Toast.fail(res.errMessage);
          Toast.clear();
        }
      });
  },

  // 通知栏
  getNoticeList() {
    $request.get(api.getNoticeList).then((res) => {
      if (res.success) {
        if (res.data && res.data.length > 0) {
          let noticeData = res.data;
          let noticeCon = ''
          // 获取当前时间戳
          let now = new Date().getTime();
          noticeData.forEach((item) => {
            if (item.status && now >= item.startTime && now <= item.endTime) {
              // 判断大于开始时间并且小于结束时间，拼接公告内容
              noticeCon += item.content;
            }
          });
          this.setData({
            noticeText: noticeCon
          })
        } else {
          Toast.fail(res.errMessage);
          Toast.clear();
        }
      }
    })
  },


  /* 页面跳转 */
  // 官方主题
  goTheme() {
    wx.switchTab({
      url: '/pages/themeList/themeList',
    })
  },

  // 传图定制
  goCustom() {
    let enterParams = JSON.stringify({
      materialId: this.data.materialId,
      modelId: this.data.modelId,
      modelName: encodeURIComponent(encodeURIComponent(this.data.modelName)),
    });

    wx.navigateTo({
      url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
    });
  },

  // 卡包中心
  goCode() {
    let userId = wx.getStorageSync('userId');
    let openid = wx.getStorageSync('openid');
    if (userId && openid && openid !== 'undefined') {
      // 卡包中心
      wx.navigateTo({
        url: "/pages/code/codeList/codeList",
      });
    } else {
      // 授权登录
      var enterParams = JSON.stringify({});
      wx.navigateTo({
        url: "/pages/login/login?enterFlag=codeList&enterParams=" + enterParams,
      });
    }
  },

  // 跳转详情
  goDetail(e) {
    let item = e.currentTarget.dataset.item;
    let enterParams = JSON.stringify({
      materialId: this.data.materialId,
      brandId: this.data.brandId,
      brandName: encodeURIComponent(encodeURIComponent(this.data.brandName)),
      modelId: this.data.modelId,
      pictureId: item.pictureId,
      picName: encodeURIComponent(encodeURIComponent(item.pictureName)),
      picType: item.type,
      modelName: encodeURIComponent(encodeURIComponent(this.data.modelName)),
      noCameraVacancyPreview: item.noCameraVacancyPreview,
      originImage: item.originImage,
    });

    wx.navigateTo({
      url: '/pages/webview/webview?enterFlag=details&enterParams=' + enterParams,
    })
  },

  // 跳转图库详情
  goDetailList(e) {
    let banner = e.currentTarget.dataset.item;

    if (banner.externalLink) {
      let enterParams = JSON.stringify({
        'externalLink': encodeURIComponent(banner.externalLink)
      });
      // 外链
      wx.navigateTo({
        url: '/pages/webview/webview?enterFlag=externalLink&enterParams=' + enterParams,
      })
    } else {
      let enterParams = JSON.stringify({
        modelId: this.data.modelId,
        modelName: encodeURIComponent(encodeURIComponent(this.data.modelName)),
        materialId: this.data.materialId,
        type: banner.type,
        categoryId: banner.seriesId,
        url: banner.bannerUrl,
        comingFlag: "index",
      });
      wx.navigateTo({
        url: '/pages/webview/webview?enterFlag=themeDetail&enterParams=' + enterParams,
      })
    }
  },

  // 获取链接参数值
  getParam: function (url, name) {
    let value
    var arrParam = url.split("&");
    var arr = [];
    for (var i in arrParam) {
      let arr2 = arrParam[i].split("=");
      let arr3 = {
        name: arr2[0],
        value: arr2[1]
      }
      arr.push(arr3)
    }
    arr.forEach(item => {
      if (item.name == name) {
        value = item.value
        return
      }
    })
    return value
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    Toast.loading({
      duration: 0, // 持续展示 toast
      message: '加载中...',
      forbidClick: true,
      loadingType: 'spinner',
      selector: '#van-toast',
    });

    // 获取参数
    let exchangeId = 0;
    if (options.scene) {
      let scene = decodeURIComponent(options.scene);
      exchangeId = this.getParam(scene, 'exchangeId')
      this.setData({
        exchangeId: exchangeId,
        scene: scene
      })
    } else if (options.exchangeId) {
      exchangeId = options.exchangeId
      this.setData({
        exchangeId: exchangeId
      })
    }
    wx.setStorageSync('exchangeId', exchangeId);

    // 获取设备信息（手机型号）
    wx.getSystemInfo({
      success: (result) => {
        this.setData({
          mobile: result.model.split('<')[0]
        })
      },
    })
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
    app.getTenantInfo().then((res) => {
      let distributorId = wx.getStorageSync('distributorId') || 2601;
      this.setData({
        distributorId: distributorId
      })
      let exchangeId = wx.getStorageSync('exchangeId') || 0;
      this.initExchangeId(exchangeId); // 获取默认兑换卡id
      this.getBannerList(); // 获取轮播图
      this.getNoticeList(); // 获取通知

      let userId = wx.getStorageSync('userId');
      if (userId) {
        this.getCardNum(userId);
      }
    })
  },

  /**
   * 生命周期函数--当前是 tab 页时，点击 tab 时触发
   */
  onTabItemTap(e) {
    Toast.clear();
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
  onShareAppMessage: function () {

  }
})