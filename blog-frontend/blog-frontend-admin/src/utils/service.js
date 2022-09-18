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

async function reRequest(error) {
  const config = error.response.config
  setTokenToHeader(config)
  return await axios.request(config)
}

let refreshTask = null

async function refreshToken(error) {
  const refreshToken = store.getters.token.refresh_token
  try {
    if (refreshTask === null) {
      refreshTask = store.dispatch('refreshToken', refreshToken)
    }

    await refreshTask
  } catch (err) {
    if (err.response && err.response.data && err.response.data.data === refreshToken) {
      message.warning('当前登录状态已失效，请重新登录')
      await store.dispatch('ToggleLoginModal', true)
    }
    Vue.$log.error('Failed to refresh token', err)
  } finally {
    refreshTask = null
  }
  return reRequest(error)
}

function getFieldValidationError(data) {
  if (!isObject(data) || !isObject(data.data)) {
    return null
  }

  const errorDetail = data.data

  return Object.keys(errorDetail).map(key => errorDetail[key])
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
        const params = data.data

        if (isObject(params)) {
          const paramMessages = Object.keys(params || {}).map(key => params[key])
          notification.error({
            message: data.message,
            description: h => {
              const errorNodes = paramMessages.map(errorDetail => {
                return h('a-alert', {
                  props: {
                    message: errorDetail,
                    banner: true,
                    showIcon: false,
                    type: 'error'
                  }
                })
              })
              return h('div', errorNodes)
            },
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
            message.warning('当前登录状态已失效，请重新登录')
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
      message.error(data.data.message || '服务器错误')
      return Promise.reject(error)
    }

    message.error('网络异常')
    return Promise.reject(error)
  }
  // error => {
  //   if (axios.isCancel(error)) {
  //     Vue.$log.debug('Cancelled uploading by user.')
  //     return Promise.reject(error)
  //   }
  //
  //   Vue.$log.error('Response failed', error)
  //
  //   const response = error.response
  //   const status = response ? response.status : -1
  //   Vue.$log.error('Server response status', status)
  //
  //   const data = response ? response.data : null
  //   if (data) {
  //     let handled = false
  //     // Business response
  //     Vue.$log.error('Business response status', data.status)
  //     if (data.status === 400) {
  //       // TODO handle 400 status error
  //       const errorDetails = getFieldValidationError(data)
  //       if (errorDetails) {
  //         handled = true
  //
  //         notification.error({
  //           message: data.message,
  //           description: h => {
  //             const errorNodes = errorDetails.map(errorDetail => {
  //               return h('a-alert', {
  //                 props: {
  //                   message: errorDetail,
  //                   banner: true,
  //                   showIcon: false,
  //                   type: 'error'
  //                 }
  //               })
  //             })
  //             return h('div', errorNodes)
  //           },
  //           duration: 10
  //         })
  //       }
  //     } else if (data.status === 401) {
  //       // TODO handle 401 status error
  //       if (store.getters.token && store.getters.token.access_token === data.data) {
  //         const res = refreshToken(error)
  //         if (res !== error) {
  //           return res
  //         }
  //       } else {
  //         // Login
  //         message.warning('当前登录状态已失效，请重新登录')
  //         store.dispatch('ToggleLoginModal', true)
  //       }
  //     } else if (data.status === 403) {
  //       // TODO handle 403 status error
  //     } else if (data.status === 404) {
  //       // TODO handle 404 status error
  //     } else if (data.status === 500) {
  //       // TODO handle 500 status error
  //     }
  //
  //     if (!handled) {
  //       message.error(data.message)
  //     }
  //   } else {
  //     message.error('网络异常')
  //   }
  //
  //   return Promise.reject(error)
  // }
)

export default service
