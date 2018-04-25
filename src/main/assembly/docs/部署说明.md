项目说明
# 项目部署

打包后的项目部署比较简单，开箱即可启动
```
//解压tar.gz包
tar -zxvf iflyrecommend-1.0.tar.gz
//解压后启动脚本在项目的bin目录中，项目配置文件在config中，日志文件在logs目录中
```

# 启动应用

第一种通过start.sh来启动
```
# 启动应用

./start.sh

# 以debug方式启动
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

# 以debug方式启动
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

服务启动的jvm参数设置目前是在start.sh中，默认设置的参数较小，
实际环境下需要自己修改jvm参数。只需要找到在start.sh中找到JAVA_MEM_OPTS修改参数即可

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

```
配置参数说明

配置参数 | 参数说明
---|---
master | HBase主节点
zkQuorum | ZooKeeper集群地址
zkPort | ZooKeeper的端口号
poolSize|hbase客服端线程池大小




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
    <secure_msg>jpghljcgaahhechpdigjlmhegkepebbbckdggmcjehfjppglmfefdloiooofpmpkdiniombjaglbokhjeekapnfhnkcgaihp</secure_msg> 
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
    <secure_msg>jpghljcgaahhechpdigjlmhegkepebbbckdggmcjehfjppglmfefdloiooofpmpkdiniombjaglbokhjeekapnfhnkcgaihp</secure_msg> 
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
    <secure_msg>jpghljcgaahhechpdigjlmhegkepebbbckdggmcjehfjppglmfefdloiooofpmpkdiniombjaglbokhjeekapnfhnkcgaihp</secure_msg> 
  </content> 
</operation_in>
'
```