package cn.studease.guzz.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Index {
    private final String name;
    private final String column;

    Index(ResultSet rs) throws SQLException {
        this.name = rs.getString("INDEX_NAME").toLowerCase();
        this.column = rs.getString("COLUMN_NAME").toLowerCase();
    }

    public String getName() {
        return this.name;
    }

    public String getColumn() {
        return this.column;
    }

    public String toString() {
        return "IndexMetadata(" + this.name + " on (" + this.column + "))";
    }
}


