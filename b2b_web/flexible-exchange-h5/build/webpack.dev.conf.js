'use strict'
const utils = require('./utils')
const webpack = require('webpack')
const config = require('../config')
const merge = require('webpack-merge')
const path = require('path')
const baseWebpackConfig = require('./webpack.base.conf')
const CopyWebpackPlugin = require('copy-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const FriendlyErrorsPlugin = require('friendly-errors-webpack-plugin')
const SpritesmithPlugin = require('webpack-spritesmith');
const portfinder = require('portfinder')

const HOST = process.env.HOST
const PORT = process.env.PORT && Number(process.env.PORT)

// 使用2倍图，以下使用都得 / 2
var templateFunctionStylMobile = function (data) {
  var sharedRem = '.sprite-icon{\n  display: inline-block;\n  background-image: url(I);\n  background-size: Dpx Hpx;\n}'
    .replace('I', data.sprites[0].image)
    .replace('D', data.sprites[0].total_width / 2)
    .replace('H', data.sprites[0].total_height / 2);

  var perSpriteRem = data.sprites.map(function (sprite) {
    return '.icon-N{\n  width: Wpx;\n  height: Hpx;\n  background-position: Xpx Ypx;\n}'
      .replace('N', sprite.name)
      .replace('W', sprite.width / 2)
      .replace('H', sprite.height / 2)
      .replace('X', sprite.offset_x / 2)
      .replace('Y', sprite.offset_y / 2);
  }).join('\n');

  return sharedRem + '\n' + perSpriteRem;
};


const devWebpackConfig = merge(baseWebpackConfig, {
  module: {
    rules: utils.styleLoaders({ sourceMap: config.dev.cssSourceMap, usePostCSS: true })
  },
  // cheap-module-eval-source-map is faster for development
  devtool: config.dev.devtool,

  // these devServer options should be customized in /config/index.js
  devServer: {
    clientLogLevel: 'warning',
    historyApiFallback: {
      rewrites: [
        { from: /.*/, to: path.posix.join(config.dev.assetsPublicPath, 'index.html') },
      ],
    },
    hot: true,
    contentBase: false, // since we use CopyWebpackPlugin.
    compress: true,
    host: HOST || config.dev.host,
    port: PORT || config.dev.port,
    open: config.dev.autoOpenBrowser,
    overlay: config.dev.errorOverlay
      ? { warnings: false, errors: true }
      : false,
    publicPath: config.dev.assetsPublicPath,
    proxy: config.dev.proxyTable,
    quiet: true, // necessary for FriendlyErrorsPlugin
    disableHostCheck: true,
    watchOptions: {
      poll: config.dev.poll,
    }
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env': require('../config/dev.env')
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NamedModulesPlugin(), // HMR shows correct file names in console on update.
    new webpack.NoEmitOnErrorsPlugin(),
    // https://github.com/ampedandwired/html-webpack-plugin
    new HtmlWebpackPlugin({
      filename: 'index.html',
      template: 'index.html',
      inject: true
    }),
    // copy custom static assets
    new CopyWebpackPlugin([
      {
        from: path.resolve(__dirname, '../static'),
        to: config.dev.assetsSubDirectory,
        ignore: ['.*']
      }
    ]),
    new SpritesmithPlugin({
      src: {
        cwd: path.resolve(__dirname, '../src/common/icons/'),
        glob: '*.png'
      },
      target: {
        image: path.resolve(__dirname, '../src/common/images/sprite.png'),
        css: [
          [path.resolve(__dirname, '../src/common/styles/sprite.styl'), {
            format: 'function_based_template'
          }]
        ]
      },
      customTemplates: {
        'function_based_template': templateFunctionStylMobile
      },
      apiOptions: {
        cssImageRef: "../images/sprite.png"
      },
      spritesmithOptions: {
        algorithm: 'binary-tree',
        padding: 2,
      }
    })
  ],
  resolve: {
    modules: [
      'node_modules',
      'common/images/' //css在哪里能找到sprite图
    ]
  },
})

module.exports = new Promise((resolve, reject) => {
  portfinder.basePort = process.env.PORT || config.dev.port
  portfinder.getPort((err, port) => {
    if (err) {
      reject(err)
    } else {
      // publish the new Port, necessary for e2e tests
      process.env.PORT = port
      // add port to devServer config
      devWebpackConfig.devServer.port = port

      // Add FriendlyErrorsPlugin
      devWebpackConfig.plugins.push(new FriendlyErrorsPlugin({
        compilationSuccessInfo: {
          messages: [`Your application is running here: http://${devWebpackConfig.devServer.host}:${port}`],
        },
        onErrors: config.dev.notifyOnErrors
        ? utils.createNotifierCallback()
        : undefined
      }))

      resolve(devWebpackConfig)
    }
  })
})
