<template>
  <div>
    <a-form-model ref="generalOptionsForm" :model="options" :rules="rules" :wrapperCol="wrapperCol" layout="vertical">
      <a-form-model-item label="主题色：" prop="mode_color_light">
        <a-input v-model="options.mode_color_light" />
      </a-form-model-item>
      <a-form-model-item label="背景图：" prop="background_light_mode">
        <AttachmentInput v-model="options.background_light_mode" title="选择背景图" />
      </a-form-model-item>
      <a-form-model-item label="背景特效：">
        <a-select v-model="options.backdrop">
          <a-select-option v-for="item in Object.keys(backdrops)" :key="item" :value="item">
            {{ backdrops[item].text }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="开启模块缓入效果：" prop="enable_show_in_up">
        <a-switch v-model="options.enable_show_in_up" />
      </a-form-model-item>
      <a-form-model-item label="最大分页按钮数：" prop="max_pager_number">
        <a-input v-model="options.max_pager_number" />
      </a-form-model-item>
      
      <a-form-model-item label="Favicon：" prop="blog_favicon">
        <AttachmentInput v-model="options.blog_favicon" title="选择 Favicon" />
      </a-form-model-item>
      <a-form-model-item label="ICP 备案号：" prop="blog_icp">
        <a-input v-model="options.icp" />
      </a-form-model-item>
      <a-form-model-item label="公网安备号：" prop="police">
        <a-input v-model="options.police" />
      </a-form-model-item>
      <a-form-model-item label="网站公告：" prop="site_notice">
        <a-input v-model="options.site_notice" :autoSize="{ minRows: 5 }" type="textarea" />
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
import { backdrops } from '@/core/constant'

export default {
  name: 'GeneralTab',
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
      backdrops,
      rules: {
        blog_title: [
          { required: true, message: '* 博客标题不能为空', trigger: ['change'] },
          { max: 1023, message: '* 字符数不能超过 1023', trigger: ['change'] }
        ],
        blog_logo: [{ max: 1023, message: '* 字符数不能超过 1023', trigger: ['change'] }],
        blog_favicon: [{ max: 1023, message: '* 字符数不能超过 1023', trigger: ['change'] }]
      }
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
      _this.$refs.generalOptionsForm.validate(valid => {
        if (valid) {
          this.$emit('onSave')
        }
      })
    }
  }
}
</script>
