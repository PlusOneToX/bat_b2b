import { getToken } from "@/utils/auth";
import {
    SET_USERNO,
    SET_PHONE,
    SET_USERID,
    SET_NIKENAME,
    SET_AVATAR,
    SET_STATUS,
    SET_TOKEN,
} from "../mutation-type";

const user = {
    state: {
        userno: "",
        phone: "",
        userId: "",
        nikename: "",
        avatar: "",
        auth: getToken(),
        token: getToken(),
        status: "",
        firstLoadOSS: true, // 标记第一次加载 OSS JS
        firstLoadCanva: true, // 标记第一次加载 Canva JS
        canvaApi: "",
        pdfImg: "", // 切图
        spriteArr: [], // Canvas 图层信息
    },
    mutations: {
        [SET_USERNO]: (state, userno) => {
            state.userno = userno;
        },
        [SET_PHONE]: (state, phone) => {
            state.phone = phone;
        },
        [SET_USERID]: (state, userId) => {
            state.userId = userId;
        },
        [SET_NIKENAME]: (state, nikename) => {
            state.nikename = nikename;
        },
        [SET_AVATAR]: (state, avatar) => {
            state.avatar = avatar;
        },
        [SET_STATUS]: (state, status) => {
            state.status = status;
        },
        [SET_TOKEN]: (state, token) => {
            state.auth = token;
            state.token = token;
        },
        // 标记第一次加载 OSS JS
        handleNotFirstLoadOSS(state) {
            state.firstLoadOSS = false;
        },
        // 标记第一次加载 Canva JS
        handleNotFirstLoadCanva(state) {
            state.firstLoadCanva = false;
        },
    },
    getters: {
        userno() {
            return user.state.userno;
        },
        phone() {
            return user.state.phone;
        },
        userId() {
            return user.state.userId;
        },
        nikename() {
            return user.state.nikename;
        },
        avatar() {
            return user.state.avatar;
        },
        auth() {
            return user.state.auth;
        },
        token() {
            return user.state.token;
        },
        status() {
            return user.state.status;
        },
        timer() {
            return user.state.timer;
        },
    },
    actions: {},
};

export default user;
