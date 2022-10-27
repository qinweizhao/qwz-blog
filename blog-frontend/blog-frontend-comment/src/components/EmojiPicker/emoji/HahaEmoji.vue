<template>
  <span class="emoji-item" :title="data.description">
    <img :src="hahaSrc" onerror="this.src='data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=='" />
  </span>
</template>

<script>
export default {
  name: "HahaEmoji",
  props: {
    data: {
      type: Object,
    },
    url: {
      type: String,
      required: false,
      default:
        `${process.env.BASE_URL}assets/emoji/haha/`,
    },
  },
  computed: {
    hahaSrc() {
      return this.url + "icon_" + this.data.name.replace('ha-','') + ".png";
    },
  },
};
</script>

