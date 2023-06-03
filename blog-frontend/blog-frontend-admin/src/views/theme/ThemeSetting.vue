<template>
  <page-view affix>
    <template slot="extra">
      <a-button @click="handleRouteToThemeVisualSetting">
        <a-icon type="eye" />
        预览模式
      </a-button>
    </template>

    <a-spin :spinning="theme.loading">
      <ThemeSettingForm />
    </a-spin>
  </page-view>
</template>
<script>
// components
import { PageView } from '@/layouts'
import ThemeSettingForm from './ThemeSettingForm'

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
      } finally {
        this.theme.loading = false
      }
    },
    handleRouteToThemeVisualSetting() {
      this.$router.push({ name: 'ThemeVisualSetting' })
    }
  }
}
</script>
