<template>
  <a-popover
    :arrowPointAtCenter="true"
    :autoAdjustOverflow="true"
    :overlayStyle="{ width: '400px', top: '50px' }"
    overlayClassName="header-comment-popover"
    placement="bottomRight"
    title="待审核评论"
    trigger="click"
  >
    <template #content>
      <div class="custom-tab-wrapper">
        <a-tabs v-model="activeKey" :animated="{ inkBar: true, tabPane: false }" @change="handleListAuditingComments">
          <a-tab-pane v-for="target in targets" :key="target.key" :tab="target.label">
            <CommentListView
              :comments="comments[target.dataKey]"
              :loading="comments.loading"
              clickable
              @click="handleRouteToCommentList(arguments[0], target)"
            />
          </a-tab-pane>
        </a-tabs>
      </div>
    </template>
    <span class="inline-block transition-all">
      <a-badge v-if="comments.post.length || comments.journal.length" dot>
        <a-icon type="bell" />
      </a-badge>
      <a-badge v-else>
        <a-icon type="bell" />
      </a-badge>
    </span>
  </a-popover>
</template>

<script>
import commentApi from '@/api/comment'
import { commentStatuses } from '@/core/constant'

const targets = [
  {
    key: 'post',
    dataKey: 'post',
    label: '文章'
  },
  {
    key: 'journal',
    dataKey: 'journal',
    label: '日志'
  }
]

export default {
  name: 'HeaderComment',
  data() {
    return {
      targets: targets,
      activeKey: 'post',
      comments: {
        post: [],
        journal: [],
        loading: false
      }
    }
  },
  created() {
    this.handleListAuditingComments()
  },
  methods: {
    async handleListAuditingComments() {
      try {
        this.comments.loading = true

        const params = { status: 'AUDITING', size: 20 }

        const responses = await Promise.all(
          targets.map(target => {
            return commentApi.queryComment(target.key, params)
          })
        )

        console.log('content')
        console.log(responses[0].data.data.content)
        this.comments.post = responses[0].data.data.content
        this.comments.journal = responses[1].data.data.content
      } catch (e) {
        this.$log.error('Failed to get auditing comments', e)
      } finally {
        this.comments.loading = false
      }
    },

    handleRouteToCommentList(comment, target) {
      this.$log.debug('Handle click auditing comment', comment, target)

      const { name } = this.$router.currentRoute

      this.$router.push({
        name: 'Comment',
        query: { activeKey: target.dataKey, defaultStatus: commentStatuses.AUDITING.value }
      })

      if (name === 'Comment') {
        this.$router.go(0)
      }
    }
  }
}
</script>
