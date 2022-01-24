module.exports = {
  publicPath: process.env.PUBLIC_PATH,
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          'border-radius-base': '2px'
        },
        javascriptEnabled: true
      }
    }
  },

  devServer: {
    host: '0.0.0.0',
    port: 8080,
    open: true,
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy
      ['/']: {
        target: `http://localhost:8090`,
        changeOrigin: true,
        pathRewrite: {
          '^/': '/'
        }
      }
    },
    disableHostCheck: true
  },

  lintOnSave: false,
  transpileDependencies: [],
  productionSourceMap: false
}
