import axios from "axios";
import { setToken, removeToken } from "./auth";

const service = axios.create({
    timeout: 60000, // 请求超时时间
    headers: {
        "Access-Control-Allow-Origin": "*",
        Accept: "application/json",
        "Content-Type": "application/json",
        Version: "1.0.0",
        Authorization: "0",
        "Cache-Control": "no-cache",
        Pragma: "no-cache",
        appkey: "10A00C1CD1F04F058BD09F55F6A76877",
        openId: "0",
    },
    withCredentials: false,
});

// request拦截器，两个参数function,第一个在请求发送之前做一些事,第二个当出现请求错误时做一些事
service.interceptors.request.use(
    (config) => {
        // 判断请求 url 是否包含域名
        if (
            config.url.indexOf("http://") < 0 &&
            config.url.indexOf("https://") < 0
        ) {
            config.url = uni.getStorageSync("tenantUrl") + config.url;
        }
        // 非获取租户地址
        if (
            config.url.indexOf("platform/v1/web/tenant/url") < 0 &&
            config.url.indexOf(
                "distributor/v1/web/user/customer/thirdparty/login"
            ) < 0
        ) {
            // 判断缓存是否有auth
            if (
                uni.getStorageSync("auth") &&
                uni.getStorageSync("auth") !== "undefined"
            ) {
                config.headers.token = uni.getStorageSync("auth")
                    ? uni.getStorageSync("auth")
                    : "";
            }
        }
        //判断缓存是否店铺编码
        if (
            uni.getStorageSync("shopCode") &&
            uni.getStorageSync("shopCode") !== "undefined" &&
            uni.getStorageSync("shopCode") !== ""
        ) {
            config.headers.shopCode = uni.getStorageSync("shopCode");
        }
        config.headers.platform = uni.getStorageSync("platform")
            ? uni.getStorageSync("platform")
            : "6";
        config.headers.orderSource = uni.getStorageSync("orderSource")
            ? uni.getStorageSync("orderSource")
            : uni.getStorageSync("platform");
        config.headers.openId = uni.getStorageSync("openId")
            ? uni.getStorageSync("openId")
            : "0";
        config.headers.distributorId = uni.getStorageSync("distributorId")
            ? uni.getStorageSync("distributorId")
            : "6182"; //6182
        // 租户编码
        config.headers.tenantNo = uni.getStorageSync("tenantNo")
            ? uni.getStorageSync("tenantNo")
            : "";
        return config;
    },
    (error) => {
        // Do something with request error
        console.log(error); // for debug

        Promise.reject(error);
    }
);

// respone拦截器
service.interceptors.response.use(
    (response) => {
        const res = response.data;
        const headers = response.header || response.headers;

        // 获取返回数据请求头 token
        if (headers["token"]) {
            setToken(headers["token"]);
        }

        if (
            headers["content-type"] === "application/octet-stream;charset=utf-8"
        ) {
            // 请求头类型：数据流
            return response.data;
        } else {
            return res;
        }
    },
    (error) => {
        if (error.toString().indexOf("Error: timeout") !== -1) {
            uni.showToast({
                icon: "none",
                title: "网络请求超时，请稍后再试",
                duration: 2000,
            });
            return Promise.reject(errorq);
        }

        if (error.toString().indexOf("Error: Network Error") !== -1) {
            uni.showToast({
                icon: "none",
                title: "网络请求错误，请稍后再试",
                duration: 2000,
            });
            return Promise.reject(errorq);
        }
        if (error.response) {
            const err = error.response.data;
            let platform = uni.getStorageSync("platform");
            if (platform === 7 && err.errCode === "B_AUTH_FAIL") {
                uni.setStorageSync("auth", "");
                uni.redirectTo({
                    url:
                        "/good_pages/login/login?platform=" +
                        uni.getStorageSync("platform") +
                        "&distributorId=" +
                        uni.getStorageSync("distributorId") +
                        "&sa=1",
                });
            } else if (
                err.errCode === "B_AUTH_FAIL" &&
                !uni.getStorageSync("IswebView")
            ) {
                uni.setStorageSync("auth", "");
                if (!uni.getStorageSync("IswebView")) {
                    uni.switchTab({
                        url: "/src/components/index/index",
                    });
                }
            } else {
                uni.setStorageSync("auth", "");
                if (err.errMessage != "用户信息已过期，请重新登录") {
                    uni.showToast({
                        title: err.errMessage,
                        icon: "none",
                        duration: 2000,
                    });
                }
            }
        }
        return Promise.reject(error.response.data);
    }
);

export default service;
