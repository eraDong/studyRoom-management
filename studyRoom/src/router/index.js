import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'layout',
      redirect: '/index',
      children: [
        {
          path: '/index',
          name: 'index',
          component: () => import('../views/indexPage.vue')
        },
        {
          path: '/studyroom',
          name: 'studyroom',
          component: () => import('../views/studyroomPage.vue')
        },
        {
          path: '/seat',
          name: 'seat',
          component: () => import('../views/seatPage.vue')
        },

      ],
      component: () => import('../views/layoutContainerPage.vue')
    },
    {
      path:'/manage',
      name:'manage',
      redirect:'/managestudyroom',
      component: () => import('../views/managePage.vue'),
      children: [
        {
          path:'/managestudent',
          name:"managestudent",
          component: () => import('../views/manageStudentPage.vue')
        },
        {
          path:'/manageseat',
          name:"manageseat",
          component: () => import('../views/manageSeatPage.vue')
        },
        {
          path:'/managestudyroom',
          name:"managestudyroom",
          component: () => import('../views/manageStudyRoomPage.vue')
        },
      ]
    }
  ]
})

export default router

