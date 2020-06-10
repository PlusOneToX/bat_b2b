<template>
    <view></view>
</template>

<script>
import api from "common/js/allApi.js";
export default {
    data() {
        return {

        };
    },

    onLoad() {


        const oScript = document.createElement('script');
        oScript.type = 'text/javascript';
        oScript.src = 'https://gw.alipayobjects.com/as/g/h5-lib/alipayjsapi/3.1.1/alipayjsapi.min.js';
        document.body.appendChild(oScript)

        var auth_code = this.getUrlCode().auth_code;
        var shopCode = this.getUrlCode().shopCode;
        var staffCode = this.getUrlCode().staffCode;
        var distributorId = this.getUrlCode().distributorId;
        var app_id = this.getUrlCode().app_id;
        this.authorization(distributorId, auth_code, shopCode, staffCode, app_id);
    },
    methods: {
        authorization(distributorId, auth_code, shopCode, staffCode, app_id) {
            this.$api.put(this, api.authorization, {
                distributorId: distributorId,
                openIds: [
                    {
                        appId: app_id,
                        openId: auth_code,
                        openType: 3
                    }
                ],
                shopCode: shopCode,
                staffCode: staffCode
            }).then((res) => {
                uni.showToast({
                    icon: "none",
                    title: "授权成功",
                    duration: 2000,
                });
                setTimeout(function () {
                    ap.popWindow();
                }, 2000);

                console.log("授权回调：", res);
            })
        },
        getUrlCode() {
            let url = location.search;
            /* eslint-disable */
            let theRequest = new Object();
            if (url.indexOf("?") !== -1) {
                let str = url.substr(1);
                let strs = str.split("&");
                for (let i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
                }
                return theRequest;
            }
        },

    },
};

</script>
