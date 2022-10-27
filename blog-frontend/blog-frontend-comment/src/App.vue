<template>
  <div id="app">
    <Comment :id="81" type="post" :configs="configs" />
  </div>
</template>

<script>
import Comment from "./components/Comment";

export default {
  name: "app",
  components: {
    Comment,
  },
  data() {
    return {
      configs: {
        autoLoad: true,
        showUserAgent: true,
        gravatarType: "mm",
        gravatarSource: "",
        loadingStyle: "default",
        aWord: "你是我一生只会遇见一次的惊喜 ...",
        authorPopup: "你的昵称是啥呢？",
        emailPopup: "你将收到回复通知",
        urlPopup: "禁止小广告😀",
        notComment: "暂无评论",
      },
    };
  },
};
</script>
