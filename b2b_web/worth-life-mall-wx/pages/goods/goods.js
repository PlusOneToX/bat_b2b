var app = getApp();
var WxParse = require('../../lib/wxParse/wxParse.js');
var util = require('../../utils/util.js');

Page({
  data: {
    height: 64, //header高度
    top: 0, //标题图标距离顶部距离
    scrollH: 0, //滚动总高度
    opcity: 0,
    iconOpcity: 0.5,
    menuShow: false,
    id: '',
    goods: {},
    gallery: [],
    attribute: [],
    issueList: [],
    comment: [],
    cartGoodsCount: 0,
    userHasCollect: 0,
    number: 1,
    checkedSpecText: '已选',
    openAttr: false,
    isIphoneX: false,
    showNavList: false,
    noCollectImage: "/static/images/icon_collect.png",
    hasCollectImage: "/static/images/icon_collect_checked.png",
    collectBackImage: "/static/images/icon_collect.png",
    commodityAttr: [],
    keyValue: [],
    selected: '已选：',
    selectPrice: '',
    selectStock: '',
    selectedText: '',
    includeGroup: [],
    tempQrCode: '', // 商品小程序码
    tempGoodsListPic: '', // 商品图
    imagePath: '',
    maskHidden: false,
    shareVisible: false,
    referrerUserId: '',
  },
  onLoad: function (options) {
    let that = this;
    //转发的用户Id
    if (options.referrer) {
      app.globalData.referrerUserId = options.referrer;
      // util.showMsg("分享人Id："+options.referrer);
      that.data.referrerUserId = options.referrer;
    }
    wx.setStorageSync("navUrl", "/pages/goods/goods?id=" + options.id);
    // 页面初始化 options为页面跳转所带来的参数
    that.setData({
      id: options.id
    });
    that.getGoodsInfo();

    util.isiPhoneX(function (ret) {
      that.setData({
        isIphoneX: ret
      })
    });
    this.setData({
      width: app.globalData.customBar.width,
      height: app.globalData.customBar.height,
      top: app.globalData.customBar.top,
      scrollH: app.globalData.customBar.scrollH
    })
  },
  //页面滚动执行方式
  onPageScroll(e) {
    let scroll = e.scrollTop <= 0 ? 0 : e.scrollTop;
    let opcity = scroll / this.data.scrollH;
    if (this.data.opcity >= 1 && opcity >= 1) {
      return;
    }
    this.setData({
      opcity: opcity,
      iconOpcity: 0.5 * (1 - opcity < 0 ? 0 : 1 - opcity)
    })
  },
  onShareTimeline: function () {
    var that = this;

    let shareDesc = that.data.goods.name;
    let goods = that.data.goods;
    shareDesc += ' ' + goods.retailPrice + "元";
    // 设置菜单中的转发按钮触发转发事件时的转发内容
    var shareObj = {
      title: shareDesc, // 默认是小程序的名称(可以写slogan等)
      path: '/pages/goods/goods?id=' + that.data.id + '&&referrer=' + wx.getStorageSync('userId'), // 默认是当前页面，必须是以‘/’开头的完整路径
      //query: 'id=' + that.data.id + '&referrer=' +wx.getStorageSync('userId'),
      imageUrl: that.data.goods.listPicUrl, //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径，支持PNG及JPG，不传入 imageUrl 则使用默认截图。显示图片长宽比是 5:4
      success: function (res) {
        // 转发成功之后的回调
        if (res.errMsg == 'shareAppMessage:ok') {
          util.showMsg('转发成功');
        }
      },
      fail: function () {
        // 转发失败之后的回调
        if (res.errMsg == 'shareAppMessage:fail cancel') {
          // 用户取消转发
        } else if (res.errMsg == 'shareAppMessage:fail') {
          // 转发失败，其中 detail message 为详细失败信息
        }
      },
      complete: function () {
        // 转发结束之后的回调（转发成不成功都会执行）
      }
    };
    return shareObj;
  },
  onShareAppMessage: function () {
    var that = this;
    let shareDesc = that.data.goods.name;
    let goods = that.data.goods;
    shareDesc += ' ' + goods.retailPrice + "元";
    // 设置菜单中的转发按钮触发转发事件时的转发内容
    var shareObj = {
      title: shareDesc, // 默认是小程序的名称(可以写slogan等)
      desc: "商业版",
      path: '/pages/goods/goods?id=' + that.data.id + '&&referrer=' + wx.getStorageSync('userId'), // 默认是当前页面，必须是以‘/’开头的完整路径
      imageUrl: that.data.goods.listPicUrl, //自定义图片路径，可以是本地文件路径、代码包文件路径或者网络图片路径，支持PNG及JPG，不传入 imageUrl 则使用默认截图。显示图片长宽比是 5:4
      success: function (res) {
        // 转发成功之后的回调
        if (res.errMsg == 'shareAppMessage:ok') {
          util.showMsg('转发成功');

        }
      },
      fail: function () {
        // 转发失败之后的回调
        if (res.errMsg == 'shareAppMessage:fail cancel') {
          // 用户取消转发
        } else if (res.errMsg == 'shareAppMessage:fail') {
          // 转发失败，其中 detail message 为详细失败信息
        }
      },
      complete: function () {
        // 转发结束之后的回调（转发成不成功都会执行）
      }
    };
    return shareObj;
  },
  toggleNav() {
    this.setData({
      showNavList: !this.data.showNavList
    })
  },
  back: function () {
    wx.navigateBack({
      fail() {
        wx.switchTab({
          url: '/pages/index/index',
        })
      }
    })
  },
  feedback: function () {
    wx.navigateTo({
      url: '/pages/ucenter/feedback/feedback',
    })
  },
  openMenu: function () {
    this.setData({
      menuShow: true
    })
  },
  closeMenu: function () {
    this.setData({
      menuShow: false
    })
  },
  pengyouquan() {
    util.showMsg('请点击右上角分享到朋友圈')
    this.setData({
      shareVisible: false
    })
  },
  switchNav(event) {
    let name = event.currentTarget.dataset.name;
    wx.switchTab({
      url: `/pages/${name}/${name}`,
    });
  },
  getGoodsInfo: function () {
    let that = this;
    util.request('goods/otherDetail', {
      goodsId: that.data.id,
      userId: wx.getStorageSync('userId') || ''
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          issueList: res.data.issue,
          comment: res.data.comment,
          userHasCollect: res.data.userHasCollect
        });
        if (res.data.userHasCollect == 1) {
          that.setData({
            'collectBackImage': that.data.hasCollectImage
          });
        } else {
          that.setData({
            'collectBackImage': that.data.noCollectImage
          });
        }
      }
    });
    util.request('goods/detail', {
      goodsId: that.data.id,
      userId: wx.getStorageSync('userId') || ''
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          goods: res.data.info,
          gallery: res.data.info.attachmentEntityList,
          attribute: res.data.info.goodsAttributeEntityList,
          selectPrice: res.data.info.retailPrice,
          selectStock: res.data.info.goodsNumber
        });
        wx.downloadFile({
          //url网络图片地址必须要在小程序中配备合法域名
          url: that.data.goods.listPicUrl,
          success(res) {
            if (res.statusCode === 200) {
              that.setData({
                tempGoodsListPic: res.tempFilePath
              })
            }
          }
        })
        that.setData({
          commodityAttr: res.data.info.goodsSkuEntityList
        })
        that.setData({
          includeGroup: that.data.commodityAttr
        });
        that.distachAttrValue(that.data.commodityAttr);
        // 只有一个属性组合的时候默认选中
        // console.log(this.data.keyValue);
        if (that.data.commodityAttr.length == 1) {
          for (var i = 0; i < that.data.commodityAttr[0].keyValue.length; i++) {
            that.data.keyValue[i].selectedValue = that.data.commodityAttr[0].keyValue[i].value;
          }
          that.setData({
            keyValue: that.data.keyValue
          });
        }
        WxParse.wxParse('goodsDetail', 'html', res.data.info.goodsDesc, that);
      }
      that.setSelect();
    });
    util.request('cart/goodsCount',{
      userId: wx.getStorageSync('userId') || ''
    }).then(function (res) {
      if (res.code === 0) {
        that.setData({
          cartGoodsCount: res.goodsCount
        });
      }
    });
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭

  },
  showShare: function () {
    this.setData({
      menuShow: false,
      shareVisible: true
    })
  },
  hideShare: function () {
    this.setData({
      shareVisible: false
    })
  },
  //点击保存到相册
  savePoster: function () {
    var that = this
    wx.saveImageToPhotosAlbum({
      filePath: that.data.imagePath,
      success(res) {
        wx.showModal({
          content: '海报已保存到相册',
          showCancel: false,
          confirmText: '确定',
          confirmColor: '#333',
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
  //取消
  cancelPoster: function () {
    var that = this
    that.setData({
      maskHidden: false
    })
  },
  //点击生成
  formSubmit: function (e) {
    var that = this;
    this.setData({
      maskHidden: false
    });
    util.showMsg('海报生成中...', true, 2000, 'loading');
    setTimeout(function () {
      wx.hideToast()
      that.createNewImg();
      that.setData({
        maskHidden: true
      });
    }, 2000)
  },
  // 生成海报
  createNewImg: function () {
    let that = this;
    this.setData({
      shareVisible: false
    })

    //小程序码长宽
    let codeSize = 100;
    util.request('goods/getGoodsQrCode', {
      goodsId: that.data.goods.id,
      width: codeSize
    }).then(function (resp) {
      if (resp.code === 0) {
        // 后台返回的小程序码
        console.log(resp.url)
        wx.downloadFile({
          //url网络图片地址必须要在小程序中配备合法域名
          url: resp.url,
          success(res) {
            if (res.statusCode === 200) {
              that.setData({
                tempQrCode: res.tempFilePath
              })

              var context = wx.createCanvasContext('mycanvas');
              context.setFillStyle("#fff")
              context.fillRect(0, 0, 350, 450)
              //绘制商品图片
              context.drawImage(that.data.tempGoodsListPic, 50, 0, 250, 250);
              //绘制商品名称
              context.setFontSize(14);
              context.setFillStyle('#333');
              context.setTextAlign('left');
              context.fillText(that.data.goods.name, 50, 260);
              context.stroke();
              //绘制商品价格
              context.setFontSize(14);
              context.setFillStyle('#e41f19');
              context.setTextAlign('left');
              context.fillText('¥' + that.data.goods.retailPrice, 50, 285);
              context.stroke();

              //画虚线
              context.drawImage('/static/images/line.png', 50, 300, 250, 10);

              //绘制左下角小程序二维码
              context.drawImage(that.data.tempQrCode, 60, 320, codeSize, codeSize);

              //绘制右下角文字
              context.setFontSize(16);
              context.setFillStyle('#000');
              context.setTextAlign('left');
              context.fillText("长按识别小程序码", 170, 370);
              context.stroke();
              context.setFontSize(12);
              context.setFillStyle('#A5A5A5');
              context.setTextAlign('left');
              context.fillText("立即购买", 210, 390);
              context.stroke();

              context.draw();
              //将生成好的图片保存到本地
              setTimeout(function () {
                wx.canvasToTempFilePath({
                  canvasId: 'mycanvas',
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
      }
    });
  },
  //放大预览轮播图片
  previewImg: function (e) {
    var curImg = e.currentTarget.dataset.img;
    var gallery = this.data.gallery;
    if (!util.isEmpty(curImg) && gallery.length > 0) {
      var urls = [];
      for (var i = 0; i < gallery.length; i++) {
        urls[i] = gallery[i].url;
      }
      wx.previewImage({
        current: curImg, // 当前显示图片的http链接
        urls: urls, // 需要预览的图片http链接列表
      })
    }
  },

  switchAttrPop: function () {
    let that = this;
    if (that.data.openAttr == false) {
      that.setData({
        openAttr: !that.data.openAttr
      });
    } else {
      that.setData({
        openAttr: !that.data.openAttr,
      })
    }
  },
  closeAttrOrCollect: function () {
    let that = this;
    if (this.data.openAttr) {
      this.setData({
        openAttr: false,
      });
      if (that.data.userHasCollect == 1) {
        that.setData({
          'collectBackImage': that.data.hasCollectImage
        });
      } else {
        that.setData({
          'collectBackImage': that.data.noCollectImage
        });
      }
    } else {
      //添加或是取消收藏
      util.request('user/addOrDelete', {
          goodsId: this.data.id
        }, "POST")
        .then(function (res) {
          let _res = res;
          if (_res.code == 0) {
            if (_res.data.type == 'add') {
              util.showMsg('收藏成功');
              that.setData({
                'collectBackImage': that.data.hasCollectImage
              });
            } else {
              util.showMsg('取消成功');
              that.setData({
                'collectBackImage': that.data.noCollectImage
              });
            }
          } else {
            util.showMsg(_res.msg);
          }
        });
    }
  },
  openCartPage: function () {
    wx.switchTab({
      url: '/pages/cart/cart',
    });
  },
  /**
   * 直接购买
   */
  buyGoods: function () {
    var that = this;
    if (this.data.openAttr == false) {
      this.setData({
        collectBackImage: "/static/images/detail_back.png"
      });
      //打开规格选择窗口
      this.setData({
        openAttr: !this.data.openAttr
      });
    } else {
      var value = [];
      for (var i = 0; i < that.data.keyValue.length; i++) {
        if (!that.data.keyValue[i].selectedValue) {
          break;
        }
        value.push(that.data.keyValue[i].selectedValue);
      }
      if (i < that.data.keyValue.length) {
        util.showMsg('请选择完整！');
      } else {
        // 直接购买商品
        util.request('buy/add', {
            skuId: this.data.includeGroup[0].id,
            goodsId: this.data.goods.id,
            number: this.data.number,
            selectedText: this.data.selectedText
          }, "POST", 'application/json')
          .then(function (res) {
            let _res = res;
            if (_res.code == 0) {
              that.setData({
                openAttr: !that.data.openAttr,
              });
              let promoUserId = '&promoUserId=';
              if (that.data.referrerUserId !== '') {
                promoUserId = promoUserId + that.data.referrerUserId;
              }

              wx.navigateTo({
                url: '/pages/shopping/checkout/checkout?isBuy=true' + promoUserId,
              })
            } else {
              util.showMsg(_res.msg);
            }
          });
      }
    }
  },
  addToCart: function () {
    var that = this;
    if (this.data.openAttr == false) {
      //打开规格选择窗口
      this.setData({
        openAttr: !this.data.openAttr
      });
    } else {
      var value = [];
      for (var i = 0; i < that.data.keyValue.length; i++) {
        if (!that.data.keyValue[i].selectedValue) {
          break;
        }
        value.push(that.data.keyValue[i].selectedValue);
      }
      if (i < that.data.keyValue.length) {
        util.showMsg('请选择完整！');
      } else {
        //添加到购物车
        util.request('cart/add', {
            skuId: this.data.includeGroup[0].id,
            goodsId: this.data.goods.id,
            referrerUserId: this.data.referrerUserId,
            number: this.data.number,
            selectedText: this.data.selectedText
          }, "POST")
          .then(function (res) {
            let _res = res;
            if (_res.code == 0) {
              util.showMsg('添加成功');
              that.setData({
                openAttr: !that.data.openAttr,
              })
              that.setData({
                cartGoodsCount: _res.cartTotal.goodsCount
              });
            } else {
              util.showMsg(_res.msg);
            }
          });
      }
    }
  },
  changeNumber: function (event) {
    let number = event.detail.value;
    this.setData({
      number: number
    });
  },
  /* 获取数据 */
  distachAttrValue: function (commodityAttr) {
    /**
    将后台返回的数据组合成类似
    {
    key:'型号',
    keyValue:['1','2','3']
    }
    */
    // 把数据对象的数据（视图使用），写到局部内
    var keyValue = this.data.keyValue;
    // 遍历获取的数据
    for (var i = 0; i < commodityAttr.length; i++) {
      for (var j = 0; j < commodityAttr[i].keyValue.length; j++) {
        var attrIndex = this.getAttrIndex(commodityAttr[i].keyValue[j].key, keyValue);
        // console.log('属性索引', attrIndex);
        // 如果还没有属性索引为-1，此时新增属性并设置属性值数组的第一个值；索引大于等于0，表示已存在的属性名的位置
        if (attrIndex >= 0) {
          // 如果属性值数组中没有该值，push新值；否则不处理
          if (!this.isValueExist(commodityAttr[i].keyValue[j].value, keyValue[attrIndex].attrValues)) {
            keyValue[attrIndex].attrValues.push(commodityAttr[i].keyValue[j].value);
          }
        } else {
          keyValue.push({
            key: commodityAttr[i].keyValue[j].key,
            attrValues: [commodityAttr[i].keyValue[j].value]
          });
        }
      }
    }
    // console.log('result', keyValue)
    for (var i = 0; i < keyValue.length; i++) {
      for (var j = 0; j < keyValue[i].attrValues.length; j++) {
        if (keyValue[i].attrValueStatus) {
          keyValue[i].attrValueStatus[j] = true;
        } else {
          keyValue[i].attrValueStatus = [];
          keyValue[i].attrValueStatus[j] = true;
        }
      }
    }
    this.setData({
      keyValue: keyValue
    });
  },
  getAttrIndex: function (attrName, keyValue) {
    // 判断数组中的attrKey是否有该属性值
    for (var i = 0; i < keyValue.length; i++) {
      if (attrName == keyValue[i].key) {
        break;
      }
    }
    return i < keyValue.length ? i : -1;
  },
  isValueExist: function (value, valueArr) {
    // 判断是否已有属性值
    for (var i = 0; i < valueArr.length; i++) {
      if (valueArr[i] == value) {
        break;
      }
    }
    return i < valueArr.length;
  },
  /* 选择属性值事件 */
  selectAttrValue: function (e) {
    /*
    点选属性值，联动判断其他属性值是否可选
    {
    key:'型号',
    keyValue:['1','2','3'],
    selectedValue:'1',
    attrValueStatus:[true,true,true]
    }
    console.log(e.currentTarget.dataset);
    */
    var keyValue = this.data.keyValue;
    var index = e.currentTarget.dataset.index; //属性索引
    var key = e.currentTarget.dataset.key;
    var value = e.currentTarget.dataset.value;
    if (e.currentTarget.dataset.status || index == this.data.firstIndex) {
      if (e.currentTarget.dataset.selectedvalue == e.currentTarget.dataset.value) {
        // 取消选中
        this.disSelectValue(keyValue, index, key, value);
      } else {
        // 选中
        this.selectValue(keyValue, index, key, value);
      }
    }
  },
  /* 选中 */
  selectValue: function (keyValue, index, key, value, unselectStatus) {
    // console.log('firstIndex', this.data.firstIndex);
    var includeGroup = [];
    if (index == this.data.firstIndex && !unselectStatus) { // 如果是第一个选中的属性值，则该属性所有值可选
      var commodityAttr = this.data.commodityAttr;
      // 其他选中的属性值全都置空
      // console.log('其他选中的属性值全都置空', index, this.data.firstIndex, !unselectStatus);
      for (var i = 0; i < keyValue.length; i++) {
        for (var j = 0; j < keyValue[i].attrValues.length; j++) {
          keyValue[i].selectedValue = '';
        }
      }
    } else {
      var commodityAttr = this.data.includeGroup;
    }

    // console.log('选中', commodityAttr, index, key, value);
    for (var i = 0; i < commodityAttr.length; i++) {
      for (var j = 0; j < commodityAttr[i].keyValue.length; j++) {
        if (commodityAttr[i].keyValue[j].key == key && commodityAttr[i].keyValue[j].value == value) {
          includeGroup.push(commodityAttr[i]);
        }
      }
    }
    keyValue[index].selectedValue = value;

    // 判断属性是否可选
    for (var i = 0; i < keyValue.length; i++) {
      for (var j = 0; j < keyValue[i].attrValues.length; j++) {
        keyValue[i].attrValueStatus[j] = false;
      }
    }
    for (var k = 0; k < keyValue.length; k++) {
      for (var i = 0; i < includeGroup.length; i++) {
        for (var j = 0; j < includeGroup[i].keyValue.length; j++) {
          if (keyValue[k].key == includeGroup[i].keyValue[j].key) {
            for (var m = 0; m < keyValue[k].attrValues.length; m++) {
              if (keyValue[k].attrValues[m] == includeGroup[i].keyValue[j].value) {
                keyValue[k].attrValueStatus[m] = true;
              }
            }
          }
        }
      }
    }
    this.setData({
      keyValue: keyValue,
      includeGroup: includeGroup
    });

    var count = 0;
    for (var i = 0; i < keyValue.length; i++) {
      for (var j = 0; j < keyValue[i].attrValues.length; j++) {
        if (keyValue[i].selectedValue) {
          count++;
          break;
        }
      }
    }
    if (count < 2) { // 第一次选中，同属性的值都可选
      this.setData({
        firstIndex: index
      });
    } else {
      this.setData({
        firstIndex: -1
      });
    }
    this.setSelect();
  },
  /* 取消选中 */
  disSelectValue: function (keyValue, index, key, value) {
    var that = this;
    var commodityAttr = that.data.commodityAttr;
    keyValue[index].selectedValue = '';

    // 判断属性是否可选
    for (var i = 0; i < keyValue.length; i++) {
      for (var j = 0; j < keyValue[i].attrValues.length; j++) {
        keyValue[i].attrValueStatus[j] = true;
      }
    }
    that.setData({
      includeGroup: commodityAttr,
      keyValue: keyValue
    });

    for (var i = 0; i < keyValue.length; i++) {
      if (keyValue[i].selectedValue) {
        that.selectValue(keyValue, i, keyValue[i].key, keyValue[i].selectedValue, true);
      }
    }
    that.setSelect();
  },
  setSelect: function () {
    var that = this;
    var valueStr = "";
    var value = [];
    var selectedText = '';
    for (var i = 0; i < that.data.keyValue.length; i++) {
      if (!that.data.keyValue[i].selectedValue) {
        break;
      }
      value.push(that.data.keyValue[i].selectedValue);
      selectedText += that.data.keyValue[i].key + '：' + that.data.keyValue[i].selectedValue + '；'
    }
    for (var i = 0; i < value.length; i++) {
      valueStr += value[i] + ",";
    }
    valueStr = valueStr.substr(0, valueStr.length - 1)
    that.setData({
      selected: '已选：' + valueStr
    })
    that.setData({
      selectPrice: that.data.includeGroup[0].retailPrice,
      selectStock: that.data.includeGroup[0].goodsNumber,
      selectedText: selectedText
    })
  }
})
