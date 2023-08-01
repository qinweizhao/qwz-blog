import service from '@/utils/service'

const baseUrl = '/api/admin/setting'

const settingApi = {}

settingApi.getMap = () => {
  return service({
    url: `${baseUrl}/map`,
    method: 'get'
  })
}

settingApi.saveMap = configs => {
  return service({
    url: `${baseUrl}/map`,
    method: 'post',
    data: configs
  })
}

settingApi.getProperty = () => {
  return service({
    url: `${baseUrl}`,
    method: 'get'
  })
}

settingApi.listConfigurations = () => {
  return service({
    url: `${baseUrl}/configurations`,
    method: 'get'
  })
}
// 开发者选项
settingApi.page = params => {
  return service({
    url: `${baseUrl}/page`,
    params: params,
    method: 'get'
  })
}

settingApi.create = config => {
  return service({
    url: baseUrl,
    data: config,
    method: 'post'
  })
}

settingApi.delete = configId => {
  return service({
    url: `${baseUrl}/${configId}`,
    method: 'delete'
  })
}

settingApi.get = configId => {
  return service({
    url: `${baseUrl}/${configId}`,
    method: 'get'
  })
}

settingApi.update = (configId, config) => {
  return service({
    url: `${baseUrl}/${configId}`,
    data: config,
    method: 'put'
  })
}

export default settingApi
