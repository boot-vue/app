
## 配置修改

```bash
1. classpath: 指定yml配置环境, 修改logback.xml日志路径
2. demo.sql跑demo用
3. 默认url:  /login  /logout /auth/authentication /auth/authorization
4. mybatis配置: classpath:mybatis
5. 要配置好elastic-job相关配置(yml里 添加scheduler.config相关配置, 没有的话不会激活elastic-job的自动配置), 见: com.bootvue.config.scheduler.ElasticJobAutoConfiguration
6. zookeeper最好配置, 任务配置或者其它功能会用到
```

## 分支
```bash
master: 基础功能
```

## 说明

```bash
util : util模块
common : entity, dao, config....
auth : 认证与授权
service : 所有的应用服务
web: 打包入口, 所有的api接口 
```

## 其它

```bash
redis: 重写了序列化规则

http:  okhttp-->resttemplate or httpclient

mybatis/mybatis-plus: com.bootvue.common.mapper

idutil: 生成唯一ID

线程池: 基于guava  ThreadPoolUtil ,demo见main方法

image处理

excel: https://github.com/Crab2died/Excel4J

任务调度: elsetic-job easyscheduler(重量级, 后期再加入)
```

### end
```bash
有一些第三方依赖好多年不维护了
谨慎使用
图片压缩的 thumbnailator
任务调度的 elastic-job, 为了兼容 elastic-job, 项目里的guava降级到了20.0版本
```


### 计划  2.X

