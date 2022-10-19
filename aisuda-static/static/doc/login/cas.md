## 简介
cas登录实现

## 流程
1. 点击CAS登录后跳转到CAS Server的登录界面，传递回调地址service
2. 在CAS Server登录界面输入用户名和密码
3. 登录成功后, CAS Server回调service地址并传递ticket
4. service地址请求CAS Server的校验接口, 校验ticket和service
5. CAS Server校验接口返回用户信息

## 登录流程
通过配置cas.authorize.path重定向到登录的跳转地址
cas.authorize.path=http://localhost:8080/cas/login

## 验证配置
通过配置cas.validate.path指定验证ticket地址

## 完整配置
```
cas.base.url=http://localhost:8080
cas.authorize.path=http://localhost:8080/cas/login
cas.validate.path=http://localhost:8080/cas/serviceValidate
cas.email.attribute=mail
cas.email.suffix=baidu-int.com
cas.display.attribute=display
cas.name.attribute=name
cas.id.attribute=id
```