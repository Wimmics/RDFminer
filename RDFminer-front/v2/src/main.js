import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import CoreuiVue from '@coreui/vue'
import CoreuiCharts from '@coreui/vue-chartjs'
import CIcon from '@coreui/icons-vue'
import { iconsSet as icons } from '@/assets/icons'
import DocsExample from '@/components/DocsExample'
import VueCookies from 'vue3-cookies';

const cookiesConfig = {
    expireTimes: "1d"
};

const app = createApp(App)
app.use(store)
app.use(router)
app.use(VueCookies, cookiesConfig)
app.use(CoreuiVue)
app.use(CoreuiCharts)
app.provide('icons', icons)
app.component('CIcon', CIcon)
app.component('DocsExample', DocsExample)

app.mount('#app')