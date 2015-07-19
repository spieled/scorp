package cn.studease.guzz;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.guzz.dialect.Mysql5Dialect;
import org.guzz.dialect.Oracle8iDialect;
import org.guzz.lang.NullValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: liushaoping
 * Date: 2015/7/19.
 */
public class GuzzUtil {

    private static final Logger log = LoggerFactory.getLogger(GuzzUtil.class);
    private static final Map<String, Class<?>> businessMap = new HashMap();
    private static Properties dbConfig = null;

    public static Properties getDbConfig() {
        if (dbConfig == null) {
            try {
                dbConfig = new Properties();
                dbConfig.load(new org.springframework.core.io.ClassPathResource("guzz.properties").getInputStream());
            } catch (Exception e) {
                log.error("获取数据库配置信息失败", e);
                dbConfig = new Properties();
            }
        }
        return dbConfig;
    }


    public static boolean isOracle(Class clazz) {
        validateEntityDatabaseConfiguration(clazz, true);
        return DdlUtil.getTable(clazz).getDialect() instanceof Oracle8iDialect;
    }


    public static boolean isOracle(Class clazz, Object tableCondition) {
        validateEntityDatabaseConfiguration(clazz, false);
        DdlUtil.setTableCondition(tableCondition);
        return DdlUtil.getTable(clazz).getDialect() instanceof Oracle8iDialect;
    }


    public static boolean isMySQL(Class clazz) {
        validateEntityDatabaseConfiguration(clazz, true);
        return DdlUtil.getTable(clazz).getDialect() instanceof Mysql5Dialect;
    }


    public static boolean isMySQL(Class clazz, Object tableCondition) {
        validateEntityDatabaseConfiguration(clazz, false);
        DdlUtil.setTableCondition(tableCondition);
        return DdlUtil.getTable(clazz).getDialect() instanceof Mysql5Dialect;
    }


    public static boolean isShadowed(Class clazz) {
        org.guzz.annotations.Table table = (org.guzz.annotations.Table) clazz.getAnnotation(org.guzz.annotations.Table.class);
        if (table == null) {
            throw new RuntimeException(clazz.getName() + "不是有效的实体，缺少@Table注解。");
        }
        return table.shadow() != NullValue.class;
    }

    private static void validateEntityDatabaseConfiguration(Class clazz, boolean checkShadow) {
        org.guzz.annotations.Table table = (org.guzz.annotations.Table) clazz.getAnnotation(org.guzz.annotations.Table.class);
        if (table == null) {
            throw new RuntimeException(clazz.getName() + "不是有效的实体，缺少@Table注解。");
        }
        if ((checkShadow) && (table.shadow() != NullValue.class)) {
            throw new RuntimeException(clazz.getName() + "设置了分表，请指定分表条件。");
        }
    }

}
