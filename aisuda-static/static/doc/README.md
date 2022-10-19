# 爱速搭独立部署

## 如何打开文档

所有文档在 doc 目录下。

1. 推荐安装 docsify 查看：

```
npm i -g docsify
docsify serve doc
```

2. 其它查看方式：

2.1 使用 python 静态文件服务器：

```
# 先 cd 到 doc 目录
cd doc
# python2运行
python -m SimpleHTTPServer
# python3运行
python3 -m http.server
```

2.2 安装 node 相关的服务，比如`static-server`：

```
npm -g install static-server
cd doc
static-server
```

## 简介

在爱速搭平台制作好应用后，支持导出独立应用、脱离平台单独部署。我们将其称之为「独立应用」。 独立应用所支持的功能与平台发布应用对标，区别如下：

1. 支持用户登录：支持。但账号体系独立于平台（可导入和同步，详见[组织架构](login/company.md)），登录方式目前仅支持账号密码、oauth、第三方认证三种；
2. 支持基于角色控制资源访问权限：支持。角色为在平台开发应用时设置的角色集合，导出独立应用时会将资源角色信息一并导出，但这些角色并未绑定独立应用中的账户，需要在创建用户后为用户绑定角色；
3. 支持页面 API 代理转发：支持；
4. 支持 API 中心代理转发：支持；
5. 支持 API 中心 API 编排：部分支持。包括`发送HTTP请求`、`数据源SQL`、`API中心节点`、`分支`、`循环`、`跳出循环`、`继续循环`、`退出`、`顺序执行`、`并行执行`、`设置变量`、`编码转换`、`日期格式化`
   、`数据映射`、`发送邮件`等 15 种节点类型；
6. 支持环境变量：部分支持。仅支持导出时配置的环境变量，不支持在运行时动态修改变更；
7. 支持数据模型：支持。（不支持绑定流程）；
8. 支持表单和流程：敬请期待；
9. 支持动态修改资源权限：敬请期待；
10. 支持对象存储：敬请期待（目前文件上传只能存储在数据库中）。

## 设计实现

整体设计是执行引擎的思路，项目本身不和任何爱速搭项目耦合，可以执行任意爱速搭导出的项目。

### 应用目录结构

登录到 Docker 中，/app-engine/bin 为工作目录，/app-engine 目录结构与直接下载 zip 文件解压后基本一致，以下为二者的并集：

```
.
├── README.md --------------------------------- 介绍如何打开文档
├── app --------------------------------------- 导出的应用配置
│   ├── apis.json ----------------------------- API中心配置
│   ├── appinfo.json -------------------------- 应用基本信息
│   ├── applayouts.json ----------------------- 应用框架
│   ├── components.json ----------------------- 自定义组件
│   ├── datasources.json ---------------------- 数据源
│   ├── home.json ----------------------------- 首页
│   ├── modelSchemas.json --------------------- 数据管理页面
│   ├── models.json --------------------------- 数据模型
│   ├── navigation.json ----------------------- 导航栏
│   ├── pages.json ---------------------------- 应用页面
│   ├── roles.json ---------------------------- 角色
│   └── sourceSql ----------------------------- 内置数据源对应的建表语句
├── app-engine-src ---------------------------- 源码(仅在Docker中包含)
│   ├── app ----------------------------------- 示例应用
│   ├── build.sh ------------------------------ 编译脚本
│   ├── doc ----------------------------------- 介绍文档
│   ├── generated ----------------------------- 生成代码依赖的基本文件
│   ├── generator ----------------------------- 代码生成工具源码
│   ├── settings.xml -------------------------- 本地构建可参考设置的Maven配置
│   ├── src ----------------------------------- 后端引擎源码
│   ├── start.sh ------------------------------ 启动脚本
│   └── stop.sh ------------------------------- 停止脚本
├── bin --------------------------------------- 可执行程序包及启停脚本
│   ├── aisuda-app-engine.jar ----------------- 后端引擎可执行jar文件
│   ├── extra-dependency ---------------------- 后端引擎jar启动依赖的文件(主要是针对数据模型生成的代码)
│   ├── generate-and-start.sh ----------------- 启动脚本
│   └── stop.sh ------------------------------- 停止脚本
├── conf
│   └── application.properties ---------------- 配置文件
├── doc --------------------------------------- 介绍文档
├── docker-compose.yml ------------------------ 本地多容器运行配置文件(应用默认会使用此文件中定义的mysql/redis镜像，如需使用自定义mysql/redis，需要修改conf中的连接配置，同时可删除此文件中相关依赖)
├── db-data ----------------------------------- 数据库数据存储目录(下载zip中会带有此空目录，单机部署时mysql数据挂载于此)
│   ├── engine -------------------------------- 单机部署时应用自身数据库数据挂载于此，如重启需清空数据将此目录清空即可
│   └── user ---------------------------------- 单机部署时用户自建数据库数据挂载于此，如重启需清空数据将此目录清空即可
├── redis-data -------------------------------- redis数据存储目录(下载zip中会带有此空目录，单机部署时redis数据挂载于此，如重启需清空数据将此目录清空即可)
├── webroot ----------------------------------- 编译好的前端资源文件夹
│   ├── index.html ---------------------------- 首页
│   ├── static -------------------------------- 一些静态资源文件
│   ├── images -------------------------------- 一些图片资源文件
│   └── style --------------------------------- 一些样式资源文件
└── .env -------------------------------------- 指定了docker-compose.yml中依赖镜像的版本
```

## 使用说明

独立应用提供两种启动方式，推荐使用 docker 安装方式直接部署，下文介绍 docker 环境下的部署启动方式。

如果希望以非 docker 方式安装部署，请参考[这里](raw-deploy.md)。

### 环境需求

需要您的机器安装`Docker`，推荐版本为`18.09`及以上，爱速搭独立应用可以运行在单机 Docker 环境上，如果需要集群化、高可用，可以使用 Docker 自带的 swarm 或者 k8s。

Docker 的安装建议参考官方文档，可以安装在 Centos、Ubuntu、Windows、Mac 等系统上。

生产环境推荐使用 Linux 操作系统，最小机器配置是 4 核 4G 内存，建议配置是 8 核 8G 及以上。

#### 部署环境修改
在构建Docker Image时，可以通过参数自定义基础镜像（注：基础镜像必须是Java环境），具体参数及其默认值如下：
```
ARG JAVA_BASE_IMAGE=openjdk:11
```
在docker构建时通过 --build-arg 修改基础环境的版本，具体命令如下：
docker build --build-arg JAVA_BASE_IMAGE=openjdk:13

### 单机部署

单机版本依赖`docker-compose`，在 Mac 和 Windows 下的 Docker 程序会自带，如果是 Linux 则需要执行以下命令来安装：

```
sudo curl -L "https://github.com/docker/compose/releases/download/1.26.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

在爱速搭平台导出独立应用后，下载得到一个 zip 文件，将其解压后 cd 到解压目录直接运行：

```
docker-compose up
```

_注_：

1. 解压路径最好不要携带非英文及特殊字符，否则可能出现不预期问题
2. 默认配置 redis/mysql 访问宿主机地址`host.docker.internal`，如果使用 docker 版本低于`18.03`，需要修改配置文件，将其改为`docker0`地址，默认为`172.17.0.1`

### 分布式版本

爱速搭独立应用支持多实例部署，目前不限制实例数量，需要先装好 mysql 和 redis，然后通过修改连接配置连到远程的 mysql/redis 节点。
另外，参考`docker-compose.yml`文件中的`volume`挂载配置，爱速搭独立应用依赖`app`、`conf`、`webroot`三个文件夹的内容，需要复制或者挂载到运行 Docker 中对应的目录；
也可在导出独立应用后直接本地构建 Dockerfile，使用本地构建的新镜像作为集群部署的基础镜像）要求本地能使用 `openjdk:11`）：
```shell
# 先 cd 到下载应用的解压目录，然后运行如下命令构建镜像（标签可自行定义）
docker build . -t registry.baidubce.com/aisuda/engine:latest
```

### docker容器下环境变量替换

在根目录.env文件中添加环境变量， 或者创建 dev.env 文件并在docker启动时设置env_file，具体内容类似如下。

    ```
    MYSQL_ROOT_PASSWORD=123456
    YOG_DEBUG=true
    YOG_ENV=dev
    NODE_ENV=dev
    ISUDA_DB_USER=root
    ISUDA_DB_PASSWORD=123456
    ISUDA_DB_NAME=isuda
    ISUDA_DB_HOST=db
    ISUDA_DB_PORT=3306
    ISUDA_REDIS_HOST=redis
    ISUDA_REDIS_PORT=6379
    ISUDA_SITE_MODE=saas
    ISUDA_LICENSE=k0OuofYbyrlgko6AVO6BmbFcCYmlVC8eT0WAgmSN8bt6y+i7+FSjIn4GOBCZFPv8OqjQAKNIi5osIBpDOX9W+DhuPKHBV7JuTpYluL0JSL3H2pxnaR6dFoCF5FBX5smGuJ+VZ2WYBdcte0vfsS4DAhFOlvdw3u98UWVjtipmZqsZax6fG8LNsUBefTt6Q5wmmmg/enKpGRLRdvmytXtmdYHi+wbcZxbvaDoXP1hyt9o=
    ```


### 如何在无网环境安装？

首先是 Docker，Windows 和 Mac 可以通过上面的安装包离线安装，而 Linux
需要参考[这里](https://docs.docker.com/engine/install/binaries/#install-daemon-and-client-binaries-on-linux)下载二进制文件来安装。

其次是 docker-compose，参考[文档](https://docs.docker.com/compose/install/)，要求在有网环境下操作，可在[这里](https://github.com/docker/compose/releases)下载对应环境的二进制文件。

接下来需要将镜像也保存为文件，方法是找一台能联网且有 Docker 的机器，运行如下命令(具体下载的版本号以导出独立应用 zip 中 docker-compose.yml 文件描述的为准)：

```
docker pull registry.baidubce.com/aisuda/engine:1.0.47
docker pull registry.baidubce.com/aisuda/mysql:5.7
docker pull registry.baidubce.com/aisuda/redis:5
docker save -o engine.tar registry.baidubce.com/aisuda/engine:1.0.47
docker save -o mysql.tar registry.baidubce.com/aisuda/mysql:5.7
docker save -o redis.tar registry.baidubce.com/aisuda/redis:5
```

将这三个文件上传到需要安装的服务器上，执行如下命令：

```
docker load -i engine.tar
docker load -i mysql.tar
docker load -i redis.tar
```
