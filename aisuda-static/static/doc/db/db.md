## 数据源配置

- 为支持多数据源兼容配置，技术方案采用Spring-Data-Jpa，底层基于Hibernate, 支持多数据库方言，具备可迁移性 
- 使用Spring默认数据源配置，便于切换Mysql/Oracle/PostgreSql，配置文件在 `conf/application.properties` 中，可自行修改
- 默认使用本地 mysql，若不修改配置文件，需要确保本地`3306`端口运行了可访问的 mysql 服务，支持使用用户名`aisuda`密码`MuYoLdc$`进行访问，并包含`schema.sql`文件所示的表结构。

## 注意事项
### 表相关
- 注意不要使用数据库关键字，例如order等
- 注意兼容oracle和mysql，尽量使用通用配置
- 自增主键不同的数据库有不同的实现，需要指定策略
### SQL相关
- 预先了解Spring-Data-Jpa对应知识，参考文档：https://docs.spring.io/spring-data/jpa/docs/2.5.5/reference/html/#preface
- 避免使用原生SQL，有复杂需求优先考虑使用JPQL