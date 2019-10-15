import Vue from 'vue'
import axios from 'axios'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'
import echarts from 'echarts'
import MyMinxins from './layout/mixin/MyMixins'

import '@/icons' // icon
import '@/permission' // permission control

// set ElementUI lang to EN
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n
Vue.use(echarts)
Vue.use(ElementUI, { locale, size: 'small' })

axios.defaults.headers.post['Authorization'] = 'Basic Y2xpZW50OnNlY3JldA=='

Vue.config.productionTip = false

Vue.mixin(MyMinxins)

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
