import Router from 'vue-router'
import oldIndex from './oldIndex.js'
import curIndex from './curIndex.js'

let routes = new Set([...oldIndex, ...curIndex])
const router = new Router({
    mode: 'history',
    // base: process.env.NODE_ENV === 'production' ? '/' : '/diyh5/',
    base: '/',
    routes: routes
})

export default router