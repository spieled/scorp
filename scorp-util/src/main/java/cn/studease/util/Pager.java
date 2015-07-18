package cn.studease.util;

import com.cloudvast.annotation.J;

@J("分页辅助类")
public abstract interface Pager {
    public abstract int getStart();

    public abstract int getLimit();

    public abstract long getTotal();

    public abstract void setTotal(long paramLong);

    public abstract void setTotal(Object paramObject);

    public abstract int getTotalPages();
}


/* Location:              C:\Users\spieled\.m2\repository\com\cloudvast\base\6.8.20\base-6.8.20.jar!\com\cloudvast\pager\Pager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */