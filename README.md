<p align="center">
  <a class="logo" href="https://github.com/qinweizhao/qwz-blog">
    <img src="https://cdn.jsdelivr.net/gh/qinweizhao/qwz-blog@master/logo.png" height="80" width="45%" alt="Blog">
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
## 简介

目前 master 分支代码不可用（开发中），选择发行版进行下载。
根据个人需求，基于 Halo-1.4.2 博客改造的个人博客。

## 结构

```
qwz-blog
├─blog-backend  后端源码
│
├─blog-frontend 前端源码
│  ├─blog-frontend-admin 后台管理
│  ├─blog-frontend-portal 前台门面
│ 
├─blog-resource 项目资源
│  ├─docker 容器部署
│  ├─image 图片
│  ├─jar 依赖包
│  ├─logs 项目日志
│  ├─sql 数据库文件
│  ├─static 静态资源
```

## 改造

|            C             |            U             |          D           |
| :----------------------: | :----------------------: | :------------------: |
| 一次性打包编译前后端代码 |       调整后台布局       |       两步验证       |
|                          |      前台搜索不准确      |      多主题选择      |
|                          |    Gradle 转为 Maven     |      迁移数据库      |
|                          | 后台管理前端 UI 显示缺陷 |     后台布局设置     |
|                          |                          |    数据库版本管理    |
|                          |                          | 与 GitHub 交互的功能 |

## 效果

### 前台：

https://www.qinweizhao.com/

### 后台：

<table>
    <tr>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201508.png"/></td>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201552.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201755.png"/></td>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201804.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201811.png"/></td>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201815.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201820.png"/></td>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201826.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201834.png"/></td>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201904.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201914.png"/></td>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201925.png"/></td>
    </tr>
    <tr>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_201931.png"/></td>
        <td><img src="https://gitee.com/qinweizhao/qwz-blog/raw/master/blog-resource/image/2022-04-02_202053.png"/></td>
    </tr>
</table>
