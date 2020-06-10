<template>
  <div class="loading-wrap" ref="loadingColor">
    <div class="cover"></div>
    <div class="spinner">
      <div class="cube1"></div>
      <div class="cube2"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: "loading",
  props: ["mainColor"],
  mounted() {
    // 页面主色调
    this.$refs.loadingColor.style.setProperty("--mainColor", this.mainColor);
  },
};
</script>

<style scoped lang='stylus'>
/* 加载动画 */
.spinner {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  z-index: 99;
  margin: auto;
  width: 32px;
  height: 32px;
}

.cube1, .cube2 {
  background-color: var(--mainColor);
  width: 30px;
  height: 30px;
  position: absolute;
  top: 0;
  left: 0;
  -webkit-animation: cubemove 1.8s infinite ease-in-out;
  animation: cubemove 1.8s infinite ease-in-out;
}

.cube2 {
  -webkit-animation-delay: -0.9s;
  animation-delay: -0.9s;
}

@keyframes cubemove {
  25% {
    -webkit-transform: translateX(42px) rotate(-90deg) scale(0.5);
  }

  50% {
    -webkit-transform: translateX(42px) translateY(42px) rotate(-180deg);
  }

  75% {
    -webkit-transform: translateX(0px) translateY(42px) rotate(-270deg) scale(0.5);
  }

  100% {
    -webkit-transform: rotate(-360deg);
  }
}

@keyframes cubemove {
  25% {
    transform: translateX(42px) rotate(-90deg) scale(0.5);
    -webkit-transform: translateX(42px) rotate(-90deg) scale(0.5);
  }

  50% {
    transform: translateX(42px) translateY(42px) rotate(-179deg);
    -webkit-transform: translateX(42px) translateY(42px) rotate(-179deg);
  }

  50.1% {
    transform: translateX(42px) translateY(42px) rotate(-180deg);
    -webkit-transform: translateX(42px) translateY(42px) rotate(-180deg);
  }

  75% {
    transform: translateX(0px) translateY(42px) rotate(-270deg) scale(0.5);
    -webkit-transform: translateX(0px) translateY(42px) rotate(-270deg) scale(0.5);
  }

  100% {
    transform: rotate(-360deg);
    -webkit-transform: rotate(-360deg);
  }
}

.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.7;
}
</style>
