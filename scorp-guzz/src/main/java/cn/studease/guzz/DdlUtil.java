package cn.studease.guzz;

import cn.studease.guzz.annotation.Index;
import cn.studease.guzz.metadata.Database;
import cn.studease.guzz.metadata.Table;
import cn.studease.util.*;
import com.alibaba.fastjson.JSON;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.guzz.GuzzContext;
import org.guzz.connection.DBGroup;
import org.guzz.dialect.Dialect;
import org.guzz.orm.Business;
import org.guzz.orm.rdms.TableColumn;
import org.guzz.util.CloseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DdlUtil {
    private static final Logger log = LoggerFactory.getLogger(DdlUtil.class);

    private static ThreadLocal<Object> tableConditionLocal = new ThreadLocal();
    private static boolean parallel = false;

    private static Map<String, Business> ddlBusinessMap = new HashMap();
    private static Map<String, org.guzz.orm.rdms.Table> ddlTableMap = new HashMap();
    private static Map<String, Dialect> ddlDialectMap = new HashMap();
    private static Map<String, DBGroup> ddlDbGroupMap = new HashMap();

    public static Object getTableCondition() {
        return tableConditionLocal.get();
    }

    public static void setTableCondition(Object tableCondition) {
        tableConditionLocal.set(tableCondition);
    }

    public static boolean isParallel() {
        return parallel;
    }

    public static void setParallel(boolean parallel) {
        parallel = parallel;
    }


    public static Business getBusiness(Class<?> clazz) {
        final String className = clazz.getName();
        return CollectionUtil.getFromMap(ddlBusinessMap, className + "." + getTableCondition(), new Callback() {
            public Business run() {
                return ((GuzzContext) WebUtil.getBean(GuzzContext.class)).getBusiness(className);
            }
        });
    }


    public static org.guzz.orm.rdms.Table getTable(Class<?> clazz) {
        final Class<?> clazz1 = clazz;
        return CollectionUtil.getFromMap(ddlTableMap, clazz.getName() + "." + getTableCondition(), new Callback() {
            public org.guzz.orm.rdms.Table run() {
                return DdlUtil.getBusiness(clazz1).getTable();
            }
        });
    }


    public static Dialect getDialect(Class<?> clazz) {
        final Class<?> clazz1 = clazz;
        return CollectionUtil.getFromMap(ddlDialectMap, clazz.getName() + "." + getTableCondition(), new Callback() {
            public Dialect run() {
                return DdlUtil.getBusiness(clazz1).getTable().getDialect();
            }
        });
    }


    public static DBGroup getDbGroup(Class<?> clazz) {
        final Class<?> clazz1 = clazz;
        return CollectionUtil.getFromMap(ddlDbGroupMap, clazz.getName() + "." + getTableCondition(), new Callback() {
            public DBGroup run() {
                return DdlUtil.getBusiness(clazz1).getMapping().getDbGroup();
            }
        });
    }


    public static Connection getConnection(Class<?> clazz) {
        try {
            return getDbGroup(clazz).getPhysicsDBGroup(getTableCondition()).getMasterDB().getDataSource().getConnection();
        } catch (SQLException ignored) {
        }
        return null;
    }


    public static TableColumn[] getColumns(Class<?> clazz) {
        return getTable(clazz).getColumnsForInsert();
    }


    public static TableColumn getPKColumn(Class<?> clazz) {
        return getTable(clazz).getPKColumn();
    }


    public static void executeSql(Class<?> clazz, String... sql) {
        if (!StringUtil.hasText(sql)) {
            return;
        }
        Connection connection = getConnection(clazz);
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            for (String s : sql) {
                try {
                    stmt.execute(s);
                } catch (Exception ex) {
                    log.warn("Ddl执行SQL失败", ex);
                }
            }
        } catch (Exception e) {
            log.warn("Ddl创建Statement失败", e);
        } finally {
            CloseUtil.close(stmt);
            CloseUtil.close(connection);
        }
    }


    public static String getColumnNameForSQL(Class<?> clazz, Field field) {
        String fieldName = field.getName();
        for (TableColumn column : getColumns(clazz)) {
            if (column.getPropName().equals(fieldName)) {
                return column.getColNameForRS();
            }
        }
        throw new DdlException("不能确定字段的数据字段类型：" + clazz.getName() + "." + field.getName());
    }


    public static Table getTableMetadata(Class<?> clazz) {
        Connection connection = getConnection(clazz);
        Table table = new Database(connection).getTableMetadata(DialectTranslator.getTableName(clazz));
        CloseUtil.close(connection);
        return table;
    }


    public static void checkEntity(Class<?> clazz) {
        Object tableCondition = getTableCondition();

        log.info("DDL操作类：" + clazz.getName() + "，分表条件：" + tableCondition + "。");


        Table tableMetadata = getTableMetadata(clazz);


        if (tableMetadata == null) {
            String sql = DialectTranslator.getCreateTableSql(clazz);
            log.info("创建表：" + sql);
            executeSql(clazz, new String[]{sql});

            for (Field field : ReflectUtil.getFieldsWithAnnotation(clazz, Index.class)) {
                String[] sql1 = DialectTranslator.getAddIndexSql(clazz, field);
                log.info("建索引：" + JSON.toJSONString(sql1));
                executeSql(clazz, sql1);
            }

            return;
        }

        log.info("表：" + (tableMetadata.getCatalog() == null ? "" : new StringBuilder().append(tableMetadata.getCatalog()).append(".").toString()) + (tableMetadata.getSchema() == null ? "" : new StringBuilder().append(tableMetadata.getSchema()).append(".").toString()) + tableMetadata.getName());
        log.info("列：" + tableMetadata.getColumnNames());
        log.info("主键：" + tableMetadata.getPkColumnNames());
        log.info("索引列：" + tableMetadata.getIndexedColumns());


        for (TableColumn column : getColumns(clazz)) {
            if (!tableMetadata.getColumnNames().contains(column.getColNameForRS().toLowerCase())) {
                String sql = DialectTranslator.getAddColumnSql(clazz, column);
                log.info("添加列：" + sql);
                executeSql(clazz, new String[]{sql});
            }
        }


        TableColumn pkColumn = getPKColumn(clazz);
        if (tableMetadata.getPkColumns().size() == 0) {
            String[] sql = DialectTranslator.getAddPrimaryKeySql(clazz, pkColumn);
            log.info("添加主键：" + JSON.toJSONString(sql));
            executeSql(clazz, sql);
        }


        for (Field field : ReflectUtil.getFieldsWithAnnotation(clazz, Index.class)) {
            if (!tableMetadata.getIndexedColumns().contains(StringUtil.toLowerCase(getColumnNameForSQL(clazz, field)))) {
                String[] sql = DialectTranslator.getAddIndexSql(clazz, field);
                log.info("添加索引：" + JSON.toJSONString(sql));
                executeSql(clazz, sql);
            }
        }
    }
}


