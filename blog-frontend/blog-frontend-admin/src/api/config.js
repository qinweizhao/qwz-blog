import service from '@/utils/service'

const baseUrl = '/api/admin/config'

const configApi = {}

configApi.list = () => {
  return service({
    url: `${baseUrl}/map_view`,
    method: 'get'
  })
}

configApi.listAllByKeys = keys => {
  return service({
    url: `${baseUrl}/map_view/keys`,
    data: keys,
    method: 'post'
  })
}

configApi.query = params => {
  return service({
    url: `${baseUrl}/list_view`,
    params: params,
    method: 'get'
  })
}

configApi.save = configs => {
  return service({
    url: `${baseUrl}/map_view/saving`,
    method: 'post',
    data: configs
  })
}

configApi.create = config => {
  return service({
    url: baseUrl,
    data: config,
    method: 'post'
  })
}

configApi.delete = configId => {
  return service({
    url: `${baseUrl}/${configId}`,
    method: 'delete'
  })
}

configApi.get = configId => {
  return service({
    url: `${baseUrl}/${configId}`,
    method: 'get'
  })
}

configApi.update = (configId, config) => {
  return service({
    url: `${baseUrl}/${configId}`,
    data: config,
    method: 'put'
  })
}

export default configApi
