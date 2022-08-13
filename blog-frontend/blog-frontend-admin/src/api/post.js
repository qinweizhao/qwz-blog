import service from '@/utils/service'

const baseUrl = '/api/admin/posts'

const postApi = {}

postApi.listLatest = top => {
  return service({
    url: `${baseUrl}/latest`,
    params: {
      top: top
    },
    method: 'get'
  })
}

postApi.query = params => {
  return service({
    url: baseUrl,
    params: params,
    method: 'get'
  })
}

postApi.get = postId => {
  return service({
    url: `${baseUrl}/${postId}`,
    method: 'get'
  })
}

postApi.create = postToCreate => {
  return service({
    url: baseUrl,
    method: 'post',
    data: postToCreate
  })
}

postApi.update = (postId, postToUpdate) => {
  return service({
    url: `${baseUrl}/${postId}`,
    method: 'put',
    data: postToUpdate
  })
}

postApi.updateDraft = (postId, content) => {
  return service({
    url: `${baseUrl}/${postId}/status/draft/content`,
    method: 'put',
    data: {
      content: content
    }
  })
}

postApi.updateStatus = (postId, status) => {
  return service({
    url: `${baseUrl}/${postId}/status/${status}`,
    method: 'put'
  })
}

postApi.updateStatusInBatch = (ids, status) => {
  return service({
    url: `${baseUrl}/status/${status}`,
    data: ids,
    method: 'put'
  })
}

postApi.delete = postId => {
  return service({
    url: `${baseUrl}/${postId}`,
    method: 'delete'
  })
}

postApi.deleteInBatch = ids => {
  return service({
    url: `${baseUrl}`,
    data: ids,
    method: 'delete'
  })
}

postApi.preview = postId => {
  return service({
    url: `${baseUrl}/preview/${postId}`,
    method: 'get'
  })
}

postApi.postStatus = {
  PUBLISHED: {
    value: 'PUBLISHED',
    color: 'green',
    status: 'success',
    text: '已发布'
  },
  DRAFT: {
    value: 'DRAFT',
    color: 'yellow',
    status: 'warning',
    text: '草稿'
  },
  RECYCLE: {
    value: 'RECYCLE',
    color: 'red',
    status: 'error',
    text: '回收站'
  },
  INTIMATE: {
    value: 'INTIMATE',
    color: 'blue',
    status: 'success',
    text: '私密'
  }
}

export default postApi
