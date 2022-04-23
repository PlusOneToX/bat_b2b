const util = require('../../utils/util.js');

Page({
    data: {
        // text:"这是一个页面"
        topicList: [],
        page: 1,
        size: 10,
        total: 0,
        scrollTop: 0,
        showPage: false
    },
    onLoad: function (options) {
        // 页面初始化 options为页面跳转所带来的参数
        this.getTopic();

    },
    onReady: function () {
        // 页面渲染完成
    },
    onShow: function () {
        // 页面显示
    },
    onHide: function () {
        // 页面隐藏
    },
    onUnload: function () {
        // 页面关闭
    },
    nextPage: function () {
        var that = this;
        if (this.data.page > that.data.total / that.data.size) {
            return true;
        }
        that.setData({
            "page": parseInt(that.data.page) + 1
        });
        this.getTopic();
    },
    getTopic: function () {
        let that = this;
        that.setData({
            scrollTop: 0,
            showPage: false,
            topicList: []
        });
        util.request('topic/list', {
            page: that.data.page,
            limit: that.data.size
        }).then(res => {
            if (res.code === 0) {
                that.setData({
                    scrollTop: 0,
                    topicList: res.data.records,
                    showPage: true,
                    total: res.data.total
                });
            }
        });
    },
    prevPage: function (event) {
        if (this.data.page <= 1) {
            return false;
        }

        var that = this;
        that.setData({
            "page": parseInt(that.data.page) - 1
        });
        this.getTopic();
    }
})
