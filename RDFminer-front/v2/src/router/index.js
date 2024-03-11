// import { h, resolveComponent } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'

import DefaultLayout from '@/layouts/DefaultLayout'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: DefaultLayout,
    // redirect: '/dashboard',
    children: [
      {
        path: '/',
        name: 'Dashboard',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () =>
          import(/* webpackChunkName: "dashboard" */ '@/views/Dashboard.vue'),
      },
      {
        path: '/publications',
        name: 'Publications',
        component: () => import('@/views/Publications.vue'),
      },
      {
        path: '/contacts',
        name: 'Contacts',
        component: () => import('@/views/Contacts.vue'),
      },
      {
        path: '/tutorial',
        name: 'Tutorial',
        component: () => import('@/views/Tutorial.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    // always scroll to top
    return { top: 0 }
  },
})

export default router
