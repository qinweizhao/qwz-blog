const pkg = require('./package.json')

const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  // 在npm run build 或 yarn build 时 ，生成文件的目录名称（要和baseUrl的生产环境路径一致）（默认dist）
  outputDir: 'target',
  publicPath: process.env.PUBLIC_PATH,

  devServer: {
    host: 'localhost',
    port: 8080,
    open: false,
    proxy: {
      // purchaseitem: https://cli.vuejs.org/config/#devserver-proxy
      [process.env.PUBLIC_PATH]: {
        ws: false,
        target: `http://localhost:8090`,
        changeOrigin: true,
        pathRewrite: {
          ['^']: ''
        }
      },
      ['/ws']: {
        ws: false,
        target: 'ws://localhost:8090'
      }
    },
    allowedHosts: 'all',
    historyApiFallback: true
  },
  chainWebpack: config => {
    config.plugin('html').tap(args => {
      args[0].version = pkg.version
      return args
    })
  },
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

  lintOnSave: false,
  transpileDependencies: [],
  productionSourceMap: false
})
