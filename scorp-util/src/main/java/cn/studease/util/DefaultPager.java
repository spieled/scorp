package cn.studease.util;

import java.io.Serializable;

public class DefaultPager
        implements Pager, Serializable {
    private int start;
    private int limit = 1;

    private long total = -1L;

    public DefaultPager() {
    }

    public DefaultPager(int start, int limit, long total) {
        this.start = start;
        this.limit = limit;
        this.total = total;
    }

    public static DefaultPager getInstance() {
        return new DefaultPager(0, 20, -1);
    }

    public static Pager instance(Pager pager) {
        return new DefaultPager(pager.getStart(), pager.getLimit(), pager.getTotal());
    }

    public int getStart() {
        return this.start;
    }

    public int getLimit() {
        return this.limit;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(Object total) {
        try {
            this.total = ((Long) total).longValue();
        } catch (Exception e) {
            this.total = 0L;
        }
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPages() {
        try {
            return (int) ((this.total - 1L) / this.limit) + 1;
        } catch (Exception ignored) {
        }
        return 1;
    }

    public String toString() {
        return "pager_" + this.start + "_" + this.limit + "_" + this.total;
    }
}


/* Location:              C:\Users\spieled\.m2\repository\com\cloudvast\base\6.8.20\base-6.8.20.jar!\com\cloudvast\pager\DefaultPager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */