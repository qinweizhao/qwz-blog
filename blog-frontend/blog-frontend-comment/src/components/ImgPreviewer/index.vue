<template>
  <div class="img-previewer" v-if="visible">
    <img
      class="preview-content"
      :src="url"
      onerror="this.src='data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=='"
      alt="评论中的图片"
      @load="loadHandler"
    />
    <span class="previewer-close" @click="close"></span>
    <div class="mask" @click="close"></div>
  </div>
</template>

<script>
export default {
  name: "img-previewer",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    url: {
      type: String,
      default: "",
    },
  },
  watch: {
    visible(n) {
      document
        .querySelector("html")
        .classList[n ? "add" : "remove"]("disable-scroll");
    },
  },
  methods: {
    loadHandler(e) {
      const { width, height } = e.target;
      e.target.setAttribute(width >= height ? "width" : "height", "100%");
    },
    close() {
      this.$emit("update:visible", false);
    },
  },
};
</script>
<style lang="scss" scoped>
.img-previewer {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  z-index: 150;
  .preview-content {
    position: absolute;
    z-index: 2;
    left: 50%;
    top: 50%;
    transform: translate3d(-50%, -50%, 0);
    max-width: 100vw;
    max-height: 100vh;
  }
  .previewer-close {
    cursor: pointer;
    position: fixed;
    top: 14px;
    right: 14px;
    width: 40px;
    height: 40px;
    z-index: 3;
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAB9UlEQVR4Xu2ZPU4EMQyFny9BzQW4AzUCOramouEItJyBComeLfmpaTgHB+AQXkWakVaj2XUytjOrxCNts8kkfl+enWhC6PyhzvUjAIQDOicQKdC5AaIIRgpECnROIFKgcwPELhApECmgJMDMlwDuht8XgC0RfSuHnX2dma8BPAC4APAK4JeIfjRzqVOAmZ8APE+C2BDRVhPY9F1mTpDfJ/+/ENGjZh4LAH8AzmeCMINwQHya8p+IztYG8Abg/kAQaghHxKcpP4jodm0AVwBS7h96FkMQxKf5bojoc1UAafKMQIsheIw5B0pdA8ZBLQO2HEtyhxkAKyfUFJ9iNgWghVBbvAuApRDWEO8GoBTCWuJdAeRCGIrU9IS3X7uKdxCp8O23m9eAzCNsboyu4t0dULBFzgFxF18NQGY6VLN91RTYnyyj2KXuVVZ+jMu9BgSAgUDm6o+8qrmgigMKxVeF4A5gofhqEFwBZIjfNHsQyhE/fjcs6Zt7gsrt5+KAJYKWvJMr8lg/cwAaIZp3l8IwBWAhwGKMEhhmACwDtxxLgmECwCNgjzHnYKgBMHPfn8WZufuLke6vxrq/HO37elzaZk69Xb0LnLpAKb4AIBFqvT0c0PoKS/rCARKh1tvDAa2vsKQvHCARar09HND6Ckv6wgESodbbd57LFFC5BnTzAAAAAElFTkSuQmCC")
      no-repeat center;
    background-size: cover;
  }
  .mask {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    z-index: 1;
    backdrop-filter: blur(4px);
    background: rgba(0, 0, 0, 0.6);
  }
}
</style>