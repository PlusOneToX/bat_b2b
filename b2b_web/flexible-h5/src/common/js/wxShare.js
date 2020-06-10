import Vue from "vue";
import wx from "weixin-js-sdk";

import request from '../../api/api'
Vue.prototype.$request = request
import api from "common/js/allApi.js";

let distributorId = localStorage.getItem("distributorId");
let platform = localStorage.getItem("platform");
let appId = localStorage.getItem("appid");
let shopIdParams = sessionStorage.getItem("shopId") ? ("&shopId=" + sessionStorage.getItem("shopId")) : "";
let shareTitle = localStorage.getItem("shareTitle") ? localStorage.getItem("shareTitle") : "BAT商城";
let curPage = "index";
let curVersion = localStorage.getItem("curVersion");
if (curVersion && curVersion === "new") {
  // 新版，recommend
  curPage = "recommend";
}

// 微信公众号
let shareLink = "https://test.bat.com/diyh5/" + curPage + "?distributorId=" + distributorId + "&platform=" + platform + "&appid=" + appId + shopIdParams;
let shareImg = "https://test.bat.com/diyh5/images/share" + (curVersion && curVersion === "new" ? "2" : "1") + ".png";
if (process.env.NODE_ENV === "production") {
  shareLink = "https://diy.bat.com/" + curPage + "?distributorId=" + distributorId + "&platform=" + platform + "&appid=" + appId + shopIdParams;
  shareImg = "https://diy.bat.com/images/share" + (curVersion && curVersion === "new" ? "2" : "1") + ".png";
}

initShare(distributorId);

export default function wxShare() {
  if (Number(platform) === 1) {
    getShareInfo();
  }
}

// 初始化分享
function initShare(distributorId) {
  // 获取shopId
  let shopId = getUrlParams("shopId");
  if (shopId) {
    // 获取店铺信息
    getShopInfo(shopId);
  } else if (distributorId) {
    // 获取分销商信息
    getDistriInfo(distributorId);
  }

  if (appId && appId !== "null") {
    let url = window.location.href.split('#')[0];
    request.post(this, api.getWxConfig, {
      url: url,
      appId: appId
    }).then((res) => {
        wx.config({
          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
          appId: res.data.appId, // 必填，公众号的唯一标识
          timestamp: res.data.timestamp, // 必填，生成签名的时间戳，刚才接口拿到的数据
          nonceStr: res.data.nonceStr, // 必填，生成签名的随机串
          signature: res.data.signature, // 必填，签名，见附录1
          jsApiList: ["updateTimelineShareData", "updateAppMessageShareData"],
        });

        getShareInfo();
    });
  }
}

// 微信分享
function getShareInfo() {
  wx.ready(function () {
    //分享到朋友圈
    wx.updateTimelineShareData({
      title: shareTitle, // 分享标题
      link: shareLink, // 分享链接
      imgUrl: shareImg, // 分享图标
      success: function () {
        console.log("分享成功");
      },
      cancel: function () {
        console.log("取消分享");
      },
    });
    //分享给朋友
    wx.updateAppMessageShareData({
      title: shareTitle, // 分享标题
      desc: "简单几步，为您个性定制手机壳，您只需把喜欢的图片上传，就可以快速生成您的专属手机壳。", // 分享描述
      link: shareLink, // 分享链接
      imgUrl: shareImg, // 分享图标
      type: "link", // 分享类型：music、video或link，不填默认为link
      dataUrl: "",
      success: function () {
        console.log("分享成功");
      },
      cancel: function () {
        console.log("取消分享");
      },
    });
  });
}

// 获取店铺信息
function getShopInfo(id) {
  request
    .get(this, api.getShopStatus, {
      id: id,
    })
    .then((res) => {
      if (res.success) {
        let shareTitle = res.data.shopName;
        localStorage.setItem("shareTitle", shareTitle);
      } else {
        this.$toast.fail(res.errMessage);
      }
    });
}

// 获取分销商信息
function getDistriInfo(distributorId) {
  if (Number(distributorId) !== 2601) {
    request
      .get(this, api.getDistributorInfo, {
        id: distributorId,
      })
      .then((res) => {
        if (res.success) {
          let shareTitle = res.data.name;
          localStorage.setItem("shareTitle", shareTitle);
        } else {
          this.$toast.fail(res.errMessage);
        }
      });
  }
}


function getUrlParams(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    const result = unescape(r[2]);
    return result;
  }
  return null;
}


Vue.prototype.$wxShare = wxShare