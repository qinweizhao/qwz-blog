<template>
  <a-row :gutter="0">
    <a-col :md="5" :sm="24" class="h-screen" style="border-right: 1px solid #f2f2f2">
      <a-spin :spinning="theme.loading" class="h-full">
        <ThemeSettingForm :theme="theme.data" :wrapperCol="{ span: 24 }" @saved="onSettingsSaved">
          <template #descriptions-item>
            <a-descriptions-item>
              <a-button @click="handleRouteToThemeSetting()">返回</a-button>
            </a-descriptions-item>
          </template>
        </ThemeSettingForm>
      </a-spin>
    </a-col>
    <a-col :md="19" :sm="24" class="h-screen">
      <iframe
        id="themeViewIframe"
        :src="options.blog_url"
        frameborder="0"
        height="100%"
        scrolling="auto"
        width="100%"
      />
    </a-col>
  </a-row>
</template>
<script>
// components
import ThemeSettingForm from './ThemeSettingForm'

import themeApi from '@/api/theme'
import { mapGetters } from 'vuex'

export default {
  name: 'ThemeVisualSetting',
  components: {
    ThemeSettingForm
  },
  data() {
    return {
      theme: {
        data: {},
        loading: false
      }
    }
  },
  computed: {
    ...mapGetters(['options'])
  },
  beforeRouteEnter(to, from, next) {
    next(async vm => {
      await vm.handleGetTheme()
    })
  },
  methods: {
    async handleGetTheme() {
      try {
        this.theme.loading = true
        const { data } = await themeApi.getProperty()
        this.theme.data = data.data
      } finally {
        this.theme.loading = false
      }
    },
    handleRouteToThemeSetting() {
      this.$router.push({ name: 'ThemeSetting' })
    },
    onSettingsSaved() {
      document.getElementById('themeViewIframe').contentWindow.location.reload()
    }
  }
}
</script>
<style scoped>
::v-deep .ant-spin-container {
  height: 100%;
}

::v-deep .ant-tabs-content {
  height: 100%;
  overflow: auto;
  padding-bottom: 20px;
}
</style>
