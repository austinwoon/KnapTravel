export default {
    // Disable server-side rendering (https://go.nuxtjs.dev/ssr-mode)
    ssr: false,

    // Global page headers (https://go.nuxtjs.dev/config-head)
    head: {
        title: 'travel-reccomender',
        meta: [
            {charset: 'utf-8'},
            {name: 'viewport', content: 'width=device-width, initial-scale=1'},
            {hid: 'description', name: 'description', content: ''}
        ],
        link: [
            {rel: 'icon', type: 'image/x-icon', href: '/favicon.ico'}
        ]
    },

    env: {
        MAPBOX_API_KEY: process.env.MAPBOX_API_KEY,
    },

    // Global CSS (https://go.nuxtjs.dev/config-css)
    css: [
        'ant-design-vue/dist/antd.css'
    ],

    // Plugins to run before rendering page (https://go.nuxtjs.dev/config-plugins)
    plugins: [
        '@/plugins/antd-ui',
    ],

    // Auto import components (https://go.nuxtjs.dev/config-components)
    components: true,

    // Modules for dev and build (recommended) (https://go.nuxtjs.dev/config-modules)
    buildModules: [],

    // Modules (https://go.nuxtjs.dev/config-modules)
    modules: [
        '@nuxtjs/axios'
    ],

    axios: {
        headers: {
            common: {
                'Accept': 'application/json, text/plain, */*'
            },
            delete: {},
            get: {},
            head: {},
            post: {},
            put: {},
            patch: {}
        }
    },

    // Build Configuration (https://go.nuxtjs.dev/config-build)
    build: {}
}
