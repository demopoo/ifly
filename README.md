项目已经增加使用assembly的打包功能，打包后可以执行脚本直接启动。
如果不想使用assembly打包，只需要将pom中的assembly插件注释掉即可，其他不用改动。

# 启动应用

第一种通过start.sh来启动
```
# 启动应用

./start.sh

# 以debug方式启动
./start.sh debug

# 启动任务并开启jmx监控

./start.sh jmx
# 获取当前的运行状态
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