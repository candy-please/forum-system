import request from './request'

export const getCommentListApi = (articleId) => {
  return request.get('/comment/list', {
    params: {
      articleId
    }
  })
}

export const addCommentApi = (data) => {
  return request.post('/comment/add', data)
}
