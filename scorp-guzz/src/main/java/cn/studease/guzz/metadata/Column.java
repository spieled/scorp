package cn.studease.guzz.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;


public class Column {
    private String name;
    private String typeName;
    private int columnSize;
    private int decimalDigits;
    private String isNullable;
    private int typeCode;

    Column(ResultSet rs)
            throws SQLException {
        try {
            this.name = rs.getString("COLUMN_NAME");
        } catch (Exception e) {
        }
        try {
            this.columnSize = rs.getInt("COLUMN_SIZE");
        } catch (Exception e) {
        }
        try {
            this.decimalDigits = rs.getInt("DECIMAL_DIGITS");
        } catch (Exception e) {
        }
        try {
            this.isNullable = rs.getString("IS_NULLABLE");
        } catch (Exception e) {
        }
        try {
            this.typeCode = rs.getInt("DATA_TYPE");
        } catch (Exception e) {
        }
        try {
            this.typeName = new StringTokenizer(rs.getString("TYPE_NAME"), "() ").nextToken();
        } catch (Exception e) {
        }
    }

    public String getName() {
        return this.name;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public int getColumnSize() {
        return this.columnSize;
    }

    public int getDecimalDigits() {
        return this.decimalDigits;
    }

    public String getIsNullable() {
        return this.isNullable;
    }

    public int getTypeCode() {
        return this.typeCode;
    }

    public String toString() {
        return "ColumnMetadata(" + this.name + ")";
    }
}


