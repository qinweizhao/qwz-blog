<template>
  <div>
    <a-form-model
      ref="loginForm"
      :model="form.model"
      :rules="form.rules"
      layout="vertical"
      @keyup.enter.native="handleLogin"
    >
      <a-form-model-item class="animated fadeInUp" :style="{ 'animation-delay': '0.1s' }" prop="username">
        <a-input placeholder="用户名/邮箱" v-model="form.model.username">
          <a-icon slot="prefix" type="user" style="color: rgba(0, 0, 0, 0.25)" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item class="animated fadeInUp" :style="{ 'animation-delay': '0.2s' }" prop="password">
        <a-input v-model="form.model.password" type="password" placeholder="密码">
          <a-icon slot="prefix" type="lock" style="color: rgba(0, 0, 0, 0.25)" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item class="animated fadeInUp" :style="{ 'animation-delay': '0.3s' }">
        <a-button :loading="form.logging" type="primary" :block="true" @click="handleLoginClick">登录</a-button>
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
          username: null,
        },
        rules: {
          username: [{ required: true, message: '* 用户名/邮箱不能为空', trigger: ['change'] }],
          password: [{ required: true, message: '* 密码不能为空', trigger: ['change'] }],
        },
        logging: false,
      },
    }
  },

  methods: {
    ...mapActions(['login', 'refreshUserCache', 'refreshOptionsCache']),
    handleLoginClick() {
      const _this = this
      _this.$refs.loginForm.validate((valid) => {
        if (valid) {
          _this.form.logging = true
          _this.handleLogin()
        }
      })
    },
    handleLogin() {
      const _this = this
      _this.form.logging = true
      _this.$refs.loginForm.validate((valid) => {
        if (valid) {
          _this
            .login(_this.form.model)
            .then((response) => {
              _this.$emit('success')
            })
            .finally(() => {
              setTimeout(() => {
                _this.form.logging = false
              }, 300)
            })
        }
      })
    },
  },
}
</script>
