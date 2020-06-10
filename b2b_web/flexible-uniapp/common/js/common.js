import EXIF from "exif-js";

export function formatDate(date, fmt) {
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
            RegExp.$1,
            (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
    }
    let o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
    };
    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + "";
            fmt = fmt.replace(
                RegExp.$1,
                RegExp.$1.length === 1 ? str : padLeftZero(str)
            );
        }
    }
    return fmt;
}

function padLeftZero(str) {
    return ("00" + str).substr(str.length);
}

// num为传入的值，n为保留的小数位
export function fomatFloat(num, n) {
    var f = parseFloat(num);
    if (isNaN(f)) {
        return false;
    }
    f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
    var s = f.toString();
    var rs = s.indexOf(".");
    // 判定如果是整数，增加小数点再补0
    if (rs < 0) {
        rs = s.length;
        s += ".";
    }
    while (s.length <= rs + n) {
        s += "0";
    }
    return s;
}

// 倒计时
export function countDown(end) {
    end--;
    // 当前时间
    let now = new Date().getTime();
    let min, second;
    // 时间差
    let leftTime = end - now;

    if (leftTime > 0) {
        // 分
        let m = Math.floor((leftTime / 1000 / 60) % 60);
        min = m < 10 ? "0" + m : m;
        // 秒
        let s = Math.floor((leftTime / 1000) % 60);
        second = s < 10 ? "0" + s : s;
        return min + ":" + second;
    } else {
        return "";
    }
}

// 从xml中获取节点内容
export function getXmlNode(str, name) {
    // 创建文档对象
    let parser = new DOMParser();
    let xmlDoc = parser.parseFromString(str, "text/xml");
    // 提取数据
    var countrys = xmlDoc.getElementsByTagName(name);
    let arr = [];
    for (var i = 0; i < countrys.length; i++) {
        arr.push(countrys[i].textContent);
    }
    return arr;
}

// 解决IOS键盘弹出问题
export function inputBlur() {
    let u = navigator.userAgent;
    let isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    if (isIOS) {
        setTimeout(() => {
            const scrollHeight =
                document.documentElement.scrollTop ||
                document.body.scrollTop ||
                0;
            window.scrollTo(0, Math.max(scrollHeight - 1, 0));
        }, 200);
    }
}

// 图片旋转
export function handleImgRotate(target) {
    let img;
    let canvas = document.createElement("canvas");
    let ctx = canvas.getContext("2d");
    let orientation = null;
    let imgObj = target;
    // 图片压缩
    if (imgObj && imgObj.width > 1000 && imgObj.height > 1000) {
        imgObj.width = imgObj.width / 6;
        imgObj.height = imgObj.height / 6;
    }

    EXIF.getData(imgObj, function () {
        // 获取某个数据方向参数
        orientation = EXIF.getTag(this, "Orientation");
        // 为6的时候，图片需宽高反转
        if (orientation && orientation === 6 && imgObj.width > imgObj.height) {
            canvas.width = imgObj.height;
            canvas.height = imgObj.width;
        } else {
            canvas.width = imgObj.width;
            canvas.height = imgObj.height;
        }
        ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
        if (orientation) {
            // 为1，正常
            switch (Number(orientation)) {
                case 3: // 需要180度旋转
                    ctx.rotate((180 * Math.PI) / 180);
                    ctx.drawImage(
                        imgObj,
                        -imgObj.width,
                        -imgObj.height,
                        imgObj.width,
                        imgObj.height
                    );
                    break;
                case 6: // 需要顺时针（向左）90度旋转
                    ctx.rotate((90 * Math.PI) / 180);
                    ctx.drawImage(imgObj, 0, 0, imgObj.width, -imgObj.height);
                    break;
                case 8: // 需要逆时针（向右）90度旋转
                    ctx.rotate((270 * Math.PI) / 180);
                    ctx.drawImage(
                        imgObj,
                        -imgObj.height,
                        0,
                        imgObj.height,
                        imgObj.width
                    );
                    break;
                default:
                    ctx.drawImage(imgObj, 0, 0, imgObj.width, imgObj.height);
                    break;
            }
        }
        img = canvas.toDataURL("image/jpeg");
    });
    return img;
}

export function setLocalStorageItem(key, value) {
    var curtime = new Date().getTime(); //获取当前时间
    uni.setStorageSync(key, JSON.stringify({ val: value, time: curtime })); //转换成json字符串序列
}
export function getLocalStorageItem(key) {
    //exp是设置的过期时间
    var val = uni.getStorageSync(key); //获取存储的元素
    if (val) {
        try {
            var dataobj = JSON.parse(val); //解析出json对象
            if (dataobj.time) {
                //如果当前时间-减去存储的元素在创建时候设置的时间 > 过期时间(单位毫秒)
                // if (new Date().getTime() - dataobj.time > 60 * 1000) {
                if (new Date().getTime() - dataobj.time > 2 * 60 * 60 * 1000) {
                    uni.removeStorage(key);
                    return uni.getStorageSync(key);
                } else {
                    // 重新设置计算有效时间
                    setLocalStorageItem(key, dataobj.val);
                    return dataobj.val;
                }
            } else {
                uni.removeStorage(key);
                return uni.getStorageSync(key);
            }
        } catch (e) {
            uni.removeStorage(key);
            return uni.getStorageSync(key);
        }
    } else {
        return uni.getStorageSync(key);
    }
}
