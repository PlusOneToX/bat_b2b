// pages/shopcart/shopcart.js
// Toast
import Toast from '../../miniprogram_npm/@vant/weapp/toast/toast';
// Dialog
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
// Api
import $request from '../../assets/api/request'
import api from '../../assets/api/allApi'

Page({

  /**
   * 页面的初始数据
   */
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
  },

  // 获取购物车列表
  initData() {
    Toast.loading({
      duration: 0, // 持续展示 toast
      message: '加载中...',
      forbidClick: true,
      loadingType: 'spinner',
      selector: '#van-toast',
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
          // 设置是否选中属性
          goods.forEach((v, k) => {
            v.isChecked = false;

            if (this.data.pid === v.id) {
              v.isChecked = true;
            }
          });
          // 根据货品编码分组显示
          let mGoods = this.arrayGroupBy(goods, "itemCode");

          // 设置默认选中项，加入购物车
          let allSelectedGoods = [];
          if (this.data.pid) {
            allSelectedGoods.push(this.data.pid);
          }

          // 判断是否全部选中
          if (allSelectedGoods.length >= goods.length) {
            // 设置全选
            this.setData({
              checkAll: true
            })
          }

          this.setData({
            goods: goods, // 商品
            mGoods: mGoods, // 分组后商品
            allSelectedGoods: allSelectedGoods, // 已选中商品
            invalidGoods: invalidData // 失效商品
          })

        } else {
          this.setData({
            noPro: true
          })
        }
      } else {
        Toast.fail(res.errMessage);
      }
      Toast.clear();
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
    this.data.goods.forEach((v, k) => {
      if (this.data.checkAll) {
        // 全选购物车数据
        if (!v.isStockOut) {
          allSelectedGoods.push(v.id);
        }
        v.isChecked = true;
      } else {
        // 反选购物车数据
        v.isChecked = false;
      }
    });

    this.setData({
      mGoods: this.data.mGoods, // 实时更新数据
      allSelectedGoods: allSelectedGoods // 已选中商品
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

    // 已选中商品
    let allSelectedGoods = [];
    this.data.goods.forEach((v, k) => {
      if (!v.isStockOut && v.isChecked) {
        allSelectedGoods.push(v.id);
      }
    });
    this.setData({
      allSelectedGoods: allSelectedGoods
    })

    // 判断是否全部选中
    if (this.data.allSelectedGoods.length >= this.data.goods.length) {
      // 设置全选
      this.setData({
        checkAll: true
      })
    }
  },

  // 结算
  handleSubmit() {
    if (this.data.allSelectedGoods.length > 0) {
      // 结算提示
      Dialog.confirm({
          title: "温馨提示",
          message: "您的勾选存在专属定制的商品，非质量问题不支持无理由退换喔，是否继续提交结算？",
          className: "confirm-v-dialog tl",
          confirmButtonText: "确认提交",
          cancelButtonText: "我再想想",
          confirmButtonColor: "#333333",
          cancelButtonColor: "#999999",
        })
        .then(() => {
          let goodsArr = [];

          this.data.goods.forEach((good) => {
            good.secretCodes = ""; // 设置兑换码字段
            if (this.data.allSelectedGoods.indexOf(good.id) >= 0) {
              goodsArr.push(good);
            }
          });
          wx.setStorageSync('goods', JSON.stringify(goodsArr))
          wx.navigateTo({
            url: "/pages/order/order/order"
          })
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      Toast("请选择需要购买的商品");
    }
  },

  // 删除确认
  handleDelete(ids) {
    Dialog.confirm({
        message: "确定要删除选中的商品吗？",
        className: "confirm-v-dialog",
        confirmButtonColor: "#333333",
        cancelButtonColor: "#999999",
      })
      .then(() => {
        this.deleteData(ids);
      })
      .catch((error) => {
        console.log(error);
      });
  },

  // 删除数据
  deleteData(ids) {
    $request.remove(api.deleteShopcart, {
        ids: ids,
      })
      .then((res) => {
        if (res.success) {
          Toast("删除成功！");
          this.initData();
        } else {
          Toast.fail(res.errMessage);
        }
      })
      .catch(() => {});
  },

  // 删除全部
  handleDeleteAll() {
    let selectedIds = this.data.allSelectedGoods;
    if (selectedIds.length <= 0) {
      Toast("未选择任何商品~");
      return false;
    } else {
      this.handleDelete(selectedIds);
    }
  },

  // 输入商品数量
  handleInput(e) {
    let k = e.currentTarget.dataset.id;
    // 获取当前点击元素
    let curItem = 'mGoods[' + e.currentTarget.dataset.parent + '][' + e.currentTarget.dataset.child + '].itemCount'

    let value = e.detail.value.replace(/\D/g, '');
    if (value <= 0) {
      Toast("购买数量不能小于1~");
      value = 1;
    }
    if (value > 999) {
      Toast("购买数量不能超过999~");
      value = 999;
    }

    this.setData({
      [curItem]: value
    })

    this.data.goods.forEach((item) => {
      if (k === item.id) {
        // 更新数据
        this.refreshData(k, item.goodsType, value);
      }
    });
  },

  // 增减商品数量
  changeNum(e) {
    let k = e.currentTarget.dataset.id;
    let type = parseInt(e.currentTarget.dataset.type);

    // 如果是减少，要判断 > 1，最少是1
    if (type === -1) {
      // 减
      this.data.goods.forEach((item) => {
        if (k === item.id) {
          if (item.itemCount > 1) {
            item.itemCount = parseInt(item.itemCount) + type;
            // 更新数据
            this.refreshData(k, item.goodsType, item.itemCount);
          } else {
            this.handleDelete([k]);
          }
        }
      });
    } else {
      // 加
      this.data.goods.forEach((item) => {
        if (k === item.id) {
          // 设置商品增加上限为 999
          if (item.itemCount < 999) {
            item.itemCount = parseInt(item.itemCount) + type;
            // 更新数据
            this.refreshData(k, item.goodsType, item.itemCount);
          } else {
            Toast("不能购买更多商品了~");
            return false;
          }
        }
      });
    }

    this.setData({
      mGoods: this.data.mGoods, // 实时更新数据
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
          Toast.fail(res.errMessage);
        }
      });
  },

  // 清空失效
  handleEmpty() {
    var ids = [];
    this.data.invalidGoods.forEach((v, k) => {
      ids.push(v.id);
    });
    Dialog.confirm({
        message: "确定要清空失效商品吗？",
        className: "confirm-v-dialog",
        confirmButtonColor: "#333333",
        cancelButtonColor: "#999999",
      })
      .then(() => {
        this.deleteData(ids);
      })
      .catch(() => {});
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options && options.pid) {
      this.setData({
        pid: Number(options.pid)
      })
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    // 获取购物车列表
    this.initData();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

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
  onShareAppMessage: function () {

  }
})