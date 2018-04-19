项目已经增加使用assembly的打包功能，打包后可以执行脚本直接启动。
如果不想使用assembly打包，只需要将pom中的assembly插件注释掉即可，其他不用改动。

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

# jvm参数调整

服务启动的jvm参数设置目前是在start.sh中，默认设置的参数较小，
实际环境下需要自己修改jvm参数。只需要找到在start.sh中找到JAVA_MEM_OPTS修改参数即可
