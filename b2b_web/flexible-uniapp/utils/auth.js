const TokenKey = "auth";

export function getToken() {
    return uni.getStorageSync(TokenKey) ? uni.getStorageSync(TokenKey) : "";
}

export function setToken(token) {
    return uni.setStorageSync(TokenKey, token);
}

export function removeToken() {
    return uni.removeStorage(TokenKey);
}
