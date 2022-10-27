<template>
  <div id="container-emoji" class="motion-container" :class="categoryClass">
     <component
      :is="categoryEmoji"
      v-for="(emoji, index) in data[category]"
      :key="index"
      :data="emoji"
      @click.native="onSelect(emoji, type)"
    />
  </div>
</template>

<script>
import HahaEmoji from './emoji/HahaEmoji'

export default {
  name: 'EmojiList',
  components: {
    HahaEmoji
  },
  data: () => ({
    categories: [
      { name: 'haha', title: '' }
    ]
  }),
  props: {
    data: {type: Object},
    category: { type: String }
  },
  methods: {
    onSelect(emoji, type) {
      this.$emit('select', emoji, type)
    },
  },
  computed: {
    categoryClass() {
      return this.category + "-container";
    },
    categoryEmoji() {
      return this.category + "Emoji";
    },
    type() {
      // if(this.category === "bilibili") {
      //   return "Math";
      // } else if(["haha","gulu","tieba"].includes(this.category)) {
      //   return "BBCode"
      // }
      if(this.category === "haha") {
        return "BBCode";
      }
      return "";
    }
  },
  watch: {
    data() {
      this.$refs['container-emoji'].scrollTop = 0
    }
  }
}
</script>
