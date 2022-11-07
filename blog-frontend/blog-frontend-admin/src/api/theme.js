import service from '@/utils/service'

const baseUrl = '/api/admin/theme'

const themeApi = {}

themeApi.getMap = () => {
  return service({
    url: `${baseUrl}/map`,
    method: 'get'
  })
}

themeApi.saveMap = configs => {
  return service({
    url: `${baseUrl}/map`,
    method: 'post',
    data: configs
  })
}

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

export default themeApi
