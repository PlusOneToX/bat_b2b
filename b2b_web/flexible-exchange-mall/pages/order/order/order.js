// pages/order/order/order.js
// Toast
import Toast from '../../../miniprogram_npm/@vant/weapp/toast/toast';
// Dialog
import Dialog from '../../../miniprogram_npm/@vant/weapp/dialog/dialog';
// Api
import $request from '../../../assets/api/request'
import api from '../../../assets/api/allApi'

Page({

  /**
   * 页面的初始数据
   */
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
    }, // 地址详情
    curAddrId: '', // 当前选择地址id
    goods: [], // 订单商品
    mGoods: [], // 订单拆单商品
    itemPrice: '', // 商品总价
    goodItems: [], // 商品列表（获取配送方式时传参）
    orderIds: [], // 商品id
    orderItems: [], // 商品列表
    checked: true, // 同意协议
    isDisable: false, // 按钮防止重复点击
    hasMailFee: false, // 是否收邮费（兑换）
    totalMailFee: 0, // 总运费
    hasChoosedCode: [], // 已选中兑换码列表
    curPOrder: 0, // 当前父操作订单（匹配兑换卡)
    curOrder: 0, // 当前操作订单（匹配兑换卡）
    cardListShow: false, // 兑换卡列表弹窗
    showMask: false, // 蒙版
    cardList: [], // 兑换卡列表
    needNum: 0, // 需要兑换卡数量
    choosedNum: 0, // 已选兑换卡数量
    message: "", // 订单备注
    fadeProtocol: false, // 协议显示
    exchangeShareData: {}, // 转发活动
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
              if (this.data.curAddrId) {
                arr.forEach((item) => {
                  if (this.data.curAddrId == item.id) {
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
                });
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
            Toast.fail(res.errMessage);
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

  // 获取订单信息
  getOrderInfo() {
    // 获取订单信息
    let goods = wx.getStorageSync('goods') ? JSON.parse(wx.getStorageSync('goods')) : "";
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

      let itemPrice = 0; // 商品总价
      let goodItems = []; // 商品列表（获取配送方式时传参）
      let orderIds = []; // 商品id
      let orderItems = []; // 商品列表
      goods.forEach((item, index) => {
        item.itemMtoCount = 0; // 预售数量
        item.itemInCount = item.itemCount; // 在库数量（实际商品数量）
        item.itemOnWayCount = 0; // 在途数量
        item.mtoType = 0; // 是否预售：1 是，0 否
        item.oversoldFlag = 0; // 是否支持超卖：1 支持，0 不支持

        // 计算商品总价
        itemPrice += item.salePrice * item.itemCount;

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
      });

      // 更新数据
      this.setData({
        goods: goods,
        mGoods: mGoods,
        itemPrice: itemPrice,
        goodItems: goodItems,
        orderIds: orderIds,
        orderItems: orderItems
      })

      // 获取兑换卡数据
      this.data.goods.forEach((item, index) => {
        this.getCardData(item, index);
      })
    }
  },

  // 地址管理
  handleAddress() {
    let enterParams = JSON.stringify({
      orderFlag: "order",
      addrId: this.data.curAddrId,
    });
    wx.navigateTo({
      url: '/pages/webview/webview?enterFlag=address&enterParams=' + enterParams,
    })
  },

  // 查询该地址是否停发
  getDeliveryStopStatus() {
    if (this.data.addrTotal <= 0) {
      Toast("请选择收货地址");
      this.handleAddress();
      return;
    }

    this.setData({
      isDisable: true
    })
    // 兑换码
    for (let i = 0; i < this.data.goods.length; i++) {
      if (
        this.data.goods[i].secretCodes === "" ||
        this.getArrByStr(this.data.goods[i].secretCodes).length !=
        this.data.goods[i].itemCount
      ) {
        this.setData({
          isDisable: false
        })
        Toast("兑换码数量与商品数量不匹配");
        this.goBindCard();
        return;
      }
      for (let j = 0; j < this.data.orderItems.length; j++) {
        if (this.data.goods[i].id === this.data.orderItems[j].mid) {
          let secretCodeList = this.getArrByStr(
            this.data.goods[i].secretCodes
          );
          this.data.orderItems[j].secretCodeList = secretCodeList;
        }
      }
    }

    let content = this.data.addrDetail.address + this.data.addrDetail.detail;
    $request.get(api.findMatchDeliveryStop, {
      content: content
    }).then((res) => {
      if (res.success) {
        if (res.data) {
          // 匹配，弹窗提示用户该地址停发
          Dialog.confirm({
            title: "温馨提示",
            message: "尊敬的用户你好，该地区可能是疫情停发区，发货可能会有影响，可正常提交订单待疫情解封可恢复发货，不便之处敬请谅解。",
            className: "pop-dialog",
            confirmButtonText: "确认提交",
            confirmButtonColor: "#333333",
            cancelButtonText: "我再想想",
            cancelButtonColor: "#999999",
          }).then(() => {
            // 支付订单
            this.submitOrder();
          }).catch(() => {
            this.setData({
              isDisable: false
            })
          });
        } else {
          // 不匹配，直接下单
          // 支付订单
          this.submitOrder();
        }
      } else {
        Toast.fail(res.errMessage);
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
    let distributionAmount = this.data.totalMailFee;
    // 商品结算金额(优惠后总金额)，兑换商品默认价格0
    let orderAmount = 0;
    // 配送方式列表
    let logisticss = [];
    this.data.mGoods.forEach((item) => {
      logisticss.push(item.orderDelivery);
    });

    // 兑换码
    this.data.goods.forEach((item) => {
      let codes = item.secretCodes.split(", ");
      item.codes = codes;
    });

    this.setData({
      goods: this.data.goods,
      mGoods: this.data.mGoods,
      orderItems: this.data.orderItems,
    })

    let shareFlag = 0;
    // 判断缓存是否已点击分享事件
    let hasShared = wx.getStorageSync('hasShared');
    if (hasShared) {
      shareFlag = 1;
    }

    // 根据兑换码收邮费模式拆单
    let curG = this.data.goods;
    let goodss = [];
    curG.forEach((cG) => {
      if (cG.selects && cG.selects.length > 0) {
        // 包邮（普通卡）
        let codes1 = {
          barCode: cG.barCode,
          codes: [],
          curNeedNum: cG.curNeedNum,
          diy: cG.diy,
          diyType: cG.diyType,
          goodsId: cG.goodsId,
          goodsName: cG.goodsName,
          goodsNo: cG.goodsNo,
          goodsType: cG.goodsType,
          height: cG.height,
          id: cG.id,
          imageUrl: cG.imageUrl,
          itemCode: cG.itemCode,
          itemCount: 0,
          itemId: cG.itemId,
          itemInCount: 0,
          itemMtoCount: cG.itemMtoCount,
          itemName: cG.itemName,
          itemOnWayCount: cG.itemOnWayCount,
          itemType: cG.itemType,
          length: cG.length,
          mailFee: 0,
          mtoType: cG.mtoType,
          openFlag: cG.openFlag,
          oversoldFlag: cG.oversoldFlag,
          salePrice: cG.salePrice,
          weight: cG.weight,
          width: cG.width,
        };
        // 收邮费（赠卡）
        let codes2 = {
          barCode: cG.barCode,
          codes: [],
          curNeedNum: cG.curNeedNum,
          diy: cG.diy,
          diyType: cG.diyType,
          goodsId: cG.goodsId,
          goodsName: cG.goodsName,
          goodsNo: cG.goodsNo,
          goodsType: cG.goodsType,
          height: cG.height,
          id: cG.id,
          imageUrl: cG.imageUrl,
          itemCode: cG.itemCode,
          itemCount: 0,
          itemId: cG.itemId,
          itemInCount: 0,
          itemMtoCount: cG.itemMtoCount,
          itemName: cG.itemName,
          itemOnWayCount: cG.itemOnWayCount,
          itemType: cG.itemType,
          length: cG.length,
          mailFee: 0,
          mtoType: cG.mtoType,
          openFlag: cG.openFlag,
          oversoldFlag: cG.oversoldFlag,
          salePrice: cG.salePrice,
          weight: cG.weight,
          width: cG.width,
        };
        // 收邮费（普通卡加收用户运费）
        let codes3 = {
          barCode: cG.barCode,
          codes: [],
          curNeedNum: cG.curNeedNum,
          diy: cG.diy,
          diyType: cG.diyType,
          goodsId: cG.goodsId,
          goodsName: cG.goodsName,
          goodsNo: cG.goodsNo,
          goodsType: cG.goodsType,
          height: cG.height,
          id: cG.id,
          imageUrl: cG.imageUrl,
          itemCode: cG.itemCode,
          itemCount: 0,
          itemId: cG.itemId,
          itemInCount: 0,
          itemMtoCount: cG.itemMtoCount,
          itemName: cG.itemName,
          itemOnWayCount: cG.itemOnWayCount,
          itemType: cG.itemType,
          length: cG.length,
          mailFee: 0,
          mtoType: cG.mtoType,
          openFlag: cG.openFlag,
          oversoldFlag: cG.oversoldFlag,
          salePrice: cG.salePrice,
          weight: cG.weight,
          width: cG.width,
        };
        cG.selects.forEach((cS) => {
          if (cS.mailSetting === 1) {
            codes1.codes.push(cS.secretCode);
            codes1.mailSetting = cS.mailSetting;
            codes1.itemCount += 1;
            codes1.itemInCount += 1;
          }
          if (cS.mailSetting === 2) {
            codes2.codes.push(cS.secretCode);
            codes2.mailSetting = cS.mailSetting;
            codes2.itemCount += 1;
            codes2.itemInCount += 1;
            codes2.mailFee += cS.mailFee;
          }
          if (cS.mailSetting === 3) {
            codes3.codes.push(cS.secretCode);
            codes3.mailSetting = cS.mailSetting;
            codes3.itemCount += 1;
            codes3.itemInCount += 1;
            codes3.mailFee += cS.mailFee;
          }
        })
        if (codes1.codes && codes1.codes.length > 0) {
          goodss.push(codes1)
        }
        if (codes2.codes && codes2.codes.length > 0) {
          goodss.push(codes2)
        }
        if (codes3.codes && codes3.codes.length > 0) {
          goodss.push(codes3)
        }
      } else {
        goodss.push(cG)
      }
    })

    let info = {
      delivery: delivery,
      distributionAmount: distributionAmount,
      orderAmount: orderAmount + distributionAmount,
      // goodss: this.data.goods,
      goodss: goodss,
      logisticss: logisticss,
      payWay: 2, // 付款方式：1 支付宝，2 微信
      remark: this.data.message,
      invoiceFlag: 0, // 是否开具发票：1 是，0 否
      shareFlag: shareFlag, // 是否分享：1 是，0 否
    };

    if (this.data.isDisable) {
      $request.post(api.addOrder, info).then((res) => {
        if (res.success) {
          // 订单编号
          let ids = res.data.ids.toString();

          let hasPrice = false;
          // 判断总邮费金额大于0
          if (this.data.totalMailFee > 0) {
            hasPrice = true
          }
          if (hasPrice) {
            // 大于0，调用支付
            this.payMent(ids);
          } else {
            wx.removeStorageSync('hasShared'); // 清除缓存已点击分享事件
            // 跳转兑换成功
            wx.redirectTo({
              url: "/pages/order/result/result?enterFlag=orderList",
            });
          }

          // 删除购物车数据
          this.delShopData(this.data.orderIds);
        } else {
          Toast.fail(res.errMessage);
        }
        this.setData({
          isDisable: false
        })
      });
    }
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
    let openid = wx.getStorageSync("openid");

    let accountInfo = wx.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;

    let info = {
      businessType: 1, // 业务类型：1 订单收款
      customerFlag: 1, // 客户标志：1 C端客户
      payMethod: "wxpay_mini_program", // 交易方式
      orderId: id,
      payerId: this.data.userId,
      platformUserId: openid,
      appId: appId,
    };

    $request.post(api.handlePayment, info).then((res) => {
      if (res.success) {
        let wxConfig = res.data.wxResp;

        var enterParams = JSON.stringify({
          timeStamp: wxConfig.timeStamp,
          nonceStr: wxConfig.nonceStr,
          package: wxConfig.prepayId.replace(/\=/g, "123456789"), // 处理等号（等号去小程序解析会报错）
          signType: wxConfig.signType,
          paySign: wxConfig.paySign.replace(/\=/g, "123456789"), // 处理等号（等号去小程序解析会报错）
          orderNo: wxConfig.outTradeNo,
          orderPrice: this.data.totalMailFee,
        });
        wx.removeStorageSync('hasShared'); // 清除缓存已点击分享事件
        wx.redirectTo({
          url: "/pages/order/payment/payment?enterFlag=payment&enterParams=" +
            enterParams,
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
          wx.removeStorageSync("goods")
        } else {
          Toast.fail(res.errMessage);
        }
      })
      .catch(() => {});
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
          Toast.fail(res.errMessage);
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
          Toast.fail(res.errMessage);
        }
      });
  },

  // 获取兑换卡
  getCardData(good, index) {
    // 清空已选（去绑定返回bug）
    this.setData({
      hasChoosedCode: []
    });

    let num = good.itemCount;
    $request.post(api.getCardData, {
        userId: this.data.userId,
        materialModels: [{
          materialId: good.diy.materialId,
          modelId: good.diy.modelId,
          pictureId: good.diy.pictureId,
          num: num,
        }],
      })
      .then((res) => {
        if (res.success) {
          // 获取对应兑换卡列表
          if (
            res.data.exchangeCodeBeans &&
            res.data.exchangeCodeBeans.length > 0
          ) {
            let codeData = res.data.exchangeCodeBeans;
            codeData = codeData.sort(this.dataUp);
            this.setData({
              ["goods[" + index + "].cardList"]: codeData
            })

            if (this.data.hasChoosedCode && this.data.hasChoosedCode.length > 0) {
              codeData.forEach((code) => {
                code.hasNSelect = 0;
                if (code.canSelect && !code.hasNSelect) {
                  // 判断已选中兑换码列表是否与当前码相同
                  this.data.hasChoosedCode.forEach((choosed) => {
                    if (choosed === code.secretCode) {
                      code.hasNSelect = 1
                    }
                  });
                }
              });
            }

            // 判断当前商品需要兑换卡数量
            let selects = [];
            codeData.forEach((item) => {
              if (item.canSelect && !item.hasNSelect) {
                if (selects.length < num) {
                  item.hasNSelect = 1;
                  selects.push(item);

                  this.data.hasChoosedCode = [
                    ...new Set(this.data.hasChoosedCode.concat(item.secretCode)),
                  ];
                }
              }
            });
            this.setData({
              ["goods[" + index + "].cardList"]: codeData
            })

            // 显示兑换卡码
            if (selects) {
              let secretCodes = "";
              // 计算邮费
              let mailFee = 0;
              for (let i = 0; i < selects.length; i++) {
                if (i === 0) {
                  secretCodes += selects[i].secretCode;
                } else {
                  secretCodes += ", " + selects[i].secretCode;
                }
                if (selects[i].mailSetting !== 1) {
                  mailFee += selects[i].mailFee;
                }
              }
              this.setData({
                ["goods[" + index + "].secretCodes"]: secretCodes,
                ["goods[" + index + "].mailFee"]: mailFee
              })
            }
            // 设置已选数量
            this.setData({
              ["goods[" + index + "].selects"]: selects,
            })

            // 获取当前所需兑换卡
            let curNeedNum = num - selects.length;
            this.setData({
              ["goods[" + index + "].curNeedNum"]: selects.length ? curNeedNum : 0,
              goods: this.data.goods
            })

            // 更新数据
            this.updateMailFee(); // 更新运费

            this.setData({
              goods: this.data.goods,
              mGoods: this.data.mGoods,
              hasChoosedCode: this.data.hasChoosedCode,
            })

            // 查看转发活动
            this.findExchangeShare();
          }
        } else {
          Toast.fail(res.errMessage);
        }
      });
  },

  // 更新运费
  updateMailFee() {
    let totalMailFee = 0; // 总运费
    this.setData({
      hasMailFee: false
    })
    this.data.mGoods.forEach((item, index) => {
      let mailFee = 0; // 计算邮费
      let hasCode = false; // 是否有兑换码
      item.forEach((pro) => {
        if (pro.secretCodes) {
          hasCode = true;
        }
        // 如果当前定制商品有运费，显示提交订单
        if (pro.mailFee) {
          this.setData({
            hasMailFee: true
          })
          // 计算邮费
          mailFee += pro.mailFee;
        }
      });

      totalMailFee += mailFee; // 总运费
      // 运费
      item.deliveryPrice = mailFee;
      item.hasCode = hasCode;
    });
    this.setData({
      totalMailFee: Number(totalMailFee).toFixed(2), // 总运费
    })
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

  // 兑换卡弹窗
  handleCard(e) {
    let item = e.currentTarget.dataset.good;
    let secretCodes = item.secretCodes;
    // 将当前所选兑换码转数组
    let secretList = this.getArrByStr(secretCodes);
    this.setData({
      curPOrder: e.currentTarget.dataset.parent,
      curOrder: e.currentTarget.dataset.child
    })

    let count = 0; // 获取已选数量
    let cardList = item.cardList;
    if (secretList && secretList.length > 0) {
      if (cardList && cardList.length > 0) {
        cardList.forEach((code) => {
          code.hasNSelect = 0;
          // 将所有已选中兑换码列表设为不可选
          this.data.hasChoosedCode.forEach((choosed) => {
            if (code.secretCode === choosed) {
              code.canSelect = 0;
              code.hasNSelect = 1;
            }
          });

          // 将当前匹配兑换码列表设为可选
          secretList.forEach((secret) => {
            if (code.secretCode === secret) {
              code.canSelect = 1;
            }
          });

          // 获取已选数量
          if (Number(code.hasNSelect) === 1 && code.canSelect) {
            count++;
          }
        });

        this.setData({
          cardList: cardList,
          choosedNum: count,
        })
      }

      this.setData({
        cardListShow: true,
        showMask: true
      })
    } else {
      this.goBindCard();
    }

    this.setData({
      needNum: item.itemCount,
      goods: this.data.goods,
      mGoods: this.data.mGoods,
    })
  },

  // 选择兑换卡
  handleSelect(e) {
    let code = e.currentTarget.dataset.item;

    let secretCodes = ""; // 兑换码
    let mainFee = 0; // 计算邮费
    let count = 0; // 获取已选数量
    this.data.cardList.forEach((item) => {
      if (this.data.needNum === 1) {
        // 一张兑换卡，直接切换选择
        if (item.id === code.id && code.canSelect) {
          item.hasNSelect = 1;
        } else {
          item.hasNSelect = 0;
        }
      } else {
        // 多张兑换卡
        if (item.id === code.id && code.canSelect) {
          item.hasNSelect = (Number(item.hasNSelect) === 1 ? 0 : 1);
        }
      }

      // 当前可选
      if (item.canSelect) {
        this.data.hasChoosedCode.forEach((choosed, index) => {
          // 选择的元素与已选兑换码列表匹配，移除
          if (item.secretCode === choosed) {
            this.data.hasChoosedCode.splice(index, 1);
          }
        });
      }

      if (Number(item.hasNSelect) === 1 && item.canSelect) {
        // 兑换码
        secretCodes += ", " + item.secretCode;
        // 计算邮费
        if (item.mailSetting !== 1) {
          mainFee += item.mailFee;
        }
        this.data.hasChoosedCode = [
          ...new Set(
            this.data.hasChoosedCode.concat(this.getArrByStr(secretCodes))
          ),
        ];
      }

      // 获取已选数量
      if (Number(item.hasNSelect) === 1 && item.canSelect) {
        count++;
      }
    });

    // 去除前面,空格
    this.data.mGoods[this.data.curPOrder][this.data.curOrder].secretCodes = secretCodes
      .replace(/^, +/, "")
      .replace(/, +$/, "");

    // 计算运费
    this.data.mGoods[this.data.curPOrder][this.data.curOrder].mailFee = mainFee;

    // 更新数据
    this.updateMailFee(); // 更新运费
    this.setData({
      cardList: this.data.cardList,
      choosedNum: count,
      hasChoosedCode: this.data.hasChoosedCode,
      mGoods: this.data.mGoods,
    })

    // 查看转发活动
    this.findExchangeShare();
  },
  closeDialog() {
    if (this.data.choosedNum > 0) {
      if (this.data.needNum !== this.data.choosedNum) {
        Toast("兑换码数量与商品数量不匹配");
      }
      this.setData({
        cardListShow: false,
        showMask: false,
      })
    } else {
      if (this.data.cardList.length <= 0) {
        this.setData({
          cardListShow: false,
          showMask: false,
        })
      } else {
        Toast("请选择兑换卡");
      }
    }

    // 设置已选择兑换码
    let goods = this.data.goods;
    goods.forEach((good) => {
      let secretCodes = good.secretCodes.split(",");
      let selects = [];
      secretCodes.forEach(codes => {
        this.data.cardList.forEach((card) => {
          if (card.secretCode === codes) {
            selects.push(card);
          }
        })
      })
      good.selects = selects;
    })
    this.setData({
      goods: goods
    })
  },

  // 关闭蒙版
  closeMask() {
    this.setData({
      cardListShow: false,
      showMask: false,
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

  // 绑定兑换卡
  goBindCard() {
    this.setData({
      cardListShow: false, // 兑换卡列表弹窗
      showMask: false, // 蒙版
    })
    wx.navigateTo({
      url: "/pages/code/bindCode/bindCode?enterFlag=submitOrder"
    });
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

  // 查看转发活动
  findExchangeShare() {
    let secretCodeList = [];
    this.data.hasChoosedCode.forEach((code) => {
      secretCodeList.push(code)
    })

    this.setData({
      exchangeShareData: {}
    })

    if (secretCodeList && secretCodeList.length > 0 && this.data.hasMailFee) {
      $request
        .post(api.exchangeShareFind, {
          activityPlatform: 1, // 活动所属平台：1 兑换商城，2 定制商城
          seat: 1, // 活动位置：1 确认订单页，2 订单详情页
          secretCodeList: secretCodeList,
        }).then((res) => {
          if (res.success) {
            if (res.data) {
              this.setData({
                exchangeShareData: res.data
              })
              // 判断缓存是否已点击分享事件
              let hasShared = wx.getStorageSync('hasShared');

              this.updateMailFee(); // 更新运费

              if (hasShared) {
                // 已点击
                let totalMailFee = Number(this.data.totalMailFee - this.data.exchangeShareData.reduceAmount).toFixed(2);
                this.setData({
                  totalMailFee: totalMailFee > 0 ? totalMailFee : 0
                })
              }
            }
          }
        })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.removeStorageSync('hasShared'); // 清除缓存已点击分享事件
    if (options && options.addrId) {
      this.setData({
        curAddrId: Number(options.addrId)
      })
    }
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
    let pages = getCurrentPages();
    let currentPage = pages[pages.length - 1];
    let options = currentPage.options;
    if (options && options.addrId) {
      this.setData({
        curAddrId: Number(options.addrId)
      })
    }

    let userId = wx.getStorageSync('userId'); // 用户id
    let distributorId = wx.getStorageSync('distributorId'); // 分销商id
    this.setData({
      distributorId: distributorId,
      userId: userId
    })

    this.getAddress(); // 获取地址信息
    this.getNoticeList(); // 获取通知

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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function (res) {
    if (res.from === "button") {
      let id = res.target.dataset.id;
      let distributorId = this.data.exchangeShareData.distributorIds[0];
      return $request
        .post(api.exchangeShareSend, {
          distributorId: distributorId,
          id: id,
        })
        .then((res) => {
          if (res.success) {
            wx.setStorageSync('hasShared', 1); // 缓存击分享事件
            return {
              title: res.data.forwardText,
              path: "/pages/activity/activity?id=" + res.data.exchangeSpecialReleaseId + "&distributorId=" + res.data.distributorId,
              imageUrl: res.data.forwardImg,
            }
          } else {
            Toast.fail(res.errMessage);
          }
        })
    }
  }
})