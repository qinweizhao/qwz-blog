import service from '@/utils/service'
const baseUrl = '/api/content'

const commentApi = {}

commentApi.createComment = (target, comment) => {
    return service({
        url: `${baseUrl}/${target}/comment`,
        method: 'post',
        data: comment
    })
}

commentApi.listComments = (target, targetId, view = 'tree_view', pagination) => {
    return service({
        url: `${baseUrl}/${target}/${targetId}/comment/${view}`,
        params: pagination,
        method: 'get'
    })
}

export default commentApi