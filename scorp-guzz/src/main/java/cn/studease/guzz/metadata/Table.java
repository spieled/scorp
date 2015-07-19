package cn.studease.guzz.metadata;

import cn.studease.util.StringUtil;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.guzz.util.CloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Table {
    private static final Logger log = LoggerFactory.getLogger(Table.class);

    private final String catalog;

    private final String schema;

    private final String name;

    private final List<Column> columns = new ArrayList();
    private final List<String> columnNames = new ArrayList();

    private final List<Column> pkColumns = new ArrayList();
    private final List<String> pkColumnNames = new ArrayList();

    private final List<String> indexedColumns = new ArrayList();

    public Table(ResultSet rs, DatabaseMetaData meta) throws SQLException {
        this.catalog = rs.getString("TABLE_CAT");
        this.schema = rs.getString("TABLE_SCHEM");
        this.name = rs.getString("TABLE_NAME");

        initColumns(meta);
        initPkColumns(meta);
        initIndexedColumns(meta);
    }

    private void initColumns(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = null;
        try {
            rs = meta.getColumns(this.catalog, this.schema, this.name, "%");
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                if (StringUtil.hasText(columnName)) {
                    Column column = new Column(rs);
                    this.columns.add(column);
                    this.columnNames.add(column.getName().toLowerCase());
                }
            }
        } finally {
            CloseUtil.close(rs);
        }
    }

    private void initPkColumns(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = null;
        try {
            rs = meta.getPrimaryKeys(this.catalog, this.schema, this.name);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                if (StringUtil.hasText(columnName)) {
                    Column column = new Column(rs);
                    this.pkColumns.add(column);
                    this.pkColumnNames.add(column.getName().toLowerCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(rs);
        }
    }

    private void initIndexedColumns(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = null;
        try {
            rs = meta.getIndexInfo(this.catalog, this.schema, this.name, false, true);
            while (rs.next()) {
                if (rs.getShort("TYPE") != 0) {

                    this.indexedColumns.add(new Index(rs).getColumn());
                }
            }
        } finally {
            CloseUtil.close(rs);
        }
    }

    public String getCatalog() {
        return this.catalog;
    }

    public String getSchema() {
        return this.schema;
    }

    public String getName() {
        return this.name;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    public List<String> getIndexedColumns() {
        return this.indexedColumns;
    }

    public List<Column> getPkColumns() {
        return this.pkColumns;
    }

    public List<String> getPkColumnNames() {
        return this.pkColumnNames;
    }
}


