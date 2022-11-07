import service from '@/utils/service'

const baseUrl = '/api/admin/tag'

const tagApi = {}

tagApi.list = more => {
  return service({
    url: baseUrl,
    params: {
      more: more
    },
    method: 'get'
  })
}

tagApi.createWithName = name => {
  return service({
    url: baseUrl,
    data: {
      name: name
    },
    method: 'post'
  })
}

tagApi.create = tag => {
  return service({
    url: baseUrl,
    data: tag,
    method: 'post'
  })
}

tagApi.update = (tagId, tag) => {
  return service({
    url: `${baseUrl}/${tagId}`,
    data: tag,
    method: 'put'
  })
}

tagApi.delete = tagId => {
  return service({
    url: `${baseUrl}/${tagId}`,
    method: 'delete'
  })
}

export default tagApi
