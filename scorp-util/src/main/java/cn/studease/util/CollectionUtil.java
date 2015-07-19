package cn.studease.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public class CollectionUtil {

    public static <T> List<T> subList(List<T> list, Pager pager) {
        if ((list.size() > 0) && (pager != null) && (pager.getLimit() != 0)) {
            pager.setTotal(list.size());
            int startIndex = pager.getStart() >= list.size() ? 0 : pager.getStart();
            int endIndex = pager.getStart() + pager.getLimit() >= list.size() ? list.size() : pager.getStart() + pager.getLimit();
            if (startIndex < endIndex) {
                return list.subList(startIndex, endIndex);
            }
            return list.subList(endIndex, startIndex);
        }

        return list;
    }

    public static <K, V> V getFromMap(Map<K, V> map, K key, V def) {
        if (!map.containsKey(key)) {
            map.put(key, def);
        }
        return (V) map.get(key);
    }

    public static <K, V> V getFromMap(Map<K, V> map, K key, Callback<V> callback) {
        if (!map.containsKey(key)) {
            map.put(key, callback.run());
        }
        return (V) map.get(key);
    }


    public static <T> Map<String, Object> sumMap(Collection<T> list, String[] sumFields) {
        Map<String, Object> sumMap = new HashMap();
        if ((list == null) || (list.size() == 0) || (sumFields == null) || (sumFields.length == 0)) {
            return sumMap;
        }
        boolean isMap = false;
        for (Class inf : list.iterator().next().getClass().getInterfaces()) {
            if (inf == Map.class) {
                isMap = true;
                break;
            }
        }
        if (!isMap) {
            return sumMap;
        }
        for (T t : list) {
            Map map = (Map) t;
            for (String field : sumFields) {
                sumMap.put(field, NumericUtil.add(new Object[]{map.get(field), sumMap.get(field)}));
            }
        }
        return sumMap;
    }


}
