<template>
  <div class="card-container h-full">
    <a-tabs class="h-full" defaultActiveKey="0" type="card">
      <a-tab-pane v-for="(group, index) in form.configurations" :key="index" :tab="group.label">
        <a-form :wrapperCol="wrapperCol" layout="vertical">
          <a-form-item v-for="(item, formItemIndex) in group.items" :key="formItemIndex" :label="item.label + '：'">
            <p v-if="item.description && item.description !== ''" slot="help" v-html="item.description"></p>
            <a-input
              v-if="item.type === 'TEXT'"
              v-model="form.settings[item.name]"
              :defaultValue="item.defaultValue"
              :placeholder="item.placeholder"
            />
            <a-input
              v-else-if="item.type === 'TEXTAREA'"
              v-model="form.settings[item.name]"
              :autoSize="{ minRows: 5 }"
              :placeholder="item.placeholder"
              type="textarea"
            />
            <a-radio-group
              v-else-if="item.type === 'RADIO'"
              v-model="form.settings[item.name]"
              :defaultValue="item.defaultValue"
            >
              <a-radio v-for="(config, radioIndex) in item.options" :key="radioIndex" :value="config.value">
                {{ config.label }}
              </a-radio>
            </a-radio-group>
            <a-select
              v-else-if="item.type === 'SELECT'"
              v-model="form.settings[item.name]"
              :defaultValue="item.defaultValue"
            >
              <a-select-option v-for="config in item.options" :key="config.value" :value="config.value">
                {{ config.label }}
              </a-select-option>
            </a-select>
            <verte
              v-else-if="item.type === 'COLOR'"
              v-model="form.settings[item.name]"
              :defaultValue="item.defaultValue"
              model="hex"
              picker="square"
              style="display: inline-block; height: 24px"
            ></verte>
            <AttachmentInput
              v-else-if="item.type === 'ATTACHMENT'"
              v-model="form.settings[item.name]"
              :defaultValue="item.defaultValue"
              :placeholder="item.placeholder"
            />
            <a-input-number
              v-else-if="item.type === 'NUMBER'"
              v-model="form.settings[item.name]"
              :defaultValue="item.defaultValue"
              style="width: 100%"
            />
            <a-switch
              v-else-if="item.type === 'SWITCH'"
              v-model="form.settings[item.name]"
              :defaultChecked="item.defaultValue"
            />
            <a-input
              v-else
              v-model="form.settings[item.name]"
              :defaultValue="item.defaultValue"
              :placeholder="item.placeholder"
            />
          </a-form-item>
          <a-form-item>
            <ReactiveButton
              :errored="form.saveErrored"
              :loading="form.saving"
              erroredText="保存失败"
              loadedText="保存成功"
              text="保存"
              type="primary"
              @callback="handleSaveSettingsCallback"
              @click="handleSaveSettings"
            ></ReactiveButton>
          </a-form-item>
          <a-form-item>
            <template>
              <a-button @click="handleRouteToThemeSetting()">返回</a-button>
            </template>
          </a-form-item>
        </a-form>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script>
// components
import Verte from 'verte'
import 'verte/dist/verte.css'

// api
import themeApi from '@/api/theme'

export default {
  name: 'ThemeSettingForm',
  components: {
    Verte
  },
  props: {
    wrapperCol: {
      type: Object,
      default: () => {
        return {
          xl: { span: 8 },
          lg: { span: 8 },
          sm: { span: 12 },
          xs: { span: 24 }
        }
      }
    }
  },
  data() {
    return {
      form: {
        settings: [],
        configurations: [],
        loading: false,
        saving: false,
        saveErrored: false
      }
    }
  },
  created() {
    this.handleGetConfigurations()
  },
  watch: {
    theme(value) {
      if (value) {
        this.handleGetConfigurations()
      }
    }
  },
  methods: {
    async handleGetConfigurations() {
      try {
        const { data } = await themeApi.listConfigurations()
        this.form.configurations = data.data
        await this.handleGetSettings()
      } catch (error) {
        this.$log.error(error)
      }
    },
    async handleGetSettings() {
      try {
        const { data } = await themeApi.getMap()
        this.form.settings = data.data
      } catch (error) {
        this.$log.error(error)
      }
    },
    async handleSaveSettings() {
      try {
        this.form.saving = true
        // await themeApi.saveSettings(this.form.settings)
        await themeApi.saveMap(this.form.settings)
      } catch (error) {
        this.$log.error(error)
        this.form.saveErrored = true
      } finally {
        setTimeout(() => {
          this.form.saving = false
        }, 400)
      }
    },
    handleSaveSettingsCallback() {
      if (this.form.saveErrored) {
        this.form.saveErrored = false
      } else {
        this.handleGetSettings()
        this.$emit('saved')
      }
    },
    handleRouteToThemeSetting() {
      this.$router.push({ name: 'ThemeSetting' })
    }
  }
}
</script>
