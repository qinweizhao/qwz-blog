<p align="center">
  <a class="logo" href="https://github.com/qinweizhao/qwz-blog">
    <img src="https://cdn.jsdelivr.net/gh/qinweizhao/qwz-blog@master/logo.png" height="80" width="45%" alt="Authority">
  </a>
</p>

<p align="center">
👉 <a href="https://www.qinweizhao.com">https://www.qinweizhao.com</a> 👈
</p>

<p align="center">
  <a href="https://github.com/qinweizhao/qwz-blog" target="_blank">
    <img src="https://img.shields.io/github/v/release/qinweizhao/qwz-blog?include_prereleases" alt="Release"/>
  </a>
</p>


![Alt](https://repobeats.axiom.co/api/embed/407d1af8c2e1faff46c37b1336137e2d0d7e27c4.svg "Analytics image")
## 1、简介

根据个人需求，基于 Halo-1.4.2 博客改造的个人（博客）站点。

### 仓库结构

```
qwz-blog
├─blog-backend  后端源码
│
├─blog-frontend 前端源码
│  ├─blog-frontend-admin 后台管理
│  ├─blog-frontend-portal 前台门面
│ 
├─blog-resource 项目资源
│  ├─img 图片
│  ├─jar 依赖包
```

## 2、改造

### 移除

- 两步验证
- 迁移数据库
- 与 GitHub 交互

### 修复

- 后台管理前端 UI 显示

### 转换

- Gradle 转为 Maven

### 新增

- 利用 maven 插件将前端代码编译并复制到后端的 resources 文件夹下。


