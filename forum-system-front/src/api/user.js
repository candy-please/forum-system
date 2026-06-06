import request from './request'

//登录
export const loginApi = (data) => {
  return request.post('/user/login', data)
}

//注册
export const registerApi = (data) => {
  return request.post('/user/register', data)
}

//获取当前用户
export const getCurrentUserApi = () => {
  return request.get('/user/current')
}
