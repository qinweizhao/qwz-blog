import service from '@/utils/service'

const baseUrl = '/api/admin/themes'

const themeApi = {}

themeApi.getProperty = () => {
  return service({
    url: `${baseUrl}`,
    method: 'get'
  })
}

themeApi.listConfigurations = () => {
  return service({
    url: `${baseUrl}/configurations`,
    method: 'get'
  })
}

themeApi.getSettings = () => {
  return service({
    url: `${baseUrl}/settings`,
    method: 'get'
  })
}

themeApi.saveSettings = settings => {
  return service({
    url: `${baseUrl}/settings`,
    data: settings,
    method: 'post'
  })
}

themeApi.exists = template => {
  return service({
    url: `${baseUrl}/activation/template/exists`,
    method: 'get',
    params: {
      template: template
    }
  })
}

export default themeApi
