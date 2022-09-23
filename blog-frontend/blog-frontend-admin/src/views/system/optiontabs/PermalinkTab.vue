<template>
  <div>
    <a-form-model ref="permalinkOptionsForm" :model="options" :rules="rules" :wrapperCol="wrapperCol" layout="vertical">
      <a-form-model-item label="归档前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.archives_prefix }}</span>
        </template>
        <a-input v-model="options.archives_prefix" />
      </a-form-model-item>
      <a-form-model-item label="分类前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.categories_prefix }}/{slug}</span>
        </template>
        <a-input v-model="options.categories_prefix" />
      </a-form-model-item>
      <a-form-model-item label="标签前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.tags_prefix }}/{slug}</span>
        </template>
        <a-input v-model="options.tags_prefix" />
      </a-form-model-item>
      <a-form-model-item label="日志页面前缀：">
        <template slot="help">
          <span>{{ options.blog_url }}/{{ options.journals_prefix }}</span>
        </template>
        <a-input v-model="options.journals_prefix" />
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
