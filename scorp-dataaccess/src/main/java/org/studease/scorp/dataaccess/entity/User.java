package org.studease.scorp.dataaccess.entity;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Author: liushaoping
 * Date: 2015/5/12.
 */
public class User implements Serializable {

    private int id;

    private String name = "";

    private String remark = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
