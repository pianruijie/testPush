## 数据源配置

使用Spring默认数据源配置，便于切换

配置文件在 `conf/application.properties` 中，可自行修改数据源

若要启用，切换配置中数据源即可，表结构见/resources/schema-oracle.sql文件

### 环境准备
如果本机安装了docker， 可以自行安装并启动oracle:

- 进入数据库并创建用户
```
sqlplus /nolog
alter user system identified by oracle;
alter user sys identified by oracle;
ALTER PROFILE DEFAULT LIMIT PASSWORD_LIFE_TIME UNLIMITED;
create user aisuda identified by MuYoLdc$;
grant create session to aisuda;
alter user aisuda quota unlimited on users;
```
- 建表， 执行schema-oracle.sql中脚本

## 注意事项
- 低于12.02版本的oracle需要关闭Flyway（spring.flyway.enabled=false）
- 高于12.02版本的oracle需要修改Flyway初始化脚本地址（spring.flyway.locations=classpath:db/oracle/migration）
- mysql中text对应oracle中clob类型
- mysql采用自增主键时，oracle需要定义对应的seq（见schema-oracle.sql文件），Dto对象需要定义相关策略（指定主键策略和对应的seq）