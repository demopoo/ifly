项目已经增加使用assembly的打包功能，打包后可以执行脚本直接启动。
如果不想使用assembly打包，只需要将pom中的assembly插件注释掉即可，其他不用改动。
hadoop 版本号2.6.2
hbase 版本号1.0.2

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

服务启动的jvm参数设置是在setenv.sh中，目前设置比较小，但是如果setenv.sh不存在，应用使用start.sh中默认的
jvm参数，强力推荐不要在start.sh中去修改jvm，设置也相对麻烦，因此推荐在setenv.sh中去设置jvm参数。


# 开发注意事项
目前脚本存在缺陷，如果自己增加的项目启动必须的配置文件，请修改start.bat和start.sh中的-Dspring.config.location
的后面追加自己的配置文件，否则config中的配置文件将不会生效，sh的脚本推荐不要windows环境上编辑
修改，最好将sh放都linux系统上修改好了在将整个文件拷贝替换。

因为hbase服务使用的是移动的二次开发的hbase，很多依赖包最好使用移动的。如有需要可获取一些相关的文档。
