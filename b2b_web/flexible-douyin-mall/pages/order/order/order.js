const app = getApp();
// Api
import $request from '../../../assets/api/request'
import api from '../../../assets/api/allApi'

Page({
  data: {
    distributorId: '', // 分销商id
    userId: '', // 用户id
    addrTotal: 0, // 地址总数
    addrDetail: {
      userName: '',
      phone: '',
      address: '',
      detail: '',
      provinceId: '',
      cityId: '',
      districtId: '',
      provinceName: '',
      cityName: '',
      districtName: '',
      defaultFlag: '',
      distributionMoney: 0,
    }, // 地址详情
    addrId: "", // 地址ID
    curAddrId: '', // 当前选择地址id
    goods: [], // 订单商品
    mGoods: [], // 订单拆单商品
    itemPrice: 0, // 商品总计
    totalPrice: 0, // 合计
    goodItems: [], // 商品列表（获取配送方式时传参）
    orderIds: [], // 商品id
    orderItems: [], // 商品列表
    checked: true, // 同意协议
    isDisable: false, // 按钮防止重复点击
    showMask: false, // 蒙版
    message: "", // 订单备注
    fadeProtocol: false, // 协议显示
    couponFlag: false, // 商品优惠券
    exchange: "", // 兑换码编号或优惠券
    couponMethod: '', // 优惠券类型
    salePrice: 0, // 优惠金额（减免/折扣）
    deliveryPrice: 0, // 配送费
    saleValue: "", // 商品优惠
    saleDiscount: 1, // 折扣
    disItemPrice: 0, // 已优惠货品价格
    goodsEnable: "", // 商品是否能用：1 能用，0 不能用
    amountEnable: "", // 金额是否到达要求：1 达到，0 未达到
    couponItems: "", // 商品列表（获取优惠券）
    // 优惠券（弹窗选择）
    showCoupon: false, // 是否显示优惠券弹窗
    couponTabs: [{
      id: 1,
      title: "待领取",
      value: "立即领取",
    },
    {
      id: 3,
      title: "未使用",
      value: "未使用",
    }],
    curTab: 3, // 当前默认优惠券状态，未使用
    couponList: [], // 优惠券列表
    couponTotal: 0, // 优惠券总数
    couponPage: 1,  // 优惠券页数
    couponSize: 8, // 优惠券数量
    // loading
    showLoading: false,
    loadingMsg: "数据加载中",
  },

  // 获取地址信息
  getAddress() {
    if (this.data.userId) {
      $request
        .get(api.getAddrList, {
          id: this.data.userId,
          page: 1,
          size: 10,
        })
        .then((res) => {
          if (res.success) {
            this.setData({
              addrTotal: 0
            })
            if (res.data.list && res.data.list.length > 0) {
              this.setData({
                addrTotal: res.data.total
              })
              let arr = res.data.list;
              let isDef = false;
              if (this.data.addrId) {
                arr.forEach((item) => {
                  if (this.data.addrId == item.id) {
                    this.setAddrDetail(item);
                    isDef = true;
                  }
                });

                arr.forEach((item) => {
                  if (!isDef && item.defaultFlag === 1) {
                    this.setAddrDetail(item);
                    isDef = true;
                  }
                  if (this.data.addrTotal > 0 && !isDef) {
                    arr.forEach((item, index) => {
                      if (index === 0) {
                        this.setAddrDetail(item);
                      }
                    });
                  }
                })
              } else {
                arr.forEach((item) => {
                  if (item.defaultFlag === 1) {
                    this.setAddrDetail(item);
                    isDef = true;
                  }
                });
                if (this.data.addrTotal > 0 && !isDef) {
                  arr.forEach((item, index) => {
                    if (index === 0) {
                      this.setAddrDetail(item);
                    }
                  });
                }
              }

              this.data.addrDetail.distributionMoney = 0;
            } else {
              this.getOrderInfo(); // 获取订单信息
            }
          } else {
            tt.showToast({
              title: res.errMessage,
              icon: "none",
              duration: 2000,
            });

            this.setData({
              showLoading: false, // loading
            });
          }
        });
    }
  },
  // 设置地址信息
  setAddrDetail(item) {
    this.setData({
      addrDetail: {
        userName: item.userName,
        phone: item.phone,
        address: item.provinceName + item.cityName + item.districtName,
        detail: item.address,
        provinceId: item.provinceId,
        cityId: item.cityId,
        districtId: item.districtId,
        provinceName: item.provinceName,
        cityName: item.cityName,
        districtName: item.districtName,
        defaultFlag: item.defaultFlag,
      },
      curAddrId: item.id,
    })

    this.getOrderInfo(); // 获取订单信息
  },

  // 获取订单信息
  getOrderInfo() {
    // 获取订单信息
    let goods = tt.getStorageSync('goods') ? JSON.parse(tt.getStorageSync('goods')) : "";
    if (goods) {
      goods = goods.sort(this.dataUp);
      // 根据工厂分组
      let mGoods = this.arrayGroupBy(goods, "manufactors");
      mGoods.forEach((item, index) => {
        // 分别计算每组的价格
        let price = 0;
        let manufactors = "";
        item.forEach((pro) => {
          price += pro.salePrice * pro.itemCount;
          manufactors = pro.diy.manufactors || "";
        });
        item.orderPrice = price;
        item.manufactors = manufactors;

        // 物流费
        item.deliveryPrice = 0;
        if (this.data.addrTotal > 0) {
          this.getDelivery(manufactors, price, index);
        }
      });

      let itemPrice = 0; // 商品总计
      let totalPrice = 0; // 合计
      let goodItems = []; // 商品列表（获取配送方式时传参）
      let orderIds = []; // 商品id
      let orderItems = []; // 商品列表
      let couponItems = []; // 优惠券传参数组
      goods.forEach((item, index) => {
        item.itemMtoCount = 0; // 预售数量
        item.itemInCount = item.itemCount; // 在库数量（实际商品数量）
        item.itemOnWayCount = 0; // 在途数量
        item.mtoType = 0; // 是否预售：1 是，0 否
        item.oversoldFlag = 0; // 是否支持超卖：1 支持，0 不支持

        // 计算商品总价
        itemPrice += item.salePrice * item.itemCount;

        // 合计
        totalPrice = itemPrice;

        // 商品列表（获取配送方式时传参）
        goodItems.push({
          itemCode: item.itemCode,
          itemCount: item.itemCount,
        });

        // 保存商品id
        orderIds.push(item.id);

        // 保存商品列表
        orderItems.push({
          modelId: item.diy.modelId,
          brandId: item.diy.brandId,
          materialId: item.diy.materialId,
          modelName: item.diy.modelName,
          brandName: item.diy.brandName,
          materialName: item.diy.materialName,
          pictureName: item.diy.pictureName,
          pictureId: item.diy.pictureId,
          previewImage: item.diy.previewImage,
          generateImage: item.diy.generateImage,
          itemName: item.itemName,
          itemCode: item.itemCode,
          salePrice: item.salePrice,
          itemCount: item.itemCount,
          manufactors: item.diy.manufactors,
        });

        // 优惠券传参数组
        couponItems.push({
          price: item.salePrice,
          count: item.itemCount,
          modelId: item.diy.modelId,
          materialId: item.diy.materialId,
        });
      });

      // 根据商品列表获取最优优惠券
      this.getOptimalCoupon(couponItems);

      // 更新数据
      this.setData({
        goods: goods,
        mGoods: mGoods,
        itemPrice: Number(itemPrice).toFixed(2),
        totalPrice: Number(totalPrice).toFixed(2),
        goodItems: goodItems,
        orderIds: orderIds,
        orderItems: orderItems,
        couponItems: couponItems,
      })
    }
  },

  // 根据商品列表获取最优优惠券
  getOptimalCoupon(couponItems) {
    $request
      .post(api.getOptimalCoupon, {
        goodss: couponItems,
      })
      .then((res) => {
        if (res.success) {
          if (res.data) {
            // 兑换邮费
            this.exchangDelivery(res.data);
            this.getCouponInfo(res.data);
            this.setData({
              goodsEnable: 1,
              amountEnable: 1,
              deliveryPrice: res.data.deliveryFee,
              couponFlag: true, // 有可用优惠券
            })
          } else {
            let itemPrice = 0; // 商品总计
            this.data.goods.forEach((good) => {
              itemPrice += good.salePrice * good.itemCount;
            });
            this.setData({
              itemPrice: itemPrice, // 商品总计
              couponFlag: false, // 无可用优惠券
            })

            // 计算价格
            this.calucatePrice();
          }
        } else {
          tt.showToast({
            title: res.errMessage,
            icon: "none",
            duration: 2000,
          });
        }

        this.setData({
          showLoading: false, // loading
        });
      });
  },

  exchangDelivery(data) {
    let flag = false;
    this.data.mGoods.forEach((item) => {
      let deliveryPrice = 0;
      let price = 0;
      item.forEach((pro) => {
        // 物流费
        if (
          data &&
          data.deliveryFeeFlag &&
          data.deliveryFeeFlag === 1 &&
          this.data.goodsEnable &&
          this.data.amountEnable &&
          !flag
        ) {
          flag = true;
          deliveryPrice = this.data.deliveryPrice;
          this.data.addrDetail.distributionMoney = deliveryPrice;
        }
        price += pro.salePrice * pro.itemCount;
      });
      price = price + deliveryPrice;

      this.data.deliveryPrice = deliveryPrice;
      this.data.addrDetail.distributionMoney = deliveryPrice;
      item.deliveryPrice = deliveryPrice;
      item.orderPrice = price;
    });

    this.setData({
      deliveryPrice: this.data.deliveryPrice,
      addrDetail: this.data.addrDetail,
      mGoods: this.data.mGoods,
    })
  },

  // 计算价格
  calucatePrice() {
    let price =
      this.data.itemPrice -
      this.data.disItemPrice +
      this.data.addrDetail.distributionMoney -
      this.data.salePrice;

    let totalPrice = price.toFixed(2) > 0 ? price.toFixed(2) : 0;

    this.setData({
      totalPrice: totalPrice
    })
  },

  // 获取优惠券数据
  getCouponInfo(item) {
    if (item) {
      this.setData({
        exchange: item.couponNo,
        couponMethod: item.couponMethod,
      })
      if (item.couponMethod === 1) {
        // 满减
        let saleDiscount = 1;
        let salePrice = Number(item.reduction);
        let saleValue = "-¥" + salePrice;
        this.data.addrDetail.distributionMoney = 0;
        this.setData({
          saleDiscount: saleDiscount,
          salePrice: salePrice,
          saleValue: saleValue,
          addrDetail: this.data.addrDetail
        })
        this.calcCoupon(item);
      } else if (item.couponMethod === 2) {
        // 折扣
        let saleDiscount = Number(item.discount) / 100;
        let salePrice = 0;
        let saleValue = saleDiscount * 10 + "折";
        this.data.addrDetail.distributionMoney = 0;
        this.setData({
          saleDiscount: saleDiscount,
          salePrice: salePrice,
          saleValue: saleValue,
          addrDetail: this.data.addrDetail
        })
        this.calcCoupon(item);
      } else {
        // 商品兑换
        this.setData({
          saleDiscount: 1,
          disItemPrice: 0, // 已优惠货品价格
        })
        // 开启物流费
        let flag = true;
        let itemPrice = 0; // 商品总计
        this.data.goods.forEach((good) => {
          good.couponNo = ""; // 清空优惠券
          itemPrice += good.salePrice * good.itemCount;
          if (flag) {
            if (Number(item.materialScope) === 2) {
              // 指定材质
              item.materialIds.forEach((material) => {
                if (good.diy.materialId === material) {
                  if (Number(item.modelScope) === 2) {
                    // 同时指定型号
                    item.modelIds.forEach((model) => {
                      if (good.diy.modelId === model) {
                        flag = false;
                        this.setData({
                          salePrice: good.salePrice,
                          saleValue: "-¥ " + good.salePrice,
                        })
                        good.couponNo = this.data.exchange; // 优惠券id
                      }
                    });
                  } else {
                    flag = false;
                    this.setData({
                      salePrice: good.salePrice,
                      saleValue: "-¥ " + good.salePrice,
                    })
                    good.couponNo = this.data.exchange; // 优惠券id
                  }
                }
              });
            } else {
              let randomPrice = 0;
              let curItem = {};
              if (Number(good.salePrice) >= Number(item.orderMoney)) {
                flag = false;
                randomPrice = good.salePrice;
                curItem = good;
              } else {
                randomPrice = this.data.orderItems[0].salePrice;
                curItem = this.data.orderItems[0];
              }
              this.setData({
                salePrice: randomPrice,
                saleValue: "-¥ " + randomPrice,
              })
              curItem.couponNo = this.data.exchange; // 优惠券id
            }
          }
        });

        this.setData({
          itemPrice: itemPrice,
          goods: this.data.goods,
          orderItems: this.data.orderItems,
        })
      }
    }
    this.calucatePrice();
  },

  calcCoupon(item) {
    let disItemPrice = 0; // 已优惠货品价格
    let itemPrice = 0; // 商品总计
    this.data.goods.forEach((good) => {
      good.couponNo = ""; // 清空优惠券

      if (Number(item.materialScope) === 2) {
        itemPrice += good.salePrice * good.itemCount; // 货品总价格
        // 指定材质
        item.materialIds.forEach((material) => {
          if (good.diy.materialId === material) {
            if (Number(item.modelScope) === 2) {
              // 同时指定型号
              item.modelIds.forEach((model) => {
                if (good.diy.modelId === model) {
                  good.couponNo = this.data.exchange; // 优惠券id

                  disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
                }
              });
            } else {
              good.couponNo = this.data.exchange; // 优惠券id

              disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
            }
          }
        });
      } else if (Number(item.modelScope) === 2) {
        itemPrice += good.salePrice * good.itemCount; // 货品总价格
        // 指定型号
        item.modelIds.forEach((model) => {
          if (good.diy.modelId === model) {
            if (Number(item.materialScope) === 2) {
              // 同时指定材质
              item.materialIds.forEach((material) => {
                if (good.diy.materialId === material) {
                  good.couponNo = this.data.exchange; // 优惠券id

                  disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
                }
              });
            } else {
              good.couponNo = this.data.exchange; // 优惠券id

              disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
            }
          }
        });
      } else {
        // 未指定，货品总优惠
        good.couponNo = this.data.exchange; // 优惠券id

        itemPrice += good.salePrice * good.itemCount * this.data.saleDiscount; // 货品总价格
      }
    });

    disItemPrice =
      this.data.disItemPrice - this.data.disItemPrice * this.data.saleDiscount;

    this.setData({
      disItemPrice: disItemPrice,
      itemPrice: itemPrice,
      goods: this.data.goods,
    })
  },

  // 打开商品优惠
  openCoupon() {
    this.getOrderCouponList(this.data.curTab);
    this.setData({
      showCoupon: true, // 显示优惠券弹窗
    })
  },
  // 根据商品列表获取优惠券列表
  getOrderCouponList(status) {
    $request
      .post(api.getGoodsCoupn, {
        page: this.data.couponPage,
        size: this.data.couponSize,
        statuss: status,
        goodss: this.data.couponItems,
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
            couponTotal: res.data.total,
            couponList: couponList,
          })
        } else {
          tt.showToast({
            title: res.errMessage,
            icon: "none",
            duration: 2000,
          });
        }
      });
  },
  // 上拉加载
  loadMore() {
    if (this.data.couponList.length < this.data.couponTotal) {
      let couponPage = this.data.couponPage + 1
      this.setData({
        couponPage: couponPage,
      })

      this.getOrderCouponList(this.data.curTab);
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
    this.getOrderCouponList(curTab);
  },
  // 选择优惠券
  handleCouponItem(e) {
    let item = e.detail;
    this.exchangDelivery(item);
    this.getCouponInfo(item);
    this.setData({
      goodsEnable: item.goodsEnable,
      amountEnable: item.amountEnable,
      deliveryPrice: item.deliveryFee,
      showCoupon: false, // 隐藏优惠券弹窗
      couponFlag: true, // 使用优惠券
    })
  },
  // 优惠券 - 不使用优惠券
  cancelCoupon() {
    // 重置货品总价格、已优惠货品价格
    let itemPrice = 0;
    this.data.goods.forEach((good) => {
      itemPrice += good.salePrice * good.itemCount;

      good.couponNo = ""; // 清空优惠券
    });

    this.setData({
      itemPrice: itemPrice,
      disItemPrice: 0,
      goods: this.data.goods,
      salePrice: 0,
      saleDiscount: 1,
      addForm: this.data.addForm,
      deliveryPrice: 0,
      goodsEnable: "",
      amountEnable: "",
      exchange: "",
      couponFlag: false, // 无可用优惠券
      showCoupon: false, // 隐藏优惠券弹窗
    })

    this.exchangDelivery(null);
    this.calucatePrice();
  },

  // 地址管理
  handleAddress() {
    let enterParams = JSON.stringify({
      orderFlag: "order",
      addrId: this.data.curAddrId,
    });
    tt.redirectTo({
      url: '/pages/webview/webview?enterFlag=address&enterParams=' + enterParams,
    })
  },

  // 查询该地址是否停发
  getDeliveryStopStatus() {
    if (this.data.addrTotal <= 0) {
      tt.showToast({
        title: "请选择收货地址",
        icon: "none",
        duration: 2000,
      });
      this.handleAddress();
      return;
    }

    this.setData({
      isDisable: true
    })

    let content = this.data.addrDetail.address + this.data.addrDetail.detail;
    $request.get(api.findMatchDeliveryStop, {
      content: content,
    }).then((res) => {
      if (res.success) {
        if (res.data) {
          // 匹配，弹窗提示用户该地址停发
          let that = this;
          tt.showModal({
            title: "温馨提示",
            content: "尊敬的用户你好，该地区可能是疫情停发区，发货可能会有影响，可正常提交订单待疫情解封可恢复发货，不便之处敬请谅解。",
            confirmText: "确认提交",
            cancelText: "我再想想",
            confirmColor: "#333333",
            cancelColor: "#999999",
            success(res) {
              if (res.confirm) {
                // 确定按钮
                that.submitOrder();
              } else {
                // 取消按钮
                that.setData({
                  isDisable: false
                })
              }
            }
          });
        } else {
          // 不匹配，直接下单
          // 支付订单
          this.submitOrder();
        }
      } else {
        tt.showToast({
          title: res.errMessage,
          icon: "none",
          duration: 2000,
        });

        this.setData({
          isDisable: false
        })
      }
    });
  },

  // 支付订单
  submitOrder() {
    // 收货地址信息
    let delivery = {
      address: this.data.addrDetail.detail,
      cityId: this.data.addrDetail.cityId,
      cityName: this.data.addrDetail.cityName,
      countryId: 37,
      countryName: "中国",
      districtId: this.data.addrDetail.districtId,
      districtName: this.data.addrDetail.districtName,
      provinceId: this.data.addrDetail.provinceId,
      provinceName: this.data.addrDetail.provinceName,
      mobile: this.data.addrDetail.phone,
      userName: this.data.addrDetail.userName,
    };
    // 物流费用(总费用)
    let distributionAmount = 0;
    // 商品结算金额(优惠后总金额)
    let orderAmount = Number(this.data.totalPrice);
    // 配送方式列表
    let logisticss = [];
    this.data.mGoods.forEach((item) => {
      logisticss.push(item.orderDelivery);
    });

    this.setData({
      goods: this.data.goods,
      mGoods: this.data.mGoods,
      orderItems: this.data.orderItems,
    })

    let shareFlag = 0;
    // 判断缓存是否已点击分享事件
    let hasShared = tt.getStorageSync('hasShared');
    if (hasShared) {
      shareFlag = 1;
    }

    let info = {
      delivery: delivery,
      distributionAmount: distributionAmount,
      orderAmount: orderAmount + distributionAmount,
      goodss: this.data.goods,
      logisticss: logisticss,
      payWay: 10, // 付款方式：1 支付宝，2 微信，10 字节
      remark: this.data.message,
      invoiceFlag: 0, // 是否开具发票：1 是，0 否
      shareFlag: shareFlag, // 是否分享：1 是，0 否
    };

    $request.post(api.addOrder, info).then((res) => {
      if (res.success) {
        // 订单编号
        let ids = res.data.ids.toString();

        let hasPrice = false;
        // 判断总价大于0
        if (this.data.totalPrice > 0) {
          hasPrice = true
        }
        if (hasPrice) {
          // 大于0，调用支付
          this.payMent(ids);
        } else {
          tt.removeStorageSync('hasShared'); // 清除缓存已点击分享事件
          // 跳转兑换成功
          tt.redirectTo({
            url: "/pages/order/result/result?enterFlag=orderList",
          });
        }

        // 删除购物车数据
        this.delShopData(this.data.orderIds);
      } else {
        this.setData({
          isDisable: false
        })

        tt.showToast({
          title: res.errMessage,
          icon: "none",
          duration: 2000,
        });
      }
    })
      .catch((error) => {
        console.log("error");
      });
  },
  getArrByStr(str) {
    var string = str.toUpperCase();
    var temp = string.split(/[\n\s+,，]/g);
    for (var i = 0; i < temp.length; i++) {
      if (temp[i] == "") {
        // 删除数组中空值
        temp.splice(i, 1);
        i--;
      }
    }
    return temp;
  },
  // 订单支付
  payMent(id) {
    let openid = tt.getStorageSync("openid");

    let appId = app.globalData.appId;

    let info = {
      businessType: 1, // 业务类型：1 订单收款
      customerFlag: 1, // 客户标志：1 C端客户
      payMethod: "toutiao", // 交易方式
      orderId: id,
      payerId: this.data.userId,
      platformUserId: openid,
      appId: appId,
    };

    $request.post(api.handlePayment, info).then((res) => {
      if (res.success) {
        let touTiaoResp = res.data.touTiaoResp.data;

        var enterParams = JSON.stringify({
          orderId: touTiaoResp.orderId,
          orderToken: touTiaoResp.orderToken.replace(/\=/g, "123456789"), // 处理等号（等号去小程序解析会报错）
          // orderNo: wxConfig.outTradeNo,
          orderPrice: this.data.totalPrice,
        });
        tt.removeStorageSync('hasShared'); // 清除缓存已点击分享事件
        tt.redirectTo({
          url: "/pages/order/payment/payment?enterFlag=payment&enterParams=" +
            enterParams,
        });
      } else {
        tt.showToast({
          title: res.errMessage,
          icon: "none",
          duration: 2000,
        });

        let enterParams = JSON.stringify({
          sid: 1
        });
        tt.redirectTo({
          url: "/pages/webview/webview?enterFlag=orderList&enterParams=" + enterParams,
        });
      }
    });
  },

  // 删除购物车数据
  delShopData(ids) {
    $request
      .remove(api.deleteShopcart, {
        ids: ids,
      })
      .then((res) => {
        if (res.success) {
          // 删除缓存数据
          tt.removeStorageSync("goods")
        } else {
          tt.showToast({
            title: res.errMessage,
            icon: "none",
            duration: 2000,
          });
        }
      })
      .catch(() => { });
  },

  // 根据工厂获取配送方式
  getDelivery(manufactor, price, index) {
    $request.post(api.getCalculations, {
      distributorId: this.data.distributorId,
      billingMethod: 1, // 订单结算方式：1重量计费，2体积计费
      countryId: 37, // 国家id
      provinceId: this.data.addrDetail.provinceId,
      cityId: this.data.addrDetail.cityId,
      useRange: 2, // 适用范围：1.普通商品，2.定制商品 3 普通商品和定制商品
      price: price,
      manufactors: manufactor,
      weight: 0,
    })
      .then((res) => {
        if (res.success && res.data && res.data.length > 0) {
          let logisticId = res.data[0].id;

          if (logisticId) {
            this.getLogisticDetail(logisticId, index);
          }
        } else {
          tt.showToast({
            title: res.errMessage,
            icon: "none",
            duration: 2000,
          });

          this.setData({
            showLoading: false, // loading
          });
        }
      });
  },
  // 查询单个配送
  getLogisticDetail(id, index) {
    $request.get(api.getLogistics, {
      id: id,
    })
      .then((res) => {
        if (res.success && res.data) {
          let delivery = res.data;
          let orderDelivery = {
            logisticsId: res.data.id,
            logisticsName: res.data.name,
            logisticsType: 2, // 配送方式类型：1 普通商品（标品） 2 定制工厂
            manufactors: res.data.manufactors
          }

          this.data.mGoods[index].delivery = delivery;
          this.data.mGoods[index].orderDelivery = orderDelivery;
          // 更新数据
          this.setData({
            mGoods: this.data.mGoods
          })
        } else {
          tt.showToast({
            title: res.errMessage,
            icon: "none",
            duration: 2000,
          });
        }

        this.setData({
          showLoading: false, // loading
        });
      });
  },

  // 排序（根据兑换卡是否可用）
  dataUp(x, y) {
    return Number(y.canSelect) - Number(x.canSelect);
  },

  // 分组函数
  groupBy(array, f) {
    let groups = {};
    array.forEach((o) => {
      var group = JSON.stringify(f(o));
      groups[group] = groups[group] || [];
      groups[group].push(o);
    });
    return Object.keys(groups).map((group) => {
      return groups[group];
    });
  },
  arrayGroupBy(list, groupId) {
    let sorted = this.groupBy(list, (item) => {
      return [item.diy[groupId]];
    });
    return sorted;
  },

  // 同意协议
  handleCheck() {
    this.setData({
      checked: !this.data.checked,
      isDisable: this.data.checked
    })
  },

  // 关闭蒙版
  closeMask() {
    this.setData({
      showCoupon: false,
    })
  },

  getArrByStr(str) {
    if (str) {
      var string = str.toUpperCase();
      var temp = string.split(/[\n\s+,，]/g);
      for (var i = 0; i < temp.length; i++) {
        if (temp[i] == "") {
          // 删除数组中空值
          temp.splice(i, 1);
          i--;
        }
      }
      return temp;
    }
  },

  // 协议弹框
  openProtocol() {
    this.setData({
      fadeProtocol: true,
    })
  },

  // 同意协议
  handleAgree() {
    this.setData({
      fadeProtocol: false,
      checked: true,
      isDisable: false,
    })
  },

  onLoad: function (options) {
    tt.removeStorageSync('hasShared'); // 清除缓存已点击分享事件
    if (options && options.addrId) {
      this.setData({
        addrId: Number(options.addrId)
      })
    }

    let userId = tt.getStorageSync('userId'); // 用户id
    let distributorId = tt.getStorageSync('distributorId') || 2601; // 分销商id
    this.setData({
      showLoading: true, // loading
      distributorId: distributorId,
      userId: userId,
    })
  },

  onShow: function () {
    this.getAddress(); // 获取地址信息
  }
})