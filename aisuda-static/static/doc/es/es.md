# 操作日志

为了方便记录用户api日志访问，需要将用户一些请求操作记录存储起来，目前仅存储api转发日志。存储介质为es，系统暂未提供可查看操作。用户可以自行安装es管理工具如：kibana

*安装命令*：

    docker network create elastic
    docker pull docker.elastic.co/elasticsearch/elasticsearch:7.14.2
    docker run --name es01-test --net elastic -p 127.0.0.1:9200:9200 -p 127.0.0.1:9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.14.2
    docker pull docker.elastic.co/kibana/kibana:7.14.2
    docker run --name kib01-test --net elastic -p 127.0.0.1:5601:5601 -e "ELASTICSEARCH_HOSTS=http://es01-test:9200" docker.elastic.co/kibana/kibana:7.14.2

访问：http://localhost:5601

## 写入

操作记录目前主要分为两块：

1. 页面API转发日志
2. API中心请求日志

### API 转发日志

实现原理：

1. api/APICenterHandler.java
2. handler/APIProxyHandler.java
3. service/EsService.java

接口转发结束的时候，将请求信息及返回信息通过 Servcie `EsService` 写入 ES 中。

## 读取

暂未实现

## 清理

目前还没有实现清理逻辑，实际上目前日志是按月存的，可以直接用 es http 接口直接将历史悠久的月份 index 删除。当然也可以写个后台任务，比如每个月底最后一天凌晨 3 点，自动将 3 年前的 es index 删除。

## 配置说明

默认服务不使用es来存储api转发日志，若要使用es，将 aisuda.es.enable 值改为 true。

*配置项说明*：

    设置es用户名密码 `aisuda.es.username` `aisuda.es.password` 配置可选，默认不开启。

    设置请求体是否存储 `aisuda.es.save.requset.payload` 配置可选，默认不开启。

    设置es地址 `aisuda.es.address` 格式为： http://esIp:esPort。

    设置es索引 `aisuda.es.index` 格式为： xxxx-{{date:YYYY-MM}} 日期会被替换，当然也可以配置非日期格式，此时会以配置为准。若此项配置为空，平台会自动生成suda-log-{{date:YYYY-MM}} 格式的索引。
     