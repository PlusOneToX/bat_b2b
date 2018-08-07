<template>
  <transition name="el-notification-fade">
    <div
      :class="['el-notification', customClass, horizontalClass]"
      v-show="visible"
      :style="positionStyle"
      @mouseenter="clearTimer()"
      @mouseleave="startTimer()"
      @click="click"
      role="alert"
    >
      <i
        class="el-notification__icon"
        :class="[ typeClass, iconClass ]"
        v-if="type || iconClass">
      </i>
    <div style="width:100%">
      <div class="content el-notification__group" :class="{ 'is-with-icon': typeClass || iconClass }">
        <h2 class="el-notification__title" v-text="title"></h2>
        <!-- <div class="el-notification__content" v-show="message">
          <slot>
            <p v-if="!dangerouslyUseHTMLString">{{ message }}</p>
            <p v-else v-html="message"></p>
          </slot>
        </div> -->
        <el-progress v-if="showProgress" style="width:200px" :percentage="percentage"></el-progress>
    </div>
    <div
          class="close el-icon-close"
          v-if="showClose"
          @click.stop="close"></div>
        </div>
    </div>
  </transition>
</template>

<script type="text/babel">
  let typeMap = {
    success: 'success',
    info: 'info',
    warning: 'warning',
    error: 'error'
  };
  export default {
    data () {
      return {
        visible: false,
        title: '报价单正在计算中',
        message: '',
        duration: 0,
        type: '',
        showClose: false,
        customClass: '',
        iconClass: '',
        percentage: 0,
        onClose: null,
        onClick: null,
        closed: false,
        verticalOffset: 0,
        timer: null,
        dangerouslyUseHTMLString: false,
        position: 'bottom-left',
        form: undefined,
        showProgress: false,
        finish: false,
        locale: 'zh'
      };
    },

    computed: {
      typeClass () {
        return this.type && typeMap[this.type] ? `el-icon-${ typeMap[this.type] }` : '';
      },

      horizontalClass () {
        return this.position.indexOf('right') > -1 ? 'right' : 'left';
      },

      verticalProperty () {
        return /^top-/.test(this.position) ? 'top' : 'bottom';
      },

      positionStyle () {
        return {
          [this.verticalProperty]: `${ this.verticalOffset }px`
        };
      }
    },
    created () {
      if (this.locale === 'zh') {
        this.title = '报价单正在计算中'
      } else {
        this.title = 'The quotation is being calculated'
      }
    },
    watch: {
      form (val) {
        this.startTimer()
        this.startLoading()
      },
      closed (newVal) {
        if (newVal) {
          this.visible = false;
          this.$el.addEventListener('transitionend', this.destroyElement);
          this.$loadingProgress = null
        }
      }
    },

    methods: {
      startLoading () {
         this.$api.exportData(this, 'user/u/user/goods/exportPriceExcel', this.form).then(res => {
          //  let blob = new Blob([res], {type: res.type})
          //   let downloadElement = document.createElement('a')
          //   let href = window.URL.createObjectURL(blob); //创建下载的链接
          //   downloadElement.href = href;
          //   downloadElement.download = '报价单.xls' //下载后文件名
          //   document.body.appendChild(downloadElement);
          //   downloadElement.click(); //点击下载
          //   document.body.removeChild(downloadElement); //下载完成移除元素
          //   window.URL.revokeObjectURL(href); //释放blob对象
          if (this.locale === 'zh') {
            this.title = '报价单下载完成'
          } else {
            this.title = 'The quotation is downloaded already'
          }
          this.finish = true
          if (res === undefined || res.code === undefined) {
            this.timeData = [] // ..成功下载Excel后初始化选中时间
            // this.formData.status = [] //..成功夏侯Excel后初始化订单状态
          }
          const content = res
          let blob = new Blob([content], {
            type: 'application/ms-excel'
          })
          let url = window.URL.createObjectURL(blob)
          if ('download' in document.createElement('a')) {
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            if (this.locale === 'zh') {
              link.setAttribute('download', '报价单.xls')
            } else {
              link.setAttribute('download', 'Quotation.xls')
            }
            document.body.appendChild(link)
            link.click()
          } else {
            if (this.locale === 'zh') {
              navigator.msSaveBlob(blob, '报价单.xls')
            } else {
              navigator.msSaveBlob(blob, 'Quotation.xls')
            }
          }
          this.close()
        })
      },
      destroyElement () {
        this.$el.removeEventListener('transitionend', this.destroyElement);
        this.$destroy(true);
        this.$el.parentNode.removeChild(this.$el);
      },

      click () {
        if (typeof this.onClick === 'function') {
          this.onClick();
        }
      },

      close () {
        this.closed = true;
        if (typeof this.onClose === 'function') {
          this.onClose();
        }
        this.closeProgress()
      },

      clearTimer () {
        if (this.timer !== undefined && this.timer !== null) {
          clearInterval(this.timer);
        }
      },
      startTimer () {
        // this.timer = setInterval(() => {
        //   this.$api.get(this, 'user/u/user/goods/exportPriceExcel/flushProgress').then(res => {
        //     if (res.code === 0) {
        //       this.percentage = res.data.percent
        //       if (this.percentage === 0) {
        //         if (this.locale === 'zh') {
        //           this.title = '报价单正在计算中'
        //         } else {
        //           this.title = 'The quotation is being calculated'
        //         }
        //         this.showClose = false
        //         this.showProgress = false
        //         this.finish = false
        //       } else if (this.percentage > 0 && this.percentage < 100) {
        //         if (this.locale === 'zh') {
        //           this.title = '报价单正在下载中'
        //         } else {
        //           this.title = 'The quotation is being downloaded'
        //         }
        //         this.showClose = false
        //         this.showProgress = true
        //         this.finish = false
        //       } else if (this.percentage === 100) {
        //         this.clearTimer()
        //         if (this.locale === 'zh') {
        //           this.title = '报价单下载完成'
        //         } else {
        //           this.title = 'The quotation is downloaded already'
        //         }
        //         this.showClose = true
        //         this.showProgress = true
        //         this.finish = true
        //       }
        //     } else {
        //       if (this.locale === 'zh') {
        //         this.title = '报价单下载失败'
        //       } else {
        //         this.title = 'The quotation is downloaded fail'
        //       }
        //       this.showClose = true
        //       this.showProgress = false
        //       this.finish = true
        //     }
        //   })
        // }, 6000);
      },
      closeProgress () {
        this.clearTimer();
      },
      keydown (e) {
        if (e.keyCode === 46 || e.keyCode === 8) {
          this.clearTimer(); // detele 取消倒计时
        } else if (e.keyCode === 27) { // esc关闭消息
          if (!this.closed) {
            this.close();
          }
        } else {
          this.startTimer(); // 恢复倒计时
        }
      }
    },
    mounted () {
      if (this.duration > 0) {
        this.timer = setTimeout(() => {
          if (!this.closed) {
            this.close();
          }
        }, this.duration);
      }
      document.addEventListener('keydown', this.keydown);
    },
    beforeDestroy () {
      document.removeEventListener('keydown', this.keydown);
    }
  };
</script>
<style lang="less" scoped>
  .close{
    float:right;
    cursor:pointer;
  }
  .content{
    float: left;
  }
</style>