import request from './request'

export const getCategoryListApi = () => {
  return request.get('/category/list')
}
