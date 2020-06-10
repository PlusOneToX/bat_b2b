// vue.config.js
if (process.env.UNI_PLATFORM === "h5") {
    // 由于这种方式的打包，会导致编译生成微信小程序（只验证了微信小程序）无法正常使用，所以必须分开
    let filePath = "./static/js/";
    let Timestamp = new Date().getTime();
    module.exports = {
        // ... webpack 相关配置
        filenameHashing: false,
        configureWebpack: {
            // webpack 配置 解决js缓存的问题，目前只适配H5端打包
            output: {
                // 输出重构  打包编译后的 文件目录 文件名称 【模块名称.时间戳】
                filename: `${filePath}[name]-${Timestamp}.js`,
                chunkFilename: `${filePath}[name]-${Timestamp}.js`,
            },
        },
    };
} else {
    // 其他打包需要的相关配置
    module.exports = {
        // ... webpack 相关配置
        filenameHashing: false,
    };
}
