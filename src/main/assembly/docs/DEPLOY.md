项目说明

# 环境要求

jdk版本：jdk 1.8

系统：linux /windows

推荐部署在linux

# 项目部署

打包后的项目部署比较简单，开箱即可启动
```
//解压tar.gz包
tar -zxvf iflyrecommend-1.0.tar.gz
//解压后启动脚本在项目的bin目录中，项目配置文件在config中，日志文件在logs目录中
```
解压后的项目大致结构
```
├─bin
│      dump.sh
│      server.sh
│      setenv.sh
│      start.bat
│      start.sh
│      stop.sh
│      yaml.sh   
├─config
│      application.yml
├─docs
│      DEPLOY.md
├─lib
│      iflyrecommend.jar  
└─logs
```

# 启动应用

第一种通过start.sh来启动
```
# 启动应用

./start.sh

# 以debug方式启动，此处debug为jvm环境的debug
./start.sh debug

# 启动并开启jmx监控

./start.sh jmx
# 获取应用当前的运行状态
./start.sh status
```
第二种通过server.sh来启动

```
# 启动应用

./server.sh

# 以debug方式启动，此处debug为jvm环境的debug
./server.sh debug

# 启动任务并开启jmx监控

./server.sh jmx
# 获取当前的运行状态
./server.sh status

```
# 停止应用
```
./stop.sh
或
./server.sh stop
```
**注意：** 以上脚本如果不能正常执行请先检查脚本的用户权限
# 启动日志

启动日志在应用的logs下
## 日志调整
目前默认开启的日志是debug，对于生产环境需要将日志关闭，修改日志只需要在config中的logback-boot.xml修改日志级别即可

```
  <!-- 级别依次为【从高到低】：FATAL > ERROR > WARN > INFO > DEBUG > TRACE  -->
<logger name="com.iflytek" level="DEBUG">
    <appender-ref ref="syslog" />
</logger>
```


# jvm参数调整

服务启动的jvm参数设置是在setenv.sh中，目前设置比较小，但是如果setenv.sh不存在，应用使用start.sh中默认的
jvm参数，强力推荐不要在start.sh中去修改jvm，设置也相对麻烦，因此推荐在setenv.sh中去设置jvm参数.
```
# set jvm params

export JAVA_OPTS="$JAVA_OPTS -Xms512m"
export JAVA_OPTS="$JAVA_OPTS -Xmx512m"
export JAVA_OPTS="$JAVA_OPTS -Xss256K"

# The hotspot server JVM has specific code-path optimizations
# which yield an approximate 10% gain over the client version.
export JAVA_OPTS="$JAVA_OPTS -server"

# Basically tell the JVM not to load awt libraries
export JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=true"

# set encoding
export JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=utf-8"

# set garbage collector
# export java_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"

# only for jdk 1.7
#export JAVA_OPTS="$JAVA_OPTS -XX:MaxPermSize="256m
```
# 关于配置文件加载
目前start.sh能够实现除xml外的配置文件自动从打包的config目录加载配置文件。xml主要有些配置文件需要相应xsd解析，
脚本自动加载的路径并不是classpath,解析会有问题。建议不要在少在springboot中使用xml配置，如果要用请在开发阶段修改assembly.xml，
主要就是增加一个将xml配置文件打包到和jar同级的目录中。

开发人员注意：对于properties配置文件，start.sh会帮你在启动时自动加载作为jar项目的配置，
但是目前只能使用spring的注解来加载properties,因为这里打包后的config不是classpath，
如果自己编写properties工具获取的将是jar中原始properties设置，值无法覆盖。

Usage:
```
@PropertySource(value = {"config.properties"})
public class Config{
   @Value("${application.name}")
   private String name;//tps

}
```

# 配置修改
注意在修改yml文件中的配置时请严格按照原来的格式修改，字符缩进错误可能会影响，应用的启动
## 应用端口号修改
端口号在config/application.yml

```
server:
  port: 9088
```
## hbase配置修改
hbase的配置在config/application.yml中
```
data:
    hbase:
      master: 192.168.45.154:16000
      zkQuorum: 192.168.45.150,192.168.45.151,192.168.45.152    
      poolSize: 100
      zkPort: 2181
      accessId: 705d97087b8ac07ba7c9
      accessKey: 163e44d7e75847ebf54310237a823f97803991f5

```
配置参数说明

配置参数     | 参数说明    
-------- | ------------- 
master   | HBase主节点      
zkQuorum | ZooKeeper集群地址 
zkPort   | ZooKeeper的端口号 
poolSize | hbase客服端线程池大小 
accessId |移动bc-hadoop bcid认证访问的id
accessKey |移动bc-hadoop bcid认证访问的key
## 查询条数限制
目前每次查询的条数是可以自由控制的，默认配置是100条，如果需要调整，则到config/config.properties修改。
```
application.limit=100
```
# 应用限流配置

tps限流配置在config/config.properties中
```
# 接口限流的tps数
application.tps=100
# 超过tps是否丢弃。false则阻塞处理超流请求
application.drop=true
```


# 接口测试

## OTT推荐

测试实例：

```
curl -X POST -H 'Content-Type: text/xml' -i http://192.168.248.1:9088/iflytek/api/suggestion/rec_ott --data '<?xml version="1.0" encoding="GBK"?>

<operation_in> 
  <verify_code>210094032167747711503958</verify_code>  
  <sysfunc_id>60500020</sysfunc_id>  
  <service_name>BSM_crm_queryMenuInfo</service_name>  
  <accept_info> 
    <accept_city>590</accept_city>  
    <accept_org_id>9990000</accept_org_id>  
    <accept_province>5910</accept_province>  
    <accept_id>1234567890</accept_id> 
  </accept_info>  
  <request_source>201028</request_source>  
  <request_time>20080109101506</request_time>  
  <content> 
    <uids>y_00000000000000,222,333</uids>  
    <request_time>20170125152411001</request_time>  
    <app_id>yishiteng20170125</app_id>  
    <secure_msg>fejlbbfnpbnnmngfjknehckkjnlkadomfpfkfgnflebhkjeecpbebognpbamffhddbohaiddcimcilbdkpmhildbjbcdpjob</secure_msg> 
  </content> 
</operation_in>
'
```
## 播放结束推荐接口
测试实例：

```
curl -X POST -H 'Content-Type: text/xml' -i http://192.168.248.1:9088/iflytek/api/suggestion/rec_vod_end --data '<?xml version="1.0" encoding="GBK"?>

<operation_in> 
  <verify_code>210094032167747711503958</verify_code>  
  <sysfunc_id>60500020</sysfunc_id>  
  <service_name>BSM_crm_queryMenuInfo</service_name>  
  <accept_info> 
    <accept_city>590</accept_city>  
    <accept_org_id>9990000</accept_org_id>  
    <accept_province>5910</accept_province>  
    <accept_id>1234567890</accept_id> 
  </accept_info>  
  <request_source>201028</request_source>  
  <request_time>20080109101506</request_time>  
  <content> 
    <uids>y_00000000000000,222,333</uids>  
    <request_time>20170125152411001</request_time>  
    <app_id>yishiteng20170125</app_id>  
    <secure_msg>fejlbbfnpbnnmngfjknehckkjnlkadomfpfkfgnflebhkjeecpbebognpbamffhddbohaiddcimcilbdkpmhildbjbcdpjob</secure_msg> 
  </content> 
</operation_in>
'
```
## 详情页关联推荐
测试实例：

```
curl -X POST -H 'Content-Type: text/xml' -i http://192.168.248.1:9088/iflytek/api/suggestion/rec_vod_relate --data '<?xml version="1.0" encoding="GBK"?>

<operation_in> 
  <verify_code>210094032167747711503958</verify_code>  
  <sysfunc_id>60500020</sysfunc_id>  
  <service_name>BSM_crm_queryMenuInfo</service_name>  
  <accept_info> 
    <accept_city>590</accept_city>  
    <accept_org_id>9990000</accept_org_id>  
    <accept_province>5910</accept_province>  
    <accept_id>1234567890</accept_id> 
  </accept_info>  
  <request_source>201028</request_source>  
  <request_time>20080109101506</request_time>  
  <content> 
    <uids>y_00000000000000,222,333</uids>  
    <request_time>20170125152411001</request_time>  
    <app_id>yishiteng20170125</app_id>  
    <secure_msg>fejlbbfnpbnnmngfjknehckkjnlkadomfpfkfgnflebhkjeecpbebognpbamffhddbohaiddcimcilbdkpmhildbjbcdpjob</secure_msg> 
  </content> 
</operation_in>
'
```