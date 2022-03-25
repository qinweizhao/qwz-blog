<template>
  <page-view :sub-title="theme.current.version || '-'" :title="theme.current.name || '-'" affix>
    <template slot="extra">
      <a-button @click="localUpgradeModel.visible = true" icon="upload"> 更新 </a-button>

      <a-button :disabled="!theme.current.activated" @click="handleRouteToThemeVisualSetting">
        <a-icon type="eye" />
        预览模式
      </a-button>
    </template>

    <a-spin :spinning="theme.loading">
      <ThemeSettingForm :theme="theme.current" />
    </a-spin>

    <ThemeLocalUpgradeModal
      :theme="theme.current"
      :visible.sync="localUpgradeModel.visible"
      @success="handleGetTheme"
    />
  </page-view>
</template>
<script>
// components
import { PageView } from '@/layouts'
import ThemeLocalUpgradeModal from './components/ThemeLocalUpgradeModal'
import ThemeSettingForm from './components/ThemeSettingForm'

// utils
import apiClient from '@/utils/api-client'

export default {
  name: 'ThemeSetting',
  components: {
    PageView,
    ThemeLocalUpgradeModal,
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
    const themeId = to.query.themeId
    next(async vm => {
      await vm.handleGetTheme(themeId)
    })
  },
  methods: {
    async handleGetTheme(themeId) {
      try {
        this.theme.loading = true
        if (themeId) {
          const { data } = await apiClient.theme.get(themeId)
          this.theme.current = data
        } else {
          const { data } = await apiClient.theme.getActivatedTheme()
          this.theme.current = data
        }
      } finally {
        this.theme.loading = false
      }
    },
    onThemeDeleteSucceed() {
      this.$router.replace({ name: 'ThemeList' })
    },

    handleRouteToThemeVisualSetting() {
      this.$router.push({ name: 'ThemeVisualSetting', query: { themeId: this.theme.current.id } })
    }
  }
}
</script>
