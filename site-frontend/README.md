## 部署方案

目前 Halo 的运行包内已经包含了构建好的页面，所以如果你不需要额外部署 admin，你无需做任何操作。

### 独立部署

#### 方式一

直接下载最新构建好的版本，然后部署即可。

https://github.com/halo-dev/halo-admin/releases

#### 方式二

1、克隆项目：

```bash
git clone https://github.com/halo-dev/halo-admin
```

2、检出最新版本：

```bash
git checkout v1.4.16
```

3、打包构建：

```bash
npm install -g pnpm

pnpm install

pnpm build
```

最后，得到 dist 文件夹之后就可以单独部署了。
