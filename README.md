## 分支

```bash
dev:  xxl-job 需要调度中心
master:  elastic-job 需要zookeeper & 调度中心(前后端分离部署)
```

## 私钥/公钥

```bash
openssl genrsa -out private.pem 4096

openssl rsa -in private.pem -pubout -out public.pem

openssl pkcs8 -topk8 -inform PEM -in private.pem -outform PEM -nocrypt > private_p8.pem
```

## 说明
```bash
1. redis :
    refresh_token:user_+id --> set(refresh_token) 30d
    access_token:user_+id ---> set(appToken) 7200s
    code:sms_ + phone --> code
    captcha:line_ + code --> code
2. accessToken: 7200s , refreshToken: 30d 多端共用
3. /auth/refresh_token : 参数 refresh_token, user_id
4. /login : 参数 username, password, captcha
   /login/sms : phone, code, captcha
5. /auth/captcha 获取图形验证码 base64
6. /logout 退出登录
7. /auth/sms 发送短信验证码
8. swagger-ui/index.html api文档
```

## 参考

[前端对接参考](https://github.com/boot-vue/dashboard)

## xxl-job

[文档](https://www.xuxueli.com/xxl-job/#/)

## elastic-job

[github](https://github.com/apache/shardingsphere-elasticjob)
[文档](https://shardingsphere.apache.org/elasticjob/current/cn/overview/)

## redisson
[文档](https://github.com/redisson/redisson/wiki/%E7%9B%AE%E5%BD%95)

