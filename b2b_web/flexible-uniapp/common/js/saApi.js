export function onSamLogin(saInfo) {
    let obj = JSON.parse(saInfo);
    if (obj.loginStatus === "login") {
        uni.setStorageSync("isSaLogin", true);
    } else {
        uni.setStorageSync("isSaLogin", false);
    }
}

export function getXsaUid(ua) {
    let xSaUid = "";
    let data = ua.split(" ");
    data.forEach((item) => {
        if (item.indexOf("x-sa-uid") !== -1) {
            xSaUid = item.split("/")[1];
        }
    });
    return xSaUid;
}

export function getClientOsVersion(ua) {
    if (ua.indexOf("Android 4.4") !== -1) {
        return 19;
    } else if (ua.indexOf("Android 5.0") !== -1) {
        return 21;
    } else if (ua.indexOf("Android 5.1") !== -1) {
        return 22;
    } else if (ua.indexOf("Android 6.0") !== -1) {
        return 23;
    } else if (ua.indexOf("Android 7.0") !== -1) {
        return 24;
    } else if (ua.indexOf("Android 7.1") !== -1) {
        return 25;
    } else if (ua.indexOf("Android 8.0") !== -1) {
        return 26;
    } else if (ua.indexOf("Android 8.1") !== -1) {
        return 27;
    } else if (ua.indexOf("Android 9") !== -1) {
        return 28;
    } else {
        return 28;
    }
}

export function getClientModel(ua) {
    var model = "";
    var data = ua.split(" ");
    for (let i = 0; i < data.length; i++) {
        if (data[i].indexOf("SM-") !== -1) {
            model = data[i];
        }
    }
    return model;
}

export function getPackageName() {
    return "com.samsung.android.app.sreminder";
}

export function getPackageVersion(ua) {
    var version = "";
    var data = ua.split(" ");
    for (let x = 0; x < data.length; x++) {
        if (data[x].indexOf("SamsungLifeService") !== -1) {
            var str = data[x];
            var code = str.split("/")[1].split(".");
            version =
                code[0] +
                code[1] +
                ("00" + code[2]).substr(-2) +
                ("000" + code[3]).substr(-3);
        }
    }
    return version;
}
