<template>
  <div>
    <a-form-model
      ref="menuForm"
      :model="menuModel"
      :rules="form.rules"
      labelAlign="left"
      @keyup.enter.native="handleCreateOrUpdateMenu"
    >
      <a-row :gutter="24">
        <a-col :lg="8" :md="12" :sm="12" :xl="8" :xs="12">
          <a-form-model-item help="* 页面上所显示的名称" label="名称" prop="name">
            <a-input v-model="menuModel.name" autoFocus />
          </a-form-model-item>
        </a-col>
        <a-col :lg="8" :md="12" :sm="12" :xl="8" :xs="12">
          <a-form-model-item help="* 菜单的地址" label="地址" prop="url">
            <a-input v-model="menuModel.url" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="8" :md="12" :sm="12" :xl="8" :xs="12">
          <a-form-model-item help="* 请根据主题的支持情况选填" label="图标" prop="icon">
            <a-input v-model="menuModel.icon" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="8" :md="12" :sm="12" :xl="8" :xs="12">
          <a-form-model-item label="打开方式" prop="target">
            <a-radio-group v-model="menuModel.target" :options="targets" />
          </a-form-model-item>
        </a-col>
        <a-col :lg="8" :md="12" :sm="12" :xl="8" :xs="12">
          <a-form-model-item :colon="false" label=" ">
            <a-space>
              <ReactiveButton
                :errored="form.errored"
                :loading="form.saving"
                erroredText="保存失败"
                loadedText="保存成功"
                text="保存"
                type="primary"
                @callback="handleSavedCallback"
                @click="handleCreateOrUpdateMenu"
              ></ReactiveButton>
              <a-button @click="handleCancel">取消</a-button>
            </a-space>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </div>
</template>
<script>
import menuApi from '@/api/menu'

const targets = [
  {
    value: '_self',
    label: '当前窗口'
  },
  {
    value: '_blank',
    label: '新窗口'
  }
]

export default {
  name: 'MenuForm',
  model: {
    prop: 'menu',
    event: 'input'
  },
  props: {
    menu: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  computed: {
    menuModel: {
      get() {
        return this.menu
      },
      set(value) {
        this.$emit('input', value)
      }
    },
    isUpdateMode() {
      return !!this.menuModel.id
    }
  },
  data() {
    return {
      targets,
      form: {
        rules: {
          name: [
            { required: true, message: '* 菜单名称不能为空', trigger: ['change'] },
            { max: 50, message: '* 菜单名称的字符长度不能超过 50', trigger: ['change'] }
          ],
          url: [
            { required: true, message: '* 菜单地址不能为空', trigger: ['change'] },
            { max: 1023, message: '* 菜单地址的字符长度不能超过 1023', trigger: ['change'] }
          ],
          icon: [{ max: 50, message: '* 菜单图标的字符长度不能超过 50', trigger: ['change'] }]
        },
        saving: false,
        errored: false
      }
    }
  },
  methods: {
    handleCreateOrUpdateMenu() {
      const _this = this
      _this.$refs.menuForm.validate(valid => {
        if (valid) {
          _this.form.saving = true
          if (_this.isUpdateMode) {
            menuApi
              .update(_this.menuModel.id, _this.menuModel)
              .catch(() => {
                _this.form.errored = true
              })
              .finally(() => {
                setTimeout(() => {
                  _this.form.saving = false
                }, 400)
              })
          } else {
            menuApi
              .create(_this.menuModel)
              .catch(() => {
                _this.form.errored = true
              })
              .finally(() => {
                setTimeout(() => {
                  _this.form.saving = false
                }, 400)
              })
          }
        }
      })
    },
    handleSavedCallback() {
      const _this = this
      if (_this.form.errored) {
        _this.form.errored = false
      } else {
        _this.menuModel = { target: '_self' }
        _this.$emit('succeed')
      }
    },
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>
