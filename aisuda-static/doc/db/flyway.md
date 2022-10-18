## 简介
Flyway是一款开源的数据库版本管理工具，支持包括Mysql、Oracle、PostgreSql、SqlServer在内的多种数据库，方便多环境部署时高效迭代数据库结构和初始化数据信息

## 启动流程
1. 项目启动时完成数据库连接后自动启动
2. 初次启动时会自动建立一张schema表（对应配置文件中spring.flyway.table），用于记录SQL执行记录
3. 项目启动时会扫描指定目录下（对应配置文件中spring.flyway.locations）的所有SQL脚本，与schema表中记录进行比对，
   注意若数据库执行记录与项目中不一致时会报错
4. 校验通过后，根据schema表中SQL记录的最大版本号，忽略小于等于的版本号，依次执行大于的版本

## 使用注意事项
1. 初始化版本以默认以V开头，加数字作为版本号，再加__作为分隔符，后续跟上脚本名称，以.sql结尾，默认只执行一次。
2. 重复执行脚本默认以R开头，命名方式同上，可执行多次
3. 新增表或字段，应当新建以V开头的一次性初始化脚本
4. 更多细节参照官方文档：https://flywaydb.org/documentation/
5. 如使用Oracle，社区版Flyway仅支持12.02以后版本，之前的版本需要关闭（spring.flyway.enabled=false）