import service from '@/utils/service'

const baseUrl = '/api/admin/post'

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

postApi.get = articleId => {
  return service({
    url: `${baseUrl}/${articleId}`,
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

postApi.update = (articleId, postToUpdate) => {
  return service({
    url: `${baseUrl}/${articleId}`,
    method: 'put',
    data: postToUpdate
  })
}

postApi.updateDraft = (articleId, content) => {
  return service({
    url: `${baseUrl}/${articleId}/status/draft/content`,
    method: 'put',
    data: {
      content: content
    }
  })
}

postApi.updateStatus = (articleId, status) => {
  return service({
    url: `${baseUrl}/${articleId}/status/${status}`,
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

postApi.delete = articleId => {
  return service({
    url: `${baseUrl}/${articleId}`,
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

postApi.preview = articleId => {
  return service({
    url: `${baseUrl}/preview/${articleId}`,
    method: 'get'
  })
}

postApi.importMarkdown = (formData, uploadProgress, cancelToken) => {
  return service({
    url: `${baseUrl}/markdown/import`,
    timeout: 8640000, // 24 hours
    data: formData, // form data
    onUploadProgress: uploadProgress,
    cancelToken: cancelToken,
    method: 'post'
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
  }
}

export default postApi
