## 部署步骤

1. 将编写的 Dockerfile 和打包后的 jar 上传到服务器并放置在同一个目录下

2. 执行构建镜像

   ```sh
   docker build -t qinweizhao/blog .
   ```

3. 运行容器

   ```sh
   docker run -it -d --name blog -p 8090:8090 -v ~/.blog:/root/.blog --restart=unless-stopped qinweizhao/blog
   ```

   