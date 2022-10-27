<template>
  <div v-show="show" class="butterBar butterBar-center">
    <p :class="['butterBar-message', type]" v-html="message"></p>
  </div>
</template>

<script>
export default {
  name: "tips",
  props: {
    type: {
      type: String,
      default: "warning",
    },
    message: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      show: false,
    };
  },
};
</script>