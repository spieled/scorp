package cn.studease.guzz;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public final class Params {
    private Map<String, Object> map = new HashMap();


    public static Params create() {
        return new Params();
    }

    public static Params create(String key, Object value) {
        return new Params().add(key, value);
    }


    public Params add(String key, Object value) {
        if ((value instanceof Enum)) {
            value = ((Enum) value).name();
        } else if ((value instanceof Date)) {
            value = new Timestamp(((Date) value).getTime());
        }
        this.map.put(key, value);
        return this;
    }

    public int size() {
        return this.map.size();
    }

    public Params clear() {
        this.map.clear();
        return this;
    }

    public Map<String, Object> getMap() {
        return this.map;
    }
}


/* Location:              C:\Users\spieled\.m2\repository\com\cloudvast\base\6.8.20\base-6.8.20.jar!\com\cloudvast\guzz\Params.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */