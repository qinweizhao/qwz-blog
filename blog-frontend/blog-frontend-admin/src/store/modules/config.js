import Vue from 'vue'
import { OPTIONS } from '@/store/mutation-types'
import settingApi from '@/api/setting'

const keys = `
  blog_url,
  developer_mode,
  attachment_upload_image_preview_enable,
  attachment_upload_max_parallel_uploads,
  attachment_upload_max_files,
  post_permalink_type,
  archives_prefix,
  default_editor,
  default_menu_team
`
const config = {
  state: {
    options: undefined
  },
  mutations: {
    SET_OPTIONS: (state, options) => {
      Vue.ls.set(OPTIONS, options)
      state.options = options
    }
  },
  actions: {
    refreshOptionsCache({ commit }) {
      return new Promise((resolve, reject) => {
        settingApi
          .getMap()
          .then(response => {
            console.log("response.data.data")
            console.log(response.data.data)
            commit('SET_OPTIONS', response.data.data)
            resolve(response)
          })
          .catch(error => {
            reject(error)
          })
      })
    }
  }
}

export default config
