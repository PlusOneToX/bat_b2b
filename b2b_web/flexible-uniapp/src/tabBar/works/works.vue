<template>
    <view>
        <WorksList :bottomOut="bottomOut" :upload="upload"></WorksList>
    </view>
</template>
<script>
import WorksList from "components/works/worksList.vue";

export default {
    components: {
        WorksList
    },

    data() {
        return {
            bottomOut: true,
            upload: false,
        }
    },
    onShow() {
        // this.upload = !this.upload;
    },
    mounted() {
    },
    onReachBottom() {
        this.bottomOut = !this.bottomOut;
    },
    methods: {

    }
}
</script>
<style scoped lang="stylus"></style>