<template>
  <a-modal v-model="visible" :bodyStyle="{ padding: '0 24px 24px' }" :width="1024" title="从系统预设链接添加菜单">
    <template slot="footer">
      <a-button @click="handleCancel"> 取消</a-button>
      <ReactiveButton
        :disabled="menus && menus.length <= 0"
        :errored="saveErrored"
        :loading="saving"
        erroredText="添加失败"
        loadedText="添加成功"
        text="添加"
        @callback="handleCreateBatchCallback"
        @click="handleCreateBatch"
      ></ReactiveButton>
    </template>
    <a-row :gutter="24">
      <a-col :span="12">
        <a-spin :spinning="loading">
          <div class="custom-tab-wrapper">
            <a-tabs :animated="{ inkBar: true, tabPane: false }" default-active-key="1">
              <a-tab-pane key="1" force-render tab="分类">
                <a-list item-layout="horizontal">
                  <a-list-item v-for="(category, index) in categories" :key="index">
                    <a-list-item-meta :description="category.fullPath" :title="category.name" />
                    <template slot="actions">
                      <a-button class="!p-0" type="link" @click="handleInsertPre(category.name, category.fullPath)">
                        <a-icon type="plus-circle" />
                      </a-button>
                    </template>
                  </a-list-item>
                </a-list>
              </a-tab-pane>
              <a-tab-pane key="2" tab="标签">
                <a-list item-layout="horizontal">
                  <a-list-item v-for="(tag, index) in tags" :key="index">
                    <a-list-item-meta :description="tag.fullPath" :title="tag.name" />
                    <template slot="actions">
                      <a-button class="!p-0" type="link" @click="handleInsertPre(tag.name, tag.fullPath)">
                        <a-icon type="plus-circle" />
                      </a-button>
                    </template>
                  </a-list-item>
                </a-list>
              </a-tab-pane>
              <a-tab-pane key="5" tab="其他">
                <a-list item-layout="horizontal">
                  <a-list-item v-for="(item, index) in otherInternalLinks" :key="index">
                    <a-list-item-meta :description="item.url" :title="item.name" />
                    <template slot="actions">
                      <a-button class="!p-0" type="link" @click="handleInsertPre(item.name, item.url)">
                        <a-icon type="plus-circle" />
                      </a-button>
                    </template>
                  </a-list-item>
                </a-list>
              </a-tab-pane>
            </a-tabs>
          </div>
        </a-spin>
      </a-col>
      <a-col :span="12">
        <div class="custom-tab-wrapper">
          <a-tabs default-active-key="1">
            <a-tab-pane key="1" force-render tab="备选">
              <a-list item-layout="horizontal">
                <a-list-item v-for="(menu, index) in menus" :key="index">
                  <a-list-item-meta :description="menu.url" :title="menu.name" />
                  <template slot="actions">
                    <a-button class="!p-0" type="link" @click="handleRemovePre(index)">
                      <a-icon type="close-circle" />
                    </a-button>
                  </template>
                </a-list-item>
              </a-list>
            </a-tab-pane>
          </a-tabs>
        </div>
      </a-col>
    </a-row>
  </a-modal>
</template>
<script>
import menuApi from '@/api/menu'
import categoryApi from '@/api/category'
import tagApi from '@/api/tag'
import configApi from '@/api/config'

export default {
  name: 'MenuInternalLinkSelector',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    team: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      options: {},
      categories: [],
      tags: [],
      menus: [],
      loading: false,
      saving: false,
      saveErrored: false
    }
  },
  computed: {
    visible: {
      get() {
        return this.value
      },
      set(value) {
        this.$emit('input', value)
      }
    },
    otherInternalLinks() {
      const options = this.options
      return [
        {
          name: '分类',
          url: `${options.blog_url}/${options.categories_prefix}`
        },
        {
          name: '日志',
          url: `${options.blog_url}/${options.journals_prefix}`
        },
        {
          name: '标签',
          url: `${options.blog_url}/${options.tags_prefix}`
        },
        {
          name: '归档',
          url: `${options.blog_url}/${options.archives_prefix}`
        },
        {
          name: '网站地图',
          url: `${options.blog_url}/sitemap.xml`
        },
        {
          name: '网站地图',
          url: `${options.blog_url}/sitemap.html`
        }
      ]
    }
  },
  watch: {
    visible(value) {
      if (value) {
        this.handleFetchAll()
      }
    }
  },
  methods: {
    handleFetchAll() {
      this.loading = true
      Promise.all([configApi.getMap(), categoryApi.list(), tagApi.list()])
        .then(response => {
          this.options = response[0].data.data
          this.categories = response[1].data.data
          this.tags = response[2].data.data
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleInsertPre(name, url) {
      this.menus.push({
        name: name,
        url: url,
        team: this.team
      })
    },
    handleRemovePre(index) {
      this.menus.splice(index, 1)
    },
    handleCancel() {
      this.menus = []
      this.visible = false
      this.$emit('reload')
    },
    handleCreateBatch() {
      this.saving = true
      menuApi
        .createBatch(this.menus)
        .catch(() => {
          this.saveErrored = false
        })
        .finally(() => {
          setTimeout(() => {
            this.saving = false
          }, 400)
        })
    },
    handleCreateBatchCallback() {
      if (this.saveErrored) {
        this.saveErrored = false
      } else {
        this.handleCancel()
      }
    }
  }
}
</script>
