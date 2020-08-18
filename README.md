## 分支
```bash
dev:  xxl-job
master:  elastic-job 需要zookeeper
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
    token:user_+id --> {userId,username,accessToken,refreshToken,authorities}
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

