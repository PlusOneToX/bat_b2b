<template>
    <view>
        <uni-popup ref="popup" type="bottom" :safeArea="false" @maskClick="close">
            <view class="sele_img_box">
                <view class="sele_img_title_box">
                    <view>添加文字</view>
                    <uni-icons type="closeempty" size="26" @click="close"></uni-icons>
                </view>
                <view class="input_item">
                    <view class="input_meu">
                        <input placeholder=" 请输入文字" v-model="value" />
                    </view>
                    <view class="bit_ok" @click="SetWord">确认</view>
                </view>
                <view :style="wordStyle" class="word_style_item">
                    <view :style="{'color':pickerColor,fontFamily: fontFile,fontWeight:wordStyle.fontWeight,textDecoration:wordStyle.textDecoration, fontStyle:wordStyle.fontStyle}">{{value}}</view>
                </view>
                <view class="word_style_box">
                    <view class="word_meu" :class="!IsseleceWord?'selece_word':''" @click="IsseleceWord=false">字体</view>
                    <view class="word_meu" :class="IsseleceWord?'selece_word':''" @click="IsseleceWord=true">样式</view>
                </view>
                <view v-if="!IsseleceWord">
                    <scroll-view class="scroll_view_y" :show-scrollbar="false" :scroll-y="true">
                        <view class="coverage_list_box" v-for="(item,index) in fontFamily" :key="index" @click="cilickFonm(item)" v-if="item.openFlag==1">
                            <view class="let_box" :class="fontFile==item.englishName?'cilckColor':''" :style="{fontFamily: item.englishName}">
                                {{item.name}}
                            </view>
                            <view class="right_box">
                            </view>
                        </view>
                    </scroll-view>
                </view>
                <view v-if="IsseleceWord">
                    <view class="color">
                        <view class="pop">
                            <scroll-view class="scroll_view_y1" :show-scrollbar="false" :scroll-y="true">
                                <view class="list flex_col" v-for="(item,index) in colorArr" :key="index">
                                    <view v-for="(v,i) in item" :key="i" :style="{'backgroundColor':v}" :data-color="v" :data-index="index" :data-i="i" :class="{'active':(index==pickerArr[0] && i==pickerArr[1])}" @tap="picker(v)"></view>
                                </view>
                            </scroll-view>
                        </view>
                    </view>
                    <view class="typeface_style_box">
                        <view class="typeface_style_meu" @click="overstriking">加粗</view>
                        <view class="typeface_style_meu" @click="incline">斜体</view>
                        <view class="typeface_style_meu" @click="underline">下滑</view>
                    </view>
                </view>
            </view>
        </uni-popup>
    </view>
</template>
<script>
import api from "../../../api/allApi";
export default {
    props: {
        TextPopup: {
            type: Boolean,
            default: '',
        }
    },
    data() {
        return {
            imgUrl: '',
            colorArr: [
                ['#000000', '#111111', '#222222', '#333333', '#444444', '#666666', '#999999', '#CCCCCC', '#EEEEEE', '#FFFFFF'],
                ['#ff0000', '#ff0033', '#ff3399', '#ff33cc', '#cc00ff', '#9900ff', '#cc00cc', '#cc0099', '#cc3399', '#cc0066'],
                ['#cc3300', '#cc6600', '#ff9933', '#ff9966', '#ff9999', '#ff99cc', '#ff99ff', '#cc66ff', '#9966ff', '#cc33ff'],
                ['#663300', '#996600', '#996633', '#cc9900', '#a58800', '#cccc00', '#ffff66', '#ffff99', '#ffffcc', '#ffcccc'],
                ['#336600', '#669900', '#009900', '#009933', '#00cc00', '#66ff66', '#339933', '#339966', '#009999', '#33cccc'],
                ['#003366', '#336699', '#3366cc', '#0099ff', '#000099', '#0000cc', '#660066', '#993366', '#993333', '#800000']
            ],
            pickerColor: '#000000',
            pickerArr: [-1, -1],
            IsseleceWord: true,
            fontFamily: [],
            fontFile: '',
            fontUrl: '',
            value: '',
            wordStyle: {
                fontWeight: 500,
                fontStyle: 'normal',//italic	浏览器会显示一个斜体的字体样式
                textDecoration: 'none',//underline 下划线
                textAlign: 'left',
                fontFile: ''
            }

        }
    },
    watch: {
        TextPopup: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.open();
                }
            },
            immediate: true
        }
    },
    mounted() {
        // this.$refs.popup.open('bottom');
        this.getFont();
    },
    methods: {
        picker(e) {
            this.pickerColor = e;
        },
        cilickFonm(item) {
            this.fontFile = item.englishName;
            this.fontUrl = item.fontFile;
        },
        // 获取字体列表
        getFont() {
            this.$api.get(this, api.getFontList).then((res) => {
                if (res.success) {
                    if (res.data && res.data.length > 0) {
                        this.fontFamily = res.data;
                        this.fontFile = this.fontFamily[0].englishName;
                        this.fontUrl = this.fontFamily[0].fontFile;
                        // this.fontFamily.forEach(item => {
                        //     uni.loadFontFace({
                        //         // global: true, // 开启全局生效
                        //         family: item.englishName,
                        //         source: item.fontFile,
                        //         scopes: ["webview", "native"],
                        //         success() {
                        //             console.log('success');
                        //         }
                        //     })
                        // });
                        console.log("字体列表：", res);
                    }
                }
            });
        },

        SetWord() {
            this.wordStyle.fontFile = this.fontFile;
            uni.request({
                url: api.txtPicture,
                data: {
                    bold: this.wordStyle.fontWeight == 500 ? 0 : 1,//加粗：0 普通 1 加粗
                    colorValue: this.pickerColor,//文字颜色
                    content: " " + this.value + "",//文字内容
                    fontUrl: this.fontUrl,//字体格式链接
                    italic: this.wordStyle.fontStyle == 'italic' ? 1 : 0,//斜体：0 普通 1 斜体
                    underline: this.wordStyle.textDecoration == 'none' ? 0 : 1,//下滑：0 普通 1 下滑 
                },
                method: 'post',
                responseType: 'arraybuffer',
                header: {
                    Authorization: "Bearer e2589fc8-8748-481b-8ec4-c63df33e4371",
                    token: uni.getStorageSync("auth")
                        ? uni.getStorageSync("auth")
                        : ""
                },
                success: res => {
                    let datas = res.data;
                    this.imgUrl = 'data:image/png;base64,' + uni.arrayBufferToBase64(datas);
                    // this.$emit("SetWord", this.value, this.pickerColor, this.wordStyle);
                    this.$emit("SetWordImg", this.imgUrl);
                    this.close();
                },
            });


        },
        overstriking() {
            this.wordStyle.fontWeight = (this.wordStyle.fontWeight == 500 ? 600 : 500);
        },
        incline() {
            this.wordStyle.fontStyle = (this.wordStyle.fontStyle == 'normal' ? 'italic' : 'normal');
        },
        underline() {
            this.wordStyle.textDecoration = (this.wordStyle.textDecoration == 'none' ? 'underline' : 'none');
        },

        open() {
            this.$refs.popup.open('bottom')
        },
        close() {
            this.$refs.popup.close();
            this.value = '';
            this.wordStyle = {
                fontWeight: 500,
                fontStyle: 'normal',//italic	浏览器会显示一个斜体的字体样式
                textDecoration: 'none',//underline 下划线
                textAlign: 'left'
            }
            this.$emit("Iscanvas", true);
        }
    }
}
</script>
<style scoped lang="scss">
.scroll_view_y1 {
    width: 700rpx;
    margin-left: 25rpx;
    height: 440rpx;
}
.pop {
    background-color: #fff;
    z-index: 100;
    font-size: 32upx;
}
.flex_col {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: flex-start;
    align-items: center;
    align-content: center;
}
.list {
    justify-content: space-between;
}
.list > view {
    width: 60upx;
    height: 60upx;
    margin-bottom: 10upx;
    box-sizing: border-box;
    border-radius: 3px;
    box-shadow: 0 0 2px #ccc;
}
.list .active {
    box-shadow: 0 0 2px #09f;
    transform: scale(1.05, 1.05);
}

.scroll_view_y {
    height: 580rpx;
    .coverage_list_box {
        width: 690rpx;
        height: 120rpx;
        border-bottom: 1rpx dotted rgba(0, 118, 164, 0.1);
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: #595959;
        .cilckColor {
            color: #0076a4;
        }
        .let_box {
            font-size: 28rpx;
        }
    }
}
.word_style_item {
    height: 80rpx;
    line-height: 80rpx;
    padding: 0 30rpx;
    font-size: 36rpx;
    box-sizing: border-box;
}
.typeface_style_box {
    height: 140rpx;
    width: 690rpx;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1rpx dotted rgba(0, 118, 164, 0.1);
    padding: 0 60rpx;
    box-sizing: border-box;
    .typeface_style_meu {
        width: 70rpx;
        height: 70rpx;
        line-height: 70rpx;
        border-radius: 100rpx;
        background-color: rgba(0, 0, 0, 0);
        color: rgba(44, 44, 44, 1);
        text-align: center;
        font-family: Arial;
        border: 1rpx solid rgba(202, 202, 202, 1);
    }
}
.color {
    width: 690rpx;
    height: 440rpx;
    // display: flex;
    // align-items: center;
    // margin: 0 auto;
    border-bottom: 1rpx dotted rgba(0, 118, 164, 0.1);
}
.word_style_box {
    height: 140rpx;
    width: 690rpx;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1rpx dotted rgba(0, 118, 164, 0.1);
    color: #2c2c2c;
    .selece_word {
        border: 1rpx solid rgba(0, 118, 164, 1);
        color: #0076a4;
    }
    .word_meu {
        width: 320rpx;
        height: 75rpx;
        line-height: 75rpx;
        border-radius: 100rpx;
        font-size: 28rpx;
        text-align: center;
    }
}
.input_item {
    width: 666rpx;
    height: 80rpx;
    border-radius: 100rpx;
    background-color: #ededed;
    color: rgba(136, 136, 136, 1);
    font-size: 28rpx;
    text-align: left;
    font-family: Arial;
    margin: 0 auto;
    margin-top: 40rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .input_meu {
        width: 500rpx;
        padding: 0 20rpx;
        box-sizing: border-box;
    }
    .bit_ok {
        width: 160rpx;
        height: 80rpx;
        line-height: 80rpx;
        border-radius: 100rpx;
        background-color: rgba(0, 118, 164, 0.75);
        color: rgba(255, 255, 255, 1);
        font-size: 28rpx;
        text-align: center;
    }
}
.sele_img_box {
    width: 750rpx;
    height: 1000rpx;
    border-radius: 40rpx 40rpx 0rpx 0rpx;
    background-color: rgba(253, 253, 253, 1);
    color: rgba(16, 16, 16, 1);
    font-family: Arial;
    border-top: 1rpx solid rgba(232, 232, 232, 0.5);
    .sele_img_title_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 750rpx;
        height: 80rpx;
        padding: 0 40rpx;
        box-sizing: border-box;
        color: rgba(59, 59, 59, 1);
        font-size: 32rpx;
    }
}
</style>