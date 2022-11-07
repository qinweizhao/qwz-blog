import service from '@/utils/service'

const baseUrl = '/api/admin'

const commentApi = {}

commentApi.latestComment = (target, top, status) => {
  return service({
    url: `${baseUrl}/${target}/comment/latest`,
    params: {
      top: top,
      status: status
    },
    method: 'get'
  })
}

commentApi.queryComment = (target, params) => {
  return service({
    url: `${baseUrl}/${target}/comment`,
    params: params,
    method: 'get'
  })
}

commentApi.commentTree = (target, id, params) => {
  return service({
    url: `${baseUrl}/${target}/comment/${id}/tree_view`,
    params: params,
    method: 'get'
  })
}

commentApi.updateStatus = (target, commentId, status) => {
  return service({
    url: `${baseUrl}/${target}/comment/${commentId}/status/${status}`,
    method: 'put'
  })
}

commentApi.updateStatusInBatch = (target, ids, status) => {
  return service({
    url: `${baseUrl}/${target}/comment/status/${status}`,
    data: ids,
    method: 'put'
  })
}

commentApi.delete = (target, commentId) => {
  return service({
    url: `${baseUrl}/${target}/comment/${commentId}`,
    method: 'delete'
  })
}

commentApi.deleteInBatch = (target, ids) => {
  return service({
    url: `${baseUrl}/${target}/comments`,
    data: ids,
    method: 'delete'
  })
}

commentApi.create = (target, comment) => {
  return service({
    url: `${baseUrl}/${target}/comment`,
    data: comment,
    method: 'post'
  })
}

commentApi.update = (target, commentId, comment) => {
  return service({
    url: `${baseUrl}/${target}/comment/${commentId}`,
    data: comment,
    method: 'put'
  })
}

// Creation api
commentApi.commentStatus = {
  PUBLISHED: {
    value: 'PUBLISHED',
    color: 'green',
    status: 'success',
    text: '已发布'
  },
  AUDITING: {
    value: 'AUDITING',
    color: 'yellow',
    status: 'warning',
    text: '待审核'
  },
  RECYCLE: {
    value: 'RECYCLE',
    color: 'red',
    status: 'error',
    text: '回收站'
  }
}

export default commentApi
