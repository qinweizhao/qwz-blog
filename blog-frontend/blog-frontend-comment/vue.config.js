const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {

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