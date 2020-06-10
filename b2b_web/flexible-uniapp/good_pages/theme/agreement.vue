<template>
    <view class="protocol">
        <view class="container">
            此处请放置用户协议文件内容
        </view>
    </view>
</template>
  
  <script >
export default {
    name: 'protocol',
    data() {
        return {
            title: '用户协议',
            userInfo: {},
            count: 5,
            timer: null,
            show: true,
            confirm: 0,
        }
    },
    mounted() {

    },
    methods: {
        // 同意协议
        handleSubmit() {
            this.$emit('agree');
        },
        toback() {
            this.$emit('close')
        }
    },

}
  </script>
  
  <style scoped lang="scss">
.protocol {
    position: fixed;
    width: 100%;
    top: 0rpx;
    bottom: 0;
    background-color: #ffffff;
    z-index: 99;

    .container {
        height: 100%;
        padding: 30rpx 80rpx 240rpx;
        box-sizing: border-box;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;
        color: #4a4a4a;
        font-size: 28rpx;
        text-align: justify;
        line-height: 1.5;

        &::-webkit-scrollbar {
            display: none;
        }

        view {
            font-size: 44rpx;
            font-weight: bold;
        }

        view {
            font-size: 28rpx;
            font-weight: bold;
            line-height: 1.8;
        }

        view {
            font-size: 28rpx;
            margin-bottom: 20rpx;

            .bold {
                font-weight: bold;
            }

            .no {
                display: inline-block;
            }
        }

        .text-indent-double {
            text-indent: 40rpx;
        }

        .margin-l-double {
            margin-left: 20rpx;
        }

        .margin-b-double {
            margin-bottom: 60rpx;
        }

        .margin-b-m {
            margin-bottom: 30rpx;
        }

        .text-align-r {
            text-align: right;
        }

        .text-align-c {
            text-align: center;
        }

        .bold {
            font-weight: bold;
        }
    }

    .btn-box {
        position: fixed;
        left: 0;
        right: 0;
        bottom: 30px;
        text-align: center;
    }
}
</style>
  