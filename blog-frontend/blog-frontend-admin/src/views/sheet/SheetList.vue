<template>
  <page-view>
    <a-row>
      <a-col :span="24">
        <div class="card-container">
          <a-tabs v-model="activeKey" type="card">
            <a-tab-pane v-for="pane in panes" :key="pane.key">
              <span slot="tab"> <a-icon :type="pane.icon" />{{ pane.title }} </span>
              <component :is="pane.component"></component>
            </a-tab-pane>
          </a-tabs>
        </div>
      </a-col>
    </a-row>
  </page-view>
</template>

<script>
import { PageView } from '@/layouts'
import IndependentSheetList from './components/IndependentSheetList'

export default {
  components: {
    PageView,
    IndependentSheetList
  },
  data() {
    const panes = [{ title: '独立页面', icon: 'paper-clip', component: 'IndependentSheetList', key: 'independent' }]
    return {
      activeKey: panes[0].key,
      panes
    }
  },
  beforeRouteEnter(to, from, next) {
    // Get post id from query
    const activeKey = to.query.activeKey
    next(vm => {
      if (activeKey) {
        vm.activeKey = activeKey
      }
    })
  },
  watch: {
    activeKey(newVal) {
      if (newVal) {
        const path = this.$router.history.current.path
        this.$router.push({ path, query: { activeKey: newVal } }).catch(err => err)
      }
    }
  }
}
</script>
