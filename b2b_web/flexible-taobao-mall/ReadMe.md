# 柔性uniapp 版本
## 背景

### 	项目需求背景

​		在与印鸽等竞品对标，借助淘宝C2B商家定制功能，打入淘宝体系。为电商部门，提供商品定制化能力。后续逐步改造成插件方式，提供给淘宝第三方合作店铺平台，捞钱

### 	技术选型背景

​		本来打算沿用微信小程序的webview，嵌套现有h5网页，但是淘宝方面不提供webview组件的使用权限。开发工具中webview可用，但是预览全部拦截，所以选择重新开发小程序。本着**减少学习成本，快速移植功能**的原则，引入uniapp框架，使用vue语法快速开发。

## 开发条件

- [熟悉vue体系 以及语法]( https://cn.vuejs.org/)

- [了解uniapp的开发使用]( https://uniapp.dcloud.io/README)

- [了解淘宝小程序的开发测试流程](https://miniapp.open.taobao.com/docV3.htm?docId=119114&amp;amp;docType=1)

## 安装

- [uniapp开发工具下载](https://www.dcloud.io/hbuilderx.html)
- [阿里小程序开发工具](https://miniapp.open.taobao.com/docV3.htm?docId=119188&amp;docType=1)

在项目根目录下使用

```javascript
npm i
```

或

```javascript
yarn
```

### 	模块说明

- 重点说三个

- [vuex-persistedstate]( https://github.com/robinvdvleuten/vuex-persistedstate)
vuex的持久化插件模块，之前项目的持久化写法不够优雅，充斥着大量重复代码。
- [uview-ui](https://github.com/YanxinNet/uView)
   uniapp的ui框架组件。使用组件化开发，轻开发压力
- _server目录为淘宝小程序云函数接口备份，正常情况下小程序工具中同步云函数即可。没有的话创建云函数，并拷贝过去

## 使用

- 参考uniapp 官方文档

## 相关项目

[柔性H5]

## 主要项目负责人

bat后端开发 电话 18659783582 邮件 56873645@163.com

## 开源协议

## 20190424项目初始化

开源项目，内容请自行修改

## 特别说明

[开发规范](https://uniapp.dcloud.io/frame?id=%e5%bc%80%e5%8f%91%e8%a7%84%e8%8c%83)



## 结语

如果后续有移植安卓的需求，强烈建议使用nvue 页面写法。uniapp会渲染成原生组件