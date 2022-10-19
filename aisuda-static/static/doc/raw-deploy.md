# 非Docker模式安装
如果无法使用Docker，爱速搭独立应用也能直接在机器上运行。

## 启动依赖

- 环境依赖：Java 11
- 服务依赖：MySQL、Redis(可选)、ES(可选)

**注**
：Java环境为应用运行依赖的基本环境；MySQL用于存储应用的用户、角色等数据，这两个依赖为必须。Redis用于存储用户登录session及页面API路由缓存，可使用MySQL替代；ES用于存储页面API转发记录，若要启用此功能可开启之，非必须。

## 服务配置

配置文件在 `conf/application.properties` 中，可自行修改配置项。

默认使用本地 redis，若要禁用redis，将 `spring.redis.enable` 值改为 `false` 。若不修改配置文件，需要确保本地 `6379` 端口运行了可访问的 redis 服务。

默认使用本地 mysql，若不修改配置文件，需要确保本地 `3306` 端口运行了可访问的 mysql 服务，支持使用用户名 `aisuda` 密码 `MuYoLdc$ `进行访问。

**注意**：如果使用本机 mysql 服务，如果版本在 8 以上，由于加密方式的修改，需要配置`serverRSAPublicKeyFile`或者将`allowPublicKeyRetrieval`设置为`true`
，即在`aisuda.mysql.url`配置末尾添加`allowPublicKeyRetrieval=True`，如下所示：

```
aisuda.mysql.url=jdbc:mysql://localhost:3306/aisuda?useSSL=false&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=round&allowPublicKeyRetrieval=true
```

默认服务不使用es来存储api转发日志，若要使用es，将 `aisuda.es.enable` 配置为 `true` 。

**配置项说明**：

- 设置es用户名密码 `aisuda.es.username` `aisuda.es.password` 配置可选，默认不开启。
- 设置请求体是否存储 `aisuda.es.save.requset.payload` 配置可选，默认不开启。
- 设置es地址 `aisuda.es.address` 格式为： http://esIp:esPort 。
- 设置es索引 `aisuda.es.index` 格式为： xxxx-{{date:YYYY-MM}} 日期会被替换，当然也可以配置非日期格式，此时会以配置为准。若此项配置为空，平台会自动生成suda-log-{{date:
  YYYY-MM}} 格式的索引。

### 运行时环境变量替换
- 前提：在平台设计时设置了应用环境变量，入口在 应用设置 -> 环境变量。
- 假定在平台配置了名为 `ENV_TEST` 的环境变量，希望在独立应用启动时进行替换，则需要在application.properties文件中新增参数aisuda.env.ENV_TEST，将会覆盖平台导出时的环境变量，
  其他环境变量同理, 统一以aisuda.env为前缀，进行相应替换。

### 自定义登录页
- 自定义登录页logo: 在application.properties中配置环境变量aisuda.login.logo，例如(aisuda.login.logo=https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png)
- 自定义登录页背景: 在application.properties中配置环境变量aisuda.login.background，例如(aisuda.login.background=https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png)

**注意**：请务必确认配置的地址浏览器可访问，建议配置网络地址或CDN地址，不支持使用本地文件相对路径

### 启动导出应用并访问

如果应用使用了平台内置数据源，可以修改`conf/application.properties`中的数据库配置，改为替代数据库。默认会使用本地`3306`端口mysql服务中名为`innerdb`
的数据库。若该数据库未初始化，登录该数据库，运行`app/sourceSql`文件夹下的sql文件，创建数据表。

**注意**：由于独立应用中数据库和平台数据库是完全隔离的，因此无法感知内置数据源中表结构的更新，为方便后续程序的升级，强烈建议不要配置使用内置数据源！

```
# 创建数据库及授权
CREATE DATABASE innerdb;
GRANT ALL PRIVILEGES ON *.* TO 'aisuda'@'%' WITH GRANT OPTION;

# 切换到数据库
USE innerdb;

# 创建数据表，注意替换成本地路径
source /your/local/path/app/sourceSql/TheExportedXxxTables.sql
```

配置好数据库后，运行如下命令启动应用：

```
cd bin
sh start.sh
```

默认端口是 8899，启动后访问: http://localhost:8899

默认的管理员账号：用户名：`admin`，密码：`adminspassword`
<br>
<br>
启动脚本默认使用`../app`文件夹下的应用配置，如果需要指定其它app文件夹，请使用如下命令：

```
cd bin
sh start.sh -d other/dir/app
```

**注意**：由于前端资源在应用导出时已经固定，因此当指定非默认app文件夹时可能会出现前端样式渲染问题，建议重新导出
