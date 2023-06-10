<template>
  <page-view affix>
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
    }
  }
}
</script>
