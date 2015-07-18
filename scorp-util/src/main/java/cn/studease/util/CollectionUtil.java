package cn.studease.util;

import java.util.List;

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

}
