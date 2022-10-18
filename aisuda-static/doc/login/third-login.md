## 简介
简单第三方登录实现

## 登录流程
登录页面配置 通过替换{{callback}}来指定重定向参数
aisuda.third.auth.authorize.url: http://www.xxx.com/login?callback={{callback}}

配置 code字段，默认期望是返回 http://www.aisuda.com/auth/third?code=xxxxxxx
如果是要配置成其他字段，可以 ISUDA_THIRD_AUTH_CODE_FIELD: ticket

Profile接口 通过{{code}}指定授权码传参
aisuda.third.auth.profile.url: http://www.xxx.com/api/getProfile?code={{code}}

如果返回 401 code_expired  则自动重新跳转登录

## 注意事项
爱速搭独立应用认定用户配置的第三方认证服务器是可信的，因此如果用户通过第三方认证登录回调后。会直接与独立应用数据库中的用户信息进行比对（以用户名作为唯一标识）并登录。并不会对用户进行二次校验。


