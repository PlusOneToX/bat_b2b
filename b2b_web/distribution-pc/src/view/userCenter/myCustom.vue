<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="content">
            <h6 class="user-right-title">{{$t('UserCenter.DIY')}}</h6>
            <div class="table" v-if="this.diyGoodsList.length > 0">
              <table>
                <tr>
                  <th>#</th>
                  <th>{{$t('ShopCarts.Picture')}}</th>
                  <th>{{$t('ShopCarts.ItemNo')}}</th>
                  <th>{{$t('ShopCarts.ItemName')}}</th>
                  <th>{{$t('ShopCarts.Spe')}}</th>
                  <th>{{$t('ShopCarts.Colors')}}</th>
                  <th>{{$t('UserCenter.CreationTime')}}</th>
                  <th>{{$t('UserCenter.Operation')}}</th>
                </tr>
                <tr v-for="(item,index) in diyGoodsList" :key="index">
                  <td width="5%">{{index + 1}}</td>
                  <td>
                    <div class="shop-img cursor-pointer">
                      <img
                        @click="magnify(item.diyPic)"
                        :src="item.diyPic + '?x-oss-process=image/resize,h_200,l_400'"
                        alt
                      />
                    </div>
                  </td>
                  <td>{{item.itemCode}}</td>
                  <td>{{item.itemName}}</td>
                  <td>{{item.specificationValueName}}</td>
                  <td>{{item.colorValueName}}</td>
                  <td class="rl-text-xxs">{{item.createTime | formatDate}}</td>
                  <td width="200px">
                    <span class="order cursor-pointer" @click="goOrder(item)">{{$t('P.Buy')}}</span>
                    <span
                      class="delet cursor-pointer"
                      @click="deleteDiy(item.diyId)"
                    >{{$t('ShopCarts.Delete')}}</span>
                  </td>
                </tr>
              </table>
            </div>
            <div class="empty-car rl-margin-zero" v-else>
              <div class="empty-car-img rl-margin-zero">
                <img width="100%" src="../../assets/images/empty-car.png" alt />
              </div>
              <p
                class="rl-tc rl-margin-top-default rl-margin-bottom-default rl-text-sm rl-text-gray"
              >{{$t('P.NoCommodities')}}</p>
            </div>
          </div>
          <div class="rl-tr rl-margin-top-default" v-if="this.diyGoodsList.length > 0">
            <el-pagination
              background
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
              layout="prev, pager, next, jumper"
              :page-size="pagesize"
              :total="totalCount"
            ></el-pagination>
          </div>
        </div>
      </div>
    </div>
    <!--    DIY定制商品选择框-->
    <el-dialog class="alls" title :visible.sync="diyDialogVisible">
      <div
        class="shop-table diy-css max-height300 rl-padding-left-default rl-padding-right-default rl-clear"
      >
        <div class="left-img rl-fl">
          <div class="diy-images">
            <img :src="diyItems.diyPic  + '?x-oss-process=image/resize,h_104,l_104'" alt />
          </div>
        </div>
        <div class="right-cons rl-fl">
          <div class="item item-name">
            <span class="rl-margin-right-default">{{$t('ShopCarts.ItemName')}}:</span>
            <span>{{diyItems.itemName}}</span>
          </div>
          <div class="item">
            <span class="rl-margin-right-default">{{$t('ShopCarts.ItemNo')}}:</span>
            <span>{{diyItems.itemCode}}</span>
          </div>
          <div class="item">
            <span class="rl-margin-right-default">{{$t('ShopCarts.Spe')}}:</span>
            <span>{{diyItems.specificationValueName}}</span>
          </div>
          <div class="item">
            <span class="rl-margin-right-default">{{$t('ShopCarts.Colors')}}:</span>
            <span>{{diyItems.colorValueName}}</span>
          </div>
          <div class="rl-clear quantity">
            <div class="rl-fl rl-margin-right-default">{{$t('ShopCarts.Quantity')}}:</div>
            <div class="rl-fl">
              <el-input-number v-model="diyNum" :min="1" label="描述文字" size="mini"></el-input-number>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="diyDialogVisible = false">{{$t('P.Cancel')}}</el-button>
        <el-button @click="diyBuy" type="danger">{{$t('P.BuyNow')}}</el-button>
        <el-button :loading="loadsing" @click="diyJoinCat" type="primary">{{$t('P.AddToCart')}}</el-button>
      </div>
    </el-dialog>
    <!--    点击图片放大-->
    <div class="cover" v-if="showImgurl"></div>
    <div class="pro-cover-img rl-relative" v-if="showImgurl">
      <div class="diy-images">
        <img :src="imgurl + '?x-oss-process=image/resize,h_200,l_400'" alt />
      </div>
      <span @click="shutLogImg" class="shut cursor-pointer"></span>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import { formatDate,  } from "@/assets/js/common.js";
import GD from "@/assets/js/globalData";
export default {
  name: "MyCustom",
  components: {
    Header,
    Left,
  },
  data() {
    return {
      imgurl: "",
      showImgurl: false,
      diyDialogVisible: false,
      userState: 2,
      totalCount: 0,
      cur_page: 1,
      pagesize: 10,
      loadsing: false,
      diyGoodsList: [],
      diyNum: 1,
      diyItems: {},
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
    };
  },
  filters: {
    keepTwoNum(value) {
      value = Number(value);
      return value.toFixed(2);
    },
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
  },
  methods: {
    fLangChange(value) {
      window.localStorage.setItem("bLang", value);
      this.$i18n.locale = value.split("-")[0];
    },
    // 当前页码
    handleCurrentChange(val) {
      this.cur_page = val;
      this.getLosegoodsList();
    },
    // 每页条数
    handleSizeChange(val) {
      this.pagesize = val;
      this.getLosegoodsList();
    },
    goOrder(item) {
      // 去下单
      this.diyNum = 1;
      this.diyDialogVisible = true;
      this.diyItems = item;
      this.loadsing = false;
    },
    // 缺货登记删除
    deleteDiy(id) {
      this.$confirm(
        this.$t("UserCenter.OperationDeleteDiy"),
        this.$t("P.Prompt"),
        {
          confirmButtonText: this.$t("Message.Confirm"),
          cancelButtonText: this.$t("P.Cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.$api
            .post(this, "user/goodsdiy/delectMyDiyWorks", { diyId: Number(id) })
            .then((res) => {
              if (res.code === 0) {
                this.$message({
                  type: "success",
                  message: this.$t("ShopCarts.SuccessfullyDeleted"),
                });
                this.getLosegoodsList();
                this.getLosegoodsListSum();
              } 
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: this.$t("P.Canceled"),
          });
        });
    },
    // diy作品集
    getLosegoodsList() {
      var myDate = new Date();
      this.$api
        .post(
          this,
          "user/goodsdiy/getMyDiyWorks?" +
            myDate.getMinutes() +
            myDate.getSeconds(),
          { page: this.cur_page, count: this.pagesize }
        )
        .then((res) => {
          if (res.code === 0) {
            this.diyGoodsList = res.data;
            this.totalCount = res.count;
          } 
        });
    },
    diyBuy() {
      this.$api
        .post(this, "user/goodsdiy/getDiyGoodsInfo", {
          diyId: 0,
          goodsCode: this.diyItems.itemCode,
          diyNum: this.diyNum,
          diyPic: this.diyItems.diyPic,
        })
        .then((res) => {
          if (res.code === 0) {
            if (res.diyItems) {
              let tempArrayOne = [];
              let songGoodsShop = [];
              let limitPurchase = [];
              let goodsId = res.diyItems.goodsId;
              Vue.set(res.diyItems, "count", 1);
              Vue.set(res.diyItems, "itemType", 3);
              Vue.set(res.diyItems, "diyId", this.diyItems.diyId);
              Vue.set(res.diyItems, "diyList", []);
              res.diyItems.count = this.diyNum;
              let product = {
                diyId: this.diyItems.diyId,
                diyNum: this.diyNum,
                diyPic: this.diyItems.diyPic,
              };
              res.diyItems.diyList.push(product);
              var limit = {
                goodsId: goodsId,
                itemId: res.diyItems.id,
                itemCount: res.diyItems.count,
                itemType: 3,
              };
              limitPurchase.push(limit);
              tempArrayOne.push(res.diyItems);
              this.$api
                .post(this, "user/u/order/restriction", {
                  goods: limitPurchase,
                })
                .then((res) => {
                  // 限购规则判断
                  if (res.code === 0) {
                    window.localStorage.setItem(
                      "songGoodsShop",
                      JSON.stringify(songGoodsShop)
                    ); // 赠品空数组
                    window.localStorage.setItem(
                      "goodsItems",
                      JSON.stringify(tempArrayOne)
                    );
                    this.$router.push({
                      name: "ConsigneeInfor",
                      query: { good_id: goodsId, goodsType: 2, values: 0 },
                    });
                  }
                });
            }
          }
        });
    },
    diyJoinCat() {
      this.loadsing = true;
      this.$api
        .post(this, "user/goodsdiy/getDiyGoodsInfo", {
          diyId: 0,
          goodsCode: this.diyItems.itemCode,
          diyNum: this.diyNum,
          diyPic: this.diyItems.diyPic,
        })
        .then((res) => {
          if (res.code === 0) {
            if (res.diyItems) {
              let diyItemList = [];
              let product = {
                diyId: this.diyItems.diyId,
                diyNum: this.diyNum,
                diyPic: this.diyItems.diyPic,
              };
              diyItemList.push(product);
              this.$api
                .post(this, "user/u/shoppingCart", {
                  goodsId: res.diyItems.goodsId,
                  itemId: this.diyItems.itemId,
                  num: this.diyNum,
                  itemType: 3,
                  orderType: 3,
                  diyList: diyItemList,
                })
                .then((res) => {
                  if (res.code === 0) {
                    this.diyDialogVisible = false;
                    if (this.$i18n.locale === "zh") {
                      this.$message.success("加入购物车成功");
                    } else {
                      this.$message.success(
                        "Add to shopping cart successfully."
                      );
                    }
                  }
                });
            }
          } else if (res.code === 3) {
            this.loadsing = false;
           
          }
        })
        .catch(() => {
          this.loadsing = false;
        });
    },
    magnify(img) {
      // 点击放大上传的图片
      this.showImgurl = true;
      this.imgurl = img;
    },
    shutLogImg() {
      this.showImgurl = false;
    },
  },
  created() {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getLosegoodsList();
  },
};
</script>
<style lang='less'>
.el-dialog__wrapper {
  .el-dialog {
    width: 600px;
    .el-dialog__header {
      text-align: center;
    }
    .el-dialog__body {
      padding: 0;
      padding-bottom: 10px;
    }
    .el-dialog__footer {
      text-align: center;
    }
  }
}
</style>
<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
    .table {
      width: 100%;
      margin-bottom: 30px;
      table {
        width: 100%;
        word-wrap: break-word;
        word-break: break-all;
        border-collapse: collapse;
        tr {
          &:hover {
            background-color: @lightGrayBg;
          }
          & + tr {
            border-top: 1px dashed @bdLighterColor;
          }
          th {
            height: 30px;
            line-height: 30px;
            text-align: center;
            background-color: @bdLightColor;
            font-size: 12px;
            color: @gray;
            font-weight: normal;
          }
          td {
            height: 50px;
            text-align: center;
            font-size: 12px;
            color: @lightBlack;
            .shop-img {
              width: 40px;
              img {
                height: 40px;
              }
            }
            .order {
              color: @blue;
              &:hover {
                opacity: 0.6;
              }
            }
            .delet {
              margin-left: 15px;
              color: @red;
              &:hover {
                opacity: 0.6;
              }
            }
          }
        }
      }
    }
  }
}
.empty-car {
  padding-bottom: 180px;
  .empty-car-img {
    margin-top: 120px;
    width: 153px;
  }
}
.diy-css {
  padding-left: 20px;
  .left-img {
    margin-right: 20px;
    width: 168px;
    height: 168px;
    .diy-images {
      display: flex;
      align-items: center;
      justify-content: center;
      img {
        max-width: 168px;
        max-height: 168px;
      }
    }
  }
  .right-cons {
    .item {
      width: 350px;
      margin-bottom: 10px;
    }
    .quantity {
      line-height: 28px;
    }
    .item-name {
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
  .buy-btn {
    text-align: center;
    background-color: @redLabel;
  }
  .join-btn {
    text-align: center;
    background-color: @blueLabel;
  }
}
.shop-img {
  display: flex;
  align-items: center;
  justify-content: center;
  img {
    height: 55px;
  }
}
/*弹框图片放大*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.6;
}
.pro-cover-img {
  padding-top: 20px;
  padding-bottom: 20px;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  width: 500px;
  height: 500px;
  background-color: #fff;
  z-index: 99;
  .diy-images {
    width: 500px;
    display: flex;
    align-items: center;
    justify-content: center;
    img {
      height: 500px;
    }
  }
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../../src/assets/images/shut.png") no-repeat center
      center;
  }
}
</style>
