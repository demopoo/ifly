package com.iflytek.dao;

import com.iflytek.config.hbase.HbaseProperties;
import com.iflytek.exception.SecurityVerificationException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Hbase数据库连接层
 *
 * @author jjliu15@iflytek.com
 * @date 2017/12/7
 */
@Slf4j
@NoArgsConstructor
public class HBaseDao {

    /**
     * 点播推荐结果表
     */
    private static final String RESULT_TB_NAME = "FJTv:DbRecommendResult";
    /**
     * 看点推荐结果表
     */
    private static final String FOCUS_TB_NAME = "FJTv:KdRecommendResult";
    /**
     * 安全校验表
     */
    private static final String SECURITY_TB_NAME = "FJTv:SecurityCheck";
    /**
     * 用户点击行为表(暂时没用)
     */
    private static final String CLICK_TB_NAME = "FJTv:UserClick";
    /**
     * 默认用户ID
     */
    private static final String DEFAULT_USER_ID = "y_00000000000000";
    /**
     * 用于管理Hbase的对象
     */
    private Admin admin;
    /**
     * 用于查询Hbase的对象
     */
    private Connection connection;

    /**
     * 通过Spring自动注入
     *
     * @param prop hbase连接配置
     */
    public HBaseDao(HbaseProperties prop) {
        init(prop);
    }

    /**
     * 初始化连接
     *
     * @param prop hbase连接配置
     */
    private void init(HbaseProperties prop) {
        try {
            Configuration conf = HBaseConfiguration.create(new Configuration());
            conf.set("hbase.zookeeper.quorum",prop.getZkQuorum());
            conf.set("hbase.zookeeper.property.clientPort", prop.getZkPort());
            conf.set("hbase.master", prop.getMaster());
            conf.set("zookeeper.znode.parent", prop.getZkBasePath());
            conf.set("hadoop.security.bdoc.access.id",prop.getAccessId());
            conf.set("hadoop.security.bdoc.access.key",prop.getAccessKey());
            this.connection = ConnectionFactory.createConnection(conf, Executors.newFixedThreadPool(prop.getPoolSize()));
            this.admin = connection.getAdmin();
            if (this.connection != null) {
                log.info("HBase连接成功");
//                initTable();
            } else {
                log.info("HBase连接失败");
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            log.info("HBase连接异常");
        }
    }

    /**
     * 初始化表，纯HBASEAPI建表
     */
    private void initTable() throws IOException {
        TableName tableName = TableName.valueOf(SECURITY_TB_NAME);
        if (!admin.tableExists(tableName)) {
            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(SECURITY_TB_NAME));
            table.addFamily(new HColumnDescriptor("secure"));
            table.addFamily(new HColumnDescriptor("info"));
            admin.createTable(table);
            log.info(SECURITY_TB_NAME + "表已创建");
        }
    }

    /**
     * 获取根据uid获取点播推荐结果,启用缓存,避免重复请求数据库
     *
     * @param uid
     * @return
     * @throws IOException
     */
    @Cacheable(value = "resCache", key = "#uid.concat('-' + #type)")
    public String getDbResult(String uid, String type) throws IOException {
        long start = System.currentTimeMillis();
        Table table = connection.getTable(TableName.valueOf(RESULT_TB_NAME));
        Get get = new Get(Bytes.toBytes(uid));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(type));
        String resStr = Bytes.toString(value);
        if (resStr == null || resStr.length() < 1) {
            resStr = getDefault(table, type);
        }
        log.debug("getDbResult costTime:{} ms",(System.currentTimeMillis() - start));
        table.close();
        return resStr;
    }



    /**
     * 获取根据uid获取看点结果,启用缓存,避免重复请求数据库
     *
     * @param uid
     * @param type
     * @return
     * @throws IOException
     */
    @Cacheable(value = "resCache", key = "#uid.concat('-' + #type)")
    public String getKdResult(String uid, String type) throws IOException {
        long start = System.currentTimeMillis();
        Table table = connection.getTable(TableName.valueOf(FOCUS_TB_NAME));
        Get get = new Get(Bytes.toBytes(uid));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(type));
        String resStr = Bytes.toString(value);
        if (resStr == null || resStr.length() < 1) {
            return getDefault(table, "rec");
        }
        log.debug("getKbResult costTime:{} ms",(System.currentTimeMillis() - start));
        table.close();
        return resStr;
    }



    /**
     * 用户无数据时获取默认推荐
     *
     * @param table
     * @param column
     * @return
     * @throws IOException
     */
    private String getDefault(Table table, String column) throws IOException {
        Get get = new Get(Bytes.toBytes(DEFAULT_USER_ID));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(column));
        String resStr = Bytes.toString(value);
        table.close();
        return resStr;
    }

    /**
     * 根据appid获取appkey
     *
     * @param appId
     * @return
     */
    @Cacheable(value = "appKeyCache", key = "#appId")
    public String getAppKey(String appId) throws IOException {
        Table table = connection.getTable(TableName.valueOf(SECURITY_TB_NAME));
        Get get = new Get(Bytes.toBytes(appId));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("secure"), Bytes.toBytes("appkey"));
        table.close();
        String resStr = Bytes.toString(value);
        if (resStr == null) {
            throw new SecurityVerificationException("appkey不存在");
        }
        return resStr;
    }

    /**
     * 待实现
     * @param uids
     * @param type
     * @return
     * @throws IOException
     */
    public Map<String,String> batchGetDbResult(List<String> uids,String type) throws IOException {
//        List<String> uids = new ArrayList<>();
//        uids.add(DEFAULT_USER_ID);
//        uids.add("aa");
        List<Get> getList = new ArrayList();
        Table table = connection.getTable(TableName.valueOf(RESULT_TB_NAME));
        for (String uid : uids){
            Get get = new Get(Bytes.toBytes(uid));
            getList.add(get);
        }

        Result[] results = table.get(getList);
        for (Result result : results){
            String rowKey = Bytes.toString(result.getRow());
            System.out.println("rowKey:"+rowKey);
            byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(type));
            String resStr = Bytes.toString(value);
            System.out.println(resStr);
//            if (resStr == null || resStr.length() < 1) {
//                resStr = getDefault(table, type);
//            }
        }
        return null;
    }

}
