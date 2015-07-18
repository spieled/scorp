package cn.studease.util;

/**
 * Author: liushaoping
 * Date: 2015/7/18.
 */
public enum Dir
        implements BaseEnum {
    ASC("从小到大"),

    DESC("从大到小");

    private String display;

    private Dir(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
