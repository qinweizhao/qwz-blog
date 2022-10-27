const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {

  devServer: {
    host: 'localhost',
    port: 8080,
    open: false,
    proxy: {
      // purchaseitem: https://cli.vuejs.org/config/#devserver-proxy
      ['/']: {
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
    // allowedHosts: 'all',
    historyApiFallback: true
  },

  publicPath: process.env.NODE_ENV === 'wc' ? '/themes/blog-frontend-portal/source/lib/halo-comment' : '/',
  configureWebpack: process.env.NODE_ENV === 'wc' ? {
    plugins: [
      new CopyWebpackPlugin([{
        from: 'public/assets',
        to: 'assets'
      }])
    ],
  } : {}

}