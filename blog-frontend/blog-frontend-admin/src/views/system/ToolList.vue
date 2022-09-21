<template>
  <page-view>
    <a-row :gutter="12">
      <a-col v-if="options.developer_mode" :lg="6" :md="12" :sm="24" :xl="6" :xs="24" class="pb-3">
        <a-card :bodyStyle="{ padding: '16px' }" :bordered="false">
          <div slot="title">
            <a-icon type="experiment" />
            开发者选项
          </div>
          <p style="min-height: 50px">点击进入开发者选项页面</p>
          <a-button class="float-right" type="primary" @click="handleToDeveloperOptions()">进入</a-button>
        </a-card>
      </a-col>
      <a-col :lg="6" :md="12" :sm="24" :xl="6" :xs="24" class="pb-3">
        <a-card :bodyStyle="{ padding: '16px' }" :bordered="false">
          <div slot="title">
            <a-icon type="file-markdown" />
            Markdown 文章导入
          </div>
          <p style="min-height: 50px">解析文件并保存为草稿</p>
          <a-button class="float-right" type="primary" @click="markdownUpload = true">导入</a-button>
        </a-card>
      </a-col>
    </a-row>
    <a-modal
      v-model="markdownUpload"
      :afterClose="onUploadClose"
      :footer="null"
      destroyOnClose
      title="Markdown 文章导入"
    >
      <FilePondUpload
        ref="upload"
        :uploadHandler="uploadHandler"
        label="拖拽或点击选择 Markdown 文件到此处"
        name="file"
      ></FilePondUpload>
    </a-modal>
  </page-view>
</template>

<script>
import { PageView } from '@/layouts'

import { mapGetters } from 'vuex'
import postApi from '@/api/post'

export default {
  components: { PageView },
  data() {
    return {
      markdownUpload: false,
      uploadHandler: (file, options) => postApi.importMarkdown(file, options)
    }
  },
  computed: {
    ...mapGetters(['options'])
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
    }
  }
}
</script>
