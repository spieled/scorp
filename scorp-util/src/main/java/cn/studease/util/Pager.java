package cn.studease.util;


public abstract interface Pager {
    public abstract int getStart();

    public abstract int getLimit();

    public abstract long getTotal();

    public abstract void setTotal(long paramLong);

    public abstract void setTotal(Object paramObject);

    public abstract int getTotalPages();
}


