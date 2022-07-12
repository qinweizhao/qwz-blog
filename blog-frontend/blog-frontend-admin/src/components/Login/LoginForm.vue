<template>
  <div>
    <a-form-model
      ref="loginForm"
      :model="form.model"
      :rules="form.rules"
      layout="vertical"
      @keyup.enter.native="handleLogin()"
    >
      <a-form-model-item v-if="!form.needAuthCode" prop="username">
        <a-input v-model="form.model.username" placeholder="用户名/邮箱">
          <a-icon slot="prefix" style="color: rgba(0, 0, 0, 0.25)" type="user" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item v-if="!form.needAuthCode" prop="password">
        <a-input v-model="form.model.password" placeholder="密码" type="password">
          <a-icon slot="prefix" style="color: rgba(0, 0, 0, 0.25)" type="lock" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item>
        <a-button :block="true" :loading="form.logging" type="primary" @click="handleLogin()">
          {{ buttonName }}
        </a-button>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>
<script>
import { mapActions } from 'vuex'

export default {
  name: 'LoginForm',
  data() {
    return {
      form: {
        model: {
          password: null,
          username: null
        },
        rules: {
          username: [{ required: true, message: '* 用户名/邮箱不能为空', trigger: ['change'] }],
          password: [{ required: true, message: '* 密码不能为空', trigger: ['change'] }]
        },
        needAuthCode: false,
        logging: false
      }
    }
  },
  computed: {
    buttonName() {
      return this.form.needAuthCode ? '验证' : '登录'
    }
  },
  methods: {
    ...mapActions(['login', 'refreshUserCache', 'refreshOptionsCache']),
    handleLogin() {
      const _this = this
      _this.$refs.loginForm.validate(valid => {
        if (valid) {
          _this.form.logging = true
          _this
            .login(_this.form.model)
            .then(() => {
              _this.$emit('success')
            })
            .finally(() => {
              setTimeout(() => {
                _this.form.logging = false
              }, 300)
            })
        }
      })
    }
  }
}
</script>
