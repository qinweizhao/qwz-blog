import service from '@/utils/service'

const baseUrl = '/api/admin/article'

const articleApi = {}

articleApi.listLatest = top => {
  return service({
    url: `${baseUrl}/latest`,
    params: {
      top: top
    },
    method: 'get'
  })
}

articleApi.query = params => {
  return service({
    url: baseUrl,
    params: params,
    method: 'get'
  })
}

articleApi.get = articleId => {
  return service({
    url: `${baseUrl}/${articleId}`,
    method: 'get'
  })
}

articleApi.create = postToCreate => {
  return service({
    url: baseUrl,
    method: 'post',
    data: postToCreate
  })
}

articleApi.update = (articleId, postToUpdate) => {
  return service({
    url: `${baseUrl}/${articleId}`,
    method: 'put',
    data: postToUpdate
  })
}

articleApi.updateDraft = (articleId, content) => {
  return service({
    url: `${baseUrl}/${articleId}/status/draft/content`,
    method: 'put',
    data: {
      content: content
    }
  })
}

articleApi.updateStatus = (articleId, status) => {
  return service({
    url: `${baseUrl}/${articleId}/status/${status}`,
    method: 'put'
  })
}

articleApi.updateStatusInBatch = (ids, status) => {
  return service({
    url: `${baseUrl}/status/${status}`,
    data: ids,
    method: 'put'
  })
}

articleApi.delete = articleId => {
  return service({
    url: `${baseUrl}/${articleId}`,
    method: 'delete'
  })
}

articleApi.deleteInBatch = ids => {
  return service({
    url: `${baseUrl}`,
    data: ids,
    method: 'delete'
  })
}

articleApi.preview = articleId => {
  return service({
    url: `${baseUrl}/preview/${articleId}`,
    method: 'get'
  })
}

articleApi.importMarkdown = (formData, uploadProgress, cancelToken) => {
  return service({
    url: `${baseUrl}/markdown/import`,
    timeout: 8640000, // 24 hours
    data: formData, // form data
    onUploadProgress: uploadProgress,
    cancelToken: cancelToken,
    method: 'post'
  })
}

articleApi.postStatus = {
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
  }
}

export default articleApi
