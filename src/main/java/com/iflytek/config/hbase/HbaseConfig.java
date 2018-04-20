package com.iflytek.config.hbase;import org.apache.hadoop.hbase.HBaseConfiguration;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.context.properties.EnableConfigurationProperties;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.data.hadoop.hbase.HbaseTemplate;/** * @author: demopoo * @Date: Created in 上午9:55 2018/4/19 * @Des: Hbase的配置文件 * @Modifyed By: */@Configuration@EnableConfigurationProperties(HbaseProperties.class)public class HbaseConfig {    @Autowired    private HbaseProperties hbaseProperties;    @Bean    public HbaseTemplate hbaseTemplate(){        //下面一行为window本地运行所用        //System.setProperty("hadoop.home.dir", "D:\\ProgramFiles\\hadoop-2.7.3\\hadoop-2.7.3");        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();        configuration.set("hbase.master",this.hbaseProperties.getMaster());        configuration.set("hbase.zookeeper.quorum", this.hbaseProperties.getZkQuorum());        configuration.set("hbase.rootdir", this.hbaseProperties.getRootDir());        configuration.set("zookeeper.znode.parent", this.hbaseProperties.getZkBasePath());        return new HbaseTemplate(configuration);    }}