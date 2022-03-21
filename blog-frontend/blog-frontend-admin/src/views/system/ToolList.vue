<template>
  <page-view>
    <a-row :gutter="12">
      <a-col
        v-if="options.developer_mode"
        :xl="6"
        :lg="6"
        :md="12"
        :sm="24"
        :xs="24"
        class="pb-3"
      >
        <a-card
          :bordered="false"
          :bodyStyle="{ padding: '16px' }"
        >
          <div slot="title">
            <a-icon type="experiment" /> 开发者选项
          </div>
          <p style="min-height: 50px;">点击进入开发者选项页面</p>
          <a-button
            type="primary"
            class="float-right"
            @click="handleToDeveloperOptions()"
          >进入</a-button>
        </a-card>
      </a-col>
      <a-col
        :xl="6"
        :lg="6"
        :md="12"
        :sm="24"
        :xs="24"
        class="pb-3"
      >
        <a-card
          :bordered="false"
          :bodyStyle="{ padding: '16px' }"
        >
          <div slot="title">
            <a-icon type="file-markdown" /> Markdown 文章导入
          </div>
          <p style="min-height: 50px;">支持 Hexo/Jekyll 文章导入并解析元数据</p>
          <a-button
            type="primary"
            class="float-right"
            @click="markdownUpload = true"
          >导入</a-button>
        </a-card>
      </a-col>
    </a-row>
    <a-modal
      title="Markdown 文章导入"
      v-model="markdownUpload"
      :footer="null"
      destroyOnClose
      :afterClose="onUploadClose"
    >
      <FilePondUpload
        ref="upload"
        name="file"
        accept="text/markdown"
        label="拖拽或点击选择 Markdown 文件到此处"
        :uploadHandler="uploadHandler"
      ></FilePondUpload>
    </a-modal>
  </page-view>
</template>

<script>
import { mapGetters } from 'vuex'
import backupApi from '@/api/backup'
import { PageView } from '@/layouts'
export default {
  components: { PageView },
  data() {
    return {
      markdownUpload: false,
      uploadHandler: backupApi.importMarkdown,
    }
  },
  computed: {
    ...mapGetters(['options']),
  },
  methods: {
    handleChange(info) {
      const status = info.file.status
      if (status !== 'uploading') {
        this.$log.debug(info.file, info.fileList)
      }
      if (status === 'done') {
        this.$message.success(`${info.file.name} 导入成功！`)
      } else if (status === 'error') {
        this.$message.error(`${info.file.name} 导入失败！`)
      }
    },
    handleToDeveloperOptions() {
      this.$router.push({ name: 'DeveloperOptions' })
    },
    onUploadClose() {
      this.$refs.upload.handleClearFileList()
    },
  },
}
</script>
