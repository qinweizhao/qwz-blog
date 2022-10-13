import axios from 'axios'
import Vue from 'vue'
import { message, notification } from 'ant-design-vue'
import store from '@/store'
import { isObject } from './util'

const service = axios.create({
  timeout: 10000,
  withCredentials: true
})

function setTokenToHeader(config) {
  // set token
  const token = store.getters.token
  Vue.$log.debug('Got token from store', token)
  if (token && token.access_token) {
    config.headers['Admin-Authorization'] = token.access_token
  }
}
service.interceptors.request.use(
  config => {
    config.baseURL = store.getters.apiUrl
    // TODO set token
    setTokenToHeader(config)
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

let isRefreshingToken = false
let pendingRequests = []

service.interceptors.response.use(
  response => {
    return response
  },
  async error => {
    if (axios.isCancel(error)) {
      return Promise.reject(error)
    }

    if (/Network Error/.test(error.message)) {
      message.error('网络错误，请检查网络连接')
      return Promise.reject(error)
    }

    const token = store.getters.token
    const originalRequest = error.config

    const response = error.response

    const data = response ? response.data : null

    if (data) {
      if (data.status === 400) {
        const params = data
        console.log('params')
        console.log(params)
        if (isObject(params)) {
          notification.error({
            message: data.message,
            duration: 10
          })
        } else {
          message.error(data.message)
        }

        return Promise.reject(error)
      }
      if (data.status === 401) {
        if (!isRefreshingToken) {
          isRefreshingToken = true
          try {
            await store.dispatch('refreshToken', token.refresh_token)

            pendingRequests.forEach(callback => callback())
            pendingRequests = []

            return axios(originalRequest)
          } catch (e) {
            // message.warning('当前登录状态已失效，请重新登录')
            await store.dispatch('ToggleLoginModal', true)
            return Promise.reject(e)
          } finally {
            isRefreshingToken = false
          }
        } else {
          return new Promise(resolve => {
            pendingRequests.push(() => {
              resolve(axios(originalRequest))
            })
          })
        }
      }
      message.error(data.message || '服务器错误')
      return Promise.reject(error)
    }

    message.error('网络异常')
    return Promise.reject(error)
  }
)

export default service
