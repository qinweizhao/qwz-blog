import service from '@/utils/service'

const baseUrl = '/api/admin/config'

const configApi = {}

configApi.getMap = keys => {
  return service({
    url: `${baseUrl}/map`,
    params: {
      keys: keys
    },
    method: 'get'
  })
}

configApi.saveMap = configs => {
  return service({
    url: `${baseUrl}/map`,
    method: 'post',
    data: configs
  })
}

configApi.page = params => {
  return service({
    url: `${baseUrl}/page`,
    params: params,
    method: 'get'
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
