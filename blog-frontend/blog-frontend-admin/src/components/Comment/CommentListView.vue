<template>
  <a-list :dataSource="comments" :loading="loading" item-layout="vertical">
    <template #renderItem="item, index">
      <a-list-item
        :key="index"
        class="!p-0"
        :class="{ 'hover:bg-gray-100 hover:!px-1 hover:rounded transition-all cursor-pointer': clickable }"
        @click="handleClick(item)"
      >
        <a-comment :avatar="item.avatar">
          <template #author>
            <a v-if="item.authorUrl" :href="item.authorUrl" class="!text-gray-800 hover:!text-blue-500" target="_blank">
              {{ item.author }}
            </a>
            <span v-else class="!text-gray-500">{{ item.author }}</span>
            发表在
            <span class="hover:!text-blue-500 cursor-pointer" @click="handleOpenTarget(item)">
              《{{ targetTitle(item) }}》
            </span>
          </template>
          <template #content>
            <p v-html="$options.filters.markdownRender(item.content)" />
          </template>
          <template #datetime>
            <a-tooltip :title="item.createTime | moment">
              <span>{{ item.createTime | timeAgo }}</span>
            </a-tooltip>
          </template>
        </a-comment>
      </a-list-item>
    </template>
  </a-list>
</template>
<script>
import { datetimeFormat } from '@/utils/datetime'
import postApi from '@/api/post'

export default {
  name: 'CommentListView',
  props: {
    comments: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    },
    clickable: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    targetTitle() {
      return function (comment) {
        return comment.target.title || datetimeFormat(comment.target.createTime)
      }
    }
  },
  methods: {
    async handleOpenTarget(comment) {
      const target = comment
      if (target) {
        const { status, fullPath, id } = target.target
        if (['PUBLISHED', 'INTIMATE'].includes(status)) {
          console.log(fullPath)
          window.open(fullPath, '_blank')
          return
        }
        if (status === 'DRAFT') {
          const link = await postApi.preview(id)
          window.open(link, '_blank')
        }
      }
    },
    handleClick(item) {
      if (this.clickable) {
        this.$emit('click', item)
      }
    }
  }
}
</script>
