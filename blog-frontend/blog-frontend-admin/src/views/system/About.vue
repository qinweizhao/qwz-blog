<template>
  <page-view>
    <a-row>
      <a-col :span="24">
        <a-card
          :bordered="false"
          :bodyStyle="{ padding: '16px' }"
        >
          <a-card
            :bordered="false"
            class="environment-info"
            :bodyStyle="{ padding: '16px' }"
          >
            <template slot="title">
              环境信息
              <a
                href="javascript:void(0);"
                @click="handleCopyEnvironments"
              >
                <a-icon type="copy" />
              </a>
            </template>
            <ul class="m-0 p-0 list-none">
              <li>版本：{{ environments.version }}</li>
              <li>数据库：{{ environments.database }}</li>
              <li>运行模式：{{ environments.mode }}</li>
              <li>启动时间：{{ environments.startTime | moment }}</li>
            </ul>
          </a-card>
        </a-card>
      </a-col>

      <a-col :span="24">
      </a-col>
    </a-row>

    <a-modal
      :title="versionContentModalTitle"
      :visible="versionContentVisible"
      ok-text="查看更多"
      @cancel="versionContentVisible=false"
      @ok="handleOpenVersionUrl"
      :width="620"
    >
      <div v-html="versionContent"></div>
    </a-modal>
  </page-view>
</template>

<script>
import adminApi from '@/api/admin'
import axios from 'axios'
import marked from 'marked'
import { PageView } from '@/layouts'
export default {
  components: {
    PageView,
  },
  data() {
    return {
      environments: {},
      contributors: [
        {
          login: '',
          id: 0,
          node_id: '',
          avatar_url: '',
          gravatar_id: '',
          url: '',
          html_url: '',
          followers_url: '',
          following_url: '',
          gists_url: '',
          starred_url: '',
          subscriptions_url: '',
          organizations_url: '',
          repos_url: '',
          events_url: '',
          received_events_url: '',
          type: '',
          site_admin: false,
          contributions: 0,
        },
      ],
      contributorsLoading: true,
      checking: false,
      isLatest: false,
      latestData: {},
      versionContentVisible: false,
    }
  },
  computed: {
    versionMessage() {
      return `当前版本：${this.environments.version}，${
        this.isLatest ? '已经是最新版本。' : `新版本：${this.latestData.name}，你可以点击下方按钮查看详情。`
      }`
    },
    versionContent() {
      if (this.latestData && this.latestData.body) {
        return marked(this.latestData.body)
      } else {
        return '暂无内容'
      }
    },
    versionContentModalTitle() {
      return `${this.latestData.name} 更新内容`
    },
  },
  created() {
    this.getEnvironments()
    this.fetchContributors()
  },
  methods: {
    async getEnvironments() {
      await adminApi.environments().then((response) => {
        this.environments = response.data.data
      })
    },
    handleCopyEnvironments() {
      const text = `版本：${this.environments.version}
数据库：${this.environments.database}
运行模式：${this.environments.mode}
User Agent：${navigator.userAgent}`
      this.$copyText(text)
        .then((message) => {
          this.$log.debug('copy', message)
          this.$message.success('复制成功！')
        })
        .catch((err) => {
          this.$log.debug('copy.err', err)
          this.$message.error('复制失败！')
        })
    },
    fetchContributors() {
      const _this = this
      _this.contributorsLoading = true
      axios
        .get('https://api.github.com/repos/halo-dev/halo/contributors')
        .then((response) => {
          _this.contributors = response.data
        })
        .catch(function(error) {
          console.error('Fetch contributors error', error)
        })
        .finally(() => {
          setTimeout(() => {
            _this.contributorsLoading = false
          }, 200)
        })
    },
    handleShowVersionContent() {
      this.versionContentVisible = true
    },
    handleOpenVersionUrl() {
      window.open(this.latestData.html_url, '_blank')
    },
    calculateIntValue(version) {
      version = version.replace(/v/g, '')
      const ss = version.split('.')
      if (ss == null || ss.length !== 3) {
        return -1
      }
      const major = parseInt(ss[0])
      const minor = parseInt(ss[1])
      const micro = parseInt(ss[2])
      if (isNaN(major) || isNaN(minor) || isNaN(micro)) {
        return -1
      }
      return major * 1000000 + minor * 1000 + micro
    },
  },
}
</script>
