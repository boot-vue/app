
## 配置修改

```bash
1. classpath: 指定yml配置环境
2. 初始启动,liquibase会加载app.sql,初始化数据库
3. 默认url:  /login  /logout 
4. mybatis配置: classpath:mybatis
5. 要配置好elastic-job相关配置(yml里 添加scheduler.config相关配置, 没有的话不会激活elastic-job的自动配置), 见: com.bootvue.config.scheduler.ElasticJobAutoConfiguration
6. zookeeper最好配置, 任务配置或者其它功能会用到
7. com.bootvue.utils.auth.JwtUtil: 修改key等配置 
```

## 私钥/公钥
```bash
openssl genrsa -out private.pem 4096

openssl rsa -in private.pem -pubout -out public.pem

openssl pkcs8 -topk8 -inform PEM -in private.pem -outform PEM -nocrypt > private_p8.pem
```

## 分支
```bash
master: 基础功能
```

## 认证
```bash
1. redis : token:user_+id --> userId,username,accessToken,refreshToken,authorities
2. accessToken: 7200s , refreshToken: 30d 多端共用
3. /auth/refresh_token : 参数 refresh_token, user_id
4. /login : 参数 username, password, captcha
5. /auth/captcha 获取图形验证码 base64
6. /logout 退出登录
```



### end
```bash
任务调度的 elastic-job, 为了兼容 elastic-job, 项目里的guava降级到了20.0版本
坐等elastic-job 3.x版本发布
```


### 计划  2.X

