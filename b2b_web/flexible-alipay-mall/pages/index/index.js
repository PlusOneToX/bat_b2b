// Api
import $request from '/assets/api/request'
import api from '/assets/api/allApi'

Page({
  data: {
    distributorId: "", // 分销商id
    platform: "", // 平台
    materialId: 82, // 材质id，默认
    modelId: "", // 型号id
    modelName: "", // 型号名称
    brandId: "", // 品牌id
    brandName: "", // 品牌名称
    modelList: [], // 型号列表
    showModelDialog: true, // 选择型号弹窗
    mobile: "", // 手机型号
    curMobile: "", // 当前手机型号
    curIndex: 0, // 默认展示第一项
    curChooseModel: {}, // 当前选中机型
    clickedTab: false, // 是否点击tab
    bannerList: [], // 轮播图列表
    curTab: 0, // 推荐/系列tab，默认0（推荐）
    tabInitTop: 0, // 推荐/系列初始化top值
    hasFixed: false, // 推荐/系列是否吸顶
    seriesList: [], // 系列列表
    recommendPage: 0, // 推荐当前分页
    recommendCount: 8, // 推荐分页条数
    recommendTotal: 0, // 推荐总数
    recommendData: [], // 推荐数据
    seriesPage: 0, // 系列当前分页
    seriesCount: 8, // 系列分页条数
    seriesTotal: 0, // 系列总数
    seriesData: [], // 系列数据
    scrollHeight: 0, // 高度
    // loading
    showLoading: false,
    message: "数据加载中",


    //优惠券信息
    curTabCou: 1, // 当前默认优惠券状态，待领取
    couponPage: 1,  // 优惠券页数
    couponSize: 8, // 优惠券数量

    couponBtnImg: 'https://flexible-resources.oss-cn-shenzhen.aliyuncs.com/zhifubao/home_coupon_all.png',
    couponBtnImgAll: 'https://flexible-resources.oss-cn-shenzhen.aliyuncs.com/zhifubao/home_coupon_all.png',
    couponBtnPhotoImg: '/assets/images/home_custom_photo.png',
    couponBtnImgGet: 'https://flexible-resources.oss-cn-shenzhen.aliyuncs.com/zhifubao/home_custom_get.gif',
    couponBtnImgUse: 'https://flexible-resources.oss-cn-shenzhen.aliyuncs.com/zhifubao/home_custom_use.gif',
    couponBtnImgLogin: 'https://flexible-resources.oss-cn-shenzhen.aliyuncs.com/zhifubao/home_custom_login.gif',

    showFlexible: false //柔性关闭弹窗显示
  },

  // 获取型号
  getModel() {
    $request
      .get(api.getModelList, {
        categoryId: 1, // 产品类型id：1 手机壳
        distributorId: this.data.distributorId,
        materialId: this.data.materialId,
        pictureId: 0,
        platform: this.data.platform, // 平台
      })
      .then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            this.setData({
              modelList: res.data
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

            // 获取推荐数据
            this.getRecommendData(1);
            // 获取首页系列
            this.getSeriesList();
          }
        } else {
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });

          this.setData({
            showLoading: false, // loading
          });
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
                my.setStorageSync({
                  key: "modelId",
                  data: model.modelId
                });
                my.setStorageSync({
                  key: "modelName",
                  data: model.name
                });
                // 缓存机型品牌id、品牌名称
                my.setStorageSync({
                  key: "brandId",
                  data: brand.modelId
                });
                my.setStorageSync({
                  key: "brandName",
                  data: brand.name
                });

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
        this.setData({
          showLoading: false, // loading
        })
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
            my.setStorageSync({
              key: "modelId",
              data: model.modelId
            });
            my.setStorageSync({
              key: "modelName",
              data: model.name
            });
            // 缓存机型品牌id、品牌名称
            my.setStorageSync({
              key: "brandId",
              data: brand.modelId
            });
            my.setStorageSync({
              key: "brandName",
              data: brand.name
            });

            // 获取推荐数据
            this.getRecommendData(1);
            // 获取首页系列
            this.getSeriesList();
          }
        });
      }
    });
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
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });

          this.setData({
            showLoading: false, // loading
          })
        }
      });
  },

  // 获取优惠券列表
  getCouponList(status) {
    var that = this;
    console.log("getCouponList");
    // 优惠券状态：1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
    $request.get(api.getCouponList, {
      page: that.data.couponPage,
      size: that.data.couponSize,
      statuss: status,
    }).then((res) => {
      console.log(res);
      if (res.success) {
        console.log(res.data.list.length);
        if (res.data.list.length >= 1) {
          //用户未领取的平台已发布的优惠券大于0
          console.log("显示【点击领取】动图");
          that.setData({
            //显示【点击领取】动图
            couponBtnImg: that.data.couponBtnImgGet,
            curTabCou: 1
          });
        } else {
          //查询用户是否存在未使用的优惠券
          $request.get(api.getCouponList, {
            page: that.data.couponPage,
            size: that.data.couponSize,
            statuss: 3,
          }).then((res_) => {
            console.log(res_);
            if (res_.success) {
              console.log(res_.data.list.length);
              if (res_.data.list.length >= 1) {
                //用户未使用的优惠券大于0
                console.log("显示【立即使用】动图");
                that.setData({
                  //显示【立即使用】动图
                  couponBtnImg: that.data.couponBtnImgUse,
                  curTabCou: 3
                });
              } else {
                console.log("显示【优惠券】静态图");
                that.setData({
                  //显示【优惠券】静态图
                  couponBtnImg: that.data.couponBtnImgAll,
                  curTabCou: 1
                });
              }
            }
          });
        }
      } else {
        console.log("用户登录信息过期、新用户，显示【登录领取】动图");
        that.setData({
          //显示【登录领取】动图
          couponBtnImg: that.data.couponBtnImgLogin,
          curTabCou: 1
        });
      }
    });
  },

  // 跳转图库详情
  goDetailList(e) {
    let banner = e.currentTarget.dataset.item;

    if (banner.externalLink) {
      let enterParams = JSON.stringify({
        'externalLink': encodeURIComponent(banner.externalLink)
      });
      // 外链
      my.navigateTo({
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
      my.navigateTo({
        url: '/pages/webview/webview?enterFlag=themeDetail&enterParams=' + enterParams,
      })
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

    my.navigateTo({
      url: '/pages/webview/webview?enterFlag=details&enterParams=' + enterParams,
    })
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
            id: 0,
          });
          this.setData({
            seriesList: seriesList
          })
        } else {
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });

          this.setData({
            showLoading: false, // loading
          })
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
            let H = my.getSystemInfoSync().screenHeight
            this.setData({
              scrollHeight: H
            })
          } else {
            this.setData({
              recommendData: [],
              recommendTotal: 0
            })
          }
        } else {
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });
        }

        this.setData({
          showLoading: false, // loading
        })
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
          } else {
            this.setData({
              seriesData: [],
              seriesTotal: 0
            })
          }
        } else {
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });
        }

        this.setData({
          showLoading: false, // loading
        })
      });
  },

  // 推荐/系列
  handleClickSeries(e) {
    let seriesId = e.currentTarget.dataset.id; // 获取id
    this.setData({
      curTab: seriesId
    })

    if (this.data.hasFixed) {
      // tab 固定时，切换 tab 内容从顶部开始
      my.pageScrollTo({
        scrollTop: this.data.tabInitTop,
        duration: 0
      });
    }

    // 判断是否点击的是系列
    if (seriesId) {
      // 系列
      this.setData({
        curTab: seriesId,
        seriesPage: 1,
      })
      this.setData({
        showLoading: true, // loading
      })
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

  // 传图定制
  goCustom() {
    let enterParams = JSON.stringify({
      materialId: this.data.materialId,
      modelId: this.data.modelId,
      modelName: encodeURIComponent(encodeURIComponent(this.data.modelName)),
    });

    my.navigateTo({
      url: "/pages/webview/webview?enterFlag=diyCustom&enterParams=" + enterParams,
    });
  },

  // 优惠券
  goCoupon(e) {

    var curTabCou = e.currentTarget.dataset.curTabCou;
    console.log("curTabCou: " + curTabCou);
    // 1-待领取 3-未使用
    my.navigateTo({
      url: '/pages/coupon/coupon?curTab=' + curTabCou
    });

  },

  // 第三方系统登录，token 失效重新获取 token
  loginOpenId() {
    my.removeStorageSync("auth");
    let openId = my.getStorageSync({ key: 'openid' }).data;
    if (openId) {
      $request
        .post(api.thirdpartyLogin, {
          openId: openId,
        })
        .then((res) => {
          if (res.success) {
            if(localStorage != undefined){
               localStorage.setItem("userId", res.data.id);
               localStorage.setItem("userNo", res.data.no);
               localStorage.setItem("phone", res.data.phone);
            }
          }
        });
    }
  },

  onLoad: function () {
    // 获取设备信息（手机型号）
    my.getSystemInfo({
      success: (result) => {
        this.setData({
          mobile: result.model.split('<')[0]
        })
      },
    })

    // 第三方系统登录，token 失效重新获取 token
    this.loginOpenId();
  },

  onShow: function () {

    let distributorId = my.getStorageSync({ key: 'distributorId' }).data;
    let platform = my.getStorageSync({ key: 'platform' }).data;

    // 缓存默认分销商，平台信息
    my.setStorageSync({
      key: "distributorId",
      data: distributorId
    });
    my.setStorageSync({
      key: "platform",
      data: platform
    });

    this.setData({
      showLoading: true, // loading
      distributorId: distributorId,
      platform: platform
    });

    this.getModel(); // 获取型号

    this.getBannerList(); // 获取轮播图

    console.log("获取优惠券列表");
    this.getCouponList(1); //获取优惠券列表      

    if (this.data.tabInitTop == 0) {
      my.createSelectorQuery().select('#recommendWrap').boundingClientRect()
        .exec((rect) => {
          if (rect[0] && rect[0].top > 0) {
            var tabInitTop = parseInt(rect[0].top);
            this.setData({
              tabInitTop: tabInitTop
            })
          }
        });
    }
  },

  onTabItemTap(e) {
    this.setData({
      showLoading: false,
    })
  },

  // 页面滚动触发事件的处理函数
  onPageScroll: function (e) {
    // 判断页面滚动值是否大于等于推荐/系列top值
    if (e.scrollTop >= this.data.tabInitTop) {
      this.setData({
        hasFixed: true
      })
    } else {
      this.setData({
        hasFixed: false
      })
    }
  },

  // 页面上拉触底事件的处理函数
  onReachBottom: function () {
    // 上拉加载
    this.loadMore();
  },

  //关闭柔性弹窗
  clickFlexible: function(){
    this.setData({
      showFlexible: false
    });
  }
})