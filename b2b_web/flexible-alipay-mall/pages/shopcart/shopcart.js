// Api
import $request from '/assets/api/request'
import api from '/assets/api/allApi'

Page({
  data: {
    pid: 0,
    distributorId: "", // 分销商id
    orderSource: "", // 订单来源
    noPro: false, // 购物车是否为空
    allSelectedGoods: [], // 已选商品
    goods: [], // 购物车商品
    mGoods: [], // 购物车拆单商品
    invalidGoods: [], // 失效商品
    checkAll: false, // 全选
    totalPrice: 0, // 合计
    // loading
    showLoading: false,
    message: "数据加载中",

    showFlexible: false //柔性关闭弹窗显示
  },

  // 获取购物车列表
  initData() {
    this.setData({
      showLoading: true, // loading
    });

    $request.get(api.getShopcartList).then((res) => {
      if (res.success) {
        if (res.data && res.data.length > 0) {
          this.setData({
            noPro: false
          })
          let goodsData = res.data;
          // 未返回价格，默认设置为0
          goodsData.forEach((item) => {
            if (!item.salePrice) {
              item.salePrice = 0;
            }
          });

          // 根据 openFlag 判断商品是否失效（1 有效，0 无效）
          let validData = goodsData.filter((item) => {
            return item.openFlag;
          });
          let invalidData = goodsData.filter((item) => {
            return !item.openFlag;
          });

          let goods = validData.sort(this.dataDown);
          let totalPrice = 0;
          let allSelectedGoods = [];
          // 设置是否选中属性
          goods.forEach((v, k) => {
            v.isChecked = false;
            
            if (this.data.checkAll) {
              // 全选购物车数据
              if (!v.isStockOut) {
                allSelectedGoods.push(v.id);
                totalPrice += v.salePrice * v.itemCount;
              }
              v.isChecked = true;
            } else {
              // 跳转进入，默认选中
              if (this.data.pid === v.id) {
                v.isChecked = true;
                totalPrice += v.salePrice * v.itemCount;
              }
            }
          });
          // 根据货品编码分组显示
          let mGoods = this.arrayGroupBy(goods, "itemCode");

          // 设置默认选中项，加入购物车
          if (this.data.pid) {
            allSelectedGoods.push(this.data.pid);
          }

          this.setData({
            goods: goods, // 商品
            mGoods: mGoods, // 分组后商品
            allSelectedGoods: allSelectedGoods, // 已选中商品
            invalidGoods: invalidData, // 失效商品
            totalPrice: Number(totalPrice).toFixed(2), // 合计
          })

        } else {
          this.setData({
            noPro: true
          })
        }
      } else {
        if (res.errCode === "B_AUTH_FAIL") {
          let enterParams = JSON.stringify({});
          my.redirectTo({
            url: '/pages/login/login?enterFlag=cartTab&enterParams=' + enterParams,
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
      });
    });
  },

  // 倒序
  dataDown(x, y) {
    return y.id - x.id;
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
      return [item[groupId]];
    });
    return sorted;
  },

  // 全选
  handleSelectAll() {
    if (this.data.checkAll) {
      // 反选
      this.setData({
        checkAll: false
      })
    } else {
      // 全选
      this.setData({
        checkAll: true
      })
    }

    let allSelectedGoods = [];
    let totalPrice = 0;

    let goods = this.data.goods;
    goods.forEach((v) => {
      if (this.data.checkAll) {
        // 全选购物车数据
        if (!v.isStockOut) {
          allSelectedGoods.push(v.id);
          totalPrice += v.salePrice * v.itemCount;
        }
        v.isChecked = true;
      } else {
        // 反选购物车数据
        v.isChecked = false;
      }
    });

    let mGoods = this.arrayGroupBy(goods, "itemCode")
    this.setData({
      goods: goods, // 实时更新数据
      mGoods: mGoods, // 实时更新数据
      allSelectedGoods: allSelectedGoods, // 已选中商品
      totalPrice: Number(totalPrice).toFixed(2), // 合计
    })
  },

  // 单选
  handleSelectSingle(e) {
    // 获取当前点击元素
    let curItem = 'mGoods[' + e.currentTarget.dataset.parent + '][' + e.currentTarget.dataset.child + '].isChecked'
    let item = e.currentTarget.dataset.good;

    if (item.isChecked) {
      this.setData({
        checkAll: false,
        [curItem]: false, // 实时更新选中状态
      })
    } else {
      this.setData({
        [curItem]: true, // 实时更新选中状态
      })
    }

    let totalPrice = 0;
    let goods = this.data.goods;
    // 已选中商品
    let allSelectedGoods = [];
    goods.forEach((v) => {
      if (v.id === item.id) {
        v.isChecked = !item.isChecked
      }
      if (!v.isStockOut && v.isChecked) {
        allSelectedGoods.push(v.id);
        totalPrice += v.salePrice * v.itemCount;
      }
    });
    this.setData({
      allSelectedGoods: allSelectedGoods,
      totalPrice: Number(totalPrice).toFixed(2), // 合计
    })

    // 判断是否全部选中
    if (allSelectedGoods.length >= goods.length) {
      // 设置全选
      this.setData({
        checkAll: true
      })
    }

    let mGoods = this.arrayGroupBy(goods, "itemCode")
    this.setData({
      goods: goods, // 实时更新数据
      mGoods: mGoods, // 实时更新数据
    });
  },

  // 结算
  handleSubmit() {
    console.log("购物车订单结算");

    if (this.data.allSelectedGoods.length > 0) {
      // 结算提示
      let that = this;
      my.confirm({
        title: "温馨提示",
        content: "您的勾选存在专属定制的商品，非质量问题不支持无理由退换喔，是否继续提交结算？",
        confirmButtonText: "确认提交",
        cancelButtonText: "我再想想",
        success(res) {
          if (res.confirm) {
            let goodsArr = [];

            that.data.goods.forEach((good) => {
              good.secretCodes = ""; // 设置兑换码字段
              if (that.data.allSelectedGoods.indexOf(good.id) >= 0) {
                goodsArr.push(good);
              }
            });
            my.setStorageSync({
              key: 'goods',
              data: JSON.stringify(goodsArr)
            })
            my.navigateTo({
              url: "/pages/order/order/order"
            })
          }
        }
      });
    } else {
      my.showToast({
        content: "请选择需要购买的商品",
        type: "none",
        duration: 2000,
      });
    }
  },

  // 删除确认
  handleDelete(ids) {
    let that = this;
    my.confirm({
      content: "确定要删除选中的商品吗？",
      success(res) {
        if (res.confirm) {
          that.deleteData(ids);
        }
      }
    });
  },

  // 删除数据
  deleteData(ids) {
    $request.put(api.deleteShopcart, {
      ids: ids,
    })
      .then((res) => {
        if (res.success) {
          my.showToast({
            content: "删除成功！",
            type: "none",
            duration: 2000,
          });
          this.initData();
        } else {
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });
        }
      })
      .catch(() => { });
  },

  // 删除全部
  handleDeleteAll() {
    let selectedIds = this.data.allSelectedGoods;
    if (selectedIds.length <= 0) {
      my.showToast({
        content: "未选择任何商品~",
        type: "none",
        duration: 2000,
      });
      return false;
    } else {
      this.handleDelete(selectedIds);
    }
  },

  // 输入商品数量
  changeInput(e) {
    // 获取当前点击元素
    let curItem = 'mGoods[' + e.currentTarget.dataset.parent + '][' + e.currentTarget.dataset.child + '].itemCount'
    let value = e.detail.value.replace(/\D/g, '');

    this.setData({
      [curItem]: Number(value)
    })

    let k = e.currentTarget.dataset.id;
    let goods = this.data.goods;
    goods.forEach((v) => {
      if (v.id === k) {
        v.itemCount = Number(value)
      }
    })
    let mGoods = this.arrayGroupBy(goods, "itemCode")
    this.setData({
      goods: goods, // 实时更新数据
      mGoods: mGoods, // 实时更新数据
    });
  },
  handleInput(e) {
    let k = e.currentTarget.dataset.id;
    // 获取当前点击元素
    let curItem = 'mGoods[' + e.currentTarget.dataset.parent + '][' + e.currentTarget.dataset.child + '].itemCount'

    let value = e.detail.value;
    if (value <= 0) {
      value = 1;
      my.showToast({
        content: "购买数量不能小于1~",
        type: "none",
        duration: 2000,
      });
    }

    if (value > 999) {
      value = 999;
      my.showToast({
        content: "购买数量不能超过999~",
        type: "none",
        duration: 2000,
      });
    }

    this.setData({
      [curItem]: value
    })

    let goods = this.data.goods;
    goods.forEach((v) => {
      if (v.id === k) {
        v.itemCount = Number(value)
      }
    })
    let mGoods = this.arrayGroupBy(goods, "itemCode")
    this.setData({
      goods: goods, // 实时更新数据
      mGoods: mGoods, // 实时更新数据
    });

    let totalPrice = 0;
    this.data.goods.forEach((item) => {
      if (item.isChecked) {
        totalPrice += item.salePrice * item.itemCount;
      }
      if (k === item.id) {
        // 更新数据
        this.refreshData(k, item.goodsType, value);
      }
    });
    this.setData({
      totalPrice: Number(totalPrice).toFixed(2), // 合计
    })
  },

  // 增减商品数量
  changeNum(e) {
    let k = e.currentTarget.dataset.id;
    let type = parseInt(e.currentTarget.dataset.type);

    // 如果是减少，要判断 > 1，最少是1
    let totalPrice = 0;
    let goods = this.data.goods;
    if (type === -1) {
      // 减
      goods.forEach((item) => {
        if (k === item.id) {
          if (item.itemCount > 1) {
            item.itemCount = parseInt(item.itemCount) + type;
            // 更新数据
            this.refreshData(k, item.goodsType, item.itemCount);
          } else {
            this.handleDelete([k]);
          }
        }
        if (item.isChecked) {
          totalPrice += item.salePrice * item.itemCount;
        }
      });
    } else {
      // 加
      goods.forEach((item) => {
        if (k === item.id) {
          // 设置商品增加上限为 999
          if (item.itemCount < 999) {
            item.itemCount = parseInt(item.itemCount) + type;
            // 更新数据
            this.refreshData(k, item.goodsType, item.itemCount);
          } else {
            my.showToast({
              content: "不能购买更多商品了~",
              type: "none",
              duration: 2000,
            });
          }
        }
        if (item.isChecked) {
          totalPrice += item.salePrice * item.itemCount;
        }
      });
    }

    let mGoods = this.arrayGroupBy(goods, "itemCode")
    this.setData({
      goods: goods, // 实时更新数据
      mGoods: mGoods, // 实时更新数据
      totalPrice: Number(totalPrice).toFixed(2), // 合计
    })

  },
  // 更新数据
  refreshData(id, goodsType, itemCount) {
    $request
      .put(api.updataShopcart, {
        id: id,
        goodsType: goodsType,
        itemCount: itemCount,
        itemType: 1, // 是否赠品：1 非赠品，2 赠品
      })
      .then((res) => {
        if (!res.success) {
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });
        }
      });
  },

  // 清空失效
  handleEmpty() {
    var ids = [];
    this.data.invalidGoods.forEach((v, k) => {
      ids.push(v.id);
    });

    let that = this;
    my.confirm({
      content: "确定要清空失效商品吗？",
      success(res) {
        if (res.confirm) {
          that.deleteData(ids);
        }
      }
    });
  },

  onLoad: function (options) {
    if (options && options.pid) {
      this.setData({
        pid: Number(options.pid)
      })
    }
  },

  onShow: function (options) {
    // 获取购物车列表
    this.initData();
  },

  onTabItemTap(e){
    this.setData({
      showLoading: false,
    })
  },

  //关闭柔性弹窗
  clickFlexible: function(){
    this.setData({
      showFlexible: false
    });
  }
})