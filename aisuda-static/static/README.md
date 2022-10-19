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

