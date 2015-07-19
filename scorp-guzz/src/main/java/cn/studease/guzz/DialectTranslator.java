package cn.studease.guzz;

import cn.studease.guzz.annotation.Index;
import cn.studease.util.ReflectUtil;
import cn.studease.util.StringUtil;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import org.guzz.dialect.Dialect;
import org.guzz.dialect.Mysql5Dialect;
import org.guzz.dialect.Oracle8iDialect;
import org.guzz.orm.rdms.Table;
import org.guzz.orm.rdms.TableColumn;
import org.guzz.util.CloseUtil;


public class DialectTranslator {
    private static final String QUOTE_MYSQL = "`";

    public static String getQuote(Class<?> clazz) {
        Dialect dialect = DdlUtil.getDialect(clazz);
        if ((dialect instanceof Mysql5Dialect))
            return "`";
        if ((dialect instanceof Oracle8iDialect)) {
            return " ";
        }
        throw new DdlException("不支持的数据库方言：" + dialect.getClass().getName());
    }


    public static String getSqlDataType(Class<?> clazz, TableColumn column) {
        Dialect dialect = DdlUtil.getDialect(clazz);

        if ((dialect instanceof Mysql5Dialect)) {
            return getSqlDataType4Mysql(clazz, column);
        }
        if ((dialect instanceof Oracle8iDialect)) {
            return getSqlDataType4Oracle(clazz, column);
        }

        throw new DdlException("不支持的数据库方言：" + dialect.getClass().getName());
    }


    public static String getTableName(Class<?> clazz) {
        Table table = DdlUtil.getTable(clazz);
        Object tableCondition = DdlUtil.getTableCondition();
        String tableName = tableCondition == null ? table.getConfigTableName() : table.getTableName(tableCondition);
        return (DdlUtil.getDialect(clazz) instanceof Oracle8iDialect) ? StringUtil.toUpperCase(tableName) : tableName;
    }


    public static String getIndexName(Class<?> clazz, String name) {
        Dialect dialect = DdlUtil.getDialect(clazz);
        if ((dialect instanceof Mysql5Dialect))
            return name;
        if ((dialect instanceof Oracle8iDialect)) {
            String indexName = getTableName(clazz) + "_" + name;
            if (indexName.length() < 27) {
                return "i_" + indexName.toLowerCase();
            }
            return "i_" + Integer.toHexString(Math.abs(indexName.hashCode()));
        }

        throw new DdlException("不支持的数据库方言：" + dialect.getClass().getName());
    }


    public static String getPrimaryKeyName(Class<?> clazz) {
        String tableName = getTableName(clazz).toLowerCase();
        Dialect dialect = DdlUtil.getDialect(clazz);
        if ((dialect instanceof Mysql5Dialect))
            return "pk_" + tableName;
        if ((dialect instanceof Oracle8iDialect)) {
            if (tableName.length() < 27) {
                return "pk_" + tableName;
            }
            return "pk_" + Integer.toHexString(Math.abs(tableName.hashCode()));
        }

        throw new DdlException("不支持的数据库方言：" + dialect.getClass().getName());
    }


    public static String getEngine(Class<?> clazz) {
        return (DdlUtil.getDialect(clazz) instanceof Oracle8iDialect) ? "" : " ENGINE=InnoDB";
    }


    public static String getCreateTableSql(Class<?> clazz) {
        String quote = getQuote(clazz);
        StringBuilder sql = new StringBuilder("create table ");
        sql.append(quote).append(getTableName(clazz)).append(quote).append(" (");
        for (TableColumn column : DdlUtil.getColumns(clazz)) {
            sql.append(quote).append(column.getColNameForSQL()).append(quote).append(' ').append(getSqlDataType(clazz, column)).append(getDefaultValue(clazz, column)).append(getNullable(column)).append(", ");
        }
        sql.append(" primary key (").append(quote).append(DdlUtil.getPKColumn(clazz).getPropName()).append(quote).append(") )");
        sql.append(getEngine(clazz));
        return sql.toString();
    }


    public static String[] getAddIndexSql(Class<?> clazz, Field field) {
        Index index = (Index) field.getAnnotation(Index.class);

        boolean unique;
        String indexName;
        if (index != null) {
            indexName = getIndexName(clazz, index.name());
            unique = index.unique();
        } else {
            if (field.getAnnotation(Id.class) != null) {
                indexName = getIndexName(clazz, "primaryKey");
                unique = true;
            } else {
                throw new RuntimeException("字段：" + clazz.getName() + "." + field.getName() + " 无法创建索引，不是@Id且缺少@Index注解。");
            }
        }
        String quote = getQuote(clazz);
        String parallel = getParallel(clazz);
        StringBuilder sql = new StringBuilder("create").append(unique ? " unique" : "").append(" index ");
        sql.append(quote).append(indexName).append(quote).append(" on ");
        sql.append(quote).append(getTableName(clazz)).append(quote);
        sql.append(" (").append(quote).append(DdlUtil.getColumnNameForSQL(clazz, field)).append(quote).append(')').append(parallel).append(getNologging(clazz));

        if (!StringUtil.hasText(StringUtil.trim(parallel))) {
            return new String[]{sql.toString()};
        }
        return new String[]{sql.toString(), "alter index " + indexName + " parallel 1"};
    }


    public static String getParallel(Class<?> clazz) {
        if ((!(DdlUtil.getDialect(clazz) instanceof Oracle8iDialect)) || (!DdlUtil.isParallel())) {
            return " ";
        }


        Connection connection = DdlUtil.getConnection(clazz);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("select value from v$osstat where stat_name='NUM_CPU_CORES'");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int cores = rs.getInt(1);
                return cores > 2 ? " parallel " + (cores - 1) : " ";
            }
        } catch (Exception e) {
        } finally {
            CloseUtil.close(pstmt);
            CloseUtil.close(connection);
        }
        return " ";
    }


    public static String getNologging(Class<?> clazz) {
        return ((DdlUtil.getDialect(clazz) instanceof Oracle8iDialect)) && (DdlUtil.isParallel()) ? " nologging" : " ";
    }


    public static String getAddColumnSql(Class<?> clazz, TableColumn column) {
        String quote = getQuote(clazz);
        StringBuilder sql = new StringBuilder("alter table ");
        sql.append(quote).append(getTableName(clazz)).append(quote).append(" add").append((DdlUtil.getDialect(clazz) instanceof Mysql5Dialect) ? " column " : " ");
        sql.append(quote).append(column.getColNameForRS()).append(quote).append(' ').append(getSqlDataType(clazz, column)).append(getDefaultValue(clazz, column)).append(getNullable(column));
        return sql.toString();
    }

    public static String[] getAddPrimaryKeySql(Class<?> clazz, TableColumn column) {
        String tableName = getTableName(clazz);
        StringBuilder sql = new StringBuilder("alter table ").append(tableName).append(" add constraint ").append(getPrimaryKeyName(clazz)).append(" primary key(").append(column.getColNameForRS()).append(")");

        if ((DdlUtil.getDialect(clazz) instanceof Oracle8iDialect)) {
            List<String> sqlList = new ArrayList();
            sqlList.add(" disable");
            for (String index : getAddIndexSql(clazz, ReflectUtil.getField(clazz, column.getPropName()))) {
                sqlList.add(index);
            }
            sqlList.add("alter table " + tableName + " enable primary key");
            return (String[]) sqlList.toArray(new String[sqlList.size()]);
        }
        return new String[]{sql.toString()};
    }


    public static String getNullable(TableColumn column) {
        switch (column.getType()) {
            case "short":
            case "int":
            case "long":
            case "double":
            case "float":
            case "boolean":
                return " not null ";
        }

        return "";
    }


    public static String getDefaultValue(Class<?> clazz, TableColumn column) {
        Object obj;

        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new DdlException("实体缺少无参的构造函数，无法实例化！" + clazz.getName());
        }

        switch (column.getType()) {
            case "java.lang.String":
            case "byte":
            case "java.lang.Byte":
            case "char":
            case "java.lang.Character":
                try {
                    Object defaultValue = clazz.getMethod("get" + StringUtil.upperCaseFirstChar(column.getPropName()), new Class[0]).invoke(obj, new Object[0]);
                    if (defaultValue == null) {
                        return "";
                    }
                    return " default '" + defaultValue + "'";
                } catch (Exception e) {
                    throw new DdlException("不支持的数据默认值");
                }

            case "short":
            case "int":
            case "long":
            case "double":
            case "float":
            case "java.lang.Short":
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Double":
            case "java.lang.Float":
            case "java.math.BigDecimal":
                try {
                    Object defaultValue = clazz.getMethod("get" + StringUtil.upperCaseFirstChar(column.getPropName()), new Class[0]).invoke(obj, new Object[0]);
                    return " default " + defaultValue;
                } catch (Exception e) {
                    throw new DdlException("不支持的数据默认值");
                }

            case "boolean":
            case "java.lang.Boolean":
                try {
                    boolean defaultValue = ((Boolean) clazz.getMethod("is" + StringUtil.upperCaseFirstChar(column.getPropName()), new Class[0]).invoke(obj, new Object[0])).booleanValue();
                    return " default " + (defaultValue ? 1 : 0);
                } catch (Exception e) {
                    throw new DdlException("不支持的数据默认值");
                }
        }

        return "";
    }


    private static String getSqlDataType4Mysql(Class<?> clazz, TableColumn column) {
        int length = 255;
        int precision = 0;
        int scale = 0;
        Column col = (Column) ReflectUtil.getField(clazz, column.getPropName()).getAnnotation(Column.class);
        if (col != null) {
            length = col.length();
            precision = col.precision();
            scale = col.scale();
        }

        String javaType = column.getType();

        switch (javaType) {
            case "java.lang.String":
                if (length < 4000) {
                    return "varchar(" + length + ")";
                }
                return "text";


            case "byte":
            case "java.lang.Byte":
                return "tinyint(4)";

            case "char":
            case "java.lang.Character":
                return "char(1)";

            case "short":
            case "java.lang.Short":
                return "smallint(6)";

            case "int":
            case "java.lang.Integer":
                return "int(" + (precision > 0 ? precision : 11) + ")";

            case "long":
            case "java.lang.Long":
                return "bigint(" + (precision > 0 ? precision : 20) + ")";

            case "boolean":
            case "java.lang.Boolean":
                return "int(1)";

            case "java.math.BigDecimal":
                return "decimal(" + (precision > 0 ? precision : 19) + "," + (scale > 0 ? scale : 2) + ")";

            case "double":
            case "java.lang.Double":
                return "double";

            case "float":
            case "java.lang.Float":
                return "float";

            case "java.util.Date":
                return "datetime";

            case "org.guzz.pojo.lob.TranClob":
                return "longtext";

            case "org.guzz.pojo.lob.TranBlob":
                return "blob";
        }


        Enumerated enu = (Enumerated) ReflectUtil.getField(clazz, column.getPropName()).getAnnotation(Enumerated.class);
        if (enu != null) {
            return "varchar(" + length + ")";
        }


        throw new DdlException("不支持的数据类型：" + javaType);
    }


    private static String getSqlDataType4Oracle(Class<?> clazz, TableColumn column) {
        int length = 255;
        int precision = 0;
        int scale = 0;
        Column col = (Column) ReflectUtil.getField(clazz, column.getPropName()).getAnnotation(Column.class);
        if (col != null) {
            length = col.length();
            precision = col.precision();
            scale = col.scale();
        }

        String javaType = column.getType();

        switch (javaType) {
            case "java.lang.String":
                if (length < 4000) {
                    return "varchar2(" + length + ")";
                }
                return "long";


            case "byte":
            case "java.lang.Byte":
                return "number(4)";

            case "char":
            case "java.lang.Character":
                return "number(1)";

            case "short":
            case "java.lang.Short":
                return "number(6)";

            case "int":
            case "java.lang.Integer":
                return "number(" + (precision > 0 ? precision : 10) + ")";

            case "long":
            case "java.lang.Long":
                return "number(" + (precision > 0 ? precision : 19) + ")";

            case "boolean":
            case "java.lang.Boolean":
                return "number(1)";

            case "double":
            case "float":
            case "java.lang.Double":
            case "java.lang.Float":
            case "java.math.BigDecimal":
                return "number(" + (precision > 0 ? precision : 19) + "," + (scale > 0 ? scale : 2) + ")";

            case "java.util.Date":
                return "date";

            case "org.guzz.pojo.lob.TranClob":
                return "clob";

            case "org.guzz.pojo.lob.TranBlob":
                return "blob";
        }


        Enumerated enu = (Enumerated) ReflectUtil.getField(clazz, column.getPropName()).getAnnotation(Enumerated.class);
        if (enu != null) {
            switch (enu.value()) {
                case STRING:
                    return "varchar2(" + length + ")";
                case ORDINAL:
                    return "number(" + (precision > 0 ? precision : 10) + ")";
            }

        }

        throw new DdlException("不支持的数据类型：" + javaType);
    }
}

