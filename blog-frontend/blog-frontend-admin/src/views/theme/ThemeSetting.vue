<template>
  <page-view :sub-title="theme.current.version || '-'" :title="theme.current.name || '-'" affix>
    <template slot="extra">
      <a-button :disabled="!theme.current.activated" @click="handleRouteToThemeVisualSetting">
        <a-icon type="eye" />
        预览模式
      </a-button>
    </template>

    <a-spin :spinning="theme.loading">
      <ThemeSettingForm :theme="theme.current" />
    </a-spin>
  </page-view>
</template>
<script>
// components
import { PageView } from '@/layouts'
import ThemeSettingForm from './components/ThemeSettingForm'

// api
import themeApi from '@/api/theme'

export default {
  name: 'ThemeSetting',
  components: {
    PageView,
    ThemeSettingForm
  },
  data() {
    return {
      theme: {
        current: {},
        loading: false
      },
      themeDeleteModal: {
        visible: false
      },
      localUpgradeModel: {
        visible: false
      }
    }
  },
  beforeRouteEnter(to, from, next) {
    // Get post id from query
    next(async vm => {
      await vm.handleGetTheme()
    })
  },
  methods: {
    async handleGetTheme() {
      try {
        this.theme.loading = true
        const { data } = await themeApi.getProperty()
        this.theme.current = data.data
      } finally {
        this.theme.loading = false
      }
    },
    onThemeDeleteSucceed() {
      this.$router.replace({ name: 'ThemeList' })
    },

    handleRouteToThemeVisualSetting() {
      this.$router.push({ name: 'ThemeVisualSetting' })
    }
  }
}
</script>
