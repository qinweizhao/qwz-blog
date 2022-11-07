<template>
  <CommentListView :comments="comments" :loading="loading" />
</template>

<script>
import commentApi from '@/api/comment'

export default {
  name: 'RecentCommentTab',
  props: {
    type: {
      type: String,
      required: false,
      default: 'post',
      validator: function (value) {
        return ['post', 'journal'].indexOf(value) !== -1
      }
    }
  },
  data() {
    return {
      comments: [],
      loading: false
    }
  },
  created() {
    this.handleListTargetComments()
  },
  methods: {
    async handleListTargetComments() {
      try {
        this.loading = true
        const { data } = await commentApi.latestComment(this.type, 5, 'PUBLISHED')
        this.comments = data.data
      } catch (e) {
        this.$log.error('Failed to load comments', e)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>
