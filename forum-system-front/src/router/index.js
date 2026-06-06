import { createRouter, createWebHistory } from 'vue-router'

import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import ArticleDetail from '../views/ArticleDetail.vue'
import ArticleEdit from '../views/ArticleEdit.vue'
import MyArticle from '@/views/MyArticle.vue'

const routes = [
  {
    path: '/',
    component: Home
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/article/detail/:id',
    component: ArticleDetail
  },
  {
    path: '/article/edit',
    component: ArticleEdit,
    meta: {
      requireLogin: true
    }
  },
  {
    path: '/article/edit/:id',
    component: ArticleEdit,
    meta: {
      requireLogin: true
    }
  },
  {
    path: '/my/article',
    component: MyArticle,
    meta: {
      requireLogin: true
    }
  },
  {
    path: '/favorite',
    name: 'FavoriteList',
    component: () => import('@/views/FavoriteList.vue'),
    meta: {
      requireLogin: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requireLogin && !token) {
    next('/login')
    return
  }

  next()
})

export default router
