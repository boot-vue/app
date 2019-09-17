> 不适合分布式架构应用

## 配置修改

```bash
1. classpath: 指定yml配置环境, 修改logback.xml日志路径
2. demo.sql跑demo用
3. 默认url:  /login  /logout /auth/authentication /auth/authorization
4. mybatis配置: classpath:mybatis
5. 要配置好xxl-job相关配置
```

## 说明

```bash
utils : util模块
common : entity, dao, config....
auth : shiro认证与授权
service : 所有的应用服务
web: 打包入口, 所有的api接口 
```

## 其它

```bash
shiro: 自定义了session manager与cache manager, 依赖redis
redis: 重写了序列化规则

http:  okhttp-->resttemplate or httpclient

mybatis/mybatis-plus: com.bootvue.common.mapper

idutil: 生成唯一ID

线程池: 基于guava

image处理: 基于openCV

任务调度: XXL-JOB
```

