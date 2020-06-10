const path = require('path');
const webpack = require('webpack');
module.exports = {
  entry: {
    vendor: ['vue/dist/vue.esm', 'vue-router', 'vuex'] //这里把vue,vue-router,vuex提取出来，可以再添加
  },
  output: {
    path: path.join(__dirname, '../static/js'),
    filename: '[name].dll.js',
    library: "[name]_library" //这个名称和下面的name值必须一样
  },
  plugins: [
    new webpack.DllPlugin({
      path: path.join(__dirname, "../static/js/vendor-manifest.json"),
      name: "[name]_library" //这个名称和上面的library值必须一样
    })
  ]
}
