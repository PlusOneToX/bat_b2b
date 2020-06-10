// Api
import $request from '/assets/api/request'
import api from '/assets/api/allApi'

Component({
  data: {
    noticeData: "", // 通知内容
    speed: 0, // 速度
  },
  properties: {
    showFlag: {
      type: String,
      value: ''
    }
  },
  methods: {
    // 获取公告
    initNotice() {
      $request.get(api.getNoticeList).then((res) => {
        if (res.success) {
          if (res.data && res.data.length > 0) {
            let noticeData = res.data;
            let notice = "";
            // 获取当前时间戳
            let now = new Date().getTime();
            noticeData.forEach((item) => {
              if (
                item.status === 1 &&
                now >= item.startTime &&
                now <= item.endTime
              ) {
                // 判断大于开始时间并且小于结束时间，拼接公告内容
                notice += item.content;
              }
            });
            this.setData({
              noticeData: notice
            })

            if (this.data.noticeData) {
              this.setData({
                showNotice: true
              })

              setTimeout(() => {
                // 公告滚动
                this.scrollNotice();
              }, 500);
            } else {
              this.setData({
                showNotice: false
              })
            }
          } else {
            this.setData({
              showNotice: false
            })
          }
        } else {
          my.showToast({
            content: res.errMessage,
            type: "none",
            duration: 2000,
          });
        }
      });
    },
    // 公告滚动
    scrollNotice() {
      let that = this;
      my.createSelectorQuery().selectAll('#marquee').boundingClientRect(function (rect) {
        // 获取内容区宽度
        let width = rect[0].width;
        let speed = 0; // 位移距离
        // 设置位移
        setInterval(function () {
          speed = speed - 1;
          // 如果位移超过文字宽度，则回到起点
          if (-speed >= width) {
            speed = my.getSystemInfoSync().windowWidth - 15;
          }
          that.setData({
            speed: speed
          })
        }, 35);
      }).exec();
    },
  },

  ready: function() { 
    this.initNotice();
  },
})