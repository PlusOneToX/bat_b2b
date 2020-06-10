import App from "./App";
import store from "./store";
import api from "./api/api";

// #ifndef VUE3
import Vue from "vue";
Vue.config.productionTip = false;
Vue.prototype.$store = store;
Vue.prototype.$adpid = "1111111111";
Vue.prototype.$backgroundAudioData = {
    playing: false,
    playTime: 0,
    formatedPlayTime: "00:00:00",
};
Vue.prototype.$api = api;
App.mpType = "app";
Vue.mixin({
    methods: {
        setData: function (obj, callback) {
            let that = this;
            const handleData = (tepData, tepKey, afterKey) => {
                tepKey = tepKey.split(".");
                tepKey.forEach((item) => {
                    if (tepData[item] === null || tepData[item] === undefined) {
                        let reg = /^[0-9]+$/;
                        tepData[item] = reg.test(afterKey) ? [] : {};
                        tepData = tepData[item];
                    } else {
                        tepData = tepData[item];
                    }
                });
                return tepData;
            };
            const isFn = function (value) {
                return typeof value == "function" || false;
            };
            Object.keys(obj).forEach(function (key) {
                let val = obj[key];
                key = key.replace(/\]/g, "").replace(/\[/g, ".");
                let front, after;
                let index_after = key.lastIndexOf(".");
                if (index_after != -1) {
                    after = key.slice(index_after + 1);
                    front = handleData(that, key.slice(0, index_after), after);
                } else {
                    after = key;
                    front = that;
                }
                if (front.$data && front.$data[after] === undefined) {
                    Object.defineProperty(front, after, {
                        get() {
                            return front.$data[after];
                        },
                        set(newValue) {
                            front.$data[after] = newValue;
                            that.$forceUpdate();
                        },
                        enumerable: true,
                        configurable: true,
                    });
                    front[after] = val;
                } else {
                    that.$set(front, after, val);
                }
            });
            // this.$forceUpdate();
            isFn(callback) && this.$nextTick(callback);
        },
    },
});
const app = new Vue({
    store,
    ...App,
});
app.$mount();
// #endif

// #ifdef VUE3
import { createSSRApp } from "vue";

export function createApp() {
    const app = createSSRApp(App);
    app.use(store);
    app.config.globalProperties.$adpid = "1111111111";
    app.config.globalProperties.$backgroundAudioData = {
        playing: false,
        playTime: 0,
        formatedPlayTime: "00:00:00",
    };
    return {
        app,
    };
}
// #endif
