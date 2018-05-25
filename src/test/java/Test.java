

import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.conf.Configuration;
import org.springframework.cache.annotation.Cacheable;

public class Test {

    /**
     * 点播推荐结果表
     */
    private static final String RESULT_TB_NAME = "FJTv:DbRecommendResult";
    /**
     * 看点推荐结果表
     */
    private static final String FOCUS_TB_NAME = "FJTv:KdRecommendResult";

    /**
     * 默认用户ID
     */
    private static final String DEFAULT_USER_ID = "y_00000000000000";

    public static void main(String[] args) {
        Configuration conf = HBaseConfiguration.create(new Configuration());
        conf.set("hbase.zookeeper.quorum", "master,slave1,slave2");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.master", "192.168.248.145:16000");
        conf.set("hadoop.security.bdoc.access.id","");
        conf.set("hadoop.security.bdoc.access.key","");
        //UserGroupInformation.setConfiguration(conf);
        try{
            Connection conn = ConnectionFactory.createConnection(conf, Executors.newFixedThreadPool(100));
//            Admin hAdmin = conn.getAdmin();
//            HTableDescriptor hTableDesc = new HTableDescriptor(
//                    TableName.valueOf("Customer"));
//            hTableDesc.addFamily(new HColumnDescriptor("name"));
//            hTableDesc.addFamily(new HColumnDescriptor("contactinfo"));
//            hTableDesc.addFamily(new HColumnDescriptor("address"));
//            hAdmin.createTable(hTableDesc);
//            hAdmin.close();
           // getTable(conn);
            getScanData(conn,RESULT_TB_NAME);
            getScanData(conn,FOCUS_TB_NAME);
            getDbResult(conn,DEFAULT_USER_ID,"rc");
            getKdResult(conn,DEFAULT_USER_ID,"rc");
            System.out.println("Table created Successfully...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 点播
     * @param conn
     * @param uid 用户编号
     * @param type qualifier
     * @return
     * @throws IOException
     */
    public static String getDbResult(Connection conn,String uid, String type) throws IOException {
        System.out.println("查询点播");
        long start = System.currentTimeMillis();
        Table table = conn.getTable(TableName.valueOf(RESULT_TB_NAME));
        Get get = new Get(Bytes.toBytes(uid));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(type));
        String resStr = Bytes.toString(value);
        if (resStr == null || resStr.length() < 1) {
            resStr = getDefault(table, type);
        }
        System.out.println("result:"+resStr);
        System.out.println("getDbResult costTime:"+(System.currentTimeMillis() - start)+" ms");
        table.close();
        return resStr;
    }

    /**
     * 看点
     *
     * @param uid
     * @param type
     * @return
     * @throws IOException
     */
    public static String getKdResult(Connection conn,String uid, String type) throws IOException {
        System.out.println("查询看点");
        long start = System.currentTimeMillis();
        Table table = conn.getTable(TableName.valueOf(FOCUS_TB_NAME));
        Get get = new Get(Bytes.toBytes(uid));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(type));
        String resStr = Bytes.toString(value);
        if (resStr == null || resStr.length() < 1) {
            return getDefault(table, "rec");
        }
        System.out.println("result:"+resStr);
        System.out.println("getDbResult costTime:"+(System.currentTimeMillis() - start)+" ms");
        table.close();
        return resStr;
    }



    private static String getDefault(Table table, String column) throws IOException {
        Get get = new Get(Bytes.toBytes(DEFAULT_USER_ID));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes(column));
        String resStr = Bytes.toString(value);
        table.close();
        return resStr;
    }

    /**
     * 扫描指定的表
     * @param conn
     * @param tableName
     */
    public static void getScanData(Connection conn,String tableName) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            ResultScanner rs = table.getScanner(new Scan());
            for (Result r : rs) {
                for (Cell cell : r.listCells()) {
                    System.out.println(new String(CellUtil.cloneRow(cell)) + "\t" + new String(CellUtil.cloneFamily(cell))
                            + "\t" + new String(CellUtil.cloneQualifier(cell)) + "\t"
                            + new String(CellUtil.cloneValue(cell)) + "\t" + cell.getTimestamp());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
