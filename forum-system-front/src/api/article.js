import request from './request'
export const getArticleListApi = (params) => {
  return request.get('/article/list', { params })
}

export const getHotArticleApi = (params) => {
  return request.get('/article/hot', { params })
}

export const getArticleDetailApi = (id) => {
  return request.get(`/article/detail/${id}`)
}

export const likeArticleApi = (id) => {
  return request.post(`/article/like/${id}`)
}

export const addArticleApi = (data) => {
  return request.post('/article/add', data)
}

// 我的文章列表
export const getMyArticleListApi = () => {
  return request.get('/article/myList')
}

// 删除文章
export const deleteArticleApi = (id) => {
  return request.delete(`/article/delete/${id}`)
}

// 修改文章
export const updateArticleApi = (data) => {
  return request.put('/article/update', data)
}

//搜索文章
export const searchArticleApi = (params) => {
  return request.get('/article/search', { params })
}

export const favoriteArticleApi = (id) => {
  return request.post(`/article/favorite/${id}`)
}

export const isFavoritedApi = (id) => {
  return request.get(`/article/isFavorited/${id}`)
}

export const favoriteListApi = () => {
  return request.get('/article/favorite/list')
}
