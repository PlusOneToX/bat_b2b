<template>
    <view class="theme_details_box">
        <image mode="widthFix" src="../../static/canva/date/theme_details1.jpg"></image>
        <image @click="gocustomization1(117,118,'KDS_FK')" mode="widthFix" src="../../static/canva/date/theme_details2.jpg"></image>
        <image @click="gocustomization1(113,114,'KDS_FK')" mode="widthFix" src="../../static/canva/date/theme_details3.jpg"></image>
        <image @click="gocustomization1(107,108,'KDS_FK')" mode="widthFix" src="../../static/canva/date/theme_details4.jpg"></image>
        <image @click="gocustomization1(107,109,'KDS_FK')" mode="widthFix" src="../../static/canva/date/theme_details5.jpg"></image>
        <image mode="widthFix" src="../../static/canva/date/theme_details6.jpg"></image>
        <image mode="widthFix" src="../../static/canva/date/theme_details7.jpg"></image>
        <image @click="gocustomization" mode="widthFix" src="../../static/canva/date/theme_details8.jpg"></image>
    </view>
</template>
<script>
export default {
    data() {
        return {
        }
    },
    methods: {
        gocustomization() {
            uni.navigateTo({
                url: '/good_pages/customization/index'
            });
        },
        gocustomization1(parentId, materialId, manufactor) {
            uni.navigateTo({
                url: '/good_pages/customization/index?parentId=' + parentId + '&materialId=' + materialId + '&manufactor=' + manufactor
            });
        },
    }
}
</script>
<style scoped lang="scss">
.theme_details_box {
    image {
        width: 750rpx;
    }
}
</style>