<template>
  <div>
    <a-form-model ref="permalinkOptionsForm" :model="options" :rules="rules" :wrapperCol="wrapperCol" layout="vertical">
      <a-form-model-item label="归档前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.archives_prefix }}{{ options.path_suffix }}</span>
        </template>
        <a-input v-model="options.archives_prefix" />
      </a-form-model-item>
      <a-form-model-item label="分类前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.categories_prefix }}/{slug}{{ options.path_suffix }}</span>
        </template>
        <a-input v-model="options.categories_prefix" />
      </a-form-model-item>
      <a-form-model-item label="标签前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.tags_prefix }}/{slug}{{ options.path_suffix }}</span>
        </template>
        <a-input v-model="options.tags_prefix" />
      </a-form-model-item>
      <a-form-model-item label="自定义页面固定链接类型：">
        <template slot="help">
          <span v-if="options.sheet_permalink_type === 'SECONDARY'"
            >{{ options.blog_url }}/{{ options.sheet_prefix }}/{slug}{{ options.path_suffix }}</span
          >
          <span v-else-if="options.sheet_permalink_type === 'ROOT'"
            >{{ options.blog_url }}/{slug}{{ options.path_suffix }}</span
          >
        </template>
        <a-select v-model="options.sheet_permalink_type">
          <a-select-option v-for="item in Object.keys(sheetPermalinkType)" :key="item" :value="item"
            >{{ sheetPermalinkType[item].text }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item v-show="options.sheet_permalink_type === 'SECONDARY'" label="自定义页面前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.sheet_prefix }}/{slug}{{ options.path_suffix }}</span>
        </template>
        <a-input v-model="options.sheet_prefix" />
      </a-form-model-item>
      <a-form-model-item label="日志页面前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.journals_prefix }}{{ options.path_suffix }}</span>
        </template>
        <a-input v-model="options.journals_prefix" />
      </a-form-model-item>
      <a-form-model-item label="路径后缀：">
        <template slot="help">
          <span>* 格式为：<code>.{suffix}</code>，仅对内建路径有效</span>
        </template>
        <a-input v-model="options.path_suffix" />
      </a-form-model-item>
      <a-form-model-item>
        <ReactiveButton
          :errored="errored"
          :loading="saving"
          erroredText="保存失败"
          loadedText="保存成功"
          text="保存"
          type="primary"
          @callback="$emit('callback')"
          @click="handleSaveOptions"
        ></ReactiveButton>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>
<script>
export default {
  name: 'PermalinkTab',
  props: {
    options: {
      type: Object,
      required: true
    },
    saving: {
      type: Boolean,
      default: false
    },
    errored: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      sheetPermalinkType: {
        SECONDARY: {
          type: 'SECONDARY',
          text: '二级路径'
        },
        ROOT: {
          type: 'ROOT',
          text: '根路径'
        }
      },
      wrapperCol: {
        xl: { span: 8 },
        lg: { span: 8 },
        sm: { span: 12 },
        xs: { span: 24 }
      },
      rules: {}
    }
  },
  watch: {
    options(val) {
      this.$emit('onChange', val)
    }
  },
  methods: {
    handleSaveOptions() {
      const _this = this
      _this.$refs.permalinkOptionsForm.validate(valid => {
        if (valid) {
          this.$emit('onSave')
        }
      })
    }
  }
}
</script>
