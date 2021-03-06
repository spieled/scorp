package cn.studease.scorp.dataaccess.dao;

import cn.studease.scorp.dataaccess.entity.User;

/**
 * Author: liushaoping
 * Date: 2015/5/12.
 */
public interface UserDao {

    void save(User user);

    User get(int id);

    void delete(int id);

}
