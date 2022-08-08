import service from '@/utils/service'

const baseUrl = '/api/admin/options'

const configApi = {}

configApi.listAll = () => {
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

configApi.save = options => {
  return service({
    url: `${baseUrl}/map_view/saving`,
    method: 'post',
    data: options
  })
}

configApi.create = option => {
  return service({
    url: baseUrl,
    data: option,
    method: 'post'
  })
}

configApi.delete = optionId => {
  return service({
    url: `${baseUrl}/${optionId}`,
    method: 'delete'
  })
}

configApi.get = optionId => {
  return service({
    url: `${baseUrl}/${optionId}`,
    method: 'get'
  })
}

configApi.update = (optionId, option) => {
  return service({
    url: `${baseUrl}/${optionId}`,
    data: option,
    method: 'put'
  })
}

configApi.type = {
  INTERNAL: {
    value: 'INTERNAL',
    text: '系统'
  },
  CUSTOM: {
    value: 'CUSTOM',
    text: '自定义'
  }
}

export default configApi
