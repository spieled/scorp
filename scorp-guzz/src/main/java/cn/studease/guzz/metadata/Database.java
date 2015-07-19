package cn.studease.guzz.metadata;

import cn.studease.guzz.DdlException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.guzz.util.CloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Database {
    private final Map<String, Table> tables = new HashMap();

    private Logger log = LoggerFactory.getLogger(Database.class);
    private DatabaseMetaData meta;

    public Database(Connection connection) {
        try {
            this.meta = connection.getMetaData();
        } catch (SQLException e) {
            throw new DdlException(e);
        }
    }


    public Table getTableMetadata(String tableName) {
        Table table = (Table) this.tables.get(tableName.toLowerCase());
        if (table != null) {
            return table;
        }


        ResultSet rs = null;
        try {
            rs = this.meta.getTables(null, null, tableName, Constants.TYPES);
            while (rs.next()) {
                String name = rs.getString("TABLE_NAME");
                if (name.equalsIgnoreCase(tableName)) {
                    table = new Table(rs, this.meta);
                    this.tables.put(tableName.toLowerCase(), table);
                    return table;
                }
            }
            this.log.info("数据表不存在：" + tableName);
        } catch (SQLException e) {
            throw new DdlException("无法获取数据库元信息", e);
        } finally {
            CloseUtil.close(rs);
        }

        return null;
    }
}


